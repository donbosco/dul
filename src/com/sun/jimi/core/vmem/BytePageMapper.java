/*     */ package com.sun.jimi.core.vmem;
/*     */ 
/*     */ import com.sun.jimi.util.RandomAccessStorage;
/*     */ import java.awt.Dimension;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public class BytePageMapper extends PageMapper
/*     */ {
/*     */   protected static final int SIZEOF_BYTE = 1;
/*     */   protected BytePageFrame[] pageFrames;
/*     */   protected int width;
/*     */   protected int height;
/*     */   protected int pageWidth;
/*     */   protected int pageHeight;
/*     */   protected int widthInPages;
/*     */   protected int heightInPages;
/*     */ 
/*     */   public BytePageMapper(RandomAccessStorage paramRandomAccessStorage, Dimension paramDimension, int paramInt)
/*     */   {
/*  51 */     super(paramRandomAccessStorage, paramDimension, paramInt);
/*     */ 
/*  53 */     this.width = paramDimension.width;
/*  54 */     this.height = paramDimension.height;
/*  55 */     this.pageWidth = this.pageDimensions.width;
/*  56 */     this.pageHeight = this.pageDimensions.height;
/*     */ 
/*  59 */     this.widthInPages = (this.width % this.pageWidth == 0 ? this.width / this.pageWidth : this.width / this.pageWidth + 1);
/*  60 */     this.heightInPages = (this.height % this.pageHeight == 0 ? this.height / this.pageHeight : this.height / this.pageHeight + 1);
/*     */ 
/*  63 */     int i = this.pageWidth * this.pageHeight;
/*  64 */     this.pageFrames = new BytePageFrame[this.numberOfPageFrames];
/*     */ 
/*  66 */     for (int j = 0; j < this.pageFrames.length; j++)
/*  67 */       this.pageFrames[j] = new BytePageFrame(i);
/*     */   }
/*     */ 
/*     */   public int getLogicalPageNumberForLocation(int paramInt1, int paramInt2)
/*     */   {
/* 216 */     return paramInt1 / this.pageWidth + paramInt2 / this.pageHeight * this.widthInPages;
/*     */   }
/*     */ 
/*     */   public Dimension getPageDimensions()
/*     */   {
/* 244 */     return new Dimension(this.pageWidth, this.pageHeight);
/*     */   }
/*     */ 
/*     */   public PageFrame[] getPageFrames()
/*     */   {
/* 239 */     return this.pageFrames;
/*     */   }
/*     */ 
/*     */   public int getPageSize()
/*     */   {
/* 225 */     return this.pageWidth * this.pageHeight;
/*     */   }
/*     */ 
/*     */   public byte getPixel(int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/*  78 */     int i = getLogicalPageNumberForLocation(paramInt1, paramInt2);
/*  79 */     BytePageFrame localBytePageFrame = (BytePageFrame)getPageFrameForReading(i);
/*  80 */     byte[] arrayOfByte = localBytePageFrame.getPageData();
/*     */ 
/*  82 */     return arrayOfByte[(paramInt1 % this.pageWidth + paramInt2 % this.pageHeight * this.pageWidth)];
/*     */   }
/*     */ 
/*     */   protected int getPixelSize()
/*     */   {
/* 230 */     return 1;
/*     */   }
/*     */ 
/*     */   public void getRectangle(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte, int paramInt5, int paramInt6)
/*     */     throws IOException
/*     */   {
/* 112 */     int i = paramInt5;
/*     */ 
/* 121 */     int i1 = paramInt4;
/* 122 */     int i2 = paramInt2;
/*     */ 
/* 125 */     while (i1 != 0) {
/* 126 */       int m = paramInt3;
/* 127 */       int n = paramInt1;
/* 128 */       int k = Math.min(this.pageHeight - i2 % this.pageHeight, i1);
/*     */ 
/* 131 */       while (m != 0) {
/* 132 */         int i3 = getLogicalPageNumberForLocation(n, i2);
/* 133 */         BytePageFrame localBytePageFrame = (BytePageFrame)getPageFrameForReading(i3);
/*     */ 
/* 136 */         int j = Math.min(this.pageWidth - n % this.pageWidth, m);
/*     */ 
/* 138 */         byte[] arrayOfByte = localBytePageFrame.getPageData();
/*     */ 
/* 141 */         for (int i4 = 0; i4 < k; i4++) {
/* 142 */           System.arraycopy(arrayOfByte, n % this.pageWidth + (i2 % this.pageHeight + i4) * this.pageWidth, 
/* 143 */             paramArrayOfByte, i + n - paramInt1 + (i2 - paramInt2 + i4) * paramInt6, j);
/*     */         }
/*     */ 
/* 146 */         n += j;
/* 147 */         m -= j;
/*     */       }
/*     */ 
/* 150 */       i2 += k;
/* 151 */       i1 -= k;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setPixel(int paramInt1, int paramInt2, byte paramByte)
/*     */     throws IOException
/*     */   {
/*  93 */     int i = getLogicalPageNumberForLocation(paramInt1, paramInt2);
/*  94 */     BytePageFrame localBytePageFrame = (BytePageFrame)getPageFrameForWriting(i);
/*  95 */     byte[] arrayOfByte = localBytePageFrame.getPageData();
/*     */ 
/*  97 */     arrayOfByte[(paramInt1 % this.pageWidth + paramInt2 % this.pageHeight * this.pageWidth)] = paramByte;
/*     */   }
/*     */ 
/*     */   public void setRectangle(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte, int paramInt5, int paramInt6)
/*     */     throws IOException
/*     */   {
/* 167 */     int i = paramInt5;
/*     */ 
/* 176 */     int i1 = paramInt4;
/* 177 */     int i2 = paramInt2;
/*     */ 
/* 180 */     while (i1 != 0) {
/* 181 */       int m = paramInt3;
/* 182 */       int n = paramInt1;
/* 183 */       int k = Math.min(this.pageHeight - i2 % this.pageHeight, i1);
/*     */ 
/* 186 */       while (m != 0) {
/* 187 */         int i3 = getLogicalPageNumberForLocation(n, i2);
/* 188 */         BytePageFrame localBytePageFrame = (BytePageFrame)getPageFrameForWriting(i3);
/*     */ 
/* 191 */         int j = Math.min(this.pageWidth - n % this.pageWidth, m);
/*     */ 
/* 193 */         byte[] arrayOfByte = localBytePageFrame.getPageData();
/*     */ 
/* 196 */         for (int i4 = 0; i4 < k; i4++) {
/* 197 */           System.arraycopy(paramArrayOfByte, i + n - paramInt1 + (i2 - paramInt2 + i4) * paramInt6, 
/* 198 */             arrayOfByte, n % this.pageWidth + (i2 % this.pageHeight + i4) * this.pageWidth, 
/* 199 */             j);
/*     */         }
/*     */ 
/* 202 */         n += j;
/* 203 */         m -= j;
/*     */       }
/*     */ 
/* 206 */       i2 += k;
/* 207 */       i1 -= k;
/*     */     }
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.vmem.BytePageMapper
 * JD-Core Version:    0.6.2
 */