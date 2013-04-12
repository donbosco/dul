/*    */ package pelib;
/*    */ 
/*    */ public class PaintExplorerMaskEvent extends PaintExplorerEvent
/*    */ {
/*    */   public static final int MASK_ADDED = 1;
/*    */   public static final int MASK_REMOVED = 2;
/*    */   public static final int MASK_COLOUR_CHANGED = 3;
/*    */   public static final int MASK_BLUR_CHANGED = 4;
/*    */   public static final int MASK_SELECTED = 5;
/*    */   public static final int MASK_CHOICE_ADDNEW = 6;
/*    */   public static final int MASK_USERDATA_CHANGED = 7;
/*    */   public static final int MASK_ITEMID_CHANGED = 8;
/*    */   public static final int MASK_POSITIONID_CHANGED = 9;
/*    */   private int type;
/*    */   private Mask mask;
/*    */ 
/*    */   public PaintExplorerMaskEvent(PaintExplorer source, int type, Mask mask)
/*    */   {
/* 29 */     super(source);
/* 30 */     this.type = type;
/* 31 */     this.mask = mask;
/*    */   }
/*    */ 
/*    */   public int getType()
/*    */   {
/* 36 */     return this.type;
/*    */   }
/*    */ 
/*    */   public Mask getMask()
/*    */   {
/* 41 */     return this.mask;
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 46 */     String str = "PaintExplorerMaskEvent ";
/* 47 */     switch (this.type)
/*    */     {
/*    */     case 1:
/* 50 */       str = str + "MASK_ADDED";
/* 51 */       break;
/*    */     case 2:
/* 53 */       str = str + "MASK_REMOVED";
/* 54 */       break;
/*    */     case 3:
/* 56 */       str = str + "MASK_COLOUR_CHANGED";
/* 57 */       break;
/*    */     case 4:
/* 59 */       str = str + "MASK_BLUR_CHANGED";
/* 60 */       break;
/*    */     case 5:
/* 62 */       str = str + "MASK_SELECTED";
/* 63 */       break;
/*    */     case 7:
/* 65 */       str = str + "MASK_USERDATA_CHANGED";
/* 66 */       break;
/*    */     case 8:
/* 68 */       str = str + "MASK_ITEMID_CHANGED";
/*    */     case 6:
/*    */     }
/* 71 */     str = str + " " + this.mask;
/* 72 */     return str;
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.PaintExplorerMaskEvent
 * JD-Core Version:    0.6.2
 */