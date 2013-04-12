/*    */ package pelibskin;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Graphics;
/*    */ 
/*    */ public class SkinnedListSwatchItem
/*    */   implements SkinnedListItem
/*    */ {
/*    */   public String label;
/*    */   public Color color;
/*    */   public Object data;
/*    */ 
/*    */   public SkinnedListSwatchItem(String label, Color color, Object data)
/*    */   {
/* 35 */     this.label = label;
/*    */ 
/* 37 */     this.color = color;
/*    */ 
/* 39 */     this.data = data;
/*    */   }
/*    */ 
/*    */   public void draw(Graphics g, SkinnedList list, int x, int y, boolean highlighted)
/*    */   {
/* 51 */     g.setColor(this.color);
/*    */ 
/* 53 */     g.fillRect(7, y - list.getLineHeight() + list.getHighlightHanging(), 16, list.getLineHeight() - 2 * list.getHighlightHanging());
/*    */ 
/* 59 */     if (highlighted)
/*    */     {
/* 61 */       g.setColor(list.getHighlightForeground());
/*    */     }
/*    */     else
/*    */     {
/* 65 */       g.setColor(list.getTextColor());
/*    */     }
/* 67 */     g.drawString(this.label, x + 26, y - list.getHighlightHanging());
/*    */   }
/*    */ 
/*    */   public Object getData()
/*    */   {
/* 77 */     return this.data;
/*    */   }
/*    */ 
/*    */   public String getLabel()
/*    */   {
/* 87 */     return this.label;
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelibskin.SkinnedListSwatchItem
 * JD-Core Version:    0.6.2
 */