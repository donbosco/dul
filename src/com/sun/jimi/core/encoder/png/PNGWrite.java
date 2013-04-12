/*     */ package com.sun.jimi.core.encoder.png;
/*     */ 
/*     */ import com.sun.jimi.core.JimiException;
/*     */ import com.sun.jimi.core.compat.AdaptiveRasterImage;
/*     */ import com.sun.jimi.core.compat.JimiEncoderBase;
/*     */ import com.sun.jimi.core.util.JimiUtil;
/*     */ import java.awt.image.ColorModel;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.FilterOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.zip.Deflater;
/*     */ 
/*     */ class PNGWrite
/*     */   implements PNGConstants
/*     */ {
/*     */   AdaptiveRasterImage ji;
/*     */   png_chunk_ihdr ihdr;
/*     */   byte filtering;
/*     */   Deflater compress;
/*     */   byte[] zbuf;
/*     */   int zbufO;
/*     */   byte[] scanLine;
/*  46 */   byte[] filter_ = new byte[1];
/*     */   byte[] prevScanLine;
/*     */   PNGChunkUtil pcu;
/*  55 */   byte[] packedBuf_ = null;
/*     */   boolean zeroIsWhite_;
/*     */   PNGEncoder encoder;
/*     */ 
/*     */   PNGWrite(AdaptiveRasterImage paramAdaptiveRasterImage, png_chunk_ihdr parampng_chunk_ihdr, int paramInt, PNGChunkUtil paramPNGChunkUtil, PNGEncoder paramPNGEncoder)
/*     */   {
/*  68 */     this.ji = paramAdaptiveRasterImage;
/*  69 */     this.ihdr = parampng_chunk_ihdr;
/*  70 */     this.pcu = paramPNGChunkUtil;
/*  71 */     this.filtering = paramPNGEncoder.getFilter();
/*  72 */     this.encoder = paramPNGEncoder;
/*     */ 
/*  74 */     this.compress = new Deflater();
/*  75 */     this.zbuf = new byte[paramInt];
/*     */ 
/*  78 */     this.compress.setStrategy(0);
/*     */ 
/*  80 */     this.compress.setLevel(paramPNGEncoder.getCompression());
/*     */ 
/*  84 */     if (parampng_chunk_ihdr.colorType == 2)
/*  85 */       this.scanLine = new byte[paramAdaptiveRasterImage.getWidth() * 3];
/*  86 */     else if (parampng_chunk_ihdr.colorType == 6)
/*  87 */       this.scanLine = new byte[paramAdaptiveRasterImage.getWidth() * 4];
/*  88 */     else if ((parampng_chunk_ihdr.colorType == 0) || 
/*  89 */       (parampng_chunk_ihdr.colorType == 3)) {
/*  90 */       this.scanLine = new byte[paramAdaptiveRasterImage.getWidth()];
/*     */     }
/*  92 */     this.prevScanLine = new byte[this.scanLine.length];
/*     */ 
/*  95 */     this.zeroIsWhite_ = false;
/*  96 */     ColorModel localColorModel = paramAdaptiveRasterImage.getColorModel();
/*     */   }
/*     */ 
/*     */   void png_write_iend(DataOutputStream paramDataOutputStream)
/*     */     throws IOException
/*     */   {
/* 280 */     byte[] arrayOfByte = new byte[1];
/* 281 */     this.pcu.write(paramDataOutputStream, PNGConstants.png_IEND, arrayOfByte, 0);
/*     */   }
/*     */ 
/*     */   void png_write_sbit(DataOutputStream paramDataOutputStream)
/*     */     throws IOException
/*     */   {
/* 291 */     ColorModel localColorModel = this.ji.getColorModel();
/*     */ 
/* 293 */     if ((this.ihdr.colorType == 2) || (this.ihdr.colorType == 6))
/*     */     {
/* 295 */       if (!JimiUtil.isRGBDefault(localColorModel))
/*     */       {
/* 297 */         byte[] arrayOfByte1 = JimiUtil.getChannelWidths(localColorModel);
/* 298 */         byte[] arrayOfByte2 = new byte[4];
/*     */ 
/* 300 */         arrayOfByte2[0] = arrayOfByte1[1];
/* 301 */         arrayOfByte2[1] = arrayOfByte1[2];
/* 302 */         arrayOfByte2[2] = arrayOfByte1[3];
/*     */ 
/* 304 */         if (this.ihdr.colorType == 2)
/*     */         {
/* 306 */           this.pcu.write(paramDataOutputStream, PNGConstants.png_sBIT, arrayOfByte2, 3);
/*     */         }
/* 308 */         else if (this.ihdr.colorType == 6)
/*     */         {
/* 310 */           arrayOfByte2[3] = arrayOfByte1[0];
/* 311 */           this.pcu.write(paramDataOutputStream, PNGConstants.png_sBIT, arrayOfByte2, 4);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   void png_write_sig(DataOutputStream paramDataOutputStream)
/*     */     throws IOException
/*     */   {
/* 274 */     paramDataOutputStream.write(PNGConstants.png_sig);
/*     */   }
/*     */ 
/*     */   void writeFilteredRow(DataOutputStream paramDataOutputStream)
/*     */     throws JimiException, IOException
/*     */   {
/* 186 */     this.compress.setInput(this.filter_, 0, 1);
/*     */     int i;
/*     */     do
/*     */     {
/* 189 */       i = this.compress.deflate(this.zbuf, this.zbufO, this.zbuf.length - this.zbufO);
/* 190 */       this.zbufO += i;
/*     */ 
/* 192 */       if (this.zbufO == this.zbuf.length)
/*     */       {
/* 194 */         this.pcu.write(paramDataOutputStream, PNGConstants.png_IDAT, this.zbuf, this.zbufO);
/* 195 */         this.zbufO = 0;
/*     */       }
/*     */     }
/* 197 */     while (!this.compress.needsInput());
/*     */ 
/* 199 */     switch (this.ihdr.colorType)
/*     */     {
/*     */     case 0:
/*     */     case 3:
/* 204 */       if (this.packedBuf_ == null)
/*     */       {
/* 206 */         i = this.ihdr.width * this.ihdr.bitDepth / 8 + (
/* 207 */           this.ihdr.width * this.ihdr.bitDepth % 8 != 0 ? 1 : 0);
/* 208 */         this.packedBuf_ = new byte[i];
/*     */       }
/* 210 */       JimiUtil.packPixels(this.ihdr.bitDepth, this.scanLine, this.packedBuf_);
/* 211 */       this.compress.setInput(this.packedBuf_, 0, this.packedBuf_.length);
/*     */ 
/* 221 */       break;
/*     */     case 1:
/*     */     case 2:
/*     */     default:
/* 224 */       this.compress.setInput(this.scanLine, 0, this.scanLine.length);
/*     */     }
/*     */ 
/*     */     do
/*     */     {
/* 229 */       i = this.compress.deflate(this.zbuf, this.zbufO, this.zbuf.length - this.zbufO);
/* 230 */       this.zbufO += i;
/*     */ 
/* 232 */       if (this.zbufO == this.zbuf.length)
/*     */       {
/* 234 */         this.pcu.write(paramDataOutputStream, PNGConstants.png_IDAT, this.zbuf, this.zbufO);
/* 235 */         this.zbufO = 0;
/*     */       }
/*     */     }
/* 237 */     while (!this.compress.needsInput());
/*     */ 
/* 240 */     byte[] arrayOfByte = this.prevScanLine;
/* 241 */     this.prevScanLine = this.scanLine;
/* 242 */     this.scanLine = arrayOfByte;
/*     */   }
/*     */ 
/*     */   void writeFinalize(DataOutputStream paramDataOutputStream)
/*     */     throws JimiException, IOException
/*     */   {
/* 251 */     this.compress.finish();
/*     */     do
/*     */     {
/* 256 */       int i = this.compress.deflate(this.zbuf, this.zbufO, this.zbuf.length - this.zbufO);
/* 257 */       this.zbufO += i;
/*     */ 
/* 260 */       if ((this.compress.finished()) || (this.zbufO == this.zbuf.length))
/*     */       {
/* 262 */         this.pcu.write(paramDataOutputStream, PNGConstants.png_IDAT, this.zbuf, this.zbufO);
/* 263 */         this.zbufO = 0;
/*     */       }
/*     */     }
/* 266 */     while (!this.compress.finished());
/*     */ 
/* 268 */     this.compress.reset();
/*     */   }
/*     */ 
/*     */   void writeFindFilter(DataOutputStream paramDataOutputStream)
/*     */     throws JimiException, IOException
/*     */   {
/* 179 */     writeFilteredRow(paramDataOutputStream);
/*     */   }
/*     */ 
/*     */   void writeImageData(DataOutputStream paramDataOutputStream)
/*     */     throws JimiException, IOException
/*     */   {
/* 106 */     if ((this.ihdr.colorType == 2) || (this.ihdr.colorType == 6)) {
/* 107 */       this.ji.setRGBDefault(true);
/*     */     }
/*     */ 
/* 110 */     switch (this.ihdr.interlaceMethod)
/*     */     {
/*     */     case 0:
/* 113 */       writeInterlaceNone(paramDataOutputStream);
/* 114 */       break;
/*     */     case 1:
/* 117 */       writeInterlaceAdam7(paramDataOutputStream);
/* 118 */       break;
/*     */     }
/*     */   }
/*     */ 
/*     */   void writeInterlaceAdam7(DataOutputStream paramDataOutputStream)
/*     */     throws JimiException, IOException
/*     */   {
/* 167 */     throw new JimiException("writeImageData() does not support writing interlace PNGs yet");
/*     */   }
/*     */ 
/*     */   void writeInterlaceNone(DataOutputStream paramDataOutputStream)
/*     */     throws JimiException, IOException
/*     */   {
/* 128 */     this.zbufO = 0;
/*     */ 
/* 130 */     for (int i = 0; i < this.ji.getHeight(); i++)
/*     */     {
/* 132 */       this.filter_[0] = 0;
/* 133 */       switch (this.ihdr.colorType)
/*     */       {
/*     */       case 2:
/* 136 */         this.ji.getChannelRGB(i, this.scanLine, 0);
/* 137 */         break;
/*     */       case 6:
/* 140 */         this.ji.getChannelRGBA(i, this.scanLine, 0);
/* 141 */         break;
/*     */       case 0:
/* 144 */         this.ji.getChannel(0, i, this.scanLine, 0);
/* 145 */         if (this.zeroIsWhite_)
/*     */         {
/* 147 */           int j = this.scanLine.length;
/*     */           do { this.scanLine[j] = ((byte)(this.scanLine[j] ^ 0xFFFFFFFF));
/*     */ 
/* 147 */             j--; } while (j >= 0);
/*     */         }
/*     */ 
/* 150 */         break;
/*     */       case 3:
/* 153 */         this.ji.getChannel(0, i, this.scanLine, 0);
/* 154 */         break;
/*     */       case 1:
/*     */       case 4:
/* 156 */       case 5: } writeFindFilter(paramDataOutputStream);
/* 157 */       this.encoder.setProgress(i * 100 / this.ji.getHeight());
/*     */     }
/*     */ 
/* 160 */     writeFinalize(paramDataOutputStream);
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.encoder.png.PNGWrite
 * JD-Core Version:    0.6.2
 */