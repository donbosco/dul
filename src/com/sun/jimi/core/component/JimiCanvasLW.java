/*     */ package com.sun.jimi.core.component;
/*     */ 
/*     */ import com.sun.jimi.core.Jimi;
/*     */ import com.sun.jimi.core.filters.ReplicatingScaleFilter;
/*     */ import com.sun.jimi.core.util.GraphicsUtils;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Image;
/*     */ import java.awt.MediaTracker;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.image.AreaAveragingScaleFilter;
/*     */ import java.awt.image.CropImageFilter;
/*     */ import java.awt.image.FilteredImageSource;
/*     */ import java.awt.image.ImageFilter;
/*     */ import java.net.URL;
/*     */ 
/*     */ public class JimiCanvasLW extends Component
/*     */ {
/*     */   public static final int EAST = 0;
/*     */   public static final int NORTH = 1;
/*     */   public static final int NORTHEAST = 2;
/*     */   public static final int NORTHWEST = 3;
/*     */   public static final int SOUTH = 4;
/*     */   public static final int SOUTHEAST = 5;
/*     */   public static final int SOUTHWEST = 6;
/*     */   public static final int WEST = 7;
/*     */   public static final int CENTER = 8;
/*     */   public static final int BEST_FIT = 0;
/*     */   public static final int CROP_AS_NECESSARY = 1;
/*     */   public static final int SCALE = 2;
/*     */   public static final int SCROLL = 3;
/*     */   public static final int FIT_TO_WIDTH = 4;
/*     */   public static final int AREA_AVERAGING = 0;
/*     */   public static final int REPLICATE = 1;
/*     */   protected transient Image myImage;
/*     */   protected URL myImageLocation;
/* 211 */   protected boolean willSizeToFit = false;
/*     */ 
/* 219 */   protected int justificationPolicy = 8;
/*     */ 
/* 228 */   protected int resizePolicy = 3;
/*     */ 
/* 236 */   protected int scalingPolicy = 1;
/*     */   private transient Image cacheImage;
/*     */   private int lastWidth;
/*     */   private int lastHeight;
/*     */   private int lastResizePolicy;
/*     */   private int lastScalingPolicy;
/*     */   private int lastJustificationPolicy;
/* 266 */   private int fitWidth = -1;
/*     */ 
/*     */   public JimiCanvasLW()
/*     */   {
/*     */   }
/*     */ 
/*     */   public JimiCanvasLW(Image paramImage)
/*     */   {
/* 285 */     this();
/*     */ 
/* 287 */     this.myImage = paramImage;
/* 288 */     this.myImageLocation = null;
/*     */   }
/*     */ 
/*     */   public JimiCanvasLW(URL paramURL)
/*     */   {
/* 299 */     this();
/*     */ 
/* 301 */     this.myImage = null;
/* 302 */     this.myImageLocation = paramURL;
/*     */   }
/*     */ 
/*     */   protected void LoadImage()
/*     */   {
/* 320 */     if (this.myImageLocation == null)
/*     */     {
/* 323 */       return;
/*     */     }
/*     */ 
/* 327 */     Image localImage = Jimi.getImage(this.myImageLocation);
/* 328 */     MediaTracker localMediaTracker = new MediaTracker(this);
/*     */ 
/* 330 */     localMediaTracker.addImage(localImage, 0);
/*     */     try
/*     */     {
/* 334 */       localMediaTracker.waitForAll();
/*     */ 
/* 336 */       if (!localMediaTracker.isErrorAny())
/*     */       {
/* 338 */         setImage(localImage);
/*     */       }
/*     */     }
/*     */     catch (InterruptedException localInterruptedException)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   private synchronized void doResize()
/*     */   {
/* 353 */     if (this.myImage == null) return;
/*     */ 
/* 356 */     setSize(new Dimension(this.myImage.getWidth(this), this.myImage.getHeight(this)));
/*     */ 
/* 358 */     Container localContainer = getParent();
/* 359 */     if (localContainer != null) {
/* 360 */       localContainer.invalidate();
/* 361 */       localContainer.layout();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected Image getCacheImage()
/*     */   {
/* 847 */     if (this.cacheImage == null) {
/* 848 */       this.cacheImage = getResizedImage(this.myImage, size().width, size().height, this.resizePolicy, this.justificationPolicy);
/*     */     }
/* 850 */     return this.cacheImage;
/*     */   }
/*     */ 
/*     */   public Image getImage()
/*     */   {
/* 812 */     return this.myImage;
/*     */   }
/*     */ 
/*     */   public URL getImageLocation()
/*     */   {
/* 749 */     return this.myImageLocation;
/*     */   }
/*     */ 
/*     */   public int getJustificationPolicy()
/*     */   {
/* 635 */     return this.justificationPolicy;
/*     */   }
/*     */ 
/*     */   public Dimension getPreferredSize()
/*     */   {
/* 823 */     if ((this.myImage != null) && 
/* 824 */       (getWillSizeToFit())) {
/* 825 */       if (getResizePolicy() == 4) {
/* 826 */         Image localImage = getCacheImage();
/* 827 */         if (localImage != null)
/* 828 */           return new Dimension(localImage.getWidth(null), localImage.getHeight(null));
/*     */       }
/*     */       else
/*     */       {
/* 832 */         return new Dimension(this.myImage.getWidth(null), this.myImage.getHeight(null));
/*     */       }
/*     */     }
/*     */ 
/* 836 */     return super.getPreferredSize();
/*     */   }
/*     */ 
/*     */   public int getResizePolicy()
/*     */   {
/* 661 */     return this.resizePolicy;
/*     */   }
/*     */ 
/*     */   Image getResizedImage(Image paramImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 414 */     int i = paramImage.getWidth(this);
/* 415 */     int j = paramImage.getHeight(this);
/*     */ 
/* 417 */     if (paramInt3 == 3)
/* 418 */       return paramImage;
/*     */     int i2;
int i1;
/* 421 */     if (paramInt3 == 1)
/*     */     {
/* 426 */       int k = 0;
/* 427 */       int n = 0;
/* 428 */       i1 = i - paramInt1;
/* 429 */       i2 = j - paramInt2;
/*     */ 
/* 431 */       if (i1 < 0)
/*     */       {
/* 433 */         i1 = 0;
/*     */       }
/*     */ 
/* 437 */       if (i2 < 0)
/*     */       {
/* 439 */         i2 = 0;
/*     */       }
/*     */ 
/* 443 */       if ((i1 == 0) && (i2 == 0)) {
/* 444 */         return paramImage;
/*     */       }
/*     */ 
/* 447 */       int i3 = i1 / 2;
/* 448 */       int i4 = i2 / 2;
/*     */ 
/* 450 */       int i5 = paramInt1 < i ? paramInt1 : i;
/* 451 */       int i6 = paramInt2 < j ? paramInt2 : j;
/*     */ 
/* 453 */       switch (paramInt4)
/*     */       {
/*     */       case 8:
/* 457 */         k = i3;
/* 458 */         n = i4;
/* 459 */         break;
/*     */       case 3:
/* 463 */         k = 0;
/* 464 */         n = 0;
/* 465 */         break;
/*     */       case 7:
/* 469 */         k = 0;
/* 470 */         n = i4;
/* 471 */         break;
/*     */       case 6:
/* 475 */         k = 0;
/* 476 */         n = i2;
/* 477 */         break;
/*     */       case 4:
/* 481 */         k = i3;
/* 482 */         n = i2;
/* 483 */         break;
/*     */       case 5:
/* 487 */         k = i1;
/* 488 */         n = i2;
/* 489 */         break;
/*     */       case 0:
/* 493 */         k = i1;
/* 494 */         n = i4;
/* 495 */         break;
/*     */       case 2:
/* 499 */         k = i1;
/* 500 */         n = 0;
/* 501 */         break;
/*     */       case 1:
/* 505 */         k = i3;
/* 506 */         n = 0;
/* 507 */         break;
/*     */       }
/*     */ 
/* 512 */       CropImageFilter localCropImageFilter = new CropImageFilter(k, n, i, j);
/* 513 */       FilteredImageSource localFilteredImageSource4 = new FilteredImageSource(paramImage.getSource(), localCropImageFilter);
/* 514 */       Image localImage = Toolkit.getDefaultToolkit().createImage(localFilteredImageSource4);
/* 515 */       paramImage.flush();
/* 516 */       GraphicsUtils.waitForImage(this, localImage);
/* 517 */       return localImage;
/*     */     }
/* 519 */     if (paramInt3 == 2)
/*     */     {
/*     */       Object localObject1;
/* 529 */       if (getScalingPolicy() == 0)
/* 530 */         localObject1 = new AreaAveragingScaleFilter(paramInt1, paramInt2);
/*     */       else {
/* 532 */         localObject1 = new ReplicatingScaleFilter(paramInt1, paramInt2);
/*     */       }
/* 534 */       FilteredImageSource localFilteredImageSource1 = new FilteredImageSource(paramImage.getSource(), (ImageFilter)localObject1);
/* 535 */       return Toolkit.getDefaultToolkit().createImage(localFilteredImageSource1);
/*     */     }
/*     */ 
/* 538 */     if (paramInt3 == 0)
/*     */     {
/* 541 */       float f1 = paramInt1 / i;
/* 542 */       int f2 = paramInt2 / j;
/*     */ 
/* 549 */       if (f1 < f2)
/*     */       {
/* 551 */         i1 = (int)(i * f1);
/* 552 */         i2 = (int)(j * f1);
/*     */       }
/*     */       else
/*     */       {
/* 556 */         i1 = (int)(i * f2);
/* 557 */         i2 = (int)(j * f2);
/*     */       }
/*     */       Object localObject3;
/* 562 */       if (getScalingPolicy() == 0)
/* 563 */         localObject3 = new AreaAveragingScaleFilter(i1, i2);
/*     */       else {
/* 565 */         localObject3 = new ReplicatingScaleFilter(i1, i2);
/*     */       }
/* 567 */       FilteredImageSource localFilteredImageSource3 = new FilteredImageSource(paramImage.getSource(), (ImageFilter)localObject3);
/* 568 */       return Toolkit.getDefaultToolkit().createImage(localFilteredImageSource3);
/*     */     }
/*     */ 
/* 573 */     if (i < this.fitWidth) {
/* 574 */       return paramImage;
/*     */     }
/* 576 */     int m = this.fitWidth == -1 ? paramInt1 : this.fitWidth;
/* 577 */     float f2 = m / i;
/* 578 */     i1 = (int)(j * f2);
/*     */ 
/* 581 */     if ((this.cacheImage != null) && (this.cacheImage.getWidth(null) == m) && (this.cacheImage.getHeight(null) == i1))
/* 582 */       return this.cacheImage;
/*     */     Object localObject2;
/* 586 */     if (getScalingPolicy() == 0)
/* 587 */       localObject2 = new AreaAveragingScaleFilter(m, i1);
/*     */     else {
/* 589 */       localObject2 = new ReplicatingScaleFilter(m, i1);
/*     */     }
/* 591 */     FilteredImageSource localFilteredImageSource2 = new FilteredImageSource(paramImage.getSource(), (ImageFilter)localObject2);
/* 592 */     return Toolkit.getDefaultToolkit().createImage(localFilteredImageSource2);
/*     */   }
/*     */ 
/*     */   public int getScalingPolicy()
/*     */   {
/* 693 */     return this.scalingPolicy;
/*     */   }
/*     */ 
/*     */   public boolean getWillSizeToFit()
/*     */   {
/* 717 */     return this.willSizeToFit;
/*     */   }
/*     */ 
/*     */   public synchronized void paint(Graphics paramGraphics)
/*     */   {
/* 855 */     if (this.myImage == null)
/*     */     {
/* 857 */       paramGraphics.setColor(getForeground());
/* 858 */       paramGraphics.fillRect(0, 0, size().width, size().height);
/*     */ 
/* 860 */       return;
/*     */     }
/*     */ 
/* 864 */     int i = 0;
/*     */ 
/* 867 */     int k = 0;
/*     */ 
/* 871 */     int j = size().width;
/* 872 */     int m = size().height;
/*     */ 
/* 875 */     if ((this.cacheImage == null) || 
/* 876 */       (j != this.lastWidth) || 
/* 877 */       (m != this.lastHeight) || 
/* 878 */       (this.resizePolicy != this.lastResizePolicy) || 
/* 879 */       (this.justificationPolicy != this.lastJustificationPolicy))
/*     */     {
/* 881 */       this.cacheImage = getResizedImage(this.myImage, j, m, this.resizePolicy, this.justificationPolicy);
/*     */ 
/* 883 */       this.lastWidth = j;
/* 884 */       this.lastHeight = m;
/* 885 */       this.lastResizePolicy = this.resizePolicy;
/* 886 */       this.lastJustificationPolicy = this.justificationPolicy;
/*     */     }
/*     */ 
/* 890 */     int n = this.cacheImage.getWidth(this);
/* 891 */     int i1 = this.cacheImage.getHeight(this);
/*     */ 
/* 894 */     switch (this.justificationPolicy)
/*     */     {
/*     */     case 8:
/* 898 */       i = (j - n) / 2;
/* 899 */       k = (m - i1) / 2;
/* 900 */       break;
/*     */     case 3:
/* 904 */       i = 0;
/* 905 */       k = 0;
/* 906 */       break;
/*     */     case 2:
/* 910 */       i = j - n;
/* 911 */       k = 0;
/* 912 */       break;
/*     */     case 1:
/* 916 */       i = (j - n) / 2;
/* 917 */       k = 0;
/* 918 */       break;
/*     */     case 4:
/* 922 */       i = (j - n) / 2;
/* 923 */       k = m - i1;
/* 924 */       break;
/*     */     case 6:
/* 928 */       i = 0;
/* 929 */       k = m - i1;
/* 930 */       break;
/*     */     case 5:
/* 934 */       i = j - n;
/* 935 */       k = m - i1;
/* 936 */       break;
/*     */     case 0:
/* 940 */       i = j - n;
/* 941 */       k = (m - i1) / 2;
/* 942 */       break;
/*     */     case 7:
/* 946 */       i = 0;
/* 947 */       k = (m - i1) / 2;
/* 948 */       break;
/*     */     }
/*     */ 
/* 953 */     paramGraphics.setColor(getForeground());
/* 954 */     paramGraphics.fillRect(0, 0, j, m);
/* 955 */     paramGraphics.drawImage(this.cacheImage, i, k, n, i1, this);
/*     */   }
/*     */ 
/*     */   public void setFitWidth(int paramInt)
/*     */   {
/* 375 */     this.fitWidth = paramInt;
/*     */   }
/*     */ 
/*     */   public synchronized void setImage(Image paramImage)
/*     */   {
/* 764 */     if (this.cacheImage != null)
/*     */     {
/* 766 */       this.cacheImage.flush();
/* 767 */       this.cacheImage = null;
/*     */     }
/*     */ 
/* 771 */     if (this.myImage != null)
/*     */     {
/* 778 */       this.myImage.flush();
/*     */     }
/*     */ 
/* 782 */     this.myImage = paramImage;
/*     */ 
/* 790 */     if ((this.willSizeToFit) || (getResizePolicy() == 4))
/*     */     {
/* 792 */       doResize();
/*     */     }
/*     */ 
/* 797 */     repaint();
/*     */   }
/*     */ 
/*     */   public synchronized void setImageLocation(URL paramURL)
/*     */   {
/* 733 */     this.myImageLocation = paramURL;
/*     */ 
/* 735 */     LoadImage();
/*     */   }
/*     */ 
/*     */   public synchronized void setJustificationPolicy(int paramInt)
/*     */   {
/* 615 */     this.justificationPolicy = paramInt;
/*     */   }
/*     */ 
/*     */   public synchronized void setResizePolicy(int paramInt)
/*     */   {
/* 649 */     this.resizePolicy = paramInt;
/*     */   }
/*     */ 
/*     */   public synchronized void setScalingPolicy(int paramInt)
/*     */   {
/* 678 */     this.scalingPolicy = paramInt;
/*     */   }
/*     */ 
/*     */   public synchronized void setWillSizeToFit(boolean paramBoolean)
/*     */   {
/* 706 */     this.willSizeToFit = paramBoolean;
/*     */   }
/*     */ 
/*     */   public void update(Graphics paramGraphics)
/*     */   {
/* 842 */     paint(paramGraphics);
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.component.JimiCanvasLW
 * JD-Core Version:    0.6.2
 */