/*    */ package com.sun.jimi.core.decoder.png;
/*    */ 
/*    */ import java.io.DataInputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.util.Enumeration;
/*    */ 
/*    */ class IDATEnumeration
/*    */   implements Enumeration
/*    */ {
/*    */   InputStream underlyingStream;
/*    */   PNGReader owner;
/* 21 */   boolean firstStream = true;
/*    */ 
/*    */   public IDATEnumeration(PNGReader paramPNGReader)
/*    */   {
/* 25 */     this.owner = paramPNGReader;
/* 26 */     this.underlyingStream = paramPNGReader.underlyingStream_;
/*    */   }
/*    */ 
/*    */   public boolean hasMoreElements()
/*    */   {
/* 37 */     DataInputStream localDataInputStream = new DataInputStream(this.underlyingStream);
/* 38 */     if (!this.firstStream)
/*    */     {
/*    */       try
/*    */       {
/* 42 */         int i = localDataInputStream.readInt();
/* 43 */         this.owner.needChunkInfo = false;
/* 44 */         this.owner.chunkLength = localDataInputStream.readInt();
/* 45 */         this.owner.chunkType = localDataInputStream.readInt();
/*    */       }
/*    */       catch (IOException localIOException)
/*    */       {
/* 49 */         return false;
/*    */       }
/*    */     }
/* 52 */     if (this.owner.chunkType == 1229209940)
/* 53 */       return true;
/* 54 */     return false;
/*    */   }
/*    */ 
/*    */   public Object nextElement()
/*    */   {
/* 31 */     this.firstStream = false;
/* 32 */     return new MeteredInputStream(this.underlyingStream, this.owner.chunkLength);
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.decoder.png.IDATEnumeration
 * JD-Core Version:    0.6.2
 */