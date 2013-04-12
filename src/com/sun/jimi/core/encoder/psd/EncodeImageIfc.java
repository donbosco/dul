package com.sun.jimi.core.encoder.psd;

import com.sun.jimi.core.JimiException;
import com.sun.jimi.core.compat.AdaptiveRasterImage;
import java.io.DataOutputStream;

public abstract interface EncodeImageIfc
{
  public abstract void encodeImage(AdaptiveRasterImage paramAdaptiveRasterImage, DataOutputStream paramDataOutputStream, int paramInt)
    throws JimiException;
}

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.encoder.psd.EncodeImageIfc
 * JD-Core Version:    0.6.2
 */