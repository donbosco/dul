/*     */ package com.sun.jimi.core.filters;
/*     */ 
/*     */ import java.awt.image.ImageProducer;
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ public class Oil extends RGBAllFilter
/*     */ {
/*     */   private int n;
/*     */ 
/*     */   public Oil(ImageProducer paramImageProducer)
/*     */   {
/*  62 */     this(paramImageProducer, 3);
/*     */   }
/*     */ 
/*     */   public Oil(ImageProducer paramImageProducer, int paramInt)
/*     */   {
/*  55 */     super(paramImageProducer);
/*  56 */     this.n = paramInt;
/*     */   }
/*     */ 
/*     */   public void filterRGBAll(int paramInt1, int paramInt2, int[][] paramArrayOfInt)
/*     */   {
/*  68 */     int[][] arrayOfInt = new int[paramInt2][paramInt1];
/*  69 */     int[] arrayOfInt1 = new int[256];
/*  70 */     int[] arrayOfInt2 = new int[256];
/*  71 */     int[] arrayOfInt3 = new int[256];
/*     */ 
/*  73 */     for (int i = 0; i < paramInt2; i++)
/*     */     {
/*  75 */       for (int j = 0; j < paramInt1; j++)
/*     */       {
/*  77 */         for (int k = 0; k < 256; k++)
/*     */         {
/*     */           int tmp62_61 = (arrayOfInt3[k] = 0); arrayOfInt2[k] = tmp62_61; arrayOfInt1[k] = tmp62_61;
/*  79 */         }int i1;
for (int m = i - this.n; m <= i + this.n; m++)
/*  80 */           if ((m >= 0) && (m < paramInt2))
/*  81 */             for (i1 = j - this.n; i1 <= j + this.n; i1++)
/*  82 */               if ((i1 >= 0) && (i1 < paramInt1))
/*     */               {
/*  84 */                 int i2 = paramArrayOfInt[m][i1];
/*  85 */                 arrayOfInt1[(i2 >> 16 & 0xFF)] += 1;
/*  86 */                 arrayOfInt2[(i2 >> 8 & 0xFF)] += 1;
/*  87 */                 arrayOfInt3[(i2 & 0xFF)] += 1;
/*     */               }
/*  89 */         i1 = 0; int i2 = 0; int i3 = 0;
/*  90 */         for (int i4 = 1; i4 < 256; i4++)
/*     */         {
/*  92 */           if (arrayOfInt1[i4] > arrayOfInt1[i1])
/*  93 */             i1 = i4;
/*  94 */           if (arrayOfInt2[i4] > arrayOfInt2[i2])
/*  95 */             i2 = i4;
/*  96 */           if (arrayOfInt3[i4] > arrayOfInt3[i3])
/*  97 */             i3 = i4;
/*     */         }
/*  99 */         arrayOfInt[i][j] = 
/* 100 */           (0xFF000000 | i1 << 16 | i2 << 8 | i3);
/*     */       }
/*     */     }
/* 103 */     setPixels(paramInt1, paramInt2, arrayOfInt);
/*     */   }
/*     */ 
/*     */   public static void main(String[] paramArrayOfString)
/*     */   {
/* 110 */     int i = -1;
/* 111 */     int j = paramArrayOfString.length;
/*     */ int k = 0;
/* 113 */     for ( k = 0; (k < j) && (paramArrayOfString[k].charAt(0) == '-'); k++)
/*     */     {
/* 115 */       if ((paramArrayOfString[k].equals("-n")) && (k + 1 < j))
/*     */       {
/* 117 */         k++;
/* 118 */         i = Integer.parseInt(paramArrayOfString[k]);
/*     */       }
/*     */       else {
/* 121 */         usage();
/*     */       }
/*     */     }

/* 123 */     if (k != j)
/* 124 */       usage();
/*     */     Oil localOil;
/* 127 */     if (i == -1)
/* 128 */       localOil = new Oil(null);
/*     */     else
/* 130 */       localOil = new Oil(null, i);
/* 131 */     System.exit(
/* 132 */       ImageFilterPlus.filterStream(System.in, System.out, localOil));
/*     */   }
/*     */ 
/*     */   private static void usage()
/*     */   {
/* 137 */     System.err.println("usage: Oil [-n N]");
/* 138 */     System.exit(1);
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.filters.Oil
 * JD-Core Version:    0.6.2
 */