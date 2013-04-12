/*    */ package com.sun.jimi.core.decoder.xbm;
/*    */ 
/*    */ import com.sun.jimi.core.JimiDecoder;
/*    */ import com.sun.jimi.core.JimiDecoderFactorySupport;
/*    */ 
/*    */ public class XBMDecoderFactory extends JimiDecoderFactorySupport
/*    */ {
/* 28 */   public static final byte[] FORMAT_SIGNATURE = { 47, 42, 32, 
/* 29 */     88, 66, 77 };
/*    */ 
/* 30 */   public static final String[] MIME_TYPES = { "image/xbm" };
/* 31 */   public static final String[] FILENAME_EXTENSIONS = { "xbm" };
/*    */   public static final String FORMAT_NAME = "X BitMap";
/*    */ 
/*    */   public JimiDecoder createDecoder()
/*    */   {
/* 42 */     return new XBMDecoder();
/*    */   }
/*    */ 
/*    */   public String[] getFilenameExtensions()
/*    */   {
/* 52 */     return FILENAME_EXTENSIONS;
/*    */   }
/*    */ 
/*    */   public String getFormatName()
/*    */   {
/* 57 */     return "X BitMap";
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
 * Qualified Name:     com.sun.jimi.core.decoder.xbm.XBMDecoderFactory
 * JD-Core Version:    0.6.2
 */