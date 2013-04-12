package com.sun.jimi.core.encoder.jpg;

public final class JpegComponentInfo
{
  short component_id;
  short component_index;
  short h_samp_factor;
  short v_samp_factor;
  short quant_tbl_no;
  short dc_tbl_no;
  short ac_tbl_no;
  int true_comp_width;
  int true_comp_height;
  short MCU_width;
  short MCU_height;
  short MCU_blocks;
  int downsampled_width;
  int downsampled_height;
}

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.encoder.jpg.JpegComponentInfo
 * JD-Core Version:    0.6.2
 */