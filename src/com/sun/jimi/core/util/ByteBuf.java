/*     */ package com.sun.jimi.core.util;
/*     */ 
/*     */ class ByteBuf
/*     */ {
/*     */   byte[] buf;
/*     */   int offset;
/*     */ 
/*     */   ByteBuf(int paramInt1, int paramInt2)
/*     */   {
/* 442 */     this.offset = paramInt1;
/* 443 */     this.buf = new byte[paramInt2];
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.util.ByteBuf
 * JD-Core Version:    0.6.2
 */