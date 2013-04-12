/*     */ package com.sun.jimi.core;
/*     */ 
/*     */ class JimiMultiImageRasterDecoderRunner
/*     */   implements Runnable
/*     */ {
/*     */   JimiMultiImageRasterDecoder decoder;
/*     */ 
/*     */   public JimiMultiImageRasterDecoderRunner(JimiMultiImageRasterDecoder paramJimiMultiImageRasterDecoder)
/*     */   {
/* 203 */     this.decoder = paramJimiMultiImageRasterDecoder;
/*     */   }
/*     */ 
/*     */   public void run()
/*     */   {
/* 208 */     this.decoder.driveDecoding();
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.JimiMultiImageRasterDecoderRunner
 * JD-Core Version:    0.6.2
 */