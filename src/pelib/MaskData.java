/*    */ package pelib;
/*    */ 
/*    */ class MaskData
/*    */ {
/*    */   public Mask mask;
/*    */   public int weight;
/*    */ 
/*    */   public final void addSample(int sample)
/*    */   {
/* 19 */     this.weight += 1;
/*    */   }
/*    */ 
/*    */   public void clear()
/*    */   {
/* 24 */     this.weight = 0;
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.MaskData
 * JD-Core Version:    0.6.2
 */