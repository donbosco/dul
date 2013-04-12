/*    */ package com.sun.jimi.core.decoder.tiff;
/*    */ 
/*    */ import com.sun.jimi.core.JimiDecoder;
/*    */ import com.sun.jimi.core.JimiDecoderFactorySupport;
/*    */ 
/*    */ public class TIFDecoderFactory extends JimiDecoderFactorySupport
/*    */ {
/* 29 */   public static final byte[][] FORMAT_SIGNATURES = { { 77, 77 }, 
/* 30 */     { 73, 73 } };
/*    */ 
/* 31 */   public static final String[] MIME_TYPES = { "image/tiff" };
/* 32 */   public static final String[] FILENAME_EXTENSIONS = { "tif", "tiff" };
/*    */   public static final String FORMAT_NAME = "TIFF";
/*    */ 
/*    */   public JimiDecoder createDecoder()
/*    */   {
/* 43 */     return new TIFDecoder();
/*    */   }
/*    */ 
/*    */   public String[] getFilenameExtensions()
/*    */   {
/* 53 */     return FILENAME_EXTENSIONS;
/*    */   }
/*    */ 
/*    */   public String getFormatName()
/*    */   {
/* 58 */     return "TIFF";
/*    */   }
/*    */ 
/*    */   public byte[][] getFormatSignatures()
/*    */   {
/* 38 */     return FORMAT_SIGNATURES;
/*    */   }
/*    */ 
/*    */   public String[] getMimeTypes()
/*    */   {
/* 48 */     return MIME_TYPES;
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.decoder.tiff.TIFDecoderFactory
 * JD-Core Version:    0.6.2
 */