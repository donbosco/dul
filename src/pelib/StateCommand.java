/*    */ package pelib;
/*    */ 
/*    */ public class StateCommand extends Command
/*    */ {
/*    */   private Memento before;
/*    */   private Memento after;
/*    */ 
/*    */   public StateCommand(PaintExplorer explorer, Memento memento)
/*    */   {
/* 16 */     super(explorer);
/* 17 */     this.before = memento;
/*    */   }
/*    */ 
/*    */   public void execute(Area dirty)
/*    */   {
/* 25 */     assert (this.after != null);
/* 26 */     this.explorer.restoreMemento(this.after);
/*    */   }
/*    */ 
/*    */   public void undo(Area dirty)
/*    */   {
/* 31 */     if (this.after == null) {
/* 32 */       this.after = this.explorer.saveMemento();
/*    */     }
/* 34 */     this.explorer.restoreMemento(this.before);
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.StateCommand
 * JD-Core Version:    0.6.2
 */