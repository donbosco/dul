/*    */ package com.sun.jimi.core.decoder.bmp;
/*    */ 
/*    */ import com.sun.jimi.core.util.LEDataInputStream;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class BMPColorMap
/*    */ {
/*    */   int noOfEntries;
/*    */   byte[] r;
/*    */   byte[] g;
/*    */   byte[] b;
/*    */ 
/*    */   public BMPColorMap(LEDataInputStream paramLEDataInputStream, BMPFileHeader paramBMPFileHeader)
/*    */     throws IOException
/*    */   {
/* 35 */     this.noOfEntries = paramBMPFileHeader.actualColorsUsed;
/*    */ 
/* 37 */     this.r = new byte[this.noOfEntries];
/* 38 */     this.g = new byte[this.noOfEntries];
/* 39 */     this.b = new byte[this.noOfEntries];
/*    */ 
/* 41 */     if (this.noOfEntries > 0)
/*    */     {
/* 43 */       for (int i = 0; i < this.noOfEntries; i++)
/*    */       {
/* 45 */         this.b[i] = paramLEDataInputStream.readByte();
/* 46 */         this.g[i] = paramLEDataInputStream.readByte();
/* 47 */         this.r[i] = paramLEDataInputStream.readByte();
/*    */ 
/* 49 */         if ((paramBMPFileHeader.bmpVersion == 3) || (paramBMPFileHeader.bmpVersion == 4))
/* 50 */           paramLEDataInputStream.readByte();
/*    */       }
/*    */     }
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.decoder.bmp.BMPColorMap
 * JD-Core Version:    0.6.2
 */