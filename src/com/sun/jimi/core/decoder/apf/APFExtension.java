/*    */ package com.sun.jimi.core.decoder.apf;
/*    */ 
/*    */ import com.sun.jimi.core.JimiDecoderFactory;
/*    */ import com.sun.jimi.core.JimiEncoderFactory;
/*    */ import com.sun.jimi.core.JimiExtension;
/*    */ import com.sun.jimi.core.encoder.apf.APFEncoderFactory;
/*    */ 
/*    */ public class APFExtension
/*    */   implements JimiExtension
/*    */ {
/*    */   public JimiDecoderFactory[] getDecoders()
/*    */   {
/* 38 */     return new JimiDecoderFactory[] { new APFDecoderFactory() };
/*    */   }
/*    */ 
/*    */   public String getDescription()
/*    */   {
/* 30 */     return "Encoder and Decoder support for the Activated Pseudo-Format (APF)";
/*    */   }
/*    */ 
/*    */   public JimiEncoderFactory[] getEncoders()
/*    */   {
/* 42 */     return new JimiEncoderFactory[] { new APFEncoderFactory() };
/*    */   }
/*    */ 
/*    */   public String getVendor()
/*    */   {
/* 26 */     return "Sun Microsystems, Inc.";
/*    */   }
/*    */ 
/*    */   public String getVersionString()
/*    */   {
/* 34 */     return "1.0";
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.decoder.apf.APFExtension
 * JD-Core Version:    0.6.2
 */