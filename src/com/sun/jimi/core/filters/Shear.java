/*     */ package com.sun.jimi.core.filters;
/*     */ 
/*     */ import java.awt.image.ImageProducer;
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ public class Shear extends RGBAllFilter
/*     */ {
/*     */   private double angle;
/*     */ 
/*     */   public Shear(ImageProducer paramImageProducer, double paramDouble)
/*     */   {
/*  73 */     super(paramImageProducer);
/*  74 */     this.angle = (paramDouble * 3.141592653589793D / 180.0D);
/*     */   }
/*     */ 
/*     */   public void filterRGBAll(int paramInt1, int paramInt2, int[][] paramArrayOfInt)
/*     */   {
/*  80 */     if (this.angle == 0.0D)
/*     */     {
/*  82 */       setPixels(paramInt1, paramInt2, paramArrayOfInt);
/*  83 */       return;
/*     */     }
/*     */ 
/*  86 */     double d1 = Math.tan(this.angle);
/*  87 */     if (d1 < 0.0D)
/*  88 */       d1 = -d1;
/*  89 */     int i = (int)(paramInt2 * d1 + paramInt1 + 0.999999D);
/*  90 */     int[][] arrayOfInt = new int[paramInt2][i];
/*  91 */     for (int j = 0; j < paramInt2; j++)
/*     */     {
/*     */       double d2;
/*  94 */       if (this.angle > 0.0D)
/*  95 */         d2 = j * d1;
/*     */       else
/*  97 */         d2 = (paramInt2 - j) * d1;
/*  98 */       int k = (int)d2;
/*  99 */       double d3 = d2 - k;
/* 100 */       double d4 = 1.0D - d3;
/*     */ 
/* 102 */       for (int m = 0; m < i; m++) {
/* 103 */         arrayOfInt[j][m] = 0;
/*     */       }
/* 105 */       int n = 0;
/* 106 */       int i1 = paramArrayOfInt[j][0] >> 16 & 0xFF;
/* 107 */       int i2 = paramArrayOfInt[j][0] >> 8 & 0xFF;
/* 108 */       int i3 = paramArrayOfInt[j][0] & 0xFF;
/* 109 */       for (int i4 = 0; i4 < paramInt1; i4++)
/*     */       {
/* 111 */         int i5 = paramArrayOfInt[j][i4];
/* 112 */         int i6 = i5 >> 24 & 0xFF;
/* 113 */         int i7 = i5 >> 16 & 0xFF;
/* 114 */         int i8 = i5 >> 8 & 0xFF;
/* 115 */         int i9 = i5 & 0xFF;
/* 116 */         arrayOfInt[j][(k + i4)] = 
/* 119 */           ((int)(d3 * n + d4 * i6) << 24 | 
/* 118 */           (int)(d3 * i1 + d4 * i7) << 16 | 
/* 119 */           (int)(d3 * i2 + d4 * i8) << 8 | 
/* 120 */           (int)(d3 * i3 + d4 * i9));
/* 121 */         n = i6;
/* 122 */         i1 = i7;
/* 123 */         i2 = i8;
/* 124 */         i3 = i9;
/*     */       }
/* 126 */       arrayOfInt[j][(k + paramInt1)] = 
/* 128 */         ((int)(d3 * n) << 24 | 
/* 128 */         i1 << 16 | i2 << 8 | i3);
/*     */     }
/* 130 */     setPixels(i, paramInt2, arrayOfInt);
/*     */   }
/*     */ 
/*     */   public static void main(String[] paramArrayOfString)
/*     */   {
/* 137 */     if (paramArrayOfString.length != 1)
/* 138 */       usage();
/* 139 */     Shear localShear = new Shear(null, Integer.parseInt(paramArrayOfString[0]));
/* 140 */     System.exit(
/* 141 */       ImageFilterPlus.filterStream(System.in, System.out, localShear));
/*     */   }
/*     */ 
/*     */   private static void usage()
/*     */   {
/* 146 */     System.err.println("usage: Shear <angle>");
/* 147 */     System.exit(1);
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.filters.Shear
 * JD-Core Version:    0.6.2
 */