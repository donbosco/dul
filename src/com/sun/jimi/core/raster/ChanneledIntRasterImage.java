package com.sun.jimi.core.raster;

import com.sun.jimi.core.ImageAccessException;

public abstract interface ChanneledIntRasterImage extends IntRasterImage
{
  public abstract void setChannelPixel(int paramInt1, int paramInt2, int paramInt3, byte paramByte)
    throws ImageAccessException;

  public abstract void setChannelRectangle(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, byte[] paramArrayOfByte, int paramInt6, int paramInt7)
    throws ImageAccessException;

  public abstract void setChannelRow(int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3)
    throws ImageAccessException;
}

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.raster.ChanneledIntRasterImage
 * JD-Core Version:    0.6.2
 */