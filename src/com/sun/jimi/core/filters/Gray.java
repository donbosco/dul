/*    */ package com.sun.jimi.core.filters;
/*    */ 
/*    */ import java.awt.image.RGBImageFilter;
/*    */ 
/*    */ public class Gray extends RGBImageFilter
/*    */ {
/*    */   public int filterRGB(int paramInt1, int paramInt2, int paramInt3)
/*    */   {
/* 15 */     int i = 
/* 16 */       ((paramInt3 >> 16 & 0xFF) + (
/* 16 */       paramInt3 >> 8 & 0xFF) + (
/* 17 */       paramInt3 & 0xFF)) / 3;
/*    */ 
/* 19 */     return 0xFF000000 | i << 16 | i << 8 | i;
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.filters.Gray
 * JD-Core Version:    0.6.2
 */