/*     */ package com.sun.jimi.core;
/*     */ 
/*     */ import com.sun.jimi.core.options.FormatOptionSet;
/*     */ import java.awt.Image;
/*     */ import java.awt.image.ImageProducer;
/*     */ 
/*     */ public class JimiImageEnumeration
/*     */ {
/*     */   protected static final int MODE_JIMIIMAGE = 0;
/*     */   protected static final int MODE_IMAGEPRODUCER = 1;
/*     */   protected int mode;
/*     */   protected ImageProducer[] producers;
/*     */   protected JimiImage[] jimiImages;
/*     */   protected int imageCount;
/*  39 */   protected int imageIndex = 0;
/*     */   protected FormatOptionSet options;
/*     */ 
/*     */   public JimiImageEnumeration(JimiImage paramJimiImage)
/*     */   {
/*  80 */     this(new JimiImage[] { paramJimiImage });
/*     */   }
/*     */ 
/*     */   public JimiImageEnumeration(Image paramImage)
/*     */   {
/*  70 */     this(new Image[] { paramImage });
/*     */   }
/*     */ 
/*     */   public JimiImageEnumeration(ImageProducer paramImageProducer)
/*     */   {
/*  75 */     this(new ImageProducer[] { paramImageProducer });
/*     */   }
/*     */ 
/*     */   public JimiImageEnumeration(JimiImage[] paramArrayOfJimiImage)
/*     */   {
/*  63 */     this.jimiImages = paramArrayOfJimiImage;
/*  64 */     this.mode = 0;
/*  65 */     this.imageCount = paramArrayOfJimiImage.length;
/*     */   }
/*     */ 
/*     */   public JimiImageEnumeration(Image[] paramArrayOfImage)
/*     */   {
/*  45 */     ImageProducer[] arrayOfImageProducer = new ImageProducer[paramArrayOfImage.length];
/*  46 */     for (int i = 0; i < paramArrayOfImage.length; i++) {
/*  47 */       arrayOfImageProducer[i] = paramArrayOfImage[i].getSource();
/*     */     }
/*  49 */     this.producers = arrayOfImageProducer;
/*  50 */     this.mode = 1;
/*  51 */     this.imageCount = arrayOfImageProducer.length;
/*     */   }
/*     */ 
/*     */   public JimiImageEnumeration(ImageProducer[] paramArrayOfImageProducer)
/*     */   {
/*  56 */     this.producers = paramArrayOfImageProducer;
/*  57 */     this.mode = 1;
/*  58 */     this.imageCount = paramArrayOfImageProducer.length;
/*     */   }
/*     */ 
/*     */   public int countImages()
/*     */   {
/*  89 */     return this.imageCount;
/*     */   }
/*     */ 
/*     */   public JimiImage getNextImage()
/*     */     throws JimiException
/*     */   {
/*  99 */     if (!hasMoreImages())
/* 100 */       return null;
/*     */     Object localObject;
/* 104 */     if (this.mode == 1) {
/* 105 */       localObject = Jimi.createRasterImage(this.producers[this.imageIndex]);
/* 106 */       this.producers[this.imageIndex] = null;
/*     */     }
/*     */     else {
/* 109 */       localObject = this.jimiImages[this.imageIndex];
/* 110 */       this.jimiImages[this.imageIndex] = null;
/*     */     }
/* 112 */     ((JimiImage)localObject).waitFinished();
/* 113 */     if (this.options != null) {
/* 114 */       ((JimiImage)localObject).setOptions(this.options);
/*     */     }
/* 116 */     this.imageIndex += 1;
/* 117 */     return (JimiImage) localObject;
/*     */   }
/*     */ 
/*     */   public boolean hasMoreImages()
/*     */   {
/* 126 */     return this.imageIndex < this.imageCount;
/*     */   }
/*     */ 
/*     */   public void setOptions(FormatOptionSet paramFormatOptionSet)
/*     */   {
/* 134 */     this.options = paramFormatOptionSet;
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.JimiImageEnumeration
 * JD-Core Version:    0.6.2
 */