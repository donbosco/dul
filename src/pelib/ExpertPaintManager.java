/*     */ package pelib;
/*     */ 
/*     */ public class ExpertPaintManager extends PaintManager
/*     */ {
/*     */   private Mask selectedMask;
/*     */   private int selectedColour;
/*     */   private int selectedBlur;
/*     */ 
/*     */   public ExpertPaintManager(PaintExplorer explorer)
/*     */   {
/*  17 */     super(explorer);
/*     */   }
/*     */ 
/*     */   public Mask getPaintedMask(Mask currentMask)
/*     */   {
/*  22 */     if (this.selectedMask != null)
/*     */     {
/*  24 */       if (currentMask == null) {
/*  25 */         return this.selectedMask;
/*     */       }
/*     */ 
/*  32 */       return currentMask;
/*     */     }
/*     */ 
/*  37 */     if (currentMask == null)
/*     */     {
/*  39 */       this.explorer.notifyMaskEvent(new PaintExplorerMaskEvent(this.explorer, 6, null));
/*     */ 
/*  43 */       return currentMask;
/*     */     }
/*     */ 
/*  48 */     this.selectedMask = currentMask;
/*  49 */     this.explorer.setMaskColour(this.selectedMask, this.selectedColour);
/*  50 */     this.explorer.setMaskBlur(this.selectedMask, this.selectedBlur);
/*  51 */     this.explorer.notifyMaskEvent(new PaintExplorerMaskEvent(this.explorer, 5, this.selectedMask));
/*     */ 
/*  56 */     return this.selectedMask;
/*     */   }
/*     */ 
/*     */   public boolean canUnpaint(Mask currentMask)
/*     */   {
/*  63 */     return true;
/*     */   }
/*     */ 
/*     */   public void setSelectedColour(int colour)
/*     */   {
/*  77 */     this.selectedColour = colour;
/*     */   }
/*     */ 
/*     */   public int getSelectedColour()
/*     */   {
/*  82 */     return this.selectedColour;
/*     */   }
/*     */ 
/*     */   public Mask getSelectedMask()
/*     */   {
/*  87 */     return this.selectedMask;
/*     */   }
/*     */ 
/*     */   public void setSelectedMask(Mask mask)
/*     */   {
/*  92 */     this.selectedMask = mask;
/*  93 */     this.explorer.notifyMaskEvent(new PaintExplorerMaskEvent(this.explorer, 5, this.selectedMask));
/*     */     try
/*     */     {
/*  98 */       setSelectedColour(this.selectedMask.getColour());
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setSelectedBlur(int blur) {
/* 106 */     this.selectedBlur = blur;
/* 107 */     if (this.selectedMask != null)
/* 108 */       this.explorer.setMaskBlur(this.selectedMask, this.selectedBlur);
/*     */   }
/*     */ 
/*     */   public boolean canSelectColour()
/*     */   {
/* 113 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean canSelectMask()
/*     */   {
/* 118 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean canSelectBlur()
/*     */   {
/* 123 */     return true;
/*     */   }
/*     */ 
/*     */   public Mask createMask()
/*     */   {
/* 134 */     this.selectedMask = this.explorer.createMask(this.selectedColour, null, 0);
/* 135 */     this.explorer.notifyMaskEvent(new PaintExplorerMaskEvent(this.explorer, 5, this.selectedMask));
/*     */ 
/* 139 */     return this.selectedMask;
/*     */   }
/*     */ 
/*     */   public void maskRemoved(Mask mask)
/*     */   {
/* 144 */     if (mask == this.selectedMask)
/* 145 */       setSelectedMask(null);
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.ExpertPaintManager
 * JD-Core Version:    0.6.2
 */