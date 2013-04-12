/*    */ package pelib.filters;
/*    */ 
/*    */ public class BoxFilter extends Convolution2PassFilter
/*    */ {
/*    */   public BoxFilter(int width)
/*    */   {
/* 12 */     super(width);
/* 13 */     this.kernel = new ConvolutionKernel1D()
/*    */     {
/*    */       public float getWeight(float distance) {
/* 16 */         return distance < BoxFilter.this.kernelWidth ? 1.0F : 0.0F;
/*    */       }
/*    */     };
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.filters.BoxFilter
 * JD-Core Version:    0.6.2
 */