/*     */ package com.sun.jimi.core.component;
/*     */ 
/*     */ import com.sun.jimi.core.raster.JimiRasterImage;
/*     */ import com.sun.jimi.core.util.GraphicsUtils;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Image;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.image.ImageProducer;
/*     */ 
/*     */ public class CropRenderer extends AbstractRenderer
/*     */ {
/*  11 */   protected Rectangle cachedArea = new Rectangle();
/*     */   protected Image cacheImage;
/*     */ 
/*     */   public CropRenderer(JimiCanvas paramJimiCanvas)
/*     */   {
/*  15 */     this.canvas = paramJimiCanvas;
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics)
/*     */   {
/*  25 */     blankBackground(paramGraphics);
/*     */ 
/*  27 */     JimiRasterImage localJimiRasterImage = getRasterImage();
/*     */ 
/*  29 */     if (localJimiRasterImage == null) {
/*  30 */       return;
/*     */     }
/*  32 */     localJimiRasterImage.waitInfoAvailable();
/*     */ 
/*  35 */     int i = localJimiRasterImage.getWidth();
/*  36 */     int j = localJimiRasterImage.getHeight();
/*  37 */     int k = this.canvas.size().width;
/*  38 */     int m = this.canvas.size().height;
/*     */ 
/*  40 */     int n = 0;
/*  41 */     int i1 = 0;
/*  42 */     int i2 = i - k;
/*  43 */     int i3 = j - m;
/*     */ 
/*  45 */     int i4 = Math.min(i, k);
/*  46 */     int i5 = Math.min(j, m);
/*     */ 
/*  48 */     if (i2 < 0) {
/*  49 */       i2 = 0;
/*     */     }
/*     */ 
/*  52 */     if (i3 < 0) {
/*  53 */       i3 = 0;
/*     */     }
/*     */ 
/*  56 */     int i6 = i2 / 2;
/*  57 */     int i7 = i3 / 2;
/*     */ 
/*  64 */     switch (this.canvas.getJustificationPolicy())
/*     */     {
/*     */     case 0:
/*  69 */       n = i6;
/*  70 */       i1 = i7;
/*  71 */       break;
/*     */     case 18:
/*  75 */       n = 0;
/*  76 */       i1 = 0;
/*  77 */       break;
/*     */     case 16:
/*  81 */       n = 0;
/*  82 */       i1 = i7;
/*  83 */       break;
/*     */     case 20:
/*  87 */       n = 0;
/*  88 */       i1 = i3;
/*  89 */       break;
/*     */     case 4:
/*  93 */       n = i6;
/*  94 */       i1 = i3;
/*  95 */       break;
/*     */     case 12:
/*  99 */       n = i2;
/* 100 */       i1 = i3;
/* 101 */       break;
/*     */     case 8:
/* 105 */       n = i2;
/* 106 */       i1 = i7;
/* 107 */       break;
/*     */     case 10:
/* 111 */       n = i2;
/* 112 */       i1 = 0;
/* 113 */       break;
/*     */     case 2:
/* 117 */       n = i6;
/* 118 */       i1 = 0;
/* 119 */       break;
/*     */     case 1:
/*     */     case 3:
/*     */     case 5:
/*     */     case 6:
/*     */     case 7:
/*     */     case 9:
/*     */     case 11:
/*     */     case 13:
/*     */     case 14:
/*     */     case 15:
/*     */     case 17:
/* 122 */     case 19: } if ((this.cacheImage == null) || 
/* 123 */       (this.cachedArea.x != n) || 
/* 124 */       (this.cachedArea.y != i1) || 
/* 125 */       (this.cachedArea.width != i4) || 
/* 126 */       (this.cachedArea.height != i5)) {
/* 127 */       ImageProducer localImageProducer = localJimiRasterImage.getCroppedImageProducer(n, i1, i4, i5);
/* 128 */       this.cacheImage = Toolkit.getDefaultToolkit().createImage(localImageProducer);
/* 129 */       GraphicsUtils.waitForImage(this.cacheImage);
/* 130 */       this.cachedArea.x = n; this.cachedArea.y = i1;
/* 131 */       this.cachedArea.width = i4; this.cachedArea.height = i5;
/*     */     }
/*     */ 
/* 134 */     this.image = this.cacheImage;
/* 135 */     super.paint(paramGraphics);
/*     */   }
/*     */ 
/*     */   public void render()
/*     */   {
/*  20 */     repaint();
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.component.CropRenderer
 * JD-Core Version:    0.6.2
 */