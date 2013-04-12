package org.kobjects.base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Base64
{
  static final char[] charTab = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();

  public static String encode(byte[] paramArrayOfByte)
  {
    return encode(paramArrayOfByte, 0, paramArrayOfByte.length, null).toString();
  }

  public static StringBuffer encode(byte[] paramArrayOfByte, int paramInt1, int paramInt2, StringBuffer paramStringBuffer)
  {
    if (paramStringBuffer == null)
      paramStringBuffer = new StringBuffer(paramArrayOfByte.length * 3 / 2);
    int i = paramInt2 - 3;
    int j = paramInt1;
    int k = 0;
    int m;
    while (j <= i)
    {
      m = (paramArrayOfByte[j] & 0xFF) << 16 | (paramArrayOfByte[(j + 1)] & 0xFF) << 8 | paramArrayOfByte[(j + 2)] & 0xFF;
      paramStringBuffer.append(charTab[(m >> 18 & 0x3F)]);
      paramStringBuffer.append(charTab[(m >> 12 & 0x3F)]);
      paramStringBuffer.append(charTab[(m >> 6 & 0x3F)]);
      paramStringBuffer.append(charTab[(m & 0x3F)]);
      j += 3;
      if (k++ >= 14)
      {
        k = 0;
        paramStringBuffer.append("\r\n");
      }
    }
    if (j == paramInt1 + paramInt2 - 2)
    {
      m = (paramArrayOfByte[j] & 0xFF) << 16 | (paramArrayOfByte[(j + 1)] & 0xFF) << 8;
      paramStringBuffer.append(charTab[(m >> 18 & 0x3F)]);
      paramStringBuffer.append(charTab[(m >> 12 & 0x3F)]);
      paramStringBuffer.append(charTab[(m >> 6 & 0x3F)]);
      paramStringBuffer.append("=");
    }
    else if (j == paramInt1 + paramInt2 - 1)
    {
      m = (paramArrayOfByte[j] & 0xFF) << 16;
      paramStringBuffer.append(charTab[(m >> 18 & 0x3F)]);
      paramStringBuffer.append(charTab[(m >> 12 & 0x3F)]);
      paramStringBuffer.append("==");
    }
    return paramStringBuffer;
  }

  static int decode(char paramChar)
  {
    if ((paramChar >= 'A') && (paramChar <= 'Z'))
      return paramChar - 'A';
    if ((paramChar >= 'a') && (paramChar <= 'z'))
      return paramChar - 'a' + 26;
    if ((paramChar >= '0') && (paramChar <= '9'))
      return paramChar - '0' + 26 + 26;
    switch (paramChar)
    {
    case '+':
      return 62;
    case '/':
      return 63;
    case '=':
      return 0;
    }
    throw new RuntimeException("unexpected code: " + paramChar);
  }

  public static byte[] decode(String paramString)
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    try
    {
      decode(paramString, localByteArrayOutputStream);
    }
    catch (IOException localIOException)
    {
      throw new RuntimeException();
    }
    return localByteArrayOutputStream.toByteArray();
  }

  public static void decode(String paramString, OutputStream paramOutputStream)
    throws IOException
  {
    int i = 0;
    int j = paramString.length();
    while (true)
      if ((i < j) && (paramString.charAt(i) <= ' '))
      {
        i++;
      }
      else
      {
        if (i == j)
          break;
        int k = (decode(paramString.charAt(i)) << 18) + (decode(paramString.charAt(i + 1)) << 12) + (decode(paramString.charAt(i + 2)) << 6) + decode(paramString.charAt(i + 3));
        paramOutputStream.write(k >> 16 & 0xFF);
        if (paramString.charAt(i + 2) == '=')
          break;
        paramOutputStream.write(k >> 8 & 0xFF);
        if (paramString.charAt(i + 3) == '=')
          break;
        paramOutputStream.write(k & 0xFF);
        i += 4;
      }
  }
}

/* Location:           dulux-signed.jar
 * Qualified Name:     org.kobjects.base64.Base64
 * JD-Core Version:    0.6.2
 */