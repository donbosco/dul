/*     */ package com.sun.jimi.core.encoder.cur;
/*     */ 
/*     */ import com.sun.jimi.core.InvalidOptionException;
/*     */ import com.sun.jimi.core.JimiException;
/*     */ import com.sun.jimi.core.compat.AdaptiveRasterImage;
/*     */ import com.sun.jimi.core.encoder.ico.ICOEncoder;
/*     */ import com.sun.jimi.core.util.LEDataOutputStream;
/*     */ import com.sun.jimi.util.ArrayEnumeration;
/*     */ import com.sun.jimi.util.IntegerRange;
/*     */ import java.awt.image.IndexColorModel;
/*     */ import java.io.IOException;
/*     */ import java.util.Enumeration;
/*     */ 
/*     */ public class CUREncoder extends ICOEncoder
/*     */ {
/*  52 */   private short xHotspot = 0;
/*  53 */   private short yHotspot = 0;
/*     */   static final String OPTION_XHOTSPOT = "hotspot location X";
/*     */   static final String OPTION_YHOTSPOT = "hotpost location Y";
/* 108 */   static final String[] OPTION_NAMES = { "hotspot location X", "hotpost location Y" };
/* 109 */   static final IntegerRange POSSIBLE_XHOTSPOT_VALUES = new IntegerRange(0, 65536);
/* 110 */   static final IntegerRange POSSIBLE_YHOTSPOT_VALUES = POSSIBLE_XHOTSPOT_VALUES;
/*     */ 
/*     */   public CUREncoder()
/*     */   {
/*  49 */     this.TYPE_FLAG = 2;
/*     */   }
/*     */ 
/*     */   public void clearProperties()
/*     */   {
/* 180 */     setHotspot((short)0, (short)0);
/*     */   }
/*     */ 
/*     */   public Object getPossibleValuesForProperty(String paramString)
/*     */   {
/* 120 */     if (paramString.equalsIgnoreCase("hotspot location X"))
/*     */     {
/* 122 */       return POSSIBLE_XHOTSPOT_VALUES;
/*     */     }
/* 124 */     if (paramString.equalsIgnoreCase("hotpost location Y"))
/*     */     {
/* 126 */       return POSSIBLE_YHOTSPOT_VALUES;
/*     */     }
/*     */ 
/* 130 */     return null;
/*     */   }
/*     */ 
/*     */   public String getPropertyDescription(String paramString)
/*     */     throws InvalidOptionException
/*     */   {
/* 185 */     if (paramString.equalsIgnoreCase("hotspot location X"))
/*     */     {
/* 187 */       return "The location of the cursor's pointer along the X-axis";
/*     */     }
/* 189 */     if (paramString.equalsIgnoreCase("hotpost location Y"))
/*     */     {
/* 191 */       return "The location of the cursor's pointer along the Y-axis";
/*     */     }
/*     */ 
/* 195 */     throw new InvalidOptionException("No such option");
/*     */   }
/*     */ 
/*     */   public Enumeration getPropertyNames()
/*     */   {
/* 114 */     return new ArrayEnumeration(OPTION_NAMES);
/*     */   }
/*     */ 
/*     */   public void setHotspot(short paramShort1, short paramShort2)
/*     */   {
/* 101 */     this.xHotspot = paramShort1;
/* 102 */     this.yHotspot = paramShort2;
/*     */   }
/*     */ 
/*     */   public void setProperty(String paramString, Object paramObject)
/*     */     throws InvalidOptionException
/*     */   {
/* 137 */     short s = 0;
/*     */     try
/*     */     {
/* 141 */       s = (short)((Integer)paramObject).intValue();
/*     */     }
/*     */     catch (ClassCastException localClassCastException)
/*     */     {
/* 145 */       throw new InvalidOptionException("Must specify a java.lang.Integer value");
/*     */     }
/*     */ 
/* 150 */     if (paramString.equalsIgnoreCase("hotspot location X"))
/*     */     {
/* 152 */       if (!POSSIBLE_XHOTSPOT_VALUES.isInRange(paramObject))
/*     */       {
/* 154 */         throw new InvalidOptionException("Value is out of range");
/*     */       }
/*     */ 
/* 158 */       setHotspot(s, this.yHotspot);
/*     */     }
/* 160 */     else if (paramString.equalsIgnoreCase("hotpost location Y"))
/*     */     {
/* 162 */       if (!POSSIBLE_YHOTSPOT_VALUES.isInRange(paramObject))
/*     */       {
/* 164 */         throw new InvalidOptionException("Value is out of range");
/*     */       }
/*     */ 
/* 168 */       setHotspot(this.xHotspot, s);
/*     */     }
/*     */     else
/*     */     {
/* 173 */       throw new InvalidOptionException("No such Option");
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void writeICOCURDIREntry(LEDataOutputStream paramLEDataOutputStream, AdaptiveRasterImage paramAdaptiveRasterImage)
/*     */     throws JimiException, IOException
/*     */   {
/*     */     IndexColorModel localIndexColorModel;
/*     */     try
/*     */     {
/*  60 */       localIndexColorModel = (IndexColorModel)paramAdaptiveRasterImage.getColorModel();
/*     */     }
/*     */     catch (ClassCastException localClassCastException)
/*     */     {
/*  64 */       throw new JimiException("image/x-cur formats can only be created from palette images");
/*     */     }
/*     */ 
/*  67 */     int i = localIndexColorModel.getMapSize();
/*  68 */     if (i > 256)
/*     */     {
/*  70 */       throw new JimiException("image/x-cur formats can only support palette with up to 256 colors");
/*     */     }
/*     */ 
/*  73 */     int j = computeBitCount(i);
/*     */ 
/*  75 */     int k = paramAdaptiveRasterImage.getWidth();
/*  76 */     int m = paramAdaptiveRasterImage.getHeight();
/*  77 */     if ((k > 256) || (m > 256)) {
/*  78 */       throw new JimiException("image/x-cur formats can only encode images up to 256 x 256 pixels");
/*     */     }
/*     */ 
/*  81 */     int n = computeImageSize(i, j, k, m);
/*     */ 
/*  83 */     paramLEDataOutputStream.writeByte((byte)k);
/*  84 */     paramLEDataOutputStream.writeByte((byte)m);
/*  85 */     paramLEDataOutputStream.writeByte((byte)i);
/*  86 */     paramLEDataOutputStream.writeByte(0);
/*  87 */     paramLEDataOutputStream.writeShort(this.xHotspot);
/*  88 */     paramLEDataOutputStream.writeShort(this.yHotspot);
/*  89 */     int i1 = 40 + n * j / 8 + (int)Math.pow(2.0D, j);
/*     */ 
/*  91 */     paramLEDataOutputStream.writeInt(i1);
/*     */ 
/*  94 */     paramLEDataOutputStream.writeInt(this.currentOffset);
/*  95 */     this.currentOffset += n;
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.encoder.cur.CUREncoder
 * JD-Core Version:    0.6.2
 */