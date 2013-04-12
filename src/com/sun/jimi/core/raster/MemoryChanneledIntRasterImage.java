/*    */ package com.sun.jimi.core.raster;
/*    */ 
/*    */ import java.awt.image.ColorModel;
/*    */ import java.awt.image.ImageConsumer;
/*    */ 
/*    */ public class MemoryChanneledIntRasterImage extends MemoryIntRasterImage
/*    */ {
/*    */   public MemoryChanneledIntRasterImage(int paramInt1, int paramInt2, ColorModel paramColorModel)
/*    */   {
/* 27 */     super(paramInt1, paramInt2, paramColorModel);
/*    */   }
/*    */ 
/*    */   public void addDirectConsumer(ImageConsumer paramImageConsumer)
/*    */   {
/* 32 */     addWaitingConsumer(paramImageConsumer);
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.raster.MemoryChanneledIntRasterImage
 * JD-Core Version:    0.6.2
 */