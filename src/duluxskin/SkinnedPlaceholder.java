/*    */ package duluxskin;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Graphics;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class SkinnedPlaceholder extends Widget
/*    */ {
/*    */   private Map attributes;
/*    */ 
/*    */   public SkinnedPlaceholder(String id, int x, int y, int width, int height)
/*    */   {
/* 33 */     super(id, x, y);
/*    */ 
/* 35 */     this.width = width;
/*    */ 
/* 37 */     this.height = height;
/*    */ 
/* 39 */     this.attributes = new HashMap();
/*    */   }
/*    */ 
/*    */   public void addAttribute(String key, String value)
/*    */   {
/* 49 */     this.attributes.put(key, value);
/*    */   }
/*    */ 
/*    */   public Map getAttributes()
/*    */   {
/* 59 */     return this.attributes;
/*    */   }
/*    */ 
/*    */   public void replace(Widget w)
/*    */   {
/* 69 */     this.layer.add(w);
/*    */ 
/* 71 */     setVisible(false);
/*    */ 
/* 73 */     w.setVisible(true);
/*    */ 
/* 75 */     w.setEnabled(true);
/*    */   }
/*    */ 
/*    */   public void paint(Graphics g)
/*    */   {
/* 85 */     g.setColor(Color.red);
/*    */ 
/* 87 */     g.drawRect(0, 0, this.width, this.height);
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     duluxskin.SkinnedPlaceholder
 * JD-Core Version:    0.6.2
 */