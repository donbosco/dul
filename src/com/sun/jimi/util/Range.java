package com.sun.jimi.util;

public abstract interface Range
{
  public abstract Object getGreatestValue();

  public abstract Object getLeastValue();

  public abstract boolean isContinuous();

  public abstract boolean isInRange(Object paramObject);
}

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.util.Range
 * JD-Core Version:    0.6.2
 */