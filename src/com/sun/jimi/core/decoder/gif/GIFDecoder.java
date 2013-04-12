/*     */ package com.sun.jimi.core.decoder.gif;
/*     */ 
/*     */ import com.sun.jimi.core.JimiException;
/*     */ import com.sun.jimi.core.compat.AdaptiveRasterImage;
/*     */ import com.sun.jimi.core.compat.JimiDecoderBase;
/*     */ import com.sun.jimi.core.options.GIFOptions;
/*     */ import com.sun.jimi.core.util.LEDataInputStream;
/*     */ import com.sun.jimi.core.util.ProgressMonitorSupport;
/*     */ import com.sun.jimi.core.util.lzw.LZWDecompressor;
/*     */ import java.awt.image.IndexColorModel;
/*     */ import java.io.FilterInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ 
/*     */ public class GIFDecoder extends JimiDecoderBase
/*     */ {
/*     */   static final byte IMAGE_SEPERATOR = 44;
/*     */   static final byte EXTENSION_INTRODUCER = 33;
/*     */   static final byte GRAPHIC_CONTROL_EXTENSION = -7;
/*     */   static final byte COMMENT_EXTENSION = -2;
/*     */   static final byte APPLICATION_EXTENSION = -1;
/*     */   static final byte PLAINTEXT_EXTENSION = 1;
/*     */   static final byte TRAILER = 59;
/*     */   private AdaptiveRasterImage ji_;
/*     */   private InputStream in_;
/*     */   private LEDataInputStream dIn_;
/*     */   private volatile int state_;
/*     */   GIFFileHeader gifFH_;
/*     */   GIFGraphicExt gifGraphicExt_;
/*  75 */   protected int numberOfLoops_ = 1;
/*     */   boolean gotPrevImageSeperator_;
/*     */   String comment_;
/*     */   AdaptiveRasterImage baseJI_;
/*     */   GIFImageDescriptor imageDescriptor_;
/* 283 */   int disposal_ = 0;
/* 284 */   int xOffset_ = 0;
/* 285 */   int yOffset_ = 0;
/* 286 */   int dWidth_ = 0;
/* 287 */   int dHeight_ = 0;
/*     */   byte[] prevSaved_;
/* 292 */   int imageDelay_ = 0;
/*     */ 
/*     */   void addImageDelta(GIFImageDescriptor paramGIFImageDescriptor, AdaptiveRasterImage paramAdaptiveRasterImage1, AdaptiveRasterImage paramAdaptiveRasterImage2)
/*     */     throws JimiException
/*     */   {
/* 400 */     setOptions(paramAdaptiveRasterImage2);
/*     */ 
/* 402 */     int j = this.baseJI_ != null ? 0 : 1;
/*     */ 
/* 404 */     if (this.baseJI_ == null)
/*     */     {
/* 408 */       this.baseJI_ = createAdaptiveRasterImage(this.gifFH_.screenWidth, this.gifFH_.screenHeight, paramAdaptiveRasterImage1.getColorModel());
/*     */ 
/* 410 */       setOptions(this.baseJI_);
/* 411 */       this.baseJI_.setPixels();
/*     */ 
/* 414 */       if ((this.gifFH_.packed & 0x80) != 0) {
/* 415 */         this.baseJI_.setChannel(this.gifFH_.backgroundColor);
/*     */       }
/*     */ 
/* 418 */       this.baseJI_.addFullCoverage();
/*     */     }
/*     */ 
/* 421 */     disposePreviousDelta();
/*     */ 
/* 424 */     if (this.gifGraphicExt_ != null)
/*     */     {
/* 427 */       this.disposal_ = ((this.gifGraphicExt_.packed & 0x1C) >> 2);
/* 428 */       this.imageDelay_ = this.gifGraphicExt_.delayTime;
/*     */     }
/*     */ 
/* 432 */     this.xOffset_ = paramGIFImageDescriptor.left;
/* 433 */     this.yOffset_ = paramGIFImageDescriptor.top;
/* 434 */     this.dWidth_ = paramGIFImageDescriptor.width;
/* 435 */     this.dHeight_ = paramGIFImageDescriptor.height;
/* 436 */     saveForNextDelta();
/*     */ 
/* 439 */     byte[] arrayOfByte = new byte[this.dWidth_];
/* 440 */     int k = this.yOffset_;
/* 441 */     for (int m = 0; m < this.dHeight_; m++)
/*     */     {
/* 443 */       paramAdaptiveRasterImage1.getChannel(0, m, arrayOfByte, 0);
/*     */ 
/* 445 */       if ((j != 0) || (this.gifGraphicExt_ == null) || ((this.gifGraphicExt_.packed & 0x1) == 0)) {
/* 446 */         this.baseJI_.setChannel(0, this.xOffset_, k, this.dWidth_, 1, arrayOfByte, 0, 0);
/*     */       }
/*     */       else {
/* 449 */         int n = this.gifGraphicExt_.colorIndex;
/* 450 */         int i1 = 0;
/* 451 */         int i2 = 0;
/*     */ 
/* 453 */         for (int i3 = 0; i3 < arrayOfByte.length; i3++) {
/* 454 */           if ((arrayOfByte[i3] == n) || (i3 == arrayOfByte.length - 1)) {
/* 455 */             if (i2 != 0) {
/* 456 */               this.baseJI_.setChannel(0, this.xOffset_ + i1, k, i3 - i1, 
/* 457 */                 1, arrayOfByte, i1, 0);
/* 458 */               i2 = 0;
/*     */             }
/*     */ 
/*     */           }
/* 462 */           else if (i2 == 0) {
/* 463 */             i1 = i3;
/* 464 */             i2 = 1;
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 470 */       k++;
/*     */     }
/*     */ 
/* 474 */     this.baseJI_.setColorModel(paramAdaptiveRasterImage1.getColorModel());
/*     */ 
/* 477 */     if (this.comment_ != null)
/*     */     {
/* 479 */       this.comment_ = null;
/*     */     }
/*     */ 
/* 483 */     arrayOfByte = new byte[this.baseJI_.getWidth()];
/* 484 */     paramAdaptiveRasterImage2.setSize(this.baseJI_.getWidth(), this.baseJI_.getHeight());
/* 485 */     paramAdaptiveRasterImage2.setColorModel(this.baseJI_.getColorModel());
/* 486 */     setOptions(paramAdaptiveRasterImage2);
/* 487 */     paramAdaptiveRasterImage2.setPixels();
/* 488 */     for (int i = 0; i < this.baseJI_.getHeight(); i++)
/*     */     {
/* 490 */       this.baseJI_.getChannel(0, i, arrayOfByte, 0);
/* 491 */       paramAdaptiveRasterImage2.setChannel(0, i, arrayOfByte);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void decodeImage(LEDataInputStream paramLEDataInputStream, GIFImageDescriptor paramGIFImageDescriptor, AdaptiveRasterImage paramAdaptiveRasterImage)
/*     */     throws JimiException, IOException
/*     */   {
/* 546 */     int j = paramGIFImageDescriptor.height;
/* 547 */     int k = paramLEDataInputStream.readByte();
/* 548 */     LZWDecompressor localLZWDecompressor = new LZWDecompressor(paramLEDataInputStream, k, false);
/* 549 */     byte[] arrayOfByte = new byte[paramGIFImageDescriptor.width];
/*     */ 
/* 551 */     int m = 0;
/*     */     int i;
/* 553 */     if (paramGIFImageDescriptor.interlace)
/*     */     {
/* 556 */       paramAdaptiveRasterImage.setHints(22);
/* 557 */       for (i = 0; i < j; i += 8)
/*     */       {
/* 559 */         localLZWDecompressor.decompress(arrayOfByte);
/* 560 */         paramAdaptiveRasterImage.setChannel(0, i, arrayOfByte);
/* 561 */         setProgress(m++ * 100 / paramGIFImageDescriptor.height);
/*     */       }
/*     */ 
/* 564 */       for (i = 4; i < j; i += 8)
/*     */       {
/* 566 */         localLZWDecompressor.decompress(arrayOfByte);
/* 567 */         paramAdaptiveRasterImage.setChannel(0, i, arrayOfByte);
/* 568 */         setProgress(m++ * 100 / paramGIFImageDescriptor.height);
/*     */       }
/*     */ 
/* 571 */       for (i = 2; i < j; i += 4)
/*     */       {
/* 573 */         localLZWDecompressor.decompress(arrayOfByte);
/* 574 */         paramAdaptiveRasterImage.setChannel(0, i, arrayOfByte);
/* 575 */         setProgress(m++ * 100 / paramGIFImageDescriptor.height);
/*     */       }
/*     */ 
/* 578 */       for (i = 1; i < j; i += 2)
/*     */       {
/* 580 */         localLZWDecompressor.decompress(arrayOfByte);
/* 581 */         paramAdaptiveRasterImage.setChannel(0, i, arrayOfByte);
/* 582 */         setProgress(m++ * 100 / paramGIFImageDescriptor.height);
/*     */       }
/* 584 */       setProgress(100);
/*     */     }
/*     */     else
/*     */     {
/* 588 */       for (i = 0; i < j; i++)
/*     */       {
/* 590 */         localLZWDecompressor.decompress(arrayOfByte);
/* 591 */         paramAdaptiveRasterImage.setChannel(0, i, arrayOfByte);
/* 592 */         setProgress(i * 100 / j);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 597 */     localLZWDecompressor.gifFinishBlocks();
/*     */   }
/*     */ 
/*     */   void disposePreviousDelta()
/*     */     throws JimiException
/*     */   {
/* 301 */     switch (this.disposal_)
/*     */     {
/*     */     case 2:
/* 310 */       byte[] arrayOfByte = new byte[this.dWidth_];
/*     */       int j;
/* 317 */       if ((this.gifGraphicExt_ != null) && 
/* 318 */         ((this.gifGraphicExt_.packed & 0x1) != 0)) {
/* 319 */         for (j = 0; j < arrayOfByte.length; j++) {
/* 320 */           arrayOfByte[j] = ((byte)this.gifGraphicExt_.colorIndex);
/*     */         }
/*     */       }
/* 323 */       if ((this.gifFH_.packed & 0x80) != 0) {
/* 324 */         if ((this.gifGraphicExt_ != null) && 
/* 325 */           ((this.gifGraphicExt_.packed & 0x1) != 0)) {
/* 326 */           for (j = 0; j < arrayOfByte.length; j++)
/* 327 */             arrayOfByte[j] = ((byte)this.gifGraphicExt_.colorIndex);
/*     */         }
/*     */         else
/*     */         {
/* 331 */           j = this.dWidth_;
/*     */           do { arrayOfByte[j] = this.gifFH_.backgroundColor;
/*     */ 
/* 331 */             j--; } while (j >= 0);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 336 */       for (int i = this.yOffset_; i < this.yOffset_ + this.dHeight_; i++) {
/* 337 */         this.baseJI_.setChannel(0, this.xOffset_, i, this.dWidth_, 1, arrayOfByte, 0, 0);
/*     */       }
/* 339 */       break;
/*     */     case 3:
/* 343 */       if (this.prevSaved_ == null)
/* 344 */         throw new RuntimeException("Internal GIFDecoder Error");
/* 345 */       this.baseJI_.setChannel(0, this.xOffset_, this.yOffset_, this.dWidth_, this.dHeight_, this.prevSaved_, 0, this.dWidth_);
/* 346 */       break;
/*     */     case 0:
/*     */     case 1:
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized boolean driveDecoder()
/*     */     throws JimiException
/*     */   {
/*     */     try
/*     */     {
/* 110 */       if (this.gifFH_ == null) {
/* 111 */         this.gifFH_ = new GIFFileHeader(this.dIn_);
/*     */       }
/*     */ 
/* 114 */       getNextImage(this.dIn_, this.ji_);
/* 115 */       this.ji_.addFullCoverage();
/*     */ 
/* 117 */       this.state_ |= 6;
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/* 121 */       this.state_ &= -9;
/* 122 */       throw new JimiException(localIOException.getMessage());
/*     */     }
/*     */     catch (JimiException localJimiException)
/*     */     {
/* 126 */       this.state_ &= -9;
/* 127 */       throw new JimiException(localJimiException.getMessage());
/*     */     }
/*     */ 
/* 130 */     return false;
/*     */   }
/*     */ 
/*     */   public void freeDecoder()
/*     */     throws JimiException
/*     */   {
/* 154 */     this.in_ = null;
/* 155 */     this.dIn_ = null;
/* 156 */     this.ji_ = null;
/*     */   }
/*     */ 
/*     */   public int getCapabilities()
/*     */   {
/* 604 */     return 1;
/*     */   }
/*     */ 
/*     */   public AdaptiveRasterImage getJimiImage()
/*     */   {
/* 167 */     return this.ji_;
/*     */   }
/*     */ 
/*     */   public void getNextImage(LEDataInputStream paramLEDataInputStream, AdaptiveRasterImage paramAdaptiveRasterImage)
/*     */     throws JimiException, IOException
/*     */   {
/* 178 */     int j = 0;
/* 179 */     AdaptiveRasterImage localAdaptiveRasterImage = createAdaptiveRasterImage();
/*     */     while (true)
/*     */     {
/*     */       int i;
/* 184 */       if (this.gotPrevImageSeperator_)
/* 185 */         i = 44;
/*     */       else {
/* 187 */         i = paramLEDataInputStream.readByte();
/*     */       }
/* 189 */       switch (i) {
/*     */       default:
/*     */       case 44:
/* 192 */         if (j != 0)
/*     */         {
/* 194 */           this.gotPrevImageSeperator_ = true;
/* 195 */           return;
/*     */         }
/* 197 */         GIFImageDescriptor localGIFImageDescriptor = new GIFImageDescriptor(paramLEDataInputStream);
/* 198 */         this.imageDescriptor_ = localGIFImageDescriptor;
/*     */ 
/* 200 */         initJimiImage(localGIFImageDescriptor, localAdaptiveRasterImage);
/* 201 */         decodeImage(paramLEDataInputStream, localGIFImageDescriptor, localAdaptiveRasterImage);
/* 202 */         localAdaptiveRasterImage.addFullCoverage();
/* 203 */         addImageDelta(localGIFImageDescriptor, localAdaptiveRasterImage, this.ji_);
/* 204 */         this.gifGraphicExt_ = null;
/* 205 */         j = 1;
/* 206 */         this.gotPrevImageSeperator_ = false;
/* 207 */         break;
/*     */       case 59:
/* 212 */         this.state_ &= -9;
/* 213 */         this.gotPrevImageSeperator_ = false;
/* 214 */         this.gifGraphicExt_ = null;
/* 215 */         return;
/*     */       case 33:
/* 218 */         i = this.dIn_.readByte();
/*     */         int k;
/* 221 */         switch (i) { case -6:
/*     */         case -5:
/*     */         case -4:
/*     */         case -3:
/*     */         case 0:
/*     */         default:
/*     */         case -1:
/*     */         case 1:
/* 225 */           int m = paramLEDataInputStream.readByte();
/* 226 */           byte[] arrayOfByte1 = new byte[m];
/* 227 */           paramLEDataInputStream.readFully(arrayOfByte1);
/* 228 */           String str = new String(arrayOfByte1);
/*     */ 
/* 230 */           if (str.equals("NETSCAPE2.0")) {
/* 231 */             int n = paramLEDataInputStream.read();
/* 232 */             byte[] arrayOfByte3 = new byte[n];
/* 233 */             for (int i1 = 0; i1 < arrayOfByte3.length; i1++) {
/* 234 */               arrayOfByte3[i1] = ((byte)paramLEDataInputStream.read());
/*     */             }
/*     */ 
/* 237 */             if ((n == 3) && (arrayOfByte3[0] == 1)) {
/* 238 */               this.numberOfLoops_ = ((arrayOfByte3[2] & 0xFF) << 8);
/* 239 */               this.numberOfLoops_ |= arrayOfByte3[1] & 0xFF;
/* 240 */               if (this.numberOfLoops_ != 0) {
/* 241 */                 this.numberOfLoops_ += 1;
/*     */               }
/*     */             }
/*     */           }
/* 245 */           while ((k = paramLEDataInputStream.readUnsignedByte()) != 0)
/* 246 */             paramLEDataInputStream.skip(k);
/* 247 */           break;
/*     */         case -2:
/* 249 */           if (this.comment_ == null)
/*     */           {
/* 252 */             while ((k = paramLEDataInputStream.readUnsignedByte()) != 0)
/*     */             {
/* 254 */               byte[] arrayOfByte2 = new byte[k];
/* 255 */               paramLEDataInputStream.readFully(arrayOfByte2);
/* 256 */               this.comment_ += new String(arrayOfByte2, 0);
/*     */             }
/*     */ 
/*     */           }
/*     */           else
/*     */           {
/* 262 */             while ((k = paramLEDataInputStream.readUnsignedByte()) != 0)
/*     */             {
/*     */               byte[] arrayOfByte2;
/* 263 */               paramLEDataInputStream.skip(k);
/*     */             }
/*     */           }
/* 265 */           break;
/*     */         case -7:
/* 268 */           this.gifGraphicExt_ = new GIFGraphicExt(paramLEDataInputStream);
/*     */         }
/*     */         break;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getNumberOfImages()
/*     */   {
/* 172 */     return -1;
/*     */   }
/*     */ 
/*     */   public int getState()
/*     */   {
/* 162 */     return this.state_;
/*     */   }
/*     */ 
/*     */   public void initDecoder(InputStream paramInputStream, AdaptiveRasterImage paramAdaptiveRasterImage)
/*     */     throws JimiException
/*     */   {
/*  96 */     this.in_ = paramInputStream;
/*  97 */     this.dIn_ = new LEDataInputStream(paramInputStream);
/*  98 */     this.ji_ = paramAdaptiveRasterImage;
/*  99 */     this.state_ = 8;
/* 100 */     this.gifFH_ = null;
/* 101 */     this.gifGraphicExt_ = null;
/* 102 */     this.gotPrevImageSeperator_ = false;
/* 103 */     this.baseJI_ = null;
/*     */   }
/*     */ 
/*     */   private void initJimiImage(GIFImageDescriptor paramGIFImageDescriptor, AdaptiveRasterImage paramAdaptiveRasterImage)
/*     */     throws JimiException
/*     */   {
/*     */     GIFColorTable localGIFColorTable;
/*     */     int i;
/* 509 */     if ((paramGIFImageDescriptor.packed & 0x80) != 0)
/*     */     {
/* 511 */       localGIFColorTable = paramGIFImageDescriptor.colorTable;
/* 512 */       i = paramGIFImageDescriptor.colorTableNumBits;
/*     */     }
/*     */     else
/*     */     {
/* 516 */       localGIFColorTable = this.gifFH_.colorTable;
/* 517 */       i = this.gifFH_.colorTableNumBits;
/*     */     }
/*     */     IndexColorModel localIndexColorModel;
/* 521 */     if ((this.gifGraphicExt_ != null) && 
/* 522 */       ((this.gifGraphicExt_.packed & 0x1) != 0))
/*     */     {
/* 524 */       localIndexColorModel = new IndexColorModel(8, localGIFColorTable.red.length, 
/* 525 */         localGIFColorTable.red, localGIFColorTable.green, localGIFColorTable.blue, 
/* 526 */         this.gifGraphicExt_.colorIndex);
/*     */     }
/*     */     else
/*     */     {
/* 530 */       localIndexColorModel = new IndexColorModel(8, localGIFColorTable.red.length, 
/* 531 */         localGIFColorTable.red, localGIFColorTable.green, localGIFColorTable.blue);
/*     */     }
/*     */ 
/* 534 */     paramAdaptiveRasterImage.setColorModel(localIndexColorModel);
/* 535 */     paramAdaptiveRasterImage.setSize(paramGIFImageDescriptor.width, paramGIFImageDescriptor.height);
/* 536 */     setOptions(paramAdaptiveRasterImage);
/* 537 */     paramAdaptiveRasterImage.setPixels();
/*     */   }
/*     */ 
/*     */   public boolean mustWaitForOptions()
/*     */   {
/* 642 */     return true;
/*     */   }
/*     */ 
/*     */   void saveForNextDelta()
/*     */     throws JimiException
/*     */   {
/* 357 */     switch (this.disposal_)
/*     */     {
/*     */     case 3:
/* 371 */       this.prevSaved_ = new byte[this.dWidth_ * this.dHeight_];
/* 372 */       int j = 0;
/* 373 */       byte[] arrayOfByte = new byte[this.baseJI_.getWidth()];
/* 374 */       int i = this.yOffset_;
/*     */       while (true) {
/* 376 */         this.baseJI_.getChannel(0, i, arrayOfByte, 0);
/* 377 */         System.arraycopy(arrayOfByte, this.xOffset_, this.prevSaved_, j, this.dWidth_);
/* 378 */         j += this.dWidth_;
/*     */ 
/* 374 */         i++; if (i >= this.yOffset_ + this.dHeight_)
/*     */         {
/* 380 */           break;
/*     */         }
/*     */       }
/*     */     case 0:
/*     */     case 1:
/*     */     case 2:
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setJimiImage(AdaptiveRasterImage paramAdaptiveRasterImage)
/*     */   {
/* 611 */     this.ji_ = paramAdaptiveRasterImage;
/*     */   }
/*     */ 
/*     */   void setOptions(AdaptiveRasterImage paramAdaptiveRasterImage)
/*     */     throws JimiException
/*     */   {
/* 136 */     GIFOptions localGIFOptions = new GIFOptions();
/* 137 */     localGIFOptions.setInterlaced(this.imageDescriptor_.interlace);
/* 138 */     localGIFOptions.setNumberOfLoops(this.numberOfLoops_);
/* 139 */     if (this.gifGraphicExt_ != null) {
/* 140 */       localGIFOptions.setFrameDelay(this.gifGraphicExt_.delayTime);
/* 141 */       if ((this.gifGraphicExt_.packed & 0x1) != 0) {
/* 142 */         localGIFOptions.setTransparentIndex(this.gifGraphicExt_.colorIndex);
/*     */       }
/* 144 */       if ((this.imageDescriptor_.packed & 0x80) != 0) {
/* 145 */         localGIFOptions.setUseLocalPalettes(true);
/*     */       }
/*     */     }
/*     */ 
/* 149 */     paramAdaptiveRasterImage.setOptions(localGIFOptions);
/*     */   }
/*     */ 
/*     */   public void skipImage()
/*     */     throws JimiException
/*     */   {
/* 620 */     AdaptiveRasterImage localAdaptiveRasterImage = createAdaptiveRasterImage();
/*     */     try
/*     */     {
/* 623 */       if (this.gifFH_ == null) {
/* 624 */         this.gifFH_ = new GIFFileHeader(this.dIn_);
/*     */       }
/* 626 */       getNextImage(this.dIn_, localAdaptiveRasterImage);
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/* 630 */       throw new JimiException(localIOException.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean usesChanneledData()
/*     */   {
/* 637 */     return true;
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.decoder.gif.GIFDecoder
 * JD-Core Version:    0.6.2
 */