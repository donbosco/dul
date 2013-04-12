/*    */ package pelib;
/*    */ 
/*    */ public class AddMaskCommand extends Command
/*    */ {
/*    */   private Mask mask;
/*    */ 
/*    */   public AddMaskCommand(PaintExplorer explorer, Mask mask)
/*    */   {
/* 14 */     super(explorer);
/* 15 */     assert (mask != null);
/* 16 */     this.mask = mask;
/*    */   }
/*    */ 
/*    */   public void execute(Area dirty)
/*    */   {
/* 21 */     this.explorer.addMask(this.mask);
/*    */   }
/*    */ 
/*    */   public void undo(Area dirty)
/*    */   {
/* 26 */     this.explorer.removeMask(this.mask);
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.AddMaskCommand
 * JD-Core Version:    0.6.2
 */