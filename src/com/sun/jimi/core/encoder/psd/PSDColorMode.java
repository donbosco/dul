/*    */ package com.sun.jimi.core.encoder.psd;
/*    */ 
/*    */ import com.sun.jimi.core.JimiException;
/*    */ import com.sun.jimi.core.compat.AdaptiveRasterImage;
/*    */ import java.awt.image.ColorModel;
/*    */ import java.awt.image.IndexColorModel;
/*    */ import java.io.DataOutputStream;
/*    */ import java.io.FilterOutputStream;
/*    */ import java.io.IOException;
/*    */ 
/*    */ class PSDColorMode
/*    */ {
/*    */   AdaptiveRasterImage ji_;
/*    */   DataOutputStream out_;
/*    */ 
/*    */   PSDColorMode(AdaptiveRasterImage paramAdaptiveRasterImage, DataOutputStream paramDataOutputStream)
/*    */     throws JimiException
/*    */   {
/* 39 */     this.ji_ = paramAdaptiveRasterImage;
/* 40 */     this.out_ = paramDataOutputStream;
/*    */   }
/*    */ 
/*    */   void write()
/*    */     throws IOException
/*    */   {
/* 49 */     ColorModel localColorModel = this.ji_.getColorModel();
/* 50 */     if ((localColorModel instanceof IndexColorModel))
/*    */     {
/* 52 */       IndexColorModel localIndexColorModel = (IndexColorModel)localColorModel;
/* 53 */       int i = 256;
/* 54 */       byte[] arrayOfByte = new byte[i];
/*    */ 
/* 56 */       this.out_.writeInt(3 * i);
/* 57 */       localIndexColorModel.getReds(arrayOfByte);
/* 58 */       this.out_.write(arrayOfByte);
/* 59 */       localIndexColorModel.getGreens(arrayOfByte);
/* 60 */       this.out_.write(arrayOfByte);
/* 61 */       localIndexColorModel.getBlues(arrayOfByte);
/* 62 */       this.out_.write(arrayOfByte);
/*    */     }
/*    */     else {
/* 65 */       this.out_.writeInt(0);
/*    */     }
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.encoder.psd.PSDColorMode
 * JD-Core Version:    0.6.2
 */