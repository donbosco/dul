/*    */ package com.sun.jimi.core.decoder.apf;
/*    */ 
/*    */ import com.sun.jimi.core.ImageAccessException;
/*    */ import com.sun.jimi.core.JimiException;
/*    */ import com.sun.jimi.core.JimiImageFactory;
/*    */ import com.sun.jimi.core.JimiSingleImageRasterDecoder;
/*    */ import com.sun.jimi.core.MutableJimiImage;
/*    */ import com.sun.jimi.core.raster.IntRasterImage;
/*    */ import com.sun.jimi.core.raster.MutableJimiRasterImage;
/*    */ import java.awt.image.ColorModel;
/*    */ import java.io.DataInputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ 
/*    */ public class APFDecoder extends JimiSingleImageRasterDecoder
/*    */ {
/*    */   protected int width;
/*    */   protected int height;
/*    */   protected DataInputStream dataInput;
/*    */   protected IntRasterImage jimiImage;
/*    */ 
/*    */   protected void createJimiImage()
/*    */     throws JimiException
/*    */   {
/* 86 */     this.jimiImage = 
/* 87 */       getJimiImageFactory().createIntRasterImage(this.width, this.height, ColorModel.getRGBdefault());
/*    */   }
/*    */ 
/*    */   public void doImageDecode()
/*    */     throws JimiException, IOException
/*    */   {
/* 53 */     readData();
/* 54 */     this.jimiImage.setFinished();
/*    */   }
/*    */ 
/*    */   public MutableJimiRasterImage doInitDecoding(JimiImageFactory paramJimiImageFactory, InputStream paramInputStream)
/*    */     throws JimiException, IOException
/*    */   {
/* 43 */     readHeader();
/* 44 */     createJimiImage();
/* 45 */     return this.jimiImage;
/*    */   }
/*    */ 
/*    */   protected void readData()
/*    */     throws IOException, ImageAccessException
/*    */   {
/* 95 */     int[] arrayOfInt = new int[this.width];
/* 96 */     for (int i = 0; i < this.height; i++) {
/* 97 */       for (int j = 0; j < this.width; j++) {
/* 98 */         arrayOfInt[j] = this.dataInput.readInt();
/*    */       }
/* 100 */       this.jimiImage.setRow(i, arrayOfInt, 0);
/* 101 */       setProgress((i + 1) * 1000 / this.height);
/*    */     }
/*    */   }
/*    */ 
/*    */   protected void readHeader()
/*    */     throws JimiException, IOException
/*    */   {
/* 65 */     byte[] arrayOfByte1 = APFDecoderFactory.FORMAT_SIGNATURES[0];
/*    */ 
/* 67 */     byte[] arrayOfByte2 = new byte[arrayOfByte1.length];
/* 68 */     getInput().read(arrayOfByte2);
/* 69 */     for (int i = 0; i < arrayOfByte1.length; i++) {
/* 70 */       if (arrayOfByte1[i] != arrayOfByte2[i]) {
/* 71 */         throw new JimiException("Invalid format signature.");
/*    */       }
/*    */     }
/*    */ 
/* 75 */     this.dataInput = new DataInputStream(getInput());
/* 76 */     this.width = this.dataInput.readInt();
/* 77 */     this.height = this.dataInput.readInt();
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.decoder.apf.APFDecoder
 * JD-Core Version:    0.6.2
 */