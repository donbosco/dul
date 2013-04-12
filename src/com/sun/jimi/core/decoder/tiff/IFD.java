/*    */ package com.sun.jimi.core.decoder.tiff;
/*    */ 
/*    */ import com.sun.jimi.core.util.SeekInputStream;
/*    */ import java.io.IOException;
/*    */ import java.util.Enumeration;
/*    */ 
/*    */ class IFD
/*    */ {
/*    */   int count;
/*    */   TIFField[] fields;
/*    */   int offset;
/*    */ 
/*    */   TIFField getField(int paramInt)
/*    */   {
/* 58 */     for (int i = 0; i < this.count; i++)
/*    */     {
/* 60 */       if (this.fields[i].equals(paramInt))
/* 61 */         return this.fields[i];
/*    */     }
/* 63 */     return null;
/*    */   }
/*    */ 
/*    */   Enumeration getFields()
/*    */   {
/* 68 */     return new TIFFieldEnumerator(this);
/*    */   }
/*    */ 
/*    */   public void read(int paramInt, SeekInputStream paramSeekInputStream)
/*    */     throws IOException
/*    */   {
/* 38 */     paramSeekInputStream.seek(paramInt);
/* 39 */     this.count = paramSeekInputStream.readUnsignedShort();
/*    */ 
/* 41 */     this.fields = new TIFField[this.count];
/* 42 */     for (int i = 0; i < this.count; i++)
/*    */     {
/* 44 */       this.fields[i] = new TIFField();
/* 45 */       this.fields[i].read(paramSeekInputStream);
/*    */     }
/*    */ 
/* 48 */     this.offset = paramSeekInputStream.readInt();
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.decoder.tiff.IFD
 * JD-Core Version:    0.6.2
 */