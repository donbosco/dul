/*    */ package com.sun.jimi.core.encoder.png;
/*    */ 
/*    */ import com.sun.jimi.core.JimiEncoder;
/*    */ import com.sun.jimi.core.JimiEncoderFactorySupport;
/*    */ import com.sun.jimi.core.util.FreeFormat;
/*    */ 
/*    */ public class PNGEncoderFactory extends JimiEncoderFactorySupport
/*    */   implements FreeFormat
/*    */ {
/* 25 */   public static final String[] MIME_TYPES = { "image/png" };
/* 26 */   public static final String[] FILENAME_EXTENSIONS = { "png" };
/*    */   public static final String FORMAT_NAME = "Activated Pseudo Format (PNG)";
/*    */ 
/*    */   public boolean canEncodeMultipleImages()
/*    */   {
/* 64 */     return false;
/*    */   }
/*    */ 
/*    */   public JimiEncoder createEncoder()
/*    */   {
/* 73 */     return new PNGEncoder();
/*    */   }
/*    */ 
/*    */   public String[] getFilenameExtensions()
/*    */   {
/* 46 */     return FILENAME_EXTENSIONS;
/*    */   }
/*    */ 
/*    */   public String getFormatName()
/*    */   {
/* 55 */     return "Activated Pseudo Format (PNG)";
/*    */   }
/*    */ 
/*    */   public String[] getMimeTypes()
/*    */   {
/* 37 */     return MIME_TYPES;
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.encoder.png.PNGEncoderFactory
 * JD-Core Version:    0.6.2
 */