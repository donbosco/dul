/*    */ package com.sun.jimi.core.encoder.tga;
/*    */ 
/*    */ import com.sun.jimi.core.JimiException;
/*    */ import com.sun.jimi.core.compat.AdaptiveRasterImage;
/*    */ import com.sun.jimi.core.util.LEDataOutputStream;
/*    */ import java.awt.image.IndexColorModel;
/*    */ import java.io.FilterOutputStream;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class TGA8Encoder
/*    */   implements TGAEncoderIfc
/*    */ {
/*    */   public void encodeTGA(AdaptiveRasterImage paramAdaptiveRasterImage, LEDataOutputStream paramLEDataOutputStream)
/*    */     throws JimiException
/*    */   {
/*    */     IndexColorModel localIndexColorModel;
/*    */     try
/*    */     {
/* 39 */       localIndexColorModel = (IndexColorModel)paramAdaptiveRasterImage.getColorModel();
/*    */     }
/*    */     catch (ClassCastException localClassCastException) {
/* 42 */       throw new JimiException("TGA8 requires an IndexColorModel.");
/*    */     }
/*    */ 
/* 45 */     int i = localIndexColorModel.getMapSize();
/*    */     try
/*    */     {
/* 51 */       int j = paramAdaptiveRasterImage.getWidth();
/* 52 */       int k = paramAdaptiveRasterImage.getHeight();
/*    */ 
/* 54 */       paramLEDataOutputStream.writeByte(0);
/* 55 */       paramLEDataOutputStream.writeByte(1);
/* 56 */       paramLEDataOutputStream.writeByte(1);
/*    */ 
/* 58 */       paramLEDataOutputStream.writeShort(0);
/* 59 */       paramLEDataOutputStream.writeShort(i);
/* 60 */       paramLEDataOutputStream.writeByte(24);
/*    */ 
/* 62 */       paramLEDataOutputStream.writeShort(0);
/* 63 */       paramLEDataOutputStream.writeShort(0);
/*    */ 
/* 65 */       paramLEDataOutputStream.writeShort(j);
/* 66 */       paramLEDataOutputStream.writeShort(k);
/*    */ 
/* 70 */       paramLEDataOutputStream.write(8);
/* 71 */       paramLEDataOutputStream.write(0);
/*    */ 
/* 76 */       byte[] arrayOfByte1 = new byte[i];
/* 77 */       byte[] arrayOfByte2 = new byte[i];
/* 78 */       byte[] arrayOfByte3 = new byte[i];
/*    */ 
/* 80 */       localIndexColorModel.getReds(arrayOfByte1);
/* 81 */       localIndexColorModel.getGreens(arrayOfByte2);
/* 82 */       localIndexColorModel.getBlues(arrayOfByte3);
/*    */ 
/* 85 */       for (int m = 0; m < i; m++) {
/* 86 */         paramLEDataOutputStream.writeByte(arrayOfByte3[m]);
/* 87 */         paramLEDataOutputStream.writeByte(arrayOfByte2[m]);
/* 88 */         paramLEDataOutputStream.writeByte(arrayOfByte1[m]);
/*    */       }
/*    */ 
/* 93 */       byte[] arrayOfByte4 = new byte[j];
/*    */ 
/* 96 */       for (int n = 0; n < k; n++)
/*    */       {
/* 98 */         int i1 = k - n - 1;
/* 99 */         paramAdaptiveRasterImage.getChannel(0, i1, arrayOfByte4, 0);
/* 100 */         paramLEDataOutputStream.write(arrayOfByte4);
/*    */       }
/*    */     }
/*    */     catch (IOException localIOException)
/*    */     {
/* 105 */       throw new JimiException("TGA8Encoder encodeTGA() IOException encountered");
/*    */     }
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.encoder.tga.TGA8Encoder
 * JD-Core Version:    0.6.2
 */