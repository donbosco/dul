/*     */ package com.sun.jimi.core.encoder.pcx;
/*     */ 
/*     */ import com.sun.jimi.core.JimiException;
/*     */ import com.sun.jimi.core.compat.AdaptiveRasterImage;
/*     */ import com.sun.jimi.core.compat.JimiEncoderBase;
/*     */ import com.sun.jimi.core.util.LEDataOutputStream;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.image.DirectColorModel;
/*     */ import java.awt.image.IndexColorModel;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.FilterOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ 
/*     */ public class PCXEncoder extends JimiEncoderBase
/*     */ {
/*     */   OutputStream myOut;
/*     */   RLEOutputStreamForPCX dataOut;
/*     */   LEDataOutputStream LEDOut;
/*     */   private int nState;
/*  44 */   int nBitsPerPixel = 8;
/*  45 */   int nBitPlanes = 1;
/*  46 */   int nBytesPerLine = 0;
/*  47 */   int nPaletteType = 1;
/*  48 */   int nVersion = 5;
/*     */   AdaptiveRasterImage myJimiImage;
/*  52 */   protected int COLOR_MODEL = 0;
/*     */ 
/*     */   public boolean driveEncoder()
/*     */     throws JimiException
/*     */   {
/* 280 */     AdaptiveRasterImage localAdaptiveRasterImage = getJimiImage();
/*     */     try
/*     */     {
/* 284 */       writePcxHeader(this.LEDOut, localAdaptiveRasterImage);
/* 285 */       writePcxImage(this.dataOut, localAdaptiveRasterImage);
/* 286 */       if (this.COLOR_MODEL == 0) {
/* 287 */         writePcxFooter(this.LEDOut, localAdaptiveRasterImage);
/*     */       }
/*     */ 
/* 293 */       this.LEDOut.flush();
/* 294 */       this.dataOut.flush();
/* 295 */       this.myOut.flush();
/* 296 */       this.nState = 2;
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/* 301 */       this.nState = 1;
/*     */     }
/* 303 */     return false;
/*     */   }
/*     */ 
/*     */   public void freeEncoder()
/*     */     throws JimiException
/*     */   {
/* 309 */     this.myOut = null;
/* 310 */     this.dataOut = null;
/* 311 */     this.LEDOut = null;
/* 312 */     super.freeEncoder();
/*     */   }
/*     */ 
/*     */   public int getState()
/*     */   {
/* 318 */     return this.nState;
/*     */   }
/*     */ 
/*     */   public void initSpecificEncoder(OutputStream paramOutputStream, AdaptiveRasterImage paramAdaptiveRasterImage)
/*     */     throws JimiException
/*     */   {
/*  58 */     this.nState = 0;
/*  59 */     this.myOut = paramOutputStream;
/*  60 */     if ((paramAdaptiveRasterImage.getColorModel() instanceof DirectColorModel))
/*     */     {
/*  62 */       this.nBitPlanes = 3;
/*  63 */       this.COLOR_MODEL = 1;
/*     */     }
/*     */ 
/*  68 */     this.LEDOut = new LEDataOutputStream(new BufferedOutputStream(this.myOut));
/*  69 */     this.dataOut = new RLEOutputStreamForPCX(this.LEDOut);
/*     */   }
/*     */ 
/*     */   public byte[] makePalette(IndexColorModel paramIndexColorModel, int paramInt)
/*     */   {
/* 156 */     int i = paramInt * 3;
/* 157 */     byte[] arrayOfByte1 = new byte[i];
/*     */ 
/* 159 */     ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream(i);
/*     */ 
/* 162 */     int j = paramIndexColorModel.getMapSize();
/*     */ 
/* 164 */     byte[] arrayOfByte2 = new byte[j];
/* 165 */     byte[] arrayOfByte3 = new byte[j];
/* 166 */     byte[] arrayOfByte4 = new byte[j];
/*     */ 
/* 170 */     paramIndexColorModel.getReds(arrayOfByte2);
/* 171 */     paramIndexColorModel.getGreens(arrayOfByte3);
/* 172 */     paramIndexColorModel.getBlues(arrayOfByte4);
/*     */     try
/*     */     {
/*     */       int k;
/* 179 */       if (paramInt < j)
/* 180 */         k = paramInt;
/*     */       else {
/* 182 */         k = j;
/*     */       }
/*     */ 
/* 185 */       for (int m = 0; m < k; m++)
/*     */       {
/* 187 */         localByteArrayOutputStream.write(arrayOfByte2[m]);
/* 188 */         localByteArrayOutputStream.write(arrayOfByte3[m]);
/* 189 */         localByteArrayOutputStream.write(arrayOfByte4[m]);
/*     */       }
/*     */ 
/* 194 */       if (paramInt > j) {
/* 195 */         for (int n = 0; n < paramInt - k; n++) {
/* 196 */           localByteArrayOutputStream.write(0);
/* 197 */           localByteArrayOutputStream.write(0);
/* 198 */           localByteArrayOutputStream.write(0);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 204 */       arrayOfByte1 = localByteArrayOutputStream.toByteArray();
/* 205 */       localByteArrayOutputStream.close();
/* 206 */       localByteArrayOutputStream = null;
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/* 210 */       this.nState = 1;
/*     */     }
/* 212 */     return arrayOfByte1;
/*     */   }
/*     */ 
/*     */   public void writePcxFooter(LEDataOutputStream paramLEDataOutputStream, AdaptiveRasterImage paramAdaptiveRasterImage)
/*     */   {
/* 259 */     byte[] arrayOfByte = new byte[768];
/*     */ 
/* 261 */     IndexColorModel localIndexColorModel = (IndexColorModel)paramAdaptiveRasterImage.getColorModel();
/*     */ 
/* 263 */     arrayOfByte = makePalette(localIndexColorModel, 256);
/*     */     try
/*     */     {
/* 267 */       paramLEDataOutputStream.write(12);
/* 268 */       paramLEDataOutputStream.write(arrayOfByte, 0, arrayOfByte.length);
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/* 272 */       this.nState = 1;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void writePcxHeader(LEDataOutputStream paramLEDataOutputStream, AdaptiveRasterImage paramAdaptiveRasterImage)
/*     */   {
/*  76 */     byte[] arrayOfByte1 = new byte[54];
/*  77 */     byte[] arrayOfByte2 = new byte[48];
/*  78 */     byte[] arrayOfByte3 = new byte[2];
/*     */ 
/*  80 */     int i = 0;
/*  81 */     int j = 0;
/*  82 */     int k = 0;
/*  83 */     int m = 0;
/*     */ 
/*  87 */     int n = paramAdaptiveRasterImage.getWidth();
/*  88 */     int i1 = paramAdaptiveRasterImage.getHeight();
/*     */ 
/*  90 */     int i2 = n;
/*     */ 
/*  92 */     k = i + n - 1;
/*  93 */     m = i + i1 - 1;
/*     */     try
/*     */     {
/* 101 */       paramLEDataOutputStream.write(10);
/* 102 */       paramLEDataOutputStream.write((byte)this.nVersion);
/* 103 */       paramLEDataOutputStream.write(1);
/* 104 */       paramLEDataOutputStream.write((byte)this.nBitsPerPixel);
/* 105 */       paramLEDataOutputStream.writeShort(0);
/* 106 */       paramLEDataOutputStream.writeShort(0);
/*     */ 
/* 108 */       paramLEDataOutputStream.writeShort(k);
/* 109 */       paramLEDataOutputStream.writeShort(m);
/* 110 */       paramLEDataOutputStream.writeShort(n);
/* 111 */       paramLEDataOutputStream.writeShort(i1);
/*     */ 
/* 117 */       if (this.COLOR_MODEL == 0) {
/* 118 */         IndexColorModel localObject = (IndexColorModel)paramAdaptiveRasterImage.getColorModel();
/* 119 */         arrayOfByte2 = makePalette((IndexColorModel)localObject, 16);
/*     */       }
/* 121 */       paramLEDataOutputStream.write(arrayOfByte2);
/*     */ 
/* 123 */       paramLEDataOutputStream.write(0);
/* 124 */       paramLEDataOutputStream.write((byte)this.nBitPlanes);
/* 125 */       paramLEDataOutputStream.writeShort(i2);
/*     */ 
/* 128 */       paramLEDataOutputStream.writeShort(this.nPaletteType);
/*     */ 
/* 133 */       Object localObject = Toolkit.getDefaultToolkit();
/*     */ 
/* 135 */       paramLEDataOutputStream.writeShort(((Toolkit)localObject).getScreenSize().width);
/* 136 */       paramLEDataOutputStream.writeShort(((Toolkit)localObject).getScreenSize().height);
/*     */ 
/* 140 */       paramLEDataOutputStream.write(arrayOfByte1);
/* 141 */       paramLEDataOutputStream.flush();
/*     */     }
/*     */     catch (IOException localIOException) {
/* 144 */       this.nState = 1;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void writePcxImage(RLEOutputStreamForPCX paramRLEOutputStreamForPCX, AdaptiveRasterImage paramAdaptiveRasterImage)
/*     */     throws JimiException
/*     */   {
/* 219 */     int i = paramAdaptiveRasterImage.getWidth();
/* 220 */     int j = paramAdaptiveRasterImage.getHeight();
/* 221 */     this.nBytesPerLine = (i + 1);
/*     */ 
/* 224 */     byte[] arrayOfByte = new byte[this.nBytesPerLine];
/*     */     try
/*     */     {
/* 228 */       for (int k = 0; k < j; k++) {
/* 229 */         if (this.COLOR_MODEL == 0)
/*     */         {
/* 234 */           paramAdaptiveRasterImage.getChannel(0, k, arrayOfByte, 0);
/* 235 */           paramRLEOutputStreamForPCX.write(arrayOfByte);
/*     */         }
/*     */         else
/*     */         {
/* 239 */           paramAdaptiveRasterImage.setRGBDefault(true);
/* 240 */           paramAdaptiveRasterImage.getChannel(1, k, arrayOfByte, 0);
/* 241 */           paramRLEOutputStreamForPCX.write(arrayOfByte);
/* 242 */           paramAdaptiveRasterImage.getChannel(2, k, arrayOfByte, 0);
/* 243 */           paramRLEOutputStreamForPCX.write(arrayOfByte);
/* 244 */           paramAdaptiveRasterImage.getChannel(3, k, arrayOfByte, 0);
/* 245 */           paramRLEOutputStreamForPCX.write(arrayOfByte);
/*     */         }
/* 247 */         setProgress(k * 100 / j);
/*     */       }
/*     */     }
/*     */     catch (IOException localIOException) {
/* 251 */       this.nState = 1;
/*     */     }
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.encoder.pcx.PCXEncoder
 * JD-Core Version:    0.6.2
 */