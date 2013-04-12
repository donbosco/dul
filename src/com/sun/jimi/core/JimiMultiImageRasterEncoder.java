/*    */ package com.sun.jimi.core;
/*    */ 
/*    */ import com.sun.jimi.core.util.ProgressListener;
/*    */ import com.sun.jimi.core.util.ProgressMonitorSupport;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ 
/*    */ public abstract class JimiMultiImageRasterEncoder extends JimiRasterEncoderSupport
/*    */ {
/*    */   public abstract void doEncodeImages(JimiRasterImageEnumeration paramJimiRasterImageEnumeration, OutputStream paramOutputStream)
/*    */     throws JimiException, IOException;
/*    */ 
/*    */   public final void encodeImages(JimiImageEnumeration paramJimiImageEnumeration, OutputStream paramOutputStream)
/*    */     throws JimiException
/*    */   {
/*    */     try
/*    */     {
/* 36 */       this.progressListener.setStarted();
/* 37 */       doEncodeImages(new JimiRasterImageEnumeration(paramJimiImageEnumeration), paramOutputStream);
/* 38 */       this.progressListener.setFinished();
/*    */     }
/*    */     catch (JimiException localJimiException) {
/* 41 */       this.progressListener.setAbort(localJimiException.getMessage());
/* 42 */       throw localJimiException;
/*    */     }
/*    */     catch (IOException localIOException) {
/* 45 */       this.progressListener.setAbort();
/* 46 */       throw new JimiException(localIOException.toString());
/*    */     }
/*    */   }
/*    */ 
/*    */   public final void encodeImages(JimiImageEnumeration paramJimiImageEnumeration, OutputStream paramOutputStream, ProgressListener paramProgressListener)
/*    */     throws JimiException
/*    */   {
/* 57 */     setProgressListener(paramProgressListener);
/* 58 */     encodeImages(paramJimiImageEnumeration, paramOutputStream);
/*    */   }
/*    */ 
/*    */   public void setProgress(int paramInt)
/*    */   {
/* 76 */     super.setProgress(paramInt);
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.JimiMultiImageRasterEncoder
 * JD-Core Version:    0.6.2
 */