/*     */ package com.sun.jimi.core.decoder.png;
/*     */ 
/*     */ import java.io.FilterInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ 
/*     */ public class MeteredInputStream extends FilterInputStream
/*     */ {
/*     */   int bytesLeft;
/*     */   int marked;
/*     */ 
/*     */   public MeteredInputStream(InputStream paramInputStream, int paramInt)
/*     */   {
/*  22 */     super(paramInputStream);
/*  23 */     this.bytesLeft = paramInt;
/*     */   }
/*     */ 
/*     */   public final int available()
/*     */     throws IOException
/*     */   {
/* 105 */     int i = this.in.available();
/* 106 */     return i > this.bytesLeft ? this.bytesLeft : i;
/*     */   }
/*     */ 
/*     */   public final void close()
/*     */     throws IOException
/*     */   {
/*     */   }
/*     */ 
/*     */   public final void mark(int paramInt)
/*     */   {
/* 129 */     this.marked = this.bytesLeft;
/* 130 */     this.in.mark(paramInt);
/*     */   }
/*     */ 
/*     */   public final boolean markSupported()
/*     */   {
/* 156 */     return this.in.markSupported();
/*     */   }
/*     */ 
/*     */   public final int read()
/*     */     throws IOException
/*     */   {
/*  34 */     if (this.bytesLeft > 0)
/*     */     {
/*  36 */       int i = this.in.read();
/*  37 */       if (i != -1)
/*  38 */         this.bytesLeft -= 1;
/*  39 */       return i;
/*     */     }
/*  41 */     return -1;
/*     */   }
/*     */ 
/*     */   public final int read(byte[] paramArrayOfByte)
/*     */     throws IOException
/*     */   {
/*  54 */     return read(paramArrayOfByte, 0, paramArrayOfByte.length);
/*     */   }
/*     */ 
/*     */   public final int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/*  72 */     if (this.bytesLeft > 0)
/*     */     {
/*  74 */       paramInt2 = paramInt2 > this.bytesLeft ? this.bytesLeft : paramInt2;
/*  75 */       int i = this.in.read(paramArrayOfByte, paramInt1, paramInt2);
/*  76 */       if (i > 0)
/*  77 */         this.bytesLeft -= i;
/*  78 */       return i;
/*     */     }
/*  80 */     return -1;
/*     */   }
/*     */ 
/*     */   public final void reset()
/*     */     throws IOException
/*     */   {
/* 147 */     this.in.reset();
/* 148 */     this.bytesLeft = this.marked;
/*     */   }
/*     */ 
/*     */   public final long skip(long paramLong)
/*     */     throws IOException
/*     */   {
/*  91 */     paramLong = paramLong > this.bytesLeft ? this.bytesLeft : paramLong;
/*  92 */     long l = this.in.skip(paramLong);
/*  93 */     if (l > 0L)
/*  94 */       this.bytesLeft = ((int)(this.bytesLeft - l));
/*  95 */     return l;
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.decoder.png.MeteredInputStream
 * JD-Core Version:    0.6.2
 */