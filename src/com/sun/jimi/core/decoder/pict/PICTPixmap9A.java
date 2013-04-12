/*     */ package com.sun.jimi.core.decoder.pict;
/*     */ 
/*     */ import com.sun.jimi.core.JimiException;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.FilterInputStream;
/*     */ import java.io.IOException;
/*     */ 
/*     */ class PICTPixmap9A
/*     */ {
/*     */   short version;
/*     */   PICTRectangle bounding;
/*     */   short packType;
/*     */   int packSize;
/*     */   int hRes;
/*     */   int vRes;
/*     */   short pixelType;
/*     */   short pixelSize;
/*     */   short compCount;
/*     */   short compSize;
/*     */   int planeBytes;
/*     */   int pmTable;
/*     */   int reserved;
/*     */ 
/*     */   PICTPixmap9A(DataInputStream paramDataInputStream)
/*     */     throws IOException, JimiException
/*     */   {
/*  58 */     paramDataInputStream.skip(4L);
/*     */ 
/*  60 */     this.version = paramDataInputStream.readShort();
/*  61 */     this.bounding = new PICTRectangle(paramDataInputStream);
/*     */ 
/*  64 */     int i = paramDataInputStream.readShort();
/*     */ 
/*  66 */     this.packType = paramDataInputStream.readShort();
/*  67 */     this.packSize = paramDataInputStream.readInt();
/*     */ 
/*  69 */     this.hRes = paramDataInputStream.readInt();
/*  70 */     this.vRes = paramDataInputStream.readInt();
/*     */ 
/*  72 */     this.pixelType = paramDataInputStream.readShort();
/*  73 */     this.pixelSize = paramDataInputStream.readShort();
/*  74 */     this.compCount = paramDataInputStream.readShort();
/*  75 */     this.compSize = paramDataInputStream.readShort();
/*     */ 
/*  77 */     this.planeBytes = paramDataInputStream.readInt();
/*  78 */     this.pmTable = paramDataInputStream.readInt();
/*  79 */     this.reserved = paramDataInputStream.readInt();
/*     */ 
/*  81 */     if (this.pixelType != 16)
/*  82 */       throw new JimiException("not RGBDirect pixmap");
/*  83 */     if ((this.compCount != 3) && (this.compCount != 4))
/*  84 */       throw new JimiException("RGBDirect requires 3 or 4 components");
/*  85 */     if (this.pixelSize == 16)
/*     */     {
/*  87 */       if (this.compSize != 5)
/*  88 */         throw new JimiException("16 bit pixels compSize is not 5 its " + this.compSize);
/*     */     }
/*  90 */     else if (this.pixelSize == 32)
/*     */     {
/*  92 */       if (this.compSize != 8)
/*  93 */         throw new JimiException("32 bit pixels compSize is not 8 its " + this.compSize);
/*     */     }
/*     */     else
/*  96 */       throw new JimiException("Pixmap 9A requires 16 or 32 bit pixels");
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 101 */     return " 9A bounding " + this.bounding.toString() + 
/* 102 */       " version " + this.version + " packType " + this.packType + 
/* 103 */       " packSize " + this.packSize + " hRes " + this.hRes + 
/* 104 */       " vRes " + this.vRes + " pixelType " + this.pixelType + 
/* 105 */       " pixelSize " + this.pixelSize + " compCount " + this.compCount + 
/* 106 */       " compSize " + this.compSize + " planeBytes " + this.planeBytes;
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.decoder.pict.PICTPixmap9A
 * JD-Core Version:    0.6.2
 */