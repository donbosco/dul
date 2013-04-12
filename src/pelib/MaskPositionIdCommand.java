/*    */ package pelib;
/*    */ 
/*    */ public class MaskPositionIdCommand extends Command
/*    */ {
/*    */   private Mask mask;
/*    */   private int positionID;
/*    */   private int oldPositionID;
/*    */ 
/*    */   public MaskPositionIdCommand(PaintExplorer explorer, Mask mask, int positionID)
/*    */   {
/* 16 */     super(explorer);
/* 17 */     this.mask = mask;
/* 18 */     this.positionID = positionID;
/*    */   }
/*    */ 
/*    */   public void execute(Area dirty)
/*    */   {
/* 23 */     this.oldPositionID = this.mask.getPositionID();
/* 24 */     this.mask.setPositionID(this.positionID);
/* 25 */     dirty.bound(this.mask.getArea());
/*    */ 
/* 27 */     this.explorer.notifyMaskEvent(new PaintExplorerMaskEvent(this.explorer, 9, this.mask));
/*    */   }
/*    */ 
/*    */   public void undo(Area dirty)
/*    */   {
/* 35 */     this.mask.setPositionID(this.oldPositionID);
/* 36 */     dirty.bound(this.mask.getArea());
/*    */ 
/* 38 */     this.explorer.notifyMaskEvent(new PaintExplorerMaskEvent(this.explorer, 8, this.mask));
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.MaskPositionIdCommand
 * JD-Core Version:    0.6.2
 */