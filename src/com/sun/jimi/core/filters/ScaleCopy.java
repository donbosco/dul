/*     */ package com.sun.jimi.core.filters;
/*     */ 
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.ImageConsumer;
/*     */ import java.awt.image.ImageFilter;
/*     */ import java.awt.image.ImageProducer;
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ public class ScaleCopy extends ImageFilterPlus
/*     */ {
/*     */   private double xScale;
/*     */   private double yScale;
/*     */   private int newWidth;
/*     */   private int newHeight;
/*     */ 
/*     */   public ScaleCopy(ImageProducer paramImageProducer, double paramDouble)
/*     */   {
/*  59 */     this(paramImageProducer, paramDouble, paramDouble);
/*     */   }
/*     */ 
/*     */   public ScaleCopy(ImageProducer paramImageProducer, double paramDouble1, double paramDouble2)
/*     */   {
/*  65 */     super(paramImageProducer);
/*  66 */     this.xScale = paramDouble1;
/*  67 */     this.yScale = paramDouble2;
/*     */   }
/*     */ 
/*     */   public static void main(String[] paramArrayOfString)
/*     */   {
/* 138 */     ScaleCopy localScaleCopy = null;
/* 139 */     if (paramArrayOfString.length == 1)
/* 140 */       localScaleCopy = new ScaleCopy(null, 
/* 141 */         Double.valueOf(paramArrayOfString[0]).doubleValue());
/* 142 */     else if (paramArrayOfString.length == 2)
/* 143 */       localScaleCopy = new ScaleCopy(null, 
/* 144 */         Double.valueOf(paramArrayOfString[0]).doubleValue(), 
/* 145 */         Double.valueOf(paramArrayOfString[1]).doubleValue());
/*     */     else
/* 147 */       usage();
/* 148 */     System.exit(
/* 149 */       ImageFilterPlus.filterStream(System.in, System.out, localScaleCopy));
/*     */   }
/*     */ 
/*     */   public void setDimensions(int paramInt1, int paramInt2)
/*     */   {
/*  73 */     this.newWidth = ((int)(paramInt1 * this.xScale));
/*  74 */     this.newHeight = ((int)(paramInt2 * this.yScale));
/*  75 */     this.consumer.setDimensions(this.newWidth, this.newHeight);
/*     */   }
/*     */ 
/*     */   public void setPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, ColorModel paramColorModel, byte[] paramArrayOfByte, int paramInt5, int paramInt6)
/*     */   {
/*  80 */     int i = Math.min((int)(paramInt1 * this.xScale), this.newWidth - 1);
/*  81 */     int j = Math.min((int)(paramInt2 * this.yScale), this.newHeight - 1);
/*  82 */     int k = Math.max((int)(paramInt3 * this.xScale), 1);
/*  83 */     if (i + k > this.newWidth)
/*  84 */       k = this.newWidth - i;
/*  85 */     int m = Math.max((int)(paramInt4 * this.yScale), 1);
/*  86 */     if (j + m > this.newHeight)
/*  87 */       m = this.newHeight - j;
/*  88 */     byte[] arrayOfByte = new byte[k * m];
/*  89 */     for (int n = 0; n < m; n++)
/*     */     {
/*  91 */       int i1 = (int)(n / this.yScale);
/*  92 */       if (i1 < paramInt4)
/*     */       {
/*  94 */         for (int i2 = 0; i2 < k; i2++)
/*     */         {
/*  96 */           int i3 = (int)(i2 / this.xScale);
/*  97 */           if (i3 < paramInt3)
/*     */           {
/*  99 */             arrayOfByte[(n * k + i2)] = 
/* 100 */               paramArrayOfByte[(i1 * paramInt6 + paramInt5 + i3)];
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 103 */     this.consumer.setPixels(i, j, k, m, paramColorModel, arrayOfByte, 0, k);
/*     */   }
/*     */ 
/*     */   public void setPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, ColorModel paramColorModel, int[] paramArrayOfInt, int paramInt5, int paramInt6)
/*     */   {
/* 108 */     int i = Math.min((int)(paramInt1 * this.xScale), this.newWidth - 1);
/* 109 */     int j = Math.min((int)(paramInt2 * this.yScale), this.newHeight - 1);
/* 110 */     int k = Math.max((int)(paramInt3 * this.xScale), 1);
/* 111 */     if (i + k > this.newWidth)
/* 112 */       k = this.newWidth - i;
/* 113 */     int m = Math.max((int)(paramInt4 * this.yScale), 1);
/* 114 */     if (j + m > this.newHeight)
/* 115 */       m = this.newHeight - j;
/* 116 */     int[] arrayOfInt = new int[k * m];
/* 117 */     for (int n = 0; n < m; n++)
/*     */     {
/* 119 */       int i1 = (int)(n / this.yScale);
/* 120 */       if (i1 < paramInt4)
/*     */       {
/* 122 */         for (int i2 = 0; i2 < k; i2++)
/*     */         {
/* 124 */           int i3 = (int)(i2 / this.xScale);
/* 125 */           if (i3 < paramInt3)
/*     */           {
/* 127 */             arrayOfInt[(n * k + i2)] = 
/* 128 */               paramArrayOfInt[(i1 * paramInt6 + paramInt5 + i3)];
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 131 */     this.consumer.setPixels(i, j, k, m, paramColorModel, arrayOfInt, 0, k);
/*     */   }
/*     */ 
/*     */   private static void usage()
/*     */   {
/* 154 */     System.err.println("usage: ScaleCopy scale");
/* 155 */     System.err.println("or:    ScaleCopy xScale yScale");
/* 156 */     System.exit(1);
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.filters.ScaleCopy
 * JD-Core Version:    0.6.2
 */