/*    */ package com.sun.jimi.core.decoder.sunraster;
/*    */ 
/*    */ import com.sun.jimi.core.JimiException;
/*    */ import java.io.DataInputStream;
/*    */ import java.io.IOException;
/*    */ 
/*    */ class SunRasterColorMap
/*    */ {
/*    */   int MapLength;
/*    */   int tableLength;
/*    */   byte[] r;
/*    */   byte[] g;
/*    */   byte[] b;
/*    */   byte[] raw;
/*    */   boolean RGBType;
/*    */ 
/*    */   SunRasterColorMap(DataInputStream paramDataInputStream, SunRasterHeader paramSunRasterHeader)
/*    */     throws IOException, JimiException
/*    */   {
/* 32 */     this.MapLength = paramSunRasterHeader.ColorMapLength;
/*    */ 
/* 34 */     if (paramSunRasterHeader.ColorMapType == 2)
/*    */     {
/* 36 */       this.RGBType = false;
/* 37 */       this.tableLength = this.MapLength;
/* 38 */       this.raw = new byte[this.tableLength];
/* 39 */       paramDataInputStream.readFully(this.raw, 0, this.tableLength);
/*    */     }
/*    */     else
/*    */     {
/* 43 */       this.RGBType = true;
/*    */ 
/* 45 */       this.tableLength = (this.MapLength / 3);
/*    */ 
/* 47 */       this.r = new byte[this.tableLength];
/* 48 */       this.g = new byte[this.tableLength];
/* 49 */       this.b = new byte[this.tableLength];
/*    */ 
/* 51 */       paramDataInputStream.readFully(this.r, 0, this.tableLength);
/* 52 */       paramDataInputStream.readFully(this.g, 0, this.tableLength);
/* 53 */       paramDataInputStream.readFully(this.b, 0, this.tableLength);
/*    */     }
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.decoder.sunraster.SunRasterColorMap
 * JD-Core Version:    0.6.2
 */