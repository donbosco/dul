/*    */ package com.sun.jimi.core.encoder.apf;
/*    */ 
/*    */ import com.sun.jimi.core.JimiException;
/*    */ import com.sun.jimi.core.JimiSingleImageRasterEncoder;
/*    */ import com.sun.jimi.core.raster.JimiRasterImage;
/*    */ import java.io.BufferedOutputStream;
/*    */ import java.io.DataOutputStream;
/*    */ import java.io.FilterOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ 
/*    */ public class APFEncoder extends JimiSingleImageRasterEncoder
/*    */ {
/*    */   public void doEncodeImage(JimiRasterImage paramJimiRasterImage, OutputStream paramOutputStream)
/*    */     throws JimiException, IOException
/*    */   {
/* 33 */     DataOutputStream localDataOutputStream = new DataOutputStream(new BufferedOutputStream(paramOutputStream));
/* 34 */     writeHeader(paramJimiRasterImage, localDataOutputStream);
/* 35 */     writeImageData(paramJimiRasterImage, localDataOutputStream);
/* 36 */     localDataOutputStream.flush();
/*    */   }
/*    */ 
/*    */   protected void writeHeader(JimiRasterImage paramJimiRasterImage, DataOutputStream paramDataOutputStream)
/*    */     throws JimiException, IOException
/*    */   {
/* 42 */     paramDataOutputStream.write(APFEncoderFactory.FORMAT_SIGNATURE);
/* 43 */     paramDataOutputStream.writeInt(paramJimiRasterImage.getWidth());
/* 44 */     paramDataOutputStream.writeInt(paramJimiRasterImage.getHeight());
/*    */   }
/*    */ 
/*    */   protected void writeImageData(JimiRasterImage paramJimiRasterImage, DataOutputStream paramDataOutputStream)
/*    */     throws JimiException, IOException
/*    */   {
/* 50 */     int[] arrayOfInt = new int[paramJimiRasterImage.getWidth()];
/* 51 */     for (int i = 0; i < paramJimiRasterImage.getHeight(); i++) {
/* 52 */       paramJimiRasterImage.getRowRGB(i, arrayOfInt, 0);
/* 53 */       for (int j = 0; j < paramJimiRasterImage.getWidth(); j++)
/* 54 */         paramDataOutputStream.writeInt(arrayOfInt[j]);
/*    */     }
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.encoder.apf.APFEncoder
 * JD-Core Version:    0.6.2
 */