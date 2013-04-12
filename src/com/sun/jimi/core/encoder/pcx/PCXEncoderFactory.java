/*    */ package com.sun.jimi.core.encoder.pcx;
/*    */ 
/*    */ import com.sun.jimi.core.JimiEncoder;
/*    */ import com.sun.jimi.core.JimiEncoderFactorySupport;
/*    */ 
/*    */ public class PCXEncoderFactory extends JimiEncoderFactorySupport
/*    */ {
/* 24 */   public static final String[] MIME_TYPES = { "image/pcx" };
/* 25 */   public static final String[] FILENAME_EXTENSIONS = { "pcx" };
/*    */   public static final String FORMAT_NAME = "X Pixmap (PCX)";
/*    */ 
/*    */   public boolean canEncodeMultipleImages()
/*    */   {
/* 63 */     return false;
/*    */   }
/*    */ 
/*    */   public JimiEncoder createEncoder()
/*    */   {
/* 72 */     return new PCXEncoder();
/*    */   }
/*    */ 
/*    */   public String[] getFilenameExtensions()
/*    */   {
/* 45 */     return FILENAME_EXTENSIONS;
/*    */   }
/*    */ 
/*    */   public String getFormatName()
/*    */   {
/* 54 */     return "X Pixmap (PCX)";
/*    */   }
/*    */ 
/*    */   public String[] getMimeTypes()
/*    */   {
/* 36 */     return MIME_TYPES;
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.encoder.pcx.PCXEncoderFactory
 * JD-Core Version:    0.6.2
 */