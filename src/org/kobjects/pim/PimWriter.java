package org.kobjects.pim;

import java.io.IOException;
import java.io.Writer;
import java.util.Enumeration;

public class PimWriter
{
  Writer writer;

  public PimWriter(Writer paramWriter)
  {
    this.writer = paramWriter;
  }

  public void writeEntry(PimItem paramPimItem)
    throws IOException
  {
    this.writer.write("begin:");
    this.writer.write(paramPimItem.getType());
    this.writer.write("\r\n");
    Enumeration localEnumeration = paramPimItem.fieldNames();
    while (localEnumeration.hasMoreElements())
    {
      String str = (String)localEnumeration.nextElement();
      for (int i = 0; i < paramPimItem.getFieldCount(str); i++)
      {
        PimField localPimField = paramPimItem.getField(str, i);
        this.writer.write(str);
        this.writer.write(58);
        this.writer.write(localPimField.getValue().toString());
        this.writer.write("\r\n");
      }
    }
    this.writer.write("end:");
    this.writer.write(paramPimItem.getType());
    this.writer.write("\r\n\r\n");
  }
}

/* Location:           dulux-signed.jar
 * Qualified Name:     org.kobjects.pim.PimWriter
 * JD-Core Version:    0.6.2
 */