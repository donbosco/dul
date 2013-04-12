package org.ksoap2.serialization;

import java.io.IOException;
import java.math.BigDecimal;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

public class MarshalFloat
  implements Marshal
{
  public Object readInstance(XmlPullParser paramXmlPullParser, String paramString1, String paramString2, PropertyInfo paramPropertyInfo)
    throws IOException, XmlPullParserException
  {
    String str = paramXmlPullParser.nextText();
    Object localObject;
    if (paramString2.equals("float"))
      localObject = new Float(str);
    else if (paramString2.equals("double"))
      localObject = new Double(str);
    else if (paramString2.equals("decimal"))
      localObject = new BigDecimal(str);
    else
      throw new RuntimeException("float, double, or decimal expected");
    return localObject;
  }

  public void writeInstance(XmlSerializer paramXmlSerializer, Object paramObject)
    throws IOException
  {
    paramXmlSerializer.text(paramObject.toString());
  }

  public void register(SoapSerializationEnvelope paramSoapSerializationEnvelope)
  {
    paramSoapSerializationEnvelope.addMapping(paramSoapSerializationEnvelope.xsd, "float", Float.class, this);
    paramSoapSerializationEnvelope.addMapping(paramSoapSerializationEnvelope.xsd, "double", Double.class, this);
    paramSoapSerializationEnvelope.addMapping(paramSoapSerializationEnvelope.xsd, "decimal", BigDecimal.class, this);
  }
}

/* Location:           dulux-signed.jar
 * Qualified Name:     org.ksoap2.serialization.MarshalFloat
 * JD-Core Version:    0.6.2
 */