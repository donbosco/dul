/*     */ package pelib;
/*     */ 
/*     */ import com.sun.jimi.core.Jimi;
/*     */ import java.awt.Container;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Image;
/*     */ import java.awt.MediaTracker;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.image.Raster;
/*     */ 
/*     */ public class ImageColour extends ImageBase
/*     */ {
/*     */   private int[] data;
/*     */ 
/*     */   public ImageColour(int width, int height)
/*     */   {
/*  24 */     super(width, height);
/*  25 */     this.data = new int[width * height];
/*     */   }
/*     */ 
/*     */   public ImageColour(Image srcImage)
/*     */     throws PaintExplorerFileFormatException
/*     */   {
/*     */     try
/*     */     {
/*  37 */       MediaTracker tracker = new MediaTracker(new Container());
/*  38 */       tracker.addImage(srcImage, 0);
/*  39 */       tracker.waitForAll();
/*     */     } catch (InterruptedException e) {
/*  41 */       this.image = null;
/*     */     }
/*     */ 
/*  44 */     this.width = srcImage.getWidth(null);
/*  45 */     this.height = srcImage.getHeight(null);
/*     */ 
/*  47 */     if (this.width < 0) {
/*  48 */       throw new PaintExplorerFileFormatException("ERR_LOAD_IMAGE");
/*     */     }
/*  50 */     this.data = new int[this.width * this.height];
/*     */ 
/*  53 */     this.image = createCompatibleBufferedImage();
/*  54 */     Graphics g = this.image.getGraphics();
/*  55 */     g.drawImage(srcImage, 0, 0, null);
/*  56 */     g.dispose();
/*     */ 
/*  59 */     int[] bdata = new int[this.width * this.height];
/*  60 */     this.image.getData().getDataElements(0, 0, this.width, this.height, bdata);
/*  61 */     System.arraycopy(bdata, 0, this.data, 0, bdata.length);
/*     */   }
/*     */ 
/*     */   public ImageColour(String filename)
/*     */     throws PaintExplorerFileFormatException
/*     */   {
/*  72 */     this(Jimi.getImage(filename, 2));
/*     */   }
/*     */ 
/*     */   public ImageColour(ImageColour source)
/*     */   {
/*  80 */     this(source.width, source.height);
/*  81 */     System.arraycopy(source.data, 0, this.data, 0, this.data.length);
/*     */   }
/*     */ 
/*     */   public final int getSamplesPerPixel()
/*     */   {
/*  86 */     return 1;
/*     */   }
/*     */ 
/*     */   public final int[] getBufferIntBGR()
/*     */   {
/*  95 */     return this.data;
/*     */   }
/*     */ 
/*     */   public Object getBufferNative()
/*     */   {
/* 100 */     return this.data;
/*     */   }
/*     */ 
/*     */   public ImageBase copy()
/*     */   {
/* 105 */     return new ImageColour(this);
/*     */   }
/*     */ 
/*     */   public ImageBase createCompatibleImage()
/*     */   {
/* 110 */     return new ImageColour(this.width, this.height);
/*     */   }
/*     */ 
/*     */   public ImageFloat createIntensityImage()
/*     */   {
/* 119 */     ImageFloat dest = new ImageFloat(this.width, this.height);
/* 120 */     float[] destBuffer = dest.getBufferFloat();
/* 121 */     for (int i = 0; i < this.data.length; i++)
/* 122 */       destBuffer[i] = (Colour.getIntensity(this.data[i]) / 256.0F);
/* 123 */     return dest;
/*     */   }
/*     */ 
/*     */   public ImageFloat createLuminanceImage()
/*     */   {
/* 128 */     ImageFloat dest = new ImageFloat(this.width, this.height);
/* 129 */     float[] destBuffer = dest.getBufferFloat();
/* 130 */     for (int i = 0; i < this.data.length; i++)
/* 131 */       destBuffer[i] = (Colour.getLuminance(this.data[i]) / 256.0F);
/* 132 */     return dest;
/*     */   }
/*     */ 
/*     */   public BufferedImage createCompatibleBufferedImage()
/*     */   {
/* 141 */     return new BufferedImage(this.width, this.height, 4);
/*     */   }
/*     */ 
/*     */   public void add(ImageColour src)
/*     */   {
/* 146 */     assert (src.data.length == this.data.length);
/* 147 */     for (int i = 0; i < this.data.length; i++)
/*     */     {
/* 149 */       int colour = src.data[i];
/* 150 */       int r = (this.data[i] & 0xFF) + (colour & 0xFF);
/* 151 */       int g = (this.data[i] >> 8 & 0xFF) + (colour >> 8 & 0xFF);
/* 152 */       int b = (this.data[i] >> 16 & 0xFF) + (colour >> 16 & 0xFF);
/* 153 */       r = r > 255 ? 255 : r;
/* 154 */       g = g > 255 ? 255 : g;
/* 155 */       b = b > 255 ? 255 : b;
/* 156 */       this.data[i] = Colour.createColour(r, g, b);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void fill(int colour)
/*     */   {
/* 162 */     for (int i = 0; i < this.data.length; i++)
/* 163 */       this.data[i] = colour;
/*     */   }
/*     */ 
/*     */   public final int getNearestPixel(int x, int y)
/*     */   {
/* 168 */     x = x < 0 ? 0 : x;
/* 169 */     x = x >= this.width ? this.width - 1 : x;
/* 170 */     y = y < 0 ? 0 : y;
/* 171 */     y = y >= this.height ? this.height - 1 : y;
/* 172 */     return this.data[(y * this.width + x)];
/*     */   }
/*     */ 
/*     */   public final void setPixel(int x, int y, int color)
/*     */   {
/* 177 */     this.data[(y * this.width + x)] = color;
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.ImageColour
 * JD-Core Version:    0.6.2
 */