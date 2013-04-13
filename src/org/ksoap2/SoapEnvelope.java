package org.ksoap2;

import java.io.IOException;
import org.kxml2.kdom.Element;
import org.kxml2.kdom.Node;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

public class SoapEnvelope
{
  public static final int VER10 = 100;
  public static final int VER11 = 110;
  public static final int VER12 = 120;
  public static final String ENV2001 = "http://www.w3.org/2001/12/soap-envelope";
  public static final String ENC2001 = "http://www.w3.org/2001/12/soap-encoding";
  public static final String ENV = "http://schemas.xmlsoap.org/soap/envelope/";
  public static final String ENC = "http://schemas.xmlsoap.org/soap/encoding/";
  public static final String XSD = "http://www.w3.org/2001/XMLSchema";
  public static final String XSI = "http://www.w3.org/2001/XMLSchema-instance";
  public static final String XSD1999 = "http://www.w3.org/1999/XMLSchema";
  public static final String XSI1999 = "http://www.w3.org/1999/XMLSchema-instance";
  public Object bodyIn;
  public Object bodyOut;
  public Element[] headerIn;
  public Element[] headerOut;
  public String encodingStyle;
  public int version;
  public String env;
  public String enc;
  public String xsi;
  public String xsd;

  public static boolean stringToBoolean(String paramString)
  {
    if (paramString == null)
      return false;
    paramString = paramString.trim().toLowerCase();
    return (paramString.equals("1")) || (paramString.equals("true"));
  }

  public SoapEnvelope(int paramInt)
  {
    this.version = paramInt;
    if (paramInt == 100)
    {
      this.xsi = "http://www.w3.org/1999/XMLSchema-instance";
      this.xsd = "http://www.w3.org/1999/XMLSchema";
    }
    else
    {
      this.xsi = "http://www.w3.org/2001/XMLSchema-instance";
      this.xsd = "http://www.w3.org/2001/XMLSchema";
    }
    if (paramInt < 120)
    {
      this.enc = "http://schemas.xmlsoap.org/soap/encoding/";
      this.env = "http://schemas.xmlsoap.org/soap/envelope/";
    }
    else
    {
      this.enc = "http://www.w3.org/2001/12/soap-encoding";
      this.env = "http://www.w3.org/2001/12/soap-envelope";
    }
  }

  public void parse(XmlPullParser paramXmlPullParser)
    throws IOException, XmlPullParserException
  {
    //paramXmlPullParser.nextTag();
    //paramXmlPullParser.require(2, this.env, "Envelope");
    this.encodingStyle = paramXmlPullParser.getAttributeValue(this.env, "encodingStyle");
    //paramXmlPullParser.nextTag();
    if ((paramXmlPullParser.getEventType() == 2) && (paramXmlPullParser.getNamespace().equals(this.env)) && (paramXmlPullParser.getName().equals("Header")))
    {
      parseHeader(paramXmlPullParser);
      //paramXmlPullParser.require(3, this.env, "Header");
      //paramXmlPullParser.nextTag();
    }
    //paramXmlPullParser.require(2, this.env, "Body");
    this.encodingStyle = paramXmlPullParser.getAttributeValue(this.env, "encodingStyle");
    parseBody(paramXmlPullParser);
    //paramXmlPullParser.require(3, this.env, "Body");
    //paramXmlPullParser.nextTag();
    //paramXmlPullParser.require(3, this.env, "Envelope");
  }

  public void parseHeader(XmlPullParser paramXmlPullParser)
    throws IOException, XmlPullParserException
  {
    paramXmlPullParser.nextTag();
    Node localNode = new Node();
    localNode.parse(paramXmlPullParser);
    int i = 0;
    Element localElement;
    for (int j = 0; j < localNode.getChildCount(); j++)
    {
      localElement = localNode.getElement(j);
      if (localElement != null)
        i++;
    }
    this.headerIn = new Element[i];
    i = 0;
    for (int j = 0; j < localNode.getChildCount(); j++)
    {
      localElement = localNode.getElement(j);
      if (localElement != null)
        this.headerIn[(i++)] = localElement;
    }
  }

  public void parseBody(XmlPullParser paramXmlPullParser)
    throws IOException, XmlPullParserException
  {
    paramXmlPullParser.nextTag();
    Object localObject;
    if ((paramXmlPullParser.getEventType() == 2) && (paramXmlPullParser.getNamespace().equals(this.env)) && (paramXmlPullParser.getName().equals("Fault")))
    {
      localObject = new SoapFault();
      ((SoapFault)localObject).parse(paramXmlPullParser);
      this.bodyIn = localObject;
    }
    else
    {
      localObject = (this.bodyIn instanceof Node) ? (Node)this.bodyIn : new Node();
      ((Node)localObject).parse(paramXmlPullParser);
      this.bodyIn = localObject;
    }
  }

  public void write(XmlSerializer paramXmlSerializer)
    throws IOException
  {
    paramXmlSerializer.setPrefix("i", this.xsi);
    paramXmlSerializer.setPrefix("d", this.xsd);
    paramXmlSerializer.setPrefix("c", this.enc);
    paramXmlSerializer.setPrefix("v", this.env);
    paramXmlSerializer.startTag(this.env, "Envelope");
    paramXmlSerializer.startTag(this.env, "Header");
    writeHeader(paramXmlSerializer);
    paramXmlSerializer.endTag(this.env, "Header");
    paramXmlSerializer.startTag(this.env, "Body");
    writeBody(paramXmlSerializer);
    paramXmlSerializer.endTag(this.env, "Body");
    paramXmlSerializer.endTag(this.env, "Envelope");
  }

  public void writeHeader(XmlSerializer paramXmlSerializer)
    throws IOException
  {
    if (this.headerOut != null)
      for (int i = 0; i < this.headerOut.length; i++)
        this.headerOut[i].write(paramXmlSerializer);
  }

  public void writeBody(XmlSerializer paramXmlSerializer)
    throws IOException
  {
    if (this.encodingStyle != null)
      paramXmlSerializer.attribute(this.env, "encodingStyle", this.encodingStyle);
    ((Node)this.bodyOut).write(paramXmlSerializer);
  }

  public void setOutputSoapObject(Object paramObject)
  {
    this.bodyOut = paramObject;
  }
}

/* Location:           dulux-signed.jar
 * Qualified Name:     org.ksoap2.SoapEnvelope
 * JD-Core Version:    0.6.2
 */