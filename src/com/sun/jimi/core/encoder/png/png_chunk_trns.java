/*    */ package com.sun.jimi.core.encoder.png;
/*    */ 
/*    */ import com.sun.jimi.core.JimiException;
/*    */ import com.sun.jimi.core.compat.AdaptiveRasterImage;
/*    */ import java.awt.image.IndexColorModel;
/*    */ import java.io.DataOutputStream;
/*    */ import java.io.FilterOutputStream;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class png_chunk_trns
/*    */   implements PNGConstants
/*    */ {
/*    */   protected byte[] alphaValues;
/* 26 */   protected int lastTransparentIndex = -1;
/*    */   protected PNGChunkUtil pngUtil;
/*    */   private static final int NO_TRANSPARENT_PIXELS = -1;
/*    */ 
/*    */   public png_chunk_trns(AdaptiveRasterImage paramAdaptiveRasterImage, PNGChunkUtil paramPNGChunkUtil, PNGEncoder paramPNGEncoder)
/*    */     throws JimiException
/*    */   {
/* 35 */     this.pngUtil = paramPNGChunkUtil;
/*    */ 
/* 38 */     if (!(paramAdaptiveRasterImage.getColorModel() instanceof IndexColorModel)) {
/* 39 */       return;
/*    */     }
/*    */ 
/* 42 */     IndexColorModel localIndexColorModel = (IndexColorModel)paramAdaptiveRasterImage.getColorModel();
/*    */ 
/* 44 */     this.alphaValues = new byte[localIndexColorModel.getMapSize()];
/* 45 */     localIndexColorModel.getAlphas(this.alphaValues);
/*    */ 
/* 47 */     for (int i = 0; i < this.alphaValues.length; i++)
/* 48 */       if (this.alphaValues[i] != 255)
/* 49 */         this.lastTransparentIndex = i;
/*    */   }
/*    */ 
/*    */   public void write(DataOutputStream paramDataOutputStream)
/*    */     throws IOException
/*    */   {
/* 56 */     if (this.lastTransparentIndex == -1) {
/* 57 */       return;
/*    */     }
/*    */ 
/* 61 */     paramDataOutputStream.writeInt(this.lastTransparentIndex + 1);
/* 62 */     paramDataOutputStream.write(PNGConstants.png_tRNS);
/*    */ 
/* 64 */     this.pngUtil.resetCRC();
/* 65 */     this.pngUtil.updateCRC(PNGConstants.png_tRNS);
/*    */ 
/* 68 */     for (int i = 0; i <= this.lastTransparentIndex; i++)
/*    */     {
/* 70 */       paramDataOutputStream.write(this.alphaValues[i]);
/* 71 */       this.pngUtil.updateCRC(this.alphaValues[i]);
/*    */     }
/*    */ 
/* 74 */     paramDataOutputStream.writeInt(this.pngUtil.getCRC());
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.encoder.png.png_chunk_trns
 * JD-Core Version:    0.6.2
 */