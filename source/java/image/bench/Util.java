package image.bench;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;

public class Util{

  public static void copy(BufferedImage input, BufferedImage output){
    
    byte [] in = ((DataBufferByte)(input.getRaster().getDataBuffer())).getData();
    byte [] out = ((DataBufferByte)(output.getRaster().getDataBuffer())).getData();
    
    for(int i = 0; i < out.length; i++){
      out[i] = in[i];
    }
  }

  public static void grayscale(BufferedImage input, BufferedImage output){
    
    byte [] in = ((DataBufferByte)(input.getRaster().getDataBuffer())).getData();
    byte [] out = ((DataBufferByte)(output.getRaster().getDataBuffer())).getData();
    
    for(int i = 0; i < out.length; i++){
      out[i] = (byte)((Byte.toUnsignedInt(in[i*3]) + 
                       Byte.toUnsignedInt(in[i*3+1]) +
                       Byte.toUnsignedInt(in[i*3+2]))/3);
    }
  }

  
  public static void multiply(BufferedImage input, float number){
    
    byte [] in = ((DataBufferByte)(input.getRaster().getDataBuffer())).getData();
    
    for(int i = 0; i < in.length; i++){
      in[i] = (byte)((Byte.toUnsignedInt(in[i])*number));
    }
  }  
  
  public static byte[] hexStringToByteArray(String s) {
    byte[] b = new byte[s.length() / 2];
    for (int i = 0; i < b.length; i++) {
      int index = i * 2;
      int v = Integer.parseInt(s.substring(index, index + 2), 16);
      b[i] = (byte) v;
    }
    return b;
  }
}