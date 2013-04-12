/*    */ package duluxskin;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import java.util.Vector;
/*    */ 
/*    */ public class WidgetGroup
/*    */ {
/*    */   private Vector widgets;
/*    */ 
/*    */   public WidgetGroup()
/*    */   {
/* 31 */     this.widgets = new Vector();
/*    */   }
/*    */ 
/*    */   public void add(Widget widget)
/*    */   {
/* 41 */     this.widgets.add(widget);
/*    */   }
/*    */ 
/*    */   public void setEnabled(boolean enabled)
/*    */   {
/* 51 */     for (Iterator it = this.widgets.iterator(); it.hasNext(); )
/*    */     {
/* 53 */       ((Widget)it.next()).setEnabled(enabled);
/*    */     }
/*    */   }
/*    */ 
/*    */   public void setVisible(boolean visible)
/*    */   {
/* 59 */     for (Iterator it = this.widgets.iterator(); it.hasNext(); )
/*    */     {
/* 61 */       ((Widget)it.next()).setVisible(visible);
/*    */     }
/*    */   }
/*    */ 
/*    */   public Iterator getIterator() {
/* 66 */     return this.widgets.iterator();
/*    */   }
/*    */ 
/*    */   public boolean contains(Widget widget) {
/* 70 */     return this.widgets.contains(widget);
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     duluxskin.WidgetGroup
 * JD-Core Version:    0.6.2
 */