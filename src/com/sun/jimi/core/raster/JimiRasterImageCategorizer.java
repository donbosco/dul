/*    */ package com.sun.jimi.core.raster;
/*    */ 
/*    */ import com.sun.jimi.core.JimiImage;
/*    */ import com.sun.jimi.core.util.JimiUtil;
/*    */ import java.awt.image.DirectColorModel;
/*    */ import java.awt.image.IndexColorModel;
/*    */ 
/*    */ public class JimiRasterImageCategorizer
/*    */ {
/*    */   public static boolean isBytePaletteImage(JimiImage paramJimiImage)
/*    */   {
/* 45 */     paramJimiImage = JimiUtil.asJimiRasterImage(paramJimiImage);
/* 46 */     return (isPaletteImage(paramJimiImage)) && ((paramJimiImage instanceof ByteRasterImage));
/*    */   }
/*    */ 
/*    */   public static boolean isGrayscaleImage(JimiImage paramJimiImage)
/*    */   {
/* 67 */     JimiRasterImage localJimiRasterImage = JimiUtil.asJimiRasterImage(paramJimiImage);
/* 68 */     if ((localJimiRasterImage.getColorModel() instanceof DirectColorModel)) {
/* 69 */       DirectColorModel localDirectColorModel = (DirectColorModel)localJimiRasterImage.getColorModel();
/* 70 */       return (localDirectColorModel.getRedMask() == localDirectColorModel.getGreenMask()) && 
/* 71 */         (localDirectColorModel.getRedMask() == localDirectColorModel.getBlueMask());
/*    */     }
/*    */ 
/* 74 */     return false;
/*    */   }
/*    */ 
/*    */   public static boolean isLongRasterImage(JimiImage paramJimiImage)
/*    */   {
/* 86 */     paramJimiImage = JimiUtil.asJimiRasterImage(paramJimiImage);
/* 87 */     return paramJimiImage instanceof LongRasterImage;
/*    */   }
/*    */ 
/*    */   public static boolean isMonoColorImage(JimiImage paramJimiImage)
/*    */   {
/* 56 */     return (isBytePaletteImage(paramJimiImage)) && 
/* 57 */       (((IndexColorModel)((JimiRasterImage)paramJimiImage).getColorModel()).getMapSize() == 2);
/*    */   }
/*    */ 
/*    */   public static boolean isPaletteImage(JimiImage paramJimiImage)
/*    */   {
/* 34 */     paramJimiImage = JimiUtil.asJimiRasterImage(paramJimiImage);
/* 35 */     return ((JimiRasterImage)paramJimiImage).getColorModel() instanceof IndexColorModel;
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.raster.JimiRasterImageCategorizer
 * JD-Core Version:    0.6.2
 */