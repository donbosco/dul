/*    */ package com.sun.jimi.core.encoder.apf;
/*    */ 
/*    */ import com.sun.jimi.core.JimiEncoder;
/*    */ import com.sun.jimi.core.JimiEncoderFactory;
/*    */ 
/*    */ public class APFEncoderFactory
/*    */   implements JimiEncoderFactory
/*    */ {
/* 24 */   public static final byte[] FORMAT_SIGNATURE = { 65, 80, 70 };
/* 25 */   public static final String[] MIME_TYPES = { "image/apf" };
/* 26 */   public static final String[] FILENAME_EXTENSIONS = { "apf" };
/*    */   public static final String FORMAT_NAME = "Activated Pseudo Format (APF)";
/*    */ 
/*    */   public boolean canEncodeMultipleImages()
/*    */   {
/* 64 */     return false;
/*    */   }
/*    */ 
/*    */   public JimiEncoder createEncoder()
/*    */   {
/* 73 */     return new APFEncoder();
/*    */   }
/*    */ 
/*    */   public String[] getFilenameExtensions()
/*    */   {
/* 46 */     return FILENAME_EXTENSIONS;
/*    */   }
/*    */ 
/*    */   public String getFormatName()
/*    */   {
/* 55 */     return "Activated Pseudo Format (APF)";
/*    */   }
/*    */ 
/*    */   public String[] getMimeTypes()
/*    */   {
/* 37 */     return MIME_TYPES;
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.encoder.apf.APFEncoderFactory
 * JD-Core Version:    0.6.2
 */