package org.ksoap2.serialization;

import java.io.IOException;
import org.kobjects.base64.Base64;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

public class MarshalBase64
  implements Marshal
{
  public static Class BYTE_ARRAY_CLASS = new byte[0].getClass();

  public Object readInstance(XmlPullParser paramXmlPullParser, String paramString1, String paramString2, PropertyInfo paramPropertyInfo)
    throws IOException, XmlPullParserException
  {
    return Base64.decode(paramXmlPullParser.nextText());
  }

  public void writeInstance(XmlSerializer paramXmlSerializer, Object paramObject)
    throws IOException
  {
    paramXmlSerializer.text(Base64.encode((byte[])paramObject));
  }

  public void register(SoapSerializationEnvelope paramSoapSerializationEnvelope)
  {
    paramSoapSerializationEnvelope.addMapping(paramSoapSerializationEnvelope.xsd, "base64Binary", BYTE_ARRAY_CLASS, this);
    paramSoapSerializationEnvelope.addMapping("http://schemas.xmlsoap.org/soap/encoding/", "base64", BYTE_ARRAY_CLASS, this);
  }
}

/* Location:           dulux-signed.jar
 * Qualified Name:     org.ksoap2.serialization.MarshalBase64
 * JD-Core Version:    0.6.2
 */