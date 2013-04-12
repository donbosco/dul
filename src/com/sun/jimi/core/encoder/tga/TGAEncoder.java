/*    */ package com.sun.jimi.core.encoder.tga;
/*    */ 
/*    */ import com.sun.jimi.core.JimiException;
/*    */ import com.sun.jimi.core.compat.AdaptiveRasterImage;
/*    */ import com.sun.jimi.core.compat.JimiEncoderBase;
/*    */ import com.sun.jimi.core.util.LEDataOutputStream;
/*    */ import java.awt.image.ColorModel;
/*    */ import java.awt.image.IndexColorModel;
/*    */ import java.io.FilterOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ 
/*    */ public class TGAEncoder extends JimiEncoderBase
/*    */   implements TGAEncoderIfc
/*    */ {
/*    */   private OutputStream out;
/*    */   private LEDataOutputStream bOut;
/*    */   private int state;
/*    */ 
/*    */   protected TGAEncoderIfc createEncoderForImage(AdaptiveRasterImage paramAdaptiveRasterImage)
/*    */   {
/* 85 */     ColorModel localColorModel = paramAdaptiveRasterImage.getColorModel();
/*    */ 
/* 88 */     if (((localColorModel instanceof IndexColorModel)) && (localColorModel.getPixelSize() == 8))
/* 89 */       return new TGA8Encoder();
/* 90 */     return new TGA24Encoder();
/*    */   }
/*    */ 
/*    */   public boolean driveEncoder()
/*    */     throws JimiException
/*    */   {
/* 48 */     AdaptiveRasterImage localAdaptiveRasterImage = getJimiImage();
/*    */ 
/* 50 */     encodeTGA(localAdaptiveRasterImage, this.bOut);
/* 51 */     this.state |= 2;
/*    */     try
/*    */     {
/* 54 */       this.bOut.flush();
/* 55 */       this.bOut.close();
/*    */     }
/*    */     catch (IOException localIOException)
/*    */     {
/* 59 */       throw new JimiException("TGAEncoder driveEncoder() IO Exception encountered");
/*    */     }
/* 61 */     return false;
/*    */   }
/*    */ 
/*    */   public void encodeTGA(AdaptiveRasterImage paramAdaptiveRasterImage, LEDataOutputStream paramLEDataOutputStream)
/*    */     throws JimiException
/*    */   {
/* 80 */     createEncoderForImage(paramAdaptiveRasterImage).encodeTGA(paramAdaptiveRasterImage, paramLEDataOutputStream);
/*    */   }
/*    */ 
/*    */   public void freeEncoder()
/*    */     throws JimiException
/*    */   {
/* 67 */     AdaptiveRasterImage localAdaptiveRasterImage = getJimiImage();
/* 68 */     this.out = null;
/* 69 */     this.bOut = null;
/* 70 */     super.freeEncoder();
/*    */   }
/*    */ 
/*    */   public int getState()
/*    */   {
/* 75 */     return this.state;
/*    */   }
/*    */ 
/*    */   protected void initSpecificEncoder(OutputStream paramOutputStream, AdaptiveRasterImage paramAdaptiveRasterImage)
/*    */     throws JimiException
/*    */   {
/* 38 */     this.out = paramOutputStream;
/* 39 */     this.bOut = new LEDataOutputStream(paramOutputStream);
/* 40 */     this.state = 0;
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.encoder.tga.TGAEncoder
 * JD-Core Version:    0.6.2
 */