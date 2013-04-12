/*    */ package pelib;
/*    */ 
/*    */ public class ImageLabelled extends ImageInteger
/*    */ {
/*    */   private int labels;
/*    */ 
/*    */   public ImageLabelled(int width, int height)
/*    */   {
/* 15 */     super(width, height);
/*    */   }
/*    */ 
/*    */   public ImageLabelled(ImageLabelled labelled)
/*    */   {
/* 20 */     super(labelled);
/* 21 */     this.labels = labelled.labels;
/*    */   }
/*    */ 
/*    */   public void setLabelCount(int labels)
/*    */   {
/* 26 */     this.labels = labels;
/*    */   }
/*    */ 
/*    */   public int getLabelCount()
/*    */   {
/* 31 */     return this.labels;
/*    */   }
/*    */ 
/*    */   public ImageBase copy()
/*    */   {
/* 36 */     return new ImageLabelled(this);
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.ImageLabelled
 * JD-Core Version:    0.6.2
 */