/*     */ package pelib;
/*     */ 
/*     */ public class ImageFillMask extends ImageByte
/*     */ {
/*     */   public ImageFillMask(ImageFloat gradientImage, float percentile)
/*     */   {
/*  12 */     super(gradientImage.getWidth(), gradientImage.getHeight());
/*  13 */     autoTolerance(gradientImage, percentile / 100.0F);
/*     */   }
/*     */ 
/*     */   private ImageFillMask(ImageFillMask compatible)
/*     */   {
/*  18 */     super(compatible.width, compatible.height);
/*     */   }
/*     */ 
/*     */   public ImageFillMask(int width, int height)
/*     */   {
/*  23 */     super(width, height);
/*     */   }
/*     */ 
/*     */   private void autoTolerance(ImageFloat gradientImage, float target)
/*     */   {
/*  28 */     float minValue = 0.0F;
/*  29 */     float maxValue = 0.5F;
/*  30 */     float error = 0.02F;
/*  31 */     int iterations = 0;
/*     */     do
/*     */     {
/*  36 */       float newValue = (minValue + maxValue) / 2.0F;
/*  37 */       float white = createMask(gradientImage, newValue) / this.data.length;
/*     */ 
/*  39 */       if (white > target)
/*  40 */         minValue = newValue;
/*     */       else
/*  42 */         maxValue = newValue;
/*  43 */       if (Math.abs(white - target) <= error) break; iterations++; } while (iterations < 10);
/*     */   }
/*     */ 
/*     */   private int createMask(ImageFloat gradientImage, float tolerance)
/*     */   {
/*  48 */     float[] gradData = gradientImage.getBufferFloat();
/*  49 */     int count = 0;
/*     */ 
/*  51 */     for (int i = 0; i < this.data.length; i++)
/*     */     {
/*  53 */       this.data[i] = ((byte)(gradData[i] > tolerance ? 1 : 0));
/*  54 */       if (this.data[i] != 0)
/*  55 */         count++;
/*     */     }
/*  57 */     return count;
/*     */   }
/*     */ 
/*     */   public ImageFillMask fillWithLimit(byte[] flags, int x, int y, Area affectedArea)
/*     */   {
/*  64 */     ImageFillMask out = new ImageFillMask(this);
/*  65 */     byte[] outData = out.data;
/*  66 */     int pitch = this.width;
/*     */ 
/*  68 */     StackInteger todo = new StackInteger();
/*  69 */     todo.push(x); todo.push(y);
/*     */     int ny;
/* 113 */     for (; !todo.empty(); 
/* 113 */       todo.push(ny))
/*     */     {
/*  73 */       y = todo.pop(); x = todo.pop();
/*  74 */       outData[(y * pitch + x)] = 1;
/*  75 */       affectedArea.bound(x, y);
/*     */ 
/*  80 */       int nx = x - 1; ny = y;
/*  81 */       if ((nx >= 0) && (this.data[(ny * pitch + nx)] == 0) && (outData[(ny * pitch + nx)] == 0) && (flags[(ny * pitch + nx)] >= 1))
/*     */       {
/*  86 */         todo.push(nx); todo.push(ny);
/*     */       }
/*     */ 
/*  89 */       nx = x + 1; ny = y;
/*  90 */       if ((nx < this.width) && (this.data[(ny * pitch + nx)] == 0) && (outData[(ny * pitch + nx)] == 0) && (flags[(ny * pitch + nx)] >= 1))
/*     */       {
/*  95 */         todo.push(nx); todo.push(ny);
/*     */       }
/*     */ 
/*  98 */       nx = x; ny = y - 1;
/*  99 */       if ((ny >= 0) && (this.data[(ny * pitch + nx)] == 0) && (outData[(ny * pitch + nx)] == 0) && (flags[(ny * pitch + nx)] >= 1))
/*     */       {
/* 104 */         todo.push(nx); todo.push(ny);
/*     */       }
/*     */ 
/* 107 */       nx = x; ny = y + 1;
/* 108 */       if ((ny < this.height) && (this.data[(ny * pitch + nx)] == 0) && (outData[(ny * pitch + nx)] == 0) && (flags[(ny * pitch + nx)] >= 1))
/*     */       {
/* 113 */         todo.push(nx);
/*     */       }
/*     */     }
/*     */ 
/* 117 */     return out;
/*     */   }
/*     */ 
/*     */   public ImageFillMask fill(int x, int y)
/*     */   {
/* 132 */     ImageFillMask out = new ImageFillMask(this);
/* 133 */     byte[] outData = out.data;
/* 134 */     int pitch = this.width;
/*     */ 
/* 136 */     StackInteger todo = new StackInteger();
/* 137 */     todo.push(x); todo.push(y);
/*     */     int ny;
/* 176 */     for (; !todo.empty(); 
/* 176 */       todo.push(ny))
/*     */     {
/* 141 */       y = todo.pop(); x = todo.pop();
/* 142 */       outData[(y * pitch + x)] = 1;
/*     */ 
/* 147 */       int nx = x - 1; ny = y;
/* 148 */       if ((nx >= 0) && (this.data[(ny * pitch + nx)] == 0) && (outData[(ny * pitch + nx)] == 0))
/*     */       {
/* 152 */         todo.push(nx); todo.push(ny);
/*     */       }
/*     */ 
/* 155 */       nx = x + 1; ny = y;
/* 156 */       if ((nx < this.width) && (this.data[(ny * pitch + nx)] == 0) && (outData[(ny * pitch + nx)] == 0))
/*     */       {
/* 160 */         todo.push(nx); todo.push(ny);
/*     */       }
/*     */ 
/* 163 */       nx = x; ny = y - 1;
/* 164 */       if ((ny >= 0) && (this.data[(ny * pitch + nx)] == 0) && (outData[(ny * pitch + nx)] == 0))
/*     */       {
/* 168 */         todo.push(nx); todo.push(ny);
/*     */       }
/*     */ 
/* 171 */       nx = x; ny = y + 1;
/* 172 */       if ((ny < this.height) && (this.data[(ny * pitch + nx)] == 0) && (outData[(ny * pitch + nx)] == 0))
/*     */       {
/* 176 */         todo.push(nx);
/*     */       }
/*     */     }
/*     */ 
/* 180 */     return out;
/*     */   }
/*     */ 
/*     */   public ImageFillMask clone()
/*     */   {
/* 185 */     ImageFillMask img = new ImageFillMask(this);
/* 186 */     System.arraycopy(this.data, 0, img.data, 0, this.data.length);
/* 187 */     return img;
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.ImageFillMask
 * JD-Core Version:    0.6.2
 */