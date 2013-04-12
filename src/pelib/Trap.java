/*    */ package pelib;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import java.util.Vector;
/*    */ 
/*    */ public class Trap extends AbstractRegion
/*    */ {
/*    */   private Vector nodes;
/*    */ 
/*    */   public Trap()
/*    */   {
/* 17 */     this.nodes = new Vector();
/*    */   }
/*    */ 
/*    */   public void addNode(Node node)
/*    */   {
/* 22 */     if (!this.nodes.contains(node))
/* 23 */       this.nodes.add(node);
/*    */   }
/*    */ 
/*    */   public Vector getNodes()
/*    */   {
/* 28 */     return this.nodes;
/*    */   }
/*    */ 
/*    */   public Node getNode(int x, int y)
/*    */   {
/* 33 */     for (Iterator it = this.nodes.iterator(); it.hasNext(); )
/*    */     {
/* 35 */       Node n = (Node)it.next();
/* 36 */       if ((n.x == x) && (n.y == y))
/* 37 */         return n;
/*    */     }
/* 39 */     return null;
/*    */   }
/*    */ 
/*    */   public void addSample(int colour)
/*    */   {
/* 44 */     this.statistics.addSample(colour);
/*    */   }
/*    */ 
/*    */   public void bound(int x, int y)
/*    */   {
/* 49 */     this.area.bound(x, y);
/*    */   }
/*    */ 
/*    */   public Node findNearestNode(int x, int y)
/*    */   {
/* 54 */     int bestDist = 2147483647;
/* 55 */     Node best = null;
/* 56 */     for (Iterator it = this.nodes.iterator(); it.hasNext(); )
/*    */     {
/* 58 */       Node n = (Node)it.next();
/* 59 */       int dist = (x - n.x) * (x - n.x) + (y - n.y) * (y - n.y);
/* 60 */       if (dist < bestDist)
/*    */       {
/* 62 */         bestDist = dist;
/* 63 */         best = n;
/*    */       }
/*    */     }
/*    */ 
/* 67 */     return best;
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.Trap
 * JD-Core Version:    0.6.2
 */