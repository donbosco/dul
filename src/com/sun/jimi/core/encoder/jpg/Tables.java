/*     */ package com.sun.jimi.core.encoder.jpg;
/*     */ 
/*     */ public final class Tables
/*     */ {
/*  13 */   public static final short[] dc_luminance_bits = { 0, 0, 1, 5, 1, 1, 1, 1, 1, 1 };
/*     */ 
/*  15 */   public static final short[] dc_luminance_val = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 };
/*     */ 
/*  18 */   public static final short[] dc_chrominance_bits = { 0, 0, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1 };
/*     */ 
/*  20 */   public static final short[] dc_chrominance_val = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 };
/*     */ 
/*  23 */   public static final short[] ac_luminance_bits = { 0, 0, 2, 1, 3, 3, 2, 4, 3, 5, 5, 4, 4, 0, 0, 1, 125 };
/*     */ 
/*  25 */   public static final short[] ac_luminance_val = { 1, 2, 3, 0, 4, 17, 5, 18, 
/*  26 */     33, 49, 65, 6, 19, 81, 97, 7, 
/*  27 */     34, 113, 20, 50, 129, 145, 161, 8, 
/*  28 */     35, 66, 177, 193, 21, 82, 209, 240, 
/*  29 */     36, 51, 98, 114, 130, 9, 10, 22, 
/*  30 */     23, 24, 25, 26, 37, 38, 39, 40, 
/*  31 */     41, 42, 52, 53, 54, 55, 56, 57, 
/*  32 */     58, 67, 68, 69, 70, 71, 72, 73, 
/*  33 */     74, 83, 84, 85, 86, 87, 88, 89, 
/*  34 */     90, 99, 100, 101, 102, 103, 104, 105, 
/*  35 */     106, 115, 116, 117, 118, 119, 120, 121, 
/*  36 */     122, 131, 132, 133, 134, 135, 136, 137, 
/*  37 */     138, 146, 147, 148, 149, 150, 151, 152, 
/*  38 */     153, 154, 162, 163, 164, 165, 166, 167, 
/*  39 */     168, 169, 170, 178, 179, 180, 181, 182, 
/*  40 */     183, 184, 185, 186, 194, 195, 196, 197, 
/*  41 */     198, 199, 200, 201, 202, 210, 211, 212, 
/*  42 */     213, 214, 215, 216, 217, 218, 225, 226, 
/*  43 */     227, 228, 229, 230, 231, 232, 233, 234, 
/*  44 */     241, 242, 243, 244, 245, 246, 247, 248, 
/*  45 */     249, 250 };
/*     */ 
/*  48 */   public static final short[] ac_chrominance_bits = { 0, 0, 2, 1, 2, 4, 4, 3, 4, 7, 5, 4, 4, 0, 1, 2, 119 };
/*     */ 
/*  50 */   public static final short[] ac_chrominance_val = { 0, 1, 2, 3, 17, 4, 5, 33, 
/*  51 */     49, 6, 18, 65, 81, 7, 97, 113, 
/*  52 */     19, 34, 50, 129, 8, 20, 66, 145, 
/*  53 */     161, 177, 193, 9, 35, 51, 82, 240, 
/*  54 */     21, 98, 114, 209, 10, 22, 36, 52, 
/*  55 */     225, 37, 241, 23, 24, 25, 26, 38, 
/*  56 */     39, 40, 41, 42, 53, 54, 55, 56, 
/*  57 */     57, 58, 67, 68, 69, 70, 71, 72, 
/*  58 */     73, 74, 83, 84, 85, 86, 87, 88, 
/*  59 */     89, 90, 99, 100, 101, 102, 103, 104, 
/*  60 */     105, 106, 115, 116, 117, 118, 119, 120, 
/*  61 */     121, 122, 130, 131, 132, 133, 134, 135, 
/*  62 */     136, 137, 138, 146, 147, 148, 149, 150, 
/*  63 */     151, 152, 153, 154, 162, 163, 164, 165, 
/*  64 */     166, 167, 168, 169, 170, 178, 179, 180, 
/*  65 */     181, 182, 183, 184, 185, 186, 194, 195, 
/*  66 */     196, 197, 198, 199, 200, 201, 202, 210, 
/*  67 */     211, 212, 213, 214, 215, 216, 217, 218, 
/*  68 */     226, 227, 228, 229, 230, 231, 232, 233, 
/*  69 */     234, 242, 243, 244, 245, 246, 247, 248, 
/*  70 */     249, 250 };
/*     */ 
/*  72 */   public static final short[] std_luminance_quant_tbl = { 
/*  73 */     16, 11, 12, 14, 12, 10, 16, 14, 
/*  74 */     13, 14, 18, 17, 16, 19, 24, 40, 
/*  75 */     26, 24, 22, 22, 24, 49, 35, 37, 
/*  76 */     29, 40, 58, 51, 61, 60, 57, 51, 
/*  77 */     56, 55, 64, 72, 92, 78, 64, 68, 
/*  78 */     87, 69, 55, 56, 80, 109, 81, 87, 
/*  79 */     95, 98, 103, 104, 103, 62, 77, 113, 
/*  80 */     121, 112, 100, 120, 92, 101, 103, 99 };
/*     */ 
/*  83 */   public static final short[] std_chrominance_quant_tbl = { 
/*  84 */     17, 18, 18, 24, 21, 24, 47, 26, 
/*  85 */     26, 47, 99, 66, 56, 66, 99, 99, 
/*  86 */     99, 99, 99, 99, 99, 99, 99, 99, 
/*  87 */     99, 99, 99, 99, 99, 99, 99, 99, 
/*  88 */     99, 99, 99, 99, 99, 99, 99, 99, 
/*  89 */     99, 99, 99, 99, 99, 99, 99, 99, 
/*  90 */     99, 99, 99, 99, 99, 99, 99, 99, 
/*  91 */     99, 99, 99, 99, 99, 99, 99, 99 };
/*     */   int scaleFactor_;
/*     */   boolean forceBaseLine_;
/*     */ 
/*     */   Tables()
/*     */   {
/* 100 */     this(75, true);
/*     */   }
/*     */ 
/*     */   Tables(int paramInt)
/*     */   {
/* 105 */     this(paramInt, true);
/*     */   }
/*     */ 
/*     */   Tables(int paramInt, boolean paramBoolean)
/*     */   {
/* 115 */     if ((paramInt < 0) || (paramInt > 100)) {
/* 116 */       throw new IllegalArgumentException("Invalid jpg quality setting");
/*     */     }
/* 118 */     if (!paramBoolean) {
/* 119 */       throw new IllegalArgumentException("Jpeg quantisation only supports Baseline currently");
/*     */     }
/* 121 */     this.scaleFactor_ = getJpegQualityScaleFactor(paramInt);
/* 122 */     this.forceBaseLine_ = paramBoolean;
/*     */   }
/*     */ 
/*     */   void buildQuantTable(int paramInt, boolean paramBoolean, short[] paramArrayOfShort1, short[] paramArrayOfShort2)
/*     */   {
/* 153 */     int k = paramInt;
/*     */ 
/* 155 */     int i = paramArrayOfShort1.length;
/*     */     do {
/* 157 */       int j = (paramArrayOfShort1[i] * k + 50) / 100;
/* 158 */       if (j <= 0)
/* 159 */         j = 1;
/* 160 */       if (j > 32767)
/* 161 */         j = 32767;
/* 162 */       if ((paramBoolean) && (j > 255))
/* 163 */         j = 255;
/* 164 */       paramArrayOfShort2[i] = ((short)j);
/*     */ 
/* 155 */       i--; } while (i >= 0);
/*     */   }
/*     */ 
/*     */   public void getChrominanceQuantTable(short[] paramArrayOfShort)
/*     */   {
/* 140 */     buildQuantTable(this.scaleFactor_, this.forceBaseLine_, std_chrominance_quant_tbl, paramArrayOfShort);
/*     */   }
/*     */ 
/*     */   int getJpegQualityScaleFactor(int paramInt)
/*     */   {
/* 176 */     if (paramInt <= 1)
/* 177 */       paramInt = 1;
/* 178 */     if (paramInt > 100) {
/* 179 */       paramInt = 100;
/*     */     }
/*     */ 
/* 186 */     if (paramInt < 50)
/* 187 */       paramInt = 5000 / paramInt;
/*     */     else
/* 189 */       paramInt = 200 - paramInt * 2;
/* 190 */     return paramInt;
/*     */   }
/*     */ 
/*     */   public void getLuminanceQuantTable(short[] paramArrayOfShort)
/*     */   {
/* 131 */     buildQuantTable(this.scaleFactor_, this.forceBaseLine_, std_luminance_quant_tbl, paramArrayOfShort);
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.encoder.jpg.Tables
 * JD-Core Version:    0.6.2
 */