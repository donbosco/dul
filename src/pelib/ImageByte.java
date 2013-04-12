/*    */ package pelib;
/*    */ 
/*    */ import java.awt.image.BufferedImage;
/*    */ import java.awt.image.IndexColorModel;
/*    */ 
/*    */ public class ImageByte extends ImageBase
/*    */ {
/*    */   protected byte[] data;
/* 23 */   private static byte[] rIndex = new byte[256];
/* 24 */   private static byte[] gIndex = new byte[256];
/* 25 */   private static byte[] bIndex = new byte[256];
/*    */ 
/* 33 */   private static IndexColorModel indexColorModel = new IndexColorModel(8, 256, rIndex, gIndex, bIndex);
/*    */ 
/*    */   private static void setColorIndex(int index, int r, int g, int b)
/*    */   {
/* 38 */     rIndex[index] = ((byte)r);
/* 39 */     gIndex[index] = ((byte)g);
/* 40 */     bIndex[index] = ((byte)b);
/*    */   }
/*    */ 
/*    */   public ImageByte(int width, int height)
/*    */   {
/* 45 */     super(width, height);
/* 46 */     this.data = new byte[width * height];
/*    */   }
/*    */ 
/*    */   public ImageByte(ImageByte src)
/*    */   {
/* 51 */     this(src.width, src.height);
/* 52 */     System.arraycopy(src.data, 0, this.data, 0, this.data.length);
/*    */   }
/*    */ 
/*    */   public Object getBufferNative()
/*    */   {
/* 57 */     return this.data;
/*    */   }
/*    */ 
/*    */   public final byte[] getBufferByte()
/*    */   {
/* 62 */     return this.data;
/*    */   }
/*    */ 
/*    */   public BufferedImage createCompatibleBufferedImage()
/*    */   {
/* 67 */     return new BufferedImage(this.width, this.height, 13, indexColorModel);
/*    */   }
/*    */ 
/*    */   public ImageBase copy()
/*    */   {
/* 74 */     return new ImageByte(this);
/*    */   }
/*    */ 
/*    */   public ImageBase createCompatibleImage()
/*    */   {
/* 79 */     return new ImageByte(this.width, this.height);
/*    */   }
/*    */ 
/*    */   public void fill(byte p)
/*    */   {
/* 84 */     for (int i = 0; i < this.data.length; i++)
/* 85 */       this.data[i] = p;
/*    */   }
/*    */ 
/*    */   public final byte getNearestPixel(int x, int y)
/*    */   {
/* 92 */     x = x < 0 ? 0 : x;
/* 93 */     x = x >= this.width ? this.width - 1 : x;
/* 94 */     y = y < 0 ? 0 : y;
/* 95 */     y = y >= this.height ? this.height - 1 : y;
/* 96 */     return this.data[(y * this.width + x)];
/*    */   }
/*    */ 
/*    */   static
/*    */   {
/* 26 */     setColorIndex(0, 0, 0, 0);
/* 27 */     setColorIndex(1, 255, 255, 255);
/* 28 */     setColorIndex(2, 0, 255, 0);
/* 29 */     setColorIndex(4, 0, 0, 255);
/* 30 */     setColorIndex(8, 255, 0, 0);
/* 31 */     setColorIndex(32, 0, 255, 255);
/* 32 */     setColorIndex(64, 255, 0, 255);
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.ImageByte
 * JD-Core Version:    0.6.2
 */