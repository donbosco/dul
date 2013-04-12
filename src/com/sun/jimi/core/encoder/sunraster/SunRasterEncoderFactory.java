/*    */ package com.sun.jimi.core.encoder.sunraster;
/*    */ 
/*    */ import com.sun.jimi.core.JimiEncoder;
/*    */ import com.sun.jimi.core.JimiEncoderFactorySupport;
/*    */ 
/*    */ public class SunRasterEncoderFactory extends JimiEncoderFactorySupport
/*    */ {
/* 24 */   public static final String[] MIME_TYPES = { "image/cmu-raster" };
/* 25 */   public static final String[] FILENAME_EXTENSIONS = { "ras" };
/*    */   public static final String FORMAT_NAME = "Sun Rasterfile (RAS)";
/*    */ 
/*    */   public boolean canEncodeMultipleImages()
/*    */   {
/* 63 */     return false;
/*    */   }
/*    */ 
/*    */   public JimiEncoder createEncoder()
/*    */   {
/* 72 */     return new SunRasterEncoder();
/*    */   }
/*    */ 
/*    */   public String[] getFilenameExtensions()
/*    */   {
/* 45 */     return FILENAME_EXTENSIONS;
/*    */   }
/*    */ 
/*    */   public String getFormatName()
/*    */   {
/* 54 */     return "Sun Rasterfile (RAS)";
/*    */   }
/*    */ 
/*    */   public String[] getMimeTypes()
/*    */   {
/* 36 */     return MIME_TYPES;
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.encoder.sunraster.SunRasterEncoderFactory
 * JD-Core Version:    0.6.2
 */