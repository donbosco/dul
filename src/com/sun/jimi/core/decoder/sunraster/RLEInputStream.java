/*    */ package com.sun.jimi.core.decoder.sunraster;
/*    */ 
/*    */ import java.io.FilterInputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ 
/*    */ public class RLEInputStream extends FilterInputStream
/*    */ {
/*    */   protected static final int RLE_ESCAPE = 128;
/*    */   protected int runLength_;
/*    */   protected int runValue_;
/*    */ 
/*    */   public RLEInputStream(InputStream paramInputStream)
/*    */   {
/* 18 */     super(paramInputStream);
/*    */   }
/*    */ 
/*    */   public boolean markSupported()
/*    */   {
/* 87 */     return false;
/*    */   }
/*    */ 
/*    */   public int read()
/*    */     throws IOException
/*    */   {
/* 24 */     if (this.runLength_ != 0)
/*    */     {
/* 26 */       this.runLength_ -= 1;
/* 27 */       return this.runValue_;
/*    */     }
/*    */ 
/* 31 */     int i = super.read() & 0xFF;
/*    */ 
/* 34 */     if (i == 128)
/*    */     {
/* 37 */       int j = super.read() & 0xFF;
/*    */ 
/* 40 */       if (j == 0) {
/* 41 */         return 128;
/*    */       }
/*    */ 
/* 46 */       this.runLength_ = j;
/* 47 */       this.runValue_ = (super.read() & 0xFF);
/*    */ 
/* 49 */       return this.runValue_;
/*    */     }
/*    */ 
/* 53 */     return i;
/*    */   }
/*    */ 
/*    */   public int read(byte[] paramArrayOfByte)
/*    */     throws IOException
/*    */   {
/* 75 */     return read(paramArrayOfByte, 0, paramArrayOfByte.length);
/*    */   }
/*    */ 
/*    */   public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*    */     throws IOException
/*    */   {
/* 60 */    int i = 0; 
			for (i = 0; i < paramInt2; i++)
/*    */     {
/* 62 */       int j = read();
/*    */ 
/* 64 */       if (j == -1) {
/*    */         break;
/*    */       }
/* 67 */       paramArrayOfByte[(paramInt1 + i)] = ((byte)j);
/*    */     }
/* 69 */     return i;
/*    */   }
/*    */ 
/*    */   public void skip(int paramInt)
/*    */     throws IOException
/*    */   {
/* 81 */     for (int i = 0; i < paramInt; i++)
/* 82 */       read();
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.decoder.sunraster.RLEInputStream
 * JD-Core Version:    0.6.2
 */