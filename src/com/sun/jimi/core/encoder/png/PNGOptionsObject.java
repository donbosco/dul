/*     */ package com.sun.jimi.core.encoder.png;
/*     */ 
/*     */ import com.sun.jimi.core.InvalidOptionException;
/*     */ import com.sun.jimi.core.OptionsObject;
/*     */ import com.sun.jimi.util.ArrayEnumeration;
/*     */ import java.util.Enumeration;
/*     */ 
/*     */ class PNGOptionsObject
/*     */   implements OptionsObject, PNGConstants
/*     */ {
/*     */   PNGEncoder encoder;
/*     */   static final String ALPHA_OPTION_NAME = "alpha";
/*     */   static final String COMPRESSION_OPTION_NAME = "compression";
/*     */   static final String INTERLACE_OPTION_NAME = "interlace";
/*     */   static final String FILTER_OPTION_NAME = "filter";
/*  62 */   static final String[] OPTION_NAMES = { 
/*  63 */     "alpha", 
/*  64 */     "compression", 
/*  65 */     "interlace", 
/*  66 */     "filter" };
/*     */ 
/*  68 */   static final Boolean[] POSSIBLE_ALPHA_OPTIONS = { 
/*  69 */     Boolean.TRUE, Boolean.FALSE, null };
/*     */   static final String COMPRESSION_NONE = "none";
/*     */   static final String COMPRESSION_DEFAULT = "default";
/*     */   static final String COMPRESSION_FAST = "fast";
/*     */   static final String COMPRESSION_MAX = "max";
/*  76 */   static final String[] POSSIBLE_COMPRESSION_OPTIONS = { 
/*  77 */     "none", "fast", "default", "max" };
/*     */   static final String INTERLACE_NONE = "none";
/*     */   static final String INTERLACE_ADAM7 = "adam7";
/*  82 */   static final String[] POSSIBLE_INTERLACE_OPTIONS = { 
/*  83 */     "none", "adam7" };
/*     */   static final String FILTER_NONE = "none";
/*     */   static final String FILTER_SUB = "sub";
/*     */   static final String FILTER_UP = "up";
/*     */   static final String FILTER_AVG = "average";
/*     */   static final String FILTER_PAETH = "paeth";
/*     */   static final String FILTER_ALL = "all";
/*  92 */   static final String[] POSSIBLE_FILTER_OPTIONS = { 
/*  93 */     "none", "sub", "up", "average", "paeth", "all" };
/*     */ 
/*     */   PNGOptionsObject(PNGEncoder paramPNGEncoder)
/*     */   {
/*  98 */     this.encoder = paramPNGEncoder;
/*     */   }
/*     */ 
/*     */   public void clearProperties()
/*     */   {
/* 215 */     this.encoder.setInterlace((byte)0);
/* 216 */     this.encoder.setAlpha(null);
/* 217 */     this.encoder.setCompression(0);
/* 218 */     this.encoder.setFilter((byte)0);
/*     */   }
/*     */ 
/*     */   public Object getPossibleValuesForProperty(String paramString)
/*     */     throws InvalidOptionException
/*     */   {
/* 191 */     if (paramString.equalsIgnoreCase("alpha"))
/*     */     {
/* 193 */       return POSSIBLE_ALPHA_OPTIONS;
/*     */     }
/* 195 */     if (paramString.equalsIgnoreCase("compression"))
/*     */     {
/* 197 */       return POSSIBLE_COMPRESSION_OPTIONS;
/*     */     }
/* 199 */     if (paramString.equalsIgnoreCase("interlace"))
/*     */     {
/* 201 */       return POSSIBLE_INTERLACE_OPTIONS;
/*     */     }
/* 203 */     if (paramString.equalsIgnoreCase("filter"))
/*     */     {
/* 205 */       return POSSIBLE_FILTER_OPTIONS;
/*     */     }
/*     */ 
/* 209 */     throw new InvalidOptionException("No such option");
/*     */   }
/*     */ 
/*     */   public Object getProperty(String paramString)
/*     */   {
/*     */     int i;
/* 224 */     if (paramString.equalsIgnoreCase("interlace"))
/*     */     {
/* 226 */       i = this.encoder.getInterlace();
/*     */ 
/* 228 */       if (i == 0) {
/* 229 */         return "none";
/*     */       }
/* 231 */       return "adam7";
/*     */     }
/*     */ 
/* 234 */     if (paramString.equalsIgnoreCase("alpha"))
/*     */     {
/* 237 */       return this.encoder.getAlpha();
/*     */     }
/* 239 */     if (paramString.equalsIgnoreCase("compression"))
/*     */     {
/* 241 */       i = (byte)this.encoder.getCompression();
/*     */ 
/* 244 */       if (i == 0)
/* 245 */         return "none";
/* 246 */       if (i == 1)
/* 247 */         return "fast";
/* 248 */       if (i == 9) {
/* 249 */         return "max";
/*     */       }
/* 251 */       return "default";
/*     */     }
/*     */ 
/* 255 */     if (paramString.equalsIgnoreCase("filter"))
/*     */     {
/* 257 */       i = this.encoder.getFilter();
/*     */ 
/* 260 */       if (i == 0)
/* 261 */         return "none";
/* 262 */       if (i == 1)
/* 263 */         return "sub";
/* 264 */       if (i == 3)
/* 265 */         return "average";
/* 266 */       if (i == 2)
/* 267 */         return "up";
/* 268 */       if (i == 4) {
/* 269 */         return "paeth";
/*     */       }
/* 271 */       return "all";
/*     */     }
/*     */ 
/* 277 */     return null;
/*     */   }
/*     */ 
/*     */   public String getPropertyDescription(String paramString)
/*     */     throws InvalidOptionException
/*     */   {
/* 285 */     if (paramString.equalsIgnoreCase("alpha"))
/*     */     {
/* 287 */       return "An alpha channel is optional, a null value will let JIMI decide";
/*     */     }
/* 289 */     if (paramString.equalsIgnoreCase("compression"))
/*     */     {
/* 291 */       return "This option has no affect";
/*     */     }
/* 293 */     if (paramString.equalsIgnoreCase("interlace"))
/*     */     {
/* 295 */       return "Not currently implemented";
/*     */     }
/* 297 */     if (paramString.equalsIgnoreCase("filter"))
/*     */     {
/* 299 */       return "Not currently implemented";
/*     */     }
/*     */ 
/* 303 */     throw new InvalidOptionException("No such option");
/*     */   }
/*     */ 
/*     */   public Enumeration getPropertyNames()
/*     */   {
/* 186 */     return new ArrayEnumeration(OPTION_NAMES);
/*     */   }
/*     */ 
/*     */   public void setProperty(String paramString, Object paramObject)
/*     */     throws InvalidOptionException
/*     */   {
/*     */     String str;
/* 105 */     if (paramString.equalsIgnoreCase("compression"))
/*     */     {
/*     */       try
/*     */       {
/* 110 */         str = (String)paramObject;
/*     */       } catch (ClassCastException localClassCastException1) {
/* 112 */         throw new InvalidOptionException("Not a valid option");
/*     */       }
/*     */ 
/* 115 */       if (str.equalsIgnoreCase("none"))
/* 116 */         this.encoder.setCompression(0);
/* 117 */       else if (str.equalsIgnoreCase("fast"))
/* 118 */         this.encoder.setCompression(1);
/* 119 */       else if (str.equalsIgnoreCase("max"))
/* 120 */         this.encoder.setCompression(9);
/* 121 */       else if (str.equalsIgnoreCase("default"))
/* 122 */         this.encoder.setCompression(-1);
/*     */       else {
/* 124 */         throw new InvalidOptionException("Not a valid option");
/*     */       }
/*     */     }
/* 127 */     else if (paramString.equalsIgnoreCase("interlace"))
/*     */     {
/* 129 */       str = paramObject.toString();
/*     */ 
/* 131 */       if (str.equalsIgnoreCase("none"))
/* 132 */         this.encoder.setInterlace((byte)0);
/* 133 */       else if (str.equalsIgnoreCase("adam7"))
/* 134 */         this.encoder.setInterlace((byte)1);
/*     */       else {
/* 136 */         throw new InvalidOptionException("Not a valid option");
/*     */       }
/*     */     }
/* 139 */     else if (paramString.equalsIgnoreCase("alpha"))
/*     */     {
/* 141 */       if (paramObject == null) {
/* 142 */         this.encoder.setAlpha(null);
/*     */       }
/*     */       else {
/*     */         try
/*     */         {
/* 147 */           this.encoder.setAlpha((Boolean)paramObject);
/*     */         }
/*     */         catch (ClassCastException localClassCastException2)
/*     */         {
/* 151 */           throw new InvalidOptionException("Value must be a java.lang.Boolean");
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/* 157 */     else if (paramString.equalsIgnoreCase("filter"))
/*     */     {
/* 159 */       str = paramObject.toString();
/*     */ 
/* 161 */       if (str.equalsIgnoreCase("none"))
/* 162 */         this.encoder.setFilter((byte)0);
/* 163 */       else if (str.equalsIgnoreCase("sub"))
/* 164 */         this.encoder.setFilter((byte)1);
/* 165 */       else if (str.equalsIgnoreCase("average"))
/* 166 */         this.encoder.setFilter((byte)3);
/* 167 */       else if (str.equalsIgnoreCase("up"))
/* 168 */         this.encoder.setFilter((byte)2);
/* 169 */       else if (str.equalsIgnoreCase("paeth"))
/* 170 */         this.encoder.setFilter((byte)4);
/* 171 */       else if (str.equalsIgnoreCase("all"))
/* 172 */         this.encoder.setFilter((byte)5);
/*     */       else {
/* 174 */         throw new InvalidOptionException("No such option");
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 179 */       throw new InvalidOptionException("No such property");
/*     */     }
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.encoder.png.PNGOptionsObject
 * JD-Core Version:    0.6.2
 */