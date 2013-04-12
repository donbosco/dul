/*    */ package pelib;
/*    */ 
/*    */ public class MaskBlurCommand extends Command
/*    */ {
/*    */   private Mask mask;
/*    */   private int blur;
/*    */   private int oldBlur;
/*    */ 
/*    */   public MaskBlurCommand(PaintExplorer explorer, Mask mask, int blur)
/*    */   {
/* 16 */     super(explorer);
/* 17 */     this.mask = mask;
/* 18 */     this.blur = blur;
/*    */   }
/*    */ 
/*    */   public void execute(Area dirty)
/*    */   {
/* 23 */     this.oldBlur = this.mask.getBlurLevel();
/* 24 */     this.mask.setBlurLevel(this.blur);
/* 25 */     dirty.bound(this.mask.getArea());
/*    */ 
/* 27 */     this.explorer.getBlurManager().reference(this.blur);
/* 28 */     this.explorer.getBlurManager().dereference(this.oldBlur);
/*    */ 
/* 30 */     this.explorer.notifyMaskEvent(new PaintExplorerMaskEvent(this.explorer, 4, this.mask));
/*    */   }
/*    */ 
/*    */   public void undo(Area dirty)
/*    */   {
/* 38 */     this.mask.setBlurLevel(this.oldBlur);
/* 39 */     dirty.bound(this.mask.getArea());
/*    */ 
/* 41 */     this.explorer.getBlurManager().reference(this.oldBlur);
/* 42 */     this.explorer.getBlurManager().dereference(this.blur);
/*    */ 
/* 44 */     this.explorer.notifyMaskEvent(new PaintExplorerMaskEvent(this.explorer, 4, this.mask));
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.MaskBlurCommand
 * JD-Core Version:    0.6.2
 */