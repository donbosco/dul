package com.sun.jimi.core;

import com.sun.jimi.core.util.ProgressListener;
import java.io.OutputStream;

public abstract interface JimiEncoder
{
  public abstract void encodeImages(JimiImageEnumeration paramJimiImageEnumeration, OutputStream paramOutputStream)
    throws JimiException;

  public abstract void encodeImages(JimiImageEnumeration paramJimiImageEnumeration, OutputStream paramOutputStream, ProgressListener paramProgressListener)
    throws JimiException;

  public abstract void setProgressListener(ProgressListener paramProgressListener);
}

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.JimiEncoder
 * JD-Core Version:    0.6.2
 */