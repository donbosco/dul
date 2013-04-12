/*     */ package com.sun.jimi.core.decoder.tiff;
/*     */ 
/*     */ import java.util.Enumeration;
/*     */ import java.util.NoSuchElementException;
/*     */ 
/*     */ class TIFFieldEnumerator
/*     */   implements Enumeration
/*     */ {
/*     */   IFD ifd;
/*     */   int idx;
/*     */ 
/*     */   TIFFieldEnumerator(IFD paramIFD)
/*     */   {
/*  88 */     this.ifd = paramIFD;
/*  89 */     this.idx = 0;
/*     */   }
/*     */ 
/*     */   public boolean hasMoreElements()
/*     */   {
/*  94 */     return this.idx < this.ifd.count;
/*     */   }
/*     */ 
/*     */   public Object nextElement()
/*     */   {
/*  99 */     if (this.idx < this.ifd.count)
/* 100 */       return this.ifd.fields[(this.idx++)];
/* 101 */     throw new NoSuchElementException("TIFFieldEnumerator");
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.decoder.tiff.TIFFieldEnumerator
 * JD-Core Version:    0.6.2
 */