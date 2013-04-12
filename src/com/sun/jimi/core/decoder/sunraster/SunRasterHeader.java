/*     */ package com.sun.jimi.core.decoder.sunraster;
/*     */ 
/*     */ import com.sun.jimi.core.JimiException;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ 
/*     */ class SunRasterHeader
/*     */ {
/*     */   public static final int MAGIC_NUMBER = 1504078485;
/*     */   public static final int OLD_TYPE = 0;
/*     */   public static final int STANDARD_TYPE = 1;
/*     */   public static final int BYTE_ENCODED_TYPE = 2;
/*     */   public static final int RGB_FORMAT_TYPE = 3;
/*     */   public static final int TIFF_FORMAT_TYPE = 4;
/*     */   public static final int IFF_FORMAT_TYPE = 5;
/*     */   public static final int EXPERIMENTAL_TYPE = 65535;
/*     */   public static final int NO_COLOR_MAP = 0;
/*     */   public static final int RGB_COLOR_MAP = 1;
/*     */   public static final int RAW_COLOR_MAP = 2;
/*     */   int MagicNumber;
/*     */   int Width;
/*     */   int Height;
/*     */   int Depth;
/*     */   int Length;
/*     */   int Type;
/*     */   int ColorMapType;
/*     */   int ColorMapLength;
/*     */ 
/*     */   SunRasterHeader(DataInputStream paramDataInputStream)
/*     */     throws IOException, JimiException
/*     */   {
/*  83 */     this.MagicNumber = paramDataInputStream.readInt();
/*     */ 
/*  85 */     if (this.MagicNumber != 1504078485)
/*     */     {
/*  87 */       throw new JimiException("Wrong Format");
/*     */     }
/*     */ 
/*  91 */     this.Width = paramDataInputStream.readInt();
/*  92 */     this.Height = paramDataInputStream.readInt();
/*  93 */     this.Depth = paramDataInputStream.readInt();
/*     */ 
/*  95 */     this.Length = paramDataInputStream.readInt();
/*  96 */     if (this.Length == 0)
/*     */     {
/*  98 */       this.Length = (this.Width * this.Height * this.Depth);
/*     */     }
/*     */ 
/* 102 */     this.Type = paramDataInputStream.readInt();
/* 103 */     if (this.Type == 65535)
/*     */     {
/* 105 */       throw new JimiException("Unsupported Format Subtype");
/*     */     }
/*     */ 
/* 109 */     this.ColorMapType = paramDataInputStream.readInt();
/* 110 */     this.ColorMapLength = paramDataInputStream.readInt();
/* 111 */     if ((this.ColorMapType == 0) && (this.ColorMapLength != 0))
/*     */     {
/* 113 */       throw new IOException("Corrupted Stream");
/*     */     }
/*     */ 
/* 117 */     if (this.Depth == 1)
/*     */     {
/* 119 */       if ((this.ColorMapType != 0) && (
/* 120 */         (this.ColorMapType != 1) || (this.ColorMapLength != 6)))
/*     */       {
/* 122 */         throw new IOException("Corrupted Stream");
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.decoder.sunraster.SunRasterHeader
 * JD-Core Version:    0.6.2
 */