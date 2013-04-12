/*     */ package pelib.filters;
/*     */ 
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.ImageConsumer;
/*     */ import java.awt.image.ImageProducer;
/*     */ import java.util.Hashtable;
/*     */ import pelib.ImageColour;
/*     */ 
/*     */ public class ResizingConsumer
/*     */   implements ImageConsumer
/*     */ {
/*     */   private ImageProducer producer;
/*     */   private int nominalWidth;
/*     */   private int nominalHeight;
/*     */   private ConvolutionKernel1D kernel;
/*     */   private ImageColour resizedImage;
/*     */   private Contribution[] contributions;
/*     */   private ImageColour intermediateImage;
/*     */   private int[] intermediateData;
/*     */   private boolean finished;
/*     */   private static final int kernelWidth = 1;
/*     */   private Object mutex;
/*     */   private ColorModel currentModel;
/*     */   private int redShift;
/*     */   private int blueShift;
/*     */   private int greenShift;
/*     */ 
/*     */   public ResizingConsumer(ImageProducer producer, int nominalWidth, int nominalHeight)
/*     */   {
/*  79 */     this.producer = producer;
/*     */ 
/*  81 */     this.nominalWidth = nominalWidth;
/*     */ 
/*  83 */     this.nominalHeight = nominalHeight;
/*     */ 
/*  85 */     this.mutex = new Object();
/*     */ 
/*  87 */     this.finished = false;
/*     */ 
/*  93 */     this.kernel = new ConvolutionKernel1D()
/*     */     {
/*     */       public float getWeight(float distance)
/*     */       {
/*  99 */         return distance < 1.0F ? 1.0F - distance : 0.0F;
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   public void waitForCompletion()
/*     */   {
/* 113 */     synchronized (this.mutex)
/*     */     {
/* 115 */       while (!this.finished)
/*     */       {
/*     */         try
/*     */         {
/* 121 */           this.mutex.wait();
/*     */         }
/*     */         catch (InterruptedException e)
/*     */         {
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public ImageColour getResizedImage()
/*     */   {
/* 139 */     return this.resizedImage;
/*     */   }
/*     */ 
/*     */   private void notifyCompletion()
/*     */   {
/* 149 */     synchronized (this.mutex)
/*     */     {
/* 151 */       this.finished = true;
/*     */ 
/* 153 */       this.mutex.notifyAll();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void imageComplete(int status)
/*     */   {
/* 165 */     if ((status & 0x3) != 0)
/*     */     {
/* 169 */       if (this.intermediateImage == null)
/*     */       {
/* 173 */         abort();
/*     */ 
/* 175 */         return;
/*     */       }
/*     */ 
/* 181 */       this.contributions = null;
/*     */ 
/* 185 */       convolveColumns(this.intermediateImage, this.resizedImage);
/*     */ 
/* 189 */       this.producer.removeConsumer(this);
/*     */ 
/* 193 */       notifyCompletion();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setColorModel(ColorModel model)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void setDimensions(int width, int height)
/*     */   {
/* 211 */     float resizeWidth = this.nominalWidth / width;
/*     */ 
/* 213 */     float resizeHeight = this.nominalHeight / height;
/*     */ 
/* 215 */     float resize = Math.min(resizeWidth, resizeHeight);
/*     */ 
/* 219 */     int finalWidth = (int)(resize * width);
/*     */ 
/* 221 */     int finalHeight = (int)(resize * height);
/*     */ 
/* 225 */     this.intermediateImage = new ImageColour(finalWidth, height);
/*     */ 
/* 227 */     this.intermediateData = this.intermediateImage.getBufferIntBGR();
/*     */ 
/* 229 */     this.resizedImage = new ImageColour(finalWidth, finalHeight);
/*     */ 
/* 235 */     this.contributions = createLineWeights(width, finalWidth, 0);
/*     */   }
/*     */ 
/*     */   public void setHints(int hintflags)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void setPixels(int x, int y, int w, int h, ColorModel model, byte[] pixels, int off, int scansize)
/*     */   {
/* 253 */     abort();
/*     */   }
/*     */ 
/*     */   public void setPixels(int x, int y, int w, int h, ColorModel model, int[] pixels, int off, int scansize)
/*     */   {
/* 269 */     decodeColorModel(model);
/*     */ 
/* 277 */     for (int row = 0; row < h; row++)
/*     */     {
/* 281 */       int destPitch = this.contributions.length;
/*     */ 
/* 283 */       for (int col = 0; col < this.contributions.length; col++)
/*     */       {
/* 287 */         float red = 0.0F;
/*     */ 
/* 289 */         float green = 0.0F;
/*     */ 
/* 291 */         float blue = 0.0F;
/*     */ 
/* 293 */         for (int srcCol = this.contributions[col].left; 
/* 295 */           srcCol <= this.contributions[col].right; 
/* 297 */           srcCol++)
/*     */         {
/* 301 */           red += this.contributions[col].weights[(srcCol - this.contributions[col].left)] * (pixels[(row * scansize + srcCol + off)] >> this.redShift & 0xFF);
/*     */ 
/* 307 */           green += this.contributions[col].weights[(srcCol - this.contributions[col].left)] * (pixels[(row * scansize + srcCol + off)] >> this.greenShift & 0xFF);
/*     */ 
/* 313 */           blue += this.contributions[col].weights[(srcCol - this.contributions[col].left)] * (pixels[(row * scansize + srcCol + off)] >> this.blueShift & 0xFF);
/*     */         }
/*     */ 
/* 321 */         int r = red > 255.0F ? 255 : (int)red;
/*     */ 
/* 323 */         int b = red > 255.0F ? 255 : (int)blue;
/*     */ 
/* 325 */         int g = red > 255.0F ? 255 : (int)green;
/*     */ 
/* 327 */         this.intermediateData[((y + row) * destPitch + col)] = (r | g << 8 | b << 16);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setProperties(Hashtable props)
/*     */   {
/*     */   }
/*     */ 
/*     */   private void convolveColumns(ImageColour srcImage, ImageColour destImage)
/*     */   {
/* 355 */     int srcPitch = srcImage.getPitch();
/*     */ 
/* 357 */     int destPitch = destImage.getPitch();
/*     */ 
/* 359 */     int width = srcImage.getWidth();
/*     */ 
/* 361 */     int srcHeight = srcImage.getHeight();
/*     */ 
/* 363 */     int destHeight = destImage.getHeight();
/*     */ 
/* 365 */     int[] src = srcImage.getBufferIntBGR();
/*     */ 
/* 367 */     int[] dest = destImage.getBufferIntBGR();
/*     */ 
/* 371 */     Contribution[] contribs = createLineWeights(srcHeight, destHeight, 0);
/*     */ 
/* 373 */     for (int row = 0; row < destHeight; row++)
/*     */     {
/* 377 */       int destRow = row;
/*     */ 
/* 379 */       for (int col = 0; col < width; col++)
/*     */       {
/* 383 */         int destCol = col;
/*     */ 
/* 385 */         float red = 0.0F;
/*     */ 
/* 387 */         float green = 0.0F;
/*     */ 
/* 389 */         float blue = 0.0F;
/*     */ 
/* 391 */         for (int srcRow = contribs[row].left; 
/* 393 */           srcRow <= contribs[row].right; 
/* 395 */           srcRow++)
/*     */         {
/* 399 */           red += contribs[row].weights[(srcRow - contribs[row].left)] * (src[(srcRow * srcPitch + col)] & 0xFF);
/*     */ 
/* 407 */           green += contribs[row].weights[(srcRow - contribs[row].left)] * (src[(srcRow * srcPitch + col)] >> 8 & 0xFF);
/*     */ 
/* 415 */           blue += contribs[row].weights[(srcRow - contribs[row].left)] * (src[(srcRow * srcPitch + col)] >> 16 & 0xFF);
/*     */         }
/*     */ 
/* 425 */         int r = red > 255.0F ? 255 : (int)red;
/*     */ 
/* 427 */         int b = red > 255.0F ? 255 : (int)blue;
/*     */ 
/* 429 */         int g = red > 255.0F ? 255 : (int)green;
/*     */ 
/* 431 */         dest[(destRow * destPitch + destCol)] = (r | g << 8 | b << 16);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private Contribution[] createLineWeights(int srcWidth, int destWidth, int srcOffset)
/*     */   {
/* 457 */     float invScale = srcWidth / destWidth;
/*     */ 
/* 459 */     int windowSize = 3;
/*     */ 
/* 463 */     Contribution[] contrib = new Contribution[destWidth];
/*     */ 
/* 465 */     for (int i = 0; i < destWidth; i++)
/*     */     {
/* 469 */       contrib[i] = new Contribution();
/*     */ 
/* 473 */       float center = i * invScale + srcOffset;
/*     */ 
/* 475 */       contrib[i].left = Math.max(0, (int)(center - 1.0F));
/*     */ 
/* 477 */       contrib[i].right = Math.min((int)(center + 1.0F + 1.0F), srcWidth + srcOffset - 1);
/*     */ 
/* 483 */       if (contrib[i].right - contrib[i].left + 1 > windowSize)
/*     */       {
/* 487 */         if (contrib[i].left < srcWidth + srcOffset - 1)
/*     */         {
/* 489 */           contrib[i].left += 1;
/*     */         }
/*     */         else
/*     */         {
/* 493 */           contrib[i].right -= 1;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 499 */       float totalWeight = 0.0F;
/*     */ 
/* 501 */       contrib[i].weights = new float[contrib[i].right - contrib[i].left + 1];
/*     */ 
/* 505 */       for (int j = contrib[i].left; j <= contrib[i].right; j++)
/*     */       {
/* 509 */         totalWeight += (contrib[i].weights[(j - contrib[i].left)] = this.kernel.getWeight(Math.abs(center - j)));
/*     */       }
/*     */ 
/* 521 */       for (int j = 0; j < contrib[i].weights.length; j++)
/*     */       {
/* 523 */         contrib[i].weights[j] /= totalWeight;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 529 */     return contrib;
/*     */   }
/*     */ 
/*     */   private void abort()
/*     */   {
/* 559 */     this.producer.removeConsumer(this);
/*     */ 
/* 561 */     this.resizedImage = null;
/*     */ 
/* 563 */     notifyCompletion();
/*     */   }
/*     */ 
/*     */   private void decodeColorModel(ColorModel model)
/*     */   {
/* 583 */     int c1 = 204;
/*     */ 
/* 585 */     int c2 = 227;
/*     */ 
/* 587 */     int c3 = 85;
/*     */ 
/* 589 */     int col = 13427541;
/*     */ 
/* 591 */     switch (model.getRed(col))
/*     */     {
/*     */     case 204:
/* 597 */       this.redShift = 16;
/*     */ 
/* 599 */       break;
/*     */     case 227:
/* 603 */       this.redShift = 8;
/*     */ 
/* 605 */       break;
/*     */     case 85:
/* 609 */       this.redShift = 0;
/*     */ 
/* 611 */       break;
/*     */     default:
/* 615 */       abort();
/*     */     }
/*     */ 
/* 619 */     switch (model.getBlue(col))
/*     */     {
/*     */     case 204:
/* 625 */       this.blueShift = 16;
/*     */ 
/* 627 */       break;
/*     */     case 227:
/* 631 */       this.blueShift = 8;
/*     */ 
/* 633 */       break;
/*     */     case 85:
/* 637 */       this.blueShift = 0;
/*     */ 
/* 639 */       break;
/*     */     default:
/* 643 */       abort();
/*     */     }
/*     */ 
/* 647 */     switch (model.getGreen(col))
/*     */     {
/*     */     case 204:
/* 653 */       this.greenShift = 16;
/*     */ 
/* 655 */       break;
/*     */     case 227:
/* 659 */       this.greenShift = 8;
/*     */ 
/* 661 */       break;
/*     */     case 85:
/* 665 */       this.greenShift = 0;
/*     */ 
/* 667 */       break;
/*     */     default:
/* 671 */       abort();
/*     */     }
/*     */ 
/* 677 */     if ((this.blueShift == this.redShift) || (this.blueShift == this.greenShift) || (this.greenShift == this.redShift))
/*     */     {
/* 683 */       abort();
/*     */     }
/*     */     else
/*     */     {
/* 687 */       this.currentModel = model;
/*     */     }
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
 * Qualified Name:     pelib.filters.ResizingConsumer
 * JD-Core Version:    0.6.2
 */