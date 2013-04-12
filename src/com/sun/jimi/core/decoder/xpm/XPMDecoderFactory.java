/*    */ package com.sun.jimi.core.decoder.xpm;
/*    */ 
/*    */ import com.sun.jimi.core.JimiDecoder;
/*    */ import com.sun.jimi.core.JimiDecoderFactorySupport;
/*    */ 
/*    */ public class XPMDecoderFactory extends JimiDecoderFactorySupport
/*    */ {
/* 28 */   public static final byte[] FORMAT_SIGNATURE = { 47, 42, 32, 
/* 29 */     88, 80, 77 };
/*    */ 
/* 30 */   public static final String[] MIME_TYPES = { "image/xpm" };
/* 31 */   public static final String[] FILENAME_EXTENSIONS = { "xpm" };
/*    */   public static final String FORMAT_NAME = "X PixMap (XPM)";
/*    */ 
/*    */   public JimiDecoder createDecoder()
/*    */   {
/* 42 */     return new XPMDecoder();
/*    */   }
/*    */ 
/*    */   public String[] getFilenameExtensions()
/*    */   {
/* 52 */     return FILENAME_EXTENSIONS;
/*    */   }
/*    */ 
/*    */   public String getFormatName()
/*    */   {
/* 57 */     return "X PixMap (XPM)";
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
 * Qualified Name:     com.sun.jimi.core.decoder.xpm.XPMDecoderFactory
 * JD-Core Version:    0.6.2
 */