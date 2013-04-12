package org.kxml2.kdom;

import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

public class Document extends Node
{
  protected int rootIndex = -1;
  String encoding;
  Boolean standalone;

  public String getEncoding()
  {
    return this.encoding;
  }

  public void setEncoding(String paramString)
  {
    this.encoding = paramString;
  }

  public void setStandalone(Boolean paramBoolean)
  {
    this.standalone = paramBoolean;
  }

  public Boolean getStandalone()
  {
    return this.standalone;
  }

  public String getName()
  {
    return "#document";
  }

  public void addChild(int paramInt1, int paramInt2, Object paramObject)
  {
    if (paramInt2 == 2)
      this.rootIndex = paramInt1;
    else if (this.rootIndex >= paramInt1)
      this.rootIndex += 1;
    super.addChild(paramInt1, paramInt2, paramObject);
  }

  public void parse(XmlPullParser paramXmlPullParser)
    throws IOException, XmlPullParserException
  {
    paramXmlPullParser.require(0, null, null);
    paramXmlPullParser.nextToken();
    this.encoding = paramXmlPullParser.getInputEncoding();
    this.standalone = ((Boolean)paramXmlPullParser.getProperty("http://xmlpull.org/v1/doc/properties.html#xmldecl-standalone"));
    super.parse(paramXmlPullParser);
    if (paramXmlPullParser.getEventType() != 1)
      throw new RuntimeException("Document end expected!");
  }

  public void removeChild(int paramInt)
  {
    if (paramInt == this.rootIndex)
      this.rootIndex = -1;
    else if (paramInt < this.rootIndex)
      this.rootIndex -= 1;
    super.removeChild(paramInt);
  }

  public Element getRootElement()
  {
    if (this.rootIndex == -1)
      throw new RuntimeException("Document has no root element!");
    return (Element)getChild(this.rootIndex);
  }

  public void write(XmlSerializer paramXmlSerializer)
    throws IOException
  {
    paramXmlSerializer.startDocument(this.encoding, this.standalone);
    writeChildren(paramXmlSerializer);
    paramXmlSerializer.endDocument();
  }
}

/* Location:           dulux-signed.jar
 * Qualified Name:     org.kxml2.kdom.Document
 * JD-Core Version:    0.6.2
 */