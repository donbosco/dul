/*    */ package com.sun.jimi.core.encoder.tga;
/*    */ 
/*    */ import com.sun.jimi.core.JimiException;
/*    */ import com.sun.jimi.core.compat.AdaptiveRasterImage;
/*    */ import com.sun.jimi.core.util.LEDataOutputStream;
/*    */ import java.io.FilterOutputStream;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class TGA24Encoder
/*    */   implements TGAEncoderIfc
/*    */ {
/*    */   public void encodeTGA(AdaptiveRasterImage paramAdaptiveRasterImage, LEDataOutputStream paramLEDataOutputStream)
/*    */     throws JimiException
/*    */   {
/*    */     try
/*    */     {
/* 38 */       int i = paramAdaptiveRasterImage.getWidth();
/* 39 */       int j = paramAdaptiveRasterImage.getHeight();
/*    */ 
/* 41 */       paramLEDataOutputStream.write(0);
/* 42 */       paramLEDataOutputStream.write(0);
/* 43 */       paramLEDataOutputStream.write(2);
/*    */ 
/* 45 */       paramLEDataOutputStream.write(0);
/* 46 */       paramLEDataOutputStream.write(0);
/* 47 */       paramLEDataOutputStream.write(0);
/* 48 */       paramLEDataOutputStream.write(0);
/* 49 */       paramLEDataOutputStream.write(0);
/*    */ 
/* 51 */       paramLEDataOutputStream.writeShort(0);
/* 52 */       paramLEDataOutputStream.writeShort(0);
/*    */ 
/* 54 */       paramLEDataOutputStream.writeShort(i);
/* 55 */       paramLEDataOutputStream.writeShort(j);
/*    */ 
/* 57 */       paramLEDataOutputStream.write(24);
/* 58 */       paramLEDataOutputStream.write(0);
/*    */ 
/* 62 */       int[] arrayOfInt = new int[i];
/* 63 */       byte[] arrayOfByte = new byte[i * 3];
/*    */ 
/* 65 */       paramAdaptiveRasterImage.setRGBDefault(true);
/* 66 */       for (int k = 0; k < j; k++)
/*    */       {
/* 68 */         int n = j - k - 1;
/* 69 */         paramAdaptiveRasterImage.getChannel(n, arrayOfInt, 0);
/*    */ 
/* 71 */         int i1 = 3 * i;
/* 72 */         int m = i;
/*    */         do {
/* 74 */           arrayOfByte[(--i1)] = ((byte)((arrayOfInt[m] & 0xFF0000) >> 16));
/* 75 */           arrayOfByte[(--i1)] = ((byte)((arrayOfInt[m] & 0xFF00) >> 8));
/* 76 */           arrayOfByte[(--i1)] = ((byte)(arrayOfInt[m] & 0xFF));
/*    */ 
/* 72 */           m--; } while (m >= 0);
/*    */ 
/* 78 */         paramLEDataOutputStream.write(arrayOfByte);
/*    */       }
/*    */     }
/*    */     catch (IOException localIOException)
/*    */     {
/* 83 */       throw new JimiException("TGA24Encoder encodeTGA() IOException encountered");
/*    */     }
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.encoder.tga.TGA24Encoder
 * JD-Core Version:    0.6.2
 */