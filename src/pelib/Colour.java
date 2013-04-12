/*     */ package pelib;
/*     */ 
/*     */ import java.awt.Color;
/*     */ 
/*     */ public class Colour
/*     */ {
/*     */   public static float getLuminance(int colour)
/*     */   {
/*  17 */     int red = colour & 0xFF;
/*  18 */     int green = colour >> 8 & 0xFF;
/*  19 */     int blue = colour >> 16 & 0xFF;
/*  20 */     return 0.21267F * red + 0.71516F * green + 0.07217F * blue;
/*     */   }
/*     */ 
/*     */   public static float getIntensity(int colour)
/*     */   {
/*  25 */     int red = colour & 0xFF;
/*  26 */     int green = colour >> 8 & 0xFF;
/*  27 */     int blue = colour >> 16 & 0xFF;
/*  28 */     return (red + green + blue) / 3.0F;
/*     */   }
/*     */ 
/*     */   public static int getMagnitude(int colour)
/*     */   {
/*  33 */     int red = colour & 0xFF;
/*  34 */     int green = colour >> 8 & 0xFF;
/*  35 */     int blue = colour >> 16 & 0xFF;
/*  36 */     return red + green + blue;
/*     */   }
/*     */ 
/*     */   public static int createColour(int red, int green, int blue)
/*     */   {
/*  41 */     return red & 0xFF | (green & 0xFF) << 8 | (blue & 0xFF) << 16;
/*     */   }
/*     */ 
/*     */   public static int createColour(Color colour)
/*     */   {
/*  46 */     return colour.getRed() & 0xFF | (colour.getGreen() & 0xFF) << 8 | (colour.getBlue() & 0xFF) << 16;
/*     */   }
/*     */ 
/*     */   static int adjust(int c, int m1, int m2, int f)
/*     */   {
/*  57 */     int red = c & 0xFF;
/*  58 */     int green = c >> 8 & 0xFF;
/*  59 */     int blue = c >> 16 & 0xFF;
/*  60 */     red *= 256;
/*  61 */     green *= 256;
/*  62 */     blue *= 256;
/*  63 */     int m1_red = m1 & 0xFF;
/*  64 */     int m1_green = m1 >> 8 & 0xFF;
/*  65 */     int m1_blue = m1 >> 16 & 0xFF;
/*  66 */     int m2_red = m2 & 0xFF;
/*  67 */     int m2_green = m2 >> 8 & 0xFF;
/*  68 */     int m2_blue = m2 >> 16 & 0xFF;
/*  69 */     red += f * (m1_red - m2_red);
/*  70 */     green += f * (m1_green - m2_green);
/*  71 */     blue += f * (m1_blue - m2_blue);
/*  72 */     red /= 256;
/*  73 */     green /= 256;
/*  74 */     blue /= 256;
/*  75 */     red = Math.max(Math.min(red, 255), 0);
/*  76 */     green = Math.max(Math.min(green, 255), 0);
/*  77 */     blue = Math.max(Math.min(blue, 255), 0);
/*  78 */     return red & 0xFF | (green & 0xFF) << 8 | (blue & 0xFF) << 16;
/*     */   }
/*     */ 
/*     */   static int blend(int c1, int c2, int weight)
/*     */   {
/*  86 */     int red = (c1 & 0xFF) * weight + (c2 & 0xFF) * (256 - weight);
/*     */ 
/*  88 */     int green = (c1 >> 8 & 0xFF) * weight + (c2 >> 8 & 0xFF) * (256 - weight);
/*     */ 
/*  90 */     int blue = (c1 >> 16 & 0xFF) * weight + (c2 >> 16 & 0xFF) * (256 - weight);
/*     */ 
/*  92 */     red /= 256;
/*  93 */     green /= 256;
/*  94 */     blue /= 256;
/*  95 */     red = Math.min(red, 255);
/*  96 */     blue = Math.min(blue, 255);
/*  97 */     green = Math.min(green, 255);
/*  98 */     return red & 0xFF | (green & 0xFF) << 8 | (blue & 0xFF) << 16;
/*     */   }
/*     */ 
/*     */   public static int createRandomColour(int seed)
/*     */   {
/* 105 */     int emph1 = seed % 3;
/* 106 */     int emph2 = (emph1 + 1) % 3;
/* 107 */     int mc = seed % 256;
/* 108 */     int sc = seed % 37 * 4711 % 256;
/* 109 */     return mc << emph1 * 8 | sc << emph2 * 8 | 0x505050;
/*     */   }
/*     */ 
/*     */   public static Color getAWTColor(int colour)
/*     */   {
/* 114 */     return new Color(colour & 0xFF, colour >> 8 & 0xFF, colour >> 16 & 0xFF);
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.Colour
 * JD-Core Version:    0.6.2
 */