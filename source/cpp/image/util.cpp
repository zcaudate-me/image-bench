#include <iostream>
#include <fstream>
#include <algorithm>
#include <string>

#include "opencv2/core/ocl.hpp"
#include "../kanya.hpp"

void print_time(int64 t){
  printf("%.3f ms\n", t/cv::getTickFrequency()*1000);
}

std::string color_to_hex(cv::Mat img){
  std::stringstream buffer;
  for(int y = 0; y < img.rows; y++){
    for(int x = 0; x < img.cols; x++){
      int i = y*img.cols + x;
      cv::Vec3b pixel = img.at<cv::Vec3b>(i);
      /*
      buffer << (int)pixel[0] << " "
             << (int)pixel[1] << " "
             << (int)pixel[2] << "|";
      */
      buffer << ya::byte_to_hex(pixel[0])
             << ya::byte_to_hex(pixel[1])
             << ya::byte_to_hex(pixel[2]) << " ";
    }
    buffer << "\n";
  }
  return buffer.str();
}

std::string grayscale_to_hex(cv::Mat img){
  std::stringstream buffer;
  for(int y = 0; y < img.rows; y++){
    for(int x = 0; x < img.cols; x++){
      int i = y*img.cols + x;
      buffer << ya::byte_to_hex(img.at<char>(i)) << " ";
    }
    buffer << "\n";
  }
  return buffer.str();
}

void print_bytes(std::string s, int group = 10000000, int width = 10000000){
  for(int i = 0; i < s.length(); i++){
    if (i%width == 0){ std::cout << "\n"; }
    if (i%group == 0){ std::cout << " "; }
    std::cout << ya::byte_to_hex(s.data()[i]);
  }  
}

std::string remove_whitespace(std::string s){
  std::string buff = s;
  buff.erase(std::remove(buff.begin(), buff.end(), ' '), buff.end());
  buff.erase(std::remove(buff.begin(), buff.end(), '\n'), buff.end());
  buff.erase(std::remove(buff.begin(), buff.end(), '\t'), buff.end());
  return buff;
}

cv::Mat hex_to_color(std::string s, int rows, int cols){
  std::string arr = ya::hex_to_bytes(remove_whitespace(s));
  cv::Mat img(rows, cols, CV_8UC3, (void*)arr.data());
  return img;
}

cv::Mat hex_to_grayscale(std::string s, int rows, int cols){
  std::string arr = ya::hex_to_bytes(remove_whitespace(s));
  cv::Mat img(rows, cols, CV_8UC1, (void*)arr.data());
  return img;  
}