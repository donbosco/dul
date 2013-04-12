/*     */ package com.sun.jimi.core.vmem;
/*     */ 
/*     */ import com.sun.jimi.util.RandomAccessStorage;
/*     */ import java.awt.Dimension;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ 
/*     */ public abstract class PageMapper
/*     */ {
/*     */   protected RandomAccessStorage storage;
/*     */   protected Dimension logicalSize;
/*     */   protected int pageFrameMemory;
/*  59 */   protected int pageFaults = 0;
/*     */   protected OutputStream output;
/*     */   protected InputStream input;
/*     */   protected int numberOfPageFrames;
/*     */   protected Dimension pageDimensions;
/*     */ 
/*     */   public PageMapper(RandomAccessStorage paramRandomAccessStorage, Dimension paramDimension, int paramInt)
/*     */   {
/*  76 */     this.storage = paramRandomAccessStorage;
/*  77 */     this.logicalSize = paramDimension;
/*  78 */     this.pageFrameMemory = paramInt;
/*     */ 
/*  80 */     this.output = paramRandomAccessStorage.asOutputStream();
/*  81 */     this.input = paramRandomAccessStorage.asInputStream();
/*     */ 
/*  83 */     configurePageFrames();
/*     */   }
/*     */ 
/*     */   public void commitPage(PageFrame paramPageFrame)
/*     */     throws IOException
/*     */   {
/* 185 */     int i = getPageSize() * paramPageFrame.getLogicalPageNumber();
/*     */ 
/* 187 */     this.storage.seek(i);
/*     */ 
/* 189 */     paramPageFrame.writeTo(this.output);
/* 190 */     this.output.flush();
/* 191 */     paramPageFrame.setModified(false);
/*     */   }
/*     */ 
/*     */   protected void configurePageFrames()
/*     */   {
/*  91 */     this.pageDimensions = new Dimension(this.logicalSize.width / 8, 200);
/*  92 */     this.numberOfPageFrames = 9;
/*     */   }
/*     */ 
/*     */   public Dimension getLogicalSize()
/*     */   {
/* 101 */     return this.logicalSize;
/*     */   }
/*     */ 
/*     */   public PageFrame getPageFrame(int paramInt)
/*     */     throws IOException
/*     */   {
/* 142 */     PageFrame[] arrayOfPageFrame = getPageFrames();
/*     */ 
/* 145 */     long l = 9223372036854775807L;
/*     */ 
/* 147 */     int i = 0;
/* 148 */     for (int j = 0; j < arrayOfPageFrame.length; j++)
/*     */     {
/* 150 */       if (arrayOfPageFrame[j].getLogicalPageNumber() == paramInt) {
/* 151 */         arrayOfPageFrame[j].touch();
/* 152 */         return arrayOfPageFrame[j];
/*     */       }
/*     */ 
/* 156 */       if (arrayOfPageFrame[j].lastTouched() < l) {
/* 157 */         i = j;
/* 158 */         l = arrayOfPageFrame[j].lastTouched();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 165 */     this.pageFaults += 1;
/*     */ 
/* 168 */     PageFrame localPageFrame = arrayOfPageFrame[i];
/*     */ 
/* 170 */     if (localPageFrame.isModified()) {
/* 171 */       commitPage(localPageFrame);
/*     */     }
/*     */ 
/* 174 */     readPageIntoFrame(localPageFrame, paramInt);
/*     */ 
/* 176 */     return localPageFrame;
/*     */   }
/*     */ 
/*     */   public PageFrame getPageFrameForReading(int paramInt)
/*     */     throws IOException
/*     */   {
/* 111 */     return getPageFrame(paramInt);
/*     */   }
/*     */ 
/*     */   public PageFrame getPageFrameForWriting(int paramInt)
/*     */     throws IOException
/*     */   {
/* 121 */     PageFrame localPageFrame = getPageFrame(paramInt);
/*     */ 
/* 123 */     localPageFrame.setModified(true);
/* 124 */     return localPageFrame;
/*     */   }
/*     */ 
/*     */   public abstract PageFrame[] getPageFrames();
/*     */ 
/*     */   public abstract int getPageSize();
/*     */ 
/*     */   protected abstract int getPixelSize();
/*     */ 
/*     */   public int howManyPageFaults()
/*     */   {
/* 222 */     return this.pageFaults;
/*     */   }
/*     */ 
/*     */   public void readPageIntoFrame(PageFrame paramPageFrame, int paramInt)
/*     */     throws IOException
/*     */   {
/* 203 */     int i = getPageSize() * paramInt;
/*     */ 
/* 205 */     this.storage.seek(i);
/*     */     try {
/* 207 */       paramPageFrame.readFrom(this.input);
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/*     */     }
/* 212 */     paramPageFrame.setLogicalPageNumber(paramInt);
/* 213 */     paramPageFrame.setModified(false);
/* 214 */     paramPageFrame.touch();
/*     */   }
/*     */ 
/*     */   public void resetPageFaultCount()
/*     */   {
/* 230 */     this.pageFaults = 0;
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.vmem.PageMapper
 * JD-Core Version:    0.6.2
 */