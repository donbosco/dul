package com.sun.jimi.core;

public abstract interface JimiDecoderFactory extends FormatFactory
{
  public abstract JimiDecoder createDecoder();

  public abstract byte[][] getFormatSignatures();
}

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.JimiDecoderFactory
 * JD-Core Version:    0.6.2
 */