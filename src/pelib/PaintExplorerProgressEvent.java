/*    */ package pelib;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import java.util.ResourceBundle;
/*    */ 
/*    */ public class PaintExplorerProgressEvent extends PaintExplorerEvent
/*    */ {
/*    */   private String description;
/*    */   private int range;
/*    */   private int value;
/*    */ 
/*    */   public PaintExplorerProgressEvent(PaintExplorer source, String description, int value, int range)
/*    */   {
/* 29 */     super(source);
/* 30 */     this.description = description;
/* 31 */     this.range = range;
/* 32 */     this.value = value;
/*    */   }
/*    */ 
/*    */   public PaintExplorerProgressEvent(PaintExplorer source)
/*    */   {
/* 37 */     super(source);
/* 38 */     this.range = -1;
/* 39 */     this.description = "";
/* 40 */     this.value = 0;
/*    */   }
/*    */ 
/*    */   public String getDescription()
/*    */   {
/* 45 */     if (this.description.length() == 0) {
/* 46 */       return "";
/*    */     }
/* 48 */     ResourceBundle bundle = ResourceBundle.getBundle("pelib.progress");
/* 49 */     return bundle.getString(this.description);
/*    */   }
/*    */ 
/*    */   public String getDescription(Locale locale)
/*    */   {
/* 54 */     if (this.description.length() == 0) {
/* 55 */       return "";
/*    */     }
/* 57 */     ResourceBundle bundle = ResourceBundle.getBundle("pelib.progress", locale);
/*    */ 
/* 59 */     return bundle.getString(this.description);
/*    */   }
/*    */ 
/*    */   public int getRange()
/*    */   {
/* 64 */     return this.range;
/*    */   }
/*    */ 
/*    */   public int getProgress()
/*    */   {
/* 69 */     return this.value;
/*    */   }
/*    */ 
/*    */   public boolean isBusy()
/*    */   {
/* 74 */     return this.range >= 0;
/*    */   }
/*    */ 
/*    */   public boolean isExtended()
/*    */   {
/* 79 */     return this.range > 0;
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.PaintExplorerProgressEvent
 * JD-Core Version:    0.6.2
 */