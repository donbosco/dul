package org.ksoap2.transport;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.ksoap2.SoapEnvelope;
import org.xmlpull.v1.XmlPullParserException;

public class HttpTransportSE extends Transport
{
  public HttpTransportSE(String paramString)
  {
    super(paramString);
  }

  public void call(String paramString, SoapEnvelope paramSoapEnvelope)
    throws IOException, XmlPullParserException
  {
    if (paramString == null)
      paramString = "\"\"";
    byte[] arrayOfByte1 = createRequestData(paramSoapEnvelope);
    this.requestDump = (this.debug ? new String(arrayOfByte1) : null);
    this.responseDump = null;
    ServiceConnection localServiceConnection = getServiceConnection();
    localServiceConnection.setRequestProperty("User-Agent", "kSOAP/2.0");
    localServiceConnection.setRequestProperty("SOAPAction", paramString);
    localServiceConnection.setRequestProperty("Content-Type", "text/xml");
    localServiceConnection.setRequestProperty("Connection", "close");
    localServiceConnection.setRequestProperty("Content-Length", "" + arrayOfByte1.length);
    localServiceConnection.setRequestMethod("POST");
    localServiceConnection.connect();
    OutputStream localOutputStream = localServiceConnection.openOutputStream();
    localOutputStream.write(arrayOfByte1, 0, arrayOfByte1.length);
    localOutputStream.flush();
    localOutputStream.close();
    arrayOfByte1 = null;
    Object localObject;
    try
    {
      localServiceConnection.connect();
      localObject = localServiceConnection.openInputStream();
    }
    catch (IOException localIOException)
    {
      localObject = localServiceConnection.getErrorStream();
      if (localObject == null)
      {
        localServiceConnection.disconnect();
        throw localIOException;
      }
    }
    if (this.debug)
    {
      ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
      byte[] arrayOfByte2 = new byte[256];
      while (true)
      {
        int i = ((InputStream)localObject).read(arrayOfByte2, 0, 256);
        if (i == -1)
          break;
        localByteArrayOutputStream.write(arrayOfByte2, 0, i);
      }
      localByteArrayOutputStream.flush();
      arrayOfByte2 = localByteArrayOutputStream.toByteArray();
      this.responseDump = new String(arrayOfByte2);
      ((InputStream)localObject).close();
      localObject = new ByteArrayInputStream(arrayOfByte2);
    }
    parseResponse(paramSoapEnvelope, (InputStream)localObject);
  }

  protected ServiceConnection getServiceConnection()
    throws IOException
  {
    return new ServiceConnectionSE(this.url);
  }
}

/* Location:           dulux-signed.jar
 * Qualified Name:     org.ksoap2.transport.HttpTransportSE
 * JD-Core Version:    0.6.2
 */