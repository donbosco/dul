/*    */ package pelib;
/*    */ 
/*    */ import java.util.Vector;
/*    */ 
/*    */ public class PaintExplorerScissorHistoryEvent extends PaintExplorerHistoryEvent
/*    */ {
/*    */   private boolean undone;
/*    */   private Vector edges;
/*    */ 
/*    */   public PaintExplorerScissorHistoryEvent(PaintExplorer source, boolean undone, Vector edges)
/*    */   {
/* 24 */     super(source);
/* 25 */     this.undone = undone;
/* 26 */     this.edges = edges;
/*    */   }
/*    */ 
/*    */   public boolean wasUndone()
/*    */   {
/* 31 */     return this.undone;
/*    */   }
/*    */ 
/*    */   public boolean wasRedone()
/*    */   {
/* 36 */     return !this.undone;
/*    */   }
/*    */ 
/*    */   public Vector getEdges()
/*    */   {
/* 45 */     return this.edges;
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.PaintExplorerScissorHistoryEvent
 * JD-Core Version:    0.6.2
 */