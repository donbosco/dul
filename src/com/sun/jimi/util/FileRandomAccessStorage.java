/*     */ package com.sun.jimi.util;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.RandomAccessFile;
/*     */ 
/*     */ public final class FileRandomAccessStorage extends RandomAccessFile
/*     */   implements RandomAccessStorage
/*     */ {
/*  26 */   protected InputStream in = new InputStreamWrapper();
/*  27 */   protected OutputStream out = new OutputStreamWrapper();
/*     */   protected File file;
/*     */ 
/*     */   public FileRandomAccessStorage(File paramFile)
/*     */     throws IOException
/*     */   {
/*  32 */     super(paramFile, "rw");
/*  33 */     this.file = paramFile;
/*     */   }
/*     */ 
/*     */   public InputStream asInputStream()
/*     */   {
/*  43 */     return this.in;
/*     */   }
/*     */ 
/*     */   public OutputStream asOutputStream()
/*     */   {
/*  38 */     return this.out;
/*     */   }
/*     */ 
/*     */   protected void finalize()
/*     */   {
/*     */     try
/*     */     {
/*  63 */       close();
/*     */     }
/*     */     catch (IOException localIOException) {
/*     */     }
/*  67 */     this.file.delete();
/*     */   }
/*     */ 
/*     */   public void seek(long paramLong)
/*     */     throws IOException
/*     */   {
/*  57 */     super.seek(paramLong);
/*     */   }
/*     */ 
/*     */   public void skip(int paramInt)
/*     */     throws IOException
/*     */   {
/*  48 */     seek(getFilePointer() + paramInt);
/*     */   }
/*     */ 
/*     */   protected final class OutputStreamWrapper extends OutputStream
/*     */   {
/*  72 */     protected FileRandomAccessStorage storage = FileRandomAccessStorage.this;
/*     */ 
/*     */     protected OutputStreamWrapper() {
/*     */     }
/*  76 */     public void write(int paramInt) throws IOException { this.storage.write(paramInt); }
/*     */ 
/*     */     public void write(byte[] paramArrayOfByte)
/*     */       throws IOException
/*     */     {
/*  81 */       this.storage.write(paramArrayOfByte);
/*     */     }
/*     */ 
/*     */     public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException
/*     */     {
/*  86 */       this.storage.write(paramArrayOfByte, paramInt1, paramInt2);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected final class InputStreamWrapper extends InputStream
/*     */   {
/*  92 */     protected FileRandomAccessStorage storage = FileRandomAccessStorage.this;
/*     */ 
/*     */     protected InputStreamWrapper() {
/*     */     }
/*  96 */     public int read() throws IOException { return this.storage.read(); }
/*     */ 
/*     */     public int read(byte[] paramArrayOfByte)
/*     */       throws IOException
/*     */     {
/* 101 */       return this.storage.read(paramArrayOfByte);
/*     */     }
/*     */ 
/*     */     public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException
/*     */     {
/* 106 */       return this.storage.read(paramArrayOfByte, paramInt1, paramInt2);
/*     */     }
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.util.FileRandomAccessStorage
 * JD-Core Version:    0.6.2
 */