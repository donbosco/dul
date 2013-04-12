/*     */ package com.sun.jimi.core;
/*     */ 
/*     */ import com.sun.jimi.core.options.BasicFormatOptionSet;
/*     */ import com.sun.jimi.core.options.FormatOption;
/*     */ import com.sun.jimi.core.options.FormatOptionSet;
/*     */ import com.sun.jimi.core.options.OptionException;
/*     */ import com.sun.jimi.core.util.FreeFormat;
/*     */ import com.sun.jimi.core.util.JimiUtil;
/*     */ import com.sun.jimi.core.util.ProgressListener;
/*     */ import java.awt.Image;
/*     */ import java.awt.image.ImageProducer;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
import java.util.Enumeration;
/*     */ 
/*     */ public class JimiWriter
/*     */ {
/*     */   protected JimiEncoder encoder;
/*     */   protected JimiImage sourceImage;
/*     */   protected JimiImageEnumeration sourceImageEnumeration;
/*  36 */   protected FormatOptionSet options = new BasicFormatOptionSet();
/*     */   protected FormatOptionSet overrideOptions;
/*     */   protected ProgressListener listener;
/*     */ 
/*     */   protected JimiWriter()
/*     */   {
/*     */   }
/*     */ 
/*     */   protected JimiWriter(OutputStream paramOutputStream, String paramString)
/*     */     throws JimiException
/*     */   {
/*  78 */     JimiEncoderFactory localJimiEncoderFactory = JimiControl.getEncoderByType(paramString);
/*  79 */     if (localJimiEncoderFactory == null) {
/*  80 */       throw new JimiException("Cannot find encoder for type: " + paramString);
/*     */     }
/*  82 */     initJimiWriter(localJimiEncoderFactory);
/*     */   }
/*     */ 
/*     */   protected JimiWriter(String paramString)
/*     */     throws JimiException
/*     */   {
/*  63 */     JimiEncoderFactory localJimiEncoderFactory = JimiControl.getEncoderByFileExtension(paramString);
/*  64 */     if (localJimiEncoderFactory == null) {
/*  65 */       throw new JimiException("Cannot find encoder for " + paramString);
/*     */     }
/*  67 */     initJimiWriter(localJimiEncoderFactory);
/*     */   }
/*     */ 
/*     */   protected JimiWriter(String paramString1, String paramString2)
/*     */     throws JimiException
/*     */   {
/*  49 */     JimiEncoderFactory localJimiEncoderFactory = JimiControl.getEncoderByType(paramString2);
/*  50 */     if (localJimiEncoderFactory == null) {
/*  51 */       throw new JimiException("Cannot find encoder for type: " + paramString2);
/*     */     }
/*  53 */     initJimiWriter(localJimiEncoderFactory);
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public void clearProperties()
/*     */   {
/* 304 */     this.options = new BasicFormatOptionSet();
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public Object getPossibleValuesForProperty(String paramString)
/*     */     throws InvalidOptionException
/*     */   {
/*     */     try
/*     */     {
/* 278 */       return this.options.getOption(paramString).getPossibleValues();
/*     */     }
/*     */     catch (OptionException localOptionException) {
/* 281 */       throw new InvalidOptionException(localOptionException.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public Object getProperty(String paramString)
/*     */   {
/*     */     try
/*     */     {
/* 313 */       return this.options.getOption(paramString).getValue();
/*     */     } catch (OptionException localOptionException) {
/*     */     }
/* 316 */     return null;
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public String getPropertyDescription(String paramString)
/*     */     throws InvalidOptionException
/*     */   {
/*     */     try
/*     */     {
/* 292 */       return this.options.getOption(paramString).getDescription();
/*     */     }
/*     */     catch (OptionException localOptionException) {
/* 295 */       throw new InvalidOptionException(localOptionException.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public Enumeration getPropertyNames()
/*     */   {
/* 325 */     FormatOption[] arrayOfFormatOption = this.options.getOptions();
/* 326 */     return new Enumeration() {
/* 327 */       int index = 0;
/*     */ 
/* 329 */       public boolean hasMoreElements() { return this.index < JimiWriter.this.sourceImageEnumeration.jimiImages.length; }
/*     */ 
/*     */       public Object nextElement() {
/* 332 */         return JimiWriter.this.sourceImageEnumeration.jimiImages[(this.index++)];//TODO: commented for removing bug
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   protected void initJimiWriter(JimiEncoderFactory paramJimiEncoderFactory)
/*     */     throws JimiException
/*     */   {
/* 111 */     if (Jimi.crippled) {
/* 112 */       throw new JimiException("Keyless operations does not permit saving.");
/*     */     }
/* 114 */     if ((Jimi.limited) && (!(paramJimiEncoderFactory instanceof FreeFormat))) {
/* 115 */       throw new JimiException("This format requires a JIMI Pro license.");
/*     */     }
/* 117 */     this.encoder = paramJimiEncoderFactory.createEncoder();
/* 118 */     if (this.listener != null)
/* 119 */       this.encoder.setProgressListener(this.listener);
/*     */   }
/*     */ 
/*     */   public void putImage(OutputStream paramOutputStream)
/*     */     throws JimiException
/*     */   {
/* 253 */     if (this.sourceImage != null) {
/* 254 */       this.sourceImage.setOptions(this.options);
/* 255 */       this.encoder.encodeImages(new JimiImageEnumeration(this.sourceImage), paramOutputStream);
/*     */     }
/* 258 */     else if (this.sourceImageEnumeration != null) {
/* 259 */       this.sourceImageEnumeration.setOptions(this.options);
/* 260 */       this.encoder.encodeImages(this.sourceImageEnumeration, paramOutputStream);
/*     */     }
/*     */     else {
/* 263 */       throw new JimiException("No source image set.");
/*     */     }
/*     */   }
/*     */ 
/*     */   public void putImage(String paramString)
/*     */     throws JimiException
/*     */   {
/*     */     try
/*     */     {
/* 236 */       Object localObject = new FileOutputStream(paramString);
/* 237 */       localObject = new BufferedOutputStream((OutputStream)localObject);
/* 238 */       putImage((OutputStream)localObject);
/* 239 */       ((OutputStream)localObject).close();
/*     */     }
/*     */     catch (IOException localIOException) {
/* 242 */       throw new JimiException();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void setMimeType(String paramString)
/*     */     throws JimiException
/*     */   {
/*  98 */     JimiEncoderFactory localJimiEncoderFactory = JimiControl.getEncoderByType(paramString);
/*  99 */     if (localJimiEncoderFactory == null) {
/* 100 */       throw new JimiException("Cannot find encoder for type: " + paramString);
/*     */     }
/* 102 */     initJimiWriter(localJimiEncoderFactory);
/*     */   }
/*     */ 
/*     */   public void setOptions(FormatOptionSet paramFormatOptionSet)
/*     */   {
/* 128 */     this.options = paramFormatOptionSet;
/* 129 */     this.overrideOptions = paramFormatOptionSet;
/*     */   }
/*     */ 
/*     */   public void setProgressListener(ProgressListener paramProgressListener)
/*     */   {
/* 137 */     this.listener = paramProgressListener;
/* 138 */     if (this.encoder != null)
/* 139 */       this.encoder.setProgressListener(paramProgressListener);
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public void setProperty(String paramString, Object paramObject)
/*     */     throws InvalidOptionException
/*     */   {
/*     */     try
/*     */     {
/* 343 */       this.options.getOption(paramString).setValue(paramObject);
/*     */     }
/*     */     catch (OptionException localOptionException) {
/* 346 */       throw new InvalidOptionException(localOptionException.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setSource(JimiImage paramJimiImage)
/*     */     throws JimiException
/*     */   {
/* 150 */     this.sourceImage = JimiUtil.asJimiRasterImage(paramJimiImage);
/* 151 */     if (this.overrideOptions != null)
/* 152 */       ((MutableJimiImage)this.sourceImage).setOptions(this.overrideOptions);
/*     */   }
/*     */ 
/*     */   public void setSource(Image paramImage)
/*     */     throws JimiException
/*     */   {
/* 163 */     setSource(paramImage.getSource());
/*     */   }
/*     */ 
/*     */   public void setSource(ImageProducer paramImageProducer)
/*     */     throws JimiException
/*     */   {
/*     */     try
/*     */     {
/* 174 */       this.sourceImage = Jimi.createRasterImage(paramImageProducer);
/*     */     }
/*     */     catch (JimiException localJimiException)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setSource(JimiImage[] paramArrayOfJimiImage)
/*     */     throws JimiException
/*     */   {
/* 187 */     this.sourceImageEnumeration = new JimiImageEnumeration(paramArrayOfJimiImage);
/*     */   }
/*     */ 
/*     */   public void setSource(Image[] paramArrayOfImage)
/*     */     throws JimiException
/*     */   {
/* 205 */     this.sourceImageEnumeration = new JimiImageEnumeration(paramArrayOfImage);
/*     */   }
/*     */ 
/*     */   public void setSource(ImageProducer[] paramArrayOfImageProducer)
/*     */     throws JimiException
/*     */   {
/* 196 */     this.sourceImageEnumeration = new JimiImageEnumeration(paramArrayOfImageProducer);
/*     */   }
/*     */ 
/*     */   public void setSource(Object[] paramArrayOfObject)
/*     */     throws JimiException
/*     */   {
/* 215 */     if ((paramArrayOfObject instanceof JimiImage[])) {
/* 216 */       setSource((JimiImage[])paramArrayOfObject);
/*     */     }
/* 218 */     else if ((paramArrayOfObject instanceof ImageProducer[])) {
/* 219 */       setSource((ImageProducer[])paramArrayOfObject);
/*     */     }
/* 221 */     else if ((paramArrayOfObject instanceof Image[])) {
/* 222 */       setSource((Image[])paramArrayOfObject);
/*     */     }
/*     */     else
/* 225 */       throw new JimiException("Invalid source.");
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.JimiWriter
 * JD-Core Version:    0.6.2
 */