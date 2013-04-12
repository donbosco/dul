package org.kobjects.xmlrpc;

import java.io.FileReader;
import org.kobjects.xml.XmlReader;

public class Driver
{
  public static void main(String[] paramArrayOfString)
    throws Exception
  {
    XmlReader localXmlReader = new XmlReader(new FileReader(paramArrayOfString[0]));
    XmlRpcParser localXmlRpcParser = new XmlRpcParser(localXmlReader);
    localXmlRpcParser.parseResponse();
  }
}

/* Location:           dulux-signed.jar
 * Qualified Name:     org.kobjects.xmlrpc.Driver
 * JD-Core Version:    0.6.2
 */