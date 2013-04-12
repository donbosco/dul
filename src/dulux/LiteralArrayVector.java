/*    */ package dulux;
/*    */ 
/*    */ import java.util.Hashtable;
/*    */ import java.util.Vector;
/*    */ import org.ksoap2.serialization.KvmSerializable;
/*    */ import org.ksoap2.serialization.PropertyInfo;
/*    */ import org.ksoap2.serialization.SoapSerializationEnvelope;
/*    */ 
/*    */ public abstract class LiteralArrayVector extends Vector
/*    */   implements KvmSerializable
/*    */ {
/*    */   public void register(SoapSerializationEnvelope envelope, String namespace, String name)
/*    */   {
/* 22 */     envelope.addMapping(namespace, name, getClass());
/* 23 */     registerElementClass(envelope, namespace);
/*    */   }
/*    */ 
/*    */   private void registerElementClass(SoapSerializationEnvelope envelope, String namespace) {
/* 27 */     Class elementClass = getElementClass();
/*    */     try {
/* 29 */       if ((elementClass.newInstance() instanceof KvmSerializable))
/* 30 */         envelope.addMapping(namespace, "", elementClass);
/*    */     }
/*    */     catch (Exception e) {
/* 33 */       e.printStackTrace();
/*    */     }
/*    */   }
/*    */ 
/*    */   public void getPropertyInfo(int index, Hashtable properties, PropertyInfo info) {
/* 38 */     info.name = getItemDescriptor();
/* 39 */     info.type = getElementClass();
/*    */   }
/*    */ 
/*    */   public Object getProperty(int index) {
/* 43 */     return this;
/*    */   }
/*    */ 
/*    */   public int getPropertyCount() {
/* 47 */     return 1;
/*    */   }
/*    */ 
/*    */   public void setProperty(int index, Object value) {
/* 51 */     addElement(value);
/*    */   }
/*    */ 
/*    */   protected abstract Class getElementClass();
/*    */ 
/*    */   protected String getItemDescriptor()
/*    */   {
/* 58 */     return "item";
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     dulux.LiteralArrayVector
 * JD-Core Version:    0.6.2
 */