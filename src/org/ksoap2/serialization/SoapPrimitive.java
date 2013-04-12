package org.ksoap2.serialization;

public class SoapPrimitive
{
  String namespace;
  String name;
  String value;

  public SoapPrimitive(String paramString1, String paramString2, String paramString3)
  {
    this.namespace = paramString1;
    this.name = paramString2;
    this.value = paramString3;
  }

  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof SoapPrimitive))
      return false;
    SoapPrimitive localSoapPrimitive = (SoapPrimitive)paramObject;
    return (this.name.equals(localSoapPrimitive.name)) && (this.namespace == null ? localSoapPrimitive.namespace == null : this.namespace.equals(localSoapPrimitive.namespace)) && (this.value == null ? localSoapPrimitive.value == null : this.value.equals(localSoapPrimitive.value));
  }

  public int hashCode()
  {
    return this.name.hashCode() ^ (this.namespace == null ? 0 : this.namespace.hashCode());
  }

  public String toString()
  {
    return this.value;
  }

  public String getNamespace()
  {
    return this.namespace;
  }

  public String getName()
  {
    return this.name;
  }
}

/* Location:           dulux-signed.jar
 * Qualified Name:     org.ksoap2.serialization.SoapPrimitive
 * JD-Core Version:    0.6.2
 */