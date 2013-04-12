/*    */ package duluxskin;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Graphics;
/*    */ 
/*    */ public class SkinnedListSwatchItem
/*    */   implements SkinnedListItem, Cloneable
/*    */ {
/*    */   public String label;
/*    */   public Color color;
/*    */   public Object data;
/*    */ 
/*    */   public SkinnedListSwatchItem(String label, Color color, Object data)
/*    */   {
/* 19 */     this.label = label;
/*    */ 
/* 21 */     this.color = color;
/*    */ 
/* 23 */     this.data = data;
/*    */   }
/*    */ 
/*    */   public void draw(Graphics g, SkinnedList list, int x, int y, boolean highlighted)
/*    */   {
/* 30 */     g.setColor(this.color);
/*    */ 
/* 32 */     g.fillRect(7, y - list.getLineHeight() + list.getHighlightHanging(), 16, list.getLineHeight() - 2 * list.getHighlightHanging());
/*    */ 
/* 37 */     if (highlighted)
/* 38 */       g.setColor(list.getHighlightForeground());
/*    */     else {
/* 40 */       g.setColor(list.getTextColor());
/*    */     }
/*    */ 
/* 43 */     g.drawString(this.label, x + 26, y - list.getHighlightHanging());
/*    */   }
/*    */ 
/*    */   public Object getData()
/*    */   {
/* 49 */     return this.data;
/*    */   }
/*    */ 
/*    */   public String getLabel()
/*    */   {
/* 55 */     return this.label;
/*    */   }
/*    */ 
/*    */   public Object clone()
/*    */   {
/*    */     try {
/* 61 */       return super.clone(); } catch (CloneNotSupportedException e) {
/*    */     }
/* 63 */     return null;
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     duluxskin.SkinnedListSwatchItem
 * JD-Core Version:    0.6.2
 */