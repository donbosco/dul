/*    */ package pelib;
/*    */ 
/*    */ public class PaintCommand extends Command
/*    */ {
/*    */   private Mask mask;
/*    */   private int labelIndex;
/*    */   private Mask oldMask;
/*    */ 
/*    */   public PaintCommand(PaintExplorer explorer, Mask mask, int labelIndex)
/*    */   {
/* 16 */     super(explorer);
/* 17 */     this.mask = mask;
/*    */ 
/* 19 */     this.labelIndex = labelIndex;
/*    */   }
/*    */ 
/*    */   public Mask getMask()
/*    */   {
/* 24 */     return this.mask;
/*    */   }
/*    */ 
/*    */   public int getLabelIndex()
/*    */   {
/* 29 */     return this.labelIndex;
/*    */   }
/*    */ 
/*    */   public Mask getOldMask()
/*    */   {
/* 34 */     return this.oldMask;
/*    */   }
/*    */ 
/*    */   public void execute(Area dirty)
/*    */   {
/* 39 */     SuperRegion region = this.explorer.getRegionForIndex(this.labelIndex);
/* 40 */     this.oldMask = region.getMask();
/* 41 */     if (this.oldMask != this.mask)
/*    */     {
/* 43 */       if (this.oldMask != null)
/*    */       {
/* 45 */         this.oldMask.removeRegion(region);
/* 46 */         if (this.oldMask.mustRepaintAll())
/* 47 */           dirty.bound(this.oldMask.getArea());
/*    */       }
/* 49 */       region.setMask(this.mask);
/* 50 */       if (this.mask != null)
/*    */       {
/* 52 */         this.mask.addRegion(region);
/* 53 */         if (this.mask.mustRepaintAll())
/* 54 */           dirty.bound(this.mask.getArea());
/*    */       }
/* 56 */       dirty.bound(region.getArea());
/*    */     }
/*    */   }
/*    */ 
/*    */   public void undo(Area dirty)
/*    */   {
/* 62 */     SuperRegion region = this.explorer.getRegionForIndex(this.labelIndex);
/* 63 */     if (this.oldMask != this.mask)
/*    */     {
/* 65 */       if (this.mask != null)
/*    */       {
/* 67 */         this.mask.removeRegion(region);
/* 68 */         if (this.mask.mustRepaintAll()) {
/* 69 */           dirty.bound(this.mask.getArea());
/*    */         }
/*    */       }
/* 72 */       region.setMask(this.oldMask);
/* 73 */       if (this.oldMask != null)
/*    */       {
/* 75 */         this.oldMask.addRegion(region);
/* 76 */         if (this.oldMask.mustRepaintAll()) {
/* 77 */           dirty.bound(this.oldMask.getArea());
/*    */         }
/*    */       }
/* 80 */       dirty.bound(region.getArea());
/*    */     }
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.PaintCommand
 * JD-Core Version:    0.6.2
 */