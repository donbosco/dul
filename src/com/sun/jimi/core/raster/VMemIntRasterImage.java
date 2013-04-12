/*     */ package com.sun.jimi.core.raster;
/*     */ 
/*     */ import com.sun.jimi.core.ImageAccessException;
/*     */ import com.sun.jimi.core.JimiException;
/*     */ import com.sun.jimi.core.vmem.IntPageMapper;
/*     */ import com.sun.jimi.util.RandomAccessStorage;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.ImageConsumer;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public class VMemIntRasterImage extends MemoryIntRasterImage
/*     */ {
/*     */   protected IntPageMapper pageMapper;
/*  34 */   int[] pixelBuf = new int[1];
/*     */ 
/*     */   public VMemIntRasterImage(RandomAccessStorage paramRandomAccessStorage, int paramInt1, int paramInt2, ColorModel paramColorModel)
/*     */     throws JimiException
/*     */   {
/*  40 */     super(paramInt1, paramInt2, paramColorModel);
/*     */ 
/*  43 */     this.pageMapper = new IntPageMapper(paramRandomAccessStorage, new Dimension(paramInt1, paramInt2), 2048);
/*     */   }
/*     */ 
/*     */   public int[] asIntArray()
/*     */   {
/* 158 */     return null;
/*     */   }
/*     */ 
/*     */   public synchronized int getPixel(int paramInt1, int paramInt2)
/*     */     throws ImageAccessException
/*     */   {
/*  79 */     getRectangle(paramInt1, paramInt2, 1, 1, this.pixelBuf, 0, 0);
/*  80 */     return this.pixelBuf[0];
/*     */   }
/*     */ 
/*     */   public int getPixelRGB(int paramInt1, int paramInt2) throws ImageAccessException
/*     */   {
/*  85 */     return this.colorModel.getRGB(getPixel(paramInt1, paramInt2));
/*     */   }
/*     */ 
/*     */   public void getRectangle(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt, int paramInt5, int paramInt6)
/*     */     throws ImageAccessException
/*     */   {
/*     */     try
/*     */     {
/*  71 */       this.pageMapper.getRectangle(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfInt, paramInt5, paramInt6);
/*     */     } catch (IOException localIOException) {
/*  73 */       throw new ImageAccessException(localIOException.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void getRectangleRGB(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt, int paramInt5, int paramInt6)
/*     */     throws ImageAccessException
/*     */   {
/*  53 */     if (this.rowBuf == null)
/*     */     {
/*  55 */       this.rowBuf = new int[paramInt3];
/*     */     }
/*  57 */     for (int i = 0; i < paramInt4; i++) {
/*  58 */       getRow(paramInt2 + i, this.rowBuf, 0);
/*  59 */       int j = 0;
/*  60 */       int k = 0;
/*  61 */       for (int m = 0; m < paramInt3; m++)
/*  62 */         paramArrayOfInt[(i * paramInt6 + k++ + paramInt5)] = this.colorModel.getRGB(this.rowBuf[(j++)]);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void initStorage()
/*     */     throws JimiException
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void sendRegionToConsumerFully(ImageConsumer paramImageConsumer, Rectangle paramRectangle)
/*     */     throws ImageAccessException
/*     */   {
/* 135 */     int i = this.pageMapper.getPageDimensions().height;
/*     */ 
/* 137 */     int j = paramRectangle.x;
/* 138 */     int k = paramRectangle.y;
/* 139 */     int m = paramRectangle.width;
/* 140 */     int n = paramRectangle.height;
/* 141 */     ColorModel localColorModel = getColorModel();
/*     */ 
/* 143 */     int i1 = getWidth() * i / m;
/* 144 */     i1 = Math.max(1, i1);
/* 145 */     int[] arrayOfInt = new int[m * i1];
/* 146 */     int i2 = 0;
/* 147 */     while (i2 < n) {
/* 148 */       int i3 = Math.min(i1, n - i2);
/* 149 */       getRectangle(j, i2 + k, m, i3, arrayOfInt, 0, m);
/* 150 */       paramImageConsumer.setPixels(0, i2, m, i3, localColorModel, arrayOfInt, 0, m);
/* 151 */       i2 += i3;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void sendToConsumerFully(ImageConsumer paramImageConsumer)
/*     */     throws ImageAccessException
/*     */   {
/* 118 */     int i = this.pageMapper.getPageDimensions().height;
/* 119 */     int[] arrayOfInt = new int[getWidth() * i];
/* 120 */     int j = getHeight();
/* 121 */     int k = getWidth();
/* 122 */     int m = 0;
/* 123 */     ColorModel localColorModel = getColorModel();
/* 124 */     while (m < j) {
/* 125 */       int n = Math.min(i, j - m);
/* 126 */       getRectangle(0, m, k, n, arrayOfInt, 0, k);
/* 127 */       paramImageConsumer.setPixels(0, m, k, n, this.colorModel, arrayOfInt, 0, k);
/* 128 */       m += n;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void storeChannelRectangle(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, byte[] paramArrayOfByte, int paramInt6, int paramInt7)
/*     */     throws ImageAccessException
/*     */   {
/* 102 */     setModified();
/*     */     try {
/* 104 */       this.pageMapper.setChannelRectangle(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramArrayOfByte, paramInt6, paramInt7);
/*     */     } catch (IOException localIOException) {
/* 106 */       throw new ImageAccessException(localIOException.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized void storePixel(int paramInt1, int paramInt2, int paramInt3) throws ImageAccessException
/*     */   {
/* 112 */     this.pixelBuf[0] = paramInt3;
/* 113 */     setRectangle(paramInt1, paramInt2, 0, 0, this.pixelBuf, 0, 0);
/*     */   }
/*     */ 
/*     */   public void storeRectangle(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt, int paramInt5, int paramInt6)
/*     */     throws ImageAccessException
/*     */   {
/*  91 */     setModified();
/*     */     try {
/*  93 */       this.pageMapper.setRectangle(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfInt, paramInt5, paramInt6);
/*     */     } catch (IOException localIOException) {
/*  95 */       throw new ImageAccessException(localIOException.getMessage());
/*     */     }
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.raster.VMemIntRasterImage
 * JD-Core Version:    0.6.2
 */