/*    */ package com.sun.jimi.core;
/*    */ 
/*    */ public abstract class JimiDecoderFactorySupport
/*    */   implements JimiDecoderFactory
/*    */ {
/*    */   public abstract JimiDecoder createDecoder();
/*    */ 
/*    */   public abstract String[] getFilenameExtensions();
/*    */ 
/*    */   public abstract String getFormatName();
/*    */ 
/*    */   public byte[] getFormatSignature()
/*    */   {
/* 29 */     return null;
/*    */   }
/*    */ 
/*    */   public byte[][] getFormatSignatures()
/*    */   {
/* 38 */     byte[] arrayOfByte = getFormatSignature();
/* 39 */     return new byte[][] { arrayOfByte == null ? null : arrayOfByte };
/*    */   }
/*    */ 
/*    */   public abstract String[] getMimeTypes();
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.JimiDecoderFactorySupport
 * JD-Core Version:    0.6.2
 */