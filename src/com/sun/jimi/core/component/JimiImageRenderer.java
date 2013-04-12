package com.sun.jimi.core.component;

import com.sun.jimi.core.raster.JimiRasterImage;
import java.awt.Component;
import java.awt.Image;
import java.awt.image.ImageProducer;

public abstract interface JimiImageRenderer
{
  public abstract Component getContentPane();

  public abstract void render();

  public abstract void setImage(Image paramImage);

  public abstract void setImageProducer(ImageProducer paramImageProducer);

  public abstract void setRasterImage(JimiRasterImage paramJimiRasterImage);
}

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.component.JimiImageRenderer
 * JD-Core Version:    0.6.2
 */