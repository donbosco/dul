/*    */ package com.sun.jimi.core.decoder.pict;
/*    */ 
/*    */ import java.io.DataInputStream;
/*    */ import java.io.IOException;
/*    */ 
/*    */ class PICTBitmap
/*    */ {
/*    */   PICTRectangle bounding;
/*    */   PICTRectangle source;
/*    */   PICTRectangle dest;
/*    */   short mode;
/*    */ 
/*    */   PICTBitmap(DataInputStream paramDataInputStream)
/*    */     throws IOException
/*    */   {
/* 46 */     this.bounding = new PICTRectangle(paramDataInputStream);
/* 47 */     this.source = new PICTRectangle(paramDataInputStream);
/* 48 */     this.dest = new PICTRectangle(paramDataInputStream);
/* 49 */     this.mode = paramDataInputStream.readShort();
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 54 */     return "bounding " + this.bounding.toString() + 
/* 55 */       " source " + this.bounding.toString() + 
/* 56 */       " dest " + this.bounding.toString() + 
/* 57 */       " mode " + this.mode;
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.decoder.pict.PICTBitmap
 * JD-Core Version:    0.6.2
 */