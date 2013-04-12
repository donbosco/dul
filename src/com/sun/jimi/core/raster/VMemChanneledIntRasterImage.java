/*    */ package com.sun.jimi.core.raster;
/*    */ 
/*    */ import com.sun.jimi.core.JimiException;
/*    */ import com.sun.jimi.util.RandomAccessStorage;
/*    */ import java.awt.image.ColorModel;
/*    */ import java.awt.image.ImageConsumer;
/*    */ 
/*    */ public class VMemChanneledIntRasterImage extends VMemIntRasterImage
/*    */ {
/*    */   public VMemChanneledIntRasterImage(RandomAccessStorage paramRandomAccessStorage, int paramInt1, int paramInt2, ColorModel paramColorModel)
/*    */     throws JimiException
/*    */   {
/* 30 */     super(paramRandomAccessStorage, paramInt1, paramInt2, paramColorModel);
/*    */   }
/*    */ 
/*    */   public void addDirectConsumer(ImageConsumer paramImageConsumer)
/*    */   {
/* 35 */     addWaitingConsumer(paramImageConsumer);
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.raster.VMemChanneledIntRasterImage
 * JD-Core Version:    0.6.2
 */