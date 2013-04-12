package org.kobjects.util;

import java.util.Enumeration;

public class SingleEnumeration
  implements Enumeration
{
  Object object;

  public SingleEnumeration(Object paramObject)
  {
    this.object = paramObject;
  }

  public boolean hasMoreElements()
  {
    return this.object != null;
  }

  public Object nextElement()
  {
    Object localObject = this.object;
    this.object = null;
    return localObject;
  }
}

/* Location:           dulux-signed.jar
 * Qualified Name:     org.kobjects.util.SingleEnumeration
 * JD-Core Version:    0.6.2
 */