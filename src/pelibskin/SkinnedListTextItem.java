/*    */ package pelibskin;
/*    */ 
/*    */ import java.awt.Graphics;
/*    */ 
/*    */ public class SkinnedListTextItem
/*    */   implements SkinnedListItem
/*    */ {
/*    */   public String label;
/*    */   public Object data;
/*    */ 
/*    */   public SkinnedListTextItem(String label, Object data)
/*    */   {
/* 33 */     this.label = label;
/*    */ 
/* 35 */     this.data = data;
/*    */   }
/*    */ 
/*    */   public void draw(Graphics g, SkinnedList list, int x, int y, boolean highlighted)
/*    */   {
/* 47 */     if (highlighted)
/*    */     {
/* 49 */       g.setColor(list.getHighlightForeground());
/*    */     }
/*    */     else
/*    */     {
/* 53 */       g.setColor(list.getTextColor());
/*    */     }
/* 55 */     g.drawString(this.label, x + 5, y - list.getHighlightHanging());
/*    */   }
/*    */ 
/*    */   public Object getData()
/*    */   {
/* 65 */     return this.data;
/*    */   }
/*    */ 
/*    */   public String getLabel()
/*    */   {
/* 75 */     return this.label;
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelibskin.SkinnedListTextItem
 * JD-Core Version:    0.6.2
 */