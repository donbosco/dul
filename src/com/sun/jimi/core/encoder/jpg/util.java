/*    */ package com.sun.jimi.core.encoder.jpg;
/*    */ 
/*    */ public final class util
/*    */ {
/*    */   public static void errexit(String paramString)
/*    */   {
/*  5 */     throw new RuntimeException("Error during encoding: " + paramString);
/*    */   }
/*    */ 
/*    */   public static int roundUp(int paramInt1, int paramInt2) {
/*  9 */     paramInt1 += paramInt2 - 1;
/* 10 */     return paramInt1 - paramInt1 % paramInt2;
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.encoder.jpg.util
 * JD-Core Version:    0.6.2
 */