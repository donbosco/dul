/*     */ package com.sun.jimi.core.component;
/*     */ 
/*     */ import com.sun.jimi.core.JimiImage;
/*     */ import com.sun.jimi.core.JimiReader;
/*     */ import com.sun.jimi.core.raster.JimiRasterImage;
/*     */ import java.util.Enumeration;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class ImageCache
/*     */ {
/*     */   public Enumeration rasterImages;
/*  31 */   public Vector cachedImages = new Vector();
/*     */   public int cacheSize;
/*     */   public boolean wrap;
/*  34 */   public boolean seriesFinished = false;
/*     */ 
/* 115 */   public int currentIndex = -1;
/*     */ 
/*     */   public ImageCache(JimiReader paramJimiReader, boolean paramBoolean)
/*     */   {
/*  38 */     this.rasterImages = paramJimiReader.getRasterImageEnumeration();
/*  39 */     this.wrap = paramBoolean;
/*     */   }
/*     */ 
/*     */   public boolean allImagesCached()
/*     */   {
/*  86 */     return this.seriesFinished;
/*     */   }
/*     */ 
/*     */   public JimiRasterImage getCachedImage(int paramInt)
/*     */     throws NoSuchElementException
/*     */   {
/*  95 */     if (this.cacheSize == 0) {
/*  96 */       throw new NoSuchElementException();
/*     */     }
/*  98 */     if (!this.wrap) {
/*  99 */       if (this.cachedImages.size() < paramInt) {
/* 100 */         throw new NoSuchElementException();
/*     */       }
/*     */ 
/* 103 */       JimiRasterImage localJimiRasterImage = (JimiRasterImage)this.cachedImages.elementAt(paramInt);
/* 104 */       localJimiRasterImage.waitFinished();
/* 105 */       return localJimiRasterImage;
/*     */     }
/*     */ 
/* 109 */     JimiRasterImage localJimiRasterImage = (JimiRasterImage)this.cachedImages.elementAt(paramInt % this.cacheSize);
/* 110 */     localJimiRasterImage.waitFinished();
/* 111 */     return localJimiRasterImage;
/*     */   }
/*     */ 
/*     */   public JimiRasterImage getImage(int paramInt)
/*     */     throws NoSuchElementException
/*     */   {
/*  46 */     if (this.cacheSize <= paramInt) {
/*  47 */       int i = paramInt - (this.cacheSize - 1);
/*  48 */       JimiRasterImage localJimiRasterImage = null;
/*     */ 
/*  51 */       if (this.seriesFinished) {
/*  52 */         return getCachedImage(paramInt);
/*     */       }
/*     */ 
/*  56 */       while (i-- > 0) {
/*  57 */         if (!this.rasterImages.hasMoreElements()) {
/*     */           break;
/*     */         }
/*  60 */         localJimiRasterImage = (JimiRasterImage)this.rasterImages.nextElement();
/*     */ 
/*  62 */         if (localJimiRasterImage == null) {
/*  63 */           this.seriesFinished = true;
/*  64 */           return getCachedImage(paramInt);
/*     */         }
/*     */ 
/*  67 */         localJimiRasterImage.waitInfoAvailable();
/*     */ 
/*  69 */         if (localJimiRasterImage.isError()) {
/*  70 */           this.seriesFinished = true;
/*  71 */           return getCachedImage(paramInt);
/*     */         }
/*  73 */         this.cacheSize += 1;
/*  74 */         this.cachedImages.addElement(localJimiRasterImage);
/*     */       }
/*  76 */       return localJimiRasterImage;
/*     */     }
/*     */ 
/*  80 */     return getCachedImage(paramInt);
/*     */   }
/*     */ 
/*     */   public JimiRasterImage getNextImage()
/*     */   {
/* 119 */     return getImage(++this.currentIndex);
/*     */   }
/*     */ 
/*     */   public JimiRasterImage getPreviousImage()
/*     */   {
/* 124 */     return getImage(--this.currentIndex);
/*     */   }
/*     */ 
/*     */   public boolean hasNextImage()
/*     */   {
/*     */     try {
/* 130 */       return getImage(this.currentIndex + 1) != null;
/*     */     } catch (NoSuchElementException localNoSuchElementException) {
/*     */     }
/* 133 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean hasPreviousImage()
/*     */   {
/* 140 */     return this.currentIndex > 0;
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.component.ImageCache
 * JD-Core Version:    0.6.2
 */