/*     */ package com.sun.jimi.core.filters;
/*     */ 
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.ImageConsumer;
/*     */ import java.awt.image.ImageFilter;
/*     */ import java.awt.image.ImageProducer;
/*     */ 
/*     */ public abstract class RGBBlockFilter extends ImageFilterPlus
/*     */ {
/*     */   public RGBBlockFilter(ImageProducer paramImageProducer)
/*     */   {
/*  68 */     super(paramImageProducer);
/*     */   }
/*     */ 
/*     */   public abstract int[][] filterRGBBlock(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[][] paramArrayOfInt);
/*     */ 
/*     */   public void setColorModel(ColorModel paramColorModel)
/*     */   {
/*  82 */     this.consumer.setColorModel(ImageFilterPlus.rgbModel);
/*     */   }
/*     */ 
/*     */   public void setPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, ColorModel paramColorModel, byte[] paramArrayOfByte, int paramInt5, int paramInt6)
/*     */   {
/*  91 */     int[][] arrayOfInt = new int[paramInt4][paramInt3];
/*  92 */     for (int i = 0; i < paramInt4; i++)
/*     */     {
/*  94 */       int j = paramInt5 + i * paramInt6;
/*  95 */       for (int k = 0; k < paramInt3; k++)
/*  96 */         arrayOfInt[i][k] = 
/*  97 */           paramColorModel.getRGB(paramArrayOfByte[(j + k)] & 0xFF);
/*     */     }
/*  99 */     setPixels(paramInt1, paramInt2, paramInt3, paramInt4, arrayOfInt);
/*     */   }
/*     */ 
/*     */   public void setPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, ColorModel paramColorModel, int[] paramArrayOfInt, int paramInt5, int paramInt6)
/*     */   {
/* 107 */     int[][] arrayOfInt = new int[paramInt4][paramInt3];
/* 108 */     for (int i = 0; i < paramInt4; i++)
/*     */     {
/* 110 */       int j = paramInt5 + i * paramInt6;
/* 111 */       int k = i * paramInt3;
/*     */ 
/* 113 */       if (paramColorModel == ImageFilterPlus.rgbModel)
/* 114 */         System.arraycopy(
/* 115 */           paramArrayOfInt, j, arrayOfInt[i], 0, paramInt3);
/*     */       else
/* 117 */         for (int m = 0; m < paramInt3; m++)
/* 118 */           arrayOfInt[i][m] = 
/* 119 */             paramColorModel.getRGB(paramArrayOfInt[(j + m)]);
/*     */     }
/* 121 */     setPixels(paramInt1, paramInt2, paramInt3, paramInt4, arrayOfInt);
/*     */   }
/*     */ 
/*     */   private void setPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[][] paramArrayOfInt)
/*     */   {
/* 127 */     int[][] arrayOfInt = filterRGBBlock(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfInt);
/*     */ 
/* 130 */     for (int i = 0; i < paramInt4; i++)
/* 131 */       this.consumer.setPixels(
/* 132 */         paramInt1, paramInt2 + i, paramInt3, 1, ImageFilterPlus.rgbModel, arrayOfInt[i], 0, paramInt3);
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.filters.RGBBlockFilter
 * JD-Core Version:    0.6.2
 */