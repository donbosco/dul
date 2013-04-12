/*    */ package pelib;
/*    */ 
/*    */ import java.awt.Image;
/*    */ import java.awt.image.BufferedImage;
/*    */ import java.awt.image.WritableRaster;
/*    */ 
/*    */ public class ImageInteger extends ImageBase
/*    */ {
/*    */   private int[] data;
/*    */ 
/*    */   public ImageInteger(int width, int height)
/*    */   {
/* 19 */     super(width, height);
/* 20 */     this.data = new int[width * height];
/*    */   }
/*    */ 
/*    */   public ImageInteger(ImageInteger src)
/*    */   {
/* 25 */     this(src.width, src.height);
/* 26 */     System.arraycopy(src.data, 0, this.data, 0, this.data.length);
/*    */   }
/*    */ 
/*    */   public Object getBufferNative()
/*    */   {
/* 31 */     return this.data;
/*    */   }
/*    */ 
/*    */   public final int[] getBufferInteger()
/*    */   {
/* 36 */     return this.data;
/*    */   }
/*    */ 
/*    */   public final Image getAWTImage()
/*    */   {
/* 41 */     if (this.image == null) {
/* 42 */       this.image = new BufferedImage(this.width, this.height, 4);
/*    */     }
/*    */ 
/* 45 */     int[] colourData = new int[this.width * this.height];
/* 46 */     for (int i = 0; i < this.data.length; i++) {
/* 47 */       colourData[i] = Colour.createRandomColour(this.data[i]);
/*    */     }
/* 49 */     WritableRaster raster = this.image.getRaster();
/* 50 */     raster.setDataElements(0, 0, this.width, this.height, colourData);
/*    */ 
/* 52 */     return this.image;
/*    */   }
/*    */ 
/*    */   public BufferedImage createCompatibleBufferedImage()
/*    */   {
/* 58 */     return new BufferedImage(this.width, this.height, 4);
/*    */   }
/*    */ 
/*    */   public ImageBase copy()
/*    */   {
/* 63 */     return new ImageInteger(this);
/*    */   }
/*    */ 
/*    */   public ImageBase createCompatibleImage()
/*    */   {
/* 68 */     return new ImageInteger(this.width, this.height);
/*    */   }
/*    */ 
/*    */   public void fill(int p)
/*    */   {
/* 73 */     for (int i = 0; i < this.data.length; i++)
/* 74 */       this.data[i] = p;
/*    */   }
/*    */ 
/*    */   public final int getNearestPixel(int x, int y)
/*    */   {
/* 79 */     x = x < 0 ? 0 : x;
/* 80 */     x = x >= this.width ? this.width - 1 : x;
/* 81 */     y = y < 0 ? 0 : y;
/* 82 */     y = y >= this.height ? this.height - 1 : y;
/* 83 */     return this.data[(y * this.width + x)];
/*    */   }
/*    */ 
/*    */   public final void fillReplace(StackInteger points, int oldVal, int newVal)
/*    */   {
/* 93 */     int pitch = getPitch();
/*    */ 
/* 95 */     while (points.size() > 0)
/*    */     {
/* 97 */       int x = points.pop();
/* 98 */       int y = points.pop();
/*    */ 
/* 101 */       if (this.data[(y * pitch + x)] == oldVal)
/*    */       {
/* 103 */         this.data[(y * pitch + x)] = newVal;
/*    */ 
/* 106 */         if (x < this.width - 1)
/*    */         {
/* 108 */           points.push(y);
/* 109 */           points.push(x + 1);
/*    */         }
/* 111 */         if (x > 0)
/*    */         {
/* 113 */           points.push(y);
/* 114 */           points.push(x - 1);
/*    */         }
/* 116 */         if (y < this.height - 1)
/*    */         {
/* 118 */           points.push(y + 1);
/* 119 */           points.push(x);
/*    */         }
/* 121 */         if (y > 0)
/*    */         {
/* 123 */           points.push(y - 1);
/* 124 */           points.push(x);
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.ImageInteger
 * JD-Core Version:    0.6.2
 */