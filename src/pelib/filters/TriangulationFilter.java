/*     */ package pelib.filters;
/*     */ 
/*     */ import java.awt.Rectangle;
/*     */ import pelib.Colour;
import pelib.ImageColour;
/*     */ 
/*     */ public class TriangulationFilter extends ResizingFilter
/*     */ {
/*     */   private boolean[] edges;
/*     */ 
/*     */   public TriangulationFilter()
/*     */   {
/*     */   }
/*     */ 
/*     */   public TriangulationFilter(ImageColour src)
/*     */   {
/*  31 */     createEdgeTable(src);
/*     */   }
/*     */ 
/*     */   public void reinitialise(ImageColour src)
/*     */   {
/*  39 */     createEdgeTable(src);
/*     */   }
/*     */ 
/*     */   public void filter(ImageColour src, ImageColour dest, Rectangle srcRect, Rectangle destRect)
/*     */   {
/*  45 */     int srcWidth = srcRect.width;
/*  46 */     int srcPitch = src.getPitch();
/*  47 */     int srcHeight = srcRect.height;
/*  48 */     int[] srcData = src.getBufferIntBGR();
/*     */ 
/*  50 */     int destWidth = destRect.width;
/*  51 */     int destPitch = dest.getPitch();
/*  52 */     int destHeight = destRect.height;
/*  53 */     int[] destData = dest.getBufferIntBGR();
/*     */ 
/*  55 */     NearestPointFinder np = new NearestPointFinder(src.getWidth(), src.getHeight(), srcPitch);
/*     */ 
/*  59 */     float scaleX = destWidth / srcWidth;
/*  60 */     float scaleY = destHeight / srcHeight;
/*     */ 
/*  65 */     boolean destroyEdges = false;
/*  66 */     if (this.edges == null)
/*     */     {
/*  68 */       destroyEdges = true;
/*  69 */       createEdgeTable(src);
/*     */     }
/*     */ 
/*  73 */     for (int destY = 0; destY < destHeight; destY++)
/*     */     {
/*  75 */       for (int destX = 0; destX < destWidth; destX++)
/*     */       {
/*  78 */         float absY = destY / scaleY + srcRect.y;
/*  79 */         float absX = destX / scaleX + srcRect.x;
/*  80 */         int srcY = (int)absY;
/*  81 */         int srcX = (int)absX;
/*     */ 
/*  86 */         float posY = absY - srcY;
/*  87 */         float posX = absX - srcX;
/*     */ 
/*  93 */         if (this.edges[(srcY * srcPitch + srcX)])
/*     */         {
/* 109 */           if (posX < posY)
/*     */           {
/* 112 */             int p1 = np.getIndex(srcX, srcY);
/* 113 */             int p2 = np.getIndex(srcX, srcY + 1);
/* 114 */             int p3 = np.getIndex(srcX + 1, srcY + 1);
/*     */ 
/* 116 */             int destColour = 0;
/*     */ 
/* 119 */             for (int sh = 0; sh < 32; sh += 8)
/*     */             {
/* 134 */               int sp1 = srcData[p1] >> sh & 0xFF;
/* 135 */               int sp2 = srcData[p2] >> sh & 0xFF;
/* 136 */               int sp3 = srcData[p3] >> sh & 0xFF;
/*     */ 
/* 139 */               int component = (int)((sp3 - sp2) * posX + (sp2 - sp1) * posY + sp1);
/*     */ 
/* 145 */               if (component > 255) {
/* 146 */                 component = 255;
/*     */               }
/*     */ 
/* 149 */               destColour |= component << sh;
/*     */             }
/*     */ 
/* 153 */             destData[((destY + destRect.y) * destPitch + destX + destRect.x)] = destColour;
/*     */           }
/*     */           else
/*     */           {
/* 159 */             int p1 = np.getIndex(srcX, srcY);
/* 160 */             int p2 = np.getIndex(srcX + 1, srcY + 1);
/* 161 */             int p3 = np.getIndex(srcX + 1, srcY);
/*     */ 
/* 163 */             int destColour = 0;
/* 164 */             for (int sh = 0; sh < 32; sh += 8)
/*     */             {
/* 166 */               int sp1 = srcData[p1] >> sh & 0xFF;
/* 167 */               int sp2 = srcData[p2] >> sh & 0xFF;
/* 168 */               int sp3 = srcData[p3] >> sh & 0xFF;
/*     */ 
/* 170 */               int component = (int)((sp3 - sp1) * posX + (sp2 - sp3) * posY + sp1);
/*     */ 
/* 174 */               if (component > 255)
/* 175 */                 component = 255;
/* 176 */               destColour |= component << sh;
/*     */             }
/* 178 */             destData[((destY + destRect.y) * destPitch + destX + destRect.x)] = destColour;
/*     */           }
/*     */ 
/*     */         }
/* 196 */         else if (1.0F - posX > posY)
/*     */         {
/* 199 */           int p1 = np.getIndex(srcX, srcY);
/* 200 */           int p2 = np.getIndex(srcX, srcY + 1);
/* 201 */           int p3 = np.getIndex(srcX + 1, srcY);
/*     */ 
/* 203 */           int destColour = 0;
/* 204 */           for (int sh = 0; sh < 32; sh += 8)
/*     */           {
/* 206 */             int sp1 = srcData[p1] >> sh & 0xFF;
/* 207 */             int sp2 = srcData[p2] >> sh & 0xFF;
/* 208 */             int sp3 = srcData[p3] >> sh & 0xFF;
/*     */ 
/* 210 */             int component = (int)((sp3 - sp1) * posX + (sp2 - sp1) * posY + sp1);
/*     */ 
/* 214 */             if (component > 255)
/* 215 */               component = 255;
/* 216 */             destColour |= component << sh;
/*     */           }
/* 218 */           destData[((destY + destRect.y) * destPitch + destX + destRect.x)] = destColour;
/*     */         }
/*     */         else
/*     */         {
/* 224 */           int p1 = np.getIndex(srcX, srcY + 1);
/* 225 */           int p2 = np.getIndex(srcX + 1, srcY + 1);
/* 226 */           int p3 = np.getIndex(srcX + 1, srcY);
/*     */ 
/* 228 */           int destColour = 0;
/* 229 */           for (int sh = 0; sh < 32; sh += 8)
/*     */           {
/* 231 */             int sp1 = srcData[p1] >> sh & 0xFF;
/* 232 */             int sp2 = srcData[p2] >> sh & 0xFF;
/* 233 */             int sp3 = srcData[p3] >> sh & 0xFF;
/*     */ 
/* 235 */             int component = (int)((sp2 - sp1) * posX + (sp2 - sp3) * posY + sp1 - sp2 + sp3);
/*     */ 
/* 239 */             if (component > 255)
/* 240 */               component = 255;
/* 241 */             destColour |= component << sh;
/*     */           }
/* 243 */           destData[((destY + destRect.y) * destPitch + destX + destRect.x)] = destColour;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 251 */     if (destroyEdges)
/* 252 */       this.edges = null;
/*     */   }
/*     */ 
/*     */   private void createEdgeTable(ImageColour src)
/*     */   {
/* 257 */     int width = src.getWidth();
/* 258 */     int height = src.getHeight();
/* 259 */     int pitch = src.getPitch();
/* 260 */     int[] data = src.getBufferIntBGR();
/*     */ 
/* 262 */     NearestPointFinder np = new NearestPointFinder(width, height, src.getPitch());
/*     */ 
/* 265 */     this.edges = new boolean[width * height];
/*     */ 
/* 267 */     for (int y = 0; y < height; y++)
/*     */     {
/* 269 */       for (int x = 0; x < width; x++)
/*     */       {
/* 271 */         int acCount = 0;
/* 272 */         int bdCount = 0;
/*     */ 
/* 275 */         for (int t = y - 1; t <= y + 1; t++)
/*     */         {
/* 277 */           for (int s = x - 1; s <= x + 1; s++)
/*     */           {
/* 289 */             float a = Colour.getLuminance(data[np.getIndex(s, t)]);
/* 290 */             float b = Colour.getLuminance(data[np.getIndex(s, t + 1)]);
/* 291 */             float c = Colour.getLuminance(data[np.getIndex(s + 1, t + 1)]);
/* 292 */             float d = Colour.getLuminance(data[np.getIndex(s + 1, t)]);
/*     */ 
/* 297 */             if (Math.abs(a - c) < Math.abs(b - d))
/* 298 */               acCount++;
/*     */             else {
/* 300 */               bdCount++;
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 307 */         this.edges[(y * width + x)] = (Boolean) (acCount > bdCount ? 1 : false);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private final class NearestPointFinder {
/*     */     private int width;
/*     */     private int height;
/*     */     private int pitch;
/*     */ 
/*     */     public NearestPointFinder(int width, int height, int pitch) {
/* 320 */       this.width = width;
/* 321 */       this.height = height;
/* 322 */       this.pitch = pitch;
/*     */     }
/*     */ 
/*     */     public final int getIndex(int x, int y)
/*     */     {
/* 327 */       return Math.min(Math.max(y, 0), this.height - 1) * this.width + Math.min(Math.max(x, 0), this.width - 1);
/*     */     }
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.filters.TriangulationFilter
 * JD-Core Version:    0.6.2
 */