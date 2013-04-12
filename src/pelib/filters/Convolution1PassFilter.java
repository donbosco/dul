/*    */ package pelib.filters;
/*    */ 
/*    */ import pelib.ImageColour;
/*    */ import pelib.ImageFloat;
/*    */ 
/*    */ public class Convolution1PassFilter extends Filter
/*    */ {
/*    */   private ConvolutionKernel2D kernel;
/*    */ 
/*    */   public Convolution1PassFilter(ConvolutionKernel2D kernel)
/*    */   {
/* 17 */     this.kernel = kernel;
/*    */   }
/*    */ 
/*    */   public void filter(ImageColour src, ImageColour dest)
/*    */   {
/* 22 */     int width = src.getWidth();
/* 23 */     int height = src.getHeight();
/* 24 */     int pitch = src.getPitch();
/* 25 */     int[] srcData = src.getBufferIntBGR();
/* 26 */     int[] destData = dest.getBufferIntBGR();
/*    */ 
/* 28 */     int kw = this.kernel.getWidth() / 2;
/* 29 */     int kh = this.kernel.getHeight() / 2;
/* 30 */     float[] k = this.kernel.getData();
/*    */ 
/* 32 */     for (int y = 0; y < height; y++)
/*    */     {
/* 34 */       for (int x = 0; x < width; x++)
/*    */       {
/* 36 */         float red = 0.0F;
/* 37 */         float green = 0.0F;
/* 38 */         float blue = 0.0F;
/* 39 */         int i = 0;
/* 40 */         for (int t = -kh; t <= kh; t++)
/*    */         {
/* 42 */           for (int s = -kw; s <= kw; s++)
/*    */           {
/* 44 */             int colour = src.getNearestPixel(x + s, y + t);
/* 45 */             red += k[i] * (colour & 0xFF);
/* 46 */             green += k[i] * (colour >> 8 & 0xFF);
/* 47 */             blue += k[i] * (colour >> 16 & 0xFF);
/* 48 */             i++;
/*    */           }
/*    */         }
/* 51 */         int r = red > 255.0F ? 255 : (int)red;
/* 52 */         int b = red > 255.0F ? 255 : (int)blue;
/* 53 */         int g = red > 255.0F ? 255 : (int)green;
/* 54 */         r = r < 0 ? 0 : r;
/* 55 */         b = b < 0 ? 0 : b;
/* 56 */         g = g < 0 ? 0 : g;
/* 57 */         destData[(y * pitch + x)] = (r | g << 8 | b << 16);
/*    */       }
/*    */     }
/*    */   }
/*    */ 
/*    */   public void filter(ImageFloat src, ImageFloat dest)
/*    */   {
/* 64 */     int width = src.getWidth();
/* 65 */     int height = src.getHeight();
/* 66 */     int pitch = src.getPitch();
/* 67 */     float[] srcData = src.getBufferFloat();
/* 68 */     float[] destData = dest.getBufferFloat();
/*    */ 
/* 70 */     int kw = this.kernel.getWidth() / 2;
/* 71 */     int kh = this.kernel.getHeight() / 2;
/* 72 */     float[] k = this.kernel.getData();
/*    */ 
/* 74 */     for (int y = 0; y < height; y++)
/*    */     {
/* 76 */       for (int x = 0; x < width; x++)
/*    */       {
/* 78 */         float val = 0.0F;
/* 79 */         int i = 0;
/* 80 */         for (int t = -kh; t <= kh; t++)
/*    */         {
/* 82 */           for (int s = -kw; s <= kw; s++)
/*    */           {
/* 84 */             val += k[i] * src.getNearestPixel(x + s, y + t);
/* 85 */             i++;
/*    */           }
/*    */         }
/* 88 */         destData[(y * pitch + x)] = val;
/*    */       }
/*    */     }
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.filters.Convolution1PassFilter
 * JD-Core Version:    0.6.2
 */