/*    */ package com.sun.jimi.util;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.ObjectInput;
/*    */ 
/*    */ public class ObjectInputToStreamAdapter extends InputStream
/*    */ {
/*    */   protected ObjectInput input_;
/*    */ 
/*    */   public ObjectInputToStreamAdapter(ObjectInput paramObjectInput)
/*    */   {
/* 17 */     this.input_ = paramObjectInput;
/*    */   }
/*    */ 
/*    */   public int available()
/*    */     throws IOException
/*    */   {
/* 42 */     return this.input_.available();
/*    */   }
/*    */ 
/*    */   public void close() throws IOException
/*    */   {
/* 47 */     this.input_.close();
/*    */   }
/*    */ 
/*    */   public void mark(int paramInt)
/*    */   {
/*    */   }
/*    */ 
/*    */   public boolean markSupported()
/*    */   {
/* 56 */     return false;
/*    */   }
/*    */ 
/*    */   public int read()
/*    */     throws IOException
/*    */   {
/* 22 */     return this.input_.read();
/*    */   }
/*    */ 
/*    */   public int read(byte[] paramArrayOfByte) throws IOException
/*    */   {
/* 27 */     return this.input_.read(paramArrayOfByte);
/*    */   }
/*    */ 
/*    */   public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException
/*    */   {
/* 32 */     return this.input_.read(paramArrayOfByte, paramInt1, paramInt2);
/*    */   }
/*    */ 
/*    */   public long skip(long paramLong) throws IOException
/*    */   {
/* 37 */     return this.input_.skip(paramLong);
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.util.ObjectInputToStreamAdapter
 * JD-Core Version:    0.6.2
 */