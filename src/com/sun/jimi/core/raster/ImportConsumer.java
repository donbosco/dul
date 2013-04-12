/*     */ package com.sun.jimi.core.raster;
/*     */ 
/*     */ import com.sun.jimi.core.ImageAccessException;
/*     */ import com.sun.jimi.core.JimiException;
/*     */ import com.sun.jimi.core.JimiImageFactory;
/*     */ import com.sun.jimi.core.MutableJimiImage;
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.DirectColorModel;
/*     */ import java.awt.image.ImageConsumer;
/*     */ import java.awt.image.ImageProducer;
/*     */ import java.awt.image.IndexColorModel;
/*     */ import java.util.Hashtable;
/*     */ 
/*     */ class ImportConsumer
/*     */   implements ImageConsumer
/*     */ {
/*     */   protected ImageProducer source;
/*     */   protected JimiImageFactory imageFactory;
/*     */   protected IntRasterImage intImage;
/*     */   protected ByteRasterImage byteImage;
/*     */   protected JimiRasterImage importedImage;
/*  47 */   protected int width = -1; protected int height = -1;
/*     */   protected ColorModel colorModel;
/*     */   protected boolean forceRGB;
/*     */   protected boolean finished;
/*     */   protected boolean aborted;
/*     */ 
/*     */   public ImportConsumer(JimiImageFactory paramJimiImageFactory, ImageProducer paramImageProducer, boolean paramBoolean)
/*     */   {
/*  63 */     this.imageFactory = paramJimiImageFactory;
/*  64 */     this.source = paramImageProducer;
/*  65 */     this.forceRGB = paramBoolean;
/*     */   }
/*     */ 
/*     */   protected synchronized void abort()
/*     */   {
/*  95 */     this.aborted = true;
/*  96 */     setFinished();
/*     */   }
/*     */ 
/*     */   protected void checkProxy(byte[] paramArrayOfByte)
/*     */     throws JimiException
/*     */   {
/* 115 */     if (this.importedImage == null)
/* 116 */       if (this.forceRGB) {
/* 117 */         createIntImage();
/*     */       }
/*     */       else
/* 120 */         createByteImage();
/*     */   }
/*     */ 
/*     */   protected void checkProxy(int[] paramArrayOfInt)
/*     */     throws JimiException
/*     */   {
/* 131 */     if (this.importedImage == null)
/* 132 */       if (this.forceRGB) {
/* 133 */         createIntImage();
/*     */       }
/*     */       else
/* 136 */         createIntImage();
/*     */   }
/*     */ 
/*     */   protected void createByteImage()
/*     */     throws JimiException
/*     */   {
/* 156 */     if (((this.colorModel instanceof IndexColorModel)) && (((IndexColorModel)this.colorModel).getMapSize() <= 2)) {
/* 157 */       this.importedImage = (this.byteImage = this.imageFactory.createBitRasterImage(this.width, this.height, this.colorModel));
/*     */     }
/*     */     else
/* 160 */       this.importedImage = (this.byteImage = this.imageFactory.createByteRasterImage(this.width, this.height, this.colorModel));
/*     */   }
/*     */ 
/*     */   protected void createIntImage()
/*     */     throws JimiException
/*     */   {
/* 147 */     this.importedImage = (this.intImage = this.imageFactory.createIntRasterImage(this.width, this.height, this.colorModel));
/*     */   }
/*     */ 
/*     */   public JimiRasterImage getImage()
/*     */   {
/* 106 */     return this.importedImage;
/*     */   }
/*     */ 
/*     */   public void imageComplete(int paramInt)
/*     */   {
/* 261 */     this.source.removeConsumer(this);
/* 262 */     if (!this.finished)
/* 263 */       if ((!this.finished) && (paramInt != 1) && (paramInt != 4)) {
/* 264 */         setFinished();
/*     */       }
/*     */       else
/* 267 */         abort();
/*     */   }
/*     */ 
/*     */   public boolean isAborted()
/*     */   {
/* 101 */     return this.aborted;
/*     */   }
/*     */ 
/*     */   public void setColorModel(ColorModel paramColorModel)
/*     */   {
/* 179 */     if (this.forceRGB) {
/* 180 */       this.colorModel = new DirectColorModel(24, 16711680, 65280, 255);
/*     */     }
/*     */     else
/* 183 */       this.colorModel = paramColorModel;
/*     */   }
/*     */ 
/*     */   public void setDimensions(int paramInt1, int paramInt2)
/*     */   {
/* 189 */     this.width = paramInt1;
/* 190 */     this.height = paramInt2;
/*     */   }
/*     */ 
/*     */   protected synchronized void setFinished()
/*     */   {
/*  86 */     this.finished = true;
/*  87 */     if (this.importedImage != null) {
/*  88 */       ((MutableJimiRasterImage)this.importedImage).setFinished();
/*     */     }
/*  90 */     notifyAll();
/*     */   }
/*     */ 
/*     */   public void setHints(int paramInt)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void setPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, ColorModel paramColorModel, byte[] paramArrayOfByte, int paramInt5, int paramInt6)
/*     */   {
/* 200 */     if (this.finished)
/* 201 */       return;
/*     */     try
/*     */     {
/* 204 */       checkProxy(paramArrayOfByte);
/*     */     }
/*     */     catch (JimiException localJimiException) {
/* 207 */       abort();
/* 208 */       return;
/*     */     }
/* 210 */     if (this.forceRGB) {
/* 211 */       setPixelsRGB(paramInt1, paramInt2, paramInt3, paramInt4, paramColorModel, paramArrayOfByte, paramInt5, paramInt6);
/*     */     }
/* 214 */     else if (paramColorModel == this.colorModel) {
/*     */       try {
/* 216 */         this.byteImage.setRectangle(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfByte, paramInt5, paramInt6);
/*     */       }
/*     */       catch (ImageAccessException localImageAccessException) {
/* 219 */         abort();
/*     */       }
/*     */     }
/*     */     else
/* 223 */       abort();
/*     */   }
/*     */ 
/*     */   public void setPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, ColorModel paramColorModel, int[] paramArrayOfInt, int paramInt5, int paramInt6)
/*     */   {
/* 231 */     if (this.finished)
/* 232 */       return;
/*     */     try
/*     */     {
/* 235 */       checkProxy(paramArrayOfInt);
/*     */     }
/*     */     catch (JimiException localJimiException) {
/* 238 */       abort();
/* 239 */       return;
/*     */     }
/* 241 */     if (this.forceRGB) {
/* 242 */       setPixelsRGB(paramInt1, paramInt2, paramInt3, paramInt4, paramColorModel, paramArrayOfInt, paramInt5, paramInt6);
/*     */     }
/* 245 */     else if (paramColorModel == this.colorModel) {
/*     */       try {
/* 247 */         this.intImage.setRectangle(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfInt, paramInt5, paramInt6);
/*     */       }
/*     */       catch (ImageAccessException localImageAccessException) {
/* 250 */         abort();
/*     */       }
/*     */     }
/*     */     else
/* 254 */       abort();
/*     */   }
/*     */ 
/*     */   protected void setPixelsRGB(int paramInt1, int paramInt2, int paramInt3, int paramInt4, ColorModel paramColorModel, byte[] paramArrayOfByte, int paramInt5, int paramInt6)
/*     */   {
/* 275 */     int[] arrayOfInt = new int[paramArrayOfByte.length];
/* 276 */     for (int i = 0; i < paramInt4; i++) {
/* 277 */       for (int j = 0; j < paramInt3; j++)
/* 278 */         arrayOfInt[j] = paramColorModel.getRGB(paramArrayOfByte[(paramInt5 + i * paramInt6 + j)]);
/*     */       try
/*     */       {
/* 281 */         this.intImage.setRectangle(paramInt1, paramInt2 + i, paramInt3, 1, arrayOfInt, 0, paramInt3);
/*     */       }
/*     */       catch (ImageAccessException localImageAccessException) {
/* 284 */         abort();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void setPixelsRGB(int paramInt1, int paramInt2, int paramInt3, int paramInt4, ColorModel paramColorModel, int[] paramArrayOfInt, int paramInt5, int paramInt6)
/*     */   {
/* 292 */     int[] arrayOfInt = new int[paramArrayOfInt.length];
/* 293 */     for (int i = 0; i < paramInt4; i++) {
/* 294 */       for (int j = 0; j < paramInt3; j++)
/* 295 */         arrayOfInt[j] = paramColorModel.getRGB(paramArrayOfInt[(paramInt5 + i * paramInt6 + j)]);
/*     */       try
/*     */       {
/* 298 */         this.intImage.setRectangle(paramInt1, paramInt2 + i, paramInt3, 1, arrayOfInt, 0, paramInt3);
/*     */       }
/*     */       catch (ImageAccessException localImageAccessException) {
/* 301 */         abort();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setProperties(Hashtable paramHashtable)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void startImporting()
/*     */   {
/*  70 */     this.source.startProduction(this);
/*     */   }
/*     */ 
/*     */   public synchronized void waitFinished()
/*     */   {
/*  75 */     while (!this.finished)
/*     */       try {
/*  77 */         wait();
/*     */       }
/*     */       catch (InterruptedException localInterruptedException)
/*     */       {
/*     */       }
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.raster.ImportConsumer
 * JD-Core Version:    0.6.2
 */