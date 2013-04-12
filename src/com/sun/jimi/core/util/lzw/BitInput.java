/*     */ package com.sun.jimi.core.util.lzw;
/*     */ 
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ 
/*     */ public class BitInput
/*     */ {
/*     */   InputStream in_;
/*     */   int bits_;
/*     */   int bitsCount_;
/*     */   int byteCount_;
/*     */   int numBits_;
/*     */   int numBitsMask_;
/*     */   boolean blocks_;
/*     */ 
/*     */   public BitInput(InputStream paramInputStream, boolean paramBoolean)
/*     */   {
/*  49 */     this.in_ = paramInputStream;
/*  50 */     this.blocks_ = paramBoolean;
/*     */ 
/*  52 */     this.bits_ = 0;
/*  53 */     this.bitsCount_ = 0;
/*  54 */     this.byteCount_ = 0;
/*     */   }
/*     */ 
/*     */   public void gifFinishBlocks()
/*     */     throws IOException
/*     */   {
/* 121 */     if (this.blocks_)
/*     */     {
/*     */       while (true)
/*     */       {
/* 125 */         if (this.byteCount_ == 0)
/*     */         {
/* 127 */           this.byteCount_ = this.in_.read();
/* 128 */           if (this.byteCount_ == -1)
/* 129 */             throw new EOFException();
/* 130 */           this.byteCount_ &= 255;
/*     */ 
/* 132 */           if (this.byteCount_ == 0) {
/*     */             break;
/*     */           }
/*     */         }
/* 136 */         if (this.byteCount_ != 0)
/*     */         {
/* 138 */           int i = this.in_.read();
/* 139 */           this.byteCount_ -= 1;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public int read()
/*     */     throws IOException
/*     */   {
/*  74 */     while (this.bitsCount_ < this.numBits_)
/*     */     {
/*  76 */       if (this.blocks_)
/*     */       {
/*  78 */         if (this.byteCount_ == 0)
/*     */         {
/*  80 */           this.byteCount_ = this.in_.read();
/*  81 */           if (this.byteCount_ == -1)
/*  82 */             throw new EOFException();
/*  83 */           this.byteCount_ &= 255;
/*     */         }
/*  85 */         this.byteCount_ -= 1;
/*     */       }
/*     */ 
/*  88 */       int i = this.in_.read();
/*  89 */       if (i == -1) {
/*  90 */         throw new EOFException();
/*     */       }
/*  92 */       if (this.blocks_)
/*  93 */         this.bits_ = (this.bits_ & (1 << this.bitsCount_) - 1 | (i & 0xFF) << this.bitsCount_);
/*     */       else
/*  95 */         this.bits_ = (this.bits_ << 8 & 0xFFFFFF00 | i & 0xFF);
/*  96 */       this.bitsCount_ += 8;
/*     */     }
/*     */ 
/* 100 */     int i = 0;
/* 101 */     if (this.blocks_)
/*     */     {
/* 103 */       i = this.bits_ & this.numBitsMask_;
/* 104 */       this.bits_ >>>= this.numBits_;
/*     */     }
/*     */     else {
/* 107 */       i = this.bits_ >>> this.bitsCount_ - this.numBits_ & this.numBitsMask_;
/*     */     }
/* 109 */     this.bitsCount_ -= this.numBits_;
/* 110 */     return i;
/*     */   }
/*     */ 
/*     */   public void setNumBits(int paramInt)
/*     */   {
/*  62 */     this.numBits_ = paramInt;
/*  63 */     this.numBitsMask_ = ((1 << this.numBits_) - 1);
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.util.lzw.BitInput
 * JD-Core Version:    0.6.2
 */