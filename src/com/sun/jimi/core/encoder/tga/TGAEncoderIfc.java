package com.sun.jimi.core.encoder.tga;

import com.sun.jimi.core.JimiException;
import com.sun.jimi.core.compat.AdaptiveRasterImage;
import com.sun.jimi.core.util.LEDataOutputStream;

public abstract interface TGAEncoderIfc
{
  public abstract void encodeTGA(AdaptiveRasterImage paramAdaptiveRasterImage, LEDataOutputStream paramLEDataOutputStream)
    throws JimiException;
}

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.encoder.tga.TGAEncoderIfc
 * JD-Core Version:    0.6.2
 */