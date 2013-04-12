/*    */ package pelib;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import java.util.ResourceBundle;
/*    */ 
/*    */ public class PaintExplorerException extends Exception
/*    */ {
/*    */   public PaintExplorerException(String key)
/*    */   {
/* 16 */     super(key);
/*    */   }
/*    */ 
/*    */   public PaintExplorerException(String key, Throwable cause)
/*    */   {
/* 21 */     super(key, cause);
/*    */   }
/*    */ 
/*    */   public String getMessage()
/*    */   {
/* 26 */     String key = super.getMessage();
/* 27 */     ResourceBundle bundle = ResourceBundle.getBundle("pelib.errors");
/* 28 */     return bundle.getString(key);
/*    */   }
/*    */ 
/*    */   public String getMessage(Locale locale)
/*    */   {
/* 33 */     String key = super.getMessage();
/* 34 */     ResourceBundle bundle = ResourceBundle.getBundle("pelib.errors", locale);
/*    */ 
/* 36 */     return bundle.getString(key);
/*    */   }
/*    */ 
/*    */   public String getKey()
/*    */   {
/* 41 */     return super.getMessage();
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.PaintExplorerException
 * JD-Core Version:    0.6.2
 */