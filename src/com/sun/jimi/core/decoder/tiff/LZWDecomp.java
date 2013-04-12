/*    */ package com.sun.jimi.core.decoder.tiff;
/*    */ 
/*    */ import com.sun.jimi.core.JimiException;
/*    */ import com.sun.jimi.core.util.lzw.LZWDecompressor;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ 
/*    */ class LZWDecomp extends Decompressor
/*    */ {
/*    */   int bitsperpixel_;
/* 19 */   byte[] bitPackedBuf_ = null;
/*    */   InputStream in_;
/*    */   LZWDecompressor decomp_;
/*    */   int predictor_;
/*    */ 
/*    */   LZWDecomp(InputStream paramInputStream, int paramInt1, int paramInt2, int paramInt3)
/*    */   {
/* 36 */     super(new TiffNumberReader(new byte[1]), paramInt1, paramInt2);
/* 37 */     this.in_ = paramInputStream;
/* 38 */     this.bitsperpixel_ = paramInt2;
/* 39 */     this.predictor_ = paramInt3;
/*    */ 
/* 42 */     this.decomp_ = new LZWDecompressor(this.in_, 8, true);
/*    */   }
/*    */ 
/*    */   public void begOfPage()
/*    */   {
/*    */   }
/*    */ 
/*    */   public void begOfStrip()
/*    */   {
/*    */   }
/*    */ 
/*    */   public void decodeLine(byte[] paramArrayOfByte, int paramInt) throws JimiException {
/* 54 */     Thread.yield();
/*    */     try
/*    */     {
/*    */       int i;
/*    */       int j;
/* 59 */       switch (this.bitsperpixel_)
/*    */       {
/*    */       case 4:
/* 62 */         if (this.bitPackedBuf_ == null)
/* 63 */           this.bitPackedBuf_ = new byte[paramInt + 1 >> 1];
/* 64 */         i = this.decomp_.decompress(this.bitPackedBuf_);
/* 65 */         if (this.invertOut_)
/*    */         {
/* 67 */           j = this.bitPackedBuf_.length;
/*    */           do { this.bitPackedBuf_[j] = ((byte)(this.bitPackedBuf_[j] ^ 0xFFFFFFFF));
/*    */ 
/* 67 */             j--; } while (j >= 0);
/*    */         }
/*    */ 
/* 70 */         System.arraycopy(this.bitPackedBuf_, 0, paramArrayOfByte, 0, this.bitPackedBuf_.length);
/* 71 */         break;
/*    */       case 1:
/*    */       case 8:
/* 75 */         i = this.decomp_.decompress(paramArrayOfByte);
/* 76 */         if (this.invertOut_)
/*    */         {
/* 78 */           j = paramArrayOfByte.length;
/*    */           do { paramArrayOfByte[j] = ((byte)(paramArrayOfByte[j] ^ 0xFFFFFFFF));
/*    */ 
/* 78 */             j--; } while (j >= 0);
/*    */         }break;
/*    */       case 2:
/*    */       case 3:
/*    */       case 5:
/*    */       case 6:
/*    */       case 7:
/*    */       }
/*    */     } catch (IOException localIOException) { throw new JimiException("error unpacking data:" + localIOException); }
/*    */ 
/*    */   }
/*    */ 
/*    */   public void setInputStream(InputStream paramInputStream)
/*    */   {
/* 47 */     this.decomp_.setInputStream(paramInputStream);
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.decoder.tiff.LZWDecomp
 * JD-Core Version:    0.6.2
 */