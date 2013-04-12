/*    */ package dulux;
/*    */ 
/*    */ import org.ksoap2.serialization.PropertyInfo;
/*    */ 
/*    */ public class DuluxWebserviceResult extends LiteralArrayVector
/*    */ {
/*    */   protected String getItemDescriptor()
/*    */   {
/* 20 */     return "string";
/*    */   }
/*    */ 
/*    */   protected Class getElementClass()
/*    */   {
/* 25 */     return PropertyInfo.STRING_CLASS;
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     dulux.DuluxWebserviceResult
 * JD-Core Version:    0.6.2
 */