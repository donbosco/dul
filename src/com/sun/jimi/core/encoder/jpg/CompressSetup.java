/*     */ package com.sun.jimi.core.encoder.jpg;
/*     */ 
/*     */ public class CompressSetup
/*     */ {
/*     */   public static final short MAX_COMPS_IN_SCAN = 4;
/*     */   public static final short DCTSIZE = 8;
/*     */   public static final short MAX_BLOCKS_IN_MCU = 10;
/*     */   public static final short BITS_IN_SAMPLE = 8;
/*     */   public static final short MAX_SAMP_FACTOR = 4;
/*     */   public static final byte CS_UNKNOWN = 0;
/*     */   public static final byte CS_GRAYSCALE = 1;
/*     */   public static final byte CS_RGB = 2;
/*     */   public static final byte CS_YCbCr = 3;
/*     */ 
/*     */   public static void initial_setup(CompressInfo paramCompressInfo)
/*     */   {
/* 170 */     paramCompressInfo.max_h_samp_factor = 1;
/* 171 */     paramCompressInfo.max_v_samp_factor = 1;
/*     */     JpegComponentInfo localJpegComponentInfo;
/* 172 */     for (int i = 0; i < paramCompressInfo.num_components; i = (short)(i + 1)) {
/* 173 */       localJpegComponentInfo = paramCompressInfo.comp_info[i];
/* 174 */       if ((localJpegComponentInfo.h_samp_factor <= 0) || (localJpegComponentInfo.h_samp_factor > 4) || 
/* 175 */         (localJpegComponentInfo.v_samp_factor <= 0) || (localJpegComponentInfo.v_samp_factor > 4))
/* 176 */         util.errexit("Bogus sampling factors");
/* 177 */       paramCompressInfo.max_h_samp_factor = ((short)Math.max(paramCompressInfo.max_h_samp_factor, 
/* 178 */         localJpegComponentInfo.h_samp_factor));
/* 179 */       paramCompressInfo.max_v_samp_factor = ((short)Math.max(paramCompressInfo.max_v_samp_factor, 
/* 180 */         localJpegComponentInfo.v_samp_factor));
/*     */     }
/*     */ 
/* 185 */     for (int i = 0; i < paramCompressInfo.num_components; i = (short)(i + 1)) {
/* 186 */       localJpegComponentInfo = paramCompressInfo.comp_info[i];
/* 187 */       localJpegComponentInfo.true_comp_width = 
/* 189 */         ((paramCompressInfo.image_width * localJpegComponentInfo.h_samp_factor + 
/* 188 */         paramCompressInfo.max_h_samp_factor - 1) / 
/* 189 */         paramCompressInfo.max_h_samp_factor);
/* 190 */       localJpegComponentInfo.true_comp_height = 
/* 192 */         ((paramCompressInfo.image_height * localJpegComponentInfo.v_samp_factor + 
/* 191 */         paramCompressInfo.max_v_samp_factor - 1) / 
/* 192 */         paramCompressInfo.max_v_samp_factor);
/*     */     }
/*     */   }
/*     */ 
/*     */   static void interleaved_scan_setup(CompressInfo paramCompressInfo)
/*     */   {
/*  29 */     if (paramCompressInfo.comps_in_scan > 4) {
/*  30 */       util.errexit("Too many components for interleaved scan");
/*     */     }
/*  32 */     paramCompressInfo.MCUs_per_row = 
/*  34 */       ((paramCompressInfo.image_width + 
/*  33 */       paramCompressInfo.max_h_samp_factor * 8 - 1) / (
/*  34 */       paramCompressInfo.max_h_samp_factor * 8));
/*     */ 
/*  36 */     paramCompressInfo.MCU_rows_in_scan = 
/*  38 */       ((paramCompressInfo.image_height + 
/*  37 */       paramCompressInfo.max_v_samp_factor * 8 - 1) / (
/*  38 */       paramCompressInfo.max_v_samp_factor * 8));
/*     */ 
/*  40 */     paramCompressInfo.blocks_in_MCU = 0;
/*     */ 
/*  42 */     for (int i = 0; i < paramCompressInfo.comps_in_scan; i = (short)(i + 1)) {
/*  43 */       JpegComponentInfo localJpegComponentInfo = paramCompressInfo.cur_comp_info[i];
/*     */ 
/*  45 */       localJpegComponentInfo.MCU_width = localJpegComponentInfo.h_samp_factor;
/*  46 */       localJpegComponentInfo.MCU_height = localJpegComponentInfo.v_samp_factor;
/*  47 */       localJpegComponentInfo.MCU_blocks = ((short)(localJpegComponentInfo.MCU_width * localJpegComponentInfo.MCU_height));
/*     */ 
/*  49 */       localJpegComponentInfo.downsampled_width = util.roundUp(localJpegComponentInfo.true_comp_width, 
/*  50 */         localJpegComponentInfo.MCU_width * 8);
/*  51 */       localJpegComponentInfo.downsampled_height = util.roundUp(localJpegComponentInfo.true_comp_height, 
/*  52 */         localJpegComponentInfo.MCU_height * 8);
/*     */ 
/*  54 */       int j = localJpegComponentInfo.MCU_blocks;
/*  55 */       if (paramCompressInfo.blocks_in_MCU + j > 10)
/*  56 */         util.errexit("Sampling factors too large for interleaved scan");
/*  57 */       while (j-- > 0)
/*  58 */         paramCompressInfo.MCU_membership[(paramCompressInfo.blocks_in_MCU++)] = (short) i;
/*     */     }
/*     */   }
/*     */ 
/*     */   static void noninterleaved_scan_setup(CompressInfo paramCompressInfo)
/*     */   {
/*  70 */     JpegComponentInfo localJpegComponentInfo = paramCompressInfo.cur_comp_info[0];
/*     */ 
/*  73 */     localJpegComponentInfo.MCU_width = 1;
/*  74 */     localJpegComponentInfo.MCU_height = 1;
/*  75 */     localJpegComponentInfo.MCU_blocks = 1;
/*     */ 
/*  77 */     localJpegComponentInfo.downsampled_width = util.roundUp(localJpegComponentInfo.true_comp_width, 
/*  78 */       8);
/*  79 */     localJpegComponentInfo.downsampled_height = util.roundUp(localJpegComponentInfo.true_comp_height, 
/*  80 */       8);
/*     */ 
/*  82 */     paramCompressInfo.MCUs_per_row = (localJpegComponentInfo.downsampled_width / 8);
/*  83 */     paramCompressInfo.MCU_rows_in_scan = (localJpegComponentInfo.downsampled_height / 8);
/*     */ 
/*  86 */     paramCompressInfo.blocks_in_MCU = 1;
/*  87 */     paramCompressInfo.MCU_membership[0] = 0;
/*     */   }
/*     */ 
/*     */   public static void setCDefaults(CompressInfo paramCompressInfo)
/*     */   {
/* 100 */     paramCompressInfo.comp_info = null;
/*     */ 
/* 102 */     paramCompressInfo.data_precision = 8;
/*     */ 
/* 106 */     paramCompressInfo.jpeg_color_space = 3;
/* 107 */     paramCompressInfo.num_components = 3;
/* 108 */     paramCompressInfo.comps_in_scan = 3;
/* 109 */     paramCompressInfo.comp_info = new JpegComponentInfo[3];
/*     */ 
/* 111 */     paramCompressInfo.comp_info[0] = new JpegComponentInfo();
/* 112 */     paramCompressInfo.comp_info[0].component_index = 0;
/* 113 */     paramCompressInfo.comp_info[0].component_id = 1;
/* 114 */     paramCompressInfo.comp_info[0].h_samp_factor = 2;
/* 115 */     paramCompressInfo.comp_info[0].v_samp_factor = 2;
/* 116 */     paramCompressInfo.comp_info[0].quant_tbl_no = 0;
/* 117 */     paramCompressInfo.comp_info[0].dc_tbl_no = 0;
/* 118 */     paramCompressInfo.comp_info[0].ac_tbl_no = 0;
/*     */ 
/* 120 */     paramCompressInfo.comp_info[1] = new JpegComponentInfo();
/* 121 */     paramCompressInfo.comp_info[1].component_index = 1;
/* 122 */     paramCompressInfo.comp_info[1].component_id = 2;
/* 123 */     paramCompressInfo.comp_info[1].h_samp_factor = 1;
/* 124 */     paramCompressInfo.comp_info[1].v_samp_factor = 1;
/* 125 */     paramCompressInfo.comp_info[1].quant_tbl_no = 1;
/* 126 */     paramCompressInfo.comp_info[1].dc_tbl_no = 1;
/* 127 */     paramCompressInfo.comp_info[1].ac_tbl_no = 1;
/*     */ 
/* 129 */     paramCompressInfo.comp_info[2] = new JpegComponentInfo();
/* 130 */     paramCompressInfo.comp_info[2].component_index = 2;
/* 131 */     paramCompressInfo.comp_info[2].component_id = 3;
/* 132 */     paramCompressInfo.comp_info[2].h_samp_factor = 1;
/* 133 */     paramCompressInfo.comp_info[2].v_samp_factor = 1;
/* 134 */     paramCompressInfo.comp_info[2].quant_tbl_no = 1;
/* 135 */     paramCompressInfo.comp_info[2].dc_tbl_no = 1;
/* 136 */     paramCompressInfo.comp_info[2].ac_tbl_no = 1;
/*     */ 
/* 138 */     paramCompressInfo.cur_comp_info = new JpegComponentInfo[4];
/* 139 */     paramCompressInfo.cur_comp_info[0] = paramCompressInfo.comp_info[0];
/* 140 */     paramCompressInfo.cur_comp_info[1] = paramCompressInfo.comp_info[1];
/* 141 */     paramCompressInfo.cur_comp_info[2] = paramCompressInfo.comp_info[2];
/*     */ 
/* 143 */     paramCompressInfo.interleave = true;
/*     */   }
/*     */ 
/*     */   public static void setMDefaults(CompressInfo paramCompressInfo)
/*     */   {
/* 152 */     paramCompressInfo.jpeg_color_space = 1;
/* 153 */     paramCompressInfo.num_components = 1;
/*     */ 
/* 155 */     paramCompressInfo.comp_info[0].h_samp_factor = 1;
/* 156 */     paramCompressInfo.comp_info[0].v_samp_factor = 1;
/* 157 */     paramCompressInfo.comps_in_scan = 1;
/* 158 */     paramCompressInfo.interleave = false;
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.encoder.jpg.CompressSetup
 * JD-Core Version:    0.6.2
 */