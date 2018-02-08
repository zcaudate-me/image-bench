#include <iostream>
#include <iomanip>
#include <fstream>
#include <string>

#include "opencv2/core/ocl.hpp"
#include "kernel.hpp"

cv::ocl::Kernel compile_kernal(std::string filename, std::string module, std::string package, char * kernel){
  std::ifstream opencl_stream(filename);
  std::stringstream buffer;
  buffer << opencl_stream.rdbuf();
  std::string opencl_kernel_src = buffer.str();
  cv::ocl::ProgramSource source(module, package, opencl_kernel_src, "");
  cv::String errmsg;
  cv::ocl::Program program(source, "", errmsg);
  
  if (program.ptr() == NULL)
  {
    std::cerr << "Can't compile OpenCL program:" << std::endl << errmsg << std::endl;
    throw "ERROR";
  }
  //! [Compile/build OpenCL for current OpenCL device]
  
  if (!errmsg.empty())
  {
    std::cout << "OpenCL program build log:" << std::endl << errmsg << std::endl;
  }
  //! [Get OpenCL kernel by name]
  cv::ocl::Kernel k(kernel, program);
  if (k.empty())
  {
    std::cerr << "Can't get kernel: " << filename << std::endl;
    throw "ERROR";
  }
  
  return k;
}