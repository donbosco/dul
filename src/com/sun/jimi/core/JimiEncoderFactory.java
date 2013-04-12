package com.sun.jimi.core;

public abstract interface JimiEncoderFactory extends FormatFactory
{
  public abstract boolean canEncodeMultipleImages();

  public abstract JimiEncoder createEncoder();
}

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.JimiEncoderFactory
 * JD-Core Version:    0.6.2
 */