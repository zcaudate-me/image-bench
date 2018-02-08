#include <iostream>
#include <string>

#include "opencv2/core/ocl.hpp"
#include "sobel.hpp"

void sobel_mat(cv::ocl::Kernel kernal,
               cv::Mat src,
               cv::Mat dst,
               size_t * localSize){
  size_t globalSize[2] = {(size_t)src.cols, (size_t)src.rows};
  bool executionResult = kernal
  .args(cv::ocl::KernelArg::ReadOnly(src.getUMat(cv::ACCESS_READ)), // size is not used (similar to 'dst' size)
        cv::ocl::KernelArg::WriteOnlyNoSize(dst.getUMat(cv::ACCESS_WRITE)))
  .run(2, globalSize, localSize, true);
  if (!executionResult){
    std::cerr << "OpenCL kernel launch failed" << std::endl;
    return;
  }
}