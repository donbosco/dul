package org.kobjects.util;

import java.util.Vector;

public class Csv
{
  public static String encode(String paramString, char paramChar)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    for (int i = 0; i < paramString.length(); i++)
    {
      char c = paramString.charAt(i);
      if ((c == paramChar) || (c == '^'))
      {
        localStringBuffer.append(c);
        localStringBuffer.append(c);
      }
      else if (c < ' ')
      {
        localStringBuffer.append('^');
        localStringBuffer.append((char)(c + '@'));
      }
      else
      {
        localStringBuffer.append(c);
      }
    }
    return localStringBuffer.toString();
  }

  public static String encode(Object[] paramArrayOfObject)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    for (int i = 0; i < paramArrayOfObject.length; i++)
    {
      if (i != 0)
        localStringBuffer.append(',');
      Object localObject = paramArrayOfObject[i];
      if (((localObject instanceof Number)) || ((localObject instanceof Boolean)))
      {
        localStringBuffer.append(localObject.toString());
      }
      else
      {
        localStringBuffer.append('"');
        localStringBuffer.append(encode(localObject.toString(), '"'));
        localStringBuffer.append('"');
      }
    }
    return localStringBuffer.toString();
  }

  public static String[] decode(String paramString)
  {
    Vector localVector = new Vector();
    int i = 0;
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
        if (paramString.charAt(i) == '"')
        {
          i++;
          StringBuffer localStringBuffer = new StringBuffer();
          while (true)
          {
            char m = paramString.charAt(i++);
            if ((m == 94) && (i < j))
            {
              int n = paramString.charAt(i++);
              localStringBuffer.append(n == 94 ? n : (char)(n - 64));
            }
            else
            {
              if (m == 34)
              {
                if ((i == j) || (paramString.charAt(i) != '"'))
                  break;
                i++;
              }
              localStringBuffer.append(m);
            }
          }
          localVector.addElement(localStringBuffer.toString());
          while ((i < j) && (paramString.charAt(i) <= ' '))
            i++;
          if (i >= j)
            break;
          if (paramString.charAt(i) != ',')
            throw new RuntimeException("Comma expected at " + i + " line: " + paramString);
          i++;
        }
        else
        {
          int k = paramString.indexOf(',', i);
          if (k == -1)
          {
            localVector.addElement(paramString.substring(i).trim());
            break;
          }
          localVector.addElement(paramString.substring(i, k).trim());
          i = k + 1;
        }
      }
    String[] arrayOfString = new String[localVector.size()];
    for (int m = 0; m < arrayOfString.length; m++)
      arrayOfString[m] = ((String)localVector.elementAt(m));
    return arrayOfString;
  }
}

/* Location:           dulux-signed.jar
 * Qualified Name:     org.kobjects.util.Csv
 * JD-Core Version:    0.6.2
 */