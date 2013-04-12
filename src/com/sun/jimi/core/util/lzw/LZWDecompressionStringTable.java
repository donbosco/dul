/*     */ package com.sun.jimi.core.util.lzw;
/*     */ 
/*     */ final class LZWDecompressionStringTable
/*     */ {
/*     */   private static final int NO_PREFIX = -1;
/*     */   private static final int DEFAULT_TABLE_SIZE = 4096;
/*     */   protected byte[][] strings_;
/*  16 */   protected int size_ = 0;
/*     */   protected int codeSize_;
/*     */   protected int tableSize_;
/*     */ 
/*     */   public LZWDecompressionStringTable(int paramInt)
/*     */   {
/*  28 */     this(paramInt, 4096);
/*     */   }
/*     */ 
/*     */   public LZWDecompressionStringTable(int paramInt1, int paramInt2)
/*     */   {
/*  38 */     this.tableSize_ = paramInt2;
/*  39 */     this.codeSize_ = paramInt1;
/*  40 */     initTable();
/*     */   }
/*     */ 
/*     */   public int addCharString(int paramInt, byte paramByte)
/*     */   {
	/*  75 */     if (paramInt == -1)
	/*     */     {
	/*  77 */       this.strings_[this.size_] = new byte[] {paramByte};
	/*     */     }
	/*     */     else
	/*     */     {
	/*  81 */       int i = this.strings_[paramInt].length + 1;
	/*  82 */       byte[] arrayOfByte = new byte[i];
	/*  83 */       System.arraycopy(this.strings_[paramInt], 0, 
	/*  84 */         arrayOfByte, 0, i - 1);
	/*  85 */       arrayOfByte[(i - 1)] = paramByte;
	/*  86 */       this.strings_[this.size_] = arrayOfByte;
	/*     */     }
	/*     */ 
	/*  89 */     return this.size_++;
	/*     */   }
/*     */ 
/*     */   public void clearTable()
/*     */   {
/*  62 */     this.size_ = ((1 << this.codeSize_) + 2);
/*     */   }
/*     */ 
/*     */   public final boolean contains(int paramInt)
/*     */   {
/* 120 */     return paramInt < this.size_;
/*     */   }
/*     */ 
/*     */   public int expandCode(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 104 */     int i = this.strings_[paramInt2].length - paramInt3;
/* 105 */     int j = paramArrayOfByte.length - paramInt1;
/* 106 */     int k = j > i ? i : j;
/*     */ 
/* 108 */     System.arraycopy(this.strings_[paramInt2], paramInt3, paramArrayOfByte, paramInt1, k);
/*     */ 
/* 110 */     return i > k ? -(k + paramInt3) : k;
/*     */   }
/*     */ 
/*     */   protected void initTable()
/*     */   {
	/*  48 */     this.strings_ = new byte[this.tableSize_][this.tableSize_];//TODO: changed
	/*     */ 
	/*  50 */     int i = (1 << this.codeSize_) + 2;
	/*  51 */     for (int j = 0; j < i; j++)
	/*     */     {
	/*  53 */       addCharString(-1, (byte)j);
	/*     */     }
	/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.util.lzw.LZWDecompressionStringTable
 * JD-Core Version:    0.6.2
 */