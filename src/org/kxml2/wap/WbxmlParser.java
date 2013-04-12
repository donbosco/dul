package org.kxml2.wap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.Vector;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class WbxmlParser
  implements XmlPullParser
{
  public static final int WAP_EXTENSION = 64;
  private static final String UNEXPECTED_EOF = "Unexpected EOF";
  private static final String ILLEGAL_TYPE = "Wrong event type";
  private InputStream in;
  private int TAG_TABLE = 0;
  private int ATTR_START_TABLE = 1;
  private int ATTR_VALUE_TABLE = 2;
  private String[] attrStartTable;
  private String[] attrValueTable;
  private String[] tagTable;
  private byte[] stringTable;
  private Hashtable cacheStringTable = null;
  private boolean processNsp;
  private int depth;
  private String[] elementStack = new String[16];
  private String[] nspStack = new String[8];
  private int[] nspCounts = new int[4];
  private int attributeCount;
  private String[] attributes = new String[16];
  private int nextId = -2;
  private Vector tables = new Vector();
  int version;
  int publicIdentifierId;
  int charSet;
  private String prefix;
  private String namespace;
  private String name;
  private String text;
  private Object wapExtensionData;
  private int wapExtensionCode;
  private int type;
  private int codePage;
  private boolean degenerated;
  private boolean isWhitespace;
  private String encoding = null;

  public boolean getFeature(String paramString)
  {
    if ("http://xmlpull.org/v1/doc/features.html#process-namespaces".equals(paramString))
      return this.processNsp;
    return false;
  }

  public String getInputEncoding()
  {
    return this.encoding;
  }

  public void defineEntityReplacementText(String paramString1, String paramString2)
    throws XmlPullParserException
  {
  }

  public Object getProperty(String paramString)
  {
    return null;
  }

  public int getNamespaceCount(int paramInt)
  {
    if (paramInt > this.depth)
      throw new IndexOutOfBoundsException();
    return this.nspCounts[paramInt];
  }

  public String getNamespacePrefix(int paramInt)
  {
    return this.nspStack[(paramInt << 1)];
  }

  public String getNamespaceUri(int paramInt)
  {
    return this.nspStack[((paramInt << 1) + 1)];
  }

  public String getNamespace(String paramString)
  {
    if ("xml".equals(paramString))
      return "http://www.w3.org/XML/1998/namespace";
    if ("xmlns".equals(paramString))
      return "http://www.w3.org/2000/xmlns/";
    for (int i = (getNamespaceCount(this.depth) << 1) - 2; i >= 0; i -= 2)
      if (paramString == null)
      {
        if (this.nspStack[i] == null)
          return this.nspStack[(i + 1)];
      }
      else if (paramString.equals(this.nspStack[i]))
        return this.nspStack[(i + 1)];
    return null;
  }

  public int getDepth()
  {
    return this.depth;
  }

  public String getPositionDescription()
  {
    StringBuffer localStringBuffer = new StringBuffer(this.type < XmlPullParser.TYPES.length ? XmlPullParser.TYPES[this.type] : "unknown");
    localStringBuffer.append(' ');
    if ((this.type == 2) || (this.type == 3))
    {
      if (this.degenerated)
        localStringBuffer.append("(empty) ");
      localStringBuffer.append('<');
      if (this.type == 3)
        localStringBuffer.append('/');
      if (this.prefix != null)
        localStringBuffer.append("{" + this.namespace + "}" + this.prefix + ":");
      localStringBuffer.append(this.name);
      int i = this.attributeCount << 2;
      for (int j = 0; j < i; j += 4)
      {
        localStringBuffer.append(' ');
        if (this.attributes[(j + 1)] != null)
          localStringBuffer.append("{" + this.attributes[j] + "}" + this.attributes[(j + 1)] + ":");
        localStringBuffer.append(this.attributes[(j + 2)] + "='" + this.attributes[(j + 3)] + "'");
      }
      localStringBuffer.append('>');
    }
    else if (this.type != 7)
    {
      if (this.type != 4)
      {
        localStringBuffer.append(getText());
      }
      else if (this.isWhitespace)
      {
        localStringBuffer.append("(whitespace)");
      }
      else
      {
        String str = getText();
        if (str.length() > 16)
          str = str.substring(0, 16) + "...";
        localStringBuffer.append(str);
      }
    }
    return localStringBuffer.toString();
  }

  public int getLineNumber()
  {
    return -1;
  }

  public int getColumnNumber()
  {
    return -1;
  }

  public boolean isWhitespace()
    throws XmlPullParserException
  {
    if ((this.type != 4) && (this.type != 7) && (this.type != 5))
      exception("Wrong event type");
    return this.isWhitespace;
  }

  public String getText()
  {
    return this.text;
  }

  public char[] getTextCharacters(int[] paramArrayOfInt)
  {
    if (this.type >= 4)
    {
      paramArrayOfInt[0] = 0;
      paramArrayOfInt[1] = this.text.length();
      char[] arrayOfChar = new char[this.text.length()];
      this.text.getChars(0, this.text.length(), arrayOfChar, 0);
      return arrayOfChar;
    }
    paramArrayOfInt[0] = -1;
    paramArrayOfInt[1] = -1;
    return null;
  }

  public String getNamespace()
  {
    return this.namespace;
  }

  public String getName()
  {
    return this.name;
  }

  public String getPrefix()
  {
    return this.prefix;
  }

  public boolean isEmptyElementTag()
    throws XmlPullParserException
  {
    if (this.type != 2)
      exception("Wrong event type");
    return this.degenerated;
  }

  public int getAttributeCount()
  {
    return this.attributeCount;
  }

  public String getAttributeType(int paramInt)
  {
    return "CDATA";
  }

  public boolean isAttributeDefault(int paramInt)
  {
    return false;
  }

  public String getAttributeNamespace(int paramInt)
  {
    if (paramInt >= this.attributeCount)
      throw new IndexOutOfBoundsException();
    return this.attributes[(paramInt << 2)];
  }

  public String getAttributeName(int paramInt)
  {
    if (paramInt >= this.attributeCount)
      throw new IndexOutOfBoundsException();
    return this.attributes[((paramInt << 2) + 2)];
  }

  public String getAttributePrefix(int paramInt)
  {
    if (paramInt >= this.attributeCount)
      throw new IndexOutOfBoundsException();
    return this.attributes[((paramInt << 2) + 1)];
  }

  public String getAttributeValue(int paramInt)
  {
    if (paramInt >= this.attributeCount)
      throw new IndexOutOfBoundsException();
    return this.attributes[((paramInt << 2) + 3)];
  }

  public String getAttributeValue(String paramString1, String paramString2)
  {
    for (int i = (this.attributeCount << 2) - 4; i >= 0; i -= 4)
      if ((this.attributes[(i + 2)].equals(paramString2)) && ((paramString1 == null) || (this.attributes[i].equals(paramString1))))
        return this.attributes[(i + 3)];
    return null;
  }

  public int getEventType()
    throws XmlPullParserException
  {
    return this.type;
  }

  public int next()
    throws XmlPullParserException, IOException
  {
    this.isWhitespace = true;
    int i = 9999;
    while (true)
    {
      String str = this.text;
      nextImpl();
      if (this.type < i)
        i = this.type;
      if (i <= 5)
      {
        if (i < 4)
          break;
        if (str != null)
          this.text = (str + this.text);
        switch (peekId())
        {
        case 2:
        case 3:
        case 4:
        case 68:
        case 132:
        case 196:
        }
      }
    }
    this.type = i;
    if (this.type > 4)
      this.type = 4;
    return this.type;
  }

  public int nextToken()
    throws XmlPullParserException, IOException
  {
    this.isWhitespace = true;
    nextImpl();
    return this.type;
  }

  public int nextTag()
    throws XmlPullParserException, IOException
  {
    next();
    if ((this.type == 4) && (this.isWhitespace))
      next();
    if ((this.type != 3) && (this.type != 2))
      exception("unexpected type");
    return this.type;
  }

  public String nextText()
    throws XmlPullParserException, IOException
  {
    if (this.type != 2)
      exception("precondition: START_TAG");
    next();
    String str;
    if (this.type == 4)
    {
      str = getText();
      next();
    }
    else
    {
      str = "";
    }
    if (this.type != 3)
      exception("END_TAG expected");
    return str;
  }

  public void require(int paramInt, String paramString1, String paramString2)
    throws XmlPullParserException, IOException
  {
    if ((paramInt != this.type) || ((paramString1 != null) && (!paramString1.equals(getNamespace()))) || ((paramString2 != null) && (!paramString2.equals(getName()))))
      exception("expected: " + XmlPullParser.TYPES[paramInt] + " {" + paramString1 + "}" + paramString2);
  }

  public void setInput(Reader paramReader)
    throws XmlPullParserException
  {
    exception("InputStream required");
  }

  public void setInput(InputStream paramInputStream, String paramString)
    throws XmlPullParserException
  {
    this.in = paramInputStream;
    try
    {
      this.version = readByte();
      this.publicIdentifierId = readInt();
      if (this.publicIdentifierId == 0)
        readInt();
      int i = readInt();
      if (null == paramString)
        switch (i)
        {
        case 4:
          this.encoding = "ISO-8859-1";
          break;
        case 106:
          this.encoding = "UTF-8";
          break;
        default:
          throw new UnsupportedEncodingException("" + i);
        }
      else
        this.encoding = paramString;
      int j = readInt();
      this.stringTable = new byte[j];
      int k = 0;
      while (k < j)
      {
        int m = paramInputStream.read(this.stringTable, k, j - k);
        if (m <= 0)
          break;
        k += m;
      }
      selectPage(0, true);
      selectPage(0, false);
    }
    catch (IOException localIOException)
    {
      exception("Illegal input format");
    }
  }

  public void setFeature(String paramString, boolean paramBoolean)
    throws XmlPullParserException
  {
    if ("http://xmlpull.org/v1/doc/features.html#process-namespaces".equals(paramString))
      this.processNsp = paramBoolean;
    else
      exception("unsupported feature: " + paramString);
  }

  public void setProperty(String paramString, Object paramObject)
    throws XmlPullParserException
  {
    throw new XmlPullParserException("unsupported property: " + paramString);
  }

  private final boolean adjustNsp()
    throws XmlPullParserException
  {
    boolean bool = false;
    String str1;
    int j;
    String str2;
    for (int i = 0; i < this.attributeCount << 2; i += 4)
    {
      str1 = this.attributes[(i + 2)];
      j = str1.indexOf(':');
      if (j != -1)
      {
        str2 = str1.substring(0, j);
        str1 = str1.substring(j + 1);
      }
      else
      {
        if (!str1.equals("xmlns"))
          continue;
        str2 = str1;
        str1 = null;
      }
      if (!str2.equals("xmlns"))
      {
        bool = true;
      }
      else
      {
        int tmp95_92 = this.depth;
        int[] tmp95_88 = this.nspCounts;
        int tmp97_96 = tmp95_88[tmp95_92];
        tmp95_88[tmp95_92] = (tmp97_96 + 1);
        int k = tmp97_96 << 1;
        this.nspStack = ensureCapacity(this.nspStack, k + 2);
        this.nspStack[k] = str1;
        this.nspStack[(k + 1)] = this.attributes[(i + 3)];
        if ((str1 != null) && (this.attributes[(i + 3)].equals("")))
          exception("illegal empty namespace");
        System.arraycopy(this.attributes, i + 4, this.attributes, i, (--this.attributeCount << 2) - i);
        i -= 4;
      }
    }
    int i;
	if (bool)
      for (i = (this.attributeCount << 2) - 4; i >= 0; i -= 4)
      {
        str1 = this.attributes[(i + 2)];
        j = str1.indexOf(':');
        if (j == 0)
          throw new RuntimeException("illegal attribute name: " + str1 + " at " + this);
        if (j != -1)
        {
          str2 = str1.substring(0, j);
          str1 = str1.substring(j + 1);
          String str3 = getNamespace(str2);
          if (str3 == null)
            throw new RuntimeException("Undefined Prefix: " + str2 + " in " + this);
          this.attributes[i] = str3;
          this.attributes[(i + 1)] = str2;
          this.attributes[(i + 2)] = str1;
          for (int m = (this.attributeCount << 2) - 4; m > i; m -= 4)
            if ((str1.equals(this.attributes[(m + 2)])) && (str3.equals(this.attributes[m])))
              exception("Duplicate Attribute: {" + str3 + "}" + str1);
        }
      }
    i = this.name.indexOf(':');
    if (i == 0)
    {
      exception("illegal tag name: " + this.name);
    }
    else if (i != -1)
    {
      this.prefix = this.name.substring(0, i);
      this.name = this.name.substring(i + 1);
    }
    this.namespace = getNamespace(this.prefix);
    if (this.namespace == null)
    {
      if (this.prefix != null)
        exception("undefined prefix: " + this.prefix);
      this.namespace = "";
    }
    return bool;
  }

  private final void setTable(int paramInt1, int paramInt2, String[] paramArrayOfString)
  {
    if (this.stringTable != null)
      throw new RuntimeException("setXxxTable must be called before setInput!");
    while (this.tables.size() < 3 * paramInt1 + 3)
      this.tables.addElement(null);
    this.tables.setElementAt(paramArrayOfString, paramInt1 * 3 + paramInt2);
  }

  private final void exception(String paramString)
    throws XmlPullParserException
  {
    throw new XmlPullParserException(paramString, this, null);
  }

  private void selectPage(int paramInt, boolean paramBoolean)
    throws XmlPullParserException
  {
    if ((this.tables.size() == 0) && (paramInt == 0))
      return;
    if (paramInt * 3 > this.tables.size())
      exception("Code Page " + paramInt + " undefined!");
    if (paramBoolean)
    {
      this.tagTable = ((String[])this.tables.elementAt(paramInt * 3 + this.TAG_TABLE));
    }
    else
    {
      this.attrStartTable = ((String[])this.tables.elementAt(paramInt * 3 + this.ATTR_START_TABLE));
      this.attrValueTable = ((String[])this.tables.elementAt(paramInt * 3 + this.ATTR_VALUE_TABLE));
    }
  }

  private final void nextImpl()
    throws IOException, XmlPullParserException
  {
    if (this.type == 3)
      this.depth -= 1;
    if (this.degenerated)
    {
      this.type = 3;
      this.degenerated = false;
      return;
    }
    this.text = null;
    this.prefix = null;
    this.name = null;
    int i ;
    for ( i = peekId(); i == 0; i = peekId())
    {
      this.nextId = -2;
      selectPage(readByte(), true);
    }
    this.nextId = -2;
    int j;
	switch (i)
    {
    case -1:
      this.type = 1;
      break;
    case 1:
      j = this.depth - 1 << 2;
      this.type = 3;
      this.namespace = this.elementStack[j];
      this.prefix = this.elementStack[(j + 1)];
      this.name = this.elementStack[(j + 2)];
      break;
    case 2:
      this.type = 6;
      j = (char)readInt();
      this.text = ("" + j);
      this.name = ("#" + j);
      break;
    case 3:
      this.type = 4;
      this.text = readStrI();
      break;
    case 64:
    case 65:
    case 66:
    case 128:
    case 129:
    case 130:
    case 192:
    case 193:
    case 194:
    case 195:
      parseWapExtension(i);
      break;
    case 67:
      throw new RuntimeException("PI curr. not supp.");
    case 131:
      this.type = 4;
      this.text = readStrT();
      break;
    default:
      parseElement(i);
    }
  }

  public void parseWapExtension(int paramInt)
    throws IOException, XmlPullParserException
  {
    this.type = 64;
    this.wapExtensionCode = paramInt;
    switch (paramInt)
    {
    case 64:
    case 65:
    case 66:
      this.wapExtensionData = readStrI();
      break;
    case 128:
    case 129:
    case 130:
      this.wapExtensionData = new Integer(readInt());
      break;
    case 192:
    case 193:
    case 194:
      break;
    case 195:
      int i = readInt();
      byte[] arrayOfByte = new byte[i];
      for (int j = 0; j < i; j++)
        arrayOfByte[j] = ((byte)readByte());
      this.wapExtensionData = arrayOfByte;
      break;
    default:
      exception("illegal id: " + paramInt);
    }
  }

  public void readAttr()
    throws IOException, XmlPullParserException
  {
    int i = readByte();
    int j = 0;
    while (i != 1)
    {
      while (i == 0)
      {
        selectPage(readByte(), false);
        i = readByte();
      }
      String str = resolveId(this.attrStartTable, i);
      int k = str.indexOf('=');
      StringBuffer localStringBuffer;
      if (k == -1)
      {
        localStringBuffer = new StringBuffer();
      }
      else
      {
        localStringBuffer = new StringBuffer(str.substring(k + 1));
        str = str.substring(0, k);
      }
      for (i = readByte(); (i > 128) || (i == 0) || (i == 2) || (i == 3) || (i == 131) || ((i >= 64) && (i <= 66)) || ((i >= 128) && (i <= 130)); i = readByte())
        switch (i)
        {
        case 0:
          selectPage(readByte(), false);
          break;
        case 2:
          localStringBuffer.append((char)readInt());
          break;
        case 3:
          localStringBuffer.append(readStrI());
          break;
        case 64:
        case 65:
        case 66:
        case 128:
        case 129:
        case 130:
        case 192:
        case 193:
        case 194:
        case 195:
          throw new RuntimeException("wap extension in attr not supported yet");
        case 131:
          localStringBuffer.append(readStrT());
          break;
        default:
          localStringBuffer.append(resolveId(this.attrValueTable, i));
        }
      this.attributes = ensureCapacity(this.attributes, j + 4);
      this.attributes[(j++)] = "";
      this.attributes[(j++)] = null;
      this.attributes[(j++)] = str;
      this.attributes[(j++)] = localStringBuffer.toString();
      this.attributeCount += 1;
    }
  }

  private int peekId()
    throws IOException
  {
    if (this.nextId == -2)
      this.nextId = this.in.read();
    return this.nextId;
  }

  String resolveId(String[] paramArrayOfString, int paramInt)
    throws IOException
  {
    int i = (paramInt & 0x7F) - 5;
    if (i == -1)
      return readStrT();
    if ((i < 0) || (paramArrayOfString == null) || (i >= paramArrayOfString.length) || (paramArrayOfString[i] == null))
      throw new IOException("id " + paramInt + " undef.");
    return paramArrayOfString[i];
  }

  void parseElement(int paramInt)
    throws IOException, XmlPullParserException
  {
    this.type = 2;
    this.name = resolveId(this.tagTable, paramInt & 0x3F);
    this.attributeCount = 0;
    if ((paramInt & 0x80) != 0)
      readAttr();
    this.degenerated = ((paramInt & 0x40) == 0);
    int i = this.depth++ << 2;
    this.elementStack = ensureCapacity(this.elementStack, i + 4);
    this.elementStack[(i + 3)] = this.name;
    if (this.depth >= this.nspCounts.length)
    {
      int[] arrayOfInt = new int[this.depth + 4];
      System.arraycopy(this.nspCounts, 0, arrayOfInt, 0, this.nspCounts.length);
      this.nspCounts = arrayOfInt;
    }
    this.nspCounts[this.depth] = this.nspCounts[(this.depth - 1)];
    for (int j = this.attributeCount - 1; j > 0; j--)
      for (int k = 0; k < j; k++)
        if (getAttributeName(j).equals(getAttributeName(k)))
          exception("Duplicate Attribute: " + getAttributeName(j));
    if (this.processNsp)
      adjustNsp();
    else
      this.namespace = "";
    this.elementStack[i] = this.namespace;
    this.elementStack[(i + 1)] = this.prefix;
    this.elementStack[(i + 2)] = this.name;
  }

  private final String[] ensureCapacity(String[] paramArrayOfString, int paramInt)
  {
    if (paramArrayOfString.length >= paramInt)
      return paramArrayOfString;
    String[] arrayOfString = new String[paramInt + 16];
    System.arraycopy(paramArrayOfString, 0, arrayOfString, 0, paramArrayOfString.length);
    return arrayOfString;
  }

  int readByte()
    throws IOException
  {
    int i = this.in.read();
    if (i == -1)
      throw new IOException("Unexpected EOF");
    return i;
  }

  int readInt()
    throws IOException
  {
    int i = 0;
    int j;
    do
    {
      j = readByte();
      i = i << 7 | j & 0x7F;
    }
    while ((j & 0x80) != 0);
    return i;
  }

  String readStrI()
    throws IOException
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    boolean bool = true;
    while (true)
    {
      int i = this.in.read();
      if (i == 0)
        break;
      if (i == -1)
        throw new IOException("Unexpected EOF");
      if (i > 32)
        bool = false;
      localByteArrayOutputStream.write(i);
    }
    this.isWhitespace = bool;
    String str = new String(localByteArrayOutputStream.toByteArray(), this.encoding);
    localByteArrayOutputStream.close();
    return str;
  }

  String readStrT()
    throws IOException
  {
    int i = readInt();
    if (this.cacheStringTable == null)
      this.cacheStringTable = new Hashtable();
    String str = (String)this.cacheStringTable.get(new Integer(i));
    if (str == null)
    {
      for (int j = i; (j < this.stringTable.length) && (this.stringTable[j] != 0); j++)
    	  str = new String(this.stringTable, i, j - i, this.encoding);
      this.cacheStringTable.put(new Integer(i), str);
    }
    return str;
  }

  public void setTagTable(int paramInt, String[] paramArrayOfString)
  {
    setTable(paramInt, this.TAG_TABLE, paramArrayOfString);
  }

  public void setAttrStartTable(int paramInt, String[] paramArrayOfString)
  {
    setTable(paramInt, this.ATTR_START_TABLE, paramArrayOfString);
  }

  public void setAttrValueTable(int paramInt, String[] paramArrayOfString)
  {
    setTable(paramInt, this.ATTR_VALUE_TABLE, paramArrayOfString);
  }
}

/* Location:           dulux-signed.jar
 * Qualified Name:     org.kxml2.wap.WbxmlParser
 * JD-Core Version:    0.6.2
 */