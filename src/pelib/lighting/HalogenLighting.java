/*    */ package pelib.lighting;
/*    */ 
/*    */ public class HalogenLighting
/*    */   implements LightingAlgorithm
/*    */ {
/*    */   public int getAdjustedColour(int colour)
/*    */   {
/* 23 */     double modifier = 1.0D;
/*    */ 
/* 25 */     int r = colour & 0xFF;
/*    */ 
/* 27 */     int g = colour >> 8 & 0xFF;
/*    */ 
/* 29 */     int b = colour >> 16 & 0xFF;
/*    */ 
/* 31 */     r = (int)(r * 1.0D * modifier);
/*    */ 
/* 33 */     g = (int)(g * 0.9708210229873657D * modifier);
/*    */ 
/* 35 */     b = (int)(b * 0.8606539965675903D * modifier);
/*    */ 
/* 37 */     return r & 0xFF | (g & 0xFF) << 8 | (b & 0xFF) << 16;
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.lighting.HalogenLighting
 * JD-Core Version:    0.6.2
 */