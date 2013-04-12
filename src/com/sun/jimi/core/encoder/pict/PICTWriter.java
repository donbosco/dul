/*     */ package com.sun.jimi.core.encoder.pict;
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
/*     */ class PICTWriter
/*     */ {
/*     */   public static final int PICT_PACKBITSRECT = 152;
/*     */   public static final int PICT_9A = 154;
/*     */   public static final int PICT_END = 255;
/*     */   public static final int PICT_CLIP_RGN = 1;
/*     */   public static final int PICT_BITSRECT = 144;
/*     */   static final int GRAYSCALE = 1;
/*     */   static final int PALETTE = 2;
/*     */   static final int RGB = 3;
/*     */   AdaptiveRasterImage ji_;
/*     */   DataOutputStream out_;
/*     */   short width_;
/*     */   short height_;
/*     */   short pixelSize_;
/*     */   short rowBytes_;
/*     */   short packType_;
/*     */   boolean compress_;
/*     */   JimiEncoderBase encoder_;
/*     */   int outputCount_;
/*     */   byte[] bufC_;
/*     */ 
/*     */   PICTWriter(JimiEncoderBase paramJimiEncoderBase, AdaptiveRasterImage paramAdaptiveRasterImage, DataOutputStream paramDataOutputStream)
/*     */     throws JimiException
/*     */   {
/*  79 */     this.ji_ = paramAdaptiveRasterImage;
/*  80 */     this.out_ = paramDataOutputStream;
/*  81 */     this.encoder_ = paramJimiEncoderBase;
/*     */   }
/*     */ 
/*     */   void outputBufRow(byte[] paramArrayOfByte)
/*     */     throws IOException
/*     */   {
/* 396 */     if (this.compress_)
/*     */     {
/* 398 */       if (this.bufC_ == null) {
/* 399 */         this.bufC_ = new byte[paramArrayOfByte.length + 1 + paramArrayOfByte.length / 8];
/*     */       }
/* 401 */       int i = Packbits.packbits(paramArrayOfByte, this.bufC_);
/* 402 */       if (this.rowBytes_ > 250)
/* 403 */         this.out_.writeShort(i);
/*     */       else
/* 405 */         this.out_.writeByte(i);
/* 406 */       this.out_.write(this.bufC_, 0, i);
/* 407 */       this.outputCount_ += i;
/*     */     }
/*     */     else
/*     */     {
/* 411 */       this.out_.write(paramArrayOfByte);
/* 412 */       this.outputCount_ += paramArrayOfByte.length;
/*     */     }
/*     */   }
/*     */ 
/*     */   void outputEvenByteFlush() throws IOException
/*     */   {
/* 418 */     if ((this.outputCount_ & 0x1) != 0)
/* 419 */       this.out_.writeByte(0);
/*     */   }
/*     */ 
/*     */   void writeBitmap()
/*     */     throws IOException
/*     */   {
/* 218 */     this.out_.writeShort(0);
/* 219 */     this.out_.writeShort(0);
/* 220 */     this.out_.writeShort(this.height_);
/* 221 */     this.out_.writeShort(this.width_);
/*     */   }
/*     */ 
/*     */   void writeBitmapImageData(boolean paramBoolean)
/*     */     throws JimiException, IOException
/*     */   {
/* 248 */     byte[] arrayOfByte1 = new byte[this.width_];
/* 249 */     byte[] arrayOfByte2 = new byte[this.rowBytes_];
/*     */ 
/* 251 */     int j = 0;
/*     */ 
/* 253 */     for (int i = 0; i < this.height_; i++)
/*     */     {
/* 255 */       this.ji_.getChannel(0, i, arrayOfByte1, 0);
/* 256 */       int k = JimiUtil.packPixels(1, arrayOfByte1, arrayOfByte2);
/*     */ 
/* 258 */       if (!paramBoolean)
/*     */       {
/* 260 */         int m = arrayOfByte2.length;
/*     */         do { arrayOfByte2[m] = ((byte)(arrayOfByte2[m] ^ 0xFFFFFFFF));
/*     */ 
/* 260 */           m--; } while (m >= 0);
/*     */       }
/*     */ 
/* 264 */       outputBufRow(arrayOfByte2);
/* 265 */       this.encoder_.setProgress(i * 100 / this.height_);
/*     */     }
/* 267 */     outputEvenByteFlush();
/*     */   }
/*     */ 
/*     */   void writeColorTable(ColorModel paramColorModel)
/*     */     throws IOException
/*     */   {
/*     */     int k;
/*     */     int i;
/* 430 */     if ((paramColorModel instanceof IndexColorModel))
/*     */     {
/* 432 */       IndexColorModel localIndexColorModel = (IndexColorModel)paramColorModel;
/* 433 */       k = localIndexColorModel.getMapSize();
/*     */ 
/* 435 */       this.out_.writeInt(0);
/* 436 */       this.out_.writeShort(0);
/* 437 */       this.out_.writeShort((short)k - 1);
/*     */ 
/* 439 */       byte[] arrayOfByte1 = new byte[k];
/* 440 */       byte[] arrayOfByte2 = new byte[k];
/* 441 */       byte[] arrayOfByte3 = new byte[k];
/* 442 */       localIndexColorModel.getReds(arrayOfByte1);
/* 443 */       localIndexColorModel.getGreens(arrayOfByte2);
/* 444 */       localIndexColorModel.getBlues(arrayOfByte3);
/* 445 */       for (i = 0; i < k; i++)
/*     */       {
/* 447 */         this.out_.writeShort(i);
/* 448 */         this.out_.writeShort((short)((arrayOfByte1[i] & 0xFF) << 8 | arrayOfByte1[i] & 0xFF));
/* 449 */         this.out_.writeShort((short)((arrayOfByte2[i] & 0xFF) << 8 | arrayOfByte2[i] & 0xFF));
/* 450 */         this.out_.writeShort((short)((arrayOfByte3[i] & 0xFF) << 8 | arrayOfByte3[i] & 0xFF));
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 455 */       int j = paramColorModel.getPixelSize();
/* 456 */       k = 1 << j;
/*     */ 
/* 461 */       this.out_.writeInt(0);
/* 462 */       this.out_.writeShort(0);
/* 463 */       this.out_.writeShort((short)k - 1);
/*     */ 
/* 465 */       for (i = 0; i < k; i++)
/*     */       {
/* 467 */         this.out_.writeShort(i);
/* 468 */         int m = paramColorModel.getRed(i);
/* 469 */         int n = paramColorModel.getGreen(i);
/* 470 */         int i1 = paramColorModel.getBlue(i);
/* 471 */         this.out_.writeShort((short)(m << 8 | m));
/* 472 */         this.out_.writeShort((short)(n << 8 | n));
/* 473 */         this.out_.writeShort((short)(i1 << 8 | i1));
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   void writeDirectImageData()
/*     */     throws JimiException, IOException
/*     */   {
/* 366 */     byte[] arrayOfByte = new byte[this.rowBytes_];
/*     */ 
/* 371 */     for (int i = 0; i < this.height_; i++)
/*     */     {
/* 373 */       int j = 0;
/* 374 */       this.ji_.getChannel(16, i, arrayOfByte, j);
/* 375 */       j += this.width_;
/* 376 */       this.ji_.getChannel(8, i, arrayOfByte, j);
/* 377 */       j += this.width_;
/* 378 */       this.ji_.getChannel(0, i, arrayOfByte, j);
/*     */ 
/* 380 */       outputBufRow(arrayOfByte);
/* 381 */       this.encoder_.setProgress(i * 100 / this.height_);
/*     */     }
/* 383 */     outputEvenByteFlush();
/*     */   }
/*     */ 
/*     */   void writeHeader()
/*     */     throws IOException
/*     */   {
/* 481 */     byte[] arrayOfByte = new byte[64];
/*     */ 
/* 484 */     int i = 8;
/*     */     do { this.out_.write(arrayOfByte);
/*     */ 
/* 484 */       i--; } while (i >= 0);
/*     */ 
/* 486 */     this.out_.writeShort(0);
/*     */ 
/* 491 */     this.out_.writeShort(0);
/* 492 */     this.out_.writeShort(0);
/* 493 */     this.out_.writeShort(this.ji_.getHeight());
/* 494 */     this.out_.writeShort(this.ji_.getWidth());
/*     */ 
/* 496 */     this.out_.writeShort(17);
/* 497 */     this.out_.writeShort(767);
/*     */   }
/*     */ 
/*     */   void writeHeaders()
/*     */     throws IOException
/*     */   {
/*  87 */     writeHeader();
/*  88 */     writeV2AdditionalHeader();
/*     */   }
/*     */ 
/*     */   void writeImage()
/*     */     throws JimiException, IOException
/*     */   {
/*  96 */     int i = -1;
/*  97 */     ColorModel localColorModel = this.ji_.getColorModel();
/*     */ 
/*  99 */     int j = this.ji_.getWidth();
/* 100 */     int k = this.ji_.getHeight();
/*     */ 
/* 102 */     if ((j > 32767) || (k > 32767)) {
/* 103 */       throw new JimiException("PICT only saves images < 32767 in width or height");
/*     */     }
/* 105 */     this.width_ = ((short)j);
/* 106 */     this.height_ = ((short)k);
/*     */     int m;
/* 109 */     if ((localColorModel instanceof DirectColorModel))
/*     */     {
/* 111 */       m = localColorModel.getPixelSize();
/* 112 */       DirectColorModel localDirectColorModel = (DirectColorModel)localColorModel;
/* 113 */       int n = localDirectColorModel.getRedMask();
/* 114 */       int i1 = localDirectColorModel.getGreenMask();
/* 115 */       int i2 = localDirectColorModel.getBlueMask();
/*     */ 
/* 117 */       if ((m <= 8) && (n == i1) && (i1 == i2))
/*     */       {
/* 119 */         i = 1;
/* 120 */         this.pixelSize_ = ((short)m);
/*     */       }
/*     */     }
/* 123 */     else if ((localColorModel instanceof IndexColorModel))
/*     */     {
/* 126 */       i = 2;
/* 127 */       this.pixelSize_ = ((short)localColorModel.getPixelSize());
/*     */     }
/*     */ 
/* 131 */     if (i == -1)
/*     */     {
/* 133 */       i = 3;
/* 134 */       this.ji_.setRGBDefault(true);
/* 135 */       this.pixelSize_ = 32;
/*     */     }
/*     */ 
/* 139 */     switch (i)
/*     */     {
/*     */     case 1:
/* 142 */       if (this.pixelSize_ == 1)
/*     */       {
/* 145 */         this.rowBytes_ = ((short)(this.width_ / 8 + (j % 8 != 0 ? 1 : 0)));
/* 146 */       }break;
/*     */     case 2:
/* 151 */       this.rowBytes_ = ((short)(this.width_ * 8 / this.pixelSize_ + (
/* 152 */         j * 8 % this.pixelSize_ != 0 ? 1 : 0)));
/* 153 */       break;
/*     */     case 3:
/* 156 */       this.rowBytes_ = ((short)(3 * this.width_));
/* 157 */       break;
/*     */     }
/*     */ 
/* 160 */     this.compress_ = ((this.rowBytes_ >= 8) || (this.pixelSize_ == 32));
/*     */ 
/* 163 */     if (i == 3) {
/* 164 */       this.out_.writeShort(154);
/*     */     }
/* 167 */     else if (this.compress_)
/* 168 */       this.out_.writeShort(152);
/*     */     else {
/* 170 */       this.out_.writeShort(144);
/*     */     }
/*     */ 
/* 174 */     switch (i)
/*     */     {
/*     */     case 1:
/* 179 */       if (this.pixelSize_ == 1)
/*     */       {
/* 182 */         this.out_.writeShort(this.rowBytes_);
/* 183 */         writeBitmap();
/* 184 */         writeSrcDestMode();
/*     */ 
/* 186 */         m = 0;
/* 187 */         writeBitmapImageData(false);
/* 188 */       }break;
/*     */     case 2:
/* 193 */       this.packType_ = 0;
/* 194 */       this.out_.writeShort(this.rowBytes_ | 0x8000);
/* 195 */       writePixmap();
/* 196 */       writeColorTable(localColorModel);
/* 197 */       writeSrcDestMode();
/* 198 */       writeImageData();
/* 199 */       break;
/*     */     case 3:
/* 202 */       this.packType_ = 4;
/*     */ 
/* 205 */       writePixmap9A();
/* 206 */       writeSrcDestMode();
/* 207 */       writeDirectImageData();
/* 208 */       break;
/*     */     }
/*     */ 
/* 212 */     this.out_.writeShort(255);
/*     */   }
/*     */ 
/*     */   void writeImageData()
/*     */     throws JimiException, IOException
/*     */   {
/* 304 */     byte[] arrayOfByte1 = new byte[this.width_];
/* 305 */     byte[] arrayOfByte2 = new byte[this.rowBytes_];
/*     */ 
/* 307 */     int j = 0;
/*     */ 
/* 309 */     for (int i = 0; i < this.height_; i++)
/*     */     {
/* 311 */       this.ji_.getChannel(0, i, arrayOfByte1, 0);
/* 312 */       int k = JimiUtil.packPixels(this.pixelSize_, arrayOfByte1, arrayOfByte2);
/* 313 */       outputBufRow(arrayOfByte2);
/* 314 */       this.encoder_.setProgress(i * 100 / this.height_);
/*     */     }
/* 316 */     outputEvenByteFlush();
/*     */   }
/*     */ 
/*     */   void writePixmap()
/*     */     throws IOException
/*     */   {
/* 273 */     this.out_.writeShort(0);
/* 274 */     this.out_.writeShort(0);
/* 275 */     this.out_.writeShort(this.height_);
/* 276 */     this.out_.writeShort(this.width_);
/*     */ 
/* 278 */     this.out_.writeShort(0);
/* 279 */     this.out_.writeShort(this.packType_);
/* 280 */     this.out_.writeInt(0);
/*     */ 
/* 282 */     this.out_.writeShort(72);
/* 283 */     this.out_.writeShort(0);
/* 284 */     this.out_.writeShort(72);
/* 285 */     this.out_.writeShort(0);
/*     */ 
/* 287 */     this.out_.writeShort(0);
/* 288 */     this.out_.writeShort(this.pixelSize_);
/* 289 */     this.out_.writeShort(1);
/* 290 */     this.out_.writeShort(this.pixelSize_);
/* 291 */     this.out_.writeInt(0);
/* 292 */     this.out_.writeInt(0);
/* 293 */     this.out_.writeInt(0);
/*     */   }
/*     */ 
/*     */   void writePixmap9A()
/*     */     throws IOException
/*     */   {
/* 322 */     this.out_.writeInt(255);
/*     */ 
/* 324 */     this.out_.writeShort(33568);
/*     */ 
/* 326 */     this.out_.writeShort(0);
/* 327 */     this.out_.writeShort(0);
/* 328 */     this.out_.writeShort(this.height_);
/* 329 */     this.out_.writeShort(this.width_);
/*     */ 
/* 331 */     this.out_.writeShort(0);
/*     */ 
/* 333 */     this.out_.writeShort(this.packType_);
/* 334 */     this.out_.writeInt(0);
/*     */ 
/* 338 */     this.out_.writeShort(72);
/* 339 */     this.out_.writeShort(0);
/* 340 */     this.out_.writeShort(72);
/* 341 */     this.out_.writeShort(0);
/*     */ 
/* 343 */     this.out_.writeShort(16);
/* 344 */     this.out_.writeShort(this.pixelSize_);
/* 345 */     this.out_.writeShort(3);
/*     */ 
/* 347 */     this.out_.writeShort(8);
/*     */ 
/* 350 */     this.out_.writeInt(0);
/* 351 */     this.out_.writeInt(0);
/* 352 */     this.out_.writeInt(0);
/*     */   }
/*     */ 
/*     */   void writeSrcDestMode()
/*     */     throws IOException
/*     */   {
/* 226 */     this.out_.writeShort(0);
/* 227 */     this.out_.writeShort(0);
/* 228 */     this.out_.writeShort(this.height_);
/* 229 */     this.out_.writeShort(this.width_);
/*     */ 
/* 231 */     this.out_.writeShort(0);
/* 232 */     this.out_.writeShort(0);
/* 233 */     this.out_.writeShort(this.height_);
/* 234 */     this.out_.writeShort(this.width_);
/*     */ 
/* 236 */     this.out_.writeShort(0);
/*     */   }
/*     */ 
/*     */   void writeV2AdditionalHeader()
/*     */     throws IOException
/*     */   {
/* 504 */     this.out_.writeShort(3072);
/* 505 */     this.out_.writeShort(65535);
/* 506 */     this.out_.writeShort(0);
/* 507 */     this.out_.writeInt(4718592);
/* 508 */     this.out_.writeInt(4718592);
/*     */ 
/* 510 */     this.out_.writeShort(0);
/* 511 */     this.out_.writeShort(0);
/* 512 */     this.out_.writeShort(0);
/* 513 */     this.out_.writeShort(0);
/*     */ 
/* 515 */     this.out_.writeInt(0);
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.encoder.pict.PICTWriter
 * JD-Core Version:    0.6.2
 */