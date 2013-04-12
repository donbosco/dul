/*    */ package com.sun.jimi.core.filters;
/*    */ 
/*    */ import java.awt.image.ImageProducer;
/*    */ import java.io.PrintStream;
/*    */ 
/*    */ public class Invert extends RGBBlockFilter
/*    */ {
/*    */   public Invert(ImageProducer paramImageProducer)
/*    */   {
/* 47 */     super(paramImageProducer);
/*    */   }
/*    */ 
/*    */   public int[][] filterRGBBlock(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[][] paramArrayOfInt)
/*    */   {
/* 53 */     for (int i = 0; i < paramInt4; i++)
/* 54 */       for (int j = 0; j < paramInt3; j++)
/*    */       {
/* 56 */         int k = paramArrayOfInt[i][j];
/* 57 */         int m = k & 0xFF000000;
/* 58 */         int n = (k ^ 0xFFFFFFFF) & 0xFFFFFF;
/* 59 */         paramArrayOfInt[i][j] = (m | n);
/*    */       }
/* 61 */     return paramArrayOfInt;
/*    */   }
/*    */ 
/*    */   public static void main(String[] paramArrayOfString)
/*    */   {
/* 68 */     Invert localInvert = null;
/* 69 */     if (paramArrayOfString.length == 0)
/* 70 */       localInvert = new Invert(null);
/*    */     else
/* 72 */       usage();
/* 73 */     System.exit(
/* 74 */       ImageFilterPlus.filterStream(System.in, System.out, localInvert));
/*    */   }
/*    */ 
/*    */   private static void usage()
/*    */   {
/* 79 */     System.err.println("usage: Invert");
/* 80 */     System.exit(1);
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.filters.Invert
 * JD-Core Version:    0.6.2
 */