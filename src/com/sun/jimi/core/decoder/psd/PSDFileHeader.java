/*    */ package com.sun.jimi.core.decoder.psd;
/*    */ 
/*    */ import com.sun.jimi.core.JimiException;
/*    */ import java.io.DataInputStream;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class PSDFileHeader
/*    */ {
/*    */   static final int PSD_SIGNATURE = 943870035;
/*    */   static final short BITMAP = 0;
/*    */   static final short GRAYSCALE = 1;
/*    */   static final short INDEXED = 2;
/*    */   static final short RGB = 3;
/*    */   static final short CMYK = 4;
/*    */   static final short MULTICHANNEL = 7;
/*    */   static final short DUOTONE = 8;
/*    */   static final short LAB = 9;
/*    */   int signature;
/*    */   short version;
/*    */   byte[] reserved;
/*    */   short channels;
/*    */   int rows;
/*    */   int columns;
/*    */   short depth;
/*    */   short mode;
/*    */ 
/*    */   PSDFileHeader(DataInputStream paramDataInputStream)
/*    */     throws JimiException, IOException
/*    */   {
/* 55 */     this.signature = paramDataInputStream.readInt();
/*    */ 
/* 57 */     if (this.signature != 943870035) {
/* 58 */       throw new JimiException("PSDFileHeader not a valid Photoshop file");
/*    */     }
/* 60 */     this.version = paramDataInputStream.readShort();
/*    */ 
/* 62 */     this.reserved = new byte[6];
/* 63 */     int i = paramDataInputStream.read(this.reserved, 0, 6);
/* 64 */     if (i != 6) {
/* 65 */       throw new IOException();
/*    */     }
/* 67 */     this.channels = paramDataInputStream.readShort();
/* 68 */     this.rows = paramDataInputStream.readInt();
/* 69 */     this.columns = paramDataInputStream.readInt();
/* 70 */     this.depth = paramDataInputStream.readShort();
/* 71 */     this.mode = paramDataInputStream.readShort();
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 76 */     return "s " + this.signature + " v " + this.version + 
/* 77 */       " ch " + this.channels + " r " + this.rows + 
/* 78 */       " co " + this.columns + " d " + this.depth + 
/* 79 */       " c " + this.mode;
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.decoder.psd.PSDFileHeader
 * JD-Core Version:    0.6.2
 */