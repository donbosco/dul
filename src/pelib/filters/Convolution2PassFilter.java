/*     */ package pelib.filters;
/*     */ 
/*     */ import java.awt.Rectangle;
/*     */ import pelib.ImageColour;
/*     */ 
/*     */ public abstract class Convolution2PassFilter extends ResizingFilter
/*     */ {
/*     */   protected ConvolutionKernel1D kernel;
/*     */   protected int kernelWidth;
/*     */ 
/*     */   protected Convolution2PassFilter(int width)
/*     */   {
/*  22 */     this.kernelWidth = width;
/*     */   }
/*     */ 
/*     */   public void filter(ImageColour src, ImageColour dest, Rectangle srcRect, Rectangle destRect)
/*     */   {
/*  28 */     int srcWidth = srcRect.width;
/*  29 */     int srcPitch = src.getPitch();
/*  30 */     int srcHeight = srcRect.height;
/*  31 */     int[] srcData = src.getBufferIntBGR();
/*     */ 
/*  33 */     int destWidth = destRect.width;
/*  34 */     int destPitch = dest.getPitch();
/*  35 */     int destHeight = destRect.height;
/*  36 */     int[] destData = dest.getBufferIntBGR();
/*     */ 
/*  38 */     ImageColour tempImage = new ImageColour(destWidth, srcHeight);
/*     */ 
/*  41 */     convolveRows(src, tempImage, srcRect);
/*     */ 
/*  44 */     convolveColumns(tempImage, dest, destRect);
/*     */   }
/*     */ 
/*     */   private void convolveRows(ImageColour srcImage, ImageColour destImage, Rectangle srcRect)
/*     */   {
/*  50 */     int srcPitch = srcImage.getPitch();
/*  51 */     int destPitch = destImage.getPitch();
/*  52 */     int srcWidth = srcRect.width;
/*  53 */     int destWidth = destImage.getWidth();
/*  54 */     int height = srcRect.height;
/*  55 */     int[] src = srcImage.getBufferIntBGR();
/*  56 */     int[] dest = destImage.getBufferIntBGR();
/*     */ 
/*  58 */     Contribution[] contribs = createLineWeights(srcWidth, destWidth, srcRect.x);
/*     */ 
/*  61 */     for (int row = 0; row < height; row++)
/*     */     {
/*  63 */       int srcRow = row + srcRect.y;
/*  64 */       for (int col = 0; col < destWidth; col++)
/*     */       {
/*  66 */         float red = 0.0F;
/*  67 */         float green = 0.0F;
/*  68 */         float blue = 0.0F;
/*  69 */         for (int srcCol = contribs[col].left; 
/*  70 */           srcCol <= contribs[col].right; 
/*  71 */           srcCol++)
/*     */         {
/*  73 */           red += contribs[col].weights[(srcCol - contribs[col].left)] * (src[(srcRow * srcPitch + srcCol)] & 0xFF);
/*     */ 
/*  77 */           green += contribs[col].weights[(srcCol - contribs[col].left)] * (src[(srcRow * srcPitch + srcCol)] >> 8 & 0xFF);
/*     */ 
/*  81 */           blue += contribs[col].weights[(srcCol - contribs[col].left)] * (src[(srcRow * srcPitch + srcCol)] >> 16 & 0xFF);
/*     */         }
/*     */ 
/*  86 */         int r = red > 255.0F ? 255 : (int)red;
/*  87 */         int b = red > 255.0F ? 255 : (int)blue;
/*  88 */         int g = red > 255.0F ? 255 : (int)green;
/*  89 */         dest[(row * destPitch + col)] = (r | g << 8 | b << 16);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void convolveColumns(ImageColour srcImage, ImageColour destImage, Rectangle destRect)
/*     */   {
/*  98 */     int srcPitch = srcImage.getPitch();
/*  99 */     int destPitch = destImage.getPitch();
/* 100 */     int width = srcImage.getWidth();
/* 101 */     int srcHeight = srcImage.getHeight();
/* 102 */     int destHeight = destRect.height;
/* 103 */     int[] src = srcImage.getBufferIntBGR();
/* 104 */     int[] dest = destImage.getBufferIntBGR();
/*     */ 
/* 106 */     Contribution[] contribs = createLineWeights(srcHeight, destHeight, 0);
/* 107 */     for (int row = 0; row < destHeight; row++)
/*     */     {
/* 109 */       int destRow = row + destRect.y;
/* 110 */       for (int col = 0; col < width; col++)
/*     */       {
/* 112 */         int destCol = col + destRect.x;
/* 113 */         float red = 0.0F;
/* 114 */         float green = 0.0F;
/* 115 */         float blue = 0.0F;
/* 116 */         for (int srcRow = contribs[row].left; 
/* 117 */           srcRow <= contribs[row].right; 
/* 118 */           srcRow++)
/*     */         {
/* 120 */           red += contribs[row].weights[(srcRow - contribs[row].left)] * (src[(srcRow * srcPitch + col)] & 0xFF);
/*     */ 
/* 124 */           green += contribs[row].weights[(srcRow - contribs[row].left)] * (src[(srcRow * srcPitch + col)] >> 8 & 0xFF);
/*     */ 
/* 128 */           blue += contribs[row].weights[(srcRow - contribs[row].left)] * (src[(srcRow * srcPitch + col)] >> 16 & 0xFF);
/*     */         }
/*     */ 
/* 133 */         int r = red > 255.0F ? 255 : (int)red;
/* 134 */         int b = red > 255.0F ? 255 : (int)blue;
/* 135 */         int g = red > 255.0F ? 255 : (int)green;
/* 136 */         dest[(destRow * destPitch + destCol)] = (r | g << 8 | b << 16);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private Contribution[] createLineWeights(int srcWidth, int destWidth, int srcOffset)
/*     */   {
/* 150 */     float invScale = srcWidth / destWidth;
/* 151 */     int windowSize = this.kernelWidth * 2 + 1;
/*     */ 
/* 153 */     Contribution[] contrib = new Contribution[destWidth];
/* 154 */     for (int i = 0; i < destWidth; i++)
/*     */     {
/* 156 */       contrib[i] = new Contribution();
/*     */ 
/* 158 */       float center = i * invScale + srcOffset;
/* 159 */       contrib[i].left = Math.max(0, (int)(center - this.kernelWidth));
/* 160 */       contrib[i].right = Math.min((int)(center + this.kernelWidth + 1.0F), srcWidth + srcOffset - 1);
/*     */ 
/* 163 */       if (contrib[i].right - contrib[i].left + 1 > windowSize)
/*     */       {
/* 165 */         if (contrib[i].left < srcWidth + srcOffset - 1)
/* 166 */           contrib[i].left += 1;
/*     */         else {
/* 168 */           contrib[i].right -= 1;
/*     */         }
/*     */       }
/* 171 */       float totalWeight = 0.0F;
/* 172 */       contrib[i].weights = new float[contrib[i].right - contrib[i].left + 1];
/*     */ 
/* 174 */       for (int j = contrib[i].left; j <= contrib[i].right; j++)
/*     */       {
/* 176 */         totalWeight += (contrib[i].weights[(j - contrib[i].left)] = this.kernel.getWeight(Math.abs(center - j)));
/*     */       }
/*     */ 
/* 182 */       for (int j = 0; j < contrib[i].weights.length; j++) {
/* 183 */         contrib[i].weights[j] /= totalWeight;
/*     */       }
/*     */     }
/* 186 */     return contrib;
/*     */   }
/*     */ 
/*     */   public ConvolutionKernel1D getKernel()
/*     */   {
/* 198 */     return this.kernel;
/*     */   }
/*     */ 
/*     */   private class Contribution
/*     */   {
/*     */     float[] weights;
/*     */     int left;
/*     */     int right;
/*     */ 
/*     */     private Contribution()
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.filters.Convolution2PassFilter
 * JD-Core Version:    0.6.2
 */