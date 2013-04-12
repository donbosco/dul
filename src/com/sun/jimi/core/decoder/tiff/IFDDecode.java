/*      */ package com.sun.jimi.core.decoder.tiff;
/*      */ 
/*      */ import com.sun.jimi.core.JimiException;
/*      */ import com.sun.jimi.core.compat.AdaptiveRasterImage;
/*      */ import com.sun.jimi.core.util.JimiUtil;
/*      */ import com.sun.jimi.core.util.ProgressMonitorSupport;
/*      */ import com.sun.jimi.core.util.SeekInputStream;
/*      */ import java.awt.image.ColorModel;
/*      */ import java.awt.image.DirectColorModel;
/*      */ import java.awt.image.IndexColorModel;
/*      */ import java.io.ByteArrayInputStream;
/*      */ import java.io.IOException;
/*      */ import java.util.Enumeration;
/*      */ 
/*      */ class IFDDecode
/*      */ {
/*      */   AdaptiveRasterImage ji;
/*      */   IFD ifd;
/*      */   SeekInputStream sis;
/*      */   int newsubfiletype;
/*      */   int imagewidth;
/*      */   int imagelength;
/*      */   short[] bitspersample;
/*      */   int compression;
/*      */   int photometricinterpretation;
/*      */   int[] stripoffsets;
/*      */   int samplesperpixel;
/*      */   int rowsperstrip;
/*      */   int[] stripbytecounts;
/*      */   float xres;
/*      */   float yres;
/*      */   int resolutionunit;
/*      */   short[] colormap;
/*      */   int planarconfiguration;
/*      */   int extrasamples;
/*      */   float ycbcrcoefficients;
/*      */   int ycbcrpositioning;
/*      */   int[] ycbcrsubsampling;
/*      */   int referenceblackwhite;
/*      */   int badfaxlines;
/*      */   int cleanfaxdata;
/*      */   int consecutivebadfaxlines;
/*      */   int tilewidth;
/*      */   int tilelength;
/*      */   int[] tilebytecounts;
/*      */   int[] tileoffsets;
/*      */   int fillorder;
/*      */   int orientation;
/*      */   int t4options;
/*      */   int t6options;
/*      */   int predictor;
/*      */   LZWDecomp lzwDecomp_;
/*      */   int stripsperimage;
/*      */   int tilesacross;
/*      */   int tilesdown;
/*      */   int tilesperimage;
/*      */   TIFDecoder decoder;
/*      */   byte[] spc_o_;
/*      */ 
/*      */   IFDDecode(TIFDecoder paramTIFDecoder, AdaptiveRasterImage paramAdaptiveRasterImage, SeekInputStream paramSeekInputStream)
/*      */     throws IOException
/*      */   {
/*  106 */     initDefaults();
/*  107 */     this.ji = paramAdaptiveRasterImage;
/*  108 */     this.sis = paramSeekInputStream;
/*  109 */     this.decoder = paramTIFDecoder;
/*      */   }
/*      */ 
/*      */   ColorModel createColorModel()
/*      */     throws JimiException
/*      */   {
/*      */     int i;
/*  309 */     switch (this.photometricinterpretation)
/*      */     {
/*      */     case 0:
/*  312 */       i = (1 << this.bitspersample[0]) - 1;
/*  313 */       return new DirectColorModel(this.bitspersample[0], i, i, i);
/*      */     case 1:
/*  316 */       i = (1 << this.bitspersample[0]) - 1;
/*  317 */       return new DirectColorModel(this.bitspersample[0], i, i, i);
/*      */     case 3:
/*  320 */       return createPaleteColorModel();
/*      */     case 2:
/*  323 */       if ((this.samplesperpixel != 3) || (this.bitspersample[0] != 8) || 
/*  324 */         (this.bitspersample[1] != 8) || 
/*  325 */         (this.bitspersample[2] != 8))
/*  326 */         throw new JimiException("RGB image not 3 x 8 bits");
/*  327 */       return ColorModel.getRGBdefault();
/*      */     }
/*      */ 
/*  330 */     return null;
/*      */   }
/*      */ 
/*      */   Decompressor createDecompressor(byte[] paramArrayOfByte)
/*      */     throws JimiException
/*      */   {
/*  522 */     Object localObject = null;
/*      */ 
/*  524 */     TiffNumberReader localTiffNumberReader = new TiffNumberReader(paramArrayOfByte);
/*      */ 
/*  526 */     switch (this.compression)
/*      */     {
/*      */     case 1:
/*  529 */       localObject = new Decompressor(localTiffNumberReader, this.fillorder, this.bitspersample[0]);
/*  530 */       break;
/*      */     case 2:
/*  533 */       localObject = new CCITT3d1Decomp(localTiffNumberReader, this.fillorder);
/*  534 */       break;
/*      */     case 3:
/*  537 */       localObject = new CCITTClassFDecomp(localTiffNumberReader, this.fillorder, (this.t4options & 0x4) != 0);
/*  538 */       break;
/*      */     case 4:
/*  541 */       localObject = new CCITT3d2Decomp(localTiffNumberReader, this.fillorder);
/*  542 */       break;
/*      */     case 32773:
/*  545 */       localObject = new Packbits(localTiffNumberReader, this.fillorder, this.bitspersample[0]);
/*  546 */       break;
/*      */     case 5:
/*  549 */       if ((this.predictor == 2) && (this.bitspersample[0] != 8)) {
/*  550 */         throw new JimiException("horizontal difference only supported for 8 bits currently");
/*      */       }
/*  552 */       if (this.lzwDecomp_ == null)
/*      */       {
/*  554 */         this.lzwDecomp_ = new LZWDecomp(new ByteArrayInputStream(paramArrayOfByte, 0, paramArrayOfByte.length - 2), 
/*  555 */           this.fillorder, this.bitspersample[0], this.predictor);
/*      */       }
/*      */       else
/*  558 */         this.lzwDecomp_.setInputStream(new ByteArrayInputStream(paramArrayOfByte, 0, paramArrayOfByte.length - 2));
/*  559 */       localObject = this.lzwDecomp_;
/*  560 */       break;
/*      */     }
/*  562 */     return (Decompressor) localObject;
/*      */   }
/*      */ 
/*      */   ColorModel createPaleteColorModel()
/*      */     throws JimiException
/*      */   {
/*  279 */     int i = this.colormap.length / 3;
/*      */ 
/*  285 */     byte[] arrayOfByte1 = new byte[i];
/*  286 */     byte[] arrayOfByte2 = new byte[i];
/*  287 */     byte[] arrayOfByte3 = new byte[i];
/*      */ 
/*  290 */     for (int j = 0; j < i; j++)
/*      */     {
/*  293 */       arrayOfByte1[j] = ((byte)(this.colormap[j] >> 8));
/*  294 */       arrayOfByte2[j] = ((byte)(this.colormap[(j + i)] >> 8));
/*  295 */       arrayOfByte3[j] = ((byte)(this.colormap[(j + 2 * i)] >> 8));
/*      */     }
/*      */ 
/*  298 */     return new IndexColorModel(8, i, arrayOfByte1, arrayOfByte2, arrayOfByte3);
/*      */   }
/*      */ 
/*      */   void decodeField(TIFField paramTIFField)
/*      */     throws IOException
/*      */   {
/*  131 */     switch (paramTIFField.id)
/*      */     {
/*      */     case 254:
/*  135 */       this.newsubfiletype = paramTIFField.getInt(this.sis);
/*  136 */       break;
/*      */     case 256:
/*  138 */       this.imagewidth = paramTIFField.getInt(this.sis);
/*  139 */       break;
/*      */     case 257:
/*  141 */       this.imagelength = paramTIFField.getInt(this.sis);
/*  142 */       break;
/*      */     case 258:
/*  144 */       this.bitspersample = paramTIFField.getShortArray(this.sis);
/*  145 */       break;
/*      */     case 259:
/*  147 */       this.compression = paramTIFField.getInt(this.sis);
/*  148 */       break;
/*      */     case 262:
/*  150 */       this.photometricinterpretation = paramTIFField.getInt(this.sis);
/*  151 */       break;
/*      */     case 273:
/*  153 */       this.stripoffsets = paramTIFField.getIntArray(this.sis);
/*  154 */       break;
/*      */     case 277:
/*  156 */       this.samplesperpixel = paramTIFField.getInt(this.sis);
/*  157 */       break;
/*      */     case 278:
/*  159 */       this.rowsperstrip = paramTIFField.getInt(this.sis);
/*  160 */       break;
/*      */     case 279:
/*  162 */       this.stripbytecounts = paramTIFField.getIntArray(this.sis);
/*  163 */       break;
/*      */     case 282:
/*  165 */       this.xres = paramTIFField.getRational(this.sis);
/*  166 */       break;
/*      */     case 283:
/*  168 */       this.yres = paramTIFField.getRational(this.sis);
/*  169 */       break;
/*      */     case 296:
/*  171 */       this.resolutionunit = paramTIFField.getInt(this.sis);
/*  172 */       break;
/*      */     case 320:
/*  176 */       this.colormap = paramTIFField.getShortArray(this.sis);
/*  177 */       break;
/*      */     case 284:
/*  181 */       this.planarconfiguration = paramTIFField.getInt(this.sis);
/*  182 */       break;
/*      */     case 338:
/*  184 */       this.extrasamples = paramTIFField.getInt(this.sis);
/*  185 */       break;
/*      */     case 529:
/*  189 */       this.ycbcrcoefficients = paramTIFField.getRational(this.sis);
/*  190 */       break;
/*      */     case 530:
/*  192 */       this.ycbcrsubsampling = paramTIFField.getIntArray(this.sis);
/*  193 */       break;
/*      */     case 531:
/*  195 */       this.ycbcrpositioning = paramTIFField.getInt(this.sis);
/*  196 */       break;
/*      */     case 326:
/*  200 */       this.badfaxlines = paramTIFField.getInt(this.sis);
/*  201 */       break;
/*      */     case 327:
/*  203 */       this.cleanfaxdata = paramTIFField.getInt(this.sis);
/*  204 */       break;
/*      */     case 328:
/*  206 */       this.consecutivebadfaxlines = paramTIFField.getInt(this.sis);
/*  207 */       break;
/*      */     case 322:
/*  211 */       this.tilewidth = paramTIFField.getInt(this.sis);
/*  212 */       break;
/*      */     case 323:
/*  214 */       this.tilelength = paramTIFField.getInt(this.sis);
/*  215 */       break;
/*      */     case 324:
/*  217 */       this.tileoffsets = paramTIFField.getIntArray(this.sis);
/*  218 */       break;
/*      */     case 325:
/*  220 */       this.tilebytecounts = paramTIFField.getIntArray(this.sis);
/*  221 */       break;
/*      */     case 266:
/*  225 */       this.fillorder = paramTIFField.getInt(this.sis);
/*  226 */       break;
/*      */     case 274:
/*  228 */       this.orientation = paramTIFField.getInt(this.sis);
/*  229 */       break;
/*      */     case 292:
/*  231 */       this.t4options = paramTIFField.getInt(this.sis);
/*  232 */       break;
/*      */     case 293:
/*  234 */       this.t6options = paramTIFField.getInt(this.sis);
/*  235 */       break;
/*      */     case 317:
/*  238 */       this.predictor = paramTIFField.getInt(this.sis);
/*  239 */       break;
/*      */     }
/*      */   }
/*      */ 
/*      */   void decodeImage()
/*      */     throws JimiException, IOException
/*      */   {
/*  337 */     if (this.planarconfiguration == 2) {
/*  338 */       throw new JimiException("Seperate planar config not supported");
/*      */     }
/*  340 */     if ((this.compression == 6) || 
/*  341 */       (this.compression == 7)) {
/*  342 */       throw new JimiException("TIFF JPG format not supported");
/*      */     }
/*      */ 
/*  346 */     if ((this.rowsperstrip & 0x80000000) != 0) {
/*  347 */       this.rowsperstrip = this.imagelength;
/*      */     }
/*  349 */     if (this.tilewidth == 0)
/*      */     {
/*  351 */       this.stripsperimage = ((this.imagelength + this.rowsperstrip - 1) / this.rowsperstrip);
/*      */     }
/*      */     else
/*      */     {
/*  355 */       this.tilesacross = ((this.imagewidth + this.tilewidth - 1) / this.tilewidth);
/*  356 */       this.tilesdown = ((this.imagelength + this.tilelength - 1) / this.tilelength);
/*  357 */       this.tilesperimage = (this.tilesacross * this.tilesdown);
/*      */ 
/*  359 */       if (this.tileoffsets == null)
/*      */       {
/*  361 */         this.tileoffsets = this.stripoffsets;
/*  362 */         this.tilebytecounts = this.stripbytecounts;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  367 */     switch (this.photometricinterpretation)
/*      */     {
/*      */     case 4:
/*  382 */       throw new JimiException("photometric MASK unsupported");
/*      */     case 5:
/*  385 */       throw new JimiException("photometric SEPERATED unsupported");
/*      */     case 6:
/*  388 */       throw new JimiException("photometric YCBCR unsupported");
/*      */     case 8:
/*  391 */       throw new JimiException("photometric CIELAB unsupported");
/*      */     case 7:
/*      */     default:
/*  394 */       throw new JimiException("Photometric Interpretation invalid " + this.photometricinterpretation);
/*      */     case 0:
/*      */     case 1:
/*      */     case 2:
/*  398 */     case 3: } switch (this.orientation)
/*      */     {
/*      */     case 1:
/*      */     case 2:
/*      */     case 3:
/*      */     case 4:
/*  404 */       this.ji.setSize(this.imagewidth, this.imagelength);
/*  405 */       break;
/*      */     case 5:
/*      */     case 6:
/*      */     case 7:
/*      */     case 8:
/*  411 */       this.ji.setSize(this.imagelength, this.imagewidth);
/*  412 */       this.ji.setHints(1);
/*  413 */       this.ji.setProperty("fixedaspect", Boolean.TRUE);
/*  414 */       break;
/*      */     }
/*      */ 
/*  417 */     this.ji.setProperty("xres", new Float(this.xres));
/*  418 */     this.ji.setProperty("yres", new Float(this.yres));
/*  419 */     ColorModel localColorModel = createColorModel();
/*  420 */     this.ji.setColorModel(localColorModel);
/*  421 */     this.ji.setPixels();
/*      */ 
/*  423 */     if (((this.photometricinterpretation == 1) || 
/*  424 */       (this.photometricinterpretation == 0) || 
/*  425 */       (this.photometricinterpretation == 3)) && 
/*  426 */       (this.samplesperpixel == 1))
/*      */     {
/*  428 */       if (this.tilewidth == 0)
/*  429 */         decodeStrips();
/*      */       else {
/*  431 */         decodeTiles();
/*      */       }
/*      */ 
/*      */     }
/*  436 */     else if ((this.compression == 1) || 
/*  437 */       (this.compression == 32773) || 
/*  438 */       (this.compression == 5))
/*      */     {
/*  441 */       if (this.tilewidth == 0)
/*  442 */         decodeStrips();
/*      */       else
/*  444 */         decodeTiles();
/*      */     }
/*      */     else
/*  447 */       throw new JimiException("Unsupported compression " + this.compression);
/*      */   }
/*      */ 
/*      */   void decodeJPG()
/*      */     throws JimiException, IOException
/*      */   {
/*  460 */     int i = this.bitspersample[0];
/*      */ 
/*  465 */     int k = 0;
/*  466 */     int j = this.stripsperimage;
/*      */     do { k += this.stripbytecounts[0];
/*      */ 
/*  466 */       j--; } while (j >= 0);
/*      */ 
/*  469 */     byte[] arrayOfByte2 = new byte[k];
/*  470 */     int m = 0;
/*  471 */     for (j = 0; j < this.stripsperimage; j++)
/*      */     {
/*  473 */       byte[] arrayOfByte1 = new byte[this.stripbytecounts[j]];
/*  474 */       this.sis.seek(this.stripoffsets[j]);
/*  475 */       this.sis.readFully(arrayOfByte1, 0, this.stripbytecounts[j]);
/*  476 */       System.arraycopy(arrayOfByte1, 0, arrayOfByte2, m, this.stripbytecounts[j]);
/*  477 */       m += this.stripbytecounts[j];
/*      */     }
/*      */   }
/*      */ 
/*      */   void decodeStrips()
/*      */     throws JimiException, IOException
/*      */   {
/*  572 */     int[] arrayOfInt = null;
/*  573 */     byte[] arrayOfByte2 = null;
/*  574 */     int i = 1;
/*  575 */     int j = this.bitspersample[0];
/*  576 */     int k = 0;
/*      */ 
/*  578 */     if (this.samplesperpixel != 1)
/*      */     {
/*  580 */       arrayOfInt = new int[this.imagewidth];
/*  581 */       arrayOfByte2 = new byte[this.imagewidth * this.samplesperpixel];
/*      */     }
/*  585 */     else if (j == 1) {
/*  586 */       arrayOfByte2 = new byte[(this.imagewidth + 7) / 8];
/*      */     } else {
/*  588 */       arrayOfByte2 = new byte[this.imagewidth];
/*      */     }
/*      */ 
/*  592 */     int m = 0;
/*  593 */     for (int n = 0; n < this.stripsperimage; n++)
/*  594 */       if (this.stripbytecounts[n] > m)
/*  595 */         m = this.stripbytecounts[n];
/*  596 */     byte[] arrayOfByte1 = new byte[m + 2];
/*      */ 
/*  598 */     for (int i1 = 0; i1 < this.stripsperimage; i1++)
/*      */     {
/*  601 */       this.sis.seek(this.stripoffsets[i1]);
/*  602 */       this.sis.readFully(arrayOfByte1, 0, this.stripbytecounts[i1]);
/*  603 */       Decompressor localDecompressor = createDecompressor(arrayOfByte1);
/*  604 */       localDecompressor.setRowsPerStrip(this.rowsperstrip);
/*      */ 
/*  606 */       if ((this.photometricinterpretation == 0) && (this.samplesperpixel == 1)) {
/*  607 */         localDecompressor.setInvert(true);
/*      */       }
/*  609 */       localDecompressor.begOfStrip();
/*  610 */       if (i != 0)
/*  611 */         localDecompressor.begOfPage();
/*  612 */       i = 0;
/*      */       int i2;
/*      */       int i3;
/*  614 */       if (this.samplesperpixel == 1)
/*      */       {
/*  616 */         if (j == 1)
/*      */         {
/*  618 */           i2 = numRows(i1);
/*      */           do {
/*  620 */             localDecompressor.decodeLine(arrayOfByte2, this.imagewidth);
/*      */ 
/*  623 */             setPackedChannel_Oriented(k, arrayOfByte2);
/*  624 */             k++;
/*  625 */             this.decoder.setProgress(k * 100 / this.imagelength);
/*      */ 
/*  618 */             i2--; } while (i2 >= 0);
/*      */         }
/*      */         else
/*      */         {
/*  630 */           i2 = numRows(i1);
/*      */           do {
/*  632 */             localDecompressor.decodeLine(arrayOfByte2, arrayOfByte2.length);
/*  633 */             if (this.compression == 5)
/*      */             {
/*  635 */               if (this.predictor == 2)
/*      */               {
/*  637 */                 for (i3 = 0; i3 < arrayOfByte2.length - 1; i3++)
/*      */                 {
/*      */                   int tmp336_335 = (i3 + 1);
/*      */                   byte[] tmp336_331 = arrayOfByte2; tmp336_331[tmp336_335] = ((byte)(tmp336_331[tmp336_335] + arrayOfByte2[i3]));
/*      */                 }
/*      */               }
/*      */             }
/*  642 */             setChannel_Oriented(0, k, arrayOfByte2);
/*  643 */             k++;
/*  644 */             this.decoder.setProgress(k * 100 / this.imagelength);
/*      */ 
/*  630 */             i2--; } while (i2 >= 0);
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*  650 */         i2 = numRows(i1);
/*      */         do {
/*  652 */           localDecompressor.decodeLine(arrayOfByte2, arrayOfByte2.length);
/*  653 */           if (this.compression == 5)
/*      */           {
/*  655 */             if (this.predictor == 2)
/*      */             {
/*  657 */               for (i3 = 0; i3 < (arrayOfByte2.length - 1) / 3; i3++)
/*      */               {
/*      */                 int tmp444_443 = ((i3 + 1) * 3);
/*      */                 byte[] tmp444_437 = arrayOfByte2; tmp444_437[tmp444_443] = ((byte)(tmp444_437[tmp444_443] + arrayOfByte2[(i3 * 3)]));
/*      */                 int tmp464_463 = ((i3 + 1) * 3 + 1);
/*      */                 byte[] tmp464_455 = arrayOfByte2; tmp464_455[tmp464_463] = ((byte)(tmp464_455[tmp464_463] + arrayOfByte2[(i3 * 3 + 1)]));
/*      */                 int tmp486_485 = ((i3 + 1) * 3 + 2);
/*      */                 byte[] tmp486_477 = arrayOfByte2; tmp486_477[tmp486_485] = ((byte)(tmp486_477[tmp486_485] + arrayOfByte2[(i3 * 3 + 2)]));
/*      */               }
/*      */             }
/*      */           }
/*  665 */           for (i3 = 0; i3 < this.imagewidth; i3++)
/*      */           {
/*  667 */             arrayOfInt[i3] = 
/*  669 */               (0xFF000000 | 
/*  668 */               (arrayOfByte2[(i3 * 3)] & 0xFF) << 16 | 
/*  669 */               (arrayOfByte2[(i3 * 3 + 1)] & 0xFF) << 8 | 
/*  670 */               arrayOfByte2[(i3 * 3 + 2)] & 0xFF);
/*      */           }
/*      */ 
/*  673 */           setChannel_Oriented(k, arrayOfInt);
/*  674 */           k++;
/*  675 */           this.decoder.setProgress(k * 100 / this.imagelength);
/*      */ 
/*  650 */           i2--; } while (i2 >= 0);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   void decodeTags(IFD paramIFD)
/*      */     throws IOException
/*      */   {
/*  116 */     Enumeration localEnumeration = paramIFD.getFields();
/*      */ 
/*  118 */     while (localEnumeration.hasMoreElements())
/*      */     {
/*  120 */       TIFField localTIFField = (TIFField)localEnumeration.nextElement();
/*  121 */       decodeField(localTIFField);
/*      */     }
/*      */   }
/*      */ 
/*      */   void decodeTiles()
/*      */     throws JimiException, IOException
/*      */   {
/*  688 */     int i = this.bitspersample[0];
/*  689 */     byte[] arrayOfByte2 = null;
/*  690 */     int[] arrayOfInt = null;
/*  691 */     int j = 1;
/*  692 */     int k = 0;
/*  693 */     int m = 0;
/*      */ 
/*  697 */     if (this.samplesperpixel != 1)
/*      */     {
/*  699 */       arrayOfInt = new int[this.tilewidth];
/*  700 */       arrayOfByte2 = new byte[this.tilewidth * this.samplesperpixel];
/*      */     }
/*  704 */     else if (i == 1) {
/*  705 */       arrayOfByte2 = new byte[(this.tilewidth + 7) / 8];
/*      */     } else {
/*  707 */       arrayOfByte2 = new byte[this.tilewidth];
/*      */     }
/*      */ 
/*  711 */     int i2 = 0;
/*  712 */     for (int i3 = 0; i3 < this.tilesperimage; i3++)
/*  713 */       if (this.tilebytecounts[i3] > i2)
/*  714 */         i2 = this.tilebytecounts[i3];
/*  715 */     byte[] arrayOfByte1 = new byte[i2 + 2];
/*      */ 
/*  717 */     for (int i4 = 0; i4 < this.tilesperimage; i4++)
/*      */     {
/*  719 */       this.sis.seek(this.tileoffsets[i4]);
/*  720 */       this.sis.readFully(arrayOfByte1, 0, this.tilebytecounts[i4]);
/*  721 */       Decompressor localDecompressor = createDecompressor(arrayOfByte1);
/*      */ 
/*  723 */       if (this.photometricinterpretation == 0) {
/*  724 */         localDecompressor.setInvert(true);
/*      */       }
/*  726 */       localDecompressor.begOfStrip();
/*  727 */       if (j != 0)
/*  728 */         localDecompressor.begOfPage();
/*  729 */       j = 0;
/*      */ 
/*  731 */       int n = m;
/*  732 */       int i5 = this.tilelength;
/*      */       do {
/*  734 */         localDecompressor.decodeLine(arrayOfByte2, arrayOfByte2.length);
/*      */         int i1;
/*  737 */         if (k + this.tilewidth > this.imagewidth)
/*  738 */           i1 = this.imagewidth - k;
/*      */         else {
/*  740 */           i1 = this.tilewidth;
/*      */         }
/*      */ 
/*  743 */         if (n < this.imagelength)
/*      */         {
/*      */           int i6;
/*  745 */           if (this.samplesperpixel == 1)
/*      */           {
/*  747 */             if (i == 1)
/*      */             {
/*  751 */               setPackedChannel_Oriented(k, n, i1, arrayOfByte2);
/*      */             }
/*      */             else
/*      */             {
/*  755 */               if (this.compression == 5)
/*      */               {
/*  757 */                 if (this.predictor == 2)
/*      */                 {
/*  759 */                   for (i6 = 0; i6 < arrayOfByte2.length - 1; i6++)
/*      */                   {
/*      */                     int tmp319_318 = (i6 + 1);
/*      */                     byte[] tmp319_314 = arrayOfByte2; tmp319_314[tmp319_318] = ((byte)(tmp319_314[tmp319_318] + arrayOfByte2[i6]));
/*      */                   }
/*      */                 }
/*      */               }
/*  763 */               setChannel_Oriented(k, n, i1, arrayOfByte2);
/*      */             }
/*      */           }
/*      */           else
/*      */           {
/*  768 */             if (this.compression == 5)
/*      */             {
/*  770 */               if (this.predictor == 2)
/*      */               {
/*  772 */                 for (i6 = 0; i6 < (arrayOfByte2.length - 1) / 3; i6++)
/*      */                 {
/*      */                   int tmp383_382 = ((i6 + 1) * 3);
/*      */                   byte[] tmp383_376 = arrayOfByte2; tmp383_376[tmp383_382] = ((byte)(tmp383_376[tmp383_382] + arrayOfByte2[(i6 * 3)]));
/*      */                   int tmp403_402 = ((i6 + 1) * 3 + 1);
/*      */                   byte[] tmp403_394 = arrayOfByte2; tmp403_394[tmp403_402] = ((byte)(tmp403_394[tmp403_402] + arrayOfByte2[(i6 * 3 + 1)]));
/*      */                   int tmp425_424 = ((i6 + 1) * 3 + 2);
/*      */                   byte[] tmp425_416 = arrayOfByte2; tmp425_416[tmp425_424] = ((byte)(tmp425_416[tmp425_424] + arrayOfByte2[(i6 * 3 + 2)]));
/*      */                 }
/*      */               }
/*      */             }
/*      */ 
/*  781 */             for (i6 = 0; i6 < this.imagewidth; i6++)
/*      */             {
/*  783 */               arrayOfInt[i6] = 
/*  785 */                 (0xFF000000 | 
/*  784 */                 (arrayOfByte2[(i6 * 3)] & 0xFF) << 16 | 
/*  785 */                 (arrayOfByte2[(i6 * 3 + 1)] & 0xFF) << 8 | 
/*  786 */                 arrayOfByte2[(i6 * 3 + 2)] & 0xFF);
/*      */             }
/*      */ 
/*  789 */             setChannel_Oriented(k, n, i1, arrayOfInt);
/*      */           }
/*  791 */           n++;
/*      */         }
/*      */         else {
/*  794 */           i5 = 0;
/*      */         }
/*  732 */         i5--; } while (i5 >= 0);
/*      */ 
/*  797 */       k += this.tilewidth;
/*  798 */       if (k > (this.tilesacross - 1) * this.tilewidth)
/*      */       {
/*  800 */         k = 0;
/*  801 */         m += this.tilelength;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   void initDefaults()
/*      */   {
/*  253 */     this.newsubfiletype = 0;
/*  254 */     this.bitspersample = new short[1];
/*  255 */     this.bitspersample[0] = 1;
/*  256 */     this.compression = 1;
/*  257 */     this.samplesperpixel = 1;
/*  258 */     this.rowsperstrip = -1;
/*  259 */     this.resolutionunit = 2;
/*      */ 
/*  261 */     this.planarconfiguration = 1;
/*      */ 
/*  263 */     this.tilewidth = 0;
/*  264 */     this.tilelength = 0;
/*      */ 
/*  266 */     this.fillorder = 1;
/*  267 */     this.orientation = 1;
/*  268 */     this.t6options = 0;
/*  269 */     this.t4options = 0;
/*  270 */     this.predictor = 1;
/*      */   }
/*      */ 
/*      */   int numRows(int paramInt)
/*      */   {
/*  489 */     if (paramInt == this.stripsperimage - 1)
/*      */     {
/*  491 */       int i = this.imagelength % this.rowsperstrip;
/*  492 */       return i != 0 ? i : this.rowsperstrip;
/*      */     }
/*      */ 
/*  495 */     return this.rowsperstrip;
/*      */   }
/*      */ 
/*      */   public int rowSize()
/*      */   {
/*  504 */     int i = this.bitspersample[0] * this.imagewidth;
/*  505 */     if (this.planarconfiguration == 1)
/*  506 */       i *= this.samplesperpixel;
/*  507 */     return (i + 7) / 8;
/*      */   }
/*      */ 
/*      */   void setChannel_Oriented(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte)
/*      */     throws JimiException
/*      */   {
/*      */     int k;
/*      */     int i;
/*      */     byte[] arrayOfByte;
/*      */     int j;
/*      */     int m;
/*  850 */     switch (this.orientation)
/*      */     {
/*      */     case 1:
/*  853 */       this.ji.setChannel(0, paramInt1, paramInt2, paramInt3, 1, paramArrayOfByte, 0, 0);
/*  854 */       break;
/*      */     case 2:
/*  857 */       k = this.ji.getWidth();
/*  858 */       i = 0;
/*  859 */       arrayOfByte = new byte[paramInt3];
/*      */ 
/*  861 */       j = paramInt3;
/*      */       do { arrayOfByte[j] = paramArrayOfByte[(i++)];
/*      */ 
/*  861 */         j--; } while (j >= 0);
/*      */ 
/*  865 */       paramInt1 = k - paramInt1 - paramInt3;
/*  866 */       this.ji.setChannel(0, paramInt1, paramInt2, paramInt3, 1, arrayOfByte, 0, 0);
/*  867 */       break;
/*      */     case 3:
/*  871 */       m = this.ji.getHeight();
/*  872 */       paramInt2 = m - paramInt2 - 1;
/*  873 */       k = this.ji.getWidth();
/*  874 */       arrayOfByte = new byte[paramInt3];
/*  875 */       i = paramInt3;
/*      */ 
/*  878 */       for (j = 0; j < paramInt3; j++) {
/*  879 */         arrayOfByte[j] = paramArrayOfByte[(--i)];
/*      */       }
/*      */ 
/*  882 */       paramInt1 = k - paramInt1 - paramInt3;
/*  883 */       this.ji.setChannel(0, paramInt1, paramInt2, paramInt3, 1, arrayOfByte, 0, 0);
/*  884 */       break;
/*      */     case 4:
/*  888 */       m = this.ji.getHeight();
/*  889 */       paramInt2 = m - paramInt2 - 1;
/*  890 */       this.ji.setChannel(0, paramInt1, paramInt2, paramInt3, 1, paramArrayOfByte, 0, 0);
/*  891 */       break;
/*      */     case 5:
/*  896 */       m = this.ji.getWidth();
/*  897 */       paramInt2 = m - paramInt2 - 1;
/*  898 */       setChannel_RotateCW90(paramInt1, paramInt2, paramInt3, paramArrayOfByte);
/*  899 */       break;
/*      */     case 6:
/*  902 */       setChannel_RotateCW90(paramInt1, paramInt2, paramInt3, paramArrayOfByte);
/*  903 */       break;
/*      */     case 7:
/*  908 */       k = this.ji.getHeight();
/*  909 */       i = 0;
/*  910 */       arrayOfByte = new byte[paramInt3];
/*      */ 
/*  913 */       j = paramInt3;
/*      */       do { arrayOfByte[j] = paramArrayOfByte[(i++)];
/*      */ 
/*  913 */         j--; } while (j >= 0);
/*      */ 
/*  917 */       paramInt1 = k - paramInt1 - paramInt3;
/*  918 */       setChannel_RotateCW90(paramInt1, paramInt2, paramInt3, arrayOfByte);
/*  919 */       break;
/*      */     case 8:
/*  925 */       m = this.ji.getWidth();
/*  926 */       paramInt2 = m - paramInt2 - 1;
/*      */ 
/*  929 */       k = this.ji.getHeight();
/*  930 */       arrayOfByte = new byte[paramInt3];
/*  931 */       i = paramInt3;
/*      */ 
/*  934 */       for (j = 0; j < paramInt3; j++) {
/*  935 */         arrayOfByte[j] = paramArrayOfByte[(--i)];
/*      */       }
/*      */ 
/*  938 */       paramInt1 = k - paramInt1 - paramInt3;
/*  939 */       setChannel_RotateCW90(paramInt1, paramInt2, paramInt3, arrayOfByte);
/*  940 */       break;
/*      */     }
/*      */   }
/*      */ 
/*      */   void setChannel_Oriented(int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt)
/*      */     throws JimiException
/*      */   {
/*      */     int k;
/*      */     int i;
/*      */     int[] arrayOfInt;
/*      */     int j;
/*      */     int m;
/*  996 */     switch (this.orientation)
/*      */     {
/*      */     case 1:
/*  999 */       this.ji.setChannel(paramInt1, paramInt2, paramInt3, 1, paramArrayOfInt, 0, 0);
/* 1000 */       break;
/*      */     case 2:
/* 1003 */       k = this.ji.getWidth();
/* 1004 */       i = 0;
/* 1005 */       arrayOfInt = new int[paramInt3];
/*      */ 
/* 1008 */       j = paramInt3;
/*      */       do { arrayOfInt[j] = paramArrayOfInt[(i++)];
/*      */ 
/* 1008 */         j--; } while (j >= 0);
/*      */ 
/* 1012 */       paramInt1 = k - paramInt1 - paramInt3;
/* 1013 */       this.ji.setChannel(paramInt1, paramInt2, paramInt3, 1, arrayOfInt, 0, 0);
/* 1014 */       break;
/*      */     case 3:
/* 1018 */       m = this.ji.getHeight();
/* 1019 */       paramInt2 = m - paramInt2 - 1;
/* 1020 */       k = this.ji.getWidth();
/* 1021 */       arrayOfInt = new int[paramInt3];
/* 1022 */       i = paramInt3;
/*      */ 
/* 1025 */       for (j = 0; j < paramInt3; j++) {
/* 1026 */         arrayOfInt[j] = paramArrayOfInt[(--i)];
/*      */       }
/*      */ 
/* 1029 */       paramInt1 = k - paramInt1 - paramInt3;
/* 1030 */       this.ji.setChannel(paramInt1, paramInt2, paramInt3, 1, arrayOfInt, 0, 0);
/* 1031 */       break;
/*      */     case 4:
/* 1035 */       m = this.ji.getHeight();
/* 1036 */       paramInt2 = m - paramInt2 - 1;
/* 1037 */       this.ji.setChannel(paramInt1, paramInt2, paramInt3, 1, paramArrayOfInt, 0, 0);
/* 1038 */       break;
/*      */     case 5:
/* 1043 */       m = this.ji.getWidth();
/* 1044 */       paramInt2 = m - paramInt2 - 1;
/* 1045 */       setChannel_RotateCW90(paramInt1, paramInt2, paramInt3, paramArrayOfInt);
/* 1046 */       break;
/*      */     case 6:
/* 1049 */       setChannel_RotateCW90(paramInt1, paramInt2, paramInt3, paramArrayOfInt);
/* 1050 */       break;
/*      */     case 7:
/* 1055 */       k = this.ji.getHeight();
/* 1056 */       i = 0;
/* 1057 */       arrayOfInt = new int[paramInt3];
/*      */ 
/* 1060 */       j = paramInt3;
/*      */       do { arrayOfInt[j] = paramArrayOfInt[(i++)];
/*      */ 
/* 1060 */         j--; } while (j >= 0);
/*      */ 
/* 1064 */       paramInt1 = k - paramInt1 - paramInt3;
/* 1065 */       setChannel_RotateCW90(paramInt1, paramInt2, paramInt3, arrayOfInt);
/* 1066 */       break;
/*      */     case 8:
/* 1072 */       m = this.ji.getWidth();
/* 1073 */       paramInt2 = m - paramInt2 - 1;
/*      */ 
/* 1076 */       k = this.ji.getHeight();
/* 1077 */       arrayOfInt = new int[paramInt3];
/* 1078 */       i = paramInt3;
/*      */ 
/* 1081 */       for (j = 0; j < paramInt3; j++) {
/* 1082 */         arrayOfInt[j] = paramArrayOfInt[(--i)];
/*      */       }
/*      */ 
/* 1085 */       paramInt1 = k - paramInt1 - paramInt3;
/* 1086 */       setChannel_RotateCW90(paramInt1, paramInt2, paramInt3, arrayOfInt);
/* 1087 */       break;
/*      */     }
/*      */   }
/*      */ 
/*      */   void setChannel_Oriented(int paramInt1, int paramInt2, byte[] paramArrayOfByte)
/*      */     throws JimiException
/*      */   {
/*  959 */     switch (this.orientation)
/*      */     {
/*      */     case 1:
/*  962 */       this.ji.setChannel(paramInt1, paramInt2, paramArrayOfByte);
/*  963 */       break;
/*      */     case 2:
/*      */     case 3:
/*      */     case 4:
/*  968 */       setChannel_Oriented(0, paramInt2, this.ji.getWidth(), paramArrayOfByte);
/*  969 */       break;
/*      */     case 5:
/*      */     case 6:
/*      */     case 7:
/*      */     case 8:
/*  977 */       setChannel_Oriented(0, paramInt2, this.ji.getHeight(), paramArrayOfByte);
/*  978 */       break;
/*      */     }
/*      */   }
/*      */ 
/*      */   void setChannel_Oriented(int paramInt, int[] paramArrayOfInt)
/*      */     throws JimiException
/*      */   {
/* 1105 */     switch (this.orientation)
/*      */     {
/*      */     case 1:
/* 1108 */       this.ji.setChannel(paramInt, paramArrayOfInt);
/* 1109 */       break;
/*      */     case 2:
/*      */     case 3:
/*      */     case 4:
/* 1114 */       setChannel_Oriented(0, paramInt, this.ji.getWidth(), paramArrayOfInt);
/* 1115 */       break;
/*      */     case 5:
/*      */     case 6:
/*      */     case 7:
/*      */     case 8:
/* 1123 */       setChannel_Oriented(0, paramInt, this.ji.getHeight(), paramArrayOfInt);
/* 1124 */       break;
/*      */     }
/*      */   }
/*      */ 
/*      */   final void setChannel_RotateCW90(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte)
/*      */     throws JimiException
/*      */   {
/*  953 */     this.ji.setChannel(0, this.ji.getWidth() - paramInt2 - 1, paramInt1, 1, paramInt3, paramArrayOfByte, 0, 1);
/*      */   }
/*      */ 
/*      */   final void setChannel_RotateCW90(int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt)
/*      */     throws JimiException
/*      */   {
/* 1100 */     this.ji.setChannel(this.ji.getWidth() - paramInt2 - 1, paramInt1, 1, paramInt3, paramArrayOfInt, 0, 1);
/*      */   }
/*      */ 
/*      */   void setPackedChannel_Oriented(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte)
/*      */     throws JimiException
/*      */   {
/*  812 */     if (this.spc_o_ == null)
/*  813 */       this.spc_o_ = new byte[paramArrayOfByte.length * 8];
/*  814 */     JimiUtil.expandOneBitPixels(paramArrayOfByte, this.spc_o_, this.imagewidth);
/*  815 */     setChannel_Oriented(paramInt1, paramInt2, paramInt3, this.spc_o_);
/*      */   }
/*      */ 
/*      */   void setPackedChannel_Oriented(int paramInt, byte[] paramArrayOfByte)
/*      */     throws JimiException
/*      */   {
/*  822 */     if (this.orientation == 1)
/*      */     {
/*  824 */       this.ji.setPackedChannel(paramInt, paramArrayOfByte);
/*      */     }
/*      */     else
/*      */     {
/*  828 */       if (this.spc_o_ == null)
/*  829 */         this.spc_o_ = new byte[paramArrayOfByte.length * 8];
/*  830 */       JimiUtil.expandOneBitPixels(paramArrayOfByte, this.spc_o_, this.imagewidth);
/*  831 */       setChannel_Oriented(0, paramInt, this.spc_o_);
/*      */     }
/*      */   }
/*      */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.decoder.tiff.IFDDecode
 * JD-Core Version:    0.6.2
 */