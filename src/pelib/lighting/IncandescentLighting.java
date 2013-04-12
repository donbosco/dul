/*    */ package pelib.lighting;
/*    */ 
/*    */ public class IncandescentLighting
/*    */   implements LightingAlgorithm
/*    */ {
/*    */   public int getAdjustedColour(int colour)
/*    */   {
/* 23 */     double modifier = 0.95D;
/*    */ 
/* 25 */     int r = colour & 0xFF;
/*    */ 
/* 27 */     int g = colour >> 8 & 0xFF;
/*    */ 
/* 29 */     int b = colour >> 16 & 0xFF;
/*    */ 
/* 31 */     r = (int)(r * 1.0D * modifier);
/*    */ 
/* 33 */     g = (int)(g * 0.9577029943466187D * modifier);
/*    */ 
/* 35 */     b = (int)(b * 0.8416479825973511D * modifier);
/*    */ 
/* 37 */     return r & 0xFF | (g & 0xFF) << 8 | (b & 0xFF) << 16;
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.lighting.IncandescentLighting
 * JD-Core Version:    0.6.2
 */