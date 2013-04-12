/*    */ package com.sun.jimi.core.decoder.psd;
/*    */ 
/*    */ import java.io.DataInputStream;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class PSDLayersMasks
/*    */ {
/*    */   int length;
/*    */ 
/*    */   PSDLayersMasks(DataInputStream paramDataInputStream)
/*    */     throws IOException
/*    */   {
/* 32 */     this.length = paramDataInputStream.readInt();
/* 33 */     if (this.length > 0)
/*    */     {
/* 35 */       paramDataInputStream.skipBytes(this.length);
/*    */     }
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 41 */     return "PSDLayersMasks length: " + this.length;
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.decoder.psd.PSDLayersMasks
 * JD-Core Version:    0.6.2
 */