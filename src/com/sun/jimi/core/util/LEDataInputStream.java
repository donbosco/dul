/*     */ package com.sun.jimi.core.util;
/*     */ 
/*     */ import java.io.DataInput;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.EOFException;
/*     */ import java.io.FilterInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ 
/*     */ public class LEDataInputStream extends FilterInputStream
/*     */   implements DataInput
/*     */ {
/*     */   DataInputStream dataIn;
/*     */ 
/*     */   public LEDataInputStream(InputStream paramInputStream)
/*     */   {
/*  42 */     super(paramInputStream);
/*  43 */     this.dataIn = new DataInputStream(paramInputStream);
/*     */   }
/*     */ 
/*     */   public void close() throws IOException
/*     */   {
/*  48 */     this.dataIn.close();
/*     */   }
/*     */ 
/*     */   public final synchronized int read(byte[] paramArrayOfByte)
/*     */     throws IOException
/*     */   {
/*  54 */     return this.dataIn.read(paramArrayOfByte, 0, paramArrayOfByte.length);
/*     */   }
/*     */ 
/*     */   public final synchronized int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException
/*     */   {
/*  59 */     int i = this.dataIn.read(paramArrayOfByte, paramInt1, paramInt2);
/*  60 */     return i;
/*     */   }
/*     */ 
/*     */   public final boolean readBoolean()
/*     */     throws IOException
/*     */   {
/*  80 */     int i = this.dataIn.read();
/*  81 */     if (i < 0)
/*  82 */       throw new EOFException();
/*  83 */     return i != 0;
/*     */   }
/*     */ 
/*     */   public final byte readByte() throws IOException
/*     */   {
/*  88 */     int i = this.dataIn.read();
/*  89 */     if (i < 0)
/*  90 */       throw new EOFException();
/*  91 */     return (byte)i;
/*     */   }
/*     */ 
/*     */   public final char readChar()
/*     */     throws IOException
/*     */   {
/* 122 */     int i = this.dataIn.read();
/* 123 */     int j = this.dataIn.read();
/* 124 */     if ((i | j) < 0)
/* 125 */       throw new EOFException();
/* 126 */     return (char)(i + (j << 8));
/*     */   }
/*     */ 
/*     */   public final double readDouble()
/*     */     throws IOException
/*     */   {
/* 154 */     return Double.longBitsToDouble(readLong());
/*     */   }
/*     */ 
/*     */   public final float readFloat()
/*     */     throws IOException
/*     */   {
/* 149 */     return Float.intBitsToFloat(readInt());
/*     */   }
/*     */ 
/*     */   public final void readFully(byte[] paramArrayOfByte)
/*     */     throws IOException
/*     */   {
/*  65 */     this.dataIn.readFully(paramArrayOfByte, 0, paramArrayOfByte.length);
/*     */   }
/*     */ 
/*     */   public final void readFully(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException
/*     */   {
/*  70 */     this.dataIn.readFully(paramArrayOfByte, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public final int readInt()
/*     */     throws IOException
/*     */   {
/* 131 */     int i = this.dataIn.read();
/* 132 */     int j = this.dataIn.read();
/* 133 */     int k = this.dataIn.read();
/* 134 */     int m = this.dataIn.read();
/* 135 */     if ((i | j | k | m) < 0)
/* 136 */       throw new EOFException();
/* 137 */     return i + (j << 8) + (k << 16) + (m << 24);
/*     */   }
/*     */ 
/*     */   public final String readLine()
/*     */     throws IOException
/*     */   {
/* 163 */     return new String();
/*     */   }
/*     */ 
/*     */   public final long readLong()
/*     */     throws IOException
/*     */   {
/* 142 */     int i = readInt();
/* 143 */     int j = readInt();
/* 144 */     return (i & 0xFFFFFFFF) + (j << 32);
/*     */   }
/*     */ 
/*     */   public final short readShort()
/*     */     throws IOException
/*     */   {
/* 104 */     int i = this.dataIn.read();
/* 105 */     int j = this.dataIn.read();
/* 106 */     if ((i | j) < 0)
/* 107 */       throw new EOFException();
/* 108 */     return (short)(i + (j << 8));
/*     */   }
/*     */ 
/*     */   public final String readUTF()
/*     */     throws IOException
/*     */   {
/* 172 */     return new String();
/*     */   }
/*     */ 
/*     */   public static final String readUTF(DataInput paramDataInput)
/*     */     throws IOException
/*     */   {
/* 181 */     return new String();
/*     */   }
/*     */ 
/*     */   public final int readUnsignedByte()
/*     */     throws IOException
/*     */   {
/*  96 */     int i = this.dataIn.read();
/*  97 */     if (i < 0)
/*  98 */       throw new EOFException();
/*  99 */     return i;
/*     */   }
/*     */ 
/*     */   public final int readUnsignedShort()
/*     */     throws IOException
/*     */   {
/* 113 */     int i = this.dataIn.read();
/* 114 */     int j = this.dataIn.read();
/* 115 */     if ((i | j) < 0)
/* 116 */       throw new EOFException();
/* 117 */     return i + (j << 8);
/*     */   }
/*     */ 
/*     */   public final int skipBytes(int paramInt)
/*     */     throws IOException
/*     */   {
/*  75 */     return this.dataIn.skipBytes(paramInt);
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.util.LEDataInputStream
 * JD-Core Version:    0.6.2
 */