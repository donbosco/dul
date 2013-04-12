/*    */ package com.sun.jimi.util;
/*    */ 
/*    */ import com.sun.jimi.core.InvalidOptionException;
/*    */ import java.util.Enumeration;
/*    */ 
/*    */ public abstract interface PropertyOwner
/*    */ {
/*    */   public static final String ANY_VALUE_STRING = "This property may take any value that is a string.  This value may be subject to interpretation based upon the context.  Illogical values will be ignored.";
/* 25 */   public static final Boolean[] BOOLEAN_ARRAY = { Boolean.TRUE, Boolean.FALSE };
/*    */ 
/*    */   public abstract void clearProperties();
/*    */ 
/*    */   public abstract Object getPossibleValuesForProperty(String paramString)
/*    */     throws InvalidOptionException;
/*    */ 
/*    */   public abstract Object getProperty(String paramString);
/*    */ 
/*    */   public abstract String getPropertyDescription(String paramString)
/*    */     throws InvalidOptionException;
/*    */ 
/*    */   public abstract Enumeration getPropertyNames();
/*    */ 
/*    */   public abstract void setProperty(String paramString, Object paramObject)
/*    */     throws InvalidOptionException;
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.util.PropertyOwner
 * JD-Core Version:    0.6.2
 */