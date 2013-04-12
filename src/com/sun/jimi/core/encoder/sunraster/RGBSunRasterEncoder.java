/*    */ package com.sun.jimi.core.encoder.sunraster;
/*    */ 
/*    */ import com.sun.jimi.core.JimiException;
/*    */ import com.sun.jimi.core.compat.AdaptiveRasterImage;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ 
/*    */ public class RGBSunRasterEncoder extends SpecificEncoder
/*    */ {
/*    */   public void doImageEncode()
/*    */     throws JimiException
/*    */   {
/* 24 */     AdaptiveRasterImage localAdaptiveRasterImage = getJimiImage();
/*    */ 
/* 26 */     localAdaptiveRasterImage.setRGBDefault(true);
/*    */     try {
/* 28 */       writeImage();
/*    */     }
/*    */     catch (IOException localIOException)
/*    */     {
/* 32 */       throw new JimiException(localIOException.toString());
/*    */     }
/*    */   }
/*    */ 
/*    */   protected void writeImage()
/*    */     throws IOException, JimiException
/*    */   {
/* 42 */     SunRasterHeader localSunRasterHeader = getHeader();
/*    */ 
/* 44 */     localSunRasterHeader.setDepth(24);
/* 45 */     localSunRasterHeader.setType(3);
/* 46 */     writeHeader();
/*    */ 
/* 48 */     OutputStream localOutputStream = getOutputStream();
/* 49 */     AdaptiveRasterImage localAdaptiveRasterImage = getJimiImage();
/* 50 */     int i = localAdaptiveRasterImage.getHeight();
/* 51 */     int j = localAdaptiveRasterImage.getWidth();
/*    */ 
/* 54 */     int k = j * 3;
/* 55 */     if (k % 2 != 0) {
/* 56 */       k++;
/*    */     }
/*    */ 
/* 59 */     byte[] arrayOfByte = new byte[k];
/*    */ 
/* 62 */     for (int m = 0; m < i; m++) {
/* 63 */       localAdaptiveRasterImage.getChannelRGB(m, arrayOfByte, 0);
/* 64 */       localOutputStream.write(arrayOfByte);
/*    */     }
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.encoder.sunraster.RGBSunRasterEncoder
 * JD-Core Version:    0.6.2
 */