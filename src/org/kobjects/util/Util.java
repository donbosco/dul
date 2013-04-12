package org.kobjects.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class Util
{
  public static OutputStream streamcopy(InputStream paramInputStream, OutputStream paramOutputStream)
    throws IOException
  {
    byte[] arrayOfByte = new byte[Runtime.getRuntime().freeMemory() >= 1048576L ? '䀀' : ''];
    while (true)
    {
      int i = paramInputStream.read(arrayOfByte, 0, arrayOfByte.length);
      if (i == -1)
        break;
      paramOutputStream.write(arrayOfByte, 0, i);
    }
    paramInputStream.close();
    return paramOutputStream;
  }

  public static int indexOf(Object[] paramArrayOfObject, Object paramObject)
  {
    for (int i = 0; i < paramArrayOfObject.length; i++)
      if (paramArrayOfObject[i].equals(paramObject))
        return i;
    return -1;
  }

  public static String buildUrl(String paramString1, String paramString2)
  {
    int i = paramString2.indexOf(':');
    if ((paramString2.startsWith("/")) || (i == 1))
      return "file:///" + paramString2;
    if ((i > 2) && (i < 6))
      return paramString2;
    if (paramString1 == null)
    {
      paramString1 = "file:///";
    }
    else
    {
      if (paramString1.indexOf(':') == -1)
        paramString1 = "file:///" + paramString1;
      if (!paramString1.endsWith("/"))
        paramString1 = paramString1 + "/";
    }
    return paramString1 + paramString2;
  }

  public static void sort(Object[] paramArrayOfObject, int paramInt1, int paramInt2)
  {
    if (paramInt2 - paramInt1 <= 2)
    {
      if ((paramInt2 - paramInt1 == 2) && (paramArrayOfObject[paramInt1].toString().compareTo(paramArrayOfObject[(paramInt1 + 1)].toString()) > 0))
      {
        Object localObject = paramArrayOfObject[paramInt1];
        paramArrayOfObject[paramInt1] = paramArrayOfObject[(paramInt1 + 1)];
        paramArrayOfObject[(paramInt1 + 1)] = localObject;
      }
      return;
    }
    if (paramInt2 - paramInt1 == 3)
    {
      sort(paramArrayOfObject, paramInt1, paramInt1 + 2);
      sort(paramArrayOfObject, paramInt1 + 1, paramInt1 + 3);
      sort(paramArrayOfObject, paramInt1, paramInt1 + 2);
      return;
    }
    int i = (paramInt1 + paramInt2) / 2;
    sort(paramArrayOfObject, paramInt1, i);
    sort(paramArrayOfObject, i, paramInt2);
    Object[] arrayOfObject = new Object[paramInt2 - paramInt1];
    int j = paramInt1;
    int k = i;
    for (int m = 0; m < arrayOfObject.length; m++)
      if (j == i)
        arrayOfObject[m] = paramArrayOfObject[(k++)];
      else if ((k == paramInt2) || (paramArrayOfObject[j].toString().compareTo(paramArrayOfObject[k].toString()) < 0))
        arrayOfObject[m] = paramArrayOfObject[(j++)];
      else
        arrayOfObject[m] = paramArrayOfObject[(k++)];
    System.arraycopy(arrayOfObject, 0, paramArrayOfObject, paramInt1, arrayOfObject.length);
  }
}

/* Location:           dulux-signed.jar
 * Qualified Name:     org.kobjects.util.Util
 * JD-Core Version:    0.6.2
 */