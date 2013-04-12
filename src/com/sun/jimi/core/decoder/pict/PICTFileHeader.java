/*    */ package com.sun.jimi.core.decoder.pict;
/*    */ 
/*    */ import java.io.DataInputStream;
/*    */ import java.io.FilterInputStream;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class PICTFileHeader
/*    */ {
/*    */   short fileSize;
/*    */   PICTRectangle frame;
/*    */   byte verOpcode;
/*    */   byte verNumber;
/*    */   short verOpcode2;
/*    */   short verNumber2;
/*    */   PICT2Header pict2Header;
/*    */   boolean ver1;
/*    */ 
/*    */   public PICTFileHeader(DataInputStream paramDataInputStream)
/*    */     throws IOException
/*    */   {
/* 59 */     paramDataInputStream.skip(512L);
/*    */ 
/* 61 */     this.fileSize = paramDataInputStream.readShort();
/* 62 */     this.frame = new PICTRectangle(paramDataInputStream);
/* 63 */     this.verOpcode = paramDataInputStream.readByte();
/* 64 */     this.verNumber = paramDataInputStream.readByte();
/*    */ 
/* 66 */     if ((this.verOpcode == 17) && (this.verNumber == 1))
/*    */     {
/* 71 */       this.ver1 = true;
/*    */     }
/* 73 */     else if ((this.verOpcode == 0) && (this.verNumber == 17))
/*    */     {
/* 78 */       this.ver1 = false;
/* 79 */       this.verOpcode2 = 17;
/* 80 */       this.verNumber2 = paramDataInputStream.readShort();
/*    */ 
/* 82 */       if (this.verNumber2 != 767) {
/* 83 */         throw new IOException("Invalid PICT file format");
/*    */       }
/* 85 */       this.pict2Header = new PICT2Header(paramDataInputStream);
/*    */     }
/*    */     else {
/* 88 */       throw new IOException("Invalid PICT file format");
/*    */     }
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 93 */     String str = "PICT Header ";
/* 94 */     if (this.ver1)
/* 95 */       str = str + "v1";
/*    */     else
/* 97 */       str = str + "v2";
/* 98 */     return str + this.pict2Header.toString();
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.decoder.pict.PICTFileHeader
 * JD-Core Version:    0.6.2
 */