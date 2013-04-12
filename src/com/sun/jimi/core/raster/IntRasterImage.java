package com.sun.jimi.core.raster;

import com.sun.jimi.core.ImageAccessException;

public abstract interface IntRasterImage extends MutableJimiRasterImage
{
  public static final int CHANNEL_ALPHA = 24;
  public static final int CHANNEL_RED = 16;
  public static final int CHANNEL_GREEN = 8;
  public static final int CHANNEL_BLUE = 0;

  public abstract int[] asIntArray();

  public abstract void getChannelRectangle(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, byte[] paramArrayOfByte, int paramInt6, int paramInt7)
    throws ImageAccessException;

  public abstract void getChannelRow(int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3)
    throws ImageAccessException;

  public abstract int getPixel(int paramInt1, int paramInt2)
    throws ImageAccessException;

  public abstract void getRectangle(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt, int paramInt5, int paramInt6)
    throws ImageAccessException;

  public abstract void getRow(int paramInt1, int[] paramArrayOfInt, int paramInt2)
    throws ImageAccessException;

  public abstract void setPixel(int paramInt1, int paramInt2, int paramInt3)
    throws ImageAccessException;

  public abstract void setRectangle(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt, int paramInt5, int paramInt6)
    throws ImageAccessException;

  public abstract void setRow(int paramInt1, int[] paramArrayOfInt, int paramInt2)
    throws ImageAccessException;
}

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.raster.IntRasterImage
 * JD-Core Version:    0.6.2
 */