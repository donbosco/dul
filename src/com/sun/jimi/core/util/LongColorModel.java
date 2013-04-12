/*    */ package com.sun.jimi.core.util;
/*    */ 
/*    */ import java.awt.image.ColorModel;
/*    */ 
/*    */ public abstract class LongColorModel extends ColorModel
/*    */ {
/*    */   public LongColorModel()
/*    */   {
/* 26 */     super(32);
/*    */   }
/*    */ 
/*    */   public abstract int getAlpha(long paramLong);
/*    */ 
/*    */   public abstract int getBlue(long paramLong);
/*    */ 
/*    */   public abstract int getGreen(long paramLong);
/*    */ 
/*    */   public abstract int getRGB(long paramLong);
/*    */ 
/*    */   public abstract int getRed(long paramLong);
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.util.LongColorModel
 * JD-Core Version:    0.6.2
 */