/*    */ package com.sun.jimi.core.options;
/*    */ 
/*    */ public class PNGOptions extends BasicFormatOptionSet
/*    */ {
/*    */   protected IntOption compression;
/*    */   public static final int COMPRESSION_NONE = 0;
/*    */   public static final int COMPRESSION_DEFAULT = 1;
/*    */   public static final int COMPRESSION_FAST = 2;
/*    */   public static final int COMPRESSION_MAX = 3;
/* 31 */   private static final String[] COMPRESSION_NAMES = { "None", "Default", "Fast", "Max" };
/*    */ 
/*    */   public PNGOptions()
/*    */   {
/* 36 */     initWithOptions(createOptions());
/*    */   }
/*    */ 
/*    */   protected FormatOption[] createOptions()
/*    */   {
/* 41 */     this.compression = new IntOption("Compression", "Style of compression", 1, 
/* 42 */       0, 3);
/* 43 */     return new FormatOption[] { this.compression };
/*    */   }
/*    */ 
/*    */   public int getCompressionType()
/*    */   {
/* 48 */     return this.compression.getIntValue();
/*    */   }
/*    */ 
/*    */   public void setCompressionType(int paramInt) throws OptionException
/*    */   {
/*    */     try
/*    */     {
/* 55 */       this.compression.setIntValue(paramInt);
/*    */     }
/*    */     catch (OptionException localOptionException) {
/* 58 */       throw new OptionException("Invalid compression type: " + paramInt);
/*    */     }
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.options.PNGOptions
 * JD-Core Version:    0.6.2
 */