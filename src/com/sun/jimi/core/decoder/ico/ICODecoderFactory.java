/*    */ package com.sun.jimi.core.decoder.ico;
/*    */ 
/*    */ import com.sun.jimi.core.JimiDecoder;
/*    */ import com.sun.jimi.core.JimiDecoderFactorySupport;
/*    */ 
/*    */ public class ICODecoderFactory extends JimiDecoderFactorySupport
/*    */ {
/* 28 */   public static final byte[] FORMAT_SIGNATURE = null;
/* 29 */   public static final String[] MIME_TYPES = { "image/ico" };
/* 30 */   public static final String[] FILENAME_EXTENSIONS = { "ico" };
/*    */   public static final String FORMAT_NAME = "ICO";
/*    */ 
/*    */   public JimiDecoder createDecoder()
/*    */   {
/* 41 */     return new ICODecoder();
/*    */   }
/*    */ 
/*    */   public String[] getFilenameExtensions()
/*    */   {
/* 51 */     return FILENAME_EXTENSIONS;
/*    */   }
/*    */ 
/*    */   public String getFormatName()
/*    */   {
/* 56 */     return "ICO";
/*    */   }
/*    */ 
/*    */   public byte[] getFormatSignature()
/*    */   {
/* 36 */     return null;
/*    */   }
/*    */ 
/*    */   public String[] getMimeTypes()
/*    */   {
/* 46 */     return MIME_TYPES;
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.decoder.ico.ICODecoderFactory
 * JD-Core Version:    0.6.2
 */