/*    */ package com.sun.jimi.core.encoder.png;
/*    */ 
/*    */ import com.sun.jimi.core.JimiException;
/*    */ import com.sun.jimi.core.compat.AdaptiveRasterImage;
/*    */ import java.awt.image.IndexColorModel;
/*    */ import java.io.DataOutputStream;
/*    */ import java.io.FilterOutputStream;
/*    */ import java.io.IOException;
/*    */ 
/*    */ class png_chunk_plte
/*    */   implements PNGConstants
/*    */ {
/*    */   byte[] reds;
/*    */   byte[] greens;
/*    */   byte[] blues;
/*    */   int mapSize;
/* 24 */   IndexColorModel cm = null;
/*    */   PNGChunkUtil pcu;
/*    */ 
/*    */   png_chunk_plte(AdaptiveRasterImage paramAdaptiveRasterImage, PNGChunkUtil paramPNGChunkUtil, PNGEncoder paramPNGEncoder)
/*    */     throws JimiException
/*    */   {
/* 30 */     if ((paramAdaptiveRasterImage.getColorModel() instanceof IndexColorModel))
/*    */     {
/* 32 */       this.cm = ((IndexColorModel)paramAdaptiveRasterImage.getColorModel());
/* 33 */       this.mapSize = this.cm.getMapSize();
/*    */ 
/* 36 */       this.reds = new byte[this.mapSize];
/* 37 */       this.greens = new byte[this.mapSize];
/* 38 */       this.blues = new byte[this.mapSize];
/*    */ 
/* 40 */       this.cm.getReds(this.reds);
/* 41 */       this.cm.getBlues(this.blues);
/* 42 */       this.cm.getGreens(this.greens);
/*    */     }
/* 44 */     this.pcu = paramPNGChunkUtil;
/*    */   }
/*    */ 
/*    */   void write(DataOutputStream paramDataOutputStream)
/*    */     throws IOException
/*    */   {
/* 53 */     if (this.cm != null)
/*    */     {
/* 56 */       paramDataOutputStream.writeInt(this.mapSize * 3);
/* 57 */       paramDataOutputStream.write(PNGConstants.png_PLTE);
/*    */ 
/* 59 */       this.pcu.resetCRC();
/* 60 */       this.pcu.updateCRC(PNGConstants.png_PLTE);
/*    */ 
/* 62 */       for (int i = 0; i < this.mapSize; i++)
/*    */       {
/* 64 */         paramDataOutputStream.writeByte(this.reds[i]);
/* 65 */         paramDataOutputStream.writeByte(this.greens[i]);
/* 66 */         paramDataOutputStream.writeByte(this.blues[i]);
/*    */ 
/* 68 */         this.pcu.updateCRC(this.reds[i]);
/* 69 */         this.pcu.updateCRC(this.greens[i]);
/* 70 */         this.pcu.updateCRC(this.blues[i]);
/*    */       }
/* 72 */       paramDataOutputStream.writeInt(this.pcu.getCRC());
/*    */     }
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.encoder.png.png_chunk_plte
 * JD-Core Version:    0.6.2
 */