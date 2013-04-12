/*    */ package com.sun.jimi.core.decoder.ico;
/*    */ 
/*    */ import com.sun.jimi.core.util.LEDataInputStream;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class IconDir
/*    */ {
/*    */   private int idReserved_;
/*    */   private int idType_;
/*    */   private int idCount_;
/*    */   private IconDirEntry[] iconDirEntries_;
/*    */ 
/*    */   public IconDir(LEDataInputStream paramLEDataInputStream)
/*    */     throws IOException
/*    */   {
/* 46 */     this.idReserved_ = paramLEDataInputStream.readShort();
/*    */ 
/* 48 */     this.idType_ = paramLEDataInputStream.readShort();
/*    */ 
/* 50 */     this.idCount_ = paramLEDataInputStream.readShort();
/*    */ 
/* 52 */     this.iconDirEntries_ = new IconDirEntry[this.idCount_];
/*    */ 
/* 54 */     for (int i = 0; i < this.idCount_; i++)
/*    */     {
/* 56 */       this.iconDirEntries_[i] = new IconDirEntry(paramLEDataInputStream);
/*    */     }
/*    */   }
/*    */ 
/*    */   public int getCount()
/*    */   {
/* 63 */     return this.idCount_;
/*    */   }
/*    */ 
/*    */   public IconDirEntry getEntry(int paramInt)
/*    */   {
/* 68 */     return this.iconDirEntries_[paramInt];
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 73 */     return "idReserved: " + this.idReserved_ + " idType: " + this.idType_ + " idCount: " + this.idCount_;
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.decoder.ico.IconDir
 * JD-Core Version:    0.6.2
 */