/*     */ package pelib.scissors;
/*     */ 
/*     */ import pelib.Edge;
/*     */ import pelib.Node;
/*     */ 
/*     */ class DijkstraNode
/*     */ {
/*     */   public Node node;
/*     */   public float cost;
/*     */   public DijkstraNode parent;
/*     */   public Edge edge;
/*     */ 
/*     */   public DijkstraNode(Node node, float cost, DijkstraNode parent, Edge edge)
/*     */   {
/* 129 */     this.node = node;
/* 130 */     this.cost = cost;
/* 131 */     this.parent = parent;
/* 132 */     this.edge = edge;
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.scissors.DijkstraNode
 * JD-Core Version:    0.6.2
 */