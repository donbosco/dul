package com.sun.jimi.core;

import com.sun.jimi.core.raster.BitRasterImage;
import com.sun.jimi.core.raster.ByteRasterImage;
import com.sun.jimi.core.raster.ChanneledIntRasterImage;
import com.sun.jimi.core.raster.IntRasterImage;
import java.awt.image.ColorModel;

public abstract interface JimiImageFactory
{
  public abstract BitRasterImage createBitRasterImage(int paramInt1, int paramInt2, ColorModel paramColorModel)
    throws JimiException;

  public abstract ByteRasterImage createByteRasterImage(int paramInt1, int paramInt2, ColorModel paramColorModel)
    throws JimiException;

  public abstract ChanneledIntRasterImage createChanneledIntRasterImage(int paramInt1, int paramInt2, ColorModel paramColorModel)
    throws JimiException;

  public abstract IntRasterImage createIntRasterImage(int paramInt1, int paramInt2, ColorModel paramColorModel)
    throws JimiException;
}

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.JimiImageFactory
 * JD-Core Version:    0.6.2
 */