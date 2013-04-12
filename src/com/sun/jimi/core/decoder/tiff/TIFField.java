/*     */ package com.sun.jimi.core.decoder.tiff;
/*     */ 
/*     */ import com.sun.jimi.core.util.SeekInputStream;
/*     */ import java.io.IOException;
/*     */ 
/*     */ class TIFField
/*     */ {
/*     */   public static final int BYTE = 1;
/*     */   public static final int ASCII = 2;
/*     */   public static final int SHORT = 3;
/*     */   public static final int LONG = 4;
/*     */   public static final int RATIONAL = 5;
/*     */   public static final int SBYTE = 6;
/*     */   public static final int UNDEFINE = 7;
/*     */   public static final int SSHORT = 8;
/*     */   public static final int SLONG = 9;
/*     */   public static final int SRATIONAL = 10;
/*     */   public static final int FLOAT = 11;
/*     */   public static final int DOUBLE = 12;
/*  30 */   public static final int[] dataWidths = { 
/*  31 */     1, 
/*  32 */     1, 
/*  33 */     1, 
/*  34 */     2, 
/*  35 */     4, 
/*  36 */     8, 
/*  37 */     1, 
/*  38 */     1, 
/*  39 */     2, 
/*  40 */     4, 
/*  41 */     8, 
/*  42 */     4, 
/*  43 */     8 };
/*     */   short id;
/*     */   short type;
/*     */   int count;
/*     */   int offset;
/* 303 */   public static final String[] typeNames = { 
/* 304 */     "no value", "BYTE", "ASCII", "SHORT", "LONG", 
/* 305 */     "RATIONAL", "SBYTE", "UNDEFINED", "SSHORT", 
/* 306 */     "SLONG", "SRATIONAL", "FLOAT", "DOUBLE" };
/*     */ 
/*     */   public final boolean equals(int paramInt)
/*     */   {
/* 109 */     return this.id == paramInt;
/*     */   }
/*     */ 
/*     */   public byte[] getByteArray(SeekInputStream paramSeekInputStream)
/*     */     throws IOException
/*     */   {
/* 261 */     byte[] arrayOfByte = new byte[this.count];
/*     */ 
/* 263 */     if ((this.type == 1) || (this.type == 6))
/*     */     {
/* 265 */       if (this.count <= 4)
/*     */       {
/* 267 */         if (this.count >= 1)
/* 268 */           arrayOfByte[0] = ((byte)(this.offset >> 24 & 0xFF));
/* 269 */         if (this.count >= 2)
/* 270 */           arrayOfByte[1] = ((byte)(this.offset >> 16 & 0xFF));
/* 271 */         if (this.count >= 3)
/* 272 */           arrayOfByte[2] = ((byte)(this.offset >> 8 & 0xFF));
/* 273 */         if (this.count == 4)
/* 274 */           arrayOfByte[3] = ((byte)(this.offset & 0xFF));
/*     */       }
/*     */       else
/*     */       {
/* 278 */         paramSeekInputStream.seek(this.offset);
/* 279 */         for (int i = 0; i < this.count; i++)
/* 280 */           arrayOfByte[i] = paramSeekInputStream.readByte();
/*     */       }
/*     */     }
/*     */     else {
/* 284 */       throw new RuntimeException("wrong method " + toString());
/*     */     }
/* 286 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   public int getInt(SeekInputStream paramSeekInputStream)
/*     */     throws IOException
/*     */   {
/* 119 */     int i = 0;
/* 120 */     int j = 0;
/*     */ 
/* 122 */     if (this.count == 1)
/*     */     {
/* 124 */       switch (this.type) {
/*     */       case 1:
/* 126 */         i = this.offset >> 24 & 0xFF; break;
/*     */       case 6:
/* 127 */         i = (byte)(this.offset >> 24 & 0xFF); break;
/*     */       case 3:
/* 128 */         i = this.offset >> 16 & 0xFFFF; break;
/*     */       case 8:
/* 129 */         i = (short)(this.offset >> 16 & 0xFFFF); break;
/*     */       case 4:
/* 130 */         i = this.offset; break;
/*     */       case 9:
/* 131 */         i = this.offset; break;
/*     */       case 2:
/*     */       case 5:
/*     */       case 7:
/*     */       default:
/* 132 */         j = 1; break;
/*     */       }
/*     */     }
/*     */     else {
/* 136 */       j = 1;
/*     */     }
/* 138 */     if (j != 0)
/* 139 */       throw new RuntimeException("wrong method " + toString());
/* 140 */     return i;
/*     */   }
/*     */ 
/*     */   public int[] getIntArray(SeekInputStream paramSeekInputStream)
/*     */     throws IOException
/*     */   {
/* 198 */     int[] arrayOfInt = new int[this.count];
/*     */ 
/* 200 */     if ((this.type == 1) || (this.type == 6) || (this.type == 3) || (this.type == 8))
/*     */     {
/* 202 */       short[] arrayOfShort = getShortArray(paramSeekInputStream);
/*     */ 
/* 205 */       for (int j = 0; j < this.count; j++)
/* 206 */         arrayOfInt[j] = arrayOfShort[j];
/*     */     }
/* 208 */     else if ((this.type == 4) || (this.type == 9))
/*     */     {
/* 210 */       if (this.count == 1) {
/* 211 */         arrayOfInt[0] = this.offset;
/*     */       }
/*     */       else {
/* 214 */         paramSeekInputStream.seek(this.offset);
/* 215 */         for (int i = 0; i < this.count; i++)
/* 216 */           arrayOfInt[i] = paramSeekInputStream.readInt();
/*     */       }
/*     */     }
/*     */     else {
/* 220 */       throw new RuntimeException("wrong method " + toString());
/*     */     }
/* 222 */     return arrayOfInt;
/*     */   }
/*     */ 
/*     */   public float getRational(SeekInputStream paramSeekInputStream)
/*     */     throws IOException
/*     */   {
/* 149 */     paramSeekInputStream.seek(this.offset);
/*     */     long l1;
/*     */     long l2;
/* 150 */     if (this.type == 5)
/*     */     {
/* 152 */       l1 = paramSeekInputStream.readInt();
/* 153 */       if (l1 < 0L)
/* 154 */         l1 &= 4294967295L;
/* 155 */       l2 = paramSeekInputStream.readInt();
/* 156 */       if (l2 < 0L)
/* 157 */         l2 &= 4294967295L;
/*     */     }
/*     */     else
/*     */     {
/* 161 */       l1 = paramSeekInputStream.readInt();
/* 162 */       l2 = paramSeekInputStream.readInt();
/*     */     }
/* 164 */     return (float)(l1 / l2);
/*     */   }
/*     */ 
/*     */   public float[] getRationalArray(SeekInputStream paramSeekInputStream)
/*     */     throws IOException
/*     */   {
/* 171 */     float[] arrayOfFloat = new float[this.count];
/*     */ 
/* 173 */     paramSeekInputStream.seek(this.offset);
/* 174 */     for (int i = 0; i < this.count; i++)
/*     */     {
/*     */       long l1;
/*     */       long l2;
/* 176 */       if (this.type == 5)
/*     */       {
/* 178 */         l1 = paramSeekInputStream.readInt();
/* 179 */         if (l1 < 0L)
/* 180 */           l1 &= 4294967295L;
/* 181 */         l2 = paramSeekInputStream.readInt();
/* 182 */         if (l2 < 0L)
/* 183 */           l2 &= 4294967295L;
/*     */       }
/*     */       else
/*     */       {
/* 187 */         l1 = paramSeekInputStream.readInt();
/* 188 */         l2 = paramSeekInputStream.readInt();
/*     */       }
/* 190 */       arrayOfFloat[i] = ((float)(l1 / l2));
/*     */     }
/* 192 */     return arrayOfFloat;
/*     */   }
/*     */ 
/*     */   public short[] getShortArray(SeekInputStream paramSeekInputStream)
/*     */     throws IOException
/*     */   {
/* 227 */     short[] arrayOfShort = new short[this.count];
/*     */ 
/* 229 */     if ((this.type == 1) || (this.type == 6))
/*     */     {
/* 231 */       byte[] arrayOfByte = getByteArray(paramSeekInputStream);
/*     */ 
/* 234 */       for (int j = 0; j < this.count; j++)
/* 235 */         arrayOfShort[j] = ((short)arrayOfByte[j]);
/*     */     }
/* 237 */     else if ((this.type == 3) || (this.type == 8))
/*     */     {
/* 239 */       if (this.count <= 2)
/*     */       {
/* 241 */         if (this.count >= 1)
/* 242 */           arrayOfShort[0] = ((short)(this.offset >> 16 & 0xFFFF));
/* 243 */         if (this.count >= 2)
/* 244 */           arrayOfShort[1] = ((short)(this.offset & 0xFFFF));
/*     */       }
/*     */       else
/*     */       {
/* 248 */         paramSeekInputStream.seek(this.offset);
/* 249 */         for (int i = 0; i < this.count; i++)
/* 250 */           arrayOfShort[i] = paramSeekInputStream.readShort();
/*     */       }
/*     */     }
/*     */     else {
/* 254 */       throw new RuntimeException("wrong method " + toString());
/*     */     }
/* 256 */     return arrayOfShort;
/*     */   }
/*     */ 
/*     */   String getString(SeekInputStream paramSeekInputStream)
/*     */     throws IOException
/*     */   {
/* 295 */     byte[] arrayOfByte = getByteArray(paramSeekInputStream);
/*     */ 
/* 298 */     return new String(arrayOfByte, 0, 0, arrayOfByte.length - 1);
/*     */   }
/*     */ 
/*     */   public final void read(SeekInputStream paramSeekInputStream)
/*     */     throws IOException
/*     */   {
/*  68 */     this.id = paramSeekInputStream.readShort();
/*  69 */     this.type = paramSeekInputStream.readShort();
/*  70 */     this.count = paramSeekInputStream.readInt();
/*     */ 
/*  73 */     if (this.count * dataWidths[this.type] > 4)
/*     */     {
/*  76 */       this.offset = paramSeekInputStream.readInt();
/*     */     }
/*     */     else
/*     */     {
/*  82 */       switch (this.type)
/*     */       {
/*     */       case 1:
/*     */       case 2:
/*     */       case 6:
/*     */       case 7:
/*  88 */         this.offset = (paramSeekInputStream.readUnsignedByte() << 24);
/*  89 */         this.offset += (paramSeekInputStream.readUnsignedByte() << 16);
/*  90 */         this.offset += (paramSeekInputStream.readUnsignedByte() << 8);
/*  91 */         this.offset += paramSeekInputStream.readUnsignedByte();
/*  92 */         break;
/*     */       case 3:
/*     */       case 8:
/*  96 */         this.offset = (paramSeekInputStream.readUnsignedShort() << 16);
/*  97 */         this.offset += paramSeekInputStream.readUnsignedShort();
/*  98 */         break;
/*     */       case 4:
/*     */       case 5:
/*     */       default:
/* 101 */         this.offset = paramSeekInputStream.readInt();
/* 102 */         break;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 311 */     return " id " + this.id + " count " + this.count + " type (" + typeNames[this.type] + ") " + this.type + " offset " + this.offset;
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.decoder.tiff.TIFField
 * JD-Core Version:    0.6.2
 */