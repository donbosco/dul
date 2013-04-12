/*     */ package com.sun.jimi.core.component;
/*     */ 
/*     */ import com.sun.jimi.core.util.GraphicsUtils;
/*     */ import java.awt.Canvas;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Image;
/*     */ import java.awt.image.FilteredImageSource;
/*     */ import java.awt.image.ImageProducer;
/*     */ 
/*     */ public class FitToWidthPanel extends Canvas
/*     */ {
/*     */   protected ImageProducer producer;
/*     */   protected Image cache;
/*     */   protected int position;
/*     */   protected boolean needsRedraw;
/*     */   protected JimiCanvas canvas;
/*     */ 
/*     */   public FitToWidthPanel(JimiCanvas paramJimiCanvas)
/*     */   {
/*  39 */     this.canvas = paramJimiCanvas;
/*     */   }
/*     */ 
/*     */   public synchronized void paint(Graphics paramGraphics)
/*     */   {
/*  64 */     if ((this.cache == null) || (this.needsRedraw)) {
/*  65 */       paramGraphics.fillRect(0, 0, getSize().width, getSize().height);
/*  66 */       this.needsRedraw = false;
/*     */     }
/*  68 */     if (this.producer == null) {
/*  69 */       return;
/*     */     }
/*  71 */     if ((this.cache == null) || (getSize().width != this.cache.getWidth(null))) {
/*  72 */       updateCache();
/*     */     }
/*  74 */     int i = Math.max(0, getSize().height - this.cache.getHeight(null));
/*  75 */     if (i > 0) this.position = 0;
/*     */     int j;
/*  77 */     if (i <= 0) {
/*  78 */       j = -this.position;
/*     */     }
/*     */     else {
/*  81 */       int k = this.canvas.getJustificationPolicy();
/*  82 */       if ((k & 0x2) != 0) {
/*  83 */         j = 0;
/*  84 */         paramGraphics.fillRect(0, getSize().height - i, getSize().width, i);
/*     */       }
/*  86 */       else if ((k & 0x4) != 0) {
/*  87 */         j = i;
/*  88 */         paramGraphics.fillRect(0, 0, getSize().width, j);
/*     */       }
/*     */       else {
/*  91 */         j = i / 2;
/*  92 */         paramGraphics.fillRect(0, 0, getSize().width, j);
/*  93 */         paramGraphics.fillRect(0, getSize().height - j, getSize().width, j);
/*     */       }
/*     */     }
/*  96 */     paramGraphics.drawImage(this.cache, 0, j, this);
/*     */   }
/*     */ 
/*     */   public synchronized void redraw()
/*     */   {
/*  58 */     this.needsRedraw = true;
/*  59 */     repaint();
/*     */   }
/*     */ 
/*     */   public void setImageProducer(ImageProducer paramImageProducer)
/*     */   {
/*  44 */     this.producer = paramImageProducer;
/*  45 */     this.cache = null;
/*  46 */     this.position = 0;
/*  47 */     redraw();
/*     */   }
/*     */ 
/*     */   public void setPosition(int paramInt)
/*     */   {
/*  52 */     this.position = paramInt;
/*  53 */     repaint();
/*     */   }
/*     */ 
/*     */   public void update(Graphics paramGraphics)
/*     */   {
/* 101 */     paint(paramGraphics);
/*     */   }
/*     */ 
/*     */   protected void updateCache()
/*     */   {
/* 106 */     AspectScaler localAspectScaler = new AspectScaler(getSize().width, 2147483647);
/* 107 */     FilteredImageSource localFilteredImageSource = new FilteredImageSource(this.producer, localAspectScaler);
/* 108 */     this.cache = createImage(localFilteredImageSource);
/* 109 */     GraphicsUtils.waitForImage(this.cache);
/* 110 */     this.position = 0;
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.component.FitToWidthPanel
 * JD-Core Version:    0.6.2
 */