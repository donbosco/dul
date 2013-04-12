package com.sun.jimi.core.util;

import com.sun.jimi.core.JimiImageFactory;

public abstract interface JimiImageFactoryProxy extends JimiImageFactory
{
  public abstract JimiImageFactory getProxiedFactory();

  public abstract void setProxiedFactory(JimiImageFactory paramJimiImageFactory);
}

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.util.JimiImageFactoryProxy
 * JD-Core Version:    0.6.2
 */