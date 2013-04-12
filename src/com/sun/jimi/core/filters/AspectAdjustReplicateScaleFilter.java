/*    */ package com.sun.jimi.core.filters;
/*    */ 
/*    */ import java.awt.image.ReplicateScaleFilter;
/*    */ 
/*    */ public class AspectAdjustReplicateScaleFilter extends ReplicateScaleFilter
/*    */ {
/*    */   protected double xResolution;
/*    */   protected double yResolution;
/*    */   protected boolean noScaling;
/*    */ 
/*    */   public AspectAdjustReplicateScaleFilter(double paramDouble1, double paramDouble2)
/*    */   {
/* 32 */     super(100, 100);
/* 33 */     this.xResolution = paramDouble1;
/* 34 */     this.yResolution = paramDouble2;
/*    */   }
/*    */ 
/*    */   public void setDimensions(int paramInt1, int paramInt2)
/*    */   {
/*    */     double d;
/* 39 */     if (this.xResolution < this.yResolution) {
/* 40 */       this.destWidth = paramInt1;
/* 41 */       d = this.xResolution / this.yResolution;
/* 42 */       this.destHeight = ((int)(paramInt2 * d));
/*    */     }
/*    */     else {
/* 45 */       this.destHeight = paramInt2;
/* 46 */       d = this.yResolution / this.xResolution;
/* 47 */       this.destWidth = ((int)(paramInt1 * d));
/*    */     }
/* 49 */     this.srcWidth = paramInt1;
/* 50 */     this.srcHeight = paramInt2;
/*    */ 
/* 52 */     super.setDimensions(paramInt1, paramInt2);
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.filters.AspectAdjustReplicateScaleFilter
 * JD-Core Version:    0.6.2
 */