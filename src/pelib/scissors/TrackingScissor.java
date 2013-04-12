/*    */ package pelib.scissors;
/*    */ 
/*    */ import java.util.Vector;
/*    */ import pelib.Node;
/*    */ 
/*    */ public class TrackingScissor
/*    */   implements ScissorAlgorithm
/*    */ {
/*    */   private Vector edges;
/*    */   private ScissorAlgorithm underlying;
/*    */   private Node lastNode;
/*    */ 
/*    */   public TrackingScissor()
/*    */   {
/* 19 */     this.edges = new Vector();
/* 20 */     this.underlying = new GreedyDistanceScissor();
/*    */   }
/*    */ 
/*    */   public void reset()
/*    */   {
/* 25 */     this.edges.clear();
/* 26 */     this.lastNode = null;
/*    */   }
/*    */ 
/*    */   public Node addEdges(Node from, Node to, Vector edgeList, boolean permanent)
/*    */   {
/* 33 */     if (this.lastNode != null) {
/* 34 */       from = this.lastNode;
/*    */     }
/* 36 */     this.lastNode = this.underlying.addEdges(from, to, this.edges, false);
/* 37 */     edgeList.addAll(this.edges);
/*    */ 
/* 39 */     if (permanent) {
/* 40 */       this.edges.clear();
/*    */     }
/* 42 */     return this.lastNode;
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.scissors.TrackingScissor
 * JD-Core Version:    0.6.2
 */