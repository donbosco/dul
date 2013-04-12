/*    */ package com.sun.jimi.core.decoder.pict;
/*    */ 
/*    */ import java.io.DataInputStream;
/*    */ import java.io.IOException;
/*    */ 
/*    */ class PICT2Header
/*    */ {
/*    */   short opcode;
/*    */   short version;
/*    */   short reserved;
/*    */   int origHoriz;
/*    */   int origVert;
/*    */   PICTRectangle frame;
/*    */   int reserved2;
/*    */ 
/*    */   public PICT2Header(DataInputStream paramDataInputStream)
/*    */     throws IOException
/*    */   {
/* 36 */     this.opcode = paramDataInputStream.readShort();
/* 37 */     if (this.opcode != 3072) {
/* 38 */       throw new IOException("Pict Version 2 Header opcode not 0xC00 " + this.opcode);
/*    */     }
/* 40 */     this.version = paramDataInputStream.readShort();
/*    */ 
/* 45 */     this.reserved = paramDataInputStream.readShort();
/* 46 */     this.origHoriz = paramDataInputStream.readInt();
/* 47 */     this.origVert = paramDataInputStream.readInt();
/* 48 */     this.frame = new PICTRectangle(paramDataInputStream);
/* 49 */     this.reserved2 = paramDataInputStream.readInt();
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 54 */     return "opcode " + Integer.toHexString(this.opcode & 0xFFFF) + 
/* 55 */       " version " + Integer.toHexString(this.version & 0xFFFF) + 
/* 56 */       " frame " + this.frame.toString();
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.decoder.pict.PICT2Header
 * JD-Core Version:    0.6.2
 */