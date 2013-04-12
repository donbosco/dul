/*     */ package com.sun.jimi.core.decoder.tga;
/*     */ 
/*     */ import com.sun.jimi.core.JimiException;
/*     */ import com.sun.jimi.core.compat.AdaptiveRasterImage;
/*     */ import com.sun.jimi.core.compat.JimiDecoderBase;
/*     */ import com.sun.jimi.core.util.LEDataInputStream;
/*     */ import com.sun.jimi.core.util.ProgressMonitorSupport;
/*     */ import java.awt.image.DirectColorModel;
/*     */ import java.awt.image.IndexColorModel;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ 
/*     */ public class TGADecoder extends JimiDecoderBase
/*     */ {
/*     */   private AdaptiveRasterImage ji;
/*     */   private InputStream in;
/*     */   private LEDataInputStream dIn;
/*     */   private int state;
/*     */   TGAFileHeader tgaFH;
/*     */   TGAColorMap tgaCM;
/*     */ 
/*     */   private void decodeImage(LEDataInputStream paramLEDataInputStream)
/*     */     throws JimiException, IOException
/*     */   {
/* 156 */     switch (this.tgaFH.imageType)
/*     */     {
/*     */     case 1:
/* 159 */       decodeImageU8(paramLEDataInputStream);
/* 160 */       break;
/*     */     case 2:
/* 163 */       switch (this.tgaFH.pixelDepth)
/*     */       {
/*     */       case 16:
/* 166 */         decodeRGBImageU16(paramLEDataInputStream);
/* 167 */         break;
/*     */       case 24:
/*     */       case 32:
/* 170 */         decodeRGBImageU24_32(paramLEDataInputStream);
/* 171 */       }break;
/*     */     case 3:
/* 176 */       decodeImageU8(paramLEDataInputStream);
/* 177 */       break;
/*     */     case 9:
/* 180 */       throw new JimiException("TGADecoder Compressed Colormapped images not supported");
/*     */     case 10:
/* 183 */       throw new IOException("TGADecoder Compressed True Color images not supported");
/*     */     case 11:
/* 186 */       throw new IOException("TGADecoder Compressed Grayscale images not supported");
/*     */     case 4:
/*     */     case 5:
/*     */     case 6:
/*     */     case 7:
/*     */     case 8:
/*     */     }
/*     */   }
/*     */ 
/*     */   private void decodeImageU8(LEDataInputStream paramLEDataInputStream)
/*     */     throws IOException, JimiException
/*     */   {
/* 199 */     byte[] arrayOfByte = new byte[this.tgaFH.width];
/*     */ 
/* 201 */     for (int i = 0; i < this.tgaFH.height; i++)
/*     */     {
/* 203 */       paramLEDataInputStream.readFully(arrayOfByte, 0, this.tgaFH.width);
/*     */       int j;
/* 205 */       if (this.tgaFH.topToBottom)
/* 206 */         j = i;
/*     */       else {
/* 208 */         j = this.tgaFH.height - i - 1;
/*     */       }
/* 210 */       this.ji.setChannel(0, j, arrayOfByte);
/* 211 */       setProgress(i * 100 / this.tgaFH.height);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void decodeRGBImageU16(LEDataInputStream paramLEDataInputStream)
/*     */     throws IOException, JimiException
/*     */   {
/* 282 */     int[] arrayOfInt = new int[this.tgaFH.width];
/*     */ 
/* 284 */     for (int i = 0; i < this.tgaFH.height; i++)
/*     */     {
/* 288 */       for (int j = 0; j < this.tgaFH.width; j++)
/*     */       {
/* 291 */         arrayOfInt[j] = (paramLEDataInputStream.readUnsignedShort() & 0x7FFF);
/*     */       }
/*     */       int k;
/* 294 */       if (this.tgaFH.topToBottom)
/* 295 */         k = i;
/*     */       else {
/* 297 */         k = this.tgaFH.height - i - 1;
/*     */       }
/* 299 */       this.ji.setChannel(k, arrayOfInt);
/* 300 */       setProgress(i * 100 / this.tgaFH.height);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void decodeRGBImageU24_32(LEDataInputStream paramLEDataInputStream)
/*     */     throws IOException, JimiException
/*     */   {
/* 225 */     int n = this.tgaFH.width * (this.tgaFH.pixelDepth / 8);
/* 226 */     byte[] arrayOfByte = new byte[n];
/* 227 */     int[] arrayOfInt = new int[this.tgaFH.width];
/*     */ 
/* 229 */     for (int i = 0; i < this.tgaFH.height; i++)
/*     */     {
/* 231 */       paramLEDataInputStream.readFully(arrayOfByte, 0, n);
/*     */       int m;
/*     */       int j;
/* 233 */       if (this.tgaFH.pixelDepth == 24)
/*     */       {
/* 235 */         m = 0;
/* 236 */         for (j = 0; j < this.tgaFH.width; j++)
/*     */         {
/* 239 */           arrayOfInt[j] = 
/* 241 */             (-16777216 + (
/* 240 */             (arrayOfByte[(m + 2)] & 0xFF) << 16) + (
/* 241 */             (arrayOfByte[(m + 1)] & 0xFF) << 8) + (
/* 242 */             arrayOfByte[m] & 0xFF));
/* 243 */           m += 3;
/*     */         }
/*     */       }
/* 246 */       else if (this.tgaFH.pixelDepth == 32)
/*     */       {
/* 248 */         m = 0;
/* 249 */         for (j = 0; j < this.tgaFH.width; j++)
/*     */         {
/* 252 */           arrayOfInt[j] = 
/* 254 */             (((arrayOfByte[(m + 3)] & 0xFF) << 24) + (
/* 253 */             (arrayOfByte[(m + 2)] & 0xFF) << 16) + (
/* 254 */             (arrayOfByte[(m + 1)] & 0xFF) << 8) + (
/* 255 */             arrayOfByte[m] & 0xFF));
/* 256 */           m += 4;
/*     */         }
/*     */       }
/*     */       else {
/* 260 */         throw new JimiException("TGADecoder pixelDepth not 24 or 32");
/*     */       }
/*     */       int k;
/* 262 */       if (this.tgaFH.topToBottom)
/* 263 */         k = i;
/*     */       else {
/* 265 */         k = this.tgaFH.height - i - 1;
/*     */       }
/* 267 */       this.ji.setChannel(k, arrayOfInt);
/* 268 */       setProgress(i * 100 / this.tgaFH.height);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean driveDecoder()
/*     */     throws JimiException
/*     */   {
/*     */     try
/*     */     {
/*  61 */       this.tgaFH = new TGAFileHeader(this.dIn);
/*  62 */       this.tgaCM = new TGAColorMap(this.dIn, this.tgaFH);
/*  63 */       initJimiImage();
/*  64 */       this.state |= 2;
/*     */ 
/*  66 */       decodeImage(this.dIn);
/*  67 */       this.state |= 4;
/*  68 */       this.ji.addFullCoverage();
/*     */     }
/*     */     catch (IOException localIOException) {
/*  71 */       throw new JimiException("IO error reading TGA file");
/*     */     }
/*     */ 
/*  74 */     return false;
/*     */   }
/*     */ 
/*     */   public void freeDecoder() throws JimiException
/*     */   {
/*  79 */     this.in = null;
/*  80 */     this.dIn = null;
/*  81 */     this.ji = null;
/*     */   }
/*     */ 
/*     */   public AdaptiveRasterImage getJimiImage()
/*     */   {
/*  92 */     return this.ji;
/*     */   }
/*     */ 
/*     */   public int getState()
/*     */   {
/*  87 */     return this.state;
/*     */   }
/*     */ 
/*     */   public void initDecoder(InputStream paramInputStream, AdaptiveRasterImage paramAdaptiveRasterImage)
/*     */     throws JimiException
/*     */   {
/*  50 */     this.in = paramInputStream;
/*  51 */     this.dIn = new LEDataInputStream(new BufferedInputStream(paramInputStream));
/*  52 */     this.ji = paramAdaptiveRasterImage;
/*  53 */     this.state = 0;
/*     */   }
/*     */ 
/*     */   private void initJimiImage()
/*     */     throws JimiException
/*     */   {
/* 103 */     switch (this.tgaFH.imageType)
/*     */     {
/*     */     case 0:
/* 106 */       throw new JimiException("TGADecoder no image found.");
/*     */     case 1:
/*     */     case 9:
/* 110 */       if (this.tgaFH.colorMapType == 0) {
/* 111 */         throw new JimiException("TGADecoder color mapped images require a color map.");
/*     */       }
/* 113 */       this.ji.setColorModel(new IndexColorModel(8, this.tgaCM.cmap.length / 3, 
/* 114 */         this.tgaCM.cmap, 0, this.tgaFH.colorMapEntrySize == 32));
/* 115 */       break;
/*     */     case 3:
/*     */     case 11:
/* 119 */       if (this.tgaFH.colorMapType != 0) {
/* 120 */         throw new JimiException("TGADecoder gray scale should not have color map.");
/*     */       }
/* 122 */       this.ji.setColorModel(new DirectColorModel(8, 255, 255, 255));
/* 123 */       break;
/*     */     case 2:
/*     */     case 10:
/* 128 */       switch (this.tgaFH.pixelDepth)
/*     */       {
/*     */       case 16:
/* 131 */         this.ji.setColorModel(new DirectColorModel(16, 31744, 992, 31));
/* 132 */         break;
/*     */       case 24:
/*     */       case 32:
/* 136 */         this.ji.setColorModel(new DirectColorModel(24, 16711680, 65280, 255));
/* 137 */       }break;
/*     */     case 4:
/*     */     case 5:
/*     */     case 6:
/*     */     case 7:
/* 141 */     case 8: } this.ji.setSize(this.tgaFH.width, this.tgaFH.height);
/* 142 */     this.ji.setPixels();
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.decoder.tga.TGADecoder
 * JD-Core Version:    0.6.2
 */