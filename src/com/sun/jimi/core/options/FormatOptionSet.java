package com.sun.jimi.core.options;

public abstract interface FormatOptionSet extends Cloneable
{
  public abstract void copyOptionsFrom(FormatOptionSet paramFormatOptionSet);

  public abstract FormatOption getOption(String paramString)
    throws OptionException;

  public abstract FormatOption[] getOptions();
}

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.options.FormatOptionSet
 * JD-Core Version:    0.6.2
 */