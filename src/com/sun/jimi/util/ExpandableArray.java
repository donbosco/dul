/*    */ package com.sun.jimi.util;
/*    */ 
/*    */ public class ExpandableArray
/*    */ {
/*    */   Object[] elementData_;
/*    */   int capacityIncrement_;
/*    */   protected int elementCount_;
/*    */ 
/*    */   public ExpandableArray()
/*    */   {
/* 46 */     this(10, 5);
/*    */   }
/*    */ 
/*    */   public ExpandableArray(int paramInt)
/*    */   {
/* 41 */     this(paramInt, 5);
/*    */   }
/*    */ 
/*    */   public ExpandableArray(int paramInt1, int paramInt2)
/*    */   {
/* 34 */     this.elementCount_ = 0;
/* 35 */     this.elementData_ = new Object[paramInt1];
/* 36 */     this.capacityIncrement_ = paramInt2;
/*    */   }
/*    */ 
/*    */   public int addElement(Object paramObject)
/*    */   {
/* 59 */     if (this.elementCount_ == this.elementData_.length)
/*    */     {
/* 61 */       Object[] arrayOfObject = new Object[this.elementCount_ + this.capacityIncrement_];
/* 62 */       System.arraycopy(this.elementData_, 0, arrayOfObject, 0, this.elementCount_);
/* 63 */       this.elementData_ = arrayOfObject;
/*    */     }
/* 65 */     this.elementData_[(this.elementCount_++)] = paramObject;
/* 66 */     return this.elementCount_ - 1;
/*    */   }
/*    */ 
/*    */   public Object elementAt(int paramInt)
/*    */   {
/* 71 */     if (paramInt >= this.elementCount_)
/* 72 */       throw new ArrayIndexOutOfBoundsException(paramInt + " >= " + this.elementCount_);
/* 73 */     return this.elementData_[paramInt];
/*    */   }
/*    */ 
/*    */   public Object lastElement()
/*    */   {
/* 93 */     return elementAt(this.elementCount_ - 1);
/*    */   }
/*    */ 
/*    */   public void removeElementAt(int paramInt)
/*    */   {
/* 78 */     if (paramInt >= this.elementCount_) {
/* 79 */       throw new ArrayIndexOutOfBoundsException(paramInt + " >= " + this.elementCount_);
/*    */     }
/* 81 */     int i = this.elementCount_ - paramInt - 1;
/* 82 */     if (i > 0)
/*    */     {
/* 84 */       System.arraycopy(this.elementData_, paramInt + 1, this.elementData_, paramInt, i);
/*    */     }
/* 86 */     this.elementCount_ -= 1;
/* 87 */     this.elementData_[this.elementCount_] = null;
/*    */   }
/*    */ 
/*    */   public int size()
/*    */   {
/* 51 */     return this.elementCount_;
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.util.ExpandableArray
 * JD-Core Version:    0.6.2
 */