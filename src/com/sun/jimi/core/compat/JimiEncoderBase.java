/*     */ package com.sun.jimi.core.compat;
/*     */ 
/*     */ import com.sun.jimi.core.JimiEncoder;
/*     */ import com.sun.jimi.core.JimiException;
/*     */ import com.sun.jimi.core.JimiImage;
/*     */ import com.sun.jimi.core.JimiImageEnumeration;
/*     */ import com.sun.jimi.core.JimiImageFactory;
/*     */ import com.sun.jimi.core.raster.JimiRasterImage;
/*     */ import com.sun.jimi.core.util.JimiImageColorReducer;
/*     */ import com.sun.jimi.core.util.JimiUtil;
/*     */ import com.sun.jimi.core.util.ProgressListener;
/*     */ import com.sun.jimi.core.util.ProgressMonitorSupport;
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.IndexColorModel;
/*     */ import java.io.OutputStream;
/*     */ 
/*     */ public abstract class JimiEncoderBase extends ProgressMonitorSupport
/*     */   implements JimiEncoder
/*     */ {
/*     */   public static final int MULTIIMAGE = 1;
/*     */   public static final int ERROR = 1;
/*     */   public static final int DONE = 2;
/*     */   public static final int NEXTIMAGE = 4;
/*     */   protected AdaptiveRasterImage currentImage;
/*     */   protected JimiImageFactory factory;
/*     */   protected static final int MAX_COLORS_RGB = -1;
/*     */ 
/*     */   public AdaptiveRasterImage createAdaptiveRasterImage()
/*     */   {
/* 140 */     return new AdaptiveRasterImage(this.factory);
/*     */   }
/*     */ 
/*     */   public AdaptiveRasterImage createAdaptiveRasterImage(int paramInt1, int paramInt2, ColorModel paramColorModel)
/*     */   {
/* 145 */     AdaptiveRasterImage localAdaptiveRasterImage = new AdaptiveRasterImage(this.factory);
/* 146 */     localAdaptiveRasterImage.setSize(paramInt1, paramInt2);
/* 147 */     localAdaptiveRasterImage.setColorModel(paramColorModel);
/*     */ 
/* 149 */     return localAdaptiveRasterImage;
/*     */   }
/*     */ 
/*     */   public abstract boolean driveEncoder()
/*     */     throws JimiException;
/*     */ 
/*     */   public void encodeImage(JimiImage paramJimiImage, OutputStream paramOutputStream)
/*     */     throws JimiException
/*     */   {
/*  48 */     paramJimiImage.waitFinished();
/*  49 */     if (this.progressListener != null)
/*  50 */       this.progressListener.setStarted();
/*     */     try
/*     */     {
/*  53 */       if (!(paramJimiImage instanceof JimiRasterImage)) {
/*  54 */         throw new JimiException("Only encodes RasterImages.");
/*     */       }
/*  56 */       JimiRasterImage localJimiRasterImage = JimiUtil.asJimiRasterImage(paramJimiImage);
/*  57 */       if ((getMaxColors() != -1) && 
/*  58 */         (!(localJimiRasterImage.getColorModel() instanceof IndexColorModel))) {
/*  59 */         JimiImageColorReducer localObject = new JimiImageColorReducer(getMaxColors());
/*  60 */         localJimiRasterImage = ((JimiImageColorReducer)localObject).colorReduceFS(localJimiRasterImage);
/*     */       }
/*  62 */       Object localObject = new AdaptiveRasterImage(localJimiRasterImage);
/*  63 */       this.currentImage = ((AdaptiveRasterImage)localObject);
/*  64 */       setNumberOfImages(1);
/*  65 */       initSpecificEncoder(paramOutputStream, (AdaptiveRasterImage)localObject);
/*  66 */       setJimiImage((AdaptiveRasterImage)localObject);
/*  67 */       while (driveEncoder());
/*  68 */       if (this.progressListener != null)
/*  69 */         this.progressListener.setFinished();
/*     */     }
/*     */     catch (JimiException localJimiException)
/*     */     {
/*  73 */       if (this.progressListener != null) {
/*  74 */         this.progressListener.setAbort();
/*     */       }
/*  76 */       throw localJimiException;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void encodeImage(JimiImage paramJimiImage, OutputStream paramOutputStream, ProgressListener paramProgressListener)
/*     */     throws JimiException
/*     */   {
/*  83 */     this.progressListener = paramProgressListener;
/*  84 */     encodeImage(paramJimiImage, paramOutputStream);
/*     */   }
/*     */ 
/*     */   public void encodeImages(JimiImageEnumeration paramJimiImageEnumeration, OutputStream paramOutputStream)
/*     */     throws JimiException
/*     */   {
/*  90 */     setNumberOfImages(paramJimiImageEnumeration.countImages());
/*  91 */     if (this.progressListener != null)
/*  92 */       this.progressListener.setStarted();
/*     */     try
/*     */     {
/*  95 */       JimiRasterImage localJimiRasterImage = JimiUtil.asJimiRasterImage(paramJimiImageEnumeration.getNextImage());
/*  96 */       if ((getMaxColors() != -1) && 
/*  97 */         (!(localJimiRasterImage.getColorModel() instanceof IndexColorModel))) {
/*  98 */         JimiImageColorReducer localObject = new JimiImageColorReducer(getMaxColors());
/*  99 */         localJimiRasterImage = ((JimiImageColorReducer)localObject).colorReduceFS(localJimiRasterImage); } Object localObject = new AdaptiveRasterImage(localJimiRasterImage);
/* 102 */       this.currentImage = ((AdaptiveRasterImage)localObject);
/* 103 */       this.factory = localJimiRasterImage.getFactory();
/* 104 */       initSpecificEncoder(paramOutputStream, (AdaptiveRasterImage)localObject);
/* 105 */       setNumberOfImages(paramJimiImageEnumeration.countImages());
/* 106 */       setJimiImage((AdaptiveRasterImage)localObject);
/*     */       int i;
/*     */       do { driveEncoder();
/* 110 */         i = getState();
/* 111 */         if (i == 4) {
/* 112 */           localObject = new AdaptiveRasterImage(JimiUtil.asJimiRasterImage(paramJimiImageEnumeration.getNextImage()));
/* 113 */           setJimiImage((AdaptiveRasterImage)localObject);
/*     */         } }
/* 115 */       while (((i & 0x1) == 0) && ((i & 0x2) == 0));
/* 116 */       if (i == 1) {
/* 117 */         throw new JimiException("Error during encoding.");
/*     */       }
/* 119 */       if (this.progressListener != null) {
/* 120 */         this.progressListener.setFinished();
/*     */       }
/*     */     }
/*     */     catch (JimiException localJimiException)
/*     */     {
/* 125 */       if (this.progressListener != null) {
/* 126 */         this.progressListener.setAbort();
/*     */       }
/* 128 */       throw localJimiException;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void encodeImages(JimiImageEnumeration paramJimiImageEnumeration, OutputStream paramOutputStream, ProgressListener paramProgressListener)
/*     */     throws JimiException
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void freeEncoder()
/*     */     throws JimiException
/*     */   {
/*     */   }
/*     */ 
/*     */   protected int getCapabilties()
/*     */   {
/* 186 */     return 0;
/*     */   }
/*     */ 
/*     */   protected AdaptiveRasterImage getJimiImage()
/*     */   {
/* 181 */     return this.currentImage;
/*     */   }
/*     */ 
/*     */   protected int getMaxColors()
/*     */   {
/* 198 */     return -1;
/*     */   }
/*     */ 
/*     */   protected int getState()
/*     */   {
/* 191 */     return 0;
/*     */   }
/*     */ 
/*     */   protected abstract void initSpecificEncoder(OutputStream paramOutputStream, AdaptiveRasterImage paramAdaptiveRasterImage)
/*     */     throws JimiException;
/*     */ 
/*     */   protected void setJimiImage(AdaptiveRasterImage paramAdaptiveRasterImage)
/*     */     throws JimiException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void setNumberOfImages(int paramInt)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void setProgress(int paramInt)
/*     */   {
/* 156 */     if (this.progressListener != null)
/* 157 */       this.progressListener.setProgressLevel(paramInt);
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.compat.JimiEncoderBase
 * JD-Core Version:    0.6.2
 */