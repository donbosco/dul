/*     */ package com.sun.jimi.core.util;
/*     */ 
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.ImageConsumer;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class MulticastImageConsumer
/*     */   implements ImageConsumer
/*     */ {
/*     */   protected int hints_;
/*     */   protected ColorModel colorModel_;
/*     */   protected int width_;
/*     */   protected int height_;
/*     */   protected Hashtable properties_;
/*  31 */   private Vector consumers_ = new Vector();
/*     */   private ImageConsumer[] consumerCache_;
/*  33 */   private boolean cacheValid_ = false;
/*     */ 
/*     */   public synchronized void addConsumer(ImageConsumer paramImageConsumer)
/*     */   {
/* 108 */     this.cacheValid_ = false;
/* 109 */     this.consumers_.addElement(paramImageConsumer);
/*     */   }
/*     */ 
/*     */   public void addConsumers(ImageConsumer[] paramArrayOfImageConsumer)
/*     */   {
/* 114 */     this.consumers_.ensureCapacity(this.consumers_.size() + paramArrayOfImageConsumer.length);
/* 115 */     for (int i = 0; i < paramArrayOfImageConsumer.length; i++)
/* 116 */       addConsumer(paramArrayOfImageConsumer[i]);
/*     */   }
/*     */ 
/*     */   public boolean contains(ImageConsumer paramImageConsumer)
/*     */   {
/* 141 */     return this.consumers_.contains(paramImageConsumer);
/*     */   }
/*     */ 
/*     */   public synchronized ImageConsumer[] getConsumers()
/*     */   {
/*  96 */     if (!this.cacheValid_)
/*     */     {
/*  98 */       this.consumerCache_ = new ImageConsumer[this.consumers_.size()];
/*  99 */       this.consumers_.copyInto(this.consumerCache_);
/* 100 */       this.cacheValid_ = true;
/*     */     }
/*     */ 
/* 103 */     return this.consumerCache_;
/*     */   }
/*     */ 
/*     */   public void imageComplete(int paramInt)
/*     */   {
/*  80 */     ImageConsumer[] arrayOfImageConsumer = getConsumers();
/*  81 */     for (int i = 0; i < arrayOfImageConsumer.length; i++)
/*  82 */       arrayOfImageConsumer[i].imageComplete(paramInt);
/*     */   }
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/* 137 */     return this.consumers_.size() == 0;
/*     */   }
/*     */ 
/*     */   public void removeAll()
/*     */   {
/* 128 */     this.cacheValid_ = false;
/* 129 */     this.consumers_.removeAllElements();
/*     */   }
/*     */ 
/*     */   public void removeConsumer(ImageConsumer paramImageConsumer)
/*     */   {
/* 122 */     this.cacheValid_ = false;
/* 123 */     this.consumers_.removeElement(paramImageConsumer);
/*     */   }
/*     */ 
/*     */   public void setColorModel(ColorModel paramColorModel)
/*     */   {
/*  47 */     this.colorModel_ = paramColorModel;
/*     */ 
/*  49 */     ImageConsumer[] arrayOfImageConsumer = getConsumers();
/*  50 */     for (int i = 0; i < arrayOfImageConsumer.length; i++)
/*  51 */       arrayOfImageConsumer[i].setColorModel(paramColorModel);
/*     */   }
/*     */ 
/*     */   public void setDimensions(int paramInt1, int paramInt2)
/*     */   {
/*  37 */     this.width_ = paramInt1;
/*  38 */     this.height_ = paramInt2;
/*     */ 
/*  40 */     ImageConsumer[] arrayOfImageConsumer = getConsumers();
/*  41 */     for (int i = 0; i < arrayOfImageConsumer.length; i++)
/*  42 */       arrayOfImageConsumer[i].setDimensions(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public void setHints(int paramInt)
/*     */   {
/*  56 */     this.hints_ = paramInt;
/*  57 */     ImageConsumer[] arrayOfImageConsumer = getConsumers();
/*  58 */     for (int i = 0; i < arrayOfImageConsumer.length; i++)
/*  59 */       arrayOfImageConsumer[i].setHints(paramInt);
/*     */   }
/*     */ 
/*     */   public void setPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, ColorModel paramColorModel, byte[] paramArrayOfByte, int paramInt5, int paramInt6)
/*     */   {
/*  65 */     ImageConsumer[] arrayOfImageConsumer = getConsumers();
/*  66 */     for (int i = 0; i < arrayOfImageConsumer.length; i++)
/*  67 */       arrayOfImageConsumer[i].setPixels(paramInt1, paramInt2, paramInt3, paramInt4, paramColorModel, paramArrayOfByte, paramInt5, paramInt6);
/*     */   }
/*     */ 
/*     */   public void setPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, ColorModel paramColorModel, int[] paramArrayOfInt, int paramInt5, int paramInt6)
/*     */   {
/*  73 */     ImageConsumer[] arrayOfImageConsumer = getConsumers();
/*  74 */     for (int i = 0; i < arrayOfImageConsumer.length; i++)
/*  75 */       arrayOfImageConsumer[i].setPixels(paramInt1, paramInt2, paramInt3, paramInt4, paramColorModel, paramArrayOfInt, paramInt5, paramInt6);
/*     */   }
/*     */ 
/*     */   public void setProperties(Hashtable paramHashtable)
/*     */   {
/*  87 */     this.properties_ = paramHashtable;
/*     */ 
/*  89 */     ImageConsumer[] arrayOfImageConsumer = getConsumers();
/*  90 */     for (int i = 0; i < arrayOfImageConsumer.length; i++)
/*  91 */       arrayOfImageConsumer[i].setProperties(paramHashtable);
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.util.MulticastImageConsumer
 * JD-Core Version:    0.6.2
 */