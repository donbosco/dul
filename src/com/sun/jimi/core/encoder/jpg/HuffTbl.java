/*    */ package com.sun.jimi.core.encoder.jpg;
/*    */ 
/*    */ public class HuffTbl
/*    */ {
/*  8 */   short[] bits = new short[17];
/*    */ 
/* 10 */   short[] huffval = new short[256];
/*    */   boolean sent_table;
/* 20 */   short[] ehufco = new short[256];
/* 21 */   short[] ehufsi = new short[256];
/*    */ 
/* 23 */   short[] mincode = new short[17];
/* 24 */   int[] maxcode = new int[18];
/*    */ 
/* 26 */   short[] valptr = new short[17];
/*    */ 
/*    */   public HuffTbl(short[] paramArrayOfShort1, short[] paramArrayOfShort2)
/*    */   {
/* 30 */     int j = paramArrayOfShort1.length;
/* 31 */     int k = paramArrayOfShort2.length;
/*    */ 
/* 34 */     for (int i = 0; i < j; i++)
/* 35 */       this.bits[i] = paramArrayOfShort1[i];
/* 36 */     for (int i = 0; i < k; i++) {
/* 37 */       this.huffval[i] = paramArrayOfShort2[i];
/*    */     }
/* 39 */     this.sent_table = false;
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.encoder.jpg.HuffTbl
 * JD-Core Version:    0.6.2
 */