/*    */ package com.sun.jimi.core.decoder.tga;
/*    */ 
/*    */ import com.sun.jimi.core.util.LEDataInputStream;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class TGAColorMap
/*    */ {
/*    */   byte[] cmap;
/*    */ 
/*    */   public TGAColorMap(LEDataInputStream paramLEDataInputStream, TGAFileHeader paramTGAFileHeader)
/*    */     throws IOException
/*    */   {
/* 36 */     if (paramTGAFileHeader.colorMapType == 1)
/*    */     {
/*    */       int i3;
/*    */       int i2;
/*    */       int n;
/*    */       int m;
/*    */       int k;
/* 38 */       switch (paramTGAFileHeader.colorMapEntrySize)
/*    */       {
/*    */       case 8:
/* 41 */         this.cmap = new byte[paramTGAFileHeader.colorMapLength];
/* 42 */         i3 = 0;
/* 43 */         for (i2 = paramTGAFileHeader.firstEntryIndex; i2 < paramTGAFileHeader.colorMapLength; i2++)
/*    */         {
/* 45 */           int j = paramLEDataInputStream.readByte();
/* 46 */           this.cmap[i3] = (byte) j;
/* 47 */           i3++;
/* 48 */           this.cmap[i3] = (byte) j;
/* 49 */           i3++;
/* 50 */           this.cmap[i3] = (byte) j;
/* 51 */           i3++;
/*    */         }
/* 53 */         break;
/*    */       case 15:
/*    */       case 16:
/* 57 */         this.cmap = new byte[paramTGAFileHeader.colorMapLength * 3];
/* 58 */         i3 = paramTGAFileHeader.firstEntryIndex * 3;
/* 59 */         for (i2 = paramTGAFileHeader.firstEntryIndex; i2 < paramTGAFileHeader.colorMapLength; i2++)
/*    */         {
/* 61 */           int i = paramLEDataInputStream.readShort();
/*    */ 
/* 67 */           this.cmap[i3] = ((byte)((i >> 10 & 0x1F) * 255 / 31));
/* 68 */           i3++;
/*    */ 
/* 70 */           this.cmap[i3] = ((byte)((i >> 5 & 0x1F) * 255 / 31));
/* 71 */           i3++;
/*    */ 
/* 73 */           this.cmap[i3] = ((byte)((i & 0x1F) * 255 / 31));
/* 74 */           i3++;
/*    */         }
/*    */ 
/* 83 */         break;
/*    */       case 24:
/* 86 */         this.cmap = new byte[paramTGAFileHeader.colorMapLength * 3];
/* 87 */         i3 = paramTGAFileHeader.firstEntryIndex * 3;
/* 88 */         for (i2 = paramTGAFileHeader.firstEntryIndex; i2 < paramTGAFileHeader.colorMapLength; i2++)
/*    */         {
/* 90 */           n = paramLEDataInputStream.readByte();
/* 91 */           m = paramLEDataInputStream.readByte();
/* 92 */           k = paramLEDataInputStream.readByte();
/*    */ 
/* 94 */           this.cmap[i3] = (byte) k;
/* 95 */           i3++;
/* 96 */           this.cmap[i3] = (byte) m;
/* 97 */           i3++;
/* 98 */           this.cmap[i3] = (byte) n;
/* 99 */           i3++;
/*    */         }
/* 101 */         break;
/*    */       case 32:
/* 104 */         this.cmap = new byte[paramTGAFileHeader.colorMapLength * 4];
/* 105 */         i3 = paramTGAFileHeader.firstEntryIndex * 4;
/* 106 */         i2 = paramTGAFileHeader.firstEntryIndex;
/*    */         while (true) {
/* 108 */           n = paramLEDataInputStream.readByte();
/* 109 */           m = paramLEDataInputStream.readByte();
/* 110 */           k = paramLEDataInputStream.readByte();
/* 111 */           int i1 = paramLEDataInputStream.readByte();
/*    */ 
/* 113 */           this.cmap[i3] = (byte) k;
/* 114 */           i3++;
/* 115 */           this.cmap[i3] = (byte) m;
/* 116 */           i3++;
/* 117 */           this.cmap[i3] = (byte) n;
/* 118 */           i3++;
/* 119 */           this.cmap[i3] = (byte) i1;
/* 120 */           i3++;
/*    */ 
/* 106 */           i2++; if (i2 >= paramTGAFileHeader.colorMapLength)
/*    */           {
/* 122 */             break;
/*    */           }
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.decoder.tga.TGAColorMap
 * JD-Core Version:    0.6.2
 */