/*     */ package com.sun.jimi.core.decoder.pcx;
/*     */ 
/*     */ import com.sun.jimi.core.JimiException;
/*     */ import com.sun.jimi.core.compat.AdaptiveRasterImage;
/*     */ import com.sun.jimi.core.compat.JimiDecoderBase;
/*     */ import com.sun.jimi.core.util.LEDataInputStream;
/*     */ import com.sun.jimi.core.util.ProgressMonitorSupport;
/*     */ import java.awt.image.ColorModel;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.FilterInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ 
/*     */ public class PCXDecoder extends JimiDecoderBase
/*     */ {
/*     */   private AdaptiveRasterImage ji_;
/*     */   private InputStream in_;
/*     */   private LEDataInputStream din_;
/*     */   private int state;
/*     */   private int fileLength;
/*     */   private PCXHeader header_;
/*     */   private byte[] byteScanLine;
/*     */   private ColorModel model_;
/*     */   private PCXImage pi;
/*     */ 
/*     */   public boolean driveDecoder()
/*     */     throws JimiException
/*     */   {
/*     */     try
/*     */     {
/* 121 */       this.header_ = new PCXHeader(this.din_, this.fileLength);
/*     */ 
/* 123 */       this.state |= 2;
/*     */ 
/* 127 */       this.pi = new PCXImage(this.din_, this.header_);
/*     */ 
/* 129 */       loadImage();
/* 130 */       this.ji_.addFullCoverage();
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/* 136 */       this.state |= 1;
/* 137 */       throw new JimiException("IO error reading PCX file");
/*     */     }
/*     */     catch (JimiException localJimiException)
/*     */     {
/* 141 */       this.state |= 1;
/* 142 */       throw localJimiException;
/*     */     }
/* 144 */     return false;
/*     */   }
/*     */ 
/*     */   public void freeDecoder() throws JimiException
/*     */   {
/* 149 */     this.in_ = null;
/* 150 */     this.din_ = null;
/* 151 */     this.ji_ = null;
/*     */   }
/*     */ 
/*     */   public AdaptiveRasterImage getJimiImage()
/*     */   {
/* 162 */     return this.ji_;
/*     */   }
/*     */ 
/*     */   public int getState()
/*     */   {
/* 157 */     return this.state;
/*     */   }
/*     */ 
/*     */   public void initDecoder(InputStream paramInputStream, AdaptiveRasterImage paramAdaptiveRasterImage)
/*     */     throws JimiException
/*     */   {
/*  56 */     this.in_ = paramInputStream;
/*     */ 
/*  58 */     BufferedInputStream localBufferedInputStream = new BufferedInputStream(paramInputStream, 2048);
/*     */ 
/*  60 */     ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
/*  61 */     byte[] arrayOfByte = new byte[1024];
/*     */ 
/*  64 */     int j = 0;
/*     */     try
/*     */     {
/*     */       int i;
/*  68 */       while ((i = localBufferedInputStream.read(arrayOfByte)) != -1)
/*     */       {
/*  70 */         j += i;
/*  71 */         localByteArrayOutputStream.write(arrayOfByte);
/*     */       }
/*     */ 
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/*     */     }
/*     */ 
/*  79 */     this.din_ = new LEDataInputStream(new ByteArrayInputStream(localByteArrayOutputStream.toByteArray()));
/*     */ 
/*  81 */     this.fileLength = j;
/*     */ 
/*  83 */     this.ji_ = paramAdaptiveRasterImage;
/*     */ 
/*  85 */     this.state = 0;
/*     */   }
/*     */ 
/*     */   private void initJimiImage(PCXImage paramPCXImage)
/*     */     throws JimiException
/*     */   {
/* 168 */     this.ji_.setSize(paramPCXImage.getWidth(), paramPCXImage.getHeight());
/*     */ 
/* 172 */     this.ji_.setColorModel(this.header_.getColorModel());
/* 173 */     this.ji_.setPixels();
/*     */   }
/*     */ 
/*     */   protected void loadImage()
/*     */     throws JimiException
/*     */   {
/*     */     try
/*     */     {
/*  92 */       initJimiImage(this.pi);
/*     */ 
/*  94 */       this.ji_.setChannel(0L);
/*     */ 
/*  96 */       for (int i = 0; i < this.pi.getHeight(); i++)
/*     */       {
/*  98 */         int j = i * this.pi.getWidth();
/*  99 */         this.ji_.setChannel(0, i, this.pi.getImageData(), j, this.pi.getWidth());
/* 100 */         setProgress(i * 100 / this.pi.getHeight());
/*     */       }
/*     */ 
/* 103 */       this.state |= 4;
/*     */     }
/*     */     catch (IndexOutOfBoundsException localIndexOutOfBoundsException)
/*     */     {
/* 107 */       this.state |= 1;
/* 108 */       throw new JimiException("No more images");
/*     */     }
/*     */     catch (JimiException localJimiException)
/*     */     {
/* 112 */       this.state |= 1;
/* 113 */       throw localJimiException;
/*     */     }
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.decoder.pcx.PCXDecoder
 * JD-Core Version:    0.6.2
 */