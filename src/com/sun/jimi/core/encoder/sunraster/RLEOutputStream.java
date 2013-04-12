/*     */ package com.sun.jimi.core.encoder.sunraster;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ 
/*     */ public class RLEOutputStream extends OutputStream
/*     */ {
/*     */   protected static final byte FLAG_VALUE = -128;
/*     */   protected OutputStream out_;
/*     */   protected byte runValue_;
/*     */   protected int runCount_;
/*     */ 
/*     */   public RLEOutputStream(OutputStream paramOutputStream)
/*     */   {
/*  32 */     this.out_ = paramOutputStream;
/*     */   }
/*     */ 
/*     */   protected synchronized void compressBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/*  43 */     int i = paramInt2;
/*  44 */     int j = this.runCount_;
/*  45 */     byte b1 = this.runValue_;
/*     */ 
/*  47 */     OutputStream localOutputStream = this.out_;
/*     */ 
/*  49 */     for (int k = paramInt1; k < i; k++)
/*     */     {
/*  51 */       byte b2 = paramArrayOfByte[k];
/*     */ 
/*  53 */       if (j == 0)
/*     */       {
/*  55 */         b1 = b2;
/*  56 */         j = 1;
/*     */       }
/*  59 */       else if ((b2 == b1) && (j < 255))
/*     */       {
/*  62 */         j++;
/*     */       }
/*     */       else
/*     */       {
/*  67 */         writeRun(j, b1);
/*     */ 
/*  69 */         b1 = b2;
/*  70 */         j = 1;
/*     */       }
/*     */     }
/*     */ 
/*  74 */     this.runCount_ = j;
/*  75 */     this.runValue_ = b1;
/*     */   }
/*     */ 
/*     */   public void flush()
/*     */     throws IOException
/*     */   {
/* 113 */     writeRun();
/* 114 */     this.runCount_ = 0;
/* 115 */     this.runValue_ = -1;
/*     */   }
/*     */ 
/*     */   public void write(int paramInt)
/*     */     throws IOException
/*     */   {
/* 106 */     byte[] arrayOfByte = new byte[1];
/* 107 */     arrayOfByte[0] = ((byte)paramInt);
/* 108 */     write(arrayOfByte);
/*     */   }
/*     */ 
/*     */   public void write(byte[] paramArrayOfByte)
/*     */     throws IOException
/*     */   {
/*  84 */     write(paramArrayOfByte, 0, paramArrayOfByte.length);
/*     */   }
/*     */ 
/*     */   public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/*  96 */     compressBytes(paramArrayOfByte, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   protected void writeLiteral(byte paramByte)
/*     */     throws IOException
/*     */   {
/* 144 */     this.out_.write(paramByte);
/*     */ 
/* 147 */     if (paramByte == -128)
/* 148 */       this.out_.write(0);
/*     */   }
/*     */ 
/*     */   protected void writeRun()
/*     */     throws IOException
/*     */   {
/* 138 */     writeRun(this.runCount_, this.runValue_);
/*     */   }
/*     */ 
/*     */   protected void writeRun(int paramInt, byte paramByte)
/*     */     throws IOException
/*     */   {
/* 121 */     if (paramInt > 2)
/*     */     {
/* 124 */       this.out_.write(-128);
/* 125 */       this.out_.write((byte)paramInt - 1);
/* 126 */       this.out_.write(paramByte);
/*     */     }
/*     */     else
/*     */     {
/* 131 */       while (paramInt-- > 0)
/* 132 */         writeLiteral(paramByte);
/*     */     }
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.encoder.sunraster.RLEOutputStream
 * JD-Core Version:    0.6.2
 */