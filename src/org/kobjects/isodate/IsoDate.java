package org.kobjects.isodate;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class IsoDate
{
  public static final int DATE = 1;
  public static final int TIME = 2;
  public static final int DATE_TIME = 3;

  static void dd(StringBuffer paramStringBuffer, int paramInt)
  {
    paramStringBuffer.append((char)(48 + paramInt / 10));
    paramStringBuffer.append((char)(48 + paramInt % 10));
  }

  public static String dateToString(Date paramDate, int paramInt)
  {
    Calendar localCalendar = Calendar.getInstance();
    localCalendar.setTimeZone(TimeZone.getTimeZone("GMT"));
    localCalendar.setTime(paramDate);
    StringBuffer localStringBuffer = new StringBuffer();
    int i;
    if ((paramInt & 0x1) != 0)
    {
      i = localCalendar.get(1);
      dd(localStringBuffer, i / 100);
      dd(localStringBuffer, i % 100);
      localStringBuffer.append('-');
      dd(localStringBuffer, localCalendar.get(2) - 0 + 1);
      localStringBuffer.append('-');
      dd(localStringBuffer, localCalendar.get(5));
      if (paramInt == 3)
        localStringBuffer.append("T");
    }
    if ((paramInt & 0x2) != 0)
    {
      dd(localStringBuffer, localCalendar.get(11));
      localStringBuffer.append(':');
      dd(localStringBuffer, localCalendar.get(12));
      localStringBuffer.append(':');
      dd(localStringBuffer, localCalendar.get(13));
      localStringBuffer.append('.');
      i = localCalendar.get(14);
      localStringBuffer.append((char)(48 + i / 100));
      dd(localStringBuffer, i % 100);
      localStringBuffer.append('Z');
    }
    return localStringBuffer.toString();
  }

  public static Date stringToDate(String paramString, int paramInt)
  {
    Calendar localCalendar = Calendar.getInstance();
    if ((paramInt & 0x1) != 0)
    {
      localCalendar.set(1, Integer.parseInt(paramString.substring(0, 4)));
      localCalendar.set(2, Integer.parseInt(paramString.substring(5, 7)) - 1 + 0);
      localCalendar.set(5, Integer.parseInt(paramString.substring(8, 10)));
      if ((paramInt != 3) || (paramString.length() < 11))
      {
        localCalendar.set(11, 0);
        localCalendar.set(12, 0);
        localCalendar.set(13, 0);
        localCalendar.set(14, 0);
        return localCalendar.getTime();
      }
      paramString = paramString.substring(11);
    }
    else
    {
      localCalendar.setTime(new Date(0L));
    }
    localCalendar.set(11, Integer.parseInt(paramString.substring(0, 2)));
    localCalendar.set(12, Integer.parseInt(paramString.substring(3, 5)));
    localCalendar.set(13, Integer.parseInt(paramString.substring(6, 8)));
    int i = 8;
    if ((i < paramString.length()) && (paramString.charAt(i) == '.'))
    {
      int j = 0;
      int k = 100;
      while (true)
      {
        int m = paramString.charAt(++i);
        if ((m < 48) || (m > 57))
          break;
        j += (m - 48) * k;
        k /= 10;
      }
      localCalendar.set(14, j);
    }
    else
    {
      localCalendar.set(14, 0);
    }
    if (i < paramString.length())
      if ((paramString.charAt(i) == '+') || (paramString.charAt(i) == '-'))
        localCalendar.setTimeZone(TimeZone.getTimeZone("GMT" + paramString.substring(i)));
      else if (paramString.charAt(i) == 'Z')
        localCalendar.setTimeZone(TimeZone.getTimeZone("GMT"));
      else
        throw new RuntimeException("illegal time format!");
    return localCalendar.getTime();
  }
}

/* Location:           dulux-signed.jar
 * Qualified Name:     org.kobjects.isodate.IsoDate
 * JD-Core Version:    0.6.2
 */