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
/*     */ public class MemoryBitRasterImage extends JimiRasterImageSupport
/*     */   implements BitRasterImage
/*     */ {
/*     */   protected byte[] imageData;
/*  31 */   protected byte[] pixelBuffer = new byte[1];
/*     */   protected byte[] rowBuffer;
/*     */   protected byte[] rowUnpackedBuffer;
/*     */   protected int rowByteWidth;
/*     */ 
/*     */   public MemoryBitRasterImage(int paramInt1, int paramInt2, ColorModel paramColorModel)
/*     */   {
/*  38 */     super(paramInt1, paramInt2, paramColorModel);
/*     */ 
/*  40 */     int i = paramColorModel.getRGB(0);
/*  41 */     int j = paramColorModel.getRGB(1);
/*  42 */     byte[] arrayOfByte1 = { (byte)(i >> 24 & 0xFF), (byte)(j >> 24 & 0xFF) };
/*  43 */     byte[] arrayOfByte2 = { (byte)(i >> 16 & 0xFF), (byte)(j >> 16 & 0xFF) };
/*  44 */     byte[] arrayOfByte3 = { (byte)(i >> 8 & 0xFF), (byte)(j >> 8 & 0xFF) };
/*  45 */     byte[] arrayOfByte4 = { (byte)(i & 0xFF), (byte)(j & 0xFF) };
/*  46 */     IndexColorModel localIndexColorModel = new IndexColorModel(8, 2, arrayOfByte2, arrayOfByte3, arrayOfByte4, arrayOfByte1);
/*  47 */     setColorModel(localIndexColorModel);
/*  48 */     this.rowByteWidth = ((paramInt1 + 7) / 8);
/*     */     try {
/*  50 */       initStorage();
/*     */     }
/*     */     catch (JimiException localJimiException) {
/*  53 */       setError();
/*     */     }
/*     */   }
/*     */ 
/*     */   public byte[] asByteArray()
/*     */   {
/* 251 */     return null;
/*     */   }
/*     */ 
/*     */   public ColorModel getColorModel()
/*     */   {
/*  66 */     return this.colorModel;
/*     */   }
/*     */ 
/*     */   public byte getPixel(int paramInt1, int paramInt2)
/*     */     throws ImageAccessException
/*     */   {
/* 162 */     return (byte) ((this.imageData[(paramInt1 / 8 + paramInt2 * this.rowByteWidth)] & 7 - paramInt1 % 8) == 0 ? 
/* 163 */       0 : 1);
/*     */   }
/*     */ 
/*     */   public int getPixelRGB(int paramInt1, int paramInt2)
/*     */     throws ImageAccessException
/*     */   {
/* 144 */     return getColorModel().getRGB(getPixel(paramInt1, paramInt2));
/*     */   }
/*     */ 
/*     */   public void getRectangle(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte, int paramInt5, int paramInt6)
/*     */     throws ImageAccessException
/*     */   {
/* 150 */     for (int i = 0; i < paramInt4; i++)
/* 151 */       JimiUtil.expandOneBitPixels(this.imageData, paramArrayOfByte, paramInt3, 
/* 152 */         (paramInt2 + i) * (this.rowByteWidth * 8) + paramInt1, 
/* 153 */         paramInt5 + i * paramInt6);
/*     */   }
/*     */ 
/*     */   public void getRectanglePacked(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte, int paramInt5, int paramInt6)
/*     */     throws ImageAccessException
/*     */   {
/* 213 */     for (int i = 0; i < paramInt4; i++)
/* 214 */       System.arraycopy(this.imageData, paramInt1 / 8 + (paramInt2 + i) * this.rowByteWidth, 
/* 215 */         paramArrayOfByte, paramInt5 + i * paramInt6, paramInt3 / 8);
/*     */   }
/*     */ 
/*     */   public void getRectangleRGB(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt, int paramInt5, int paramInt6)
/*     */     throws ImageAccessException
/*     */   {
/* 127 */     ColorModel localColorModel = getColorModel();
/* 128 */     for (int i = 0; i < paramInt4; i++) {
/* 129 */       getRow(paramInt2 + i, this.rowUnpackedBuffer, 0);
/* 130 */       for (int j = 0; j < paramInt3; j++) {
/* 131 */         int k = i * paramInt6 + j;
/* 132 */         paramArrayOfInt[k] = localColorModel.getRGB(this.rowUnpackedBuffer[j]);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void getRow(int paramInt1, byte[] paramArrayOfByte, int paramInt2)
/*     */     throws ImageAccessException
/*     */   {
/* 158 */     getRectangle(0, paramInt1, getWidth(), 1, paramArrayOfByte, paramInt2, 0);
/*     */   }
/*     */ 
/*     */   public void getRowPacked(int paramInt1, byte[] paramArrayOfByte, int paramInt2)
/*     */     throws ImageAccessException
/*     */   {
/* 220 */     getRectanglePacked(0, paramInt1, getWidth(), 1, paramArrayOfByte, paramInt2, 0);
/*     */   }
/*     */ 
/*     */   public void getRowRGB(int paramInt1, int[] paramArrayOfInt, int paramInt2)
/*     */     throws ImageAccessException
/*     */   {
/* 139 */     getRectangleRGB(0, paramInt1, getWidth(), 1, paramArrayOfInt, paramInt2, getWidth());
/*     */   }
/*     */ 
/*     */   protected void initStorage()
/*     */     throws JimiException
/*     */   {
/*  59 */     this.imageData = new byte[this.rowByteWidth * getHeight()];
/*  60 */     this.rowBuffer = new byte[this.rowByteWidth];
/*  61 */     this.rowUnpackedBuffer = new byte[this.rowByteWidth * 8];
/*     */   }
/*     */ 
/*     */   protected synchronized void sendPixel(int paramInt1, int paramInt2, byte paramByte)
/*     */   {
/* 106 */     if (!hasDirectConsumer()) {
/* 107 */       return;
/*     */     }
/* 109 */     MulticastImageConsumer localMulticastImageConsumer = getDirectConsumer();
/*     */ 
/* 111 */     this.pixelBuffer[0] = paramByte;
/* 112 */     localMulticastImageConsumer.setPixels(paramInt1, paramInt2, 1, 1, getColorModel(), this.pixelBuffer, 0, 0);
/*     */   }
/*     */ 
/*     */   public void sendRectangle(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte, int paramInt5, int paramInt6)
/*     */     throws ImageAccessException
/*     */   {
/*  79 */     if (!hasDirectConsumer()) {
/*  80 */       return;
/*     */     }
/*  82 */     MulticastImageConsumer localMulticastImageConsumer = getDirectConsumer();
/*  83 */     localMulticastImageConsumer.setPixels(paramInt1, paramInt2, paramInt3, paramInt4, getColorModel(), paramArrayOfByte, paramInt5, paramInt6);
/*     */   }
/*     */ 
/*     */   protected void sendRectanglePacked(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte, int paramInt5, int paramInt6)
/*     */     throws ImageAccessException
/*     */   {
/* 189 */     if (!hasDirectConsumer()) {
/* 190 */       return;
/*     */     }
/*     */ 
/* 193 */     MulticastImageConsumer localMulticastImageConsumer = getDirectConsumer();
/*     */ 
/* 196 */     paramInt5 *= 8;
/*     */ 
/* 198 */     for (int i = 0; i < paramInt4; i++) {
/* 199 */       JimiUtil.expandOneBitPixels(paramArrayOfByte, this.rowUnpackedBuffer, 
/* 200 */         paramInt5 + paramInt3, i * paramInt3 * 8, i * paramInt3);
/*     */     }
/* 202 */     localMulticastImageConsumer.setPixels(paramInt1, paramInt2, paramInt3, paramInt4, getColorModel(), this.rowUnpackedBuffer, 0, paramInt3);
/*     */   }
/*     */ 
/*     */   protected void sendRegionToConsumerFully(ImageConsumer paramImageConsumer, Rectangle paramRectangle)
/*     */   {
/* 236 */     int i = paramRectangle.x;
/* 237 */     int j = paramRectangle.y;
/* 238 */     int k = paramRectangle.width;
/* 239 */     int m = paramRectangle.height;
/* 240 */     ColorModel localColorModel = getColorModel();
/*     */ 
/* 242 */     for (int n = 0; n < paramRectangle.height; n++) {
/* 243 */       JimiUtil.expandOneBitPixels(this.imageData, this.rowUnpackedBuffer, paramRectangle.width, 
/* 244 */         this.rowByteWidth * 8 * (n + j) + i, 0);
/* 245 */       paramImageConsumer.setPixels(0, n, k, 1, localColorModel, this.rowUnpackedBuffer, 0, 0);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void sendToConsumerFully(ImageConsumer paramImageConsumer)
/*     */   {
/* 225 */     int i = getWidth();
/* 226 */     int j = getHeight();
/* 227 */     for (int k = 0; k < j; k++) {
/* 228 */       JimiUtil.expandOneBitPixels(this.imageData, this.rowUnpackedBuffer, i, 
/* 229 */         this.rowByteWidth * 8 * k, 0);
/* 230 */       paramImageConsumer.setPixels(0, k, i, 1, getColorModel(), this.rowUnpackedBuffer, 0, 0);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setPixel(int paramInt1, int paramInt2, byte paramByte)
/*     */     throws ImageAccessException
/*     */   {
/* 100 */     setModified();
/* 101 */     storePixel(paramInt1, paramInt2, paramByte);
/* 102 */     sendPixel(paramInt1, paramInt2, paramByte);
/*     */   }
/*     */ 
/*     */   public void setRectangle(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte, int paramInt5, int paramInt6)
/*     */     throws ImageAccessException
/*     */   {
/*  72 */     setModified();
/*  73 */     sendRectangle(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfByte, paramInt5, paramInt6);
/*  74 */     storeRectangle(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfByte, paramInt5, paramInt6);
/*     */   }
/*     */ 
/*     */   public void setRectanglePacked(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte, int paramInt5, int paramInt6)
/*     */     throws ImageAccessException
/*     */   {
/* 173 */     setModified();
/* 174 */     sendRectanglePacked(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfByte, paramInt5, paramInt6);
/* 175 */     storeRectanglePacked(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfByte, paramInt5, paramInt6);
/*     */   }
/*     */ 
/*     */   public void setRow(int paramInt1, byte[] paramArrayOfByte, int paramInt2)
/*     */     throws ImageAccessException
/*     */   {
/*  96 */     setRectangle(0, paramInt1, getWidth(), 1, paramArrayOfByte, paramInt2, 0);
/*     */   }
/*     */ 
/*     */   public void setRowPacked(int paramInt1, byte[] paramArrayOfByte, int paramInt2)
/*     */     throws ImageAccessException
/*     */   {
/* 207 */     setRectanglePacked(0, paramInt1, getWidth(), 1, paramArrayOfByte, paramInt2, 0);
/*     */   }
/*     */ 
/*     */   protected void storePixel(int paramInt1, int paramInt2, byte paramByte)
/*     */     throws ImageAccessException
/*     */   {
/* 117 */     int i = (byte)(1 << 7 - paramInt1 % 8);
/* 118 */     int j = (paramInt1 >>> 3) + paramInt2 * this.rowByteWidth;
/*     */     int tmp30_28 = j;
/*     */     byte[] tmp30_25 = this.imageData; tmp30_25[tmp30_28] = ((byte)(tmp30_25[tmp30_28] & (i ^ 0xFFFFFFFF)));
/*     */     int tmp45_43 = j;
/*     */     byte[] tmp45_40 = this.imageData; tmp45_40[tmp45_43] = ((byte)(tmp45_40[tmp45_43] | i));
/*     */   }
/*     */ 
/*     */   public void storeRectangle(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte, int paramInt5, int paramInt6)
/*     */     throws ImageAccessException
/*     */   {
/*  88 */     for (int i = 0; i < paramInt4; i++)
/*     */     {
/*  90 */       JimiUtil.packOneBitPixels(paramArrayOfByte, paramInt5 + i * paramInt6, this.imageData, 
/*  91 */         (paramInt2 + i) * this.rowByteWidth, paramInt1, paramInt3);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void storeRectanglePacked(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte, int paramInt5, int paramInt6)
/*     */     throws ImageAccessException
/*     */   {
/* 181 */     for (int i = 0; i < paramInt4; i++)
/* 182 */       System.arraycopy(paramArrayOfByte, paramInt5 + i * paramInt6, 
/* 183 */         this.imageData, paramInt1 / 8 + (paramInt2 + i) * this.rowByteWidth, paramInt3 / 8);
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.raster.MemoryBitRasterImage
 * JD-Core Version:    0.6.2
 */