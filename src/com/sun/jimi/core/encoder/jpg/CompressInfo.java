/*    */ package com.sun.jimi.core.encoder.jpg;
/*    */ 
/*    */ import com.sun.jimi.core.compat.AdaptiveRasterImage;
/*    */ import java.io.DataOutputStream;
/*    */ 
/*    */ public final class CompressInfo
/*    */ {
/*  9 */   final short NUM_QUANT_TBLS = 4;
/* 10 */   final short NUM_HUFF_TBLS = 4;
/* 11 */   final short NUM_ARITH_TBLS = 16;
/* 12 */   final short MAX_COMPS_IN_SCAN = 4;
/* 13 */   final short MAX_SAMP_FACTOR = 4;
/* 14 */   final short MAX_BLOCKS_IN_MCU = 10;
/* 15 */   final short DCTSIZE = 8;
/*    */   public AdaptiveRasterImage ji;
/*    */   public DataOutputStream output_file;
/*    */   public int image_width;
/*    */   public int image_height;
/*    */   public short input_components;
/* 25 */   public short data_precision = 8;
/*    */   public short in_color_space;
/*    */   public short jpeg_color_space;
/*    */   public short num_components;
/*    */   public JpegComponentInfo[] comp_info;
/*    */   public short[][] quant_tbl;
/*    */   public HuffTbl[] dc_huff_tbl;
/*    */   public HuffTbl[] ac_huff_tbl;
/*    */   public boolean interleave;
/*    */   public short max_h_samp_factor;
/*    */   public short max_v_samp_factor;
/*    */   public short comps_in_scan;
/* 52 */   public JpegComponentInfo[] cur_comp_info = new JpegComponentInfo[4];
/*    */   public int MCUs_per_row;
/*    */   public int MCU_rows_in_scan;
/*    */   public short blocks_in_MCU;
/* 59 */   public short[] MCU_membership = new short[10];
/*    */ 
/* 64 */   public int[] last_dc_val = new int[4];
/*    */ 
/*    */   public CompressInfo(int paramInt)
/*    */   {
/* 72 */     Tables localTables = new Tables(paramInt);
/*    */ 
/* 76 */     this.quant_tbl = new short[4][64];
/* 77 */     localTables.getLuminanceQuantTable(this.quant_tbl[0]);
/*    */ 
/* 79 */     localTables.getChrominanceQuantTable(this.quant_tbl[1]);
/*    */ 
/* 81 */     this.quant_tbl[2] = null;
/* 82 */     this.quant_tbl[3] = null;
/*    */ 
/* 86 */     this.dc_huff_tbl = new HuffTbl[4];
/* 87 */     this.dc_huff_tbl[0] = new HuffTbl(Tables.dc_luminance_bits, Tables.dc_luminance_val);
/* 88 */     this.dc_huff_tbl[1] = new HuffTbl(Tables.dc_chrominance_bits, Tables.dc_chrominance_val);
/* 89 */     this.dc_huff_tbl[2] = null;
/* 90 */     this.dc_huff_tbl[3] = null;
/*    */ 
/* 92 */     this.ac_huff_tbl = new HuffTbl[4];
/* 93 */     this.ac_huff_tbl[0] = new HuffTbl(Tables.ac_luminance_bits, Tables.ac_luminance_val);
/* 94 */     this.ac_huff_tbl[1] = new HuffTbl(Tables.ac_chrominance_bits, Tables.ac_chrominance_val);
/* 95 */     this.ac_huff_tbl[2] = null;
/* 96 */     this.ac_huff_tbl[3] = null;
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.encoder.jpg.CompressInfo
 * JD-Core Version:    0.6.2
 */