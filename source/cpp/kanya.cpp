#include <cstdint>
#include <fstream>
#include <iostream>
#include <string>
#include <sstream>
#include <vector>

#include "kanya.hpp"

namespace ya{
  /*
  std::string slurp(std::string filename){
    std::ifstream input(filename);
    std::stringstream buffer;
    buffer << input.rdbuf();
    return buffer.str();
  }*/
  std::string slurp(std::string filename){
    std::ifstream ifs(filename);
    std::string content((std::istreambuf_iterator<char>(ifs)),
                        (std::istreambuf_iterator<char>()));
    return content;
  }
  
  void spit(std::string filename, std::string out){
    std::ofstream out_file(filename);
    out_file << out;
    out_file.close();
  }

  static const char * const byte_to_hex_lut = "0123456789ABCDEF";

  std::string byte_to_hex(uint8_t b)
  {
      std::string output;
      output.reserve(2);
      output.push_back(byte_to_hex_lut[b >> 4]);
      output.push_back(byte_to_hex_lut[b & 15]);
      return output;
  }
  
  struct hex_to_byte
  {
      static uint8_t low(const char& value)
      {
          if(value <= '9' && '0' <= value)
          {
              return static_cast<uint8_t>(value - '0');
          }
          else // ('A' <= value && value <= 'F')
          {
              return static_cast<uint8_t>(10 + (value - 'A'));
          }
      }

      static uint8_t high(const char& value)
      {
          return (low(value) << 4);
      }
  };

  std::string hex_to_bytes(std::string hex)
  {
      std::stringstream buffer;
      const char * hex_data = hex.data();
      for(int i = 0; i < hex.length(); i += 2){
          char ch = (hex_to_byte::high(hex_data[i]) | hex_to_byte::low(hex_data[i+1]));
          buffer << ch;
      }
      return buffer.str();
  }
  

  void hex_to_bytes(std::string hex, uint8_t * ptr)
  {
      const char * hex_data = hex.data();
      for(int i = 0; i < hex.length(); i += 2){
          char ch = (hex_to_byte::high(hex_data[i]) | hex_to_byte::low(hex_data[i+1]));
          ptr[i] = ch;
      }
  }

}