/*    */ package pelib.filters;
/*    */ 
/*    */ public class BilinearFilter extends Convolution2PassFilter
/*    */ {
/*    */   public BilinearFilter(int width)
/*    */   {
/* 12 */     super(width);
/* 13 */     this.kernel = new ConvolutionKernel1D()
/*    */     {
/*    */       public float getWeight(float distance) {
/* 16 */         return distance < BilinearFilter.this.kernelWidth ? BilinearFilter.this.kernelWidth - distance : 0.0F;
/*    */       }
/*    */     };
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.filters.BilinearFilter
 * JD-Core Version:    0.6.2
 */