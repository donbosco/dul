/*     */ package com.sun.jimi.core.decoder.tiff;
/*     */ 
/*     */ import com.sun.jimi.core.JimiException;
/*     */ import com.sun.jimi.core.UnsupportedFormatException;
/*     */ import com.sun.jimi.core.compat.AdaptiveRasterImage;
/*     */ import com.sun.jimi.core.compat.JimiDecoderBase;
/*     */ import com.sun.jimi.core.util.SeekInputStream;
/*     */ import com.sun.jimi.util.ExpandableArray;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ 
/*     */ public class TIFDecoder extends JimiDecoderBase
/*     */ {
/*  44 */   public final short TIF_SIG_BE = 19789;
/*     */ 
/*  47 */   public final short TIF_SIG_LE = 18761;
/*     */ 
/*  50 */   public final short TIF_VERSION = 42;
/*     */ 
/*  53 */   public final short TIF_HEADER_SIZE = 8;
/*     */   private AdaptiveRasterImage ji;
/*     */   private InputStream in;
/*     */   private InputStream bufIn;
/*     */   private SeekInputStream sis;
/*     */   private int state;
/*     */   private short tifSignature;
/*     */   private short version;
/*     */   private int ifdOffset;
/*     */   int nextIFDoffset;
/*     */   ExpandableArray IFDs;
/*     */ 
/*     */   public void decode(IFD paramIFD)
/*     */     throws JimiException, IOException
/*     */   {
/* 197 */     IFDDecode localIFDDecode = new IFDDecode(this, this.ji, this.sis);
/* 198 */     localIFDDecode.decodeTags(paramIFD);
/* 199 */     localIFDDecode.decodeImage();
/*     */   }
/*     */ 
/*     */   public boolean driveDecoder()
/*     */     throws JimiException
/*     */   {
/*     */     try
/*     */     {
/* 146 */       if (this.nextIFDoffset == 0) {
/* 147 */         throw new JimiException("No more images.");
/*     */       }
/* 149 */       IFD localIFD = new IFD();
/* 150 */       localIFD.read(this.nextIFDoffset, this.sis);
/* 151 */       this.nextIFDoffset = localIFD.offset;
/* 152 */       if (this.nextIFDoffset == 0) {
/* 153 */         this.state &= -9;
/*     */       }
/*     */       else {
/* 156 */         this.state |= 8;
/*     */       }
/* 158 */       this.IFDs.addElement(localIFD);
/*     */       try {
/* 160 */         decode(localIFD); } catch (Exception localException) {
/*     */       }
/* 162 */       this.ji.addFullCoverage();
/* 163 */       this.state = 0;
/* 164 */       this.state |= 2;
/* 165 */       this.state |= 4;
/* 166 */       if (this.nextIFDoffset != 0) {
/* 167 */         this.state |= 8;
/*     */       }
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/* 172 */       throw new JimiException("TIFF IO Error");
/*     */     }
/* 174 */     return false;
/*     */   }
/*     */ 
/*     */   public void freeDecoder() throws JimiException
/*     */   {
/* 179 */     this.in = null;
/* 180 */     this.bufIn = null;
/* 181 */     this.sis = null;
/* 182 */     this.ji = null;
/*     */   }
/*     */ 
/*     */   public int getCapabilities()
/*     */   {
/* 204 */     return 1;
/*     */   }
/*     */ 
/*     */   public AdaptiveRasterImage getJimiImage()
/*     */   {
/* 192 */     return this.ji;
/*     */   }
/*     */ 
/*     */   public int getState()
/*     */   {
/* 187 */     return this.state;
/*     */   }
/*     */ 
/*     */   public void initDecoder(InputStream paramInputStream, AdaptiveRasterImage paramAdaptiveRasterImage)
/*     */     throws JimiException
/*     */   {
/*     */     try
/*     */     {
/*  89 */       this.in = paramInputStream;
/*  90 */       this.bufIn = paramInputStream;
/*  91 */       DataInputStream localDataInputStream = new DataInputStream(this.bufIn);
/*     */ 
/*  94 */       this.tifSignature = localDataInputStream.readShort();
/*     */ 
/*  96 */       if ((this.tifSignature != 18761) && (this.tifSignature != 19789)) {
/*  97 */         throw new UnsupportedFormatException("not a TIFF file");
/*     */       }
/*     */ 
/* 100 */       this.sis = new SeekInputStream(this.tifSignature == 19789, this.bufIn, 2);
/*     */ 
/* 102 */       this.version = this.sis.readShort();
/* 103 */       this.ifdOffset = this.sis.readInt();
/*     */ 
/* 105 */       if ((this.version != 42) || (this.ifdOffset < 8)) {
/* 106 */         throw new UnsupportedFormatException("invalid TIFF Version or TIFF Header size");
/*     */       }
/* 108 */       this.ji = paramAdaptiveRasterImage;
/* 109 */       this.nextIFDoffset = this.ifdOffset;
/* 110 */       this.state = 8;
/* 111 */       this.IFDs = new ExpandableArray();
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/* 115 */       throw new JimiException("IO Error reading TIFF header");
/*     */     }
/*     */   }
/*     */ 
/*     */   public void skipImage()
/*     */     throws JimiException
/*     */   {
/*     */     try
/*     */     {
/* 129 */       IFD localIFD = new IFD();
/* 130 */       localIFD.read(this.nextIFDoffset, this.sis);
/* 131 */       this.nextIFDoffset = localIFD.offset;
/* 132 */       if (this.nextIFDoffset == 0)
/* 133 */         this.state &= -9;
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/* 137 */       throw new JimiException("Read error");
/*     */     }
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.decoder.tiff.TIFDecoder
 * JD-Core Version:    0.6.2
 */