package org.ksoap2.serialization;

import java.io.IOException;
import java.util.Date;
import org.kobjects.isodate.IsoDate;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

public class MarshalDate
  implements Marshal
{
  public static Class DATE_CLASS = new Date().getClass();

  public Object readInstance(XmlPullParser paramXmlPullParser, String paramString1, String paramString2, PropertyInfo paramPropertyInfo)
    throws IOException, XmlPullParserException
  {
    return IsoDate.stringToDate(paramXmlPullParser.nextText(), 3);
  }

  public void writeInstance(XmlSerializer paramXmlSerializer, Object paramObject)
    throws IOException
  {
    paramXmlSerializer.text(IsoDate.dateToString((Date)paramObject, 3));
  }

  public void register(SoapSerializationEnvelope paramSoapSerializationEnvelope)
  {
    paramSoapSerializationEnvelope.addMapping(paramSoapSerializationEnvelope.xsd, "dateTime", DATE_CLASS, this);
  }
}

/* Location:           dulux-signed.jar
 * Qualified Name:     org.ksoap2.serialization.MarshalDate
 * JD-Core Version:    0.6.2
 */