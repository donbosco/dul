/*    */ package pelib;
/*    */ 
/*    */ public class Crack
/*    */ {
/*    */   public int x1;
/*    */   public int y1;
/*    */   public int x2;
/*    */   public int y2;
/*    */ 
/*    */   public Crack(int x1, int y1, int x2, int y2)
/*    */   {
/* 20 */     this.x1 = Math.min(x1, x2);
/* 21 */     this.y1 = Math.min(y1, y2);
/* 22 */     this.x2 = Math.max(x1, x2);
/* 23 */     this.y2 = Math.max(y1, y2);
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 28 */     return "<" + this.x1 + "," + this.y1 + "::" + this.x2 + "," + this.y2 + ">";
/*    */   }
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 34 */     return this.x1 + this.y1 + this.x2 + this.y2;
/*    */   }
/*    */ 
/*    */   public boolean equals(Object o)
/*    */   {
/* 39 */     if ((o instanceof Crack))
/*    */     {
/* 41 */       Crack other = (Crack)o;
/* 42 */       return (this.x1 == other.x1) && (this.y1 == other.y1) && (this.x2 == other.x2) && (this.y2 == other.y2);
/*    */     }
/*    */ 
/* 47 */     return false;
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.Crack
 * JD-Core Version:    0.6.2
 */