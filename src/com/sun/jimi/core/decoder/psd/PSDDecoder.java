/*     */ package com.sun.jimi.core.decoder.psd;
/*     */ 
/*     */ import com.sun.jimi.core.JimiException;
/*     */ import com.sun.jimi.core.compat.AdaptiveRasterImage;
/*     */ import com.sun.jimi.core.compat.JimiDecoderBase;
/*     */ import com.sun.jimi.core.util.JimiUtil;
/*     */ import com.sun.jimi.core.util.Packbits;
/*     */ import com.sun.jimi.core.util.ProgressMonitorSupport;
/*     */ import java.awt.image.DirectColorModel;
/*     */ import java.awt.image.IndexColorModel;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ 
/*     */ public class PSDDecoder extends JimiDecoderBase
/*     */ {
/*     */   private AdaptiveRasterImage ji;
/*     */   private InputStream in;
/*     */   private DataInputStream dIn;
/*     */   private int state;
/*     */   PSDFileHeader psdFH;
/*     */   PSDColorMode psdCM;
/*     */   PSDLayersMasks psdLM;
/*     */   PSDImageResources psdIR;
/*     */   short compression;
/*     */   short maxSLLen_;
/*     */   short[] scanlineLengths_;
/*     */ 
/*     */   private void decodeCompressedBitmapChannel()
/*     */     throws JimiException, IOException
/*     */   {
/* 380 */     byte[] arrayOfByte1 = new byte[this.maxSLLen_];
/* 381 */     byte[] arrayOfByte2 = new byte[this.psdFH.columns / 8 + (this.psdFH.columns % 8 != 0 ? 1 : 0)];
/* 382 */     byte[] arrayOfByte3 = new byte[this.psdFH.columns];
/* 383 */     for (int i = 0; i < this.psdFH.rows; i++)
/*     */     {
/* 385 */       this.dIn.readFully(arrayOfByte1, 0, this.scanlineLengths_[i]);
/* 386 */       Packbits.unpackbits(arrayOfByte1, arrayOfByte2);
/*     */ 
/* 390 */       int j = arrayOfByte2.length;
/*     */       do { arrayOfByte2[j] = ((byte)(arrayOfByte2[j] ^ 0xFFFFFFFF));
/*     */ 
/* 390 */         j--; } while (j >= 0);
/*     */ 
/* 393 */       JimiUtil.expandPixels(1, arrayOfByte2, arrayOfByte3, arrayOfByte3.length);
/* 394 */       this.ji.setChannel(0, i, arrayOfByte3);
/* 395 */       setProgress(i * 100 / this.psdFH.rows);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void decodeCompressedChannel()
/*     */     throws JimiException, IOException
/*     */   {
/* 413 */     byte[] arrayOfByte2 = new byte[this.psdFH.columns];
/* 414 */     byte[] arrayOfByte1 = new byte[this.maxSLLen_];
/*     */ 
/* 416 */     for (int i = 0; i < this.psdFH.rows; i++)
/*     */     {
/* 418 */       this.dIn.readFully(arrayOfByte1, 0, this.scanlineLengths_[i]);
/* 419 */       Packbits.unpackbits(arrayOfByte1, arrayOfByte2);
/* 420 */       this.ji.setChannel(0, i, arrayOfByte2);
/* 421 */       setProgress(i * 100 / this.psdFH.rows);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void decodeCompressedRGB()
/*     */     throws JimiException, IOException
/*     */   {
/* 288 */     byte[] arrayOfByte2 = new byte[this.psdFH.columns];
/* 289 */     byte[] arrayOfByte1 = new byte[this.maxSLLen_];
/*     */ 
/* 291 */     int j = this.psdFH.rows * 3;
/* 292 */     int k = this.psdFH.rows;
/*     */ int i = 0;
/* 294 */     for ( i = 0; i < this.psdFH.rows; i++)
/*     */     {
/* 296 */       this.dIn.readFully(arrayOfByte1, 0, this.scanlineLengths_[i]);
/* 297 */       Packbits.unpackbits(arrayOfByte1, arrayOfByte2);
/* 298 */       this.ji.setChannel(16, i, arrayOfByte2);
/* 299 */       setProgress(i * 100 / j);
/*     */     }
			
/* 301 */     for (i = 0; i < this.psdFH.rows; i++)
/*     */     {
/* 303 */       this.dIn.readFully(arrayOfByte1, 0, this.scanlineLengths_[(i + this.psdFH.rows)]);
/* 304 */       Packbits.unpackbits(arrayOfByte1, arrayOfByte2);
/* 305 */       this.ji.setChannel(8, i, arrayOfByte2);
/* 306 */       setProgress((k + i) * 100 / j);
/*     */     }
/*     */ 
/* 309 */     for (i = 0; i < this.psdFH.rows; i++)
/*     */     {
/* 311 */       this.dIn.readFully(arrayOfByte1, 0, this.scanlineLengths_[(i + 2 * this.psdFH.rows)]);
/* 312 */       Packbits.unpackbits(arrayOfByte1, arrayOfByte2);
/* 313 */       this.ji.setChannel(0, i, arrayOfByte2);
/* 314 */       setProgress((k * 2 + i) * 100 / j);
/*     */     }
/*     */ 
/* 318 */     if (this.psdFH.channels >= 4)
/* 319 */       for (i = 0; i < this.psdFH.rows; i++) {
/* 320 */         this.dIn.readFully(arrayOfByte1, 0, this.scanlineLengths_[(i + 3 * this.psdFH.rows)]);
/* 321 */         Packbits.unpackbits(arrayOfByte1, arrayOfByte2);
/* 322 */         this.ji.setChannel(24, i, arrayOfByte2);
/* 323 */         setProgress((k * 3 + i) * 100 / j);
/*     */       }
/*     */   }
/*     */ 
/*     */   void decodeImage()
/*     */     throws JimiException, IOException
/*     */   {
/* 180 */     this.compression = this.dIn.readShort();
/* 181 */     if ((this.compression != 1) && (this.compression != 0)) {
/* 182 */       throw new JimiException("PSDDecoder invalid compression code " + this.compression);
/*     */     }
/* 184 */     switch (this.psdFH.mode)
/*     */     {
/*     */     case 3:
/* 187 */       if (this.compression == 0) {
/* 188 */         decodeRawRGB();
/*     */       }
/*     */       else {
/* 191 */         decodeSLLData();
/* 192 */         decodeCompressedRGB();
/*     */       }
/* 194 */       break;
/*     */     case 2:
/* 198 */       if (this.compression == 0) {
/* 199 */         decodeRawChannel();
/*     */       }
/*     */       else {
/* 202 */         decodeSLLData();
/* 203 */         decodeCompressedChannel();
/*     */       }
/* 205 */       break;
/*     */     case 1:
/* 209 */       if (this.compression == 0) {
/* 210 */         decodeRawChannel();
/*     */       }
/*     */       else {
/* 213 */         decodeSLLData();
/* 214 */         decodeCompressedChannel();
/*     */       }
/* 216 */       break;
/*     */     case 0:
/* 220 */       if (this.compression == 0) {
/* 221 */         decodeRawBitmapChannel();
/*     */       }
/*     */       else {
/* 224 */         decodeSLLData();
/* 225 */         decodeCompressedBitmapChannel();
/*     */       }
/* 227 */       break;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void decodeRawBitmapChannel()
/*     */     throws JimiException, IOException
/*     */   {
/* 354 */     byte[] arrayOfByte1 = new byte[this.psdFH.columns / 8 + (this.psdFH.columns % 8 != 0 ? 1 : 0)];
/* 355 */     byte[] arrayOfByte2 = new byte[this.psdFH.columns];
/* 356 */     for (int i = 0; i < this.psdFH.rows; i++)
/*     */     {
/* 358 */       this.dIn.readFully(arrayOfByte1);
/*     */ 
/* 362 */       int j = arrayOfByte1.length;
/*     */       do { arrayOfByte1[j] = ((byte)(arrayOfByte1[j] ^ 0xFFFFFFFF));
/*     */ 
/* 362 */         j--; } while (j >= 0);
/*     */ 
/* 365 */       JimiUtil.expandPixels(1, arrayOfByte1, arrayOfByte2, arrayOfByte2.length);
/* 366 */       this.ji.setChannel(0, i, arrayOfByte2);
/* 367 */       setProgress(i * 100 / this.psdFH.rows);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void decodeRawChannel()
/*     */     throws JimiException, IOException
/*     */   {
/* 338 */     byte[] arrayOfByte = new byte[this.psdFH.columns];
/* 339 */     for (int i = 0; i < this.psdFH.rows; i++)
/*     */     {
/* 341 */       this.dIn.readFully(arrayOfByte);
/* 342 */       this.ji.setChannel(0, i, arrayOfByte);
/* 343 */       setProgress(i * 100 / this.psdFH.rows);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void decodeRawRGB()
/*     */     throws JimiException, IOException
/*     */   {
/* 241 */     byte[] arrayOfByte = new byte[this.psdFH.columns];
/*     */ 
/* 243 */     int j = this.psdFH.rows * Math.min(4, this.psdFH.channels);
/* 244 */     int k = this.psdFH.rows;
/* 245 */     int i = 0;
			  for (i = 0; i < this.psdFH.rows; i++)
/*     */     {
/* 247 */       this.dIn.readFully(arrayOfByte);
/* 248 */       this.ji.setChannel(16, i, arrayOfByte);
/* 249 */       setProgress(i * 100 / j);
/*     */     }
/* 251 */     for (i = 0; i < this.psdFH.rows; i++)
/*     */     {
/* 253 */       this.dIn.readFully(arrayOfByte);
/* 254 */       this.ji.setChannel(8, i, arrayOfByte);
/* 255 */       setProgress((k + i) * 100 / j);
/*     */     }
/*     */ 
/* 258 */     for (i = 0; i < this.psdFH.rows; i++)
/*     */     {
/* 260 */       this.dIn.readFully(arrayOfByte);
/* 261 */       this.ji.setChannel(0, i, arrayOfByte);
/* 262 */       setProgress((k * 2 + i) * 100 / j);
/*     */     }
/*     */ 
/* 265 */     if (this.psdFH.channels >= 4)
/* 266 */       for (i = 0; i < this.psdFH.rows; i++)
/*     */       {
/* 268 */         this.dIn.readFully(arrayOfByte);
/*     */ 
/* 270 */         this.ji.setChannel(24, i, arrayOfByte);
/* 271 */         setProgress((k * 3 + i) * 100 / j);
/*     */       }
/*     */   }
/*     */ 
/*     */   private void decodeSLLData()
/*     */     throws IOException
/*     */   {
/* 435 */     this.scanlineLengths_ = new short[this.psdFH.rows * this.psdFH.channels];
/* 436 */     this.maxSLLen_ = -1;
/* 437 */     for (int i = 0; i < this.psdFH.rows * this.psdFH.channels; i++)
/*     */     {
/* 439 */       int j = this.dIn.readShort();
/* 440 */       this.scanlineLengths_[i] = (short) j;
/* 441 */       this.maxSLLen_ = (short) (j > this.maxSLLen_ ? j : this.maxSLLen_);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean driveDecoder()
/*     */     throws JimiException
/*     */   {
/*     */     try
/*     */     {
/*  76 */       this.psdFH = new PSDFileHeader(this.dIn);
/*  77 */       this.psdCM = new PSDColorMode(this.dIn, this.psdFH);
/*  78 */       initJimiImage();
/*  79 */       this.state |= 2;
/*     */ 
/*  82 */       this.psdIR = new PSDImageResources(this.dIn);
/*  83 */       this.psdLM = new PSDLayersMasks(this.dIn);
/*     */ 
/*  85 */       decodeImage();
/*  86 */       this.ji.addFullCoverage();
/*  87 */       this.state |= 4;
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/*  91 */       throw new JimiException(localIOException.getMessage());
/*     */     }
/*     */ 
/*  94 */     return false;
/*     */   }
/*     */ 
/*     */   public void freeDecoder() throws JimiException
/*     */   {
/*  99 */     this.in = null;
/* 100 */     this.ji = null;
/*     */   }
/*     */ 
/*     */   public AdaptiveRasterImage getJimiImage()
/*     */   {
/* 111 */     return this.ji;
/*     */   }
/*     */ 
/*     */   public int getState()
/*     */   {
/* 106 */     return this.state;
/*     */   }
/*     */ 
/*     */   public void initDecoder(InputStream paramInputStream, AdaptiveRasterImage paramAdaptiveRasterImage)
/*     */     throws JimiException
/*     */   {
/*  65 */     this.in = paramInputStream;
/*     */ 
/*  67 */     this.dIn = new DataInputStream(new BufferedInputStream(paramInputStream));
/*  68 */     this.ji = paramAdaptiveRasterImage;
/*  69 */     this.state = 0;
/*     */   }
/*     */ 
/*     */   private void initJimiImage()
/*     */     throws JimiException
/*     */   {
/* 124 */     if ((this.psdFH.signature != 943870035) || (this.psdFH.version != 1)) {
/* 125 */       throw new JimiException("PSDDecoder invalid PSD file format");
/*     */     }
/* 127 */     if (((this.psdFH.mode == 0) && (this.psdFH.depth != 1)) || (
/* 128 */       (this.psdFH.mode != 0) && (this.psdFH.depth != 8))) {
/* 129 */       throw new JimiException("Unsupported depth for mode file format. mode = " + this.psdFH.mode + " depth = " + this.psdFH.depth);
/*     */     }
/* 131 */     this.ji.setSize(this.psdFH.columns, this.psdFH.rows);
/*     */ 
/* 133 */     switch (this.psdFH.mode)
/*     */     {
/*     */     case 3:
/* 136 */       this.ji.setColorModel(new DirectColorModel(24, 16711680, 65280, 255));
/*     */ 
/* 146 */       this.ji.setPixels();
/* 147 */       this.ji.setChannel(-1048576L);
/* 148 */       break;
/*     */     case 2:
/* 151 */       if (this.psdFH.channels != 1)
/* 152 */         throw new JimiException("PSDDecoder INDEXED mode files must have 1 channel");
/* 153 */       if (this.psdCM.length == 0)
/* 154 */         throw new JimiException("PSDDecoder INDEXED mode files require a color mode section");
/* 155 */       this.ji.setColorModel(new IndexColorModel(8, this.psdCM.cmap.length / 3, this.psdCM.cmap, 0, false));
/* 156 */       this.ji.setPixels();
/* 157 */       break;
/*     */     case 1:
/* 160 */       if (this.psdFH.channels != 1)
/* 161 */         throw new JimiException("PSDDecoder GRAYSCALE mode files must have 1 channel");
/* 162 */       this.ji.setColorModel(new DirectColorModel(8, 255, 255, 255));
/* 163 */       this.ji.setPixels();
/* 164 */       break;
/*     */     case 0:
/* 167 */       if ((this.psdFH.channels != 1) || (this.psdFH.depth != 1))
/* 168 */         throw new JimiException("PSDDecoder BITMAP must be 1 channel and 1 bit depth");
/* 169 */       this.ji.setColorModel(new DirectColorModel(1, 1, 1, 1));
/* 170 */       this.ji.setPixels();
/* 171 */       break;
/*     */     default:
/* 174 */       throw new JimiException("PSDDecoder unsupported image mode format " + this.psdFH.mode);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean usesChanneledData()
/*     */   {
/* 447 */     return true;
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.decoder.psd.PSDDecoder
 * JD-Core Version:    0.6.2
 */