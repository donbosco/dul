/*     */ package com.sun.jimi.core.encoder.sunraster;
/*     */ 
/*     */ import com.sun.jimi.core.InvalidOptionException;
/*     */ import com.sun.jimi.core.JimiException;
/*     */ import com.sun.jimi.core.OptionsObject;
/*     */ import com.sun.jimi.core.compat.AdaptiveRasterImage;
/*     */ import com.sun.jimi.core.compat.JimiEncoderBase;
/*     */ import com.sun.jimi.core.options.SunRasterOptions;
/*     */ import com.sun.jimi.util.ArrayEnumeration;
/*     */ import com.sun.jimi.util.PropertyOwner;
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.IndexColorModel;
/*     */ import java.io.OutputStream;
/*     */ import java.util.Enumeration;
/*     */ 
/*     */ public class SunRasterEncoder extends JimiEncoderBase
/*     */   implements OptionsObject
/*     */ {
/*     */   protected int state_;
/*     */   protected OutputStream output_;
/*     */   static final boolean DEFAULT_COMPRESSION = true;
/*  36 */   protected boolean useRLE_ = true;
/*     */   static final String OPTION_COMPRESSION = "compression";
/* 132 */   static final Boolean[] POSSIBLE_COMPRESSION_VALUES = PropertyOwner.BOOLEAN_ARRAY;
/* 133 */   static final String[] OPTION_NAMES = { "compression" };
/*     */ 
/*     */   public void clearProperties()
/*     */   {
/* 162 */     setUseRLE(true);
/*     */   }
/*     */ 
/*     */   protected SpecificEncoder createEncoder()
/*     */     throws JimiException
/*     */   {
/*  91 */     AdaptiveRasterImage localAdaptiveRasterImage = getJimiImage();
/*     */ 
/*  94 */     SunRasterHeader localSunRasterHeader = new SunRasterHeader(localAdaptiveRasterImage);
/*  95 */     ColorModel localColorModel = localAdaptiveRasterImage.getColorModel();
/*     */     Object localObject;
/*  98 */     if (((localColorModel instanceof IndexColorModel)) && (localColorModel.getPixelSize() == 8))
/*     */     {
/* 100 */       localObject = new PaletteSunRasterEncoder();
/*     */ 
/* 102 */       ((PaletteSunRasterEncoder)localObject).setUseRLE(this.useRLE_);
/*     */     }
/*     */     else
/*     */     {
/* 106 */       localObject = new RGBSunRasterEncoder();
/*     */     }
/*     */ 
/* 109 */     ((SpecificEncoder)localObject).initEncoder(localSunRasterHeader, this.output_, localAdaptiveRasterImage);
/*     */ 
/* 111 */     return (SpecificEncoder) localObject;
/*     */   }
/*     */ 
/*     */   public boolean driveEncoder()
/*     */     throws JimiException
/*     */   {
/*     */     try
/*     */     {
/*  59 */       SpecificEncoder localSpecificEncoder = createEncoder();
/*     */ 
/*  61 */       localSpecificEncoder.doImageEncode();
/*     */     }
/*     */     catch (JimiException localJimiException)
/*     */     {
/*  66 */       this.state_ = 1;
/*  67 */       throw localJimiException;
/*     */     }
/*     */ 
/*  70 */     this.state_ = 2;
/*     */ 
/*  72 */     return false;
/*     */   }
/*     */ 
/*     */   public OptionsObject getOptionsObject()
/*     */   {
/* 120 */     return this;
/*     */   }
/*     */ 
/*     */   public Object getPossibleValuesForProperty(String paramString)
/*     */   {
/* 142 */     if (paramString.equalsIgnoreCase("compression"))
/* 143 */       return POSSIBLE_COMPRESSION_VALUES;
/* 144 */     return null;
/*     */   }
/*     */ 
/*     */   public Object getProperty(String paramString)
/*     */   {
/* 179 */     return null;
/*     */   }
/*     */ 
/*     */   public String getPropertyDescription(String paramString)
/*     */     throws InvalidOptionException
/*     */   {
/* 167 */     if (paramString.equalsIgnoreCase("compression"))
/*     */     {
/* 169 */       return "RLE compression tends to be smaller than not using compression";
/*     */     }
/*     */ 
/* 173 */     throw new InvalidOptionException("No such option");
/*     */   }
/*     */ 
/*     */   public Enumeration getPropertyNames()
/*     */   {
/* 137 */     return new ArrayEnumeration(OPTION_NAMES);
/*     */   }
/*     */ 
/*     */   public int getState()
/*     */   {
/*  80 */     return this.state_;
/*     */   }
/*     */ 
/*     */   public void initSpecificEncoder(OutputStream paramOutputStream, AdaptiveRasterImage paramAdaptiveRasterImage)
/*     */   {
/*  46 */     this.output_ = paramOutputStream;
/*  47 */     if ((paramAdaptiveRasterImage.getOptions() instanceof SunRasterOptions))
/*  48 */       setUseRLE(((SunRasterOptions)paramAdaptiveRasterImage.getOptions()).isUsingRLE());
/*     */   }
/*     */ 
/*     */   public void setProperty(String paramString, Object paramObject)
/*     */     throws InvalidOptionException
/*     */   {
/* 149 */     if (paramString.equalsIgnoreCase("compression")) {
/*     */       try {
/* 151 */         setUseRLE(((Boolean)paramObject).booleanValue());
/*     */       } catch (ClassCastException localClassCastException) {
/* 153 */         throw new InvalidOptionException("Must specify a java.lang.Boolean value");
/*     */       }
/*     */     }
/*     */ 
/* 157 */     throw new InvalidOptionException("No such option");
/*     */   }
/*     */ 
/*     */   void setUseRLE(boolean paramBoolean)
/*     */   {
/* 125 */     this.useRLE_ = paramBoolean;
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.encoder.sunraster.SunRasterEncoder
 * JD-Core Version:    0.6.2
 */