/*    */ package com.sun.jimi.core;
/*    */ 
/*    */ import java.awt.image.ImageProducer;
/*    */ 
/*    */ public abstract class ImageSeriesDecodingController
/*    */ {
/*    */   public static final int UNKNOWN = -1;
/*    */   protected JimiDecodingController currentController;
/*    */ 
/*    */   protected abstract JimiDecodingController createNextController();
/*    */ 
/*    */   public JimiDecodingController getNextController()
/*    */   {
/* 62 */     if (this.currentController != null) {
/* 63 */       this.currentController.requestDecoding();
/*    */     }
/* 65 */     this.currentController = createNextController();
/* 66 */     return this.currentController;
/*    */   }
/*    */ 
/*    */   public ImageProducer getNextImageProducer()
/*    */   {
/* 41 */     JimiDecodingController localJimiDecodingController = getNextController();
/* 42 */     return localJimiDecodingController.getJimiImage().getImageProducer();
/*    */   }
/*    */ 
/*    */   public JimiImage getNextJimiImage()
/*    */   {
/* 34 */     JimiDecodingController localJimiDecodingController = getNextController();
/* 35 */     localJimiDecodingController.requestDecoding();
/* 36 */     return localJimiDecodingController.getJimiImage();
/*    */   }
/*    */ 
/*    */   public int getNumberOfImages()
/*    */   {
/* 57 */     return -1;
/*    */   }
/*    */ 
/*    */   public abstract boolean hasMoreImages();
/*    */ 
/*    */   public abstract void skipNextImage()
/*    */     throws JimiException;
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.ImageSeriesDecodingController
 * JD-Core Version:    0.6.2
 */