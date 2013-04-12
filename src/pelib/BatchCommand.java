/*    */ package pelib;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import java.util.Vector;
/*    */ 
/*    */ public class BatchCommand extends Command
/*    */ {
/*    */   private Vector commands;
/*    */ 
/*    */   public BatchCommand(PaintExplorer explorer)
/*    */   {
/* 17 */     super(explorer);
/* 18 */     this.commands = new Vector();
/*    */   }
/*    */ 
/*    */   public void add(Command cmd)
/*    */   {
/* 23 */     this.commands.add(cmd);
/*    */   }
/*    */ 
/*    */   public void execute(Area dirty)
/*    */   {
/* 28 */     for (Iterator it = this.commands.iterator(); it.hasNext(); )
/*    */     {
/* 30 */       Command cmd = (Command)it.next();
/* 31 */       cmd.execute(dirty);
/*    */     }
/*    */   }
/*    */ 
/*    */   public void undo(Area dirty)
/*    */   {
/* 37 */     for (int i = this.commands.size() - 1; i >= 0; i--)
/*    */     {
/* 39 */       Command cmd = (Command)this.commands.get(i);
/* 40 */       cmd.undo(dirty);
/*    */     }
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.BatchCommand
 * JD-Core Version:    0.6.2
 */