/*     */ package com.sun.jimi.core.decoder.cur;
/*     */ 
/*     */ import com.sun.jimi.core.JimiException;
/*     */ import com.sun.jimi.core.compat.AdaptiveRasterImage;
/*     */ import com.sun.jimi.core.compat.JimiDecoderBase;
/*     */ import com.sun.jimi.core.decoder.ico.IconDir;
/*     */ import com.sun.jimi.core.decoder.ico.IconDirEntry;
/*     */ import com.sun.jimi.core.decoder.ico.IconImage;
/*     */ import com.sun.jimi.core.decoder.ico.RGBQuad;
/*     */ import com.sun.jimi.core.util.LEDataInputStream;
/*     */ import com.sun.jimi.core.util.ProgressMonitorSupport;
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.IndexColorModel;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.FilterInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ 
/*     */ public class CURDecoder extends JimiDecoderBase
/*     */ {
/*     */   private AdaptiveRasterImage ji_;
/*     */   private InputStream in_;
/*     */   private LEDataInputStream din_;
/*     */   private int state;
/*     */   private IconDir curDir_;
/*     */   private byte[] byteScanLine;
/*     */   private ColorModel model_;
/*     */ 
/*     */   public boolean driveDecoder()
/*     */     throws JimiException
/*     */   {
/*     */     try
/*     */     {
/*  77 */       this.curDir_ = new IconDir(this.din_);
/*     */ 
/*  79 */       this.state |= 2;
/*     */ 
/*  83 */       IconImage[] arrayOfIconImage = new IconImage[this.curDir_.getCount()];
/*     */ 
/*  85 */       for (int i = 0; i < arrayOfIconImage.length; i++)
/*     */       {
/*  88 */         this.din_.reset();
/*     */ 
/*  91 */         this.din_.skip(this.curDir_.getEntry(i).getImageOffset());
/*     */ 
/*  93 */         arrayOfIconImage[i] = new IconImage(this.din_);
/*     */       }
/*     */ 
/*  96 */       initJimiImage(arrayOfIconImage[0]);
/*     */ 
/*  98 */       this.ji_.setChannel(0L);
/*     */ 
/* 100 */       for (int j = 0; j < arrayOfIconImage[0].getHeight(); j++)
/*     */       {
/* 102 */         this.ji_.setChannel(0, arrayOfIconImage[0].getHeight() - 1 - j, arrayOfIconImage[0].getXORMap(), j * arrayOfIconImage[0].getWidth(), arrayOfIconImage[0].getWidth());
/* 103 */         setProgress(j * 100 / arrayOfIconImage[0].getHeight());
/*     */       }
/*     */ 
/* 106 */       this.ji_.addFullCoverage();
/* 107 */       this.state |= 4;
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/* 111 */       this.state |= 1;
/* 112 */       throw new JimiException("IO error reading CUR file");
/*     */     }
/*     */     catch (JimiException localJimiException)
/*     */     {
/* 116 */       this.state |= 1;
/* 117 */       throw localJimiException;
/*     */     }
/*     */ 
/* 120 */     return false;
/*     */   }
/*     */ 
/*     */   public void freeDecoder() throws JimiException
/*     */   {
/* 125 */     this.in_ = null;
/* 126 */     this.din_ = null;
/* 127 */     this.ji_ = null;
/*     */   }
/*     */ 
/*     */   public AdaptiveRasterImage getJimiImage()
/*     */   {
/* 138 */     return this.ji_;
/*     */   }
/*     */ 
/*     */   public int getState()
/*     */   {
/* 133 */     return this.state;
/*     */   }
/*     */ 
/*     */   public void initDecoder(InputStream paramInputStream, AdaptiveRasterImage paramAdaptiveRasterImage)
/*     */     throws JimiException
/*     */   {
/*  58 */     this.curDir_ = null;
/*     */ 
/*  60 */     this.in_ = paramInputStream;
/*  61 */     this.din_ = new LEDataInputStream(new BufferedInputStream(this.in_));
/*     */ 
/*  63 */     this.din_.mark(4096);
/*     */ 
/*  65 */     this.ji_ = paramAdaptiveRasterImage;
/*     */ 
/*  67 */     this.state = 0;
/*     */   }
/*     */ 
/*     */   private void initJimiImage(IconImage paramIconImage)
/*     */     throws JimiException
/*     */   {
/* 145 */     this.ji_.setSize(paramIconImage.getWidth(), paramIconImage.getHeight());
/*     */ 
/* 149 */     RGBQuad[] arrayOfRGBQuad = paramIconImage.getColors();
/*     */ 
/* 153 */     byte[] arrayOfByte1 = new byte[arrayOfRGBQuad.length];
/* 154 */     byte[] arrayOfByte2 = new byte[arrayOfRGBQuad.length];
/* 155 */     byte[] arrayOfByte3 = new byte[arrayOfRGBQuad.length];
/*     */ 
/* 157 */     for (int i = 0; i < arrayOfRGBQuad.length; i++)
/*     */     {
/* 159 */       arrayOfByte1[i] = ((byte)arrayOfRGBQuad[i].getRed());
/* 160 */       arrayOfByte2[i] = ((byte)arrayOfRGBQuad[i].getGreen());
/* 161 */       arrayOfByte3[i] = ((byte)arrayOfRGBQuad[i].getBlue());
/*     */     }
/*     */ 
/* 164 */     this.model_ = new IndexColorModel(8, arrayOfByte1.length, arrayOfByte1, arrayOfByte2, arrayOfByte3);
/*     */ 
/* 166 */     this.ji_.setColorModel(this.model_);
/* 167 */     this.ji_.setPixels();
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.decoder.cur.CURDecoder
 * JD-Core Version:    0.6.2
 */