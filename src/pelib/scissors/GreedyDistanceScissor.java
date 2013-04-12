/*    */ package pelib.scissors;
/*    */ 
/*    */ import java.util.Vector;
/*    */ import pelib.Edge;
/*    */ import pelib.Node;
/*    */ 
/*    */ public class GreedyDistanceScissor
/*    */   implements ScissorAlgorithm
/*    */ {
/*    */   public Node addEdges(Node from, Node to, Vector edgeList, boolean permanent)
/*    */   {
/* 17 */     int limit = 500;
/* 18 */     while ((from != to) && (limit > 0))
/*    */     {
/* 20 */       Edge e = from.findNearestEdge(to.x, to.y);
/* 21 */       if (e == null) {
/*    */         break;
/*    */       }
/* 24 */       edgeList.add(e);
/*    */ 
/* 27 */       Node nextNode = e.getNodeA();
/* 28 */       if (nextNode == from) {
/* 29 */         nextNode = e.getNodeB();
/*    */       }
/* 31 */       from = nextNode;
/* 32 */       limit--;
/*    */     }
/*    */ 
/* 35 */     return from;
/*    */   }
/*    */ 
/*    */   public void reset()
/*    */   {
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.scissors.GreedyDistanceScissor
 * JD-Core Version:    0.6.2
 */