/*     */ package pelib;
/*     */ 
/*     */ public class ThreadWrapper
/*     */   implements PaintExplorerListener
/*     */ {
/*     */   private PaintExplorer explorer;
/*     */   private boolean busy;
/*     */   private Throwable lastException;
/*     */   private PaintExplorerProgressEvent lastProgress;
/*     */ 
/*     */   public ThreadWrapper(PaintExplorer explorer)
/*     */   {
/*  33 */     this.explorer = explorer;
/*     */ 
/*  35 */     this.busy = false;
/*     */ 
/*  37 */     explorer.addListener(this);
/*     */   }
/*     */ 
/*     */   public void load(String filename)
/*     */   {
/*  49 */     this.busy = true;
/*     */ 
/*  51 */     this.lastException = null;
/*     */ 
/*  53 */     this.lastProgress = null;
/*     */ 
/*  55 */     new LoadThread(filename).start();
/*     */   }
/*     */ 
/*     */   public boolean isBusy()
/*     */   {
/* 107 */     return this.busy;
/*     */   }
/*     */ 
/*     */   public Throwable getException()
/*     */   {
/* 117 */     Throwable e = this.lastException;
/*     */ 
/* 119 */     this.lastException = null;
/*     */ 
/* 121 */     return e;
/*     */   }
/*     */ 
/*     */   public PaintExplorerProgressEvent getProgress()
/*     */   {
/* 131 */     PaintExplorerProgressEvent ev = this.lastProgress;
/*     */ 
/* 133 */     this.lastProgress = null;
/*     */ 
/* 135 */     return ev;
/*     */   }
/*     */ 
/*     */   public void onHistoryEvent(PaintExplorerHistoryEvent e)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void onMaskEvent(PaintExplorerMaskEvent e)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void onProgress(PaintExplorerProgressEvent e)
/*     */   {
/* 149 */     this.lastProgress = e;
/*     */   }
/*     */ 
/*     */   public void onScissorsEvent(PaintExplorerScissorsEvent e)
/*     */   {
/*     */   }
/*     */ 
/*     */   private class LoadThread extends Thread
/*     */   {
/*     */     private String filename;
/*     */ 
/*     */     public LoadThread(String filename)
/*     */     {
/*  73 */       this.filename = filename;
/*     */     }
/*     */ 
/*     */     public void run()
/*     */     {
/*     */       try
/*     */       {
/*  85 */         ThreadWrapper.this.explorer.load(this.filename);
/*     */       }
/*     */       catch (Throwable e)
/*     */       {
/*  89 */         ThreadWrapper.this.lastException = e;
/*     */       }
/*     */ 
/*  95 */       ThreadWrapper.this.busy = false;
/*     */     }
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.ThreadWrapper
 * JD-Core Version:    0.6.2
 */