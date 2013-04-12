/*     */ package com.sun.jimi.core.raster;
/*     */ 
/*     */ import com.sun.jimi.core.ImageAccessException;
/*     */ import com.sun.jimi.core.JimiException;
/*     */ import com.sun.jimi.core.util.JimiUtil;
/*     */ import com.sun.jimi.core.util.MulticastImageConsumer;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.ImageConsumer;
/*     */ import java.awt.image.IndexColorModel;
/*     */ 
/*     */ public class MemoryByteRasterImage extends JimiRasterImageSupport
/*     */   implements ByteRasterImage, BitRasterImage
/*     */ {
/*     */   protected byte[] imageData;
/*  32 */   protected byte[] pixelBuffer = new byte[1];
/*     */ 
/*     */   public MemoryByteRasterImage(int paramInt1, int paramInt2, ColorModel paramColorModel)
/*     */   {
/*  36 */     super(paramInt1, paramInt2, paramColorModel);
/*     */     try {
/*  38 */       initStorage();
/*     */     }
/*     */     catch (JimiException localJimiException) {
/*  41 */       setError();
/*     */     }
/*     */   }
/*     */ 
/*     */   public byte[] asByteArray()
/*     */   {
/* 256 */     return this.imageData;
/*     */   }
/*     */ 
/*     */   protected ColorModel getAppropriateColorModel(ColorModel paramColorModel)
/*     */   {
/* 231 */     if ((paramColorModel instanceof IndexColorModel))
/*     */     {
/* 233 */       return paramColorModel;
/*     */     }
/*     */ 
/* 236 */     byte[] arrayOfByte1 = new byte[256];
/* 237 */     byte[] arrayOfByte2 = new byte[256];
/* 238 */     byte[] arrayOfByte3 = new byte[256];
/* 239 */     byte[] arrayOfByte4 = new byte[256];
/*     */ 
/* 241 */     for (int i = 0; i < 256; i++) {
/* 242 */       int j = paramColorModel.getRGB(i);
/* 243 */       arrayOfByte4[i] = ((byte)(j >>> 24));
/* 244 */       arrayOfByte1[i] = ((byte)(j >>> 16));
/* 245 */       arrayOfByte2[i] = ((byte)(j >>> 8));
/* 246 */       arrayOfByte3[i] = ((byte)j);
/*     */     }
/*     */ 
/* 249 */     paramColorModel = new IndexColorModel(8, 256, arrayOfByte1, arrayOfByte2, arrayOfByte3);
/*     */ 
/* 251 */     return paramColorModel;
/*     */   }
/*     */ 
/*     */   public byte getPixel(int paramInt1, int paramInt2)
/*     */     throws ImageAccessException
/*     */   {
/*     */     try
/*     */     {
/*  99 */       return this.imageData[(paramInt1 + paramInt2 * getWidth())];
/*     */     }
/*     */     catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException) {
/* 102 */       throw new ImageAccessException(localArrayIndexOutOfBoundsException.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getPixelRGB(int paramInt1, int paramInt2)
/*     */     throws ImageAccessException
/*     */   {
/*  74 */     return getColorModel().getRGB(this.imageData[(paramInt1 + paramInt2 * getWidth())] & 0xFF);
/*     */   }
/*     */ 
/*     */   public void getRectangle(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte, int paramInt5, int paramInt6) throws ImageAccessException
/*     */   {
/*     */     try
/*     */     {
/*  81 */       for (int i = 0; i < paramInt4; i++)
/*  82 */         System.arraycopy(this.imageData, paramInt1 + (paramInt2 + i) * getWidth(), 
/*  83 */           paramArrayOfByte, paramInt5 + i * paramInt6, getWidth());
/*     */     }
/*     */     catch (RuntimeException localRuntimeException)
/*     */     {
/*  87 */       throw new ImageAccessException(localRuntimeException.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void getRectanglePacked(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte, int paramInt5, int paramInt6)
/*     */     throws ImageAccessException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void getRectangleRGB(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt, int paramInt5, int paramInt6)
/*     */     throws ImageAccessException
/*     */   {
/*  53 */     ColorModel localColorModel = getColorModel();
/*     */     try {
/*  55 */       for (int i = 0; i < paramInt4; i++) {
/*  56 */         for (int j = 0; j < paramInt3; j++)
/*  57 */           paramArrayOfInt[(paramInt5 + j + i * paramInt6)] = 
/*  58 */             localColorModel.getRGB(this.imageData[(paramInt1 + j + (paramInt2 + i) * getWidth())] & 0xFF);
/*     */       }
/*     */     }
/*     */     catch (RuntimeException localRuntimeException)
/*     */     {
/*  63 */       throw new ImageAccessException(localRuntimeException.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void getRow(int paramInt1, byte[] paramArrayOfByte, int paramInt2)
/*     */     throws ImageAccessException
/*     */   {
/*  93 */     getRectangle(0, paramInt1, getWidth(), 1, paramArrayOfByte, paramInt2, 0);
/*     */   }
/*     */ 
/*     */   public void getRowPacked(int paramInt1, byte[] paramArrayOfByte, int paramInt2)
/*     */     throws ImageAccessException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void getRowRGB(int paramInt1, int[] paramArrayOfInt, int paramInt2)
/*     */     throws ImageAccessException
/*     */   {
/*  69 */     getRectangleRGB(0, paramInt1, getWidth(), 1, paramArrayOfInt, paramInt2, 0);
/*     */   }
/*     */ 
/*     */   protected void initStorage()
/*     */     throws JimiException
/*     */   {
/*  47 */     this.imageData = new byte[getWidth() * getHeight()];
/*     */   }
/*     */ 
/*     */   protected synchronized void sendPixel(int paramInt1, int paramInt2, byte paramByte)
/*     */   {
/* 179 */     if (!hasDirectConsumer()) {
/* 180 */       return;
/*     */     }
/* 182 */     MulticastImageConsumer localMulticastImageConsumer = getDirectConsumer();
/*     */ 
/* 184 */     this.pixelBuffer[0] = paramByte;
/* 185 */     localMulticastImageConsumer.setPixels(paramInt1, paramInt2, 1, 1, getColorModel(), this.pixelBuffer, 0, 1);
/*     */   }
/*     */ 
/*     */   protected void sendRectangle(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte, int paramInt5, int paramInt6)
/*     */   {
/* 164 */     if (!hasDirectConsumer()) {
/* 165 */       return;
/*     */     }
/*     */ 
/* 168 */     MulticastImageConsumer localMulticastImageConsumer = getDirectConsumer();
/* 169 */     localMulticastImageConsumer.setPixels(paramInt1, paramInt2, paramInt3, paramInt4, getColorModel(), paramArrayOfByte, paramInt5, getWidth());
/*     */   }
/*     */ 
/*     */   protected void sendRegionToConsumerFully(ImageConsumer paramImageConsumer, Rectangle paramRectangle)
/*     */     throws ImageAccessException
/*     */   {
/* 224 */     int i = paramRectangle.x + paramRectangle.y * getWidth();
/* 225 */     paramImageConsumer.setPixels(0, 0, paramRectangle.width, paramRectangle.height, getColorModel(), 
/* 226 */       this.imageData, i, getWidth());
/*     */   }
/*     */ 
/*     */   protected void sendRow(int paramInt1, byte[] paramArrayOfByte, int paramInt2)
/*     */   {
/* 174 */     sendRectangle(0, paramInt1, getWidth(), 1, paramArrayOfByte, paramInt2, 0);
/*     */   }
/*     */ 
/*     */   protected void sendToConsumerFully(ImageConsumer paramImageConsumer)
/*     */     throws ImageAccessException
/*     */   {
/* 218 */     paramImageConsumer.setPixels(0, 0, getWidth(), getHeight(), getColorModel(), this.imageData, 0, getWidth());
/*     */   }
/*     */ 
/*     */   public void setPixel(int paramInt1, int paramInt2, byte paramByte)
/*     */     throws ImageAccessException
/*     */   {
/* 126 */     setModified();
/* 127 */     sendPixel(paramInt1, paramInt2, paramByte);
/* 128 */     storePixel(paramInt1, paramInt2, paramByte);
/*     */   }
/*     */ 
/*     */   public void setRectangle(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte, int paramInt5, int paramInt6)
/*     */     throws ImageAccessException
/*     */   {
/* 109 */     setModified();
/* 110 */     sendRectangle(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfByte, paramInt5, paramInt6);
/* 111 */     storeRectangle(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfByte, paramInt5, paramInt6);
/*     */   }
/*     */ 
/*     */   public void setRectanglePacked(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte, int paramInt5, int paramInt6)
/*     */     throws ImageAccessException
/*     */   {
/* 195 */     byte[] arrayOfByte = new byte[paramInt3 * paramInt4];
/*     */ 
/* 197 */     for (int i = 0; i < paramInt4; i++) {
/* 198 */       JimiUtil.expandOneBitPixels(paramArrayOfByte, arrayOfByte, paramInt3, paramInt6 * i, paramInt3 * i);
/*     */     }
/* 200 */     setRectangle(paramInt1, paramInt2, paramInt3, paramInt4, arrayOfByte, 0, paramInt3);
/*     */   }
/*     */ 
/*     */   public void setRow(int paramInt1, byte[] paramArrayOfByte, int paramInt2)
/*     */     throws ImageAccessException
/*     */   {
/* 121 */     setRectangle(0, paramInt1, getWidth(), 1, paramArrayOfByte, paramInt2, 0);
/*     */   }
/*     */ 
/*     */   public void setRowPacked(int paramInt1, byte[] paramArrayOfByte, int paramInt2)
/*     */     throws ImageAccessException
/*     */   {
/* 204 */     setRectanglePacked(0, paramInt1, getWidth(), 1, paramArrayOfByte, paramInt2, getWidth());
/*     */   }
/*     */ 
/*     */   public void storePixel(int paramInt1, int paramInt2, byte paramByte)
/*     */     throws ImageAccessException
/*     */   {
/*     */     try
/*     */     {
/* 153 */       this.imageData[(paramInt1 + paramInt2 * getWidth())] = paramByte;
/*     */     }
/*     */     catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException) {
/* 156 */       throw new ImageAccessException(localArrayIndexOutOfBoundsException.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void storeRectangle(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte, int paramInt5, int paramInt6)
/*     */     throws ImageAccessException
/*     */   {
/*     */     try
/*     */     {
/* 135 */       for (int i = 0; i < paramInt4; i++)
/* 136 */         System.arraycopy(paramArrayOfByte, paramInt5 + i * paramInt6, 
/* 137 */           this.imageData, paramInt1 + (paramInt2 + i) * getWidth(), paramInt3);
/*     */     }
/*     */     catch (RuntimeException localRuntimeException)
/*     */     {
/* 141 */       throw new ImageAccessException(localRuntimeException.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void storeRow(int paramInt1, byte[] paramArrayOfByte, int paramInt2) throws ImageAccessException
/*     */   {
/* 147 */     storeRectangle(0, paramInt1, getWidth(), 1, paramArrayOfByte, paramInt2, 0);
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.raster.MemoryByteRasterImage
 * JD-Core Version:    0.6.2
 */