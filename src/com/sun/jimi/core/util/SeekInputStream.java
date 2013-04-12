/*     */ package com.sun.jimi.core.util;
/*     */ 
/*     */ import com.sun.jimi.util.ExpandableArray;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.DataInput;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.FilterInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ 
/*     */ public class SeekInputStream extends FilterInputStream
/*     */   implements DataInput
/*     */ {
/*     */   InputStream in;
/*     */   int curOffset;
/*     */   ExpandableArray bufs;
/*     */   DataInput setD;
/*     */   InputStream setDIS;
/*     */   boolean be;
/*     */   boolean underlying;
/*     */ 
/*     */   public SeekInputStream(boolean paramBoolean, InputStream paramInputStream, int paramInt)
/*     */     throws IOException
/*     */   {
/* 111 */     super(paramInputStream);
/* 112 */     this.be = paramBoolean;
/* 113 */     this.in = paramInputStream;
/* 114 */     this.bufs = new ExpandableArray();
/* 115 */     this.curOffset = paramInt;
/* 116 */     setInputStream(this.curOffset);
/*     */   }
/*     */ 
/*     */   public int available()
/*     */     throws IOException
/*     */   {
/* 291 */     return this.setDIS.available();
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 258 */     this.setD = null;
/* 259 */     this.in.close();
/* 260 */     this.in = null;
/* 261 */     this.bufs = null;
/* 262 */     this.curOffset = 0;
/*     */   }
/*     */ 
/*     */   protected InputStream getBufIS(int paramInt)
/*     */     throws IOException
/*     */   {
/* 188 */     for (int i = 0; i < this.bufs.size(); i++)
/*     */     {
/* 190 */       ByteBuf localByteBuf = (ByteBuf)this.bufs.elementAt(i);
/* 191 */       if ((paramInt >= localByteBuf.offset) && (paramInt < localByteBuf.offset + localByteBuf.buf.length))
/*     */       {
/* 194 */         int j = paramInt - localByteBuf.offset;
/* 195 */         return new ByteArrayInputStream(localByteBuf.buf, j, localByteBuf.buf.length - j);
/*     */       }
/*     */     }
/* 198 */     throw new IOException("not buffered");
/*     */   }
/*     */ 
/*     */   public synchronized void mark(int paramInt)
/*     */   {
/*     */   }
/*     */ 
/*     */   public boolean markSupported()
/*     */   {
/* 302 */     return false;
/*     */   }
/*     */ 
/*     */   public final synchronized int read()
/*     */     throws IOException
/*     */   {
/* 223 */     int i = this.setDIS.read();
/* 224 */     if ((i >= 0) && (this.underlying))
/* 225 */       this.curOffset += 1;
/* 226 */     return i;
/*     */   }
/*     */ 
/*     */   public final synchronized int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException
/*     */   {
/* 231 */     int i = this.setDIS.read(paramArrayOfByte, paramInt1, paramInt2);
/* 232 */     if (i < 0)
/*     */     {
/* 234 */       if (!this.underlying)
/*     */       {
/* 239 */         setInputStream(this.curOffset);
/* 240 */         return 0;
/*     */       }
/*     */ 
/* 243 */       return i;
/*     */     }
/*     */ 
/* 247 */     if (this.underlying)
/* 248 */       this.curOffset += i;
/* 249 */     return i;
/*     */   }
/*     */ 
/*     */   public final boolean readBoolean()
/*     */     throws IOException
/*     */   {
/* 336 */     if (this.underlying)
/* 337 */       this.curOffset += 1;
/* 338 */     return this.setD.readBoolean();
/*     */   }
/*     */ 
/*     */   public final byte readByte() throws IOException
/*     */   {
/* 343 */     byte b = this.setD.readByte();
/* 344 */     if (b >= 0)
/* 345 */       this.curOffset += 1;
/* 346 */     return b;
/*     */   }
/*     */ 
/*     */   public final char readChar()
/*     */     throws IOException
/*     */   {
/* 375 */     char c = this.setD.readChar();
/* 376 */     if (this.underlying)
/* 377 */       this.curOffset += 2;
/* 378 */     return c;
/*     */   }
/*     */ 
/*     */   public final double readDouble()
/*     */     throws IOException
/*     */   {
/* 404 */     return Double.longBitsToDouble(readLong());
/*     */   }
/*     */ 
/*     */   public final float readFloat()
/*     */     throws IOException
/*     */   {
/* 399 */     return Float.intBitsToFloat(readInt());
/*     */   }
/*     */ 
/*     */   protected static int readFully(InputStream paramInputStream, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {int i = 0;
/* 210 */     for ( i = 0; i < paramInt2; )
/*     */     {
/* 212 */       int j = paramInputStream.read(paramArrayOfByte, i, paramInt2 - i);
/* 213 */       if (j < 0)
/* 214 */         return -i;
/* 215 */       i += j;
/*     */     }
/* 217 */     return i;
/*     */   }
/*     */ 
/*     */   public final void readFully(byte[] paramArrayOfByte)
/*     */     throws IOException
/*     */   {
/* 314 */     this.setD.readFully(paramArrayOfByte, 0, paramArrayOfByte.length);
/* 315 */     if (this.underlying)
/* 316 */       this.curOffset += paramArrayOfByte.length;
/*     */   }
/*     */ 
/*     */   public final void readFully(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException
/*     */   {
/* 321 */     this.setD.readFully(paramArrayOfByte, paramInt1, paramInt2);
/* 322 */     if (this.underlying)
/* 323 */       this.curOffset += paramInt2;
/*     */   }
/*     */ 
/*     */   public final int readInt()
/*     */     throws IOException
/*     */   {
/* 383 */     int i = this.setD.readInt();
/* 384 */     if (this.underlying)
/* 385 */       this.curOffset += 4;
/* 386 */     return i;
/*     */   }
/*     */ 
/*     */   public final String readLine()
/*     */     throws IOException
/*     */   {
/* 410 */     return new String();
/*     */   }
/*     */ 
/*     */   public final long readLong()
/*     */     throws IOException
/*     */   {
/* 391 */     long l = this.setD.readLong();
/* 392 */     if (this.underlying)
/* 393 */       this.curOffset += 8;
/* 394 */     return l;
/*     */   }
/*     */ 
/*     */   public final short readShort()
/*     */     throws IOException
/*     */   {
/* 359 */     short s = this.setD.readShort();
/* 360 */     if (this.underlying)
/* 361 */       this.curOffset += 2;
/* 362 */     return s;
/*     */   }
/*     */ 
/*     */   public final String readUTF()
/*     */     throws IOException
/*     */   {
/* 416 */     return this.setD.readUTF();
/*     */   }
/*     */ 
/*     */   public final String readUTF(DataInput paramDataInput)
/*     */     throws IOException
/*     */   {
/* 422 */     return DataInputStream.readUTF(paramDataInput);
/*     */   }
/*     */ 
/*     */   public final int readUnsignedByte()
/*     */     throws IOException
/*     */   {
/* 351 */     int i = this.setD.readUnsignedByte();
/* 352 */     if (i >= 0)
/* 353 */       this.curOffset += 1;
/* 354 */     return i;
/*     */   }
/*     */ 
/*     */   public final int readUnsignedShort()
/*     */     throws IOException
/*     */   {
/* 367 */     int i = this.setD.readUnsignedShort();
/* 368 */     if ((i >= 0) && (this.underlying))
/* 369 */       this.curOffset += 2;
/* 370 */     return i;
/*     */   }
/*     */ 
/*     */   public synchronized void reset()
/*     */     throws IOException
/*     */   {
/*     */   }
/*     */ 
/*     */   public synchronized void seek(int paramInt)
/*     */     throws IOException
/*     */   {
/* 127 */     setInputStream(paramInt);
/*     */   }
/*     */ 
/*     */   protected void setInputStream(int paramInt)
/*     */     throws IOException
/*     */   {
/* 142 */     this.underlying = false;
/*     */     InputStream localInputStream;
/* 143 */     if (paramInt == this.curOffset)
/*     */     {
/* 145 */       localInputStream = this.in;
/* 146 */       this.underlying = true;
/*     */     }
/* 148 */     else if (paramInt > this.curOffset)
/*     */     {
/* 151 */       int i = paramInt - this.curOffset;
/* 152 */       ByteBuf localByteBuf = new ByteBuf(this.curOffset, i);
/* 153 */       int j = readFully(this.in, localByteBuf.buf, 0, i);
/* 154 */       if (j < 0)
/*     */       {
/* 156 */         this.curOffset += -j;
/* 157 */         throw new IOException();
/*     */       }
/*     */ 
/* 160 */       this.curOffset += j;
/*     */ 
/* 162 */       this.bufs.addElement(localByteBuf);
/* 163 */       localInputStream = this.in;
/* 164 */       this.underlying = true;
/*     */     }
/*     */     else
/*     */     {
/* 168 */       localInputStream = getBufIS(paramInt);
/*     */     }
/*     */ 
/* 171 */     if (this.be)
/* 172 */       this.setD = new DataInputStream(localInputStream);
/*     */     else
/* 174 */       this.setD = new LEDataInputStream(localInputStream);
/* 175 */     this.setDIS = ((InputStream)this.setD);
/*     */   }
/*     */ 
/*     */   public long skip(long paramLong)
/*     */     throws IOException
/*     */   {
/* 267 */     long l = this.setDIS.skip(paramLong);
/* 268 */     if (l < 0L)
/*     */     {
/* 270 */       if (!this.underlying)
/*     */       {
/* 275 */         setInputStream(this.curOffset);
/* 276 */         return 0L;
/*     */       }
/*     */ 
/* 279 */       return l;
/*     */     }
/*     */ 
/* 283 */     if (this.underlying)
/* 284 */       this.curOffset = ((int)(this.curOffset + l));
/* 285 */     return l;
/*     */   }
/*     */ 
/*     */   public final int skipBytes(int paramInt)
/*     */     throws IOException
/*     */   {
/* 328 */     int i = this.setD.skipBytes(paramInt);
/* 329 */     if (this.underlying)
/* 330 */       this.curOffset += i;
/* 331 */     return i;
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.util.SeekInputStream
 * JD-Core Version:    0.6.2
 */