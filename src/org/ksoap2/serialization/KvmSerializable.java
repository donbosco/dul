package org.ksoap2.serialization;

import java.util.Hashtable;

public abstract interface KvmSerializable
{
  public abstract Object getProperty(int paramInt);

  public abstract int getPropertyCount();

  public abstract void setProperty(int paramInt, Object paramObject);

  public abstract void getPropertyInfo(int paramInt, Hashtable paramHashtable, PropertyInfo paramPropertyInfo);
}

/* Location:           dulux-signed.jar
 * Qualified Name:     org.ksoap2.serialization.KvmSerializable
 * JD-Core Version:    0.6.2
 */