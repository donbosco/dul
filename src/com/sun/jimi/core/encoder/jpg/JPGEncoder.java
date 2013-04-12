/*     */ package com.sun.jimi.core.encoder.jpg;
/*     */ 
/*     */ import com.sun.jimi.core.InvalidOptionException;
/*     */ import com.sun.jimi.core.JimiException;
/*     */ import com.sun.jimi.core.OptionsObject;
/*     */ import com.sun.jimi.core.compat.AdaptiveRasterImage;
/*     */ import com.sun.jimi.core.compat.JimiEncoderBase;
/*     */ import com.sun.jimi.core.options.JPGOptions;
/*     */ import com.sun.jimi.util.ArrayEnumeration;
/*     */ import com.sun.jimi.util.IntegerRange;
/*     */ import java.awt.image.ColorModel;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.util.Enumeration;
/*     */ 
/*     */ public class JPGEncoder extends JimiEncoderBase
/*     */   implements OptionsObject
/*     */ {
/*     */   private int state;
/*     */   CompressInfo cinfo;
/*     */   OutputStream out;
/*     */   short[][][] sample_buf;
/*  45 */   short[][][] sampled_img = null;
/*     */   int fullwidth;
/*     */   int rows_in_mcu;
/*     */   int rows_to_get;
/*     */   int cur_pixel_row;
/*     */   int ci;
/*     */   public static final short CS_UNKNOWN = 0;
/*     */   public static final short CS_GRAYSCALE = 1;
/*     */   public static final short CS_RGB = 2;
/*     */   public static final short CS_YCbCr = 3;
/*     */   static final short DCTSIZE = 8;
/*     */   static final int DEFAULT_QUALITY = 75;
/*  57 */   int quality_ = 75;
/*     */ 
/*  60 */   public Shared shared = new Shared();
/*     */   static final String QUALITY_OPTION_NAME = "quality";
/* 230 */   static final IntegerRange POSSIBLE_QUALITY_VALUES = new IntegerRange(0, 100);
/*     */ 
/* 232 */   static final String[] PROPERTY_NAMES = { "quality" };
/*     */ 
/*     */   public void clearProperties()
/*     */   {
/* 294 */     this.quality_ = 75;
/*     */   }
/*     */ 
/*     */   public boolean driveEncoder()
/*     */     throws JimiException
/*     */   {
/* 162 */     initCInfo();
/*     */ 
/* 165 */     Write.write_file_header(this.cinfo);
/* 166 */     Write.write_scan_header(this.cinfo);
/*     */ 
/* 168 */     for (this.cur_pixel_row = 0; this.cur_pixel_row < this.cinfo.image_height; 
/* 169 */       this.cur_pixel_row += this.rows_in_mcu)
/*     */     {
/* 171 */       this.rows_to_get = Math.min(this.rows_in_mcu, this.cinfo.image_height - this.cur_pixel_row);
/*     */ 
/* 174 */       if (this.cinfo.num_components == 1)
/*     */       {
/* 176 */         this.shared.convertColor.get_grayscale_rows(this.cinfo, this.rows_to_get, this.sample_buf);
/*     */       }
/* 178 */       else if (this.cinfo.num_components == 3)
/*     */       {
/* 180 */         this.shared.convertColor.get_rgb_ycc_rows(this.cinfo, this.rows_to_get, this.sample_buf);
/*     */       }
/*     */ 
/* 184 */       Sample.edge_expand(this.cinfo, this.cinfo.image_width, this.rows_to_get, 
/* 185 */         this.fullwidth, this.rows_in_mcu, this.sample_buf);
/*     */ 
/* 188 */       for (this.ci = 0; this.ci < this.cinfo.num_components; this.ci += 1)
/*     */       {
/* 190 */         Sample.downsample(this.cinfo, this.ci, this.cinfo.comp_info[this.ci].true_comp_width, 
/* 191 */           this.cinfo.comp_info[this.ci].v_samp_factor * 8, 
/* 192 */           this.sample_buf[this.ci], this.sampled_img[this.ci]);
/*     */       }
/* 194 */       this.shared.mcu.extract_MCUs(this.cinfo, this.sampled_img, 1);
/*     */     }
/*     */ 
/* 198 */     this.shared.huffEncode.huff_term(this.cinfo);
/*     */ 
/* 201 */     Write.write_file_trailer(this.cinfo);
/*     */ 
/* 203 */     this.state |= 2;
/* 204 */     return false;
/*     */   }
/*     */ 
/*     */   public void freeEncoder() throws JimiException
/*     */   {
/* 209 */     this.sample_buf = null;
/* 210 */     this.sampled_img = null;
/* 211 */     this.cinfo.ji = null;
/* 212 */     this.cinfo = null;
/* 213 */     super.freeEncoder();
/*     */   }
/*     */ 
/*     */   public OptionsObject getOptionsObject()
/*     */   {
/* 226 */     return this;
/*     */   }
/*     */ 
/*     */   public Object getPossibleValuesForProperty(String paramString)
/*     */     throws InvalidOptionException
/*     */   {
/* 278 */     if (paramString.equalsIgnoreCase("quality")) {
/* 279 */       return POSSIBLE_QUALITY_VALUES;
/*     */     }
/* 281 */     throw new InvalidOptionException("No such option");
/*     */   }
/*     */ 
/*     */   public Object getProperty(String paramString)
/*     */   {
/* 241 */     if (paramString.equalsIgnoreCase("quality"))
/* 242 */       return new Integer(this.quality_);
/* 243 */     return null;
/*     */   }
/*     */ 
/*     */   public String getPropertyDescription(String paramString)
/*     */     throws InvalidOptionException
/*     */   {
/* 286 */     if (paramString.equalsIgnoreCase("quality")) {
/* 287 */       return "A lower quality image is smaller, but has more information loss";
/*     */     }
/* 289 */     throw new InvalidOptionException("No such option");
/*     */   }
/*     */ 
/*     */   public Enumeration getPropertyNames()
/*     */   {
/* 236 */     return new ArrayEnumeration(PROPERTY_NAMES);
/*     */   }
/*     */ 
/*     */   public int getState()
/*     */   {
/* 218 */     return this.state;
/*     */   }
/*     */ 
/*     */   void initCInfo()
/*     */     throws JimiException
/*     */   {
/* 116 */     this.cinfo = new CompressInfo(this.quality_);
/* 117 */     this.cinfo.output_file = new DataOutputStream(new BufferedOutputStream(this.out));
/*     */ 
/* 121 */     this.cinfo.ji = getJimiImage();
/*     */ 
/* 123 */     input_init(this.cinfo);
/*     */ 
/* 125 */     CompressSetup.setCDefaults(this.cinfo);
/*     */ 
/* 128 */     if (this.cinfo.in_color_space == 1) {
/* 129 */       CompressSetup.setMDefaults(this.cinfo);
/*     */     }
/*     */ 
/* 132 */     CompressSetup.initial_setup(this.cinfo);
/*     */ 
/* 136 */     if (this.cinfo.interleave)
/* 137 */       CompressSetup.interleaved_scan_setup(this.cinfo);
/*     */     else {
/* 139 */       CompressSetup.noninterleaved_scan_setup(this.cinfo);
/*     */     }
/*     */ 
/* 142 */     this.fullwidth = util.roundUp(this.cinfo.image_width, this.cinfo.max_h_samp_factor * 8);
/*     */ 
/* 145 */     this.rows_in_mcu = (this.cinfo.max_v_samp_factor * 8);
/*     */ 
/* 148 */     this.sample_buf = new short[this.cinfo.num_components][this.rows_in_mcu][this.fullwidth];
/*     */ 
/* 151 */     this.sampled_img = new short[this.cinfo.num_components][this.rows_in_mcu][this.fullwidth];
/*     */ 
/* 154 */     this.shared.convertColor.rgb_ycc_init(this.cinfo);
/*     */ 
/* 157 */     this.shared.huffEncode.huff_init(this.cinfo);
/*     */   }
/*     */ 
/*     */   public void initSpecificEncoder(OutputStream paramOutputStream, AdaptiveRasterImage paramAdaptiveRasterImage)
/*     */     throws JimiException
/*     */   {
/* 106 */     this.out = paramOutputStream;
/* 107 */     this.state = 0;
/*     */ 
/* 109 */     if ((paramAdaptiveRasterImage.getOptions() instanceof JPGOptions))
/* 110 */       this.quality_ = ((JPGOptions)paramAdaptiveRasterImage.getOptions()).getQuality();
/*     */   }
/*     */ 
/*     */   void input_init(CompressInfo paramCompressInfo)
/*     */   {
/*  65 */     ColorModel localColorModel = paramCompressInfo.ji.getColorModel();
/*     */ 
/*  67 */     paramCompressInfo.in_color_space = -1;
/*     */ 
/*  85 */     if (paramCompressInfo.in_color_space == -1)
/*     */     {
/*  88 */       paramCompressInfo.input_components = 3;
/*  89 */       paramCompressInfo.in_color_space = 2;
/*  90 */       paramCompressInfo.ji.setRGBDefault(true);
/*     */     }
/*     */ 
/*  93 */     paramCompressInfo.image_width = paramCompressInfo.ji.getWidth();
/*  94 */     paramCompressInfo.image_height = paramCompressInfo.ji.getHeight();
/*     */   }
/*     */ 
/*     */   public void setProperty(String paramString, Object paramObject)
/*     */     throws InvalidOptionException
/*     */   {
/* 248 */     if (paramString.equalsIgnoreCase("quality"))
/*     */     {
/*     */       int i;
/*     */       try
/*     */       {
/* 253 */         i = ((Integer)paramObject).intValue();
/*     */       }
/*     */       catch (ClassCastException localClassCastException)
/*     */       {
/* 257 */         throw new InvalidOptionException("Value must be a java.lang.Integer");
/*     */       }
/*     */ 
/* 260 */       if (!POSSIBLE_QUALITY_VALUES.isInRange(i))
/*     */       {
/* 262 */         throw new InvalidOptionException("Value must be between " + 
/* 263 */           POSSIBLE_QUALITY_VALUES.getLeastValue() + " and " + 
/* 264 */           POSSIBLE_QUALITY_VALUES.getGreatestValue());
/*     */       }
/*     */ 
/* 267 */       this.quality_ = i;
/*     */     }
/*     */     else
/*     */     {
/* 272 */       throw new InvalidOptionException("No such option");
/*     */     }
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.encoder.jpg.JPGEncoder
 * JD-Core Version:    0.6.2
 */