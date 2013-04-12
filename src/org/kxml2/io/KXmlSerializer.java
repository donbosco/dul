package org.kxml2.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import org.xmlpull.v1.XmlSerializer;

public class KXmlSerializer
  implements XmlSerializer
{
  private Writer writer;
  private boolean pending;
  private int auto;
  private int depth;
  private String[] elementStack = new String[12];
  private int[] nspCounts = new int[4];
  private String[] nspStack = new String[8];
  private boolean[] indent = new boolean[4];
  private boolean unicode;
  private String encoding;

  private final void check(boolean paramBoolean)
    throws IOException
  {
    if (!this.pending)
      return;
    this.depth += 1;
    this.pending = false;
    if (this.indent.length <= this.depth)
    {
      boolean[] arrayOfBoolean = new boolean[this.depth + 4];
      System.arraycopy(this.indent, 0, arrayOfBoolean, 0, this.depth);
      this.indent = arrayOfBoolean;
    }
    this.indent[this.depth] = this.indent[(this.depth - 1)];
    for (int i = this.nspCounts[(this.depth - 1)]; i < this.nspCounts[this.depth]; i++)
    {
      this.writer.write(32);
      this.writer.write("xmlns");
      if (!"".equals(this.nspStack[(i * 2)]))
      {
        this.writer.write(58);
        this.writer.write(this.nspStack[(i * 2)]);
      }
      else if (("".equals(getNamespace())) && (!"".equals(this.nspStack[(i * 2 + 1)])))
      {
        throw new IllegalStateException("Cannot set default namespace for elements in no namespace");
      }
      this.writer.write("=\"");
      writeEscaped(this.nspStack[(i * 2 + 1)], 34);
      this.writer.write(34);
    }
    if (this.nspCounts.length <= this.depth + 1)
    {
      int[] arrayOfInt = new int[this.depth + 8];
      System.arraycopy(this.nspCounts, 0, arrayOfInt, 0, this.depth + 1);
      this.nspCounts = arrayOfInt;
    }
    this.nspCounts[(this.depth + 1)] = this.nspCounts[this.depth];
    this.writer.write(paramBoolean ? " />" : ">");
  }

  private final void writeEscaped(String paramString, int paramInt)
    throws IOException
  {
    for (int i = 0; i < paramString.length(); i++)
    {
      int j = paramString.charAt(i);
      switch (j)
      {
      case 9:
      case 10:
      case 13:
        if (paramInt == -1)
          this.writer.write(j);
        else
          this.writer.write("&#" + j + ';');
        break;
      case 38:
        this.writer.write("&amp;");
        break;
      case 62:
        this.writer.write("&gt;");
        break;
      case 60:
        this.writer.write("&lt;");
        break;
      case 34:
      case 39:
        if (j == paramInt)
          this.writer.write(j == 34 ? "&quot;" : "&apos;");
        break;
      }
      if ((j >= 32) && (j != 64) && ((j < 127) || (this.unicode)))
        this.writer.write(j);
      else
        this.writer.write("&#" + j + ";");
    }
  }

  public void docdecl(String paramString)
    throws IOException
  {
    this.writer.write("<!DOCTYPE");
    this.writer.write(paramString);
    this.writer.write(">");
  }

  public void endDocument()
    throws IOException
  {
    while (this.depth > 0)
      endTag(this.elementStack[(this.depth * 3 - 3)], this.elementStack[(this.depth * 3 - 1)]);
    flush();
  }

  public void entityRef(String paramString)
    throws IOException
  {
    check(false);
    this.writer.write(38);
    this.writer.write(paramString);
    this.writer.write(59);
  }

  public boolean getFeature(String paramString)
  {
    return "http://xmlpull.org/v1/doc/features.html#indent-output".equals(paramString) ? this.indent[this.depth] : false;
  }

  public String getPrefix(String paramString, boolean paramBoolean)
  {
    try
    {
      return getPrefix(paramString, false, paramBoolean);
    }
    catch (IOException localIOException)
    {
      throw new RuntimeException(localIOException.toString());
    }
  }

  private final String getPrefix(String paramString, boolean paramBoolean1, boolean paramBoolean2)
    throws IOException
  {
    for (int i = this.nspCounts[(this.depth + 1)] * 2 - 2; i >= 0; i -= 2)
      if ((this.nspStack[(i + 1)].equals(paramString)) && ((paramBoolean1) || (!this.nspStack[i].equals(""))))
      {
        String str2 = this.nspStack[i];
        for (int k = i + 2; k < this.nspCounts[(this.depth + 1)] * 2; k++)
          if (this.nspStack[k].equals(str2))
          {
            str2 = null;
            break;
          }
        if (str2 != null)
          return str2;
      }
    if (!paramBoolean2)
      return null;
    String str1;
    if ("".equals(paramString))
      str1 = "";
    else
      do
      {
        str1 = "n" + this.auto++;
        for (int j = this.nspCounts[(this.depth + 1)] * 2 - 2; j >= 0; j -= 2)
          if (str1.equals(this.nspStack[j]))
          {
            str1 = null;
            break;
          }
      }
      while (str1 == null);
    boolean bool = this.pending;
    this.pending = false;
    setPrefix(str1, paramString);
    this.pending = bool;
    return str1;
  }

  public Object getProperty(String paramString)
  {
    throw new RuntimeException("Unsupported property");
  }

  public void ignorableWhitespace(String paramString)
    throws IOException
  {
    text(paramString);
  }

  public void setFeature(String paramString, boolean paramBoolean)
  {
    if ("http://xmlpull.org/v1/doc/features.html#indent-output".equals(paramString))
      this.indent[this.depth] = paramBoolean;
    else
      throw new RuntimeException("Unsupported Feature");
  }

  public void setProperty(String paramString, Object paramObject)
  {
    throw new RuntimeException("Unsupported Property:" + paramObject);
  }

  public void setPrefix(String paramString1, String paramString2)
    throws IOException
  {
    check(false);
    if (paramString1 == null)
      paramString1 = "";
    if (paramString2 == null)
      paramString2 = "";
    String str = getPrefix(paramString2, true, false);
    if (paramString1.equals(str))
      return;
    int tmp46_45 = (this.depth + 1);
    int[] tmp46_37 = this.nspCounts;
    int tmp48_47 = tmp46_37[tmp46_45];
    tmp46_37[tmp46_45] = (tmp48_47 + 1);
    int i = tmp48_47 << 1;
    if (this.nspStack.length < i + 1)
    {
      String[] arrayOfString = new String[this.nspStack.length + 16];
      System.arraycopy(this.nspStack, 0, arrayOfString, 0, i);
      this.nspStack = arrayOfString;
    }
    this.nspStack[(i++)] = paramString1;
    this.nspStack[i] = paramString2;
  }

  public void setOutput(Writer paramWriter)
  {
    this.writer = paramWriter;
    this.nspCounts[0] = 2;
    this.nspCounts[1] = 2;
    this.nspStack[0] = "";
    this.nspStack[1] = "";
    this.nspStack[2] = "xml";
    this.nspStack[3] = "http://www.w3.org/XML/1998/namespace";
    this.pending = false;
    this.auto = 0;
    this.depth = 0;
    this.unicode = false;
  }

  public void setOutput(OutputStream paramOutputStream, String paramString)
    throws IOException
  {
    if (paramOutputStream == null)
      throw new IllegalArgumentException();
    setOutput(paramString == null ? new OutputStreamWriter(paramOutputStream) : new OutputStreamWriter(paramOutputStream, paramString));
    this.encoding = paramString;
    if ((paramString != null) && (paramString.toLowerCase().startsWith("utf")))
      this.unicode = true;
  }

  public void startDocument(String paramString, Boolean paramBoolean)
    throws IOException
  {
    this.writer.write("<?xml version='1.0' ");
    if (paramString != null)
    {
      this.encoding = paramString;
      if (paramString.toLowerCase().startsWith("utf"))
        this.unicode = true;
    }
    if (this.encoding != null)
    {
      this.writer.write("encoding='");
      this.writer.write(this.encoding);
      this.writer.write("' ");
    }
    if (paramBoolean != null)
    {
      this.writer.write("standalone='");
      this.writer.write(paramBoolean.booleanValue() ? "yes" : "no");
      this.writer.write("' ");
    }
    this.writer.write("?>");
  }

  public XmlSerializer startTag(String paramString1, String paramString2)
    throws IOException
  {
    check(false);
    if (this.indent[this.depth])
    {
      this.writer.write("\r\n");
      for (int i = 0; i < this.depth; i++)
        this.writer.write("  ");
    }
    int i = this.depth * 3;
    if (this.elementStack.length < i + 3)
    {
      String[] localObject = new String[this.elementStack.length + 12];
      System.arraycopy(this.elementStack, 0, localObject, 0, i);
      this.elementStack = ((String[])localObject);
    }
    Object localObject = paramString1 == null ? "" : getPrefix(paramString1, true, true);
    if ("".equals(paramString1))
      for (int j = this.nspCounts[this.depth]; j < this.nspCounts[(this.depth + 1)]; j++)
        if (("".equals(this.nspStack[(j * 2)])) && (!"".equals(this.nspStack[(j * 2 + 1)])))
          throw new IllegalStateException("Cannot set default namespace for elements in no namespace");
    this.elementStack[(i++)] = paramString1;
    this.elementStack[(i++)] = (String) localObject;
    this.elementStack[i] = paramString2;
    this.writer.write(60);
    if (!"".equals(localObject))
    {
      this.writer.write((String)localObject);
      this.writer.write(58);
    }
    this.writer.write(paramString2);
    this.pending = true;
    return this;
  }

  public XmlSerializer attribute(String paramString1, String paramString2, String paramString3)
    throws IOException
  {
    if (!this.pending)
      throw new IllegalStateException("illegal position for attribute");
    if (paramString1 == null)
      paramString1 = "";
    String str = "".equals(paramString1) ? "" : getPrefix(paramString1, false, true);
    this.writer.write(32);
    if (!"".equals(str))
    {
      this.writer.write(str);
      this.writer.write(58);
    }
    this.writer.write(paramString2);
    this.writer.write(61);
    int i = paramString3.indexOf('"') == -1 ? 34 : 39;
    this.writer.write(i);
    writeEscaped(paramString3, i);
    this.writer.write(i);
    return this;
  }

  public void flush()
    throws IOException
  {
    check(false);
    this.writer.flush();
  }

  public XmlSerializer endTag(String paramString1, String paramString2)
    throws IOException
  {
    if (!this.pending)
      this.depth -= 1;
    if (((paramString1 == null) && (this.elementStack[(this.depth * 3)] != null)) || ((paramString1 != null) && (!paramString1.equals(this.elementStack[(this.depth * 3)]))) || (!this.elementStack[(this.depth * 3 + 2)].equals(paramString2)))
      throw new IllegalArgumentException("</{" + paramString1 + "}" + paramString2 + "> does not match start");
    if (this.pending)
    {
      check(true);
      this.depth -= 1;
    }
    else
    {
      if (this.indent[(this.depth + 1)])
      {
        this.writer.write("\r\n");
        for (int i = 0; i < this.depth; i++)
          this.writer.write("  ");
      }
      this.writer.write("</");
      String str = this.elementStack[(this.depth * 3 + 1)];
      if (!"".equals(str))
      {
        this.writer.write(str);
        this.writer.write(58);
      }
      this.writer.write(paramString2);
      this.writer.write(62);
    }
    this.nspCounts[(this.depth + 1)] = this.nspCounts[this.depth];
    return this;
  }

  public String getNamespace()
  {
    return getDepth() == 0 ? null : this.elementStack[(getDepth() * 3 - 3)];
  }

  public String getName()
  {
    return getDepth() == 0 ? null : this.elementStack[(getDepth() * 3 - 1)];
  }

  public int getDepth()
  {
    return this.pending ? this.depth + 1 : this.depth;
  }

  public XmlSerializer text(String paramString)
    throws IOException
  {
    check(false);
    this.indent[this.depth] = false;
    writeEscaped(paramString, -1);
    return this;
  }

  public XmlSerializer text(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws IOException
  {
    text(new String(paramArrayOfChar, paramInt1, paramInt2));
    return this;
  }

  public void cdsect(String paramString)
    throws IOException
  {
    check(false);
    this.writer.write("<![CDATA[");
    this.writer.write(paramString);
    this.writer.write("]]>");
  }

  public void comment(String paramString)
    throws IOException
  {
    check(false);
    this.writer.write("<!--");
    this.writer.write(paramString);
    this.writer.write("-->");
  }

  public void processingInstruction(String paramString)
    throws IOException
  {
    check(false);
    this.writer.write("<?");
    this.writer.write(paramString);
    this.writer.write("?>");
  }
}

/* Location:           dulux-signed.jar
 * Qualified Name:     org.kxml2.io.KXmlSerializer
 * JD-Core Version:    0.6.2
 */