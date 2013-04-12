/*     */ package com.sun.jimi.core.raster;
/*     */ 
/*     */ import com.sun.jimi.core.ImageAccessException;
/*     */ import com.sun.jimi.core.JimiException;
/*     */ import com.sun.jimi.core.vmem.BytePageMapper;
/*     */ import com.sun.jimi.util.RandomAccessStorage;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.ImageConsumer;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public class VMemByteRasterImage extends MemoryByteRasterImage
/*     */ {
/*     */   protected BytePageMapper pageMapper;
/*     */   protected byte[] rowBuffer;
/*  33 */   protected byte[] pixelBuf = new byte[1];
/*     */ 
/*     */   public VMemByteRasterImage(RandomAccessStorage paramRandomAccessStorage, int paramInt1, int paramInt2, ColorModel paramColorModel)
/*     */     throws JimiException
/*     */   {
/*  39 */     super(paramInt1, paramInt2, paramColorModel);
/*     */ 
/*  42 */     this.pageMapper = new BytePageMapper(paramRandomAccessStorage, new Dimension(paramInt1, paramInt2), 2048);
/*     */   }
/*     */ 
/*     */   public byte[] asByteArray()
/*     */   {
/* 150 */     return null;
/*     */   }
/*     */ 
/*     */   public byte getPixel(int paramInt1, int paramInt2)
/*     */     throws ImageAccessException
/*     */   {
/*  77 */     getRectangle(paramInt1, paramInt2, 1, 1, this.pixelBuf, 0, 0);
/*  78 */     return this.pixelBuf[0];
/*     */   }
/*     */ 
/*     */   public void getRectangle(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte, int paramInt5, int paramInt6)
/*     */     throws ImageAccessException
/*     */   {
/*     */     try
/*     */     {
/*  69 */       this.pageMapper.getRectangle(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfByte, paramInt5, paramInt6);
/*     */     } catch (IOException localIOException) {
/*  71 */       throw new ImageAccessException(localIOException.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getRectangleRGB(int paramInt1, int paramInt2)
/*     */     throws ImageAccessException
/*     */   {
/*  83 */     return getColorModel().getRGB(getPixel(paramInt1, paramInt2));
/*     */   }
/*     */ 
/*     */   public void getRectangleRGB(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt, int paramInt5, int paramInt6)
/*     */     throws ImageAccessException
/*     */   {
/*  52 */     ColorModel localColorModel = getColorModel();
/*  53 */     if (this.rowBuffer == null) {
/*  54 */       this.rowBuffer = new byte[getWidth()];
/*     */     }
/*  56 */     for (int i = 0; i < paramInt4; i++) {
/*  57 */       getRectangle(paramInt1, paramInt2 + i, paramInt3, 1, this.rowBuffer, 0, 0);
/*  58 */       for (int j = 0; j < paramInt3; j++) {
/*  59 */         paramArrayOfInt[(paramInt5++)] = localColorModel.getRGB(this.rowBuffer[j] & 0xFF);
/*     */       }
/*  61 */       paramInt5 += paramInt6 - paramInt3;
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
/*     */     try
/*     */     {
/* 122 */       int i = this.pageMapper.getPageDimensions().height;
/*     */ 
/* 124 */       int j = paramRectangle.x;
/* 125 */       int k = paramRectangle.y;
/* 126 */       int m = paramRectangle.width;
/* 127 */       int n = paramRectangle.height;
/* 128 */       ColorModel localColorModel = getColorModel();
/*     */ 
/* 131 */       int i1 = 5;
/* 132 */       i1 = Math.max(1, i1);
/* 133 */       byte[] arrayOfByte = new byte[m * i1];
/* 134 */       int i2 = 0;
/* 135 */       while (i2 < n) {
/* 136 */         int i3 = Math.min(i1, n - i2);
/* 137 */         getRectangle(j, i2 + k, m, i3, arrayOfByte, 0, m);
/* 138 */         paramImageConsumer.setPixels(0, i2, m, i3, localColorModel, arrayOfByte, 0, m);
/* 139 */         i2 += i3;
/*     */       }
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/* 144 */       throw new ImageAccessException();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void sendToConsumerFully(ImageConsumer paramImageConsumer)
/*     */     throws ImageAccessException
/*     */   {
/* 104 */     int i = this.pageMapper.getPageDimensions().height;
/* 105 */     byte[] arrayOfByte = new byte[getWidth() * i];
/* 106 */     int j = getHeight();
/* 107 */     int k = getWidth();
/* 108 */     int m = 0;
/* 109 */     ColorModel localColorModel = getColorModel();
/* 110 */     while (m < j) {
/* 111 */       int n = Math.min(i, j - m);
/* 112 */       getRectangle(0, m, k, n, arrayOfByte, 0, k);
/* 113 */       paramImageConsumer.setPixels(0, m, k, n, this.colorModel, arrayOfByte, 0, k);
/* 114 */       m += n;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void storePixel(int paramInt1, int paramInt2, byte paramByte)
/*     */     throws ImageAccessException
/*     */   {
/*  98 */     this.pixelBuf[0] = paramByte;
/*  99 */     setRectangle(paramInt1, paramInt2, 1, 1, this.pixelBuf, 0, 0);
/*     */   }
/*     */ 
/*     */   public void storeRectangle(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte, int paramInt5, int paramInt6)
/*     */     throws ImageAccessException
/*     */   {
/*     */     try
/*     */     {
/*  90 */       this.pageMapper.setRectangle(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfByte, paramInt5, paramInt6);
/*     */     } catch (IOException localIOException) {
/*  92 */       throw new ImageAccessException(localIOException.getMessage());
/*     */     }
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.raster.VMemByteRasterImage
 * JD-Core Version:    0.6.2
 */