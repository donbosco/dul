/*    */ package pelibskin;
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
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelibskin.WidgetGroup
 * JD-Core Version:    0.6.2
 */