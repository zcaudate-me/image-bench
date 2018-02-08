cmake_minimum_required(VERSION 2.8)
project( image_bench )
find_package( OpenCV REQUIRED )
include_directories( ${OpenCV_INCLUDE_DIRS})
set(CMAKE_RUNTIME_OUTPUT_DIRECTORY ${CMAKE_BINARY_DIR}/../bin)

add_executable(image_calibration source/cpp/bench/image_calibration.cpp 
                                 source/cpp/kanya.cpp
                                 source/cpp/image/kernel.cpp
                                 source/cpp/image/sobel.cpp
                                 source/cpp/image/grayscale.cpp
                                 source/cpp/image/util.cpp)
target_link_libraries(image_calibration ${OpenCV_LIBS})


add_executable(image_generation  source/cpp/bench/image_generation.cpp 
                                 source/cpp/kanya.cpp
                                 source/cpp/image/kernel.cpp
                                 source/cpp/image/sobel.cpp
                                 source/cpp/image/grayscale.cpp
                                 source/cpp/image/util.cpp)
target_link_libraries(image_generation ${OpenCV_LIBS})


add_executable(sobel_benchmark   source/cpp/bench/sobel_benchmark.cpp 
                                 source/cpp/kanya.cpp
                                 source/cpp/image/kernel.cpp
                                 source/cpp/image/sobel.cpp
                                 source/cpp/image/grayscale.cpp
                                 source/cpp/image/util.cpp)
target_link_libraries(sobel_benchmark ${OpenCV_LIBS})


# add_executable(hex_string test/cpp/hex_string/v1.cpp)
# target_link_libraries(hex_string ${OpenCV_LIBS})

# add_executable(sobel_bench_opencv source/cpp/sobel_bench_opencv.cpp)
# target_link_libraries(sobel_bench_opencv ${OpenCV_LIBS})