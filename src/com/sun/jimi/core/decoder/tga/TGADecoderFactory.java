/*    */ package com.sun.jimi.core.decoder.tga;
/*    */ 
/*    */ import com.sun.jimi.core.JimiDecoder;
/*    */ import com.sun.jimi.core.JimiDecoderFactorySupport;
/*    */ 
/*    */ public class TGADecoderFactory extends JimiDecoderFactorySupport
/*    */ {
/* 28 */   public static final byte[] FORMAT_SIGNATURE = null;
/* 29 */   public static final String[] MIME_TYPES = { "image/targa" };
/* 30 */   public static final String[] FILENAME_EXTENSIONS = { "tga" };
/*    */   public static final String FORMAT_NAME = "Targa (TGA)";
/*    */ 
/*    */   public JimiDecoder createDecoder()
/*    */   {
/* 41 */     return new TGADecoder();
/*    */   }
/*    */ 
/*    */   public String[] getFilenameExtensions()
/*    */   {
/* 51 */     return FILENAME_EXTENSIONS;
/*    */   }
/*    */ 
/*    */   public String getFormatName()
/*    */   {
/* 56 */     return "Targa (TGA)";
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
 * Qualified Name:     com.sun.jimi.core.decoder.tga.TGADecoderFactory
 * JD-Core Version:    0.6.2
 */