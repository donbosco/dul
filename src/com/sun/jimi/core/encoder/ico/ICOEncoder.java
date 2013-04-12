/*     */ package com.sun.jimi.core.encoder.ico;
/*     */ 
/*     */ import com.sun.jimi.core.JimiException;
/*     */ import com.sun.jimi.core.compat.AdaptiveRasterImage;
/*     */ import com.sun.jimi.core.compat.JimiEncoderBase;
/*     */ import com.sun.jimi.core.util.LEDataOutputStream;
/*     */ import java.awt.image.IndexColorModel;
/*     */ import java.io.FilterOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ 
/*     */ public class ICOEncoder extends JimiEncoderBase
/*     */ {
/*     */   protected static final int BMPINFOHEADER_SIZE = 40;
/*  45 */   private boolean isEncodingFirstImage = true;
/*     */   protected LEDataOutputStream destination;
/*  50 */   protected short TYPE_FLAG = 1;
/*     */ 
/*  54 */   protected int stateFlag = 4;
/*     */ 
/*  56 */   protected int currentOffset = 0;
/*     */ 
/*     */   protected int computeBitCount(int paramInt)
/*     */   {
/* 417 */     if (paramInt <= 4)
/* 418 */       return 2;
/* 419 */     if (paramInt <= 16) {
/* 420 */       return 4;
/*     */     }
/* 422 */     return 8;
/*     */   }
/*     */ 
/*     */   protected int computeImageSize(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 411 */     return paramInt3 * paramInt4;
/*     */   }
/*     */ 
/*     */   public boolean driveEncoder()
/*     */     throws JimiException
/*     */   {
/*  92 */     AdaptiveRasterImage localAdaptiveRasterImage = getJimiImage();
/*     */     try
/*     */     {
/* 123 */       AdaptiveRasterImage[] arrayOfAdaptiveRasterImage = new AdaptiveRasterImage[1];
/* 124 */       arrayOfAdaptiveRasterImage[0] = localAdaptiveRasterImage;
/* 125 */       writeICOCURDirectory(this.destination, arrayOfAdaptiveRasterImage);
/* 126 */       writeDIBImage(this.destination, localAdaptiveRasterImage);
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/* 130 */       this.stateFlag = 1;
/* 131 */       throw new JimiException(localIOException.toString());
/*     */     }
/*     */     catch (JimiException localJimiException)
/*     */     {
/* 135 */       this.stateFlag = 1;
/* 136 */       throw localJimiException;
/*     */     }
/*     */ 
/* 140 */     this.stateFlag = 2;
/* 141 */     return false;
/*     */   }
/*     */   public void freeEncoder() throws JimiException {
/*     */   }
/*     */ 
/*     */   public int getState() {
/* 147 */     return this.stateFlag;
/*     */   }
/*     */ 
/*     */   public void initSpecificEncoder(OutputStream paramOutputStream, AdaptiveRasterImage paramAdaptiveRasterImage)
/*     */   {
/*  86 */     this.destination = new LEDataOutputStream(paramOutputStream);
/*     */   }
/*     */ 
/*     */   protected void writeBitmapInfoHeader(LEDataOutputStream paramLEDataOutputStream, int paramInt1, int paramInt2, AdaptiveRasterImage paramAdaptiveRasterImage, IndexColorModel paramIndexColorModel)
/*     */     throws JimiException, IOException
/*     */   {
/* 274 */     int i = paramAdaptiveRasterImage.getWidth();
/* 275 */     int j = paramAdaptiveRasterImage.getHeight() * 2;
/*     */ 
/* 277 */     int k = computeImageSize(paramInt2, paramInt1, i, j);
/*     */ 
/* 279 */     paramLEDataOutputStream.writeInt(40);
/* 280 */     paramLEDataOutputStream.writeInt(i);
/* 281 */     paramLEDataOutputStream.writeInt(j);
/* 282 */     paramLEDataOutputStream.writeShort(1);
/* 283 */     paramLEDataOutputStream.writeShort((short)paramInt1);
/* 284 */     paramLEDataOutputStream.writeInt(0);
/* 285 */     paramLEDataOutputStream.writeInt(k);
/* 286 */     paramLEDataOutputStream.writeInt(0);
/* 287 */     paramLEDataOutputStream.writeInt(0);
/* 288 */     paramLEDataOutputStream.writeInt(0);
/* 289 */     paramLEDataOutputStream.writeInt(0);
/*     */   }
/*     */ 
/*     */   protected void writeDIBImage(LEDataOutputStream paramLEDataOutputStream, AdaptiveRasterImage paramAdaptiveRasterImage)
/*     */     throws JimiException, IOException
/*     */   {
/* 248 */     IndexColorModel localIndexColorModel = (IndexColorModel)paramAdaptiveRasterImage.getColorModel();
/* 249 */     int i = localIndexColorModel.getMapSize();
/* 250 */     int j = computeBitCount(i);
/*     */ 
/* 252 */     writeBitmapInfoHeader(paramLEDataOutputStream, j, i, paramAdaptiveRasterImage, localIndexColorModel);
/* 253 */     writeRGBQuads(paramLEDataOutputStream, localIndexColorModel);
/* 254 */     writePixels(paramLEDataOutputStream, j, paramAdaptiveRasterImage);
/*     */   }
/*     */ 
/*     */   protected void writeICOCURDIREntry(LEDataOutputStream paramLEDataOutputStream, AdaptiveRasterImage paramAdaptiveRasterImage)
/*     */     throws JimiException, IOException
/*     */   {
/*     */     IndexColorModel localIndexColorModel;
/*     */     try
/*     */     {
/* 197 */       localIndexColorModel = (IndexColorModel)paramAdaptiveRasterImage.getColorModel();
/*     */     }
/*     */     catch (ClassCastException localClassCastException)
/*     */     {
/* 201 */       throw new JimiException("image/x-ico formats can only be created from palette images");
/*     */     }
/*     */ 
/* 204 */     int i = localIndexColorModel.getMapSize();
/* 205 */     if (i > 256)
/*     */     {
/* 207 */       throw new JimiException("image/x-ico formats can only support palette with up to 256 colors");
/*     */     }
/*     */ 
/* 210 */     int j = computeBitCount(i);
/*     */ 
/* 212 */     int k = paramAdaptiveRasterImage.getWidth();
/* 213 */     int m = paramAdaptiveRasterImage.getHeight();
/* 214 */     if ((k > 256) || (m > 256)) {
/* 215 */       throw new JimiException("image/x-ico formats can only encode images up to 256 x 256 pixels");
/*     */     }
/*     */ 
/* 218 */     int n = computeImageSize(i, j, k, m);
/*     */ 
/* 220 */     paramLEDataOutputStream.writeByte((byte)k);
/* 221 */     paramLEDataOutputStream.writeByte((byte)m);
/* 222 */     paramLEDataOutputStream.writeByte((byte)i);
/* 223 */     paramLEDataOutputStream.writeByte(0);
/* 224 */     paramLEDataOutputStream.writeShort(0);
/*     */ 
/* 226 */     paramLEDataOutputStream.writeShort((short)j);
/*     */ 
/* 228 */     int i1 = 40 + n * j / 8 + (int)Math.pow(2.0D, j);
/*     */ 
/* 230 */     paramLEDataOutputStream.writeInt(i1);
/*     */ 
/* 233 */     paramLEDataOutputStream.writeInt(this.currentOffset);
/* 234 */     this.currentOffset += n;
/*     */   }
/*     */ 
/*     */   protected void writeICOCURDirectory(LEDataOutputStream paramLEDataOutputStream, AdaptiveRasterImage[] paramArrayOfAdaptiveRasterImage)
/*     */     throws JimiException, IOException
/*     */   {
/* 166 */     paramLEDataOutputStream.writeShort(0);
/* 167 */     paramLEDataOutputStream.writeShort(1);
/* 168 */     paramLEDataOutputStream.writeShort((short)paramArrayOfAdaptiveRasterImage.length);
/*     */ 
/* 170 */     this.currentOffset = 6;
/* 171 */     this.currentOffset += paramArrayOfAdaptiveRasterImage.length * 16;
/*     */ 
/* 173 */     for (int i = 0; i < paramArrayOfAdaptiveRasterImage.length; i++)
/*     */     {
/* 175 */       writeICOCURDIREntry(paramLEDataOutputStream, paramArrayOfAdaptiveRasterImage[i]);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void writePixels(LEDataOutputStream paramLEDataOutputStream, int paramInt, AdaptiveRasterImage paramAdaptiveRasterImage)
/*     */     throws JimiException, IOException
/*     */   {
/* 336 */     paramAdaptiveRasterImage.setRGBDefault(false);
/* 337 */     int i = paramAdaptiveRasterImage.getHeight();
/* 338 */     int j = paramAdaptiveRasterImage.getWidth();
/*     */ 
/* 340 */     int[] arrayOfInt = new int[j];
/*     */     int k;
/*     */     int i2;
/* 343 */     if (paramInt == 2)
/*     */     {
/* 345 */       for (k = i - 1; k > -1; k--)
/*     */       {
/* 347 */         paramAdaptiveRasterImage.getChannel(k, arrayOfInt, 0);
/* 348 */         for (int m = 0; m < j; m++) {
/* 349 */           i2 = (byte)(arrayOfInt[m] << 6);
/* 350 */           i2 = (byte)(i2 | (byte)(arrayOfInt[(++m)] << 4));
/* 351 */           i2 = (byte)(i2 | (byte)(arrayOfInt[(++m)] << 2));
/* 352 */           i2 = (byte)(i2 | (byte)arrayOfInt[(++m)]);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 358 */       byte[] arrayOfByte1 = new byte[j / 4];
/* 359 */       for (i2 = 0; i2 < i; i2++) {
/* 360 */         paramLEDataOutputStream.write(arrayOfByte1);
/*     */       }
/*     */     }
/* 363 */     else if (paramInt == 4)
/*     */     {
/* 365 */       for (k = i - 1; k > -1; k--)
/*     */       {
/* 367 */         paramAdaptiveRasterImage.getChannel(k, arrayOfInt, 0);
/* 368 */         for (int n = 0; n < j; n++) {
/* 369 */           i2 = (byte)(arrayOfInt[n] << 4);
/* 370 */           i2 = (byte)(i2 | (byte)arrayOfInt[(++n)]);
/* 371 */           paramLEDataOutputStream.writeByte(i2);
/*     */         }
/*     */       }
/*     */ 
/* 375 */       byte[] arrayOfByte2 = new byte[j / 2];
/* 376 */       for (i2 = 0; i2 < i; i2++) {
/* 377 */         paramLEDataOutputStream.write(arrayOfByte2);
/*     */       }
/*     */     }
/* 380 */     else if (paramInt == 8)
/*     */     {
/* 382 */       for (k = i - 1; k > -1; k--)
/*     */       {
/* 384 */         paramAdaptiveRasterImage.getChannel(k, arrayOfInt, 0);
/* 385 */         for (int i1 = 0; i1 < j; i1++) {
/* 386 */           paramLEDataOutputStream.writeByte((byte)arrayOfInt[i1]);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 394 */       byte[] arrayOfByte3 = new byte[j];
/* 395 */       for (i2 = 0; i2 < i; i2++)
/*     */       {
/* 397 */         paramLEDataOutputStream.write(arrayOfByte3);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void writeRGBQuads(LEDataOutputStream paramLEDataOutputStream, IndexColorModel paramIndexColorModel)
/*     */     throws JimiException, IOException
/*     */   {
/* 302 */     int i = paramIndexColorModel.getMapSize();
/*     */ 
/* 304 */     int j = computeBitCount(i);
/* 305 */     int k = (int)Math.pow(2.0D, j) - i;
/*     */ 
/* 307 */     byte[] arrayOfByte1 = new byte[i];
/* 308 */     byte[] arrayOfByte2 = new byte[i];
/* 309 */     byte[] arrayOfByte3 = new byte[i];
/*     */ 
/* 311 */     paramIndexColorModel.getReds(arrayOfByte1);
/* 312 */     paramIndexColorModel.getBlues(arrayOfByte2);
/* 313 */     paramIndexColorModel.getGreens(arrayOfByte3);
/*     */ 
/* 315 */     for (int m = 0; m < i; m++)
/*     */     {
/* 319 */       paramLEDataOutputStream.writeByte(arrayOfByte2[m]);
/* 320 */       paramLEDataOutputStream.writeByte(arrayOfByte3[m]);
/* 321 */       paramLEDataOutputStream.writeByte(arrayOfByte1[m]);
/* 322 */       paramLEDataOutputStream.writeByte(0);
/*     */     }
/*     */ 
/* 326 */     byte[] arrayOfByte4 = new byte[4];
/*     */ 
/* 328 */     for (int n = 0; n < k; n++)
/* 329 */       paramLEDataOutputStream.write(arrayOfByte4);
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.encoder.ico.ICOEncoder
 * JD-Core Version:    0.6.2
 */