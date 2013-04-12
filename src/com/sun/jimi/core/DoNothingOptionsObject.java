/*    */ package com.sun.jimi.core;
/*    */ 
/*    */ import com.sun.jimi.util.ArrayEnumeration;
/*    */ import java.util.Enumeration;
/*    */ 
/*    */ class DoNothingOptionsObject
/*    */   implements OptionsObject
/*    */ {
/*    */   public void clearProperties()
/*    */   {
/*    */   }
/*    */ 
/*    */   public Object getPossibleValuesForProperty(String paramString)
/*    */     throws InvalidOptionException
/*    */   {
/* 33 */     throw new InvalidOptionException("No such option");
/*    */   }
/*    */ 
/*    */   public Object getProperty(String paramString)
/*    */   {
/* 17 */     return null;
/*    */   }
/*    */ 
/*    */   public String getPropertyDescription(String paramString)
/*    */     throws InvalidOptionException
/*    */   {
/* 55 */     throw new InvalidOptionException("No such option");
/*    */   }
/*    */ 
/*    */   public Enumeration getPropertyNames()
/*    */   {
/* 41 */     return new ArrayEnumeration(new String[0]);
/*    */   }
/*    */ 
/*    */   public void setProperty(String paramString, Object paramObject)
/*    */     throws InvalidOptionException
/*    */   {
/* 25 */     throw new InvalidOptionException("No such option");
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.DoNothingOptionsObject
 * JD-Core Version:    0.6.2
 */