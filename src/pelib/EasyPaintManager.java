/*     */ package pelib;
/*     */ 
/*     */ public class EasyPaintManager extends PaintManager
/*     */ {
/*     */   private int selectedColour;
/*     */   private Mask selectedMask;
/*     */ 
/*     */   public EasyPaintManager(PaintExplorer explorer)
/*     */   {
/*  17 */     super(explorer);
/*     */   }
/*     */ 
/*     */   public Mask getPaintedMask(Mask currentMask)
/*     */   {
/*  22 */     if (currentMask == null)
/*     */     {
/*  24 */       if (this.selectedMask == null)
/*  25 */         this.selectedMask = this.explorer.createMask(this.selectedColour, null, 0);
/*  26 */       return this.selectedMask;
/*     */     }
/*     */ 
/*  30 */     if (this.selectedMask == null)
/*     */     {
/*  32 */       this.explorer.setMaskColour(currentMask, this.selectedColour);
/*  33 */       this.selectedMask = currentMask;
/*     */     }
/*  35 */     else if (this.selectedMask != currentMask) {
/*  36 */       this.explorer.mergeMask(this.selectedMask, currentMask);
/*     */     }
/*  38 */     return this.selectedMask;
/*     */   }
/*     */ 
/*     */   public boolean canUnpaint(Mask currentMask)
/*     */   {
/*  44 */     return true;
/*     */   }
/*     */ 
/*     */   public void setSelectedColour(int colour)
/*     */   {
/*  49 */     this.selectedColour = colour;
/*  50 */     this.selectedMask = null;
/*     */   }
/*     */ 
/*     */   public void setSelectedMask(Mask mask)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void setSelectedBlur(int blur)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void setCanAddMask(boolean canAddMask)
/*     */   {
/*     */   }
/*     */ 
/*     */   public boolean canSelectColour()
/*     */   {
/*  70 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean canSelectMask()
/*     */   {
/*  75 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean canSelectBlur()
/*     */   {
/*  80 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean canAddMask()
/*     */   {
/*  85 */     return true;
/*     */   }
/*     */ 
/*     */   public int getSelectedColour()
/*     */   {
/*  90 */     return this.selectedColour;
/*     */   }
/*     */ 
/*     */   public Mask getSelectedMask()
/*     */   {
/*  95 */     return this.selectedMask;
/*     */   }
/*     */ 
/*     */   public Mask createMask()
/*     */   {
/* 100 */     return null;
/*     */   }
/*     */ 
/*     */   public void maskRemoved(Mask mask)
/*     */   {
/* 105 */     if (mask == this.selectedMask)
/* 106 */       this.selectedMask = null;
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.EasyPaintManager
 * JD-Core Version:    0.6.2
 */