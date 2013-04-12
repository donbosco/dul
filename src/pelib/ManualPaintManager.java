/*     */ package pelib;
/*     */ 
/*     */ public class ManualPaintManager extends PaintManager
/*     */ {
/*     */   private Mask selectedMask;
/*     */ 
/*     */   public ManualPaintManager(PaintExplorer explorer)
/*     */   {
/*  16 */     super(explorer);
/*     */   }
/*     */ 
/*     */   public Mask getPaintedMask(Mask currentMask)
/*     */   {
/*  21 */     if (this.selectedMask == null) {
/*  22 */       return currentMask;
/*     */     }
/*  24 */     return this.selectedMask;
/*     */   }
/*     */ 
/*     */   public boolean canUnpaint(Mask currentMask)
/*     */   {
/*  29 */     return true;
/*     */   }
/*     */ 
/*     */   public void setSelectedColour(int colour)
/*     */   {
/*  34 */     if (this.selectedMask != null)
/*  35 */       this.explorer.setMaskColour(this.selectedMask, colour);
/*     */   }
/*     */ 
/*     */   public void setSelectedMask(Mask mask)
/*     */   {
/*  40 */     this.selectedMask = mask;
/*  41 */     this.explorer.notifyMaskEvent(new PaintExplorerMaskEvent(this.explorer, 5, mask));
/*     */   }
/*     */ 
/*     */   public void setSelectedBlur(int blur)
/*     */   {
/*  49 */     if (this.selectedMask != null)
/*  50 */       this.explorer.setMaskBlur(this.selectedMask, blur);
/*     */   }
/*     */ 
/*     */   public boolean canSelectColour()
/*     */   {
/*  60 */     return this.selectedMask != null;
/*     */   }
/*     */ 
/*     */   public boolean canSelectMask()
/*     */   {
/*  65 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean canSelectBlur()
/*     */   {
/*  70 */     return true;
/*     */   }
/*     */ 
/*     */   public int getSelectedColour()
/*     */   {
/*  80 */     return 0;
/*     */   }
/*     */ 
/*     */   public Mask getSelectedMask()
/*     */   {
/*  85 */     return this.selectedMask;
/*     */   }
/*     */ 
/*     */   public Mask createMask()
/*     */   {
/*  90 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean autoRemoveEmptyMasks()
/*     */   {
/*  95 */     return false;
/*     */   }
/*     */ 
/*     */   public void maskRemoved(Mask mask)
/*     */   {
/* 100 */     if (mask == this.selectedMask)
/* 101 */       setSelectedMask(null);
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.ManualPaintManager
 * JD-Core Version:    0.6.2
 */