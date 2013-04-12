package org.kobjects.xmlrpc;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;
import org.kobjects.base64.Base64;
import org.kobjects.isodate.IsoDate;
import org.kobjects.xml.XmlReader;

public class XmlRpcParser
{
  private XmlReader parser = null;

  public XmlRpcParser(XmlReader paramXmlReader)
  {
    this.parser = paramXmlReader;
  }

  private final Hashtable parseStruct()
    throws IOException
  {
    Hashtable localHashtable = new Hashtable();
    for (int i = nextTag(); i != 3; i = nextTag())
    {
      nextTag();
      String str = nextText();
      nextTag();
      localHashtable.put(str, parseValue());
    }
    nextTag();
    return localHashtable;
  }

  private final Object parseValue()
    throws IOException
  {
    Object localObject = null;
    int i = this.parser.next();
    if (i == 4)
    {
      localObject = this.parser.getText();
      i = this.parser.next();
    }
    if (i == 2)
    {
      String str = this.parser.getName();
      if (str.equals("array"))
      {
        localObject = parseArray();
      }
      else if (str.equals("struct"))
      {
        localObject = parseStruct();
      }
      else
      {
        if (str.equals("string"))
          localObject = nextText();
        else if ((str.equals("i4")) || (str.equals("int")))
          localObject = new Integer(Integer.parseInt(nextText().trim()));
        else if (str.equals("boolean"))
          localObject = new Boolean(nextText().trim().equals("1"));
        else if (str.equals("dateTime.iso8601"))
          localObject = IsoDate.stringToDate(nextText(), 3);
        else if (str.equals("base64"))
          localObject = Base64.decode(nextText());
        else if (str.equals("double"))
          localObject = nextText();
        nextTag();
      }
    }
    nextTag();
    return localObject;
  }

  private final Vector parseArray()
    throws IOException
  {
    nextTag();
    int i = nextTag();
    Vector localVector = new Vector();
    while (i != 3)
    {
      localVector.addElement(parseValue());
      i = this.parser.getType();
    }
    nextTag();
    nextTag();
    return localVector;
  }

  private final Object parseFault()
    throws IOException
  {
    nextTag();
    Object localObject = parseValue();
    nextTag();
    return localObject;
  }

  private final Object parseParams()
    throws IOException
  {
    Vector localVector = new Vector();
    for (int i = nextTag(); i != 3; i = nextTag())
    {
      nextTag();
      localVector.addElement(parseValue());
    }
    nextTag();
    return localVector;
  }

  public final Object parseResponse()
    throws IOException
  {
    Object localObject = null;
    nextTag();
    int i = nextTag();
    if (i == 2)
      if ("fault".equals(this.parser.getName()))
        localObject = parseFault();
      else if ("params".equals(this.parser.getName()))
        localObject = parseParams();
    return localObject;
  }

  private final int nextTag()
    throws IOException
  {
    int i = this.parser.getType();
    i = this.parser.next();
    if ((i == 4) && (this.parser.isWhitespace()))
      i = this.parser.next();
    if ((i != 3) && (i != 2))
      throw new IOException("unexpected type: " + i);
    return i;
  }

  private final String nextText()
    throws IOException
  {
    int i = this.parser.getType();
    if (i != 2)
      throw new IOException("precondition: START_TAG");
    i = this.parser.next();
    String str;
    if (i == 4)
    {
      str = this.parser.getText();
      i = this.parser.next();
    }
    else
    {
      str = "";
    }
    if (i != 3)
      throw new IOException("END_TAG expected");
    return str;
  }
}

/* Location:           dulux-signed.jar
 * Qualified Name:     org.kobjects.xmlrpc.XmlRpcParser
 * JD-Core Version:    0.6.2
 */