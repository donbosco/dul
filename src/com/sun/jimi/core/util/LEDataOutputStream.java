/*     */ package com.sun.jimi.core.util;
/*     */ 
/*     */ import java.io.DataOutput;
/*     */ import java.io.FilterOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ 
/*     */ public class LEDataOutputStream extends FilterOutputStream
/*     */   implements DataOutput
/*     */ {
/*  30 */   protected int written = 0;
/*     */ 
/*     */   public LEDataOutputStream(OutputStream paramOutputStream)
/*     */   {
/*  29 */     super(paramOutputStream);
/*     */   }
/*     */ 
/*     */   public void flush()
/*     */     throws IOException
/*     */   {
/*  47 */     this.out.flush();
/*     */   }
/*     */ 
/*     */   public final int size()
/*     */   {
/* 145 */     return this.written;
/*     */   }
/*     */ 
/*     */   public synchronized void write(int paramInt)
/*     */     throws IOException
/*     */   {
/*  35 */     this.out.write(paramInt);
/*  36 */     this.written += 1;
/*     */   }
/*     */ 
/*     */   public synchronized void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException
/*     */   {
/*  41 */     this.out.write(paramArrayOfByte, paramInt1, paramInt2);
/*  42 */     this.written += paramInt2;
/*     */   }
/*     */ 
/*     */   public final void writeBoolean(boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/*  52 */     this.out.write(paramBoolean ? 1 : 0);
/*  53 */     this.written += 1;
/*     */   }
/*     */ 
/*     */   public final void writeByte(int paramInt) throws IOException
/*     */   {
/*  58 */     this.out.write(paramInt);
/*  59 */     this.written += 1;
/*     */   }
/*     */ 
/*     */   public final void writeBytes(String paramString)
/*     */     throws IOException
/*     */   {
/* 114 */     OutputStream localOutputStream = this.out;
/* 115 */     int i = paramString.length();
/* 116 */     for (int j = 0; j < i; j++)
/*     */     {
/* 118 */       localOutputStream.write((byte)paramString.charAt(j));
/*     */     }
/* 120 */     this.written += i;
/*     */   }
/*     */ 
/*     */   public final void writeChar(int paramInt)
/*     */     throws IOException
/*     */   {
/*  72 */     OutputStream localOutputStream = this.out;
/*  73 */     localOutputStream.write(paramInt & 0xFF);
/*  74 */     localOutputStream.write(paramInt >>> 8 & 0xFF);
/*  75 */     this.written += 2;
/*     */   }
/*     */ 
/*     */   public final void writeChars(String paramString)
/*     */     throws IOException
/*     */   {
/* 125 */     OutputStream localOutputStream = this.out;
/* 126 */     int i = paramString.length();
/* 127 */     for (int j = 0; j < i; j++)
/*     */     {
/* 129 */       int k = paramString.charAt(j);
/* 130 */       localOutputStream.write(k >>> 8 & 0xFF);
/* 131 */       localOutputStream.write(k & 0xFF);
/*     */     }
/* 133 */     this.written += i * 2;
/*     */   }
/*     */ 
/*     */   public final void writeDouble(double paramDouble)
/*     */     throws IOException
/*     */   {
/* 109 */     writeLong(Double.doubleToLongBits(paramDouble));
/*     */   }
/*     */ 
/*     */   public final void writeFloat(float paramFloat)
/*     */     throws IOException
/*     */   {
/* 104 */     writeInt(Float.floatToIntBits(paramFloat));
/*     */   }
/*     */ 
/*     */   public final void writeInt(int paramInt)
/*     */     throws IOException
/*     */   {
/*  80 */     OutputStream localOutputStream = this.out;
/*  81 */     localOutputStream.write(paramInt & 0xFF);
/*  82 */     localOutputStream.write(paramInt >>> 8 & 0xFF);
/*  83 */     localOutputStream.write(paramInt >>> 16 & 0xFF);
/*  84 */     localOutputStream.write(paramInt >>> 24 & 0xFF);
/*  85 */     this.written += 4;
/*     */   }
/*     */ 
/*     */   public final void writeLong(long paramLong) throws IOException
/*     */   {
/*  90 */     OutputStream localOutputStream = this.out;
/*  91 */     localOutputStream.write((int)paramLong & 0xFF);
/*  92 */     localOutputStream.write((int)(paramLong >>> 8) & 0xFF);
/*  93 */     localOutputStream.write((int)(paramLong >>> 16) & 0xFF);
/*  94 */     localOutputStream.write((int)(paramLong >>> 24) & 0xFF);
/*  95 */     localOutputStream.write((int)(paramLong >>> 32) & 0xFF);
/*  96 */     localOutputStream.write((int)(paramLong >>> 40) & 0xFF);
/*  97 */     localOutputStream.write((int)(paramLong >>> 48) & 0xFF);
/*  98 */     localOutputStream.write((int)(paramLong >>> 56) & 0xFF);
/*  99 */     this.written += 8;
/*     */   }
/*     */ 
/*     */   public final void writeShort(int paramInt)
/*     */     throws IOException
/*     */   {
/*  64 */     OutputStream localOutputStream = this.out;
/*  65 */     localOutputStream.write(paramInt & 0xFF);
/*  66 */     localOutputStream.write(paramInt >>> 8 & 0xFF);
/*  67 */     this.written += 2;
/*     */   }
/*     */ 
/*     */   public final void writeUTF(String paramString)
/*     */     throws IOException
/*     */   {
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.util.LEDataOutputStream
 * JD-Core Version:    0.6.2
 */