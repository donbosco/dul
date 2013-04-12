/*     */ package com.sun.jimi.core.raster;
/*     */ 
/*     */ import com.sun.jimi.core.ImageAccessException;
/*     */ import com.sun.jimi.core.JimiException;
/*     */ import com.sun.jimi.core.util.LongColorModel;
/*     */ import com.sun.jimi.core.util.MulticastImageConsumer;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.ImageConsumer;
/*     */ 
/*     */ public class MemoryLongRasterImage extends JimiRasterImageSupport
/*     */   implements LongRasterImage
/*     */ {
/*     */   protected long[] imageData;
/*     */   protected int[] pixelBuffer;
/*     */   protected LongColorModel lcm;
/*  36 */   protected ColorModel rgbcm = ColorModel.getRGBdefault();
/*     */ 
/*     */   public MemoryLongRasterImage(int paramInt1, int paramInt2, LongColorModel paramLongColorModel)
/*     */   {
/*  40 */     super(paramInt1, paramInt2, paramLongColorModel);
/*  41 */     this.lcm = paramLongColorModel;
/*     */     try {
/*  43 */       initStorage();
/*     */     }
/*     */     catch (JimiException localJimiException) {
/*  46 */       setError();
/*     */     }
/*     */   }
/*     */ 
/*     */   public long getPixel(int paramInt1, int paramInt2)
/*     */     throws ImageAccessException
/*     */   {
/*     */     try
/*     */     {
/* 104 */       return this.imageData[(paramInt1 + paramInt2 * getWidth())];
/*     */     }
/*     */     catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException) {
/* 107 */       throw new ImageAccessException(localArrayIndexOutOfBoundsException.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getPixelRGB(int paramInt1, int paramInt2)
/*     */     throws ImageAccessException
/*     */   {
/*  79 */     return this.lcm.getRGB(this.imageData[(paramInt1 + paramInt2 * getWidth())]);
/*     */   }
/*     */ 
/*     */   public void getRectangle(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long[] paramArrayOfLong, int paramInt5, int paramInt6) throws ImageAccessException
/*     */   {
/*     */     try
/*     */     {
/*  86 */       for (int i = 0; i < paramInt4; i++)
/*  87 */         System.arraycopy(this.imageData, paramInt1 + (paramInt2 + i) * getWidth(), 
/*  88 */           paramArrayOfLong, paramInt5 + i * paramInt6, getWidth());
/*     */     }
/*     */     catch (RuntimeException localRuntimeException)
/*     */     {
/*  92 */       throw new ImageAccessException(localRuntimeException.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void getRectangleRGB(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt, int paramInt5, int paramInt6)
/*     */     throws ImageAccessException
/*     */   {
/*     */     try
/*     */     {
/*  60 */       for (int i = 0; i < paramInt4; i++) {
/*  61 */         for (int j = 0; j < paramInt3; j++)
/*  62 */           paramArrayOfInt[(paramInt5 + j + i * paramInt6)] = 
/*  63 */             this.lcm.getRGB(this.imageData[(paramInt1 + j + (paramInt2 + i) * getWidth())] & 0xFF);
/*     */       }
/*     */     }
/*     */     catch (RuntimeException localRuntimeException)
/*     */     {
/*  68 */       throw new ImageAccessException(localRuntimeException.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void getRow(int paramInt1, long[] paramArrayOfLong, int paramInt2)
/*     */     throws ImageAccessException
/*     */   {
/*  98 */     getRectangle(0, paramInt1, getWidth(), 1, paramArrayOfLong, paramInt2, 0);
/*     */   }
/*     */ 
/*     */   public void getRowRGB(int paramInt1, int[] paramArrayOfInt, int paramInt2)
/*     */     throws ImageAccessException
/*     */   {
/*  74 */     getRectangleRGB(0, paramInt1, getWidth(), 1, paramArrayOfInt, paramInt2, 0);
/*     */   }
/*     */ 
/*     */   protected void initStorage()
/*     */     throws JimiException
/*     */   {
/*  52 */     this.imageData = new long[getWidth() * getHeight()];
/*  53 */     this.pixelBuffer = new int[getWidth()];
/*     */   }
/*     */ 
/*     */   protected synchronized void sendPixel(int paramInt1, int paramInt2, long paramLong)
/*     */   {
/* 189 */     if (!hasDirectConsumer()) {
/* 190 */       return;
/*     */     }
/* 192 */     MulticastImageConsumer localMulticastImageConsumer = getDirectConsumer();
/*     */ 
/* 194 */     this.pixelBuffer[0] = this.lcm.getRGB(paramLong);
/* 195 */     localMulticastImageConsumer.setPixels(paramInt1, paramInt2, 1, 1, this.rgbcm, this.pixelBuffer, 0, 1);
/*     */   }
/*     */ 
/*     */   protected synchronized void sendRectangle(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long[] paramArrayOfLong, int paramInt5, int paramInt6)
/*     */   {
/* 169 */     if (!hasDirectConsumer()) {
/* 170 */       return;
/*     */     }
/*     */ 
/* 173 */     MulticastImageConsumer localMulticastImageConsumer = getDirectConsumer();
/* 174 */     for (int i = 0; i < paramInt4; i++) {
/* 175 */       for (int j = 0; j < paramInt3; j++) {
/* 176 */         this.pixelBuffer[j] = this.lcm.getRGB(paramArrayOfLong[(paramInt1 + paramInt5)]);
/*     */       }
/* 178 */       localMulticastImageConsumer.setPixels(paramInt1, paramInt2 + i, paramInt3, 1, this.rgbcm, this.pixelBuffer, 0, 0);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void sendRegionToConsumerFully(ImageConsumer paramImageConsumer, Rectangle paramRectangle)
/*     */     throws ImageAccessException
/*     */   {
/* 207 */     sendRectangle(paramRectangle.x, paramRectangle.y, paramRectangle.width, paramRectangle.height, this.imageData, 
/* 208 */       paramRectangle.x + paramRectangle.y * getWidth(), getWidth());
/*     */   }
/*     */ 
/*     */   protected void sendRow(int paramInt1, long[] paramArrayOfLong, int paramInt2)
/*     */   {
/* 184 */     sendRectangle(0, paramInt1, getWidth(), 1, paramArrayOfLong, paramInt2, 0);
/*     */   }
/*     */ 
/*     */   protected void sendToConsumerFully(ImageConsumer paramImageConsumer)
/*     */     throws ImageAccessException
/*     */   {
/* 201 */     sendRectangle(0, 0, getWidth(), getHeight(), this.imageData, 0, getWidth());
/*     */   }
/*     */ 
/*     */   public void setPixel(int paramInt1, int paramInt2, long paramLong)
/*     */     throws ImageAccessException
/*     */   {
/* 131 */     setModified();
/* 132 */     setPixel(paramInt1, paramInt2, paramLong);
/* 133 */     sendPixel(paramInt1, paramInt2, paramLong);
/*     */   }
/*     */ 
/*     */   public void setRectangle(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long[] paramArrayOfLong, int paramInt5, int paramInt6)
/*     */     throws ImageAccessException
/*     */   {
/* 114 */     setModified();
/* 115 */     sendRectangle(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfLong, paramInt5, paramInt6);
/* 116 */     storeRectangle(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfLong, paramInt5, paramInt6);
/*     */   }
/*     */ 
/*     */   public void setRow(int paramInt1, long[] paramArrayOfLong, int paramInt2)
/*     */     throws ImageAccessException
/*     */   {
/* 126 */     setRectangle(0, paramInt1, getWidth(), 1, paramArrayOfLong, paramInt2, 0);
/*     */   }
/*     */ 
/*     */   public void storePixel(int paramInt1, int paramInt2, long paramLong)
/*     */     throws ImageAccessException
/*     */   {
/*     */     try
/*     */     {
/* 158 */       this.imageData[(paramInt1 + paramInt2 * getWidth())] = paramLong;
/*     */     }
/*     */     catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException) {
/* 161 */       throw new ImageAccessException(localArrayIndexOutOfBoundsException.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void storeRectangle(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long[] paramArrayOfLong, int paramInt5, int paramInt6)
/*     */     throws ImageAccessException
/*     */   {
/*     */     try
/*     */     {
/* 140 */       for (int i = 0; i < paramInt4; i++)
/* 141 */         System.arraycopy(paramArrayOfLong, paramInt5 + i * paramInt6, 
/* 142 */           this.imageData, paramInt1 + (paramInt2 + i) * getWidth(), paramInt3);
/*     */     }
/*     */     catch (RuntimeException localRuntimeException)
/*     */     {
/* 146 */       throw new ImageAccessException(localRuntimeException.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void storeRow(int paramInt1, long[] paramArrayOfLong, int paramInt2) throws ImageAccessException
/*     */   {
/* 152 */     storeRectangle(0, paramInt1, getWidth(), 1, paramArrayOfLong, paramInt2, 0);
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.raster.MemoryLongRasterImage
 * JD-Core Version:    0.6.2
 */