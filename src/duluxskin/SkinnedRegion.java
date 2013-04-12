/*    */ package duluxskin;
/*    */ 
/*    */ import java.awt.AlphaComposite;
/*    */ import java.awt.Color;
/*    */ import java.awt.Composite;
/*    */ import java.awt.Graphics;
/*    */ import java.awt.Graphics2D;
/*    */ 
/*    */ public class SkinnedRegion extends Widget
/*    */ {
/*    */   private Color color;
/*    */   private float opacity;
/*    */ 
/*    */   public SkinnedRegion(String id, int x, int y, int width, int height)
/*    */   {
/* 33 */     super(id, x, y);
/*    */ 
/* 35 */     this.width = width;
/*    */ 
/* 37 */     this.height = height;
/*    */ 
/* 41 */     this.color = null;
/*    */ 
/* 43 */     this.opacity = 1.0F;
/*    */   }
/*    */ 
/*    */   public void setColor(Color color)
/*    */   {
/* 53 */     this.color = color;
/*    */ 
/* 55 */     invalidate();
/*    */   }
/*    */ 
/*    */   public void setOpacity(float opacity)
/*    */   {
/* 65 */     this.opacity = opacity;
/*    */ 
/* 67 */     invalidate();
/*    */   }
/*    */ 
/*    */   public void paint(Graphics g)
/*    */   {
/* 77 */     if (this.color != null)
/*    */     {
/* 81 */       Graphics2D g2 = (Graphics2D)g;
/*    */ 
/* 83 */       Composite oldComp = g2.getComposite();
/*    */ 
/* 85 */       g2.setColor(this.color);
/*    */ 
/* 87 */       g2.setComposite(AlphaComposite.getInstance(3, this.opacity));
/*    */ 
/* 91 */       g2.fillRect(0, 0, this.width, this.height);
/*    */ 
/* 93 */       g2.setComposite(oldComp);
/*    */     }
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     duluxskin.SkinnedRegion
 * JD-Core Version:    0.6.2
 */