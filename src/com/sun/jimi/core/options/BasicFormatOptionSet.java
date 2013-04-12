/*    */ package com.sun.jimi.core.options;
/*    */ 
/*    */ import java.util.Hashtable;
/*    */ 
/*    */ public class BasicFormatOptionSet
/*    */   implements FormatOptionSet
/*    */ {
/*    */   protected Hashtable nameToOptionMap;
/*    */   protected FormatOption[] options;
/*    */ 
/*    */   public BasicFormatOptionSet()
/*    */   {
/* 39 */     this(new FormatOption[0]);
/*    */   }
/*    */ 
/*    */   public BasicFormatOptionSet(FormatOption[] paramArrayOfFormatOption)
/*    */   {
/* 34 */     initWithOptions(paramArrayOfFormatOption);
/*    */   }
/*    */ 
/*    */   public Object clone()
/*    */   {
/* 84 */     FormatOption[] arrayOfFormatOption = new FormatOption[this.options.length];
/* 85 */     for (int i = 0; i < this.options.length; i++) {
/* 86 */       arrayOfFormatOption[i] = ((FormatOption)this.options[i].clone());
/*    */     }
/* 88 */     return arrayOfFormatOption;
/*    */   }
/*    */ 
/*    */   public void copyOptionsFrom(FormatOptionSet paramFormatOptionSet)
/*    */   {
/*    */   }
/*    */ 
/*    */   public FormatOption getOption(String paramString)
/*    */     throws OptionException
/*    */   {
/* 69 */     FormatOption localFormatOption = (FormatOption)this.nameToOptionMap.get(paramString);
/* 70 */     if (localFormatOption == null) {
/* 71 */       throw new OptionException("No such option.");
/*    */     }
/*    */ 
/* 74 */     return localFormatOption;
/*    */   }
/*    */ 
/*    */   public FormatOption[] getOptions()
/*    */   {
/* 60 */     return this.options;
/*    */   }
/*    */ 
/*    */   protected void initWithOptions(FormatOption[] paramArrayOfFormatOption)
/*    */   {
/* 48 */     this.options = paramArrayOfFormatOption;
/* 49 */     this.nameToOptionMap = new Hashtable();
/* 50 */     for (int i = 0; i < paramArrayOfFormatOption.length; i++)
/* 51 */       this.nameToOptionMap.put(paramArrayOfFormatOption[i].getName(), paramArrayOfFormatOption[i]);
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.options.BasicFormatOptionSet
 * JD-Core Version:    0.6.2
 */