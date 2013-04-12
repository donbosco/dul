/*     */ package pelib.scissors;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Map;
/*     */ import java.util.Vector;
/*     */ import pelib.Edge;
/*     */ import pelib.Node;
/*     */ 
/*     */ public class Dijkstra1Scissor
/*     */   implements ScissorAlgorithm
/*     */ {
/*     */   private DijkstraPriorityQueue priority;
/*     */   private Map open;
/*     */   private Map closed;
/*     */ 
/*     */   public Dijkstra1Scissor()
/*     */   {
/*  19 */     this.priority = new DijkstraPriorityQueue();
/*  20 */     this.closed = new HashMap();
/*  21 */     this.open = new HashMap();
/*     */   }
/*     */   public Node addEdges(Node from, Node to, Vector edgeList, boolean permanent) {
/*  27 */     this.priority.clear();
/*  28 */     this.closed.clear();
/*  29 */     this.open.clear();
/*     */ 
/*  31 */     DijkstraNode firstNode = new DijkstraNode(from, 0.0F, null, null);
/*  32 */     this.open.put(from, firstNode);
/*  33 */     this.priority.add(firstNode);
/*     */     DijkstraNode current;
/*     */     while (true) { current = this.priority.first();
/*     */ 
/*  41 */       if (current == null)
/*     */       {
/*  43 */         return from;
/*     */       }
/*     */ 
/*  47 */       if (current.node == to)
/*     */       {
/*     */         break;
/*     */       }
/*  51 */       this.open.remove(current.node);
/*  52 */       this.closed.put(current.node, current);
/*     */ 
/*  55 */       for (int i = 0; i < current.node.getEdgeCount(); i++)
/*     */       {
/*  57 */         Edge edge = current.node.edges[i];
/*  58 */         Node other = edge.getNodeA();
/*  59 */         if (other == current.node) {
/*  60 */           other = edge.getNodeB();
/*     */         }
/*     */ 
/*  63 */         float newCost = current.cost + edge.getCost();
/*     */ 
/*  66 */         DijkstraNode openNode = (DijkstraNode)this.open.get(other);
/*  67 */         if (openNode != null)
/*     */         {
/*  69 */           if (openNode.cost > newCost)
/*     */           {
/*  71 */             this.priority.remove(openNode);
/*  72 */             openNode.edge = edge;
/*  73 */             openNode.parent = current;
/*  74 */             openNode.cost = newCost;
/*  75 */             this.priority.add(openNode);
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/*  80 */           DijkstraNode closedNode = (DijkstraNode)this.closed.get(other);
/*  81 */           if (closedNode == null)
/*     */           {
/*  98 */             DijkstraNode newNode = new DijkstraNode(other, newCost, current, edge);
/*     */ 
/* 100 */             this.open.put(other, newNode);
/* 101 */             this.priority.add(newNode);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 106 */     LinkedList reverseEdges = new LinkedList();
/* 107 */     while (current.parent != null)
/*     */     {
/* 109 */       reverseEdges.addFirst(current.edge);
/* 110 */       current = current.parent;
/*     */     }
/* 112 */     edgeList.addAll(reverseEdges);
/*     */ 
/* 114 */     return to;
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */   {
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.scissors.Dijkstra1Scissor
 * JD-Core Version:    0.6.2
 */