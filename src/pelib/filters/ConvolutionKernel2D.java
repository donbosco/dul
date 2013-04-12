/*    */ package pelib.filters;
/*    */ 
/*    */ public class ConvolutionKernel2D
/*    */ {
/*    */   private float[] data;
/*    */   private int width;
/*    */   private int height;
/*    */ 
/*    */   public ConvolutionKernel2D(float[] data, int width, int height)
/*    */   {
/* 16 */     this.data = data;
/* 17 */     this.width = width;
/* 18 */     this.height = height;
/*    */   }
/*    */ 
/*    */   public float[] getData()
/*    */   {
/* 23 */     return this.data;
/*    */   }
/*    */ 
/*    */   public int getWidth()
/*    */   {
/* 28 */     return this.width;
/*    */   }
/*    */ 
/*    */   public int getHeight()
/*    */   {
/* 33 */     return this.height;
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.filters.ConvolutionKernel2D
 * JD-Core Version:    0.6.2
 */