/*     */ package com.sun.jimi.core.encoder.jpg;
/*     */ 
/*     */ public class Sample
/*     */ {
/*     */   public static void downsample(CompressInfo paramCompressInfo, int paramInt1, int paramInt2, int paramInt3, short[][] paramArrayOfShort1, short[][] paramArrayOfShort2)
/*     */   {
/*  72 */     int i = paramCompressInfo.max_v_samp_factor / 
/*  73 */       paramCompressInfo.comp_info[paramInt1].v_samp_factor;
/*  74 */     int j = paramCompressInfo.max_h_samp_factor / 
/*  75 */       paramCompressInfo.comp_info[paramInt1].h_samp_factor;
/*     */ 
/*  77 */     if ((i == 1) && (j == 1))
/*  78 */       fullsize_downsample(paramInt2, paramInt3, paramArrayOfShort1, paramArrayOfShort2);
/*  79 */     else if ((j == 2) && (i == 2))
/*  80 */       h2v2_downsample(paramInt2, paramInt3, paramArrayOfShort1, paramArrayOfShort2);
/*  81 */     else if ((j == 2) && (i == 1))
/*  82 */       h2v1_downsample(paramInt2, paramInt3, paramArrayOfShort1, paramArrayOfShort2);
/*     */     else
/*  84 */       util.errexit("Down sample ratio not supported.");
/*     */   }
/*     */ 
/*     */   public static void edge_expand(CompressInfo paramCompressInfo, int paramInt1, int paramInt2, int paramInt3, int paramInt4, short[][][] paramArrayOfShort)
/*     */   {
/*     */     int k;
/*     */     int i;
/*     */     int j;
/*  95 */     if (paramInt1 < paramInt3)
/*     */     {
/* 100 */       int n = paramInt3 - paramInt1;
/*     */ 
/* 102 */       for (int m = 0; m < paramCompressInfo.num_components; m = (short)(m + 1)) {
/* 103 */         for (k = 0; k < paramInt2; k++) {
/* 104 */           i = paramArrayOfShort[m][k][(paramInt1 - 1)];
/* 105 */           for (j = n; j > 0; j--) {
/* 106 */             paramArrayOfShort[m][k][(paramInt3 - j)] = (short) i;
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 113 */     if (paramInt2 < paramInt4)
/*     */     {
/* 117 */       for (k = 0; k < paramCompressInfo.num_components; k = (short)(k + 1))
/* 118 */         for (i = paramInt2; i < paramInt4; i++)
/* 119 */           for (j = 0; j < paramInt3; j++)
/* 120 */             paramArrayOfShort[k][i][j] = paramArrayOfShort[k][(paramInt2 - 1)][j];
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void fullsize_downsample(int paramInt1, int paramInt2, short[][] paramArrayOfShort1, short[][] paramArrayOfShort2)
/*     */   {
/*  61 */     for (int j = 0; j < paramInt2; j++)
/*  62 */       for (int i = 0; i < paramInt1; i++)
/*  63 */         paramArrayOfShort2[j][i] = paramArrayOfShort1[j][i];
/*     */   }
/*     */ 
/*     */   public static void h2v1_downsample(int paramInt1, int paramInt2, short[][] paramArrayOfShort1, short[][] paramArrayOfShort2)
/*     */   {
/*  17 */     for (int i = 0; i < paramInt2; i++) {
/*  18 */       int j = 0; for (int k = 0; j < paramInt1; k += 2) {
/*  19 */         paramArrayOfShort2[i][j] = ((short)
/*  20 */           ((paramArrayOfShort1[i][k] + paramArrayOfShort1[i][(k + 1)] + 1) / 2));
/*     */ 
/*  18 */         j++;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void h2v2_downsample(int paramInt1, int paramInt2, short[][] paramArrayOfShort1, short[][] paramArrayOfShort2)
/*     */   {
/*  38 */     int i = 0;
/*  39 */     for (int j = 0; j < paramInt2; j++) {
/*  40 */       int k = 0; for (int m = 0; k < paramInt1; m += 2) {
/*  41 */         paramArrayOfShort2[j][k] = ((short)
/*  43 */           ((paramArrayOfShort1[i][m] + 
/*  42 */           paramArrayOfShort1[i][(m + 1)] + paramArrayOfShort1[(i + 1)][m] + 
/*  43 */           paramArrayOfShort1[(i + 1)][(m + 1)] + 2) / 4));
/*     */ 
/*  40 */         k++;
/*     */       }
/*     */ 
/*  45 */       i += 2;
/*     */     }
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.encoder.jpg.Sample
 * JD-Core Version:    0.6.2
 */