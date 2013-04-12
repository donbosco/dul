/*     */ package com.sun.jimi.core;
/*     */ 
/*     */ import com.sun.jimi.core.raster.MutableJimiRasterImage;
/*     */ import com.sun.jimi.core.util.ProgressListener;
/*     */ import com.sun.jimi.core.util.ProgressMonitorSupport;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ 
/*     */ public abstract class JimiSingleImageRasterDecoder extends JimiRasterDecoderSupport
/*     */ {
/*     */   private JimiDecodingController controller;
/*     */   private JimiImageHandle handle;
/*     */   private boolean error;
/*     */ 
/*     */   public abstract void doImageDecode()
/*     */     throws JimiException, IOException;
/*     */ 
/*     */   public abstract MutableJimiRasterImage doInitDecoding(JimiImageFactory paramJimiImageFactory, InputStream paramInputStream)
/*     */     throws JimiException, IOException;
/*     */ 
/*     */   public void driveDecoding()
/*     */   {
/*     */     try
/*     */     {
/*  55 */       if (this.progressListener != null) {
/*  56 */         this.progressListener.setStarted();
/*     */       }
/*  58 */       setJimiImage(doInitDecoding(getJimiImageFactory(), getInput()));
/*  59 */       doImageDecode();
/*  60 */       if (this.progressListener != null)
/*  61 */         this.progressListener.setFinished();
/*     */     }
/*     */     catch (JimiException localJimiException)
/*     */     {
/*  65 */       if (this.progressListener != null)
/*  66 */         this.progressListener.setAbort(localJimiException.getMessage());
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/*  70 */       if (this.progressListener != null) {
/*  71 */         this.progressListener.setAbort();
/*     */       }
/*     */     }
/*  74 */     cleanup();
/*     */   }
/*     */ 
/*     */   public InputStream getInput()
/*     */   {
/* 125 */     return this.input;
/*     */   }
/*     */ 
/*     */   public JimiImageFactory getJimiImageFactory()
/*     */   {
/* 117 */     return this.factory;
/*     */   }
/*     */ 
/*     */   public ImageSeriesDecodingController initDecoding(JimiImageFactory paramJimiImageFactory, InputStream paramInputStream)
/*     */   {
/*  35 */     super.init(paramJimiImageFactory, paramInputStream);
/*     */ 
/*  37 */     this.handle = new JimiImageHandle();
/*  38 */     this.controller = new JimiDecodingController(this.handle);
/*     */ 
/*  41 */     Runnable local1 = new Runnable() {
/*     */       public void run() {
/*  43 */         JimiSingleImageRasterDecoder.this.driveDecoding();
/*     */       }
/*     */     };
/*  46 */     new Thread(local1).start();
/*     */ 
/*  49 */     return new SingleImageSeriesDecodingController(this.controller);
/*     */   }
/*     */ 
/*     */   private synchronized void setJimiImage(MutableJimiRasterImage paramMutableJimiRasterImage)
/*     */   {
/* 107 */     paramMutableJimiRasterImage.setDecodingController(this.controller);
/* 108 */     this.handle.setJimiImage(paramMutableJimiRasterImage);
/* 109 */     this.controller.waitDecodingRequest();
/*     */   }
/*     */ 
/*     */   public void setProgress(int paramInt)
/*     */   {
/* 100 */     if (this.controller.hasProgressListener())
/* 101 */       this.controller.getProgressListener().setProgressLevel(paramInt);
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.JimiSingleImageRasterDecoder
 * JD-Core Version:    0.6.2
 */