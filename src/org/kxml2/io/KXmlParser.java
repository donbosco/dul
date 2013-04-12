package org.kxml2.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Hashtable;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class KXmlParser
  implements XmlPullParser
{
  private Object location;
  private static final String UNEXPECTED_EOF = "Unexpected EOF";
  private static final String ILLEGAL_TYPE = "Wrong event type";
  private static final int LEGACY = 999;
  private static final int XML_DECL = 998;
  private String version;
  private Boolean standalone;
  private boolean processNsp;
  private boolean relaxed;
  private Hashtable entityMap;
  private int depth;
  private String[] elementStack = new String[16];
  private String[] nspStack = new String[8];
  private int[] nspCounts = new int[4];
  private Reader reader;
  private String encoding;
  private char[] srcBuf = new char[Runtime.getRuntime().freeMemory() >= 1048576L ? ' ' : ''];
  private int srcPos;
  private int srcCount;
  private int line;
  private int column;
  private char[] txtBuf = new char[''];
  private int txtPos;
  private int type;
  private boolean isWhitespace;
  private String namespace;
  private String prefix;
  private String name;
  private boolean degenerated;
  private int attributeCount;
  private String[] attributes = new String[16];
  private int stackMismatch = 0;
  private String error;
  private int[] peek = new int[2];
  private int peekCount;
  private boolean wasCR;
  private boolean unresolved;
  private boolean token;

  private final boolean isProp(String paramString1, boolean paramBoolean, String paramString2)
  {
    if (!paramString1.startsWith("http://xmlpull.org/v1/doc/"))
      return false;
    if (paramBoolean)
      return paramString1.substring(42).equals(paramString2);
    return paramString1.substring(40).equals(paramString2);
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
          error("illegal empty namespace");
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
        if ((j == 0) && (!this.relaxed))
          throw new RuntimeException("illegal attribute name: " + str1 + " at " + this);
        if (j != -1)
        {
          str2 = str1.substring(0, j);
          str1 = str1.substring(j + 1);
          String str3 = getNamespace(str2);
          if ((str3 == null) && (!this.relaxed))
            throw new RuntimeException("Undefined Prefix: " + str2 + " in " + this);
          this.attributes[i] = str3;
          this.attributes[(i + 1)] = str2;
          this.attributes[(i + 2)] = str1;
        }
      }
    i = this.name.indexOf(':');
    if (i == 0)
      error("illegal tag name: " + this.name);
    if (i != -1)
    {
      this.prefix = this.name.substring(0, i);
      this.name = this.name.substring(i + 1);
    }
    this.namespace = getNamespace(this.prefix);
    if (this.namespace == null)
    {
      if (this.prefix != null)
        error("undefined prefix: " + this.prefix);
      this.namespace = "";
    }
    return bool;
  }

  private final String[] ensureCapacity(String[] paramArrayOfString, int paramInt)
  {
    if (paramArrayOfString.length >= paramInt)
      return paramArrayOfString;
    String[] arrayOfString = new String[paramInt + 16];
    System.arraycopy(paramArrayOfString, 0, arrayOfString, 0, paramArrayOfString.length);
    return arrayOfString;
  }

  private final void error(String paramString)
    throws XmlPullParserException
  {
    if (this.relaxed)
    {
      if (this.error == null)
        this.error = ("ERR: " + paramString);
    }
    else
      exception(paramString);
  }

  private final void exception(String paramString)
    throws XmlPullParserException
  {
    throw new XmlPullParserException(paramString.substring(0, 100) + "\n", this, null);
  }

  private final void nextImpl()
    throws IOException, XmlPullParserException
  {
    if (this.reader == null)
      exception("No Input specified");
    if (this.type == 3)
      this.depth -= 1;
    do
    {
      this.attributeCount = -1;
      if (this.degenerated)
      {
        this.degenerated = false;
        this.type = 3;
        return;
      }
      int i;
      if (this.error != null)
      {
        for (i = 0; i < this.error.length(); i++)
          push(this.error.charAt(i));
        this.error = null;
        this.type = 9;
        return;
      }
      if ((this.relaxed) && ((this.stackMismatch > 0) || ((peek(0) == -1) && (this.depth > 0))))
      {
        i = this.depth - 1 << 2;
        this.type = 3;
        this.namespace = this.elementStack[i];
        this.prefix = this.elementStack[(i + 1)];
        this.name = this.elementStack[(i + 2)];
        if (this.stackMismatch != 1)
          this.error = ("missing end tag /" + this.name + " inserted");
        if (this.stackMismatch > 0)
          this.stackMismatch -= 1;
        return;
      }
      this.prefix = null;
      this.name = null;
      this.namespace = null;
      this.type = peekType();
      switch (this.type)
      {
      case 6:
        pushEntity();
        return;
      case 2:
        parseStartTag(false);
        return;
      case 3:
        parseEndTag();
        return;
      case 1:
        return;
      case 4:
        pushText(60, !this.token);
        if ((this.depth == 0) && (this.isWhitespace))
          this.type = 7;
        return;
      case 5:
      }
      this.type = parseLegacy(this.token);
    }
    while (this.type == 998);
  }

  private final int parseLegacy(boolean paramBoolean)
    throws IOException, XmlPullParserException
  {
    String str1 = "";
    int k = 0;
    read();
    int m = read();
    int i;
    int j;
    if (m == 63)
    {
      if (((peek(0) == 120) || (peek(0) == 88)) && ((peek(1) == 109) || (peek(1) == 77)))
      {
        if (paramBoolean)
        {
          push(peek(0));
          push(peek(1));
        }
        read();
        read();
        if (((peek(0) == 108) || (peek(0) == 76)) && (peek(1) <= 32))
        {
          if ((this.line != 1) || (this.column > 4))
            error("PI must not start with xml");
          parseStartTag(true);
          if ((this.attributeCount < 1) || (!"version".equals(this.attributes[2])))
            error("version expected");
          this.version = this.attributes[3];
          int n = 1;
          if ((n < this.attributeCount) && ("encoding".equals(this.attributes[6])))
          {
            this.encoding = this.attributes[7];
            n++;
          }
          if ((n < this.attributeCount) && ("standalone".equals(this.attributes[(4 * n + 2)])))
          {
            String str2 = this.attributes[(3 + 4 * n)];
            if ("yes".equals(str2))
              this.standalone = new Boolean(true);
            else if ("no".equals(str2))
              this.standalone = new Boolean(false);
            else
              error("illegal standalone value: " + str2);
            n++;
          }
          if (n != this.attributeCount)
            error("illegal xmldecl");
          this.isWhitespace = true;
          this.txtPos = 0;
          return 998;
        }
      }
      i = 63;
      j = 8;
    }
    else if (m == 33)
    {
      if (peek(0) == 45)
      {
        j = 9;
        str1 = "--";
        i = 45;
      }
      else if (peek(0) == 91)
      {
        j = 5;
        str1 = "[CDATA[";
        i = 93;
        paramBoolean = true;
      }
      else
      {
        j = 10;
        str1 = "DOCTYPE";
        i = -1;
      }
    }
    else
    {
      error("illegal: <" + m);
      return 9;
    }
    for (int n = 0; n < str1.length(); n++)
      read(str1.charAt(n));
    if (j == 10)
    {
      parseDoctype(paramBoolean);
    }
    else
    {
      while (true)
      {
        m = read();
        if (m == -1)
        {
          error("Unexpected EOF");
          return 9;
        }
        if (paramBoolean)
          push(m);
        if (((i == 63) || (m == i)) && (peek(0) == i) && (peek(1) == 62))
          break;
        k = m;
      }
      if ((i == 45) && (k == 45))
        error("illegal comment delimiter: --->");
      read();
      read();
      if ((paramBoolean) && (i != 63))
        this.txtPos -= 1;
    }
    return j;
  }

  private final void parseDoctype(boolean paramBoolean)
    throws IOException, XmlPullParserException
  {
    int i = 1;
    int j = 0;
    while (true)
    {
      int k = read();
      switch (k)
      {
      case -1:
        error("Unexpected EOF");
        return;
      case 39:
        j = j == 0 ? 1 : 0;
        break;
      case 60:
        if (j == 0)
          i++;
        break;
      case 62:
        if (j == 0)
        {
          i--;
          if (i == 0)
            return;
        }
        break;
      }
      if (paramBoolean)
        push(k);
    }
  }

  private final void parseEndTag()
    throws IOException, XmlPullParserException
  {
    read();
    read();
    this.name = readName();
    skip();
    read('>');
    int i = this.depth - 1 << 2;
    if (this.depth == 0)
    {
      error("element stack empty");
      this.type = 9;
      return;
    }
    if (!this.name.equals(this.elementStack[(i + 3)]))
    {
      error("expected: /" + this.elementStack[(i + 3)] + " read: " + this.name);
      int j = i;
      for (j = i; (j >= 0) && (!this.name.toLowerCase().equals(this.elementStack[(j + 3)].toLowerCase())); j -= 4)
        this.stackMismatch += 1;
      if (j < 0)
      {
        this.stackMismatch = 0;
        this.type = 9;
        return;
      }
    }
    this.namespace = this.elementStack[i];
    this.prefix = this.elementStack[(i + 1)];
    this.name = this.elementStack[(i + 2)];
  }

  private final int peekType()
    throws IOException
  {
    switch (peek(0))
    {
    case -1:
      return 1;
    case 38:
      return 6;
    case 60:
      switch (peek(1))
      {
      case 47:
        return 3;
      case 33:
      case 63:
        return 999;
      }
      return 2;
    }
    return 4;
  }

  private final String get(int paramInt)
  {
    return new String(this.txtBuf, paramInt, this.txtPos - paramInt);
  }

  private final void push(int paramInt)
  {
    this.isWhitespace &= paramInt <= 32;
    if (this.txtPos == this.txtBuf.length)
    {
      char[] arrayOfChar = new char[this.txtPos * 4 / 3 + 4];
      System.arraycopy(this.txtBuf, 0, arrayOfChar, 0, this.txtPos);
      this.txtBuf = arrayOfChar;
    }
    this.txtBuf[(this.txtPos++)] = ((char)paramInt);
  }

  private final void parseStartTag(boolean paramBoolean)
    throws IOException, XmlPullParserException
  {
    if (!paramBoolean)
      read();
    this.name = readName();
    this.attributeCount = 0;
    Object localObject;
    while (true)
    {
      skip();
      int i = peek(0);
      if (paramBoolean)
      {
        if (i == 63)
        {
          read();
          read('>');
        }
      }
      else
      {
        if (i == 47)
        {
          this.degenerated = true;
          read();
          skip();
          read('>');
          break;
        }
        if ((i == 62) && (!paramBoolean))
        {
          read();
          break;
        }
      }
      if (i == -1)
      {
        error("Unexpected EOF");
        return;
      }
      localObject = readName();
      if (((String)localObject).length() == 0)
      {
        error("attr name expected");
        break;
      }
      int j = this.attributeCount++ << 2;
      this.attributes = ensureCapacity(this.attributes, j + 4);
      this.attributes[(j++)] = "";
      this.attributes[(j++)] = null;
      this.attributes[(j++)] = (String) localObject;
      skip();
      if (peek(0) != 61)
      {
        error("Attr.value missing f. " + (String)localObject);
        this.attributes[j] = "1";
      }
      else
      {
        read('=');
        skip();
        int k = peek(0);
        if ((k != 39) && (k != 34))
        {
          error("attr value delimiter missing!");
          k = 32;
        }
        else
        {
          read();
        }
        int m = this.txtPos;
        pushText(k, true);
        this.attributes[j] = get(m);
        this.txtPos = m;
        if (k != 32)
          read();
      }
    }
    int i = this.depth++ << 2;
    this.elementStack = ensureCapacity(this.elementStack, i + 4);
    this.elementStack[(i + 3)] = this.name;
    if (this.depth >= this.nspCounts.length)
    {
      localObject = new int[this.depth + 4];
      System.arraycopy(this.nspCounts, 0, localObject, 0, this.nspCounts.length);
      this.nspCounts = ((int[])localObject);
    }
    this.nspCounts[this.depth] = this.nspCounts[(this.depth - 1)];
    if (this.processNsp)
      adjustNsp();
    else
      this.namespace = "";
    this.elementStack[i] = this.namespace;
    this.elementStack[(i + 1)] = this.prefix;
    this.elementStack[(i + 2)] = this.name;
  }

  private final void pushEntity()
    throws IOException, XmlPullParserException
  {
    push(read());
    int i = this.txtPos;
    while (true)
    {
      int j = read();
      if (j == 59)
        break;
      if ((j < 128) && ((j < 48) || (j > 57)) && ((j < 97) || (j > 122)) && ((j < 65) || (j > 90)) && (j != 95) && (j != 45) && (j != 35))
      {
        if (!this.relaxed)
          error("unterminated entity ref");
        if (j != -1)
          push(j);
        return;
      }
      push(j);
    }
    String str1 = get(i);
    this.txtPos = (i - 1);
    if ((this.token) && (this.type == 6))
      this.name = str1;
    if (str1.charAt(0) == '#')
    {
      int k = str1.charAt(1) == 'x' ? Integer.parseInt(str1.substring(2), 16) : Integer.parseInt(str1.substring(1));
      push(k);
      return;
    }
    String str2 = (String)this.entityMap.get(str1);
    this.unresolved = (str2 == null);
    if (this.unresolved)
    {
      if (!this.token)
        error("unresolved: &" + str1 + ";");
    }
    else
      for (int m = 0; m < str2.length(); m++)
        push(str2.charAt(m));
  }

  private final void pushText(int paramInt, boolean paramBoolean)
    throws IOException, XmlPullParserException
  {
    int i = peek(0);
    int j = 0;
    while ((i != -1) && (i != paramInt) && ((paramInt != 32) || ((i > 32) && (i != 62))))
    {
      if (i == 38)
      {
        if (!paramBoolean)
          break;
        pushEntity();
      }
      else if ((i == 10) && (this.type == 2))
      {
        read();
        push(32);
      }
      else
      {
        push(read());
      }
      if ((i == 62) && (j >= 2) && (paramInt != 93))
        error("Illegal: ]]>");
      if (i == 93)
        j++;
      else
        j = 0;
      i = peek(0);
    }
  }

  private final void read(char paramChar)
    throws IOException, XmlPullParserException
  {
    char c = (char) read();
    if (c != paramChar)
      error("expected: '" + paramChar + "' actual: '" + (char)c + "'");
  }

  private final int read()
    throws IOException
  {
    int i;
    if (this.peekCount == 0)
    {
      i = peek(0);
    }
    else
    {
      i = this.peek[0];
      this.peek[0] = this.peek[1];
    }
    this.peekCount -= 1;
    this.column += 1;
    if (i == 10)
    {
      this.line += 1;
      this.column = 1;
    }
    return i;
  }

  private final int peek(int paramInt)
    throws IOException
  {
    while (paramInt >= this.peekCount)
    {
      int i;
      if (this.srcBuf.length <= 1)
      {
        i = this.reader.read();
      }
      else if (this.srcPos < this.srcCount)
      {
        i = this.srcBuf[(this.srcPos++)];
      }
      else
      {
        this.srcCount = this.reader.read(this.srcBuf, 0, this.srcBuf.length);
        if (this.srcCount <= 0)
          i = -1;
        else
          i = this.srcBuf[0];
        this.srcPos = 1;
      }
      if (i == 13)
      {
        this.wasCR = true;
        this.peek[(this.peekCount++)] = 10;
      }
      else
      {
        if (i == 10)
        {
          if (!this.wasCR)
            this.peek[(this.peekCount++)] = 10;
        }
        else
          this.peek[(this.peekCount++)] = i;
        this.wasCR = false;
      }
    }
    return this.peek[paramInt];
  }

  private final String readName()
    throws IOException, XmlPullParserException
  {
    int i = this.txtPos;
    int j = peek(0);
    if (((j < 97) || (j > 122)) && ((j < 65) || (j > 90)) && (j != 95) && (j != 58) && (j < 192) && (!this.relaxed))
      error("name expected");
    do
    {
      push(read());
      j = peek(0);
    }
    while (((j >= 97) && (j <= 122)) || ((j >= 65) && (j <= 90)) || ((j >= 48) && (j <= 57)) || (j == 95) || (j == 45) || (j == 58) || (j == 46) || (j >= 183));
    String str = get(i);
    this.txtPos = i;
    return str;
  }

  private final void skip()
    throws IOException
  {
    while (true)
    {
      int i = peek(0);
      if ((i > 32) || (i == -1))
        break;
      read();
    }
  }

  public void setInput(Reader paramReader)
    throws XmlPullParserException
  {
    this.reader = paramReader;
    this.line = 1;
    this.column = 0;
    this.type = 0;
    this.name = null;
    this.namespace = null;
    this.degenerated = false;
    this.attributeCount = -1;
    this.encoding = null;
    this.version = null;
    this.standalone = null;
    if (paramReader == null)
      return;
    this.srcPos = 0;
    this.srcCount = 0;
    this.peekCount = 0;
    this.depth = 0;
    this.entityMap = new Hashtable();
    this.entityMap.put("amp", "&");
    this.entityMap.put("apos", "'");
    this.entityMap.put("gt", ">");
    this.entityMap.put("lt", "<");
    this.entityMap.put("quot", "\"");
  }

  public void setInput(InputStream paramInputStream, String paramString)
    throws XmlPullParserException
  {
    this.srcPos = 0;
    this.srcCount = 0;
    String str1 = paramString;
    if (paramInputStream == null)
      throw new IllegalArgumentException();
    try
    {
      if (str1 == null)
      {
        int i = 0;
        int j;
        while (this.srcCount < 4)
        {
          j = paramInputStream.read();
          if (j == -1)
            break;
          i = i << 8 | j;
          this.srcBuf[(this.srcCount++)] = ((char)j);
        }
        if (this.srcCount == 4)
          switch (i)
          {
          case 65279:
            str1 = "UTF-32BE";
            this.srcCount = 0;
            break;
          case -131072:
            str1 = "UTF-32LE";
            this.srcCount = 0;
            break;
          case 60:
            str1 = "UTF-32BE";
            this.srcBuf[0] = '<';
            this.srcCount = 1;
            break;
          case 1006632960:
            str1 = "UTF-32LE";
            this.srcBuf[0] = '<';
            this.srcCount = 1;
            break;
          case 3932223:
            str1 = "UTF-16BE";
            this.srcBuf[0] = '<';
            this.srcBuf[1] = '?';
            this.srcCount = 2;
            break;
          case 1006649088:
            str1 = "UTF-16LE";
            this.srcBuf[0] = '<';
            this.srcBuf[1] = '?';
            this.srcCount = 2;
            break;
          case 1010792557:
            while (true)
            {
              j = paramInputStream.read();
              if (j == -1)
                break;
              this.srcBuf[(this.srcCount++)] = ((char)j);
              if (j == 62)
              {
                String str2 = new String(this.srcBuf, 0, this.srcCount);
                int k = str2.indexOf("encoding");
                if (k == -1)
                  break;
                while ((str2.charAt(k) != '"') && (str2.charAt(k) != '\''))
                  k++;
                int m = str2.charAt(k++);
                int n = str2.indexOf(m, k);
                str1 = str2.substring(k, n);
                break;
              }
            }
          default:
            if ((i & 0xFFFF0000) == -16842752)
            {
              str1 = "UTF-16BE";
              this.srcBuf[0] = ((char)(this.srcBuf[2] << '\b' | this.srcBuf[3]));
              this.srcCount = 1;
            }
            else if ((i & 0xFFFF0000) == -131072)
            {
              str1 = "UTF-16LE";
              this.srcBuf[0] = ((char)(this.srcBuf[3] << '\b' | this.srcBuf[2]));
              this.srcCount = 1;
            }
            else if ((i & 0xFFFFFF00) == -272908544)
            {
              str1 = "UTF-8";
              this.srcBuf[0] = this.srcBuf[3];
              this.srcCount = 1;
            }
            break;
          }
      }
      if (str1 == null)
        str1 = "UTF-8";
      int i = this.srcCount;
      setInput(new InputStreamReader(paramInputStream, str1));
      this.encoding = paramString;
      this.srcCount = i;
    }
    catch (Exception localException)
    {
      throw new XmlPullParserException("Invalid stream or encoding: " + localException.toString(), this, localException);
    }
  }

  public boolean getFeature(String paramString)
  {
    if ("http://xmlpull.org/v1/doc/features.html#process-namespaces".equals(paramString))
      return this.processNsp;
    if (isProp(paramString, false, "relaxed"))
      return this.relaxed;
    return false;
  }

  public String getInputEncoding()
  {
    return this.encoding;
  }

  public void defineEntityReplacementText(String paramString1, String paramString2)
    throws XmlPullParserException
  {
    if (this.entityMap == null)
      throw new RuntimeException("entity replacement text must be defined after setInput!");
    this.entityMap.put(paramString1, paramString2);
  }

  public Object getProperty(String paramString)
  {
    if (isProp(paramString, true, "xmldecl-version"))
      return this.version;
    if (isProp(paramString, true, "xmldecl-standalone"))
      return this.standalone;
    if (isProp(paramString, true, "location"))
      return this.location != null ? this.location : this.reader.toString();
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
    localStringBuffer.append("@" + this.line + ":" + this.column);
    if (this.location != null)
    {
      localStringBuffer.append(" in ");
      localStringBuffer.append(this.location);
    }
    else if (this.reader != null)
    {
      localStringBuffer.append(" in ");
      localStringBuffer.append(this.reader.toString());
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
    throws XmlPullParserException
  {
    if ((this.type != 4) && (this.type != 7) && (this.type != 5))
      exception("Wrong event type");
    return this.isWhitespace;
  }

  public String getText()
  {
    return (this.type < 4) || ((this.type == 6) && (this.unresolved)) ? null : get(0);
  }

  public char[] getTextCharacters(int[] paramArrayOfInt)
  {
    if (this.type >= 4)
    {
      if (this.type == 6)
      {
        paramArrayOfInt[0] = 0;
        paramArrayOfInt[1] = this.name.length();
        return this.name.toCharArray();
      }
      paramArrayOfInt[0] = 0;
      paramArrayOfInt[1] = this.txtPos;
      return this.txtBuf;
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
    this.txtPos = 0;
    this.isWhitespace = true;
    int i = 9999;
    this.token = false;
    do
    {
      nextImpl();
      if (this.type < i)
        i = this.type;
    }
    while ((i > 6) || ((i >= 4) && (peekType() >= 4)));
    this.type = i;
    if (this.type > 4)
      this.type = 4;
    return this.type;
  }

  public int nextToken()
    throws XmlPullParserException, IOException
  {
    this.isWhitespace = true;
    this.txtPos = 0;
    this.token = true;
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

  public void require(int paramInt, String paramString1, String paramString2)
    throws XmlPullParserException, IOException
  {
    if ((paramInt != this.type) || ((paramString1 != null) && (!paramString1.equals(getNamespace()))) || ((paramString2 != null) && (!paramString2.equals(getName()))))
      exception("expected: " + XmlPullParser.TYPES[paramInt] + " {" + paramString1 + "}" + paramString2);
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

  public void setFeature(String paramString, boolean paramBoolean)
    throws XmlPullParserException
  {
    if ("http://xmlpull.org/v1/doc/features.html#process-namespaces".equals(paramString))
      this.processNsp = paramBoolean;
    else if (isProp(paramString, false, "relaxed"))
      this.relaxed = paramBoolean;
    else
      exception("unsupported feature: " + paramString);
  }

  public void setProperty(String paramString, Object paramObject)
    throws XmlPullParserException
  {
    if (isProp(paramString, true, "location"))
      this.location = paramObject;
    else
      throw new XmlPullParserException("unsupported property: " + paramString);
  }

  public void skipSubTree()
    throws XmlPullParserException, IOException
  {
    require(2, null, null);
    int i = 1;
    while (i > 0)
    {
      int j = next();
      if (j == 3)
        i--;
      else if (j == 2)
        i++;
    }
  }
}

/* Location:           dulux-signed.jar
 * Qualified Name:     org.kxml2.io.KXmlParser
 * JD-Core Version:    0.6.2
 */