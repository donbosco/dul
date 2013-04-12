/*     */ package com.sun.jimi.core.options;
/*     */ 
/*     */ public class FormatOption
/*     */   implements Cloneable
/*     */ {
/*     */   protected String name;
/*     */   protected String description;
/*     */   protected Object value;
/*     */   protected Object possibleValues;
/*     */   protected Object defaultValue;
/*     */ 
/*     */   public FormatOption(String paramString1, String paramString2, Object paramObject1, Object paramObject2)
/*     */   {
/*  39 */     this.name = paramString1;
/*  40 */     this.description = paramString2;
/*  41 */     this.possibleValues = paramObject1;
/*  42 */     this.value = (this.defaultValue = paramObject2);
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/*     */     try
/*     */     {
/* 115 */       return super.clone();
/*     */     }
/*     */     catch (Exception localException) {
/*     */     }
/* 119 */     return this;
/*     */   }
/*     */ 
/*     */   public Object getDefaultValue()
/*     */   {
/* 106 */     return this.value;
/*     */   }
/*     */ 
/*     */   public String getDescription()
/*     */   {
/*  61 */     return this.description;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/*  51 */     return this.name;
/*     */   }
/*     */ 
/*     */   public Object getPossibleValues()
/*     */   {
/*  70 */     return this.possibleValues;
/*     */   }
/*     */ 
/*     */   public Object getValue()
/*     */   {
/*  79 */     return this.value;
/*     */   }
/*     */ 
/*     */   public void parseValue(String paramString)
/*     */     throws OptionException
/*     */   {
/*  98 */     setValue(paramString);
/*     */   }
/*     */ 
/*     */   public void setValue(Object paramObject)
/*     */     throws OptionException
/*     */   {
/*  88 */     this.value = paramObject;
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.options.FormatOption
 * JD-Core Version:    0.6.2
 */