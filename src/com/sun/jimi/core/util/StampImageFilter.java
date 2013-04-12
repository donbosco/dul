/*    */ package com.sun.jimi.core.util;
/*    */ 
/*    */ import java.awt.image.ColorModel;
/*    */ import java.awt.image.ImageFilter;
/*    */ 
/*    */ public class StampImageFilter extends ImageFilter
/*    */ {
/*    */   protected int stampx;
/*    */   protected int stampy;
/*    */   protected int stampwidth;
/*    */   protected int stampheight;
/*    */ 
/*    */   public void setDimensions(int paramInt1, int paramInt2)
/*    */   {
/* 53 */     this.stampx = Math.max(0, (paramInt1 - 46) / 2);
/* 54 */     this.stampy = Math.max(0, (paramInt2 - 30) / 2);
/* 55 */     this.stampwidth = Math.min(46, paramInt1);
/* 56 */     this.stampheight = Math.min(30, paramInt2);
/*    */ 
/* 58 */     super.setDimensions(paramInt1, paramInt2);
/*    */   }
/*    */ 
/*    */   public void setPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, ColorModel paramColorModel, int[] paramArrayOfInt, int paramInt5, int paramInt6)
/*    */   {
/* 34 */     if ((paramInt1 + paramInt3 >= this.stampx) && (paramInt1 < this.stampx + this.stampwidth) && 
/* 35 */       (paramInt2 + paramInt4 >= this.stampy) && (paramInt2 < this.stampy + this.stampheight))
/*    */     {
/* 37 */       int i = Math.max(paramInt1, this.stampx);
/* 38 */       int j = Math.min(paramInt1 + paramInt3, this.stampx + this.stampwidth);
/* 39 */       int k = Math.max(paramInt2, this.stampy);
/* 40 */       int m = Math.min(paramInt2 + paramInt4, this.stampy + this.stampheight);
/* 41 */       for (int n = k; n < m; n++) {
/* 42 */         for (int i1 = i; i1 < j; i1++) {
/* 43 */           paramArrayOfInt[(paramInt5 + (n - paramInt2) * paramInt6 + (i1 - paramInt1))] = 
/* 44 */             StampImage.pixels[((n - this.stampy) * 46 + (i1 - this.stampx))];
/*    */         }
/*    */       }
/*    */     }
/* 48 */     super.setPixels(paramInt1, paramInt2, paramInt3, paramInt4, paramColorModel, paramArrayOfInt, paramInt5, paramInt6);
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.util.StampImageFilter
 * JD-Core Version:    0.6.2
 */