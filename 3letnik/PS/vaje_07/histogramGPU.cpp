#include "FreeImage.h"
#include <vector>
#include <CL/cl.hpp>
#include <string>
#include <iostream>
#include <fstream>
#include <sstream>
#include <chrono>

#define WG_SIZE 32

std::string getShaderSource(std::string path);

int main(void)
{
  std::vector<cl::Platform> all_platforms;  // create vector for storing platforms
  cl::Platform::get(&all_platforms);        // get all platforms and store them
  cl::Platform platform = all_platforms[0]; // take first platform as our desired platform

  // getting devices
  std::vector<cl::Device> all_devices;                   // create devices vec
  platform.getDevices(CL_DEVICE_TYPE_GPU, &all_devices); // get all devices from selected platform
  cl::Device device = all_devices[0];                    // get first gpu device
  cl::Context context(device);                           // get clContext from gpu

  // IMAGE LOADING PART
  //Load image from file
	FIBITMAP *imageBitmap = FreeImage_Load(FIF_BMP, "test.bmp", 0);
	//Convert it to a 32-bit image
  FIBITMAP *imageBitmap32 = FreeImage_ConvertTo32Bits(imageBitmap);
	
    //Get image dimensions
  int width = FreeImage_GetWidth(imageBitmap32);
	int height = FreeImage_GetHeight(imageBitmap32);
	int pitch = FreeImage_GetPitch(imageBitmap32);
  unsigned char *imageIn = (unsigned char *)malloc(height*pitch * sizeof(unsigned char));
  //Extract raw data from the image
	FreeImage_ConvertToRawBits(imageIn, imageBitmap, pitch, 32, FI_RGBA_RED_MASK, FI_RGBA_GREEN_MASK, FI_RGBA_BLUE_MASK, TRUE);
  //Free source image data
	FreeImage_Unload(imageBitmap32);
	FreeImage_Unload(imageBitmap);
  // END IMAGE LAODING

  // creating program
  std::string kernel_code = getShaderSource("shader.cl");                       // read shader from file
  cl::Program::Sources sources(1, {kernel_code.c_str(), kernel_code.length()}); // add kernel code to a program

  cl::Program program(context, sources);     // init program on context
  if (program.build({device}) != CL_SUCCESS) // try to build shader
  {
    std::cout << " Error building: " << program.getBuildInfo<CL_PROGRAM_BUILD_LOG>(device) << std::endl;
    exit(1);
  }

  cl::CommandQueue queue(context, device);             // create command queue
  cl::Kernel kernel_hist(program, "hist"); // create a program (program, program_name) where program_name is set in a shader file
 
  // // pass data to the GPU
  // // Create a buffer for the result
  cl::Buffer hist(context, CL_MEM_COPY_HOST_PTR | CL_MEM_WRITE_ONLY, 256);
  unsigned char *data = new unsigned char[256];
  kernel_hist.setArg(0, hist);               // set arguments for output

  // // Create a buffer for the image
  int imsize = width*height*4*sizeof(unsigned char);
  cl::Buffer im(context, CL_MEM_READ_ONLY, imsize);
  kernel_hist.setArg(1, im);               // set arguments for output
  queue.enqueueWriteBuffer(im,CL_TRUE,0,imsize,imageIn);  

  // start timing
  auto timeStart = std::chrono::high_resolution_clock::now();
  queue.enqueueNDRangeKernel(kernel_hist, cl::NullRange, cl::NDRange(width, height), cl::NDRange(WG_SIZE)); // too long..read this (kernel, ignore/idk, global size, local size)
  queue.finish();                                                                                          // wait for the thing to finish
  queue.enqueueReadBuffer(hist, CL_TRUE, 0, 255, data);
  auto stop = std::chrono::high_resolution_clock::now();
  // end timing
  auto timeStop = std::chrono::duration_cast<std::chrono::microseconds>(stop - timeStart);
  std::cout << "duration micro: " << timeStop.count() << std::endl;

  return 0;
}

// basically just read from file
std::string getShaderSource(std::string path)
{
  std::ifstream file(path);
  if (!file.is_open())
    exit(1);

  std::stringstream ss;
  ss << file.rdbuf();
  return ss.str();
}