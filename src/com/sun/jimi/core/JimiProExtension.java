/*     */ package com.sun.jimi.core;
/*     */ 
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class JimiProExtension
/*     */   implements JimiExtension
/*     */ {
/*     */   private static final String VENDOR = "Sun Microsystems, Inc.";
/*     */   private static final String DESCRIPTION = "Jimi Professional formats";
/*     */   private static final String VERSION = "2.0";
/*     */   private static JimiDecoderFactory[] DECODERS;
/*  33 */   private static String[] decoderNames = { 
/*  34 */     "com.sun.jimi.core.decoder.bmp.BMPDecoderFactory", 
/*  35 */     "com.sun.jimi.core.decoder.cur.CURDecoderFactory", 
/*  36 */     "com.sun.jimi.core.decoder.gif.GIFDecoderFactory", 
/*  37 */     "com.sun.jimi.core.decoder.ico.ICODecoderFactory", 
/*  38 */     "com.sun.jimi.core.decoder.png.PNGDecoderFactory", 
/*  39 */     "com.sun.jimi.core.decoder.sunraster.SunRasterDecoderFactory", 
/*  40 */     "com.sun.jimi.core.decoder.tga.TGADecoderFactory", 
/*  41 */     "com.sun.jimi.core.decoder.tiff.TIFDecoderFactory", 
/*  42 */     "com.sun.jimi.core.decoder.pcx.PCXDecoderFactory", 
/*  43 */     "com.sun.jimi.core.decoder.pict.PICTDecoderFactory", 
/*  44 */     "com.sun.jimi.core.decoder.psd.PSDDecoderFactory", 
/*  45 */     "com.sun.jimi.core.decoder.xbm.XBMDecoderFactory", 
/*  46 */     "com.sun.jimi.core.decoder.xpm.XPMDecoderFactory", 
/*  47 */     "com.sun.jimi.core.decoder.builtin.BuiltinDecoderFactory" };
/*     */   private static JimiEncoderFactory[] ENCODERS;
/*  53 */   private static String[] encoderNames = { 
/*  54 */     "com.sun.jimi.core.encoder.xpm.XPMEncoderFactory", 
/*  55 */     "com.sun.jimi.core.encoder.xbm.XBMEncoderFactory", 
/*  56 */     "com.sun.jimi.core.encoder.png.PNGEncoderFactory", 
/*  57 */     "com.sun.jimi.core.encoder.sunraster.SunRasterEncoderFactory", 
/*  58 */     "com.sun.jimi.core.encoder.bmp.BMPEncoderFactory", 
/*  59 */     "com.sun.jimi.core.encoder.psd.PSDEncoderFactory", 
/*  60 */     "com.sun.jimi.core.encoder.pict.PICTEncoderFactory", 
/*  61 */     "com.sun.jimi.core.encoder.pcx.PCXEncoderFactory", 
/*  62 */     "com.sun.jimi.core.encoder.tga.TGAEncoderFactory", 
/*  63 */     null };
/*     */ 
/*     */   static
/*     */   {
/*  74 */     encoderNames[(encoderNames.length - 1)] = "com.sun.jimi.core.encoder.jpg.JPGEncoderFactory";
/*     */ 
/*  76 */     Vector localVector = new Vector();
/*  77 */     for (int i = 0; i < decoderNames.length; i++)
/*     */       try {
/*  79 */         Class localObject = Class.forName(decoderNames[i]);
/*  80 */         JimiDecoderFactory localJimiDecoderFactory = (JimiDecoderFactory)((Class)localObject).newInstance();
/*  81 */         localVector.addElement(localJimiDecoderFactory);
/*     */       }
/*     */       catch (Exception localException1)
/*     */       {
/*     */       }
/*  86 */     DECODERS = new JimiDecoderFactory[localVector.size()];
/*  87 */     localVector.copyInto(DECODERS);
/*     */ 
/*  89 */     Object localObject = new Vector();
/*  90 */     for (int j = 0; j < encoderNames.length; j++)
/*     */       try {
/*  92 */         Class localClass = Class.forName(encoderNames[j]);
/*  93 */         JimiEncoderFactory localJimiEncoderFactory = (JimiEncoderFactory)localClass.newInstance();
/*  94 */         ((Vector)localObject).addElement(localJimiEncoderFactory);
/*     */       }
/*     */       catch (Exception localException2)
/*     */       {
/*     */       }
/*  99 */     ENCODERS = new JimiEncoderFactory[((Vector)localObject).size()];
/* 100 */     ((Vector)localObject).copyInto(ENCODERS);
/*     */   }
/*     */ 
/*     */   public JimiDecoderFactory[] getDecoders()
/*     */   {
/* 117 */     return DECODERS;
/*     */   }
/*     */ 
/*     */   public String getDescription()
/*     */   {
/* 109 */     return "Jimi Professional formats";
/*     */   }
/*     */ 
/*     */   public JimiEncoderFactory[] getEncoders()
/*     */   {
/* 121 */     return ENCODERS;
/*     */   }
/*     */ 
/*     */   public String getVendor()
/*     */   {
/* 105 */     return "Sun Microsystems, Inc.";
/*     */   }
/*     */ 
/*     */   public String getVersionString()
/*     */   {
/* 113 */     return "2.0";
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.JimiProExtension
 * JD-Core Version:    0.6.2
 */