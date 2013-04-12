/*    */ package com.sun.jimi.core.decoder.pcx;
/*    */ 
/*    */ import com.sun.jimi.core.util.LEDataInputStream;
/*    */ import java.awt.image.ColorModel;
/*    */ import java.awt.image.IndexColorModel;
/*    */ import java.io.FilterInputStream;
/*    */ 
/*    */ public class PCXColorMap
/*    */ {
/*    */   private byte[] red;
/*    */   private byte[] green;
/*    */   private byte[] blue;
/*    */   private ColorModel colorModel;
/*    */ 
/*    */   public PCXColorMap(LEDataInputStream paramLEDataInputStream, int paramInt, byte paramByte)
/*    */   {
/* 26 */     if (paramByte == PCXHeader.V3_0p)
/*    */     {
/*    */       try
/*    */       {
/* 34 */         paramLEDataInputStream.mark(paramInt);
/* 35 */         paramLEDataInputStream.skip(paramInt - 16 - 769);
/*    */ 
/* 37 */         int i = paramLEDataInputStream.readByte();
/*    */         int j;
/* 39 */         if (i == 12)
/*    */         {
/* 44 */           this.red = new byte[256];
/* 45 */           this.green = new byte[256];
/* 46 */           this.blue = new byte[256];
/*    */ 
/* 48 */           for (j = 0; j < 256; j++)
/*    */           {
/* 50 */             this.red[j] = paramLEDataInputStream.readByte();
/* 51 */             this.green[j] = paramLEDataInputStream.readByte();
/* 52 */             this.blue[j] = paramLEDataInputStream.readByte();
/*    */           }
/*    */ 
/* 55 */           this.colorModel = new IndexColorModel(8, 256, this.red, this.green, this.blue);
/*    */ 
/* 57 */           paramLEDataInputStream.reset();
/* 58 */           paramLEDataInputStream.skip(48L);
/*    */         }
/*    */         else
/*    */         {
/* 63 */           paramLEDataInputStream.reset();
/* 64 */           this.red = new byte[16];
/* 65 */           this.green = new byte[16];
/* 66 */           this.blue = new byte[16];
/*    */ 
/* 68 */           for (j = 0; j < 16; j++)
/*    */           {
/* 70 */             this.red[j] = paramLEDataInputStream.readByte();
/* 71 */             this.green[j] = paramLEDataInputStream.readByte();
/* 72 */             this.blue[j] = paramLEDataInputStream.readByte();
/*    */           }
/*    */ 
/* 76 */           this.colorModel = new IndexColorModel(4, 16, this.red, this.green, this.blue);
/*    */         }
/*    */       }
/*    */       catch (Exception localException)
/*    */       {
/*    */       }
/*    */     }
/*    */   }
/*    */ 
/*    */   public ColorModel getColorModel()
/*    */   {
/* 95 */     return this.colorModel;
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.decoder.pcx.PCXColorMap
 * JD-Core Version:    0.6.2
 */