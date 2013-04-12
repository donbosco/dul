/*    */ package com.sun.jimi.core.decoder.builtin;
/*    */ 
/*    */ import com.sun.jimi.core.JimiDecoder;
/*    */ import com.sun.jimi.core.JimiDecoderFactorySupport;
/*    */ import com.sun.jimi.core.util.FreeFormat;
/*    */ 
/*    */ public class BuiltinDecoderFactory extends JimiDecoderFactorySupport
/*    */   implements FreeFormat
/*    */ {
/* 30 */   public static final byte[] FORMAT_SIGNATURE = { -1, -40, -1 };
/*    */ 
/* 32 */   public static final String[] MIME_TYPES = { "image/jpeg", "image/jpg" };
/* 33 */   public static final String[] FILENAME_EXTENSIONS = { "jpg", "jpeg" };
/*    */   public static final String FORMAT_NAME = "JPEG";
/*    */ 
/*    */   public JimiDecoder createDecoder()
/*    */   {
/* 44 */     return null;
/*    */   }
/*    */ 
/*    */   public String[] getFilenameExtensions()
/*    */   {
/* 54 */     return FILENAME_EXTENSIONS;
/*    */   }
/*    */ 
/*    */   public String getFormatName()
/*    */   {
/* 59 */     return "JPEG";
/*    */   }
/*    */ 
/*    */   public byte[] getFormatSignature()
/*    */   {
/* 39 */     return FORMAT_SIGNATURE;
/*    */   }
/*    */ 
/*    */   public String[] getMimeTypes()
/*    */   {
/* 49 */     return MIME_TYPES;
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.decoder.builtin.BuiltinDecoderFactory
 * JD-Core Version:    0.6.2
 */