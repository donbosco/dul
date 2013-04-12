package org.ksoap2;

import java.io.IOException;
import org.kxml2.kdom.Node;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

public class SoapFault extends IOException
{
  public String faultcode;
  public String faultstring;
  public String faultactor;
  public Node detail;

  public void parse(XmlPullParser paramXmlPullParser)
    throws IOException, XmlPullParserException
  {
    paramXmlPullParser.require(2, "http://schemas.xmlsoap.org/soap/envelope/", "Fault");
    while (paramXmlPullParser.nextTag() == 2)
    {
      String str = paramXmlPullParser.getName();
      if (str.equals("detail"))
      {
        this.detail = new Node();
        this.detail.parse(paramXmlPullParser);
      }
      else
      {
        if (str.equals("faultcode"))
          this.faultcode = paramXmlPullParser.nextText();
        else if (str.equals("faultstring"))
          this.faultstring = paramXmlPullParser.nextText();
        else if (str.equals("faultactor"))
          this.faultactor = paramXmlPullParser.nextText();
        else
          throw new RuntimeException("unexpected tag:" + str);
        paramXmlPullParser.require(3, null, str);
      }
    }
  }

  public void write(XmlSerializer paramXmlSerializer)
    throws IOException
  {
    paramXmlSerializer.startTag("http://schemas.xmlsoap.org/soap/envelope/", "Fault");
    paramXmlSerializer.startTag(null, "faultcode");
    paramXmlSerializer.text("" + this.faultcode);
    paramXmlSerializer.endTag(null, "faultcode");
    paramXmlSerializer.startTag(null, "faultstring");
    paramXmlSerializer.text("" + this.faultstring);
    paramXmlSerializer.endTag(null, "faultstring");
    paramXmlSerializer.startTag(null, "detail");
    if (this.detail != null)
      this.detail.write(paramXmlSerializer);
    paramXmlSerializer.endTag(null, "detail");
    paramXmlSerializer.endTag("http://schemas.xmlsoap.org/soap/envelope/", "Fault");
  }

  public String toString()
  {
    return "SoapFault - faultcode: '" + this.faultcode + "' faultstring: '" + this.faultstring + "' faultactor: '" + this.faultactor + "' detail: " + this.detail;
  }
}

/* Location:           dulux-signed.jar
 * Qualified Name:     org.ksoap2.SoapFault
 * JD-Core Version:    0.6.2
 */