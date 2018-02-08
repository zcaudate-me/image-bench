__kernel void grayscale_uchar(__global const uchar * src, 
                              int src_step, 
                              int src_offset, 
                              int rows, 
                              int cols,
                              __global uchar* dst, 
                              int dst_step, 
                              int dst_offset){
  uint x = get_global_id(0);
  uint y = get_global_id(1);
  
  if(x <= cols - 1 && y <= rows -1){
    uint idx = y * cols + x;
    dst[idx] = (src[3*idx] + src[3*idx + 1] + src[3*idx + 2])/3;
  }
}