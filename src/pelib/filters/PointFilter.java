/*    */ package pelib.filters;
/*    */ 
/*    */ public class PointFilter extends Convolution2PassFilter
/*    */ {
/*    */   public PointFilter()
/*    */   {
/* 12 */     super(1);
/* 13 */     this.kernel = new ConvolutionKernel1D()
/*    */     {
/*    */       public float getWeight(float distance) {
/* 16 */         return 1.0F;
/*    */       }
/*    */     };
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.filters.PointFilter
 * JD-Core Version:    0.6.2
 */