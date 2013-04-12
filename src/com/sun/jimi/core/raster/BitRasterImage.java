package com.sun.jimi.core.raster;

import com.sun.jimi.core.ImageAccessException;

public abstract interface BitRasterImage extends ByteRasterImage
{
  public abstract void getRectanglePacked(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte, int paramInt5, int paramInt6)
    throws ImageAccessException;

  public abstract void getRowPacked(int paramInt1, byte[] paramArrayOfByte, int paramInt2)
    throws ImageAccessException;

  public abstract void setRectanglePacked(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte, int paramInt5, int paramInt6)
    throws ImageAccessException;

  public abstract void setRowPacked(int paramInt1, byte[] paramArrayOfByte, int paramInt2)
    throws ImageAccessException;
}

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.raster.BitRasterImage
 * JD-Core Version:    0.6.2
 */