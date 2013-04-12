/*    */ package pelib;
/*    */ 
/*    */ public class MaskNameCommand extends Command
/*    */ {
/*    */   private Mask mask;
/*    */   private String name;
/*    */   private String oldName;
/*    */ 
/*    */   public MaskNameCommand(PaintExplorer explorer, Mask mask, String name)
/*    */   {
/* 16 */     super(explorer);
/* 17 */     this.mask = mask;
/* 18 */     this.name = name;
/*    */   }
/*    */ 
/*    */   public void execute(Area dirty)
/*    */   {
/* 23 */     this.oldName = ((String)this.mask.getUserData());
/* 24 */     this.mask.setUserData(this.name);
/* 25 */     dirty.bound(this.mask.getArea());
/*    */ 
/* 27 */     this.explorer.notifyMaskEvent(new PaintExplorerMaskEvent(this.explorer, 7, this.mask));
/*    */   }
/*    */ 
/*    */   public void undo(Area dirty)
/*    */   {
/* 35 */     this.mask.setUserData(this.oldName);
/* 36 */     dirty.bound(this.mask.getArea());
/*    */ 
/* 38 */     this.explorer.notifyMaskEvent(new PaintExplorerMaskEvent(this.explorer, 7, this.mask));
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.MaskNameCommand
 * JD-Core Version:    0.6.2
 */