/*     */ package com.sun.jimi.core.decoder.xpm;
/*     */ 
/*     */ import com.sun.jimi.core.JimiException;
/*     */ import com.sun.jimi.core.compat.AdaptiveRasterImage;
/*     */ import com.sun.jimi.core.compat.JimiDecoderBase;
/*     */ import com.sun.jimi.core.util.x11.XpmParser;
/*     */ import java.awt.Color;
/*     */ import java.awt.image.IndexColorModel;
/*     */ import java.io.InputStream;
/*     */ 
/*     */ public class XPMDecoder extends JimiDecoderBase
/*     */ {
/*     */   protected XpmParser parser_;
/*     */   protected AdaptiveRasterImage jimiImage_;
/*     */   protected int state_;
/*     */ 
/*     */   public boolean driveDecoder()
/*     */     throws JimiException
/*     */   {
/*     */     try
/*     */     {
/*  52 */       if (this.parser_.parse())
/*     */       {
/*  54 */         xpmInitialize(this.parser_.getWidth(), this.parser_.getHeight(), 
/*  55 */           this.parser_.getPixmap(), this.parser_.getColorTable());
/*     */       }
/*     */       else
/*     */       {
/*  59 */         throw new JimiException("Image does not parse.");
/*     */       }
/*     */     }
/*     */     catch (JimiException localJimiException)
/*     */     {
/*  64 */       this.state_ = 1;
/*  65 */       throw localJimiException;
/*     */     }
/*     */     catch (RuntimeException localRuntimeException)
/*     */     {
/*  69 */       this.state_ = 1;
/*  70 */       throw new JimiException(localRuntimeException.getMessage());
/*     */     }
/*  72 */     this.state_ = 4;
/*  73 */     this.jimiImage_.addFullCoverage();
/*     */ 
/*  76 */     return false;
/*     */   }
/*     */ 
/*     */   public void freeDecoder()
/*     */   {
/*     */   }
/*     */ 
/*     */   public AdaptiveRasterImage getJimiImage()
/*     */   {
/*  91 */     return this.jimiImage_;
/*     */   }
/*     */ 
/*     */   public int getState()
/*     */   {
/*  81 */     return this.state_;
/*     */   }
/*     */ 
/*     */   public void initDecoder(InputStream paramInputStream, AdaptiveRasterImage paramAdaptiveRasterImage)
/*     */   {
/*  36 */     this.parser_ = new XpmParser(paramInputStream);
/*  37 */     this.jimiImage_ = paramAdaptiveRasterImage;
/*     */ 
/*  39 */     this.state_ = 0;
/*     */   }
/*     */ 
/*     */   protected void xpmInitialize(int paramInt1, int paramInt2, byte[] paramArrayOfByte, Color[] paramArrayOfColor)
/*     */     throws JimiException
/*     */   {
/* 105 */     int i = paramArrayOfColor.length;
/* 106 */     int j = 3 * i;
/* 107 */     byte[] arrayOfByte = new byte[j];
/*     */ 
/* 111 */     int k = -1;
/* 112 */     for (int m = 0; m < i; m++) {
/* 113 */       if (paramArrayOfColor[m] == null)
/*     */       {
/* 115 */         k = m;
/*     */       }
/*     */       else {
/* 118 */         arrayOfByte[(3 * m)] = ((byte)paramArrayOfColor[m].getRed());
/* 119 */         arrayOfByte[(3 * m + 1)] = ((byte)paramArrayOfColor[m].getGreen());
/* 120 */         arrayOfByte[(3 * m + 2)] = ((byte)paramArrayOfColor[m].getBlue());
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 125 */     IndexColorModel localIndexColorModel = new IndexColorModel(8, i, arrayOfByte, 
/* 126 */       0, false, k);
/*     */ 
/* 129 */     this.jimiImage_.setSize(paramInt1, paramInt2);
/* 130 */     this.jimiImage_.setColorModel(localIndexColorModel);
/* 131 */     this.jimiImage_.setPixels();
/* 132 */     this.jimiImage_.setChannel(0, 0, 0, paramInt1, paramInt2, paramArrayOfByte, 0, paramInt1);
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.decoder.xpm.XPMDecoder
 * JD-Core Version:    0.6.2
 */