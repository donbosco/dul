/*    */ package com.sun.jimi.util;
/*    */ 
/*    */ public class IntegerRange
/*    */   implements Range
/*    */ {
/*    */   private final Integer Lowest;
/*    */   private final Integer Greatest;
/*    */ 
/*    */   public IntegerRange(int paramInt1, int paramInt2)
/*    */   {
/* 21 */     this.Lowest = new Integer(paramInt1);
/* 22 */     this.Greatest = new Integer(paramInt2);
/*    */   }
/*    */ 
/*    */   public IntegerRange(Integer paramInteger1, Integer paramInteger2)
/*    */   {
/* 28 */     this.Lowest = paramInteger1;
/* 29 */     this.Greatest = paramInteger2;
/*    */   }
/*    */ 
/*    */   public Object getGreatestValue()
/*    */   {
/* 38 */     return this.Greatest;
/*    */   }
/*    */ 
/*    */   public Object getLeastValue()
/*    */   {
/* 34 */     return this.Lowest;
/*    */   }
/*    */ 
/*    */   public boolean isContinuous()
/*    */   {
/* 42 */     return true;
/*    */   }
/*    */ 
/*    */   public boolean isInRange(int paramInt)
/*    */   {
/* 78 */     if (paramInt < this.Lowest.intValue())
/* 79 */       return false;
/* 80 */     if (paramInt > this.Greatest.intValue()) {
/* 81 */       return false;
/*    */     }
/* 83 */     return true;
/*    */   }
/*    */ 
/*    */   public boolean isInRange(Object paramObject)
/*    */   {
/*    */     Integer localInteger;
/*    */     try
/*    */     {
/* 51 */       localInteger = (Integer)paramObject;
/*    */     } catch (ClassCastException localClassCastException) {
/* 53 */       return false;
/*    */     }
/*    */ 
/* 57 */     if (localInteger.intValue() < this.Lowest.intValue())
/*    */     {
/* 59 */       return false;
/*    */     }
/* 61 */     if (localInteger.intValue() > this.Greatest.intValue())
/*    */     {
/* 63 */       return false;
/*    */     }
/*    */ 
/* 67 */     return true;
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.util.IntegerRange
 * JD-Core Version:    0.6.2
 */