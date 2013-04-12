/*    */ package com.sun.jimi.core.util;
/*    */ 
/*    */ import java.awt.image.ImageConsumer;
/*    */ import java.awt.image.ImageProducer;
/*    */ import java.util.Enumeration;
/*    */ import java.util.Vector;
/*    */ 
/*    */ public class QueuedImageProducerProxy
/*    */   implements ImageProducer
/*    */ {
/* 15 */   protected Vector queuedConsumers = new Vector();
/*    */   protected ImageProducer realProducer;
/*    */ 
/*    */   public synchronized void addConsumer(ImageConsumer paramImageConsumer)
/*    */   {
/* 47 */     if (this.realProducer == null) {
/* 48 */       this.queuedConsumers.addElement(paramImageConsumer);
/*    */     }
/*    */     else
/* 51 */       this.realProducer.addConsumer(paramImageConsumer);
/*    */   }
/*    */ 
/*    */   public synchronized ImageConsumer[] getConsumers()
/*    */   {
/* 39 */     ImageConsumer[] arrayOfImageConsumer = new ImageConsumer[this.queuedConsumers.size()];
/* 40 */     this.queuedConsumers.copyInto(arrayOfImageConsumer);
/*    */ 
/* 42 */     return arrayOfImageConsumer;
/*    */   }
/*    */ 
/*    */   public ImageProducer getImageProducer()
/*    */   {
/* 34 */     return this.realProducer;
/*    */   }
/*    */ 
/*    */   public synchronized boolean isConsumer(ImageConsumer paramImageConsumer)
/*    */   {
/* 57 */     if (this.realProducer == null) {
/* 58 */       return this.queuedConsumers.contains(paramImageConsumer);
/*    */     }
/*    */ 
/* 61 */     return this.realProducer.isConsumer(paramImageConsumer);
/*    */   }
/*    */ 
/*    */   public synchronized void removeConsumer(ImageConsumer paramImageConsumer)
/*    */   {
/* 67 */     if (this.realProducer == null) {
/* 68 */       this.queuedConsumers.removeElement(paramImageConsumer);
/*    */     }
/*    */     else
/* 71 */       this.realProducer.removeConsumer(paramImageConsumer);
/*    */   }
/*    */ 
/*    */   public void requestTopDownLeftRightResend(ImageConsumer paramImageConsumer)
/*    */   {
/* 88 */     removeConsumer(paramImageConsumer);
/* 89 */     addConsumer(paramImageConsumer);
/*    */   }
/*    */ 
/*    */   public synchronized void setImageProducer(ImageProducer paramImageProducer)
/*    */   {
/* 25 */     this.realProducer = paramImageProducer;
/* 26 */     Enumeration localEnumeration = this.queuedConsumers.elements();
/* 27 */     while (localEnumeration.hasMoreElements())
/* 28 */       this.realProducer.addConsumer((ImageConsumer)localEnumeration.nextElement());
/*    */   }
/*    */ 
/*    */   public void startProduction(ImageConsumer paramImageConsumer)
/*    */   {
/* 80 */     addConsumer(paramImageConsumer);
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.util.QueuedImageProducerProxy
 * JD-Core Version:    0.6.2
 */