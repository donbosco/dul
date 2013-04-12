/*     */ package com.sun.jimi.core.filters;
/*     */ 
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.ImageConsumer;
/*     */ import java.awt.image.ImageFilter;
/*     */ import java.awt.image.ImageProducer;
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ public class Flip extends ImageFilterPlus
/*     */ {
/*     */   public static final int FLIP_NULL = 0;
/*     */   public static final int FLIP_LR = 1;
/*     */   public static final int FLIP_TB = 2;
/*     */   public static final int FLIP_XY = 3;
/*     */   public static final int FLIP_CW = 4;
/*     */   public static final int FLIP_CCW = 5;
/*     */   public static final int FLIP_R180 = 6;
/*     */   private int flipType;
/*     */   private int width;
/*     */   private int height;
/*     */   private int newWidth;
/*     */   private int newHeight;
/*     */ 
/*     */   public Flip(ImageProducer paramImageProducer, int paramInt)
/*     */   {
/*  73 */     super(paramImageProducer, true);
/*  74 */     this.flipType = paramInt;
/*     */   }
/*     */ 
/*     */   public static void main(String[] paramArrayOfString)
/*     */   {
/* 274 */     if (paramArrayOfString.length != 1)
/* 275 */       usage();
/* 276 */     int i = 0;
/* 277 */     if (paramArrayOfString[0].equalsIgnoreCase("-lr"))
/* 278 */       i = 1;
/* 279 */     else if (paramArrayOfString[0].equalsIgnoreCase("-tb"))
/* 280 */       i = 2;
/* 281 */     else if (paramArrayOfString[0].equalsIgnoreCase("-xy"))
/* 282 */       i = 3;
/* 283 */     else if (paramArrayOfString[0].equalsIgnoreCase("-cw"))
/* 284 */       i = 4;
/* 285 */     else if (paramArrayOfString[0].equalsIgnoreCase("-ccw"))
/* 286 */       i = 5;
/* 287 */     else if (paramArrayOfString[0].equalsIgnoreCase("-r180"))
/* 288 */       i = 6;
/*     */     else
/* 290 */       usage();
/* 291 */     Flip localFlip = new Flip(null, i);
/* 292 */     System.exit(
/* 293 */       ImageFilterPlus.filterStream(System.in, System.out, localFlip));
/*     */   }
/*     */ 
/*     */   public void setDimensions(int paramInt1, int paramInt2)
/*     */   {
/*  87 */     this.width = paramInt1;
/*  88 */     this.height = paramInt2;
/*  89 */     switch (this.flipType)
/*     */     {
/*     */     case 0:
/*     */     case 1:
/*     */     case 2:
/*     */     case 6:
/*  95 */       this.newWidth = paramInt1;
/*  96 */       this.newHeight = paramInt2;
/*  97 */       break;
/*     */     case 3:
/*     */     case 4:
/*     */     case 5:
/* 101 */       this.newWidth = paramInt2;
/* 102 */       this.newHeight = paramInt1;
/* 103 */       break;
/*     */     }
/* 105 */     this.consumer.setDimensions(this.newWidth, this.newHeight);
/*     */   }
/*     */ 
/*     */   public void setHints(int paramInt)
/*     */   {
/*  82 */     super.setHints(paramInt);
/*     */   }
/*     */ 
/*     */   public void setPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, ColorModel paramColorModel, byte[] paramArrayOfByte, int paramInt5, int paramInt6)
/*     */   {
/* 110 */     int i = paramInt1;
/* 111 */     int j = paramInt2;
/* 112 */     int k = paramInt3;
/* 113 */     int m = paramInt4;
/* 114 */     switch (this.flipType)
/*     */     {
/*     */     case 1:
/* 119 */       i = this.width - (paramInt1 + paramInt3);
/* 120 */       break;
/*     */     case 2:
/* 122 */       j = this.height - (paramInt2 + paramInt4);
/* 123 */       break;
/*     */     case 3:
/* 125 */       k = paramInt4;
/* 126 */       m = paramInt3;
/* 127 */       i = paramInt2;
/* 128 */       j = paramInt1;
/* 129 */       break;
/*     */     case 4:
/* 131 */       k = paramInt4;
/* 132 */       m = paramInt3;
/* 133 */       i = this.height - (paramInt2 + paramInt4);
/* 134 */       j = paramInt1;
/* 135 */       break;
/*     */     case 5:
/* 137 */       k = paramInt4;
/* 138 */       m = paramInt3;
/* 139 */       i = paramInt2;
/* 140 */       j = this.width - (paramInt1 + paramInt3);
/* 141 */       break;
/*     */     case 6:
/* 143 */       i = this.width - (paramInt1 + paramInt3);
/* 144 */       j = this.height - (paramInt2 + paramInt4);
/* 145 */       break;
/*     */     case 0:
/* 147 */     }byte[] arrayOfByte = new byte[k * m];
/* 148 */     for (int n = 0; n < paramInt4; n++)
/*     */     {
/* 150 */       for (int i1 = 0; i1 < paramInt3; i1++)
/*     */       {
/* 152 */         int i2 = n * paramInt6 + paramInt5 + i1;
/* 153 */         int i3 = n;
/* 154 */         int i4 = i1;
/* 155 */         switch (this.flipType)
/*     */         {
/*     */         case 1:
/* 160 */           i4 = paramInt3 - i1 - 1;
/* 161 */           break;
/*     */         case 2:
/* 163 */           i3 = paramInt4 - n - 1;
/* 164 */           break;
/*     */         case 3:
/* 166 */           i3 = i1;
/* 167 */           i4 = n;
/* 168 */           break;
/*     */         case 4:
/* 170 */           i3 = i1;
/* 171 */           i4 = paramInt4 - n - 1;
/* 172 */           break;
/*     */         case 5:
/* 174 */           i3 = paramInt3 - i1 - 1;
/* 175 */           i4 = n;
/* 176 */           break;
/*     */         case 6:
/* 178 */           i3 = paramInt4 - n - 1;
/* 179 */           i4 = paramInt3 - i1 - 1;
/* 180 */           break;
/*     */         case 0:
/* 182 */         }int i5 = i3 * k + i4;
/* 183 */         arrayOfByte[i5] = paramArrayOfByte[i2];
/*     */       }
/*     */     }
/* 186 */     this.consumer.setPixels(i, j, k, m, paramColorModel, arrayOfByte, 0, k);
/*     */   }
/*     */ 
/*     */   public void setPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, ColorModel paramColorModel, int[] paramArrayOfInt, int paramInt5, int paramInt6)
/*     */   {
/* 191 */     int i = paramInt1;
/* 192 */     int j = paramInt2;
/* 193 */     int k = paramInt3;
/* 194 */     int m = paramInt4;
/* 195 */     switch (this.flipType)
/*     */     {
/*     */     case 1:
/* 200 */       i = this.width - (paramInt1 + paramInt3);
/* 201 */       break;
/*     */     case 2:
/* 203 */       j = this.height - (paramInt2 + paramInt4);
/* 204 */       break;
/*     */     case 3:
/* 206 */       k = paramInt4;
/* 207 */       m = paramInt3;
/* 208 */       i = paramInt2;
/* 209 */       j = paramInt1;
/* 210 */       break;
/*     */     case 4:
/* 212 */       k = paramInt4;
/* 213 */       m = paramInt3;
/* 214 */       i = this.height - (paramInt2 + paramInt4);
/* 215 */       j = paramInt1;
/* 216 */       break;
/*     */     case 5:
/* 218 */       k = paramInt4;
/* 219 */       m = paramInt3;
/* 220 */       i = paramInt2;
/* 221 */       j = this.width - (paramInt1 + paramInt3);
/* 222 */       break;
/*     */     case 6:
/* 224 */       i = this.width - (paramInt1 + paramInt3);
/* 225 */       j = this.height - (paramInt2 + paramInt4);
/* 226 */       break;
/*     */     case 0:
/* 228 */     }int[] arrayOfInt = new int[k * m];
/* 229 */     for (int n = 0; n < paramInt4; n++)
/*     */     {
/* 231 */       for (int i1 = 0; i1 < paramInt3; i1++)
/*     */       {
/* 233 */         int i2 = n * paramInt6 + paramInt5 + i1;
/* 234 */         int i3 = n;
/* 235 */         int i4 = i1;
/* 236 */         switch (this.flipType)
/*     */         {
/*     */         case 1:
/* 241 */           i4 = paramInt3 - i1 - 1;
/* 242 */           break;
/*     */         case 2:
/* 244 */           i3 = paramInt4 - n - 1;
/* 245 */           break;
/*     */         case 3:
/* 247 */           i3 = i1;
/* 248 */           i4 = n;
/* 249 */           break;
/*     */         case 4:
/* 251 */           i3 = i1;
/* 252 */           i4 = paramInt4 - n - 1;
/* 253 */           break;
/*     */         case 5:
/* 255 */           i3 = paramInt3 - i1 - 1;
/* 256 */           i4 = n;
/* 257 */           break;
/*     */         case 6:
/* 259 */           i3 = paramInt4 - n - 1;
/* 260 */           i4 = paramInt3 - i1 - 1;
/* 261 */           break;
/*     */         case 0:
/* 263 */         }int i5 = i3 * k + i4;
/* 264 */         arrayOfInt[i5] = paramArrayOfInt[i2];
/*     */       }
/*     */     }
/* 267 */     this.consumer.setPixels(i, j, k, m, paramColorModel, arrayOfInt, 0, k);
/*     */   }
/*     */ 
/*     */   private static void usage()
/*     */   {
/* 298 */     System.err.println("usage: Flip -lr|-tb|-xy|-cw|-ccw|-r180");
/* 299 */     System.exit(1);
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.filters.Flip
 * JD-Core Version:    0.6.2
 */