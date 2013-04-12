/*    */ package com.sun.jimi.core.encoder.jpg;
/*    */ 
/*    */ public class Shared
/*    */ {
/* 23 */   public HuffEncode huffEncode = new HuffEncode();
/* 24 */   public Mcu mcu = new Mcu(this);
/* 25 */   public ConvertColor convertColor = new ConvertColor(this);
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.encoder.jpg.Shared
 * JD-Core Version:    0.6.2
 */