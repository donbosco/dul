/*     */ package com.sun.jimi.core.compat;
/*     */ 
/*     */ import com.sun.jimi.core.JimiException;
/*     */ import com.sun.jimi.core.JimiImage;
/*     */ import com.sun.jimi.core.JimiImageFactory;
/*     */ import com.sun.jimi.core.MutableJimiImage;
/*     */ import com.sun.jimi.core.options.FormatOptionSet;
/*     */ import com.sun.jimi.core.raster.BitRasterImage;
/*     */ import com.sun.jimi.core.raster.ByteRasterImage;
/*     */ import com.sun.jimi.core.raster.ChanneledIntRasterImage;
/*     */ import com.sun.jimi.core.raster.IntRasterImage;
/*     */ import com.sun.jimi.core.raster.JimiRasterImage;
/*     */ import com.sun.jimi.core.raster.MutableJimiRasterImage;
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.DirectColorModel;
/*     */ import java.awt.image.IndexColorModel;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ 
/*     */ /** @deprecated */
/*     */ public class AdaptiveRasterImage
/*     */ {
/*     */   public static final int CHANNEL_ALPHA = 24;
/*     */   public static final int CHANNEL_RED = 16;
/*     */   public static final int CHANNEL_GREEN = 8;
/*     */   public static final int CHANNEL_BLUE = 0;
/*     */   public static final int NO_ALPHA = 0;
/*     */   public static final int ALPHA_DATA = 1;
/*     */   public static final int ALPHA = 0;
/*     */   public static final int RED = 1;
/*     */   public static final int GREEN = 2;
/*     */   public static final int BLUE = 3;
/*     */   protected static final int MODE_INT = 0;
/*     */   protected static final int MODE_BYTE = 1;
/*     */   protected static final int MODE_BIT = 2;
/*     */   protected static final int MODE_CHANNELED = 3;
/*     */   protected int mode;
/*     */   protected ColorModel cm;
/*  59 */   protected int hints = -1;
/*     */   protected int width;
/*     */   protected int height;
/*     */   protected JimiImageFactory factory;
/*     */   protected IntRasterImage intImage;
/*     */   protected ChanneledIntRasterImage channelImage;
/*     */   protected ByteRasterImage byteImage;
/*     */   protected BitRasterImage bitImage;
/*     */   protected JimiRasterImage rasterImage;
/*     */   protected MutableJimiRasterImage mutableImage;
/*     */   protected JimiDecoderBase decoder;
/*  81 */   protected boolean rgbDefault = false;
/*     */ 
/*  83 */   protected Hashtable properties = new Hashtable();
/*     */   protected Boolean alphaSignificant;
/*     */   protected boolean waitForOptions;
/*     */   protected FormatOptionSet options;
/*     */ 
/*     */   public AdaptiveRasterImage(JimiImageFactory paramJimiImageFactory)
/*     */   {
/*  97 */     this.factory = paramJimiImageFactory;
/*     */   }
/*     */ 
/*     */   public AdaptiveRasterImage(JimiImageFactory paramJimiImageFactory, JimiDecoderBase paramJimiDecoderBase)
/*     */   {
/* 107 */     this(paramJimiImageFactory);
/* 108 */     setDecoder(paramJimiDecoderBase);
/* 109 */     setWaitForOptions(paramJimiDecoderBase.mustWaitForOptions());
/*     */   }
/*     */ 
/*     */   public AdaptiveRasterImage(JimiRasterImage paramJimiRasterImage)
/*     */   {
/* 118 */     this.rasterImage = paramJimiRasterImage;
/* 119 */     this.width = paramJimiRasterImage.getWidth();
/* 120 */     this.height = paramJimiRasterImage.getHeight();
/* 121 */     if ((paramJimiRasterImage instanceof ChanneledIntRasterImage)) {
/* 122 */       this.mode = 3;
/* 123 */       this.intImage = (this.channelImage = (ChanneledIntRasterImage)paramJimiRasterImage);
/*     */     }
/* 125 */     else if ((paramJimiRasterImage instanceof IntRasterImage)) {
/* 126 */       this.intImage = ((IntRasterImage)paramJimiRasterImage);
/* 127 */       this.mode = 0;
/*     */     }
/* 129 */     else if ((paramJimiRasterImage instanceof ByteRasterImage)) {
/* 130 */       this.byteImage = ((ByteRasterImage)paramJimiRasterImage);
/* 131 */       this.mode = 1;
/*     */     }
/* 133 */     else if ((paramJimiRasterImage instanceof BitRasterImage)) {
/* 134 */       this.bitImage = ((BitRasterImage)paramJimiRasterImage);
/* 135 */       this.mode = 2;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addFullCoverage()
/*     */   {
/* 288 */     if (this.mutableImage != null)
/* 289 */       this.mutableImage.setFinished();
/*     */   }
/*     */ 
/*     */   protected void createBackEnd()
/*     */     throws JimiException
/*     */   {
/* 232 */     int i = this.cm.getPixelSize();
/*     */ 
/* 234 */     if (i == 1) {
/* 235 */       this.rasterImage = (this.mutableImage = this.bitImage = this.factory.createBitRasterImage(this.width, this.height, this.cm));
/* 236 */       this.mode = 2;
/*     */     }
/* 239 */     else if (i <= 8) {
/* 240 */       this.rasterImage = (this.mutableImage = this.byteImage = this.factory.createByteRasterImage(this.width, this.height, this.cm));
/* 241 */       this.mode = 1;
/*     */     }
/* 245 */     else if ((this.decoder != null) && (this.decoder.usesChanneledData())) {
/* 246 */       this.rasterImage = (this.mutableImage = this.intImage = this.channelImage = this.factory.createChanneledIntRasterImage(this.width, this.height, this.cm));
/* 247 */       this.mode = 3;
/*     */     }
/*     */     else {
/* 250 */       this.rasterImage = (this.mutableImage = this.intImage = this.factory.createIntRasterImage(this.width, this.height, this.cm));
/* 251 */       this.mode = 0;
/*     */     }
/*     */ 
/* 254 */     if (this.hints != -1) {
/* 255 */       this.mutableImage.setImageConsumerHints(this.hints);
/*     */     }
/* 257 */     Enumeration localEnumeration = this.properties.keys();
/* 258 */     while (localEnumeration.hasMoreElements()) {
/* 259 */       Hashtable localHashtable = this.mutableImage.getProperties();
/* 260 */       Object localObject = localEnumeration.nextElement();
/* 261 */       localHashtable.put(localObject, this.properties.get(localObject));
/*     */     }
/* 263 */     if (this.options != null) {
/* 264 */       this.mutableImage.setOptions(this.options);
/*     */     }
/* 266 */     if (this.decoder != null)
/* 267 */       this.decoder.jimiImageCreated(this.mutableImage);
/*     */   }
/*     */ 
/*     */   public int getAlphaStatus()
/*     */   {
/* 537 */     if (this.alphaSignificant != null) {
/* 538 */       return this.alphaSignificant.booleanValue() ? 1 : 0;
/*     */     }
/*     */ 
/* 541 */     if (((this.cm instanceof DirectColorModel)) && 
/* 542 */       (((DirectColorModel)this.cm).getAlphaMask() != 0)) {
/* 543 */       this.alphaSignificant = new Boolean(true);
/* 544 */       return getAlphaStatus();
/*     */     }
/* 546 */     if ((this.cm instanceof IndexColorModel)) {
/* 547 */       IndexColorModel localIndexColorModel = (IndexColorModel)this.cm;
/* 548 */       byte[] arrayOfByte = new byte[localIndexColorModel.getMapSize()];
/* 549 */       localIndexColorModel.getAlphas(arrayOfByte);
/* 550 */       for (int i = 0; i < arrayOfByte.length; i++) {
/* 551 */         if (arrayOfByte[i] != 255) {
/* 552 */           this.alphaSignificant = new Boolean(true);
/* 553 */           return getAlphaStatus();
/*     */         }
/*     */       }
/*     */     }
/* 557 */     this.alphaSignificant = new Boolean(false);
/* 558 */     return getAlphaStatus();
/*     */   }
/*     */ 
/*     */   public JimiRasterImage getBackend()
/*     */   {
/* 144 */     return this.rasterImage;
/*     */   }
/*     */ 
/*     */   public long getChannel(int paramInt1, int paramInt2)
/*     */     throws JimiException
/*     */   {
/* 500 */     return this.rasterImage.getPixelRGB(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public void getChannel(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, byte[] paramArrayOfByte, int paramInt6, int paramInt7)
/*     */     throws JimiException
/*     */   {
/* 409 */     switch (this.mode)
/*     */     {
/*     */     case 0:
/* 412 */       this.intImage.getChannelRectangle(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramArrayOfByte, paramInt6, paramInt7);
/* 413 */       break;
/*     */     case 3:
/* 415 */       this.channelImage.getChannelRectangle(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramArrayOfByte, paramInt6, paramInt7);
/* 416 */       break;
/*     */     case 1:
/* 418 */       this.byteImage.getRectangle(paramInt2, paramInt3, paramInt4, paramInt5, paramArrayOfByte, paramInt6, paramInt7);
/* 419 */       break;
/*     */     case 2:
/* 421 */       this.bitImage.getRectangle(paramInt2, paramInt3, paramInt4, paramInt5, paramArrayOfByte, paramInt6, paramInt7);
/* 422 */       break;
/*     */     default:
/* 424 */       throw new JimiException("");
/*     */     }
/*     */   }
/*     */ 
/*     */   public void getChannel(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt, int paramInt5, int paramInt6)
/*     */     throws JimiException
/*     */   {
/* 434 */     if (this.rgbDefault) {
/* 435 */       this.rasterImage.getRectangleRGB(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfInt, paramInt5, paramInt6);
/*     */     }
/*     */     else
/* 438 */       switch (this.mode)
/*     */       {
/*     */       case 0:
/*     */       case 3:
/* 442 */         this.intImage.getRectangle(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfInt, paramInt5, paramInt6);
/* 443 */         break;
/*     */       case 1:
/*     */       case 2:
/*     */       default:
/* 445 */         byte[] arrayOfByte = new byte[getWidth()];
/* 446 */         for (int i = 0; i < paramInt4; i++) {
/* 447 */           if (this.mode == 1) {
/* 448 */             this.byteImage.getRow(paramInt2, arrayOfByte, paramInt5);
/*     */           }
/* 450 */           else if (this.mode == 2) {
/* 451 */             this.bitImage.getRow(paramInt2, arrayOfByte, paramInt5);
/*     */           }
/*     */           else {
/* 454 */             throw new JimiException("int[] getChannel on non-int image");
/*     */           }
/* 456 */           for (int j = 0; j < paramInt3; j++)
/* 457 */             paramArrayOfInt[(paramInt5 + i * paramInt6 + j)] = (arrayOfByte[j] & 0xFF);
/*     */         }
/*     */       }
/*     */   }
/*     */ 
/*     */   public void getChannel(int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3)
/*     */     throws JimiException
/*     */   {
/* 468 */     getChannel(paramInt1, 0, paramInt2, getWidth(), 1, paramArrayOfByte, paramInt3, 0);
/*     */   }
/*     */ 
/*     */   public void getChannel(int paramInt1, int[] paramArrayOfInt, int paramInt2)
/*     */     throws JimiException
/*     */   {
/* 492 */     getChannel(0, paramInt1, this.width, 1, paramArrayOfInt, paramInt2, 0);
/*     */   }
/*     */ 
/*     */   public void getChannelRGB(int paramInt1, byte[] paramArrayOfByte, int paramInt2)
/*     */     throws JimiException
/*     */   {
/* 476 */     this.rasterImage.getRectangleRGBChannels(0, paramInt1, getWidth(), 1, paramArrayOfByte, paramInt2, getWidth());
/*     */   }
/*     */ 
/*     */   public void getChannelRGBA(int paramInt1, byte[] paramArrayOfByte, int paramInt2)
/*     */     throws JimiException
/*     */   {
/* 484 */     this.rasterImage.getRectangleRGBAChannels(0, paramInt1, getWidth(), 1, paramArrayOfByte, paramInt2, getWidth());
/*     */   }
/*     */ 
/*     */   public ColorModel getColorModel()
/*     */   {
/* 178 */     return this.rasterImage == null ? this.cm : this.rasterImage.getColorModel();
/*     */   }
/*     */ 
/*     */   public int getHeight()
/*     */   {
/* 173 */     return this.rasterImage == null ? this.height : this.rasterImage.getHeight();
/*     */   }
/*     */ 
/*     */   public FormatOptionSet getOptions()
/*     */   {
/* 200 */     return this.rasterImage.getOptions();
/*     */   }
/*     */ 
/*     */   public int getWidth()
/*     */   {
/* 168 */     return this.rasterImage == null ? this.width : this.rasterImage.getWidth();
/*     */   }
/*     */ 
/*     */   public void setAbort()
/*     */   {
/* 273 */     setError();
/*     */   }
/*     */ 
/*     */   public synchronized void setChannel(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, byte[] paramArrayOfByte, int paramInt6, int paramInt7)
/*     */     throws JimiException
/*     */   {
/* 306 */     switch (this.mode)
/*     */     {
/*     */     case 3:
/* 309 */       this.channelImage.setChannelRectangle(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramArrayOfByte, paramInt6, paramInt7);
/* 310 */       break;
/*     */     case 0:
/* 312 */       throw new JimiException();
/*     */     case 1:
/* 315 */       this.byteImage.setRectangle(paramInt2, paramInt3, paramInt4, paramInt5, paramArrayOfByte, paramInt6, paramInt7);
/* 316 */       break;
/*     */     case 2:
/* 319 */       this.bitImage.setRectangle(paramInt2, paramInt3, paramInt4, paramInt5, paramArrayOfByte, paramInt6, paramInt7);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setChannel(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt, int paramInt5, int paramInt6)
/*     */     throws JimiException
/*     */   {
/* 330 */     switch (this.mode)
/*     */     {
/*     */     case 0:
/*     */     case 3:
/* 334 */       this.intImage.setRectangle(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfInt, paramInt5, paramInt6);
/* 335 */       break;
/*     */     case 1:
/*     */     case 2:
/*     */     default:
/* 337 */       throw new JimiException();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setChannel(int paramInt1, int paramInt2, long paramLong)
/*     */     throws JimiException
/*     */   {
/* 508 */     switch (this.mode)
/*     */     {
/*     */     case 0:
/* 511 */       this.intImage.setPixel(paramInt1, paramInt2, (int)paramLong);
/* 512 */       break;
/*     */     case 1:
/* 514 */       this.byteImage.setPixel(paramInt1, paramInt2, (byte)(int)paramLong);
/* 515 */       break;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setChannel(int paramInt1, int paramInt2, byte[] paramArrayOfByte)
/*     */     throws JimiException
/*     */   {
/* 381 */     setChannel(paramInt1, paramInt2, paramArrayOfByte, 0, this.width);
/*     */   }
/*     */ 
/*     */   public void setChannel(int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3, int paramInt4)
/*     */     throws JimiException
/*     */   {
/* 372 */     setChannel(paramInt1, 0, paramInt2, this.width, 1, paramArrayOfByte, paramInt3, paramInt4);
/*     */   }
/*     */ 
/*     */   public void setChannel(int paramInt, int[] paramArrayOfInt)
/*     */     throws JimiException
/*     */   {
/* 390 */     setChannel(0, paramInt, this.width, 1, paramArrayOfInt, 0, this.width);
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public void setChannel(long paramLong)
/*     */     throws JimiException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void setColorModel(ColorModel paramColorModel)
/*     */   {
/* 187 */     this.cm = paramColorModel;
/*     */   }
/*     */ 
/*     */   public void setDecoder(JimiDecoderBase paramJimiDecoderBase)
/*     */   {
/* 152 */     this.decoder = paramJimiDecoderBase;
/*     */   }
/*     */ 
/*     */   public void setError()
/*     */   {
/* 278 */     this.mutableImage.setError();
/*     */   }
/*     */ 
/*     */   public void setHints(int paramInt)
/*     */   {
/* 210 */     this.hints = paramInt;
/* 211 */     if (this.mutableImage != null)
/* 212 */       this.mutableImage.setImageConsumerHints(paramInt);
/*     */   }
/*     */ 
/*     */   public void setOptions(FormatOptionSet paramFormatOptionSet)
/*     */   {
/* 192 */     this.options = paramFormatOptionSet;
/* 193 */     if (this.mutableImage != null)
/* 194 */       this.mutableImage.setOptions(paramFormatOptionSet);
/*     */   }
/*     */ 
/*     */   public void setPackedChannel(int paramInt, byte[] paramArrayOfByte)
/*     */     throws JimiException
/*     */   {
/* 363 */     setPackedChannel(paramInt, paramArrayOfByte, 0, this.width);
/*     */   }
/*     */ 
/*     */   public void setPackedChannel(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3)
/*     */     throws JimiException
/*     */   {
/* 347 */     switch (this.mode)
/*     */     {
/*     */     case 2:
/* 350 */       this.bitImage.setRectanglePacked(0, paramInt1, this.width, 1, paramArrayOfByte, paramInt2, paramInt3);
/* 351 */       break;
/*     */     default:
/* 353 */       throw new JimiException();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setPixels()
/*     */     throws JimiException
/*     */   {
/* 222 */     createBackEnd();
/*     */   }
/*     */ 
/*     */   public void setProperty(String paramString, Object paramObject)
/*     */   {
/* 524 */     if (this.mutableImage != null) {
/* 525 */       this.mutableImage.getProperties().put(paramString, paramObject);
/*     */     }
/*     */     else
/* 528 */       this.properties.put(paramString, paramObject);
/*     */   }
/*     */ 
/*     */   public void setRGBDefault(boolean paramBoolean)
/*     */   {
/* 283 */     this.rgbDefault = paramBoolean;
/*     */   }
/*     */ 
/*     */   public void setSize(int paramInt1, int paramInt2)
/*     */   {
/* 162 */     this.width = paramInt1;
/* 163 */     this.height = paramInt2;
/*     */   }
/*     */ 
/*     */   public void setWaitForOptions(boolean paramBoolean)
/*     */   {
/* 567 */     if (this.mutableImage != null)
/*     */     {
/* 569 */       this.mutableImage.setWaitForOptions(paramBoolean);
/*     */     }
/* 571 */     this.waitForOptions = paramBoolean;
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.compat.AdaptiveRasterImage
 * JD-Core Version:    0.6.2
 */