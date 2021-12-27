#include "FreeImage.h"
#include <vector>
#include <CL/cl.hpp>
#include <string>
#include <iostream>
#include <fstream>
#include <sstream>
#include <chrono>

std::string getShaderSource(std::string path);

int main(void)
{
  int height = 3840;
  int width = 2160;
  int pitch = ((32 * width + 31) / 32) * 4;

  // rezerviramo prostor za sliko (RGBA)
  int imsize = height * width * sizeof(unsigned char) * 4;
  unsigned char *image = (unsigned char *)malloc(imsize);
  std::cout << "im buff" << std::endl;

  std::vector<cl::Platform> all_platforms;  // create vector for storing platforms
  cl::Platform::get(&all_platforms);        // get all platforms and store them
  cl::Platform platform = all_platforms[0]; // take first platform as our desired platform

  // getting devices
  std::vector<cl::Device> all_devices;                   // create devices vec
  platform.getDevices(CL_DEVICE_TYPE_GPU, &all_devices); // get all devices from selected platform
  cl::Device device = all_devices[0];                    // get first gpu device
  cl::Context context(device);                           // get clContext from gpu

  // creating program
  std::string kernel_code = getShaderSource("shader.cl");                       // read shader from file
  cl::Program::Sources sources(1, {kernel_code.c_str(), kernel_code.length()}); // add kernel code to a program

  cl::Program program(context, sources);     // init program on context
  if (program.build({device}) != CL_SUCCESS) // try to build shader
  {
    std::cout << " Error building: " << program.getBuildInfo<CL_PROGRAM_BUILD_LOG>(device) << std::endl;
    exit(1);
  }

  // pass data to the GPU
  // Create a buffer for the result
  cl::Buffer clResult(context, CL_MEM_WRITE_ONLY, imsize);
  unsigned char *data = new unsigned char[imsize];
  std::cout << "data" << std::endl;

  cl::CommandQueue queue(context, device);             // create command queue
  cl::Kernel kernel_mandelbrot(program, "mandelbrot"); // create a program (program, program_name) where program_name is set in a shader file
  kernel_mandelbrot.setArg(0, clResult);               // set arguments for output
  // start timing
  auto timeStart = std::chrono::high_resolution_clock::now();
  queue.enqueueNDRangeKernel(kernel_mandelbrot, cl::NullRange, cl::NDRange(width, height), cl::NullRange); // too long..read this (kernel, ignore/idk, global size, local size)
  queue.finish();                                                                                          // wait for the thing to finish

  queue.enqueueReadBuffer(clResult, CL_TRUE, 0, imsize, data);
  auto stop = std::chrono::high_resolution_clock::now();
  // end timing
  auto timeStop = std::chrono::duration_cast<std::chrono::microseconds>(stop - timeStart);
  std::cout << "duration micro: " << timeStop.count() << std::endl;

  // shranimo sliko
  FIBITMAP *dst = FreeImage_ConvertFromRawBits(data, width, height, pitch,
                                               32, FI_RGBA_RED_MASK, FI_RGBA_GREEN_MASK, FI_RGBA_BLUE_MASK, TRUE);
  FreeImage_Save(FIF_PNG, dst, "mandelbrot.png", 0);
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