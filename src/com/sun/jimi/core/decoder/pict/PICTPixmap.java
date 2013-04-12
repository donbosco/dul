/*    */ package com.sun.jimi.core.decoder.pict;
/*    */ 
/*    */ import com.sun.jimi.core.JimiException;
/*    */ import java.io.DataInputStream;
/*    */ import java.io.IOException;
/*    */ 
/*    */ class PICTPixmap
/*    */ {
/*    */   PICTRectangle bounding;
/*    */   short version;
/*    */   short packType;
/*    */   int packSize;
/*    */   int hRes;
/*    */   int vRes;
/*    */   short pixelType;
/*    */   short pixelSize;
/*    */   short compCount;
/*    */   short compSize;
/*    */   int planeBytes;
/*    */   int pmTable;
/*    */   int reserved;
/*    */ 
/*    */   PICTPixmap(DataInputStream paramDataInputStream)
/*    */     throws IOException, JimiException
/*    */   {
/* 49 */     this.bounding = new PICTRectangle(paramDataInputStream);
/* 50 */     this.version = paramDataInputStream.readShort();
/* 51 */     this.packType = paramDataInputStream.readShort();
/*    */ 
/* 53 */     this.packSize = paramDataInputStream.readInt();
/* 54 */     this.hRes = paramDataInputStream.readInt();
/* 55 */     this.vRes = paramDataInputStream.readInt();
/*    */ 
/* 57 */     this.pixelType = paramDataInputStream.readShort();
/* 58 */     this.pixelSize = paramDataInputStream.readShort();
/* 59 */     this.compCount = paramDataInputStream.readShort();
/* 60 */     this.compSize = paramDataInputStream.readShort();
/*    */ 
/* 62 */     this.planeBytes = paramDataInputStream.readInt();
/* 63 */     this.pmTable = paramDataInputStream.readInt();
/* 64 */     this.reserved = paramDataInputStream.readInt();
/*    */ 
/* 66 */     if (this.pixelType != 0)
/* 67 */       throw new JimiException("not a chunky format pixmap");
/* 68 */     if (this.compCount != 1)
/* 69 */       throw new JimiException("invalid component count");
/* 70 */     if (this.pixelSize != this.compSize)
/* 71 */       throw new JimiException("pixel size != component size");
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 76 */     return " bounding " + this.bounding.toString() + 
/* 77 */       " version " + this.version + " packType " + this.packType + 
/* 78 */       " packSize " + this.packSize + " hRes " + this.hRes + 
/* 79 */       " vRes " + this.vRes + " pixelType " + this.pixelType + 
/* 80 */       " pixelSize " + this.pixelSize + " compCount " + this.compCount + 
/* 81 */       " compSize " + this.compSize + " planeBytes " + this.planeBytes;
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.decoder.pict.PICTPixmap
 * JD-Core Version:    0.6.2
 */