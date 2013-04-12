/*    */ package com.sun.jimi.core.options;
/*    */ 
/*    */ public class SunRasterOptions extends BasicFormatOptionSet
/*    */ {
/*    */   protected BooleanOption useRLE;
/*    */ 
/*    */   public SunRasterOptions()
/*    */   {
/* 26 */     this.useRLE = new BooleanOption("Use RLE compression", 
/* 27 */       "True if Run-Length Encoding is used to compress image data.", 
/* 28 */       true);
/* 29 */     initWithOptions(new FormatOption[] { this.useRLE });
/*    */   }
/*    */ 
/*    */   public boolean isUsingRLE()
/*    */   {
/* 34 */     return this.useRLE.getBooleanValue();
/*    */   }
/*    */ 
/*    */   public void setUseRLE(boolean paramBoolean)
/*    */   {
/* 39 */     this.useRLE.setBooleanValue(paramBoolean);
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.options.SunRasterOptions
 * JD-Core Version:    0.6.2
 */