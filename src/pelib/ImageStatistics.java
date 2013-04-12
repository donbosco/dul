/*    */ package pelib;
/*    */ 
/*    */ public final class ImageStatistics
/*    */ {
/*    */   private Statistics intensity;
/*    */   private Statistics red;
/*    */   private Statistics green;
/*    */   private Statistics blue;
/*    */   private static final float COLOUR_FILTER_TOLERANCE = 12.8F;
/*    */ 
/*    */   public ImageStatistics()
/*    */   {
/* 21 */     this.intensity = new Statistics();
/* 22 */     this.red = new Statistics();
/* 23 */     this.green = new Statistics();
/* 24 */     this.blue = new Statistics();
/*    */   }
/*    */ 
/*    */   public final void addSample(int colour)
/*    */   {
/* 29 */     this.intensity.add(Colour.getIntensity(colour) / 256.0F);
/* 30 */     this.red.add(colour & 0xFF);
/* 31 */     this.green.add(colour >> 8 & 0xFF);
/* 32 */     this.blue.add(colour >> 16 & 0xFF);
/*    */   }
/*    */ 
/*    */   public final Statistics getIntensityStatistics()
/*    */   {
/* 37 */     return this.intensity;
/*    */   }
/*    */ 
/*    */   public final float getIntensityMean()
/*    */   {
/* 42 */     return this.intensity.getMean();
/*    */   }
/*    */ 
/*    */   public final void add(ImageStatistics stats)
/*    */   {
/* 47 */     this.intensity.add(stats.intensity);
/* 48 */     this.red.add(stats.red);
/* 49 */     this.green.add(stats.green);
/* 50 */     this.blue.add(stats.blue);
/*    */   }
/*    */ 
/*    */   public final void clear()
/*    */   {
/* 55 */     this.intensity.clear();
/* 56 */     this.red.clear();
/* 57 */     this.green.clear();
/* 58 */     this.blue.clear();
/*    */   }
/*    */ 
/*    */   public final float computeCost(ImageStatistics stats)
/*    */   {
/* 67 */     float r = Math.abs(stats.red.getMean() - this.red.getMean());
/* 68 */     float g = Math.abs(stats.green.getMean() - this.green.getMean());
/* 69 */     float b = Math.abs(stats.blue.getMean() - this.blue.getMean());
/* 70 */     if (r + g + b > 12.8F) {
/* 71 */       return 20.48F;
/*    */     }
/*    */ 
/* 74 */     return Math.abs(getIntensityMean() - stats.getIntensityMean());
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.ImageStatistics
 * JD-Core Version:    0.6.2
 */