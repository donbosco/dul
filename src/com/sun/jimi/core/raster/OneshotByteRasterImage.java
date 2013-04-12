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
/*     */ public class OneshotByteRasterImage extends MemoryByteRasterImage
/*     */ {
/*  30 */   protected byte[] pixelBuffer = new byte[1];
/*     */   protected byte[] rowUnpackedBuffer;
/*     */   protected int rowByteWidth;
/*     */ 
/*     */   public OneshotByteRasterImage(int paramInt1, int paramInt2, ColorModel paramColorModel, boolean paramBoolean)
/*     */   {
/*  36 */     super(paramInt1, paramInt2, paramColorModel);
/*  37 */     this.rowByteWidth = ((paramInt1 + 7) / 8);
/*  38 */     if (paramBoolean)
/*     */     {
/*  40 */       if (!(paramColorModel instanceof IndexColorModel)) {
/*  41 */         int i = paramColorModel.getRGB(0);
/*  42 */         int j = paramColorModel.getRGB(1);
/*  43 */         byte[] arrayOfByte1 = { (byte)(i >> 24 & 0xFF), (byte)(j >> 24 & 0xFF) };
/*  44 */         byte[] arrayOfByte2 = { (byte)(i >> 16 & 0xFF), (byte)(j >> 16 & 0xFF) };
/*  45 */         byte[] arrayOfByte3 = { (byte)(i >> 8 & 0xFF), (byte)(j >> 8 & 0xFF) };
/*  46 */         byte[] arrayOfByte4 = { (byte)(i & 0xFF), (byte)(j & 0xFF) };
/*  47 */         IndexColorModel localIndexColorModel = new IndexColorModel(1, 2, arrayOfByte2, arrayOfByte3, arrayOfByte4, arrayOfByte1);
/*  48 */         setColorModel(localIndexColorModel);
/*     */       }
/*     */     }
/*     */     try {
/*  52 */       initStorage();
/*     */     }
/*     */     catch (JimiException localJimiException) {
/*  55 */       setError();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void addWaitingConsumer(ImageConsumer paramImageConsumer)
/*     */   {
/* 146 */     paramImageConsumer.imageComplete(1);
/*     */   }
/*     */ 
/*     */   public void getRectanglePacked(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte, int paramInt5, int paramInt6)
/*     */     throws ImageAccessException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void getRowPacked(int paramInt1, byte[] paramArrayOfByte, int paramInt2)
/*     */     throws ImageAccessException
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void initStorage()
/*     */     throws JimiException
/*     */   {
/*  61 */     this.rowUnpackedBuffer = new byte[this.rowByteWidth * 8];
/*     */   }
/*     */ 
/*     */   protected synchronized void sendPixel(int paramInt1, int paramInt2, byte paramByte)
/*     */   {
/*  98 */     if (!hasDirectConsumer()) {
/*  99 */       return;
/*     */     }
/* 101 */     MulticastImageConsumer localMulticastImageConsumer = getDirectConsumer();
/*     */ 
/* 103 */     this.pixelBuffer[0] = paramByte;
/* 104 */     localMulticastImageConsumer.setPixels(paramInt1, paramInt2, 1, 1, getColorModel(), this.pixelBuffer, 0, 1);
/*     */   }
/*     */ 
/*     */   protected void sendRectangle(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte, int paramInt5, int paramInt6)
/*     */   {
/*  80 */     if (!hasDirectConsumer()) {
/*  81 */       return;
/*     */     }
/*  83 */     MulticastImageConsumer localMulticastImageConsumer = getDirectConsumer();
/*  84 */     localMulticastImageConsumer.setPixels(paramInt1, paramInt2, paramInt3, paramInt4, getColorModel(), paramArrayOfByte, paramInt5, paramInt6);
/*     */   }
/*     */ 
/*     */   protected void sendRectanglePacked(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte, int paramInt5, int paramInt6)
/*     */     throws ImageAccessException
/*     */   {
/* 133 */     if (!hasDirectConsumer()) {
/* 134 */       return;
/*     */     }
/* 136 */     MulticastImageConsumer localMulticastImageConsumer = getDirectConsumer();
/* 137 */     for (int i = 0; i < paramInt4; i++) {
/* 138 */       JimiUtil.expandOneBitPixels(paramArrayOfByte, this.rowUnpackedBuffer, paramInt3, i * paramInt3 * 8, 0);
/* 139 */       localMulticastImageConsumer.setPixels(paramInt1, paramInt2 + i, paramInt3, 1, getColorModel(), this.rowUnpackedBuffer, 0, paramInt3);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void sendRegionToConsumerFully(ImageConsumer paramImageConsumer, Rectangle paramRectangle)
/*     */     throws ImageAccessException
/*     */   {
/* 158 */     throw new ImageAccessException();
/*     */   }
/*     */ 
/*     */   protected void sendRow(int paramInt1, byte[] paramArrayOfByte, int paramInt2)
/*     */   {
/*  89 */     if (!hasDirectConsumer()) {
/*  90 */       return;
/*     */     }
/*  92 */     MulticastImageConsumer localMulticastImageConsumer = getDirectConsumer();
/*  93 */     localMulticastImageConsumer.setPixels(0, paramInt1, getWidth(), 1, getColorModel(), paramArrayOfByte, paramInt2, 0);
/*     */   }
/*     */ 
/*     */   protected void sendToConsumerFully(ImageConsumer paramImageConsumer)
/*     */     throws ImageAccessException
/*     */   {
/* 152 */     throw new ImageAccessException();
/*     */   }
/*     */ 
/*     */   public void setPixel(int paramInt1, int paramInt2, byte paramByte)
/*     */     throws ImageAccessException
/*     */   {
/*  73 */     setModified();
/*  74 */     sendPixel(paramInt1, paramInt2, paramByte);
/*     */   }
/*     */ 
/*     */   public void setRectangle(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte, int paramInt5, int paramInt6)
/*     */     throws ImageAccessException
/*     */   {
/*  67 */     setModified();
/*  68 */     sendRectangle(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfByte, paramInt5, paramInt6);
/*     */   }
/*     */ 
/*     */   public void setRectanglePacked(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte, int paramInt5, int paramInt6)
/*     */     throws ImageAccessException
/*     */   {
/* 114 */     setModified();
/* 115 */     sendRectanglePacked(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfByte, paramInt5, paramInt6);
/*     */   }
/*     */ 
/*     */   public void setRowPacked(int paramInt1, byte[] paramArrayOfByte, int paramInt2) throws ImageAccessException {
/* 119 */     setRectanglePacked(0, paramInt1, getWidth(), 1, paramArrayOfByte, paramInt2, getWidth());
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.raster.OneshotByteRasterImage
 * JD-Core Version:    0.6.2
 */