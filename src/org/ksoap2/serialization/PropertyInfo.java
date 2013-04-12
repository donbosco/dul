package org.ksoap2.serialization;

import java.util.Vector;

public class PropertyInfo
{
  public static final Class OBJECT_CLASS = new Object().getClass();
  public static final Class STRING_CLASS = "".getClass();
  public static final Class INTEGER_CLASS = new Integer(0).getClass();
  public static final Class LONG_CLASS = new Long(0L).getClass();
  public static final Class BOOLEAN_CLASS = new Boolean(true).getClass();
  public static final Class VECTOR_CLASS = new Vector().getClass();
  public static final PropertyInfo OBJECT_TYPE = new PropertyInfo();
  public static final int TRANSIENT = 1;
  public static final int MULTI_REF = 2;
  public static final int REF_ONLY = 4;
  public String name;
  public String namespace;
  public int flags;
  public Object type = OBJECT_CLASS;
  public boolean multiRef;
  public PropertyInfo elementType;

  public void clear()
  {
    this.type = OBJECT_CLASS;
    this.flags = 0;
    this.name = null;
    this.namespace = null;
  }
}

/* Location:           dulux-signed.jar
 * Qualified Name:     org.ksoap2.serialization.PropertyInfo
 * JD-Core Version:    0.6.2
 */