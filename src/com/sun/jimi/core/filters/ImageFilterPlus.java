/*     */ package com.sun.jimi.core.filters;
/*     */ 
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.ImageConsumer;
/*     */ import java.awt.image.ImageFilter;
/*     */ import java.awt.image.ImageProducer;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ 
/*     */ public class ImageFilterPlus extends ImageFilter
/*     */ {
/*     */   private ImageProducer producer;
/*     */   private boolean pixelOrderChanges;
/*  94 */   public static final ColorModel rgbModel = ColorModel.getRGBdefault();
/*     */ 
/*     */   public ImageFilterPlus(ImageProducer paramImageProducer)
/*     */   {
/*  75 */     this(paramImageProducer, false);
/*     */   }
/*     */ 
/*     */   public ImageFilterPlus(ImageProducer paramImageProducer, boolean paramBoolean)
/*     */   {
/*  88 */     setSource(paramImageProducer);
/*  89 */     this.pixelOrderChanges = paramBoolean;
/*     */   }
/*     */ 
/*     */   public static int filterStream(InputStream paramInputStream, OutputStream paramOutputStream, ImageFilterPlus paramImageFilterPlus)
/*     */   {
/* 144 */     return 0;
/*     */   }
/*     */ 
/*     */   public ImageProducer getSource()
/*     */   {
/* 100 */     return this.producer;
/*     */   }
/*     */ 
/*     */   public void imageComplete(int paramInt)
/*     */   {
/* 134 */     super.imageComplete(paramInt);
/*     */   }
/*     */ 
/*     */   public void setHints(int paramInt)
/*     */   {
/* 116 */     if (this.pixelOrderChanges)
/* 117 */       paramInt &= -7;
/* 118 */     this.consumer.setHints(paramInt);
/* 119 */     super.setHints(paramInt);
/*     */   }
/*     */ 
/*     */   public void setSource(ImageProducer paramImageProducer)
/*     */   {
/* 107 */     this.producer = paramImageProducer;
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.filters.ImageFilterPlus
 * JD-Core Version:    0.6.2
 */