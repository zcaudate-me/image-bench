#include <iostream>
#include <fstream>
#include <algorithm>
#include <string>

#include "opencv2/core/ocl.hpp"
#include "opencv2/highgui.hpp"
#include "opencv2/imgcodecs.hpp"

#include "../kanya.hpp"
#include "../image/util.hpp"

int main(){

  std::cout << "IMAGE GENERATION\n";
  
  cv::Mat img;
  std::string img_string;
  
  img = cv::imread("resources/eye-0016x0016.jpg");
  img_string = color_to_hex(img);
  ya::spit("resources/eye-0016x0016.txt", img_string);

  img = cv::imread("resources/eye-0032x0032.jpg");
  img_string = color_to_hex(img);
  ya::spit("resources/eye-0032x0032.txt", img_string);
  
  img = cv::imread("resources/rainbow-0032x0032.jpg");
  img_string = color_to_hex(img);
  ya::spit("resources/rainbow-0032x0032.txt", img_string);
  
  img = cv::imread("resources/lena-0250x0250.jpg");
  img_string = color_to_hex(img);
  ya::spit("resources/lena-0250x0250.txt", img_string);

  img = cv::imread("resources/lena-0500x0500.jpg");
  img_string = color_to_hex(img);
  ya::spit("resources/lena-0500x0500.txt", img_string);

  img = cv::imread("resources/lena-1000x1000.jpg");
  img_string = color_to_hex(img);
  ya::spit("resources/lena-1000x1000.txt", img_string);
  
  img = cv::imread("resources/lena-2000x2000.jpg");
  img_string = color_to_hex(img);
  ya::spit("resources/lena-2000x2000.txt", img_string);
  
  img = cv::imread("resources/lena-4000x4000.jpg");
  img_string = color_to_hex(img);
  ya::spit("resources/lena-4000x4000.txt", img_string);
  
  img = cv::imread("resources/lena-8000x8000.jpg");
  img_string = color_to_hex(img);
  ya::spit("resources/lena-8000x8000.txt", img_string);
  
  return 0;
  
}


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
