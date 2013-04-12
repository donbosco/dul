/*    */ package pelib.scissors;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import java.util.LinkedList;
/*    */ import java.util.Vector;
/*    */ import pelib.Edge;
/*    */ import pelib.Node;
/*    */ import pelib.PaintExplorer;
/*    */ 
/*    */ public class Dijkstra2Scissor
/*    */   implements ScissorAlgorithm
/*    */ {
/*    */   private NodePriorityQueue priority;
/*    */   private PaintExplorer explorer;
/*    */   private Vector markedNodes;
/*    */ 
/*    */   public Dijkstra2Scissor(PaintExplorer explorer)
/*    */   {
/* 19 */     this.priority = new NodePriorityQueue();
/* 20 */     this.explorer = explorer;
/* 21 */     this.markedNodes = new Vector();
/*    */   }
/*    */ 
/*    */   public Node addEdges(Node from, Node to, Vector edgeList, boolean permanent) {
/* 27 */     this.priority.clear();
/*    */ 
/* 29 */     from.leadingEdge = null;
/* 30 */     from.cost = 0.0F;
/* 31 */     this.priority.add(from);
/*    */     Node current;
/*    */     while (true) {
/* 36 */       current = this.priority.first();
/* 37 */       if (current == null) {
/*    */         break;
/*    */       }
/* 40 */       if (current == to) {
/*    */         break;
/*    */       }
/* 43 */       current.marked = true;
/* 44 */       this.markedNodes.add(current);
/*    */ 
/* 46 */       for (int i = 0; i < current.getEdgeCount(); i++)
/*    */       {
/* 48 */         Edge edge = current.edges[i];
/* 49 */         Node other = edge.getNodeA();
/* 50 */         if (other == current) {
/* 51 */           other = edge.getNodeB();
/*    */         }
/* 53 */         if (!other.marked)
/*    */         {
/* 57 */           if (!this.priority.remove(other)) {
/* 58 */             other.cost = 3.4028235E+38F;
/*    */           }
/* 60 */           float newCost = current.cost + edge.getCost();
/* 61 */           if (other.cost > newCost)
/*    */           {
/* 63 */             other.cost = newCost;
/* 64 */             other.leadingEdge = edge;
/*    */           }
/*    */ 
/* 68 */           this.priority.add(other);
/*    */         }
/*    */       }
/*    */     }
/*    */ 
/* 73 */     for (Iterator it = this.markedNodes.iterator(); it.hasNext(); )
/*    */     {
/* 75 */       ((Node)it.next()).marked = false;
/*    */     }
/* 77 */     this.markedNodes.clear();
/*    */ 
/* 80 */     if (current != null)
/*    */     {
/* 82 */       LinkedList reverseEdges = new LinkedList();
/* 83 */       while (current.leadingEdge != null)
/*    */       {
/* 85 */         reverseEdges.addFirst(current.leadingEdge);
/* 86 */         Node n = current.leadingEdge.getNodeA();
/* 87 */         if (n == current)
/* 88 */           n = current.leadingEdge.getNodeB();
/* 89 */         current = n;
/*    */       }
/* 91 */       edgeList.addAll(reverseEdges);
/* 92 */       return to;
/*    */     }
/*    */ 
/* 97 */     return from;
/*    */   }
/*    */ 
/*    */   public void reset()
/*    */   {
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.scissors.Dijkstra2Scissor
 * JD-Core Version:    0.6.2
 */