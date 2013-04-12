/*    */ package pelib.filters;
/*    */ 
/*    */ import pelib.Colour;
/*    */ import pelib.Histogram;
/*    */ import pelib.ImageColour;
/*    */ 
/*    */ public class MedianFilter extends Filter
/*    */ {
/*    */   private int size;
/*    */   private float tolerance;
/*    */ 
/*    */   public MedianFilter()
/*    */   {
/* 17 */     this(1, 0.3F);
/*    */   }
/*    */ 
/*    */   public MedianFilter(int size, float tolerance)
/*    */   {
/* 26 */     this.size = size;
/* 27 */     this.tolerance = tolerance;
/*    */   }
/*    */ 
/*    */   private float abs(float x)
/*    */   {
/* 32 */     return x < 0.0F ? -x : x;
/*    */   }
/*    */ 
/*    */   public void filter(ImageColour src, ImageColour dest)
/*    */   {
/* 38 */     Histogram r = new Histogram(8);
/* 39 */     Histogram g = new Histogram(8);
/* 40 */     Histogram b = new Histogram(8);
/*    */ 
/* 42 */     int width = src.getWidth();
/* 43 */     int pitch = src.getPitch();
/* 44 */     int height = src.getHeight();
/* 45 */     int[] srcData = src.getBufferIntBGR();
/* 46 */     int[] destData = dest.getBufferIntBGR();
/*    */ 
/* 48 */     for (int y = 0; y < height; y++)
/*    */     {
/* 50 */       for (int x = 0; x < width; x++)
/*    */       {
/* 52 */         r.clear();
/* 53 */         g.clear();
/* 54 */         b.clear();
/*    */ 
/* 56 */         for (int j = -this.size; j <= this.size; j++)
/*    */         {
/* 58 */           for (int i = -this.size; i <= this.size; i++)
/*    */           {
/* 60 */             int colour = src.getNearestPixel(x + i, y + j);
/* 61 */             r.addSample(colour & 0xFF);
/* 62 */             g.addSample(colour >> 8 & 0xFF);
/* 63 */             b.addSample(colour >> 16 & 0xFF);
/*    */           }
/* 65 */           int out = Colour.createColour(r.getMedian(), g.getMedian(), b.getMedian());
/*    */ 
/* 68 */           if (this.tolerance != 0.0F)
/*    */           {
/* 70 */             int c = srcData[(y * pitch + x)];
/* 71 */             float a = Colour.getIntensity(out);
/* 72 */             float d = Colour.getIntensity(c);
/* 73 */             if (abs(a - d) > d * this.tolerance)
/* 74 */               out = c;
/*    */           }
/* 76 */           destData[(y * pitch + x)] = out;
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.filters.MedianFilter
 * JD-Core Version:    0.6.2
 */