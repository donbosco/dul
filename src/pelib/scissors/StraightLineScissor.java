/*    */ package pelib.scissors;
/*    */ 
/*    */ import java.util.Vector;
/*    */ import pelib.Node;
/*    */ import pelib.StraightLineEdge;
/*    */ 
/*    */ public class StraightLineScissor
/*    */   implements ScissorAlgorithm
/*    */ {
/*    */   public Node addEdges(Node from, Node to, Vector edgeList, boolean permanent)
/*    */   {
/* 20 */     StraightLineEdge newEdge = new StraightLineEdge(from, to);
/* 21 */     edgeList.add(newEdge);
/*    */ 
/* 23 */     return to;
/*    */   }
/*    */ 
/*    */   public void reset()
/*    */   {
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.scissors.StraightLineScissor
 * JD-Core Version:    0.6.2
 */