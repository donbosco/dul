/*    */ package com.sun.jimi.core.decoder.png;
/*    */ 
/*    */ import com.sun.jimi.core.JimiException;
/*    */ import com.sun.jimi.core.compat.AdaptiveRasterImage;
/*    */ import com.sun.jimi.core.compat.JimiDecoderBase;
/*    */ import java.io.DataInputStream;
/*    */ import java.io.InputStream;
/*    */ 
/*    */ public class PNGDecoder extends JimiDecoderBase
/*    */ {
/*    */   private AdaptiveRasterImage ji_;
/*    */   private InputStream in_;
/*    */   private DataInputStream dIn_;
/*    */   private int state_;
/*    */ 
/*    */   public boolean driveDecoder()
/*    */     throws JimiException
/*    */   {
/* 55 */     PNGReader localPNGReader = new PNGReader(this.dIn_, this.ji_);
/*    */ 
/* 57 */     localPNGReader.decodeImage();
/* 58 */     this.ji_.addFullCoverage();
/* 59 */     this.state_ |= 2;
/* 60 */     this.state_ |= 4;
/* 61 */     return false;
/*    */   }
/*    */ 
/*    */   public void freeDecoder() throws JimiException
/*    */   {
/* 66 */     this.in_ = null;
/* 67 */     this.ji_ = null;
/* 68 */     this.dIn_ = null;
/*    */   }
/*    */ 
/*    */   public int getState()
/*    */   {
/* 73 */     return this.state_;
/*    */   }
/*    */ 
/*    */   public void initDecoder(InputStream paramInputStream, AdaptiveRasterImage paramAdaptiveRasterImage)
/*    */     throws JimiException
/*    */   {
/* 47 */     this.in_ = paramInputStream;
/* 48 */     this.ji_ = paramAdaptiveRasterImage;
/*    */ 
/* 50 */     this.dIn_ = new DataInputStream(this.in_);
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.decoder.png.PNGDecoder
 * JD-Core Version:    0.6.2
 */