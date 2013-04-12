/*     */ package com.sun.jimi.core.filters;
/*     */ 
/*     */ import java.awt.image.ImageProducer;
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ public class Shrink extends RGBAllFilter
/*     */ {
/*     */   private int divisor;
/*     */ 
/*     */   public Shrink(ImageProducer paramImageProducer, int paramInt)
/*     */   {
/*  54 */     super(paramImageProducer);
/*  55 */     this.divisor = paramInt;
/*     */   }
/*     */ 
/*     */   public void filterRGBAll(int paramInt1, int paramInt2, int[][] paramArrayOfInt)
/*     */   {
/*  61 */     int i = this.divisor * this.divisor;
/*  62 */     int j = Math.max(paramInt1 / this.divisor, 1);
/*  63 */     int k = Math.max(paramInt2 / this.divisor, 1);
/*  64 */     int[][] arrayOfInt = new int[k][j];
/*  65 */     for (int m = 0; m < k; m++)
/*     */     {
/*  67 */       for (int n = 0; n < j; n++)
/*     */       {
/*  69 */         int i1 = 0; int i2 = 0; int i3 = 0; int i4 = 0;
/*  70 */         for (int i5 = 0; i5 < this.divisor; i5++)
/*     */         {
/*  72 */           int i6 = m * this.divisor + i5;
/*  73 */           if (i6 < paramInt2)
/*     */           {
/*  75 */             for (int i7 = 0; i7 < this.divisor; i7++)
/*     */             {
/*  77 */               int i8 = n * this.divisor + i7;
/*  78 */               if (i8 < paramInt1)
/*     */               {
/*  80 */                 int i9 = paramArrayOfInt[i6][i8];
/*  81 */                 i1 += (i9 >> 24 & 0xFF);
/*  82 */                 i2 += (i9 >> 16 & 0xFF);
/*  83 */                 i3 += (i9 >> 8 & 0xFF);
/*  84 */                 i4 += (i9 & 0xFF);
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*  87 */         i1 /= i;
/*  88 */         i2 /= i;
/*  89 */         i3 /= i;
/*  90 */         i4 /= i;
/*  91 */         arrayOfInt[m][n] = 
/*  92 */           (i1 << 24 | i2 << 16 | i3 << 8 | i4);
/*     */       }
/*     */     }
/*  95 */     setPixels(j, k, arrayOfInt);
/*     */   }
/*     */ 
/*     */   public static void main(String[] paramArrayOfString)
/*     */   {
/* 102 */     if (paramArrayOfString.length != 1)
/* 103 */       usage();
/* 104 */     Enlarge localEnlarge = new Enlarge(
/* 105 */       null, Integer.parseInt(paramArrayOfString[0]));
/* 106 */     System.exit(
/* 107 */       ImageFilterPlus.filterStream(System.in, System.out, localEnlarge));
/*     */   }
/*     */ 
/*     */   private static void usage()
/*     */   {
/* 112 */     System.err.println("usage: Shrink <divisor>");
/* 113 */     System.exit(1);
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.filters.Shrink
 * JD-Core Version:    0.6.2
 */