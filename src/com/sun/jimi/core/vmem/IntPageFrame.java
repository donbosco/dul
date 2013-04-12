/*     */ package com.sun.jimi.core.vmem;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ 
/*     */ public class IntPageFrame extends PageFrame
/*     */ {
	/*     */   protected static final int BUFFER_VALUES = 5120;
	/*     */   protected int[] pageData;
	/*  35 */   protected static byte[] iobuffer = new byte[20480];
	/*     */ 
	/*     */   public IntPageFrame(int paramInt)
	/*     */   {
	/*  43 */     this.pageData = new int[paramInt];
	/*     */   }
	/*     */ 
	/*     */   public int[] getPageData()
	/*     */   {
	/* 121 */     return this.pageData;
	/*     */   }
	/*     */ 
	/*     */   public void readFrom(InputStream paramInputStream)
	/*     */     throws IOException
	/*     */   {
	/*  95 */     int i = 0;
	/*  96 */     int j = this.pageData.length;
	/*  97 */     int k = iobuffer.length;
	/*     */     int n;
	/*     */     int i1;
	/*  98 */     for (i=0; i < this.pageData.length;i++ )
	/*     */     {
	/*  99 */       int m = (j - i) * 4;
	/* 100 */       n = k > m ? m : k;
	/*     */ 
	/* 102 */       paramInputStream.read(iobuffer, 0, n);
	/*     */ 
	/* 104 */       i1 = 0;
	/* 105 */       
	/* 106 */       this.pageData[(i++)] = 
	/* 109 */         (iobuffer[(i1++)] << 24 & 0xFF000000 | 
	/* 108 */         iobuffer[(i1++)] << 16 & 0xFF0000 | 
	/* 109 */         iobuffer[(i1++)] << 8 & 0xFF00 | 
	/* 110 */         iobuffer[(i1++)] & 0xFF);
					continue;
	/*     */     }
	/*     */   }
	/*     */ 
	/*     */   public void writeTo(OutputStream paramOutputStream)
	/*     */     throws IOException
	/*     */   {
	/*  59 */     int i = 0;
	/*  60 */     int j = this.pageData.length;
	/*  61 */     int k = iobuffer.length;
	/*     */ 
	/*  63 */     while (i < this.pageData.length)
	/*     */     {
	/*  65 */       int m = j - i;
	/*  66 */       int n = k > m ? m : k;
	/*     */ 
	/*  68 */       int i1 = 0;
	/*  69 */       while (i1 < n) {
	/*  70 */         int i2 = this.pageData[(i++)];
	/*     */ 
	/*  72 */         iobuffer[(i1++)] = ((byte)(i2 >>> 24));
	/*  73 */         iobuffer[(i1++)] = ((byte)(i2 >>> 16));
	/*  74 */         iobuffer[(i1++)] = ((byte)(i2 >>> 8));
	/*  75 */         iobuffer[(i1++)] = ((byte)i2);
	/*     */       }
	/*  77 */       paramOutputStream.write(iobuffer, 0, i1);
	/*     */     }
	/*  79 */     paramOutputStream.flush();
	/*     */   }
	/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.vmem.IntPageFrame
 * JD-Core Version:    0.6.2
 */