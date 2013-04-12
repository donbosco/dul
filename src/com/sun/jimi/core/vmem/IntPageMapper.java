/*     */ package com.sun.jimi.core.vmem;
/*     */ 
/*     */ import com.sun.jimi.util.RandomAccessStorage;
/*     */ import java.awt.Dimension;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public class IntPageMapper extends PageMapper
/*     */ {
/*     */   protected static final int SIZEOF_INT = 4;
/*     */   protected IntPageFrame[] pageFrames;
/*     */   protected int width;
/*     */   protected int height;
/*     */   protected int pageWidth;
/*     */   protected int pageHeight;
/*     */   protected int widthInPages;
/*     */   protected int heightInPages;
/*     */ 
/*     */   public IntPageMapper(RandomAccessStorage paramRandomAccessStorage, Dimension paramDimension, int paramInt)
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
/*  64 */     this.pageFrames = new IntPageFrame[this.numberOfPageFrames];
/*     */ 
/*  66 */     for (int j = 0; j < this.pageFrames.length; j++)
/*  67 */       this.pageFrames[j] = new IntPageFrame(i);
/*     */   }
/*     */ 
/*     */   public int getLogicalPageNumberForLocation(int paramInt1, int paramInt2)
/*     */   {
/* 283 */     return paramInt1 / this.pageWidth + paramInt2 / this.pageHeight * this.widthInPages;
/*     */   }
/*     */ 
/*     */   public Dimension getPageDimensions()
/*     */   {
/* 311 */     return new Dimension(this.pageWidth, this.pageHeight);
/*     */   }
/*     */ 
/*     */   public PageFrame[] getPageFrames()
/*     */   {
/* 306 */     return this.pageFrames;
/*     */   }
/*     */ 
/*     */   public int getPageSize()
/*     */   {
/* 292 */     return this.pageWidth * this.pageHeight * 4;
/*     */   }
/*     */ 
/*     */   public int getPixel(int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/*  78 */     int i = getLogicalPageNumberForLocation(paramInt1, paramInt2);
/*  79 */     IntPageFrame localIntPageFrame = (IntPageFrame)getPageFrameForReading(i);
/*  80 */     int[] arrayOfInt = localIntPageFrame.getPageData();
/*     */ 
/*  82 */     return arrayOfInt[(paramInt1 % this.pageWidth + paramInt2 % this.pageHeight * this.pageWidth)];
/*     */   }
/*     */ 
/*     */   protected int getPixelSize()
/*     */   {
/* 297 */     return 4;
/*     */   }
/*     */ 
/*     */   public void getRectangle(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt, int paramInt5, int paramInt6)
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
/* 133 */         IntPageFrame localIntPageFrame = (IntPageFrame)getPageFrameForReading(i3);
/*     */ 
/* 136 */         int j = Math.min(this.pageWidth - n % this.pageWidth, m);
/*     */ 
/* 138 */         int[] arrayOfInt = localIntPageFrame.getPageData();
/*     */ 
/* 141 */         for (int i4 = 0; i4 < k; i4++) {
/* 142 */           System.arraycopy(arrayOfInt, n % this.pageWidth + (i2 % this.pageHeight + i4) * this.pageWidth, 
/* 143 */             paramArrayOfInt, i + n - paramInt1 + (i2 - paramInt2 + i4) * paramInt6, j);
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
/*     */   public void setChannelRectangle(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, byte[] paramArrayOfByte, int paramInt6, int paramInt7)
/*     */     throws IOException
/*     */   {
/* 225 */     int i = paramInt1;
/* 226 */     int j = 255 << i ^ 0xFFFFFFFF;
/*     */ 
/* 228 */     int k = paramInt6;
/*     */ 
/* 237 */     int i3 = paramInt5;
/* 238 */     int i4 = paramInt3;
/*     */ 
/* 241 */     while (i3 != 0) {
/* 242 */       int i1 = paramInt4;
/* 243 */       int i2 = paramInt2;
/* 244 */       int n = Math.min(this.pageHeight - i4 % this.pageHeight, i3);
/*     */ 
/* 246 */       int i5 = i2 % this.pageWidth;
/* 247 */       int i6 = i4 % this.pageWidth;
/*     */ 
/* 250 */       while (i1 != 0) {
/* 251 */         int i7 = getLogicalPageNumberForLocation(i2, i4);
/* 252 */         IntPageFrame localIntPageFrame = (IntPageFrame)getPageFrameForWriting(i7);
/*     */ 
/* 255 */         int m = Math.min(this.pageWidth - i2 % this.pageWidth, i1);
/*     */ 
/* 257 */         int[] arrayOfInt = localIntPageFrame.getPageData();
/*     */ 
/* 260 */         for (int i8 = 0; i8 < n; i8++) {
/* 261 */           int i9 = i2 % this.pageWidth + (i4 % this.pageHeight + i8) * this.pageWidth;
/* 262 */           int i10 = k + i2 - paramInt2 + (i4 - paramInt3 + i8) * paramInt7;
/* 263 */           for (int i11 = 0; i11 < m; i11++) {
/* 264 */             arrayOfInt[(i9 + i11)] &= j;
/* 265 */             arrayOfInt[(i9 + i11)] |= 0xFF000000 | (paramArrayOfByte[(i10 + i11)] & 0xFF) << i;
/*     */           }
/*     */         }
/*     */ 
/* 269 */         i2 += m;
/* 270 */         i1 -= m;
/*     */       }
/*     */ 
/* 273 */       i4 += n;
/* 274 */       i3 -= n;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setPixel(int paramInt1, int paramInt2, int paramInt3)
/*     */     throws IOException
/*     */   {
/*  93 */     int i = getLogicalPageNumberForLocation(paramInt1, paramInt2);
/*  94 */     IntPageFrame localIntPageFrame = (IntPageFrame)getPageFrameForWriting(i);
/*  95 */     int[] arrayOfInt = localIntPageFrame.getPageData();
/*     */ 
/*  97 */     arrayOfInt[(paramInt1 % this.pageWidth + paramInt2 % this.pageHeight * this.pageWidth)] = paramInt3;
/*     */   }
/*     */ 
/*     */   public void setRectangle(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt, int paramInt5, int paramInt6)
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
/* 188 */         IntPageFrame localIntPageFrame = (IntPageFrame)getPageFrameForWriting(i3);
/*     */ 
/* 191 */         int j = Math.min(this.pageWidth - n % this.pageWidth, m);
/*     */ 
/* 193 */         int[] arrayOfInt = localIntPageFrame.getPageData();
/*     */ 
/* 196 */         for (int i4 = 0; i4 < k; i4++) {
/* 197 */           System.arraycopy(paramArrayOfInt, i + n - paramInt1 + (i2 - paramInt2 + i4) * paramInt6, 
/* 198 */             arrayOfInt, n % this.pageWidth + (i2 % this.pageHeight + i4) * this.pageWidth, 
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
 * Qualified Name:     com.sun.jimi.core.vmem.IntPageMapper
 * JD-Core Version:    0.6.2
 */