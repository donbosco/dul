/*     */ package com.sun.jimi.core;
/*     */ 
/*     */ import com.sun.jimi.core.raster.MutableJimiRasterImage;
/*     */ import com.sun.jimi.core.util.ProgressListener;
/*     */ import com.sun.jimi.core.util.ProgressMonitorSupport;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ 
/*     */ public abstract class JimiMultiImageRasterDecoder extends JimiRasterDecoderSupport
/*     */ {
/*     */   protected JimiDecodingController currentDecodingController;
/*     */   protected JimiImageHandle currentHandle;
/*     */   protected MImageSeriesDecodingController controller;
/*     */   protected boolean error;
/*  35 */   protected boolean busy = false;
/*     */ 
/*     */   public abstract MutableJimiRasterImage doInitNextDecoding(JimiImageFactory paramJimiImageFactory, InputStream paramInputStream)
/*     */     throws JimiException, IOException;
/*     */ 
/*     */   public abstract void doNextImageDecode()
/*     */     throws JimiException, IOException;
/*     */ 
/*     */   public abstract void doSkipNextImage()
/*     */     throws JimiException, IOException;
/*     */ 
/*     */   public void driveDecoding()
/*     */   {
/*     */     try
/*     */     {
/*  67 */       if (this.progressListener != null) {
/*  68 */         this.progressListener.setStarted();
/*     */       }
/*  70 */       setJimiImage(doInitNextDecoding(getJimiImageFactory(), getInput()));
/*  71 */       doNextImageDecode();
/*  72 */       if (this.progressListener != null)
/*  73 */         this.progressListener.setFinished();
/*     */     }
/*     */     catch (JimiException localJimiException)
/*     */     {
/*  77 */       if (this.progressListener != null)
/*  78 */         this.progressListener.setAbort(localJimiException.getMessage());
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/*  82 */       if (this.progressListener != null)
/*  83 */         this.progressListener.setAbort();
/*     */     }
/*     */     finally
/*     */     {
/*  87 */       this.progressListener = null;
/*  88 */       setBusy(false);
/*  89 */       if (this.noMoreRequests)
/*  90 */         cleanup();
/*     */     }
/*     */   }
/*     */ 
/*     */   public InputStream getInput()
/*     */   {
/* 151 */     return this.input;
/*     */   }
/*     */ 
/*     */   public JimiImageFactory getJimiImageFactory()
/*     */   {
/* 134 */     return this.factory;
/*     */   }
/*     */ 
/*     */   public abstract boolean hasMoreImages();
/*     */ 
/*     */   public ImageSeriesDecodingController initDecoding(JimiImageFactory paramJimiImageFactory, InputStream paramInputStream)
/*     */   {
/*  39 */     super.init(paramJimiImageFactory, paramInputStream);
/*  40 */     this.controller = new MImageSeriesDecodingController(this);
/*     */ 
/*  42 */     return this.controller;
/*     */   }
/*     */ 
/*     */   protected JimiDecodingController initNextDecoding()
/*     */   {
/*  50 */     waitReady();
/*  51 */     this.currentHandle = new JimiImageHandle();
/*  52 */     this.currentDecodingController = new JimiDecodingController(this.currentHandle);
/*     */ 
/*  54 */     JimiMultiImageRasterDecoderRunner localJimiMultiImageRasterDecoderRunner = new JimiMultiImageRasterDecoderRunner(this);
/*     */ 
/*  56 */     new Thread(localJimiMultiImageRasterDecoderRunner).start();
/*     */ 
/*  58 */     return this.currentDecodingController;
/*     */   }
/*     */ 
/*     */   protected synchronized void setBusy(boolean paramBoolean)
/*     */   {
/* 156 */     this.busy = paramBoolean;
/* 157 */     notifyAll();
/*     */   }
/*     */ 
/*     */   protected synchronized void setJimiImage(MutableJimiRasterImage paramMutableJimiRasterImage)
/*     */   {
/* 124 */     paramMutableJimiRasterImage.setDecodingController(this.currentDecodingController);
/* 125 */     this.currentHandle.setJimiImage(paramMutableJimiRasterImage);
/* 126 */     this.currentDecodingController.waitDecodingRequest();
/*     */   }
/*     */ 
/*     */   public void setProgress(int paramInt)
/*     */   {
/* 143 */     super.setProgress(paramInt);
/*     */   }
/*     */ 
/*     */   protected synchronized void waitReady()
/*     */   {
/* 162 */     while (this.busy) try {
/* 163 */         wait();
/*     */       }
/*     */       catch (InterruptedException localInterruptedException)
/*     */       {
/*     */       }
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.JimiMultiImageRasterDecoder
 * JD-Core Version:    0.6.2
 */