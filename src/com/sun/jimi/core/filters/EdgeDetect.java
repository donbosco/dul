/*     */ package com.sun.jimi.core.filters;
/*     */ 
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.ImageProducer;
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ public class EdgeDetect extends RGBAllFilter
/*     */ {
/*     */   private static final double SCALE = 1.8D;
/*     */ 
/*     */   public EdgeDetect(ImageProducer paramImageProducer)
/*     */   {
/*  56 */     super(paramImageProducer);
/*     */   }
/*     */ 
/*     */   public synchronized void filterRGBAll(int paramInt1, int paramInt2, int[][] paramArrayOfInt)
/*     */   {
/*  64 */     int[][] arrayOfInt = new int[paramInt2][paramInt1];
/*     */     try
/*     */     {
/*  71 */       for (int m = 0; m < paramInt1; m++)
/*     */       {
/*  73 */         arrayOfInt[0][m] = -16777216;
/*  74 */         arrayOfInt[(paramInt2 - 1)][m] = -16777216;
/*     */       }
/*     */     } catch (RuntimeException localRuntimeException1) { throw localRuntimeException1; }
/*     */     try {
/*  78 */       for (int n = 1; n < paramInt2 - 1; n++)
/*     */       {
/*  81 */         arrayOfInt[n][0] = -16777216;
/*  82 */         arrayOfInt[n][(paramInt1 - 1)] = -16777216;
/*     */ 
/*  84 */         for (int i1 = 1; i1 < paramInt1 - 1; i1++)
/*     */         {
/*  86 */           long l1 = 
/*  87 */             ImageFilterPlus.rgbModel.getRed(paramArrayOfInt[(n - 1)][(i1 + 1)]) - 
/*  88 */             ImageFilterPlus.rgbModel.getRed(paramArrayOfInt[(n - 1)][(i1 - 1)]) + 
/*  89 */             2 * (
/*  90 */             ImageFilterPlus.rgbModel.getRed(paramArrayOfInt[n][(i1 + 1)]) - 
/*  91 */             ImageFilterPlus.rgbModel.getRed(paramArrayOfInt[n][(i1 - 1)])) + 
/*  92 */             ImageFilterPlus.rgbModel.getRed(paramArrayOfInt[(n + 1)][(i1 + 1)]) - 
/*  93 */             ImageFilterPlus.rgbModel.getRed(paramArrayOfInt[(n + 1)][(i1 - 1)]);
/*  94 */           long l2 = 
/*  95 */             ImageFilterPlus.rgbModel.getRed(paramArrayOfInt[(n + 1)][(i1 - 1)]) + 
/*  96 */             2 * ImageFilterPlus.rgbModel.getRed(paramArrayOfInt[(n + 1)][i1]) + 
/*  97 */             ImageFilterPlus.rgbModel.getRed(paramArrayOfInt[(n + 1)][(i1 + 1)]) - (
/*  99 */             ImageFilterPlus.rgbModel.getRed(paramArrayOfInt[(n - 1)][(i1 - 1)]) + 
/* 100 */             2 * ImageFilterPlus.rgbModel.getRed(paramArrayOfInt[(n - 1)][i1]) + 
/* 101 */             ImageFilterPlus.rgbModel.getRed(paramArrayOfInt[(n - 1)][(i1 + 1)]));
/*     */ 
/* 103 */           double d = Math.sqrt(l1 * l1 + l2 * l2) / 1.8D;
/* 104 */           int i = Math.min((int)d, 255);
/*     */ 
/* 106 */           l1 = 
/* 107 */             ImageFilterPlus.rgbModel.getGreen(paramArrayOfInt[(n - 1)][(i1 + 1)]) - 
/* 108 */             ImageFilterPlus.rgbModel.getGreen(paramArrayOfInt[(n - 1)][(i1 - 1)]) + 
/* 109 */             2 * (
/* 110 */             ImageFilterPlus.rgbModel.getGreen(paramArrayOfInt[n][(i1 + 1)]) - 
/* 111 */             ImageFilterPlus.rgbModel.getGreen(paramArrayOfInt[n][(i1 - 1)])) + 
/* 112 */             ImageFilterPlus.rgbModel.getGreen(paramArrayOfInt[(n + 1)][(i1 + 1)]) - 
/* 113 */             ImageFilterPlus.rgbModel.getGreen(paramArrayOfInt[(n + 1)][(i1 - 1)]);
/* 114 */           l2 = 
/* 115 */             ImageFilterPlus.rgbModel.getGreen(paramArrayOfInt[(n + 1)][(i1 - 1)]) + 
/* 116 */             2 * ImageFilterPlus.rgbModel.getGreen(paramArrayOfInt[(n + 1)][i1]) + 
/* 117 */             ImageFilterPlus.rgbModel.getGreen(paramArrayOfInt[(n + 1)][(i1 + 1)]) - (
/* 119 */             ImageFilterPlus.rgbModel.getGreen(paramArrayOfInt[(n - 1)][(i1 - 1)]) + 
/* 120 */             2 * ImageFilterPlus.rgbModel.getGreen(paramArrayOfInt[(n - 1)][i1]) + 
/* 121 */             ImageFilterPlus.rgbModel.getGreen(paramArrayOfInt[(n - 1)][(i1 + 1)]));
/*     */ 
/* 123 */           d = Math.sqrt(l1 * l1 + l2 * l2) / 1.8D;
/* 124 */           int j = Math.min((int)d, 255);
/*     */ 
/* 126 */           l1 = 
/* 127 */             ImageFilterPlus.rgbModel.getBlue(paramArrayOfInt[(n - 1)][(i1 + 1)]) - 
/* 128 */             ImageFilterPlus.rgbModel.getBlue(paramArrayOfInt[(n - 1)][(i1 - 1)]) + 
/* 129 */             2 * (
/* 130 */             ImageFilterPlus.rgbModel.getBlue(paramArrayOfInt[n][(i1 + 1)]) - 
/* 131 */             ImageFilterPlus.rgbModel.getBlue(paramArrayOfInt[n][(i1 - 1)])) + 
/* 132 */             ImageFilterPlus.rgbModel.getBlue(paramArrayOfInt[(n + 1)][(i1 + 1)]) - 
/* 133 */             ImageFilterPlus.rgbModel.getBlue(paramArrayOfInt[(n + 1)][(i1 - 1)]);
/* 134 */           l2 = 
/* 135 */             ImageFilterPlus.rgbModel.getBlue(paramArrayOfInt[(n + 1)][(i1 - 1)]) + 
/* 136 */             2 * ImageFilterPlus.rgbModel.getBlue(paramArrayOfInt[(n + 1)][i1]) + 
/* 137 */             ImageFilterPlus.rgbModel.getBlue(paramArrayOfInt[(n + 1)][(i1 + 1)]) - (
/* 139 */             ImageFilterPlus.rgbModel.getBlue(paramArrayOfInt[(n - 1)][(i1 - 1)]) + 
/* 140 */             2 * ImageFilterPlus.rgbModel.getBlue(paramArrayOfInt[(n - 1)][i1]) + 
/* 141 */             ImageFilterPlus.rgbModel.getBlue(paramArrayOfInt[(n - 1)][(i1 + 1)]));
/*     */ 
/* 143 */           d = Math.sqrt(l1 * l1 + l2 * l2) / 1.8D;
/* 144 */           int k = Math.min((int)d, 255);
/*     */ 
/* 146 */           arrayOfInt[n][i1] = 
/* 147 */             (0xFF000000 | i << 16 | j << 8 | k);
/*     */         }
/*     */       }
/*     */     } catch (RuntimeException localRuntimeException2) { throw localRuntimeException2; }
/* 151 */     setPixels(paramInt1, paramInt2, arrayOfInt);
/*     */   }
/*     */ 
/*     */   public static void main(String[] paramArrayOfString)
/*     */   {
/* 158 */     if (paramArrayOfString.length != 0)
/* 159 */       usage();
/* 160 */     EdgeDetect localEdgeDetect = new EdgeDetect(null);
/* 161 */     System.exit(
/* 162 */       ImageFilterPlus.filterStream(System.in, System.out, localEdgeDetect));
/*     */   }
/*     */ 
/*     */   private static void usage()
/*     */   {
/* 167 */     System.err.println("usage: EdgeDetect");
/* 168 */     System.exit(1);
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.filters.EdgeDetect
 * JD-Core Version:    0.6.2
 */