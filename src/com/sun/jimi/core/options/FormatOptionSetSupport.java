/*    */ package com.sun.jimi.core.options;
/*    */ 
/*    */ import java.util.Hashtable;
/*    */ 
/*    */ public abstract class FormatOptionSetSupport
/*    */ {
/*    */   protected Hashtable nameToOptionMap;
/*    */   protected FormatOption[] options;
/*    */ 
/*    */   public FormatOption getOption(String paramString)
/*    */     throws OptionException
/*    */   {
/* 54 */     FormatOption localFormatOption = (FormatOption)this.nameToOptionMap.get(paramString);
/* 55 */     if (localFormatOption == null) {
/* 56 */       throw new OptionException("No such option.");
/*    */     }
/*    */ 
/* 59 */     return localFormatOption;
/*    */   }
/*    */ 
/*    */   public FormatOption[] getOptions()
/*    */   {
/* 48 */     return this.options;
/*    */   }
/*    */ 
/*    */   protected void initWithOptions(FormatOption[] paramArrayOfFormatOption)
/*    */   {
/* 39 */     this.options = paramArrayOfFormatOption;
/* 40 */     this.nameToOptionMap = new Hashtable();
/* 41 */     for (int i = 0; i < paramArrayOfFormatOption.length; i++)
/* 42 */       this.nameToOptionMap.put(paramArrayOfFormatOption[i].getName(), paramArrayOfFormatOption[i]);
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.options.FormatOptionSetSupport
 * JD-Core Version:    0.6.2
 */