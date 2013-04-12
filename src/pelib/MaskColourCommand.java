/*    */ package pelib;
/*    */ 
/*    */ public class MaskColourCommand extends Command
/*    */ {
/*    */   private Mask mask;
/*    */   private int colour;
/*    */   private int oldColour;
/*    */ 
/*    */   public MaskColourCommand(PaintExplorer explorer, Mask mask, int colour)
/*    */   {
/* 16 */     super(explorer);
/* 17 */     this.mask = mask;
/* 18 */     this.colour = colour;
/*    */   }
/*    */ 
/*    */   public void execute(Area dirty)
/*    */   {
/* 23 */     this.oldColour = this.mask.getColour();
/* 24 */     this.mask.setColour(this.colour);
/* 25 */     dirty.bound(this.mask.getArea());
/*    */ 
/* 27 */     this.explorer.notifyMaskEvent(new PaintExplorerMaskEvent(this.explorer, 3, this.mask));
/*    */   }
/*    */ 
/*    */   public void undo(Area dirty)
/*    */   {
/* 35 */     this.mask.setColour(this.oldColour);
/* 36 */     dirty.bound(this.mask.getArea());
/*    */ 
/* 38 */     this.explorer.notifyMaskEvent(new PaintExplorerMaskEvent(this.explorer, 3, this.mask));
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.MaskColourCommand
 * JD-Core Version:    0.6.2
 */