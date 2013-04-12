/*    */ package com.sun.jimi.core.vmem;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.OutputStream;
/*    */ 
/*    */ public abstract class PageFrame
/*    */ {
/*    */   protected boolean modified;
/*    */   protected int logicalPageNumber;
/*    */   protected long timeStamp;
/*    */ 
/*    */   public int getLogicalPageNumber()
/*    */   {
/* 70 */     return this.logicalPageNumber;
/*    */   }
/*    */ 
/*    */   public boolean isModified()
/*    */   {
/* 43 */     return this.modified;
/*    */   }
/*    */ 
/*    */   public synchronized long lastTouched()
/*    */   {
/* 91 */     return this.timeStamp;
/*    */   }
/*    */ 
/*    */   public abstract void readFrom(InputStream paramInputStream)
/*    */     throws IOException;
/*    */ 
/*    */   public void setLogicalPageNumber(int paramInt)
/*    */   {
/* 61 */     this.logicalPageNumber = paramInt;
/*    */   }
/*    */ 
/*    */   public void setModified(boolean paramBoolean)
/*    */   {
/* 52 */     this.modified = paramBoolean;
/*    */   }
/*    */ 
/*    */   public synchronized void touch()
/*    */   {
/* 99 */     this.timeStamp = System.currentTimeMillis();
/*    */   }
/*    */ 
/*    */   public abstract void writeTo(OutputStream paramOutputStream)
/*    */     throws IOException;
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.vmem.PageFrame
 * JD-Core Version:    0.6.2
 */