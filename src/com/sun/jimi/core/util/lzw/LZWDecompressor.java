/*     */ package com.sun.jimi.core.util.lzw;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ 
/*     */ public final class LZWDecompressor
/*     */ {
/*     */   private int CLEAR_CODE;
/*     */   private int END_OF_INPUT;
/*     */   protected BitInput input_;
/*     */   protected int initialCodeSize_;
/*     */   protected int codeSize_;
/*     */   protected int limit_;
/*     */   protected boolean tiff_;
/*     */   protected LZWDecompressionStringTable table_;
/*     */   protected int count_;
/*     */   protected int oldCode_;
/*     */   protected byte oldCodeFirstChar_;
/*     */   protected int leftOverCode_;
/*     */   protected int leftOverIndex_;
/*     */   protected int leftOverOldCode_;
/*     */   protected boolean isLeftOver_;
/*     */ 
/*     */   public LZWDecompressor(InputStream paramInputStream, int paramInt, boolean paramBoolean)
/*     */   {
/*  50 */     this.initialCodeSize_ = paramInt;
/*  51 */     this.tiff_ = paramBoolean;
/*     */ 
/*  53 */     this.CLEAR_CODE = (1 << paramInt);
/*  54 */     this.END_OF_INPUT = ((1 << paramInt) + 1);
/*     */ 
/*  56 */     this.table_ = new LZWDecompressionStringTable(paramInt);
/*  57 */     setInputStream(paramInputStream);
/*  58 */     clearTable();
/*     */   }
/*     */ 
/*     */   protected void clearTable()
/*     */   {
/* 249 */     this.table_.clearTable();
/* 250 */     resetCodeSize();
/*     */   }
/*     */ 
/*     */   public int decompress(byte[] paramArrayOfByte)
/*     */     throws IOException
/*     */   {
/*  82 */     int i = 0;
/*     */ 
/*  87 */     int k = writeLeftOver(paramArrayOfByte);
/*     */ 
/*  89 */     if (this.isLeftOver_)
/*     */     {
/*  91 */       return paramArrayOfByte.length;
/*     */     }
/*     */ 
/*  94 */     i += k;
/*     */ 
/*  96 */     while (i < paramArrayOfByte.length)
/*     */     {
/*  98 */       int j = getNextCode();
/*     */ 
/* 100 */       if (j == this.END_OF_INPUT)
/*     */       {
/*     */         break;
/*     */       }
/* 104 */       if (j == this.CLEAR_CODE)
/*     */       {
/* 106 */         clearTable();
/*     */ 
/* 108 */         j = getNextCode();
/* 109 */         if (j == this.END_OF_INPUT)
/*     */         {
/*     */           break;
/*     */         }
/* 113 */         paramArrayOfByte[(i++)] = ((byte)j);
/*     */ 
/* 115 */         this.oldCode_ = j;
/* 116 */         this.oldCodeFirstChar_ = ((byte)j);
/*     */       }
/*     */       else
/*     */       {
/*     */         int m;
/*     */         int n;
/* 121 */         if (this.table_.contains(j))
/*     */         {
/* 123 */           m = writeCode(paramArrayOfByte, i, j);
/*     */ 
/* 126 */           n = this.table_.addCharString(this.oldCode_, paramArrayOfByte[i]);
/*     */ 
/* 129 */           if (n == this.limit_) {
/* 130 */             incrementCodeSize();
/*     */           }
/*     */ 
/* 133 */           this.oldCode_ = j;
/* 134 */           this.oldCodeFirstChar_ = paramArrayOfByte[i];
/*     */ 
/* 137 */           if (m < 0)
/*     */           {
/* 139 */             return paramArrayOfByte.length;
/*     */           }
/*     */ 
/* 143 */           i += m;
/*     */         }
/*     */         else
/*     */         {
/* 148 */           m = this.table_.addCharString(this.oldCode_, this.oldCodeFirstChar_);
/* 149 */           n = writeCode(paramArrayOfByte, i, m);
/* 150 */           this.oldCode_ = j;
/* 151 */           this.oldCodeFirstChar_ = paramArrayOfByte[i];
/* 152 */           if (m == this.limit_)
/* 153 */             incrementCodeSize();
/* 154 */           if (n < 0) {
/* 155 */             return paramArrayOfByte.length;
/*     */           }
/*     */ 
/* 158 */           i += n;
/*     */         }
/*     */       }
/*     */     }
/* 162 */     return i;
/*     */   }
/*     */ 
/*     */   protected int getNextCode()
/*     */     throws IOException
/*     */   {
/* 238 */     int i = this.input_.read();
/*     */ 
/* 240 */     return i;
/*     */   }
/*     */ 
/*     */   public void gifFinishBlocks()
/*     */     throws IOException
/*     */   {
/* 289 */     if (!this.tiff_)
/* 290 */       this.input_.gifFinishBlocks();
/*     */   }
/*     */ 
/*     */   protected void incrementCodeSize()
/*     */   {
/* 268 */     if (this.codeSize_ != 12)
/*     */     {
/* 270 */       this.codeSize_ += 1;
/* 271 */       this.limit_ = ((1 << this.codeSize_) - 1);
/*     */ 
/* 273 */       if (this.tiff_) {
/* 274 */         this.limit_ -= 1;
/*     */       }
/* 276 */       this.input_.setNumBits(this.codeSize_);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void resetCodeSize()
/*     */   {
/* 258 */     this.count_ = 0;
/* 259 */     this.codeSize_ = this.initialCodeSize_;
/* 260 */     incrementCodeSize();
/*     */   }
/*     */ 
/*     */   public void setInputStream(InputStream paramInputStream)
/*     */   {
/*  67 */     this.input_ = new BitInput(paramInputStream, this.tiff_ ^ true);
/*  68 */     clearTable();
/*  69 */     resetCodeSize();
/*     */   }
/*     */ 
/*     */   protected int writeCode(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */   {
/* 180 */     return writeCode(paramArrayOfByte, paramInt1, paramInt2, 0);
/*     */   }
/*     */ 
/*     */   protected int writeCode(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 199 */     int i = this.table_.expandCode(paramArrayOfByte, paramInt1, paramInt2, paramInt3);
/* 200 */     if (i < 0)
/*     */     {
/* 202 */       this.leftOverCode_ = paramInt2;
/* 203 */       this.leftOverOldCode_ = this.oldCode_;
/* 204 */       this.isLeftOver_ = true;
/* 205 */       this.leftOverIndex_ = (-i);
/*     */     }
/*     */     else {
/* 208 */       this.isLeftOver_ = false;
/*     */     }
/* 210 */     return i;
/*     */   }
/*     */ 
/*     */   protected int writeLeftOver(byte[] paramArrayOfByte)
/*     */   {
/* 224 */     if (!this.isLeftOver_) {
/* 225 */       return 0;
/*     */     }
/* 227 */     int i = writeCode(paramArrayOfByte, 0, this.leftOverCode_, this.leftOverIndex_);
/*     */ 
/* 229 */     return i;
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.util.lzw.LZWDecompressor
 * JD-Core Version:    0.6.2
 */