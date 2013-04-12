/*    */ package duluxskin;
/*    */ 
/*    */ import java.awt.AWTEvent;
/*    */ 
/*    */ public class LabelEvent extends AWTEvent
/*    */ {
/*    */   public static final int ITEM_RENAMED = 1;
/*    */   public static final int ITEM_SELECTED = 2;
/*    */ 
/*    */   public LabelEvent(SkinnedLabel source, int id)
/*    */   {
/* 20 */     super(source, id);
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     duluxskin.LabelEvent
 * JD-Core Version:    0.6.2
 */