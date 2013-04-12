package org.kobjects.pim;

import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;
import java.util.Vector;
import org.kobjects.io.LookAheadReader;

public class PimParser
{
  LookAheadReader reader;
  Class type;

  public PimParser(Reader paramReader, Class paramClass)
  {
    this.reader = new LookAheadReader(paramReader);
    this.type = paramClass;
  }

  public PimItem readItem()
    throws IOException
  {
    String str1 = readName();
    if (str1 == null)
      return null;
    if (!str1.equals("begin"))
      throw new RuntimeException("'begin:' expected");
    PimItem localPimItem;
    try
    {
      localPimItem = (PimItem)this.type.newInstance();
    }
    catch (Exception localException)
    {
      throw new RuntimeException(localException.toString());
    }
    this.reader.read();
    if (!localPimItem.getType().equals(readStringValue().toLowerCase()))
      throw new RuntimeException("item types do not match!");
    while (true)
    {
      String str2 = readName();
      if (str2.equals("end"))
        break;
      PimField localPimField = new PimField(str2);
      readProperties(localPimField);
      Object localObject;
      switch (localPimItem.getType(str2))
      {
      case 1:
        localObject = readArrayValue(localPimItem.getArraySize(str2));
        break;
      default:
        localObject = readStringValue();
      }
      localPimField.setValue(localObject);
      System.out.println("value:" + localObject);
      localPimItem.addField(localPimField);
    }
    this.reader.read();
    System.out.println("end:" + readStringValue());
    return localPimItem;
  }

  String readName()
    throws IOException
  {
    String str = this.reader.readTo(":;").trim().toLowerCase();
    System.out.println("name:" + str);
    return this.reader.peek(0) == -1 ? null : str;
  }

  String[] readArrayValue(int paramInt)
    throws IOException
  {
    Vector localVector = new Vector();
    StringBuffer localStringBuffer = new StringBuffer();
    int i = 1;
    do
    {
      localStringBuffer.append(this.reader.readTo(";\n\r"));
      switch (this.reader.read())
      {
      case 59:
        localVector.addElement(localStringBuffer.toString());
        localStringBuffer.setLength(0);
        break;
      case 13:
        if (this.reader.peek(0) == 10)
          this.reader.read();
      case 10:
        if (this.reader.peek(0) != 32)
          i = 0;
        else
          this.reader.read();
        break;
      }
    }
    while (i != 0);
    if (localStringBuffer.length() != 0)
      localVector.addElement(localStringBuffer.toString());
    String[] arrayOfString = new String[paramInt];
    for (int j = 0; j < Math.min(arrayOfString.length, localVector.size()); j++)
      arrayOfString[j] = ((String)localVector.elementAt(j));
    return arrayOfString;
  }

  String readStringValue()
    throws IOException
  {String str;
    for (str = this.reader.readLine(); this.reader.peek(0) == 32; str = str + this.reader.readLine())
      this.reader.read();
    return str;
  }

  void readProperties(PimField paramPimField)
    throws IOException
  {
	  int i ;
    for ( i = this.reader.read(); i == 32; i = this.reader.read());
	while (i != 58)
    {
      String str = this.reader.readTo(":;=").trim().toLowerCase();
      i = this.reader.read();
      if (i == 61)
      {
        paramPimField.setProperty(str, this.reader.readTo(":;").trim().toLowerCase());
        i = this.reader.read();
      }
      else
      {
        paramPimField.setAttribute(str, true);
      }
    }
  }
}

/* Location:           dulux-signed.jar
 * Qualified Name:     org.kobjects.pim.PimParser
 * JD-Core Version:    0.6.2
 */