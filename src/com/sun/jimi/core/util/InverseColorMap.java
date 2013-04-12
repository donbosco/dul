/*     */ package com.sun.jimi.core.util;
/*     */ 
/*     */ class InverseColorMap
/*     */ {
/*     */   static final int QUANTBITS = 5;
/*     */   static final int TRUNCBITS = 3;
/*     */   static final int QUANTMASK_BLUE = 31;
/*     */   static final int QUANTMASK_GREEN = 992;
/*     */   static final int QUANTMASK_RED = 31744;
/*     */   static final int MAXQUANTVAL = 32;
/*     */   byte[] rgbCMap_;
/*     */   int numColors_;
/*     */   int maxColor_;
/*     */   byte[] irgb_;
/*     */ 
/*     */   InverseColorMap(byte[] paramArrayOfByte)
/*     */   {
/*  50 */     this.rgbCMap_ = paramArrayOfByte;
/*  51 */     this.numColors_ = (this.rgbCMap_.length / 4);
/*     */ 
/*  53 */     this.irgb_ = new byte[32768];
/*  54 */     initIRGB(new int[32768]);
/*     */   }
/*     */ 
/*     */   public final int getIndexNearest(int paramInt)
/*     */   {
/* 116 */     return this.irgb_[
/* 117 */       ((paramInt >> 9 & 0x7C00) + (
/* 117 */       paramInt >> 6 & 0x3E0) + (
/* 118 */       paramInt >> 3 & 0x1F))] & 0xFF;
/*     */   }
/*     */ 
/*     */   public final int getIndexNearest(int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 127 */     return this.irgb_[
/* 128 */       ((paramInt1 << 7 & 0x7C00) + (
/* 128 */       paramInt2 << 2 & 0x3E0) + (
/* 129 */       paramInt3 >> 3 & 0x1F))] & 0xFF;
/*     */   }
/*     */ 
/*     */   void initIRGB(int[] paramArrayOfInt)
/*     */   {
/*  66 */     int i12 = 8;
/*  67 */     int i13 = 64;
/*  68 */     int i14 = i13 + i13;
/*     */ 
/*  70 */     byte[] arrayOfByte = this.irgb_;
/*     */ 
/*  72 */     for (int i = 0; i < this.numColors_; i++)
/*     */     {
/*  74 */       int j = this.rgbCMap_[(i * 4)] & 0xFF;
/*  75 */       int i2 = this.rgbCMap_[(i * 4 + 1)] & 0xFF;
/*  76 */       int i7 = this.rgbCMap_[(i * 4 + 2)] & 0xFF;
/*     */ 
/*  78 */       int m = j - i12 / 2;
/*  79 */       int i4 = i2 - i12 / 2;
/*  80 */       int i9 = i7 - i12 / 2;
/*  81 */       m = m * m + i4 * i4 + i9 * i9;
/*     */ 
/*  83 */       int n = 2 * (i13 - (j << 3));
/*  84 */       int i5 = 2 * (i13 - (i2 << 3));
/*  85 */       int i10 = 2 * (i13 - (i7 << 3));
/*     */ 
/*  87 */       int i15 = 0;
/*  88 */       int k = 0; for (int i1 = n; k < 32; 
/*  89 */         i1 += i14)
/*     */       {
/*  91 */         int i3 = 0; i4 = m; for (int i6 = i5; i3 < 32; 
/*  92 */           i6 += i14)
/*     */         {
/*  94 */           int i8 = 0; i9 = i4; for (int i11 = i10; i8 < 32; 
/*  95 */             i11 += i14)
/*     */           {
/*  97 */             if ((i == 0) || (paramArrayOfInt[i15] > i9))
/*     */             {
/*  99 */               paramArrayOfInt[i15] = i9;
/* 100 */               arrayOfByte[i15] = ((byte)i);
/*     */             }
/*  95 */             i9 += i11; i8++; i15++;
/*     */           }
/*  92 */           i4 += i6; i3++;
/*     */         }
/*  89 */         m += i1; k++;
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.util.InverseColorMap
 * JD-Core Version:    0.6.2
 */