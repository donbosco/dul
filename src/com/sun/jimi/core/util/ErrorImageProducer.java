/*    */ package com.sun.jimi.core.util;
/*    */ 
/*    */ import java.awt.image.ImageConsumer;
/*    */ import java.awt.image.ImageProducer;
/*    */ import java.util.Vector;
/*    */ 
/*    */ public class ErrorImageProducer
/*    */   implements ImageProducer
/*    */ {
/* 25 */   protected Vector consumers = new Vector();
/*    */ 
/*    */   public void addConsumer(ImageConsumer paramImageConsumer)
/*    */   {
/* 29 */     sendError(paramImageConsumer);
/* 30 */     this.consumers.addElement(paramImageConsumer);
/*    */   }
/*    */ 
/*    */   public boolean isConsumer(ImageConsumer paramImageConsumer)
/*    */   {
/* 35 */     return this.consumers.contains(paramImageConsumer);
/*    */   }
/*    */ 
/*    */   public void removeConsumer(ImageConsumer paramImageConsumer)
/*    */   {
/* 40 */     this.consumers.removeElement(paramImageConsumer);
/*    */   }
/*    */ 
/*    */   public void requestTopDownLeftRightResend(ImageConsumer paramImageConsumer)
/*    */   {
/* 50 */     addConsumer(paramImageConsumer);
/*    */   }
/*    */ 
/*    */   protected void sendError(ImageConsumer paramImageConsumer)
/*    */   {
/* 55 */     paramImageConsumer.imageComplete(1);
/*    */   }
/*    */ 
/*    */   public void startProduction(ImageConsumer paramImageConsumer)
/*    */   {
/* 45 */     addConsumer(paramImageConsumer);
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.util.ErrorImageProducer
 * JD-Core Version:    0.6.2
 */