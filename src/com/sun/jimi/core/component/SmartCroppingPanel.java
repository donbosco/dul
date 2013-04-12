/*     */ package com.sun.jimi.core.component;
/*     */ 
/*     */ import com.sun.jimi.core.raster.JimiRasterImage;
/*     */ import com.sun.jimi.core.util.GraphicsUtils;
/*     */ import java.awt.Canvas;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Image;
/*     */ import java.awt.Point;
/*     */ import java.awt.Rectangle;
/*     */ 
/*     */ public class SmartCroppingPanel extends Canvas
/*     */ {
/*     */   public static final int HORIZONTAL_PADDING = 150;
/*     */   public static final int VERTICAL_PADDING = 150;
/*     */   protected JimiRasterImage raster;
/*     */   protected Image cached;
/*     */   protected Rectangle cachedArea;
/*  36 */   protected Point position = new Point(0, 0);
/*  37 */   protected Rectangle viewingArea = new Rectangle();
/*     */   protected JimiCanvas canvas;
/*  39 */   protected boolean needsRedraw = true;
/*     */ 
/* 234 */   private Point offset = new Point();
/*     */ 
/*     */   public SmartCroppingPanel(JimiCanvas paramJimiCanvas)
/*     */   {
/*  43 */     this.canvas = paramJimiCanvas;
/*     */   }
/*     */ 
/*     */   protected Point calculatePosition()
/*     */   {
/* 238 */     int i = size().width;
/* 239 */     int j = size().height;
/*     */ 
/* 241 */     int k = 0;
/* 242 */     int m = 0;
/*     */ 
/* 244 */     int n = this.raster.getWidth();
/* 245 */     int i1 = this.raster.getHeight();
/*     */ 
/* 248 */     switch (this.canvas.getJustificationPolicy())
/*     */     {
/*     */     case 0:
/* 252 */       m = (i - n) / 2;
/* 253 */       k = (j - i1) / 2;
/* 254 */       break;
/*     */     case 18:
/* 258 */       m = 0;
/* 259 */       k = 0;
/* 260 */       break;
/*     */     case 10:
/* 264 */       m = i - n;
/* 265 */       k = 0;
/* 266 */       break;
/*     */     case 2:
/* 270 */       m = (i - n) / 2;
/* 271 */       k = 0;
/* 272 */       break;
/*     */     case 4:
/* 276 */       m = (i - n) / 2;
/* 277 */       k = j - i1;
/* 278 */       break;
/*     */     case 20:
/* 282 */       m = 0;
/* 283 */       k = j - i1;
/* 284 */       break;
/*     */     case 12:
/* 288 */       m = i - n;
/* 289 */       k = j - i1;
/* 290 */       break;
/*     */     case 8:
/* 294 */       m = i - n;
/* 295 */       k = (j - i1) / 2;
/* 296 */       break;
/*     */     case 16:
/* 300 */       m = 0;
/* 301 */       k = (j - i1) / 2;
/* 302 */       break;
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
/* 306 */     case 19: } this.offset.x = m;
/* 307 */     this.offset.y = k;
/*     */ 
/* 310 */     if (i < n) {
/* 311 */       this.offset.x = (this.cachedArea.x - this.position.x);
/*     */     }
/* 313 */     if (j < i1) {
/* 314 */       this.offset.y = (this.cachedArea.y - this.position.y);
/*     */     }
/* 316 */     return this.offset;
/*     */   }
/*     */ 
/*     */   protected boolean isCacheValid()
/*     */   {
/*  87 */     Dimension localDimension = getSize();
/*  88 */     return (this.position.x >= this.cachedArea.x) && 
/*  89 */       (this.position.y >= this.cachedArea.y) && 
/*  90 */       (this.position.x + localDimension.width < this.cachedArea.x + this.cachedArea.width) && 
/*  91 */       (this.position.y + localDimension.height < this.cachedArea.y + this.cachedArea.height);
/*     */   }
/*     */ 
/*     */   public synchronized void paint(Graphics paramGraphics)
/*     */   {
/* 107 */     if ((this.raster == null) || (this.needsRedraw)) {
/* 108 */       paramGraphics.fillRect(0, 0, size().width, size().height);
/* 109 */       this.needsRedraw = false;
/*     */     }
/*     */ 
/* 112 */     if (this.raster != null) {
/* 113 */       if ((this.cached == null) || (!isCacheValid())) {
/* 114 */         updateCache();
/*     */       }
/*     */ 
/* 119 */       int i = size().width;
/* 120 */       int j = size().height;
/*     */ 
/* 122 */       int k = 0;
/* 123 */       int m = 0;
/*     */ 
/* 125 */       int n = this.raster.getWidth();
/* 126 */       int i1 = this.raster.getHeight();
/*     */ 
/* 129 */       switch (this.canvas.getJustificationPolicy())
/*     */       {
/*     */       case 0:
/* 133 */         m = (i - n) / 2;
/* 134 */         k = (j - i1) / 2;
/* 135 */         break;
/*     */       case 18:
/* 139 */         m = 0;
/* 140 */         k = 0;
/* 141 */         break;
/*     */       case 10:
/* 145 */         m = i - n;
/* 146 */         k = 0;
/* 147 */         break;
/*     */       case 2:
/* 151 */         m = (i - n) / 2;
/* 152 */         k = 0;
/* 153 */         break;
/*     */       case 4:
/* 157 */         m = (i - n) / 2;
/* 158 */         k = j - i1;
/* 159 */         break;
/*     */       case 20:
/* 163 */         m = 0;
/* 164 */         k = j - i1;
/* 165 */         break;
/*     */       case 12:
/* 169 */         m = i - n;
/* 170 */         k = j - i1;
/* 171 */         break;
/*     */       case 8:
/* 175 */         m = i - n;
/* 176 */         k = (j - i1) / 2;
/* 177 */         break;
/*     */       case 16:
/* 181 */         m = 0;
/* 182 */         k = (j - i1) / 2;
/* 183 */         break;
/*     */       case 1:
/*     */       case 3:
/*     */       case 5:
/*     */       case 6:
/*     */       case 7:
/*     */       case 9:
/*     */       case 11:
/*     */       case 13:
/*     */       case 14:
/*     */       case 15:
/*     */       case 17:
/* 188 */       case 19: } if ((i > n) && (j > i1)) {
/* 189 */         paramGraphics.fillRect(0, 0, i, j);
/*     */       }
/*     */       else
/*     */       {
/*     */         int i2;
/* 192 */         if (j > i1) {
/* 193 */           i2 = this.canvas.getJustificationPolicy();
/* 194 */           if ((i2 & 0x2) != 0) {
/* 195 */             paramGraphics.fillRect(0, i1, i, j - i1);
/*     */           }
/* 197 */           else if ((i2 & 0x4) != 0) {
/* 198 */             paramGraphics.fillRect(0, 0, i, j - i1);
/*     */           }
/*     */           else {
/* 201 */             paramGraphics.fillRect(0, 0, i, (j - i1) / 2);
/* 202 */             paramGraphics.fillRect(0, i1 + (j - i1) / 2, i, (j - i1) / 2);
/*     */           }
/*     */         }
/*     */ 
/* 206 */         if (i > n) {
/* 207 */           i2 = this.canvas.getJustificationPolicy();
/* 208 */           if ((i2 & 0x8) != 0) {
/* 209 */             paramGraphics.fillRect(0, 0, i - n, j);
/*     */           }
/* 211 */           else if ((i2 & 0x10) != 0) {
/* 212 */             paramGraphics.fillRect(n, 0, i - n, j);
/*     */           }
/*     */           else {
/* 215 */             paramGraphics.fillRect(0, 0, (i - n) / 2, j);
/* 216 */             paramGraphics.fillRect(n + (i - n) / 2, 0, (i - n) / 2, j);
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 222 */       if (i < n) {
/* 223 */         m = this.cachedArea.x - this.position.x;
/*     */       }
/* 225 */       if (j < i1) {
/* 226 */         k = this.cachedArea.y - this.position.y;
/*     */       }
/*     */ 
/* 229 */       paramGraphics.drawImage(this.cached, m, k, null);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void redraw()
/*     */   {
/* 101 */     this.needsRedraw = true;
/* 102 */     repaint();
/*     */   }
/*     */ 
/*     */   public void setImage(JimiRasterImage paramJimiRasterImage)
/*     */   {
/*  56 */     this.raster = paramJimiRasterImage;
/*  57 */     updateCache();
/*     */ 
/*  59 */     setPosition(0, 0);
/*  60 */     redraw();
/*     */   }
/*     */ 
/*     */   public void setPosition(int paramInt1, int paramInt2)
/*     */   {
/*  48 */     this.position.x = paramInt1;
/*  49 */     this.position.y = paramInt2;
/*     */ 
/*  51 */     repaint();
/*     */   }
/*     */ 
/*     */   public void update(Graphics paramGraphics)
/*     */   {
/*  96 */     paint(paramGraphics);
/*     */   }
/*     */ 
/*     */   protected void updateCache()
/*     */   {
/*  65 */     if (this.cached != null) {
/*  66 */       this.cached.flush();
/*     */     }
/*  68 */     Dimension localDimension = getSize();
/*     */ 
/*  70 */     int i = Math.max(0, this.position.x - 150);
/*     */ 
/*  72 */     int j = Math.min(this.raster.getWidth(), this.position.x + localDimension.width + 150);
/*     */ 
/*  74 */     int k = Math.max(0, this.position.y - 150);
/*     */ 
/*  76 */     int m = Math.min(this.raster.getHeight(), this.position.y + localDimension.height + 150);
/*     */ 
/*  78 */     this.cachedArea = new Rectangle(i, k, 
/*  79 */       j - i, m - k);
/*  80 */     this.cached = createImage(
/*  81 */       this.raster.getCroppedImageProducer(this.cachedArea.x, this.cachedArea.y, this.cachedArea.width, this.cachedArea.height));
/*  82 */     GraphicsUtils.waitForImage(this.cached);
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.component.SmartCroppingPanel
 * JD-Core Version:    0.6.2
 */