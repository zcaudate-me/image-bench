#include <string>
#include "opencv2/core/ocl.hpp"

#define DECLARE_TIMING  int64 timeStart; double timeEnd;

#define TIME(label, block, output) \
timeStart = cv::getTickCount(); \
printf(label); \
block; \
timeEnd = cv::getTickCount(); \
output(timeEnd-timeStart);

void print_time(int64 t);

std::string color_to_hex(cv::Mat img);

std::string grayscale_to_hex(cv::Mat img);

void print_bytes(std::string s, int group = 10000000, int width = 10000000);

std::string remove_whitespace(std::string s);

cv::Mat hex_to_color(std::string s, int rows, int cols);

cv::Mat hex_to_grayscale(std::string s, int rows, int cols);