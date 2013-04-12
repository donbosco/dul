package org.kxml2.kdom;

import java.io.IOException;
import java.util.Vector;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

public class Element extends Node
{
  protected String namespace;
  protected String name;
  protected Vector attributes;
  protected Node parent;
  protected Vector prefixes;

  public void init()
  {
  }

  public void clear()
  {
    this.attributes = null;
    this.children = null;
  }

  public Element createElement(String paramString1, String paramString2)
  {
    return this.parent == null ? super.createElement(paramString1, paramString2) : this.parent.createElement(paramString1, paramString2);
  }

  public int getAttributeCount()
  {
    return this.attributes == null ? 0 : this.attributes.size();
  }

  public String getAttributeNamespace(int paramInt)
  {
    return ((String[])(String[])this.attributes.elementAt(paramInt))[0];
  }

  public String getAttributeName(int paramInt)
  {
    return ((String[])(String[])this.attributes.elementAt(paramInt))[1];
  }

  public String getAttributeValue(int paramInt)
  {
    return ((String[])(String[])this.attributes.elementAt(paramInt))[2];
  }

  public String getAttributeValue(String paramString1, String paramString2)
  {
    for (int i = 0; i < getAttributeCount(); i++)
      if ((paramString2.equals(getAttributeName(i))) && ((paramString1 == null) || (paramString1.equals(getAttributeNamespace(i)))))
        return getAttributeValue(i);
    return null;
  }

  public Node getRoot()
  {
	  Element localElement;
    for (localElement = this; localElement.parent != null; localElement = (Element)localElement.parent)
      if (!(localElement.parent instanceof Element))
        return localElement.parent;
    return localElement;
  }

  public String getName()
  {
    return this.name;
  }

  public String getNamespace()
  {
    return this.namespace;
  }

  public String getNamespaceUri(String paramString)
  {
    int i = getNamespaceCount();
    for (int j = 0; j < i; j++)
      if ((paramString == getNamespacePrefix(j)) || ((paramString != null) && (paramString.equals(getNamespacePrefix(j)))))
        return getNamespaceUri(j);
    return (this.parent instanceof Element) ? ((Element)this.parent).getNamespaceUri(paramString) : null;
  }

  public int getNamespaceCount()
  {
    return this.prefixes == null ? 0 : this.prefixes.size();
  }

  public String getNamespacePrefix(int paramInt)
  {
    return ((String[])(String[])this.prefixes.elementAt(paramInt))[0];
  }

  public String getNamespaceUri(int paramInt)
  {
    return ((String[])(String[])this.prefixes.elementAt(paramInt))[1];
  }

  public Node getParent()
  {
    return this.parent;
  }

  public void parse(XmlPullParser paramXmlPullParser)
    throws IOException, XmlPullParserException
  {
    for (int i = paramXmlPullParser.getNamespaceCount(paramXmlPullParser.getDepth() - 1); i < paramXmlPullParser.getNamespaceCount(paramXmlPullParser.getDepth()); i++)
      setPrefix(paramXmlPullParser.getNamespacePrefix(i), paramXmlPullParser.getNamespaceUri(i));
    for (int i = 0; i < paramXmlPullParser.getAttributeCount(); i++)
      setAttribute(paramXmlPullParser.getAttributeNamespace(i), paramXmlPullParser.getAttributeName(i), paramXmlPullParser.getAttributeValue(i));
    init();
    if (paramXmlPullParser.isEmptyElementTag())
    {
      paramXmlPullParser.nextToken();
    }
    else
    {
      paramXmlPullParser.nextToken();
      super.parse(paramXmlPullParser);
      if (getChildCount() == 0)
        addChild(7, "");
    }
    paramXmlPullParser.require(3, getNamespace(), getName());
    paramXmlPullParser.nextToken();
  }

  public void setAttribute(String paramString1, String paramString2, String paramString3)
  {
    if (this.attributes == null)
      this.attributes = new Vector();
    if (paramString1 == null)
      paramString1 = "";
    for (int i = this.attributes.size() - 1; i >= 0; i--)
    {
      String[] arrayOfString = (String[])this.attributes.elementAt(i);
      if ((arrayOfString[0].equals(paramString1)) && (arrayOfString[1].equals(paramString2)))
      {
        if (paramString3 == null)
          this.attributes.removeElementAt(i);
        else
          arrayOfString[2] = paramString3;
        return;
      }
    }
    this.attributes.addElement(new String[] { paramString1, paramString2, paramString3 });
  }

  public void setPrefix(String paramString1, String paramString2)
  {
    if (this.prefixes == null)
      this.prefixes = new Vector();
    this.prefixes.addElement(new String[] { paramString1, paramString2 });
  }

  public void setName(String paramString)
  {
    this.name = paramString;
  }

  public void setNamespace(String paramString)
  {
    if (paramString == null)
      throw new NullPointerException("Use \"\" for empty namespace");
    this.namespace = paramString;
  }

  protected void setParent(Node paramNode)
  {
    this.parent = paramNode;
  }

  public void write(XmlSerializer paramXmlSerializer)
    throws IOException
  {
    int i;
	if (this.prefixes != null)
      for (i = 0; i < this.prefixes.size(); i++)
        paramXmlSerializer.setPrefix(getNamespacePrefix(i), getNamespaceUri(i));
    paramXmlSerializer.startTag(getNamespace(), getName());
     i = getAttributeCount();
    for (int j = 0; j < i; j++)
      paramXmlSerializer.attribute(getAttributeNamespace(j), getAttributeName(j), getAttributeValue(j));
    writeChildren(paramXmlSerializer);
    paramXmlSerializer.endTag(getNamespace(), getName());
  }
}

/* Location:           dulux-signed.jar
 * Qualified Name:     org.kxml2.kdom.Element
 * JD-Core Version:    0.6.2
 */