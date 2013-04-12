/*     */ package com.sun.jimi.core.decoder.bmp;
/*     */ 
/*     */ import com.sun.jimi.core.JimiException;
/*     */ import com.sun.jimi.core.util.LEDataInputStream;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public class BMPFileHeader
/*     */ {
/*     */   static final short FILETYPE_BM = 19778;
/*     */   static final short VERSION_2X = 12;
/*     */   static final short VERSION_3X = 40;
/*     */   static final short VERSION_4X = 108;
/*     */   LEDataInputStream in_;
/*  41 */   short fileType = 19778;
/*     */   int fileSize;
/*  43 */   short reserved1 = 0;
/*  44 */   short reserved2 = 0;
/*     */   int bitmapOffset;
/*     */   int size;
/*     */   int width;
/*     */   int height;
/*     */   int planes;
/*     */   int bitsPerPixel;
/*     */   int compression;
/*     */   int sizeOfBitmap;
/*     */   int horzResolution;
/*     */   int vertResolution;
/*     */   int colorsUsed;
/*     */   int colorsImportant;
/*     */   int redMask;
/*     */   int greenMask;
/*     */   int blueMask;
/*     */   int alphaMask;
/*     */   int csType;
/*     */   int redX;
/*     */   int redY;
/*     */   int redZ;
/*     */   int greenX;
/*     */   int greenY;
/*     */   int greenZ;
/*     */   int blueX;
/*     */   int blueY;
/*     */   int blueZ;
/*     */   int gammaRed;
/*     */   int gammaGreen;
/*     */   int gammaBlue;
/*     */   boolean topDown;
/*     */   int actualSizeOfBitmap;
/*     */   int scanLineSize;
/*     */   int actualColorsUsed;
/*     */   int noOfPixels;
/*     */   int bmpVersion;
/*     */ 
/*     */   public BMPFileHeader(LEDataInputStream paramLEDataInputStream)
/*     */     throws IOException, JimiException
/*     */   {
/* 186 */     this.in_ = paramLEDataInputStream;
/*     */ 
/* 188 */     this.fileType = this.in_.readShort();
/*     */ 
/* 190 */     if (this.fileType != 19778) {
/* 191 */       throw new JimiException("Not a BMP file");
/*     */     }
/* 193 */     this.fileSize = this.in_.readInt();
/* 194 */     this.reserved1 = this.in_.readShort();
/* 195 */     this.reserved2 = this.in_.readShort();
/* 196 */     this.bitmapOffset = this.in_.readInt();
/*     */ 
/* 198 */     this.size = this.in_.readInt();
/* 199 */     if (this.size == 12)
/* 200 */       readVersion2x();
/* 201 */     else if (this.size == 40)
/* 202 */       readVersion3x();
/* 203 */     else if (this.size == 108)
/* 204 */       readVersion4x();
/*     */     else {
/* 206 */       throw new JimiException("Unsupported BMP version " + this.size);
/*     */     }
/*     */ 
/* 211 */     if (this.topDown) {
/* 212 */       throw new JimiException("Unsupported topdown BMPs");
/*     */     }
/* 214 */     this.noOfPixels = (this.width * this.height);
/*     */ 
/* 219 */     this.scanLineSize = ((this.width * this.bitsPerPixel + 31) / 32 * 4);
/*     */ 
/* 221 */     if (this.sizeOfBitmap != 0) {
/* 222 */       this.actualSizeOfBitmap = this.sizeOfBitmap;
/*     */     }
/*     */     else {
/* 225 */       this.actualSizeOfBitmap = (this.scanLineSize * this.height);
/*     */     }
/* 227 */     if (this.colorsUsed != 0)
/*     */     {
/* 229 */       this.actualColorsUsed = this.colorsUsed;
/*     */     }
/* 234 */     else if (this.bitsPerPixel < 16)
/*     */     {
/* 236 */       this.actualColorsUsed = (1 << this.bitsPerPixel);
/*     */     }
/*     */     else
/*     */     {
/* 240 */       this.actualColorsUsed = 0;
/*     */     }
/*     */   }
/*     */ 
/*     */   void readVersion2x()
/*     */     throws IOException
/*     */   {
/*  94 */     this.width = this.in_.readShort();
/*  95 */     this.height = this.in_.readShort();
/*  96 */     this.planes = this.in_.readUnsignedShort();
/*  97 */     this.bitsPerPixel = this.in_.readUnsignedShort();
/*     */ 
/*  99 */     this.compression = 0;
/* 100 */     this.bmpVersion = 2;
/* 101 */     this.topDown = (this.height < 0);
/*     */   }
/*     */ 
/*     */   void readVersion3x()
/*     */     throws IOException
/*     */   {
/* 112 */     this.width = this.in_.readInt();
/* 113 */     this.height = this.in_.readInt();
/* 114 */     this.planes = this.in_.readUnsignedShort();
/* 115 */     this.bitsPerPixel = this.in_.readUnsignedShort();
/*     */ 
/* 117 */     this.compression = this.in_.readInt();
/* 118 */     this.sizeOfBitmap = this.in_.readInt();
/* 119 */     this.horzResolution = this.in_.readInt();
/* 120 */     this.vertResolution = this.in_.readInt();
/* 121 */     this.colorsUsed = this.in_.readInt();
/* 122 */     this.colorsImportant = this.in_.readInt();
/*     */ 
/* 124 */     if (this.compression == 3)
/*     */     {
/* 126 */       this.redMask = this.in_.readInt();
/* 127 */       this.greenMask = this.in_.readInt();
/* 128 */       this.blueMask = this.in_.readInt();
/*     */     }
/* 133 */     else if (this.bitsPerPixel == 16)
/*     */     {
/* 135 */       this.redMask = 31744;
/* 136 */       this.greenMask = 992;
/* 137 */       this.blueMask = 31;
/* 138 */       this.alphaMask = 0;
/*     */     }
/*     */ 
/* 142 */     this.bmpVersion = 3;
/* 143 */     this.topDown = (this.height < 0);
/*     */   }
/*     */ 
/*     */   void readVersion4x() throws IOException
/*     */   {
/* 148 */     this.width = this.in_.readInt();
/* 149 */     this.height = this.in_.readInt();
/* 150 */     this.planes = this.in_.readUnsignedShort();
/* 151 */     this.bitsPerPixel = this.in_.readUnsignedShort();
/*     */ 
/* 153 */     this.compression = this.in_.readInt();
/* 154 */     this.sizeOfBitmap = this.in_.readInt();
/* 155 */     this.horzResolution = this.in_.readInt();
/* 156 */     this.vertResolution = this.in_.readInt();
/* 157 */     this.colorsUsed = this.in_.readInt();
/* 158 */     this.colorsImportant = this.in_.readInt();
/*     */ 
/* 160 */     this.redMask = this.in_.readInt();
/* 161 */     this.greenMask = this.in_.readInt();
/* 162 */     this.blueMask = this.in_.readInt();
/* 163 */     this.alphaMask = this.in_.readInt();
/* 164 */     this.csType = this.in_.readInt();
/* 165 */     this.redX = this.in_.readInt();
/* 166 */     this.redY = this.in_.readInt();
/* 167 */     this.redZ = this.in_.readInt();
/* 168 */     this.greenX = this.in_.readInt();
/* 169 */     this.greenY = this.in_.readInt();
/* 170 */     this.greenZ = this.in_.readInt();
/* 171 */     this.blueX = this.in_.readInt();
/* 172 */     this.blueY = this.in_.readInt();
/* 173 */     this.blueZ = this.in_.readInt();
/* 174 */     this.gammaRed = this.in_.readInt();
/* 175 */     this.gammaGreen = this.in_.readInt();
/* 176 */     this.gammaBlue = this.in_.readInt();
/*     */ 
/* 178 */     this.bmpVersion = 4;
/* 179 */     this.topDown = (this.height < 0);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 247 */     return "BMP Header  size=" + 
/* 248 */       this.size + 
/* 249 */       " width=" + this.width + 
/* 250 */       " height=" + this.height + 
/* 251 */       " planes=" + this.planes + 
/* 252 */       " bitsPerPixel=" + this.bitsPerPixel + 
/* 253 */       " compression=" + this.compression + 
/* 254 */       " sizeOfBitmap=" + this.sizeOfBitmap + 
/* 255 */       " actualSizeOfBitmap=" + this.actualSizeOfBitmap + 
/* 256 */       " scanLineSize=" + this.scanLineSize + 
/* 257 */       " horzResolution=" + this.horzResolution + 
/* 258 */       " vertResolution=" + this.vertResolution + 
/* 259 */       " colorsUsed=" + this.colorsUsed + 
/* 260 */       " colorsImportant=" + this.colorsImportant;
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.decoder.bmp.BMPFileHeader
 * JD-Core Version:    0.6.2
 */