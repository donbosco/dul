package com.sun.jimi.core;

import com.sun.jimi.core.options.FormatOptionSet;

public abstract interface MutableJimiImage extends JimiImage
{
  public abstract boolean mustWaitForOptions();

  public abstract void setDecodingController(JimiDecodingController paramJimiDecodingController);

  public abstract void setError();

  public abstract void setFinished();

  public abstract void setImageConsumerHints(int paramInt);

  public abstract void setOptions(FormatOptionSet paramFormatOptionSet);

  public abstract void setWaitForOptions(boolean paramBoolean);
}

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.MutableJimiImage
 * JD-Core Version:    0.6.2
 */