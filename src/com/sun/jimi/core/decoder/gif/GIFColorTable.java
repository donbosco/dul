/*    */ package com.sun.jimi.core.decoder.gif;
/*    */ 
/*    */ import com.sun.jimi.core.util.LEDataInputStream;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class GIFColorTable
/*    */ {
/*    */   byte[] red;
/*    */   byte[] green;
/*    */   byte[] blue;
/*    */ 
/*    */   public GIFColorTable(int paramInt)
/*    */     throws IOException
/*    */   {
/* 61 */     int i = 1 << paramInt;
/*    */ 
/* 63 */     this.red = new byte[i];
/* 64 */     this.green = this.red;
/* 65 */     this.blue = this.red;
/*    */ 
/* 67 */     this.red[0] = -1;
/* 68 */     for (int j = 1; j < i; j++)
/* 69 */       this.red[j] = ((byte)((j - 1) * 255 / (i - 1)));
/*    */   }
/*    */ 
/*    */   public GIFColorTable(LEDataInputStream paramLEDataInputStream, int paramInt)
/*    */     throws IOException
/*    */   {
/* 39 */     int j = 1 << paramInt;
/*    */ 
/* 41 */     this.red = new byte[j];
/* 42 */     this.green = new byte[j];
/* 43 */     this.blue = new byte[j];
/*    */ 
/* 45 */     for (int i = 0; i < j; i++)
/*    */     {
/* 47 */       this.red[i] = paramLEDataInputStream.readByte();
/* 48 */       this.green[i] = paramLEDataInputStream.readByte();
/* 49 */       this.blue[i] = paramLEDataInputStream.readByte();
/*    */     }
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.decoder.gif.GIFColorTable
 * JD-Core Version:    0.6.2
 */