/*    */ package pelib;
/*    */ 
/*    */ public final class Histogram
/*    */ {
/*    */   private int granularity;
/*    */   private int samples;
/*    */   private int[] frequency;
/*    */ 
/*    */   public Histogram()
/*    */   {
/* 18 */     this(3);
/*    */   }
/*    */ 
/*    */   public Histogram(int granularity)
/*    */   {
/* 26 */     if (granularity > 8)
/* 27 */       granularity = 8;
/* 28 */     this.granularity = granularity;
/* 29 */     this.samples = 0;
/* 30 */     this.frequency = new int[1 << granularity];
/*    */   }
/*    */ 
/*    */   public void addSample(int value)
/*    */   {
/* 35 */     this.samples += 1;
/* 36 */     this.frequency[(value >> 8 - this.granularity)] += 1;
/*    */   }
/*    */ 
/*    */   public void subtractSample(int value)
/*    */   {
/* 41 */     this.samples -= 1;
/* 42 */     this.frequency[(value >> 8 - this.granularity)] -= 1;
/*    */   }
/*    */ 
/*    */   public void clear()
/*    */   {
/* 47 */     this.samples = 0;
/* 48 */     for (int i = 0; i < this.frequency.length; i++)
/* 49 */       this.frequency[i] = 0;
/*    */   }
/*    */ 
/*    */   public int getMedian()
/*    */   {
/* 54 */     int half = this.samples / 2;
/*    */ int i;
/* 56 */     for ( i = 0; (i < this.frequency.length) && (half > 0); i++)
/* 57 */       half -= this.frequency[i];
/* 58 */     return i << 8 - this.granularity;
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.Histogram
 * JD-Core Version:    0.6.2
 */