/*    */ package pelib;
/*    */ 
/*    */ public class StackInteger
/*    */ {
/*    */   private int[] data;
/*    */   private int len;
/*    */ 
/*    */   public StackInteger(int capacity)
/*    */   {
/* 15 */     this.data = new int[capacity];
/* 16 */     this.len = 0;
/*    */   }
/*    */ 
/*    */   public StackInteger()
/*    */   {
/* 21 */     this.data = new int[10];
/* 22 */     this.len = 0;
/*    */   }
/*    */ 
/*    */   public final void push(int val)
/*    */   {
/* 27 */     if (this.len == this.data.length)
/*    */     {
/* 29 */       int[] newData = new int[this.data.length * 2];
/* 30 */       System.arraycopy(this.data, 0, newData, 0, this.data.length);
/* 31 */       this.data = newData;
/*    */     }
/* 33 */     this.data[(this.len++)] = val;
/*    */   }
/*    */ 
/*    */   public final void pushPair(int x, int y)
/*    */   {
/* 38 */     push(y);
/* 39 */     push(x);
/*    */   }
/*    */ 
/*    */   public final int pop()
/*    */   {
/* 44 */     assert (this.len > 0);
/* 45 */     return this.data[(--this.len)];
/*    */   }
/*    */ 
/*    */   public final boolean empty()
/*    */   {
/* 50 */     return this.len == 0;
/*    */   }
/*    */ 
/*    */   public final int size()
/*    */   {
/* 55 */     return this.len;
/*    */   }
/*    */ 
/*    */   public final void clear()
/*    */   {
/* 60 */     this.len = 0;
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.StackInteger
 * JD-Core Version:    0.6.2
 */