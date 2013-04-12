package org.ksoap2.transport;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServiceConnectionSE
  implements ServiceConnection
{
  private HttpURLConnection connection;

  public ServiceConnectionSE(String paramString)
    throws IOException
  {
    this.connection = ((HttpURLConnection)new URL(paramString).openConnection());
    this.connection.setUseCaches(false);
    this.connection.setDoOutput(true);
    this.connection.setDoInput(true);
  }

  public void connect()
    throws IOException
  {
    this.connection.connect();
  }

  public void disconnect()
  {
    this.connection.disconnect();
  }

  public void setRequestProperty(String paramString1, String paramString2)
  {
    this.connection.setRequestProperty(paramString1, paramString2);
  }

  public void setRequestMethod(String paramString)
    throws IOException
  {
    this.connection.setRequestMethod(paramString);
  }

  public OutputStream openOutputStream()
    throws IOException
  {
    return this.connection.getOutputStream();
  }

  public InputStream openInputStream()
    throws IOException
  {
    return this.connection.getInputStream();
  }

  public InputStream getErrorStream()
  {
    return this.connection.getErrorStream();
  }
}

/* Location:           dulux-signed.jar
 * Qualified Name:     org.ksoap2.transport.ServiceConnectionSE
 * JD-Core Version:    0.6.2
 */