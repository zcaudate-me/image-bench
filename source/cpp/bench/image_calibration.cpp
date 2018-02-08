#include <iostream>
#include <fstream>
#include <algorithm>
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

int main(){

  std::cout << "IMAGE CALIBRATION\n";

  cv::ocl::Kernel grayscale = compile_kernal("source/opencl/grayscale.cl", "grayscale", "transform", "grayscale_uchar");
  cv::ocl::Kernel sobel = compile_kernal("source/opencl/sobel.cl", "sobel", "transform", "sobel_uchar");  
  
  int size = 500;
  std::string filename = "resources/lena-0500x0500.txt";
    
  std::string img_string = ya::slurp(filename);
  std::string byte_data =  ya::hex_to_bytes(remove_whitespace(img_string));
  cv::Mat img(size, size, CV_8UC3, (void*)byte_data.data());
  cv::Mat img_gray(size, size, CV_8UC1);
  cv::Mat img_sobel(size, size, CV_8UC1);
  
  size_t local[2] = {1, 1};
  grayscale_mat(grayscale, img, img_gray, local);
  sobel_mat(sobel, img_gray, img_sobel, local);
  
  cv::imshow("color", img);
  cv::imshow("grayscale",  img_gray);
  cv::imshow("sobel", img_sobel);
  cv::waitKey(0);
  
  return 0;
}

/*
int main(){

  cv::ocl::Kernel grayscale = compile_kernal("source/opencl/grayscale.cl", "grayscale", "transform", "grayscale_uchar");
  cv::ocl::Kernel sobel = compile_kernal("source/opencl/sobel.cl", "sobel", "transform", "sobel_uchar");  
  
  int size = 32;
  std::string filename = "resources/eye-0032x0032.txt";
    
  std::string img_string = ya::slurp(filename);
  std::string  byte_data =  ya::hex_to_bytes(remove_whitespace(img_string));
  cv::Mat img(size, size, CV_8UC3, (void*)byte_data.data());
  cv::Mat img_gray(size, size, CV_8UC1);
  cv::Mat img_sobel(size, size, CV_8UC1);
  
  size_t local[2] = {1, 1};
  grayscale_mat(grayscale, img, img_gray, local);
  sobel_mat(sobel, img_gray, img_sobel, local);
  
  std::cout << "\nCOLOR:\n" << color_to_hex(img);  
  std::cout << "\nGRAYSCALE:\n" << grayscale_to_hex(img_gray);  
  std::cout << "\nSOBEL:\n" << grayscale_to_hex(img_sobel);
  
  return 0;
}
*/


/*
for (int y = 0; y < 1000; y++) {
    for (int x = 0; x < 1000; x++) {
        int i = (y * 1000 + x);
        img.at<cv::Vec3b>(i)[0] = byte_data[i];
        img.at<cv::Vec3b>(i)[1] = byte_data[i+1];
        img.at<cv::Vec3b>(i)[2] = byte_data[i+2];
    }
}*/

  /*
  cv::Mat img = cv::imread("resources/lena-1000x1000.jpg");
  std::string img_string = color_to_hex(img);
  ya::spit("resources/lena-1000x1000.txt", img_string);
  return 0;
  */

  /*
  std::string partial_string = remove_whitespace("F1FCFF F9FFFF FFFDF7 FFFFF7 FFFFF7 FBFFFA");
  std::string byte_string = ya::hex_to_bytes(partial_string);  
  std::cout << "DATA:\n";
  */

  /*
  std::string img_string = ya::slurp("resources/rainbow-0016x0016.txt");
  std::string byte_data =  ya::hex_to_bytes(remove_whitespace(img_string));
  cv::Mat img(16, 16, CV_8UC3, (void*)byte_data.data());

  cv::imshow("rainbow", img);
  cv::waitKey(0);
  return 0;
  */
