/*     */ package com.sun.jimi.core.raster;
/*     */ 
/*     */ import com.sun.jimi.core.ImageAccessException;
/*     */ import com.sun.jimi.core.JimiException;
/*     */ import com.sun.jimi.core.util.MulticastImageConsumer;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.DirectColorModel;
/*     */ import java.awt.image.ImageConsumer;
/*     */ import java.awt.image.IndexColorModel;
/*     */ 
/*     */ public class MemoryIntRasterImage extends JimiRasterImageSupport
/*     */   implements ChanneledIntRasterImage
/*     */ {
/*     */   protected int[] imageData;
/*  31 */   protected int[] pixelBuffer = new int[1];
/*     */ 
/*     */   public MemoryIntRasterImage(int paramInt1, int paramInt2, ColorModel paramColorModel)
/*     */   {
/*  35 */     super(paramInt1, paramInt2, paramColorModel);
/*     */     try {
/*  37 */       initStorage();
/*     */     }
/*     */     catch (JimiException localJimiException) {
/*  40 */       setError();
/*     */     }
/*     */   }
/*     */ 
/*     */   public int[] asIntArray()
/*     */   {
/* 302 */     return this.imageData;
/*     */   }
/*     */ 
/*     */   protected ColorModel getAppropriateColorModel(ColorModel paramColorModel)
/*     */   {
/* 279 */     if ((paramColorModel instanceof IndexColorModel))
/*     */     {
/* 281 */       return paramColorModel;
/*     */     }
/* 283 */     if ((paramColorModel instanceof DirectColorModel))
/*     */     {
/* 285 */       DirectColorModel localDirectColorModel = (DirectColorModel)paramColorModel;
/* 286 */       if ((localDirectColorModel.getRedMask() == 16711680) && 
/* 287 */         (localDirectColorModel.getGreenMask() == 65280) && 
/* 288 */         (localDirectColorModel.getBlueMask() == 255))
/*     */       {
/* 290 */         return paramColorModel;
/*     */       }
/* 292 */       if (localDirectColorModel.getAlphaMask() == 0)
/*     */       {
/* 294 */         return new DirectColorModel(24, 16711680, 65280, 255);
/*     */       }
/*     */     }
/* 297 */     return ColorModel.getRGBdefault();
/*     */   }
/*     */ 
/*     */   public void getChannelRectangle(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, byte[] paramArrayOfByte, int paramInt6, int paramInt7)
/*     */     throws ImageAccessException
/*     */   {
/*     */     try
/*     */     {
/* 109 */       int[] arrayOfInt = new int[paramInt4];
/* 110 */       for (int i = 0; i < paramInt5; i++) {
/* 111 */         getRowRGB(paramInt3 + i, arrayOfInt, 0);
/* 112 */         for (int j = 0; j < paramInt4; j++)
/* 113 */           paramArrayOfByte[(paramInt6 + j + i * paramInt7)] = 
/* 114 */             ((byte)(arrayOfInt[j] >>> paramInt1));
/*     */       }
/*     */     }
/*     */     catch (RuntimeException localRuntimeException)
/*     */     {
/* 119 */       throw new ImageAccessException(localRuntimeException.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void getChannelRow(int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3) throws ImageAccessException {
/* 124 */     getChannelRectangle(paramInt1, 0, paramInt2, getWidth(), 1, paramArrayOfByte, paramInt3, 0);
/*     */   }
/*     */ 
/*     */   public int getPixel(int paramInt1, int paramInt2)
/*     */     throws ImageAccessException
/*     */   {
/*     */     try
/*     */     {
/*  98 */       return this.imageData[(paramInt1 + paramInt2 * getWidth())];
/*     */     }
/*     */     catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException) {
/* 101 */       throw new ImageAccessException(localArrayIndexOutOfBoundsException.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getPixelRGB(int paramInt1, int paramInt2)
/*     */     throws ImageAccessException
/*     */   {
/*  73 */     return getColorModel().getRGB(this.imageData[(paramInt1 + paramInt2 * getWidth())]);
/*     */   }
/*     */ 
/*     */   public void getRectangle(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt, int paramInt5, int paramInt6) throws ImageAccessException
/*     */   {
/*     */     try
/*     */     {
/*  80 */       for (int i = 0; i < paramInt4; i++)
/*  81 */         System.arraycopy(this.imageData, paramInt1 + (paramInt2 + i) * getWidth(), 
/*  82 */           paramArrayOfInt, paramInt5 + i * paramInt6, getWidth());
/*     */     }
/*     */     catch (RuntimeException localRuntimeException)
/*     */     {
/*  86 */       throw new ImageAccessException(localRuntimeException.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void getRectangleRGB(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt, int paramInt5, int paramInt6)
/*     */     throws ImageAccessException
/*     */   {
/*  52 */     ColorModel localColorModel = getColorModel();
/*     */     try {
/*  54 */       for (int i = 0; i < paramInt4; i++) {
/*  55 */         for (int j = 0; j < paramInt3; j++)
/*  56 */           paramArrayOfInt[(paramInt5 + j + i * paramInt6)] = 
/*  57 */             localColorModel.getRGB(this.imageData[(paramInt1 + j + (paramInt2 + i) * getWidth())]);
/*     */       }
/*     */     }
/*     */     catch (RuntimeException localRuntimeException)
/*     */     {
/*  62 */       throw new ImageAccessException(localRuntimeException.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void getRow(int paramInt1, int[] paramArrayOfInt, int paramInt2)
/*     */     throws ImageAccessException
/*     */   {
/*  92 */     getRectangle(0, paramInt1, getWidth(), 1, paramArrayOfInt, paramInt2, 0);
/*     */   }
/*     */ 
/*     */   public void getRowRGB(int paramInt1, int[] paramArrayOfInt, int paramInt2)
/*     */     throws ImageAccessException
/*     */   {
/*  68 */     getRectangleRGB(0, paramInt1, getWidth(), 1, paramArrayOfInt, paramInt2, 0);
/*     */   }
/*     */ 
/*     */   protected void initStorage()
/*     */     throws JimiException
/*     */   {
/*  46 */     this.imageData = new int[getWidth() * getHeight()];
/*     */   }
/*     */ 
/*     */   protected synchronized void sendPixel(int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 237 */     if (!hasDirectConsumer()) {
/* 238 */       return;
/*     */     }
/* 240 */     MulticastImageConsumer localMulticastImageConsumer = getDirectConsumer();
/*     */ 
/* 242 */     this.pixelBuffer[0] = paramInt3;
/* 243 */     localMulticastImageConsumer.setPixels(paramInt1, paramInt2, 1, 1, getColorModel(), this.pixelBuffer, 0, 1);
/*     */   }
/*     */ 
/*     */   protected void sendRectangle(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt, int paramInt5, int paramInt6)
/*     */   {
/* 218 */     if (!hasDirectConsumer()) {
/* 219 */       return;
/*     */     }
/*     */ 
/* 222 */     MulticastImageConsumer localMulticastImageConsumer = getDirectConsumer();
/* 223 */     localMulticastImageConsumer.setPixels(paramInt1, paramInt2, paramInt3, paramInt4, getColorModel(), paramArrayOfInt, paramInt5, getWidth());
/*     */   }
/*     */ 
/*     */   protected void sendRegionToConsumerFully(ImageConsumer paramImageConsumer, Rectangle paramRectangle)
/*     */     throws ImageAccessException
/*     */   {
/* 255 */     int i = paramRectangle.x + paramRectangle.y * getWidth();
/* 256 */     paramImageConsumer.setPixels(0, 0, paramRectangle.width, paramRectangle.height, getColorModel(), 
/* 257 */       this.imageData, i, getWidth());
/*     */   }
/*     */ 
/*     */   protected void sendRow(int paramInt1, int[] paramArrayOfInt, int paramInt2)
/*     */   {
/* 228 */     if (!hasDirectConsumer()) {
/* 229 */       return;
/*     */     }
/* 231 */     MulticastImageConsumer localMulticastImageConsumer = getDirectConsumer();
/* 232 */     localMulticastImageConsumer.setPixels(0, paramInt1, getWidth(), 1, getColorModel(), paramArrayOfInt, paramInt2, 0);
/*     */   }
/*     */ 
/*     */   protected void sendToConsumerFully(ImageConsumer paramImageConsumer)
/*     */     throws ImageAccessException
/*     */   {
/* 249 */     paramImageConsumer.setPixels(0, 0, getWidth(), getHeight(), getColorModel(), this.imageData, 0, getWidth());
/*     */   }
/*     */ 
/*     */   public void setChannelPixel(int paramInt1, int paramInt2, int paramInt3, byte paramByte)
/*     */     throws ImageAccessException
/*     */   {
/* 194 */     setModified();
/* 195 */     this.imageData[(paramInt2 + paramInt3 * getWidth())] |= (paramByte & 0xFF) << paramInt1;
/*     */   }
/*     */ 
/*     */   public void setChannelRectangle(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, byte[] paramArrayOfByte, int paramInt6, int paramInt7)
/*     */     throws ImageAccessException
/*     */   {
/* 185 */     setModified();
/* 186 */     storeChannelRectangle(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramArrayOfByte, paramInt6, paramInt7);
/*     */   }
/*     */ 
/*     */   public void setChannelRow(int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3) throws ImageAccessException {
/* 190 */     setChannelRectangle(paramInt1, 0, paramInt2, getWidth(), 1, paramArrayOfByte, paramInt3, 0);
/*     */   }
/*     */ 
/*     */   public void setPixel(int paramInt1, int paramInt2, int paramInt3)
/*     */     throws ImageAccessException
/*     */   {
/* 145 */     setModified();
/* 146 */     if (this.forceRGB) {
/* 147 */       paramInt3 = toRGB(paramInt3);
/*     */     }
/* 149 */     sendPixel(paramInt1, paramInt2, paramInt3);
/* 150 */     storePixel(paramInt1, paramInt2, paramInt3);
/*     */   }
/*     */ 
/*     */   public void setRectangle(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt, int paramInt5, int paramInt6)
/*     */     throws ImageAccessException
/*     */   {
/* 130 */     setModified();
/* 131 */     if (this.forceRGB) {
/* 132 */       toRGB(paramArrayOfInt, paramInt3, paramInt4, paramInt5, paramInt6);
/*     */     }
/* 134 */     sendRectangle(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfInt, paramInt5, paramInt6);
/* 135 */     storeRectangle(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfInt, paramInt5, paramInt6);
/*     */   }
/*     */ 
/*     */   public void setRow(int paramInt1, int[] paramArrayOfInt, int paramInt2) throws ImageAccessException
/*     */   {
/* 140 */     setRectangle(0, paramInt1, getWidth(), 1, paramArrayOfInt, paramInt2, 0);
/*     */   }
/*     */ 
/*     */   public void storeChannelRectangle(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, byte[] paramArrayOfByte, int paramInt6, int paramInt7)
/*     */     throws ImageAccessException
/*     */   {
/*     */     try
/*     */     {
/* 202 */       for (int i = 0; i < paramInt5; i++) {
/* 203 */         for (int j = 0; j < paramInt4; j++)
/* 204 */           this.imageData[(paramInt2 + j + (paramInt3 + i) * getWidth())] |= 
/* 205 */             (paramArrayOfByte[(paramInt6 + j + i * paramInt7)] & 0xFF) << paramInt1;
/*     */       }
/*     */     }
/*     */     catch (RuntimeException localRuntimeException)
/*     */     {
/* 210 */       throw new ImageAccessException(localRuntimeException.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void storePixel(int paramInt1, int paramInt2, int paramInt3)
/*     */     throws ImageAccessException
/*     */   {
/*     */     try
/*     */     {
/* 175 */       this.imageData[(paramInt1 + paramInt2 * getWidth())] = paramInt3;
/*     */     }
/*     */     catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException) {
/* 178 */       throw new ImageAccessException(localArrayIndexOutOfBoundsException.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void storeRectangle(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt, int paramInt5, int paramInt6)
/*     */     throws ImageAccessException
/*     */   {
/*     */     try
/*     */     {
/* 157 */       for (int i = 0; i < paramInt4; i++)
/* 158 */         System.arraycopy(paramArrayOfInt, paramInt5 + i * paramInt6, 
/* 159 */           this.imageData, paramInt1 + (paramInt2 + i) * getWidth(), paramInt3);
/*     */     }
/*     */     catch (RuntimeException localRuntimeException)
/*     */     {
/* 163 */       throw new ImageAccessException(localRuntimeException.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void storeRow(int paramInt1, int[] paramArrayOfInt, int paramInt2) throws ImageAccessException
/*     */   {
/* 169 */     storeRectangle(0, paramInt1, getWidth(), 1, paramArrayOfInt, paramInt2, 0);
/*     */   }
/*     */ 
/*     */   protected int toRGB(int paramInt)
/*     */   {
/* 274 */     return this.sourceColorModel.getRGB(paramInt);
/*     */   }
/*     */ 
/*     */   protected void toRGB(int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 262 */     int i = paramInt1 - paramInt4;
/* 263 */     for (int j = 0; j < paramInt2; j++) {
/* 264 */       for (int k = 0; k < paramInt1; k++) {
/* 265 */         paramArrayOfInt[paramInt3] = this.sourceColorModel.getRGB(paramArrayOfInt[paramInt3]);
/* 266 */         paramInt3++;
/*     */       }
/* 268 */       paramInt3 += i;
/*     */     }
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.raster.MemoryIntRasterImage
 * JD-Core Version:    0.6.2
 */