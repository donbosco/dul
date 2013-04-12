package com.sun.jimi.core;

import com.sun.jimi.core.options.FormatOptionSet;
import java.awt.image.ImageProducer;

public abstract interface JimiImage
{
  public abstract JimiImageFactory getFactory();

  public abstract ImageProducer getImageProducer();

  public abstract FormatOptionSet getOptions();

  public abstract boolean isError();

  public abstract void setOptions(FormatOptionSet paramFormatOptionSet);

  public abstract void waitFinished();
}

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.JimiImage
 * JD-Core Version:    0.6.2
 */