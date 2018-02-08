#include "opencv2/core/ocl.hpp"

void sobel_mat(cv::ocl::Kernel kernal,
               cv::Mat src,
               cv::Mat dst,
               size_t * localSize);