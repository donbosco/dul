/*     */ package com.sun.jimi.core.compat;
/*     */ 
/*     */ import com.sun.jimi.core.ImageSeriesDecodingController;
/*     */ import com.sun.jimi.core.JimiDecodingController;
/*     */ import com.sun.jimi.core.JimiException;
/*     */ import com.sun.jimi.core.JimiImage;
/*     */ import com.sun.jimi.core.util.ErrorJimiImage;
/*     */ 
/*     */ class JimiDecoderBaseSeriesController extends ImageSeriesDecodingController
/*     */ {
/*     */   protected JimiDecoderBase decoder;
/*     */   protected JimiDecodingController nextController;
/*     */ 
/*     */   public JimiDecoderBaseSeriesController(JimiDecoderBase paramJimiDecoderBase)
/*     */   {
/* 339 */     this.decoder = paramJimiDecoderBase;
/*     */   }
/*     */ 
/*     */   public JimiDecodingController createNextController()
/*     */   {
/* 349 */     if (this.decoder.error) {
/* 350 */       return new JimiDecodingController(new ErrorJimiImage());
/*     */     }
/* 352 */     if (this.nextController != null) {
/* 353 */       JimiDecodingController localJimiDecodingController = this.nextController;
/* 354 */       this.nextController = null;
/* 355 */       return localJimiDecodingController;
/*     */     }
/* 357 */     return this.decoder.decodeNextImage();
/*     */   }
/*     */ 
/*     */   public int getNumberOfImages() {
/* 361 */     return this.decoder.getNumberOfImages();
/*     */   }
/*     */ 
/*     */   public boolean hasMoreImages() {
/* 365 */     if ((this.decoder.getState() & 0x8) == 0) {
/* 366 */       return false;
/*     */     }
/*     */ 
/* 369 */     if (this.nextController == null) {
/* 370 */       this.nextController = createNextController();
/*     */     }
/* 372 */     JimiImage localJimiImage = this.nextController.getJimiImage();
/* 373 */     if (localJimiImage.isError()) {
/* 374 */       return false;
/*     */     }
/*     */ 
/* 377 */     return true;
/*     */   }
/*     */ 
/*     */   public void skipNextImage()
/*     */     throws JimiException
/*     */   {
/* 343 */     if (!this.decoder.error)
/* 344 */       this.decoder.skipImage();
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.compat.JimiDecoderBaseSeriesController
 * JD-Core Version:    0.6.2
 */