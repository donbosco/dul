/*    */ package pelib;
/*    */ 
/*    */ public final class Statistics
/*    */ {
/*    */   private int samples;
/*    */   private float sum;
/*    */   private float sumSquared;
/*    */   private float variance;
/*    */ 
/*    */   public Statistics()
/*    */   {
/* 19 */     this.samples = 0;
/* 20 */     this.sum = 0.0F;
/* 21 */     this.sumSquared = 0.0F;
/* 22 */     this.variance = -1.0F;
/*    */   }
/*    */ 
/*    */   public void clear()
/*    */   {
/* 27 */     this.samples = 0;
/* 28 */     this.sum = 0.0F;
/* 29 */     this.sumSquared = 0.0F;
/* 30 */     this.variance = -1.0F;
/*    */   }
/*    */ 
/*    */   public void add(Statistics stats)
/*    */   {
/* 35 */     this.samples += stats.samples;
/* 36 */     this.sum += stats.sum;
/* 37 */     this.sumSquared += stats.sumSquared;
/* 38 */     this.variance = -1.0F;
/*    */   }
/*    */ 
/*    */   public void subtract(Statistics stats)
/*    */   {
/* 43 */     this.samples -= stats.samples;
/* 44 */     this.sum -= stats.sum;
/* 45 */     this.sumSquared -= stats.sumSquared;
/* 46 */     this.variance = -1.0F;
/*    */   }
/*    */ 
/*    */   public void add(float sample)
/*    */   {
/* 51 */     this.samples += 1;
/* 52 */     this.sum += sample;
/* 53 */     this.sumSquared += sample * sample;
/* 54 */     this.variance = -1.0F;
/*    */   }
/*    */ 
/*    */   public int getSamples()
/*    */   {
/* 59 */     return this.samples;
/*    */   }
/*    */ 
/*    */   public float getMean()
/*    */   {
/* 64 */     return this.sum / this.samples;
/*    */   }
/*    */ 
/*    */   public float getVariance()
/*    */   {
/* 69 */     if (this.variance < 0.0F)
/* 70 */       updateVariance();
/* 71 */     return this.variance;
/*    */   }
/*    */ 
/*    */   private void updateVariance()
/*    */   {
/* 76 */     if (this.samples < 2) {
/* 77 */       this.variance = 0.0F;
/*    */     }
/*    */     else {
/* 80 */       float a = this.samples * this.sumSquared - this.sum * this.sum;
/* 81 */       float b = this.samples * (this.samples - 1.0F);
/* 82 */       this.variance = (a / b);
/* 83 */       if (this.variance < 0.0F)
/* 84 */         this.variance = 0.0F;
/*    */     }
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.Statistics
 * JD-Core Version:    0.6.2
 */