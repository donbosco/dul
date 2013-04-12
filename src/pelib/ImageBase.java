/*    */ package pelib;
/*    */ 
/*    */ import java.awt.Image;
/*    */ import java.awt.Rectangle;
/*    */ import java.awt.image.BufferedImage;
/*    */ import java.awt.image.WritableRaster;
/*    */ 
/*    */ public abstract class ImageBase
/*    */ {
/*    */   protected int width;
/*    */   protected int height;
/*    */   protected BufferedImage image;
/*    */ 
/*    */   protected ImageBase(int width, int height)
/*    */   {
/* 24 */     this.width = width;
/* 25 */     this.height = height;
/*    */   }
/*    */ 
/*    */   protected ImageBase()
/*    */   {
/*    */   }
/*    */ 
/*    */   public int getWidth()
/*    */   {
/* 38 */     return this.width;
/*    */   }
/*    */ 
/*    */   public int getHeight()
/*    */   {
/* 43 */     return this.height;
/*    */   }
/*    */ 
/*    */   public Rectangle getBounds()
/*    */   {
/* 48 */     return new Rectangle(0, 0, this.width, this.height);
/*    */   }
/*    */ 
/*    */   public Area getArea()
/*    */   {
/* 53 */     return new Area(0, 0, this.width - 1, this.height - 1);
/*    */   }
/*    */ 
/*    */   public int getPitch()
/*    */   {
/* 59 */     return this.width;
/*    */   }
/*    */ 
/*    */   public int getSamplesPerPixel()
/*    */   {
/* 65 */     return 1;
/*    */   }
/*    */ 
/*    */   public abstract Object getBufferNative();
/*    */ 
/*    */   public abstract BufferedImage createCompatibleBufferedImage();
/*    */ 
/*    */   public Image getAWTImage()
/*    */   {
/* 79 */     if (this.image == null) {
/* 80 */       this.image = createCompatibleBufferedImage();
/*    */     }
/*    */ 
/* 83 */     WritableRaster raster = this.image.getRaster();
/* 84 */     raster.setDataElements(0, 0, this.width, this.height, getBufferNative());
/*    */ 
/* 86 */     return this.image;
/*    */   }
/*    */ 
/*    */   public abstract ImageBase copy();
/*    */ 
/*    */   public abstract ImageBase createCompatibleImage();
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.ImageBase
 * JD-Core Version:    0.6.2
 */