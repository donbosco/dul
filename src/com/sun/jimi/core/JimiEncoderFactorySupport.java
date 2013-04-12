package com.sun.jimi.core;

public abstract class JimiEncoderFactorySupport
  implements JimiEncoderFactory
{
  public abstract boolean canEncodeMultipleImages();

  public abstract JimiEncoder createEncoder();

  public abstract String[] getFilenameExtensions();

  public abstract String getFormatName();

  public abstract String[] getMimeTypes();
}

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.JimiEncoderFactorySupport
 * JD-Core Version:    0.6.2
 */