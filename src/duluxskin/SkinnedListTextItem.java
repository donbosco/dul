/*    */ package duluxskin;
/*    */ 
/*    */ import dulux.DuluxColour;
/*    */ import java.awt.Graphics;
/*    */ 
/*    */ public class SkinnedListTextItem
/*    */   implements SkinnedListItem, Cloneable
/*    */ {
/*    */   public String label;
/*    */   public Object data;
/*    */ 
/*    */   public SkinnedListTextItem(String label, Object data)
/*    */   {
/* 19 */     this.label = label;
/*    */ 
/* 21 */     this.data = data;
/*    */   }
/*    */ 
/*    */   public void draw(Graphics g, SkinnedList list, int x, int y, boolean highlighted)
/*    */   {
/* 28 */     if (highlighted)
/* 29 */       g.setColor(list.getHighlightForeground());
/*    */     else {
/* 31 */       g.setColor(list.getTextColor());
/*    */     }
/*    */ 
/* 34 */     g.drawString(this.label, x + 5, y - list.getHighlightHanging());
/*    */   }
/*    */ 
/*    */   public Object getData()
/*    */   {
/* 40 */     return this.data;
/*    */   }
/*    */ 
/*    */   public String getLabel()
/*    */   {
/* 46 */     return this.label;
/*    */   }
/*    */ 
/*    */   public Object clone()
/*    */   {
/* 52 */     return ((DuluxColour)this.data).clone();
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     duluxskin.SkinnedListTextItem
 * JD-Core Version:    0.6.2
 */