/*     */ package com.sun.jimi.core.filters;
/*     */ 
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.ImageConsumer;
/*     */ import java.awt.image.ImageFilter;
/*     */ import java.awt.image.ImageProducer;
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ public class Enlarge extends ImageFilterPlus
/*     */ {
/*     */   private int multiplier;
/*     */   private int newWidth;
/*     */   private int newHeight;
/*     */ 
/*     */   public Enlarge(ImageProducer paramImageProducer, int paramInt)
/*     */   {
/*  54 */     super(paramImageProducer);
/*  55 */     this.multiplier = paramInt;
/*     */   }
/*     */ 
/*     */   public static void main(String[] paramArrayOfString)
/*     */   {
/* 126 */     if (paramArrayOfString.length != 1)
/* 127 */       usage();
/* 128 */     Enlarge localEnlarge = 
/* 129 */       new Enlarge(null, Integer.parseInt(paramArrayOfString[0]));
/* 130 */     System.exit(
/* 131 */       ImageFilterPlus.filterStream(System.in, System.out, localEnlarge));
/*     */   }
/*     */ 
/*     */   public void setDimensions(int paramInt1, int paramInt2)
/*     */   {
/*  61 */     this.newWidth = (paramInt1 * this.multiplier);
/*  62 */     this.newHeight = (paramInt2 * this.multiplier);
/*  63 */     this.consumer.setDimensions(this.newWidth, this.newHeight);
/*     */   }
/*     */ 
/*     */   public void setPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, ColorModel paramColorModel, byte[] paramArrayOfByte, int paramInt5, int paramInt6)
/*     */   {
/*  68 */     int i = Math.min(paramInt1 * this.multiplier, this.newWidth - 1);
/*  69 */     int j = Math.min(paramInt2 * this.multiplier, this.newHeight - 1);
/*  70 */     int k = paramInt3 * this.multiplier;
/*  71 */     if (i + k > this.newWidth)
/*  72 */       k = this.newWidth - i;
/*  73 */     int m = paramInt4 * this.multiplier;
/*  74 */     if (j + m > this.newHeight)
/*  75 */       m = this.newHeight - j;
/*  76 */     byte[] arrayOfByte = new byte[k * m];
/*  77 */     for (int n = 0; n < paramInt4; n++)
/*     */     {
/*  79 */       for (int i1 = 0; i1 < paramInt3; i1++)
/*     */       {
/*  81 */         int i2 = paramArrayOfByte[(n * paramInt6 + paramInt5 + i1)];
/*  82 */         for (int i3 = 0; i3 < this.multiplier; i3++)
/*  83 */           for (int i4 = 0; i4 < this.multiplier; i4++)
/*     */           {
/*  85 */             int i5 = n * this.multiplier + i3;
/*  86 */             int i6 = i1 * this.multiplier + i4;
/*  87 */             arrayOfByte[(i5 * k + i6)] = (byte) i2;
/*     */           }
/*     */       }
/*     */     }
/*  91 */     this.consumer.setPixels(i, j, k, m, paramColorModel, arrayOfByte, 0, k);
/*     */   }
/*     */ 
/*     */   public void setPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, ColorModel paramColorModel, int[] paramArrayOfInt, int paramInt5, int paramInt6)
/*     */   {
/*  96 */     int i = Math.min(paramInt1 * this.multiplier, this.newWidth - 1);
/*  97 */     int j = Math.min(paramInt2 * this.multiplier, this.newHeight - 1);
/*  98 */     int k = paramInt3 * this.multiplier;
/*  99 */     if (i + k > this.newWidth)
/* 100 */       k = this.newWidth - i;
/* 101 */     int m = paramInt4 * this.multiplier;
/* 102 */     if (j + m > this.newHeight)
/* 103 */       m = this.newHeight - j;
/* 104 */     int[] arrayOfInt = new int[k * m];
/* 105 */     for (int n = 0; n < paramInt4; n++)
/*     */     {
/* 107 */       for (int i1 = 0; i1 < paramInt3; i1++)
/*     */       {
/* 109 */         int i2 = paramArrayOfInt[(n * paramInt6 + paramInt5 + i1)];
/* 110 */         for (int i3 = 0; i3 < this.multiplier; i3++)
/* 111 */           for (int i4 = 0; i4 < this.multiplier; i4++)
/*     */           {
/* 113 */             int i5 = n * this.multiplier + i3;
/* 114 */             int i6 = i1 * this.multiplier + i4;
/* 115 */             arrayOfInt[(i5 * k + i6)] = i2;
/*     */           }
/*     */       }
/*     */     }
/* 119 */     this.consumer.setPixels(i, j, k, m, paramColorModel, arrayOfInt, 0, k);
/*     */   }
/*     */ 
/*     */   private static void usage()
/*     */   {
/* 136 */     System.err.println("usage: Enlarge <multiplier>");
/* 137 */     System.exit(1);
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.filters.Enlarge
 * JD-Core Version:    0.6.2
 */