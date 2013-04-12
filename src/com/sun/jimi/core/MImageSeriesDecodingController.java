/*     */ package com.sun.jimi.core;
/*     */ 
/*     */ class MImageSeriesDecodingController extends ImageSeriesDecodingController
/*     */ {
/*     */   protected JimiMultiImageRasterDecoder decoder;
/*     */ 
/*     */   public MImageSeriesDecodingController(JimiMultiImageRasterDecoder paramJimiMultiImageRasterDecoder)
/*     */   {
/* 173 */     this.decoder = paramJimiMultiImageRasterDecoder;
/*     */   }
/*     */ 
/*     */   protected JimiDecodingController createNextController()
/*     */   {
/* 178 */     return this.decoder.initNextDecoding();
/*     */   }
/*     */ 
/*     */   public boolean hasMoreImages()
/*     */   {
/* 192 */     return this.decoder.hasMoreImages();
/*     */   }
/*     */ 
/*     */   public void skipNextImage()
/*     */   {
/*     */     try
/*     */     {
/* 184 */       this.decoder.doSkipNextImage();
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.MImageSeriesDecodingController
 * JD-Core Version:    0.6.2
 */