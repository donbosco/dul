/*     */ package com.sun.jimi.core.decoder.pict;
/*     */ 
/*     */ import com.sun.jimi.core.JimiException;
/*     */ import com.sun.jimi.core.compat.AdaptiveRasterImage;
/*     */ import com.sun.jimi.core.compat.JimiDecoderBase;
/*     */ import com.sun.jimi.core.util.JimiUtil;
/*     */ import com.sun.jimi.core.util.Packbits;
/*     */ import com.sun.jimi.core.util.ProgressMonitorSupport;
/*     */ import com.sun.jimi.util.ByteCountInputStream;
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.DirectColorModel;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.FilterInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ 
/*     */ public class PICTDecoder extends JimiDecoderBase
/*     */ {
/*     */   private AdaptiveRasterImage ji;
/*     */   private InputStream in;
/*     */   private ByteCountInputStream bcis;
/*     */   private DataInputStream dIn;
/*     */   private int state;
/*     */   PICTFileHeader pictFH;
/*     */   public static final int PICT_CLIP_RGN = 1;
/*     */   public static final int PICT_BITSRECT = 144;
/*     */   public static final int PICT_BITSRGN = 145;
/*     */   public static final int PICT_PACKBITSRECT = 152;
/*     */   public static final int PICT_PACKBITSRGN = 153;
/*     */   public static final int PICT_9A = 154;
/*     */   public static final int PICT_HEADER = 3072;
/*     */   public static final int PICT_END = 255;
/*     */   public static final int PICT_LONGCOMMENT = 161;
/*     */   int rowBytes;
/*     */   static final int INITIAL = 1;
/*     */   static final int STATE2 = 2;
/*     */   int pictState;
/*     */ 
/*     */   public boolean driveDecoder()
/*     */     throws JimiException
/*     */   {
/*  97 */     switch (this.pictState)
/*     */     {
/*     */     case 1:
/*     */       try
/*     */       {
/* 102 */         this.pictFH = new PICTFileHeader(this.dIn);
/*     */       }
/*     */       catch (IOException localIOException1)
/*     */       {
/* 107 */         throw new JimiException("IO error reading PICT file");
/*     */       }
/* 109 */       this.pictState = 2;
/* 110 */       this.state |= 2;
/* 111 */       return true;
/*     */     case 2:
/*     */       try
/*     */       {
/*     */         int i;
/* 117 */         if (this.pictFH.ver1)
/*     */         {
/* 119 */           i = this.dIn.readUnsignedByte();
/*     */         }
/*     */         else
/*     */         {
/* 124 */           if ((this.bcis.getCount() & 1L) != 0L)
/* 125 */             this.dIn.readByte();
/* 126 */           i = this.dIn.readUnsignedShort();
/*     */         }
/* 128 */         return drivePictDecoder(i);
/*     */       }
/*     */       catch (IOException localIOException2)
/*     */       {
/* 133 */         this.state |= 1;
/* 134 */         throw new JimiException("reading opcode/version");
/*     */       }
/*     */       catch (JimiException localJimiException)
/*     */       {
/* 138 */         this.state |= 1;
/* 139 */         throw localJimiException;
/*     */       }
/*     */     }
/* 142 */     return true;
/*     */   }
/*     */ 
/*     */   boolean drivePictDecoder(int paramInt)
/*     */     throws JimiException, IOException
/*     */   {
/* 183 */     int i = 0;
/* 184 */     switch (paramInt)
/*     */     {
/*     */     case 145:
/* 187 */       handlePackBits(paramInt);
/* 188 */       break;
/*     */     case 153:
/* 191 */       handlePackBits(paramInt);
/* 192 */       break;
/*     */     case 1:
/* 195 */       skip_01();
/* 196 */       break;
/*     */     case 144:
/* 199 */       handlePackBits(paramInt);
/* 200 */       break;
/*     */     case 152:
/* 203 */       handlePackBits(paramInt);
/* 204 */       break;
/*     */     case 154:
/* 207 */       handlePackBits(paramInt);
/* 208 */       break;
/*     */     case 161:
/* 211 */       skip_06();
/* 212 */       break;
/*     */     case 255:
/* 215 */       this.state |= 4;
/* 216 */       return false;
/*     */     }
/*     */ 
/* 223 */     if (i != 0) {
/* 224 */       skipPictOpcode(paramInt);
/*     */     }
/* 226 */     return true;
/*     */   }
/*     */ 
/*     */   public void freeDecoder()
/*     */     throws JimiException
/*     */   {
/* 147 */     this.in = null;
/* 148 */     this.dIn = null;
/* 149 */     this.pictState = 1;
/* 150 */     this.ji = null;
/*     */   }
/*     */ 
/*     */   public AdaptiveRasterImage getJimiImage()
/*     */   {
/* 160 */     return this.ji;
/*     */   }
/*     */ 
/*     */   public int getState()
/*     */   {
/* 155 */     return this.state;
/*     */   }
/*     */ 
/*     */   void handleBitmap(int paramInt)
/*     */     throws IOException, JimiException
/*     */   {
/* 259 */     this.rowBytes &= 16383;
/* 260 */     DirectColorModel localDirectColorModel = new DirectColorModel(1, 1, 1, 1);
/* 261 */     PICTBitmap localPICTBitmap = new PICTBitmap(this.dIn);
/* 262 */     int j = localPICTBitmap.bounding.brX - localPICTBitmap.bounding.tlX;
/* 263 */     int k = localPICTBitmap.bounding.brY - localPICTBitmap.bounding.tlY;
/*     */ 
/* 265 */     this.ji.setSize(j, k);
/* 266 */     this.ji.setColorModel(localDirectColorModel);
/* 267 */     this.ji.setPixels();
/*     */ 
/* 270 */     byte[] arrayOfByte1 = new byte[this.rowBytes + 1 + this.rowBytes / 128];
/* 271 */     byte[] arrayOfByte2 = new byte[this.rowBytes];
/* 272 */     byte[] arrayOfByte3 = new byte[j];
/*     */ 
/* 274 */     for (int i = 0; i < k; i++)
/*     */     {
/*     */       int m;
/* 276 */       if (this.rowBytes < 8)
/*     */       {
/* 278 */         this.dIn.readFully(arrayOfByte1, 0, this.rowBytes);
/*     */ 
/* 282 */         m = arrayOfByte1.length;
/*     */         do { arrayOfByte1[m] = ((byte)(arrayOfByte1[m] ^ 0xFFFFFFFF));
/*     */ 
/* 282 */           m--; } while (m >= 0);
/*     */ 
/* 285 */         JimiUtil.expandPixels(1, arrayOfByte1, arrayOfByte3, arrayOfByte3.length);
/*     */       }
/*     */       else
/*     */       {
/* 290 */         if (this.rowBytes > 250)
/* 291 */           m = this.dIn.readUnsignedShort();
/*     */         else {
/* 293 */           m = this.dIn.readUnsignedByte();
/*     */         }
/* 295 */         this.dIn.readFully(arrayOfByte1, 0, m);
/* 296 */         Packbits.unpackbits(arrayOfByte1, arrayOfByte2);
/*     */ 
/* 300 */         int n = arrayOfByte2.length;
/*     */         do { arrayOfByte2[n] = ((byte)(arrayOfByte2[n] ^ 0xFFFFFFFF));
/*     */ 
/* 300 */           n--; } while (n >= 0);
/*     */ 
/* 303 */         JimiUtil.expandPixels(1, arrayOfByte2, arrayOfByte3, arrayOfByte3.length);
/*     */       }
/* 305 */       this.ji.setChannel(0, i, arrayOfByte3);
/* 306 */       setProgress(i * 100 / (k - 1));
/*     */     }
/*     */   }
/*     */ 
/*     */   void handlePackBits(int paramInt)
/*     */     throws IOException, JimiException
/*     */   {
/* 234 */     if (paramInt == 154) {
/* 235 */       handlePixmap(paramInt);
/*     */     }
/*     */     else {
/* 238 */       this.rowBytes = this.dIn.readUnsignedShort();
/* 239 */       if ((this.pictFH.ver1) || ((this.rowBytes & 0x8000) == 0))
/* 240 */         handleBitmap(paramInt);
/*     */       else
/* 242 */         handlePixmap(paramInt);
/*     */     }
/* 244 */     this.ji.addFullCoverage();
/*     */   }
/*     */ 
/*     */   void handlePixmap(int paramInt)
/*     */     throws IOException, JimiException
/*     */   {
/* 318 */     PICTPixmap localPICTPixmap = null;
/* 319 */     PICTPixmap9A localPICTPixmap9A = null;
/* 320 */     PICTColorTable localPICTColorTable = null;
/* 321 */     Object localObject = null;
/*     */     int i;
/*     */     int j;
/*     */     int k;
/*     */     short s;
/* 326 */     if (paramInt == 154)
/*     */     {
/* 329 */       localPICTPixmap9A = new PICTPixmap9A(this.dIn);
/* 330 */       i = localPICTPixmap9A.bounding.brX - localPICTPixmap9A.bounding.tlX;
/* 331 */       j = localPICTPixmap9A.bounding.brY - localPICTPixmap9A.bounding.tlY;
/* 332 */       k = localPICTPixmap9A.pixelSize;
/* 333 */       s = localPICTPixmap9A.compCount;
/*     */ 
/* 337 */       switch (k)
/*     */       {
/*     */       case 32:
/* 340 */         this.rowBytes = (i * localPICTPixmap9A.compCount);
/*     */ 
/* 343 */         localObject = new DirectColorModel(24, 16711680, 65280, 255);
/* 344 */         break;
/*     */       case 16:
/* 347 */         this.rowBytes = (i * 2);
/*     */ 
/* 349 */         localObject = new DirectColorModel(16, 31744, 992, 31);
/* 350 */         break;
/*     */       default:
/* 353 */         throw new JimiException("Opcode 9a has pixelSize of " + k);
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 358 */       this.rowBytes &= 16383;
/*     */ 
/* 360 */       localPICTPixmap = new PICTPixmap(this.dIn);
/* 361 */       localPICTColorTable = new PICTColorTable(this.dIn);
/* 362 */       localObject = localPICTColorTable.createColorModel(localPICTPixmap);
/* 363 */       i = localPICTPixmap.bounding.brX - localPICTPixmap.bounding.tlX;
/* 364 */       j = localPICTPixmap.bounding.brY - localPICTPixmap.bounding.tlY;
/*     */ 
/* 366 */       k = localPICTPixmap.pixelSize;
/* 367 */       s = localPICTPixmap.compCount;
/*     */     }
/*     */ 
/* 370 */     PICTRectangle localPICTRectangle1 = new PICTRectangle(this.dIn);
/* 371 */     PICTRectangle localPICTRectangle2 = new PICTRectangle(this.dIn);
/* 372 */     int m = this.dIn.readShort();
/*     */ 
/* 374 */     if ((paramInt == 145) || (paramInt == 153)) {
/* 375 */       skip_01();
/*     */     }
/*     */ 
/* 378 */     this.ji.setSize(i, j);
/* 379 */     this.ji.setColorModel((ColorModel)localObject);
/* 380 */     this.ji.setPixels();
/*     */ 
/* 382 */     handlePixmap(this.ji, this.rowBytes, k, s);
/*     */   }
/*     */ 
/*     */   private void handlePixmap(AdaptiveRasterImage paramAdaptiveRasterImage, int paramInt1, int paramInt2, short paramShort)
/*     */     throws IOException, JimiException
/*     */   {
/* 400 */     int n = (paramInt1 < 8) && (paramInt2 != 32) ? 0 : 1;
/* 401 */     int k = paramInt1;
/* 402 */     byte[] arrayOfByte2 = null;
/* 403 */     byte[] arrayOfByte3 = null;
/* 404 */     int[] arrayOfInt = null;
/* 405 */     int i1 = paramAdaptiveRasterImage.getWidth();
/* 406 */     int i2 = paramAdaptiveRasterImage.getHeight();
/* 407 */     int m = i1;
/*     */ 
/* 410 */     switch (paramInt2)
/*     */     {
/*     */     case 32:
/* 413 */       if (n == 0)
/* 414 */         arrayOfInt = new int[i1];
/*     */       else
/* 416 */         arrayOfByte2 = new byte[k];
/* 417 */       break;
/*     */     case 16:
/* 420 */       arrayOfInt = new int[i1];
/* 421 */       break;
/*     */     case 8:
/* 424 */       arrayOfByte2 = new byte[k];
/* 425 */       break;
/*     */     default:
/* 428 */       arrayOfByte3 = new byte[m];
/* 429 */       arrayOfByte2 = new byte[k];
/*     */     }
/*     */     byte[] arrayOfByte1;
/*     */     int i;
/*     */     int i3;
/* 433 */     if (n == 0)
/*     */     {
/* 435 */       arrayOfByte1 = new byte[k];
/* 436 */       for (i = 0; i < i2; i++)
/*     */       {
/* 438 */         this.dIn.readFully(arrayOfByte1, 0, paramInt1);
/* 439 */         switch (paramInt2)
/*     */         {
/*     */         case 16:
/* 442 */           for (i3 = 0; i3 < i1; i3++)
/* 443 */             arrayOfInt[i3] = (((arrayOfByte1[(i3 * 2)] & 0xFF) << 8) + (arrayOfByte1[(i3 * 2 + 1)] & 0xFF));
/* 444 */           paramAdaptiveRasterImage.setChannel(i, arrayOfInt);
/* 445 */           break;
/*     */         case 8:
/* 448 */           paramAdaptiveRasterImage.setChannel(0, i, arrayOfByte1);
/* 449 */           break;
/*     */         default:
/* 452 */           JimiUtil.expandPixels(paramInt2, arrayOfByte1, arrayOfByte3, arrayOfByte3.length);
/* 453 */           paramAdaptiveRasterImage.setChannel(0, i, arrayOfByte3);
/*     */         }
/*     */ 
/* 456 */         setProgress(i * 100 / i2);
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 462 */       arrayOfByte1 = new byte[k + 1 + k / 128];
/*     */ 
/* 464 */       for (i = 0; i < i2; i++)
/*     */       {
/*     */         int j;
/* 468 */         if (paramInt1 > 250)
/* 469 */           j = this.dIn.readUnsignedShort();
/*     */         else {
/* 471 */           j = this.dIn.readUnsignedByte();
/*     */         }
/* 473 */         if (j > k + 1 + k / 128) {
/* 474 */           throw new JimiException("**** rawLen " + j + " bufSize " + k);
/*     */         }
/* 476 */         this.dIn.readFully(arrayOfByte1, 0, j);
/*     */ 
/* 478 */         if (paramInt2 == 16)
/*     */         {
/* 480 */           Packbits.unpackbits(arrayOfByte1, arrayOfInt);
/* 481 */           paramAdaptiveRasterImage.setChannel(i, arrayOfInt);
/*     */         }
/*     */         else
/*     */         {
/*     */           try
/*     */           {
/* 487 */             Packbits.unpackbitsLimit(arrayOfByte1, j, arrayOfByte2);
/*     */           }
/*     */           catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException)
/*     */           {
/* 491 */             throw new JimiException("Error unpacking data");
/*     */           }
/*     */ 
/* 494 */           if (paramInt2 < 8)
/*     */           {
/* 496 */             JimiUtil.expandPixels(paramInt2, arrayOfByte2, arrayOfByte3, arrayOfByte3.length);
/* 497 */             paramAdaptiveRasterImage.setChannel(0, i, arrayOfByte3);
/*     */           }
/* 499 */           else if (paramInt2 == 8)
/*     */           {
/* 501 */             paramAdaptiveRasterImage.setChannel(0, i, arrayOfByte2);
/*     */           }
/* 503 */           else if ((paramInt2 == 24) || (paramInt2 == 32))
/*     */           {
/* 505 */             i3 = 0;
/*     */ 
/* 507 */             if (paramShort == 4)
/*     */             {
/* 509 */               paramAdaptiveRasterImage.setChannel(24, i, arrayOfByte2, i3, i1);
/* 510 */               i3 += i1;
/*     */             }
/*     */ 
/* 514 */             paramAdaptiveRasterImage.setChannel(16, i, arrayOfByte2, i3, i1);
/* 515 */             i3 += i1;
/* 516 */             paramAdaptiveRasterImage.setChannel(8, i, arrayOfByte2, i3, i1);
/* 517 */             i3 += i1;
/*     */ 
/* 519 */             paramAdaptiveRasterImage.setChannel(0, i, arrayOfByte2, i3, i1);
/*     */           }
/*     */         }
/*     */       }
/* 523 */       setProgress(i * 100 / i2);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void initDecoder(InputStream paramInputStream, AdaptiveRasterImage paramAdaptiveRasterImage)
/*     */     throws JimiException
/*     */   {
/*  75 */     this.in = paramInputStream;
/*     */ 
/*  78 */     this.bcis = new ByteCountInputStream(new BufferedInputStream(paramInputStream));
/*     */ 
/*  80 */     this.dIn = new DataInputStream(this.bcis);
/*  81 */     this.ji = paramAdaptiveRasterImage;
/*  82 */     this.state = 0;
/*  83 */     this.pictState = 1;
/*     */   }
/*     */ 
/*     */   void skipPictOpcode(int paramInt)
/*     */   {
/*     */   }
/*     */ 
/*     */   void skip_01()
/*     */     throws IOException
/*     */   {
/* 589 */     int i = this.dIn.readShort();
/* 590 */     this.dIn.skip(i - 2);
/*     */   }
/*     */ 
/*     */   void skip_02() throws IOException
/*     */   {
/* 595 */     this.dIn.skip(4L);
/* 596 */     skip_03();
/*     */   }
/*     */ 
/*     */   void skip_03() throws IOException
/*     */   {
/* 601 */     int i = this.dIn.readUnsignedByte();
/* 602 */     this.dIn.skip(i);
/*     */   }
/*     */ 
/*     */   void skip_04() throws IOException
/*     */   {
/* 607 */     this.dIn.skip(1L);
/* 608 */     skip_03();
/*     */   }
/*     */ 
/*     */   void skip_05()
/*     */     throws IOException
/*     */   {
/*     */   }
/*     */ 
/*     */   void skip_06()
/*     */     throws IOException
/*     */   {
/* 619 */     this.dIn.skip(2L);
/* 620 */     int i = this.dIn.readUnsignedShort();
/* 621 */     this.dIn.skip(i);
/*     */   }
/*     */ 
/*     */   public boolean usesChanneledData()
/*     */   {
/* 165 */     return true;
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.decoder.pict.PICTDecoder
 * JD-Core Version:    0.6.2
 */