/*    */ package com.sun.jimi.core.encoder.pict;
/*    */ 
/*    */ import com.sun.jimi.core.JimiException;
/*    */ import com.sun.jimi.core.compat.AdaptiveRasterImage;
/*    */ import com.sun.jimi.core.compat.JimiEncoderBase;
/*    */ import java.io.DataOutputStream;
/*    */ import java.io.FilterOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ 
/*    */ public class PICTEncoder extends JimiEncoderBase
/*    */ {
/*    */   private OutputStream out_;
/*    */   private DataOutputStream dOut_;
/*    */   private int state_;
/*    */ 
/*    */   public boolean driveEncoder()
/*    */     throws JimiException
/*    */   {
/* 60 */     AdaptiveRasterImage localAdaptiveRasterImage = getJimiImage();
/*    */     try
/*    */     {
/* 64 */       encodePICT(localAdaptiveRasterImage, this.dOut_);
/* 65 */       this.state_ |= 2;
/* 66 */       this.dOut_.flush();
/* 67 */       this.dOut_.close();
/*    */     }
/*    */     catch (IOException localIOException)
/*    */     {
/* 71 */       throw new JimiException(localIOException.getMessage());
/*    */     }
/* 73 */     return false;
/*    */   }
/*    */ 
/*    */   public void encodePICT(AdaptiveRasterImage paramAdaptiveRasterImage, DataOutputStream paramDataOutputStream)
/*    */     throws JimiException, IOException
/*    */   {
/* 92 */     PICTWriter localPICTWriter = new PICTWriter(this, paramAdaptiveRasterImage, paramDataOutputStream);
/*    */ 
/* 94 */     localPICTWriter.writeHeaders();
/* 95 */     localPICTWriter.writeImage();
/*    */   }
/*    */ 
/*    */   public void freeEncoder()
/*    */     throws JimiException
/*    */   {
/* 79 */     AdaptiveRasterImage localAdaptiveRasterImage = getJimiImage();
/* 80 */     this.out_ = null;
/* 81 */     this.dOut_ = null;
/* 82 */     super.freeEncoder();
/*    */   }
/*    */ 
/*    */   public int getState()
/*    */   {
/* 87 */     return this.state_;
/*    */   }
/*    */ 
/*    */   protected void initSpecificEncoder(OutputStream paramOutputStream, AdaptiveRasterImage paramAdaptiveRasterImage)
/*    */     throws JimiException
/*    */   {
/* 51 */     this.out_ = paramOutputStream;
/* 52 */     this.dOut_ = new DataOutputStream(paramOutputStream);
/* 53 */     this.state_ = 0;
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.encoder.pict.PICTEncoder
 * JD-Core Version:    0.6.2
 */