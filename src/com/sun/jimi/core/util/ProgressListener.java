package com.sun.jimi.core.util;

public abstract interface ProgressListener
{
  public abstract void setAbort();

  public abstract void setAbort(String paramString);

  public abstract void setFinished();

  public abstract void setProgressLevel(int paramInt);

  public abstract void setStarted();
}

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.util.ProgressListener
 * JD-Core Version:    0.6.2
 */