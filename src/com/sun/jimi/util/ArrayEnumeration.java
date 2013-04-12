/*    */ package com.sun.jimi.util;
/*    */ 
/*    */ import java.util.Enumeration;
/*    */ 
/*    */ public class ArrayEnumeration
/*    */   implements Enumeration
/*    */ {
/*    */   Object[] array_;
/*    */   int idx_;
/*    */ 
/*    */   public ArrayEnumeration(Object[] paramArrayOfObject)
/*    */   {
/* 21 */     this.array_ = paramArrayOfObject;
/* 22 */     this.idx_ = 0;
/*    */   }
/*    */ 
/*    */   public boolean hasMoreElements()
/*    */   {
/* 27 */     return this.idx_ < this.array_.length;
/*    */   }
/*    */ 
/*    */   public Object nextElement()
/*    */   {
/* 32 */     return this.array_[(this.idx_++)];
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.util.ArrayEnumeration
 * JD-Core Version:    0.6.2
 */