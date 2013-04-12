/*    */ package pelibskin;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Graphics;
/*    */ 
/*    */ public class SkinnedProgress extends Widget
/*    */ {
/*    */   private float value;
/*    */   private Color color;
/*    */ 
/*    */   public SkinnedProgress(String id, int x, int y, int width, int height)
/*    */   {
/* 33 */     super(id, x, y);
/*    */ 
/* 35 */     this.width = width;
/*    */ 
/* 37 */     this.height = height;
/*    */   }
/*    */ 
/*    */   public void setColor(Color c)
/*    */   {
/* 47 */     this.color = c;
/*    */   }
/*    */ 
/*    */   public void setValue(float v)
/*    */   {
/* 57 */     this.value = Math.max(0.0F, Math.min(1.0F, v));
/*    */ 
/* 59 */     invalidate();
/*    */   }
/*    */ 
/*    */   public float getValue()
/*    */   {
/* 69 */     return this.value;
/*    */   }
/*    */ 
/*    */   public void paint(Graphics g)
/*    */   {
/* 79 */     g.setColor(this.color);
/*    */ 
/* 81 */     g.fillRect(0, 0, (int)(this.width * this.value), this.height);
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelibskin.SkinnedProgress
 * JD-Core Version:    0.6.2
 */