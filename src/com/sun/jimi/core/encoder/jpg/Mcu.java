/*     */ package com.sun.jimi.core.encoder.jpg;
/*     */ 
/*     */ public class Mcu
/*     */ {
/*     */   Shared shared;
/*     */   static final short DCTSIZE = 8;
/*     */   static final short CENTERJSAMPLE = 128;
/*     */   static final short MAX_BLOCKS_IN_MCU = 10;
/*  19 */   static final short[] ZZ = { 0, 
/*  20 */     1, 8, 16, 9, 2, 3, 10, 
/*  21 */     17, 24, 32, 25, 18, 11, 4, 5, 
/*  22 */     12, 19, 26, 33, 40, 48, 41, 34, 
/*  23 */     27, 20, 13, 6, 7, 14, 21, 28, 
/*  24 */     35, 42, 49, 56, 57, 50, 43, 36, 
/*  25 */     29, 22, 15, 23, 30, 37, 44, 51, 
/*  26 */     58, 59, 52, 45, 38, 31, 39, 46, 
/*  27 */     53, 60, 61, 54, 47, 55, 62, 63 };
/*     */ 
/*  31 */   int[] block_ = new int[64];
/*     */ 
/*  84 */   int[][] MCU_data_ = new int[10][64];
/*     */ 
/*     */   public Mcu(Shared paramShared)
/*     */   {
/*   9 */     this.shared = paramShared;
/*     */   }
/*     */ 
/*     */   void extract_MCUs(CompressInfo paramCompressInfo, short[][][] paramArrayOfShort, int paramInt)
/*     */   {
/*  92 */     int[][] arrayOfInt = this.MCU_data_;
/*     */ 
/* 100 */     for (int i = 0; i < paramInt; i++)
/*     */     {
/* 102 */       for (int j = 0; j < paramCompressInfo.MCUs_per_row; j++)
/*     */       {
/* 105 */         int k = 0;
/* 106 */         for (int m = 0; m < paramCompressInfo.comps_in_scan; m = (short)(m + 1))
/*     */         {
/* 108 */           short[][] arrayOfShort1 = paramArrayOfShort[m];
/* 109 */           JpegComponentInfo localJpegComponentInfo = paramCompressInfo.cur_comp_info[m];
/* 110 */           short[] arrayOfShort = paramCompressInfo.quant_tbl[localJpegComponentInfo.quant_tbl_no];
/* 111 */           for (int i1 = 0; i1 < localJpegComponentInfo.MCU_height; i1 = (short)(i1 + 1))
/*     */           {
/* 113 */             for (int n = 0; n < localJpegComponentInfo.MCU_width; n = (short)(n + 1))
/*     */             {
/* 115 */               extract_block(arrayOfShort1, 
/* 116 */                 (i * localJpegComponentInfo.MCU_height + i1) * 8, 
/* 117 */                 (j * localJpegComponentInfo.MCU_width + n) * 8, 
/* 118 */                 arrayOfInt[k], arrayOfShort);
/* 119 */               k = (short)(k + 1);
/*     */             }
/*     */           }
/*     */         }
/*     */ 
/* 124 */         this.shared.huffEncode.huff_encode(paramCompressInfo, arrayOfInt);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   void extract_block(short[][] paramArrayOfShort, int paramInt1, int paramInt2, int[] paramArrayOfInt, short[] paramArrayOfShort1)
/*     */   {
/*  41 */     int[] arrayOfInt = this.block_;
/*     */ 
/*  46 */     int k = 0;
/*  47 */     int j = 8;
/*     */     do
/*     */     {
/*  50 */       int m = paramInt2;
/*  51 */       short[] arrayOfShort = paramArrayOfShort[(paramInt1++)];
/*  52 */       arrayOfShort[(m++)] -= 128;
/*  53 */       arrayOfShort[(m++)] -= 128;
/*  54 */       arrayOfShort[(m++)] -= 128;
/*  55 */       arrayOfShort[(m++)] -= 128;
/*  56 */       arrayOfShort[(m++)] -= 128;
/*  57 */       arrayOfShort[(m++)] -= 128;
/*  58 */       arrayOfShort[(m++)] -= 128;
/*  59 */       arrayOfShort[(m++)] -= 128;
/*     */ 
/*  47 */       j--; } while (j >= 0);
/*     */ 
/*  62 */     Fwddct.fwd_dct(arrayOfInt);
/*     */ 
/*  64 */     int n = 64;
/*     */     do {
/*  66 */       int i = arrayOfInt[ZZ[n]];
/*     */ 
/*  68 */       if (i < 0)
/*     */       {
/*  70 */         i = -i;
/*  71 */         i += (paramArrayOfShort1[n] >> 1);
/*  72 */         i /= paramArrayOfShort1[n];
/*  73 */         i = -i;
/*     */       }
/*     */       else
/*     */       {
/*  77 */         i += (paramArrayOfShort1[n] >> 1);
/*  78 */         i /= paramArrayOfShort1[n];
/*     */       }
/*  80 */       paramArrayOfInt[n] = i;
/*     */ 
/*  64 */       n--; } while (n >= 0);
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.encoder.jpg.Mcu
 * JD-Core Version:    0.6.2
 */