/*     */ package com.sun.jimi.core.util;
/*     */ 
/*     */ import java.util.Random;
/*     */ 
/*     */ public class FSDither
/*     */ {
/*     */   static final int FS_SCALE = 1024;
/*     */   int[] thisRerr_;
/*     */   int[] nextRerr_;
/*     */   int[] thisGerr_;
/*     */   int[] nextGerr_;
/*     */   int[] thisBerr_;
/*     */   int[] nextBerr_;
/*     */   boolean forward_;
/*     */   static final int MAXVAL = 255;
/*     */   byte[] rgbCMap_;
/*     */   int numColors_;
/*     */   int width_;
/*     */   InverseColorMap invCM_;
/*     */ 
/*     */   FSDither(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */   {
/*  66 */     this.rgbCMap_ = paramArrayOfByte;
/*  67 */     this.numColors_ = paramInt1;
/*  68 */     this.width_ = paramInt2;
/*  69 */     this.invCM_ = new InverseColorMap(this.rgbCMap_);
/*  70 */     init();
/*     */   }
/*     */ 
/*     */   void ditherRow(int[] paramArrayOfInt, byte[] paramArrayOfByte)
/*     */   {
/* 106 */     int i = this.nextRerr_.length;
/*     */     do { this.nextRerr_[i] = 0;
/*     */ 
/* 106 */       i--; } while (i >= 0);
/*     */ 
/* 108 */     System.arraycopy(this.nextRerr_, 0, this.nextGerr_, 0, this.nextRerr_.length);
/* 109 */     System.arraycopy(this.nextRerr_, 0, this.nextBerr_, 0, this.nextRerr_.length);
/*     */     int k;
/*     */     int j;
/* 112 */     if (this.forward_)
/*     */     {
/* 114 */       k = 0;
/* 115 */       i = 0;
/* 116 */       j = this.width_;
/*     */     }
/*     */     else
/*     */     {
/* 120 */       k = this.width_ - 1;
/* 121 */       i = this.width_ - 1;
/* 122 */       j = -1;
/*     */     }
/*     */ 
/*     */     while (true)
/*     */     {
/* 134 */       int n = paramArrayOfInt[i];
/*     */       int i1;
/*     */       int i3;
/*     */       int i5;
/*     */       int m;
/*     */       int i2;
/*     */       int i4;
/*     */       int i6;
/*     */       long l;
/* 135 */       if ((n & 0xFF000000) == 0) {
/* 136 */         if (this.forward_) {
/* 137 */           paramArrayOfByte[(k++)] = ((byte)this.numColors_);
/* 138 */           i++;
/* 139 */           if (i >= j) break; 
/*     */         }
/*     */         else
/*     */         {
/* 142 */           paramArrayOfByte[(k--)] = ((byte)this.numColors_);
/* 143 */           i--;
/* 144 */           if (i <= j) break; 
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 148 */         i1 = (n & 0xFF0000) >> 16;
/* 149 */         i3 = (n & 0xFF00) >> 8;
/* 150 */         i5 = n & 0xFF;
/* 151 */         i1 = ((n & 0xFF0000) >> 16) + this.thisRerr_[(i + 1)] / 1024;
/* 152 */         i3 = ((n & 0xFF00) >> 8) + this.thisGerr_[(i + 1)] / 1024;
/* 153 */         i5 = (n & 0xFF) + this.thisBerr_[(i + 1)] / 1024;
/*     */ 
/* 156 */         if (i1 < 0)
/* 157 */           i1 = 0;
/* 158 */         else if (i1 > 255)
/* 159 */           i1 = 255;
/* 160 */         if (i3 < 0)
/* 161 */           i3 = 0;
/* 162 */         else if (i3 > 255)
/* 163 */           i3 = 255;
/* 164 */         if (i5 < 0)
/* 165 */           i5 = 0;
/* 166 */         else if (i5 > 255) {
/* 167 */           i5 = 255;
/*     */         }
/*     */ 
/* 171 */         m = this.invCM_.getIndexNearest(i1, i3, i5);
/*     */ 
/* 174 */         i2 = this.rgbCMap_[(m * 4)] & 0xFF;
/* 175 */         i4 = this.rgbCMap_[(m * 4 + 1)] & 0xFF;
/* 176 */         i6 = this.rgbCMap_[(m * 4 + 2)] & 0xFF;
/*     */ 
/* 179 */         if (this.forward_)
/*     */         {
/* 181 */           paramArrayOfByte[(k++)] = ((byte)m);
/* 182 */           l = (i1 - i2) * 1024L;
/*     */           int tmp433_432 = (i + 2);
/*     */           int[] tmp433_427 = this.thisRerr_; tmp433_427[tmp433_432] = ((int)(tmp433_427[tmp433_432] + l * 7L / 16L));
/*     */           int tmp454_453 = i;
/*     */           int[] tmp454_450 = this.nextRerr_; tmp454_450[tmp454_453] = ((int)(tmp454_450[tmp454_453] + l * 3L / 16L));
/*     */           int tmp477_476 = (i + 1);
/*     */           int[] tmp477_471 = this.nextRerr_; tmp477_471[tmp477_476] = ((int)(tmp477_471[tmp477_476] + l * 5L / 16L));
/*     */           int tmp500_499 = (i + 2);
/*     */           int[] tmp500_494 = this.nextRerr_; tmp500_494[tmp500_499] = ((int)(tmp500_494[tmp500_499] + l / 16L));
/* 187 */           l = (i3 - i4) * 1024L;
/*     */           int tmp531_530 = (i + 2);
/*     */           int[] tmp531_525 = this.thisGerr_; tmp531_525[tmp531_530] = ((int)(tmp531_525[tmp531_530] + l * 7L / 16L));
/*     */           int tmp552_551 = i;
/*     */           int[] tmp552_548 = this.nextGerr_; tmp552_548[tmp552_551] = ((int)(tmp552_548[tmp552_551] + l * 3L / 16L));
/*     */           int tmp575_574 = (i + 1);
/*     */           int[] tmp575_569 = this.nextGerr_; tmp575_569[tmp575_574] = ((int)(tmp575_569[tmp575_574] + l * 5L / 16L));
/*     */           int tmp598_597 = (i + 2);
/*     */           int[] tmp598_592 = this.nextGerr_; tmp598_592[tmp598_597] = ((int)(tmp598_592[tmp598_597] + l / 16L));
/* 192 */           l = (i5 - i6) * 1024L;
/*     */           int tmp629_628 = (i + 2);
/*     */           int[] tmp629_623 = this.thisBerr_; tmp629_623[tmp629_628] = ((int)(tmp629_623[tmp629_628] + l * 7L / 16L));
/*     */           int tmp650_649 = i;
/*     */           int[] tmp650_646 = this.nextBerr_; tmp650_646[tmp650_649] = ((int)(tmp650_646[tmp650_649] + l * 3L / 16L));
/*     */           int tmp673_672 = (i + 1);
/*     */           int[] tmp673_667 = this.nextBerr_; tmp673_667[tmp673_672] = ((int)(tmp673_667[tmp673_672] + l * 5L / 16L));
/*     */           int tmp696_695 = (i + 2);
/*     */           int[] tmp696_690 = this.nextBerr_; tmp696_690[tmp696_695] = ((int)(tmp696_690[tmp696_695] + l / 16L));
/* 197 */           i++;
/* 198 */           if (i >= j)
/* 199 */             break;
/*     */         }
/*     */         else
/*     */         {
/* 203 */           paramArrayOfByte[(k--)] = ((byte)m);
/* 204 */           l = (i1 - i2) * 1024L;
/*     */           int tmp747_746 = i;
/*     */           int[] tmp747_743 = this.thisRerr_; tmp747_743[tmp747_746] = ((int)(tmp747_743[tmp747_746] + l * 7L / 16L));
/*     */           int tmp770_769 = (i + 2);
/*     */           int[] tmp770_764 = this.nextRerr_; tmp770_764[tmp770_769] = ((int)(tmp770_764[tmp770_769] + l * 3L / 16L));
/*     */           int tmp793_792 = (i + 1);
/*     */           int[] tmp793_787 = this.nextRerr_; tmp793_787[tmp793_792] = ((int)(tmp793_787[tmp793_792] + l * 5L / 16L));
/*     */           int tmp814_813 = i;
/*     */           int[] tmp814_810 = this.nextRerr_; tmp814_810[tmp814_813] = ((int)(tmp814_810[tmp814_813] + l / 16L));
/* 209 */           l = (i3 - i4) * 1024L;
/*     */           int tmp843_842 = i;
/*     */           int[] tmp843_839 = this.thisGerr_; tmp843_839[tmp843_842] = ((int)(tmp843_839[tmp843_842] + l * 7L / 16L));
/*     */           int tmp866_865 = (i + 2);
/*     */           int[] tmp866_860 = this.nextGerr_; tmp866_860[tmp866_865] = ((int)(tmp866_860[tmp866_865] + l * 3L / 16L));
/*     */           int tmp889_888 = (i + 1);
/*     */           int[] tmp889_883 = this.nextGerr_; tmp889_883[tmp889_888] = ((int)(tmp889_883[tmp889_888] + l * 5L / 16L));
/*     */           int tmp910_909 = i;
/*     */           int[] tmp910_906 = this.nextGerr_; tmp910_906[tmp910_909] = ((int)(tmp910_906[tmp910_909] + l / 16L));
/* 214 */           l = (i5 - i6) * 1024L;
/*     */           int tmp939_938 = i;
/*     */           int[] tmp939_935 = this.thisBerr_; tmp939_935[tmp939_938] = ((int)(tmp939_935[tmp939_938] + l * 7L / 16L));
/*     */           int tmp962_961 = (i + 2);
/*     */           int[] tmp962_956 = this.nextBerr_; tmp962_956[tmp962_961] = ((int)(tmp962_956[tmp962_961] + l * 3L / 16L));
/*     */           int tmp985_984 = (i + 1);
/*     */           int[] tmp985_979 = this.nextBerr_; tmp985_979[tmp985_984] = ((int)(tmp985_979[tmp985_984] + l * 5L / 16L));
/*     */           int tmp1006_1005 = i;
/*     */           int[] tmp1006_1002 = this.nextBerr_; tmp1006_1002[tmp1006_1005] = ((int)(tmp1006_1002[tmp1006_1005] + l / 16L));
/* 219 */           i--;
/* 220 */           if (i <= j) {
/* 221 */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 227 */     int[] arrayOfInt = this.thisRerr_;
/* 228 */     this.thisRerr_ = this.nextRerr_;
/* 229 */     this.nextRerr_ = arrayOfInt;
/* 230 */     arrayOfInt = this.thisGerr_;
/* 231 */     this.thisGerr_ = this.nextGerr_;
/* 232 */     this.nextGerr_ = arrayOfInt;
/* 233 */     arrayOfInt = this.thisBerr_;
/* 234 */     this.thisBerr_ = this.nextBerr_;
/* 235 */     this.nextBerr_ = arrayOfInt;
/* 236 */     this.forward_ ^= true;
/*     */   }
/*     */ 
/*     */   int findIdx(int paramInt1, int paramInt2, int paramInt3)
/*     */     throws ArrayIndexOutOfBoundsException
/*     */   {
/* 247 */     int n = 0;
/*     */ 
/* 249 */     long l1 = 2000000000L;
/* 250 */     int i = this.numColors_;
/*     */     do {
/* 252 */       int j = this.rgbCMap_[(i * 4)] & 0xFF;
/* 253 */       int k = this.rgbCMap_[(i * 4 + 1)] & 0xFF;
/* 254 */       int m = this.rgbCMap_[(i * 4 + 2)] & 0xFF;
/*     */ 
/* 256 */       long l2 = (paramInt1 - j) * (paramInt1 - j) + 
/* 257 */         (paramInt2 - k) * (paramInt2 - k) + 
/* 258 */         (paramInt3 - m) * (paramInt3 - m);
/* 259 */       if (l2 < l1)
/*     */       {
/* 261 */         n = i;
/* 262 */         l1 = l2;
/*     */       }
/* 250 */       i--; } while (i >= 0);
/*     */ 
/* 265 */     return n;
/*     */   }
/*     */ 
/*     */   void init()
/*     */   {
/*  77 */     this.thisRerr_ = new int[this.width_ + 2];
/*  78 */     this.nextRerr_ = new int[this.width_ + 2];
/*  79 */     this.thisGerr_ = new int[this.width_ + 2];
/*  80 */     this.nextGerr_ = new int[this.width_ + 2];
/*  81 */     this.thisBerr_ = new int[this.width_ + 2];
/*  82 */     this.nextBerr_ = new int[this.width_ + 2];
/*  83 */     Random localRandom = new Random();
/*     */ 
/*  86 */     for (int i = 0; i < this.width_ + 2; i++)
/*     */     {
/*  88 */       this.thisRerr_[i] = (Math.abs(localRandom.nextInt()) % 2048 - 1024);
/*  89 */       this.thisGerr_[i] = (Math.abs(localRandom.nextInt()) % 2048 - 1024);
/*  90 */       this.thisBerr_[i] = (Math.abs(localRandom.nextInt()) % 2048 - 1024);
/*     */     }
/*  92 */     this.forward_ = true;
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.util.FSDither
 * JD-Core Version:    0.6.2
 */