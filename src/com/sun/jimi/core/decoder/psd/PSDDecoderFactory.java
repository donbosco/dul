/*    */ package com.sun.jimi.core.decoder.psd;
/*    */ 
/*    */ import com.sun.jimi.core.JimiDecoder;
/*    */ import com.sun.jimi.core.JimiDecoderFactorySupport;
/*    */ 
/*    */ public class PSDDecoderFactory extends JimiDecoderFactorySupport
/*    */ {
/* 28 */   public static final byte[] FORMAT_SIGNATURE = { 56, 66, 80, 83 };
/* 29 */   public static final String[] MIME_TYPES = { "image/psd" };
/* 30 */   public static final String[] FILENAME_EXTENSIONS = { "psd" };
/*    */   public static final String FORMAT_NAME = "Adobe Photoshop";
/*    */ 
/*    */   public JimiDecoder createDecoder()
/*    */   {
/* 41 */     return new PSDDecoder();
/*    */   }
/*    */ 
/*    */   public String[] getFilenameExtensions()
/*    */   {
/* 51 */     return FILENAME_EXTENSIONS;
/*    */   }
/*    */ 
/*    */   public String getFormatName()
/*    */   {
/* 56 */     return "Adobe Photoshop";
/*    */   }
/*    */ 
/*    */   public byte[] getFormatSignature()
/*    */   {
/* 36 */     return FORMAT_SIGNATURE;
/*    */   }
/*    */ 
/*    */   public String[] getMimeTypes()
/*    */   {
/* 46 */     return MIME_TYPES;
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.decoder.psd.PSDDecoderFactory
 * JD-Core Version:    0.6.2
 */