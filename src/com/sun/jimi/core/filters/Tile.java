/*     */ package com.sun.jimi.core.filters;
/*     */ 
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.ImageConsumer;
/*     */ import java.awt.image.ImageFilter;
/*     */ import java.awt.image.ImageProducer;
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ public class Tile extends ImageFilterPlus
/*     */ {
/*     */   private int width;
/*     */   private int height;
/*     */   private int newWidth;
/*     */   private int newHeight;
/*     */   private int nWide;
/*     */   private int nHigh;
/*     */ 
/*     */   public Tile(ImageProducer paramImageProducer, int paramInt1, int paramInt2)
/*     */   {
/*  52 */     super(paramImageProducer, true);
/*  53 */     this.newWidth = paramInt1;
/*  54 */     this.newHeight = paramInt2;
/*     */   }
/*     */ 
/*     */   public static void main(String[] paramArrayOfString)
/*     */   {
/* 111 */     if (paramArrayOfString.length != 2)
/* 112 */       usage();
/* 113 */     Tile localTile = 
/* 114 */       new Tile(null, 
/* 115 */       Integer.parseInt(paramArrayOfString[0]), Integer.parseInt(paramArrayOfString[1]));
/* 116 */     System.exit(
/* 117 */       ImageFilterPlus.filterStream(System.in, System.out, localTile));
/*     */   }
/*     */ 
/*     */   public void setDimensions(int paramInt1, int paramInt2)
/*     */   {
/*  60 */     this.width = paramInt1;
/*  61 */     this.height = paramInt2;
/*  62 */     this.consumer.setDimensions(this.newWidth, this.newHeight);
/*  63 */     this.nWide = ((this.newWidth + paramInt1 - 1) / paramInt1);
/*  64 */     this.nHigh = ((this.newHeight + paramInt2 - 1) / paramInt2);
/*     */   }
/*     */ 
/*     */   public void setPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, ColorModel paramColorModel, byte[] paramArrayOfByte, int paramInt5, int paramInt6)
/*     */   {
/*  69 */     for (int i = 0; i < this.nHigh; i++)
/*     */     {
/*  71 */       int j = i * this.height + paramInt2;
/*  72 */       int k = paramInt4;
/*  73 */       if (j + k > this.newHeight)
/*  74 */         k = this.newHeight - j;
/*  75 */       for (int m = 0; m < this.nWide; m++)
/*     */       {
/*  77 */         int n = m * this.width + paramInt1;
/*  78 */         int i1 = paramInt3;
/*  79 */         if (n + i1 > this.newWidth)
/*  80 */           i1 = this.newWidth - n;
/*  81 */         this.consumer.setPixels(
/*  82 */           n, j, i1, k, paramColorModel, paramArrayOfByte, paramInt5, paramInt6);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, ColorModel paramColorModel, int[] paramArrayOfInt, int paramInt5, int paramInt6)
/*     */   {
/*  89 */     for (int i = 0; i < this.nHigh; i++)
/*     */     {
/*  91 */       int j = i * this.height + paramInt2;
/*  92 */       int k = paramInt4;
/*  93 */       if (j + k > this.newHeight)
/*  94 */         k = this.newHeight - j;
/*  95 */       for (int m = 0; m < this.nWide; m++)
/*     */       {
/*  97 */         int n = m * this.width + paramInt1;
/*  98 */         int i1 = paramInt3;
/*  99 */         if (n + i1 > this.newWidth)
/* 100 */           i1 = this.newWidth - n;
/* 101 */         this.consumer.setPixels(
/* 102 */           n, j, i1, k, paramColorModel, paramArrayOfInt, paramInt5, paramInt6);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void usage()
/*     */   {
/* 122 */     System.err.println("usage: Tile <width> <height>");
/* 123 */     System.exit(1);
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.filters.Tile
 * JD-Core Version:    0.6.2
 */