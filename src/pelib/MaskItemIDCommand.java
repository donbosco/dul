/*    */ package pelib;
/*    */ 
/*    */ public class MaskItemIDCommand extends Command
/*    */ {
/*    */   private Mask mask;
/*    */   private int itemId;
/*    */   private int oldItemId;
/*    */ 
/*    */   public MaskItemIDCommand(PaintExplorer explorer, Mask mask, int itemId)
/*    */   {
/* 16 */     super(explorer);
/* 17 */     this.mask = mask;
/* 18 */     this.itemId = itemId;
/*    */   }
/*    */ 
/*    */   public void execute(Area dirty)
/*    */   {
/* 23 */     this.oldItemId = this.mask.getItemId();
/* 24 */     this.mask.setItemId(this.itemId);
/* 25 */     dirty.bound(this.mask.getArea());
/* 26 */     this.explorer.notifyMaskEvent(new PaintExplorerMaskEvent(this.explorer, 8, this.mask));
/*    */   }
/*    */ 
/*    */   public void undo(Area dirty)
/*    */   {
/* 34 */     this.mask.setItemId(this.oldItemId);
/* 35 */     dirty.bound(this.mask.getArea());
/*    */ 
/* 37 */     this.explorer.notifyMaskEvent(new PaintExplorerMaskEvent(this.explorer, 8, this.mask));
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.MaskItemIDCommand
 * JD-Core Version:    0.6.2
 */