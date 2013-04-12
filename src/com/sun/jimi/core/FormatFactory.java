package com.sun.jimi.core;

public abstract interface FormatFactory
{
  public abstract String[] getFilenameExtensions();

  public abstract String getFormatName();

  public abstract String[] getMimeTypes();
}

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.FormatFactory
 * JD-Core Version:    0.6.2
 */