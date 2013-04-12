/*    */ package com.sun.jimi.core;
/*    */ 
/*    */ import com.sun.jimi.core.raster.BitRasterImage;
/*    */ import com.sun.jimi.core.raster.ByteRasterImage;
/*    */ import com.sun.jimi.core.raster.ChanneledIntRasterImage;
/*    */ import com.sun.jimi.core.raster.IntRasterImage;
/*    */ import com.sun.jimi.core.raster.MemoryChanneledIntRasterImage;
/*    */ import com.sun.jimi.core.raster.OneshotByteRasterImage;
/*    */ import com.sun.jimi.core.raster.OneshotIntRasterImage;
/*    */ import java.awt.image.ColorModel;
/*    */ 
/*    */ public class OneshotJimiImageFactory
/*    */   implements JimiImageFactory
/*    */ {
/*    */   public BitRasterImage createBitRasterImage(int paramInt1, int paramInt2, ColorModel paramColorModel)
/*    */   {
/* 37 */     return new OneshotByteRasterImage(paramInt1, paramInt2, paramColorModel, true);
/*    */   }
/*    */ 
/*    */   public ByteRasterImage createByteRasterImage(int paramInt1, int paramInt2, ColorModel paramColorModel)
/*    */   {
/* 33 */     return new OneshotByteRasterImage(paramInt1, paramInt2, paramColorModel, false);
/*    */   }
/*    */ 
/*    */   public ChanneledIntRasterImage createChanneledIntRasterImage(int paramInt1, int paramInt2, ColorModel paramColorModel)
/*    */   {
/* 42 */     return new MemoryChanneledIntRasterImage(paramInt1, paramInt2, paramColorModel);
/*    */   }
/*    */ 
/*    */   public IntRasterImage createIntRasterImage(int paramInt1, int paramInt2, ColorModel paramColorModel)
/*    */   {
/* 28 */     return new OneshotIntRasterImage(paramInt1, paramInt2, paramColorModel);
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.OneshotJimiImageFactory
 * JD-Core Version:    0.6.2
 */