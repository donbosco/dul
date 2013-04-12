/*    */ package com.sun.jimi.core.encoder.sunraster;
/*    */ 
/*    */ import com.sun.jimi.core.JimiException;
/*    */ import com.sun.jimi.core.compat.AdaptiveRasterImage;
/*    */ import java.awt.image.IndexColorModel;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ 
/*    */ public class PaletteSunRasterEncoder extends SpecificEncoder
/*    */ {
/* 21 */   protected boolean useRLE_ = false;
/*    */ 
/*    */   public void doImageEncode()
/*    */     throws JimiException
/*    */   {
/* 28 */     AdaptiveRasterImage localAdaptiveRasterImage = getJimiImage();
/*    */     try {
/* 30 */       writeImage();
/*    */     }
/*    */     catch (IOException localIOException)
/*    */     {
/* 34 */       throw new JimiException(localIOException.toString());
/*    */     }
/*    */   }
/*    */ 
/*    */   public void setUseRLE(boolean paramBoolean)
/*    */   {
/* 44 */     this.useRLE_ = paramBoolean;
/*    */   }
/*    */ 
/*    */   protected void writeImage()
/*    */     throws IOException, JimiException
/*    */   {
/* 54 */     AdaptiveRasterImage localAdaptiveRasterImage = getJimiImage();
/*    */ 
/* 56 */     SunRasterHeader localSunRasterHeader = getHeader();
/* 57 */     localSunRasterHeader.setDepth(8);
/*    */ 
/* 60 */     if (this.useRLE_)
/* 61 */       localSunRasterHeader.setType(2);
/*    */     else {
/* 63 */       localSunRasterHeader.setType(1);
/*    */     }
/* 65 */     localSunRasterHeader.setPalette((IndexColorModel)localAdaptiveRasterImage.getColorModel());
/* 66 */     writeHeader();
/*    */ 
/* 68 */     Object localObject = getOutputStream();
/*    */ 
/* 70 */     if (this.useRLE_) {
/* 71 */       localObject = new RLEOutputStream((OutputStream)localObject);
/*    */     }
/* 73 */     int i = localAdaptiveRasterImage.getHeight();
/* 74 */     int j = localAdaptiveRasterImage.getWidth();
/*    */ 
/* 76 */     int k = j;
/*    */ 
/* 78 */     if (k % 2 != 0) {
/* 79 */       k++;
/*    */     }
/*    */ 
/* 82 */     byte[] arrayOfByte = new byte[k];
/*    */ 
/* 85 */     for (int m = 0; m < i; m++) {
/* 86 */       localAdaptiveRasterImage.getChannel(0, m, arrayOfByte, 0);
/* 87 */       ((OutputStream)localObject).write(arrayOfByte);
/*    */     }
/*    */ 
/* 90 */     ((OutputStream)localObject).flush();
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.encoder.sunraster.PaletteSunRasterEncoder
 * JD-Core Version:    0.6.2
 */