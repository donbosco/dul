/*     */ package com.sun.jimi.core.component;
/*     */ 
/*     */ import com.sun.jimi.core.Jimi;
/*     */ import com.sun.jimi.core.JimiImage;
/*     */ import com.sun.jimi.core.raster.JimiRasterImage;
/*     */ import com.sun.jimi.core.util.GraphicsUtils;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Image;
/*     */ import java.awt.Panel;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.event.ComponentAdapter;
/*     */ import java.awt.event.ComponentEvent;
/*     */ import java.awt.image.ImageProducer;
/*     */ 
/*     */ public abstract class AbstractRenderer extends Panel
/*     */   implements JimiImageRenderer
/*     */ {
/*     */   public JimiRasterImage raster;
/*     */   public Image image;
/*     */   public ImageProducer producer;
/*     */   public JimiCanvas canvas;
/*     */ 
/*     */   public AbstractRenderer()
/*     */   {
/*  20 */     addComponentListener(new ResizeWatcher());
/*     */   }
/*     */ 
/*     */   public final void blankBackground(Graphics paramGraphics)
/*     */   {
/* 240 */     paramGraphics.setColor(getForeground());
/* 241 */     paramGraphics.fillRect(0, 0, this.canvas.size().width, this.canvas.size().height);
/*     */   }
/*     */ 
/*     */   public Component getContentPane()
/*     */   {
/*  86 */     if (this.image != null) {
/*  87 */       render();
/*     */     }
/*  89 */     return this;
/*     */   }
/*     */ 
/*     */   public Image getImage()
/*     */   {
/* 202 */     if (this.image != null) {
/* 203 */       return this.image;
/*     */     }
/* 205 */     if (this.producer != null) {
/* 206 */       Image localImage = Toolkit.getDefaultToolkit().createImage(this.producer);
/* 207 */       GraphicsUtils.waitForImage(localImage);
/* 208 */       this.image = localImage;
/* 209 */       return localImage;
/*     */     }
/*     */ 
/* 212 */     return null;
/*     */   }
/*     */ 
/*     */   public Dimension getPreferredSize()
/*     */   {
/* 102 */     if (this.image != null) {
/* 103 */       return new Dimension(this.image.getWidth(this), this.image.getHeight(this));
/*     */     }
/* 105 */     return super.getPreferredSize();
/*     */   }
/*     */ 
/*     */   public JimiRasterImage getRasterImage()
/*     */   {
/* 218 */     if (this.raster != null) {
/* 219 */       return this.raster;
/*     */     }
/* 221 */     if (this.producer != null)
/*     */       try {
/* 223 */         return Jimi.createRasterImage(this.producer);
/*     */       }
/*     */       catch (Exception localException) {
/*     */       }
/* 227 */     return null;
/*     */   }
/*     */ 
/*     */   public synchronized void paint(Graphics paramGraphics)
/*     */   {
/* 113 */     blankBackground(paramGraphics);
/* 114 */     if (this.producer == null)
/*     */     {
/* 116 */       return;
/*     */     }
/*     */ 
/* 120 */     Image localImage = getImage();
/* 121 */     int i = size().width;
/* 122 */     int j = size().height;
/*     */ 
/* 124 */     int k = 0;
/* 125 */     int m = 0;
/*     */ 
/* 127 */     int n = localImage.getWidth(this);
/* 128 */     int i1 = localImage.getHeight(this);
/*     */ 
/* 131 */     switch (this.canvas.getJustificationPolicy())
/*     */     {
/*     */     case 0:
/* 135 */       m = (i - n) / 2;
/* 136 */       k = (j - i1) / 2;
/* 137 */       break;
/*     */     case 18:
/* 141 */       m = 0;
/* 142 */       k = 0;
/* 143 */       break;
/*     */     case 10:
/* 147 */       m = i - n;
/* 148 */       k = 0;
/* 149 */       break;
/*     */     case 2:
/* 153 */       m = (i - n) / 2;
/* 154 */       k = 0;
/* 155 */       break;
/*     */     case 4:
/* 159 */       m = (i - n) / 2;
/* 160 */       k = j - i1;
/* 161 */       break;
/*     */     case 20:
/* 165 */       m = 0;
/* 166 */       k = j - i1;
/* 167 */       break;
/*     */     case 12:
/* 171 */       m = i - n;
/* 172 */       k = j - i1;
/* 173 */       break;
/*     */     case 8:
/* 177 */       m = i - n;
/* 178 */       k = (j - i1) / 2;
/* 179 */       break;
/*     */     case 16:
/* 183 */       m = 0;
/* 184 */       k = (j - i1) / 2;
/* 185 */       break;
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
/* 192 */     case 19: } paramGraphics.drawImage(localImage, m, k, this);
/*     */   }
/*     */ 
/*     */   public void render()
/*     */   {
/*  97 */     repaint();
/*     */   }
/*     */ 
/*     */   public void setImage(Image paramImage)
/*     */   {
/*  68 */     if (paramImage == null) {
/*  69 */       this.image = null;
/*  70 */       this.producer = null;
/*  71 */       this.raster = null;
/*     */     }
/*     */     else {
/*  74 */       this.image = paramImage;
/*  75 */       this.producer = paramImage.getSource();
/*     */     }
/*  77 */     render();
/*     */   }
/*     */ 
/*     */   public void setImageProducer(ImageProducer paramImageProducer)
/*     */   {
/*  49 */     if (paramImageProducer == null) {
/*  50 */       setImage(null);
/*     */     }
/*     */     else {
/*  53 */       this.image = null;
/*  54 */       this.producer = paramImageProducer;
/*  55 */       this.raster = null;
/*  56 */       render();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setRasterImage(JimiRasterImage paramJimiRasterImage)
/*     */   {
/*  30 */     if (paramJimiRasterImage == null) {
/*  31 */       setImage(null);
/*     */     }
/*     */     else {
/*  34 */       this.raster = paramJimiRasterImage;
/*  35 */       this.producer = paramJimiRasterImage.getImageProducer();
/*  36 */       this.image = null;
/*     */ 
/*  38 */       render();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void update(Graphics paramGraphics)
/*     */   {
/* 197 */     paint(paramGraphics);
/*     */   }
/*     */ 
/*     */   public class ResizeWatcher extends ComponentAdapter
/*     */   {
/*     */     public ResizeWatcher()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void componentResized(ComponentEvent paramComponentEvent)
/*     */     {
/* 234 */       AbstractRenderer.this.render();
/*     */     }
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.component.AbstractRenderer
 * JD-Core Version:    0.6.2
 */