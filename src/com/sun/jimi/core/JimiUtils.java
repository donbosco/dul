/*     */ package com.sun.jimi.core;
/*     */ 
/*     */ import com.sun.jimi.core.filters.AspectAdjustReplicateScaleFilter;
/*     */ import com.sun.jimi.core.filters.AspectReplicateScaleFilter;
/*     */ import com.sun.jimi.core.raster.JimiRasterImage;
/*     */ import com.sun.jimi.core.util.JimiUtil;
/*     */ import java.awt.Image;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.image.FilteredImageSource;
/*     */ import java.awt.image.ImageProducer;
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
/*     */ import java.util.Hashtable;
/*     */ 
/*     */ public class JimiUtils
/*     */ {
/*     */   public static ImageProducer aspectAdjust(JimiRasterImage paramJimiRasterImage)
/*     */   {
/*  45 */     if (paramJimiRasterImage == null) {
/*  46 */       return JimiUtil.getErrorImageProducer();
/*     */     }
/*     */ 
/*  49 */     paramJimiRasterImage.waitInfoAvailable();
/*     */ 
/*  51 */     if (paramJimiRasterImage.getProperties() == null) {
/*  52 */       return paramJimiRasterImage.getImageProducer();
/*     */     }
/*     */ 
/*  55 */     if (paramJimiRasterImage.getProperties().get("fixedaspect") != null) {
/*  56 */       return paramJimiRasterImage.getImageProducer();
/*     */     }
/*  58 */     Object localObject1 = paramJimiRasterImage.getProperties().get("xres");
/*  59 */     Object localObject2 = paramJimiRasterImage.getProperties().get("yres");
/*     */ 
/*  61 */     if ((localObject1 != null) && (localObject2 != null) && 
/*  62 */       ((localObject1 instanceof Number)) && ((localObject2 instanceof Number))) {
/*  63 */       double d1 = ((Number)localObject1).doubleValue();
/*  64 */       double d2 = ((Number)localObject2).doubleValue();
/*  65 */       if (d1 == d2) {
/*  66 */         return paramJimiRasterImage.getImageProducer();
/*     */       }
/*  68 */       AspectAdjustReplicateScaleFilter localAspectAdjustReplicateScaleFilter = new AspectAdjustReplicateScaleFilter(d1, d2);
/*  69 */       FilteredImageSource localFilteredImageSource = new FilteredImageSource(paramJimiRasterImage.getImageProducer(), localAspectAdjustReplicateScaleFilter);
/*  70 */       return localFilteredImageSource;
/*     */     }
/*     */ 
/*  73 */     return paramJimiRasterImage.getImageProducer();
/*     */   }
/*     */ 
/*     */   public static Image getThumbnail(InputStream paramInputStream, int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 149 */     return Toolkit.getDefaultToolkit().createImage(getThumbnailProducer(paramInputStream, paramInt1, paramInt2, paramInt3));
/*     */   }
/*     */ 
/*     */   public static Image getThumbnail(InputStream paramInputStream, int paramInt1, int paramInt2, int paramInt3, String paramString)
/*     */   {
/* 181 */     return Toolkit.getDefaultToolkit().createImage(getThumbnailProducer(paramInputStream, paramInt1, paramInt2, paramInt3, paramString));
/*     */   }
/*     */ 
/*     */   public static Image getThumbnail(String paramString, int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/*  87 */     return Toolkit.getDefaultToolkit().createImage(getThumbnailProducer(paramString, paramInt1, paramInt2, paramInt3));
/*     */   }
/*     */ 
/*     */   public static Image getThumbnail(URL paramURL, int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 118 */     return Toolkit.getDefaultToolkit().createImage(getThumbnailProducer(paramURL, paramInt1, paramInt2, paramInt3));
/*     */   }
/*     */ 
/*     */   public static ImageProducer getThumbnailProducer(InputStream paramInputStream, int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 162 */     ImageProducer localImageProducer = Jimi.getImageProducer(paramInputStream, paramInt3);
/* 163 */     AspectReplicateScaleFilter localAspectReplicateScaleFilter = new AspectReplicateScaleFilter(paramInt1, paramInt2);
/* 164 */     FilteredImageSource localFilteredImageSource = new FilteredImageSource(localImageProducer, localAspectReplicateScaleFilter);
/*     */ 
/* 166 */     return localFilteredImageSource;
/*     */   }
/*     */ 
/*     */   public static ImageProducer getThumbnailProducer(InputStream paramInputStream, int paramInt1, int paramInt2, int paramInt3, String paramString)
/*     */   {
/* 195 */     ImageProducer localImageProducer = Jimi.getImageProducer(paramInputStream, paramString, paramInt3);
/* 196 */     AspectReplicateScaleFilter localAspectReplicateScaleFilter = new AspectReplicateScaleFilter(paramInt1, paramInt2);
/* 197 */     FilteredImageSource localFilteredImageSource = new FilteredImageSource(localImageProducer, localAspectReplicateScaleFilter);
/*     */ 
/* 199 */     return localFilteredImageSource;
/*     */   }
/*     */ 
/*     */   public static ImageProducer getThumbnailProducer(String paramString, int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 100 */     ImageProducer localImageProducer = Jimi.getImageProducer(paramString, paramInt3);
/* 101 */     AspectReplicateScaleFilter localAspectReplicateScaleFilter = new AspectReplicateScaleFilter(paramInt1, paramInt2);
/* 102 */     FilteredImageSource localFilteredImageSource = new FilteredImageSource(localImageProducer, localAspectReplicateScaleFilter);
/*     */ 
/* 104 */     return localFilteredImageSource;
/*     */   }
/*     */ 
/*     */   public static ImageProducer getThumbnailProducer(URL paramURL, int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 131 */     ImageProducer localImageProducer = Jimi.getImageProducer(paramURL, paramInt3);
/* 132 */     AspectReplicateScaleFilter localAspectReplicateScaleFilter = new AspectReplicateScaleFilter(paramInt1, paramInt2);
/* 133 */     FilteredImageSource localFilteredImageSource = new FilteredImageSource(localImageProducer, localAspectReplicateScaleFilter);
/*     */ 
/* 135 */     return localFilteredImageSource;
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.JimiUtils
 * JD-Core Version:    0.6.2
 */