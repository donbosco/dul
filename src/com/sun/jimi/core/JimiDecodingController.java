/*     */ package com.sun.jimi.core;
/*     */ 
/*     */ import com.sun.jimi.core.util.ProgressListener;
/*     */ 
/*     */ public class JimiDecodingController
/*     */ {
/*     */   protected JimiImage jimiImage;
/*     */   protected ProgressListener progressListener;
/*  59 */   protected boolean decodingAllowed = false;
/*     */ 
/*     */   public JimiDecodingController(JimiImage paramJimiImage)
/*     */   {
/*  63 */     this.jimiImage = paramJimiImage;
/*     */   }
/*     */ 
/*     */   public JimiImage getJimiImage()
/*     */   {
/*  68 */     return this.jimiImage;
/*     */   }
/*     */ 
/*     */   public ProgressListener getProgressListener()
/*     */   {
/* 111 */     return this.progressListener;
/*     */   }
/*     */ 
/*     */   public boolean hasProgressListener()
/*     */   {
/* 120 */     return this.progressListener != null;
/*     */   }
/*     */ 
/*     */   public synchronized void requestDecoding()
/*     */   {
/*  91 */     this.decodingAllowed = true;
/*  92 */     notifyAll();
/*     */   }
/*     */ 
/*     */   public void setProgressListener(ProgressListener paramProgressListener)
/*     */   {
/* 101 */     this.progressListener = paramProgressListener;
/*     */   }
/*     */ 
/*     */   public synchronized void waitDecodingRequest()
/*     */   {
/*  76 */     while (!this.decodingAllowed)
/*     */       try {
/*  78 */         wait();
/*     */       }
/*     */       catch (InterruptedException localInterruptedException)
/*     */       {
/*     */       }
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.JimiDecodingController
 * JD-Core Version:    0.6.2
 */