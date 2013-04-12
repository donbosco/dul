/*    */ package pelib.print;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Graphics2D;
/*    */ 
/*    */ public class DefaultPrintContentProvider
/*    */   implements PrintContentProvider
/*    */ {
/*    */   public boolean provideContentVisible(String id)
/*    */   {
/* 27 */     return true;
/*    */   }
/*    */ 
/*    */   public String provideContentText(String id)
/*    */   {
/* 37 */     return null;
/*    */   }
/*    */ 
/*    */   public boolean provideContentGraphics(String id, Graphics2D g, int x, int y, int width, int height)
/*    */   {
/* 57 */     return false;
/*    */   }
/*    */ 
/*    */   public Color provideContentFillColor(String id)
/*    */   {
/* 67 */     return null;
/*    */   }
/*    */ 
/*    */   public Color provideContentStrokeColor(String id)
/*    */   {
/* 77 */     return null;
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.print.DefaultPrintContentProvider
 * JD-Core Version:    0.6.2
 */