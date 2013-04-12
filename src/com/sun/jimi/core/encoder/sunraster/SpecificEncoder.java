/*    */ package com.sun.jimi.core.encoder.sunraster;
/*    */ 
/*    */ import com.sun.jimi.core.JimiException;
/*    */ import com.sun.jimi.core.compat.AdaptiveRasterImage;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ 
/*    */ public abstract class SpecificEncoder
/*    */ {
/*    */   private SunRasterHeader header_;
/*    */   private OutputStream output_;
/*    */   private AdaptiveRasterImage jimiImage_;
/*    */ 
/*    */   public abstract void doImageEncode()
/*    */     throws JimiException;
/*    */ 
/*    */   protected SunRasterHeader getHeader()
/*    */   {
/* 58 */     return this.header_;
/*    */   }
/*    */ 
/*    */   protected AdaptiveRasterImage getJimiImage()
/*    */   {
/* 74 */     return this.jimiImage_;
/*    */   }
/*    */ 
/*    */   protected OutputStream getOutputStream()
/*    */   {
/* 66 */     return this.output_;
/*    */   }
/*    */ 
/*    */   public void initEncoder(SunRasterHeader paramSunRasterHeader, OutputStream paramOutputStream, AdaptiveRasterImage paramAdaptiveRasterImage)
/*    */   {
/* 35 */     this.header_ = paramSunRasterHeader;
/* 36 */     this.output_ = paramOutputStream;
/* 37 */     this.jimiImage_ = paramAdaptiveRasterImage;
/*    */   }
/*    */ 
/*    */   protected void writeHeader()
/*    */     throws IOException
/*    */   {
/* 50 */     getHeader().writeTo(getOutputStream());
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.encoder.sunraster.SpecificEncoder
 * JD-Core Version:    0.6.2
 */