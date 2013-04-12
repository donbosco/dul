/*     */ package com.sun.jimi.core.filters;
/*     */ 
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.ImageConsumer;
/*     */ import java.awt.image.ImageFilter;
/*     */ import java.util.Hashtable;
/*     */ 
/*     */ public class ReplicatingScaleFilter extends ImageFilter
/*     */ {
/*     */   protected int srcWidth;
/*     */   protected int srcHeight;
/*     */   protected int destWidth;
/*     */   protected int destHeight;
/*     */   protected int[] srcrows;
/*     */   protected int[] srccols;
/*     */   protected Object outpixbuf;
/*     */ 
/*     */   public ReplicatingScaleFilter(int paramInt1, int paramInt2)
/*     */   {
/*  44 */     this.destWidth = paramInt1;
/*  45 */     this.destHeight = paramInt2;
/*     */   }
/*     */ 
/*     */   private void calculateMaps()
/*     */   {
/*  86 */     this.srcrows = new int[this.destHeight + 1];
/*  87 */     for (int i = 0; i <= this.destHeight; i++) {
/*  88 */       this.srcrows[i] = ((2 * i * this.srcHeight + this.srcHeight) / (2 * this.destHeight));
/*     */     }
/*  90 */     this.srccols = new int[this.destWidth + 1];
/*  91 */     for (int j = 0; j <= this.destWidth; j++)
/*  92 */       this.srccols[j] = ((2 * j * this.srcWidth + this.srcWidth) / (2 * this.destWidth));
/*     */   }
/*     */ 
/*     */   public void setDimensions(int paramInt1, int paramInt2)
/*     */   {
/*  70 */     this.srcWidth = paramInt1;
/*  71 */     this.srcHeight = paramInt2;
/*  72 */     if (this.destWidth < 0) {
/*  73 */       if (this.destHeight < 0) {
/*  74 */         this.destWidth = this.srcWidth;
/*  75 */         this.destHeight = this.srcHeight;
/*     */       } else {
/*  77 */         this.destWidth = (this.srcWidth * this.destHeight / this.srcHeight);
/*     */       }
/*  79 */     } else if (this.destHeight < 0) {
/*  80 */       this.destHeight = (this.srcHeight * this.destWidth / this.srcWidth);
/*     */     }
/*  82 */     this.consumer.setDimensions(this.destWidth, this.destHeight);
/*     */   }
/*     */ 
/*     */   public void setPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, ColorModel paramColorModel, byte[] paramArrayOfByte, int paramInt5, int paramInt6)
/*     */   {
/* 104 */     if ((this.srcrows == null) || (this.srccols == null)) {
/* 105 */       calculateMaps();
/*     */     }
/*     */ 
/* 108 */     int k = (2 * paramInt1 * this.destWidth + this.srcWidth - 1) / (2 * this.srcWidth);
/* 109 */     int m = (2 * paramInt2 * this.destHeight + this.srcHeight - 1) / (2 * this.srcHeight);
/*     */     byte[] arrayOfByte;
/* 111 */     if ((this.outpixbuf != null) && ((this.outpixbuf instanceof byte[]))) {
/* 112 */       arrayOfByte = (byte[])this.outpixbuf;
/*     */     } else {
/* 114 */       arrayOfByte = new byte[this.destWidth];
/* 115 */       this.outpixbuf = arrayOfByte;
/*     */     }
/*     */     int j;
/* 117 */     for (int n = m; (j = this.srcrows[n]) < paramInt2 + paramInt4; n++) {
/* 118 */       int i1 = paramInt5 + paramInt6 * (j - paramInt2);
/*     */       int i;
int i2 = k;
/* 120 */       for ( i2 = k; (i = this.srccols[i2]) < paramInt1 + paramInt3; i2++) {
/* 121 */         arrayOfByte[i2] = paramArrayOfByte[(i1 + i - paramInt1)];
/*     */       }
/* 123 */       if (i2 > k)
/* 124 */         this.consumer.setPixels(k, n, i2 - k, 1, 
/* 125 */           paramColorModel, arrayOfByte, k, this.destWidth);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, ColorModel paramColorModel, int[] paramArrayOfInt, int paramInt5, int paramInt6)
/*     */   {
/* 138 */     if ((this.srcrows == null) || (this.srccols == null)) {
/* 139 */       calculateMaps();
/*     */     }
/*     */ 
/* 142 */     int k = (2 * paramInt1 * this.destWidth + this.srcWidth - 1) / (2 * this.srcWidth);
/* 143 */     int m = (2 * paramInt2 * this.destHeight + this.srcHeight - 1) / (2 * this.srcHeight);
/*     */     int[] arrayOfInt;
/* 145 */     if ((this.outpixbuf != null) && ((this.outpixbuf instanceof int[]))) {
/* 146 */       arrayOfInt = (int[])this.outpixbuf;
/*     */     } else {
/* 148 */       arrayOfInt = new int[this.destWidth];
/* 149 */       this.outpixbuf = arrayOfInt;
/*     */     }
/*     */     int j;
/* 151 */     for (int n = m; (j = this.srcrows[n]) < paramInt2 + paramInt4; n++) {
/* 152 */       int i1 = paramInt5 + paramInt6 * (j - paramInt2);
/*     */       int i;
int i2 = k;
/* 154 */       for ( i2 = k; (i = this.srccols[i2]) < paramInt1 + paramInt3; i2++) {
/* 155 */         arrayOfInt[i2] = paramArrayOfInt[(i1 + i - paramInt1)];
/*     */       }
/* 157 */       if (i2 > k)
/* 158 */         this.consumer.setPixels(k, n, i2 - k, 1, 
/* 159 */           paramColorModel, arrayOfInt, k, this.destWidth);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setProperties(Hashtable paramHashtable)
/*     */   {
/*  53 */     paramHashtable = (Hashtable)paramHashtable.clone();
/*  54 */     String str1 = "rescale";
/*  55 */     String str2 = this.destWidth + "x" + this.destHeight;
/*  56 */     Object localObject = paramHashtable.get(str1);
/*  57 */     if ((localObject != null) && ((localObject instanceof String))) {
/*  58 */       str2 = (String)localObject + ", " + str2;
/*     */     }
/*  60 */     paramHashtable.put(str1, str2);
/*  61 */     super.setProperties(paramHashtable);
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.filters.ReplicatingScaleFilter
 * JD-Core Version:    0.6.2
 */