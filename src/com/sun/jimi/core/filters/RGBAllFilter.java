/*     */ package com.sun.jimi.core.filters;
/*     */ 
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.ImageConsumer;
/*     */ import java.awt.image.ImageFilter;
/*     */ import java.awt.image.ImageProducer;
/*     */ 
/*     */ public abstract class RGBAllFilter extends ImageFilterPlus
/*     */ {
/*  85 */   private int width = -1; private int height = -1;
/*  86 */   private int[][] rgbPixels = null;
/*     */ 
/*     */   public RGBAllFilter(ImageProducer paramImageProducer)
/*     */   {
/*  90 */     super(paramImageProducer);
/*     */   }
/*     */ 
/*     */   public abstract void filterRGBAll(int paramInt1, int paramInt2, int[][] paramArrayOfInt);
/*     */ 
/*     */   public synchronized void imageComplete(int paramInt)
/*     */   {
/* 156 */     if ((paramInt == 1) || 
/* 157 */       (paramInt == 4))
/*     */     {
/* 159 */       super.imageComplete(paramInt);
/* 160 */       return;
/*     */     }
/*     */ 
/* 164 */     filterRGBAll(this.width, this.height, this.rgbPixels);
/*     */ 
/* 167 */     super.imageComplete(paramInt);
/*     */   }
/*     */ 
/*     */   public synchronized void setColorModel(ColorModel paramColorModel)
/*     */   {
/* 115 */     this.consumer.setColorModel(ImageFilterPlus.rgbModel);
/*     */   }
/*     */ 
/*     */   public synchronized void setDimensions(int paramInt1, int paramInt2)
/*     */   {
/* 120 */     if ((paramInt1 == this.width) && (paramInt2 == this.height))
/* 121 */       return;
/* 122 */     this.width = paramInt1;
/* 123 */     this.height = paramInt2;
/* 124 */     this.rgbPixels = new int[paramInt2][paramInt1];
/*     */   }
/*     */ 
/*     */   public synchronized void setPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, ColorModel paramColorModel, byte[] paramArrayOfByte, int paramInt5, int paramInt6)
/*     */   {
/* 129 */     for (int i = 0; i < paramInt4; i++)
/*     */     {
/* 131 */       int j = i * paramInt6 + paramInt5;
/* 132 */       for (int k = 0; k < paramInt3; k++)
/* 133 */         this.rgbPixels[(paramInt2 + i)][(paramInt1 + k)] = 
/* 134 */           paramColorModel.getRGB(paramArrayOfByte[(j + k)] & 0xFF);
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized void setPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, ColorModel paramColorModel, int[] paramArrayOfInt, int paramInt5, int paramInt6)
/*     */   {
/* 141 */     for (int i = 0; i < paramInt4; i++)
/*     */     {
/* 143 */       int j = i * paramInt6 + paramInt5;
/* 144 */       if (paramColorModel == ImageFilterPlus.rgbModel)
/* 145 */         System.arraycopy(
/* 146 */           paramArrayOfInt, j, this.rgbPixels[(paramInt2 + i)], paramInt1, paramInt3);
/*     */       else
/* 148 */         for (int k = 0; k < paramInt3; k++)
/* 149 */           this.rgbPixels[(paramInt2 + i)][(paramInt1 + k)] = 
/* 150 */             paramColorModel.getRGB(paramArrayOfInt[(j + k)]);
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized void setPixels(int paramInt1, int paramInt2, int[][] paramArrayOfInt)
/*     */   {
/* 105 */     this.consumer.setDimensions(paramInt1, paramInt2);
/* 106 */     for (int i = 0; i < paramInt2; i++)
/* 107 */       this.consumer.setPixels(
/* 108 */         0, i, paramInt1, 1, ImageFilterPlus.rgbModel, paramArrayOfInt[i], 0, paramInt1);
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.filters.RGBAllFilter
 * JD-Core Version:    0.6.2
 */