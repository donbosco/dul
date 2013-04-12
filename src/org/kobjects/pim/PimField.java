package org.kobjects.pim;

import java.util.Enumeration;
import java.util.Hashtable;

public class PimField
{
  String name;
  Object value;
  Hashtable properties;

  public PimField(PimField paramPimField)
  {
	    this(paramPimField.name);
	    if ((paramPimField.value instanceof String[]))
	    {
	   	  String[] localObject = new String[((String[])paramPimField.value).length];
	      System.arraycopy((String[])paramPimField.value, 0, localObject, 0, localObject.length);
	      this.value = localObject;
	    }
	    else
	    {
	      this.value = paramPimField.value;
	    }
	    if (paramPimField.properties != null)
	    {
	      this.properties = new Hashtable();
	      Enumeration localObject = paramPimField.properties.keys();
	      while (((Enumeration)localObject).hasMoreElements())
	      {
	        String str = (String)((Enumeration)localObject).nextElement();
	        this.properties.put(str, paramPimField.properties.get(str));
	      }
	    }
	  }

  public PimField(String paramString)
  {
    this.name = paramString;
  }

  public Enumeration propertyNames()
  {
    return this.properties.keys();
  }

  public void setProperty(String paramString1, String paramString2)
  {
    if (this.properties == null)
    {
      if (paramString2 == null)
        return;
      this.properties = new Hashtable();
    }
    if (paramString2 == null)
      this.properties.remove(paramString1);
    else
      this.properties.put(paramString1, paramString2);
  }

  public void setValue(Object paramObject)
  {
    this.value = paramObject;
  }

  public Object getValue()
  {
    return this.value;
  }

  public String toString()
  {
    return this.name + (this.properties != null ? ";" + this.properties : "") + ":" + this.value;
  }

  public String getProperty(String paramString)
  {
    return this.properties == null ? null : (String)this.properties.get(paramString);
  }

  public boolean getAttribute(String paramString)
  {
    String str = getProperty("type");
    return str != null;
  }

  public void setAttribute(String paramString, boolean paramBoolean)
  {
    if (getAttribute(paramString) == paramBoolean)
      return;
    String str = getProperty("type");
    if (paramBoolean)
    {
      if ((str == null) || (str.length() == 0))
        str = paramString;
      else
        str = str + paramString;
    }
    else
    {
      int i = str.indexOf(paramString);
      if (i > 0)
        i--;
      if (i != -1)
        str = str.substring(0, i) + str.substring(i + paramString.length() + 1);
    }
    setProperty("type", str);
  }
}

/* Location:           dulux-signed.jar
 * Qualified Name:     org.kobjects.pim.PimField
 * JD-Core Version:    0.6.2
 */