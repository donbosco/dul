package org.kobjects.rss;

import java.io.IOException;
import java.io.Reader;
import org.kobjects.xml.XmlReader;

public class RssReader
{
  public static final int TITLE = 0;
  public static final int LINK = 1;
  public static final int DESCRIPTION = 2;
  public static final int DATE = 3;
  public static final int AUTHOR = 4;
  XmlReader xr;

  public RssReader(Reader paramReader)
    throws IOException
  {
    this.xr = new XmlReader(paramReader);
  }

  void readText(StringBuffer paramStringBuffer)
    throws IOException
  {
    while (this.xr.next() != 3)
      switch (this.xr.getType())
      {
      case 4:
        paramStringBuffer.append(this.xr.getText());
        break;
      case 2:
        readText(paramStringBuffer);
      }
  }

  public String[] next()
    throws IOException
  {
    String[] arrayOfString = new String[5];
    while (this.xr.next() != 1)
      if (this.xr.getType() == 2)
      {
        String str1 = this.xr.getName().toLowerCase();
        if ((str1.equals("item")) || (str1.endsWith(":item")))
        {
          while (this.xr.next() != 3)
            if (this.xr.getType() == 2)
            {
              String str2 = this.xr.getName().toLowerCase();
              int i = str2.indexOf(":");
              if (i != -1)
                str2 = str2.substring(i + 1);
              StringBuffer localStringBuffer = new StringBuffer();
              readText(localStringBuffer);
              String str3 = localStringBuffer.toString();
              if (str2.equals("title"))
                arrayOfString[0] = str3;
              else if (str2.equals("link"))
                arrayOfString[1] = str3;
              else if (str2.equals("description"))
                arrayOfString[2] = str3;
              else if (str2.equals("date"))
                arrayOfString[3] = str3;
              else if (str2.equals("author"))
                arrayOfString[4] = str3;
            }
          return arrayOfString;
        }
      }
    return null;
  }
}

/* Location:           dulux-signed.jar
 * Qualified Name:     org.kobjects.rss.RssReader
 * JD-Core Version:    0.6.2
 */