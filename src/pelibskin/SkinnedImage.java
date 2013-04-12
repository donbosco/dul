/*    */ package pelibskin;
/*    */ 
/*    */ import java.awt.Graphics;
/*    */ import java.awt.Image;
/*    */ 
/*    */ public class SkinnedImage extends Widget
/*    */ {
/*    */   private Image img;
/*    */ 
/*    */   public SkinnedImage(String id, int x, int y, Image img)
/*    */   {
/* 31 */     super(id, x, y);
/*    */ 
/* 33 */     this.img = img;
/*    */ 
/* 35 */     this.width = img.getWidth(null);
/*    */ 
/* 37 */     this.height = img.getHeight(null);
/*    */   }
/*    */ 
/*    */   public void paint(Graphics g)
/*    */   {
/* 47 */     g.drawImage(this.img, 0, 0, this.width, this.height, null);
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelibskin.SkinnedImage
 * JD-Core Version:    0.6.2
 */