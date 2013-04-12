/*     */ package com.sun.jimi.core.component;
/*     */ 
/*     */ import com.sun.jimi.core.Jimi;
/*     */ import com.sun.jimi.core.JimiException;
/*     */ import com.sun.jimi.core.JimiImage;
/*     */ import com.sun.jimi.core.raster.JimiRasterImage;
/*     */ import java.awt.AWTEvent;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Image;
/*     */ import java.awt.Panel;
/*     */ import java.awt.Scrollbar;
/*     */ import java.awt.event.AdjustmentEvent;
/*     */ import java.awt.event.AdjustmentListener;
/*     */ import java.awt.event.ComponentAdapter;
/*     */ import java.awt.event.ComponentEvent;
/*     */ import java.awt.image.ImageProducer;
/*     */ 
/*     */ public class SmartScrollingRenderer extends Panel
/*     */   implements JimiImageRenderer, AdjustmentListener
/*     */ {
/*     */   protected JimiRasterImage rasterImage;
/*     */   protected Scrollbar vsb;
/*     */   protected Scrollbar hsb;
/*     */   protected SmartCroppingPanel cropper;
/*     */   protected JimiCanvas canvas;
/* 166 */   static int count = 0;
/*     */ 
/*     */   public SmartScrollingRenderer(JimiCanvas paramJimiCanvas)
/*     */   {
/*  40 */     this.canvas = paramJimiCanvas;
/*     */ 
/*  42 */     this.vsb = new Scrollbar(1, 0, 10, 0, 100);
/*  43 */     this.hsb = new Scrollbar(0, 0, 10, 0, 100);
/*  44 */     this.vsb.setUnitIncrement(10);
/*  45 */     this.hsb.setUnitIncrement(10);
/*     */ 
/*  47 */     this.vsb.addAdjustmentListener(this);
/*  48 */     this.hsb.addAdjustmentListener(this);
/*     */ 
/*  50 */     addComponentListener(new ResizeListener());
/*     */ 
/*  52 */     this.cropper = new SmartCroppingPanel(paramJimiCanvas);
/*  53 */     setLayout(new BorderLayout());
/*  54 */     add("Center", this.cropper);
/*  55 */     add("East", this.vsb);
/*  56 */     add("South", this.hsb);
/*     */ 
/*  58 */     this.vsb.setVisible(false); this.hsb.setVisible(false);
/*     */ 
/*  60 */     addComponentListener(new ResizeWatcher());
/*     */   }
/*     */ 
/*     */   public void adjustmentValueChanged(AdjustmentEvent paramAdjustmentEvent)
/*     */   {
/* 149 */     switch (paramAdjustmentEvent.getID())
/*     */     {
/*     */     case 601:
/*     */     }
/*     */ 
/* 155 */     this.cropper.setPosition(this.hsb.getValue(), this.vsb.getValue());
/*     */   }
/*     */ 
/*     */   protected void calibrateScrolling()
/*     */   {
/*  65 */     if (this.rasterImage != null) {
/*  66 */       int i = this.rasterImage.getWidth();
/*  67 */       int j = this.rasterImage.getHeight();
/*  68 */       int k = this.cropper.getSize().width;
/*  69 */       int m = this.cropper.getSize().height;
/*     */ 
/*  72 */       if ((i <= k) && 
/*  73 */         (j <= m)) {
/*  74 */         this.hsb.setVisible(false);
/*  75 */         this.vsb.setVisible(false);
/*     */       }
/*     */       else
/*     */       {
/*  80 */         int n = i - k;
/*  81 */         int i1 = j - m;
/*     */ 
/*  83 */         this.vsb.setValues(0, m, 0, j);
/*  84 */         this.hsb.setValues(0, k, 0, i);
/*     */ 
/*  87 */         this.vsb.setBlockIncrement(m);
/*  88 */         this.vsb.setPageIncrement(m);
/*  89 */         this.hsb.setBlockIncrement(m);
/*  90 */         this.hsb.setPageIncrement(m);
/*     */ 
/*  92 */         this.hsb.setVisible(true);
/*  93 */         this.vsb.setVisible(true);
/*     */       }
/*     */ 
/*  96 */       validate();
/*     */     }
/*     */   }
/*     */ 
/*     */   public Component getContentPane()
/*     */   {
/* 137 */     return this;
/*     */   }
/*     */ 
/*     */   public void render()
/*     */   {
/* 143 */     this.cropper.redraw();
/*     */   }
/*     */ 
/*     */   public void setImage(Image paramImage)
/*     */   {
/* 106 */     if (paramImage == null)
/* 107 */       return;
/*     */     try
/*     */     {
/* 110 */       setRasterImage(Jimi.createRasterImage(paramImage.getSource()));
/*     */     } catch (JimiException localJimiException) {
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setImageProducer(ImageProducer paramImageProducer) {
/* 116 */     if (paramImageProducer == null)
/* 117 */       return;
/*     */     try
/*     */     {
/* 120 */       setRasterImage(Jimi.createRasterImage(paramImageProducer));
/*     */     } catch (JimiException localJimiException) {
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setRasterImage(JimiRasterImage paramJimiRasterImage) {
/* 126 */     if (paramJimiRasterImage == null) {
/* 127 */       return;
/*     */     }
/* 129 */     paramJimiRasterImage.waitFinished();
/* 130 */     this.rasterImage = paramJimiRasterImage;
/* 131 */     this.cropper.setImage(paramJimiRasterImage);
/* 132 */     calibrateScrolling();
/*     */   }
/*     */ 
/*     */   class ResizeListener extends ComponentAdapter
/*     */   {
/*     */     ResizeListener()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void componentResized(ComponentEvent paramComponentEvent)
/*     */     {
/* 162 */       SmartScrollingRenderer.this.calibrateScrolling();
/*     */     }
/*     */   }
/*     */ 
/*     */   class ResizeWatcher extends ComponentAdapter
/*     */   {
/*     */     ResizeWatcher() {
/*     */     }
/*     */ 
/*     */     public void componentResized(ComponentEvent paramComponentEvent) {
/* 172 */       SmartScrollingRenderer.this.calibrateScrolling();
/*     */     }
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.component.SmartScrollingRenderer
 * JD-Core Version:    0.6.2
 */