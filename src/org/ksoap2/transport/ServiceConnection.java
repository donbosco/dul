package org.ksoap2.transport;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract interface ServiceConnection
{
  public abstract void connect()
    throws IOException;

  public abstract void disconnect()
    throws IOException;

  public abstract void setRequestProperty(String paramString1, String paramString2)
    throws IOException;

  public abstract void setRequestMethod(String paramString)
    throws IOException;

  public abstract OutputStream openOutputStream()
    throws IOException;

  public abstract InputStream openInputStream()
    throws IOException;

  public abstract InputStream getErrorStream();
}

/* Location:           dulux-signed.jar
 * Qualified Name:     org.ksoap2.transport.ServiceConnection
 * JD-Core Version:    0.6.2
 */