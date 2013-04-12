/*     */ package com.sun.jimi.core;
/*     */ 
/*     */ class SingleImageSeriesDecodingController extends ImageSeriesDecodingController
/*     */ {
/*     */   protected JimiDecodingController controller;
/*     */ 
/*     */   public SingleImageSeriesDecodingController(JimiDecodingController paramJimiDecodingController)
/*     */   {
/* 140 */     this.controller = paramJimiDecodingController;
/*     */   }
/*     */ 
/*     */   protected JimiDecodingController createNextController()
/*     */   {
/* 145 */     JimiDecodingController localJimiDecodingController = this.controller;
/* 146 */     this.controller = null;
/* 147 */     return localJimiDecodingController;
/*     */   }
/*     */ 
/*     */   public boolean hasMoreImages()
/*     */   {
/* 157 */     return this.controller != null;
/*     */   }
/*     */ 
/*     */   public void skipNextImage()
/*     */   {
/* 152 */     this.controller = null;
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.SingleImageSeriesDecodingController
 * JD-Core Version:    0.6.2
 */