package com.sun.jimi.core.raster;

import com.sun.jimi.core.ImageAccessException;
import com.sun.jimi.core.JimiImage;
import java.awt.image.ColorModel;
import java.awt.image.ImageProducer;
import java.util.Hashtable;

public abstract interface JimiRasterImage extends JimiImage
{
  public static final int UNKNOWN = -1;
  public static final int CHANNEL_ALPHA = 24;
  public static final int CHANNEL_RED = 16;
  public static final int CHANNEL_GREEN = 8;
  public static final int CHANNEL_BLUE = 0;

  public abstract void getChannelRectangle(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, byte[] paramArrayOfByte, int paramInt6, int paramInt7)
    throws ImageAccessException;

  public abstract ColorModel getColorModel();

  public abstract ImageProducer getCroppedImageProducer(int paramInt1, int paramInt2, int paramInt3, int paramInt4);

  public abstract int getHeight();

  public abstract int getPixelRGB(int paramInt1, int paramInt2)
    throws ImageAccessException;

  /** @deprecated */
  public abstract Hashtable getProperties();

  public abstract void getRectangleARGBChannels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte, int paramInt5, int paramInt6)
    throws ImageAccessException;

  public abstract void getRectangleRGB(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt, int paramInt5, int paramInt6)
    throws ImageAccessException;

  public abstract void getRectangleRGBAChannels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte, int paramInt5, int paramInt6)
    throws ImageAccessException;

  public abstract void getRectangleRGBChannels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte, int paramInt5, int paramInt6)
    throws ImageAccessException;

  public abstract void getRowRGB(int paramInt1, int[] paramArrayOfInt, int paramInt2)
    throws ImageAccessException;

  public abstract int getWidth();

  public abstract void waitInfoAvailable();
}

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.raster.JimiRasterImage
 * JD-Core Version:    0.6.2
 */