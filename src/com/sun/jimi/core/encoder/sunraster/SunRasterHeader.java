/*     */ package com.sun.jimi.core.encoder.sunraster;
/*     */ 
/*     */ import com.sun.jimi.core.compat.AdaptiveRasterImage;
/*     */ import java.awt.image.IndexColorModel;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.FilterOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ 
/*     */ public class SunRasterHeader
/*     */ {
/*     */   protected static final int MAGIC_NUMBER = 1504078485;
/*     */   public static final int TYPE_OLD = 0;
/*     */   public static final int TYPE_STANDARD = 1;
/*     */   public static final int TYPE_BYTE_ENCODED = 2;
/*     */   public static final int TYPE_RGB = 3;
/*     */   public static final int TYPE_TIFF = 4;
/*     */   public static final int TYPE_IFF = 5;
/*     */   public static final int TYPE_EXPERIMENTAL = 65535;
/*     */   public static final int NO_COLOR_MAP = 0;
/*     */   public static final int RGB_COLOR_MAP = 1;
/*     */   public static final int RAW_COLOR_MAP = 2;
/*     */   protected static final int NO_COLORMAP = 0;
/*     */   protected static final int RGB_COLORMAP = 1;
/*     */   protected static final int RAW_COLORMAP = 2;
/*     */   protected AdaptiveRasterImage jimiImage_;
/*     */   protected int width_;
/*     */   protected int height_;
/*     */   protected int depth_;
/*     */   protected int type_;
/*     */   protected int colorMapType_;
/*     */   protected int colorMapLength_;
/*     */   protected IndexColorModel palette_;
/*     */   protected int scanLineSize_;
/*     */ 
/*     */   public SunRasterHeader()
/*     */   {
/*  75 */     this.type_ = 1;
/*  76 */     this.colorMapType_ = 0;
/*     */   }
/*     */ 
/*     */   public SunRasterHeader(AdaptiveRasterImage paramAdaptiveRasterImage)
/*     */   {
/*  86 */     this.width_ = paramAdaptiveRasterImage.getWidth();
/*  87 */     this.height_ = paramAdaptiveRasterImage.getHeight();
/*  88 */     this.type_ = 1;
/*  89 */     this.colorMapType_ = 0;
/*     */   }
/*     */ 
/*     */   public int getDepth()
/*     */   {
/* 107 */     return this.depth_;
/*     */   }
/*     */ 
/*     */   public IndexColorModel getPalette()
/*     */   {
/* 143 */     return this.palette_;
/*     */   }
/*     */ 
/*     */   public int getType()
/*     */   {
/* 125 */     return this.type_;
/*     */   }
/*     */ 
/*     */   public void setDepth(int paramInt)
/*     */   {
/*  98 */     this.depth_ = paramInt;
/*     */   }
/*     */ 
/*     */   public void setPalette(IndexColorModel paramIndexColorModel)
/*     */   {
/* 134 */     this.palette_ = paramIndexColorModel;
/*     */   }
/*     */ 
/*     */   public void setType(int paramInt)
/*     */   {
/* 116 */     this.type_ = paramInt;
/*     */   }
/*     */ 
/*     */   protected void writePaletteInfoTo(DataOutputStream paramDataOutputStream)
/*     */     throws IOException
/*     */   {
/* 180 */     if (this.palette_ == null)
/*     */     {
/* 183 */       paramDataOutputStream.writeInt(0);
/*     */ 
/* 185 */       paramDataOutputStream.writeInt(0);
/*     */     }
/*     */     else
/*     */     {
/* 190 */       byte[] arrayOfByte = new byte[this.palette_.getMapSize()];
/*     */ 
/* 192 */       paramDataOutputStream.writeInt(1);
/*     */ 
/* 194 */       paramDataOutputStream.writeInt(arrayOfByte.length * 3);
/*     */ 
/* 197 */       this.palette_.getReds(arrayOfByte);
/* 198 */       paramDataOutputStream.write(arrayOfByte);
/*     */ 
/* 201 */       this.palette_.getGreens(arrayOfByte);
/* 202 */       paramDataOutputStream.write(arrayOfByte);
/*     */ 
/* 205 */       this.palette_.getBlues(arrayOfByte);
/* 206 */       paramDataOutputStream.write(arrayOfByte);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void writeTo(OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/* 152 */     DataOutputStream localDataOutputStream = new DataOutputStream(paramOutputStream);
/*     */ 
/* 155 */     localDataOutputStream.writeInt(1504078485);
/*     */ 
/* 157 */     localDataOutputStream.writeInt(this.width_);
/*     */ 
/* 159 */     localDataOutputStream.writeInt(this.height_);
/*     */ 
/* 161 */     localDataOutputStream.writeInt(this.depth_);
/*     */ 
/* 164 */     localDataOutputStream.writeInt(0);
/*     */ 
/* 166 */     localDataOutputStream.writeInt(this.type_);
/*     */ 
/* 168 */     writePaletteInfoTo(localDataOutputStream);
/* 169 */     localDataOutputStream.flush();
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.encoder.sunraster.SunRasterHeader
 * JD-Core Version:    0.6.2
 */