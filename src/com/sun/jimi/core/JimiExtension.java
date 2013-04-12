package com.sun.jimi.core;

public abstract interface JimiExtension
{
  public abstract JimiDecoderFactory[] getDecoders();

  public abstract String getDescription();

  public abstract JimiEncoderFactory[] getEncoders();

  public abstract String getVendor();

  public abstract String getVersionString();
}

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.JimiExtension
 * JD-Core Version:    0.6.2
 */