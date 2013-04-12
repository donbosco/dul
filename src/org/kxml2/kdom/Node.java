package org.kxml2.kdom;

import java.io.IOException;
import java.util.Vector;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

public class Node
{
  public static final int DOCUMENT = 0;
  public static final int ELEMENT = 2;
  public static final int TEXT = 4;
  public static final int CDSECT = 5;
  public static final int ENTITY_REF = 6;
  public static final int IGNORABLE_WHITESPACE = 7;
  public static final int PROCESSING_INSTRUCTION = 8;
  public static final int COMMENT = 9;
  public static final int DOCDECL = 10;
  protected Vector children;
  protected StringBuffer types;

  public void addChild(int paramInt1, int paramInt2, Object paramObject)
  {
    if (paramObject == null)
      throw new NullPointerException();
    if (this.children == null)
    {
      this.children = new Vector();
      this.types = new StringBuffer();
    }
    if (paramInt2 == 2)
    {
      if (!(paramObject instanceof Element))
        throw new RuntimeException("Element obj expected)");
      ((Element)paramObject).setParent(this);
    }
    else if (!(paramObject instanceof String))
    {
      throw new RuntimeException("String expected");
    }
    this.children.insertElementAt(paramObject, paramInt1);
    this.types.insert(paramInt1, (char)paramInt2);
  }

  public void addChild(int paramInt, Object paramObject)
  {
    addChild(getChildCount(), paramInt, paramObject);
  }

  public Element createElement(String paramString1, String paramString2)
  {
    Element localElement = new Element();
    localElement.namespace = (paramString1 == null ? "" : paramString1);
    localElement.name = paramString2;
    return localElement;
  }

  public Object getChild(int paramInt)
  {
    return this.children.elementAt(paramInt);
  }

  public int getChildCount()
  {
    return this.children == null ? 0 : this.children.size();
  }

  public Element getElement(int paramInt)
  {
    Object localObject = getChild(paramInt);
    return (localObject instanceof Element) ? (Element)localObject : null;
  }

  public Element getElement(String paramString1, String paramString2)
  {
    int i = indexOf(paramString1, paramString2, 0);
    int j = indexOf(paramString1, paramString2, i + 1);
    if ((i == -1) || (j != -1))
      throw new RuntimeException("Element {" + paramString1 + "}" + paramString2 + (i == -1 ? " not found in " : " more than once in ") + this);
    return getElement(i);
  }

  public String getText(int paramInt)
  {
    return isText(paramInt) ? (String)getChild(paramInt) : null;
  }

  public int getType(int paramInt)
  {
    return this.types.charAt(paramInt);
  }

  public int indexOf(String paramString1, String paramString2, int paramInt)
  {
    int i = getChildCount();
    for (int j = paramInt; j < i; j++)
    {
      Element localElement = getElement(j);
      if ((localElement != null) && (paramString2.equals(localElement.getName())) && ((paramString1 == null) || (paramString1.equals(localElement.getNamespace()))))
        return j;
    }
    return -1;
  }

  public boolean isText(int paramInt)
  {
    int i = getType(paramInt);
    return (i == 4) || (i == 7) || (i == 5);
  }

  public void parse(XmlPullParser paramXmlPullParser)
    throws IOException, XmlPullParserException
  {
    int i = 0;
    do
    {
      int j = paramXmlPullParser.getEventType();
      switch (j)
      {
      case 2:
        Element localElement = createElement(paramXmlPullParser.getNamespace(), paramXmlPullParser.getName());
        addChild(2, localElement);
        localElement.parse(paramXmlPullParser);
        break;
      case 1:
      case 3:
        i = 1;
        break;
      default:
        if (paramXmlPullParser.getText() != null)
          addChild(j == 6 ? 4 : j, paramXmlPullParser.getText());
        else if ((j == 6) && (paramXmlPullParser.getName() != null))
          addChild(6, paramXmlPullParser.getName());
        paramXmlPullParser.nextToken();
      }
    }
    while (i == 0);
  }

  public void removeChild(int paramInt)
  {
    this.children.removeElementAt(paramInt);
    int i = this.types.length() - 1;
    for (int j = paramInt; j < i; j++)
      this.types.setCharAt(j, this.types.charAt(j + 1));
    this.types.setLength(i);
  }

  public void write(XmlSerializer paramXmlSerializer)
    throws IOException
  {
    writeChildren(paramXmlSerializer);
    paramXmlSerializer.flush();
  }

  public void writeChildren(XmlSerializer paramXmlSerializer)
    throws IOException
  {
    if (this.children == null)
      return;
    int i = this.children.size();
    for (int j = 0; j < i; j++)
    {
      int k = getType(j);
      Object localObject = this.children.elementAt(j);
      switch (k)
      {
      case 2:
        ((Element)localObject).write(paramXmlSerializer);
        break;
      case 4:
        paramXmlSerializer.text((String)localObject);
        break;
      case 7:
        paramXmlSerializer.ignorableWhitespace((String)localObject);
        break;
      case 5:
        paramXmlSerializer.cdsect((String)localObject);
        break;
      case 9:
        paramXmlSerializer.comment((String)localObject);
        break;
      case 6:
        paramXmlSerializer.entityRef((String)localObject);
        break;
      case 8:
        paramXmlSerializer.processingInstruction((String)localObject);
        break;
      case 10:
        paramXmlSerializer.docdecl((String)localObject);
        break;
      case 3:
      default:
        throw new RuntimeException("Illegal type: " + k);
      }
    }
  }
}

/* Location:           dulux-signed.jar
 * Qualified Name:     org.kxml2.kdom.Node
 * JD-Core Version:    0.6.2
 */