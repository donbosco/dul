/*     */ package com.sun.jimi.core.decoder.pcx;
/*     */ 
/*     */ import com.sun.jimi.core.util.LEDataInputStream;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public class PCXImage
/*     */ {
/*     */   private PCXHeader header_;
/*     */   private byte[] imageData_;
/*     */   private int bytesPerLine_;
/*     */   private int bitCount_;
/*     */   private int planes_;
/* 112 */   private int count = 0;
/* 113 */   private int c = 0;
/*     */ 
/*     */   public PCXImage(LEDataInputStream paramLEDataInputStream, PCXHeader paramPCXHeader)
/*     */     throws IOException
/*     */   {
/*  35 */     this.header_ = paramPCXHeader;
/*     */ 
/*  37 */     this.planes_ = paramPCXHeader.getPlanes();
/*     */ 
/*  41 */     this.bytesPerLine_ = paramPCXHeader.getBytesPerLine();
/*     */ 
/*  43 */     this.bitCount_ = getBitCount();
/*     */ 
/*  45 */     this.imageData_ = new byte[getHeight() * getWidth()];
/*     */ 
/*  47 */     int i = 0;
/*     */ 
/*  49 */     switch (this.bitCount_) {
/*     */     case 1:
/*  51 */       if ((this.planes_ >= 1) && (this.planes_ <= 4))
/*     */       {
/*  53 */         get16ColorPCX(paramLEDataInputStream);
/*     */       }
/*  55 */       break;
/*     */     case 2:
/*     */     case 4:
/*  58 */       if (this.planes_ == 1)
/*     */       {
/*  60 */         get16ColorPCX(paramLEDataInputStream);
/*     */       }
/*     */ 
/*     */     case 8:
/*  64 */       switch (this.planes_) {
/*     */       case 1:
/*  66 */         get256ColorPCX(paramLEDataInputStream);
/*  67 */         break;
/*     */       case 3:
/*     */       case 4:
/*  70 */         getTrueColorPCX(paramLEDataInputStream);
/*  71 */       case 2: } break;
/*     */     case 3:
/*     */     case 5:
/*     */     case 6:
/*     */     case 7:
/*     */     }
/*     */   }
/*     */ 
/*  79 */   public void get16ColorPCX(LEDataInputStream paramLEDataInputStream) throws IOException { int i = getWidth();
/*  80 */     int j = getHeight();
/*     */ 
/*  83 */     int k = this.bytesPerLine_ * 8 / this.bitCount_;
/*     */ 
/*  85 */     if (i > k)
/*     */     {
/*  88 */       i = k;
/*     */     }
/*     */ 
/*  91 */     byte[] arrayOfByte1 = new byte[k];
/*  92 */     byte[] arrayOfByte2 = new byte[this.planes_ * this.bytesPerLine_];
/*     */ 
/*  94 */     for (int m = 0; m < j; m++)
/*     */     {
/*  96 */       getPCXRow(paramLEDataInputStream, arrayOfByte2, this.planes_ * this.bytesPerLine_);
/*     */ 
/*  98 */       if (this.planes_ == 1)
/*  99 */         pcxUnpackPixels(arrayOfByte1, arrayOfByte2);
/*     */       else {
/* 101 */         pcxPlanesToPixels(arrayOfByte1, arrayOfByte2);
/*     */       }
/* 103 */       for (int n = 0; n < i; n++)
/*     */       {
/* 105 */         this.imageData_[(m * getWidth() + n)] = arrayOfByte1[n];
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void get256ColorPCX(LEDataInputStream paramLEDataInputStream)
/*     */   {
/*     */     try
/*     */     {
/* 242 */       for (int j = 0; j < getHeight(); j++)
/*     */       {
/* 244 */         int i = 0;
/*     */ 
/* 246 */         while (i < this.bytesPerLine_)
/*     */         {
/* 248 */           int k = paramLEDataInputStream.readByte();
/* 249 */           int m = 0;
/*     */ 
/* 251 */           if ((k & 0xC0) == 192)
/*     */           {
/* 253 */             int n = paramLEDataInputStream.readByte();
/*     */ 
/* 255 */             m = k & 0x3F;
/*     */ 
/* 257 */             for (int i1 = 0; i1 < m; i1++)
/*     */             {
/* 259 */               this.imageData_[(i + j * this.bytesPerLine_)] = ((byte)n);
/* 260 */               i++;
/*     */             }
/*     */           }
/*     */           else
/*     */           {
/* 265 */             this.imageData_[(i + j * this.bytesPerLine_)] = ((byte)k);
/* 266 */             i++;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getBitCount()
/*     */   {
/* 293 */     return this.header_.getDepth();
/*     */   }
/*     */ 
/*     */   public int getBytesPerLine()
/*     */   {
/* 283 */     return this.bytesPerLine_;
/*     */   }
/*     */ 
/*     */   public int getHeight()
/*     */   {
/* 303 */     return this.header_.getHeight();
/*     */   }
/*     */ 
/*     */   public byte[] getImageData()
/*     */   {
/* 288 */     return this.imageData_;
/*     */   }
/*     */ 
/*     */   public void getPCXRow(LEDataInputStream paramLEDataInputStream, byte[] paramArrayOfByte, int paramInt)
/*     */   {
/*     */     try
/*     */     {
/* 119 */       this.count = 0;
/*     */ 
/* 121 */       int i = 0; int j = 0;
/*     */ 
/* 123 */       while (i < paramInt)
/*     */       {
/* 125 */         if (this.count > 0)
/*     */         {
/* 127 */           paramArrayOfByte[(i++)] = ((byte)this.c);
/* 128 */           this.count -= 1;
/*     */         }
/*     */         else
/*     */         {
/* 132 */           this.c = paramLEDataInputStream.readByte();
/* 133 */           if ((this.c & 0xC0) != 192)
/*     */           {
/* 135 */             paramArrayOfByte[(i++)] = ((byte)this.c);
/*     */           }
/*     */           else
/*     */           {
/* 139 */             this.count = (this.c & 0x3F);
/* 140 */             this.c = paramLEDataInputStream.readByte();
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public void getTrueColorPCX(LEDataInputStream paramLEDataInputStream)
/*     */   {
/*     */   }
/*     */ 
/*     */   public int getWidth()
/*     */   {
/* 298 */     return this.header_.getWidth();
/*     */   }
/*     */ 
/*     */   public void pcxPlanesToPixels(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
/*     */   {
/* 211 */     int k = 0;
/* 212 */     int m = 0;
/*     */ 
/* 214 */     for (int i = 0; i < this.planes_; i++)
/*     */     {
/* 218 */       k = 0;
/*     */ 
/* 220 */       int n = 1 << i;
/* 221 */       for (int j = 0; j < this.bytesPerLine_; j++)
/*     */       {
/* 223 */         int i1 = paramArrayOfByte2[(m++)];
int tmp53_51 = k;
/* 224 */         for (int i2 = 128; tmp53_51 != 0; k++)
/*     */         {
/* 226 */           if ((i1 & i2) > 0)
/*     */           {
/*     */             byte[] tmp53_50 = paramArrayOfByte1; tmp53_50[tmp53_51] = ((byte)(tmp53_50[tmp53_51] | n));
/*     */           }
/* 224 */           tmp53_51 >>= 1;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void pcxUnpackPixels(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
/*     */   {
/* 156 */     int j = this.bytesPerLine_;
/*     */ 
/* 159 */     int k = 0;
/* 160 */     int m = 0;
/*     */ 
/* 162 */     if (this.bitCount_ == 8)
/*     */     {
/*     */       do {
/* 165 */         paramArrayOfByte1[(k++)] = paramArrayOfByte2[(m++)];
/*     */ 
/* 164 */         j--; } while (j >= 0);
/*     */     }
/*     */     else
/*     */     {
/*     */       int i;
/* 168 */       if (this.bitCount_ == 4)
/*     */       {
/*     */         do
/*     */         {
/* 172 */           i = paramArrayOfByte2[(m++)];
/* 173 */           paramArrayOfByte1[(k++)] = ((byte)(i >> 4 & 0xF));
/* 174 */           paramArrayOfByte1[(k++)] = ((byte)(i & 0xF));
/*     */ 
/* 170 */           j--; } while (j >= 0);
/*     */       }
/* 177 */       else if (this.bitCount_ == 2)
/*     */       {
/*     */         do
/*     */         {
/* 181 */           i = paramArrayOfByte2[(m++)];
/* 182 */           paramArrayOfByte1[(k++)] = ((byte)(i >> 6 & 0x3));
/* 183 */           paramArrayOfByte1[(k++)] = ((byte)(i >> 4 & 0x3));
/* 184 */           paramArrayOfByte1[(k++)] = ((byte)(i >> 2 & 0x3));
/* 185 */           paramArrayOfByte1[(k++)] = ((byte)(i & 0x3));
/*     */ 
/* 179 */           j--; } while (j >= 0);
/*     */       }
/* 188 */       else if (this.bitCount_ == 1)
/*     */       {
/*     */         do
/*     */         {
/* 192 */           i = paramArrayOfByte2[(m++)];
/* 193 */           paramArrayOfByte1[(k++)] = ((byte)(i >> 7 & 0x1));
/* 194 */           paramArrayOfByte1[(k++)] = ((byte)(i >> 6 & 0x1));
/* 195 */           paramArrayOfByte1[(k++)] = ((byte)(i >> 5 & 0x1));
/* 196 */           paramArrayOfByte1[(k++)] = ((byte)(i >> 4 & 0x1));
/* 197 */           paramArrayOfByte1[(k++)] = ((byte)(i >> 3 & 0x1));
/* 198 */           paramArrayOfByte1[(k++)] = ((byte)(i >> 2 & 0x1));
/* 199 */           paramArrayOfByte1[(k++)] = ((byte)(i >> 1 & 0x1));
/* 200 */           paramArrayOfByte1[(k++)] = ((byte)(i & 0x1));
/*     */ 
/* 190 */           j--; } while (j >= 0);
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.decoder.pcx.PCXImage
 * JD-Core Version:    0.6.2
 */