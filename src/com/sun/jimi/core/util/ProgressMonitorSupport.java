/*    */ package com.sun.jimi.core.util;
/*    */ 
/*    */ public class ProgressMonitorSupport
/*    */ {
/*    */   protected ProgressListener progressListener;
/* 25 */   protected int currentProgressLevel = -1;
/*    */ 
/*    */   public void setProgress(int paramInt)
/*    */   {
/* 42 */     if ((this.progressListener != null) && (paramInt > this.currentProgressLevel)) {
/* 43 */       this.progressListener.setProgressLevel(paramInt);
/* 44 */       this.currentProgressLevel = paramInt;
/*    */     }
/*    */   }
/*    */ 
/*    */   public void setProgressListener(ProgressListener paramProgressListener)
/*    */   {
/* 33 */     this.progressListener = paramProgressListener;
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.util.ProgressMonitorSupport
 * JD-Core Version:    0.6.2
 */