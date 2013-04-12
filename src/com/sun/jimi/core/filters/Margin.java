/*     */ package com.sun.jimi.core.filters;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.DirectColorModel;
/*     */ import java.awt.image.ImageConsumer;
/*     */ import java.awt.image.ImageFilter;
/*     */ import java.awt.image.ImageProducer;
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ public class Margin extends ImageFilterPlus
/*     */ {
/*     */   private Color color;
/*     */   private int size;
/*     */   private int width;
/*     */   private int height;
/*     */   private int newWidth;
/*  69 */   private boolean started = false;
/*     */ 
/*     */   public Margin(ImageProducer paramImageProducer, Color paramColor, int paramInt)
/*     */   {
/*  54 */     super(paramImageProducer, true);
/*  55 */     this.color = paramColor;
/*  56 */     this.size = paramInt;
/*     */   }
/*     */ 
/*     */   public static void main(String[] paramArrayOfString)
/*     */   {
/* 126 */     if (paramArrayOfString.length != 1)
/* 127 */       usage();
/* 128 */     Margin localMargin = 
/* 129 */       new Margin(null, Color.black, Integer.parseInt(paramArrayOfString[0]));
/* 130 */     System.exit(
/* 131 */       ImageFilterPlus.filterStream(System.in, System.out, localMargin));
/*     */   }
/*     */ 
/*     */   public void resendTopDownLeftRight(ImageProducer paramImageProducer)
/*     */   {
/*  72 */     this.started = false;
/*  73 */     super.resendTopDownLeftRight(paramImageProducer);
/*     */   }
/*     */ 
/*     */   public void setDimensions(int paramInt1, int paramInt2)
/*     */   {
/*  62 */     this.width = paramInt1;
/*  63 */     this.height = paramInt2;
/*  64 */     this.newWidth = (paramInt1 + this.size * 2);
/*  65 */     this.consumer.setDimensions(this.newWidth, paramInt2 + this.size * 2);
/*  66 */     this.started = false;
/*     */   }
/*     */ 
/*     */   public void setPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, ColorModel paramColorModel, byte[] paramArrayOfByte, int paramInt5, int paramInt6)
/*     */   {
/* 108 */     if (!this.started)
/* 109 */       start();
/* 110 */     this.consumer.setPixels(
/* 111 */       paramInt1 + this.size, paramInt2 + this.size, paramInt3, paramInt4, paramColorModel, paramArrayOfByte, paramInt5, paramInt6);
/*     */   }
/*     */ 
/*     */   public void setPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, ColorModel paramColorModel, int[] paramArrayOfInt, int paramInt5, int paramInt6)
/*     */   {
/* 116 */     if (!this.started)
/* 117 */       start();
/* 118 */     this.consumer.setPixels(
/* 119 */       paramInt1 + this.size, paramInt2 + this.size, paramInt3, paramInt4, paramColorModel, paramArrayOfInt, paramInt5, paramInt6);
/*     */   }
/*     */ 
/*     */   private void start()
/*     */   {
/*  78 */     DirectColorModel localDirectColorModel = new DirectColorModel(24, 16711680, 65280, 255);
/*  79 */     this.started = true;
/*  80 */     int i = this.color.getRGB();
/*     */ 
/*  82 */     int[] arrayOfInt1 = new int[this.newWidth];
/*  83 */     for (int j = 0; j < this.newWidth; j++)
/*  84 */       arrayOfInt1[j] = i;
/*  85 */     for (int k = 0; k < this.size; k++)
/*     */     {
/*  87 */       this.consumer.setPixels(
/*  88 */         0, k, this.newWidth, 1, localDirectColorModel, arrayOfInt1, 0, this.newWidth);
/*  89 */       this.consumer.setPixels(
/*  90 */         0, this.size + this.height + k, this.newWidth, 1, localDirectColorModel, arrayOfInt1, 0, 
/*  91 */         this.newWidth);
/*     */     }
/*     */ 
/*  94 */     int[] arrayOfInt2 = new int[this.size];
/*  95 */     for (int m = 0; m < this.size; m++)
/*  96 */       arrayOfInt2[m] = i;
/*  97 */     for (int n = 0; n < this.height; n++)
/*     */     {
/*  99 */       this.consumer.setPixels(
/* 100 */         0, this.size + n, this.size, 1, localDirectColorModel, arrayOfInt2, 0, this.size);
/* 101 */       this.consumer.setPixels(
/* 102 */         this.size + this.width, this.size + n, this.size, 1, localDirectColorModel, arrayOfInt2, 0, this.size);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void usage()
/*     */   {
/* 136 */     System.err.println("usage: Margin <size>");
/* 137 */     System.exit(1);
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.filters.Margin
 * JD-Core Version:    0.6.2
 */