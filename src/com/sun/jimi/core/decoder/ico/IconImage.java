/*     */ package com.sun.jimi.core.decoder.ico;
/*     */ 
/*     */ import com.sun.jimi.core.util.LEDataInputStream;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public class IconImage
/*     */ {
/*     */   private static final boolean DEBUG = false;
/*     */   private BitmapInfoHeader header_;
/*     */   private RGBQuad[] colors_;
/*     */   private byte[] xorMap_;
/*     */   private byte[] andMap_;
/*     */ 
/*     */   public IconImage(LEDataInputStream paramLEDataInputStream)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/*  41 */       this.header_ = new BitmapInfoHeader(paramLEDataInputStream);
/*     */ 
/*  43 */       int i = (int)Math.pow(2.0D, getBitCount());
/*     */ 
/*  45 */       this.colors_ = new RGBQuad[i];
/*     */ 
/*  47 */       for (int j = 0; j < this.colors_.length; j++)
/*     */       {
/*  49 */         this.colors_[j] = new RGBQuad(paramLEDataInputStream);
/*     */       }
/*     */ 
/*  52 */       int k = (int)(this.header_.getWidth() * this.header_.getHeight() / 2L);
/*     */ 
/*  54 */       this.xorMap_ = new byte[k];
/*     */ 
/*  56 */       byte[] arrayOfByte = new byte[k / (8 / getBitCount())];
/*     */ 
/*  58 */       for (int m = 0; m < arrayOfByte.length; m++)
/*     */       {
/*  60 */         byte n = (byte)paramLEDataInputStream.readUnsignedByte();
/*  61 */         arrayOfByte[m] = n;
/*     */       }
/*     */ 
/*  64 */       int n = getBitCount();
/*     */       int i2;
int i1;
/*  69 */       switch (n)
/*     */       {
/*     */       case 1:
/*  72 */         i1 = 0; for (i2 = 0; i1 < arrayOfByte.length; i2 += 8)
/*     */         {
/*  74 */           this.xorMap_[i2] = ((byte)((arrayOfByte[i1] & 0x80) >>> 7));
/*  75 */           this.xorMap_[(i2 + 1)] = ((byte)((arrayOfByte[i1] & 0x40) >>> 6));
/*  76 */           this.xorMap_[(i2 + 2)] = ((byte)((arrayOfByte[i1] & 0x20) >>> 5));
/*  77 */           this.xorMap_[(i2 + 3)] = ((byte)((arrayOfByte[i1] & 0x10) >>> 4));
/*  78 */           this.xorMap_[(i2 + 4)] = ((byte)((arrayOfByte[i1] & 0x8) >>> 3));
/*  79 */           this.xorMap_[(i2 + 5)] = ((byte)((arrayOfByte[i1] & 0x4) >>> 2));
/*  80 */           this.xorMap_[(i2 + 6)] = ((byte)((arrayOfByte[i1] & 0x2) >>> 1));
/*  81 */           this.xorMap_[(i2 + 7)] = ((byte)(arrayOfByte[i1] & 0x1));
/*     */ 
/*  72 */           i1++;
/*     */         }
/*     */ 
/*  83 */         break;
/*     */       case 3:
/*  85 */         i1 = 0; for (i2 = 0; i1 < arrayOfByte.length; i2 += 4)
/*     */         {
/*  87 */           this.xorMap_[i2] = ((byte)((arrayOfByte[i1] & 0xC0) >>> 6));
/*  88 */           this.xorMap_[(i2 + 1)] = ((byte)((arrayOfByte[i1] & 0x30) >>> 4));
/*  89 */           this.xorMap_[(i2 + 2)] = ((byte)((arrayOfByte[i1] & 0xC) >>> 2));
/*  90 */           this.xorMap_[(i2 + 3)] = ((byte)(arrayOfByte[i1] & 0x3));
/*     */ 
/*  85 */           i1++;
/*     */         }
/*     */ 
/*  92 */         break;
/*     */       case 4:
/*  95 */         i1 = 0; for (i2 = 0; i1 < arrayOfByte.length; i2 += 2)
/*     */         {
/*  97 */           this.xorMap_[i2] = ((byte)((arrayOfByte[i1] & 0xF0) >>> 4));
/*  98 */           this.xorMap_[(i2 + 1)] = ((byte)(arrayOfByte[i1] & 0xF));
/*     */ 
/*  95 */           i1++;
/*     */         }
/*     */ 
/* 100 */         break;
/*     */       case 8:
/* 103 */         this.xorMap_ = arrayOfByte;
/* 104 */         break;
/*     */       case 2:
/*     */       case 5:
/*     */       case 6:
/* 108 */       case 7: } this.andMap_ = new byte[k / 8];
/*     */ 
/* 111 */       for (i1 = 0; i1 < this.andMap_.length; i1++)
/*     */       {
/* 113 */         i2 = (byte)paramLEDataInputStream.readUnsignedByte();
/* 114 */         this.andMap_[i1] = (byte) i2;
/*     */       }
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public byte[] getANDMap()
/*     */   {
/* 149 */     return this.andMap_;
/*     */   }
/*     */ 
/*     */   public int getBitCount()
/*     */   {
/* 124 */     return this.header_.getBitCount();
/*     */   }
/*     */ 
/*     */   public RGBQuad[] getColors()
/*     */   {
/* 139 */     return this.colors_;
/*     */   }
/*     */ 
/*     */   public int getHeight()
/*     */   {
/* 134 */     return (int)this.header_.getHeight() / 2;
/*     */   }
/*     */ 
/*     */   public int getWidth()
/*     */   {
/* 129 */     return (int)this.header_.getWidth();
/*     */   }
/*     */ 
/*     */   public byte[] getXORMap()
/*     */   {
/* 144 */     return this.xorMap_;
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.decoder.ico.IconImage
 * JD-Core Version:    0.6.2
 */