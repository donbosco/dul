/*    */ package com.sun.jimi.core.encoder.bmp;
/*    */ 
/*    */ import com.sun.jimi.core.JimiEncoder;
/*    */ import com.sun.jimi.core.JimiEncoderFactorySupport;
/*    */ 
/*    */ public class BMPEncoderFactory extends JimiEncoderFactorySupport
/*    */ {
/* 24 */   public static final String[] MIME_TYPES = { "image/bmp" };
/* 25 */   public static final String[] FILENAME_EXTENSIONS = { "bmp" };
/*    */   public static final String FORMAT_NAME = "X Pixmap (BMP)";
/*    */ 
/*    */   public boolean canEncodeMultipleImages()
/*    */   {
/* 63 */     return false;
/*    */   }
/*    */ 
/*    */   public JimiEncoder createEncoder()
/*    */   {
/* 72 */     return new BMPEncoder();
/*    */   }
/*    */ 
/*    */   public String[] getFilenameExtensions()
/*    */   {
/* 45 */     return FILENAME_EXTENSIONS;
/*    */   }
/*    */ 
/*    */   public String getFormatName()
/*    */   {
/* 54 */     return "X Pixmap (BMP)";
/*    */   }
/*    */ 
/*    */   public String[] getMimeTypes()
/*    */   {
/* 36 */     return MIME_TYPES;
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.encoder.bmp.BMPEncoderFactory
 * JD-Core Version:    0.6.2
 */