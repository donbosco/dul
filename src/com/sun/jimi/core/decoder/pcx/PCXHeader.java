/*     */ package com.sun.jimi.core.decoder.pcx;
/*     */ 
/*     */ import com.sun.jimi.core.util.LEDataInputStream;
/*     */ import java.awt.image.ColorModel;
/*     */ import java.io.FilterInputStream;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public class PCXHeader
/*     */ {
/*  29 */   public static byte PC_PAINTBRUSH = 10;
/*     */ 
/*  31 */   public static byte V2_5 = 0;
/*  32 */   public static byte V2_8p = 2;
/*  33 */   public static byte V2_8 = 3;
/*  34 */   public static byte V3_0p = 5;
/*     */ 
/*  36 */   public static byte RLE_ENCODING = 1;
/*     */   private byte manufacturer_;
/*     */   private byte version_;
/*     */   private byte encoding_;
/*     */   private byte depth_;
/*     */   private int width_;
/*     */   private int height_;
/*     */   private int xmin_;
/*     */   private int ymin_;
/*     */   private int xmax_;
/*     */   private int ymax_;
/*     */   private int hres_;
/*     */   private int vres_;
/*     */   private int nPlanes;
/*     */   private int bytesPerLine;
/*     */   private int paletteInfo;
/*     */   private PCXColorMap pcxColorMap;
/*     */ 
/*     */   public PCXHeader(LEDataInputStream paramLEDataInputStream, int paramInt)
/*     */     throws IOException
/*     */   {
/*  60 */     this.manufacturer_ = ((byte)paramLEDataInputStream.readUnsignedByte());
/*     */ 
/*  62 */     this.version_ = ((byte)paramLEDataInputStream.readUnsignedByte());
/*     */ 
/*  64 */     this.encoding_ = ((byte)paramLEDataInputStream.readUnsignedByte());
/*     */ 
/*  66 */     this.depth_ = ((byte)paramLEDataInputStream.readUnsignedByte());
/*     */ 
/*  68 */     this.xmin_ = paramLEDataInputStream.readShort();
/*  69 */     this.ymin_ = paramLEDataInputStream.readShort();
/*  70 */     this.xmax_ = paramLEDataInputStream.readShort();
/*  71 */     this.ymax_ = paramLEDataInputStream.readShort();
/*     */ 
/*  73 */     this.hres_ = paramLEDataInputStream.readShort();
/*  74 */     this.vres_ = paramLEDataInputStream.readShort();
/*     */ 
/*  76 */     this.width_ = (this.xmax_ - this.xmin_ + 1);
/*  77 */     this.height_ = (this.ymax_ - this.ymin_ + 1);
/*     */ 
/*  79 */     this.pcxColorMap = new PCXColorMap(paramLEDataInputStream, paramInt, this.version_);
/*     */ 
/*  83 */     paramLEDataInputStream.skip(1L);
/*     */ 
/*  85 */     this.nPlanes = paramLEDataInputStream.readByte();
/*     */ 
/*  87 */     this.bytesPerLine = paramLEDataInputStream.readShort();
/*     */ 
/*  89 */     this.paletteInfo = paramLEDataInputStream.readShort();
/*     */ 
/*  92 */     paramLEDataInputStream.skip(58L);
/*     */   }
/*     */ 
/*     */   public int getBytesPerLine()
/*     */   {
/* 128 */     return this.bytesPerLine;
/*     */   }
/*     */ 
/*     */   public ColorModel getColorModel()
/*     */   {
/*  99 */     return this.pcxColorMap.getColorModel();
/*     */   }
/*     */ 
/*     */   public int getDepth()
/*     */   {
/* 104 */     return this.depth_;
/*     */   }
/*     */ 
/*     */   public int getHeight()
/*     */   {
/* 123 */     return this.height_;
/*     */   }
/*     */ 
/*     */   public int getPlanes()
/*     */   {
/* 109 */     return this.nPlanes;
/*     */   }
/*     */ 
/*     */   public int getWidth()
/*     */   {
/* 114 */     if (this.width_ % 2 == 0)
/*     */     {
/* 116 */       return this.width_;
/*     */     }
/* 118 */     return this.width_ + 1;
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.decoder.pcx.PCXHeader
 * JD-Core Version:    0.6.2
 */