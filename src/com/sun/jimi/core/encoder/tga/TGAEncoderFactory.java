/*    */ package com.sun.jimi.core.encoder.tga;
/*    */ 
/*    */ import com.sun.jimi.core.JimiEncoder;
/*    */ import com.sun.jimi.core.JimiEncoderFactorySupport;
/*    */ 
/*    */ public class TGAEncoderFactory extends JimiEncoderFactorySupport
/*    */ {
/* 24 */   public static final String[] MIME_TYPES = { "image/tga" };
/* 25 */   public static final String[] FILENAME_EXTENSIONS = { "tga" };
/*    */   public static final String FORMAT_NAME = "Targa (TGA)";
/*    */ 
/*    */   public boolean canEncodeMultipleImages()
/*    */   {
/* 63 */     return false;
/*    */   }
/*    */ 
/*    */   public JimiEncoder createEncoder()
/*    */   {
/* 72 */     return new TGAEncoder();
/*    */   }
/*    */ 
/*    */   public String[] getFilenameExtensions()
/*    */   {
/* 45 */     return FILENAME_EXTENSIONS;
/*    */   }
/*    */ 
/*    */   public String getFormatName()
/*    */   {
/* 54 */     return "Targa (TGA)";
/*    */   }
/*    */ 
/*    */   public String[] getMimeTypes()
/*    */   {
/* 36 */     return MIME_TYPES;
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.encoder.tga.TGAEncoderFactory
 * JD-Core Version:    0.6.2
 */