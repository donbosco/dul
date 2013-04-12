/*     */ package com.sun.jimi.core.util;
/*     */ 
/*     */ import com.sun.jimi.core.JimiException;
/*     */ import com.sun.jimi.core.JimiImage;
/*     */ import com.sun.jimi.core.JimiImageFactory;
/*     */ import com.sun.jimi.core.JimiImageHandle;
/*     */ import com.sun.jimi.core.JimiLicenseInformation;
/*     */ import com.sun.jimi.core.MemoryJimiImageFactory;
/*     */ import com.sun.jimi.core.raster.JimiRasterImage;
/*     */ import java.awt.Image;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.DirectColorModel;
/*     */ import java.awt.image.ImageProducer;
/*     */ import java.awt.image.IndexColorModel;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class JimiUtil
/*     */ {
/*     */   private static final String nonFileName = "X#jimi#X";
/*     */   public static final boolean TRUE = true;
/* 209 */   static byte[] expansionTable = new byte[2048];
/* 210 */   static int[] intExpansionTable = new int[2048];
/*     */ 
/*     */   static
/*     */   {
/* 215 */     int i = 0;
/* 216 */     for (int j = 0; j < 256; j++)
/*     */     {
/* 218 */       expansionTable[(i++)] = (byte) ((j & 0x80) == 0 ? 0 : 1);
/* 219 */       expansionTable[(i++)] = (byte) ((j & 0x40) == 0 ? 0 : 1);
/* 220 */       expansionTable[(i++)] = (byte) ((j & 0x20) == 0 ? 0 : 1);
/* 221 */       expansionTable[(i++)] = (byte) ((j & 0x10) == 0 ? 0 : 1);
/* 222 */       expansionTable[(i++)] = (byte) ((j & 0x8) == 0 ? 0 : 1);
/* 223 */       expansionTable[(i++)] = (byte) ((j & 0x4) == 0 ? 0 : 1);
/* 224 */       expansionTable[(i++)] = (byte) ((j & 0x2) == 0 ? 0 : 1);
/* 225 */       expansionTable[(i++)] = (byte) ((j & 0x1) == 0 ? 0 : 1);
/*     */     }
/*     */ 
/* 232 */     i = 0;
/* 233 */     for (int j = 0; j < 256; j++)
/*     */     {
/* 235 */       intExpansionTable[(i++)] = ((j & 0x80) == 0 ? -16777216 : -1);
/* 236 */       intExpansionTable[(i++)] = ((j & 0x40) == 0 ? -16777216 : -1);
/* 237 */       intExpansionTable[(i++)] = ((j & 0x20) == 0 ? -16777216 : -1);
/* 238 */       intExpansionTable[(i++)] = ((j & 0x10) == 0 ? -16777216 : -1);
/* 239 */       intExpansionTable[(i++)] = ((j & 0x8) == 0 ? -16777216 : -1);
/* 240 */       intExpansionTable[(i++)] = ((j & 0x4) == 0 ? -16777216 : -1);
/* 241 */       intExpansionTable[(i++)] = ((j & 0x2) == 0 ? -16777216 : -1);
/* 242 */       intExpansionTable[(i++)] = ((j & 0x1) == 0 ? -16777216 : -1);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static JimiRasterImage asJimiRasterImage(JimiImage paramJimiImage)
/*     */   {
/* 670 */     if ((paramJimiImage instanceof JimiRasterImage)) {
/* 671 */       return (JimiRasterImage)paramJimiImage;
/*     */     }
/*     */ 
/* 674 */     if ((paramJimiImage instanceof JimiImageHandle)) {
/*     */       try {
/* 676 */         return asJimiRasterImage(((JimiImageHandle)paramJimiImage).getWrappedJimiImage());
/*     */       }
/*     */       catch (JimiException localJimiException) {
/* 679 */         return null;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 684 */     return null;
/*     */   }
/*     */ 
/*     */   public static byte countBitsSet(int paramInt)
/*     */   {
/* 103 */     byte b = 0;
/* 104 */     int i = 1;
/*     */ 
/* 106 */     for (int j = 0; j < 32; j++)
/*     */     {
/* 108 */       if ((paramInt & i) != 0)
/* 109 */         b = (byte)(b + 1);
/* 110 */       i <<= 1;
/*     */     }
/* 112 */     return b;
/*     */   }
/*     */ 
/*     */   public static void expandOneBitPixels(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt)
/*     */   {
/* 315 */     int i = paramInt % 8;
/* 316 */     int j = paramInt / 8;
/* 317 */     for (int k = 0; k < j; k++)
/*     */     {
/* 319 */       int m = (paramArrayOfByte1[k] & 0xFF) * 8;
/* 320 */       System.arraycopy(expansionTable, m, paramArrayOfByte2, k * 8, 8);
/*     */     }
/*     */ 
/* 323 */     if (i != 0)
/*     */     {
/* 325 */       System.arraycopy(expansionTable, (paramArrayOfByte1[j] & 0xFF) * 8, paramArrayOfByte2, j * 8, i);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void expandOneBitPixels(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 252 */     int i = paramInt2 % 8;
/* 253 */     paramInt2 /= 8;
/*     */ 
/* 255 */     if (i != 0)
/*     */     {
/* 257 */       i = 8 - i;
/*     */ 
/* 259 */       System.arraycopy(expansionTable, ((paramArrayOfByte1[(paramInt2++)] & 0xFF) << 3) + 8 - i, 
/* 260 */         paramArrayOfByte2, paramInt3, i);
/* 261 */       paramInt3 += i;
/*     */     }
/*     */ 
/* 264 */     int j = paramInt1 % 8;
/* 265 */     int k = paramInt2 + paramInt1 / 8;
/*     */ 
/* 268 */     for (int m = paramInt2; m < k; m++)
/*     */     {
/* 270 */       System.arraycopy(expansionTable, (paramArrayOfByte1[m] & 0xFF) << 3, paramArrayOfByte2, paramInt3, 8);
/* 271 */       paramInt3 += 8;
/*     */     }
/* 273 */     if (j != 0)
/*     */     {
/* 275 */       System.arraycopy(expansionTable, (paramArrayOfByte1[(k - 1)] & 0xFF) << 3, paramArrayOfByte2, paramInt3, j);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void expandOneBitPixelsToBW(byte[] paramArrayOfByte, int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 285 */     int i = paramInt2 % 8;
/* 286 */     paramInt2 /= 8;
/*     */ 
/* 288 */     if (i != 0)
/*     */     {
/* 290 */       i = 8 - i;
/*     */ 
/* 292 */       System.arraycopy(intExpansionTable, (paramArrayOfByte[(paramInt2++)] << 3) + 8 - i, 
/* 293 */         paramArrayOfInt, paramInt3, i);
/* 294 */       paramInt3 += i;
/*     */     }
/*     */ 
/* 297 */     int j = paramInt1 % 8;
/* 298 */     int k = paramInt2 + paramInt1 / 8;
/*     */ 
/* 301 */     for (int m = paramInt2; m < k; m++)
/*     */     {
/* 303 */       System.arraycopy(intExpansionTable, (paramArrayOfByte[m] & 0xFF) << 3, paramArrayOfInt, paramInt3, 8);
/* 304 */       paramInt3 += 8;
/*     */     }
/* 306 */     if (j != 0)
/*     */     {
/* 308 */       System.arraycopy(intExpansionTable, (paramArrayOfByte[k] & 0xFF) << 3, paramArrayOfInt, paramInt3, j);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void expandPixels(int paramInt1, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt2)
/*     */   {
/* 144 */     if (paramInt1 == 1)
/*     */     {
/* 146 */       expandOneBitPixels(paramArrayOfByte1, paramArrayOfByte2, paramInt2);
/* 147 */       return;
/*     */     }
/*     */ 
/* 154 */     int n = 0;
/* 155 */     int i1 = 1;
/* 156 */     int i2 = 0;
/* 157 */     int i3 = 0;
/* 158 */     int i4 = 0;
/* 159 */     int i5 = 0;
/*     */ 
/* 162 */     switch (paramInt1)
/*     */     {
/*     */     case 1:
/* 165 */       i5 = 128;
/* 166 */       i1 = 1;
/* 167 */       n = 8;
/* 168 */       i2 = 7;
/* 169 */       i4 = 1;
/* 170 */       break;
/*     */     case 2:
/* 172 */       i5 = 192;
/* 173 */       i1 = 2;
/* 174 */       n = 4;
/* 175 */       i2 = 6;
/* 176 */       i4 = 2;
/* 177 */       break;
/*     */     case 4:
/* 179 */       i5 = 240;
/* 180 */       i1 = 4;
/* 181 */       n = 2;
/* 182 */       i2 = 4;
/* 183 */       i4 = 4;
/* 184 */       break;
/*     */     case 3:
/*     */     default:
/* 186 */       throw new RuntimeException("support only expand for 1, 2, 4");
/*     */     }
/*     */ 
/* 190 */     int i = 0;
/* 191 */     for (int j = 0; j < paramArrayOfByte2.length; )
/*     */     {
/* 193 */       int i6 = i5;
/* 194 */       i3 = i2;
/* 195 */       int m = paramArrayOfByte1[i];
/* 196 */       for (int k = 0; (k < n) && (j < paramArrayOfByte2.length); j++)
/*     */       {
/* 199 */         paramArrayOfByte2[j] = ((byte)((m & i6) >>> i3 & 0xFF));
/* 200 */         i6 = (byte)((i6 & 0xFF) >>> i1);
/* 201 */         i3 -= i4;
/*     */ 
/* 196 */         k++;
/*     */       }
/*     */ 
/* 203 */       i++;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static boolean flagSet(int paramInt1, int paramInt2)
/*     */   {
/* 690 */     return (paramInt1 & paramInt2) != 0;
/*     */   }
/*     */ 
/*     */   public static boolean flagsSet(int paramInt1, int paramInt2)
/*     */   {
/* 695 */     return (paramInt1 & paramInt2) == paramInt2;
/*     */   }
/*     */ 
/*     */   public static byte[] getChannelWidths(ColorModel paramColorModel)
/*     */   {
/*  75 */     byte[] arrayOfByte = new byte[8];
/*     */ 
/*  77 */     if ((paramColorModel instanceof DirectColorModel))
/*     */     {
/*  79 */       DirectColorModel localDirectColorModel = (DirectColorModel)paramColorModel;
/*  80 */       arrayOfByte[0] = countBitsSet(localDirectColorModel.getAlphaMask());
/*  81 */       arrayOfByte[1] = countBitsSet(localDirectColorModel.getRedMask());
/*  82 */       arrayOfByte[2] = countBitsSet(localDirectColorModel.getGreenMask());
/*  83 */       arrayOfByte[3] = countBitsSet(localDirectColorModel.getBlueMask());
/*     */     }
/*  85 */     else if ((paramColorModel instanceof IndexColorModel))
/*     */     {
/*  87 */       arrayOfByte[0] = 8;
/*  88 */       arrayOfByte[1] = 8;
/*  89 */       arrayOfByte[2] = 8;
/*  90 */       arrayOfByte[3] = 8;
/*     */     }
/*  92 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   public static Image getErrorImage()
/*     */   {
/* 120 */     return Toolkit.getDefaultToolkit().createImage(getErrorImageProducer());
/*     */   }
/*     */ 
/*     */   public static ImageProducer getErrorImageProducer()
/*     */   {
/* 129 */     return new ErrorImageProducer();
/*     */   }
/*     */ 
/*     */   public static boolean isCompatibleWithJavaVersion(int paramInt1, int paramInt2)
/*     */   {
/*     */     try
/*     */     {
/* 720 */       String str1 = System.getProperty("java.version");
/* 721 */       int i = Integer.parseInt(str1.substring(0, str1.indexOf('.')));
/* 722 */       String str2 = str1.substring(str1.indexOf(".") + 1);
/* 723 */       if (str2.indexOf('.') != -1) {
/* 724 */         str2 = str2.substring(0, str2.indexOf('.'));
/*     */       }
/* 726 */       int j = Integer.parseInt(str2);
/*     */ 
/* 728 */       return (i > paramInt1) || ((i == paramInt1) && (j >= paramInt2));
/*     */     }
/*     */     catch (Exception localException) {
/*     */     }
/* 732 */     return false;
/*     */   }
/*     */ 
/*     */   public static boolean isRGBDefault(ColorModel paramColorModel)
/*     */   {
/*  44 */     if ((paramColorModel instanceof DirectColorModel))
/*     */     {
/*  46 */       DirectColorModel localDirectColorModel1 = (DirectColorModel)paramColorModel;
/*  47 */       DirectColorModel localDirectColorModel2 = (DirectColorModel)ColorModel.getRGBdefault();
/*     */ 
/*  49 */       int i = localDirectColorModel2.getAlphaMask();
/*  50 */       int j = localDirectColorModel2.getRedMask();
/*  51 */       int k = localDirectColorModel2.getGreenMask();
/*  52 */       int m = localDirectColorModel2.getBlueMask();
/*     */ 
/*  54 */       int n = localDirectColorModel1.getAlphaMask();
/*  55 */       int i1 = localDirectColorModel1.getRedMask();
/*  56 */       int i2 = localDirectColorModel1.getGreenMask();
/*  57 */       int i3 = localDirectColorModel1.getBlueMask();
/*     */ 
/*  59 */       return (i == n) && (j == i1) && 
/*  60 */         (k == i2) && (m == i3);
/*     */     }
/*  62 */     return false;
/*     */   }
/*     */ 
/*     */   public static void packOneBitPixels(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 346 */     int i = paramInt1;
/*     */ 
/* 348 */     paramInt2 += paramInt3 / 8;
/* 349 */     paramInt3 %= 8;
/*     */ 
/* 352 */     int j = paramInt3 == 0 ? paramInt4 / 8 : (paramInt4 - (8 - paramInt3)) / 8;
/*     */ 
/* 354 */     int k = 0;
/*     */ 
/* 358 */     int m = paramInt3 == 0 ? 0 : 1;
/*     */ 
/* 360 */     int n = paramInt2 + (paramInt3 == 0 ? 0 : 1);
/*     */ 
/* 364 */     int i1 = paramInt4 < 8 - paramInt3 ? paramInt4 : 8 - paramInt3;
/*     */     int i3;
/* 365 */     if (paramInt3 != 0)
/*     */     {
/* 368 */       int i2 = 7 - paramInt3;
/*     */ 
/* 370 */       for (i3 = 0; i3 < i1; i3++)
/*     */       {
/* 372 */         if (paramArrayOfByte1[(i++)] == 0)
/*     */         {
/*     */           int tmp125_124 = paramInt2;
/*     */           byte[] tmp125_123 = paramArrayOfByte2; tmp125_123[tmp125_124] = ((byte)(tmp125_123[tmp125_124] & (1 << i2 - i3 ^ 0xFFFFFFFF)));
/*     */         }
/*     */         else
/*     */         {
/*     */           int tmp144_143 = paramInt2;
/*     */           byte[] tmp144_142 = paramArrayOfByte2; tmp144_142[tmp144_143] = ((byte)(tmp144_142[tmp144_143] | 1 << i2 - i3));
/*     */         }
/*     */       }
/*     */     }
/* 379 */     for (int i2 = m; i2 < j; i2++)
/*     */     {
/* 381 */       k = 0;
/*     */ 
/* 383 */       if (paramArrayOfByte1[(i++)] != 0) {
/* 384 */         k += 128;
/*     */       }
/* 386 */       if (paramArrayOfByte1[(i++)] != 0) {
/* 387 */         k += 64;
/*     */       }
/* 389 */       if (paramArrayOfByte1[(i++)] != 0) {
/* 390 */         k += 32;
/*     */       }
/* 392 */       if (paramArrayOfByte1[(i++)] != 0) {
/* 393 */         k += 16;
/*     */       }
/* 395 */       if (paramArrayOfByte1[(i++)] != 0) {
/* 396 */         k += 8;
/*     */       }
/* 398 */       if (paramArrayOfByte1[(i++)] != 0) {
/* 399 */         k += 4;
/*     */       }
/* 401 */       if (paramArrayOfByte1[(i++)] != 0) {
/* 402 */         k += 2;
/*     */       }
/* 404 */       if (paramArrayOfByte1[(i++)] != 0) {
/* 405 */         k++;
/*     */       }
/*     */ 
/* 408 */       paramArrayOfByte2[(n + i2)] = ((byte)k);
/*     */     }
/*     */ 
/* 413 */     if (paramInt3 == 0)
/* 414 */       i3 = paramInt4 % 8;
/*     */     else {
/* 416 */       i3 = paramInt4 - (8 - paramInt3) % 8;
/*     */     }
/* 418 */     i3 %= 8;
/*     */ 
/* 421 */     int i4 = paramInt2 + j;
/*     */ 
/* 424 */     if (i3 > 0)
/*     */     {
/* 426 */       for (int i5 = 0; i5 < i3; i5++)
/*     */       {
/* 428 */         if (paramArrayOfByte1[(i++)] == 0)
/*     */         {
/*     */           int tmp368_366 = i4;
/*     */           byte[] tmp368_365 = paramArrayOfByte2; tmp368_365[tmp368_366] = ((byte)(tmp368_365[tmp368_366] & (1 << 7 - i5 ^ 0xFFFFFFFF)));
/*     */         }
/*     */         else
/*     */         {
/*     */           int tmp388_386 = i4;
/*     */           byte[] tmp388_385 = paramArrayOfByte2; tmp388_385[tmp388_386] = ((byte)(tmp388_385[tmp388_386] | 1 << 7 - i5));
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static int packPixels(int paramInt, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
/*     */   {
/* 498 */     int k = (1 << paramInt) - 1;
/*     */ 
/* 504 */     int i = paramArrayOfByte1.length;
/*     */     int m;
/*     */     int n;
/*     */     int j;
/*     */     int i1;
/*     */     int i2;
/*     */     int i3;
/* 505 */     switch (paramInt)
/*     */     {
/*     */     case 1:
/* 508 */       m = i % 8;
/* 509 */       n = i / 8;
/* 510 */       j = n + 1;
/*     */ 
/* 513 */       i1 = i;
/* 514 */       i2 = 0;
/* 515 */       switch (m)
/*     */       {
/*     */       case 7:
/* 518 */         i2 |= (paramArrayOfByte1[(--i1)] & k) << 1;
/*     */       case 6:
/* 521 */         i2 |= (paramArrayOfByte1[(--i1)] & k) << 2;
/*     */       case 5:
/* 524 */         i2 |= (paramArrayOfByte1[(--i1)] & k) << 3;
/*     */       case 4:
/* 527 */         i2 |= (paramArrayOfByte1[(--i1)] & k) << 4;
/*     */       case 3:
/* 530 */         i2 |= (paramArrayOfByte1[(--i1)] & k) << 5;
/*     */       case 2:
/* 533 */         i2 |= (paramArrayOfByte1[(--i1)] & k) << 6;
/*     */       case 1:
/* 536 */         i2 |= (paramArrayOfByte1[(--i1)] & k) << 7;
/* 537 */         paramArrayOfByte2[n] = ((byte)i2);
/* 538 */         break;
/*     */       case 0:
/* 540 */         j--;
/* 541 */         break;
/*     */       }
/*     */ 
/* 545 */       i1 = i - m;
/* 546 */       i3 = n;
/*     */       do {
/* 548 */         paramArrayOfByte2[i3] = ((byte)
/* 555 */           (paramArrayOfByte1[(--i1)] & k | 
/* 550 */           (paramArrayOfByte1[(--i1)] & k) << 1 | 
/* 551 */           (paramArrayOfByte1[(--i1)] & k) << 2 | 
/* 552 */           (paramArrayOfByte1[(--i1)] & k) << 3 | 
/* 553 */           (paramArrayOfByte1[(--i1)] & k) << 4 | 
/* 554 */           (paramArrayOfByte1[(--i1)] & k) << 5 | 
/* 555 */           (paramArrayOfByte1[(--i1)] & k) << 6 | 
/* 556 */           (paramArrayOfByte1[(--i1)] & k) << 7));
/*     */ 
/* 546 */         i3--; } while (i3 >= 0);
/*     */ 
/* 558 */       return j;
/*     */     case 2:
/* 561 */       m = i % 4;
/* 562 */       n = i / 4;
/* 563 */       j = n + 1;
/*     */ 
/* 566 */       i1 = i;
/* 567 */       i2 = 0;
/* 568 */       switch (m)
/*     */       {
/*     */       case 3:
/* 571 */         i2 |= (paramArrayOfByte1[(--i1)] & k) << 2;
/*     */       case 2:
/* 574 */         i2 |= (paramArrayOfByte1[(--i1)] & k) << 4;
/*     */       case 1:
/* 577 */         i2 |= (paramArrayOfByte1[(--i1)] & k) << 6;
/* 578 */         paramArrayOfByte2[n] = ((byte)i2);
/* 579 */         break;
/*     */       case 0:
/* 581 */         j--;
/* 582 */         break;
/*     */       }
/*     */ 
/* 586 */       i1 = i - m;
/* 587 */       i3 = n;
/*     */       do {
/* 589 */         paramArrayOfByte2[i3] = ((byte)
/* 592 */           (paramArrayOfByte1[(--i1)] & k | 
/* 591 */           (paramArrayOfByte1[(--i1)] & k) << 2 | 
/* 592 */           (paramArrayOfByte1[(--i1)] & k) << 4 | 
/* 593 */           (paramArrayOfByte1[(--i1)] & k) << 6));
/*     */ 
/* 587 */         i3--; } while (i3 >= 0);
/*     */ 
/* 595 */       return j;
/*     */     case 4:
/* 598 */       m = i % 2;
/* 599 */       n = i / 2;
/* 600 */       j = n + 1;
/*     */ 
/* 603 */       i1 = i;
/* 604 */       i2 = 0;
/* 605 */       switch (m)
/*     */       {
/*     */       case 1:
/* 608 */         i2 |= (paramArrayOfByte1[(--i1)] & k) << 4;
/* 609 */         paramArrayOfByte2[n] = ((byte)i2);
/* 610 */         break;
/*     */       case 0:
/* 612 */         j--;
/* 613 */         break;
/*     */       }
/*     */ 
/* 617 */       i1 = i - m;
/* 618 */       i3 = n;
/*     */       do {
/* 620 */         paramArrayOfByte2[i3] = ((byte)
/* 621 */           (paramArrayOfByte1[(--i1)] & k | 
/* 622 */           (paramArrayOfByte1[(--i1)] & k) << 4));
/*     */ 
/* 618 */         i3--; } while (i3 >= 0);
/*     */ 
/* 624 */       return j;
/*     */     case 8:
/* 627 */       System.arraycopy(paramArrayOfByte1, 0, paramArrayOfByte2, 0, i);
/* 628 */       return i;
/*     */     case 3:
/*     */     case 5:
/*     */     case 6:
/* 631 */     case 7: } throw new IllegalArgumentException("depth must be 1 2 4 or 8, not " + paramInt);
/*     */   }
/*     */ 
/*     */   public static void packPixels(int[] paramArrayOfInt, int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3)
/*     */   {
/* 453 */     int i = paramInt1;
/* 454 */     int j = paramInt3 / 8;
/* 455 */     for (int k = 0; k < j; k++) {
/* 456 */       int m = 0;
/*     */ 
/* 458 */       if (paramArrayOfInt[(i++)] == -1) m |= 128;
/* 459 */       if (paramArrayOfInt[(i++)] == -1) m |= 64;
/* 460 */       if (paramArrayOfInt[(i++)] == -1) m |= 32;
/* 461 */       if (paramArrayOfInt[(i++)] == -1) m |= 16;
/* 462 */       if (paramArrayOfInt[(i++)] == -1) m |= 8;
/* 463 */       if (paramArrayOfInt[(i++)] == -1) m |= 4;
/* 464 */       if (paramArrayOfInt[(i++)] == -1) m |= 2;
/* 465 */       if (paramArrayOfInt[(i++)] == -1) m |= 1;
/*     */ 
/* 467 */       paramArrayOfByte[(paramInt2 + k)] = ((byte)m);
/*     */     }
/* 469 */     int m = paramInt3 % 8;
/* 470 */     int n = 7;
/* 471 */     int i1 = 255;
/* 472 */     while (m-- > 0) {
/* 473 */       if (paramArrayOfInt[(i++)] != -1) i1 &= (1 << n-- ^ 0xFFFFFFFF);
/*     */     }
/* 475 */     paramArrayOfByte[(paramInt2 + j)] = ((byte)i1);
/*     */   }
/*     */ 
/*     */   public static void pixelDepthChange(int paramInt1, byte[] paramArrayOfByte, int paramInt2)
/*     */   {
/* 650 */     if (paramInt2 <= paramInt1) {
/* 651 */       throw new IllegalArgumentException("pixelDepth Must < newPixelSize");
/*     */     }
/*     */ 
/* 654 */     int j = paramInt2 - paramInt1;
/* 655 */     int k = (1 << paramInt2) - 1;
/* 656 */     int m = (1 << paramInt1) - 1;
/* 657 */     int i = paramArrayOfByte.length;
/*     */     do { paramArrayOfByte[i] = ((byte)(paramArrayOfByte[i] * k / m));
/*     */ 
/* 657 */       i--; } while (i >= 0);
/*     */   }
/*     */ 
/*     */   public static void runCommands(Vector paramVector)
/*     */   {
/* 741 */     Enumeration localEnumeration = paramVector.elements();
/* 742 */     while (localEnumeration.hasMoreElements())
/* 743 */       ((Runnable)localEnumeration.nextElement()).run();
/*     */   }
/*     */ 
/*     */   public static JimiImageFactory stripStamping(JimiImageFactory paramJimiImageFactory)
/*     */   {
/* 700 */     if (JimiLicenseInformation.isCrippled()) {
/* 701 */       return paramJimiImageFactory;
/*     */     }
/* 703 */     JimiImageFactory localJimiImageFactory = paramJimiImageFactory;
/*     */ 
/* 706 */     while ((localJimiImageFactory instanceof JimiImageFactoryProxy)) {
/* 707 */       localJimiImageFactory = ((JimiImageFactoryProxy)localJimiImageFactory).getProxiedFactory();
/*     */     }
/* 709 */     if (((localJimiImageFactory instanceof MemoryJimiImageFactory)) || 
/* 711 */       (localJimiImageFactory.getClass().getName().equals("com.sun.jimi.core.VMemStampedJimiImageFactory"))) {
/* 712 */       paramJimiImageFactory = localJimiImageFactory;
/*     */     }
/* 714 */     return paramJimiImageFactory;
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.util.JimiUtil
 * JD-Core Version:    0.6.2
 */