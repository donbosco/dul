/*    */ package com.sun.jimi.core.component;
/*    */ 
/*    */ import com.sun.jimi.core.filters.AreaAverageScaleFilter;
/*    */ import com.sun.jimi.core.filters.ReplicatingScaleFilter;
/*    */ import com.sun.jimi.core.util.GraphicsUtils;
/*    */ import java.awt.Component;
/*    */ import java.awt.Dimension;
/*    */ import java.awt.Toolkit;
/*    */ import java.awt.image.FilteredImageSource;
/*    */ import java.awt.image.ImageFilter;
/*    */ 
/*    */ public class ScaleRenderer extends AbstractRenderer
/*    */ {
/*    */   public ScaleRenderer(JimiCanvas paramJimiCanvas)
/*    */   {
/* 15 */     this.canvas = paramJimiCanvas;
/*    */   }
/*    */ 
/*    */   public void render()
/*    */   {
/* 20 */     if (this.producer == null) {
/* 21 */       return;
/*    */     }
/*    */ 
/* 25 */     int i = this.canvas.size().width;
/* 26 */     int j = this.canvas.size().height;
/*    */     Object localObject;
/* 29 */     if (this.canvas.getScalingPolicy() == 0)
/* 30 */       localObject = new AreaAverageScaleFilter(i, j);
/*    */     else {
/* 32 */       localObject = new ReplicatingScaleFilter(i, j);
/*    */     }
/* 34 */     FilteredImageSource localFilteredImageSource = new FilteredImageSource(this.producer, (ImageFilter)localObject);
/* 35 */     this.image = Toolkit.getDefaultToolkit().createImage(localFilteredImageSource);
/* 36 */     GraphicsUtils.waitForImage(this.image);
/*    */ 
/* 38 */     repaint();
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.component.ScaleRenderer
 * JD-Core Version:    0.6.2
 */