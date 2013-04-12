package com.sun.jimi.core.encoder.bmp;

import com.sun.jimi.core.JimiException;
import com.sun.jimi.core.compat.AdaptiveRasterImage;
import com.sun.jimi.core.util.LEDataOutputStream;

public abstract interface BMPEncoderIfc
{
  public static final int __BMP_TYPE = 19778;
  public static final int __BITMAP_FILE_HEADER_SIZE = 14;
  public static final int __BITMAP_INFO_HEADER_SIZE = 40;

  public abstract void encodeBMP(BMPEncoder paramBMPEncoder, AdaptiveRasterImage paramAdaptiveRasterImage, LEDataOutputStream paramLEDataOutputStream)
    throws JimiException;
}

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.encoder.bmp.BMPEncoderIfc
 * JD-Core Version:    0.6.2
 */