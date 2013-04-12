/*    */ package com.sun.jimi.core.decoder.gif;
/*    */ 
/*    */ import com.sun.jimi.core.util.LEDataInputStream;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class GIFGraphicExt
/*    */ {
/*    */   byte blockSize;
/*    */   byte packed;
/*    */   int delayTime;
/*    */   int colorIndex;
/*    */   byte terminator;
/*    */   public static final int GCE_TRANSPARENT = 1;
/*    */   public static final int GCE_USERINPUT = 2;
/*    */   public static final int GCE_DISPOSAL = 28;
/*    */   public static final int GCE_DISPOSAL_BITOFFSET = 2;
/*    */   public static final int GCE_RESERVED = 224;
/*    */   public static final int GCE_DISP_NOTSPECIFIED = 0;
/*    */   public static final int GCE_DISP_LEAVE = 1;
/*    */   public static final int GCE_DISP_BACKGROUND = 2;
/*    */   public static final int GCE_DISP_PREVGRAPHIC = 3;
/*    */ 
/*    */   public GIFGraphicExt(LEDataInputStream paramLEDataInputStream)
/*    */     throws IOException
/*    */   {
/* 48 */     this.blockSize = paramLEDataInputStream.readByte();
/* 49 */     this.packed = paramLEDataInputStream.readByte();
/* 50 */     this.delayTime = paramLEDataInputStream.readUnsignedShort();
/* 51 */     this.colorIndex = paramLEDataInputStream.readUnsignedByte();
/* 52 */     this.terminator = paramLEDataInputStream.readByte();
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 57 */     return "Graphic Extension " + this.packed;
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.decoder.gif.GIFGraphicExt
 * JD-Core Version:    0.6.2
 */