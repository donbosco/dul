/*    */ package com.sun.jimi.core.component;
/*    */ 
/*    */ import com.sun.jimi.core.filters.ReplicatingScaleFilter;
/*    */ import java.awt.image.ColorModel;
/*    */ import java.awt.image.ImageConsumer;
/*    */ import java.awt.image.ImageFilter;
/*    */ 
/*    */ public class AspectScaler extends ReplicatingScaleFilter
/*    */ {
/*    */   protected int fixedDimension;
/*    */   protected int maxWidth;
/*    */   protected int maxHeight;
/*    */   protected boolean noScaling;
/*    */ 
/*    */   public AspectScaler(int paramInt1, int paramInt2)
/*    */   {
/* 33 */     super(paramInt1, paramInt2);
/* 34 */     this.maxWidth = paramInt1;
/* 35 */     this.maxHeight = paramInt2;
/*    */   }
/*    */ 
/*    */   public void setDimensions(int paramInt1, int paramInt2)
/*    */   {
/* 40 */     this.srcWidth = paramInt1;
/* 41 */     this.srcHeight = paramInt2;
/* 42 */     double d1 = paramInt1 / this.maxWidth;
/* 43 */     double d2 = paramInt2 / this.maxHeight;
/*    */     int j;
/*    */     int i;
/* 46 */     if (d1 < d2) {
/* 47 */       j = this.maxHeight;
/* 48 */       i = (int)(paramInt1 / d2);
/*    */     }
/*    */     else {
/* 51 */       i = this.maxWidth;
/* 52 */       j = (int)(paramInt2 / d1);
/*    */     }
/* 54 */     this.destWidth = i;
/* 55 */     this.destHeight = j;
/* 56 */     this.consumer.setDimensions(this.destWidth, this.destHeight);
/*    */   }
/*    */ 
/*    */   public void setPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, ColorModel paramColorModel, byte[] paramArrayOfByte, int paramInt5, int paramInt6)
/*    */   {
/* 62 */     if (this.noScaling) {
/* 63 */       this.consumer.setPixels(paramInt1, paramInt2, paramInt3, paramInt4, paramColorModel, paramArrayOfByte, paramInt5, paramInt6);
/*    */     }
/*    */     else
/* 66 */       super.setPixels(paramInt1, paramInt2, paramInt3, paramInt4, paramColorModel, paramArrayOfByte, paramInt5, paramInt6);
/*    */   }
/*    */ 
/*    */   public void setPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, ColorModel paramColorModel, int[] paramArrayOfInt, int paramInt5, int paramInt6)
/*    */   {
/* 74 */     if (this.noScaling) {
/* 75 */       this.consumer.setPixels(paramInt1, paramInt2, paramInt3, paramInt4, paramColorModel, paramArrayOfInt, paramInt5, paramInt6);
/*    */     }
/*    */     else
/* 78 */       super.setPixels(paramInt1, paramInt2, paramInt3, paramInt4, paramColorModel, paramArrayOfInt, paramInt5, paramInt6);
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.component.AspectScaler
 * JD-Core Version:    0.6.2
 */