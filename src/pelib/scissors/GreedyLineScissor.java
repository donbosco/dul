/*    */ package pelib.scissors;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import java.util.Vector;
/*    */ import pelib.Edge;
/*    */ import pelib.Node;
/*    */ 
/*    */ public class GreedyLineScissor
/*    */   implements ScissorAlgorithm
/*    */ {
/*    */   public Node addEdges(Node from, Node to, Vector edgeList, boolean permanent)
/*    */   {
/* 36 */     int x1 = from.x;
/* 37 */     int y1 = from.y;
/* 38 */     int x2 = to.x;
/* 39 */     int y2 = to.y;
/* 40 */     int lastDistance = (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1);
/* 41 */     while (from != to)
/*    */     {
/* 43 */       int bestLineDist = 2147483647;
/* 44 */       int bestDist = 2147483647;
/* 45 */       Node bestNode = null;
/* 46 */       Edge bestEdge = null;
/* 47 */       for (Iterator it = from.getEdges(); it.hasNext(); )
/*    */       {
/* 49 */         Edge e = (Edge)it.next();
/* 50 */         Node check = e.getNodeA();
/* 51 */         if (check == from) {
/* 52 */           check = e.getNodeB();
/*    */         }
/* 54 */         int dist = (check.x - x2) * (check.x - x2) + (check.y - y2) * (check.y - y2);
/* 55 */         if (dist < lastDistance)
/*    */         {
/* 58 */           int lineDist = Math.abs((x2 - x1) * (y1 - check.y) - (x1 - check.x) * (y2 - y1));
/*    */ 
/* 60 */           if (lineDist < bestLineDist)
/*    */           {
/* 62 */             bestLineDist = lineDist;
/* 63 */             bestDist = dist;
/* 64 */             bestEdge = e;
/* 65 */             bestNode = check;
/*    */           }
/*    */         }
/*    */       }
/* 69 */       if (bestEdge == null)
/*    */         break;
/* 71 */       edgeList.add(bestEdge);
/* 72 */       from = bestNode;
/* 73 */       lastDistance = bestDist;
/*    */     }
/*    */ 
/* 82 */     return from;
/*    */   }
/*    */ 
/*    */   public void reset()
/*    */   {
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.scissors.GreedyLineScissor
 * JD-Core Version:    0.6.2
 */