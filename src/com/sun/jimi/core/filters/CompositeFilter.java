/*     */ package com.sun.jimi.core.filters;
/*     */ 
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.FilteredImageSource;
/*     */ import java.awt.image.ImageConsumer;
/*     */ import java.awt.image.ImageFilter;
/*     */ import java.awt.image.ImageProducer;
/*     */ import java.util.Hashtable;
/*     */ 
/*     */ public class CompositeFilter extends ImageFilterPlus
/*     */ {
/*     */   private ImageFilterPlus filterOne;
/*     */   private ImageFilterPlus filterTwo;
/*     */   private ImageFilter instanceOne;
/*     */   private ImageFilter instanceTwo;
/*     */ 
/*     */   public CompositeFilter(ImageProducer paramImageProducer, ImageFilterPlus paramImageFilterPlus1, ImageFilterPlus paramImageFilterPlus2)
/*     */   {
/*  49 */     super(paramImageProducer);
/*  50 */     this.filterOne = paramImageFilterPlus1;
/*  51 */     this.filterTwo = paramImageFilterPlus2;
/*     */ 
/*  55 */     paramImageFilterPlus1.setSource(paramImageProducer);
/*  56 */     FilteredImageSource localFilteredImageSource = 
/*  57 */       new FilteredImageSource(paramImageProducer, paramImageFilterPlus1);
/*  58 */     paramImageFilterPlus2.setSource(localFilteredImageSource);
/*     */   }
/*     */ 
/*     */   public ImageFilter getFilterInstance(ImageConsumer paramImageConsumer)
/*     */   {
/*  65 */     ImageFilter localImageFilter = this.filterOne.getFilterInstance(this.instanceTwo);
/*  66 */     CompositeFilter localCompositeFilter = (CompositeFilter)super.getFilterInstance(paramImageConsumer);
/*  67 */     localCompositeFilter.instanceTwo = this.filterTwo.getFilterInstance(paramImageConsumer);
/*  68 */     localCompositeFilter.instanceOne = this.filterOne.getFilterInstance(this.instanceTwo);
/*  69 */     return localCompositeFilter;
/*     */   }
/*     */ 
/*     */   public void imageComplete(int paramInt)
/*     */   {
/* 108 */     this.instanceOne.imageComplete(paramInt);
/*     */   }
/*     */ 
/*     */   public void setColorModel(ColorModel paramColorModel)
/*     */   {
/*  77 */     this.instanceOne.setColorModel(paramColorModel);
/*     */   }
/*     */ 
/*     */   public void setDimensions(int paramInt1, int paramInt2)
/*     */   {
/*  82 */     this.instanceOne.setDimensions(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public void setHints(int paramInt)
/*     */   {
/*  87 */     this.instanceOne.setHints(paramInt);
/*     */   }
/*     */ 
/*     */   public void setPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, ColorModel paramColorModel, byte[] paramArrayOfByte, int paramInt5, int paramInt6)
/*     */   {
/*  97 */     this.instanceOne.setPixels(paramInt1, paramInt2, paramInt3, paramInt4, paramColorModel, paramArrayOfByte, paramInt5, paramInt6);
/*     */   }
/*     */ 
/*     */   public void setPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, ColorModel paramColorModel, int[] paramArrayOfInt, int paramInt5, int paramInt6)
/*     */   {
/* 102 */     this.instanceOne.setPixels(paramInt1, paramInt2, paramInt3, paramInt4, paramColorModel, paramArrayOfInt, paramInt5, paramInt6);
/*     */   }
/*     */ 
/*     */   public void setProperties(Hashtable paramHashtable)
/*     */   {
/*  92 */     this.instanceOne.setProperties(paramHashtable);
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.filters.CompositeFilter
 * JD-Core Version:    0.6.2
 */