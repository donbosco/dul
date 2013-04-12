/*    */ package com.sun.jimi.util;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ 
/*    */ public class ByteCountInputStream extends InputStream
/*    */ {
/*    */   protected InputStream in;
/*    */   protected long count;
/*    */ 
/*    */   public ByteCountInputStream(InputStream paramInputStream)
/*    */   {
/* 22 */     this.in = paramInputStream;
/* 23 */     this.count = 0L;
/*    */   }
/*    */ 
/*    */   public int available()
/*    */     throws IOException
/*    */   {
/* 73 */     return this.in.available();
/*    */   }
/*    */ 
/*    */   public void close() throws IOException
/*    */   {
/* 78 */     this.in.close();
/*    */   }
/*    */ 
/*    */   public long getCount()
/*    */   {
/* 32 */     return this.count;
/*    */   }
/*    */ 
/*    */   public synchronized void mark(int paramInt)
/*    */   {
/* 83 */     this.in.mark(paramInt);
/*    */   }
/*    */ 
/*    */   public boolean markSupported()
/*    */   {
/* 93 */     return this.in.markSupported();
/*    */   }
/*    */ 
/*    */   public int read()
/*    */     throws IOException
/*    */   {
/* 46 */     this.count += 1L;
/* 47 */     return this.in.read();
/*    */   }
/*    */ 
/*    */   public int read(byte[] paramArrayOfByte) throws IOException
/*    */   {
/* 52 */     int i = read(paramArrayOfByte, 0, paramArrayOfByte.length);
/* 53 */     this.count += i;
/* 54 */     return i;
/*    */   }
/*    */ 
/*    */   public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException
/*    */   {
/* 59 */     int i = this.in.read(paramArrayOfByte, paramInt1, paramInt2);
/* 60 */     this.count += i;
/* 61 */     return i;
/*    */   }
/*    */ 
/*    */   public synchronized void reset()
/*    */     throws IOException
/*    */   {
/* 88 */     this.in.reset();
/*    */   }
/*    */ 
/*    */   public synchronized void resetCount()
/*    */   {
/* 41 */     this.count = 0L;
/*    */   }
/*    */ 
/*    */   public long skip(long paramLong)
/*    */     throws IOException
/*    */   {
/* 66 */     long l = this.in.skip(paramLong);
/* 67 */     this.count += l;
/* 68 */     return l;
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.util.ByteCountInputStream
 * JD-Core Version:    0.6.2
 */