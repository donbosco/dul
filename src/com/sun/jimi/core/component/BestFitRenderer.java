/*    */ package com.sun.jimi.core.component;
/*    */ 
/*    */ import com.sun.jimi.core.JimiImage;
/*    */ import com.sun.jimi.core.filters.ReplicatingScaleFilter;
/*    */ import com.sun.jimi.core.raster.JimiRasterImage;
/*    */ import com.sun.jimi.core.util.GraphicsUtils;
/*    */ import java.awt.Component;
/*    */ import java.awt.Dimension;
/*    */ import java.awt.Toolkit;
/*    */ import java.awt.image.AreaAveragingScaleFilter;
/*    */ import java.awt.image.FilteredImageSource;
/*    */ import java.awt.image.ImageFilter;
/*    */ 
/*    */ public class BestFitRenderer extends AbstractRenderer
/*    */ {
/*    */   public BestFitRenderer(JimiCanvas paramJimiCanvas)
/*    */   {
/* 16 */     this.canvas = paramJimiCanvas;
/*    */   }
/*    */ 
/*    */   public void render()
/*    */   {
/* 21 */     JimiRasterImage localJimiRasterImage = getRasterImage();
/* 22 */     if (localJimiRasterImage == null) {
/* 23 */       return;
/*    */     }
/* 25 */     int i = localJimiRasterImage.getWidth();
/* 26 */     int j = localJimiRasterImage.getHeight();
/* 27 */     int k = this.canvas.size().width;
/* 28 */     int m = this.canvas.size().height;
/*    */ 
/* 31 */     float f1 = k / i;
/* 32 */     float f2 = m / j;
/*    */     int n;
/*    */     int i1;
/* 39 */     if (f1 < f2)
/*    */     {
/* 41 */       n = (int)(i * f1);
/* 42 */       i1 = (int)(j * f1);
/*    */     }
/*    */     else
/*    */     {
/* 46 */       n = (int)(i * f2);
/* 47 */       i1 = (int)(j * f2);
/*    */     }
/*    */     Object localObject;
/* 53 */     if (this.canvas.getScalingPolicy() == 0)
/* 54 */       localObject = new AreaAveragingScaleFilter(n, i1);
/*    */     else {
/* 56 */       localObject = new ReplicatingScaleFilter(n, i1);
/*    */     }
/* 58 */     FilteredImageSource localFilteredImageSource = new FilteredImageSource(localJimiRasterImage.getImageProducer(), (ImageFilter)localObject);
/* 59 */     this.image = Toolkit.getDefaultToolkit().createImage(localFilteredImageSource);
/* 60 */     GraphicsUtils.waitForImage(this.image);
/*    */ 
/* 62 */     super.render();
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.component.BestFitRenderer
 * JD-Core Version:    0.6.2
 */