package org.kobjects.io;

import java.io.IOException;
import java.io.InputStream;

public class BoundInputStream extends InputStream
{
  int remaining;
  InputStream is;

  public BoundInputStream(InputStream paramInputStream, int paramInt)
  {
    this.is = paramInputStream;
    this.remaining = paramInt;
  }

  public int available()
    throws IOException
  {
    int i = this.is.available();
    return i < this.remaining ? i : this.remaining;
  }

  public int read()
    throws IOException
  {
    if (this.remaining <= 0)
      return -1;
    this.remaining -= 1;
    return this.is.read();
  }

  public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    if (paramInt2 > this.remaining)
      paramInt2 = this.remaining;
    int i = this.is.read(paramArrayOfByte, paramInt1, paramInt2);
    if (i > 0)
      this.remaining -= i;
    return i;
  }

  public void close()
  {
    try
    {
      this.is.close();
    }
    catch (IOException localIOException)
    {
    }
  }
}

/* Location:           dulux-signed.jar
 * Qualified Name:     org.kobjects.io.BoundInputStream
 * JD-Core Version:    0.6.2
 */