/*     */ package com.sun.jimi.core.filters;
/*     */ 
/*     */ import java.awt.image.ImageProducer;
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ public class Smooth extends RGBAllFilter
/*     */ {
/*     */   private int n;
/*     */ 
/*     */   public Smooth(ImageProducer paramImageProducer)
/*     */   {
/*  55 */     this(paramImageProducer, 1);
/*     */   }
/*     */ 
/*     */   public Smooth(ImageProducer paramImageProducer, int paramInt)
/*     */   {
/*  48 */     super(paramImageProducer);
/*  49 */     this.n = paramInt;
/*     */   }
/*     */ 
/*     */   public void filterRGBAll(int paramInt1, int paramInt2, int[][] paramArrayOfInt)
/*     */   {
/*  61 */     int[][] arrayOfInt = new int[paramInt2][paramInt1];
/*  62 */     for (int i = 0; i < paramInt2; i++)
/*  63 */       for (int j = 0; j < paramInt1; j++)
/*     */       {
/*  65 */         int k = 0; int m = 0; int i1 = 0; int i2 = 0; int i3 = 0;
/*  66 */         for (int i4 = i - this.n; i4 <= i + this.n; i4++)
/*  67 */           if ((i4 >= 0) && (i4 < paramInt2))
/*  68 */             for (int i5 = j - this.n; i5 <= j + this.n; i5++)
/*  69 */               if ((i5 >= 0) && (i5 < paramInt1))
/*     */               {
/*  71 */                 int i6 = paramArrayOfInt[i4][i5];
/*  72 */                 k += (i6 >> 24 & 0xFF);
/*  73 */                 m += (i6 >> 16 & 0xFF);
/*  74 */                 i1 += (i6 >> 8 & 0xFF);
/*  75 */                 i2 += (i6 & 0xFF);
/*  76 */                 i3++;
/*     */               }
/*  78 */         k /= i3;
/*  79 */         m /= i3;
/*  80 */         i1 /= i3;
/*  81 */         i2 /= i3;
/*  82 */         arrayOfInt[i][j] = 
/*  83 */           (k << 24 | m << 16 | i1 << 8 | i2);
/*     */       }
/*  85 */     setPixels(paramInt1, paramInt2, arrayOfInt);
/*     */   }
/*     */ 
/*     */   public static void main(String[] paramArrayOfString)
/*     */   {
/*  92 */     int i = -1;
/*  93 */     int j = paramArrayOfString.length;
/*     */ int k = 0;
/*  95 */     for ( k = 0; (k < j) && (paramArrayOfString[k].charAt(0) == '-'); k++)
/*     */     {
/*  97 */       if ((paramArrayOfString[k].equals("-n")) && (k + 1 < j))
/*     */       {
/*  99 */         k++;
/* 100 */         i = Integer.parseInt(paramArrayOfString[k]);
/*     */       }
/*     */       else {
/* 103 */         usage();
/*     */       }
/*     */     }
/* 105 */     if (k != j)
/* 106 */       usage();
/*     */     Smooth localSmooth;
/* 109 */     if (i == -1)
/* 110 */       localSmooth = new Smooth(null);
/*     */     else
/* 112 */       localSmooth = new Smooth(null, i);
/* 113 */     System.exit(
/* 114 */       ImageFilterPlus.filterStream(System.in, System.out, localSmooth));
/*     */   }
/*     */ 
/*     */   private static void usage()
/*     */   {
/* 119 */     System.err.println("usage: Smooth [-n N]");
/* 120 */     System.exit(1);
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.filters.Smooth
 * JD-Core Version:    0.6.2
 */