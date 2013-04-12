/*     */ package com.sun.jimi.core.compat;
/*     */ 
/*     */ import com.sun.jimi.core.ImageSeriesDecodingController;
/*     */ import com.sun.jimi.core.JimiDecoder;
/*     */ import com.sun.jimi.core.JimiDecodingController;
/*     */ import com.sun.jimi.core.JimiException;
/*     */ import com.sun.jimi.core.JimiImageFactory;
/*     */ import com.sun.jimi.core.JimiImageHandle;
/*     */ import com.sun.jimi.core.MemoryJimiImageFactory;
/*     */ import com.sun.jimi.core.MutableJimiImage;
/*     */ import com.sun.jimi.core.util.ErrorJimiImage;
/*     */ import com.sun.jimi.core.util.JimiImageFactoryProxy;
/*     */ import com.sun.jimi.core.util.JimiUtil;
/*     */ import com.sun.jimi.core.util.ProgressListener;
/*     */ import com.sun.jimi.core.util.ProgressMonitorSupport;
/*     */ import java.awt.image.ColorModel;
/*     */ import java.io.InputStream;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public abstract class JimiDecoderBase extends ProgressMonitorSupport
/*     */   implements JimiDecoder, Runnable
/*     */ {
/*     */   public static final int ERROR = 1;
/*     */   public static final int INFOAVAIL = 2;
/*     */   public static final int IMAGEAVAIL = 4;
/*     */   public static final int MOREIMAGES = 8;
/*     */   public static final int MULTIIMAGE = 1;
/*     */   public static final int UNKNOWNCOUNT = -1;
/*     */   protected AdaptiveRasterImage jimiImage;
/*     */   protected JimiImageFactory factory;
/*     */   protected InputStream input;
/*     */   protected JimiDecodingController currentController;
/*     */   protected JimiImageHandle currentHandle;
/*     */   protected boolean error;
/*     */   protected volatile boolean busyDecoding;
/*  75 */   protected Object decodingLock = new Object();
/*     */ 
/*  78 */   protected Vector cleanupCommands = new Vector();
/*     */   protected boolean finishedDecoding;
/*     */ 
/*     */   public void addCleanupCommand(Runnable paramRunnable)
/*     */   {
/* 328 */     this.cleanupCommands.addElement(paramRunnable);
/*     */   }
/*     */ 
/*     */   protected AdaptiveRasterImage createAdaptiveRasterImage()
/*     */   {
/* 169 */     while ((this.factory instanceof JimiImageFactoryProxy)) {
/* 170 */       this.factory = ((JimiImageFactoryProxy)this.factory).getProxiedFactory();
/*     */     }
/* 172 */     if (this.factory.getClass().getName().equals("com.sun.jimi.core.OneshotJimiImageFactory")) {
/* 173 */       return new AdaptiveRasterImage(new MemoryJimiImageFactory());
/*     */     }
/*     */ 
/* 176 */     return new AdaptiveRasterImage(this.factory);
/*     */   }
/*     */ 
/*     */   protected AdaptiveRasterImage createAdaptiveRasterImage(int paramInt1, int paramInt2, ColorModel paramColorModel)
/*     */   {
/* 186 */     if ((this.factory instanceof JimiImageFactoryProxy))
/* 187 */       this.factory = ((JimiImageFactoryProxy)this.factory).getProxiedFactory();
/*     */     AdaptiveRasterImage localAdaptiveRasterImage;
/* 189 */     if (this.factory.getClass().getName().equals("com.sun.jimi.core.OneshotJimiImageFactory")) {
/* 190 */       localAdaptiveRasterImage = new AdaptiveRasterImage(new MemoryJimiImageFactory());
/*     */     }
/*     */     else {
/* 193 */       localAdaptiveRasterImage = new AdaptiveRasterImage(this.factory);
/*     */     }
/* 195 */     localAdaptiveRasterImage.setSize(paramInt1, paramInt2);
/* 196 */     localAdaptiveRasterImage.setColorModel(paramColorModel);
/*     */ 
/* 198 */     return localAdaptiveRasterImage;
/*     */   }
/*     */ 
/*     */   protected JimiDecodingController decodeNextImage()
/*     */   {
/* 207 */     if (this.currentController != null) {
/* 208 */       this.currentController.requestDecoding();
/*     */     }
/* 210 */     synchronized (this) {
/* 211 */       waitReady();
/* 212 */       this.busyDecoding = true;
/*     */     }
/* 214 */     this.currentHandle = new JimiImageHandle();
/* 215 */     this.currentController = new JimiDecodingController(this.currentHandle);
/* 216 */     new Thread(this).start();
/* 217 */     return this.currentController;
/*     */   }
/*     */ 
/*     */   protected abstract boolean driveDecoder()
/*     */     throws JimiException;
/*     */ 
/*     */   protected synchronized void finishedDecode()
/*     */   {
/* 225 */     this.busyDecoding = false;
/* 226 */     notifyAll();
/* 227 */     if ((this.finishedDecoding) || ((getState() & 0x8) == 0))
/* 228 */       JimiUtil.runCommands(this.cleanupCommands);
/*     */   }
/*     */ 
/*     */   protected abstract void freeDecoder()
/*     */     throws JimiException;
/*     */ 
/*     */   protected int getCapabilities()
/*     */   {
/* 277 */     return 0;
/*     */   }
/*     */ 
/*     */   public int getNumberOfImages()
/*     */   {
/* 294 */     return 1;
/*     */   }
/*     */ 
/*     */   protected abstract int getState();
/*     */ 
/*     */   protected abstract void initDecoder(InputStream paramInputStream, AdaptiveRasterImage paramAdaptiveRasterImage)
/*     */     throws JimiException;
/*     */ 
/*     */   public ImageSeriesDecodingController initDecoding(JimiImageFactory paramJimiImageFactory, InputStream paramInputStream)
/*     */   {
/*  88 */     this.jimiImage = new AdaptiveRasterImage(paramJimiImageFactory, this);
/*  89 */     this.jimiImage.setDecoder(this);
/*  90 */     this.factory = paramJimiImageFactory;
/*  91 */     this.input = paramInputStream;
/*     */     try
/*     */     {
/*  94 */       initDecoder(paramInputStream, this.jimiImage);
/*     */     }
/*     */     catch (JimiException localJimiException) {
/*  97 */       this.error = true;
/*  98 */       if (this.currentHandle != null) {
/*  99 */         this.currentHandle.setJimiImage(new ErrorJimiImage());
/*     */       }
/*     */     }
/* 102 */     return new JimiDecoderBaseSeriesController(this);
/*     */   }
/*     */ 
/*     */   protected void jimiImageCreated(MutableJimiImage paramMutableJimiImage)
/*     */   {
/* 110 */     this.currentHandle.setJimiImage(paramMutableJimiImage);
/* 111 */     paramMutableJimiImage.setDecodingController(this.currentController);
/* 112 */     this.currentController.waitDecodingRequest();
/* 113 */     if (this.progressListener != null)
/* 114 */       this.progressListener.setStarted();
/*     */   }
/*     */ 
/*     */   public boolean mustWaitForOptions()
/*     */   {
/* 312 */     return false;
/*     */   }
/*     */ 
/*     */   public void run()
/*     */   {
/* 123 */     if (this.finishedDecoding) {
/* 124 */       this.error = true;
/* 125 */       this.currentHandle.setJimiImage(new ErrorJimiImage());
/* 126 */       return;
/*     */     }
/* 128 */     synchronized (this.decodingLock) {
/* 129 */       if (this.error)
/*     */         return;
/*     */       do
/*     */       {
/*     */         try
/*     */         {
/* 135 */           while (driveDecoder());
/*     */         }
/*     */         catch (Exception localException) {
/* 138 */           this.error = true;
/* 139 */           this.currentHandle.setJimiImage(new ErrorJimiImage());
/*     */         }
/* 141 */         if (((getState() & 0x1) != 0) || (!this.currentHandle.isImageSet())) {
/* 142 */           this.error = true;
/* 143 */           this.currentHandle.setJimiImage(new ErrorJimiImage());
/*     */         }
/* 146 */         else if (this.progressListener != null) {
/* 147 */           this.progressListener.setFinished();
/*     */         }
/*     */       }
/*     */ 
/* 151 */       while ((!this.error) && 
/* 152 */         ((getState() & 0x4) == 0));
/*     */ 
/* 154 */       if (((getState() & 0x1) != 0) && 
/* 155 */         (this.progressListener != null)) {
/* 156 */         this.progressListener.setAbort();
/*     */       }
/*     */ 
/* 160 */       finishedDecode();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setFinished()
/*     */   {
/* 320 */     this.finishedDecoding = true;
/*     */   }
/*     */ 
/*     */   public void skipImage()
/*     */     throws JimiException
/*     */   {
/*     */   }
/*     */ 
/*     */   public boolean usesChanneledData()
/*     */   {
/* 303 */     return false;
/*     */   }
/*     */ 
/*     */   protected synchronized void waitReady()
/*     */   {
/* 237 */     while (this.busyDecoding)
/*     */       try {
/* 239 */         wait();
/*     */       }
/*     */       catch (InterruptedException localInterruptedException)
/*     */       {
/*     */       }
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.compat.JimiDecoderBase
 * JD-Core Version:    0.6.2
 */