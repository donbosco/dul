/*     */ package com.sun.jimi.core;
/*     */ 
/*     */ import com.sun.jimi.core.raster.JimiRasterImage;
/*     */ import com.sun.jimi.core.util.FreeFormat;
/*     */ import com.sun.jimi.core.util.GraphicsUtils;
/*     */ import com.sun.jimi.core.util.JimiUtil;
/*     */ import com.sun.jimi.core.util.ProgressListener;
/*     */ import com.sun.jimi.core.util.StampImageFilter;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Image;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.image.FilteredImageSource;
/*     */ import java.awt.image.ImageFilter;
/*     */ import java.awt.image.ImageProducer;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PushbackInputStream;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.util.Enumeration;
/*     */ 
/*     */ public class JimiReader
/*     */ {
/*     */   public static final int UNKNOWN = -1;
/*     */   protected static final int STREAM_BUFFER_SIZE = 10240;
/*     */   protected JimiDecoderFactory decoderFactory;
/*     */   protected JimiDecoder decoder;
/*     */   protected JimiImageFactory imageFactory;
/*     */   protected InputStream input;
/*     */   protected JimiRasterImage cacheJimiImage;
/*     */   protected ImageProducer cacheImageProducer;
/*     */   protected Image cacheImage;
/*  46 */   protected int cacheIndex = -1;
/*     */ 
/*  49 */   protected int seriesIndex = 0;
/*     */   protected ImageSeriesDecodingController series;
/*     */   protected boolean synchronous;
/*     */   protected boolean builtinJPEG;
/*     */   protected ImageProducer jpegProducer;
/*     */   protected URL location;
/*     */   protected String filename;
/*     */   protected ProgressListener listener;
/*     */   protected Runnable cleanupCommand;
/*     */ 
/*     */   protected JimiReader(JimiImageFactory paramJimiImageFactory)
/*     */     throws JimiException
/*     */   {
/* 233 */     initReader(paramJimiImageFactory);
/*     */   }
/*     */ 
/*     */   protected JimiReader(JimiImageFactory paramJimiImageFactory, JimiDecoderFactory paramJimiDecoderFactory, InputStream paramInputStream)
/*     */     throws JimiException
/*     */   {
/*  76 */     initReader(paramJimiImageFactory, paramJimiDecoderFactory, paramInputStream);
/*     */   }
/*     */ 
/*     */   protected JimiReader(JimiImageFactory paramJimiImageFactory, InputStream paramInputStream)
/*     */     throws JimiException
/*     */   {
/* 217 */     PushbackInputStream localPushbackInputStream = new PushbackInputStream(paramInputStream, 128);
/* 218 */     JimiDecoderFactory localJimiDecoderFactory = JimiControl.getDecoderForInputStream(localPushbackInputStream);
/* 219 */     if (localJimiDecoderFactory == null) {
/* 220 */       throw new JimiException("Cannot find decoder for stream");
/*     */     }
/* 222 */     initReader(paramJimiImageFactory, localJimiDecoderFactory, localPushbackInputStream);
/*     */   }
/*     */ 
/*     */   protected JimiReader(JimiImageFactory paramJimiImageFactory, InputStream paramInputStream, String paramString)
/*     */     throws JimiException
/*     */   {
/*  89 */     JimiDecoderFactory localJimiDecoderFactory = JimiControl.getDecoderByType(paramString);
/*  90 */     if (localJimiDecoderFactory == null) {
/*  91 */       throw new JimiException("No decoder available for " + paramString);
/*     */     }
/*  93 */     initReader(paramJimiImageFactory, localJimiDecoderFactory, paramInputStream);
/*     */   }
/*     */ 
/*     */   protected JimiReader(JimiImageFactory paramJimiImageFactory, String paramString)
/*     */     throws JimiException
/*     */   {
/* 192 */     this.cleanupCommand = new StreamCloseCommand();
/* 193 */     this.filename = paramString;
/*     */     try {
/* 195 */       JimiDecoderFactory localJimiDecoderFactory = JimiControl.getDecoderByFileExtension(paramString);
/* 196 */       if (localJimiDecoderFactory == null) {
/* 197 */         throw new JimiException("No decoder available for file: " + paramString);
/*     */       }
/* 199 */       Object localObject = new FileInputStream(paramString);
/* 200 */       localObject = new BufferedInputStream((InputStream)localObject, 10240);
/* 201 */       initReader(paramJimiImageFactory, localJimiDecoderFactory, (InputStream)localObject);
/*     */     }
/*     */     catch (IOException localIOException) {
/* 204 */       throw new JimiException(localIOException.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   protected JimiReader(JimiImageFactory paramJimiImageFactory, String paramString1, String paramString2)
/*     */     throws JimiException
/*     */   {
/* 106 */     this.cleanupCommand = new StreamCloseCommand();
/* 107 */     JimiDecoderFactory localJimiDecoderFactory = JimiControl.getDecoderByType(paramString2);
/* 108 */     if (localJimiDecoderFactory == null)
/* 109 */       throw new JimiException("No decoder available for " + paramString2);
/*     */     try
/*     */     {
/* 112 */       Object localObject = new FileInputStream(paramString1);
/* 113 */       localObject = new BufferedInputStream((InputStream)localObject, 10240);
/* 114 */       initReader(this.imageFactory, localJimiDecoderFactory, (InputStream)localObject);
/*     */     }
/*     */     catch (IOException localIOException) {
/* 117 */       throw new JimiException(localIOException.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   protected JimiReader(JimiImageFactory paramJimiImageFactory, URL paramURL)
/*     */     throws JimiException
/*     */   {
/* 130 */     this.cleanupCommand = new StreamCloseCommand();
/* 131 */     this.location = paramURL;
/*     */     try {
/* 133 */       JimiDecoderFactory localJimiDecoderFactory = 
/* 134 */         JimiControl.getDecoderByFileExtension(paramURL.toString());
/* 135 */       URLConnection localURLConnection = paramURL.openConnection();
/* 136 */       Object localObject = paramURL.openStream();
/* 137 */       if (localJimiDecoderFactory == null) {
/* 138 */         localJimiDecoderFactory = JimiControl.getDecoderByType(localURLConnection.getContentType());
/*     */       }
/* 140 */       if (localJimiDecoderFactory == null) {
/* 141 */         PushbackInputStream localPushbackInputStream = new PushbackInputStream((InputStream)localObject, 128);
/* 142 */         localJimiDecoderFactory = JimiControl.getDecoderForInputStream(localPushbackInputStream);
/* 143 */         localObject = localPushbackInputStream;
/*     */       }
/* 145 */       if (localJimiDecoderFactory == null) {
/* 146 */         throw new JimiException("No decoder available for location: " + paramURL);
/*     */       }
/* 148 */       localObject = new BufferedInputStream((InputStream)localObject, 10240);
/* 149 */       initReader(paramJimiImageFactory, localJimiDecoderFactory, (InputStream)localObject);
/*     */     }
/*     */     catch (IOException localIOException) {
/* 152 */       throw new JimiException(localIOException.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   protected JimiReader(JimiImageFactory paramJimiImageFactory, URL paramURL, String paramString)
/*     */     throws JimiException
/*     */   {
/* 166 */     this.cleanupCommand = new StreamCloseCommand();
/* 167 */     this.location = paramURL;
/*     */     try {
/* 169 */       JimiDecoderFactory localJimiDecoderFactory = 
/* 170 */         JimiControl.getDecoderByType(paramString);
/* 171 */       if (localJimiDecoderFactory == null) {
/* 172 */         throw new JimiException("No decoder available for file: " + paramURL);
/*     */       }
/* 174 */       Object localObject = paramURL.openStream();
/* 175 */       localObject = new BufferedInputStream((InputStream)localObject, 10240);
/* 176 */       initReader(paramJimiImageFactory, localJimiDecoderFactory, (InputStream)localObject);
/*     */     }
/*     */     catch (IOException localIOException) {
/* 179 */       throw new JimiException(localIOException.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void close()
/*     */   {
/* 588 */     if (this.decoder != null)
/* 589 */       this.decoder.setFinished();
/*     */   }
/*     */ 
/*     */   protected Image getBuiltinImage()
/*     */   {
/* 748 */     JimiRasterImage localJimiRasterImage = null;
/*     */     try {
/* 750 */       localJimiRasterImage = getBuiltinJimiImage();
/*     */     }
/*     */     catch (JimiException localJimiException) {
/* 753 */       return JimiUtil.getErrorImage();
/*     */     }
/* 755 */     this.cacheJimiImage = localJimiRasterImage;
/* 756 */     this.cacheImageProducer = localJimiRasterImage.getImageProducer();
/* 757 */     Image localImage = Toolkit.getDefaultToolkit().createImage(localJimiRasterImage.getImageProducer());
/*     */ 
/* 759 */     return localImage;
/*     */   }
/*     */ 
/*     */   protected Image getBuiltinJPEG()
/*     */   {
/* 780 */     Image localImage = null;
/* 781 */     Toolkit localToolkit = Toolkit.getDefaultToolkit();
/*     */     Object localObject1;
/*     */     Object localObject2;
/* 782 */     if (this.location != null) {
/* 783 */       localImage = localToolkit.getImage(this.location);
/*     */     }
/* 785 */     else if (this.filename != null)
/* 786 */       localImage = localToolkit.getImage(this.filename);
/*     */     else {
/*     */       try
/*     */       {
/* 790 */         localObject1 = new ByteArrayOutputStream();
/* 791 */         localObject2 = new byte[10240];
/*     */ 
/* 793 */         int i = 0;
/* 794 */         while ((i = this.input.read((byte[])localObject2)) != -1) {
/* 795 */           ((ByteArrayOutputStream)localObject1).write((byte[])localObject2, 0, i);
/*     */         }
/* 797 */         this.input.close();
/*     */ 
/* 799 */         byte[] arrayOfByte = ((ByteArrayOutputStream)localObject1).toByteArray();
/*     */ 
/* 801 */         localImage = localToolkit.createImage(arrayOfByte);
/*     */       }
/*     */       catch (IOException localIOException) {
/* 804 */         localImage = JimiUtil.getErrorImage();
/*     */       }
/*     */     }
/*     */ 
/* 808 */     if (Jimi.crippled) {
/* 809 */       localObject1 = new StampImageFilter();
/* 810 */       localObject2 = new FilteredImageSource(localImage.getSource(), (ImageFilter)localObject1);
/* 811 */       localImage = Toolkit.getDefaultToolkit().createImage((ImageProducer)localObject2);
/*     */     }
/*     */ 
/* 814 */     return localImage;
/*     */   }
/*     */ 
/*     */   protected JimiRasterImage getBuiltinJimiImage()
/*     */     throws JimiException
/*     */   {
/* 769 */     Image localImage = getBuiltinJPEG();
/* 770 */     JimiRasterImage localJimiRasterImage = Jimi.createRasterImage(localImage.getSource(), this.imageFactory);
/* 771 */     return localJimiRasterImage;
/*     */   }
/*     */ 
/*     */   public Image getImage()
/*     */   {
/*     */     Image localImage;
/* 469 */     if (this.cacheIndex == 0) {
/* 470 */       if (this.cacheImage != null) {
/* 471 */         return this.cacheImage;
/*     */       }
/*     */ 
/* 474 */       localImage = Toolkit.getDefaultToolkit().createImage(this.cacheImageProducer);
/* 475 */       GraphicsUtils.waitForImage(localImage);
/* 476 */       this.cacheImage = localImage;
/* 477 */       return localImage;
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 482 */       localImage = getNextImage();
/* 483 */       if (this.decoder != null) {
/* 484 */         this.decoder.setFinished();
/*     */       }
/* 486 */       return localImage;
/*     */     } catch (Exception localException) {
/*     */     }
/* 489 */     return JimiUtil.getErrorImage();
/*     */   }
/*     */ 
/*     */   public Image getImage(int paramInt)
/*     */     throws JimiException
/*     */   {
/* 568 */     if ((paramInt == this.cacheIndex) && (this.cacheImage != null)) {
/* 569 */       return this.cacheImage;
/*     */     }
/*     */ 
/* 572 */     ImageProducer localImageProducer = getImageProducer(paramInt);
/* 573 */     Image localImage = Toolkit.getDefaultToolkit().createImage(localImageProducer);
/* 574 */     if (this.synchronous) {
/* 575 */       GraphicsUtils.waitForImage(localImage);
/*     */     }
/* 577 */     return localImage;
/*     */   }
/*     */ 
/*     */   public Enumeration getImageEnumeration()
/*     */   {
/* 509 */     return new ImageSeriesEnumerator(this, 0);
/*     */   }
/*     */ 
/*     */   public ImageProducer getImageProducer()
/*     */   {
/*     */     try
/*     */     {
/* 447 */       if (this.cacheIndex == 0) {
/* 448 */         return this.cacheImageProducer;
/*     */       }
/* 450 */       if (this.seriesIndex == 0) {
/* 451 */         ImageProducer localImageProducer = getNextImageProducer();
/* 452 */         if (this.decoder != null) {
/* 453 */           this.decoder.setFinished();
/*     */         }
/* 455 */         return localImageProducer;
/*     */       }
/*     */     }
/*     */     catch (JimiException localJimiException) {
/*     */     }
/* 460 */     return JimiUtil.getErrorImageProducer();
/*     */   }
/*     */ 
/*     */   public ImageProducer getImageProducer(int paramInt)
/*     */     throws JimiException
/*     */   {
/* 548 */     if (paramInt < this.seriesIndex) {
/* 549 */       throw new JimiException("Unable to access image number " + paramInt);
/*     */     }
/* 551 */     while (this.seriesIndex < paramInt) {
/* 552 */       skipNextImage();
/*     */     }
/* 554 */     return getNextImageProducer();
/*     */   }
/*     */ 
/*     */   public Enumeration getImageProducerEnumeration()
/*     */   {
/* 518 */     return new ImageSeriesEnumerator(this, 2);
/*     */   }
/*     */ 
/*     */   protected JimiDecodingController getNextController()
/*     */   {
/* 737 */     JimiDecodingController localJimiDecodingController = this.series.getNextController();
/*     */ 
/* 739 */     return localJimiDecodingController;
/*     */   }
/*     */ 
/*     */   protected Image getNextImage()
/*     */     throws JimiException
/*     */   {
/* 688 */     if (this.builtinJPEG) {
Image localObject;
/* 689 */       if (this.cacheIndex == 0) {
/* 690 */         if (this.cacheImage != null) {
/* 691 */           return this.cacheImage;
/*     */         }
/*     */ 
/* 694 */         localObject = Toolkit.getDefaultToolkit().createImage(this.cacheImageProducer);
/* 695 */         GraphicsUtils.waitForImage((Image)localObject);
/* 696 */         return localObject;
/*     */       }
/*     */ 
/* 699 */       if (this.seriesIndex == 0) {
/* 700 */         localObject = getBuiltinJPEG();
/* 701 */         this.cacheIndex = 0;
/* 702 */         this.cacheImage = ((Image)localObject);
/* 703 */         this.cacheImageProducer = ((Image)localObject).getSource();
/* 704 */         this.seriesIndex += 1;
/* 705 */         GraphicsUtils.waitForImage((Image)localObject);
/* 706 */         return localObject;
/*     */       }
/*     */ 
/* 709 */       throw new JimiException();
/*     */     }
/*     */ 
/* 712 */     Object localObject = getNextImageProducer();
/* 713 */     Image localImage = Toolkit.getDefaultToolkit().createImage((ImageProducer)localObject);
/* 714 */     this.cacheImage = localImage;
/* 715 */     GraphicsUtils.waitForImage(localImage);
/* 716 */     return localImage;
/*     */   }
/*     */ 
/*     */   protected ImageProducer getNextImageProducer()
/*     */     throws JimiException
/*     */   {
/* 655 */     if (this.builtinJPEG) {
/* 656 */       if (this.cacheIndex == 0) {
/* 657 */         return this.cacheImageProducer;
/*     */       }
/* 659 */       if (this.seriesIndex == 0) {
/* 660 */         Image localObject = getBuiltinJPEG();
/* 661 */         this.cacheIndex = 0;
/* 662 */         this.cacheImage = ((Image)localObject);
/* 663 */         this.cacheImageProducer = ((Image)localObject).getSource();
/* 664 */         this.seriesIndex += 1;
/* 665 */         return ((Image)localObject).getSource();
/*     */       }
/*     */ 
/* 668 */       throw new JimiException();
/*     */     }
/*     */ 
/* 672 */     Object localObject = getNextJimiImage(false);
/* 673 */     this.cacheJimiImage = ((JimiRasterImage)localObject);
/* 674 */     this.cacheImageProducer = ((JimiImage)localObject).getImageProducer();
/* 675 */     this.cacheIndex = this.seriesIndex;
/* 676 */     return ((JimiImage)localObject).getImageProducer();
/*     */   }
/*     */ 
/*     */   protected JimiRasterImage getNextJimiImage()
/*     */     throws JimiException
/*     */   {
/* 596 */     return getNextJimiImage(true);
/*     */   }
/*     */ 
/*     */   protected JimiRasterImage getNextJimiImage(boolean paramBoolean)
/*     */     throws JimiException
/*     */   {
/* 609 */     if (this.builtinJPEG) {
/* 610 */       if (this.cacheIndex == 0) {
/* 611 */         return this.cacheJimiImage;
/*     */       }
/* 613 */       if (this.seriesIndex == 0) {
/* 614 */         Image localObject = getBuiltinJPEG();
/* 615 */         this.cacheImage = ((Image)localObject);
/* 616 */         this.cacheImageProducer = null;
/* 617 */         this.cacheJimiImage = null;
/* 618 */         this.cacheIndex = 0;
/* 619 */         this.seriesIndex += 1;
/* 620 */         return Jimi.createRasterImage(((Image)localObject).getSource());
/*     */       }
/*     */ 
/* 623 */       throw new JimiException();
/*     */     }
/*     */ 
/* 627 */     Object localObject = this.series.getNextController();
/* 628 */     JimiImage localJimiImage = ((JimiDecodingController)localObject).getJimiImage();
/* 629 */     if ((paramBoolean) && (this.synchronous)) {
/* 630 */       ((JimiDecodingController)localObject).requestDecoding();
/* 631 */       localJimiImage.waitFinished();
/*     */     }
/* 633 */     JimiRasterImage localJimiRasterImage = JimiUtil.asJimiRasterImage(localJimiImage);
/* 634 */     if (localJimiRasterImage == null) {
/* 635 */       throw new JimiException();
/*     */     }
/* 637 */     this.cacheJimiImage = localJimiRasterImage;
/* 638 */     this.cacheImageProducer = localJimiRasterImage.getImageProducer();
/* 639 */     this.cacheImage = null;
/* 640 */     this.cacheIndex = this.seriesIndex;
/* 641 */     this.seriesIndex += 1;
/*     */ 
/* 643 */     return localJimiRasterImage;
/*     */   }
/*     */ 
/*     */   public int getNumberOfImages()
/*     */   {
/* 241 */     return -1;
/*     */   }
/*     */ 
/*     */   public JimiRasterImage getRasterImage()
/*     */     throws JimiException
/*     */   {
/* 419 */     if (this.cacheIndex == 0) {
/* 420 */       if (this.cacheJimiImage != null) {
/* 421 */         return this.cacheJimiImage;
/*     */       }
/*     */ 
/* 424 */       return Jimi.createRasterImage(this.cacheImageProducer);
/*     */     }
/*     */ 
/* 427 */     if (this.seriesIndex == 0) {
/* 428 */       JimiRasterImage localJimiRasterImage = getNextJimiImage();
/* 429 */       if (this.decoder != null) {
/* 430 */         this.decoder.setFinished();
/*     */       }
/* 432 */       return localJimiRasterImage;
/*     */     }
/*     */ 
/* 435 */     throw new JimiException();
/*     */   }
/*     */ 
/*     */   public Enumeration getRasterImageEnumeration()
/*     */   {
/* 500 */     return new ImageSeriesEnumerator(this, 1);
/*     */   }
/*     */ 
/*     */   public Dimension getSize()
/*     */     throws JimiException
/*     */   {
/* 406 */     JimiRasterImage localJimiRasterImage = getRasterImage();
/* 407 */     localJimiRasterImage.waitInfoAvailable();
/* 408 */     return new Dimension(localJimiRasterImage.getWidth(), localJimiRasterImage.getHeight());
/*     */   }
/*     */ 
/*     */   protected boolean hasMoreElements()
/*     */   {
/* 724 */     if (this.builtinJPEG) {
/* 725 */       return this.seriesIndex == 0;
/*     */     }
/*     */ 
/* 728 */     return this.series.hasMoreImages();
/*     */   }
/*     */ 
/*     */   protected void initReader(JimiImageFactory paramJimiImageFactory)
/*     */   {
/* 329 */     this.imageFactory = paramJimiImageFactory;
/*     */   }
/*     */ 
/*     */   protected void initReader(JimiImageFactory paramJimiImageFactory, JimiDecoderFactory paramJimiDecoderFactory)
/*     */   {
/* 306 */     if ((paramJimiDecoderFactory instanceof FreeFormat)) {
/* 307 */       paramJimiImageFactory = JimiUtil.stripStamping(paramJimiImageFactory);
/*     */     }
/* 309 */     initReader(paramJimiImageFactory);
/* 310 */     this.decoderFactory = paramJimiDecoderFactory;
/* 311 */     this.decoder = paramJimiDecoderFactory.createDecoder();
/* 312 */     if (this.listener != null) {
/* 313 */       this.decoder.setProgressListener(this.listener);
/*     */     }
/*     */ 
/* 316 */     if (paramJimiDecoderFactory.getClass().getName()
/* 316 */       .equals("com.sun.jimi.core.decoder.builtin.BuiltinDecoderFactory")) {
/* 317 */       this.builtinJPEG = true;
/*     */     }
/* 319 */     else if (this.cleanupCommand != null)
/* 320 */       this.decoder.addCleanupCommand(this.cleanupCommand);
/*     */   }
/*     */ 
/*     */   protected void initReader(JimiImageFactory paramJimiImageFactory, JimiDecoderFactory paramJimiDecoderFactory, InputStream paramInputStream)
/*     */   {
/* 279 */     if ((paramJimiDecoderFactory instanceof FreeFormat)) {
/* 280 */       paramJimiImageFactory = JimiUtil.stripStamping(paramJimiImageFactory);
/*     */     }
/* 282 */     this.imageFactory = paramJimiImageFactory;
/* 283 */     this.decoderFactory = paramJimiDecoderFactory;
/* 284 */     this.decoder = paramJimiDecoderFactory.createDecoder();
/* 285 */     if ((this.listener != null) && (this.decoder != null)) {
/* 286 */       this.decoder.setProgressListener(this.listener);
/*     */     }
/* 288 */     this.input = paramInputStream;
/*     */ 
/* 290 */     if (paramJimiDecoderFactory.getClass().getName()
/* 290 */       .equals("com.sun.jimi.core.decoder.builtin.BuiltinDecoderFactory")) {
/* 291 */       this.builtinJPEG = true;
/*     */     }
/*     */     else {
/* 294 */       if (this.cleanupCommand != null) {
/* 295 */         this.decoder.addCleanupCommand(this.cleanupCommand);
/*     */       }
/* 297 */       this.series = this.decoder.initDecoding(paramJimiImageFactory, paramInputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setBlocking(boolean paramBoolean)
/*     */   {
/* 397 */     this.synchronous = paramBoolean;
/*     */   }
/*     */ 
/*     */   public void setMimeType(String paramString)
/*     */     throws JimiException
/*     */   {
/* 261 */     JimiDecoderFactory localJimiDecoderFactory = JimiControl.getDecoderByType(paramString);
/* 262 */     if (localJimiDecoderFactory == null) {
/* 263 */       throw new JimiException("Cannot find decoder for type: " + paramString);
/*     */     }
/* 265 */     if (this.input != null) {
/* 266 */       initReader(this.imageFactory, localJimiDecoderFactory, this.input);
/*     */     }
/*     */     else
/* 269 */       initReader(this.imageFactory, localJimiDecoderFactory);
/*     */   }
/*     */ 
/*     */   public void setProgressListener(ProgressListener paramProgressListener)
/*     */   {
/* 249 */     this.listener = paramProgressListener;
/* 250 */     if (this.decoder != null)
/* 251 */       this.decoder.setProgressListener(paramProgressListener);
/*     */   }
/*     */ 
/*     */   public void setSource(InputStream paramInputStream)
/*     */     throws JimiException
/*     */   {
/* 347 */     initReader(this.imageFactory, this.decoderFactory, paramInputStream);
/*     */   }
/*     */ 
/*     */   public void setSource(String paramString)
/*     */     throws JimiException
/*     */   {
/*     */     Object localObject;
/*     */     try
/*     */     {
/* 360 */       localObject = new FileInputStream(paramString);
/* 361 */       this.filename = paramString;
/* 362 */       localObject = new BufferedInputStream((InputStream)localObject, 10240);
/*     */     }
/*     */     catch (Exception localException) {
/* 365 */       throw new JimiException("Unable to open source file.");
/*     */     }
/* 367 */     initReader(this.imageFactory, this.decoderFactory, (InputStream)localObject);
/*     */   }
/*     */ 
/*     */   public void setSource(URL paramURL)
/*     */     throws JimiException
/*     */   {
/*     */     try
/*     */     {
/* 380 */       Object localObject = paramURL.openStream();
/* 381 */       this.location = paramURL;
/* 382 */       localObject = new BufferedInputStream((InputStream)localObject, 10240);
/*     */     }
/*     */     catch (IOException localIOException) {
/* 385 */       throw new JimiException("Unable to open source URL.");
/*     */     }
/*     */   }
/*     */ 
/*     */   public void skipNextImage()
/*     */     throws JimiException
/*     */   {
/* 524 */     if (!this.series.hasMoreImages()) {
/* 525 */       this.series.skipNextImage();
/* 526 */       this.seriesIndex += 1;
/*     */     }
/*     */     else {
/* 529 */       throw new JimiException("Attemping to move beyond last image.");
/*     */     }
/*     */   }
/*     */ 
/*     */   class StreamCloseCommand
/*     */     implements Runnable
/*     */   {
/*     */     StreamCloseCommand()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void run()
/*     */     {
/*     */       try
/*     */       {
/* 827 */         JimiReader.this.input.close();
/*     */       }
/*     */       catch (IOException localIOException)
/*     */       {
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.JimiReader
 * JD-Core Version:    0.6.2
 */