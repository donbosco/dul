/*     */ package com.sun.jimi.core.decoder.xbm;
/*     */ 
/*     */ import com.sun.jimi.core.JimiException;
/*     */ import com.sun.jimi.core.compat.AdaptiveRasterImage;
/*     */ import com.sun.jimi.core.compat.JimiDecoderBase;
/*     */ import com.sun.jimi.core.util.ProgressMonitorSupport;
/*     */ import com.sun.jimi.core.util.x11.XbmParser;
/*     */ import java.awt.Color;
/*     */ import java.awt.image.IndexColorModel;
/*     */ import java.io.InputStream;
/*     */ 
/*     */ public class XBMDecoder extends JimiDecoderBase
/*     */ {
/*     */   protected XbmParser parser_;
/*     */   protected AdaptiveRasterImage jimiImage_;
/*     */   protected int state_;
/*     */   protected InputStream input_;
/*     */   protected byte[] pixels_;
/*     */ 
/*     */   public boolean driveDecoder()
/*     */     throws JimiException
/*     */   {
/*  53 */     if (this.parser_.parse())
/*     */     {
/*  55 */       xbmInitialize(this.parser_.getWidth(), this.parser_.getHeight(), 
/*  56 */         this.parser_.getBitmap(), Color.black, Color.white, true);
/*     */     }
/*     */     else
/*     */     {
/*  60 */       throw new JimiException("Image does not parse.");
/*     */     }
/*     */ 
/*  63 */     this.state_ = 4;
/*  64 */     this.jimiImage_.addFullCoverage();
/*     */ 
/*  67 */     return false;
/*     */   }
/*     */ 
/*     */   public void freeDecoder()
/*     */   {
/*     */   }
/*     */ 
/*     */   public AdaptiveRasterImage getJimiImage()
/*     */   {
/*  82 */     return this.jimiImage_;
/*     */   }
/*     */ 
/*     */   public int getState()
/*     */   {
/*  72 */     return this.state_;
/*     */   }
/*     */ 
/*     */   public void initDecoder(InputStream paramInputStream, AdaptiveRasterImage paramAdaptiveRasterImage)
/*     */   {
/*  39 */     this.parser_ = new XbmParser(paramInputStream);
/*  40 */     this.jimiImage_ = paramAdaptiveRasterImage;
/*  41 */     this.input_ = paramInputStream;
/*     */ 
/*  43 */     this.state_ = 0;
/*     */   }
/*     */ 
/*     */   private void xbmInitialize(int paramInt1, int paramInt2, int[] paramArrayOfInt, Color paramColor1, Color paramColor2, boolean paramBoolean)
/*     */     throws JimiException
/*     */   {
/*  88 */     int i = paramInt1;
/*  89 */     int j = paramInt2;
/*     */ 
/*  93 */     byte[] arrayOfByte = new byte[6];
/*  94 */     int k = -1;
/*     */ 
/*  96 */     if (paramBoolean)
/*     */     {
/*  98 */       k = 0;
/*  99 */       arrayOfByte[0] = -1;
/* 100 */       arrayOfByte[1] = -1;
/* 101 */       arrayOfByte[2] = -1;
/*     */     }
/*     */     else
/*     */     {
/* 105 */       arrayOfByte[0] = ((byte)paramColor2.getRed());
/* 106 */       arrayOfByte[1] = ((byte)paramColor2.getGreen());
/* 107 */       arrayOfByte[2] = ((byte)paramColor2.getBlue());
/*     */     }
/*     */ 
/* 110 */     arrayOfByte[3] = ((byte)paramColor1.getRed());
/* 111 */     arrayOfByte[4] = ((byte)paramColor1.getGreen());
/* 112 */     arrayOfByte[5] = ((byte)paramColor1.getBlue());
/*     */ 
/* 115 */     IndexColorModel localIndexColorModel = new IndexColorModel(8, 2, arrayOfByte, 0, false, k);
/*     */ 
/* 118 */     this.pixels_ = new byte[i * j];
/*     */ 
/* 121 */     int m = 0;
/* 122 */     int n = 0;
/* 123 */     int i1 = i / 8;
/*     */     try {
/* 125 */       for (int i2 = 0; i2 < j; i2++)
/* 126 */         for (int i3 = 0; i3 < i1; i3++) {
/* 127 */           int i4 = (byte)paramArrayOfInt[n];
/* 128 */           for (int i5 = 0; i5 < 8; i5++) {
/* 129 */             int i6 = i4 & 1 << i5;
/* 130 */             this.pixels_[m] = (byte) (i6 != 0 ? 1 : 0);
/* 131 */             m++;
/*     */           }
/* 133 */           n++;
/*     */         }
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/* 138 */       throw new JimiException(localException.getMessage());
/*     */     }
/*     */ 
/* 142 */     this.jimiImage_.setSize(i, j);
/* 143 */     this.jimiImage_.setColorModel(localIndexColorModel);
/* 144 */     this.jimiImage_.setPixels();
/* 145 */     this.jimiImage_.setChannel(0, 0, 0, i, j, this.pixels_, 0, i);
/* 146 */     setProgress(100);
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.decoder.xbm.XBMDecoder
 * JD-Core Version:    0.6.2
 */