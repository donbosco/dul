package org.kobjects.pim;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public abstract class PimItem
{
  Hashtable fields = new Hashtable();
  public static final int TYPE_STRING = 0;
  public static final int TYPE_STRING_ARRAY = 1;

  public PimItem()
  {
  }

  public PimItem(PimItem paramPimItem)
  {
    Enumeration localEnumeration = paramPimItem.fields();
    while (localEnumeration.hasMoreElements())
      addField(new PimField((PimField)localEnumeration.nextElement()));
  }

  public Enumeration fieldNames()
  {
    return this.fields.keys();
  }

  public void addField(PimField paramPimField)
  {
    Vector localVector = (Vector)this.fields.get(paramPimField.name);
    if (localVector == null)
    {
      localVector = new Vector();
      this.fields.put(paramPimField.name, localVector);
    }
    localVector.addElement(paramPimField);
  }

  public Enumeration fields()
  {
    Vector localVector = new Vector();
    Enumeration localEnumeration1 = fieldNames();
    while (localEnumeration1.hasMoreElements())
    {
      String str = (String)localEnumeration1.nextElement();
      Enumeration localEnumeration2 = fields(str);
      while (localEnumeration2.hasMoreElements())
        localVector.addElement(localEnumeration2.nextElement());
    }
    return localVector.elements();
  }

  public Enumeration fields(String paramString)
  {
    Vector localVector = (Vector)this.fields.get(paramString);
    if (localVector == null)
      localVector = new Vector();
    return localVector.elements();
  }

  public PimField getField(String paramString, int paramInt)
  {
    return (PimField)((Vector)this.fields.get(paramString)).elementAt(paramInt);
  }

  public int getFieldCount(String paramString)
  {
    Vector localVector = (Vector)this.fields.get(paramString);
    return localVector == null ? 0 : localVector.size();
  }

  public abstract String getType();

  public abstract int getArraySize(String paramString);

  public int getType(String paramString)
  {
    return getArraySize(paramString) == -1 ? 0 : 1;
  }

  public void removeField(String paramString, int paramInt)
  {
    ((Vector)this.fields.get(paramString)).removeElementAt(paramInt);
  }

  public String toString()
  {
    return getType() + ":" + this.fields.toString();
  }
}

/* Location:           dulux-signed.jar
 * Qualified Name:     org.kobjects.pim.PimItem
 * JD-Core Version:    0.6.2
 */