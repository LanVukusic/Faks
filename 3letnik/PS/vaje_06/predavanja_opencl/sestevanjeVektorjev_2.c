#include <stdio.h>
#include <stdlib.h>
#include <CL/cl.h>

#define SIZE			(1024)
#define WORKGROUP_SIZE	(512)

const char *source_str = 
	"__kernel void vector_add(__global const int *A,		\n"
	"						  __global const int *B,		\n"
	"						  __global int *C,				\n"
	"						  int size)						\n"
	"{														\n"
	"	// globalni indeks elementa							\n"
	"	int i = get_global_id(0);							\n"
	"	// izracun											\n"
	"	while( i < size )									\n"
	"	{													\n"
	"		C[i] = A[i] + B[i];								\n"
	"		i += get_global_size(0);						\n"
	"	}													\n"
	"}														\n";

int main(void) 
{
	char ch;
    int i;
	cl_int ret;

	int vectorSize = SIZE;
	
	// Rezervacija pomnilnika
	int *A = (int*)malloc(vectorSize*sizeof(int));
    int *B = (int*)malloc(vectorSize*sizeof(int));
    int *C = (int*)malloc(vectorSize*sizeof(int));

    // Inicializacija vektorjev
	for(i = 0; i < vectorSize; i++) 
	{
        A[i] = i;
        B[i] = vectorSize - i;
    }
 
    // Podatki o platformi
    cl_platform_id	platform_id[10];
    cl_uint			ret_num_platforms;
	char			*buf;
	size_t			buf_len;
	ret = clGetPlatformIDs(10, platform_id, &ret_num_platforms);
			// max. "stevilo platform, kazalec na platforme, dejansko "stevilo platform
	
	// Podatki o napravi
	cl_device_id	device_id[10];
	cl_uint			ret_num_devices;
	// Delali bomo s platform_id[0] na GPU
	ret = clGetDeviceIDs(platform_id[0], CL_DEVICE_TYPE_GPU, 10,	
						 device_id, &ret_num_devices);				
			// izbrana platforma, tip naprave, koliko naprav nas zanima
			// kazalec na naprave, dejansko "stevilo naprav

    // Kontekst
    cl_context context = clCreateContext(NULL, 1, &device_id[0], NULL, NULL, &ret);
			// kontekst: vklju"cene platforme - NULL je privzeta, "stevilo naprav, 
			// kazalci na naprave, kazalec na call-back funkcijo v primeru napake
			// dodatni parametri funkcije, "stevilka napake
 
    // Ukazna vrsta
    cl_command_queue command_queue = clCreateCommandQueue(context, device_id[0], 0, &ret);
			// kontekst, naprava, INORDER/OUTOFORDER, napake

	// Delitev dela
    size_t local_item_size = WORKGROUP_SIZE;
	size_t num_groups = ((vectorSize-1)/local_item_size+1);		
    size_t global_item_size = num_groups*local_item_size;		

    // Alokacija pomnilnika na napravi
    cl_mem a_mem_obj = clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
									  vectorSize*sizeof(int), A, &ret);
			// kontekst, na"cin, koliko, lokacija na hostu, napaka	
    cl_mem b_mem_obj = clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
									  vectorSize*sizeof(int), B, &ret);
    cl_mem c_mem_obj = clCreateBuffer(context, CL_MEM_WRITE_ONLY, 
									  vectorSize*sizeof(int), NULL, &ret);
  
    // Priprava programa
    cl_program program = clCreateProgramWithSource(context,	1, (const char **)&source_str,  
												   NULL, &ret);
			// kontekst, "stevilo kazalcev na kodo, kazalci na kodo,		
			// stringi so NULL terminated, napaka													
 
    // Prevajanje
    ret = clBuildProgram(program, 1, &device_id[0], NULL, NULL, NULL);
			// program, "stevilo naprav, lista naprav, opcije pri prevajanju,
			// kazalec na funkcijo, uporabni"ski argumenti

	// Log
	size_t build_log_len;
	char *build_log;
	ret = clGetProgramBuildInfo(program, device_id[0], CL_PROGRAM_BUILD_LOG, 
								0, NULL, &build_log_len);
			// program, "naprava, tip izpisa, 
			// maksimalna dol"zina niza, kazalec na niz, dejanska dol"zina niza
	build_log =(char *)malloc(sizeof(char)*(build_log_len+1));
	ret = clGetProgramBuildInfo(program, device_id[0], CL_PROGRAM_BUILD_LOG, 
							    build_log_len, build_log, NULL);
	printf("%s\n", build_log);
	free(build_log);

	scanf("%c", &ch);
 
    // "s"cepec: priprava objekta
    cl_kernel kernel = clCreateKernel(program, "vector_add", &ret);
			// program, ime "s"cepca, napaka
 
    // "s"cepec: argumenti
    ret = clSetKernelArg(kernel, 0, sizeof(cl_mem), (void *)&a_mem_obj);
    ret |= clSetKernelArg(kernel, 1, sizeof(cl_mem), (void *)&b_mem_obj);
    ret |= clSetKernelArg(kernel, 2, sizeof(cl_mem), (void *)&c_mem_obj);
    ret |= clSetKernelArg(kernel, 3, sizeof(cl_int), (void *)&vectorSize);
			// "s"cepec, "stevilka argumenta, velikost podatkov, kazalec na podatke

	// "s"cepec: zagon
    ret = clEnqueueNDRangeKernel(command_queue, kernel, 1, NULL,						
								 &global_item_size, &local_item_size, 0, NULL, NULL);	
			// vrsta, "s"cepec, dimenzionalnost, mora biti NULL, 
			// kazalec na "stevilo vseh niti, kazalec na lokalno "stevilo niti, 
			// dogodki, ki se morajo zgoditi pred klicem
																						
    // Kopiranje rezultatov
    ret = clEnqueueReadBuffer(command_queue, c_mem_obj, CL_TRUE, 0,						
							  vectorSize*sizeof(int), C, 0, NULL, NULL);				
			// branje v pomnilnik iz naparave, 0 = offset
			// zadnji trije - dogodki, ki se morajo zgoditi prej

    // Prikaz rezultatov
    for(i = 0; i < vectorSize; i++)
        printf("%d + %d = %d\n", A[i], B[i], C[i]);
 
    // "ci"s"cenje
    ret = clFlush(command_queue);
    ret = clFinish(command_queue);
    ret = clReleaseKernel(kernel);
    ret = clReleaseProgram(program);
    ret = clReleaseMemObject(a_mem_obj);
    ret = clReleaseMemObject(b_mem_obj);
    ret = clReleaseMemObject(c_mem_obj);
    ret = clReleaseCommandQueue(command_queue);
    ret = clReleaseContext(context);
	
    free(A);
    free(B);
    free(C);

    return 0;
}
