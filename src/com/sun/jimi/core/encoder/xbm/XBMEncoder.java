/*     */ package com.sun.jimi.core.encoder.xbm;
/*     */ 
/*     */ import com.sun.jimi.core.JimiException;
/*     */ import com.sun.jimi.core.compat.AdaptiveRasterImage;
/*     */ import com.sun.jimi.core.compat.JimiEncoderBase;
/*     */ import com.sun.jimi.core.util.JimiImageColorReducer;
/*     */ import java.awt.image.IndexColorModel;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ public class XBMEncoder extends JimiEncoderBase
/*     */ {
/*     */   protected PrintStream output;
/*     */   protected AdaptiveRasterImage jimiImage;
/*     */   protected int state;
/*     */ 
/*     */   protected void doImageEncode()
/*     */     throws JimiException, IOException
/*     */   {
/*  70 */     this.jimiImage = getJimiImage();
/*     */ 
/*  72 */     writeHeader();
/*  73 */     writeImageData();
/*  74 */     writeTrailer();
/*     */   }
/*     */ 
/*     */   public boolean driveEncoder()
/*     */     throws JimiException
/*     */   {
/*     */     try
/*     */     {
/*  45 */       this.jimiImage = getJimiImage();
/*  46 */       doImageEncode();
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/*  51 */       this.state = 1;
/*  52 */       throw new JimiException(localException.getMessage());
/*     */     }
/*     */ 
/*  55 */     this.state = 2;
/*     */ 
/*  57 */     return false;
/*     */   }
/*     */ 
/*     */   public int getState()
/*     */   {
/*  62 */     return this.state;
/*     */   }
/*     */ 
/*     */   public void initSpecificEncoder(OutputStream paramOutputStream, AdaptiveRasterImage paramAdaptiveRasterImage)
/*     */   {
/*  36 */     this.output = new PrintStream(paramOutputStream);
/*     */   }
/*     */ 
/*     */   protected int intensity(int paramInt)
/*     */   {
/* 143 */     return (paramInt & 0xFF) + (paramInt >> 8 & 0xFF) + (paramInt >> 16 & 0xFF);
/*     */   }
/*     */ 
/*     */   protected void writeHeader()
/*     */   {
/*  82 */     this.output.println("#define jimi_xbitmap_width " + this.jimiImage.getWidth());
/*  83 */     this.output.println("#define jimi_xbitmap_height " + this.jimiImage.getHeight());
/*  84 */     this.output.println("static char jimi_xbitmap_bits = {");
/*     */   }
/*     */ 
/*     */   protected void writeImageData()
/*     */     throws JimiException
/*     */   {
/*  94 */     JimiImageColorReducer localJimiImageColorReducer = new JimiImageColorReducer(2);
/*  95 */     this.jimiImage = new AdaptiveRasterImage(localJimiImageColorReducer.colorReduceFS(this.jimiImage.getBackend()));
/*     */ 
/*  98 */     IndexColorModel localIndexColorModel = (IndexColorModel)this.jimiImage.getColorModel();
/*     */ 
/* 102 */     int i = intensity(localIndexColorModel.getRGB(0));
/* 103 */     int j = intensity(localIndexColorModel.getRGB(1));
/* 104 */     int k = i < j ? 
/* 105 */       i : j;
/*     */ 
/* 107 */     int m = this.jimiImage.getWidth();
/* 108 */     int n = this.jimiImage.getHeight();
/*     */ 
/* 110 */     int i1 = m % 8 == 0 ? m / 8 : m / 8 + 1;
/*     */ 
/* 112 */     int[] arrayOfInt = new int[i1 * 8];
/* 113 */     for (int i2 = 0; i2 < n; i2++) {
/* 114 */       int i3 = 0;
/*     */ 
/* 116 */       this.jimiImage.getChannel(i2, arrayOfInt, 0);
/*     */ 
/* 118 */       for (int i4 = 0; i4 < i1; i4++) {
/* 119 */         int i5 = 0;
/*     */ 
/* 121 */         for (int i6 = 0; i6 < 8; i6++)
/*     */         {
/* 123 */           if (arrayOfInt[(i3++)] != k) {
/* 124 */             i5 = (byte)(i5 | 1 << i6);
/*     */           }
/*     */         }
/*     */ 
/* 128 */         this.output.print("0x" + Integer.toHexString(i5 & 0xFF));
/*     */ 
/* 130 */         if ((i2 != n - 1) || (i4 < i1 - 1)) {
/* 131 */           this.output.print(",");
/*     */         }
/*     */       }
/* 134 */       this.output.println();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void writeTrailer()
/*     */   {
/* 151 */     this.output.println("};");
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.encoder.xbm.XBMEncoder
 * JD-Core Version:    0.6.2
 */