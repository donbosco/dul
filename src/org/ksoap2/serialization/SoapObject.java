package org.ksoap2.serialization;

import java.util.Hashtable;
import java.util.Vector;

public class SoapObject
  implements KvmSerializable
{
  String namespace;
  String name;
  Vector info = new Vector();
  Vector data = new Vector();

  public SoapObject(String paramString1, String paramString2)
  {
    this.namespace = paramString1;
    this.name = paramString2;
  }

  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof SoapObject))
      return false;
    SoapObject localSoapObject = (SoapObject)paramObject;
    int i = this.data.size();
    if (i != localSoapObject.data.size())
      return false;
    try
    {
      for (int j = 0; j < i; j++)
        if (!this.data.elementAt(j).equals(localSoapObject.getProperty(((PropertyInfo)this.info.elementAt(j)).name)))
          return false;
    }
    catch (Exception localException)
    {
      return false;
    }
    return true;
  }

  public String getName()
  {
    return this.name;
  }

  public String getNamespace()
  {
    return this.namespace;
  }

  public Object getProperty(int paramInt)
  {
    return this.data.elementAt(paramInt);
  }

  public Object getProperty(String paramString)
  {
    for (int i = 0; i < this.data.size(); i++)
      if (paramString.equals(((PropertyInfo)this.info.elementAt(i)).name))
        return this.data.elementAt(i);
    throw new RuntimeException("illegal property: " + paramString);
  }

  public int getPropertyCount()
  {
    return this.data.size();
  }

  public void getPropertyInfo(int paramInt, Hashtable paramHashtable, PropertyInfo paramPropertyInfo)
  {
    PropertyInfo localPropertyInfo = (PropertyInfo)this.info.elementAt(paramInt);
    paramPropertyInfo.name = localPropertyInfo.name;
    paramPropertyInfo.namespace = localPropertyInfo.namespace;
    paramPropertyInfo.flags = localPropertyInfo.flags;
    paramPropertyInfo.type = localPropertyInfo.type;
    paramPropertyInfo.elementType = localPropertyInfo.elementType;
  }

  public SoapObject newInstance()
  {
    SoapObject localSoapObject = new SoapObject(this.namespace, this.name);
    for (int i = 0; i < this.data.size(); i++)
    {
      PropertyInfo localPropertyInfo = (PropertyInfo)this.info.elementAt(i);
      localSoapObject.addProperty(localPropertyInfo, this.data.elementAt(i));
    }
    return localSoapObject;
  }

  public void setProperty(int paramInt, Object paramObject)
  {
    this.data.setElementAt(paramObject, paramInt);
  }

  public SoapObject addProperty(String paramString, Object paramObject)
  {
    PropertyInfo localPropertyInfo = new PropertyInfo();
    localPropertyInfo.name = paramString;
    localPropertyInfo.type = (paramObject == null ? PropertyInfo.OBJECT_CLASS : paramObject.getClass());
    return addProperty(localPropertyInfo, paramObject);
  }

  public SoapObject addProperty(PropertyInfo paramPropertyInfo, Object paramObject)
  {
    this.info.addElement(paramPropertyInfo);
    this.data.addElement(paramObject);
    return this;
  }

  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer("" + this.name + "{");
    for (int i = 0; i < getPropertyCount(); i++)
      localStringBuffer.append("" + ((PropertyInfo)this.info.elementAt(i)).name + "=" + getProperty(i) + "; ");
    localStringBuffer.append("}");
    return localStringBuffer.toString();
  }
}

/* Location:           dulux-signed.jar
 * Qualified Name:     org.ksoap2.serialization.SoapObject
 * JD-Core Version:    0.6.2
 */