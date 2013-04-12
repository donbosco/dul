/*     */ package com.sun.jimi.core.decoder.ico;
/*     */ 
/*     */ import com.sun.jimi.core.util.LEDataInputStream;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public class IconDirEntry
/*     */ {
/*     */   private short bWidth_;
/*     */   private short bHeight_;
/*     */   private short bColorCount_;
/*     */   private short bReserved_;
/*     */   private int wPlanes_;
/*     */   private int wBitCount_;
/*     */   private long dwBytesInRes_;
/*     */   private long dwImageOffset_;
/*     */ 
/*     */   public IconDirEntry(LEDataInputStream paramLEDataInputStream)
/*     */     throws IOException
/*     */   {
/*  73 */     this.bWidth_ = ((short)paramLEDataInputStream.readUnsignedByte());
/*  74 */     this.bHeight_ = ((short)paramLEDataInputStream.readUnsignedByte());
/*  75 */     this.bColorCount_ = ((short)paramLEDataInputStream.readUnsignedByte());
/*  76 */     this.bReserved_ = ((short)paramLEDataInputStream.readUnsignedByte());
/*     */ 
/*  78 */     this.wPlanes_ = paramLEDataInputStream.readShort();
/*  79 */     this.wBitCount_ = paramLEDataInputStream.readShort();
/*     */ 
/*  81 */     this.dwBytesInRes_ = paramLEDataInputStream.readInt();
/*  82 */     this.dwImageOffset_ = paramLEDataInputStream.readInt();
/*     */   }
/*     */ 
/*     */   public int getColorCount()
/*     */   {
/*  97 */     return this.bColorCount_;
/*     */   }
/*     */ 
/*     */   public int getHeight()
/*     */   {
/*  92 */     return this.bHeight_;
/*     */   }
/*     */ 
/*     */   public long getImageOffset()
/*     */   {
/* 102 */     return this.dwImageOffset_;
/*     */   }
/*     */ 
/*     */   public int getWidth()
/*     */   {
/*  87 */     return this.bWidth_;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 108 */     return "Width:" + getWidth() + 
/* 109 */       " Height: " + getHeight() + 
/* 110 */       " ColorCount: " + getColorCount() + 
/* 111 */       " Offset: " + getImageOffset();
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.decoder.ico.IconDirEntry
 * JD-Core Version:    0.6.2
 */