/*     */ package com.sun.jimi.core.component;
/*     */ 
/*     */ import com.sun.jimi.core.Jimi;
/*     */ import com.sun.jimi.core.JimiException;
/*     */ import com.sun.jimi.core.JimiImage;
/*     */ import com.sun.jimi.core.JimiReader;
/*     */ import com.sun.jimi.core.JimiUtils;
/*     */ import com.sun.jimi.core.raster.JimiRasterImage;
/*     */ import com.sun.jimi.core.util.GraphicsUtils;
/*     */ import com.sun.jimi.core.util.ProgressListener;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Image;
/*     */ import java.awt.image.ImageProducer;
/*     */ import java.net.URL;
/*     */ 
/*     */ public class JimiCanvas extends Container
/*     */ {
/*     */   public static final int NORTH = 2;
/*     */   public static final int SOUTH = 4;
/*     */   public static final int EAST = 8;
/*     */   public static final int WEST = 16;
/*     */   public static final int NORTHEAST = 10;
/*     */   public static final int NORTHWEST = 18;
/*     */   public static final int SOUTHEAST = 12;
/*     */   public static final int SOUTHWEST = 20;
/*     */   public static final int CENTER = 0;
/*     */   public static final int BEST_FIT = 0;
/*     */   public static final int CROP = 1;
/*     */   public static final int SCALE = 2;
/*     */   public static final int SCROLL = 3;
/*     */   public static final int FIT_WIDTH = 4;
/*     */   public static final int AREA = 5;
/*     */   public static final int MULTIPAGE = 6;
/*     */   public static final int PAGED = 7;
/*     */   public static final int CROP_AS_NECESSARY = 1;
/*     */   public static final int FIT_TO_WIDTH = 4;
/*     */   public static final int AREA_AVERAGING = 0;
/*     */   public static final int REPLICATE = 1;
/*     */   protected transient Image myImage;
/*     */   protected transient ImageProducer myImageProducer;
/*     */   protected transient URL myImageLocation;
/* 104 */   protected boolean willSizeToFit = true;
/*     */ 
/* 106 */   protected int fitWidth = -1;
/*     */   protected JimiImageRenderer renderer;
/* 113 */   protected int justificationPolicy = 0;
/* 114 */   protected int resizePolicy = 7;
/* 115 */   protected int scalingPolicy = 1;
/*     */   protected ProgressListener progressListener;
/*     */   protected int loadingFlags;
/* 121 */   protected boolean aspectAdjust = false;
/*     */   protected ImageCache imageCache;
/*     */ 
/*     */   public JimiCanvas()
/*     */   {
/* 129 */     setLayout(new BorderLayout());
/*     */ 
/* 132 */     setResizePolicy(7);
/*     */   }
/*     */ 
/*     */   public JimiCanvas(int paramInt)
/*     */   {
/* 179 */     setLayout(new BorderLayout());
/* 180 */     setResizePolicy(paramInt);
/*     */   }
/*     */ 
/*     */   public JimiCanvas(Image paramImage)
/*     */   {
/* 143 */     this();
/* 144 */     setImage(paramImage);
/*     */   }
/*     */ 
/*     */   public JimiCanvas(String paramString)
/*     */   {
/* 169 */     this();
/* 170 */     setImagePath(paramString);
/*     */   }
/*     */ 
/*     */   public JimiCanvas(URL paramURL)
/*     */   {
/* 155 */     this();
/* 156 */     this.myImageLocation = paramURL;
/*     */ 
/* 158 */     setImageLocation(paramURL);
/*     */   }
/*     */ 
/*     */   private synchronized void doResize()
/*     */   {
/* 312 */     if (this.myImage == null) return;
/*     */ 
/* 315 */     resize(new Dimension(this.myImage.getWidth(this), this.myImage.getHeight(this)));
/*     */ 
/* 317 */     Container localContainer = getParent();
/* 318 */     if (localContainer != null)
/*     */     {
/* 320 */       localContainer.invalidate();
/* 321 */       localContainer.layout();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void firstImage()
/*     */   {
/* 640 */     if (this.imageCache == null) {
/* 641 */       return;
/*     */     }
/* 643 */     JimiRasterImage localJimiRasterImage = null;
/* 644 */     while (this.imageCache.hasPreviousImage()) {
/* 645 */       localJimiRasterImage = this.imageCache.getPreviousImage();
/*     */     }
/* 647 */     if (localJimiRasterImage != null)
/* 648 */       setRasterImage(localJimiRasterImage);
/*     */   }
/*     */ 
/*     */   public int getFitWidth()
/*     */   {
/* 339 */     return this.fitWidth;
/*     */   }
/*     */ 
/*     */   public Image getImage()
/*     */   {
/* 534 */     if (this.myImage != null) {
/* 535 */       return this.myImage;
/*     */     }
/* 537 */     if (this.myImageProducer != null) {
/* 538 */       this.myImage = createImage(this.myImageProducer);
/* 539 */       GraphicsUtils.waitForImage(this.myImage);
/* 540 */       return this.myImage;
/*     */     }
/*     */ 
/* 543 */     return null;
/*     */   }
/*     */ 
/*     */   public URL getImageLocation()
/*     */   {
/* 492 */     return this.myImageLocation;
/*     */   }
/*     */ 
/*     */   public int getJustificationPolicy()
/*     */   {
/* 371 */     return this.justificationPolicy;
/*     */   }
/*     */ 
/*     */   public JimiImageRenderer getRenderer()
/*     */   {
/* 205 */     return this.renderer;
/*     */   }
/*     */ 
/*     */   protected JimiImageRenderer getRenderer(int paramInt)
/*     */   {
/* 220 */     if (paramInt == 3) {
/* 221 */       this.renderer = new ScrollRenderer(this);
/*     */     }
/* 224 */     else if (paramInt == 1) {
/* 225 */       this.renderer = new CropRenderer(this);
/*     */     }
/* 228 */     else if (paramInt == 2) {
/* 229 */       this.renderer = new ScaleRenderer(this);
/*     */     }
/* 232 */     else if (paramInt == 0) {
/* 233 */       this.renderer = new BestFitRenderer(this);
/*     */     }
/* 236 */     else if (paramInt == 4) {
/* 237 */       this.renderer = new FitWidthRenderer(this);
/*     */     }
/* 240 */     else if (paramInt == 5) {
/* 241 */       this.renderer = new AreaRenderer(this);
/*     */     }
/* 243 */     else if (paramInt == 7) {
/* 244 */       this.renderer = new SmartScrollingRenderer(this);
/*     */     }
/*     */ 
/* 247 */     return this.renderer;
/*     */   }
/*     */ 
/*     */   public int getResizePolicy()
/*     */   {
/* 408 */     return this.resizePolicy;
/*     */   }
/*     */ 
/*     */   public int getScalingPolicy()
/*     */   {
/* 436 */     return this.scalingPolicy;
/*     */   }
/*     */ 
/*     */   public boolean getWillSizeToFit()
/*     */   {
/* 455 */     return this.willSizeToFit;
/*     */   }
/*     */ 
/*     */   public boolean hasNextImage()
/*     */   {
/* 674 */     return this.imageCache == null ? false : this.imageCache.hasNextImage();
/*     */   }
/*     */ 
/*     */   public boolean hasPreviousImage()
/*     */   {
/* 682 */     return this.imageCache == null ? false : this.imageCache.hasPreviousImage();
/*     */   }
/*     */ 
/*     */   public void lastImage()
/*     */   {
/* 657 */     if (this.imageCache == null) {
/* 658 */       return;
/*     */     }
/* 660 */     JimiRasterImage localJimiRasterImage = null;
/* 661 */     while (this.imageCache.hasNextImage()) {
/* 662 */       localJimiRasterImage = this.imageCache.getNextImage();
/*     */     }
/* 664 */     if (localJimiRasterImage != null)
/* 665 */       setRasterImage(localJimiRasterImage);
/*     */   }
/*     */ 
/*     */   protected ImageProducer loadImageProducer(JimiReader paramJimiReader)
/*     */   {
/* 273 */     if (this.progressListener != null) {
/* 274 */       paramJimiReader.setProgressListener(this.progressListener);
/*     */     }
/* 276 */     this.imageCache = new ImageCache(paramJimiReader, false);
/* 277 */     return this.imageCache.getNextImage().getImageProducer();
/*     */   }
/*     */ 
/*     */   protected ImageProducer loadImageProducer(String paramString)
/*     */   {
/*     */     try
/*     */     {
/* 291 */       return loadImageProducer(Jimi.createJimiReader(paramString, this.loadingFlags));
/*     */     } catch (Exception localException) {
/* 293 */       showError(localException.toString());
/* 294 */     }return null;
/*     */   }
/*     */ 
/*     */   protected ImageProducer loadImageProducer(URL paramURL)
/*     */   {
/*     */     try
/*     */     {
/* 282 */       return loadImageProducer(Jimi.createJimiReader(paramURL, this.loadingFlags));
/*     */     } catch (Exception localException) {
/* 284 */       showError(localException.toString());
/* 285 */     }return null;
/*     */   }
/*     */ 
/*     */   public void nextImage()
/*     */   {
/*     */     try
/*     */     {
/* 617 */       setRasterImage(this.imageCache.getNextImage());
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public void previousImage()
/*     */   {
/*     */     try
/*     */     {
/* 629 */       setRasterImage(this.imageCache.getPreviousImage());
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setAspectAdjust(boolean paramBoolean)
/*     */   {
/* 691 */     this.aspectAdjust = paramBoolean;
/*     */   }
/*     */ 
/*     */   public void setFitWidth(int paramInt)
/*     */   {
/* 332 */     this.fitWidth = paramInt;
/*     */   }
/*     */ 
/*     */   public synchronized void setImage(Image paramImage)
/*     */   {
/* 504 */     this.myImage = paramImage;
/* 505 */     if (this.renderer != null) {
/* 506 */       this.renderer.setImage(paramImage);
/*     */     }
/*     */ 
/* 509 */     validate();
/*     */   }
/*     */ 
/*     */   public synchronized void setImageLocation(URL paramURL)
/*     */   {
/* 482 */     setImageProducer(loadImageProducer(paramURL));
/*     */   }
/*     */ 
/*     */   public synchronized void setImagePath(String paramString)
/*     */   {
/* 520 */     setImageProducer(loadImageProducer(paramString));
/*     */   }
/*     */ 
/*     */   public void setImageProducer(ImageProducer paramImageProducer)
/*     */   {
/* 555 */     this.myImage = null;
/* 556 */     if (this.renderer != null) {
/* 557 */       if ((paramImageProducer != null) && (this.aspectAdjust)) {
/*     */         try {
/* 559 */           paramImageProducer = JimiUtils.aspectAdjust(Jimi.createRasterImage(paramImageProducer));
/*     */         }
/*     */         catch (JimiException localJimiException) {
/* 562 */           paramImageProducer = null;
/*     */         }
/*     */       }
/* 565 */       this.myImageProducer = paramImageProducer;
/* 566 */       this.renderer.setImageProducer(paramImageProducer);
/* 567 */       invalidate();
/* 568 */       validate();
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized void setJustificationPolicy(int paramInt)
/*     */   {
/* 361 */     this.justificationPolicy = paramInt;
/* 362 */     if (this.renderer != null)
/* 363 */       this.renderer.render();
/*     */   }
/*     */ 
/*     */   public void setLoadingFlags(int paramInt)
/*     */   {
/* 264 */     this.loadingFlags = paramInt;
/* 265 */     this.loadingFlags &= -3;
/*     */   }
/*     */ 
/*     */   public void setProgressListener(ProgressListener paramProgressListener)
/*     */   {
/* 255 */     this.progressListener = paramProgressListener;
/*     */   }
/*     */ 
/*     */   public void setRasterImage(JimiRasterImage paramJimiRasterImage)
/*     */   {
/* 579 */     setImageProducer(paramJimiRasterImage == null ? null : paramJimiRasterImage.getImageProducer());
/*     */   }
/*     */ 
/*     */   public void setRenderer(JimiImageRenderer paramJimiImageRenderer)
/*     */   {
/* 188 */     this.renderer = paramJimiImageRenderer;
/* 189 */     if (this.myImage != null) {
/* 190 */       paramJimiImageRenderer.setImage(this.myImage);
/*     */     }
/* 192 */     else if (this.myImageProducer != null) {
/* 193 */       paramJimiImageRenderer.setImageProducer(this.myImageProducer);
/*     */     }
/* 195 */     removeAll();
/* 196 */     add(paramJimiImageRenderer.getContentPane(), "Center");
/* 197 */     invalidate();
/* 198 */     validate();
/*     */   }
/*     */ 
/*     */   public synchronized void setResizePolicy(int paramInt)
/*     */   {
/* 386 */     this.resizePolicy = paramInt;
/* 387 */     this.renderer = getRenderer(this.resizePolicy);
/* 388 */     this.renderer.getContentPane().setBackground(getBackground());
/* 389 */     this.renderer.getContentPane().setForeground(getForeground());
/* 390 */     setRenderer(this.renderer);
/* 391 */     if (this.myImage != null) {
/* 392 */       this.renderer.setImage(this.myImage);
/*     */     }
/* 394 */     else if (this.myImageProducer != null)
/* 395 */       this.renderer.setImageProducer(this.myImageProducer);
/*     */   }
/*     */ 
/*     */   public synchronized void setScalingPolicy(int paramInt)
/*     */   {
/* 423 */     this.scalingPolicy = paramInt;
/*     */   }
/*     */ 
/*     */   public synchronized void setWillSizeToFit(boolean paramBoolean)
/*     */   {
/* 446 */     this.willSizeToFit = paramBoolean;
/*     */   }
/*     */ 
/*     */   protected void showError(String paramString)
/*     */   {
/* 300 */     if (this.progressListener != null)
/* 301 */       this.progressListener.setAbort(paramString);
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.component.JimiCanvas
 * JD-Core Version:    0.6.2
 */