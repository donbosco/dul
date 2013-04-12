/*     */ package com.sun.jimi.core.encoder.png;
/*     */ 
/*     */ import com.sun.jimi.core.JimiException;
/*     */ import com.sun.jimi.core.OptionsObject;
/*     */ import com.sun.jimi.core.compat.AdaptiveRasterImage;
/*     */ import com.sun.jimi.core.compat.JimiEncoderBase;
/*     */ import com.sun.jimi.core.options.PNGOptions;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ 
/*     */ public class PNGEncoder extends JimiEncoderBase
/*     */   implements PNGConstants
/*     */ {
/*     */   private OutputStream out;
/*     */   private BufferedOutputStream bOut;
/*     */   private DataOutputStream dOut;
/*     */   private int state;
/* 206 */   Boolean alpha = null;
/*     */ 
/* 220 */   byte interlace = 0;
/*     */ 
/* 236 */   int compression = 0;
/*     */ 
/*     */   public boolean driveEncoder()
/*     */     throws JimiException
/*     */   {
/* 139 */     AdaptiveRasterImage localAdaptiveRasterImage = getJimiImage();
/*     */ 
/* 141 */     encodePNG(localAdaptiveRasterImage, this.dOut);
/* 142 */     this.state |= 2;
/*     */     try
/*     */     {
/* 147 */       this.dOut.flush();
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/* 151 */       throw new JimiException("IOException");
/*     */     }
/*     */ 
/* 154 */     return false;
/*     */   }
/*     */ 
/*     */   private void encodePNG(AdaptiveRasterImage paramAdaptiveRasterImage, DataOutputStream paramDataOutputStream)
/*     */     throws JimiException
/*     */   {
/*     */     try
/*     */     {
/* 178 */       PNGChunkUtil localPNGChunkUtil = new PNGChunkUtil();
/* 179 */       png_chunk_ihdr localpng_chunk_ihdr = new png_chunk_ihdr(paramAdaptiveRasterImage, localPNGChunkUtil, this);
/* 180 */       png_chunk_plte localpng_chunk_plte = new png_chunk_plte(paramAdaptiveRasterImage, localPNGChunkUtil, this);
/* 181 */       Object localObject = this.alpha != Boolean.FALSE ? 
/* 182 */         new png_chunk_trns(paramAdaptiveRasterImage, localPNGChunkUtil, this) : null;
/*     */ 
/* 184 */       PNGWrite localPNGWrite = new PNGWrite(paramAdaptiveRasterImage, localpng_chunk_ihdr, 8192, localPNGChunkUtil, this);
/* 185 */       localPNGWrite.png_write_sig(paramDataOutputStream);
/* 186 */       localpng_chunk_ihdr.write(paramDataOutputStream);
/* 187 */       localpng_chunk_plte.write(paramDataOutputStream);
/* 188 */       if (this.alpha != Boolean.FALSE)
/* 189 */         //localObject.write(paramDataOutputStream);//TODO: commented for removing compilation error.
/* 190 */       localPNGWrite.png_write_sbit(paramDataOutputStream);
/* 191 */       localPNGWrite.writeImageData(paramDataOutputStream);
/* 192 */       localPNGWrite.png_write_iend(paramDataOutputStream);
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/* 196 */       throw new JimiException("IOException");
/*     */     }
/*     */   }
/*     */ 
/*     */   public void freeEncoder()
/*     */     throws JimiException
/*     */   {
/* 159 */     this.out = null;
/* 160 */     this.bOut = null;
/* 161 */     super.freeEncoder();
/*     */   }
/*     */ 
/*     */   Boolean getAlpha()
/*     */   {
/* 210 */     return this.alpha;
/*     */   }
/*     */ 
/*     */   int getCompression()
/*     */   {
/* 246 */     return this.compression;
/*     */   }
/*     */ 
/*     */   byte getFilter()
/*     */   {
/* 233 */     return 0;
/*     */   }
/*     */ 
/*     */   byte getInterlace()
/*     */   {
/* 223 */     return this.interlace;
/*     */   }
/*     */ 
/*     */   public OptionsObject getOptionsObject()
/*     */   {
/* 202 */     return new PNGOptionsObject(this);
/*     */   }
/*     */ 
/*     */   public int getState()
/*     */   {
/* 166 */     return this.state;
/*     */   }
/*     */ 
/*     */   protected void initSpecificEncoder(OutputStream paramOutputStream, AdaptiveRasterImage paramAdaptiveRasterImage)
/*     */     throws JimiException
/*     */   {
/* 106 */     this.out = paramOutputStream;
/* 107 */     this.bOut = new BufferedOutputStream(paramOutputStream);
/* 108 */     this.dOut = new DataOutputStream(this.bOut);
/* 109 */     this.state = 0;
/*     */ 
/* 112 */     if ((paramAdaptiveRasterImage.getOptions() instanceof PNGOptions)) {
/* 113 */       int i = ((PNGOptions)paramAdaptiveRasterImage.getOptions()).getCompressionType();
/* 114 */       switch (i) {
/*     */       case 0:
/* 116 */         setCompression(0);
/* 117 */         break;
/*     */       case 2:
/* 119 */         setCompression(1);
/* 120 */         break;
/*     */       case 3:
/* 122 */         setCompression(9);
/* 123 */         break;
/*     */       case 1:
/*     */       default:
/* 126 */         setCompression(-1);
/* 127 */         break;
/*     */       }
/*     */     }
/*     */     else {
/* 131 */       setCompression(-1);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setAlpha(Boolean paramBoolean)
/*     */   {
/* 216 */     this.alpha = paramBoolean;
/*     */   }
/*     */ 
/*     */   void setCompression(int paramInt)
/*     */   {
/* 240 */     this.compression = paramInt;
/*     */   }
/*     */ 
/*     */   void setFilter(byte paramByte)
/*     */   {
/*     */   }
/*     */ 
/*     */   void setInterlace(byte paramByte)
/*     */   {
/* 227 */     this.interlace = paramByte;
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.encoder.png.PNGEncoder
 * JD-Core Version:    0.6.2
 */