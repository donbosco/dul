/*    */ package com.sun.jimi.core.decoder.apf;
/*    */ 
/*    */ import com.sun.jimi.core.JimiDecoder;
/*    */ import com.sun.jimi.core.JimiDecoderFactory;
/*    */ 
/*    */ public class APFDecoderFactory
/*    */   implements JimiDecoderFactory
/*    */ {
/* 28 */   public static final byte[][] FORMAT_SIGNATURES = { { 65, 80, 70 } };
/* 29 */   public static final String[] MIME_TYPES = { "image/apf" };
/* 30 */   public static final String[] FILENAME_EXTENSIONS = { "apf" };
/*    */   public static final String FORMAT_NAME = "Activated Pseudo Format (APF)";
/*    */ 
/*    */   public JimiDecoder createDecoder()
/*    */   {
/* 41 */     return new APFDecoder();
/*    */   }
/*    */ 
/*    */   public String[] getFilenameExtensions()
/*    */   {
/* 51 */     return FILENAME_EXTENSIONS;
/*    */   }
/*    */ 
/*    */   public String getFormatName()
/*    */   {
/* 56 */     return "Activated Pseudo Format (APF)";
/*    */   }
/*    */ 
/*    */   public byte[][] getFormatSignatures()
/*    */   {
/* 36 */     return FORMAT_SIGNATURES;
/*    */   }
/*    */ 
/*    */   public String[] getMimeTypes()
/*    */   {
/* 46 */     return MIME_TYPES;
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.decoder.apf.APFDecoderFactory
 * JD-Core Version:    0.6.2
 */