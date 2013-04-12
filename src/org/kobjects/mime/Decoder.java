package org.kobjects.mime;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.Hashtable;
import org.kobjects.base64.Base64;

public class Decoder
{
  InputStream is;
  Hashtable header;
  boolean eof;
  boolean consumed;
  String boundary;
  String characterEncoding;
  char[] buf = new char[256];

  private final String readLine()
    throws IOException
  {
    int i = 0;
    while (true)
    {
      int j = this.is.read();
      if ((j == -1) && (i == 0))
        return null;
      if ((j == -1) || (j == 10))
        return new String(this.buf, 0, i);
      if (j != 13)
      {
        if (i >= this.buf.length)
        {
          char[] arrayOfChar = new char[this.buf.length * 3 / 2];
          System.arraycopy(this.buf, 0, arrayOfChar, 0, this.buf.length);
          this.buf = arrayOfChar;
        }
        this.buf[(i++)] = ((char)j);
      }
    }
  }

  public static Hashtable getHeaderElements(String paramString)
  {
    String str = "";
    int i = 0;
    Hashtable localHashtable = new Hashtable();
    int j = paramString.length();
    while (true)
      if ((i < j) && (paramString.charAt(i) <= ' '))
      {
        i++;
      }
      else
      {
        if (i >= j)
          break;
        int k;
		if (paramString.charAt(i) == '"')
        {
          i++;
          k = paramString.indexOf('"', i);
          if (k == -1)
            throw new RuntimeException("End quote expected in " + paramString);
          localHashtable.put(str, paramString.substring(i, k));
          i = k + 2;
          if (i >= j)
            break;
          if (paramString.charAt(i - 1) != ';')
            throw new RuntimeException("; expected in " + paramString);
        }
        else
        {
          k = paramString.indexOf(';', i);
          if (k == -1)
          {
            localHashtable.put(str, paramString.substring(i));
            break;
          }
          localHashtable.put(str, paramString.substring(i, k));
          i = k + 1;
        }
        k = paramString.indexOf('=', i);
        if (k == -1)
          break;
        str = paramString.substring(i, k).toLowerCase().trim();
        i = k + 1;
      }
    return localHashtable;
  }

  public Decoder(InputStream paramInputStream, String paramString)
    throws IOException
  {
    this(paramInputStream, paramString, null);
  }

  public Decoder(InputStream paramInputStream, String paramString1, String paramString2)
    throws IOException
  {
    this.characterEncoding = paramString2;
    this.is = paramInputStream;
    this.boundary = ("--" + paramString1);
    String str = null;
    while (true)
    {
      str = readLine();
      if (str == null)
        throw new IOException("Unexpected EOF");
      if (str.startsWith(this.boundary))
        break;
    }
    if (str.endsWith("--"))
    {
      this.eof = true;
      paramInputStream.close();
    }
    this.consumed = true;
  }

  public Enumeration getHeaderNames()
  {
    return this.header.keys();
  }

  public String getHeader(String paramString)
  {
    return (String)this.header.get(paramString.toLowerCase());
  }

  public String readContent()
    throws IOException
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    readContent(localByteArrayOutputStream);
    String str = this.characterEncoding == null ? new String(localByteArrayOutputStream.toByteArray()) : new String(localByteArrayOutputStream.toByteArray(), this.characterEncoding);
    System.out.println("Field content: '" + str + "'");
    return str;
  }

  public void readContent(OutputStream paramOutputStream)
    throws IOException
  {
    if (this.consumed)
      throw new RuntimeException("Content already consumed!");
    String str1 = "";
    String str2 = getHeader("Content-Type");
    if ("base64".equals(getHeader("Content-Transfer-Encoding")))
    {
      ByteArrayOutputStream localObject = new ByteArrayOutputStream();
      while (true)
      {
        str1 = readLine();
        if (str1 == null)
          throw new IOException("Unexpected EOF");
        if (str1.startsWith(this.boundary))
          break;
        Base64.decode(str1, paramOutputStream);
      }
    }
    Object localObject = "\r\n" + this.boundary;
    int i = 0;
    while (true)
    {
      int j = this.is.read();
      if (j == -1)
        throw new RuntimeException("Unexpected EOF");
      if ((char)j == ((String)localObject).charAt(i))
      {
        i++;
        if (i == ((String)localObject).length())
          break;
      }
      else
      {
        if (i > 0)
        {
          for (int k = 0; k < i; k++)
            paramOutputStream.write((byte)((String)localObject).charAt(k));
          i = (char)j == ((String)localObject).charAt(0) ? 1 : 0;
        }
        if (i == 0)
          paramOutputStream.write((byte)j);
      }
    }
    str1 = readLine();
    if (str1.endsWith("--"))
      this.eof = true;
    this.consumed = true;
  }

  public boolean next()
    throws IOException
  {
    if (!this.consumed)
      readContent(null);
    if (this.eof)
      return false;
    this.header = new Hashtable();
    while (true)
    {
      String str = readLine();
      if ((str == null) || (str.equals("")))
        break;
      int i = str.indexOf(':');
      if (i == -1)
        throw new IOException("colon missing in multipart header line: " + str);
      this.header.put(str.substring(0, i).trim().toLowerCase(), str.substring(i + 1).trim());
    }
    this.consumed = false;
    return true;
  }
}

/* Location:           dulux-signed.jar
 * Qualified Name:     org.kobjects.mime.Decoder
 * JD-Core Version:    0.6.2
 */