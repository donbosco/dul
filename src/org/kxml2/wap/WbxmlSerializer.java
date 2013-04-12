package org.kxml2.wap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Hashtable;
import java.util.Vector;
import org.xmlpull.v1.XmlSerializer;

public class WbxmlSerializer
  implements XmlSerializer
{
  Hashtable stringTable = new Hashtable();
  OutputStream out;
  ByteArrayOutputStream buf = new ByteArrayOutputStream();
  ByteArrayOutputStream stringTableBuf = new ByteArrayOutputStream();
  String pending;
  int depth;
  String name;
  String namespace;
  Vector attributes = new Vector();
  Hashtable attrStartTable = new Hashtable();
  Hashtable attrValueTable = new Hashtable();
  Hashtable tagTable = new Hashtable();
  private int attrPage;
  private int tagPage;
  private String encoding = null;

  public XmlSerializer attribute(String paramString1, String paramString2, String paramString3)
  {
    this.attributes.addElement(paramString2);
    this.attributes.addElement(paramString3);
    return this;
  }

  public void cdsect(String paramString)
    throws IOException
  {
    text(paramString);
  }

  public void comment(String paramString)
  {
  }

  public void docdecl(String paramString)
  {
    throw new RuntimeException("Cannot write docdecl for WBXML");
  }

  public void entityRef(String paramString)
  {
    throw new RuntimeException("EntityReference not supported for WBXML");
  }

  public int getDepth()
  {
    return this.depth;
  }

  public boolean getFeature(String paramString)
  {
    return false;
  }

  public String getNamespace()
  {
    throw new RuntimeException("NYI");
  }

  public String getName()
  {
    throw new RuntimeException("NYI");
  }

  public String getPrefix(String paramString, boolean paramBoolean)
  {
    throw new RuntimeException("NYI");
  }

  public Object getProperty(String paramString)
  {
    return null;
  }

  public void ignorableWhitespace(String paramString)
  {
  }

  public void endDocument()
    throws IOException
  {
    writeInt(this.out, this.stringTableBuf.size());
    this.out.write(this.stringTableBuf.toByteArray());
    this.out.write(this.buf.toByteArray());
    this.out.flush();
  }

  public void flush()
  {
  }

  public void checkPending(boolean paramBoolean)
    throws IOException
  {
    if (this.pending == null)
      return;
    int i = this.attributes.size();
    int[] arrayOfInt = (int[])this.tagTable.get(this.pending);
    if (arrayOfInt == null)
    {
      this.buf.write(paramBoolean ? 132 : i == 0 ? 68 : paramBoolean ? 4 : 196);
      writeStrT(this.pending);
    }
    else
    {
      if (arrayOfInt[0] != this.tagPage)
      {
        this.tagPage = arrayOfInt[0];
        this.buf.write(0);
        this.buf.write(this.tagPage);
      }
      this.buf.write(paramBoolean ? arrayOfInt[1] | 0x80 : i == 0 ? arrayOfInt[1] | 0x40 : paramBoolean ? arrayOfInt[1] : arrayOfInt[1] | 0xC0);
    }
    for (int j = 0; j < i; j++)
    {
      arrayOfInt = (int[])this.attrStartTable.get(this.attributes.elementAt(j));
      if (arrayOfInt == null)
      {
        this.buf.write(4);
        writeStrT((String)this.attributes.elementAt(j));
      }
      else
      {
        if (arrayOfInt[0] != this.attrPage)
        {
          this.attrPage = arrayOfInt[1];
          this.buf.write(0);
          this.buf.write(this.attrPage);
        }
        this.buf.write(arrayOfInt[1]);
      }
      arrayOfInt = (int[])this.attrValueTable.get(this.attributes.elementAt(++j));
      if (arrayOfInt == null)
      {
        this.buf.write(3);
        writeStrI(this.buf, (String)this.attributes.elementAt(j));
      }
      else
      {
        if (arrayOfInt[0] != this.attrPage)
        {
          this.attrPage = arrayOfInt[1];
          this.buf.write(0);
          this.buf.write(this.attrPage);
        }
        this.buf.write(arrayOfInt[1]);
      }
    }
    if (i > 0)
      this.buf.write(1);
    this.pending = null;
    this.attributes.removeAllElements();
  }

  public void processingInstruction(String paramString)
  {
    throw new RuntimeException("PI NYI");
  }

  public void setFeature(String paramString, boolean paramBoolean)
  {
    throw new IllegalArgumentException("unknown feature " + paramString);
  }

  public void setOutput(Writer paramWriter)
  {
    throw new RuntimeException("Wbxml requires an OutputStream!");
  }

  public void setOutput(OutputStream paramOutputStream, String paramString)
    throws IOException
  {
    if (paramString != null)
      throw new IllegalArgumentException("encoding not yet supported for WBXML");
    this.out = paramOutputStream;
    this.buf = new ByteArrayOutputStream();
    this.stringTableBuf = new ByteArrayOutputStream();
  }

  public void setPrefix(String paramString1, String paramString2)
  {
    throw new RuntimeException("NYI");
  }

  public void setProperty(String paramString, Object paramObject)
  {
    throw new IllegalArgumentException("unknown property " + paramString);
  }

  public void startDocument(String paramString, Boolean paramBoolean)
    throws IOException
  {
    this.out.write(3);
    this.out.write(1);
    String[] arrayOfString = { "UTF-8", "ISO-8859-1" };
    if ((paramString == null) || (paramString.toUpperCase().equals(arrayOfString[0])))
    {
      this.encoding = arrayOfString[0];
      this.out.write(106);
    }
    else if (true == paramString.toUpperCase().equals(arrayOfString[1]))
    {
      this.encoding = arrayOfString[1];
      this.out.write(4);
    }
    else
    {
      throw new UnsupportedEncodingException(paramString);
    }
  }

  public XmlSerializer startTag(String paramString1, String paramString2)
    throws IOException
  {
    if ((paramString1 != null) && (!"".equals(paramString1)))
      throw new RuntimeException("NSP NYI");
    checkPending(false);
    this.pending = paramString2;
    this.depth += 1;
    return this;
  }

  public XmlSerializer text(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws IOException
  {
    checkPending(false);
    this.buf.write(3);
    writeStrI(this.buf, new String(paramArrayOfChar, paramInt1, paramInt2));
    return this;
  }

  public XmlSerializer text(String paramString)
    throws IOException
  {
    checkPending(false);
    this.buf.write(3);
    writeStrI(this.buf, paramString);
    return this;
  }

  public XmlSerializer endTag(String paramString1, String paramString2)
    throws IOException
  {
    if (this.pending != null)
      checkPending(true);
    else
      this.buf.write(1);
    this.depth -= 1;
    return this;
  }

  public void writeLegacy(int paramInt, String paramString)
  {
  }

  static void writeInt(OutputStream paramOutputStream, int paramInt)
    throws IOException
  {
    byte[] arrayOfByte = new byte[5];
    int i = 0;
    do
    {
      arrayOfByte[(i++)] = ((byte)(paramInt & 0x7F));
      paramInt >>= 7;
    }
    while (paramInt != 0);
    while (i > 1)
      paramOutputStream.write(arrayOfByte[(--i)] | 0x80);
    paramOutputStream.write(arrayOfByte[0]);
  }

  static void writeStrI(OutputStream paramOutputStream, String paramString)
    throws IOException
  {
    for (int i = 0; i < paramString.length(); i++)
      paramOutputStream.write((byte)paramString.charAt(i));
    paramOutputStream.write(0);
  }

  void writeStrT(String paramString)
    throws IOException
  {
    Integer localInteger = (Integer)this.stringTable.get(paramString);
    if (localInteger == null)
    {
      localInteger = new Integer(this.stringTableBuf.size());
      this.stringTable.put(paramString, localInteger);
      writeStrI(this.stringTableBuf, paramString);
      this.stringTableBuf.flush();
    }
    writeInt(this.buf, localInteger.intValue());
  }

  public void setTagTable(int paramInt, String[] paramArrayOfString)
  {
    if (paramInt != 0)
      return;
    for (int i = 0; i < paramArrayOfString.length; i++)
      if (paramArrayOfString[i] != null)
      {
        int[] arrayOfInt = { paramInt, i + 5 };
        this.tagTable.put(paramArrayOfString[i], arrayOfInt);
      }
  }

  public void setAttrStartTable(int paramInt, String[] paramArrayOfString)
  {
    for (int i = 0; i < paramArrayOfString.length; i++)
      if (paramArrayOfString[i] != null)
      {
        int[] arrayOfInt = { paramInt, i + 5 };
        this.attrStartTable.put(paramArrayOfString[i], arrayOfInt);
      }
  }

  public void setAttrValueTable(int paramInt, String[] paramArrayOfString)
  {
    for (int i = 0; i < paramArrayOfString.length; i++)
      if (paramArrayOfString[i] != null)
      {
        int[] arrayOfInt = { paramInt, i + 133 };
        this.attrValueTable.put(paramArrayOfString[i], arrayOfInt);
      }
  }
}

/* Location:           dulux-signed.jar
 * Qualified Name:     org.kxml2.wap.WbxmlSerializer
 * JD-Core Version:    0.6.2
 */