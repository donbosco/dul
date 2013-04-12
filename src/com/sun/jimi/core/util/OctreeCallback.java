package com.sun.jimi.core.util;

abstract interface OctreeCallback
{
  public static final int MAXLEV = 8;

  public abstract void cacheONode(OctreeNode paramOctreeNode);

  public abstract OctreeNode getONode(OctreeCallback paramOctreeCallback, int paramInt);

  public abstract OctreeNode getReducible();

  public abstract void markReducible(OctreeNode paramOctreeNode);
}

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.util.OctreeCallback
 * JD-Core Version:    0.6.2
 */