/*     */ package com.sun.jimi.core.decoder.sunraster;
/*     */ 
/*     */ import com.sun.jimi.core.JimiException;
/*     */ import com.sun.jimi.core.compat.AdaptiveRasterImage;
/*     */ import com.sun.jimi.core.compat.JimiDecoderBase;
/*     */ import com.sun.jimi.core.util.ProgressMonitorSupport;
/*     */ import java.awt.image.DirectColorModel;
/*     */ import java.awt.image.IndexColorModel;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ 
/*     */ public class SunRasterDecoder extends JimiDecoderBase
/*     */ {
/*     */   DataInputStream lin;
/*     */   AdaptiveRasterImage sinkImage;
/*     */   SunRasterHeader header;
/*     */   SunRasterColorMap colormap;
/*     */   int state;
/*     */   private static final int RLE_ESCAPE = 128;
/*     */ 
/*     */   protected void RLEDecodeImage()
/*     */     throws IOException, JimiException
/*     */   {
/* 418 */     RLEInputStream localRLEInputStream = new RLEInputStream(this.lin);
/* 419 */     byte[] arrayOfByte = new byte[this.header.Width];
/* 420 */     int i = arrayOfByte.length % 2 == 0 ? 0 : 1;
/* 421 */     int j = this.header.Height;
/* 422 */     for (int k = 0; k < j; k++)
/*     */     {
/* 424 */       localRLEInputStream.read(arrayOfByte);
/*     */ 
/* 426 */       if (i != 0)
/* 427 */         localRLEInputStream.read();
/* 428 */       flushOut(arrayOfByte, this.sinkImage, k);
/*     */     }
/*     */   }
/*     */ 
/*     */   private boolean decodeImage()
/*     */     throws IOException, JimiException
/*     */   {
	/*     */     try
	/*     */     {
	/* 197 */       if (this.header.Type == 2)
	/*     */       {
	/* 199 */         RLEDecodeImage();
	/*     */       }
	/*     */       else
	/*     */       {
	/* 203 */         int i = this.header.Width * this.header.Depth;
	/* 204 */         if (i % 16 != 0)
	/*     */         {
	/* 206 */           i += 16 - i % 16;
	/*     */         }
	/*     */ 
	/* 209 */         i /= 8;
	/*     */        
	/*     */         int j;
	/* 211 */         if (this.header.Depth == 8)
	/*     */         {
	/* 213 */           byte[] localObject = new byte[this.header.Width];
	/* 214 */           for (j = 0; j < this.header.Height; j++)
	/*     */           {
	/* 216 */             this.lin.readFully((byte[])localObject, 0, this.header.Width);
	/* 217 */             this.lin.skipBytes(i - localObject.length);
	/* 218 */             this.sinkImage.setChannel(0, j, (byte[])localObject);
	/* 219 */             setProgress(j * 100 / this.header.Height);
	/*     */           }
	/*     */         }
	/*     */         else
	/*     */         {
	/*     */           int k;
	/*     */           int m;
	/* 222 */           if (this.header.Depth == 4)
	/*     */           {
	/* 224 */             byte[] localObject = new byte[this.header.Width];
	/* 225 */             for (j = 0; j < this.header.Height; j++)
	/*     */             {
	/* 227 */               for (k = 0; k < localObject.length; k += 2)
	/*     */               {
	/* 229 */                 m = this.lin.readByte();
	/* 230 */                 localObject[k] = ((byte)(m >> 4));
	/* 231 */                 localObject[(k + 1)] = ((byte)(m & 0xF));
	/*     */               }
	/*     */ 
	/* 234 */               this.lin.skipBytes(i - localObject.length / 2);
	/* 235 */               this.sinkImage.setChannel(0, j, (byte[])localObject);
	/* 236 */               setProgress(j * 100 / this.header.Height);
	/*     */             }
	/*     */ 
	/*     */           }
	/* 240 */           else if (this.header.Depth >= 8)
	/*     */           {
	/* 290 */             if (this.header.Depth == 16)
	/*     */             {
	/* 292 */               int[] localObject = new int[this.header.Width];
	/* 293 */               for (j = 0; j < this.header.Height; j++)
	/*     */               {
	/* 295 */                 for (k = 0; k < localObject.length; k++)
	/*     */                 {
	/* 297 */                   localObject[k] = this.lin.readInt();
	/*     */                 }
	/*     */ 
	/* 301 */                 this.lin.skipBytes(i - localObject.length * 2);
	/* 302 */                 this.sinkImage.setChannel(j, (int[])localObject);
	/* 303 */                 setProgress(j * 100 / this.header.Height);
	/*     */               }
	/*     */ 
	/*     */             }
	/* 307 */             else if (this.header.Depth >= 16)
	/*     */             {
	/*     */               int n;
	/*     */               int i1;
	/* 315 */               if (this.header.Depth == 24)
	/*     */               {
	/* 317 */                 int[] localObject = new int[this.header.Width];
	/* 318 */                 for (j = 0; j < this.header.Height; j++)
	/*     */                 {
	/* 320 */                   if (this.header.Type == 3)
	/*     */                   {
	/* 323 */                     for (k = 0; k < localObject.length; k++)
	/*     */                     {
	/* 325 */                       m = this.lin.readUnsignedByte();
	/* 326 */                       n = this.lin.readUnsignedByte();
	/* 327 */                       i1 = this.lin.readUnsignedByte();
	/*     */ 
	/* 329 */                       localObject[k] = (m << 16 | n << 8 | i1);
	/*     */                     }
	/*     */ 
	/*     */                   }
	/*     */                   else
	/*     */                   {
	/* 336 */                     for (k = 0; k < localObject.length; k++)
	/*     */                     {
	/* 338 */                       m = this.lin.readUnsignedByte();
	/* 339 */                       n = this.lin.readUnsignedByte();
	/* 340 */                       i1 = this.lin.readUnsignedByte();
	/*     */ 
	/* 342 */                       localObject[k] = (i1 << 16 | n << 8 | m);
	/*     */                     }
	/*     */ 
	/*     */                   }
	/*     */ 
	/* 347 */                   this.lin.skipBytes(i - localObject.length * 3);
	/* 348 */                   this.sinkImage.setChannel(j, (int[])localObject);
	/* 349 */                   setProgress(j * 100 / this.header.Height);
	/*     */                 }
	/*     */ 
	/*     */               }
	/* 353 */               else if (this.header.Depth == 32)
	/*     */               {
	/* 355 */                 int[] localObject = new int[this.header.Width];
	/* 356 */                 for (j = 0; j < this.header.Height; j++)
	/*     */                 {
	/* 358 */                   if (this.header.Type == 3)
	/*     */                   {
	/* 360 */                     for (k = 0; k < localObject.length; k++)
	/*     */                     {
	/* 362 */                       this.lin.readByte();
	/* 363 */                       m = this.lin.readUnsignedByte();
	/* 364 */                       n = this.lin.readUnsignedByte();
	/* 365 */                       i1 = this.lin.readUnsignedByte();
	/*     */ 
	/* 367 */                       localObject[k] = (m << 16 | n << 8 | i1);
	/*     */                     }
	/*     */ 
	/*     */                   }
	/*     */                   else
	/*     */                   {
	/* 374 */                     for (k = 0; k < localObject.length; k++)
	/*     */                     {
	/* 377 */                       this.lin.readByte();
	/* 378 */                       m = this.lin.readUnsignedByte();
	/* 379 */                       n = this.lin.readUnsignedByte();
	/* 380 */                       i1 = this.lin.readUnsignedByte();
	/*     */ 
	/* 382 */                       localObject[k] = (i1 << 16 | n << 8 | m);
	/*     */                     }
	/*     */ 
	/*     */                   }
	/*     */ 
	/* 387 */                   this.lin.skipBytes(i - localObject.length * 4);
	/*     */ 
	/* 389 */                   this.sinkImage.setChannel(j, (int[])localObject);
	/* 390 */                   setProgress(j * 100 / this.header.Height);
	/*     */                 }
	/*     */               }
	/*     */             }
	/*     */ 
	/*     */           }
	/*     */ 
	/*     */         }
	/*     */ 
	/*     */       }
	/*     */ 
	/*     */     }
	/*     */     catch (JimiException localJimiException)
	/*     */     {
	/* 404 */       throw localJimiException;
	/*     */     }
	/*     */     catch (IOException localIOException) {
	/* 407 */       throw localIOException;
	/*     */     }
	/*     */ 
	/* 411 */     this.state |= 4;
	/* 412 */     return true;
	/*     */   }
/*     */ 
/*     */   public boolean driveDecoder()
/*     */     throws JimiException
/*     */   {
/*     */     try
/*     */     {
/*  76 */       this.sinkImage = getJimiImage();
/*     */ 
/*  78 */       if (this.state == 0)
/*     */       {
/*  80 */         this.header = new SunRasterHeader(this.lin);
/*  81 */         if (this.header.ColorMapType != 0)
/*     */         {
/*  83 */           this.colormap = new SunRasterColorMap(this.lin, this.header);
/*     */         }
/*  85 */         else if (this.header.ColorMapType == 2)
/*     */         {
/*  87 */           throw new JimiException("Unsupported Format Subtype");
/*     */         }
/*     */ 
/*  91 */         initJimiImage();
/*  92 */         this.state |= 2;
/*     */ 
/*  94 */         return true;
/*     */       }
/*     */ 
/* 100 */       if (decodeImage())
/*     */       {
/* 102 */         this.state |= 4;
/*     */       }
/*     */ 
/* 109 */       this.sinkImage.addFullCoverage();
/*     */     }
/*     */     catch (JimiException localJimiException)
/*     */     {
/* 115 */       this.state |= 1;
/* 116 */       throw localJimiException;
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/* 120 */       this.state |= 1;
/* 121 */       throw new JimiException("IOException while reading file : " + localIOException.toString());
/*     */     }
/*     */ 
/* 125 */     return false;
/*     */   }
/*     */ 
/*     */   private static void flushOut(byte[] paramArrayOfByte, AdaptiveRasterImage paramAdaptiveRasterImage, int paramInt)
/*     */     throws JimiException
/*     */   {
/* 434 */     paramAdaptiveRasterImage.setChannel(0, paramInt, paramArrayOfByte);
/*     */   }
/*     */ 
/*     */   public void freeDecoder()
/*     */     throws JimiException
/*     */   {
/*  65 */     this.lin = null;
/*  66 */     this.sinkImage = null;
/*  67 */     this.header = null;
/*  68 */     this.colormap = null;
/*     */   }
/*     */ 
/*     */   public AdaptiveRasterImage getJimiImage()
/*     */   {
/* 137 */     return this.sinkImage;
/*     */   }
/*     */ 
/*     */   public int getState()
/*     */   {
/* 131 */     return this.state;
/*     */   }
/*     */ 
/*     */   public void initDecoder(InputStream paramInputStream, AdaptiveRasterImage paramAdaptiveRasterImage)
/*     */     throws JimiException
/*     */   {
/*  49 */     if ((paramInputStream == null) || (paramAdaptiveRasterImage == null))
/*     */     {
/*  51 */       throw new IllegalArgumentException("Null values to constructor.");
/*     */     }
/*     */ 
/*  55 */     this.lin = new DataInputStream(paramInputStream);
/*  56 */     this.sinkImage = paramAdaptiveRasterImage;
/*     */ 
/*  58 */     this.state = 0;
/*     */   }
/*     */ 
/*     */   private void initJimiImage()
/*     */     throws JimiException
/*     */   {
/*     */     try
/*     */     {
/* 145 */       this.sinkImage.setSize(this.header.Width, this.header.Height);
/*     */ 
/* 148 */       if (this.colormap == null)
/*     */       {
/* 156 */         int i = 0;
/* 157 */         for (int j = this.header.Depth / 3; j > 0; j--)
/*     */         {
/* 159 */           i <<= 1;
/* 160 */           i++;
/*     */         }
/*     */ 
/* 163 */         this.sinkImage.setColorModel(new DirectColorModel(24, 16711680, 65280, 255));
/*     */       }
/* 167 */       else if (this.colormap.RGBType)
/*     */       {
/* 169 */         this.sinkImage.setColorModel(new IndexColorModel(8, 
/* 170 */           this.colormap.tableLength, this.colormap.r, this.colormap.g, this.colormap.b));
/*     */       }
/*     */       else
/*     */       {
/* 174 */         this.sinkImage.setColorModel(new IndexColorModel(8, 
/* 175 */           this.colormap.tableLength, this.colormap.raw, 0, false));
/*     */       }
/*     */ 
/* 182 */       this.sinkImage.setPixels();
/*     */     }
/*     */     catch (JimiException localJimiException)
/*     */     {
/* 186 */       throw localJimiException;
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean usesChanneledData()
/*     */   {
/* 440 */     return true;
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.decoder.sunraster.SunRasterDecoder
 * JD-Core Version:    0.6.2
 */