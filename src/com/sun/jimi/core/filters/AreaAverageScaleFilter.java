/*     */ package com.sun.jimi.core.filters;
/*     */ 
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.ImageConsumer;
/*     */ import java.awt.image.ImageFilter;
/*     */ 
/*     */ public class AreaAverageScaleFilter extends ReplicatingScaleFilter
/*     */ {
/*  26 */   private static final ColorModel rgbmodel = ColorModel.getRGBdefault();
/*     */   private static final int neededHints = 6;
/*  30 */   private boolean passthrough = true;
/*     */   private float[] reds;
/*     */   private float[] greens;
/*     */   private float[] blues;
/*     */   private float[] alphas;
/*     */   private int savedy;
/*     */   private int savedyrem;
/*     */ 
/*     */   public AreaAverageScaleFilter(int paramInt1, int paramInt2)
/*     */   {
/*  42 */     super(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   private void accumPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, ColorModel paramColorModel, Object paramObject, int paramInt5, int paramInt6)
/*     */   {
/*  85 */     if (this.reds == null) {
/*  86 */       makeAccumBuffers();
/*     */     }
/*  88 */     int i = paramInt2;
/*  89 */     int j = this.destHeight;
/*     */     int k;
/*     */     int m;
/*  91 */     if (i == 0) {
/*  92 */       k = 0;
/*  93 */       m = 0;
/*     */     } else {
/*  95 */       k = this.savedy;
/*  96 */       m = this.savedyrem;
/*     */     }
/*  98 */     while (i < paramInt2 + paramInt4)
/*     */     {
/* 100 */       if (m == 0) {
/* 101 */         for (int i1 = 0; i1 < this.destWidth; i1++)
/*     */         {
/*     */           float tmp89_88 = (this.greens[i1] = this.blues[i1] = 0.0F); this.reds[i1] = tmp89_88; this.alphas[i1] = tmp89_88;
/*     */         }
/* 104 */         m = this.srcHeight;
/*     */       }
/*     */       int n;
/* 106 */       if (j < m)
/* 107 */         n = j;
/*     */       else {
/* 109 */         n = m;
/*     */       }
/* 111 */       int i1 = 0;
/* 112 */       int i2 = 0;
/* 113 */       int i3 = 0;
/* 114 */       int i4 = this.srcWidth;
/* 115 */       float f1 = 0.0F; float f2 = 0.0F; float f3 = 0.0F; float f4 = 0.0F;
/* 116 */       while (i1 < paramInt3)
/*     */       {
/*     */         int i5;
/* 117 */         if (i3 == 0) {
/* 118 */           i3 = this.destWidth;
/*     */ 
/* 120 */           if ((paramObject instanceof byte[]))
/* 121 */             i5 = ((byte[])paramObject)[(paramInt5 + i1)] & 0xFF;
/*     */           else {
/* 123 */             i5 = ((int[])paramObject)[(paramInt5 + i1)];
/*     */           }
/* 125 */           i5 = paramColorModel.getRGB(i5);
/* 126 */           f1 = i5 >>> 24;
/* 127 */           f2 = i5 >> 16 & 0xFF;
/* 128 */           f3 = i5 >> 8 & 0xFF;
/* 129 */           f4 = i5 & 0xFF;
/*     */         }
/*     */ 
/* 132 */         if (i3 < i4)
/* 133 */           i5 = i3;
/*     */         else {
/* 135 */           i5 = i4;
/*     */         }
/* 137 */         float f5 = i5 * n;
/* 138 */         this.alphas[i2] += f5 * f1;
/* 139 */         this.reds[i2] += f5 * f2;
/* 140 */         this.greens[i2] += f5 * f3;
/* 141 */         this.blues[i2] += f5 * f4;
/* 142 */         if ((i3 -= i5) == 0) {
/* 143 */           i1++;
/*     */         }
/* 145 */         if ((i4 -= i5) == 0) {
/* 146 */           i2++;
/* 147 */           i4 = this.srcWidth;
/*     */         }
/*     */       }
/* 150 */       if ((m -= n) == 0) {
/* 151 */         int[] arrayOfInt = calcRow();
/*     */         do {
/* 153 */           this.consumer.setPixels(0, k, this.destWidth, 1, 
/* 154 */             rgbmodel, arrayOfInt, 0, this.destWidth);
/* 155 */           k++;
/* 156 */           if ((j -= n) < n) break;  } while (n == this.srcHeight);
/*     */       } else {
/* 158 */         j -= n;
/*     */       }
/* 160 */       if (j == 0) {
/* 161 */         j = this.destHeight;
/* 162 */         i++;
/* 163 */         paramInt5 += paramInt6;
/*     */       }
/*     */     }
/* 166 */     this.savedyrem = m;
/* 167 */     this.savedy = k;
/*     */   }
/*     */ 
/*     */   private int[] calcRow()
/*     */   {
/*  63 */     float f = this.srcWidth * this.srcHeight;
/*  64 */     if ((this.outpixbuf == null) || (!(this.outpixbuf instanceof int[]))) {
/*  65 */       this.outpixbuf = new int[this.destWidth];
/*     */     }
/*  67 */     int[] arrayOfInt = (int[])this.outpixbuf;
/*  68 */     for (int i = 0; i < this.destWidth; i++) {
/*  69 */       int j = Math.round(this.alphas[i] / f);
/*  70 */       int k = Math.round(this.reds[i] / f);
/*  71 */       int m = Math.round(this.greens[i] / f);
/*  72 */       int n = Math.round(this.blues[i] / f);
/*  73 */       if (j < 0) j = 0; else if (j > 255) j = 255;
/*  74 */       if (k < 0) k = 0; else if (k > 255) k = 255;
/*  75 */       if (m < 0) m = 0; else if (m > 255) m = 255;
/*  76 */       if (n < 0) n = 0; else if (n > 255) n = 255;
/*  77 */       arrayOfInt[i] = (j << 24 | k << 16 | m << 8 | n);
/*     */     }
/*  79 */     return arrayOfInt;
/*     */   }
/*     */ 
/*     */   private void makeAccumBuffers()
/*     */   {
/*  56 */     this.reds = new float[this.destWidth];
/*  57 */     this.greens = new float[this.destWidth];
/*  58 */     this.blues = new float[this.destWidth];
/*  59 */     this.alphas = new float[this.destWidth];
/*     */   }
/*     */ 
/*     */   public void setHints(int paramInt)
/*     */   {
/*  51 */     this.passthrough = ((paramInt & 0x6) != 6);
/*  52 */     super.setHints(paramInt);
/*     */   }
/*     */ 
/*     */   public void setPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, ColorModel paramColorModel, byte[] paramArrayOfByte, int paramInt5, int paramInt6)
/*     */   {
/* 182 */     if (this.passthrough)
/* 183 */       super.setPixels(paramInt1, paramInt2, paramInt3, paramInt4, paramColorModel, paramArrayOfByte, paramInt5, paramInt6);
/*     */     else
/* 185 */       accumPixels(paramInt1, paramInt2, paramInt3, paramInt4, paramColorModel, paramArrayOfByte, paramInt5, paramInt6);
/*     */   }
/*     */ 
/*     */   public void setPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, ColorModel paramColorModel, int[] paramArrayOfInt, int paramInt5, int paramInt6)
/*     */   {
/* 201 */     if (this.passthrough)
/* 202 */       super.setPixels(paramInt1, paramInt2, paramInt3, paramInt4, paramColorModel, paramArrayOfInt, paramInt5, paramInt6);
/*     */     else
/* 204 */       accumPixels(paramInt1, paramInt2, paramInt3, paramInt4, paramColorModel, paramArrayOfInt, paramInt5, paramInt6);
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.filters.AreaAverageScaleFilter
 * JD-Core Version:    0.6.2
 */