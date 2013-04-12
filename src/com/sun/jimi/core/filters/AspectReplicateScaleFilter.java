/*    */ package com.sun.jimi.core.filters;
/*    */ 
/*    */ import java.awt.image.ColorModel;
/*    */ import java.awt.image.ImageConsumer;
/*    */ import java.awt.image.ImageFilter;
/*    */ 
/*    */ public class AspectReplicateScaleFilter extends ReplicatingScaleFilter
/*    */ {
/*    */   protected int fixedDimension;
/*    */   protected int maxWidth;
/*    */   protected int maxHeight;
/*    */   protected boolean noScaling;
/*    */ 
/*    */   public AspectReplicateScaleFilter(int paramInt1, int paramInt2)
/*    */   {
/* 32 */     super(paramInt1, paramInt2);
/* 33 */     this.maxWidth = paramInt1;
/* 34 */     this.maxHeight = paramInt2;
/*    */   }
/*    */ 
/*    */   public void setDimensions(int paramInt1, int paramInt2)
/*    */   {
/* 39 */     this.srcWidth = paramInt1;
/* 40 */     this.srcHeight = paramInt2;
/* 41 */     if ((paramInt1 <= this.maxWidth) && (paramInt2 <= this.maxHeight)) {
/* 42 */       this.destWidth = paramInt1;
/* 43 */       this.destHeight = paramInt2;
/* 44 */       this.noScaling = true;
/* 45 */       this.consumer.setDimensions(paramInt1, paramInt2);
/*    */     }
/*    */     else {
/* 48 */       double d1 = paramInt1 / this.maxWidth;
/* 49 */       double d2 = paramInt2 / this.maxHeight;
/*    */       int j;
/*    */       int i;
/* 52 */       if (d1 < d2) {
/* 53 */         j = this.maxHeight;
/* 54 */         i = (int)(paramInt1 / d2);
/*    */       }
/*    */       else {
/* 57 */         i = this.maxWidth;
/* 58 */         j = (int)(paramInt2 / d1);
/*    */       }
/* 60 */       this.destWidth = i;
/* 61 */       this.destHeight = j;
/* 62 */       this.consumer.setDimensions(this.destWidth, this.destHeight);
/*    */     }
/*    */   }
/*    */ 
/*    */   public void setPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, ColorModel paramColorModel, byte[] paramArrayOfByte, int paramInt5, int paramInt6)
/*    */   {
/* 69 */     if (this.noScaling) {
/* 70 */       this.consumer.setPixels(paramInt1, paramInt2, paramInt3, paramInt4, paramColorModel, paramArrayOfByte, paramInt5, paramInt6);
/*    */     }
/*    */     else
/* 73 */       super.setPixels(paramInt1, paramInt2, paramInt3, paramInt4, paramColorModel, paramArrayOfByte, paramInt5, paramInt6);
/*    */   }
/*    */ 
/*    */   public void setPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, ColorModel paramColorModel, int[] paramArrayOfInt, int paramInt5, int paramInt6)
/*    */   {
/* 81 */     if (this.noScaling) {
/* 82 */       this.consumer.setPixels(paramInt1, paramInt2, paramInt3, paramInt4, paramColorModel, paramArrayOfInt, paramInt5, paramInt6);
/*    */     }
/*    */     else
/* 85 */       super.setPixels(paramInt1, paramInt2, paramInt3, paramInt4, paramColorModel, paramArrayOfInt, paramInt5, paramInt6);
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.filters.AspectReplicateScaleFilter
 * JD-Core Version:    0.6.2
 */