/*    */ package com.sun.jimi.core.options;
/*    */ 
/*    */ public class BooleanOption extends FormatOption
/*    */ {
/*    */   protected boolean booleanValue;
/*    */ 
/*    */   public BooleanOption(String paramString1, String paramString2, boolean paramBoolean)
/*    */   {
/* 26 */     super(paramString1, paramString2, Boolean.class, new Boolean(paramBoolean));
/* 27 */     this.booleanValue = paramBoolean;
/*    */   }
/*    */ 
/*    */   public boolean getBooleanValue()
/*    */   {
/* 43 */     return this.booleanValue;
/*    */   }
/*    */ 
/*    */   public Object getValue()
/*    */   {
/* 59 */     return new Boolean(this.booleanValue);
/*    */   }
/*    */ 
/*    */   public void parseValue(String paramString)
/*    */     throws OptionException
/*    */   {
/* 33 */     setBooleanValue(Boolean.getBoolean(paramString));
/*    */   }
/*    */ 
/*    */   public void setBooleanValue(boolean paramBoolean)
/*    */   {
/* 38 */     this.booleanValue = paramBoolean;
/*    */   }
/*    */ 
/*    */   public void setValue(Object paramObject)
/*    */     throws OptionException
/*    */   {
/* 49 */     if ((paramObject instanceof Boolean)) {
/* 50 */       setBooleanValue(((Boolean)paramObject).booleanValue());
/*    */     }
/*    */     else
/* 53 */       throw new OptionException("Invalid option.");
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.options.BooleanOption
 * JD-Core Version:    0.6.2
 */