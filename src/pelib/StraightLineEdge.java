/*    */ package pelib;
/*    */ 
/*    */ import java.util.Vector;
/*    */ 
/*    */ public class StraightLineEdge extends Edge
/*    */ {
/*    */   private boolean steep;
/*    */ 
/*    */   public StraightLineEdge(Node nodeA, Node nodeB)
/*    */   {
/* 18 */     super(nodeA, nodeB, null, null, null);
/*    */ 
/* 21 */     createCrackSequence(nodeA.x, nodeA.y, nodeB.x, nodeB.y);
/*    */   }
/*    */ 
/*    */   private void createCrackSequence(int x0, int y0, int x1, int y1)
/*    */   {
/* 35 */     this.steep = (Math.abs(y1 - y0) > Math.abs(x1 - x0));
/* 36 */     if (this.steep)
/*    */     {
/* 38 */       createCrackSequenceSteep(x0, y0, x1, y1);
/* 39 */       return;
/*    */     }
/* 41 */     this.sequence = new Vector(Math.abs(x1 - x0) + 1);
/* 42 */     int deltax = Math.abs(x1 - x0);
/* 43 */     int deltay = Math.abs(y1 - y0);
/* 44 */     int error = 0;
/* 45 */     int deltaerror = deltay;
/* 46 */     int x = x0;
/* 47 */     int y = y0;
/* 48 */     int xstep = x0 < x1 ? 1 : -1;
/* 49 */     int ystep = y0 < y1 ? 1 : -1;
/* 50 */     this.sequence.add(new Crack(x, y, x, y + 1));
/* 51 */     while (x != x1)
/*    */     {
/* 53 */       x += xstep;
/* 54 */       error += deltaerror;
/* 55 */       if (error << 1 >= deltax)
/*    */       {
/* 57 */         y += ystep;
/* 58 */         error -= deltax;
/*    */       }
/* 60 */       this.sequence.add(new Crack(x, y, x, y + 1));
/*    */     }
/*    */   }
/*    */ 
/*    */   private void createCrackSequenceSteep(int x0, int y0, int x1, int y1)
/*    */   {
/* 70 */     this.sequence = new Vector(Math.abs(y1 - y0) + 1);
/* 71 */     int deltax = Math.abs(x1 - x0);
/* 72 */     int deltay = Math.abs(y1 - y0);
/* 73 */     int error = 0;
/* 74 */     int deltaerror = deltax;
/* 75 */     int x = x0;
/* 76 */     int y = y0;
/* 77 */     int xstep = x0 < x1 ? 1 : -1;
/* 78 */     int ystep = y0 < y1 ? 1 : -1;
/* 79 */     this.sequence.add(new Crack(x, y, x + 1, y));
/* 80 */     while (y != y1)
/*    */     {
/* 82 */       y += ystep;
/* 83 */       error += deltaerror;
/* 84 */       if (error << 1 >= deltay)
/*    */       {
/* 86 */         x += xstep;
/* 87 */         error -= deltay;
/*    */       }
/* 89 */       this.sequence.add(new Crack(x, y, x + 1, y));
/*    */     }
/*    */   }
/*    */ 
/*    */   public boolean isSteep()
/*    */   {
/* 98 */     return this.steep;
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.StraightLineEdge
 * JD-Core Version:    0.6.2
 */