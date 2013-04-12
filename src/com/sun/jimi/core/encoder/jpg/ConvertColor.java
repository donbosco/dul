/*     */ package com.sun.jimi.core.encoder.jpg;
/*     */ 
/*     */ import com.sun.jimi.core.JimiException;
/*     */ import com.sun.jimi.core.compat.AdaptiveRasterImage;
/*     */ 
/*     */ public class ConvertColor
/*     */ {
/*     */   protected Shared shared;
/*     */   short[][] pixel_row;
/*     */   int[] jiIntBuf_;
/*     */   byte[] jiBufByt;
/*     */   static final short CS_UNKNOWN = 0;
/*     */   static final short CS_GRAYSCALE = 1;
/*     */   static final short CS_RGB = 2;
/*     */   static final short CS_YCbCr = 3;
/*     */   static final short SCALEBITS = 16;
/*     */   static final int ONE_HALF = 32768;
/*     */   static final short MAXJSAMPLE = 256;
/*     */   int[] rgb_ycc_tab;
/*     */   static final int R_Y_OFF = 0;
/*     */   static final int G_Y_OFF = 256;
/*     */   static final int B_Y_OFF = 512;
/*     */   static final int R_CB_OFF = 768;
/*     */   static final int G_CB_OFF = 1024;
/*     */   static final int B_CB_OFF = 1280;
/*     */   static final int R_CR_OFF = 1280;
/*     */   static final int G_CR_OFF = 1536;
/*     */   static final int B_CR_OFF = 1792;
/*     */   static final int TABLE_SIZE = 2048;
/*     */   int row_idx_;
/*     */ 
/*     */   public ConvertColor(Shared paramShared)
/*     */   {
/*  21 */     this.shared = paramShared;
/*     */   }
/*     */ 
/*     */   void Patching_get_gray_row(CompressInfo paramCompressInfo, short[] paramArrayOfShort)
/*     */     throws JimiException
/*     */   {
/* 132 */     if (this.jiBufByt == null) {
/* 133 */       this.jiBufByt = new byte[paramCompressInfo.image_width];
/*     */     }
/* 135 */     paramCompressInfo.ji.getChannel(0, this.row_idx_, this.jiBufByt, 0);
/* 136 */     this.row_idx_ += 1;
/*     */ 
/* 138 */     for (int i = 0; i < paramCompressInfo.image_width; i++)
/*     */     {
/* 140 */       paramArrayOfShort[i] = ((short)this.jiBufByt[i]);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void get_grayscale_rows(CompressInfo paramCompressInfo, int paramInt, short[][][] paramArrayOfShort)
/*     */     throws JimiException
/*     */   {
/* 214 */     for (int i = 0; i < paramInt; i++)
/*     */     {
/* 217 */       Patching_get_gray_row(paramCompressInfo, this.pixel_row[0]);
/*     */ 
/* 220 */       for (int j = 0; j < paramCompressInfo.image_width; j++)
/* 221 */         paramArrayOfShort[0][i][j] = this.pixel_row[0][j];
/*     */     }
/*     */   }
/*     */ 
/*     */   public void get_rgb_ycc_rows(CompressInfo paramCompressInfo, int paramInt, short[][][] paramArrayOfShort)
/*     */     throws JimiException
/*     */   {
/* 151 */     AdaptiveRasterImage localAdaptiveRasterImage = paramCompressInfo.ji;
/* 152 */     int i2 = this.row_idx_ + paramInt;
/* 153 */     int i3 = paramCompressInfo.image_width;
/*     */ 
/* 155 */     int[] arrayOfInt1 = this.jiIntBuf_;
/*     */ 
/* 159 */     int[] arrayOfInt2 = this.rgb_ycc_tab;
/*     */ 
/* 161 */     int i1 = this.row_idx_;
/* 162 */     for (int n = 0; n < paramInt; n++)
/*     */     {
/* 164 */       localAdaptiveRasterImage.getChannel(i1, arrayOfInt1, 0);
/*     */ 
/* 166 */       for (int m = 0; m < i3; m++)
/*     */       {
/* 168 */         int i4 = arrayOfInt1[m];
/* 169 */         int i = i4 >> 16 & 0xFF;
/* 170 */         int j = i4 >> 8 & 0xFF;
/* 171 */         int k = i4 & 0xFF;
/*     */ 
/* 179 */         paramArrayOfShort[0][n][m] = ((short)
/* 182 */           (arrayOfInt2[i] + 
/* 181 */           arrayOfInt2[(j + 256)] + 
/* 182 */           arrayOfInt2[(k + 512)] >> 16));
/*     */ 
/* 184 */         paramArrayOfShort[1][n][m] = ((short)
/* 187 */           (arrayOfInt2[(i + 768)] + 
/* 186 */           arrayOfInt2[(j + 1024)] + 
/* 187 */           arrayOfInt2[(k + 1280)] >> 16));
/*     */ 
/* 189 */         paramArrayOfShort[2][n][m] = ((short)
/* 192 */           (arrayOfInt2[(i + 1280)] + 
/* 191 */           arrayOfInt2[(j + 1536)] + 
/* 192 */           arrayOfInt2[(k + 1792)] >> 16));
/*     */       }
/* 194 */       i1++;
/*     */     }
/*     */ 
/* 197 */     this.row_idx_ += paramInt;
/*     */   }
/*     */ 
/*     */   public void rgb_ycc_init(CompressInfo paramCompressInfo)
/*     */   {
/*  70 */     this.pixel_row = new short[paramCompressInfo.input_components][paramCompressInfo.image_width];
/*     */ 
/*  73 */     this.rgb_ycc_tab = new int[2048];
/*     */ 
/*  76 */     for (int i = 0; i < 256; i++)
/*     */     {
/*  78 */       this.rgb_ycc_tab[i] = (scaleToInt(0.299D) * i);
/*  79 */       this.rgb_ycc_tab[(i + 256)] = (scaleToInt(0.587D) * i);
/*  80 */       this.rgb_ycc_tab[(i + 512)] = (scaleToInt(0.114D) * i + 32768);
/*  81 */       this.rgb_ycc_tab[(i + 768)] = (-scaleToInt(0.16874D) * i);
/*  82 */       this.rgb_ycc_tab[(i + 1024)] = (-scaleToInt(0.33126D) * i);
/*  83 */       this.rgb_ycc_tab[(i + 1280)] = (scaleToInt(0.5D) * i + 8388608);
/*     */ 
/*  87 */       this.rgb_ycc_tab[(i + 1536)] = (-scaleToInt(0.41869D) * i);
/*  88 */       this.rgb_ycc_tab[(i + 1792)] = (-scaleToInt(0.08130999999999999D) * i);
/*     */     }
/*     */ 
/*  91 */     this.row_idx_ = 0;
/*  92 */     this.jiIntBuf_ = new int[paramCompressInfo.image_width];
/*     */   }
/*     */ 
/*     */   public static int scaleToInt(double paramDouble)
/*     */   {
/*  58 */     return (int)(paramDouble * 65536.5D);
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.encoder.jpg.ConvertColor
 * JD-Core Version:    0.6.2
 */