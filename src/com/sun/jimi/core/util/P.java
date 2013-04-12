/*    */ package com.sun.jimi.core.util;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ 
/*    */ public class P
/*    */ {
/* 12 */   public static boolean debug = true;
/*    */ 
/*    */   public static void dump(byte[] paramArrayOfByte)
/*    */   {
/* 39 */     for (int i = 0; i < paramArrayOfByte.length; i++)
/*    */     {
/* 41 */       rtN("x" + Integer.toHexString(paramArrayOfByte[i] & 0xFF) + " ");
/* 42 */       if ((i & 0xF) == 15)
/* 43 */         rt("");
/*    */     }
/*    */   }
/*    */ 
/*    */   public static void dump(int[] paramArrayOfInt)
/*    */   {
/* 50 */     for (int i = 0; i < paramArrayOfInt.length; i++)
/*    */     {
/* 52 */       rtN("x" + Integer.toHexString(paramArrayOfInt[i]) + " ");
/* 53 */       if ((i & 0xF) == 15)
/* 54 */         rt("");
/*    */     }
/*    */   }
/*    */ 
/*    */   public static void dumpd(byte[] paramArrayOfByte)
/*    */   {
/* 60 */     if (debug)
/* 61 */       dump(paramArrayOfByte);
/*    */   }
/*    */ 
/*    */   static void pause(int paramInt) {
/*    */     try {
/* 66 */       Thread.sleep(paramInt);
/*    */     }
/*    */     catch (InterruptedException localInterruptedException)
/*    */     {
/*    */     }
/*    */   }
/*    */ 
/*    */   public static void rt(String paramString)
/*    */   {
/* 16 */     System.out.println(paramString);
/*    */   }
/*    */ 
/*    */   public static void rtN(String paramString)
/*    */   {
/* 21 */     System.out.print(paramString);
/*    */   }
/*    */ 
/*    */   public static void rtd(String paramString)
/*    */   {
/* 26 */     if (debug)
/* 27 */       System.out.println(paramString);
/*    */   }
/*    */ 
/*    */   public static void rtdN(String paramString)
/*    */   {
/* 32 */     if (debug)
/* 33 */       System.out.print(paramString);
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.util.P
 * JD-Core Version:    0.6.2
 */