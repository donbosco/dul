/*     */ package com.sun.jimi.core;
/*     */ 
/*     */ import com.sun.jimi.core.raster.BitRasterImage;
/*     */ import com.sun.jimi.core.raster.ByteRasterImage;
/*     */ import com.sun.jimi.core.raster.ChanneledIntRasterImage;
/*     */ import com.sun.jimi.core.raster.IntRasterImage;
/*     */ import com.sun.jimi.core.raster.VMemByteRasterImage;
/*     */ import com.sun.jimi.core.raster.VMemChanneledIntRasterImage;
/*     */ import com.sun.jimi.core.raster.VMemIntRasterImage;
/*     */ import com.sun.jimi.util.FileRandomAccessStorage;
/*     */ import com.sun.jimi.util.RandomAccessStorage;
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.IndexColorModel;
/*     */ import java.io.File;
import java.io.IOException;
/*     */ 
/*     */ public class VMemJimiImageFactory
/*     */   implements JimiImageFactory
/*     */ {
/*     */   protected static long id;
/*  33 */   protected JimiImageFactory memoryFactory = new MemoryJimiImageFactory();
/*     */ 
/*     */   public BitRasterImage createBitRasterImage(int paramInt1, int paramInt2, ColorModel paramColorModel)
/*     */   {
/*     */     try
/*     */     {
/*  68 */       if (paramInt1 * paramInt2 / 8 < VMMControl.threshold) {
/*  69 */         return this.memoryFactory.createBitRasterImage(paramInt1, paramInt2, paramColorModel);
/*     */       }
/*  71 */       int i = paramColorModel.getRGB(0);
/*  72 */       int j = paramColorModel.getRGB(1);
/*  73 */       byte[] arrayOfByte1 = { (byte)(i >> 24 & 0xFF), (byte)(j >> 24 & 0xFF) };
/*  74 */       byte[] arrayOfByte2 = { (byte)(i >> 16 & 0xFF), (byte)(j >> 16 & 0xFF) };
/*  75 */       byte[] arrayOfByte3 = { (byte)(i >> 8 & 0xFF), (byte)(j >> 8 & 0xFF) };
/*  76 */       byte[] arrayOfByte4 = { (byte)(i & 0xFF), (byte)(j & 0xFF) };
/*  77 */       paramColorModel = new IndexColorModel(8, 2, arrayOfByte2, arrayOfByte3, arrayOfByte4, arrayOfByte1);
/*  78 */       return (BitRasterImage)createByteRasterImage(paramInt1, paramInt2, paramColorModel);
/*     */     } catch (Exception localException) {
/*     */     }
/*  81 */     return null;
/*     */   }
/*     */ 
/*     */   public ByteRasterImage createByteRasterImage(int paramInt1, int paramInt2, ColorModel paramColorModel)
/*     */   {
/*     */     try
/*     */     {
/*  55 */       if (paramInt1 * paramInt2 < VMMControl.threshold) {
/*  56 */         return this.memoryFactory.createByteRasterImage(paramInt1, paramInt2, paramColorModel);
/*     */       }
/*  58 */       return new VMemByteRasterImage(createNextStorage(VMMControl.getDirectory()), paramInt1, paramInt2, paramColorModel);
/*     */     } catch (Exception localException) {
/*     */     }
/*  61 */     return null;
/*     */   }
/*     */ 
/*     */   public ChanneledIntRasterImage createChanneledIntRasterImage(int paramInt1, int paramInt2, ColorModel paramColorModel)
/*     */   {
/*     */     try
/*     */     {
/*  88 */       if (paramInt1 * paramInt2 * 4 < VMMControl.threshold) {
/*  89 */         return this.memoryFactory.createChanneledIntRasterImage(paramInt1, paramInt2, paramColorModel);
/*     */       }
/*  91 */       return new VMemChanneledIntRasterImage(createNextStorage(VMMControl.getDirectory()), paramInt1, paramInt2, paramColorModel);
/*     */     } catch (Exception localException) {
/*     */     }
/*  94 */     return null;
/*     */   }
/*     */ 
/*     */   public IntRasterImage createIntRasterImage(int paramInt1, int paramInt2, ColorModel paramColorModel)
/*     */   {
/*     */     try
/*     */     {
/*  42 */       if (paramInt1 * paramInt2 * 4 < VMMControl.threshold) {
/*  43 */         return this.memoryFactory.createIntRasterImage(paramInt1, paramInt2, paramColorModel);
/*     */       }
/*  45 */       return new VMemIntRasterImage(createNextStorage(VMMControl.getDirectory()), paramInt1, paramInt2, paramColorModel);
/*     */     } catch (Exception localException) {
/*     */     }
/*  48 */     return null;
/*     */   }
/*     */ 
/*     */   protected static synchronized RandomAccessStorage createNextStorage(File paramFile)
/*     */     throws IOException
/*     */   {
/* 101 */     File localFile = null;
/*     */     do
/*     */     {
/* 104 */       id += 1L;
/* 105 */       String localObject = String.valueOf(paramFile) + "/" + "jimidat." + id;
/* 106 */       localFile = new File((String)localObject);
/*     */     }
/* 108 */     while ((localFile.exists()) && (!localFile.delete()));
/* 109 */     Object localObject = new FileRandomAccessStorage(localFile);
/* 110 */     return (RandomAccessStorage) localObject;
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.VMemJimiImageFactory
 * JD-Core Version:    0.6.2
 */