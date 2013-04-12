/*    */ package com.sun.jimi.core.filters;
/*    */ 
/*    */ import com.sun.jimi.core.util.JimiUtil;
/*    */ import java.awt.image.ColorModel;
/*    */ import java.awt.image.CropImageFilter;
/*    */ import java.awt.image.ImageConsumer;
/*    */ import java.awt.image.ImageFilter;
/*    */ 
/*    */ public class EnhancedCropImageFilter extends CropImageFilter
/*    */ {
/*    */   protected boolean finished;
/*    */   protected boolean tdlr;
/*    */   protected boolean singleFrame;
/*    */   protected int cX;
/*    */   protected int cY;
/*    */   protected int cW;
/*    */   protected int cH;
/*    */ 
/*    */   public EnhancedCropImageFilter(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*    */   {
/* 35 */     super(paramInt1, paramInt2, paramInt3, paramInt4);
/* 36 */     this.cX = paramInt1;
/* 37 */     this.cY = paramInt2;
/* 38 */     this.cW = paramInt3;
/* 39 */     this.cH = paramInt4;
/*    */   }
/*    */ 
/*    */   public void imageComplete(int paramInt)
/*    */   {
/* 69 */     if (!this.finished)
/* 70 */       super.imageComplete(paramInt);
/*    */   }
/*    */ 
/*    */   public void setHints(int paramInt)
/*    */   {
/* 44 */     super.setHints(paramInt);
/*    */ 
/* 46 */     if ((JimiUtil.flagSet(paramInt, 2)) && 
/* 47 */       (JimiUtil.flagSet(paramInt, 8)) && 
/* 48 */       (JimiUtil.flagSet(paramInt, 4))) {
/* 49 */       this.tdlr = true;
/* 50 */       this.singleFrame = JimiUtil.flagSet(paramInt, 16);
/*    */     }
/*    */   }
/*    */ 
/*    */   public void setPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, ColorModel paramColorModel, byte[] paramArrayOfByte, int paramInt5, int paramInt6)
/*    */   {
/* 57 */     if (!this.finished) {
/* 58 */       super.setPixels(paramInt1, paramInt2, paramInt3, paramInt4, paramColorModel, paramArrayOfByte, paramInt5, paramInt6);
/* 59 */       if (paramInt2 >= this.cY + this.cH) {
/* 60 */         this.finished = true;
/* 61 */         this.consumer.imageComplete(this.singleFrame ? 3 : 
/* 62 */           2);
/*    */       }
/*    */     }
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.filters.EnhancedCropImageFilter
 * JD-Core Version:    0.6.2
 */