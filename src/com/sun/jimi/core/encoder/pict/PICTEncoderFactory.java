/*    */ package com.sun.jimi.core.encoder.pict;
/*    */ 
/*    */ import com.sun.jimi.core.JimiEncoder;
/*    */ import com.sun.jimi.core.JimiEncoderFactorySupport;
/*    */ 
/*    */ public class PICTEncoderFactory extends JimiEncoderFactorySupport
/*    */ {
/* 24 */   public static final String[] MIME_TYPES = { "image/pict" };
/* 25 */   public static final String[] FILENAME_EXTENSIONS = { "pict", "pct" };
/*    */   public static final String FORMAT_NAME = "Macintosh PICT (PICT)";
/*    */ 
/*    */   public boolean canEncodeMultipleImages()
/*    */   {
/* 63 */     return false;
/*    */   }
/*    */ 
/*    */   public JimiEncoder createEncoder()
/*    */   {
/* 72 */     return new PICTEncoder();
/*    */   }
/*    */ 
/*    */   public String[] getFilenameExtensions()
/*    */   {
/* 45 */     return FILENAME_EXTENSIONS;
/*    */   }
/*    */ 
/*    */   public String getFormatName()
/*    */   {
/* 54 */     return "Macintosh PICT (PICT)";
/*    */   }
/*    */ 
/*    */   public String[] getMimeTypes()
/*    */   {
/* 36 */     return MIME_TYPES;
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.encoder.pict.PICTEncoderFactory
 * JD-Core Version:    0.6.2
 */