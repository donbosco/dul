/*    */ package com.sun.jimi.core.decoder.gif;
/*    */ 
/*    */ import com.sun.jimi.core.util.LEDataInputStream;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class GIFImageDescriptor
/*    */ {
/*    */   int left;
/*    */   int top;
/*    */   int width;
/*    */   int height;
/*    */   byte packed;
/*    */   static final int LOCAL_CT_FLAG = 128;
/*    */   static final int INTERLACE_FLAG = 64;
/*    */   static final int SORT_FLAG = 32;
/*    */   static final int RESERVED = 24;
/*    */   static final int LOCAL_CT_SIZE = 7;
/*    */   int colorTableNumBits;
/*    */   boolean interlace;
/*    */   GIFColorTable colorTable;
/*    */ 
/*    */   public GIFImageDescriptor(LEDataInputStream paramLEDataInputStream)
/*    */     throws IOException
/*    */   {
/* 54 */     this.left = paramLEDataInputStream.readUnsignedShort();
/* 55 */     this.top = paramLEDataInputStream.readUnsignedShort();
/* 56 */     this.width = paramLEDataInputStream.readUnsignedShort();
/* 57 */     this.height = paramLEDataInputStream.readUnsignedShort();
/*    */ 
/* 59 */     this.packed = paramLEDataInputStream.readByte();
/*    */ 
/* 61 */     if ((this.packed & 0x80) != 0)
/*    */     {
/* 63 */       this.colorTableNumBits = ((this.packed & 0x7) + 1);
/* 64 */       this.colorTable = new GIFColorTable(paramLEDataInputStream, this.colorTableNumBits);
/*    */     }
/*    */     else {
/* 67 */       this.colorTableNumBits = 0;
/*    */     }
/* 69 */     this.interlace = ((this.packed & 0x40) != 0);
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 76 */     return "GIF Image Descriptor " + this.left + " " + this.top + " " + this.width + " " + this.height + " interlace " + this.interlace;
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.decoder.gif.GIFImageDescriptor
 * JD-Core Version:    0.6.2
 */