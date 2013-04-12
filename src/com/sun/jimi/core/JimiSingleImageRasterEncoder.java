/*     */ package com.sun.jimi.core;
/*     */ 
/*     */ import com.sun.jimi.core.raster.JimiRasterImage;
/*     */ import com.sun.jimi.core.util.JimiUtil;
/*     */ import com.sun.jimi.core.util.ProgressListener;
/*     */ import com.sun.jimi.core.util.ProgressMonitorSupport;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ 
/*     */ public abstract class JimiSingleImageRasterEncoder extends ProgressMonitorSupport
/*     */   implements JimiEncoder
/*     */ {
/*     */   public abstract void doEncodeImage(JimiRasterImage paramJimiRasterImage, OutputStream paramOutputStream)
/*     */     throws JimiException, IOException;
/*     */ 
/*     */   public final void encodeImage(JimiImage paramJimiImage, OutputStream paramOutputStream)
/*     */     throws JimiException
/*     */   {
/*     */     try
/*     */     {
/*  35 */       paramJimiImage.waitFinished();
/*  36 */       doEncodeImage(JimiUtil.asJimiRasterImage(paramJimiImage), paramOutputStream);
/*     */     }
/*     */     catch (IOException localIOException) {
/*  39 */       throw new JimiException(localIOException.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void encodeImage(JimiImage paramJimiImage, OutputStream paramOutputStream, ProgressListener paramProgressListener)
/*     */     throws JimiException
/*     */   {
/*  50 */     this.progressListener = paramProgressListener;
/*     */     try {
/*  52 */       paramJimiImage.waitFinished();
/*  53 */       this.progressListener.setStarted();
/*  54 */       doEncodeImage(JimiUtil.asJimiRasterImage(paramJimiImage), paramOutputStream);
/*  55 */       this.progressListener.setFinished();
/*     */     }
/*     */     catch (JimiException localJimiException) {
/*  58 */       this.progressListener.setAbort(localJimiException.getMessage());
/*  59 */       throw localJimiException;
/*     */     }
/*     */     catch (IOException localIOException) {
/*  62 */       this.progressListener.setAbort();
/*  63 */       throw new JimiException(localIOException.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void encodeImages(JimiImageEnumeration paramJimiImageEnumeration, OutputStream paramOutputStream)
/*     */     throws JimiException
/*     */   {
/*  82 */     encodeImage(paramJimiImageEnumeration.getNextImage(), paramOutputStream);
/*     */   }
/*     */ 
/*     */   public final void encodeImages(JimiImageEnumeration paramJimiImageEnumeration, OutputStream paramOutputStream, ProgressListener paramProgressListener)
/*     */     throws JimiException
/*     */   {
/*  93 */     encodeImage(paramJimiImageEnumeration.getNextImage(), paramOutputStream, paramProgressListener);
/*     */   }
/*     */ 
/*     */   public void setProgress(int paramInt)
/*     */   {
/* 102 */     super.setProgress(paramInt);
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.JimiSingleImageRasterEncoder
 * JD-Core Version:    0.6.2
 */