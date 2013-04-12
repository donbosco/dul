/*    */ package com.sun.jimi.core.decoder.sunraster;
/*    */ 
/*    */ import com.sun.jimi.core.JimiDecoder;
/*    */ import com.sun.jimi.core.JimiDecoderFactorySupport;
/*    */ 
/*    */ public class SunRasterDecoderFactory extends JimiDecoderFactorySupport
/*    */ {
/* 28 */   public static final byte[] FORMAT_SIGNATURE = { 89, -90, 106, -107 };
/*    */ 
/* 30 */   public static final String[] MIME_TYPES = { "image/cmu-raster" };
/* 31 */   public static final String[] FILENAME_EXTENSIONS = { "ras" };
/*    */   public static final String FORMAT_NAME = "Sun Raster Format";
/*    */ 
/*    */   public JimiDecoder createDecoder()
/*    */   {
/* 42 */     return new SunRasterDecoder();
/*    */   }
/*    */ 
/*    */   public String[] getFilenameExtensions()
/*    */   {
/* 52 */     return FILENAME_EXTENSIONS;
/*    */   }
/*    */ 
/*    */   public String getFormatName()
/*    */   {
/* 57 */     return "Sun Raster Format";
/*    */   }
/*    */ 
/*    */   public byte[] getFormatSignature()
/*    */   {
/* 37 */     return FORMAT_SIGNATURE;
/*    */   }
/*    */ 
/*    */   public String[] getMimeTypes()
/*    */   {
/* 47 */     return MIME_TYPES;
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.decoder.sunraster.SunRasterDecoderFactory
 * JD-Core Version:    0.6.2
 */