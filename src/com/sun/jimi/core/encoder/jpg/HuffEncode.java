/*     */ package com.sun.jimi.core.encoder.jpg;
/*     */ 
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public class HuffEncode
/*     */ {
/*     */   static final short DCTSIZE = 8;
/*     */   int huff_put_buffer;
/*     */   int huff_put_bits;
/*     */   byte[] output_buffer;
/*     */   int bytes_in_buffer;
/*     */ 
/*     */   void encode_one_block(CompressInfo paramCompressInfo, int[] paramArrayOfInt, HuffTbl paramHuffTbl1, HuffTbl paramHuffTbl2)
/*     */   {
/*     */     int j;
/* 145 */     int i = j = paramArrayOfInt[0];
/*     */ 
/* 147 */     if (i < 0) {
/* 148 */       i = -i;
/* 149 */       j--;
/*     */     }
/*     */ 
/* 153 */     int k = 0;
/* 154 */     while (i != 0) {
/* 155 */       k++;
/* 156 */       i >>= 1;
/*     */     }
/*     */ 
/* 160 */     write_bits(paramCompressInfo, paramHuffTbl1.ehufco[k], paramHuffTbl1.ehufsi[k]);
/*     */ 
/* 164 */     if (k != 0) {
/* 165 */       write_bits(paramCompressInfo, (short)j, k);
/*     */     }
/*     */ 
/* 169 */     int n = 0;
/*     */ 
/* 171 */     for (int m = 1; m < 64; m++) {
/* 172 */       if ((i = paramArrayOfInt[m]) == 0) {
/* 173 */         n++;
/*     */       }
/*     */       else {
/* 176 */         while (n > 15) {
/* 177 */           write_bits(paramCompressInfo, paramHuffTbl2.ehufco['ð'], paramHuffTbl2.ehufsi['ð']);
/* 178 */           n -= 16;
/*     */         }
/*     */ 
/* 181 */         j = i;
/* 182 */         if (i < 0) {
/* 183 */           i = -i;
/*     */ 
/* 185 */           j--;
/*     */         }
/*     */ 
/* 189 */         k = 1;
/* 190 */         while ((i >>= 1) != 0) {
/* 191 */           k++;
/*     */         }
/*     */ 
/* 194 */         int i1 = (n << 4) + k;
/* 195 */         write_bits(paramCompressInfo, paramHuffTbl2.ehufco[i1], paramHuffTbl2.ehufsi[i1]);
/*     */ 
/* 199 */         write_bits(paramCompressInfo, (short)j, k);
/*     */ 
/* 201 */         n = 0;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 206 */     if (n > 0)
/* 207 */       write_bits(paramCompressInfo, paramHuffTbl2.ehufco[0], paramHuffTbl2.ehufsi[0]);
/*     */   }
/*     */ 
/*     */   void flush_bits(CompressInfo paramCompressInfo)
/*     */   {
/* 126 */     write_bits(paramCompressInfo, 127, 7);
/* 127 */     this.huff_put_buffer = 0;
/* 128 */     this.huff_put_bits = 0;
/*     */   }
/*     */ 
/*     */   void flush_bytes(CompressInfo paramCompressInfo)
/*     */   {
/*     */     try
/*     */     {
/*  76 */       paramCompressInfo.output_file.flush();
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   void huff_encode(CompressInfo paramCompressInfo, int[][] paramArrayOfInt)
/*     */   {
/* 248 */     for (int i = 0; i < paramCompressInfo.blocks_in_MCU; i = (short)(i + 1)) {
/* 249 */       int j = paramCompressInfo.MCU_membership[i];
/* 250 */       JpegComponentInfo localJpegComponentInfo = paramCompressInfo.cur_comp_info[j];
/*     */ 
/* 252 */       int k = paramArrayOfInt[i][0];
/* 253 */       paramArrayOfInt[i][0] -= paramCompressInfo.last_dc_val[j];
/* 254 */       paramCompressInfo.last_dc_val[j] = k;
/* 255 */       encode_one_block(paramCompressInfo, paramArrayOfInt[i], 
/* 256 */         paramCompressInfo.dc_huff_tbl[localJpegComponentInfo.dc_tbl_no], 
/* 257 */         paramCompressInfo.ac_huff_tbl[localJpegComponentInfo.ac_tbl_no]);
/*     */     }
/*     */   }
/*     */ 
/*     */   void huff_init(CompressInfo paramCompressInfo)
/*     */   {
/* 219 */     this.huff_put_buffer = 0;
/* 220 */     this.huff_put_bits = 0;
/*     */ 
/* 222 */     for (int i = 0; i < paramCompressInfo.comps_in_scan; i = (short)(i + 1)) {
/* 223 */       JpegComponentInfo localJpegComponentInfo = paramCompressInfo.cur_comp_info[i];
/*     */ 
/* 225 */       if ((paramCompressInfo.dc_huff_tbl[localJpegComponentInfo.dc_tbl_no] == null) || 
/* 226 */         (paramCompressInfo.ac_huff_tbl[localJpegComponentInfo.ac_tbl_no] == null)) {
/* 227 */         util.errexit("Use of undefined Huffman table");
/*     */       }
/*     */ 
/* 230 */       init_huff_tbl(paramCompressInfo.dc_huff_tbl[localJpegComponentInfo.dc_tbl_no]);
/* 231 */       init_huff_tbl(paramCompressInfo.ac_huff_tbl[localJpegComponentInfo.ac_tbl_no]);
/*     */ 
/* 233 */       paramCompressInfo.last_dc_val[i] = 0;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void huff_term(CompressInfo paramCompressInfo)
/*     */   {
/* 270 */     flush_bits(paramCompressInfo);
/* 271 */     flush_bytes(paramCompressInfo);
/*     */   }
/*     */ 
/*     */   void init_huff_tbl(HuffTbl paramHuffTbl)
/*     */   {
/*  21 */     byte[] arrayOfByte = new byte[257];
/*  22 */     short[] arrayOfShort = new short[257];
/*     */ 
/*  28 */     int i = 0;
/*  29 */     for (int k = 1; k <= 16; k++) {
/*  30 */       for (int j = 1; j <= paramHuffTbl.bits[k]; j++)
/*  31 */         arrayOfByte[(i++)] = ((byte)k);
/*     */     }
/*  33 */     arrayOfByte[i] = 0;
/*  34 */     int m = i;
/*     */ 
/*  39 */     int i1 = 0;
/*  40 */     int n = arrayOfByte[0];
/*  41 */     i = 0;
/*  42 */     while (arrayOfByte[i] != 0) {
/*  43 */       while (arrayOfByte[i] == n) {
/*  44 */         arrayOfShort[(i++)] = (short) i1;
/*  45 */         i1 = (short)(i1 + 1);
/*     */       }
/*  47 */       i1 = (short)(i1 << 1);
/*  48 */       n++;
/*     */     }
/*     */ 
/*  57 */     for (int j = 0; j < paramHuffTbl.ehufsi.length; j++) {
/*  58 */       paramHuffTbl.ehufsi[j] = 0;
/*     */     }
/*  60 */     for (i = 0; i < m; i++) {
/*  61 */       paramHuffTbl.ehufco[paramHuffTbl.huffval[i]] = arrayOfShort[i];
/*  62 */       paramHuffTbl.ehufsi[paramHuffTbl.huffval[i]] = ((short)arrayOfByte[i]);
/*     */     }
/*     */   }
/*     */ 
/*     */   void write_bits(CompressInfo paramCompressInfo, int paramInt1, int paramInt2)
/*     */   {
/*  92 */     int i = paramInt1;
/*  93 */     int j = this.huff_put_bits;
/*     */ 
/*  96 */     if (paramInt2 == 0) {
/*  97 */       util.errexit("Missing Huffman code table entry");
/*     */     }
/*  99 */     i &= (1 << paramInt2) - 1;
/*     */ 
/* 101 */     j += paramInt2;
/*     */ 
/* 103 */     i <<= 24 - j;
/*     */ 
/* 105 */     i |= this.huff_put_buffer;
/*     */ 
/* 107 */     while (j >= 8) {
/* 108 */       int k = i >> 16 & 0xFF;
/*     */ 
/* 110 */       write_byte(paramCompressInfo, k);
/* 111 */       if (k == 255) {
/* 112 */         write_byte(paramCompressInfo, 0);
/*     */       }
/* 114 */       i <<= 8;
/* 115 */       j -= 8;
/*     */     }
/*     */ 
/* 118 */     this.huff_put_buffer = i;
/* 119 */     this.huff_put_bits = j;
/*     */   }
/*     */ 
/*     */   public void write_byte(CompressInfo paramCompressInfo, int paramInt)
/*     */   {
/*     */     try
/*     */     {
/*  82 */       paramCompressInfo.output_file.writeByte(paramInt);
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.encoder.jpg.HuffEncode
 * JD-Core Version:    0.6.2
 */