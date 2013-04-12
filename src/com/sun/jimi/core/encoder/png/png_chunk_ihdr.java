/*     */ package com.sun.jimi.core.encoder.png;
/*     */ 
/*     */ import com.sun.jimi.core.JimiException;
/*     */ import com.sun.jimi.core.compat.AdaptiveRasterImage;
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.DirectColorModel;
/*     */ import java.awt.image.IndexColorModel;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.FilterOutputStream;
/*     */ import java.io.IOException;
/*     */ 
/*     */ class png_chunk_ihdr
/*     */   implements PNGConstants
/*     */ {
/*     */   int width;
/*     */   int height;
/*     */   byte bitDepth;
/*     */   byte colorType;
/*     */   byte compressionType;
/*     */   byte filterMethod;
/*     */   byte interlaceMethod;
/*     */   PNGChunkUtil pcu;
/*     */ 
/*     */   png_chunk_ihdr(AdaptiveRasterImage paramAdaptiveRasterImage, PNGChunkUtil paramPNGChunkUtil, PNGEncoder paramPNGEncoder)
/*     */     throws JimiException
/*     */   {
/*  58 */     this.width = paramAdaptiveRasterImage.getWidth();
/*  59 */     this.height = paramAdaptiveRasterImage.getHeight();
/*     */ 
/*  62 */     process(paramAdaptiveRasterImage, paramPNGEncoder);
/*     */ 
/*  66 */     this.compressionType = 0;
/*     */ 
/*  69 */     this.filterMethod = paramPNGEncoder.getFilter();
/*     */ 
/*  72 */     this.interlaceMethod = paramPNGEncoder.getInterlace();
/*     */ 
/*  74 */     this.pcu = paramPNGChunkUtil;
/*     */   }
/*     */ 
/*     */   void process(AdaptiveRasterImage paramAdaptiveRasterImage, PNGEncoder paramPNGEncoder)
/*     */     throws JimiException
/*     */   {
/*  82 */     ColorModel localColorModel = paramAdaptiveRasterImage.getColorModel();
/*  83 */     this.colorType = -1;
/*     */     Object localObject;
/*  85 */     if ((localColorModel instanceof DirectColorModel))
/*     */     {
/*  87 */       localObject = (DirectColorModel)localColorModel;
/*  88 */       int i = ((DirectColorModel)localObject).getRedMask();
/*  89 */       int j = ((DirectColorModel)localObject).getGreenMask();
/*  90 */       int k = ((DirectColorModel)localObject).getBlueMask();
/*  91 */       int m = ((ColorModel)localObject).getPixelSize();
/*  92 */       int n = (1 << m) - 1;
/*     */ 
/*  96 */       if ((m <= 8) && (i == n) && (j == n) && (k == n))
/*     */       {
/*  99 */         this.bitDepth = ((byte)localColorModel.getPixelSize());
/* 100 */         this.colorType = 0;
/*     */       }
/*     */     }
/* 103 */     else if ((localColorModel instanceof IndexColorModel))
/*     */     {
/* 106 */       this.bitDepth = ((byte)localColorModel.getPixelSize());
/* 107 */       this.colorType = 3;
/*     */     }
/*     */ 
/* 110 */     if (this.colorType == -1)
/*     */     {
/* 114 */       paramAdaptiveRasterImage.setRGBDefault(true);
/* 115 */       this.bitDepth = 8;
/*     */ 
/* 117 */       localObject = paramPNGEncoder.getAlpha();
/* 118 */       if (localObject == null)
/*     */       {
/* 121 */         if (paramAdaptiveRasterImage.getAlphaStatus() == 1)
/*     */         {
/* 123 */           this.colorType = 6;
/*     */         }
/*     */         else
/*     */         {
/* 127 */           this.colorType = 2;
/*     */         }
/*     */ 
/*     */       }
/* 131 */       else if (((Boolean)localObject).equals(Boolean.TRUE))
/*     */       {
/* 133 */         this.colorType = 6;
/*     */       }
/*     */       else
/*     */       {
/* 137 */         this.colorType = 2;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   void write(DataOutputStream paramDataOutputStream)
/*     */     throws IOException
/*     */   {
/* 148 */     paramDataOutputStream.writeInt(13);
/* 149 */     paramDataOutputStream.write(PNGConstants.png_IHDR);
/*     */ 
/* 151 */     paramDataOutputStream.writeInt(this.width);
/* 152 */     paramDataOutputStream.writeInt(this.height);
/* 153 */     paramDataOutputStream.writeByte(this.bitDepth);
/* 154 */     paramDataOutputStream.writeByte(this.colorType);
/* 155 */     paramDataOutputStream.writeByte(this.compressionType);
/* 156 */     paramDataOutputStream.writeByte(this.filterMethod);
/* 157 */     paramDataOutputStream.writeByte(this.interlaceMethod);
/*     */ 
/* 159 */     this.pcu.resetCRC();
/* 160 */     this.pcu.updateCRC(PNGConstants.png_IHDR);
/* 161 */     this.pcu.updateCRC(this.width);
/* 162 */     this.pcu.updateCRC(this.height);
/* 163 */     this.pcu.updateCRC(this.bitDepth);
/* 164 */     this.pcu.updateCRC(this.colorType);
/* 165 */     this.pcu.updateCRC(this.compressionType);
/* 166 */     this.pcu.updateCRC(this.filterMethod);
/* 167 */     this.pcu.updateCRC(this.interlaceMethod);
/*     */ 
/* 169 */     paramDataOutputStream.writeInt(this.pcu.getCRC());
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.encoder.png.png_chunk_ihdr
 * JD-Core Version:    0.6.2
 */