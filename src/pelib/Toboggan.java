/*     */ package pelib;
/*     */ 
/*     */ public class Toboggan
/*     */ {
/*  13 */   private static boolean enableFloodFill = true;
/*     */   public static final byte DIRECTION_RIGHT = 1;
/*     */   public static final byte DIRECTION_LEFT = 2;
/*     */   public static final byte DIRECTION_DOWN = 4;
/*     */   public static final byte DIRECTION_UP = 8;
/*     */   public static final byte DIRECTION_PLATEAU = 32;
/*     */   public static final byte DIRECTION_MINIMA = 64;
/*     */ 
/*     */   public static ImageByte createDirectionMap(ImageFloat img)
/*     */   {
/*  28 */     int width = img.getWidth();
/*  29 */     int height = img.getHeight();
/*  30 */     int pitch = img.getPitch();
/*  31 */     float[] imgData = img.getBufferFloat();
/*  32 */     ImageByte map = new ImageByte(width, height);
/*  33 */     map.fill((byte)0);
/*  34 */     byte[] mapData = map.getBufferByte();
/*     */ 
/*  36 */     for (int y = 0; y < height; y++)
/*     */     {
/*  38 */       for (int x = 0; x < width; x++)
/*     */       {
/*  40 */         byte dir = 0;
/*  41 */         float min = imgData[(y * pitch + x)];
/*  42 */         float up = img.getNearestPixel(x, y - 1);
/*  43 */         float down = img.getNearestPixel(x, y + 1);
/*  44 */         float left = img.getNearestPixel(x - 1, y);
/*  45 */         float right = img.getNearestPixel(x + 1, y);
/*     */ 
/*  47 */         if (up < min) { min = up; dir = 8; }
/*  48 */         if (down < min) { min = down; dir = 4; }
/*  49 */         if (left < min) { min = left; dir = 2; }
/*  50 */         if (right < min) { min = right; dir = 1;
/*     */         }
/*     */ 
/*  53 */         if (dir == 0)
/*     */         {
/*  55 */           dir = 64;
/*  56 */           if ((up == min) || (down == min) || (left == min) || (right == min)) {
/*  57 */             dir = (byte)(dir | 0x20);
/*     */           }
/*     */         }
/*  60 */         mapData[(y * pitch + x)] = dir;
/*     */       }
/*     */     }
/*     */ 
/*  64 */     return map;
/*     */   }
/*     */ 
/*     */   public static ImageLabelled createLabelledImage(ImageByte map)
/*     */   {
/*  73 */     int width = map.getWidth();
/*  74 */     int height = map.getHeight();
/*  75 */     int pitch = map.getPitch();
/*  76 */     byte[] mapData = map.getBufferByte();
/*  77 */     ImageLabelled result = new ImageLabelled(width, height);
/*  78 */     result.fill(-1);
/*  79 */     int[] resultData = result.getBufferInteger();
/*     */ 
/*  81 */     StackInteger trail = new StackInteger(64);
/*  82 */     int nextLabel = 0;
/*     */ 
/*  84 */     for (int y = 0; y < height; y++)
/*     */     {
/*  86 */       for (int x = 0; x < width; x++)
/*     */       {
/*  88 */         int label = nextLabel;
/*  89 */         int tX = x;
/*  90 */         int tY = y;
/*     */         while (true)
/*     */         {
/*  95 */           int tIdx = tY * pitch + tX;
/*  96 */           int existing = resultData[tIdx];
/*     */ 
/*  99 */           if (existing != -1)
/*     */           {
/* 101 */             label = existing;
/* 102 */             break;
/*     */           }
/*     */ 
/* 106 */           resultData[tIdx] = label;
/*     */ 
/* 109 */           byte dir = mapData[tIdx];
/* 110 */           if ((dir & 0x40) != 0)
/*     */           {
/* 112 */             if (((dir & 0x20) == 0) || (!enableFloodFill)) {
/*     */               break;
/*     */             }
/* 115 */             floodFill(tX, tY, map, result, label); break;
/*     */           }
/*     */ 
/* 121 */           trail.push(tIdx);
/*     */ 
/* 124 */           switch (dir)
/*     */           {
/*     */           case 1:
/* 127 */             tX++;
/* 128 */             break;
/*     */           case 2:
/* 130 */             tX--;
/* 131 */             break;
/*     */           case 8:
/* 133 */             tY--;
/* 134 */             break;
/*     */           case 4:
/* 136 */             tY++;
/* 137 */             break;
/*     */           case 3:
/*     */           case 5:
/*     */           case 6:
/*     */           case 7:
/*     */           default:
/* 139 */             //if (!$assertionsDisabled) throw new AssertionError();
/*     */             break;
/*     */           }
/*     */         }
/*     */ 
/* 144 */         if (label != nextLabel)
/*     */         {
/* 146 */           while (!trail.empty()) {
/* 147 */             resultData[trail.pop()] = label;
/*     */           }
/*     */         }
/*     */ 
/* 151 */         trail.clear();
/* 152 */         nextLabel++;
/*     */       }
/*     */     }
/*     */ 
/* 156 */     result.setLabelCount(nextLabel);
/* 157 */     return result;
/*     */   }
/*     */ 
/*     */   private static void floodFill(int x, int y, ImageByte map, ImageInteger out, int label)
/*     */   {
/* 168 */     int mapPitch = map.getPitch();
/* 169 */     byte[] mapData = map.getBufferByte();
/* 170 */     int outPitch = out.getPitch();
/* 171 */     int[] outData = out.getBufferInteger();
/*     */ 
/* 173 */     int width = map.getWidth();
/* 174 */     int height = map.getHeight();
/*     */ 
/* 176 */     byte target = mapData[(y * mapPitch + x)];
/*     */ 
/* 178 */     StackInteger todo = new StackInteger();
/* 179 */     todo.push(x); todo.push(y);
/*     */     int ny;
/* 219 */     for (; !todo.empty(); 
/* 219 */       todo.push(ny))
/*     */     {
/* 183 */       y = todo.pop(); x = todo.pop();
/*     */ 
/* 185 */       outData[(y * outPitch + x)] = label;
/*     */ 
/* 190 */       int nx = x - 1; ny = y;
/* 191 */       if ((nx >= 0) && (mapData[(ny * mapPitch + nx)] == target) && (outData[(ny * outPitch + nx)] == -1))
/*     */       {
/* 195 */         todo.push(nx); todo.push(ny);
/*     */       }
/*     */ 
/* 198 */       nx = x + 1; ny = y;
/* 199 */       if ((nx < width) && (mapData[(ny * mapPitch + nx)] == target) && (outData[(ny * outPitch + nx)] == -1))
/*     */       {
/* 203 */         todo.push(nx); todo.push(ny);
/*     */       }
/*     */ 
/* 206 */       nx = x; ny = y - 1;
/* 207 */       if ((ny >= 0) && (mapData[(ny * mapPitch + nx)] == target) && (outData[(ny * outPitch + nx)] == -1))
/*     */       {
/* 211 */         todo.push(nx); todo.push(ny);
/*     */       }
/*     */ 
/* 214 */       nx = x; ny = y + 1;
/* 215 */       if ((ny < height) && (mapData[(ny * mapPitch + nx)] == target) && (outData[(ny * outPitch + nx)] == -1))
/*     */       {
/* 219 */         todo.push(nx);
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.Toboggan
 * JD-Core Version:    0.6.2
 */