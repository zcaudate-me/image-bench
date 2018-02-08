__kernel void sobel_uchar(__global const uchar * src, 
                          int src_step, 
                          int src_offset, 
                          int rows, 
                          int cols,
                          __global uchar* dst, 
                          int dst_step, 
                          int dst_offset){
  uint x = get_global_id(0);
  uint y = get_global_id(1);
  uint idx = y * cols + x;
  
  if(x > 0 && x < cols - 1 && y > 0 && y < rows -1){
  
    uint a = src[idx - cols - 1],  b = src[idx - cols],    c = src[idx - cols + 1];
    uint d = src[idx        - 1],                          f = src[idx        + 1];
    uint g = src[idx + cols - 1],  h = src[idx + cols],    i = src[idx + cols + 1];
  
    uint dx = a + 2*d + g - c - 2*f - i;
    uint dy = a + 2*b + c - c - 2*h - i;

    uchar val = (uchar)sqrt((float)dx*dx+dy*dy);
    if (val == 255)
      val = 0;
    dst[idx] = val;
    //dst[idx] = (uchar)min((uint)255, (uint)sqrt((float)dx*dx+dy*dy));
  } else {
    dst[idx] = 0;
  }
}