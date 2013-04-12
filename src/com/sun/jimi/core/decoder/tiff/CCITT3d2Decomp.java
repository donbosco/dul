/*     */ package com.sun.jimi.core.decoder.tiff;
/*     */ 
/*     */ import com.sun.jimi.core.JimiException;
/*     */ 
/*     */ class CCITT3d2Decomp extends CCITT3d1Decomp
/*     */ {
/*     */   private byte[] LastLine;
/*  31 */   static final byte[] zeroRuns = { 
/*  32 */     8, 7, 6, 6, 5, 5, 5, 5, 4, 4, 4, 4, 4, 4, 4, 4, 
/*  33 */     3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 
/*  34 */     2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
/*  35 */     2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
/*  36 */     1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 
/*  37 */     1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 
/*  38 */     1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 
/*  39 */     1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };
/*     */ 
/*  53 */   static final byte[] oneRuns = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
/*  62 */     1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 
/*  63 */     1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 
/*  64 */     1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 
/*  65 */     1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 
/*  66 */     2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
/*  67 */     2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
/*  68 */     3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 
/*  69 */     4, 4, 4, 4, 4, 4, 4, 4, 5, 5, 5, 5, 6, 6, 7, 8 };
/*     */ 
/* 131 */   static final int[] bitMask_ = { 128, 64, 32, 16, 8, 4, 2, 1 };
/*     */   public static final int modePass = 1;
/*     */   public static final int modeHorizontal = 2;
/*     */   public static final int modeV0 = 3;
/*     */   public static final int modeVR1 = 4;
/*     */   public static final int modeVR2 = 5;
/*     */   public static final int modeVR3 = 6;
/*     */   public static final int modeVL1 = 7;
/*     */   public static final int modeVL2 = 8;
/*     */   public static final int modeVL3 = 9;
/* 391 */   private static final int[] Dim2dDict = { 
/* 392 */     67073, 
/* 393 */     67330, 
/* 394 */     66819, 
/* 395 */     66569, 
/* 396 */     1, 
/* 397 */     2, 
/* 398 */     3, 
/* 399 */     67599, 
/* 400 */     4, 
/* 401 */     68108, 
/* 402 */     68368, 
/* 403 */     5, 
/* 404 */     68864, 
/* 405 */     69137, 
/* 406 */     6, 
/* 407 */     7, 
/* 408 */     8, 
/* 409 */     9 };
/*     */ 
/*     */   CCITT3d2Decomp(TiffNumberReader paramTiffNumberReader, int paramInt)
/*     */   {
/*  20 */     super(paramTiffNumberReader, paramInt);
/*     */   }
/*     */ 
/*     */   public void begOfStrip()
/*     */   {
/*  25 */     this.bitOffset = 0;
/*     */   }
/*     */ 
/*     */   public int decode2DWord()
/*     */   {
/* 183 */     int i = 0;
/* 184 */     int j = Dim2dDict[0];
/*     */     do
/*     */     {
/* 187 */       if ((this.bitOffset++ & 0x7) == 0)
/* 188 */         this.byteSource = readByte();
/*     */       else {
/* 190 */         this.byteSource = ((byte)(this.byteSource << 1));
/*     */       }
/* 192 */       if ((this.byteSource & 0x80) != 0)
/* 193 */         i = (j & 0xFF00) >>> 8;
/*     */       else {
/* 195 */         i = j & 0xFF;
/*     */       }
/* 197 */       j = Dim2dDict[i];
/* 198 */     }while ((j & 0x10000) != 0);
/* 199 */     return j;
/*     */   }
/*     */ 
/*     */   public void decodeLine(byte[] paramArrayOfByte, int paramInt)
/*     */     throws JimiException
/*     */   {
/* 205 */     if ((this.LastLine == null) || (this.LastLine.length != paramArrayOfByte.length))
/*     */     {
/* 207 */       this.LastLine = new byte[paramArrayOfByte.length];
/* 208 */       if (this.invertOut_) {
/* 209 */         CCITT3d1Decomp.setArrayFF(this.LastLine);
/*     */       }
/*     */     }
/*     */ 
/* 213 */     CCITT3d1Decomp.setArrayZero(paramArrayOfByte);
/*     */ 
/* 216 */     int i = -1;
/* 217 */     byte b = 0;
/*     */ 
/* 220 */     if (this.invertOut_)
/*     */     {
/* 223 */       while (i < paramInt)
/*     */       {
/* 225 */         int k = findB1(i, paramInt, b);
/*     */ 
/* 227 */         if (i == -1) {
/* 228 */           i = 0;
/*     */         }
/* 230 */         int m = decode2DWord();
int j;
/*     */ 
/* 232 */         switch (m)
/*     */         {
/*     */         case 1:
/* 235 */           b = (byte)(b ^ 0x1);
/* 236 */           k = findBitColorChange(paramInt, b, ++k);
/* 237 */           if (b == 1)
/* 238 */             outputBitRun(paramArrayOfByte, i, k - i);
/* 239 */           i += k - i;
/* 240 */           break;
/*     */         case 2:
/* 242 */           j = getRunLength(b);
/* 243 */           if (b == 0)
/* 244 */             outputBitRun(paramArrayOfByte, i, j);
/* 245 */           i += j;
/* 246 */           b = (byte)(b ^ 0x1);
/* 247 */           j = getRunLength(b);
/* 248 */           if (b == 0)
/* 249 */             outputBitRun(paramArrayOfByte, i, j);
/* 250 */           i += j;
/* 251 */           break;
/*     */         case 3:
/* 253 */           if (b == 0)
/* 254 */             outputBitRun(paramArrayOfByte, i, k - i);
/* 255 */           i += k - i;
/* 256 */           break;
/*     */         case 4:
/* 258 */           if (b == 0)
/* 259 */             outputBitRun(paramArrayOfByte, i, 1 + k - i);
/* 260 */           i += 1 + k - i;
/* 261 */           break;
/*     */         case 5:
/* 263 */           if (b == 0)
/* 264 */             outputBitRun(paramArrayOfByte, i, 2 + k - i);
/* 265 */           i += 2 + k - i;
/* 266 */           break;
/*     */         case 6:
/* 268 */           if (b == 0)
/* 269 */             outputBitRun(paramArrayOfByte, i, 3 + k - i);
/* 270 */           i += 3 + k - i;
/* 271 */           break;
/*     */         case 7:
/* 273 */           if (b == 0)
/* 274 */             outputBitRun(paramArrayOfByte, i, k - i - 1);
/* 275 */           i += k - i - 1;
/* 276 */           break;
/*     */         case 8:
/* 278 */           if (b == 0)
/* 279 */             outputBitRun(paramArrayOfByte, i, k - i - 2);
/* 280 */           i += k - i - 2;
/* 281 */           break;
/*     */         case 9:
/* 283 */           if (b == 0)
/* 284 */             outputBitRun(paramArrayOfByte, i, k - i - 3);
/* 285 */           i += k - i - 3;
/* 286 */           break;
/*     */         }
/* 288 */         b = (byte)(b ^ 0x1);
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 293 */       while (i < paramInt)
/*     */       {
/*     */         int j;
/* 297 */         int k = findB1(i, paramInt, b);
/*     */ 
/* 300 */         if (i == -1) {
/* 301 */           i = 0;
/*     */         }
/* 303 */         int m = decode2DWord();
/*     */ 
/* 305 */         switch (m)
/*     */         {
/*     */         case 1:
/* 308 */           b = (byte)(b ^ 0x1);
/* 309 */           k = findBitColorChange(paramInt, b, ++k);
/* 310 */           if (b == 0)
/* 311 */             outputBitRun(paramArrayOfByte, i, k - i);
/* 312 */           i += k - i;
/* 313 */           break;
/*     */         case 2:
/* 315 */           j = getRunLength(b);
/* 316 */           if (b == 1)
/* 317 */             outputBitRun(paramArrayOfByte, i, j);
/* 318 */           i += j;
/* 319 */           b = (byte)(b ^ 0x1);
/* 320 */           j = getRunLength(b);
/* 321 */           if (b == 1)
/* 322 */             outputBitRun(paramArrayOfByte, i, j);
/* 323 */           i += j;
/* 324 */           break;
/*     */         case 3:
/* 326 */           if (b == 1)
/* 327 */             outputBitRun(paramArrayOfByte, i, k - i);
/* 328 */           i += k - i;
/* 329 */           break;
/*     */         case 4:
/* 331 */           if (b == 1)
/* 332 */             outputBitRun(paramArrayOfByte, i, 1 + k - i);
/* 333 */           i += 1 + k - i;
/* 334 */           break;
/*     */         case 5:
/* 336 */           if (b == 1)
/* 337 */             outputBitRun(paramArrayOfByte, i, 2 + k - i);
/* 338 */           i += 2 + k - i;
/* 339 */           break;
/*     */         case 6:
/* 341 */           if (b == 1)
/* 342 */             outputBitRun(paramArrayOfByte, i, 3 + k - i);
/* 343 */           i += 3 + k - i;
/* 344 */           break;
/*     */         case 7:
/* 346 */           if (b == 1)
/* 347 */             outputBitRun(paramArrayOfByte, i, k - i - 1);
/* 348 */           i += k - i - 1;
/* 349 */           break;
/*     */         case 8:
/* 351 */           if (b == 1)
/* 352 */             outputBitRun(paramArrayOfByte, i, k - i - 2);
/* 353 */           i += k - i - 2;
/* 354 */           break;
/*     */         case 9:
/* 356 */           if (b == 1)
/* 357 */             outputBitRun(paramArrayOfByte, i, k - i - 3);
/* 358 */           i += k - i - 3;
/* 359 */           break;
/*     */         }
/* 361 */         b = (byte)(b ^ 0x1);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 366 */     System.arraycopy(paramArrayOfByte, 0, this.LastLine, 0, paramArrayOfByte.length);
/*     */   }
/*     */ 
/*     */   public int findB1(int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 135 */     int i = paramInt1 + 1;
/* 136 */     if (i < paramInt2)
/*     */     {
/*     */       int j;
/* 139 */       if (paramInt1 < 0) {
/* 140 */         j = 0;
/*     */       }
/* 143 */       else if (this.invertOut_)
/*     */       {
/* 145 */         j = 1;
/* 146 */         if ((this.LastLine[(paramInt1 >> 3)] & bitMask_[(paramInt1 & 0x7)]) != 0)
/* 147 */           j = 0;
/*     */       }
/*     */       else
/*     */       {
/* 151 */         j = 0;
/* 152 */         if ((this.LastLine[(paramInt1 >> 3)] & bitMask_[(paramInt1 & 0x7)]) != 0) {
/* 153 */           j = 1;
/*     */         }
/*     */       }
/*     */ 
/* 157 */       i = findBitColorChange(paramInt2, j, i);
/*     */ 
/* 159 */       if (i < paramInt2)
/*     */       {
/* 161 */         if (this.invertOut_)
/*     */         {
/* 163 */           j = 1;
/* 164 */           if ((this.LastLine[(i >> 3)] & bitMask_[(i & 0x7)]) != 0)
/* 165 */             j = 0;
/*     */         }
/*     */         else
/*     */         {
/* 169 */           j = 0;
/* 170 */           if ((this.LastLine[(i >> 3)] & bitMask_[(i & 0x7)]) != 0) {
/* 171 */             j = 1;
/*     */           }
/*     */         }
/* 174 */         if (paramInt3 == j)
/* 175 */           i = findBitColorChange(paramInt2, paramInt3, ++i);
/*     */       }
/*     */     }
/* 178 */     return i;
/*     */   }
/*     */ 
/*     */   public int findBitColorChange(int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/*     */     byte[] arrayOfByte;
/*  83 */     if (this.invertOut_)
/*  84 */       arrayOfByte = paramInt2 == 0 ? oneRuns : zeroRuns;
/*     */     else {
/*  86 */       arrayOfByte = paramInt2 == 0 ? zeroRuns : oneRuns;
/*     */     }
/*  88 */     int i = paramInt1 - paramInt3;
/*  89 */     int j = paramInt3 >> 3;
/*     */ 
/*  91 */     int m = paramInt3 & 0x7;
/*     */     int k;
/*  94 */     if ((i > 0) && (m != 0))
/*     */     {
/*  96 */       k = arrayOfByte[(this.LastLine[j] << m & 0xFF)];
/*  97 */       if (k > 8 - m)
/*  98 */         k = 8 - m;
/*  99 */       if (k > i)
/* 100 */         k = i;
/* 101 */       if (m + k < 8) {
/* 102 */         return paramInt3 + k;
/*     */       }
/* 104 */       i -= k;
/* 105 */       j++;
/*     */     }
/*     */     else {
/* 108 */       k = 0;
/*     */     }
/*     */     int n;
/* 112 */     while (i >= 8)
/*     */     {
/* 114 */       n = arrayOfByte[(this.LastLine[(j++)] & 0xFF)];
/* 115 */       k += n;
/* 116 */       i -= n;
/* 117 */       if (n < 8) {
/* 118 */         return paramInt3 + k;
/*     */       }
/*     */     }
/*     */ 
/* 122 */     if (i > 0)
/*     */     {
/* 124 */       n = arrayOfByte[(this.LastLine[(j++)] & 0xFF)];
/* 125 */       k += (n > i ? i : n);
/*     */     }
/* 127 */     return paramInt3 + k;
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.decoder.tiff.CCITT3d2Decomp
 * JD-Core Version:    0.6.2
 */