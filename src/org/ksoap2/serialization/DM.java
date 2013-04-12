package org.ksoap2.serialization;

import java.io.IOException;
import org.ksoap2.SoapEnvelope;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

class DM
  implements Marshal
{
  public Object readInstance(XmlPullParser paramXmlPullParser, String paramString1, String paramString2, PropertyInfo paramPropertyInfo)
    throws IOException, XmlPullParserException
  {
    String str = paramXmlPullParser.nextText();
    switch (paramString2.charAt(0))
    {
    case 's':
      return str;
    case 'i':
      return new Integer(Integer.parseInt(str));
    case 'l':
      return new Long(Long.parseLong(str));
    case 'b':
      return new Boolean(SoapEnvelope.stringToBoolean(str));
    }
    throw new RuntimeException();
  }

  public void writeInstance(XmlSerializer paramXmlSerializer, Object paramObject)
    throws IOException
  {
    paramXmlSerializer.text(paramObject.toString());
  }

  public void register(SoapSerializationEnvelope paramSoapSerializationEnvelope)
  {
    paramSoapSerializationEnvelope.addMapping(paramSoapSerializationEnvelope.xsd, "int", PropertyInfo.INTEGER_CLASS, this);
    paramSoapSerializationEnvelope.addMapping(paramSoapSerializationEnvelope.xsd, "long", PropertyInfo.LONG_CLASS, this);
    paramSoapSerializationEnvelope.addMapping(paramSoapSerializationEnvelope.xsd, "string", PropertyInfo.STRING_CLASS, this);
    paramSoapSerializationEnvelope.addMapping(paramSoapSerializationEnvelope.xsd, "boolean", PropertyInfo.BOOLEAN_CLASS, this);
  }
}

/* Location:           dulux-signed.jar
 * Qualified Name:     org.ksoap2.serialization.DM
 * JD-Core Version:    0.6.2
 */