/*    */ package com.sun.jimi.core.encoder.png;
/*    */ 
/*    */ import java.io.DataOutputStream;
/*    */ import java.io.FilterOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.util.zip.CRC32;
/*    */ 
/*    */ class PNGChunkUtil
/*    */ {
/* 17 */   protected CRC32 crcEngine = new CRC32();
/*    */ 
/*    */   int getCRC()
/*    */   {
/* 21 */     return (int)this.crcEngine.getValue();
/*    */   }
/*    */ 
/*    */   void resetCRC()
/*    */   {
/* 26 */     this.crcEngine.reset();
/*    */   }
/*    */ 
/*    */   void updateCRC(byte paramByte)
/*    */   {
/* 45 */     this.crcEngine.update(paramByte);
/*    */   }
/*    */ 
/*    */   void updateCRC(int paramInt)
/*    */   {
/* 31 */     this.crcEngine.update((paramInt & 0xFF000000) >> 24);
/* 32 */     this.crcEngine.update((paramInt & 0xFF0000) >> 16);
/* 33 */     this.crcEngine.update((paramInt & 0xFF00) >> 8);
/* 34 */     this.crcEngine.update(paramInt & 0xFF);
/*    */   }
/*    */ 
/*    */   void updateCRC(short paramShort)
/*    */   {
/* 39 */     this.crcEngine.update((paramShort & 0xFF00) >> 8);
/* 40 */     this.crcEngine.update(paramShort & 0xFF);
/*    */   }
/*    */ 
/*    */   void updateCRC(byte[] paramArrayOfByte)
/*    */   {
/* 50 */     this.crcEngine.update(paramArrayOfByte, 0, paramArrayOfByte.length);
/*    */   }
/*    */ 
/*    */   void updateCRC(byte[] paramArrayOfByte, int paramInt)
/*    */   {
/* 55 */     this.crcEngine.update(paramArrayOfByte, 0, paramInt);
/*    */   }
/*    */ 
/*    */   void write(DataOutputStream paramDataOutputStream, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt) throws IOException
/*    */   {
/* 60 */     paramDataOutputStream.writeInt(paramInt);
/* 61 */     paramDataOutputStream.write(paramArrayOfByte1);
/* 62 */     paramDataOutputStream.write(paramArrayOfByte2, 0, paramInt);
/*    */ 
/* 64 */     resetCRC();
/* 65 */     updateCRC(paramArrayOfByte1);
/* 66 */     updateCRC(paramArrayOfByte2, paramInt);
/* 67 */     paramDataOutputStream.writeInt(getCRC());
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.encoder.png.PNGChunkUtil
 * JD-Core Version:    0.6.2
 */