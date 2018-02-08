#include <string>

namespace ya{
  std::string slurp(std::string filename);
  
  void spit(std::string filename, std::string out);

  std::string byte_to_hex(uint8_t b);

  std::string bytes_to_hex(uint8_t * array, int len);
    
  std::string hex_to_bytes(std::string hex);
  
  void hex_to_bytes(std::string hex, uint8_t * ptr);
  
  
}