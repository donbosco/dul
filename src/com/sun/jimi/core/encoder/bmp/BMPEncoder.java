/*     */ package com.sun.jimi.core.encoder.bmp;
/*     */ 
/*     */ import com.sun.jimi.core.JimiException;
/*     */ import com.sun.jimi.core.compat.AdaptiveRasterImage;
/*     */ import com.sun.jimi.core.compat.JimiEncoderBase;
/*     */ import com.sun.jimi.core.util.LEDataOutputStream;
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.IndexColorModel;
/*     */ import java.io.FilterOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ 
/*     */ public class BMPEncoder extends JimiEncoderBase
/*     */   implements BMPEncoderIfc
/*     */ {
/*     */   private OutputStream out;
/*     */   private LEDataOutputStream LEbOut;
/*     */   private int state;
/*     */ 
/*     */   protected BMPEncoderIfc createEncoderForImage(AdaptiveRasterImage paramAdaptiveRasterImage)
/*     */   {
/*  99 */     ColorModel localColorModel = paramAdaptiveRasterImage.getColorModel();
/*     */ 
/* 102 */     if (((localColorModel instanceof IndexColorModel)) && (localColorModel.getPixelSize() == 8))
/* 103 */       return new BMP8Encoder();
/* 104 */     return new BMP24Encoder();
/*     */   }
/*     */ 
/*     */   public boolean driveEncoder()
/*     */     throws JimiException
/*     */   {
/*  59 */     AdaptiveRasterImage localAdaptiveRasterImage = getJimiImage();
/*  60 */     encodeBMP(this, localAdaptiveRasterImage, this.LEbOut);
/*  61 */     this.state |= 2;
/*     */     try
/*     */     {
/*  65 */       this.LEbOut.flush();
/*  66 */       this.LEbOut.close();
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/*  70 */       throw new JimiException("BMPEncoder driveEncoder() IO Exception encountered");
/*     */     }
/*  72 */     return false;
/*     */   }
/*     */ 
/*     */   public void encodeBMP(BMPEncoder paramBMPEncoder, AdaptiveRasterImage paramAdaptiveRasterImage, LEDataOutputStream paramLEDataOutputStream)
/*     */     throws JimiException
/*     */   {
/*  94 */     createEncoderForImage(paramAdaptiveRasterImage).encodeBMP(this, paramAdaptiveRasterImage, paramLEDataOutputStream);
/*     */   }
/*     */ 
/*     */   public void freeEncoder()
/*     */     throws JimiException
/*     */   {
/*  77 */     this.out = null;
/*  78 */     this.LEbOut = null;
/*  79 */     super.freeEncoder();
/*     */   }
/*     */ 
/*     */   public int getState()
/*     */   {
/*  84 */     return this.state;
/*     */   }
/*     */ 
/*     */   public void initSpecificEncoder(OutputStream paramOutputStream, AdaptiveRasterImage paramAdaptiveRasterImage)
/*     */     throws JimiException
/*     */   {
/*  50 */     this.out = paramOutputStream;
/*  51 */     this.LEbOut = new LEDataOutputStream(paramOutputStream);
/*  52 */     this.state = 0;
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.encoder.bmp.BMPEncoder
 * JD-Core Version:    0.6.2
 */