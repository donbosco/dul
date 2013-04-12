package org.kobjects.util;

public class Strings
{
  public static String replace(String paramString1, String paramString2, String paramString3)
  {
    int i = paramString1.indexOf(paramString2);
    if (i == -1)
      return paramString1;
    StringBuffer localStringBuffer = new StringBuffer(paramString1.substring(0, i));
    while (true)
    {
      localStringBuffer.append(paramString3);
      i += paramString2.length();
      int j = paramString1.indexOf(paramString2, i);
      if (j == -1)
        break;
      localStringBuffer.append(paramString1.substring(i, j));
      i = j;
    }
    localStringBuffer.append(paramString1.substring(i));
    return localStringBuffer.toString();
  }

  public static String toAscii(String paramString)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    for (int i = 0; i < paramString.length(); i++)
    {
      char c = paramString.charAt(i);
      if (c <= ' ')
        localStringBuffer.append(' ');
      else if (c < '')
        localStringBuffer.append(c);
      else
        switch (c)
        {
        case 'Ä':
          localStringBuffer.append("Ae");
          break;
        case 'ä':
          localStringBuffer.append("ae");
          break;
        case 'Ö':
          localStringBuffer.append("Oe");
          break;
        case 'ö':
          localStringBuffer.append("oe");
          break;
        case 'Ü':
          localStringBuffer.append("Ue");
          break;
        case 'ü':
          localStringBuffer.append("ue");
          break;
        case 'ß':
          localStringBuffer.append("ss");
          break;
        default:
          localStringBuffer.append('?');
        }
    }
    return localStringBuffer.toString();
  }

  public static String fill(String paramString, int paramInt, char paramChar)
  {
    int i = paramInt < 0 ? 1 : 0;
    paramInt = Math.abs(paramInt);
    if (paramString.length() >= paramInt)
      return paramString;
    StringBuffer localStringBuffer = new StringBuffer();
    paramInt -= paramString.length();
    while (paramInt > 0)
    {
      localStringBuffer.append(paramChar);
      paramInt--;
    }
    if (i != 0)
    {
      localStringBuffer.append(paramString);
      return localStringBuffer.toString();
    }
    return paramString + localStringBuffer.toString();
  }

  public static String beautify(String paramString)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    if (paramString.length() > 0)
    {
      localStringBuffer.append(Character.toUpperCase(paramString.charAt(0)));
      for (int i = 1; i < paramString.length() - 1; i++)
      {
        char c = paramString.charAt(i);
        if ((Character.isUpperCase(c)) && (Character.isLowerCase(paramString.charAt(i - 1))) && (Character.isLowerCase(paramString.charAt(i + 1))))
          localStringBuffer.append(" ");
        localStringBuffer.append(c);
      }
      if (paramString.length() > 1)
        localStringBuffer.append(paramString.charAt(paramString.length() - 1));
    }
    return localStringBuffer.toString();
  }

  public static String lTrim(String paramString1, String paramString2)
  {
    int i = 0;
    int j = paramString1.length();
    while ((i < j) && (paramString2 == null ? paramString1.charAt(i) <= ' ' : paramString2.indexOf(paramString1.charAt(i)) != -1))
      i++;
    return i == 0 ? paramString1 : paramString1.substring(i);
  }

  public static String rTrim(String paramString1, String paramString2)
  {
	  int i;
	    for (i = paramString1.length() - 1; (i >= 0) && (paramString2 == null ? paramString1.charAt(i) <= ' ' : paramString2.indexOf(paramString1.charAt(i)) != -1); i--);
	    return i == paramString1.length() - 1 ? paramString1 : paramString1.substring(0, i + 1);
	  }
}

/* Location:           dulux-signed.jar
 * Qualified Name:     org.kobjects.util.Strings
 * JD-Core Version:    0.6.2
 */