/*    */ package com.sun.jimi.core.util;
/*    */ 
/*    */ import com.sun.jimi.core.JimiImage;
/*    */ import com.sun.jimi.core.JimiImageFactory;
/*    */ import com.sun.jimi.core.options.BasicFormatOptionSet;
/*    */ import com.sun.jimi.core.options.FormatOption;
/*    */ import com.sun.jimi.core.options.FormatOptionSet;
/*    */ import java.awt.image.ImageProducer;
/*    */ 
/*    */ public class ErrorJimiImage
/*    */   implements JimiImage
/*    */ {
/*    */   public JimiImageFactory getFactory()
/*    */   {
/* 40 */     return null;
/*    */   }
/*    */ 
/*    */   public ImageProducer getImageProducer()
/*    */   {
/* 30 */     return JimiUtil.getErrorImageProducer();
/*    */   }
/*    */ 
/*    */   public FormatOptionSet getOptions()
/*    */   {
/* 53 */     return new BasicFormatOptionSet(new FormatOption[0]);
/*    */   }
/*    */ 
/*    */   public boolean isError()
/*    */   {
/* 49 */     return true;
/*    */   }
/*    */ 
/*    */   public void setOptions(FormatOptionSet paramFormatOptionSet)
/*    */   {
/*    */   }
/*    */ 
/*    */   public void waitFinished()
/*    */   {
/*    */   }
/*    */ 
/*    */   public void waitInfoAvailable()
/*    */   {
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.util.ErrorJimiImage
 * JD-Core Version:    0.6.2
 */