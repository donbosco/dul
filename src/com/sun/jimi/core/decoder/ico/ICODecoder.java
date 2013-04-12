/*     */ package com.sun.jimi.core.decoder.ico;
/*     */ 
/*     */ import com.sun.jimi.core.JimiException;
/*     */ import com.sun.jimi.core.compat.AdaptiveRasterImage;
/*     */ import com.sun.jimi.core.compat.JimiDecoderBase;
/*     */ import com.sun.jimi.core.util.LEDataInputStream;
/*     */ import com.sun.jimi.core.util.ProgressMonitorSupport;
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.IndexColorModel;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.FilterInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ 
/*     */ public class ICODecoder extends JimiDecoderBase
/*     */ {
/*     */   protected static final int STREAM_BUFFER_SIZE = 10240;
/*     */   private AdaptiveRasterImage ji_;
/*     */   private InputStream in_;
/*     */   private LEDataInputStream din_;
/*     */   private int state;
/*  46 */   private int numberOfImages_ = -1;
/*     */   private int currentImage_;
/*     */   private IconDir iconDir_;
/*     */   private byte[] byteScanLine;
/*     */   private ColorModel model_;
/*     */   private IconImage[] ii_;
/*     */ 
/*     */   public boolean driveDecoder()
/*     */     throws JimiException
/*     */   {
/*     */     try
/*     */     {
/* 195 */       loadImage();
/* 196 */       this.ji_.addFullCoverage();
/* 197 */       this.state |= 6;
/*     */     }
/*     */     catch (JimiException localJimiException)
/*     */     {
/* 202 */       this.state |= 1;
/* 203 */       throw localJimiException;
/*     */     }
/*     */ 
/* 206 */     return false;
/*     */   }
/*     */ 
/*     */   public void freeDecoder() throws JimiException
/*     */   {
/* 211 */     this.in_ = null;
/* 212 */     this.din_ = null;
/* 213 */     this.ji_ = null;
/*     */   }
/*     */ 
/*     */   public int getCapabilities()
/*     */   {
/* 112 */     return 1;
/*     */   }
/*     */ 
/*     */   public AdaptiveRasterImage getJimiImage()
/*     */   {
/* 224 */     return this.ji_;
/*     */   }
/*     */ 
/*     */   public int getNumberOfImages()
/*     */   {
/* 104 */     return this.numberOfImages_;
/*     */   }
/*     */ 
/*     */   public int getState()
/*     */   {
/* 219 */     return this.state;
/*     */   }
/*     */ 
/*     */   public void initDecoder(InputStream paramInputStream, AdaptiveRasterImage paramAdaptiveRasterImage)
/*     */     throws JimiException
/*     */   {
/*  57 */     this.state = 8;
/*     */ 
/*  59 */     this.in_ = paramInputStream;
/*     */ 
/*  61 */     BufferedInputStream localBufferedInputStream = new BufferedInputStream(paramInputStream, 2048);
/*     */ 
/*  63 */     ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
/*  64 */     byte[] arrayOfByte = new byte[10240];
/*     */     try
/*     */     {
/*  68 */       while (localBufferedInputStream.read(arrayOfByte) != -1) {
/*  69 */         localByteArrayOutputStream.write(arrayOfByte);
/*     */       }
/*     */     }
/*     */     catch (IOException localIOException1)
/*     */     {
/*     */     }
/*  75 */     this.din_ = new LEDataInputStream(new ByteArrayInputStream(localByteArrayOutputStream.toByteArray()));
/*     */ 
/*  77 */     this.din_.mark(arrayOfByte.length);
/*     */ 
/*  79 */     this.ji_ = paramAdaptiveRasterImage;
/*     */     try
/*     */     {
/*  85 */       this.iconDir_ = new IconDir(this.din_);
/*  86 */       this.numberOfImages_ = this.iconDir_.getCount();
/*  87 */       this.ii_ = new IconImage[this.numberOfImages_];
/*  88 */       this.state |= 2;
/*     */ 
/*  91 */       this.currentImage_ = 0;
/*     */ 
/*  93 */       if (this.numberOfImages_ > 0) this.state |= 8;
/*     */     }
/*     */     catch (IOException localIOException2)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   private void initJimiImage(IconImage paramIconImage)
/*     */     throws JimiException
/*     */   {
/* 231 */     this.ji_.setSize(paramIconImage.getWidth(), paramIconImage.getHeight());
/*     */ 
/* 235 */     RGBQuad[] arrayOfRGBQuad = paramIconImage.getColors();
/*     */ 
/* 239 */     byte[] arrayOfByte1 = new byte[arrayOfRGBQuad.length];
/* 240 */     byte[] arrayOfByte2 = new byte[arrayOfRGBQuad.length];
/* 241 */     byte[] arrayOfByte3 = new byte[arrayOfRGBQuad.length];
/*     */ 
/* 243 */     for (int i = 0; i < arrayOfRGBQuad.length; i++)
/*     */     {
/* 245 */       arrayOfByte1[i] = ((byte)arrayOfRGBQuad[i].getRed());
/* 246 */       arrayOfByte2[i] = ((byte)arrayOfRGBQuad[i].getGreen());
/* 247 */       arrayOfByte3[i] = ((byte)arrayOfRGBQuad[i].getBlue());
/*     */     }
/*     */ 
/* 250 */     this.model_ = new IndexColorModel(8, arrayOfByte1.length, arrayOfByte1, arrayOfByte2, arrayOfByte3);
/*     */ 
/* 252 */     this.ji_.setColorModel(this.model_);
/* 253 */     this.ji_.setPixels();
/*     */   }
/*     */ 
/*     */   protected void loadImage()
/*     */     throws JimiException
/*     */   {
/*     */     try
/*     */     {
/* 140 */       if (this.ii_[this.currentImage_] == null)
/*     */       {
/*     */         try
/*     */         {
/* 146 */           this.din_.reset();
/*     */ 
/* 149 */           this.din_.skip(this.iconDir_.getEntry(this.currentImage_).getImageOffset());
/*     */ 
/* 152 */           this.ii_[this.currentImage_] = new IconImage(this.din_);
/*     */         }
/*     */         catch (IOException localIOException)
/*     */         {
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 160 */       initJimiImage(this.ii_[this.currentImage_]);
/*     */ 
/* 162 */       this.ji_.setChannel(0L);
/*     */ 
/* 164 */       for (int i = 0; i < this.ii_[this.currentImage_].getHeight(); i++)
/*     */       {
/* 166 */         this.ji_.setChannel(0, this.ii_[this.currentImage_].getHeight() - 1 - i, this.ii_[this.currentImage_].getXORMap(), i * this.ii_[this.currentImage_].getWidth(), this.ii_[this.currentImage_].getWidth());
/* 167 */         setProgress(i * 100 / this.ii_[this.currentImage_].getHeight());
/*     */       }
/*     */ 
/* 171 */       this.state |= 4;
/* 172 */       this.currentImage_ += 1;
/* 173 */       if (this.currentImage_ >= this.numberOfImages_)
/*     */       {
/* 175 */         this.state ^= 8;
/*     */       }
/*     */     }
/*     */     catch (IndexOutOfBoundsException localIndexOutOfBoundsException)
/*     */     {
/* 180 */       this.state |= 1;
/* 181 */       throw new JimiException("No more images");
/*     */     }
/*     */     catch (JimiException localJimiException)
/*     */     {
/* 185 */       this.state |= 1;
/* 186 */       throw localJimiException;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setJimiImage(AdaptiveRasterImage paramAdaptiveRasterImage)
/*     */   {
/* 120 */     this.ji_ = paramAdaptiveRasterImage;
/*     */   }
/*     */ 
/*     */   public void skipImage()
/*     */     throws JimiException
/*     */   {
/* 129 */     this.currentImage_ += 1;
/* 130 */     if (this.currentImage_ >= this.numberOfImages_)
/*     */     {
/* 132 */       this.state ^= 8;
/*     */     }
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.decoder.ico.ICODecoder
 * JD-Core Version:    0.6.2
 */