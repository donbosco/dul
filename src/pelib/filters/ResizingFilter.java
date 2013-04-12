/*    */ package pelib.filters;
/*    */ 
/*    */ import java.awt.Rectangle;
/*    */ import pelib.ImageColour;
/*    */ 
/*    */ public abstract class ResizingFilter extends Filter
/*    */ {
/*    */   public void filter(ImageColour src, ImageColour dest)
/*    */   {
/* 17 */     if (src == dest) {
/* 18 */       throw new IllegalArgumentException("Source and destination images are the same");
/*    */     }
/*    */ 
/* 21 */     Rectangle srcRect = src.getBounds();
/* 22 */     Rectangle destRect = dest.getBounds();
/* 23 */     filter(src, dest, srcRect, destRect);
/*    */   }
/*    */ 
/*    */   public abstract void filter(ImageColour paramImageColour1, ImageColour paramImageColour2, Rectangle paramRectangle1, Rectangle paramRectangle2);
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.filters.ResizingFilter
 * JD-Core Version:    0.6.2
 */