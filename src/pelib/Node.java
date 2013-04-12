/*     */ package pelib;
/*     */ 
/*     */ import java.awt.Point;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ public final class Node
/*     */ {
/*     */   public int x;
/*     */   public int y;
/*     */   private boolean border;
/*     */   private Trap[] traps;
/*  20 */   private int trapCount = 0;
/*     */   public Edge[] edges;
/*  24 */   private int edgeCount = 0;
/*     */   public float cost;
/*     */   public Edge leadingEdge;
/*     */   public boolean marked;
/*     */ 
/*     */   public Node(int x, int y, int trapCount, boolean border)
/*     */   {
/*  35 */     this.x = x;
/*  36 */     this.y = y;
/*  37 */     this.traps = new Trap[trapCount];
/*  38 */     this.edges = new Edge[4];
/*  39 */     this.border = border;
/*     */   }
/*     */ 
/*     */   public void addTrap(Trap t)
/*     */   {
/*  44 */     this.traps[(this.trapCount++)] = t;
/*     */   }
/*     */ 
/*     */   public Trap getTrap(int i)
/*     */   {
/*  49 */     return this.traps[i];
/*     */   }
/*     */ 
/*     */   public int getTrapCount()
/*     */   {
/*  54 */     return this.trapCount;
/*     */   }
/*     */ 
/*     */   public boolean hasEdge(Node terminator, Trap trapA, Trap trapB)
/*     */   {
/*  59 */     for (int i = 0; i < this.edgeCount; i++)
/*     */     {
/*  61 */       Edge e = this.edges[i];
/*  62 */       Node other = e.getNodeA();
/*  63 */       if (other == this)
/*  64 */         other = e.getNodeB();
/*  65 */       if ((other == terminator) && (e.matches(trapA, trapB)))
/*  66 */         return true;
/*     */     }
/*  68 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean hasTrap(Trap t)
/*     */   {
/*  73 */     for (int i = 0; i < this.trapCount; i++)
/*     */     {
/*  75 */       if (this.traps[i] == t)
/*  76 */         return true;
/*     */     }
/*  78 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isBorder()
/*     */   {
/*  83 */     return this.border;
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public Point getPosition()
/*     */   {
/*  91 */     return new Point(this.x, this.y);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/*  96 */     return "Node at " + this.x + ", " + this.y;
/*     */   }
/*     */ 
/*     */   public void addEdge(Edge e)
/*     */   {
/* 101 */     assert (!hasEdge(e));
/* 102 */     if (this.edgeCount >= this.edges.length)
/*     */     {
/* 104 */       System.out.println("Failed assertion on " + this);
/* 105 */       System.out.println("Tried to add edge ");
/* 106 */       System.out.println("  " + e);
/* 107 */       System.out.println("Edges: ");
/* 108 */       for (int i = 0; i < this.edgeCount; i++)
/* 109 */         System.out.println("   " + this.edges[i]);
/*     */     }
/* 111 */     assert (this.edgeCount < this.edges.length);
/* 112 */     this.edges[(this.edgeCount++)] = e;
/*     */   }
/*     */ 
/*     */   public void removeEdge(Edge e)
/*     */   {int i ;
/* 117 */     for ( i = 0; 
/* 118 */       i < this.edgeCount; i++)
/*     */     {
/* 120 */       if (this.edges[i] == e)
/*     */         break;
/*     */     }
/* 123 */     if (i != this.edgeCount)
/*     */     {
/* 125 */       for (i++; i < this.edgeCount; i++)
/* 126 */         this.edges[(i - 1)] = this.edges[i];
/* 127 */       this.edgeCount -= 1;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 136 */     this.edgeCount = 0;
/* 137 */     this.trapCount = 0;
/*     */ 
/* 141 */     this.traps = new Trap[4];
/* 142 */     this.edges = new Edge[4];
/*     */   }
/*     */ 
/*     */   public int getEdgeCount()
/*     */   {
/* 147 */     return this.edgeCount;
/*     */   }
/*     */ 
/*     */   public boolean hasEdge(Trap a, Trap b)
/*     */   {
/* 152 */     for (int i = 0; i < this.edgeCount; i++)
/*     */     {
/* 154 */       if (this.edges[i].matches(a, b))
/* 155 */         return true;
/*     */     }
/* 157 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean hasEdge(Edge e)
/*     */   {
/* 162 */     for (int i = 0; i < this.edgeCount; i++)
/*     */     {
/* 164 */       if (this.edges[i] == e)
/* 165 */         return true;
/*     */     }
/* 167 */     return false;
/*     */   }
/*     */ 
/*     */   public Iterator getEdges()
/*     */   {
/* 172 */     return new EdgeIterator();
/*     */   }
/*     */ 
/*     */   public Edge findNearestEdge(int x, int y)
/*     */   {
/* 205 */     int bestDist = 2147483647;
/* 206 */     Edge best = null;
/*     */ 
/* 208 */     for (int i = 0; i < this.edgeCount; i++)
/*     */     {
/* 210 */       Node n = this.edges[i].getNodeA();
/* 211 */       if (n == this)
/* 212 */         n = this.edges[i].getNodeB();
/* 213 */       int dist = (x - n.x) * (x - n.x) + (y - n.y) * (y - n.y);
/* 214 */       if (dist < bestDist)
/*     */       {
/* 216 */         bestDist = dist;
/* 217 */         best = this.edges[i];
/*     */       }
/*     */     }
/* 220 */     return best;
/*     */   }
/*     */ 
/*     */   private class EdgeIterator
/*     */     implements Iterator
/*     */   {
/*     */     private int index;
/*     */ 
/*     */     public EdgeIterator()
/*     */     {
/*     */     }
/*     */ 
/*     */     public boolean hasNext()
/*     */     {
/* 185 */       return this.index < Node.this.edgeCount;
/*     */     }
/*     */ 
/*     */     public Object next()
/*     */     {
/* 190 */       return Node.this.edges[(this.index++)];
/*     */     }
/*     */ 
/*     */     public void remove()
/*     */     {
/* 195 */       throw new UnsupportedOperationException();
/*     */     }
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.Node
 * JD-Core Version:    0.6.2
 */