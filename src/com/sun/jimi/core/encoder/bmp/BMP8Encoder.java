/*    */ package com.sun.jimi.core.encoder.bmp;
/*    */ 
/*    */ import com.sun.jimi.core.JimiException;
/*    */ import com.sun.jimi.core.compat.AdaptiveRasterImage;
/*    */ import com.sun.jimi.core.compat.JimiEncoderBase;
/*    */ import com.sun.jimi.core.util.LEDataOutputStream;
/*    */ import java.awt.image.IndexColorModel;
/*    */ import java.io.FilterOutputStream;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class BMP8Encoder
/*    */   implements BMPEncoderIfc
/*    */ {
/*    */   public void encodeBMP(BMPEncoder paramBMPEncoder, AdaptiveRasterImage paramAdaptiveRasterImage, LEDataOutputStream paramLEDataOutputStream)
/*    */     throws JimiException
/*    */   {
/*    */     try
/*    */     {
/* 37 */       int i = 19778;
/* 38 */       int j = (paramAdaptiveRasterImage.getWidth() * 8 + 31) / 32 * 4;
/* 39 */       int k = 1078;
/* 40 */       int m = j * paramAdaptiveRasterImage.getHeight() + k;
/*    */ 
/* 43 */       paramLEDataOutputStream.writeShort(i);
/* 44 */       paramLEDataOutputStream.writeInt(m);
/* 45 */       paramLEDataOutputStream.writeShort(0);
/* 46 */       paramLEDataOutputStream.writeShort(0);
/* 47 */       paramLEDataOutputStream.writeInt(k);
/*    */ 
/* 50 */       paramLEDataOutputStream.writeInt(40);
/* 51 */       paramLEDataOutputStream.writeInt(paramAdaptiveRasterImage.getWidth());
/* 52 */       paramLEDataOutputStream.writeInt(paramAdaptiveRasterImage.getHeight());
/* 53 */       paramLEDataOutputStream.writeShort(1);
/* 54 */       paramLEDataOutputStream.writeShort(8);
/* 55 */       paramLEDataOutputStream.writeInt(0);
/* 56 */       paramLEDataOutputStream.writeInt(j * paramAdaptiveRasterImage.getHeight());
/* 57 */       paramLEDataOutputStream.writeInt(0);
/* 58 */       paramLEDataOutputStream.writeInt(0);
/* 59 */       paramLEDataOutputStream.writeInt(0);
/* 60 */       paramLEDataOutputStream.writeInt(0);
/*    */       IndexColorModel localIndexColorModel;
/*    */       try
/*    */       {
/* 64 */         localIndexColorModel = (IndexColorModel)paramAdaptiveRasterImage.getColorModel();
/*    */       }
/*    */       catch (ClassCastException localClassCastException) {
/* 67 */         throw new JimiException("BMP8 encoding requires an IndexColorModel.");
/*    */       }
/* 69 */       int n = Math.max(256, localIndexColorModel.getMapSize());
/*    */ 
/* 71 */       byte[] arrayOfByte1 = new byte[n];
/* 72 */       byte[] arrayOfByte2 = new byte[n];
/* 73 */       byte[] arrayOfByte3 = new byte[n];
/*    */ 
/* 75 */       localIndexColorModel.getReds(arrayOfByte1);
/* 76 */       localIndexColorModel.getGreens(arrayOfByte2);
/* 77 */       localIndexColorModel.getBlues(arrayOfByte3);
/*    */ 
/* 80 */       for (int i1 = 0; i1 < 256; i1++)
/*    */       {
/* 82 */         if (i1 < n) {
/* 83 */           paramLEDataOutputStream.writeByte(arrayOfByte3[i1]);
/* 84 */           paramLEDataOutputStream.writeByte(arrayOfByte2[i1]);
/* 85 */           paramLEDataOutputStream.writeByte(arrayOfByte1[i1]);
/* 86 */           paramLEDataOutputStream.writeByte(0);
/*    */         }
/*    */         else {
/* 89 */           paramLEDataOutputStream.writeInt(0);
/*    */         }
/*    */       }
/*    */ 
/* 93 */       int i2 = paramAdaptiveRasterImage.getWidth();
/* 94 */       int i3 = paramAdaptiveRasterImage.getHeight() - 1;
/* 95 */       byte[] arrayOfByte4 = new byte[i2];
/* 96 */       int i4 = j - i2;
/*    */ 
/* 98 */       for (int i5 = i3; i5 > -1; i5--)
/*    */       {
/* 100 */         paramAdaptiveRasterImage.getChannel(0, i5, arrayOfByte4, 0);
/* 101 */         paramLEDataOutputStream.write(arrayOfByte4);
/* 102 */         for (int i6 = 0; i6 < i4; i6++)
/* 103 */           paramLEDataOutputStream.write(0);
/* 104 */         paramBMPEncoder.setProgress((i3 - i5) * 100 / i3);
/*    */       }
/*    */     }
/*    */     catch (IOException localIOException)
/*    */     {
/* 109 */       throw new JimiException("BMP8Encoder encodeBMP() IOException encountered");
/*    */     }
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.encoder.bmp.BMP8Encoder
 * JD-Core Version:    0.6.2
 */