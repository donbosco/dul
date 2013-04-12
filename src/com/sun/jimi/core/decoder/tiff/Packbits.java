/*     */ package com.sun.jimi.core.decoder.tiff;
/*     */ 
/*     */ import com.sun.jimi.core.JimiException;
/*     */ import com.sun.jimi.core.util.JimiUtil;
/*     */ 
/*     */ class Packbits extends Decompressor
/*     */ {
/*     */   int bitsperpixel_;
/*  18 */   byte[] bitPackedBuf_ = null;
/*     */ 
/*     */   Packbits(TiffNumberReader paramTiffNumberReader, int paramInt1, int paramInt2)
/*     */   {
/*  29 */     super(paramTiffNumberReader, paramInt1);
/*  30 */     this.bitsperpixel_ = paramInt2;
/*     */   }
/*     */ 
/*     */   public void begOfPage()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void begOfStrip()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void decodeLine(byte[] paramArrayOfByte, int paramInt)
/*     */     throws JimiException
/*     */   {
/*  45 */     switch (this.bitsperpixel_)
/*     */     {
/*     */     case 4:
/*  48 */       if (this.bitPackedBuf_ == null)
/*  49 */         this.bitPackedBuf_ = new byte[(paramInt + 1) / 2];
/*  50 */       if (this.invertOut_)
/*  51 */         unpackbitsInvert(this.bitPackedBuf_);
/*     */       else
/*  53 */         unpackbits(this.bitPackedBuf_);
/*  54 */       JimiUtil.expandPixels(this.bitsperpixel_, this.bitPackedBuf_, paramArrayOfByte, paramInt);
/*  55 */       break;
/*     */     case 1:
/*     */     case 8:
/*  59 */       if (this.invertOut_)
/*  60 */         unpackbitsInvert(paramArrayOfByte);
/*     */       else
/*  62 */         unpackbits(paramArrayOfByte);
/*  63 */       break;
/*     */     case 2:
/*     */     case 3:
/*     */     case 5:
/*     */     case 6:
/*     */     case 7:
/*     */     }
/*     */   }
/*     */ 
/*     */   public void unpackbits(byte[] paramArrayOfByte)
/*     */   {
/*  90 */     int i = 0;
/*  91 */     for (i = 0; i < paramArrayOfByte.length; )
/*     */     {
/*  93 */       int j = readByte();
/*  94 */       if (j >= 0)
/*     */       {
/*  96 */         j++;
/*  97 */         for (int n = 0; n < j; n++)
/*  98 */           paramArrayOfByte[(i + n)] = readByte();
/*  99 */         i += j;
/*     */       }
/* 101 */       else if (j != -128)
/*     */       {
/* 103 */         int k = readByte();
/* 104 */         int m = i - j + 1;
/* 105 */         for (; i < m; i++)
/* 106 */           paramArrayOfByte[i] = (byte) k;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void unpackbitsInvert(byte[] paramArrayOfByte)
/*     */   {
/* 120 */     int i = 0;
/* 121 */     for (i = 0; i < paramArrayOfByte.length; )
/*     */     {
/* 123 */       int j = readByte();
/* 124 */       if (j >= 0)
/*     */       {
/* 126 */         j++;
/* 127 */         for (int n = 0; n < j; n++)
/* 128 */           paramArrayOfByte[(i + n)] = ((byte)(readByte() ^ 0xFFFFFFFF));
/* 129 */         i += j;
/*     */       }
/* 131 */       else if (j != -128)
/*     */       {
/* 133 */         int k = readByte();
/* 134 */         int m = i - j + 1;
/* 135 */         k = (byte)(k ^ 0xFFFFFFFF);
/* 136 */         for (; i < m; i++)
/* 137 */           paramArrayOfByte[i] = (byte) k;
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.decoder.tiff.Packbits
 * JD-Core Version:    0.6.2
 */