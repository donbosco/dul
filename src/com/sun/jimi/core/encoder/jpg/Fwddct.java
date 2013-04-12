/*    */ package com.sun.jimi.core.encoder.jpg;
/*    */ 
/*    */ public class Fwddct
/*    */ {
/*    */   static final int DCTSIZE = 8;
/*    */   static final int CONST_BITS = 13;
/*    */   static final short PASS1_BITS = 2;
/*    */   static final int CONST_SCALE = 8192;
/*    */   static final int FIX_0_298631336 = 2446;
/*    */   static final int FIX_0_390180644 = 3196;
/*    */   static final int FIX_0_541196100 = 4433;
/*    */   static final int FIX_0_765366865 = 6270;
/*    */   static final int FIX_0_899976223 = 7373;
/*    */   static final int FIX_1_175875602 = 9633;
/*    */   static final int FIX_1_501321110 = 12299;
/*    */   static final int FIX_1_847759065 = 15137;
/*    */   static final int FIX_1_961570560 = 16069;
/*    */   static final int FIX_2_053119869 = 16819;
/*    */   static final int FIX_2_562915447 = 20995;
/*    */   static final int FIX_3_072711026 = 25172;
/*    */ 
/*    */   public static int deScale(int paramInt1, int paramInt2)
/*    */   {
/* 38 */     return paramInt1 + (1 << paramInt2 - 1) >> paramInt2; } 
/* 57 */   public static void fwd_dct(int[] paramArrayOfInt) { int i14 = 0;
/* 58 */     int i13 = 8;
/*    */     int i;
/*    */     int i3;
/*    */     int j;
/*    */     int i2;
/*    */     int k;
/*    */     int i1;
/*    */     int m;
/*    */     int n;
/*    */     int i4;
/*    */     int i7;
/*    */     int i5;
/*    */     int i6;
/*    */     int i8;
/*    */     int i9;
/*    */     int i10;
/*    */     int i11;
/*    */     int i12;
/*    */     do { i = paramArrayOfInt[i14] + paramArrayOfInt[(i14 + 7)];
/* 61 */       i3 = paramArrayOfInt[i14] - paramArrayOfInt[(i14 + 7)];
/* 62 */       j = paramArrayOfInt[(i14 + 1)] + paramArrayOfInt[(i14 + 6)];
/* 63 */       i2 = paramArrayOfInt[(i14 + 1)] - paramArrayOfInt[(i14 + 6)];
/* 64 */       k = paramArrayOfInt[(i14 + 2)] + paramArrayOfInt[(i14 + 5)];
/* 65 */       i1 = paramArrayOfInt[(i14 + 2)] - paramArrayOfInt[(i14 + 5)];
/* 66 */       m = paramArrayOfInt[(i14 + 3)] + paramArrayOfInt[(i14 + 4)];
/* 67 */       n = paramArrayOfInt[(i14 + 3)] - paramArrayOfInt[(i14 + 4)];
/*    */ 
/* 71 */       i4 = i + m;
/* 72 */       i7 = i - m;
/* 73 */       i5 = j + k;
/* 74 */       i6 = j - k;
/*    */ 
/* 76 */       paramArrayOfInt[i14] = (i4 + i5 << 2);
/* 77 */       paramArrayOfInt[(i14 + 4)] = (i4 - i5 << 2);
/*    */ 
/* 79 */       i8 = (i6 + i7) * 4433;
/* 80 */       paramArrayOfInt[(i14 + 2)] = deScale(i8 + i7 * 6270, 
/* 81 */         11);
/* 82 */       paramArrayOfInt[(i14 + 6)] = deScale(i8 + i6 * -1 * 15137, 
/* 83 */         11);
/*    */ 
/* 88 */       i8 = n + i3;
/* 89 */       i9 = i1 + i2;
/* 90 */       i10 = n + i2;
/* 91 */       i11 = i1 + i3;
/* 92 */       i12 = (i10 + i11) * 9633;
/*    */ 
/* 94 */       n *= 2446;
/* 95 */       i1 *= 16819;
/* 96 */       i2 *= 25172;
/* 97 */       i3 *= 12299;
/* 98 */       i8 = i8 * -1 * 7373;
/* 99 */       i9 = i9 * -1 * 20995;
/* 100 */       i10 = i10 * -1 * 16069;
/* 101 */       i11 = i11 * -1 * 3196;
/*    */ 
/* 103 */       i10 += i12;
/* 104 */       i11 += i12;
/*    */ 
/* 106 */       paramArrayOfInt[(i14 + 7)] = deScale(n + i8 + i10, 11);
/* 107 */       paramArrayOfInt[(i14 + 5)] = deScale(i1 + i9 + i11, 11);
/* 108 */       paramArrayOfInt[(i14 + 3)] = deScale(i2 + i9 + i10, 11);
/* 109 */       paramArrayOfInt[(i14 + 1)] = deScale(i3 + i8 + i11, 11);
/*    */ 
/* 111 */       i14 += 8;
/*    */ 
/* 58 */       i13--; } while (i13 >= 0);
/*    */ 
/* 117 */     int i15 = 0;
/* 118 */     i13 = 8;
/*    */     do {
/* 120 */       i = paramArrayOfInt[i15] + paramArrayOfInt[(56 + i15)];
/* 121 */       i3 = paramArrayOfInt[i15] - paramArrayOfInt[(56 + i15)];
/* 122 */       j = paramArrayOfInt[(8 + i15)] + paramArrayOfInt[(48 + i15)];
/* 123 */       i2 = paramArrayOfInt[(8 + i15)] - paramArrayOfInt[(48 + i15)];
/* 124 */       k = paramArrayOfInt[(16 + i15)] + paramArrayOfInt[(40 + i15)];
/* 125 */       i1 = paramArrayOfInt[(16 + i15)] - paramArrayOfInt[(40 + i15)];
/* 126 */       m = paramArrayOfInt[(24 + i15)] + paramArrayOfInt[(32 + i15)];
/* 127 */       n = paramArrayOfInt[(24 + i15)] - paramArrayOfInt[(32 + i15)];
/*    */ 
/* 130 */       i4 = i + m;
/* 131 */       i7 = i - m;
/* 132 */       i5 = j + k;
/* 133 */       i6 = j - k;
/*    */ 
/* 135 */       paramArrayOfInt[i15] = deScale(i4 + i5, 5);
/* 136 */       paramArrayOfInt[(32 + i15)] = deScale(i4 - i5, 5);
/*    */ 
/* 138 */       i8 = (i6 + i7) * 4433;
/* 139 */       paramArrayOfInt[(16 + i15)] = deScale(i8 + i7 * 6270, 
/* 140 */         18);
/* 141 */       paramArrayOfInt[(48 + i15)] = deScale(i8 + i6 * -1 * 15137, 
/* 142 */         18);
/*    */ 
/* 145 */       i8 = n + i3;
/* 146 */       i9 = i1 + i2;
/* 147 */       i10 = n + i2;
/* 148 */       i11 = i1 + i3;
/* 149 */       i12 = (i10 + i11) * 9633;
/*    */ 
/* 151 */       n *= 2446;
/* 152 */       i1 *= 16819;
/* 153 */       i2 *= 25172;
/* 154 */       i3 *= 12299;
/* 155 */       i8 = i8 * -1 * 7373;
/* 156 */       i9 = i9 * -1 * 20995;
/* 157 */       i10 = i10 * -1 * 16069;
/* 158 */       i11 = i11 * -1 * 3196;
/*    */ 
/* 160 */       i10 += i12;
/* 161 */       i11 += i12;
/*    */ 
/* 163 */       paramArrayOfInt[(56 + i15)] = deScale(n + i8 + i10, 18);
/* 164 */       paramArrayOfInt[(40 + i15)] = deScale(i1 + i9 + i11, 18);
/* 165 */       paramArrayOfInt[(24 + i15)] = deScale(i2 + i9 + i10, 18);
/* 166 */       paramArrayOfInt[(8 + i15)] = deScale(i3 + i8 + i11, 18);
/*    */ 
/* 168 */       i15++;
/*    */ 
/* 118 */       i13--; } while (i13 >= 0);
/*    */   }
/*    */ 
/*    */   public static int scaleToInt(float paramFloat)
/*    */   {
/* 15 */     return (int)(paramFloat * 8192.0F + 0.5D);
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.encoder.jpg.Fwddct
 * JD-Core Version:    0.6.2
 */