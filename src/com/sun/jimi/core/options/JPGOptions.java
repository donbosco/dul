/*    */ package com.sun.jimi.core.options;
/*    */ 
/*    */ public class JPGOptions extends BasicFormatOptionSet
/*    */ {
/*    */   protected IntOption quality;
/*    */ 
/*    */   public JPGOptions()
/*    */   {
/* 26 */     this.quality = new IntOption("JPG Quality", 
/* 27 */       "Quality of JPEG image between 0 and 100 (higher is better quality)", 
/* 28 */       75, 0, 100);
/* 29 */     initWithOptions(new FormatOption[] { this.quality });
/*    */   }
/*    */ 
/*    */   public int getQuality()
/*    */   {
/* 34 */     return this.quality.getIntValue();
/*    */   }
/*    */ 
/*    */   public void setQuality(int paramInt)
/*    */     throws OptionException
/*    */   {
/* 40 */     this.quality.setIntValue(paramInt);
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.options.JPGOptions
 * JD-Core Version:    0.6.2
 */