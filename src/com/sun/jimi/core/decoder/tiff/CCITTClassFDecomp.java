/*    */ package com.sun.jimi.core.decoder.tiff;
/*    */ 
/*    */ class CCITTClassFDecomp extends CCITT3d1Decomp
/*    */ {
/*    */   private boolean fillBits;
/*    */   int lastRow;
/*    */   int curRow;
/* 55 */   protected byte[] pbuf = new byte[100];
/* 56 */   protected int bufVals = 0;
/*    */ 
/*    */   CCITTClassFDecomp(TiffNumberReader paramTiffNumberReader, int paramInt, boolean paramBoolean)
/*    */   {
/* 38 */     super(paramTiffNumberReader, paramInt);
/* 39 */     this.fillBits = paramBoolean;
/*    */   }
/*    */ 
/*    */   private void advancePointer(int paramInt)
/*    */   {
/* 78 */     if ((this.bitOffset != 0) && (this.bitOffset % 8 == 0)) {
/* 79 */       this.byteSource = readByte();
/*    */     }
/* 81 */     int i = this.bitOffset % 8 + paramInt;
/* 82 */     int j = i / 8;
/* 83 */     while (j-- > 0) this.byteSource = readByte();
/* 84 */     this.byteSource = ((byte)(this.byteSource << i % 8));
/* 85 */     this.bitOffset = i;
/* 86 */     this.bitOffset += paramInt;
/*    */   }
/*    */ 
/*    */   public void begOfPage()
/*    */   {
/*    */   }
/*    */ 
/*    */   public void begOfStrip()
/*    */   {
/* 49 */     super.begOfStrip();
/*    */ 
/* 52 */     readByte();
/* 53 */     readByte();
/*    */   }
/*    */ 
/*    */   public void endOfLine()
/*    */   {
/* 93 */     if (++this.curRow == this.lastRow) {
/* 94 */       return;
/*    */     }
/*    */ 
/* 97 */     this.bitOffset = 0;
/* 98 */     while (readByte() != 1);
/*    */   }
/*    */ 
/*    */   public final boolean getFillBits()
/*    */   {
/* 33 */     return this.fillBits;
/*    */   }
/*    */ 
/*    */   public void pushValue(byte paramByte)
/*    */   {
/* 68 */     this.pbuf[(this.bufVals++)] = paramByte;
/*    */   }
/*    */ 
/*    */   public byte readByte()
/*    */   {
/* 60 */     if (this.bufVals > 0) {
/* 61 */       return this.pbuf[(--this.bufVals)];
/*    */     }
/* 63 */     return super.readByte();
/*    */   }
/*    */ 
/*    */   public void setRowsPerStrip(int paramInt)
/*    */   {
/* 43 */     this.lastRow = paramInt;
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.decoder.tiff.CCITTClassFDecomp
 * JD-Core Version:    0.6.2
 */