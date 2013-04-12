/*     */ package com.sun.jimi.core.filters;
/*     */ 
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.ImageConsumer;
/*     */ import java.awt.image.ImageFilter;
/*     */ 
/*     */ public class Rotate extends ImageFilter
/*     */ {
/*     */   private double angle;
/*     */   private double cos;
/*     */   private double sin;
/*     */   private Rectangle rotatedSpace;
/*     */   private Rectangle originalSpace;
/*     */   private ColorModel defaultRGBModel;
/*     */   private int[] inPixels;
/*     */   private int[] outPixels;
/*     */ 
/*     */   public Rotate(double paramDouble)
/*     */   {
/*  17 */     this.angle = (paramDouble * 0.0174532925199433D);
/*  18 */     this.cos = Math.cos(this.angle);
/*  19 */     this.sin = Math.sin(this.angle);
/*  20 */     this.defaultRGBModel = ColorModel.getRGBdefault();
/*     */   }
/*     */ 
/*     */   public void imageComplete(int paramInt)
/*     */   {
/* 147 */     if ((paramInt == 1) || (paramInt == 4))
/*     */     {
/* 149 */       this.consumer.imageComplete(paramInt);
/* 150 */       return;
/*     */     }
/* 152 */     double[] arrayOfDouble1 = new double[2];
/* 153 */     int i = this.originalSpace.width;
/* 154 */     int j = this.originalSpace.height;
/* 155 */     int k = this.rotatedSpace.width;
/* 156 */     int m = this.rotatedSpace.height;
/*     */ 
/* 159 */     this.outPixels = new int[k * m];
/* 160 */     int n = this.rotatedSpace.x;
/* 161 */     int i1 = this.rotatedSpace.y;
/* 162 */     double[] arrayOfDouble2 = new double[2];
/* 163 */     int i4 = 0;
/* 164 */     for (int i5 = 0; i5 < m; i5++)
/*     */     {
/* 166 */       for (int i6 = 0; i6 < k; i6++)
/*     */       {
/* 169 */         transformBack(n + i6, i1 + i5, arrayOfDouble1);
/* 170 */         int i2 = (int)Math.round(arrayOfDouble1[0]);
/* 171 */         int i3 = (int)Math.round(arrayOfDouble1[1]);
/*     */ 
/* 176 */         if ((i2 < 0) || (i2 >= i) || 
/* 177 */           (i3 < 0) || (i3 >= j))
/*     */         {
/* 179 */           this.outPixels[(i4++)] = 0;
/*     */         }
/*     */         else
/*     */         {
/* 183 */           this.outPixels[(i4++)] = this.inPixels[(i3 * i + i2)];
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 188 */     this.consumer.setPixels(0, 0, k, m, this.defaultRGBModel, 
/* 189 */       this.outPixels, 0, k);
/*     */ 
/* 192 */     this.consumer.imageComplete(paramInt);
/*     */   }
/*     */ 
/*     */   public void setColorModel(ColorModel paramColorModel)
/*     */   {
/*  91 */     this.consumer.setColorModel(this.defaultRGBModel);
/*     */   }
/*     */ 
/*     */   public void setDimensions(int paramInt1, int paramInt2)
/*     */   {
/*  75 */     this.originalSpace = new Rectangle(0, 0, paramInt1, paramInt2);
/*  76 */     this.rotatedSpace = new Rectangle(0, 0, paramInt1, paramInt2);
/*  77 */     transformSpace(this.rotatedSpace);
/*  78 */     this.inPixels = new int[this.originalSpace.width * this.originalSpace.height];
/*  79 */     this.consumer.setDimensions(this.rotatedSpace.width, this.rotatedSpace.height);
/*     */   }
/*     */ 
/*     */   public void setPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, ColorModel paramColorModel, byte[] paramArrayOfByte, int paramInt5, int paramInt6)
/*     */   {
/* 102 */     int i = paramInt2 * this.originalSpace.width + paramInt1;
/* 103 */     int j = paramInt5;
/* 104 */     int k = paramInt6 - paramInt3;
/* 105 */     int m = this.originalSpace.width - paramInt3;
/* 106 */     for (int n = 0; n < paramInt4; n++)
/*     */     {
/* 108 */       for (int i1 = 0; i1 < paramInt3; i1++)
/*     */       {
/* 110 */         this.inPixels[(i++)] = paramColorModel.getRGB(paramArrayOfByte[(j++)] & 0xFF);
/*     */       }
/* 112 */       j += k;
/* 113 */       i += m;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, ColorModel paramColorModel, int[] paramArrayOfInt, int paramInt5, int paramInt6)
/*     */   {
/* 125 */     int i = paramInt2 * this.originalSpace.width + paramInt1;
/* 126 */     int j = paramInt5;
/* 127 */     int k = paramInt6 - paramInt3;
/* 128 */     int m = this.originalSpace.width - paramInt3;
/* 129 */     for (int n = 0; n < paramInt4; n++)
/*     */     {
/* 131 */       for (int i1 = 0; i1 < paramInt3; i1++)
/*     */       {
/* 133 */         this.inPixels[(i++)] = paramColorModel.getRGB(paramArrayOfInt[(j++)]);
/*     */       }
/* 135 */       j += k;
/* 136 */       i += m;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void transform(int paramInt1, int paramInt2, double[] paramArrayOfDouble)
/*     */   {
/*  25 */     paramArrayOfDouble[0] = (paramInt1 * this.cos + paramInt2 * this.sin);
/*  26 */     paramArrayOfDouble[1] = (paramInt2 * this.cos - paramInt1 * this.sin);
/*     */   }
/*     */ 
/*     */   private void transformBack(int paramInt1, int paramInt2, double[] paramArrayOfDouble)
/*     */   {
/*  31 */     paramArrayOfDouble[0] = (paramInt1 * this.cos - paramInt2 * this.sin);
/*  32 */     paramArrayOfDouble[1] = (paramInt2 * this.cos + paramInt1 * this.sin);
/*     */   }
/*     */ 
/*     */   public void transformSpace(Rectangle paramRectangle)
/*     */   {
/*  37 */     double[] arrayOfDouble = new double[2];
/*     */ 
/*  39 */     double d1 = 1.7976931348623157E+308D;
/*  40 */     double d2 = 1.7976931348623157E+308D;
/*  41 */     double d3 = Double.MIN_VALUE;
/*  42 */     double d4 = Double.MIN_VALUE;
/*  43 */     int i = paramRectangle.width;
/*  44 */     int j = paramRectangle.height;
/*  45 */     int k = paramRectangle.x;
/*  46 */     int m = paramRectangle.y;
/*     */ 
/*  48 */     for (int n = 0; n < 4; n++)
/*     */     {
/*  50 */       switch (n) {
/*     */       case 0:
/*  52 */         transform(k, m, arrayOfDouble); break;
/*     */       case 1:
/*  53 */         transform(k + i, m, arrayOfDouble); break;
/*     */       case 2:
/*  54 */         transform(k, m + j, arrayOfDouble); break;
/*     */       case 3:
/*  55 */         transform(k + i, m + j, arrayOfDouble); break;
/*     */       }
/*  57 */       d1 = Math.min(d1, arrayOfDouble[0]);
/*  58 */       d2 = Math.min(d2, arrayOfDouble[1]);
/*  59 */       d3 = Math.max(d3, arrayOfDouble[0]);
/*  60 */       d4 = Math.max(d4, arrayOfDouble[1]);
/*     */     }
/*  62 */     paramRectangle.x = ((int)Math.floor(d1));
/*  63 */     paramRectangle.y = ((int)Math.floor(d2));
/*  64 */     paramRectangle.width = ((int)Math.ceil(d3) - paramRectangle.x);
/*  65 */     paramRectangle.height = ((int)Math.ceil(d4) - paramRectangle.y);
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.filters.Rotate
 * JD-Core Version:    0.6.2
 */