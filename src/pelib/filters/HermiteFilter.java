/*    */ package pelib.filters;
/*    */ 
/*    */ public class HermiteFilter extends Convolution2PassFilter
/*    */ {
/*    */   public HermiteFilter(int width)
/*    */   {
/* 12 */     super(width);
/*    */ 
/* 14 */     this.kernel = new ConvolutionKernel1D()
/*    */     {
/*    */       public float getWeight(float distance) {
/* 17 */         float val = HermiteFilter.this.kernelWidth - distance;
/* 18 */         return (2.0F * val - 3.0F) * val * val + 1.0F;
/*    */       }
/*    */     };
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.filters.HermiteFilter
 * JD-Core Version:    0.6.2
 */