package com.sun.jimi.core.raster;

import com.sun.jimi.core.ImageAccessException;

public abstract interface LongRasterImage
{
  public abstract long getPixel(int paramInt1, int paramInt2)
    throws ImageAccessException;

  public abstract void getRectangle(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long[] paramArrayOfLong, int paramInt5, int paramInt6)
    throws ImageAccessException;

  public abstract void getRow(int paramInt1, long[] paramArrayOfLong, int paramInt2)
    throws ImageAccessException;

  public abstract void setPixel(int paramInt1, int paramInt2, long paramLong)
    throws ImageAccessException;

  public abstract void setRectangle(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long[] paramArrayOfLong, int paramInt5, int paramInt6)
    throws ImageAccessException;

  public abstract void setRow(int paramInt1, long[] paramArrayOfLong, int paramInt2)
    throws ImageAccessException;
}

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.raster.LongRasterImage
 * JD-Core Version:    0.6.2
 */