/*    */ package com.sun.jimi.core.util;
/*    */ 
/*    */ import com.sun.jimi.core.JimiException;
/*    */ import com.sun.jimi.core.JimiImage;
/*    */ import com.sun.jimi.core.raster.JimiRasterImage;
/*    */ import java.awt.Image;
/*    */ import java.awt.Toolkit;
/*    */ import java.awt.image.ImageProducer;
/*    */ 
/*    */ public class ColorReducer
/*    */ {
/*    */   protected JimiImageColorReducer reducer_;
/*    */   protected boolean dither_;
/*    */ 
/*    */   public ColorReducer(int paramInt)
/*    */   {
/* 32 */     this(paramInt, false);
/*    */   }
/*    */ 
/*    */   public ColorReducer(int paramInt, boolean paramBoolean)
/*    */   {
/* 43 */     this.reducer_ = new JimiImageColorReducer(paramInt);
/* 44 */     this.dither_ = paramBoolean;
/*    */   }
/*    */ 
/*    */   protected JimiRasterImage doColorReduction(ImageProducer paramImageProducer)
/*    */     throws JimiException
/*    */   {
/* 90 */     if (this.dither_) {
/* 91 */       return this.reducer_.colorReduceFS(paramImageProducer);
/*    */     }
/* 93 */     return this.reducer_.colorReduce(paramImageProducer);
/*    */   }
/*    */ 
/*    */   public Image getColorReducedImage(Image paramImage)
/*    */     throws JimiException
/*    */   {
/* 84 */     return getColorReducedImage(paramImage.getSource());
/*    */   }
/*    */ 
/*    */   public Image getColorReducedImage(ImageProducer paramImageProducer)
/*    */     throws JimiException
/*    */   {
/* 74 */     return Toolkit.getDefaultToolkit().createImage(doColorReduction(paramImageProducer).getImageProducer());
/*    */   }
/*    */ 
/*    */   public ImageProducer getColorReducedImageProducer(Image paramImage)
/*    */     throws JimiException
/*    */   {
/* 64 */     return getColorReducedImageProducer(paramImage.getSource());
/*    */   }
/*    */ 
/*    */   public ImageProducer getColorReducedImageProducer(ImageProducer paramImageProducer)
/*    */     throws JimiException
/*    */   {
/* 54 */     return doColorReduction(paramImageProducer).getImageProducer();
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.util.ColorReducer
 * JD-Core Version:    0.6.2
 */