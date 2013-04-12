/*     */ package com.sun.jimi.core.decoder.tiff;
/*     */ 
/*     */ import com.sun.jimi.core.JimiException;
/*     */ import com.sun.jimi.core.util.JimiUtil;
/*     */ 
/*     */ class Decompressor
/*     */ {
/*     */   public static final int boNormal = 1;
/*     */   public static final int boReversed = 2;
/*     */   private byte[] bitOrder;
/*     */   private TiffNumberReader reader;
/*     */   int bitspersample_;
/* 154 */   private static final byte[][] reverseBytes = new byte[2][256];
/*     */ 
/* 173 */   protected boolean invertOut_ = false;
/*     */ 
/*     */   static
/*     */   {
/* 157 */     for (int i = 0; i < 256; i++)
/*     */     {
/* 159 */       reverseBytes[0][i] = ((byte)i);
/* 160 */       reverseBytes[1][i] = ((byte)
/* 167 */         ((i & 0x1) << 7 | 
/* 162 */         (i & 0x2) << 5 | 
/* 163 */         (i & 0x4) << 3 | 
/* 164 */         (i & 0x8) << 1 | 
/* 165 */         (i & 0x10) >>> 1 | 
/* 166 */         (i & 0x20) >>> 3 | 
/* 167 */         (i & 0x40) >>> 5 | 
/* 168 */         (i & 0x80) >>> 7));
/*     */     }
/*     */   }
/*     */ 
/*     */   Decompressor(TiffNumberReader paramTiffNumberReader, int paramInt)
/*     */   {
/*  55 */     this(paramTiffNumberReader, paramInt, 1);
/*     */   }
/*     */ 
/*     */   Decompressor(TiffNumberReader paramTiffNumberReader, int paramInt1, int paramInt2)
/*     */   {
/*  48 */     this.bitOrder = reverseBytes[(paramInt1 - 1)];
/*  49 */     this.reader = paramTiffNumberReader;
/*  50 */     this.bitspersample_ = paramInt2;
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
/*     */     int i;
/*     */     byte[] arrayOfByte;
/*     */     int j;
/*  85 */     switch (this.bitspersample_)
/*     */     {
/*     */     case 1:
/*  88 */       i = paramInt + 7 >> 3;
/*  89 */       arrayOfByte = paramArrayOfByte;
/*     */ 
/*  91 */       if (this.invertOut_)
/*     */       {
/*  93 */         for (j = 0; j < i; j++) {
/*  94 */           arrayOfByte[j] = ((byte)(readByte() ^ 0xFFFFFFFF));
/*     */         }
/*     */       }
/*     */       else {
/*  98 */         for (j = 0; j < i; j++)
/*  99 */           arrayOfByte[j] = readByte();
/*     */       }
/* 101 */       break;
/*     */     case 4:
/* 104 */       i = paramInt + 1 >> 1;
/* 105 */       arrayOfByte = new byte[i];
/*     */ 
/* 107 */       if (this.invertOut_)
/*     */       {
/* 109 */         for (j = 0; j < i; j++) {
/* 110 */           arrayOfByte[j] = ((byte)(readByte() ^ 0xFFFFFFFF));
/*     */         }
/*     */       }
/*     */       else {
/* 114 */         for (j = 0; j < i; j++)
/* 115 */           arrayOfByte[j] = readByte();
/*     */       }
/* 117 */       JimiUtil.expandPixels(this.bitspersample_, arrayOfByte, paramArrayOfByte, paramInt);
/* 118 */       break;
/*     */     case 8:
/* 121 */       i = paramInt;
/* 122 */       arrayOfByte = paramArrayOfByte;
/*     */ 
/* 124 */       if (this.invertOut_)
/*     */       {
/* 126 */         for (j = 0; j < i; j++)
/* 127 */           arrayOfByte[j] = ((byte)(readByte() ^ 0xFFFFFFFF));
/*     */       }
/*     */       else
/*     */       {
/* 131 */         j = 0;
/*     */         while (true) { arrayOfByte[j] = readByte();
/*     */ 
/* 131 */           j++; if (j >= i)
/*     */           {
/* 134 */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */       break;
/*     */     case 2:
/*     */     case 3:
/*     */     case 5:
/*     */     case 6:
/*     */     case 7:
/*     */     }
/*     */   }
/*     */ 
/*     */   public byte readByte()
/*     */   {
/*  64 */     return this.bitOrder[(this.reader.readByte() & 0xFF)];
/*     */   }
/*     */ 
/*     */   public void setInvert(boolean paramBoolean)
/*     */   {
/* 182 */     this.invertOut_ = paramBoolean;
/*     */   }
/*     */ 
/*     */   public void setRowsPerStrip(int paramInt)
/*     */   {
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.decoder.tiff.Decompressor
 * JD-Core Version:    0.6.2
 */