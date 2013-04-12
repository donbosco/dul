/*     */ package com.sun.jimi.core;
/*     */ 
/*     */ import com.sun.jimi.core.options.FormatOptionSet;
/*     */ import com.sun.jimi.core.raster.JimiRasterImage;
/*     */ import com.sun.jimi.core.util.QueuedImageProducerProxy;
/*     */ import java.awt.image.ImageProducer;
/*     */ 
/*     */ public class JimiImageHandle
/*     */   implements JimiImage
/*     */ {
/*     */   protected JimiImage image;
/*     */   protected boolean error;
/*  38 */   protected QueuedImageProducerProxy producerProxy = new QueuedImageProducerProxy();
/*     */ 
/*     */   public JimiImageFactory getFactory()
/*     */   {
/* 124 */     if (this.image != null) {
/* 125 */       return this.image.getFactory();
/*     */     }
/*     */ 
/* 128 */     return new MemoryJimiImageFactory();
/*     */   }
/*     */ 
/*     */   public ImageProducer getImageProducer()
/*     */   {
/* 113 */     return this.producerProxy;
/*     */   }
/*     */ 
/*     */   public FormatOptionSet getOptions()
/*     */   {
/* 134 */     waitImageSet();
/* 135 */     if ((this.image instanceof JimiRasterImage)) {
/* 136 */       ((JimiRasterImage)this.image).waitInfoAvailable();
/*     */     }
/* 138 */     return this.image.getOptions();
/*     */   }
/*     */ 
/*     */   public JimiImage getWrappedJimiImage()
/*     */     throws JimiException
/*     */   {
/*  89 */     waitImageSet();
/*  90 */     if (this.image == null) {
/*  91 */       throw new JimiException();
/*     */     }
/*  93 */     return this.image;
/*     */   }
/*     */ 
/*     */   public boolean isError()
/*     */   {
/* 118 */     waitImageSet();
/* 119 */     return this.image.isError();
/*     */   }
/*     */ 
/*     */   public boolean isImageSet()
/*     */   {
/*  80 */     return this.image != null;
/*     */   }
/*     */ 
/*     */   public synchronized void setJimiImage(JimiImage paramJimiImage)
/*     */   {
/*  53 */     this.image = paramJimiImage;
/*  54 */     this.producerProxy.setImageProducer(paramJimiImage.getImageProducer());
/*     */ 
/*  57 */     notifyAll();
/*     */   }
/*     */ 
/*     */   public void setOptions(FormatOptionSet paramFormatOptionSet)
/*     */   {
/* 143 */     waitImageSet();
/* 144 */     this.image.setOptions(paramFormatOptionSet);
/*     */   }
/*     */ 
/*     */   public void waitFinished()
/*     */   {
/* 102 */     waitImageSet();
/* 103 */     this.image.waitFinished();
/*     */   }
/*     */ 
/*     */   protected synchronized void waitImageSet()
/*     */   {
/*  65 */     while (this.image == null)
/*     */       try {
/*  67 */         wait();
/*     */       }
/*     */       catch (InterruptedException localInterruptedException)
/*     */       {
/*     */       }
/*     */   }
/*     */ 
/*     */   public void waitInfoAvailable()
/*     */   {
/* 108 */     waitImageSet();
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.JimiImageHandle
 * JD-Core Version:    0.6.2
 */