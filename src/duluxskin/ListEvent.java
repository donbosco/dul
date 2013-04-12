/*    */ package duluxskin;
/*    */ 
/*    */ import java.awt.AWTEvent;
/*    */ 
/*    */ public class ListEvent extends AWTEvent
/*    */ {
/*    */   public static final int ITEM_SELECTED = 65537;
/*    */   public static final int LIST_MODIFIED = 65538;
/*    */   public static final int ITEM_HIGHLIGHTED = 65539;
/*    */   public static final int ITEM_RENAMED = 65540;
/*    */ 
/*    */   public ListEvent(SkinnedList source, int id)
/*    */   {
/* 36 */     super(source, id);
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     duluxskin.ListEvent
 * JD-Core Version:    0.6.2
 */