/*    */ package com.sun.jimi.core;
/*    */ 
/*    */ import com.sun.jimi.core.util.JimiUtil;
/*    */ import com.sun.jimi.core.util.ProgressMonitorSupport;
/*    */ import java.io.InputStream;
/*    */ import java.util.Vector;
/*    */ 
/*    */ public abstract class JimiRasterDecoderSupport extends ProgressMonitorSupport
/*    */   implements JimiRasterDecoder
/*    */ {
/*    */   protected InputStream input;
/*    */   protected JimiImageFactory factory;
/*    */   protected boolean noMoreRequests;
/* 36 */   protected Vector cleanupCommands = new Vector();
/*    */ 
/*    */   public void addCleanupCommand(Runnable paramRunnable)
/*    */   {
/* 81 */     this.cleanupCommands.addElement(paramRunnable);
/*    */   }
/*    */ 
/*    */   protected void cleanup()
/*    */   {
/* 86 */     JimiUtil.runCommands(this.cleanupCommands);
/*    */   }
/*    */ 
/*    */   protected InputStream getInput()
/*    */   {
/* 64 */     return this.input;
/*    */   }
/*    */ 
/*    */   protected JimiImageFactory getJimiImageFactory()
/*    */   {
/* 55 */     return this.factory;
/*    */   }
/*    */ 
/*    */   protected void init(JimiImageFactory paramJimiImageFactory, InputStream paramInputStream)
/*    */   {
/* 45 */     this.factory = paramJimiImageFactory;
/* 46 */     this.input = paramInputStream;
/*    */   }
/*    */ 
/*    */   public abstract ImageSeriesDecodingController initDecoding(JimiImageFactory paramJimiImageFactory, InputStream paramInputStream);
/*    */ 
/*    */   public void setFinished()
/*    */   {
/* 73 */     this.noMoreRequests = true;
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.JimiRasterDecoderSupport
 * JD-Core Version:    0.6.2
 */