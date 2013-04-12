/*    */ package pelib;
/*    */ 
/*    */ public class VectorInteger
/*    */ {
/*    */   private int[] data;
/*    */   private int len;
/*    */ 
/*    */   public VectorInteger(int capacity)
/*    */   {
/* 16 */     this.data = new int[capacity];
/* 17 */     this.len = 0;
/*    */   }
/*    */ 
/*    */   public VectorInteger()
/*    */   {
/* 22 */     this.data = new int[10];
/* 23 */     this.len = 0;
/*    */   }
/*    */ 
/*    */   public final int[] getData()
/*    */   {
/* 28 */     return this.data;
/*    */   }
/*    */ 
/*    */   public final void add(int val)
/*    */   {
/* 33 */     if (this.len == this.data.length)
/*    */     {
/* 35 */       int[] newData = new int[this.data.length * 2];
/* 36 */       System.arraycopy(this.data, 0, newData, 0, this.data.length);
/* 37 */       this.data = newData;
/*    */     }
/* 39 */     this.data[(this.len++)] = val;
/*    */   }
/*    */ 
/*    */   public final boolean empty()
/*    */   {
/* 44 */     return this.len == 0;
/*    */   }
/*    */ 
/*    */   public final int size()
/*    */   {
/* 49 */     return this.len;
/*    */   }
/*    */ 
/*    */   public final void clear()
/*    */   {
/* 54 */     this.len = 0;
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.VectorInteger
 * JD-Core Version:    0.6.2
 */