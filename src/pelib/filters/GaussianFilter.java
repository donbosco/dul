/*    */ package pelib.filters;
/*    */ 
/*    */ public class GaussianFilter extends Convolution2PassFilter
/*    */ {
/*    */   private float a;
/*    */   private float b;
/*    */ 
/*    */   public GaussianFilter()
/*    */   {
/* 19 */     this(3, 1.0F);
/*    */   }
/*    */ 
/*    */   public GaussianFilter(int width, float stddev)
/*    */   {
/* 24 */     super(width);
/* 25 */     this.a = ((float)Math.sqrt(6.283185307179586D) * stddev);
/* 26 */     this.b = (2.0F * stddev * stddev);
/* 27 */     this.kernel = new ConvolutionKernel1D()
/*    */     {
/*    */       public float getWeight(float distance) {
/* 30 */         return (float)Math.exp(-(distance * distance) / GaussianFilter.this.b) / GaussianFilter.this.a;
/*    */       }
/*    */     };
/*    */   }
/*    */ 
/*    */   public static GaussianFilter getSmall()
/*    */   {
/* 37 */     return new GaussianFilter(3, 1.0F);
/*    */   }
/*    */ 
/*    */   public static GaussianFilter getLarge()
/*    */   {
/* 42 */     return new GaussianFilter(5, 1.4F);
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.filters.GaussianFilter
 * JD-Core Version:    0.6.2
 */