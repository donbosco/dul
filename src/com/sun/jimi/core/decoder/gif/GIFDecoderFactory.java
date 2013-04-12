/*    */ package com.sun.jimi.core.decoder.gif;
/*    */ 
/*    */ import com.sun.jimi.core.JimiDecoder;
/*    */ import com.sun.jimi.core.JimiDecoderFactorySupport;
/*    */ import com.sun.jimi.core.util.FreeFormat;
/*    */ 
/*    */ public class GIFDecoderFactory extends JimiDecoderFactorySupport
/*    */   implements FreeFormat
/*    */ {
/* 29 */   public static final byte[] FORMAT_SIGNATURE = { 71, 73, 70 };
/* 30 */   public static final String[] MIME_TYPES = { "image/gif" };
/* 31 */   public static final String[] FILENAME_EXTENSIONS = { "gif" };
/*    */   public static final String FORMAT_NAME = "Graphics Interchange Format (GIF)";
/*    */ 
/*    */   public JimiDecoder createDecoder()
/*    */   {
/* 42 */     return new GIFDecoder();
/*    */   }
/*    */ 
/*    */   public String[] getFilenameExtensions()
/*    */   {
/* 52 */     return FILENAME_EXTENSIONS;
/*    */   }
/*    */ 
/*    */   public String getFormatName()
/*    */   {
/* 57 */     return "Graphics Interchange Format (GIF)";
/*    */   }
/*    */ 
/*    */   public byte[] getFormatSignature()
/*    */   {
/* 37 */     return FORMAT_SIGNATURE;
/*    */   }
/*    */ 
/*    */   public String[] getMimeTypes()
/*    */   {
/* 47 */     return MIME_TYPES;
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.decoder.gif.GIFDecoderFactory
 * JD-Core Version:    0.6.2
 */