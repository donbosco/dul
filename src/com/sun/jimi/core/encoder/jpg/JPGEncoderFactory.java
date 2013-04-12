/*    */ package com.sun.jimi.core.encoder.jpg;
/*    */ 
/*    */ import com.sun.jimi.core.JimiEncoder;
/*    */ import com.sun.jimi.core.JimiEncoderFactorySupport;
/*    */ import com.sun.jimi.core.util.FreeFormat;
/*    */ 
/*    */ public class JPGEncoderFactory extends JimiEncoderFactorySupport
/*    */   implements FreeFormat
/*    */ {
/* 25 */   public static final String[] MIME_TYPES = { "image/jpg", "image/jpeg" };
/* 26 */   public static final String[] FILENAME_EXTENSIONS = { "jpg", "jpeg" };
/*    */   public static final String FORMAT_NAME = "JPEG";
/*    */ 
/*    */   public boolean canEncodeMultipleImages()
/*    */   {
/* 64 */     return false;
/*    */   }
/*    */ 
/*    */   public JimiEncoder createEncoder()
/*    */   {
/* 73 */     return new JPGEncoder();
/*    */   }
/*    */ 
/*    */   public String[] getFilenameExtensions()
/*    */   {
/* 46 */     return FILENAME_EXTENSIONS;
/*    */   }
/*    */ 
/*    */   public String getFormatName()
/*    */   {
/* 55 */     return "JPEG";
/*    */   }
/*    */ 
/*    */   public String[] getMimeTypes()
/*    */   {
/* 37 */     return MIME_TYPES;
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.encoder.jpg.JPGEncoderFactory
 * JD-Core Version:    0.6.2
 */