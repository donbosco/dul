package com.sun.jimi.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract interface RandomAccessStorage
{
  /** @deprecated */
  public abstract InputStream asInputStream();

  /** @deprecated */
  public abstract OutputStream asOutputStream();

  public abstract void seek(long paramLong)
    throws IOException;

  public abstract void skip(int paramInt)
    throws IOException;
}

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.util.RandomAccessStorage
 * JD-Core Version:    0.6.2
 */