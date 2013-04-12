/*     */ package pelib;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class Edge
/*     */ {
/*     */   protected Node nodeA;
/*     */   protected Node nodeB;
/*     */   private Trap trapA;
/*     */   private Trap trapB;
/*     */   protected Vector sequence;
/*     */   protected Vector gradientVector;
/*     */   private float cost;
/*     */   private boolean cut;
/*     */   public boolean marked;
/*     */ 
/*     */   public Edge(Node nodeA, Node nodeB, Trap trapA, Trap trapB, Vector sequence)
/*     */   {
/*  31 */     this.nodeA = nodeA;
/*  32 */     this.nodeB = nodeB;
/*  33 */     this.trapA = trapA;
/*  34 */     this.trapB = trapB;
/*  35 */     this.sequence = sequence;
/*     */   }
/*     */ 
/*     */   public boolean matches(Trap a, Trap b)
/*     */   {
/*  40 */     return ((a == this.trapA) && (b == this.trapB)) || ((a == this.trapB) && (b == this.trapA));
/*     */   }
/*     */ 
/*     */   public Vector getCrackSequence()
/*     */   {
/*  45 */     return this.sequence;
/*     */   }
/*     */ 
/*     */   public Trap getTrapA()
/*     */   {
/*  50 */     return this.trapA;
/*     */   }
/*     */ 
/*     */   public Trap getTrapB()
/*     */   {
/*  55 */     return this.trapB;
/*     */   }
/*     */ 
/*     */   public Node getNodeA()
/*     */   {
/*  60 */     return this.nodeA;
/*     */   }
/*     */ 
/*     */   public Node getNodeB()
/*     */   {
/*  65 */     return this.nodeB;
/*     */   }
/*     */ 
/*     */   public void setGradientVector(Vector v)
/*     */   {
/*  70 */     this.gradientVector = v;
/*     */   }
/*     */ 
/*     */   public Vector getGradientVector()
/*     */   {
/*  75 */     return this.gradientVector;
/*     */   }
/*     */ 
/*     */   public float getCost()
/*     */   {
/*  80 */     return this.cost;
/*     */   }
/*     */ 
/*     */   public void setCost(float cost)
/*     */   {
/*  85 */     this.cost = cost;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/*  90 */     String s = "Edge between " + this.nodeA + " and " + this.nodeB + "; traps " + this.trapA + " and " + this.trapB + "; cracks: ";
/*     */ 
/*  93 */     for (Iterator it = this.sequence.iterator(); it.hasNext(); )
/*     */     {
/*  95 */       Crack c = (Crack)it.next();
/*  96 */       s = s + c;
/*     */     }
/*  98 */     return s;
/*     */   }
/*     */ 
/*     */   public void cut()
/*     */   {
/* 104 */     this.cut = true;
/*     */   }
/*     */ 
/*     */   public void uncut()
/*     */   {
/* 109 */     this.cut = false;
/*     */   }
/*     */ 
/*     */   public boolean isCut()
/*     */   {
/* 114 */     return this.cut;
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.Edge
 * JD-Core Version:    0.6.2
 */