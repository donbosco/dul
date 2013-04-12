/*    */ package com.sun.jimi.core.decoder.ico;
/*    */ 
/*    */ import com.sun.jimi.core.util.LEDataInputStream;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class RGBQuad
/*    */ {
/*    */   private short blue_;
/*    */   private short green_;
/*    */   private short red_;
/*    */   private short reserved_;
/*    */ 
/*    */   public RGBQuad(LEDataInputStream paramLEDataInputStream)
/*    */     throws IOException
/*    */   {
/* 35 */     this.blue_ = ((short)paramLEDataInputStream.readUnsignedByte());
/* 36 */     this.green_ = ((short)paramLEDataInputStream.readUnsignedByte());
/* 37 */     this.red_ = ((short)paramLEDataInputStream.readUnsignedByte());
/* 38 */     this.reserved_ = ((short)paramLEDataInputStream.readUnsignedByte());
/*    */   }
/*    */ 
/*    */   public short getBlue()
/*    */   {
/* 53 */     return this.blue_;
/*    */   }
/*    */ 
/*    */   public short getGreen()
/*    */   {
/* 48 */     return this.green_;
/*    */   }
/*    */ 
/*    */   public short getRed()
/*    */   {
/* 43 */     return this.red_;
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.decoder.ico.RGBQuad
 * JD-Core Version:    0.6.2
 */