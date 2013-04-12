/*     */ package com.sun.jimi.core.decoder.tga;
/*     */ 
/*     */ import com.sun.jimi.core.util.LEDataInputStream;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public class TGAFileHeader
/*     */ {
/*     */   int tgaType;
/*     */   int idLength;
/*     */   int colorMapType;
/*     */   int imageType;
/*     */   int firstEntryIndex;
/*     */   int colorMapLength;
/*     */   byte colorMapEntrySize;
/*     */   int xOrigin;
/*     */   int yOrigin;
/*     */   int width;
/*     */   int height;
/*     */   byte pixelDepth;
/*     */   byte imageDescriptor;
/*     */   byte attribPerPixel;
/*     */   boolean leftToRight;
/*     */   boolean topToBottom;
/*     */   byte interleave;
/*     */   byte[] imageIDbuf;
/*     */   String imageID;
/*     */   static final int TYPE_NEW = 0;
/*     */   static final int TYPE_OLD = 1;
/*     */   static final int TYPE_UNK = 2;
/*     */   static final int NO_IMAGE = 0;
/*     */   static final int UCOLORMAPPED = 1;
/*     */   static final int UTRUECOLOR = 2;
/*     */   static final int UBLACKWHITE = 3;
/*     */   static final int COLORMAPPED = 9;
/*     */   static final int TRUECOLOR = 10;
/*     */   static final int BLACKWHITE = 11;
/*     */   static final int ID_ATTRIBPERPIXEL = 15;
/*     */   static final int ID_LEFTTORIGHT = 16;
/*     */   static final int ID_TOPTOBOTTOM = 32;
/*     */   static final int ID_INTERLEAVE = 192;
/*     */   static final int I_NOTINTERLEAVED = 0;
/*     */   static final int I_TWOWAY = 1;
/*     */   static final int I_FOURWAY = 2;
/*     */ 
/*     */   public TGAFileHeader(LEDataInputStream paramLEDataInputStream)
/*     */     throws IOException
/*     */   {
/* 137 */     this.tgaType = 1;
/*     */ 
/* 140 */     this.idLength = paramLEDataInputStream.readUnsignedByte();
/* 141 */     this.colorMapType = paramLEDataInputStream.readUnsignedByte();
/* 142 */     this.imageType = paramLEDataInputStream.readUnsignedByte();
/*     */ 
/* 145 */     this.firstEntryIndex = paramLEDataInputStream.readUnsignedShort();
/* 146 */     this.colorMapLength = paramLEDataInputStream.readUnsignedShort();
/* 147 */     this.colorMapEntrySize = paramLEDataInputStream.readByte();
/*     */ 
/* 150 */     this.xOrigin = paramLEDataInputStream.readUnsignedShort();
/* 151 */     this.yOrigin = paramLEDataInputStream.readUnsignedShort();
/* 152 */     this.width = paramLEDataInputStream.readUnsignedShort();
/* 153 */     this.height = paramLEDataInputStream.readUnsignedShort();
/* 154 */     this.pixelDepth = paramLEDataInputStream.readByte();
/* 155 */     this.imageDescriptor = paramLEDataInputStream.readByte();
/*     */ 
/* 157 */     this.attribPerPixel = ((byte)(this.imageDescriptor & 0xF));
/* 158 */     this.leftToRight = ((this.imageDescriptor & 0x10) != 0);
/* 159 */     this.topToBottom = ((this.imageDescriptor & 0x20) != 0);
/* 160 */     this.interleave = ((byte)((this.imageDescriptor & 0xC0) >> 6));
/*     */ 
/* 162 */     if (this.idLength > 0)
/*     */     {
/* 164 */       this.imageIDbuf = new byte[this.idLength];
/* 165 */       paramLEDataInputStream.read(this.imageIDbuf, 0, this.idLength);
/* 166 */       this.imageID = new String(this.imageIDbuf, 0);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 172 */     return "TGA Header  id length: " + 
/* 173 */       this.idLength + 
/* 174 */       " color map type: " + this.colorMapType + 
/* 175 */       " image type: " + this.imageType + 
/* 176 */       " first entry index: " + this.firstEntryIndex + 
/* 177 */       " color map length: " + this.colorMapLength + 
/* 178 */       " color map entry size: " + this.colorMapEntrySize + 
/* 179 */       " x Origin: " + this.xOrigin + 
/* 180 */       " y Origin: " + this.yOrigin + 
/* 181 */       " width: " + this.width + 
/* 182 */       " height: " + this.height + 
/* 183 */       " pixel depth: " + this.pixelDepth + 
/* 184 */       " image descriptor: " + this.imageDescriptor + (
/* 185 */       this.imageIDbuf == null ? "" : new StringBuffer(" ID String: ").append(this.imageID).toString());
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.decoder.tga.TGAFileHeader
 * JD-Core Version:    0.6.2
 */