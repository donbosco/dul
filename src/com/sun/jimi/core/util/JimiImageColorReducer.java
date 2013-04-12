/*     */ package com.sun.jimi.core.util;
/*     */ 
/*     */ import com.sun.jimi.core.Jimi;
/*     */ import com.sun.jimi.core.JimiException;
/*     */ import com.sun.jimi.core.JimiImage;
/*     */ import com.sun.jimi.core.MemoryJimiImageFactory;
/*     */ import com.sun.jimi.core.MutableJimiImage;
/*     */ import com.sun.jimi.core.raster.ByteRasterImage;
/*     */ import com.sun.jimi.core.raster.JimiRasterImage;
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.ImageProducer;
/*     */ import java.awt.image.IndexColorModel;
/*     */ 
/*     */ public class JimiImageColorReducer
/*     */ {
/*     */   ColorOctree octree;
/*     */   int maxColors;
/*     */   byte[] rgbCMap;
/*     */   int numColors;
/*     */ 
/*     */   public JimiImageColorReducer(int paramInt)
/*     */   {
/*  41 */     this.maxColors = paramInt;
/*  42 */     this.octree = new ColorOctree(paramInt);
/*     */   }
/*     */ 
/*     */   public JimiImageColorReducer(IndexColorModel paramIndexColorModel)
/*     */   {
/*  53 */     this(paramIndexColorModel, paramIndexColorModel.getMapSize());
/*     */   }
/*     */ 
/*     */   public JimiImageColorReducer(IndexColorModel paramIndexColorModel, int paramInt)
/*     */   {
/*  58 */     this.maxColors = paramInt;
/*  59 */     byte[] arrayOfByte1 = new byte[paramIndexColorModel.getMapSize() * 4];
/*  60 */     this.numColors = paramIndexColorModel.getMapSize();
/*  61 */     byte[] arrayOfByte2 = new byte[paramIndexColorModel.getMapSize()];
/*  62 */     byte[] arrayOfByte3 = new byte[paramIndexColorModel.getMapSize()];
/*  63 */     byte[] arrayOfByte4 = new byte[paramIndexColorModel.getMapSize()];
/*  64 */     byte[] arrayOfByte5 = new byte[paramIndexColorModel.getMapSize()];
/*  65 */     paramIndexColorModel.getReds(arrayOfByte2);
/*  66 */     paramIndexColorModel.getGreens(arrayOfByte3);
/*  67 */     paramIndexColorModel.getBlues(arrayOfByte4);
/*  68 */     paramIndexColorModel.getAlphas(arrayOfByte5);
/*  69 */     int i = 0;
/*  70 */     for (int j = 0; j < arrayOfByte2.length; j++) {
/*  71 */       arrayOfByte1[(i++)] = arrayOfByte2[j];
/*  72 */       arrayOfByte1[(i++)] = arrayOfByte3[j];
/*  73 */       arrayOfByte1[(i++)] = arrayOfByte4[j];
/*  74 */       arrayOfByte1[(i++)] = arrayOfByte5[j];
/*     */     }
/*  76 */     this.rgbCMap = arrayOfByte1;
/*     */   }
/*     */ 
/*     */   public JimiImageColorReducer(byte[] paramArrayOfByte, int paramInt)
/*     */   {
/*  47 */     this.rgbCMap = paramArrayOfByte;
/*  48 */     this.numColors = paramInt;
/*     */   }
/*     */ 
/*     */   public JimiRasterImage colorReduce(JimiRasterImage paramJimiRasterImage)
/*     */     throws JimiException
/*     */   {
/*  95 */     paramJimiRasterImage.waitFinished();
/*     */ 
/* 100 */     ColorModel localColorModel = paramJimiRasterImage.getColorModel();
/* 101 */     if ((localColorModel instanceof IndexColorModel))
/*     */     {
/* 103 */       if (((IndexColorModel)localColorModel).getMapSize() <= this.maxColors)
/* 104 */         return paramJimiRasterImage;
/*     */     }
/*     */     ByteRasterImage localByteRasterImage;
/* 107 */     if (this.octree != null)
/*     */     {
/* 109 */       fillOctree(paramJimiRasterImage);
/* 110 */       byte[] arrayOfByte = new byte[this.maxColors * 4];
/* 111 */       int i = this.octree.getPalette(arrayOfByte);
/* 112 */       localByteRasterImage = new MemoryJimiImageFactory().createByteRasterImage(paramJimiRasterImage.getWidth(), paramJimiRasterImage.getHeight(), 
/* 113 */         new IndexColorModel(8, arrayOfByte.length / 4, arrayOfByte, 0, true));
/* 114 */       fillJimiRasterImage(arrayOfByte, i, localByteRasterImage, paramJimiRasterImage);
/*     */     }
/*     */     else
/*     */     {
/* 118 */       localByteRasterImage = new MemoryJimiImageFactory().createByteRasterImage(paramJimiRasterImage.getWidth(), paramJimiRasterImage.getHeight(), 
/* 119 */         new IndexColorModel(8, this.numColors, this.rgbCMap, 0, false));
/* 120 */       fillJimiRasterImage(this.rgbCMap, this.numColors, localByteRasterImage, paramJimiRasterImage);
/*     */     }
/*     */ 
/* 123 */     return localByteRasterImage;
/*     */   }
/*     */ 
/*     */   public JimiRasterImage colorReduce(ImageProducer paramImageProducer)
/*     */     throws JimiException
/*     */   {
/*  81 */     return colorReduce(Jimi.createRasterImage(paramImageProducer));
/*     */   }
/*     */ 
/*     */   public JimiRasterImage colorReduceFS(JimiRasterImage paramJimiRasterImage)
/*     */     throws JimiException
/*     */   {
/* 133 */     paramJimiRasterImage.waitFinished();
/*     */ 
/* 138 */     ColorModel localColorModel = paramJimiRasterImage.getColorModel();
/* 139 */     if ((localColorModel instanceof IndexColorModel))
/*     */     {
/* 141 */       if (((IndexColorModel)localColorModel).getMapSize() <= this.maxColors)
/* 142 */         return paramJimiRasterImage;
/*     */     }
/*     */     ByteRasterImage localByteRasterImage;
/* 145 */     if (this.octree != null)
/*     */     {
/* 147 */       fillOctree(paramJimiRasterImage);
/* 148 */       byte[] arrayOfByte = new byte[this.maxColors * 4];
/* 149 */       int i = this.octree.getPalette(arrayOfByte);
/*     */ 
/* 151 */       if (this.octree.hasAlpha()) {
/* 152 */         i--;
/*     */       }
/* 154 */       localByteRasterImage = new MemoryJimiImageFactory().createByteRasterImage(paramJimiRasterImage.getWidth(), paramJimiRasterImage.getHeight(), 
/* 155 */         new IndexColorModel(8, this.maxColors, arrayOfByte, 0, true));
/*     */ 
/* 157 */       fillJimiRasterImageFS(arrayOfByte, i, localByteRasterImage, paramJimiRasterImage);
/*     */     }
/*     */     else
/*     */     {
/* 161 */       localByteRasterImage = new MemoryJimiImageFactory().createByteRasterImage(paramJimiRasterImage.getWidth(), paramJimiRasterImage.getHeight(), 
/* 162 */         new IndexColorModel(8, this.maxColors, this.rgbCMap, 0, false));
/* 163 */       fillJimiRasterImageFS(this.rgbCMap, this.numColors, localByteRasterImage, paramJimiRasterImage);
/*     */     }
/*     */ 
/* 166 */     return localByteRasterImage;
/*     */   }
/*     */ 
/*     */   public JimiRasterImage colorReduceFS(ImageProducer paramImageProducer)
/*     */     throws JimiException
/*     */   {
/* 128 */     return colorReduceFS(Jimi.createRasterImage(paramImageProducer));
/*     */   }
/*     */ 
/*     */   private void fillJimiRasterImage(byte[] paramArrayOfByte, int paramInt, ByteRasterImage paramByteRasterImage, JimiRasterImage paramJimiRasterImage)
/*     */     throws JimiException
/*     */   {
/* 187 */     int[] arrayOfInt = new int[paramJimiRasterImage.getWidth()];
/* 188 */     InverseColorMap localInverseColorMap = new InverseColorMap(paramArrayOfByte);
/*     */ 
/* 190 */     byte[] arrayOfByte = new byte[paramJimiRasterImage.getWidth()];
/* 191 */     for (int i = 0; i < paramJimiRasterImage.getHeight(); i++)
/*     */     {
/* 193 */       paramJimiRasterImage.getRowRGB(i, arrayOfInt, 0);
/*     */ 
/* 195 */       for (int j = 0; j < paramByteRasterImage.getWidth(); j++)
/*     */       {
/*     */         int k;
/* 197 */         if (this.octree != null)
/*     */         {
/* 199 */           k = this.octree.quantizeColor(arrayOfInt[j]);
/* 200 */           arrayOfByte[j] = ((byte)(k / 3));
/*     */         }
/*     */         else
/*     */         {
/* 204 */           k = localInverseColorMap.getIndexNearest(arrayOfInt[j]);
/* 205 */           arrayOfByte[j] = ((byte)k);
/*     */         }
/*     */       }
/*     */ 
/* 209 */       paramByteRasterImage.setRow(i, arrayOfByte, 0);
/*     */     }
/* 211 */     paramByteRasterImage.setFinished();
/*     */   }
/*     */ 
/*     */   private void fillJimiRasterImageFS(byte[] paramArrayOfByte, int paramInt, ByteRasterImage paramByteRasterImage, JimiRasterImage paramJimiRasterImage)
/*     */     throws JimiException
/*     */   {
/* 252 */     int[] arrayOfInt = new int[paramJimiRasterImage.getWidth()];
/*     */ 
/* 254 */     FSDither localFSDither = new FSDither(paramArrayOfByte, paramInt, paramJimiRasterImage.getWidth());
/*     */ 
/* 256 */     byte[] arrayOfByte = new byte[paramJimiRasterImage.getWidth()];
/* 257 */     for (int i = 0; i < paramJimiRasterImage.getHeight(); i++)
/*     */     {
/* 259 */       paramJimiRasterImage.getRowRGB(i, arrayOfInt, 0);
/* 260 */       localFSDither.ditherRow(arrayOfInt, arrayOfByte);
/* 261 */       paramByteRasterImage.setRow(i, arrayOfByte, 0);
/*     */     }
/* 263 */     paramByteRasterImage.setFinished();
/*     */   }
/*     */ 
/*     */   private void fillOctree(JimiRasterImage paramJimiRasterImage)
/*     */     throws JimiException
/*     */   {
/* 278 */     int[] arrayOfInt = new int[paramJimiRasterImage.getWidth()];
/*     */ 
/* 280 */     for (int i = 0; i < paramJimiRasterImage.getHeight(); i++)
/*     */     {
/* 282 */       paramJimiRasterImage.getRowRGB(i, arrayOfInt, 0);
/* 283 */       this.octree.addColor(arrayOfInt);
/*     */     }
/*     */   }
/*     */ 
/*     */   int findIdx(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */   {
/* 219 */     int i = (paramInt2 & 0xFF0000) >> 16;
/* 220 */     int j = (paramInt2 & 0xFF00) >> 8;
/* 221 */     int k = paramInt2 & 0xFF;
/*     */ 
/* 225 */     int i3 = 0;
/*     */ 
/* 227 */     long l1 = 2000000000L;
/* 228 */     int m = paramInt1;
/*     */     do {
/* 230 */       int n = paramArrayOfByte[(m * 3)] & 0xFF;
/* 231 */       int i1 = paramArrayOfByte[(m * 3 + 1)] & 0xFF;
/* 232 */       int i2 = paramArrayOfByte[(m * 3 + 2)] & 0xFF;
/*     */ 
/* 234 */       long l2 = (i - n) * (i - n) + 
/* 235 */         (j - i1) * (j - i1) + 
/* 236 */         (k - i2) * (k - i2);
/* 237 */       if (l2 < l1)
/*     */       {
/* 239 */         i3 = m;
/* 240 */         l1 = l2;
/*     */       }
/* 228 */       m--; } while (m >= 0);
/*     */ 
/* 243 */     return i3;
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.util.JimiImageColorReducer
 * JD-Core Version:    0.6.2
 */