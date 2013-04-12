package com.sun.jimi.core;

import com.sun.jimi.core.util.ProgressListener;
import java.io.InputStream;

public abstract interface JimiDecoder
{
  public abstract void addCleanupCommand(Runnable paramRunnable);

  public abstract ImageSeriesDecodingController initDecoding(JimiImageFactory paramJimiImageFactory, InputStream paramInputStream);

  public abstract void setFinished();

  public abstract void setProgressListener(ProgressListener paramProgressListener);
}

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.JimiDecoder
 * JD-Core Version:    0.6.2
 */