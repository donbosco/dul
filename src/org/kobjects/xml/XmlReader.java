package org.kobjects.xml;

import java.io.IOException;
import java.io.Reader;
import java.util.Hashtable;

public class XmlReader
{
  public static final int START_DOCUMENT = 0;
  public static final int END_DOCUMENT = 1;
  public static final int START_TAG = 2;
  public static final int END_TAG = 3;
  public static final int TEXT = 4;
  static final int CDSECT = 5;
  static final int ENTITY_REF = 6;
  private static final String UNEXPECTED_EOF = "Unexpected EOF";
  private static final int LEGACY = 999;
  public boolean relaxed;
  private Hashtable entityMap;
  private int depth;
  private String[] elementStack = new String[4];
  private Reader reader;
  private char[] srcBuf = new char[Runtime.getRuntime().freeMemory() >= 1048576L ? ' ' : ''];
  private int srcPos;
  private int srcCount;
  private boolean eof;
  private int line;
  private int column;
  private int peek0;
  private int peek1;
  private char[] txtBuf = new char[''];
  private int txtPos;
  private int type;
  private String text;
  private boolean isWhitespace;
  private String name;
  private boolean degenerated;
  private int attributeCount;
  private String[] attributes = new String[16];
  private String[] TYPES = { "Start Document", "End Document", "Start Tag", "End Tag", "Text" };

  private final int read()
    throws IOException
  {
    int i = this.peek0;
    this.peek0 = this.peek1;
    if (this.peek0 == -1)
    {
      this.eof = true;
      return i;
    }
    if ((i == 10) || (i == 13))
    {
      this.line += 1;
      this.column = 0;
      if ((i == 13) && (this.peek0 == 10))
        this.peek0 = 0;
    }
    this.column += 1;
    if (this.srcPos >= this.srcCount)
    {
      this.srcCount = this.reader.read(this.srcBuf, 0, this.srcBuf.length);
      if (this.srcCount <= 0)
      {
        this.peek1 = -1;
        return i;
      }
      this.srcPos = 0;
    }
    this.peek1 = this.srcBuf[(this.srcPos++)];
    return i;
  }

  private final void exception(String paramString)
    throws IOException
  {
    throw new IOException(paramString + " pos: " + getPositionDescription());
  }

  private final void push(int paramInt)
  {
    if (paramInt == 0)
      return;
    if (this.txtPos == this.txtBuf.length)
    {
      char[] arrayOfChar = new char[this.txtPos * 4 / 3 + 4];
      System.arraycopy(this.txtBuf, 0, arrayOfChar, 0, this.txtPos);
      this.txtBuf = arrayOfChar;
    }
    this.txtBuf[(this.txtPos++)] = ((char)paramInt);
  }

  private final void read(char paramChar)
    throws IOException
  {
    if (read() != paramChar)
      if (this.relaxed)
      {
        if (paramChar <= ' ')
        {
          skip();
          read();
        }
      }
      else
        exception("expected: '" + paramChar + "'");
  }

  private final void skip()
    throws IOException
  {
    while ((!this.eof) && (this.peek0 <= 32))
      read();
  }

  private final String pop(int paramInt)
  {
    String str = new String(this.txtBuf, paramInt, this.txtPos - paramInt);
    this.txtPos = paramInt;
    return str;
  }

  private final String readName()
    throws IOException
  {
    int i = this.txtPos;
    int j = this.peek0;
    if (((j < 97) || (j > 122)) && ((j < 65) || (j > 90)) && (j != 95) && (j != 58) && (!this.relaxed))
      exception("name expected");
    do
    {
      push(read());
      j = this.peek0;
    }
    while (((j >= 97) && (j <= 122)) || ((j >= 65) && (j <= 90)) || ((j >= 48) && (j <= 57)) || (j == 95) || (j == 45) || (j == 58) || (j == 46));
    return pop(i);
  }

  private final void parseLegacy(boolean paramBoolean)
    throws IOException
  {
    String str = "";
    read();
    int j = read();
    int i;
    if (j == 63)
    {
      i = 63;
    }
    else if (j == 33)
    {
      if (this.peek0 == 45)
      {
        str = "--";
        i = 45;
      }
      else
      {
        str = "DOCTYPE";
        i = -1;
      }
    }
    else
    {
      if (j != 91)
        exception("cantreachme: " + j);
      str = "CDATA[";
      i = 93;
    }
    for (int k = 0; k < str.length(); k++)
      read(str.charAt(k));
    if (i == -1)
    {
      parseDoctype();
    }
    else
    {
      while (true)
      {
        if (this.eof)
          exception("Unexpected EOF");
        j = read();
        if (paramBoolean)
          push(j);
        if (((i == 63) || (j == i)) && (this.peek0 == i) && (this.peek1 == 62))
          break;
      }
      read();
      read();
      if ((paramBoolean) && (i != 63))
        pop(this.txtPos - 1);
    }
  }

  private final void parseDoctype()
    throws IOException
  {
    int i = 1;
    while (true)
    {
      int j = read();
      switch (j)
      {
      case -1:
        exception("Unexpected EOF");
      case 60:
        i++;
        break;
      case 62:
        i--;
        if (i == 0)
          return;
        break;
      }
    }
  }

  private final void parseEndTag()
    throws IOException
  {
    read();
    read();
    this.name = readName();
    if ((this.depth == 0) && (!this.relaxed))
      exception("element stack empty");
    if (this.name.equals(this.elementStack[(this.depth - 1)]))
      this.depth -= 1;
    else if (!this.relaxed)
      exception("expected: " + this.elementStack[this.depth]);
    skip();
    read('>');
  }

  private final int peekType()
  {
    switch (this.peek0)
    {
    case -1:
      return 1;
    case 38:
      return 6;
    case 60:
      switch (this.peek1)
      {
      case 47:
        return 3;
      case 91:
        return 5;
      case 33:
      case 63:
        return 999;
      }
      return 2;
    }
    return 4;
  }

  private static final String[] ensureCapacity(String[] paramArrayOfString, int paramInt)
  {
    if (paramArrayOfString.length >= paramInt)
      return paramArrayOfString;
    String[] arrayOfString = new String[paramInt + 16];
    System.arraycopy(paramArrayOfString, 0, arrayOfString, 0, paramArrayOfString.length);
    return arrayOfString;
  }

  private final void parseStartTag()
    throws IOException
  {
    read();
    this.name = readName();
    this.elementStack = ensureCapacity(this.elementStack, this.depth + 1);
    this.elementStack[(this.depth++)] = this.name;
    while (true)
    {
      skip();
      int i = this.peek0;
      if (i == 47)
      {
        this.degenerated = true;
        read();
        skip();
        read('>');
        break;
      }
      if (i == 62)
      {
        read();
        break;
      }
      if (i == -1)
        exception("Unexpected EOF");
      String str = readName();
      if (str.length() == 0)
        exception("attr name expected");
      skip();
      read('=');
      skip();
      int j = read();
      if ((j != 39) && (j != 34))
      {
        if (!this.relaxed)
          exception("<" + this.name + ">: invalid delimiter: " + (char)j);
        j = 32;
      }
      int k = this.attributeCount++ << 1;
      this.attributes = ensureCapacity(this.attributes, k + 4);
      this.attributes[(k++)] = str;
      int m = this.txtPos;
      pushText(j);
      this.attributes[k] = pop(m);
      if (j != 32)
        read();
    }
  }

  public final boolean pushEntity()
    throws IOException
  {
    read();
    int i = this.txtPos;
    while ((!this.eof) && (this.peek0 != 59))
      push(read());
    String str1 = pop(i);
    read();
    if ((str1.length() > 0) && (str1.charAt(0) == '#'))
    {
      int j = str1.charAt(1) == 'x' ? Integer.parseInt(str1.substring(2), 16) : Integer.parseInt(str1.substring(1));
      push(j);
      return j <= 32;
    }
    String str2 = (String)this.entityMap.get(str1);
    boolean bool = true;
    if (str2 == null)
      str2 = "&" + str1 + ";";
    for (int k = 0; k < str2.length(); k++)
    {
      int m = str2.charAt(k);
      if (m > 32)
        bool = false;
      push(m);
    }
    return bool;
  }

  private final boolean pushText(int paramInt)
    throws IOException
  {
    boolean bool = true;
    for (int i = this.peek0; (!this.eof) && (i != paramInt) && ((paramInt != 32) || ((i > 32) && (i != 62))); i = this.peek0)
      if (i == 38)
      {
        if (!pushEntity())
          bool = false;
      }
      else
      {
        if (i > 32)
          bool = false;
        push(read());
      }
    return bool;
  }

  public XmlReader(Reader paramReader)
    throws IOException
  {
    this.reader = paramReader;
    this.peek0 = paramReader.read();
    this.peek1 = paramReader.read();
    this.eof = (this.peek0 == -1);
    this.entityMap = new Hashtable();
    this.entityMap.put("amp", "&");
    this.entityMap.put("apos", "'");
    this.entityMap.put("gt", ">");
    this.entityMap.put("lt", "<");
    this.entityMap.put("quot", "\"");
    this.line = 1;
    this.column = 1;
  }

  public void defineCharacterEntity(String paramString1, String paramString2)
  {
    this.entityMap.put(paramString1, paramString2);
  }

  public int getDepth()
  {
    return this.depth;
  }

  public String getPositionDescription()
  {
    StringBuffer localStringBuffer = new StringBuffer(this.type < this.TYPES.length ? this.TYPES[this.type] : "Other");
    localStringBuffer.append(" @" + this.line + ":" + this.column + ": ");
    if ((this.type == 2) || (this.type == 3))
    {
      localStringBuffer.append('<');
      if (this.type == 3)
        localStringBuffer.append('/');
      localStringBuffer.append(this.name);
      localStringBuffer.append('>');
    }
    else if (this.isWhitespace)
    {
      localStringBuffer.append("[whitespace]");
    }
    else
    {
      localStringBuffer.append(getText());
    }
    return localStringBuffer.toString();
  }

  public int getLineNumber()
  {
    return this.line;
  }

  public int getColumnNumber()
  {
    return this.column;
  }

  public boolean isWhitespace()
  {
    return this.isWhitespace;
  }

  public String getText()
  {
    if (this.text == null)
      this.text = pop(0);
    return this.text;
  }

  public String getName()
  {
    return this.name;
  }

  public boolean isEmptyElementTag()
  {
    return this.degenerated;
  }

  public int getAttributeCount()
  {
    return this.attributeCount;
  }

  public String getAttributeName(int paramInt)
  {
    if (paramInt >= this.attributeCount)
      throw new IndexOutOfBoundsException();
    return this.attributes[(paramInt << 1)];
  }

  public String getAttributeValue(int paramInt)
  {
    if (paramInt >= this.attributeCount)
      throw new IndexOutOfBoundsException();
    return this.attributes[((paramInt << 1) + 1)];
  }

  public String getAttributeValue(String paramString)
  {
    for (int i = (this.attributeCount << 1) - 2; i >= 0; i -= 2)
      if (this.attributes[i].equals(paramString))
        return this.attributes[(i + 1)];
    return null;
  }

  public int getType()
  {
    return this.type;
  }

  public int next()
    throws IOException
  {
    if (this.degenerated)
    {
      this.type = 3;
      this.degenerated = false;
      this.depth -= 1;
      return this.type;
    }
    this.txtPos = 0;
    this.isWhitespace = true;
    do
    {
      this.attributeCount = 0;
      this.name = null;
      this.text = null;
      this.type = peekType();
      switch (this.type)
      {
      case 6:
        this.isWhitespace &= pushEntity();
        this.type = 4;
        break;
      case 2:
        parseStartTag();
        break;
      case 3:
        parseEndTag();
        break;
      case 1:
        break;
      case 4:
        this.isWhitespace &= pushText(60);
        break;
      case 5:
        parseLegacy(true);
        this.isWhitespace = false;
        this.type = 4;
        break;
      default:
        parseLegacy(false);
      }
    }
    while ((this.type > 4) || ((this.type == 4) && (peekType() >= 4)));
    this.isWhitespace &= this.type == 4;
    return this.type;
  }

  public void require(int paramInt, String paramString)
    throws IOException
  {
    if ((this.type == 4) && (paramInt != 4) && (isWhitespace()))
      next();
    if ((paramInt != this.type) || ((paramString != null) && (!paramString.equals(getName()))))
      exception("expected: " + this.TYPES[paramInt] + "/" + paramString);
  }

  public String readText()
    throws IOException
  {
    if (this.type != 4)
      return "";
    String str = getText();
    next();
    return str;
  }
}

/* Location:           dulux-signed.jar
 * Qualified Name:     org.kobjects.xml.XmlReader
 * JD-Core Version:    0.6.2
 */