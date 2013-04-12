/*    */ package pelib;
/*    */ 
/*    */ public abstract class PaintExplorerEvent
/*    */ {
/*    */   protected PaintExplorer source;
/*    */ 
/*    */   protected PaintExplorerEvent(PaintExplorer source)
/*    */   {
/* 14 */     this.source = source;
/*    */   }
/*    */ 
/*    */   public PaintExplorer getSource()
/*    */   {
/* 19 */     return this.source;
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.PaintExplorerEvent
 * JD-Core Version:    0.6.2
 */