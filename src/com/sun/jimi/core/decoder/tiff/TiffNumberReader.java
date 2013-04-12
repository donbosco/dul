/*    */ package com.sun.jimi.core.decoder.tiff;
/*    */ 
/*    */ class TiffNumberReader
/*    */ {
/*    */   private int bytePtr;
/*    */   protected byte[] data;
/*    */ 
/*    */   TiffNumberReader(byte[] paramArrayOfByte)
/*    */   {
/* 22 */     this.bytePtr = 0;
/* 23 */     this.data = paramArrayOfByte;
/*    */   }
/*    */ 
/*    */   public final byte readByte()
/*    */   {
/* 28 */     return this.data[(this.bytePtr++)];
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 33 */     return "data len " + this.data.length + " index " + this.bytePtr;
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.decoder.tiff.TiffNumberReader
 * JD-Core Version:    0.6.2
 */