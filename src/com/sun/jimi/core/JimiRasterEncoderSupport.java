package com.sun.jimi.core;

import com.sun.jimi.core.util.ProgressListener;
import com.sun.jimi.core.util.ProgressMonitorSupport;
import java.io.OutputStream;

public abstract class JimiRasterEncoderSupport extends ProgressMonitorSupport
  implements JimiEncoder
{
  public abstract void encodeImages(JimiImageEnumeration paramJimiImageEnumeration, OutputStream paramOutputStream)
    throws JimiException;

  public abstract void encodeImages(JimiImageEnumeration paramJimiImageEnumeration, OutputStream paramOutputStream, ProgressListener paramProgressListener)
    throws JimiException;
}

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.JimiRasterEncoderSupport
 * JD-Core Version:    0.6.2
 */