/*     */ package com.sun.jimi.core.util;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public class Packbits
/*     */ {
/*     */   static final int INITIAL = 0;
/*     */   static final int LITERAL = 1;
/*     */   static final int UNDECIDED = 2;
/*     */   static byte[] runb;
/*     */ 
/*     */   public static int getAllRuns(byte[] paramArrayOfByte)
/*     */   {
/* 266 */     int i = 0;
/* 267 */     int j = 0;
/*     */     do
/*     */     {
/* 270 */       int n = i;
/* 271 */       int k = paramArrayOfByte[(i++)];
/* 272 */       while ((i < paramArrayOfByte.length) && (k == paramArrayOfByte[i]))
/* 273 */         i++;
/* 274 */       int m = i - n;
/* 275 */       while (m > 128)
/*     */       {
/* 278 */         runb[(j++)] = 127;
/* 279 */         m -= 128;
/*     */       }
/* 281 */       runb[(j++)] = ((byte)(m - 1));
/* 282 */     }while (i < paramArrayOfByte.length);
/*     */ 
/* 284 */     return j;
/*     */   }
/*     */ 
/*     */   public static int packbits(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
/*     */     throws ArrayStoreException, ArrayIndexOutOfBoundsException
/*     */   {
/* 342 */     if ((runb == null) || (runb.length != paramArrayOfByte1.length)) {
/* 343 */       runb = new byte[paramArrayOfByte1.length];
/*     */     }
/* 345 */     int i = getAllRuns(paramArrayOfByte1);
/*     */ 
/* 347 */     int i2 = 0;
/* 348 */     int m = 0;
/* 349 */     int n = 0;
/* 350 */     int i1 = 0;
/* 351 */     for (int j = 0; j < i; j++)
/*     */     {
/* 353 */       int k = runb[j] + 1;
/*     */ 
/* 355 */       switch (m)
/*     */       {
/*     */       case 0:
/*     */         do
/*     */         {
/* 360 */           paramArrayOfByte2[(i1++)] = -127;
/* 361 */           paramArrayOfByte2[(i1++)] = paramArrayOfByte1[n];
/* 362 */           n += 128;
/* 363 */           k -= 128;
/*     */         }
/* 358 */         while (k > 128);
/*     */ 
/* 365 */         if (k == 1)
/*     */         {
/* 367 */           m = 1;
/* 368 */           i2 = 1;
/*     */         }
/* 370 */         else if (k == 2)
/*     */         {
/* 372 */           m = 2;
/* 373 */           i2 = 2;
/*     */         }
/*     */         else
/*     */         {
/* 378 */           paramArrayOfByte2[(i1++)] = ((byte)-(k - 1));
/* 379 */           paramArrayOfByte2[(i1++)] = paramArrayOfByte1[n];
/* 380 */           n += k;
/*     */         }
/* 382 */         break;
/*     */       case 1:
/* 385 */         if (k < 3)
/*     */         {
/* 388 */           i2 += k;
/*     */         }
/*     */         else
/*     */         {
/* 392 */           m = 0;
/*     */ 
/* 395 */           while (i2 > 128)
/*     */           {
/* 397 */             paramArrayOfByte2[(i1++)] = 127;
/* 398 */             System.arraycopy(paramArrayOfByte1, n, paramArrayOfByte2, i1, 128);
/* 399 */             n += 128;
/* 400 */             i1 += 128;
/* 401 */             i2 -= 128;
/*     */           }
/*     */ 
/* 404 */           paramArrayOfByte2[(i1++)] = ((byte)(i2 - 1));
/* 405 */           System.arraycopy(paramArrayOfByte1, n, paramArrayOfByte2, i1, i2);
/* 406 */           n += i2;
/* 407 */           i1 += i2;
/*     */ 
/* 410 */           while (k > 128)
/*     */           {
/* 412 */             paramArrayOfByte2[(i1++)] = -127;
/* 413 */             paramArrayOfByte2[(i1++)] = paramArrayOfByte1[n];
/* 414 */             n += 128;
/* 415 */             k -= 128;
/*     */           }
/*     */ 
/* 418 */           if (k == 1)
/*     */           {
/* 420 */             m = 1;
/* 421 */             i2 = 1;
/*     */           }
/* 423 */           else if (k == 2)
/*     */           {
/* 425 */             m = 2;
/* 426 */             i2 = 2;
/*     */           }
/*     */           else
/*     */           {
/* 430 */             paramArrayOfByte2[(i1++)] = ((byte)-(k - 1));
/* 431 */             paramArrayOfByte2[(i1++)] = paramArrayOfByte1[n];
/* 432 */             n += k;
/*     */           }
/*     */         }
/* 435 */         break;
/*     */       case 2:
/* 438 */         if (k == 1)
/*     */         {
/* 440 */           m = 1;
/* 441 */           i2++;
/*     */         }
/* 443 */         else if (k == 2)
/*     */         {
/* 446 */           i2 += 2;
/*     */         }
/*     */         else
/*     */         {
/* 450 */           m = 0;
/*     */ 
/* 456 */           for (; i2 > 0; i2 -= 2)
/*     */           {
/* 458 */             paramArrayOfByte2[(i1++)] = -1;
/* 459 */             paramArrayOfByte2[(i1++)] = paramArrayOfByte1[n];
/* 460 */             n += 2;
/*     */           }
/*     */ 
/* 464 */           while (k > 128)
/*     */           {
/* 466 */             paramArrayOfByte2[(i1++)] = -127;
/* 467 */             paramArrayOfByte2[(i1++)] = paramArrayOfByte1[n];
/* 468 */             n += 128;
/* 469 */             k -= 128;
/*     */           }
/*     */ 
/* 472 */           if (k == 1)
/*     */           {
/* 474 */             m = 1;
/* 475 */             i2 = 1;
/*     */           }
/* 477 */           else if (k == 2)
/*     */           {
/* 479 */             m = 2;
/* 480 */             i2 = 2;
/*     */           }
/*     */           else
/*     */           {
/* 485 */             paramArrayOfByte2[(i1++)] = ((byte)-(k - 1));
/* 486 */             paramArrayOfByte2[(i1++)] = paramArrayOfByte1[n];
/* 487 */             n += k;
/*     */           }
/*     */         }
/*     */         break;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 495 */     switch (m)
/*     */     {
/*     */     case 1:
/*     */       do
/*     */       {
/* 500 */         paramArrayOfByte2[(i1++)] = 127;
/* 501 */         System.arraycopy(paramArrayOfByte1, n, paramArrayOfByte2, i1, 128);
/* 502 */         n += 128;
/* 503 */         i1 += 128;
/* 504 */         i2 -= 128;
/*     */       }
/* 498 */       while (i2 > 128);
/*     */ 
/* 506 */       if (i2 > 0)
/*     */       {
/* 508 */         paramArrayOfByte2[(i1++)] = ((byte)(i2 - 1));
/* 509 */         System.arraycopy(paramArrayOfByte1, n, paramArrayOfByte2, i1, i2);
/* 510 */         n += i2;
/* 511 */         i1 += i2;
/*     */       }
/* 513 */       break;
/*     */     case 2:
/*     */       while (true)
/*     */       {
/* 520 */         paramArrayOfByte2[(i1++)] = -1;
/* 521 */         paramArrayOfByte2[(i1++)] = paramArrayOfByte1[n];
/* 522 */         n += 2;
/*     */ 
/* 518 */         i2 -= 2; if (i2 <= 0)
/*     */         {
/* 524 */           break;
/*     */         }
/*     */       }
/*     */     }
/* 527 */     return i1;
/*     */   }
/*     */ 
/*     */   public static void unpackbits(DataInputStream paramDataInputStream, byte[] paramArrayOfByte)
/*     */     throws ArrayStoreException, ArrayIndexOutOfBoundsException, IOException
/*     */   {
/* 213 */     int i = 0;
/* 214 */     for (i = 0; i < paramArrayOfByte.length; )
/*     */     {
/* 217 */       int j = paramDataInputStream.readByte();
/* 218 */       if (j >= 0)
/*     */       {
/* 220 */         j++;
/*     */ 
/* 222 */         for (int n = 0; n < j; n++) {
/* 223 */           paramArrayOfByte[(i + n)] = paramDataInputStream.readByte();
/*     */         }
/* 225 */         i += j;
/*     */       }
/* 227 */       else if (j != -128)
/*     */       {
/* 231 */         int k = paramDataInputStream.readByte();
/* 232 */         int m = i - j + 1;
/* 233 */         for (; i < m; i++)
/* 234 */           paramArrayOfByte[i] = (byte) k;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void unpackbits(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2, int paramInt3)
/*     */   {
/*     */   }
/*     */ 
/*     */   public static void unpackbits(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
/*     */     throws ArrayStoreException, ArrayIndexOutOfBoundsException
/*     */   {
/* 136 */     int i = 0;
/* 137 */     int j = 0;
/* 138 */     for (j = 0; j < paramArrayOfByte2.length; )
/*     */     {
/* 140 */       int k = paramArrayOfByte1[(i++)];
/* 141 */       if (k >= 0)
/*     */       {
/* 143 */         k++;
/* 144 */         System.arraycopy(paramArrayOfByte1, i, paramArrayOfByte2, j, k);
/* 145 */         i += k;
/* 146 */         j += k;
/*     */       }
/* 148 */       else if (k != -128)
/*     */       {
/* 150 */         int m = paramArrayOfByte1[(i++)];
/* 151 */         int n = j - k + 1;
/* 152 */         for (; j < n; j++)
/* 153 */           paramArrayOfByte2[j] = (byte) m;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void unpackbits(byte[] paramArrayOfByte, int[] paramArrayOfInt)
/*     */     throws ArrayStoreException, ArrayIndexOutOfBoundsException
/*     */   {
/* 171 */     int i = 0;
/* 172 */     int j = 0;
/* 173 */     for (j = 0; j < paramArrayOfInt.length; )
/*     */     {
/* 175 */       int k = paramArrayOfByte[(i++)];
/*     */       int n;
/* 176 */       if (k >= 0)
/*     */       {
/* 178 */         k++;
/* 179 */         n = j + k;
/* 180 */         for (; j < n; i += 2) {
/* 181 */           paramArrayOfInt[j] = (((paramArrayOfByte[i] & 0xFF) << 8) + (paramArrayOfByte[(i + 1)] & 0xFF) & 0xFFFF);
/*     */ 
/* 180 */           j++;
/*     */         }
/*     */       }
/* 183 */       else if (k != -128)
/*     */       {
/* 186 */         int m = ((paramArrayOfByte[i] & 0xFF) << 8) + (paramArrayOfByte[(i + 1)] & 0xFF) & 0xFFFF;
/* 187 */         i += 2;
/*     */ 
/* 189 */         n = j - k + 1;
/* 190 */         for (; j < n; j++)
/* 191 */           paramArrayOfInt[j] = m;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void unpackbitsLimit(byte[] paramArrayOfByte1, int paramInt, byte[] paramArrayOfByte2)
/*     */     throws ArrayStoreException, ArrayIndexOutOfBoundsException
/*     */   {
/*  78 */     int i = 0;
/*  79 */     int j = 0;
/*  80 */     for (j = 0; j < paramArrayOfByte2.length; )
/*     */     {
/*  82 */       if (i == paramInt) {
/*     */         break;
/*     */       }
/*  85 */       int k = paramArrayOfByte1[(i++)];
/*  86 */       if (k >= 0)
/*     */       {
/*  88 */         k++;
/*  89 */         System.arraycopy(paramArrayOfByte1, i, paramArrayOfByte2, j, k);
/*  90 */         i += k;
/*  91 */         j += k;
/*     */       }
/*  93 */       else if (k != -128)
/*     */       {
/*  96 */         int m = paramArrayOfByte1[(i++)];
/*  97 */         int n = j - k + 1;
/*  98 */         for (; j < n; j++) {
/*  99 */           paramArrayOfByte2[j] = (byte) m;
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 105 */     while (j < paramArrayOfByte2.length)
/* 106 */       paramArrayOfByte2[(j++)] = 0;
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.util.Packbits
 * JD-Core Version:    0.6.2
 */