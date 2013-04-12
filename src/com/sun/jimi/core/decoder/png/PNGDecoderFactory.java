/*    */ package com.sun.jimi.core.decoder.png;
/*    */ 
/*    */ import com.sun.jimi.core.JimiDecoder;
/*    */ import com.sun.jimi.core.JimiDecoderFactorySupport;
/*    */ import com.sun.jimi.core.util.FreeFormat;
/*    */ 
/*    */ public class PNGDecoderFactory extends JimiDecoderFactorySupport
/*    */   implements FreeFormat
/*    */ {
/* 25 */   public static final byte[] FORMAT_SIGNATURE = { 
/* 26 */     -119, 80, 78, 71, 13, 10, 26, 10 };
/*    */ 
/* 27 */   public static final String[] MIME_TYPES = { "image/png" };
/* 28 */   public static final String[] FILENAME_EXTENSIONS = { "png" };
/*    */   public static final String FORMAT_NAME = "Portable Network Graphics (PNG)";
/*    */ 
/*    */   public JimiDecoder createDecoder()
/*    */   {
/* 39 */     return new PNGDecoder();
/*    */   }
/*    */ 
/*    */   public String[] getFilenameExtensions()
/*    */   {
/* 49 */     return FILENAME_EXTENSIONS;
/*    */   }
/*    */ 
/*    */   public String getFormatName()
/*    */   {
/* 54 */     return "Portable Network Graphics (PNG)";
/*    */   }
/*    */ 
/*    */   public byte[] getFormatSignature()
/*    */   {
/* 34 */     return FORMAT_SIGNATURE;
/*    */   }
/*    */ 
/*    */   public String[] getMimeTypes()
/*    */   {
/* 44 */     return MIME_TYPES;
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.decoder.png.PNGDecoderFactory
 * JD-Core Version:    0.6.2
 */