/*    */ package com.sun.jimi.core.options;
/*    */ 
/*    */ public class IntOption extends FormatOption
/*    */ {
/*    */   protected int intValue;
/* 24 */   protected boolean bounded = false;
/*    */   protected int min;
/*    */   protected int max;
/*    */ 
/*    */   public IntOption(String paramString1, String paramString2, int paramInt)
/*    */   {
/* 32 */     super(paramString1, paramString2, Integer.class, new Integer(paramInt));
/* 33 */     this.intValue = paramInt;
/*    */   }
/*    */ 
/*    */   public IntOption(String paramString1, String paramString2, int paramInt1, int paramInt2, int paramInt3)
/*    */   {
/* 43 */     this(paramString1, paramString2, paramInt1);
/* 44 */     this.bounded = true;
/* 45 */     this.min = paramInt2;
/* 46 */     this.max = paramInt3;
/*    */   }
/*    */ 
/*    */   public int getIntValue()
/*    */   {
/* 87 */     return this.intValue;
/*    */   }
/*    */ 
/*    */   public Object getValue()
/*    */   {
/* 73 */     return new Integer(this.intValue);
/*    */   }
/*    */ 
/*    */   public void parseValue(String paramString)
/*    */     throws OptionException
/*    */   {
/*    */     try
/*    */     {
/* 53 */       setIntValue(Integer.parseInt(paramString));
/*    */     }
/*    */     catch (Exception localException) {
/* 56 */       throw new OptionException("Invalid option: " + paramString);
/*    */     }
/*    */   }
/*    */ 
/*    */   public void setIntValue(int paramInt)
/*    */     throws OptionException
/*    */   {
/* 79 */     if ((this.bounded) && ((paramInt < this.min) || (paramInt > this.max))) {
/* 80 */       throw new OptionException("Value out of bounds.");
/*    */     }
/* 82 */     this.intValue = paramInt;
/*    */   }
/*    */ 
/*    */   public void setValue(Object paramObject)
/*    */     throws OptionException
/*    */   {
/* 63 */     if ((paramObject instanceof Number)) {
/* 64 */       setIntValue(((Number)paramObject).intValue());
/*    */     }
/*    */     else
/* 67 */       throw new OptionException("Invalid option: " + paramObject);
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.options.IntOption
 * JD-Core Version:    0.6.2
 */