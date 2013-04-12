/*    */ package com.sun.jimi.core;
/*    */ 
/*    */ import com.sun.jimi.core.raster.JimiRasterImage;
/*    */ import com.sun.jimi.core.util.JimiUtil;
/*    */ 
/*    */ public class JimiRasterImageEnumeration
/*    */ {
/*    */   protected JimiImageEnumeration jdField_enum;
/*    */   protected boolean synchronous;
/*    */ 
/*    */   public JimiRasterImageEnumeration(JimiImageEnumeration paramJimiImageEnumeration)
/*    */   {
/* 33 */     this.jdField_enum = paramJimiImageEnumeration;
/*    */   }
/*    */ 
/*    */   public JimiRasterImageEnumeration(JimiImageEnumeration paramJimiImageEnumeration, boolean paramBoolean)
/*    */   {
/* 38 */     this(paramJimiImageEnumeration);
/* 39 */     this.synchronous = paramBoolean;
/*    */   }
/*    */ 
/*    */   public int countImages()
/*    */   {
/* 72 */     return this.jdField_enum.countImages();
/*    */   }
/*    */ 
/*    */   public JimiRasterImage getNextImage()
/*    */     throws JimiException
/*    */   {
/* 49 */     JimiRasterImage localJimiRasterImage = JimiUtil.asJimiRasterImage(this.jdField_enum.getNextImage());
/* 50 */     if (this.synchronous) {
/* 51 */       localJimiRasterImage.waitFinished();
/*    */     }
/* 53 */     return localJimiRasterImage;
/*    */   }
/*    */ 
/*    */   public boolean hasMoreImages()
/*    */   {
/* 62 */     return this.jdField_enum.hasMoreImages();
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.JimiRasterImageEnumeration
 * JD-Core Version:    0.6.2
 */