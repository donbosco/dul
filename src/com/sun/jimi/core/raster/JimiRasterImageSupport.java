/*     */ package com.sun.jimi.core.raster;
/*     */ 
/*     */ import com.sun.jimi.core.ImageAccessException;
/*     */ import com.sun.jimi.core.JimiDecodingController;
/*     */ import com.sun.jimi.core.JimiImageFactory;
/*     */ import com.sun.jimi.core.MemoryJimiImageFactory;
/*     */ import com.sun.jimi.core.options.BasicFormatOptionSet;
/*     */ import com.sun.jimi.core.options.FormatOption;
/*     */ import com.sun.jimi.core.options.FormatOptionSet;
/*     */ import com.sun.jimi.core.util.MulticastImageConsumer;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.ImageConsumer;
/*     */ import java.awt.image.ImageProducer;
/*     */ import java.util.Hashtable;
/*     */ 
/*     */ public abstract class JimiRasterImageSupport
/*     */   implements MutableJimiRasterImage, ImageProducer
/*     */ {
/*     */   private static final int HINTS_UNSET = -1;
/*  40 */   protected boolean error = false;
/*  41 */   protected boolean abort = false;
/*     */ 
/*  44 */   protected boolean productionStarted = false;
/*     */ 
/*  47 */   protected boolean modified = false;
/*     */ 
/*  50 */   protected boolean productionAllowed = false;
/*     */ 
/*  53 */   protected boolean finished = false;
/*     */ 
/*  56 */   protected boolean newFrameData = false;
/*     */   protected ColorModel colorModel;
/*  61 */   protected Hashtable properties = new Hashtable();
/*     */ 
/*  63 */   private boolean hasConsumer = false;
/*     */   private int width;
/*     */   private int height;
/*     */   private int state;
/*  74 */   private int DEFAULT_HINTS = 30;
/*     */ 
/*  77 */   private int hints = this.DEFAULT_HINTS;
/*     */ 
/*  80 */   private MulticastImageConsumer directConsumer = new MulticastImageConsumer();
/*  81 */   private MulticastImageConsumer waitingConsumer = new MulticastImageConsumer();
/*     */   private JimiDecodingController decodingController;
/*  87 */   private JimiImageFactory imageFactory = new MemoryJimiImageFactory();
/*     */ 
/*  89 */   private FormatOptionSet options = new BasicFormatOptionSet(new FormatOption[0]);
/*     */ 
/*  91 */   protected ColorModel sourceColorModel = null;
/*  92 */   protected boolean forceRGB = false;
/*     */   protected int[] rowBuf;
/*     */   protected boolean waitForOptions;
/*     */ 
/*     */   protected JimiRasterImageSupport(int paramInt1, int paramInt2, ColorModel paramColorModel)
/*     */   {
/* 106 */     this.width = paramInt1;
/* 107 */     this.height = paramInt2;
/* 108 */     setColorModel(paramColorModel);
/*     */   }
/*     */ 
/*     */   public synchronized void addConsumer(ImageConsumer paramImageConsumer)
/*     */   {
/* 279 */     this.hasConsumer = true;
/*     */ 
/* 281 */     initConsumer(paramImageConsumer);
/*     */ 
/* 284 */     if (!this.productionStarted) {
/* 285 */       addDirectConsumer(paramImageConsumer);
/* 286 */       this.productionStarted = true;
/* 287 */       if (this.decodingController != null) {
/* 288 */         this.decodingController.requestDecoding();
/*     */       }
/*     */ 
/*     */     }
/* 294 */     else if (this.finished)
/*     */     {
/* 296 */       addWaitingConsumer(paramImageConsumer);
/*     */     }
/*     */     else
/*     */     {
/* 303 */       boolean bool = catchupConsumer(paramImageConsumer);
/*     */ 
/* 305 */       if (bool) {
/* 306 */         addDirectConsumer(paramImageConsumer);
/*     */       }
/*     */       else
/*     */       {
/* 310 */         addWaitingConsumer(paramImageConsumer);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void addDirectConsumer(ImageConsumer paramImageConsumer)
/*     */   {
/* 318 */     paramImageConsumer.setDimensions(getWidth(), getHeight());
/* 319 */     this.directConsumer.addConsumer(paramImageConsumer);
/* 320 */     paramImageConsumer.setHints(this.hints);
/*     */   }
/*     */ 
/*     */   protected synchronized void addWaitingConsumer(ImageConsumer paramImageConsumer)
/*     */   {
/* 325 */     paramImageConsumer.setDimensions(getWidth(), getHeight());
/* 326 */     paramImageConsumer.setHints(this.DEFAULT_HINTS);
/* 327 */     if (this.finished) {
/*     */       try {
/* 329 */         sendToConsumerFully(paramImageConsumer);
/* 330 */         paramImageConsumer.imageComplete(3);
/*     */       }
/*     */       catch (ImageAccessException localImageAccessException) {
/* 333 */         paramImageConsumer.imageComplete(1);
/*     */       }
/*     */     }
/*     */     else
/* 337 */       this.waitingConsumer.addConsumer(paramImageConsumer);
/*     */   }
/*     */ 
/*     */   protected synchronized boolean catchupConsumer(ImageConsumer paramImageConsumer)
/*     */   {
/* 347 */     return false;
/*     */   }
/*     */ 
/*     */   protected ColorModel getAppropriateColorModel(ColorModel paramColorModel)
/*     */   {
/* 442 */     return paramColorModel;
/*     */   }
/*     */ 
/*     */   public void getChannelRectangle(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, byte[] paramArrayOfByte, int paramInt6, int paramInt7)
/*     */     throws ImageAccessException
/*     */   {
/*     */     try
/*     */     {
/* 466 */       if (this.rowBuf == null)
/*     */       {
/* 468 */         this.rowBuf = new int[paramInt4];
/*     */       }
/*     */ 
/* 471 */       for (int i = 0; i < paramInt5; i++) {
/* 472 */         getRowRGB(paramInt3 + i, this.rowBuf, 0);
/* 473 */         for (int j = 0; j < paramInt4; j++)
/* 474 */           paramArrayOfByte[(paramInt6 + j + i * paramInt7)] = 
/* 475 */             ((byte)(this.rowBuf[j] >>> paramInt1));
/*     */       }
/*     */     }
/*     */     catch (RuntimeException localRuntimeException)
/*     */     {
/* 480 */       throw new ImageAccessException(localRuntimeException.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized void getChannelRow(int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3) throws ImageAccessException {
/* 485 */     getChannelRectangle(paramInt1, 0, paramInt2, getWidth(), 1, paramArrayOfByte, paramInt3, 0);
/*     */   }
/*     */ 
/*     */   public ColorModel getColorModel()
/*     */   {
/* 386 */     return this.colorModel;
/*     */   }
/*     */ 
/*     */   public ImageProducer getCroppedImageProducer(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 396 */     return new CroppedRasterImageProducer(this, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */   }
/*     */ 
/*     */   protected MulticastImageConsumer getDirectConsumer()
/*     */   {
/* 417 */     return this.directConsumer;
/*     */   }
/*     */ 
/*     */   public JimiImageFactory getFactory()
/*     */   {
/* 185 */     return this.imageFactory;
/*     */   }
/*     */ 
/*     */   public int getHeight()
/*     */   {
/* 126 */     return this.height;
/*     */   }
/*     */ 
/*     */   public ImageProducer getImageProducer()
/*     */   {
/* 357 */     return this;
/*     */   }
/*     */ 
/*     */   public FormatOptionSet getOptions()
/*     */   {
/* 201 */     if (this.waitForOptions) {
/* 202 */       waitFinished();
/*     */     }
/* 204 */     return this.options;
/*     */   }
/*     */ 
/*     */   public abstract int getPixelRGB(int paramInt1, int paramInt2)
/*     */     throws ImageAccessException;
/*     */ 
/*     */   public Hashtable getProperties()
/*     */   {
/* 391 */     return this.properties;
/*     */   }
/*     */ 
/*     */   public synchronized void getRectangleARGBChannels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte, int paramInt5, int paramInt6)
/*     */     throws ImageAccessException
/*     */   {
/* 510 */     if (this.rowBuf == null)
/*     */     {
/* 512 */       this.rowBuf = new int[paramInt3];
/*     */     }
/* 514 */     for (int i = 0; i < paramInt4; i++) {
/* 515 */       getRowRGB(paramInt2 + i, this.rowBuf, 0);
/* 516 */       int j = 0;
/* 517 */       int k = 0;
/* 518 */       for (int m = 0; m < paramInt3; m++) {
/* 519 */         paramArrayOfByte[(i * paramInt6 + k++ + paramInt5)] = ((byte)(this.rowBuf[j] >>> 24));
/* 520 */         paramArrayOfByte[(i * paramInt6 + k++ + paramInt5)] = ((byte)(this.rowBuf[j] >>> 16));
/* 521 */         paramArrayOfByte[(i * paramInt6 + k++ + paramInt5)] = ((byte)(this.rowBuf[j] >>> 8));
/* 522 */         paramArrayOfByte[(i * paramInt6 + k++ + paramInt5)] = ((byte)this.rowBuf[(j++)]);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public abstract void getRectangleRGB(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt, int paramInt5, int paramInt6) throws ImageAccessException;
/*     */ 
/*     */   public synchronized void getRectangleRGBAChannels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte, int paramInt5, int paramInt6) throws ImageAccessException {
/* 530 */     if (this.rowBuf == null)
/*     */     {
/* 532 */       this.rowBuf = new int[paramInt3];
/*     */     }
/* 534 */     for (int i = 0; i < paramInt4; i++) {
/* 535 */       getRowRGB(paramInt2 + i, this.rowBuf, 0);
/* 536 */       int j = 0;
/* 537 */       int k = 0;
/* 538 */       for (int m = 0; m < paramInt3; m++) {
/* 539 */         paramArrayOfByte[(i * paramInt6 + k++ + paramInt5)] = ((byte)(this.rowBuf[j] >>> 16));
/* 540 */         paramArrayOfByte[(i * paramInt6 + k++ + paramInt5)] = ((byte)(this.rowBuf[j] >>> 8));
/* 541 */         paramArrayOfByte[(i * paramInt6 + k++ + paramInt5)] = ((byte)this.rowBuf[j]);
/* 542 */         paramArrayOfByte[(i * paramInt6 + k++ + paramInt5)] = ((byte)(this.rowBuf[(j++)] >>> 24));
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized void getRectangleRGBChannels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte, int paramInt5, int paramInt6)
/*     */     throws ImageAccessException
/*     */   {
/* 491 */     if (this.rowBuf == null)
/*     */     {
/* 493 */       this.rowBuf = new int[paramInt3];
/*     */     }
/* 495 */     for (int i = 0; i < paramInt4; i++) {
/* 496 */       getRowRGB(paramInt2 + i, this.rowBuf, 0);
/* 497 */       int j = 0;
/* 498 */       int k = 0;
/* 499 */       for (int m = 0; m < paramInt3; m++) {
/* 500 */         paramArrayOfByte[(i * paramInt6 + k++ + paramInt5)] = ((byte)(this.rowBuf[j] >>> 16));
/* 501 */         paramArrayOfByte[(i * paramInt6 + k++ + paramInt5)] = ((byte)(this.rowBuf[j] >>> 8));
/* 502 */         paramArrayOfByte[(i * paramInt6 + k++ + paramInt5)] = ((byte)this.rowBuf[(j++)]);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public abstract void getRowRGB(int paramInt1, int[] paramArrayOfInt, int paramInt2)
/*     */     throws ImageAccessException;
/*     */ 
/*     */   public int getWidth()
/*     */   {
/* 117 */     return this.width;
/*     */   }
/*     */ 
/*     */   protected boolean hasDirectConsumer()
/*     */   {
/* 422 */     return this.directConsumer.isEmpty() ^ true;
/*     */   }
/*     */ 
/*     */   protected void initConsumer(ImageConsumer paramImageConsumer)
/*     */   {
/* 433 */     paramImageConsumer.setColorModel(getColorModel());
/* 434 */     paramImageConsumer.setProperties(this.properties);
/*     */   }
/*     */ 
/*     */   public boolean isConsumer(ImageConsumer paramImageConsumer)
/*     */   {
/* 362 */     return (this.directConsumer.contains(paramImageConsumer)) || (this.waitingConsumer.contains(paramImageConsumer));
/*     */   }
/*     */ 
/*     */   public boolean isError()
/*     */   {
/* 158 */     return this.error;
/*     */   }
/*     */ 
/*     */   public boolean mustWaitForOptions()
/*     */   {
/* 554 */     return this.waitForOptions;
/*     */   }
/*     */ 
/*     */   public synchronized void produceCroppedImage(ImageConsumer paramImageConsumer, Rectangle paramRectangle)
/*     */   {
/* 401 */     waitFinished();
/*     */ 
/* 403 */     initConsumer(paramImageConsumer);
/* 404 */     paramImageConsumer.setDimensions(paramRectangle.width, paramRectangle.height);
/* 405 */     paramImageConsumer.setHints(this.DEFAULT_HINTS);
/*     */     try {
/* 407 */       sendRegionToConsumerFully(paramImageConsumer, paramRectangle);
/* 408 */       paramImageConsumer.imageComplete(3);
/*     */     }
/*     */     catch (Exception localException) {
/* 411 */       paramImageConsumer.imageComplete(1);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void removeConsumer(ImageConsumer paramImageConsumer)
/*     */   {
/* 367 */     this.directConsumer.removeConsumer(paramImageConsumer);
/* 368 */     this.waitingConsumer.removeConsumer(paramImageConsumer);
/*     */   }
/*     */ 
/*     */   public void requestTopDownLeftRightResend(ImageConsumer paramImageConsumer)
/*     */   {
/* 381 */     addWaitingConsumer(paramImageConsumer);
/*     */   }
/*     */ 
/*     */   protected abstract void sendRegionToConsumerFully(ImageConsumer paramImageConsumer, Rectangle paramRectangle)
/*     */     throws ImageAccessException;
/*     */ 
/*     */   protected abstract void sendToConsumerFully(ImageConsumer paramImageConsumer)
/*     */     throws ImageAccessException;
/*     */ 
/*     */   public void setColorModel(ColorModel paramColorModel)
/*     */   {
/* 134 */     if (!this.forceRGB)
/*     */     {
/* 136 */       this.colorModel = getAppropriateColorModel(paramColorModel);
/* 137 */       this.forceRGB |= paramColorModel != this.colorModel;
/*     */     }
/* 139 */     this.sourceColorModel = paramColorModel;
/*     */   }
/*     */ 
/*     */   public synchronized void setDecodingController(JimiDecodingController paramJimiDecodingController)
/*     */   {
/* 267 */     this.decodingController = paramJimiDecodingController;
/* 268 */     if ((this.hasConsumer) || (this.productionStarted))
/* 269 */       paramJimiDecodingController.requestDecoding();
/*     */   }
/*     */ 
/*     */   public synchronized void setError()
/*     */   {
/* 166 */     this.error = true;
/* 167 */     this.directConsumer.imageComplete(1);
/* 168 */     this.waitingConsumer.imageComplete(1);
/* 169 */     notifyAll();
/*     */   }
/*     */ 
/*     */   public void setFactory(JimiImageFactory paramJimiImageFactory)
/*     */   {
/* 177 */     this.imageFactory = paramJimiImageFactory;
/*     */   }
/*     */ 
/*     */   public synchronized void setFinished()
/*     */   {
/* 213 */     if (this.finished) {
/* 214 */       return;
/*     */     }
/*     */ 
/* 217 */     this.finished = true;
/*     */ 
/* 219 */     this.newFrameData = false;
/*     */ 
/* 221 */     this.directConsumer.imageComplete(3);
/*     */ 
/* 223 */     this.directConsumer.removeAll();
/* 224 */     if (!this.waitingConsumer.isEmpty()) {
/*     */       try {
/* 226 */         sendToConsumerFully(this.waitingConsumer);
/*     */       }
/*     */       catch (ImageAccessException localImageAccessException) {
/* 229 */         setError();
/* 230 */         return;
/*     */       }
/*     */     }
/* 233 */     ImageConsumer[] arrayOfImageConsumer = this.waitingConsumer.getConsumers();
/*     */ 
/* 235 */     this.waitingConsumer.imageComplete(3);
/*     */ 
/* 237 */     this.waitingConsumer.removeAll();
/*     */ 
/* 239 */     notifyAll();
/*     */   }
/*     */ 
/*     */   public void setImageConsumerHints(int paramInt)
/*     */   {
/* 149 */     paramInt |= 16;
/* 150 */     this.hints = paramInt;
/*     */   }
/*     */ 
/*     */   protected void setModified()
/*     */   {
/* 447 */     if (!this.modified) {
/* 448 */       this.modified = true;
/* 449 */       this.directConsumer.setHints(this.hints);
/* 450 */       this.productionStarted = true;
/* 451 */       if (this.decodingController != null)
/* 452 */         this.decodingController.requestDecoding();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setOptions(FormatOptionSet paramFormatOptionSet)
/*     */   {
/* 193 */     this.options = paramFormatOptionSet;
/*     */   }
/*     */ 
/*     */   public void setWaitForOptions(boolean paramBoolean)
/*     */   {
/* 549 */     this.waitForOptions = paramBoolean;
/*     */   }
/*     */ 
/*     */   public void startProduction(ImageConsumer paramImageConsumer)
/*     */   {
/* 375 */     removeConsumer(paramImageConsumer);
/* 376 */     addConsumer(paramImageConsumer);
/*     */   }
/*     */ 
/*     */   public synchronized void waitFinished()
/*     */   {
/* 251 */     this.productionStarted = true;
/* 252 */     if (this.decodingController != null) {
/* 253 */       this.decodingController.requestDecoding();
/*     */     }
/* 255 */     while ((!this.finished) && (!this.error))
/*     */       try {
/* 257 */         wait();
/*     */       }
/*     */       catch (InterruptedException localInterruptedException)
/*     */       {
/*     */       }
/*     */   }
/*     */ 
/*     */   public void waitInfoAvailable()
/*     */   {
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.raster.JimiRasterImageSupport
 * JD-Core Version:    0.6.2
 */