/*    */ package pelib;
/*    */ 
/*    */ public abstract class Command
/*    */ {
/*    */   protected PaintExplorer explorer;
/*    */ 
/*    */   protected Command(PaintExplorer explorer)
/*    */   {
/* 13 */     this.explorer = explorer;
/*    */   }
/*    */ 
/*    */   public abstract void execute(Area paramArea);
/*    */ 
/*    */   public abstract void undo(Area paramArea);
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.Command
 * JD-Core Version:    0.6.2
 */