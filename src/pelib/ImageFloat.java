/*     */ package pelib;
/*     */ 
/*     */ import java.awt.Image;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.image.WritableRaster;
/*     */ 
/*     */ public class ImageFloat extends ImageBase
/*     */ {
/*     */   private float[] data;
/*     */ 
/*     */   public ImageFloat(int width, int height)
/*     */   {
/*  19 */     super(width, height);
/*  20 */     this.data = new float[width * height];
/*     */   }
/*     */ 
/*     */   public ImageFloat(ImageFloat src)
/*     */   {
/*  25 */     this(src.width, src.height);
/*  26 */     System.arraycopy(src.data, 0, this.data, 0, this.data.length);
/*     */   }
/*     */ 
/*     */   public Object getBufferNative()
/*     */   {
/*  31 */     return this.data;
/*     */   }
/*     */ 
/*     */   public final float[] getBufferFloat()
/*     */   {
/*  36 */     return this.data;
/*     */   }
/*     */ 
/*     */   private void fillColourBuffer(int[] intData)
/*     */   {
/*  42 */     float min = 3.4028235E+38F;
/*  43 */     float max = 1.4E-45F;
/*  44 */     for (int i = 0; i < this.data.length; i++)
/*     */     {
/*  46 */       if (this.data[i] < min)
/*  47 */         min = this.data[i];
/*  48 */       if (this.data[i] > max) {
/*  49 */         max = this.data[i];
/*     */       }
/*     */     }
/*     */ 
/*  53 */     float width = max - min;
/*  54 */     for (int i = 0; i < this.data.length; i++)
/*     */     {
/*  56 */       int colour = (int)((this.data[i] - min) / width * 256.0F);
/*  57 */       if (colour > 255)
/*  58 */         colour = 255;
/*  59 */       intData[i] = (colour | colour << 8 | colour << 16);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Image getAWTImage()
/*     */   {
/*  65 */     if (this.image == null) {
/*  66 */       this.image = new BufferedImage(this.width, this.height, 4);
/*     */     }
/*     */ 
/*  69 */     int[] intData = new int[this.width * this.height];
/*  70 */     fillColourBuffer(intData);
/*     */ 
/*  72 */     WritableRaster raster = this.image.getRaster();
/*  73 */     raster.setDataElements(0, 0, this.width, this.height, intData);
/*     */ 
/*  75 */     return this.image;
/*     */   }
/*     */ 
/*     */   public ImageColour createColourImage()
/*     */   {
/*  80 */     ImageColour dest = new ImageColour(this.width, this.height);
/*  81 */     int[] destBuffer = dest.getBufferIntBGR();
/*     */ 
/*  83 */     fillColourBuffer(destBuffer);
/*     */ 
/*  85 */     return dest;
/*     */   }
/*     */ 
/*     */   public BufferedImage createCompatibleBufferedImage()
/*     */   {
/*  90 */     return null;
/*     */   }
/*     */ 
/*     */   public ImageBase copy()
/*     */   {
/* 111 */     return new ImageFloat(this);
/*     */   }
/*     */ 
/*     */   public void add(ImageFloat src)
/*     */   {
/* 116 */     assert (src.data.length == this.data.length);
/*     */ 
/* 118 */     for (int i = 0; i < this.data.length; i++)
/* 119 */       this.data[i] += src.data[i];
/*     */   }
/*     */ 
/*     */   public void addAbsolute(ImageFloat src)
/*     */   {
/* 128 */     assert (src.data.length == this.data.length);
/*     */ 
/* 130 */     for (int i = 0; i < this.data.length; i++)
/* 131 */       this.data[i] = (Math.abs(this.data[i]) + Math.abs(src.data[i]));
/*     */   }
/*     */ 
/*     */   public ImageBase createCompatibleImage()
/*     */   {
/* 137 */     return new ImageFloat(this.width, this.height);
/*     */   }
/*     */ 
/*     */   public final float getNearestPixel(int x, int y)
/*     */   {
/* 142 */     return this.data[(Math.max(Math.min(y, this.height - 1), 0) * this.width + Math.max(Math.min(x, this.width - 1), 0))];
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.ImageFloat
 * JD-Core Version:    0.6.2
 */