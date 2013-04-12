package org.kobjects.io;

import java.io.IOException;
import java.io.Reader;

public class LookAheadReader extends Reader
{
  char[] buf = new char[Runtime.getRuntime().freeMemory() > 1000000L ? '䀀' : ''];
  int bufPos = 0;
  int bufValid = 0;
  Reader reader;

  public LookAheadReader(Reader paramReader)
  {
    this.reader = paramReader;
  }

  public int read(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws IOException
  {
    if ((this.bufValid == 0) && (peek(0) == -1))
      return -1;
    if (paramInt2 > this.bufValid)
      paramInt2 = this.bufValid;
    if (paramInt2 > this.buf.length - this.bufPos)
      paramInt2 = this.buf.length - this.bufPos;
    System.arraycopy(this.buf, this.bufPos, paramArrayOfChar, paramInt1, paramInt2);
    this.bufValid -= paramInt2;
    this.bufPos += paramInt2;
    if (this.bufPos > this.buf.length)
      this.bufPos -= this.buf.length;
    return paramInt2;
  }

  public String readTo(String paramString)
    throws IOException
  {
    StringBuffer localStringBuffer = new StringBuffer();
    while ((peek(0) != -1) && (paramString.indexOf((char)peek(0)) == -1))
      localStringBuffer.append((char)read());
    return localStringBuffer.toString();
  }

  public String readTo(char paramChar)
    throws IOException
  {
    StringBuffer localStringBuffer = new StringBuffer();
    while ((peek(0) != -1) && (peek(0) != paramChar))
      localStringBuffer.append((char)read());
    return localStringBuffer.toString();
  }

  public void close()
    throws IOException
  {
    this.reader.close();
  }

  public int read()
    throws IOException
  {
    int i = peek(0);
    if (i != -1)
    {
      if (++this.bufPos == this.buf.length)
        this.bufPos = 0;
      this.bufValid -= 1;
    }
    return i;
  }

  public int peek(int paramInt)
    throws IOException
  {
    if (paramInt > 127)
      throw new RuntimeException("peek > 127 not supported!");
    while (paramInt >= this.bufValid)
    {
      int i = (this.bufPos + this.bufValid) % this.buf.length;
      int j = Math.min(this.buf.length - i, this.buf.length - this.bufValid);
      j = this.reader.read(this.buf, i, j);
      if (j == -1)
        return -1;
      this.bufValid += j;
    }
    return this.buf[(this.bufPos + paramInt % this.buf.length)];
  }

  public String readLine()
    throws IOException
  {
    if (peek(0) == -1)
      return null;
    String str = readTo("\r\n");
    if ((read() == 13) && (peek(0) == 10))
      read();
    return str;
  }

  public String readWhile(String paramString)
    throws IOException
  {
    StringBuffer localStringBuffer = new StringBuffer();
    while ((peek(0) != -1) && (paramString.indexOf((char)peek(0)) != -1))
      localStringBuffer.append((char)read());
    return localStringBuffer.toString();
  }

  public void skip(String paramString)
    throws IOException
  {
    StringBuffer localStringBuffer = new StringBuffer();
    while ((peek(0) != -1) && (paramString.indexOf((char)peek(0)) != -1))
      read();
  }
}

/* Location:           dulux-signed.jar
 * Qualified Name:     org.kobjects.io.LookAheadReader
 * JD-Core Version:    0.6.2
 */