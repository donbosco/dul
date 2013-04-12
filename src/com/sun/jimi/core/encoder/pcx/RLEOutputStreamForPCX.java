/*     */ package com.sun.jimi.core.encoder.pcx;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ 
/*     */ public class RLEOutputStreamForPCX extends OutputStream
/*     */ {
/*     */   protected static final byte ONEFLAG = -64;
/*     */   protected static final byte ZEROFLAG = 63;
/*     */   protected OutputStream out_;
/*     */   protected byte runValue_;
/*  26 */   protected boolean endOfLine = false;
/*     */   protected int runCount_;
/*     */ 
/*     */   public RLEOutputStreamForPCX(OutputStream paramOutputStream)
/*     */   {
/*  37 */     this.out_ = paramOutputStream;
/*     */   }
/*     */ 
/*     */   protected synchronized void compressBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/*  48 */     int i = paramInt2;
/*  49 */     int j = this.runCount_;
/*  50 */     byte b1 = this.runValue_;
/*     */ 
/*  52 */     OutputStream localOutputStream = this.out_;
/*     */ 
/*  65 */     j = 0;
/*  66 */     b1 = paramArrayOfByte[paramInt1];
/*     */ 
/*  68 */     for (int k = paramInt1; k < i; k++)
/*     */     {
/*  70 */       byte b2 = paramArrayOfByte[k];
/*     */ 
/*  73 */       if ((b2 == b1) && (j < 63) && (k != i - 1))
/*     */       {
/*  76 */         j++;
/*     */       }
/*     */       else
/*     */       {
/*  81 */         writeRun(j, b1);
/*     */ 
/*  83 */         b1 = b2;
/*  84 */         j = 1;
/*     */       }
/*     */     }
/*     */ 
/*  88 */     this.runCount_ = j;
/*  89 */     this.runValue_ = b1;
/*     */   }
/*     */ 
/*     */   public void flush()
/*     */     throws IOException
/*     */   {
/* 126 */     writeRun();
/* 127 */     this.runCount_ = 0;
/* 128 */     this.runValue_ = -1;
/*     */   }
/*     */ 
/*     */   public void write(int paramInt)
/*     */     throws IOException
/*     */   {
/* 121 */     write(new byte[] { (byte)paramInt });
/*     */   }
/*     */ 
/*     */   public void write(byte[] paramArrayOfByte)
/*     */     throws IOException
/*     */   {
/*  98 */     write(paramArrayOfByte, 0, paramArrayOfByte.length);
/*     */   }
/*     */ 
/*     */   public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 111 */     compressBytes(paramArrayOfByte, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   protected void writeRun()
/*     */     throws IOException
/*     */   {
/* 147 */     writeRun(this.runCount_, this.runValue_);
/*     */   }
/*     */ 
/*     */   protected void writeRun(int paramInt, byte paramByte)
/*     */     throws IOException
/*     */   {
/* 135 */     int i = (byte)paramInt;
/*     */ 
/* 138 */     i = (byte)(i | 0xFFFFFFC0);
/* 139 */     this.out_.write(i);
/* 140 */     this.out_.write(paramByte);
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.encoder.pcx.RLEOutputStreamForPCX
 * JD-Core Version:    0.6.2
 */