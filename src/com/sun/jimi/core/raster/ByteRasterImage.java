package com.sun.jimi.core.raster;

import com.sun.jimi.core.ImageAccessException;

public abstract interface ByteRasterImage extends MutableJimiRasterImage
{
  public abstract byte[] asByteArray();

  public abstract byte getPixel(int paramInt1, int paramInt2)
    throws ImageAccessException;

  public abstract void getRectangle(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte, int paramInt5, int paramInt6)
    throws ImageAccessException;

  public abstract void getRow(int paramInt1, byte[] paramArrayOfByte, int paramInt2)
    throws ImageAccessException;

  public abstract void setPixel(int paramInt1, int paramInt2, byte paramByte)
    throws ImageAccessException;

  public abstract void setRectangle(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte, int paramInt5, int paramInt6)
    throws ImageAccessException;

  public abstract void setRow(int paramInt1, byte[] paramArrayOfByte, int paramInt2)
    throws ImageAccessException;
}

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.raster.ByteRasterImage
 * JD-Core Version:    0.6.2
 */