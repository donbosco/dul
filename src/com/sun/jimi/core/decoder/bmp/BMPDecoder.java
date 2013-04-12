/*     */ package com.sun.jimi.core.decoder.bmp;
/*     */ 
/*     */ import com.sun.jimi.core.JimiException;
/*     */ import com.sun.jimi.core.compat.AdaptiveRasterImage;
/*     */ import com.sun.jimi.core.compat.JimiDecoderBase;
/*     */ import com.sun.jimi.core.util.JimiUtil;
/*     */ import com.sun.jimi.core.util.LEDataInputStream;
/*     */ import com.sun.jimi.core.util.ProgressMonitorSupport;
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.DirectColorModel;
/*     */ import java.awt.image.IndexColorModel;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ 
/*     */ public class BMPDecoder extends JimiDecoderBase
/*     */ {
/*     */   private AdaptiveRasterImage ji_;
/*     */   private InputStream in_;
/*     */   private LEDataInputStream leInput;
/*     */   private int state;
/*     */   private BMPFileHeader bmpHeader;
/*     */   private BMPColorMap bmpColorMap;
/*     */   private ColorModel model;
/*     */   byte[] rawScanLine;
/*     */   int[] intScanLine;
/*     */   byte[] byteScanLine;
/*     */ 
/*     */   public boolean driveDecoder()
/*     */     throws JimiException
/*     */   {
/*     */     try
/*     */     {
/*  65 */       this.bmpHeader = new BMPFileHeader(this.leInput);
/*     */ 
/*  67 */       this.bmpColorMap = new BMPColorMap(this.leInput, this.bmpHeader);
/*     */ 
/*  69 */       initJimiImage();
/*  70 */       this.state |= 2;
/*     */ 
/*  73 */       loadImage(this.leInput);
/*     */ 
/*  75 */       this.ji_.addFullCoverage();
/*  76 */       this.state |= 4;
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/*  80 */       this.state |= 1;
/*  81 */       throw new JimiException("IO error reading BMP file");
/*     */     }
/*     */     catch (JimiException localJimiException)
/*     */     {
/*  85 */       this.state |= 1;
/*  86 */       throw localJimiException;
/*     */     }
/*     */ 
/*  89 */     return false;
/*     */   }
/*     */ 
/*     */   public void freeDecoder() throws JimiException
/*     */   {
/*  94 */     this.in_ = null;
/*  95 */     this.ji_ = null;
/*     */   }
/*     */ 
/*     */   public AdaptiveRasterImage getJimiImage()
/*     */   {
/* 106 */     return this.ji_;
/*     */   }
/*     */ 
/*     */   public int getState()
/*     */   {
/* 101 */     return this.state;
/*     */   }
/*     */ 
/*     */   public void initDecoder(InputStream paramInputStream, AdaptiveRasterImage paramAdaptiveRasterImage)
/*     */     throws JimiException
/*     */   {
/*  51 */     this.bmpHeader = null;
/*  52 */     this.bmpColorMap = null;
/*     */ 
/*  54 */     this.in_ = paramInputStream;
/*  55 */     this.leInput = new LEDataInputStream(new BufferedInputStream(this.in_));
/*  56 */     this.ji_ = paramAdaptiveRasterImage;
/*  57 */     this.state = 0;
/*     */   }
/*     */ 
/*     */   private void initJimiImage()
/*     */     throws JimiException
/*     */   {
/* 112 */     this.ji_.setSize(this.bmpHeader.width, this.bmpHeader.height);
/*     */ 
/* 115 */     if (this.bmpHeader.bitsPerPixel == 32)
/*     */     {
/* 117 */       this.model = ColorModel.getRGBdefault();
/*     */     }
/* 119 */     else if (this.bmpHeader.bitsPerPixel == 24)
/*     */     {
/* 121 */       this.model = new DirectColorModel(24, 16711680, 65280, 255);
/*     */     }
/* 123 */     else if (this.bmpHeader.bitsPerPixel == 16)
/*     */     {
/* 125 */       this.model = new DirectColorModel(16, this.bmpHeader.redMask, this.bmpHeader.greenMask, 
/* 126 */         this.bmpHeader.blueMask, this.bmpHeader.alphaMask);
/*     */     }
/*     */     else
/*     */     {
/* 130 */       if (this.bmpColorMap.noOfEntries <= 0) {
/* 131 */         throw new JimiException("8 bit or less bitsperpixel requies pallete");
/*     */       }
/*     */ 
/* 134 */       this.model = new IndexColorModel(8, 
/* 135 */         this.bmpColorMap.noOfEntries, 
/* 136 */         this.bmpColorMap.r, this.bmpColorMap.g, this.bmpColorMap.b);
/*     */     }
/*     */ 
/* 139 */     this.ji_.setColorModel(this.model);
/* 140 */     this.ji_.setPixels();
/*     */ 
/* 142 */     if (this.bmpHeader.bitsPerPixel == 32)
/*     */     {
/* 144 */       this.intScanLine = new int[this.bmpHeader.scanLineSize / 4];
/*     */     }
/* 146 */     if (this.bmpHeader.bitsPerPixel == 24)
/*     */     {
/* 149 */       this.intScanLine = new int[this.bmpHeader.scanLineSize / 3];
/*     */     }
/* 151 */     if (this.bmpHeader.bitsPerPixel == 16)
/*     */     {
/* 153 */       this.intScanLine = new int[this.bmpHeader.scanLineSize / 2];
/*     */     }
/*     */     else
/*     */     {
/* 157 */       this.byteScanLine = new byte[this.bmpHeader.width];
/*     */     }
/* 159 */     this.rawScanLine = new byte[this.bmpHeader.scanLineSize];
/*     */   }
/*     */ 
/*     */   private void loadImage(LEDataInputStream paramLEDataInputStream) throws JimiException, IOException
/*     */   {
/* 164 */     switch (this.bmpHeader.compression)
/*     */     {
/*     */     case 1:
/* 170 */       this.ji_.setChannel(0L);
/* 171 */       unpackRLE8(paramLEDataInputStream);
/*     */ 
/* 174 */       return;
/*     */     case 2:
/* 177 */       this.ji_.setChannel(0L);
/* 178 */       unpackRLE4(paramLEDataInputStream);
/*     */ 
/* 181 */       return;
/*     */     default:
/* 187 */       throw new JimiException("Unsupported compression " + this.bmpHeader.compression);
/*     */     case 0:
/*     */     case 3:
/*     */     }
/* 191 */     for (int i = this.bmpHeader.height - 1; i >= 0; i--)
/*     */     {
/* 193 */       paramLEDataInputStream.readFully(this.rawScanLine, 0, this.bmpHeader.scanLineSize);
/*     */ 
/* 195 */       if (this.bmpHeader.bitsPerPixel == 32)
/*     */       {
/* 197 */         pack32ToInt(this.rawScanLine, 0, this.intScanLine, 0, this.bmpHeader.width);
/* 198 */         this.ji_.setChannel(i, this.intScanLine);
/*     */       }
/* 200 */       else if (this.bmpHeader.bitsPerPixel == 24)
/*     */       {
/* 203 */         pack24ToInt(this.rawScanLine, 0, this.intScanLine, 0, this.bmpHeader.width);
/* 204 */         this.ji_.setChannel(i, this.intScanLine);
/*     */       }
/* 206 */       else if (this.bmpHeader.bitsPerPixel == 16)
/*     */       {
/* 208 */         pack16ToInt(this.rawScanLine, 0, this.intScanLine, 0, this.bmpHeader.width);
/* 209 */         this.ji_.setChannel(i, this.intScanLine);
/*     */       }
/* 213 */       else if (this.bmpHeader.bitsPerPixel < 8)
/*     */       {
/* 215 */         JimiUtil.expandPixels(this.bmpHeader.bitsPerPixel, this.rawScanLine, this.byteScanLine, this.bmpHeader.width);
/* 216 */         this.ji_.setChannel(0, i, this.byteScanLine);
/*     */       }
/*     */       else
/*     */       {
/* 220 */         this.ji_.setChannel(0, i, this.rawScanLine, 0, this.bmpHeader.width);
/*     */       }
/*     */ 
/* 223 */       setProgress((this.bmpHeader.height - i - 1) * 100 / (this.bmpHeader.height - 1));
/*     */     }
/*     */   }
/*     */ 
/*     */   private void pack16ToInt(byte[] paramArrayOfByte, int paramInt1, int[] paramArrayOfInt, int paramInt2, int paramInt3)
/*     */   {
/* 230 */     int i = paramInt2;
/* 231 */     int j = paramInt1;
/* 232 */     int k = 255;
/*     */ 
/* 234 */     for (int m = 0; m < paramInt3; m++)
/*     */     {
/* 236 */       int n = paramArrayOfByte[(j++)] & k;
/* 237 */       int i1 = (paramArrayOfByte[(j++)] & k) << 8;
/* 238 */       paramArrayOfInt[i] = (n | i1);
/* 239 */       i++;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void pack24ToInt(byte[] paramArrayOfByte, int paramInt1, int[] paramArrayOfInt, int paramInt2, int paramInt3)
/*     */   {
/* 246 */     int i = paramInt2;
/* 247 */     int j = paramInt1;
/* 248 */     int k = 255;
/* 249 */     for (int m = 0; m < paramInt3; m++)
/*     */     {
/* 251 */       int n = paramArrayOfByte[(j++)] & k;
/* 252 */       int i1 = (paramArrayOfByte[(j++)] & k) << 8;
/* 253 */       int i2 = (paramArrayOfByte[(j++)] & k) << 16;
/* 254 */       paramArrayOfInt[i] = (0xFF000000 | n | i1 | i2);
/* 255 */       i++;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void pack32ToInt(byte[] paramArrayOfByte, int paramInt1, int[] paramArrayOfInt, int paramInt2, int paramInt3)
/*     */   {
/* 262 */     int i = paramInt2;
/* 263 */     int j = paramInt1;
/* 264 */     int k = 255;
/* 265 */     for (int m = 0; m < paramInt3; m++)
/*     */     {
/* 267 */       int n = paramArrayOfByte[(j++)] & k;
/* 268 */       int i1 = (paramArrayOfByte[(j++)] & k) << 8;
/* 269 */       int i2 = (paramArrayOfByte[(j++)] & k) << 16;
/* 270 */       int i3 = (paramArrayOfByte[(j++)] & k) << 24;
/* 271 */       paramArrayOfInt[i] = (0xFF000000 | n | i1 | i2);
/* 272 */       i++;
/*     */     }
/*     */   }
/*     */ 
/*     */   void unpackRLE4(InputStream paramInputStream)
/*     */     throws JimiException, IOException
/*     */   {
/* 418 */     int n = this.bmpHeader.height - 1;
/* 419 */     int i1 = 0;
/* 420 */     byte[] arrayOfByte1 = new byte[this.bmpHeader.width];
/* 421 */     int i2 = 0;
/*     */     while (true)
/*     */     {
/* 425 */       int i = paramInputStream.read();
/* 426 */       if (i < 0)
/* 427 */         throw new EOFException();
/*     */       int i4;
/* 428 */       if (i == 0)
/*     */       {
/* 431 */         int k = paramInputStream.read();
/* 432 */         if (k < 0)
/* 433 */           throw new EOFException();
/* 434 */         switch (k)
/*     */         {
/*     */         case 0:
/* 438 */           if (i2 > i1)
/* 439 */             this.ji_.setChannel(0, i1, n, i2 - i1, 1, 
/* 440 */               arrayOfByte1, i1, arrayOfByte1.length);
/* 441 */           i2 = 0;
/* 442 */           i1 = 0;
/* 443 */           n--;
/* 444 */           break;
/*     */         case 1:
/* 447 */           return;
/*     */         case 2:
/* 451 */           if (i2 > i1) {
/* 452 */             this.ji_.setChannel(0, i1, n, i2 - i1, 1, 
/* 453 */               arrayOfByte1, i1, arrayOfByte1.length);
/*     */           }
/* 455 */           int i3 = paramInputStream.read();
/* 456 */           i4 = paramInputStream.read();
/*     */ 
/* 458 */           i1 += i3;
/* 459 */           i2 = i1;
/* 460 */           n += i4;
/* 461 */           break;
/*     */         default:
/* 464 */           int m = 0;
/* 465 */           if (((k & 0x3) == 1) || ((k & 0x3) == 2))
/* 466 */             m = 1;
/* 467 */           int i5 = 0;
/* 468 */           byte[] arrayOfByte3 = new byte[2];
/*     */           do
/*     */           {
/* 471 */             if (i5 == 0)
/*     */             {
/* 473 */               int i6 = paramInputStream.read();
/* 474 */               if (i6 < 0)
/* 475 */                 throw new EOFException();
/* 476 */               arrayOfByte3[0] = ((byte)((i6 & 0xF0) >> 4));
/* 477 */               arrayOfByte3[1] = ((byte)(i6 & 0xF));
/*     */             }
/*     */ 
/* 480 */             if (i2 == arrayOfByte1.length)
/*     */             {
/* 482 */               if (i2 > i1)
/* 483 */                 this.ji_.setChannel(0, i1, n, i2 - i1, 1, 
/* 484 */                   arrayOfByte1, i1, arrayOfByte1.length);
/* 485 */               i2 = 0;
/* 486 */               i1 = 0;
/* 487 */               n--;
/*     */             }
/* 489 */             arrayOfByte1[(i2++)] = arrayOfByte3[i5];
/* 490 */             i5 ^= 1;
/*     */ 
/* 469 */             k--; } while (k >= 0);
/*     */ 
/* 492 */           if (m == 0) continue;
/* 493 */           paramInputStream.read();
/* 494 */           break;
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 499 */         int j = paramInputStream.read();
/* 500 */         if (j < 0)
/* 501 */           throw new EOFException();
/* 502 */         byte[] arrayOfByte2 = new byte[2];
/* 503 */         arrayOfByte2[0] = ((byte)((j & 0xF0) >> 4));
/* 504 */         arrayOfByte2[1] = ((byte)(j & 0xF));
/* 505 */         i4 = 0;
/*     */         do
/*     */         {
/* 508 */           if (i2 == arrayOfByte1.length)
/*     */           {
/* 511 */             if (i2 > i1)
/*     */             {
/* 513 */               this.ji_.setChannel(0, i1, n, i2 - i1, 1, 
/* 514 */                 arrayOfByte1, i1, arrayOfByte1.length);
/*     */             }
/* 516 */             i2 = 0;
/* 517 */             i1 = 0;
/* 518 */             n--;
/*     */           }
/* 520 */           arrayOfByte1[(i2++)] = arrayOfByte2[i4];
/* 521 */           i4 ^= 1;
/*     */ 
/* 506 */           i--; } while (i >= 0);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   void unpackRLE8(InputStream paramInputStream)
/*     */     throws JimiException, IOException
/*     */   {
/* 298 */     int n = this.bmpHeader.height - 1;
/* 299 */     int i1 = 0;
/* 300 */     byte[] arrayOfByte = new byte[this.bmpHeader.width];
/* 301 */     int i2 = 0;
/*     */     while (true)
/*     */     {
/* 305 */       int i = paramInputStream.read();
/* 306 */       if (i < 0)
/* 307 */         throw new EOFException();
/* 308 */       if (i == 0)
/*     */       {
/* 311 */         int k = paramInputStream.read();
/* 312 */         if (k < 0)
/* 313 */           throw new EOFException();
/* 314 */         switch (k)
/*     */         {
/*     */         case 0:
/* 318 */           if (i2 > i1)
/*     */           {
/* 320 */             this.ji_.setChannel(0, i1, n, i2 - i1, 1, 
/* 321 */               arrayOfByte, i1, arrayOfByte.length);
/*     */           }
/* 323 */           i2 = 0;
/* 324 */           i1 = 0;
/* 325 */           n--;
/* 326 */           setProgress((this.bmpHeader.height - 1 - n) * 100 / (this.bmpHeader.height - 1));
/* 327 */           break;
/*     */         case 1:
/* 330 */           return;
/*     */         case 2:
/* 334 */           this.ji_.setChannel(0, i1, n, i2 - i1, 1, 
/* 335 */             arrayOfByte, i1, arrayOfByte.length);
/*     */ 
/* 337 */           int i3 = paramInputStream.read();
/* 338 */           int i4 = paramInputStream.read();
/*     */ 
/* 341 */           i1 += i3;
/* 342 */           i2 = i1;
/* 343 */           n += i4;
/* 344 */           break;
/*     */         default:
/* 347 */           int m = 0;
/* 348 */           if ((k & 0x1) != 0)
/* 349 */             m = 1;
/*     */           do
/*     */           {
/* 352 */             int i5 = paramInputStream.read();
/* 353 */             if (i5 < 0)
/* 354 */               throw new EOFException();
/* 355 */             if (i2 == arrayOfByte.length)
/*     */             {
/* 357 */               this.ji_.setChannel(0, i1, n, i2 - i1, 1, 
/* 358 */                 arrayOfByte, i1, arrayOfByte.length);
/* 359 */               i2 = 0;
/* 360 */               i1 = 0;
/* 361 */               n--;
/* 362 */               setProgress((this.bmpHeader.height - 1 - n) * 100 / (this.bmpHeader.height - 1));
/*     */             }
/* 364 */             arrayOfByte[(i2++)] = ((byte)i5);
/*     */ 
/* 350 */             k--; } while (k >= 0);
/*     */ 
/* 366 */           if (m == 0) continue;
/* 367 */           paramInputStream.read();
/* 368 */           break;
/*     */         }
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 374 */         int j = paramInputStream.read();
/* 375 */         if (j < 0)
/* 376 */           throw new EOFException();
/*     */         do
/*     */         {
/* 379 */           if (i2 == arrayOfByte.length)
/*     */           {
/* 382 */             this.ji_.setChannel(0, i1, n, i2 - i1, 1, 
/* 383 */               arrayOfByte, i1, arrayOfByte.length);
/* 384 */             i2 = 0;
/* 385 */             i1 = 0;
/* 386 */             n--;
/*     */           }
/* 388 */           arrayOfByte[(i2++)] = ((byte)j);
/*     */ 
/* 377 */           i--; } while (i >= 0);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean usesChanneledData()
/*     */   {
/* 529 */     return true;
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.decoder.bmp.BMPDecoder
 * JD-Core Version:    0.6.2
 */