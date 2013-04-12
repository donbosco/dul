/*    */ package pelib;
/*    */ 
/*    */ import java.util.Vector;
/*    */ 
/*    */ public class ScissorsStateCommand extends StateCommand
/*    */ {
/*    */   private Vector cutEdges;
/*    */ 
/*    */   public ScissorsStateCommand(PaintExplorer explorer, Memento memento, Vector cutEdges)
/*    */   {
/* 17 */     super(explorer, memento);
/* 18 */     this.cutEdges = cutEdges;
/*    */   }
/*    */ 
/*    */   public void execute(Area dirty)
/*    */   {
/* 23 */     super.execute(dirty);
/* 24 */     this.explorer.notifyHistoryEvent(new PaintExplorerScissorHistoryEvent(this.explorer, false, this.cutEdges));
/*    */   }
/*    */ 
/*    */   public void undo(Area dirty)
/*    */   {
/* 30 */     super.undo(dirty);
/* 31 */     this.explorer.notifyHistoryEvent(new PaintExplorerScissorHistoryEvent(this.explorer, true, this.cutEdges));
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.ScissorsStateCommand
 * JD-Core Version:    0.6.2
 */