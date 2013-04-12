/*     */ package com.sun.jimi.core.raster;
/*     */ 
/*     */ import com.sun.jimi.core.ImageAccessException;
/*     */ import com.sun.jimi.core.JimiException;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.ImageConsumer;
/*     */ 
/*     */ public class OneshotIntRasterImage extends MemoryIntRasterImage
/*     */ {
/*  29 */   protected int[] pixelBuffer = new int[1];
/*     */ 
/*     */   public OneshotIntRasterImage(int paramInt1, int paramInt2, ColorModel paramColorModel)
/*     */   {
/*  33 */     super(paramInt1, paramInt2, paramColorModel);
/*     */     try {
/*  35 */       initStorage();
/*     */     }
/*     */     catch (JimiException localJimiException) {
/*  38 */       setError();
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
/* 125 */     throw new ImageAccessException();
/*     */   }
/*     */ 
/*     */   protected void sendToConsumerFully(ImageConsumer paramImageConsumer)
/*     */     throws ImageAccessException
/*     */   {
/* 119 */     throw new ImageAccessException();
/*     */   }
/*     */ 
/*     */   public void setChannelPixel(int paramInt1, int paramInt2, int paramInt3, byte paramByte)
/*     */     throws ImageAccessException
/*     */   {
/*  97 */     setModified();
/*  98 */     this.imageData[(paramInt2 + paramInt3 * getWidth())] |= (paramByte & 0xFF) << paramInt1;
/*     */   }
/*     */ 
/*     */   public void setChannelRectangle(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, byte[] paramArrayOfByte, int paramInt6, int paramInt7)
/*     */     throws ImageAccessException
/*     */   {
/*  89 */     setModified();
/*     */   }
/*     */ 
/*     */   public void setChannelRow(int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3) throws ImageAccessException {
/*  93 */     setChannelRectangle(paramInt1, 0, paramInt2, getWidth(), 1, paramArrayOfByte, paramInt3, 0);
/*     */   }
/*     */ 
/*     */   public void setPixel(int paramInt1, int paramInt2, int paramInt3)
/*     */     throws ImageAccessException
/*     */   {
/*  60 */     setModified();
/*  61 */     sendPixel(paramInt1, paramInt2, paramInt3);
/*     */   }
/*     */ 
/*     */   public void setRectangle(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt, int paramInt5, int paramInt6)
/*     */     throws ImageAccessException
/*     */   {
/*  49 */     setModified();
/*  50 */     sendRectangle(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfInt, paramInt5, paramInt6);
/*     */   }
/*     */ 
/*     */   public void setRow(int paramInt1, int[] paramArrayOfInt, int paramInt2) throws ImageAccessException
/*     */   {
/*  55 */     setRectangle(0, paramInt1, getWidth(), 1, paramArrayOfInt, paramInt2, 0);
/*     */   }
/*     */ 
/*     */   public void storeChannelRectangle(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, byte[] paramArrayOfByte, int paramInt6, int paramInt7)
/*     */     throws ImageAccessException
/*     */   {
/*     */     try
/*     */     {
/* 105 */       for (int i = 0; i < paramInt5; i++) {
/* 106 */         for (int j = 0; j < paramInt4; j++)
/* 107 */           this.imageData[(paramInt2 + j + (paramInt3 + i) * getWidth())] |= 
/* 108 */             (paramArrayOfByte[(paramInt6 + j + i * paramInt7)] & 0xFF) << paramInt1;
/*     */       }
/*     */     }
/*     */     catch (RuntimeException localRuntimeException)
/*     */     {
/* 113 */       throw new ImageAccessException(localRuntimeException.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void storePixel(int paramInt1, int paramInt2, int paramInt3)
/*     */     throws ImageAccessException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void storeRectangle(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt, int paramInt5, int paramInt6)
/*     */     throws ImageAccessException
/*     */   {
/*     */     try
/*     */     {
/*  68 */       for (int i = 0; i < paramInt4; i++)
/*  69 */         System.arraycopy(paramArrayOfInt, paramInt5 + i * paramInt6, 
/*  70 */           this.imageData, paramInt1 + (paramInt2 + i) * getWidth(), paramInt3);
/*     */     }
/*     */     catch (RuntimeException localRuntimeException)
/*     */     {
/*  74 */       throw new ImageAccessException(localRuntimeException.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void storeRow(int paramInt1, int[] paramArrayOfInt, int paramInt2)
/*     */     throws ImageAccessException
/*     */   {
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.raster.OneshotIntRasterImage
 * JD-Core Version:    0.6.2
 */