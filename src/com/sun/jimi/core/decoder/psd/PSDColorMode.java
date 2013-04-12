/*    */ package com.sun.jimi.core.decoder.psd;
/*    */ 
/*    */ import com.sun.jimi.core.JimiException;
/*    */ import java.io.DataInputStream;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class PSDColorMode
/*    */ {
/*    */   int length;
/*    */   byte[] data;
/*    */   byte[] cmap;
/*    */ 
/*    */   PSDColorMode(DataInputStream paramDataInputStream, PSDFileHeader paramPSDFileHeader)
/*    */     throws JimiException, IOException
/*    */   {
/* 68 */     this.length = paramDataInputStream.readInt();
/* 69 */     if (this.length > 0)
/*    */     {
/* 71 */       this.data = new byte[this.length];
/* 72 */       paramDataInputStream.read(this.data, 0, this.length);
/*    */ 
/* 74 */       if (paramPSDFileHeader.mode == 2)
/* 75 */         buildCMAP();
/*    */     }
/*    */   }
/*    */ 
/*    */   private void buildCMAP()
/*    */   {
/* 49 */     if (this.cmap == null)
/*    */     {
/* 53 */       this.cmap = new byte[this.data.length];
/* 54 */       int k = this.data.length / 3;
/* 55 */       int m = 2 * k;
/* 56 */       int i = 0; for (int j = 0; i < k; j += 3)
/*    */       {
/* 58 */         this.cmap[j] = this.data[i];
/* 59 */         this.cmap[(j + 1)] = this.data[(i + k)];
/* 60 */         this.cmap[(j + 2)] = this.data[(i + m)];
/*    */ 
/* 56 */         i++;
/*    */       }
/*    */     }
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 81 */     return "PSDColorMode length " + this.length;
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.decoder.psd.PSDColorMode
 * JD-Core Version:    0.6.2
 */