/*    */ package com.sun.jimi.core.encoder.bmp;
/*    */ 
/*    */ import com.sun.jimi.core.JimiException;
/*    */ import com.sun.jimi.core.compat.AdaptiveRasterImage;
/*    */ import com.sun.jimi.core.compat.JimiEncoderBase;
/*    */ import com.sun.jimi.core.util.LEDataOutputStream;
/*    */ import java.io.FilterOutputStream;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class BMP24Encoder
/*    */   implements BMPEncoderIfc
/*    */ {
/*    */   public void encodeBMP(BMPEncoder paramBMPEncoder, AdaptiveRasterImage paramAdaptiveRasterImage, LEDataOutputStream paramLEDataOutputStream)
/*    */     throws JimiException
/*    */   {
/*    */     try
/*    */     {
/* 37 */       int i = 19778;
/* 38 */       int j = (paramAdaptiveRasterImage.getWidth() * 24 + 31) / 32 * 4;
/* 39 */       int k = 54;
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
/* 54 */       paramLEDataOutputStream.writeShort(24);
/* 55 */       paramLEDataOutputStream.writeInt(0);
/* 56 */       paramLEDataOutputStream.writeInt(j * paramAdaptiveRasterImage.getHeight());
/* 57 */       paramLEDataOutputStream.writeInt(0);
/* 58 */       paramLEDataOutputStream.writeInt(0);
/* 59 */       paramLEDataOutputStream.writeInt(0);
/* 60 */       paramLEDataOutputStream.writeInt(0);
/*    */ 
/* 62 */       int n = paramAdaptiveRasterImage.getWidth();
/* 63 */       int[] arrayOfInt = new int[n];
/* 64 */       int i1 = paramAdaptiveRasterImage.getHeight() - 1;
/* 65 */       int i2 = j - paramAdaptiveRasterImage.getWidth() * 3;
/* 66 */       byte[] arrayOfByte = new byte[3 * n + i2];
/*    */ 
/* 68 */       paramAdaptiveRasterImage.setRGBDefault(true);
/* 69 */       for (int i3 = i1; i3 > -1; i3--)
/*    */       {
/* 71 */         paramAdaptiveRasterImage.getChannel(i3, arrayOfInt, 0);
/* 72 */         int i4 = 3 * n;
/* 73 */         int i5 = n;
/*    */         do {
/* 75 */           arrayOfByte[(--i4)] = ((byte)((arrayOfInt[i5] & 0xFF0000) >> 16));
/* 76 */           arrayOfByte[(--i4)] = ((byte)((arrayOfInt[i5] & 0xFF00) >> 8));
/* 77 */           arrayOfByte[(--i4)] = ((byte)(arrayOfInt[i5] & 0xFF));
/*    */ 
/* 73 */           i5--; } while (i5 >= 0);
/*    */ 
/* 79 */         i4 = 3 * n;
/* 80 */         int i6 = i2;
/*    */         do { arrayOfByte[(i4++)] = 0;
/*    */ 
/* 80 */           i6--; } while (i6 >= 0);
/*    */ 
/* 82 */         paramLEDataOutputStream.write(arrayOfByte);
/* 83 */         paramBMPEncoder.setProgress((i1 - i3) * 100 / i1);
/*    */       }
/*    */     }
/*    */     catch (IOException localIOException)
/*    */     {
/* 88 */       throw new JimiException("BMP24Encoder encodeBMP() IOException encountered");
/*    */     }
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.encoder.bmp.BMP24Encoder
 * JD-Core Version:    0.6.2
 */