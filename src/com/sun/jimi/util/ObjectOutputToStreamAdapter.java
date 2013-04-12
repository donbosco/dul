/*    */ package com.sun.jimi.util;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.ObjectOutput;
/*    */ import java.io.OutputStream;
/*    */ 
/*    */ public class ObjectOutputToStreamAdapter extends OutputStream
/*    */ {
/*    */   protected ObjectOutput output_;
/*    */ 
/*    */   public ObjectOutputToStreamAdapter(ObjectOutput paramObjectOutput)
/*    */   {
/* 17 */     this.output_ = paramObjectOutput;
/*    */   }
/*    */ 
/*    */   public void close()
/*    */     throws IOException
/*    */   {
/* 37 */     this.output_.close();
/*    */   }
/*    */ 
/*    */   public void write(int paramInt)
/*    */     throws IOException
/*    */   {
/* 22 */     this.output_.write(paramInt);
/*    */   }
/*    */ 
/*    */   public void write(byte[] paramArrayOfByte) throws IOException
/*    */   {
/* 27 */     this.output_.write(paramArrayOfByte);
/*    */   }
/*    */ 
/*    */   public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException
/*    */   {
/* 32 */     this.output_.write(paramArrayOfByte, paramInt1, paramInt2);
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.util.ObjectOutputToStreamAdapter
 * JD-Core Version:    0.6.2
 */