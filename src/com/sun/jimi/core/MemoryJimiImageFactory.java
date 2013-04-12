/*    */ package com.sun.jimi.core;
/*    */ 
/*    */ import com.sun.jimi.core.raster.BitRasterImage;
/*    */ import com.sun.jimi.core.raster.ByteRasterImage;
/*    */ import com.sun.jimi.core.raster.ChanneledIntRasterImage;
/*    */ import com.sun.jimi.core.raster.IntRasterImage;
/*    */ import com.sun.jimi.core.raster.LongRasterImage;
/*    */ import com.sun.jimi.core.raster.MemoryBitRasterImage;
/*    */ import com.sun.jimi.core.raster.MemoryByteRasterImage;
/*    */ import com.sun.jimi.core.raster.MemoryChanneledIntRasterImage;
/*    */ import com.sun.jimi.core.raster.MemoryIntRasterImage;
/*    */ import com.sun.jimi.core.raster.MemoryLongRasterImage;
/*    */ import com.sun.jimi.core.util.LongColorModel;
/*    */ import java.awt.image.ColorModel;
/*    */ 
/*    */ public class MemoryJimiImageFactory
/*    */   implements JimiImageFactory
/*    */ {
/*    */   public BitRasterImage createBitRasterImage(int paramInt1, int paramInt2, ColorModel paramColorModel)
/*    */   {
/* 38 */     return new MemoryBitRasterImage(paramInt1, paramInt2, paramColorModel);
/*    */   }
/*    */ 
/*    */   public ByteRasterImage createByteRasterImage(int paramInt1, int paramInt2, ColorModel paramColorModel)
/*    */   {
/* 34 */     return new MemoryByteRasterImage(paramInt1, paramInt2, paramColorModel);
/*    */   }
/*    */ 
/*    */   public ChanneledIntRasterImage createChanneledIntRasterImage(int paramInt1, int paramInt2, ColorModel paramColorModel)
/*    */   {
/* 43 */     return new MemoryChanneledIntRasterImage(paramInt1, paramInt2, paramColorModel);
/*    */   }
/*    */ 
/*    */   public IntRasterImage createIntRasterImage(int paramInt1, int paramInt2, ColorModel paramColorModel)
/*    */   {
/* 29 */     return new MemoryIntRasterImage(paramInt1, paramInt2, paramColorModel);
/*    */   }
/*    */ 
/*    */   public LongRasterImage createLongRasterImage(int paramInt1, int paramInt2, LongColorModel paramLongColorModel)
/*    */   {
/* 48 */     return new MemoryLongRasterImage(paramInt1, paramInt2, paramLongColorModel);
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.MemoryJimiImageFactory
 * JD-Core Version:    0.6.2
 */