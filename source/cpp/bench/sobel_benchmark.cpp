//
//  sobel_bench.cpp
//  Test.OpenCL
//
//  Created by Chris Zheng on 19/1/18.
//

#include <iostream>
#include <iomanip>
#include <fstream>
#include <string>

#include "opencv2/core/ocl.hpp"
#include "opencv2/highgui.hpp"
#include "opencv2/imgcodecs.hpp"
#include "opencv2/imgproc.hpp"

#include "../kanya.hpp"
#include "../image/kernel.hpp"
#include "../image/grayscale.hpp"
#include "../image/sobel.hpp"
#include "../image/util.hpp"

int main(int argc, const char * argv[]) {
  
  std::cout << "SOBEL BENCHMARK\n";
  
  DECLARE_TIMING;
  cv::ocl::Kernel sobel = compile_kernal("source/opencl/sobel.cl", "sobel", "transform", "sobel_uchar");
  
  size_t localSizes[4][2] = {{1, 1}, {1, 8}, {8, 8}, {32, 8}};
  
  cv::Mat  src_0250 = cv::imread("resources/lena-0250x0250.jpg", cv::IMREAD_GRAYSCALE);
  cv::Mat  dst_0250 = cv::Mat(src_0250.size(), CV_8UC1);
  
  cv::Mat  src_0500 = cv::imread("resources/lena-0500x0500.jpg", cv::IMREAD_GRAYSCALE);
  cv::Mat  dst_0500 = cv::Mat(src_0500.size(), CV_8UC1);

  cv::Mat  src_1000 = cv::imread("resources/lena-1000x1000.jpg", cv::IMREAD_GRAYSCALE);
  cv::Mat  dst_1000 = cv::Mat(src_1000.size(), CV_8UC1);

  cv::Mat  src_2000 = cv::imread("resources/lena-2000x2000.jpg", cv::IMREAD_GRAYSCALE);
  cv::Mat  dst_2000 = cv::Mat(src_2000.size(), CV_8UC1);

  cv::Mat  src_4000 = cv::imread("resources/lena-4000x4000.jpg", cv::IMREAD_GRAYSCALE);
  cv::Mat  dst_4000 = cv::Mat(src_4000.size(), CV_8UC1);
  
  cv::Mat  src_8000 = cv::imread("resources/lena-8000x8000.jpg", cv::IMREAD_GRAYSCALE);
  cv::Mat  dst_8000 = cv::Mat(src_8000.size(), CV_8UC1);

  //Warm Up
  sobel_mat(sobel, src_0250, dst_0250, localSizes[0]);
    
  for (int i = 0; i < 4; i++){
    std::cout << "--------------------------------------" << std::endl;
    std::cout << "SOBEL TRANSFORM (" << localSizes[i][0] << ", " << localSizes[i][1] << ")" << std::endl;
    std::cout << "--------------------------------------" << std::endl;
    TIME("0250x0250:          ", sobel_mat(sobel, src_0250, dst_0250, localSizes[i]), print_time);
    TIME("0500x0500:          ", sobel_mat(sobel, src_0500, dst_0500, localSizes[i]), print_time);
    TIME("1000x1000:          ", sobel_mat(sobel, src_1000, dst_1000, localSizes[i]), print_time);
    
    cv::imshow("sobel output", dst_0500);
    cv::waitKey(0);
    
    TIME("2000x2000:          ", sobel_mat(sobel, src_2000, dst_2000, localSizes[i]), print_time);
    //TIME("4000x4000:          ", sobel_mat(sobel, src_4000, dst_4000, localSizes[i]), print_time);
    //TIME("8000x8000:          ", sobel_mat(sobel, src_8000, dst_8000, localSizes[i]), print_time);
    std::cout << "--------------------------------------" << std::endl;
    
  }
  return 0;
}
