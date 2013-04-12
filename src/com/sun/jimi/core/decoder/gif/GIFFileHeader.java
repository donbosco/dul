/*    */ package com.sun.jimi.core.decoder.gif;
/*    */ 
/*    */ import com.sun.jimi.core.JimiException;
/*    */ import com.sun.jimi.core.util.LEDataInputStream;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class GIFFileHeader
/*    */ {
/* 31 */   byte[] sig = new byte[3];
/* 32 */   byte[] ver = new byte[3];
/*    */   int screenWidth;
/*    */   int screenHeight;
/*    */   byte packed;
/*    */   byte backgroundColor;
/*    */   byte aspectRatio;
/*    */   static final int CT_SIZE = 7;
/*    */   static final int CT_SORT = 8;
/*    */   static final int CT_RES = 112;
/*    */   static final int CT_GLOBAL = 128;
/*    */   int colorTableNumBits;
/*    */   GIFColorTable colorTable;
/*    */ 
/*    */   public GIFFileHeader(LEDataInputStream paramLEDataInputStream)
/*    */     throws JimiException, IOException
/*    */   {
/* 54 */     this.sig[0] = paramLEDataInputStream.readByte();
/* 55 */     this.sig[1] = paramLEDataInputStream.readByte();
/* 56 */     this.sig[2] = paramLEDataInputStream.readByte();
/*    */ 
/* 58 */     if ((this.sig[0] != 71) || (this.sig[1] != 73) || (this.sig[2] != 70)) {
/* 59 */       throw new JimiException("Not a GIF");
/*    */     }
/* 61 */     this.ver[0] = paramLEDataInputStream.readByte();
/* 62 */     this.ver[1] = paramLEDataInputStream.readByte();
/* 63 */     this.ver[2] = paramLEDataInputStream.readByte();
/*    */ 
/* 65 */     if ((this.ver[0] != 56) || ((this.ver[1] != 55) && (this.ver[1] != 57)) || (this.ver[2] != 97)) {
/* 66 */       throw new JimiException("Unknown GIF version");
/*    */     }
/* 68 */     this.screenWidth = paramLEDataInputStream.readUnsignedShort();
/* 69 */     this.screenHeight = paramLEDataInputStream.readUnsignedShort();
/* 70 */     this.packed = paramLEDataInputStream.readByte();
/*    */ 
/* 72 */     this.backgroundColor = paramLEDataInputStream.readByte();
/* 73 */     this.aspectRatio = paramLEDataInputStream.readByte();
/*    */ 
/* 76 */     this.colorTableNumBits = ((this.packed & 0x7) + 1);
/*    */ 
/* 78 */     if ((this.packed & 0x80) != 0)
/* 79 */       this.colorTable = new GIFColorTable(paramLEDataInputStream, this.colorTableNumBits);
/*    */     else
/* 81 */       this.colorTable = new GIFColorTable(this.colorTableNumBits);
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 88 */     return "GIF Header " + this.screenWidth + " " + this.screenHeight;
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.decoder.gif.GIFFileHeader
 * JD-Core Version:    0.6.2
 */