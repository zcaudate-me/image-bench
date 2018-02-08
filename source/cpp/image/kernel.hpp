#include <string>
#include "opencv2/core/ocl.hpp"

cv::ocl::Kernel compile_kernal(std::string filename, std::string module, std::string package, char * kernel);