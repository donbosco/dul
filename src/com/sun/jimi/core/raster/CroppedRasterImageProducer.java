/*    */ package com.sun.jimi.core.raster;
/*    */ 
/*    */ import java.awt.Rectangle;
/*    */ import java.awt.image.ImageConsumer;
/*    */ import java.awt.image.ImageProducer;
/*    */ import java.util.Vector;
/*    */ 
/*    */ public class CroppedRasterImageProducer
/*    */   implements ImageProducer
/*    */ {
/*    */   protected Rectangle region;
/*    */   protected JimiRasterImageSupport image;
/* 29 */   protected Vector consumers = new Vector();
/*    */ 
/*    */   public CroppedRasterImageProducer(JimiRasterImageSupport paramJimiRasterImageSupport, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*    */   {
/* 34 */     this.image = paramJimiRasterImageSupport;
/* 35 */     this.region = new Rectangle(paramInt1, paramInt2, paramInt3, paramInt4);
/*    */   }
/*    */ 
/*    */   public void addConsumer(ImageConsumer paramImageConsumer)
/*    */   {
/* 40 */     if (!this.consumers.contains(paramImageConsumer)) {
/* 41 */       this.consumers.addElement(paramImageConsumer);
/*    */     }
/* 43 */     this.image.produceCroppedImage(paramImageConsumer, this.region);
/*    */   }
/*    */ 
/*    */   public boolean isConsumer(ImageConsumer paramImageConsumer)
/*    */   {
/* 48 */     return this.consumers.contains(paramImageConsumer);
/*    */   }
/*    */ 
/*    */   public void removeConsumer(ImageConsumer paramImageConsumer)
/*    */   {
/* 53 */     this.consumers.removeElement(paramImageConsumer);
/*    */   }
/*    */ 
/*    */   public void requestTopDownLeftRightResend(ImageConsumer paramImageConsumer)
/*    */   {
/* 58 */     addConsumer(paramImageConsumer);
/*    */   }
/*    */ 
/*    */   public void startProduction(ImageConsumer paramImageConsumer)
/*    */   {
/* 63 */     addConsumer(paramImageConsumer);
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.raster.CroppedRasterImageProducer
 * JD-Core Version:    0.6.2
 */