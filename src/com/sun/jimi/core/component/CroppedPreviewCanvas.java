/*     */ package com.sun.jimi.core.component;
/*     */ 
/*     */ import com.sun.jimi.core.JimiImage;
/*     */ import com.sun.jimi.core.raster.JimiRasterImage;
/*     */ import com.sun.jimi.core.util.GraphicsUtils;
/*     */ import java.awt.Canvas;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Image;
/*     */ import java.awt.ScrollPane;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.image.ImageProducer;
/*     */ 
/*     */ public class CroppedPreviewCanvas extends Canvas
/*     */ {
/*     */   protected int cropWidth;
/*     */   protected int cropHeight;
/*     */   protected JimiRasterImage rasterImage;
/*     */   protected Image previewImage;
/*     */   protected Image completeImage;
/*     */ 
/*     */   public CroppedPreviewCanvas()
/*     */   {
/*     */   }
/*     */ 
/*     */   public CroppedPreviewCanvas(int paramInt1, int paramInt2)
/*     */   {
/*  44 */     setCropDimensions(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public CroppedPreviewCanvas(int paramInt1, int paramInt2, JimiRasterImage paramJimiRasterImage)
/*     */   {
/*  49 */     this(paramInt1, paramInt2);
/*  50 */     setImage(paramJimiRasterImage);
/*     */   }
/*     */ 
/*     */   protected void createCompleteImage()
/*     */   {
/* 142 */     this.completeImage = createImage(this.rasterImage.getImageProducer());
/* 143 */     GraphicsUtils.waitForImage(this, this.completeImage);
/*     */   }
/*     */ 
/*     */   protected void createPreviewImage()
/*     */   {
/* 126 */     int i = Math.min(this.cropWidth, this.rasterImage.getWidth());
/* 127 */     int j = Math.min(this.cropHeight, this.rasterImage.getHeight());
/* 128 */     if ((i != this.rasterImage.getWidth()) || (j != this.rasterImage.getHeight())) {
/* 129 */       ImageProducer localImageProducer = this.rasterImage.getCroppedImageProducer(0, 0, i, j);
/* 130 */       this.previewImage = createImage(localImageProducer);
/* 131 */       GraphicsUtils.waitForImage(this, this.previewImage);
/*     */     }
/* 133 */     if ((i == this.rasterImage.getWidth()) && (j == this.rasterImage.getHeight())) {
/* 134 */       this.previewImage = createImage(this.rasterImage.getImageProducer());
/* 135 */       GraphicsUtils.waitForImage(this, this.previewImage);
/* 136 */       this.completeImage = this.previewImage;
/*     */     }
/*     */   }
/*     */ 
/*     */   public Dimension getPreferredSize()
/*     */   {
/*  55 */     if (this.rasterImage != null) {
/*  56 */       return new Dimension(this.rasterImage.getWidth(), this.rasterImage.getHeight());
/*     */     }
/*     */ 
/*  59 */     return new Dimension(610, 410);
/*     */   }
/*     */ 
/*     */   public synchronized void paint(Graphics paramGraphics)
/*     */   {
/*  88 */     if (this.rasterImage == null) {
/*  89 */       return;
/*     */     }
/*  91 */     int i = 0; int j = 0;
/*  92 */     if (this.rasterImage.getWidth() < this.cropWidth)
/*  93 */       i = (this.cropWidth - this.rasterImage.getWidth()) / 2;
/*  94 */     if (this.rasterImage.getHeight() < this.cropHeight)
/*  95 */       j = (this.cropHeight - this.rasterImage.getHeight()) / 2;
/*  96 */     if ((this.rasterImage == null) || (this.rasterImage.isError())) {
/*  97 */       return;
/*     */     }
/*  99 */     Dimension localDimension = getSize();
/*     */ 
/* 101 */     if (this.completeImage != null) {
/* 102 */       if ((getParent() instanceof ScrollPane)) {
/* 103 */         getParent().enable();
/*     */       }
/* 105 */       paramGraphics.drawImage(this.completeImage, i, j, null);
/*     */     }
/* 107 */     else if (this.previewImage != null) {
/* 108 */       if ((getParent() instanceof ScrollPane)) {
/* 109 */         getParent().disable();
/*     */       }
/* 111 */       paramGraphics.drawImage(this.previewImage, i, j, null);
/* 112 */       getToolkit().sync();
/* 113 */       if (this.completeImage == null) {
/* 114 */         createCompleteImage();
/*     */       }
/* 116 */       paint(paramGraphics);
/*     */     }
/*     */     else {
/* 119 */       createPreviewImage();
/* 120 */       paint(paramGraphics);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setCropDimensions(int paramInt1, int paramInt2)
/*     */   {
/*  81 */     this.cropWidth = paramInt1;
/*  82 */     this.cropHeight = paramInt2;
/*  83 */     repaint();
/*     */   }
/*     */ 
/*     */   public synchronized void setImage(JimiRasterImage paramJimiRasterImage)
/*     */   {
/*  65 */     paramJimiRasterImage.waitInfoAvailable();
/*  66 */     this.rasterImage = paramJimiRasterImage;
/*  67 */     if (this.previewImage != null) {
/*  68 */       this.previewImage.flush();
/*     */     }
/*  70 */     if (this.completeImage != null) {
/*  71 */       this.completeImage.flush();
/*     */     }
/*  73 */     this.previewImage = null;
/*  74 */     this.completeImage = null;
/*  75 */     update(getGraphics());
/*  76 */     getParent().invalidate();
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.component.CroppedPreviewCanvas
 * JD-Core Version:    0.6.2
 */