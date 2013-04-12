/*    */ package com.sun.jimi.core.component;
/*    */ 
/*    */ import com.sun.jimi.core.Jimi;
/*    */ import com.sun.jimi.core.JimiException;
/*    */ import com.sun.jimi.core.raster.JimiRasterImage;
/*    */ import com.sun.jimi.core.util.GraphicsUtils;
/*    */ import java.awt.Component;
/*    */ import java.awt.Graphics;
/*    */ import java.awt.Image;
/*    */ import java.awt.Toolkit;
/*    */ import java.awt.event.AdjustmentEvent;
/*    */ import java.awt.event.AdjustmentListener;
/*    */ 
/*    */ public class AreaRenderer extends AbstractRenderer
/*    */   implements AdjustmentListener
/*    */ {
/* 22 */   private int cropX = 0;
/* 23 */   private int cropY = 0;
/* 24 */   private int cropWidth = 0;
/* 25 */   private int cropHeight = 0;
/* 26 */   private int parts = 4;
/*    */   private Image[] images;
/*    */   private JimiScrollPane jsp;
/*    */   private Image bufferImage;
/*    */   private Graphics buffer;
/*    */ 
/*    */   public AreaRenderer(JimiCanvas paramJimiCanvas)
/*    */   {
/* 36 */     this.canvas = paramJimiCanvas;
/*    */ 
/* 38 */     this.jsp = new JimiScrollPane();
/* 39 */     this.jsp.add(this);
/*    */ 
/* 46 */     this.images = new Image[this.parts];
/*    */   }
/*    */ 
/*    */   public void adjustmentValueChanged(AdjustmentEvent paramAdjustmentEvent)
/*    */   {
/* 97 */     if ((this.cropX > this.cropWidth) || (this.cropY > this.cropHeight))
/*    */     {
/* 99 */       render();
/*    */     }
/*    */   }
/*    */ 
/*    */   public Component getContentPane()
/*    */   {
/* 50 */     return this.jsp;
/*    */   }
/*    */ 
/*    */   public void paint(Graphics paramGraphics)
/*    */   {
/* 90 */     if (this.bufferImage != null)
/* 91 */       paramGraphics.drawImage(this.bufferImage, 0, 0, this);
/*    */   }
/*    */ 
/*    */   public void render()
/*    */   {
/* 55 */     this.cropWidth = (this.image.getWidth(null) / this.parts);
/* 56 */     int i = this.image.getWidth(null) % this.parts;
/* 57 */     this.cropHeight = this.image.getHeight(null);
/*    */ 
/* 60 */     this.cropX = this.jsp.getHorizontalPosition();
/* 61 */     this.cropY = this.jsp.getVerticalPosition();
/*    */     try
/*    */     {
/* 64 */       this.raster = Jimi.createRasterImage(this.image.getSource());
/*    */     }
/*    */     catch (JimiException localJimiException)
/*    */     {
/*    */     }
/* 69 */     this.bufferImage = this.canvas.createImage(this.image.getWidth(null), this.image.getHeight(null));
/* 70 */     this.buffer = this.bufferImage.getGraphics();
/*    */ 
/* 73 */     if (this.cropWidth % 2 != 0) {
/* 74 */       this.cropWidth += 1;
/*    */     }
/*    */ 
/* 77 */     for (int j = 0; j < this.parts; j++)
/*    */     {
/* 79 */       this.producer = this.raster.getCroppedImageProducer(j * this.cropWidth, -j, this.cropWidth, this.cropHeight);
/* 80 */       this.images[j] = Toolkit.getDefaultToolkit().createImage(this.producer);
/*    */ 
/* 82 */       this.buffer.drawImage(this.images[j], j * this.cropWidth, 0, null);
/*    */     }
/*    */ 
/* 85 */     GraphicsUtils.waitForImage(this.image);
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.component.AreaRenderer
 * JD-Core Version:    0.6.2
 */