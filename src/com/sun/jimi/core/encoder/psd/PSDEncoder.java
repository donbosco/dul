/*     */ package com.sun.jimi.core.encoder.psd;
/*     */ 
/*     */ import com.sun.jimi.core.InvalidOptionException;
/*     */ import com.sun.jimi.core.JimiException;
/*     */ import com.sun.jimi.core.OptionsObject;
/*     */ import com.sun.jimi.core.compat.AdaptiveRasterImage;
/*     */ import com.sun.jimi.core.compat.JimiEncoderBase;
/*     */ import com.sun.jimi.util.ArrayEnumeration;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.FilterOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.Enumeration;
/*     */ 
/*     */ public class PSDEncoder extends JimiEncoderBase
/*     */   implements OptionsObject
/*     */ {
/*     */   private OutputStream out_;
/*     */   private DataOutputStream dOut_;
/*     */   private int state_;
/*     */   static final int NO_COMPRESSION = 0;
/*     */   static final int RLE_COMPRESSION = 1;
/*     */   static final int DEFAULT_COMPRESSION = 0;
/*  53 */   int compression_ = 0;
/*     */   static final String COMPRESSION_OPTION_NAME = "compression";
/*     */   static final String NO_COMPRESSION_OPTION = "none";
/*     */   static final String RLE_COMPRESSION_OPTION = "rle";
/* 122 */   static final String[] POSSIBLE_COMPRESSION_OPTIONS = { "none", "rle" };
/*     */ 
/* 124 */   static final String[] PROPERTY_NAMES = { "compression" };
/*     */ 
/*     */   public void clearProperties()
/*     */   {
/* 200 */     this.compression_ = 0;
/*     */   }
/*     */ 
/*     */   public boolean driveEncoder()
/*     */     throws JimiException
/*     */   {
/*  71 */     AdaptiveRasterImage localAdaptiveRasterImage = getJimiImage();
/*     */     try
/*     */     {
/*  75 */       encodePSD(localAdaptiveRasterImage, this.dOut_);
/*  76 */       this.state_ |= 2;
/*  77 */       this.dOut_.flush();
/*  78 */       this.dOut_.close();
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/*  82 */       throw new JimiException(localIOException.getMessage());
/*     */     }
/*  84 */     return false;
/*     */   }
/*     */ 
/*     */   public void encodePSD(AdaptiveRasterImage paramAdaptiveRasterImage, DataOutputStream paramDataOutputStream)
/*     */     throws JimiException, IOException
/*     */   {
/* 101 */     PSDFileHeader localPSDFileHeader = new PSDFileHeader(this, paramAdaptiveRasterImage, paramDataOutputStream);
/* 102 */     localPSDFileHeader.write();
/*     */ 
/* 104 */     PSDColorMode localPSDColorMode = new PSDColorMode(paramAdaptiveRasterImage, paramDataOutputStream);
/* 105 */     localPSDColorMode.write();
/*     */ 
/* 108 */     paramDataOutputStream.writeInt(0);
/*     */ 
/* 111 */     paramDataOutputStream.writeInt(0);
/*     */ 
/* 113 */     EncodeImageIfc localEncodeImageIfc = localPSDFileHeader.createEncodeImage();
/* 114 */     localEncodeImageIfc.encodeImage(paramAdaptiveRasterImage, paramDataOutputStream, this.compression_);
/*     */   }
/*     */ 
/*     */   public void freeEncoder()
/*     */     throws JimiException
/*     */   {
/*  89 */     this.out_ = null;
/*  90 */     this.dOut_ = null;
/*  91 */     super.freeEncoder();
/*     */   }
/*     */ 
/*     */   public OptionsObject getOptionsObject()
/*     */   {
/*  64 */     return this;
/*     */   }
/*     */ 
/*     */   public Object getPossibleValuesForProperty(String paramString)
/*     */     throws InvalidOptionException
/*     */   {
/* 177 */     if (paramString.equalsIgnoreCase("compression"))
/*     */     {
/* 179 */       return POSSIBLE_COMPRESSION_OPTIONS;
/*     */     }
/*     */ 
/* 183 */     throw new InvalidOptionException("No such option");
/*     */   }
/*     */ 
/*     */   public Object getProperty(String paramString)
/*     */   {
/* 133 */     if (paramString.equalsIgnoreCase("compression"))
/*     */     {
/* 135 */       if (this.compression_ == 0) {
/* 136 */         return "none";
/*     */       }
/* 138 */       return "rle";
/*     */     }
/*     */ 
/* 143 */     return null;
/*     */   }
/*     */ 
/*     */   public String getPropertyDescription(String paramString)
/*     */     throws InvalidOptionException
/*     */   {
/* 188 */     if (paramString.equalsIgnoreCase("compression"))
/*     */     {
/* 190 */       return "RLE compression tends to be smaller than not using compression";
/*     */     }
/*     */ 
/* 194 */     throw new InvalidOptionException("No such option");
/*     */   }
/*     */ 
/*     */   public Enumeration getPropertyNames()
/*     */   {
/* 128 */     return new ArrayEnumeration(PROPERTY_NAMES);
/*     */   }
/*     */ 
/*     */   public int getState()
/*     */   {
/*  96 */     return this.state_;
/*     */   }
/*     */ 
/*     */   protected void initSpecificEncoder(OutputStream paramOutputStream, AdaptiveRasterImage paramAdaptiveRasterImage)
/*     */     throws JimiException
/*     */   {
/*  58 */     this.out_ = paramOutputStream;
/*  59 */     this.dOut_ = new DataOutputStream(paramOutputStream);
/*  60 */     this.state_ = 0;
/*     */   }
/*     */ 
/*     */   public void setProperty(String paramString, Object paramObject)
/*     */     throws InvalidOptionException
/*     */   {
/* 149 */     if (paramString.equalsIgnoreCase("compression"))
/*     */     {
/* 151 */       String str = paramObject.toString();
/*     */ 
/* 153 */       if (str.equalsIgnoreCase("none"))
/*     */       {
/* 155 */         this.compression_ = 0;
/*     */       }
/* 157 */       else if (str.equalsIgnoreCase("rle"))
/*     */       {
/* 159 */         this.compression_ = 1;
/*     */       }
/*     */       else
/*     */       {
/* 163 */         throw new InvalidOptionException("Not a valid value");
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 169 */       throw new InvalidOptionException("No such option");
/*     */     }
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.encoder.psd.PSDEncoder
 * JD-Core Version:    0.6.2
 */