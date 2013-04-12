/*     */ package com.sun.jimi.core.encoder.psd;
/*     */ 
/*     */ import com.sun.jimi.core.JimiException;
/*     */ import com.sun.jimi.core.compat.AdaptiveRasterImage;
/*     */ import com.sun.jimi.core.compat.JimiEncoderBase;
/*     */ import com.sun.jimi.core.util.JimiUtil;
/*     */ import com.sun.jimi.core.util.Packbits;
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.DirectColorModel;
/*     */ import java.awt.image.IndexColorModel;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.FilterOutputStream;
/*     */ import java.io.IOException;
/*     */ 
/*     */ class PSDFileHeader
/*     */   implements EncodeImageIfc
/*     */ {
/*     */   static final int PSD_SIGNATURE = 943870035;
/*     */   static final short BITMAP = 0;
/*     */   static final short GRAYSCALE = 1;
/*     */   static final short INDEXED = 2;
/*     */   static final short RGB = 3;
/*     */   AdaptiveRasterImage ji_;
/*     */   DataOutputStream out_;
/*     */   short channels_;
/*     */   int rows_;
/*     */   int cols_;
/*     */   short depth_;
/*     */   short colorMode_;
/*     */   JimiEncoderBase encoder_;
/*     */   short[] SLLTable_;
/*     */   int sllOffset_;
/*     */ 
/*     */   PSDFileHeader(JimiEncoderBase paramJimiEncoderBase, AdaptiveRasterImage paramAdaptiveRasterImage, DataOutputStream paramDataOutputStream)
/*     */     throws JimiException
/*     */   {
/*  68 */     this.ji_ = paramAdaptiveRasterImage;
/*  69 */     this.out_ = paramDataOutputStream;
/*     */ 
/*  71 */     this.encoder_ = paramJimiEncoderBase;
/*     */ 
/*  73 */     this.colorMode_ = -1;
/*  74 */     ColorModel localColorModel = this.ji_.getColorModel();
/*  75 */     this.cols_ = this.ji_.getWidth();
/*  76 */     this.rows_ = this.ji_.getHeight();
/*     */ 
/*  78 */     if ((localColorModel instanceof IndexColorModel))
/*     */     {
/*  80 */       this.channels_ = 1;
/*  81 */       this.colorMode_ = 2;
/*  82 */       this.depth_ = 8;
/*     */     }
/*  84 */     else if ((localColorModel instanceof DirectColorModel))
/*     */     {
/*  86 */       DirectColorModel localDirectColorModel = (DirectColorModel)localColorModel;
/*  87 */       int i = localDirectColorModel.getRedMask();
/*  88 */       int j = localDirectColorModel.getGreenMask();
/*  89 */       int k = localDirectColorModel.getBlueMask();
/*  90 */       int m = localDirectColorModel.getPixelSize();
/*     */ 
/*  94 */       if ((i == (1 << m) - 1) && 
/*  95 */         (i == j) && (j == k))
/*     */       {
/*  97 */         this.channels_ = 1;
/*  98 */         if (m == 1)
/*     */         {
/* 100 */           this.depth_ = 1;
/* 101 */           this.colorMode_ = 0;
/*     */         }
/*     */         else
/*     */         {
/* 105 */           this.depth_ = 8;
/* 106 */           this.colorMode_ = 1;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 113 */     if (this.colorMode_ == -1)
/*     */     {
/* 115 */       this.channels_ = 3;
/* 116 */       this.colorMode_ = 3;
/* 117 */       this.depth_ = 8;
/* 118 */       this.ji_.setRGBDefault(true);
/*     */     }
/*     */   }
/*     */ 
/*     */   EncodeImageIfc createEncodeImage()
/*     */   {
/* 141 */     return this;
/*     */   }
/*     */ 
/*     */   public void encodeImage(AdaptiveRasterImage paramAdaptiveRasterImage, DataOutputStream paramDataOutputStream, int paramInt)
/*     */     throws JimiException
/*     */   {
/*     */     try
/*     */     {
/* 152 */       paramDataOutputStream.writeShort(paramInt);
/*     */ 
/* 154 */       switch (this.colorMode_)
/*     */       {
/*     */       case 0:
/* 157 */         if (paramInt == 0)
/*     */         {
/* 162 */           outputGrayChannel(3, paramAdaptiveRasterImage, paramDataOutputStream, 0);
/*     */         }
/* 164 */         else outputRLE(paramAdaptiveRasterImage, paramDataOutputStream);
/* 165 */         break;
/*     */       case 1:
/* 168 */         if (paramInt == 0)
/*     */         {
/* 173 */           outputGrayChannel(3, paramAdaptiveRasterImage, paramDataOutputStream, 0);
/*     */         }
/* 175 */         else outputRLE(paramAdaptiveRasterImage, paramDataOutputStream);
/* 176 */         break;
/*     */       case 2:
/* 179 */         if (paramInt == 0)
/* 180 */           outputRawChannel(3, paramAdaptiveRasterImage, paramDataOutputStream);
/*     */         else
/* 182 */           outputRLE(paramAdaptiveRasterImage, paramDataOutputStream);
/* 183 */         break;
/*     */       case 3:
/* 186 */         if (paramInt == 0)
/* 187 */           outputRawRGB(paramAdaptiveRasterImage, paramDataOutputStream);
/*     */         else
/* 189 */           outputRLERGB(paramAdaptiveRasterImage, paramDataOutputStream);
/*     */         break;
/*     */       }
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/* 195 */       throw new JimiException(localIOException.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   void getRLELens(int paramInt1, AdaptiveRasterImage paramAdaptiveRasterImage, short[] paramArrayOfShort, int paramInt2)
/*     */     throws JimiException, IOException
/*     */   {
/* 409 */     byte[] arrayOfByte1 = new byte[this.cols_];
/* 410 */     byte[] arrayOfByte2 = new byte[this.cols_ + this.cols_ / 128 + 1];
/*     */ 
/* 412 */     for (int i = 0; i < this.rows_; i++)
/*     */     {
/* 414 */       paramAdaptiveRasterImage.getChannel(paramInt1, i, arrayOfByte1, 0);
/* 415 */       paramArrayOfShort[paramInt2] = ((short)Packbits.packbits(arrayOfByte1, arrayOfByte2));
/* 416 */       paramInt2++;
/* 417 */       this.encoder_.setProgress(i * 100 / this.rows_);
/*     */     }
/*     */   }
/*     */ 
/*     */   void outputGrayChannel(int paramInt1, AdaptiveRasterImage paramAdaptiveRasterImage, DataOutputStream paramDataOutputStream, int paramInt2)
/*     */     throws JimiException, IOException
/*     */   {
/* 313 */     byte[] arrayOfByte1 = new byte[this.cols_];
/* 314 */     byte[] arrayOfByte2 = null;
/* 315 */     byte[] arrayOfByte3 = null;
/*     */ 
/* 317 */     if (paramInt2 != 0) {
/* 318 */       arrayOfByte2 = new byte[this.cols_ + this.cols_ / 128 + 1];
/*     */     }
/* 320 */     int k = paramAdaptiveRasterImage.getColorModel().getPixelSize();
/*     */ 
/* 322 */     int m = 0;
/* 323 */     ColorModel localColorModel = paramAdaptiveRasterImage.getColorModel();
/*     */ 
/* 326 */     for (int i = 0; i < this.rows_; i++)
/*     */     {
/* 328 */       this.encoder_.setProgress(i * 100 / this.rows_);
/* 329 */       paramAdaptiveRasterImage.getChannel(paramInt1, i, arrayOfByte1, 0);
/*     */       int n;
/*     */       int j;
/* 334 */       if (k == 1)
/*     */       {
/* 337 */         if (arrayOfByte3 == null) {
/* 338 */           arrayOfByte3 = new byte[arrayOfByte1.length / 8 + (arrayOfByte1.length % 8 != 0 ? 1 : 0)];
/*     */         }
/* 340 */         JimiUtil.packPixels(1, arrayOfByte1, arrayOfByte3);
/*     */ 
/* 344 */         if (m == 0)
/*     */         {
/* 346 */           n = arrayOfByte3.length;
/*     */           do { arrayOfByte3[n] = ((byte)(arrayOfByte3[n] ^ 0xFFFFFFFF));
/*     */ 
/* 346 */             n--; } while (n >= 0);
/*     */         }
/*     */ 
/* 350 */         if (paramInt2 == 0) {
/* 351 */           paramDataOutputStream.write(arrayOfByte3);
/*     */         }
/*     */         else {
/* 354 */           j = Packbits.packbits(arrayOfByte3, arrayOfByte2);
/* 355 */           if (this.SLLTable_ != null)
/* 356 */             this.SLLTable_[(this.sllOffset_++)] = ((short)j);
/*     */           else
/* 358 */             paramDataOutputStream.write(arrayOfByte2, 0, j);
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 363 */         if (k < 8) {
/* 364 */           JimiUtil.pixelDepthChange(k, arrayOfByte1, 8);
/*     */         }
/*     */ 
/* 368 */         if (m != 0)
/*     */         {
/* 370 */           n = arrayOfByte1.length;
/*     */           do { arrayOfByte1[n] = ((byte)(arrayOfByte1[n] ^ 0xFFFFFFFF));
/*     */ 
/* 370 */             n--; } while (n >= 0);
/*     */         }
/*     */ 
/* 374 */         if (paramInt2 == 0) {
/* 375 */           paramDataOutputStream.write(arrayOfByte1);
/*     */         }
/*     */         else {
/* 378 */           j = Packbits.packbits(arrayOfByte1, arrayOfByte2);
/* 379 */           if (this.SLLTable_ != null)
/* 380 */             this.SLLTable_[(this.sllOffset_++)] = ((short)j);
/*     */           else
/* 382 */             paramDataOutputStream.write(arrayOfByte2, 0, j);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   void outputRLE(AdaptiveRasterImage paramAdaptiveRasterImage, DataOutputStream paramDataOutputStream)
/*     */     throws JimiException, IOException
/*     */   {
/* 243 */     short[] arrayOfShort = new short[this.rows_ * this.channels_];
/*     */ 
/* 246 */     if (this.colorMode_ == 2)
/*     */     {
/* 248 */       getRLELens(0, paramAdaptiveRasterImage, arrayOfShort, 0);
/*     */     }
/*     */     else
/*     */     {
/* 252 */       this.SLLTable_ = arrayOfShort;
/* 253 */       this.sllOffset_ = 0;
/* 254 */       outputGrayChannel(0, paramAdaptiveRasterImage, paramDataOutputStream, 1);
/* 255 */       this.SLLTable_ = null;
/*     */     }
/*     */ 
/* 259 */     for (int i = 0; i < arrayOfShort.length; i++)
/*     */     {
/* 261 */       paramDataOutputStream.write((arrayOfShort[i] & 0xFF00) >> 8);
/* 262 */       paramDataOutputStream.write(arrayOfShort[i] & 0xFF);
/*     */     }
/*     */ 
/* 265 */     if (this.colorMode_ == 2)
/*     */     {
/* 267 */       outputRLEChannel(0, paramAdaptiveRasterImage, paramDataOutputStream);
/*     */     }
/*     */     else
/*     */     {
/* 275 */       outputGrayChannel(0, paramAdaptiveRasterImage, paramDataOutputStream, 1);
/*     */     }
/*     */   }
/*     */ 
/*     */   void outputRLEChannel(int paramInt, AdaptiveRasterImage paramAdaptiveRasterImage, DataOutputStream paramDataOutputStream)
/*     */     throws JimiException, IOException
/*     */   {
/* 428 */     byte[] arrayOfByte1 = new byte[this.cols_];
/* 429 */     byte[] arrayOfByte2 = new byte[this.cols_ + this.cols_ / 128 + 1];
/*     */ 
/* 432 */     for (int i = 0; i < this.rows_; i++)
/*     */     {
/* 434 */       paramAdaptiveRasterImage.getChannel(paramInt, i, arrayOfByte1, 0);
/* 435 */       int j = Packbits.packbits(arrayOfByte1, arrayOfByte2);
/* 436 */       paramDataOutputStream.write(arrayOfByte2, 0, j);
/* 437 */       this.encoder_.setProgress(i * 100 / this.rows_);
/*     */     }
/*     */   }
/*     */ 
/*     */   void outputRLERGB(AdaptiveRasterImage paramAdaptiveRasterImage, DataOutputStream paramDataOutputStream)
/*     */     throws JimiException, IOException
/*     */   {
/* 211 */     byte[] arrayOfByte = new byte[this.cols_];
/* 212 */     short[] arrayOfShort = new short[this.rows_ * this.channels_];
/*     */ 
/* 215 */     int i = 0;
/* 216 */     getRLELens(16, paramAdaptiveRasterImage, arrayOfShort, i);
/* 217 */     i += this.rows_;
/* 218 */     getRLELens(8, paramAdaptiveRasterImage, arrayOfShort, i);
/* 219 */     i += this.rows_;
/* 220 */     getRLELens(0, paramAdaptiveRasterImage, arrayOfShort, i);
/*     */ 
/* 223 */     for (int j = 0; j < arrayOfShort.length; j++)
/*     */     {
/* 225 */       paramDataOutputStream.write((arrayOfShort[j] & 0xFF00) >> 8);
/* 226 */       paramDataOutputStream.write(arrayOfShort[j] & 0xFF);
/*     */     }
/*     */ 
/* 229 */     outputRLEChannel(16, paramAdaptiveRasterImage, paramDataOutputStream);
/* 230 */     outputRLEChannel(8, paramAdaptiveRasterImage, paramDataOutputStream);
/* 231 */     outputRLEChannel(0, paramAdaptiveRasterImage, paramDataOutputStream);
/*     */   }
/*     */ 
/*     */   void outputRawChannel(int paramInt, AdaptiveRasterImage paramAdaptiveRasterImage, DataOutputStream paramDataOutputStream)
/*     */     throws JimiException, IOException
/*     */   {
/* 392 */     byte[] arrayOfByte = new byte[this.cols_];
/*     */ 
/* 394 */     for (int i = 0; i < this.rows_; i++)
/*     */     {
/* 396 */       paramAdaptiveRasterImage.getChannel(paramInt, i, arrayOfByte, 0);
/* 397 */       paramDataOutputStream.write(arrayOfByte);
/* 398 */       this.encoder_.setProgress(i * 100 / this.rows_);
/*     */     }
/*     */   }
/*     */ 
/*     */   void outputRawRGB(AdaptiveRasterImage paramAdaptiveRasterImage, DataOutputStream paramDataOutputStream)
/*     */     throws JimiException, IOException
/*     */   {
/* 202 */     outputRawChannel(16, paramAdaptiveRasterImage, paramDataOutputStream);
/* 203 */     outputRawChannel(8, paramAdaptiveRasterImage, paramDataOutputStream);
/* 204 */     outputRawChannel(0, paramAdaptiveRasterImage, paramDataOutputStream);
/*     */   }
/*     */ 
/*     */   void write()
/*     */     throws IOException
/*     */   {
/* 125 */     this.out_.writeInt(943870035);
/* 126 */     this.out_.writeShort(1);
/*     */ 
/* 128 */     int i = 6;
/*     */     do { this.out_.writeByte(0);
/*     */ 
/* 128 */       i--; } while (i >= 0);
/*     */ 
/* 131 */     this.out_.writeShort(this.channels_);
/* 132 */     this.out_.writeInt(this.rows_);
/* 133 */     this.out_.writeInt(this.cols_);
/* 134 */     this.out_.writeShort(this.depth_);
/*     */ 
/* 136 */     this.out_.writeShort(this.colorMode_);
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.encoder.psd.PSDFileHeader
 * JD-Core Version:    0.6.2
 */