/*    */ package com.sun.jimi.core.decoder.ico;
/*    */ 
/*    */ import com.sun.jimi.core.util.LEDataInputStream;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class BitmapInfoHeader
/*    */ {
/*    */   private long biSize_;
/*    */   private long biWidth_;
/*    */   private long biHeight_;
/*    */   private int biPlanes_;
/*    */   private int biBitCount_;
/*    */   private long biCompression_;
/*    */   private long biSizeImage_;
/*    */   private long biXPelsPerMeter_;
/*    */   private long biYPelsPerMeter_;
/*    */   private long biClrUsed_;
/*    */   private long biClrImportant_;
/*    */ 
/*    */   public BitmapInfoHeader(LEDataInputStream paramLEDataInputStream)
/*    */     throws IOException
/*    */   {
/* 50 */     this.biSize_ = paramLEDataInputStream.readInt();
/*    */ 
/* 52 */     this.biWidth_ = paramLEDataInputStream.readInt();
/*    */ 
/* 54 */     this.biHeight_ = paramLEDataInputStream.readInt();
/*    */ 
/* 56 */     this.biPlanes_ = paramLEDataInputStream.readShort();
/*    */ 
/* 58 */     this.biBitCount_ = paramLEDataInputStream.readShort();
/*    */ 
/* 60 */     this.biCompression_ = paramLEDataInputStream.readInt();
/*    */ 
/* 62 */     this.biSizeImage_ = paramLEDataInputStream.readInt();
/*    */ 
/* 64 */     this.biXPelsPerMeter_ = paramLEDataInputStream.readInt();
/*    */ 
/* 66 */     this.biYPelsPerMeter_ = paramLEDataInputStream.readInt();
/*    */ 
/* 68 */     this.biClrUsed_ = paramLEDataInputStream.readInt();
/*    */ 
/* 70 */     this.biClrImportant_ = paramLEDataInputStream.readInt();
/*    */   }
/*    */ 
/*    */   public int getBitCount()
/*    */   {
/* 75 */     return this.biBitCount_;
/*    */   }
/*    */ 
/*    */   public long getHeight()
/*    */   {
/* 85 */     return this.biHeight_;
/*    */   }
/*    */ 
/*    */   public long getWidth()
/*    */   {
/* 80 */     return this.biWidth_;
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 91 */     return "Width:" + this.biWidth_ + " Height: " + this.biHeight_;
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.decoder.ico.BitmapInfoHeader
 * JD-Core Version:    0.6.2
 */