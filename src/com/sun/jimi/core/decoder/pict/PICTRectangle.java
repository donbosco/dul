/*    */ package com.sun.jimi.core.decoder.pict;
/*    */ 
/*    */ import java.io.DataInputStream;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class PICTRectangle
/*    */ {
/*    */   public short tlY;
/*    */   public short tlX;
/*    */   public short brY;
/*    */   public short brX;
/*    */ 
/*    */   public PICTRectangle()
/*    */   {
/* 35 */     this.tlY = 0;
/* 36 */     this.tlX = 0;
/* 37 */     this.brY = 0;
/* 38 */     this.brX = 0;
/*    */   }
/*    */ 
/*    */   public PICTRectangle(DataInputStream paramDataInputStream)
/*    */     throws IOException
/*    */   {
/* 47 */     this.tlY = paramDataInputStream.readShort();
/* 48 */     this.tlX = paramDataInputStream.readShort();
/* 49 */     this.brY = paramDataInputStream.readShort();
/* 50 */     this.brX = paramDataInputStream.readShort();
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 55 */     return "rectangle " + this.tlX + " " + this.tlY + " " + this.brX + " " + this.brY;
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.decoder.pict.PICTRectangle
 * JD-Core Version:    0.6.2
 */