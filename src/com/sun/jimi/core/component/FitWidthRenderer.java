/*     */ package com.sun.jimi.core.component;
/*     */ 
/*     */ import com.sun.jimi.core.Jimi;
/*     */ import com.sun.jimi.core.JimiException;
/*     */ import com.sun.jimi.core.JimiImage;
/*     */ import com.sun.jimi.core.raster.JimiRasterImage;
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
/*     */ public class FitWidthRenderer extends Panel
/*     */   implements JimiImageRenderer, AdjustmentListener
/*     */ {
/*     */   protected JimiRasterImage rasterImage;
/*     */   protected Scrollbar scroller;
/*     */   protected FitToWidthPanel panel;
/*     */   protected JimiCanvas canvas;
/*     */ 
/*     */   public FitWidthRenderer(JimiCanvas paramJimiCanvas)
/*     */   {
/*  36 */     this.canvas = paramJimiCanvas;
/*     */ 
/*  38 */     this.panel = new FitToWidthPanel(paramJimiCanvas);
/*     */ 
/*  40 */     this.scroller = new Scrollbar(1, 0, 10, 0, 100);
/*  41 */     this.scroller.setUnitIncrement(10);
/*  42 */     this.scroller.addAdjustmentListener(this);
/*  43 */     this.scroller.setVisible(false);
/*     */ 
/*  45 */     this.scroller.addAdjustmentListener(this);
/*  46 */     addComponentListener(new ResizeListener());
/*  47 */     setLayout(new BorderLayout());
/*  48 */     add(this.panel, "Center");
/*  49 */     add(this.scroller, "East");
/*     */ 
/*  51 */     setBackground(paramJimiCanvas.getBackground());
/*  52 */     this.panel.setBackground(paramJimiCanvas.getBackground());
/*     */   }
/*     */ 
/*     */   public void adjustmentValueChanged(AdjustmentEvent paramAdjustmentEvent)
/*     */   {
/* 150 */     this.panel.setPosition(this.scroller.getValue());
/*     */   }
/*     */ 
/*     */   public void calibrateScrolling()
/*     */   {
/*  57 */     if ((this.rasterImage == null) || 
/*  58 */       (this.rasterImage.getWidth() == 0) || 
/*  59 */       (this.rasterImage.getHeight() == 0)) {
/*  60 */       return;
/*     */     }
/*     */ 
/*  63 */     int i = this.panel.getSize().width;
/*  64 */     int j = this.panel.getSize().height;
/*     */ 
/*  66 */     if ((i == 0) || (j == 0)) {
/*  67 */       return;
/*     */     }
/*     */ 
/*  70 */     int k = this.rasterImage.getWidth();
/*  71 */     int m = this.rasterImage.getHeight();
/*     */ 
/*  73 */     int n = m * i / k;
/*     */ 
/*  76 */     if (n > j) {
/*  77 */       this.scroller.setValues(0, j, 0, n);
/*  78 */       this.scroller.setBlockIncrement(j);
/*  79 */       this.scroller.setPageIncrement(j);
/*  80 */       this.scroller.setVisible(true);
/*     */     }
/*     */     else {
/*  83 */       this.scroller.setVisible(false);
/*     */     }
/*  85 */     validate();
/*     */   }
/*     */ 
/*     */   public Component getContentPane()
/*     */   {
/* 140 */     return this;
/*     */   }
/*     */ 
/*     */   public void render()
/*     */   {
/* 145 */     this.panel.redraw();
/*     */   }
/*     */ 
/*     */   public void setImage(Image paramImage)
/*     */   {
/* 107 */     if (paramImage == null)
/* 108 */       return;
/*     */     try
/*     */     {
/* 111 */       setRasterImage(Jimi.createRasterImage(paramImage.getSource()));
/*     */     }
/*     */     catch (JimiException localJimiException) {
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setImageProducer(ImageProducer paramImageProducer) {
/* 118 */     if (paramImageProducer == null)
/* 119 */       return;
/*     */     try
/*     */     {
/* 122 */       setRasterImage(Jimi.createRasterImage(paramImageProducer));
/*     */     }
/*     */     catch (JimiException localJimiException) {
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setRasterImage(JimiRasterImage paramJimiRasterImage) {
/* 129 */     if (paramJimiRasterImage == null) {
/* 130 */       return;
/*     */     }
/* 132 */     paramJimiRasterImage.waitFinished();
/* 133 */     this.rasterImage = paramJimiRasterImage;
/* 134 */     this.panel.setImageProducer(paramJimiRasterImage.getImageProducer());
/* 135 */     calibrateScrolling();
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
/*  92 */       FitWidthRenderer.this.calibrateScrolling();
/*     */     }
/*     */   }
/*     */ 
/*     */   class ResizeWatcher extends ComponentAdapter {
/*     */     ResizeWatcher() {
/*     */     }
/*     */ 
/* 100 */     public void componentResized(ComponentEvent paramComponentEvent) { FitWidthRenderer.this.calibrateScrolling();
/* 101 */       FitWidthRenderer.this.panel.redraw();
/*     */     }
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.component.FitWidthRenderer
 * JD-Core Version:    0.6.2
 */