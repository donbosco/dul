/*     */ package com.sun.jimi.core.encoder.jpg;
/*     */ 
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public class Write
/*     */ {
/*     */   static final short NUM_QUANT_TBLS = 4;
/*     */   static final short DCTSIZE = 8;
/*     */   static final short NUM_ARITH_TBLS = 4;
/*     */   public static final byte SOF0 = -64;
/*     */   public static final byte SOF1 = -63;
/*     */   public static final byte SOF2 = -62;
/*     */   public static final byte SOF3 = -61;
/*     */   public static final byte SOF5 = -59;
/*     */   public static final byte SOF6 = -58;
/*     */   public static final byte SOF7 = -57;
/*     */   public static final byte JPG = -56;
/*     */   public static final byte SOF9 = -55;
/*     */   public static final byte SOF10 = -54;
/*     */   public static final byte SOF11 = -53;
/*     */   public static final byte SOF13 = -51;
/*     */   public static final byte SOF14 = -50;
/*     */   public static final byte SOF15 = -49;
/*     */   public static final byte DHT = -60;
/*     */   public static final byte DAC = -52;
/*     */   public static final byte RST0 = -48;
/*     */   public static final byte RST1 = -47;
/*     */   public static final byte RST2 = -46;
/*     */   public static final byte RST3 = -45;
/*     */   public static final byte RST4 = -44;
/*     */   public static final byte RST5 = -43;
/*     */   public static final byte RST6 = -42;
/*     */   public static final byte RST7 = -41;
/*     */   public static final byte SOI = -40;
/*     */   public static final byte EOI = -39;
/*     */   public static final byte SOS = -38;
/*     */   public static final byte DQT = -37;
/*     */   public static final byte DNL = -36;
/*     */   public static final byte DRI = -35;
/*     */   public static final byte DHP = -34;
/*     */   public static final byte EXP = -33;
/*     */   public static final byte APP0 = -32;
/*     */   public static final byte APP15 = -17;
/*     */   public static final byte JPG0 = -16;
/*     */   public static final byte JPG13 = -3;
/*     */   public static final byte COM = -2;
/*     */   public static final byte TEM = 1;
/*     */   public static final byte ERROR = 0;
/*     */ 
/*     */   static void write_2bytes(CompressInfo paramCompressInfo, int paramInt)
/*     */   {
/*  95 */     write_byte(paramCompressInfo, paramInt >> 8 & 0xFF);
/*  96 */     write_byte(paramCompressInfo, paramInt & 0xFF);
/*     */   }
/*     */ 
/*     */   static void write_byte(CompressInfo paramCompressInfo, int paramInt)
/*     */   {
/*     */     try
/*     */     {
/*  11 */       paramCompressInfo.output_file.writeByte(paramInt);
/*     */     } catch (IOException localIOException) {
/*     */     }
/*     */   }
/*     */ 
/*     */   static void write_bytes(CompressInfo paramCompressInfo, byte[] paramArrayOfByte, int paramInt) {
/*     */     try {
/*  18 */       paramCompressInfo.output_file.write(paramArrayOfByte, 0, paramInt);
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   static void write_dht(CompressInfo paramCompressInfo, int paramInt, boolean paramBoolean)
/*     */   {
/*     */     HuffTbl localHuffTbl;
/* 128 */     if (paramBoolean) {
/* 129 */       localHuffTbl = paramCompressInfo.ac_huff_tbl[paramInt];
/* 130 */       paramInt += 16;
/*     */     } else {
/* 132 */       localHuffTbl = paramCompressInfo.dc_huff_tbl[paramInt];
/*     */     }
/*     */ 
/* 135 */     if (localHuffTbl == null) {
/* 136 */       util.errexit("Huffman table " + paramInt + " was not defined");
/*     */     }
/* 138 */     if (!localHuffTbl.sent_table) {
/* 139 */       write_marker(paramCompressInfo, (byte)-60);
/*     */ 
/* 141 */       int i = 0;
/* 142 */       for (int j = 1; j <= 16; j++) {
/* 143 */         i += localHuffTbl.bits[j];
/*     */       }
/* 145 */       write_2bytes(paramCompressInfo, i + 2 + 1 + 16);
/* 146 */       write_byte(paramCompressInfo, paramInt);
/*     */ 
/* 148 */       for (int j = 1; j <= 16; j++) {
/* 149 */         write_byte(paramCompressInfo, localHuffTbl.bits[j]);
/*     */       }
/* 151 */       for (int j = 0; j < i; j++) {
/* 152 */         write_byte(paramCompressInfo, localHuffTbl.huffval[j]);
/*     */       }
/* 154 */       localHuffTbl.sent_table = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   static void write_dqt(CompressInfo paramCompressInfo, int paramInt)
/*     */   {
/* 107 */     write_marker(paramCompressInfo, (byte)-37);
/*     */ 
/* 110 */     write_2bytes(paramCompressInfo, 67);
/*     */ 
/* 113 */     write_byte(paramCompressInfo, paramInt);
/*     */ 
/* 115 */     for (int i = 0; i < 64; i++)
/* 116 */       write_byte(paramCompressInfo, paramCompressInfo.quant_tbl[paramInt][i]);
/*     */   }
/*     */ 
/*     */   public static void write_file_header(CompressInfo paramCompressInfo)
/*     */   {
/* 214 */     short[] arrayOfShort = new short[4];
/*     */ 
/* 218 */     write_marker(paramCompressInfo, (byte)-40);
/*     */ 
/* 222 */     for (int i = 0; i < 4; i++) {
/* 223 */       arrayOfShort[i] = 0;
/*     */     }
int i;
/* 225 */     for (i = 0; i < paramCompressInfo.num_components; i++) {
/* 226 */       arrayOfShort[paramCompressInfo.comp_info[i].quant_tbl_no] = 1;
/*     */     }
/* 228 */     int j = 0;
/* 229 */     for (i = 0; i < 4; i++) {
/* 230 */       if (arrayOfShort[i] != 0) {
/* 231 */         write_dqt(paramCompressInfo, i);
/*     */       }
/*     */     }
/* 234 */     int k = 1;
/*     */ 
/* 238 */     if (k != 0)
/* 239 */       write_sof(paramCompressInfo, (byte)-64);
/*     */     else
/* 241 */       write_sof(paramCompressInfo, (byte)-63);
/*     */   }
/*     */ 
/*     */   public static void write_file_trailer(CompressInfo paramCompressInfo)
/*     */   {
/* 284 */     write_marker(paramCompressInfo, (byte)-39);
/*     */     try {
/* 286 */       paramCompressInfo.output_file.flush();
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void write_jpeg_data(CompressInfo paramCompressInfo, byte[] paramArrayOfByte, int paramInt)
/*     */   {
/* 273 */     write_bytes(paramCompressInfo, paramArrayOfByte, paramInt);
/*     */   }
/*     */ 
/*     */   static void write_marker(CompressInfo paramCompressInfo, byte paramByte)
/*     */   {
/*  86 */     write_byte(paramCompressInfo, 255);
/*  87 */     write_byte(paramCompressInfo, paramByte);
/*     */   }
/*     */ 
/*     */   public static void write_scan_header(CompressInfo paramCompressInfo)
/*     */   {
/* 257 */     for (int i = 0; i < paramCompressInfo.comps_in_scan; i++) {
/* 258 */       write_dht(paramCompressInfo, paramCompressInfo.cur_comp_info[i].dc_tbl_no, false);
/* 259 */       write_dht(paramCompressInfo, paramCompressInfo.cur_comp_info[i].ac_tbl_no, true);
/*     */     }
/*     */ 
/* 262 */     write_sos(paramCompressInfo);
/*     */   }
/*     */ 
/*     */   static void write_sof(CompressInfo paramCompressInfo, byte paramByte)
/*     */   {
/* 165 */     write_marker(paramCompressInfo, paramByte);
/*     */ 
/* 167 */     write_2bytes(paramCompressInfo, 3 * paramCompressInfo.num_components + 2 + 5 + 1);
/*     */ 
/* 169 */     write_byte(paramCompressInfo, paramCompressInfo.data_precision);
/* 170 */     write_2bytes(paramCompressInfo, paramCompressInfo.image_height);
/* 171 */     write_2bytes(paramCompressInfo, paramCompressInfo.image_width);
/*     */ 
/* 173 */     write_byte(paramCompressInfo, paramCompressInfo.num_components);
/*     */ 
/* 175 */     for (int i = 0; i < paramCompressInfo.num_components; i++) {
/* 176 */       write_byte(paramCompressInfo, paramCompressInfo.comp_info[i].component_id);
/* 177 */       write_byte(paramCompressInfo, (paramCompressInfo.comp_info[i].h_samp_factor << 4) + 
/* 178 */         paramCompressInfo.comp_info[i].v_samp_factor);
/* 179 */       write_byte(paramCompressInfo, paramCompressInfo.comp_info[i].quant_tbl_no);
/*     */     }
/*     */   }
/*     */ 
/*     */   static void write_sos(CompressInfo paramCompressInfo)
/*     */   {
/* 190 */     write_marker(paramCompressInfo, (byte)-38);
/*     */ 
/* 192 */     write_2bytes(paramCompressInfo, 2 * paramCompressInfo.comps_in_scan + 2 + 1 + 3);
/*     */ 
/* 194 */     write_byte(paramCompressInfo, paramCompressInfo.comps_in_scan);
/*     */ 
/* 196 */     for (int i = 0; i < paramCompressInfo.comps_in_scan; i++) {
/* 197 */       write_byte(paramCompressInfo, paramCompressInfo.cur_comp_info[i].component_id);
/* 198 */       write_byte(paramCompressInfo, (paramCompressInfo.cur_comp_info[i].dc_tbl_no << 4) + 
/* 199 */         paramCompressInfo.cur_comp_info[i].ac_tbl_no);
/*     */     }
/*     */ 
/* 202 */     write_byte(paramCompressInfo, 0);
/* 203 */     write_byte(paramCompressInfo, 63);
/* 204 */     write_byte(paramCompressInfo, 0);
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.encoder.jpg.Write
 * JD-Core Version:    0.6.2
 */