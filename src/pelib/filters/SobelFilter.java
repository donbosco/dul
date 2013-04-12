/*    */ package pelib.filters;
/*    */ 
/*    */ import pelib.ImageColour;
/*    */ import pelib.ImageFloat;
/*    */ 
/*    */ public class SobelFilter extends Filter
/*    */ {
/*    */   private Convolution1PassFilter horizontalFilter;
/*    */   private Convolution1PassFilter verticalFilter;
/*    */ 
/*    */   public SobelFilter()
/*    */   {
/* 17 */     this.horizontalFilter = new Convolution1PassFilter(new SobelHorizontalKernel());
/*    */ 
/* 19 */     this.verticalFilter = new Convolution1PassFilter(new SobelVerticalKernel());
/*    */   }
/*    */ 
/*    */   public void filter(ImageColour src, ImageColour dest)
/*    */   {
/* 25 */     ImageColour temp = (ImageColour)dest.createCompatibleImage();
/* 26 */     this.horizontalFilter.filter(src, temp);
/* 27 */     this.verticalFilter.filter(src, dest);
/* 28 */     dest.add(temp);
/*    */   }
/*    */ 
/*    */   public void filter(ImageFloat src, ImageFloat dest)
/*    */   {
/* 33 */     ImageFloat temp = (ImageFloat)dest.createCompatibleImage();
/* 34 */     this.horizontalFilter.filter(src, temp);
/* 35 */     this.verticalFilter.filter(src, dest);
/* 36 */     dest.addAbsolute(temp);
/*    */ 
/* 39 */     float[] data = dest.getBufferFloat();
/* 40 */     for (int i = 0; i < data.length; i++)
/* 41 */       data[i] /= 4.0F;
/*    */   }
/*    */ 
/*    */   private static class SobelVerticalKernel extends ConvolutionKernel2D
/*    */   {
/* 59 */     private static float[] kern = { 1.0F, 0.0F, -1.0F, 2.0F, 0.0F, -2.0F, 1.0F, 0.0F, -1.0F };
/*    */ 
/*    */     public SobelVerticalKernel()
/*    */     {
/* 66 */       super(kern, 3, 3);
/*    */     }
/*    */   }
/*    */ 
/*    */   private static class SobelHorizontalKernel extends ConvolutionKernel2D
/*    */   {
/* 46 */     private static float[] kern = { 1.0F, 2.0F, 1.0F, 0.0F, 0.0F, 0.0F, -1.0F, -2.0F, -1.0F };
/*    */ 
/*    */     public SobelHorizontalKernel()
/*    */     {
/* 53 */       super(kern, 3, 3);
/*    */     }
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.filters.SobelFilter
 * JD-Core Version:    0.6.2
 */