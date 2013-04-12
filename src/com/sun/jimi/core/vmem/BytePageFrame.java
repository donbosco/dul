/*    */ package com.sun.jimi.core.vmem;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.OutputStream;
/*    */ 
/*    */ public class BytePageFrame extends PageFrame
/*    */ {
/*    */   protected static final int BUFFER_VALUES = 5120;
/*    */   protected byte[] pageData;
/* 35 */   protected static byte[] iobuffer = new byte[20480];
/*    */ 
/*    */   public BytePageFrame(int paramInt)
/*    */   {
/* 43 */     this.pageData = new byte[paramInt];
/*    */   }
/*    */ 
/*    */   public byte[] getPageData()
/*    */   {
/* 70 */     return this.pageData;
/*    */   }
/*    */ 
/*    */   public void readFrom(InputStream paramInputStream)
/*    */     throws IOException
/*    */   {
/* 61 */     paramInputStream.read(this.pageData);
/*    */   }
/*    */ 
/*    */   public void writeTo(OutputStream paramOutputStream)
/*    */     throws IOException
/*    */   {
/* 52 */     paramOutputStream.write(this.pageData);
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.vmem.BytePageFrame
 * JD-Core Version:    0.6.2
 */