/*    */ package com.sun.jimi.core.decoder.pict;
/*    */ 
/*    */ import com.sun.jimi.core.JimiException;
/*    */ import java.awt.image.ColorModel;
/*    */ import java.awt.image.IndexColorModel;
/*    */ import java.io.DataInputStream;
/*    */ import java.io.IOException;
/*    */ 
/*    */ class PICTColorTable
/*    */ {
/*    */   int id;
/*    */   short flags;
/*    */   short count;
/*    */   short[] red;
/*    */   short[] blue;
/*    */   short[] green;
/*    */ 
/*    */   PICTColorTable(DataInputStream paramDataInputStream)
/*    */     throws IOException
/*    */   {
/* 45 */     this.id = paramDataInputStream.readInt();
/* 46 */     this.flags = paramDataInputStream.readShort();
/* 47 */     this.count = paramDataInputStream.readShort();
/* 48 */     this.count = ((short)(this.count + 1));
/* 49 */     this.red = new short[this.count];
/* 50 */     this.green = new short[this.count];
/* 51 */     this.blue = new short[this.count];
/*    */ 
/* 53 */     for (int i = 0; i < this.count; i++)
/*    */     {
/* 55 */       int j = paramDataInputStream.readShort();
/*    */ 
/* 58 */       if ((this.flags & 0x8000) != 0)
/* 59 */         j = i;
/* 60 */       this.red[j] = paramDataInputStream.readShort();
/* 61 */       this.green[j] = paramDataInputStream.readShort();
/* 62 */       this.blue[j] = paramDataInputStream.readShort();
/*    */     }
/*    */   }
/*    */ 
/*    */   ColorModel createColorModel(PICTPixmap paramPICTPixmap)
/*    */     throws JimiException
/*    */   {
/* 71 */     if (this.count < 2) {
/* 72 */       throw new JimiException("color count < 2");
/*    */     }
/*    */ 
/* 80 */     byte[] arrayOfByte1 = new byte[this.count];
/* 81 */     byte[] arrayOfByte2 = new byte[this.count];
/* 82 */     byte[] arrayOfByte3 = new byte[this.count];
/* 83 */     for (int i = 0; i < this.count; i++)
/*    */     {
/* 85 */       arrayOfByte1[i] = ((byte)(this.red[i] >> 8));
/* 86 */       arrayOfByte2[i] = ((byte)(this.green[i] >> 8));
/* 87 */       arrayOfByte3[i] = ((byte)(this.blue[i] >> 8));
/*    */     }
/*    */ 
/* 90 */     IndexColorModel localIndexColorModel = new IndexColorModel(8, this.count, arrayOfByte1, arrayOfByte2, arrayOfByte3);
/* 91 */     return localIndexColorModel;
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.decoder.pict.PICTColorTable
 * JD-Core Version:    0.6.2
 */