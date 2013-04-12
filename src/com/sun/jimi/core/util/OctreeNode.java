/*     */ package com.sun.jimi.core.util;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ public class OctreeNode
/*     */ {
/*     */   static boolean debug;
/*  42 */   static int[] levCounts = new int[9];
/*     */ 
/*  57 */   public static int numNodes = 0;
/*     */   boolean leaf;
/*     */   boolean marked;
/*     */   long sumR;
/*     */   long sumG;
/*     */   long sumB;
/*     */   int count;
/*     */   OctreeNode r0g0b0;
/*     */   OctreeNode r0g0b1;
/*     */   OctreeNode r0g1b0;
/*     */   OctreeNode r0g1b1;
/*     */   OctreeNode r1g0b0;
/*     */   OctreeNode r1g0b1;
/*     */   OctreeNode r1g1b0;
/*     */   OctreeNode r1g1b1;
/*     */   int childCount;
/*     */   int level;
/*     */   int redMask;
/*     */   int greenMask;
/*     */   int blueMask;
/*     */   int pIndex;
/*     */   OctreeCallback oc;
/*     */ 
/*     */   static
/*     */   {
/*  43 */     for (int i = 0; i < 9; i++)
/*  44 */       levCounts[i] = 0;
/*     */   }
/*     */ 
/*     */   OctreeNode(OctreeCallback paramOctreeCallback)
/*     */   {
/* 181 */     this(paramOctreeCallback, 0);
/*     */   }
/*     */ 
/*     */   OctreeNode(OctreeCallback paramOctreeCallback, int paramInt)
/*     */   {
/* 192 */     numNodes += 1;
/* 193 */     setFields(paramOctreeCallback, paramInt);
/*     */   }
/*     */ 
/*     */   int collapseOctree()
/*     */   {
/* 482 */     int i = 0;
/*     */ 
/* 484 */     if (this.childCount == 0) {
/* 485 */       return 0;
/*     */     }
/* 487 */     if (this.r0g0b0 != null)
/*     */     {
/* 489 */       if (this.r0g0b0.leaf)
/* 490 */         i++;
/* 491 */       i += this.r0g0b0.collapseOctree();
/* 492 */       this.oc.cacheONode(this.r0g0b0);
/*     */     }
/* 494 */     if (this.r0g0b1 != null)
/*     */     {
/* 496 */       if (this.r0g0b1.leaf)
/* 497 */         i++;
/* 498 */       i += this.r0g0b1.collapseOctree();
/* 499 */       this.oc.cacheONode(this.r0g0b1);
/*     */     }
/* 501 */     if (this.r0g1b0 != null)
/*     */     {
/* 503 */       if (this.r0g1b0.leaf)
/* 504 */         i++;
/* 505 */       i += this.r0g1b0.collapseOctree();
/* 506 */       this.oc.cacheONode(this.r0g1b0);
/*     */     }
/* 508 */     if (this.r0g1b1 != null)
/*     */     {
/* 510 */       if (this.r0g1b1.leaf)
/* 511 */         i++;
/* 512 */       i += this.r0g1b1.collapseOctree();
/* 513 */       this.oc.cacheONode(this.r0g1b1);
/*     */     }
/*     */ 
/* 516 */     if (this.r1g0b0 != null)
/*     */     {
/* 518 */       if (this.r1g0b0.leaf)
/* 519 */         i++;
/* 520 */       i += this.r1g0b0.collapseOctree();
/* 521 */       this.oc.cacheONode(this.r1g0b0);
/*     */     }
/* 523 */     if (this.r1g0b1 != null)
/*     */     {
/* 525 */       if (this.r1g0b1.leaf)
/* 526 */         i++;
/* 527 */       i += this.r1g0b1.collapseOctree();
/* 528 */       this.oc.cacheONode(this.r1g0b1);
/*     */     }
/* 530 */     if (this.r1g1b0 != null)
/*     */     {
/* 532 */       if (this.r1g1b0.leaf)
/* 533 */         i++;
/* 534 */       i += this.r1g1b0.collapseOctree();
/* 535 */       this.oc.cacheONode(this.r1g1b0);
/*     */     }
/* 537 */     if (this.r1g1b1 != null)
/*     */     {
/* 539 */       if (this.r1g1b1.leaf)
/* 540 */         i++;
/* 541 */       i += this.r1g1b1.collapseOctree();
/* 542 */       this.oc.cacheONode(this.r1g1b1);
/*     */     }
/*     */ 
/* 545 */     this.r0g0b0 = null;
/* 546 */     this.r0g0b1 = null;
/* 547 */     this.r0g1b0 = null;
/* 548 */     this.r0g1b1 = null;
/* 549 */     this.r1g0b0 = null;
/* 550 */     this.r1g0b1 = null;
/* 551 */     this.r1g1b0 = null;
/* 552 */     this.r1g1b1 = null;
/* 553 */     this.childCount = 0;
/* 554 */     return i;
/*     */   }
/*     */ 
/*     */   int createPalette(byte[] paramArrayOfByte, int paramInt)
/*     */   {
/* 583 */     int i = 0;
/*     */ 
/* 586 */     if (this.leaf)
/*     */     {
/* 588 */       paramArrayOfByte[paramInt] = ((byte)(int)(this.sumR / this.count));
/* 589 */       paramArrayOfByte[(paramInt + 1)] = ((byte)(int)(this.sumG / this.count));
/* 590 */       paramArrayOfByte[(paramInt + 2)] = ((byte)(int)(this.sumB / this.count));
/* 591 */       this.pIndex = paramInt;
/* 592 */       i += 3;
/*     */     }
/*     */ 
/* 595 */     if (this.childCount > 0)
/*     */     {
/* 597 */       if (this.r0g0b0 != null)
/* 598 */         i += this.r0g0b0.createPalette(paramArrayOfByte, paramInt + i);
/* 599 */       if (this.r0g0b1 != null)
/* 600 */         i += this.r0g0b1.createPalette(paramArrayOfByte, paramInt + i);
/* 601 */       if (this.r0g1b0 != null)
/* 602 */         i += this.r0g1b0.createPalette(paramArrayOfByte, paramInt + i);
/* 603 */       if (this.r0g1b1 != null)
/* 604 */         i += this.r0g1b1.createPalette(paramArrayOfByte, paramInt + i);
/* 605 */       if (this.r1g0b0 != null)
/* 606 */         i += this.r1g0b0.createPalette(paramArrayOfByte, paramInt + i);
/* 607 */       if (this.r1g0b1 != null)
/* 608 */         i += this.r1g0b1.createPalette(paramArrayOfByte, paramInt + i);
/* 609 */       if (this.r1g1b0 != null)
/* 610 */         i += this.r1g1b0.createPalette(paramArrayOfByte, paramInt + i);
/* 611 */       if (this.r1g1b1 != null)
/* 612 */         i += this.r1g1b1.createPalette(paramArrayOfByte, paramInt + i);
/*     */     }
/* 614 */     return i;
/*     */   }
/*     */ 
/*     */   public void dump(String paramString)
/*     */   {
/* 155 */     System.out.println(paramString + "[" + this.level + "]" + toString());
/* 156 */     if (this.r1g1b1 != null)
/* 157 */       this.r1g1b1.dump(paramString + "111 ");
/* 158 */     if (this.r1g1b0 != null)
/* 159 */       this.r1g1b0.dump(paramString + "110 ");
/* 160 */     if (this.r1g0b1 != null)
/* 161 */       this.r1g0b1.dump(paramString + "101 ");
/* 162 */     if (this.r1g0b0 != null)
/* 163 */       this.r1g0b0.dump(paramString + "100 ");
/* 164 */     if (this.r0g1b1 != null)
/* 165 */       this.r0g1b1.dump(paramString + "011 ");
/* 166 */     if (this.r0g1b0 != null)
/* 167 */       this.r0g1b0.dump(paramString + "010 ");
/* 168 */     if (this.r0g0b1 != null)
/* 169 */       this.r0g0b1.dump(paramString + "001 ");
/* 170 */     if (this.r0g0b0 != null)
/* 171 */       this.r0g0b0.dump(paramString + "000 ");
/*     */   }
/*     */ 
/*     */   static void dumpLevCounts()
/*     */   {
/*  50 */     String str = "";
/*  51 */     for (int i = 0; i < 9; i++)
/*  52 */       str = str + " " + levCounts[i] + " ";
/*  53 */     System.out.println("levCounts " + str);
/*     */   }
/*     */ 
/*     */   OctreeNode findChild(int paramInt)
/*     */   {
/* 240 */     if (this.childCount == 0)
/*     */     {
/* 242 */       return this;
/*     */     }
/*     */ 
/* 246 */     int i = paramInt & this.redMask;
/* 247 */     int j = paramInt & this.greenMask;
/* 248 */     int k = paramInt & this.blueMask;
/*     */ 
/* 250 */     if (i == 0)
/*     */     {
/* 252 */       if (j == 0) {
/* 253 */         if (k == 0) {
/* 254 */           if (this.r0g0b0 != null)
/*     */           {
/* 256 */             return this.r0g0b0.findChild(paramInt);
/*     */           }
/*     */ 
/* 260 */           return this;
/*     */         }
/*     */ 
/* 263 */         if (this.r0g0b1 != null)
/*     */         {
/* 265 */           return this.r0g0b1.findChild(paramInt);
/*     */         }
/*     */ 
/* 269 */         return this;
/*     */       }
/*     */ 
/* 272 */       if (k == 0) {
/* 273 */         if (this.r0g1b0 != null) {
/* 274 */           return this.r0g1b0.findChild(paramInt);
/*     */         }
/* 276 */         return this;
/*     */       }
/* 278 */       if (this.r0g1b1 != null) {
/* 279 */         return this.r0g1b1.findChild(paramInt);
/*     */       }
/* 281 */       return this;
/*     */     }
/*     */ 
/* 285 */     if (j == 0) {
/* 286 */       if (k == 0) {
/* 287 */         if (this.r1g0b0 != null) {
/* 288 */           return this.r1g0b0.findChild(paramInt);
/*     */         }
/* 290 */         return this;
/*     */       }
/* 292 */       if (this.r1g0b1 != null) {
/* 293 */         return this.r1g0b1.findChild(paramInt);
/*     */       }
/* 295 */       return this;
/*     */     }
/* 297 */     if (k == 0) {
/* 298 */       if (this.r1g1b0 != null) {
/* 299 */         return this.r1g1b0.findChild(paramInt);
/*     */       }
/* 301 */       return this;
/*     */     }
/* 303 */     if (this.r1g1b1 != null) {
/* 304 */       return this.r1g1b1.findChild(paramInt);
/*     */     }
/* 306 */     return this;
/*     */   }
/*     */ 
/*     */   final int getColor()
/*     */   {
/* 563 */     int i = (int)(this.sumR / this.count);
/* 564 */     int j = (int)(this.sumG / this.count);
/* 565 */     int k = (int)(this.sumB / this.count);
/* 566 */     return (i << 16) + (j << 8) + k;
/*     */   }
/*     */ 
/*     */   int insertColor(int paramInt1, int paramInt2)
/*     */   {
/* 321 */     int i = 0;
/* 322 */     int j = this.level + 1;
/*     */ 
/* 324 */     this.count += 1;
/* 325 */     this.sumR += ((paramInt1 & 0xFF0000) >> 16);
/* 326 */     this.sumG += ((paramInt1 & 0xFF00) >> 8);
/* 327 */     this.sumB += (paramInt1 & 0xFF);
/*     */ 
/* 330 */     if (!this.leaf)
/*     */     {
/* 333 */       int k = paramInt1 & this.redMask;
/* 334 */       int m = paramInt1 & this.greenMask;
/* 335 */       int n = paramInt1 & this.blueMask;
/* 336 */       if (k == 0)
/*     */       {
/* 338 */         if (m == 0)
/*     */         {
/* 340 */           if (n == 0)
/*     */           {
/* 342 */             if (this.r0g0b0 == null)
/*     */             {
/* 344 */               this.r0g0b0 = this.oc.getONode(this.oc, j);
/* 345 */               if (j >= paramInt2)
/*     */               {
/* 347 */                 this.r0g0b0.leaf = true;
/* 348 */                 i++;
/*     */               }
/* 350 */               this.childCount += 1;
/*     */             }
/* 352 */             i += this.r0g0b0.insertColor(paramInt1, paramInt2);
/*     */           }
/*     */           else
/*     */           {
/* 356 */             if (this.r0g0b1 == null)
/*     */             {
/* 358 */               this.r0g0b1 = this.oc.getONode(this.oc, j);
/* 359 */               if (j >= paramInt2)
/*     */               {
/* 361 */                 this.r0g0b1.leaf = true;
/* 362 */                 i++;
/*     */               }
/* 364 */               this.childCount += 1;
/*     */             }
/* 366 */             i += this.r0g0b1.insertColor(paramInt1, paramInt2);
/*     */           }
/*     */ 
/*     */         }
/* 371 */         else if (n == 0)
/*     */         {
/* 373 */           if (this.r0g1b0 == null)
/*     */           {
/* 375 */             this.r0g1b0 = this.oc.getONode(this.oc, j);
/* 376 */             if (j >= paramInt2)
/*     */             {
/* 378 */               this.r0g1b0.leaf = true;
/* 379 */               i++;
/*     */             }
/* 381 */             this.childCount += 1;
/*     */           }
/* 383 */           i += this.r0g1b0.insertColor(paramInt1, paramInt2);
/*     */         }
/*     */         else
/*     */         {
/* 387 */           if (this.r0g1b1 == null)
/*     */           {
/* 389 */             this.r0g1b1 = this.oc.getONode(this.oc, j);
/* 390 */             if (j >= paramInt2)
/*     */             {
/* 392 */               this.r0g1b1.leaf = true;
/* 393 */               i++;
/*     */             }
/* 395 */             this.childCount += 1;
/*     */           }
/* 397 */           i += this.r0g1b1.insertColor(paramInt1, paramInt2);
/*     */         }
/*     */ 
/*     */       }
/* 403 */       else if (m == 0)
/*     */       {
/* 405 */         if (n == 0)
/*     */         {
/* 407 */           if (this.r1g0b0 == null)
/*     */           {
/* 409 */             this.r1g0b0 = this.oc.getONode(this.oc, j);
/* 410 */             if (j >= paramInt2)
/*     */             {
/* 412 */               this.r1g0b0.leaf = true;
/* 413 */               i++;
/*     */             }
/* 415 */             this.childCount += 1;
/*     */           }
/* 417 */           i += this.r1g0b0.insertColor(paramInt1, paramInt2);
/*     */         }
/*     */         else
/*     */         {
/* 421 */           if (this.r1g0b1 == null)
/*     */           {
/* 423 */             this.r1g0b1 = this.oc.getONode(this.oc, j);
/* 424 */             if (j >= paramInt2)
/*     */             {
/* 426 */               this.r1g0b1.leaf = true;
/* 427 */               i++;
/*     */             }
/* 429 */             this.childCount += 1;
/*     */           }
/* 431 */           i += this.r1g0b1.insertColor(paramInt1, paramInt2);
/*     */         }
/*     */ 
/*     */       }
/* 436 */       else if (n == 0)
/*     */       {
/* 438 */         if (this.r1g1b0 == null)
/*     */         {
/* 440 */           this.r1g1b0 = this.oc.getONode(this.oc, j);
/* 441 */           if (j >= paramInt2)
/*     */           {
/* 443 */             this.r1g1b0.leaf = true;
/* 444 */             i++;
/*     */           }
/* 446 */           this.childCount += 1;
/*     */         }
/* 448 */         i += this.r1g1b0.insertColor(paramInt1, paramInt2);
/*     */       }
/*     */       else
/*     */       {
/* 452 */         if (this.r1g1b1 == null)
/*     */         {
/* 454 */           this.r1g1b1 = this.oc.getONode(this.oc, j);
/* 455 */           if (j >= paramInt2)
/*     */           {
/* 457 */             this.r1g1b1.leaf = true;
/* 458 */             i++;
/*     */           }
/* 460 */           this.childCount += 1;
/*     */         }
/* 462 */         i += this.r1g1b1.insertColor(paramInt1, paramInt2);
/*     */       }
/*     */ 
/* 466 */       if ((this.childCount > 1) && (!this.marked)) {
/* 467 */         this.oc.markReducible(this);
/*     */       }
/*     */     }
/* 470 */     return i;
/*     */   }
/*     */ 
/*     */   final int quantizeColor(int paramInt)
/*     */   {
/* 626 */     if (this.leaf) {
/* 627 */       return this.pIndex;
/*     */     }
/*     */ 
/* 630 */     OctreeNode localOctreeNode = findChild(paramInt);
/* 631 */     if (localOctreeNode.leaf == false)
/*     */     {
/* 633 */       debug = true;
/* 634 */       localOctreeNode = findChild(paramInt);
/* 635 */       debug = false;
/*     */ 
/* 637 */       if (this.level == 0) {
/* 638 */         dump("");
/*     */       }
/*     */     }
/* 641 */     return findChild(paramInt).pIndex;
/*     */   }
/*     */ 
/*     */   void setFields(OctreeCallback paramOctreeCallback, int paramInt)
/*     */   {
/* 203 */     this.count = 0;
/* 204 */     this.sumR = 0L;
/* 205 */     this.sumG = 0L;
/* 206 */     this.sumB = 0L;
/* 207 */     this.level = paramInt;
/* 208 */     this.pIndex = -1;
/* 209 */     this.childCount = 0;
/* 210 */     this.leaf = false;
/* 211 */     this.marked = false;
/*     */ 
/* 213 */     this.r0g0b0 = (this.r0g0b1 = this.r0g1b0 = this.r0g1b1 = 
/* 214 */       this.r1g0b0 = this.r1g0b1 = this.r1g1b0 = this.r1g1b1 = null);
/*     */ 
/* 219 */     if (paramInt != 8)
/*     */     {
/* 221 */       this.blueMask = (128 >> paramInt);
/* 222 */       this.greenMask = (this.blueMask << 8);
/* 223 */       this.redMask = (this.greenMask << 8);
/*     */     }
/*     */ 
/* 226 */     this.oc = paramOctreeCallback;
/*     */ 
/* 229 */     levCounts[paramInt] += 1;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 125 */     String str = "l " + this.leaf;
/* 126 */     str = str + " m " + this.marked;
/* 127 */     str = str + " count " + this.count;
/* 128 */     str = str + " childCount " + this.childCount;
/* 129 */     str = str + " level " + this.level;
/* 130 */     if (this.r0g0b0 != null)
/* 131 */       str = str + " r0g0b0 ";
/* 132 */     if (this.r0g0b1 != null)
/* 133 */       str = str + " r0g0b1 ";
/* 134 */     if (this.r0g1b0 != null)
/* 135 */       str = str + " r0g1b0 ";
/* 136 */     if (this.r0g1b1 != null)
/* 137 */       str = str + " r0g1b1 ";
/* 138 */     if (this.r1g0b0 != null)
/* 139 */       str = str + " r1g0b0 ";
/* 140 */     if (this.r1g0b1 != null)
/* 141 */       str = str + " r1g0b1 ";
/* 142 */     if (this.r1g1b0 != null)
/* 143 */       str = str + " r1g1b0 ";
/* 144 */     if (this.r1g1b1 != null)
/* 145 */       str = str + " r1g1b1 ";
/* 146 */     return str;
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.util.OctreeNode
 * JD-Core Version:    0.6.2
 */