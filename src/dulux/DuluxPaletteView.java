/*     */ package dulux;
/*     */ 
/*     */ import duluxskin.Widget;
/*     */ import java.awt.Color;
/*     */ import java.awt.Font;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Shape;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.util.Iterator;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class DuluxPaletteView extends Widget
/*     */ {
/*     */   private boolean isWhites;
/*     */   private int viewRows;
/*     */   private int viewCols;
/*     */   private int chipWidth;
/*     */   private int chipHeight;
/*     */   private int initialCenterX;
/*     */   private int initialCenterY;
/*     */   private boolean initialCenter;
/*     */   private int offsetX;
/*     */   private int offsetY;
/*     */   private float floatOffsetX;
/*     */   private float floatOffsetY;
/*     */   private int targetOffsetX;
/*     */   private int targetOffsetY;
/*     */   private DuluxPalette palette;
/*     */   private DuluxColour selected;
/*     */   private boolean centerSelected;
/*  42 */   private float speed = 0.1F;
/*  43 */   private Object panSemaphore = new Object();
/*     */   private Vector paletteListeners;
/*     */   private Boolean isTop;
/*     */   private Boolean isRight;
/*     */ 
/*     */   public DuluxPaletteView(String id, int x, int y, int width, int height, int rows, int cols)
/*     */   {
/*  51 */     super(id, x, y);
/*     */ 
/*  53 */     this.width = width;
/*     */ 
/*  55 */     this.height = height;
/*     */ 
/*  57 */     this.isWhites = false;
/*     */ 
/*  59 */     this.viewRows = rows;
/*     */ 
/*  61 */     this.viewCols = cols;
/*     */ 
/*  63 */     this.centerSelected = true;
/*     */ 
/*  65 */     this.chipHeight = (height / this.viewRows);
/*     */ 
/*  67 */     this.chipWidth = (width / this.viewCols);
/*  68 */     this.paletteListeners = new Vector();
/*  69 */     this.isTop = Boolean.valueOf(true);
/*  70 */     this.isRight = Boolean.valueOf(false);
/*     */ 
/*  74 */     addMouseListener(new MouseAdapter()
/*     */     {
/*     */       public void mousePressed(MouseEvent e)
/*     */       {
/*  78 */         if ((e.getX() - DuluxPaletteView.this.offsetX >= 0) && (e.getY() - DuluxPaletteView.this.offsetY >= 0))
/*     */         {
/*  80 */           int col = (e.getX() - DuluxPaletteView.this.offsetX) / DuluxPaletteView.this.chipWidth;
/*     */ 
/*  82 */           int row = (e.getY() - DuluxPaletteView.this.offsetY) / DuluxPaletteView.this.chipHeight;
/*     */ 
/*  84 */           DuluxPaletteView.this.selectPosition(col, row);
/*     */         }
/*     */       }
/*     */     });
/*  93 */     new PainterThread().start();
/*     */   }
/*     */ 
/*     */   public void addListener(DuluxPaletteListener l)
/*     */   {
/*  99 */     this.paletteListeners.add(l);
/*     */   }
/*     */ 
/*     */   public void setCenterSelected(boolean centerSelected)
/*     */   {
/* 135 */     this.centerSelected = centerSelected;
/*     */   }
/*     */ 
/*     */   public void selectPosition(int col, int row)
/*     */   {
/* 140 */     if ((col < 0) || (col >= this.palette.cols) || (row < 0) || (row >= this.palette.rows)) {
/* 141 */       return;
/*     */     }
/*     */ 
/* 146 */     DuluxColour colour = this.palette.colours[col][row];
/*     */ 
/* 149 */     if (colour != null)
/*     */     {
/* 151 */       this.selected = colour;
/*     */ 
/* 153 */       for (Iterator it = this.paletteListeners.iterator(); it.hasNext(); ) {
/* 154 */         ((DuluxPaletteListener)it.next()).colourSelected(colour);
/*     */       }
/*     */ 
/* 157 */       if (this.centerSelected);
/* 161 */       invalidate();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void centerPosition(int col, int row, boolean immediate)
/*     */   {
/* 171 */     if (this.chipWidth == 0)
/*     */     {
/* 173 */       this.initialCenter = true;
/*     */ 
/* 175 */       this.initialCenterX = col;
/*     */ 
/* 177 */       this.initialCenterY = row;
/*     */     }
/*     */ 
/* 183 */     this.targetOffsetX = (-(col - this.viewCols / 2) * this.chipWidth);
/*     */ 
/* 185 */     this.targetOffsetY = (-(row - this.viewRows / 2) * this.chipHeight);
/*     */ 
/* 187 */     if (immediate)
/*     */     {
/* 189 */       this.offsetX = this.targetOffsetX;
/*     */ 
/* 191 */       this.offsetY = this.targetOffsetY;
/*     */ 
/* 193 */       this.floatOffsetX = this.offsetX;
/*     */ 
/* 195 */       this.floatOffsetY = this.offsetY;
/*     */     }
/*     */ 
/* 199 */     synchronized (this.panSemaphore)
/*     */     {
/* 201 */       this.panSemaphore.notify();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setPalette(DuluxPalette palette)
/*     */   {
/* 208 */     setPalette(palette, true);
/* 209 */     this.isWhites = false;
/*     */   }
/*     */ 
/*     */   public void setWhites(boolean isWhites) {
/* 213 */     this.isWhites = isWhites;
/*     */   }
/*     */ 
/*     */   public void setPalette(DuluxPalette palette, boolean selectColour)
/*     */   {
/* 218 */     this.palette = palette;
/*     */ 
/* 222 */     if (selectColour)
/*     */     {
/* 232 */       centerPosition(palette.centerX, palette.centerY, true);
/*     */     }
/*     */   }
/*     */ 
/*     */   public DuluxPalette getPalette()
/*     */   {
/* 243 */     return this.palette;
/*     */   }
/*     */ 
/*     */   public DuluxColour getSelectedColour()
/*     */   {
/* 249 */     return this.selected;
/*     */   }
/*     */ 
/*     */   public void setSelectedColour(DuluxColour col, boolean smooth)
/*     */   {
/* 255 */     boolean immediate = !smooth;
/*     */ 
/* 271 */     if (this.centerSelected)
/*     */     {
/* 273 */       for (int y = 0; y < this.palette.rows; y++)
/*     */       {
/* 275 */         for (int x = 0; x < this.palette.cols; x++)
/*     */         {
/* 277 */           if (this.palette.colours[x][y] != col);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 287 */     this.selected = col;
/*     */ 
/* 291 */     invalidate();
/*     */   }
/*     */ 
/*     */   public void paint(Graphics g)
/*     */   {
/* 297 */     Shape oldClip = g.getClip();
/* 298 */     g.setClip(0, 0, this.width, this.height);
/*     */ 
/* 302 */     if (this.initialCenter)
/*     */     {
/* 304 */       this.initialCenter = false;
/*     */ 
/* 306 */       centerPosition(this.initialCenterX, this.initialCenterY, true);
/*     */     }
/*     */ 
/* 314 */     if ((this.offsetX != this.targetOffsetX) || (this.offsetY != this.targetOffsetY))
/*     */     {
/* 316 */       float deltaX = (this.targetOffsetX - this.floatOffsetX) * this.speed;
/*     */ 
/* 318 */       this.floatOffsetX += deltaX;
/*     */ 
/* 320 */       float deltaY = (this.targetOffsetY - this.floatOffsetY) * this.speed;
/*     */ 
/* 322 */       this.floatOffsetY += deltaY;
/*     */ 
/* 324 */       if (Math.abs(this.targetOffsetX - this.floatOffsetX) < 1.0F)
/* 325 */         this.floatOffsetX = (this.offsetX = this.targetOffsetX);
/*     */       else {
/* 327 */         this.offsetX = ((int)this.floatOffsetX);
/*     */       }
/*     */ 
/* 330 */       if (Math.abs(this.targetOffsetY - this.floatOffsetY) < 1.0F)
/* 331 */         this.floatOffsetY = (this.offsetY = this.targetOffsetY);
/*     */       else {
/* 333 */         this.offsetY = ((int)this.floatOffsetY);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 342 */     g.setColor(Color.white);
/*     */ 
/* 344 */     g.fillRect(0, 0, this.width, this.height);
/*     */ 
/* 348 */     int selectedX = -1;
/*     */ 
/* 350 */     int selectedY = -1;
/*     */ 
/* 357 */     for (int y = 0; y < this.palette.rows; y++)
/*     */     {
/* 359 */       for (int x = 0; x < this.palette.cols; x++) {
/* 360 */         DuluxColour dcol = this.palette.colours[x][y];
/*     */ 
/* 362 */         if (dcol != null)
/*     */         {
/* 368 */           int col = dcol.displayColour;
/* 369 */           Color displayColour = new Color(col);
/* 370 */           g.setColor(displayColour);
/*     */ 
/* 372 */           g.fillRect(x * this.chipWidth + this.offsetX, y * this.chipHeight + this.offsetY, this.chipWidth, this.chipHeight);
/*     */ 
/* 375 */           g.setColor(Color.white);
/* 376 */           g.setFont(new Font("", 0, 8));
/*     */ 
/* 378 */           double luminance = displayColour.getRed() * 0.2126D + 0.7152D * displayColour.getGreen() + 0.0722D * displayColour.getBlue();
/*     */ 
/* 381 */           if (luminance > 190.0D) {
/* 382 */             g.setColor(Color.gray);
/*     */           }
/* 384 */           String name = new String(dcol.name);
/* 385 */           if (name.length() > 11) {
/* 386 */             name = name.substring(0, 11);
/*     */           }
/* 388 */           g.drawString(name, x * this.chipWidth + this.offsetX, y * this.chipHeight + this.chipHeight - 2 + this.offsetY);
/*     */ 
/* 390 */           g.drawString(dcol.chip, x * this.chipWidth + this.offsetX, y * this.chipHeight + this.chipHeight - 5 + this.offsetY - 5);
/*     */ 
/* 394 */           if (dcol == this.selected)
/*     */           {
/* 396 */             selectedX = x;
/*     */ 
/* 398 */             selectedY = y;
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 412 */     g.setColor(Color.white);
/*     */ 
/* 414 */     int modOffsetX = this.offsetX % this.chipWidth;
/*     */ 
/* 416 */     int modOffsetY = this.offsetY % this.chipHeight;
/*     */ 
/* 418 */     for (int i = 0; i <= this.viewRows; i++) {
/* 419 */       g.drawLine(0, i * this.chipHeight + modOffsetY, this.width - 1, i * this.chipHeight + modOffsetY);
/*     */     }
/*     */ 
/* 423 */     for (int i = 0; i <= this.viewCols; i++) {
/* 424 */       g.drawLine(i * this.chipWidth + modOffsetX, 0, i * this.chipWidth + modOffsetX, this.height - 1);
/*     */     }
/*     */ 
/* 432 */     if ((selectedX >= 0) && (selectedY >= 0))
/*     */     {
/* 434 */       int x = selectedX * this.chipWidth + this.offsetX;
/*     */ 
/* 436 */       int y = selectedY * this.chipHeight + this.offsetY;
/*     */ 
/* 438 */       g.setColor(Color.gray);
/*     */ 
/* 440 */       drawBrokenRect(g, x - 1, y - 1, this.chipWidth + 2, this.chipHeight + 2);
/*     */ 
/* 442 */       g.setColor(Color.lightGray);
/*     */ 
/* 444 */       drawBrokenRect(g, x - 2, y - 2, this.chipWidth + 4, this.chipHeight + 4);
/*     */     }
/*     */ 
/* 454 */     g.setClip(oldClip);
/*     */   }
/*     */ 
/*     */   public void setDefaultPosition()
/*     */   {
/* 460 */     this.isRight = Boolean.valueOf(false);
/* 461 */     this.isTop = Boolean.valueOf(true);
/*     */   }
/*     */ 
/*     */   private void drawBrokenRect(Graphics g, int l, int t, int w, int h)
/*     */   {
/* 468 */     g.drawLine(l, t, l + w, t);
/*     */ 
/* 470 */     g.drawLine(l, t + h, l + w, t + h);
/*     */ 
/* 472 */     g.drawLine(l, t, l, t + h);
/*     */ 
/* 474 */     g.drawLine(l + w, t, l + w, t + h);
/*     */   }
/*     */ 
/*     */   public void moveRight()
/*     */   {
/* 479 */     if (this.palette.cols <= 10)
/*     */       return;
/*     */     int top;
/* 483 */     if (this.isTop.booleanValue())
/* 484 */       top = 3;
/*     */     else {
/* 486 */       top = 10;
/*     */     }
/* 488 */     this.isRight = Boolean.valueOf(true);
/* 489 */     centerPosition(this.palette.cols - 5, top, false);
/*     */   }
/*     */ 
/*     */   public void moveLeft()
/*     */   {
/*     */     int top;
/* 494 */     if (this.isTop.booleanValue())
/* 495 */       top = 3;
/*     */     else {
/* 497 */       top = 10;
/*     */     }
/* 499 */     this.isRight = Boolean.valueOf(false);
/* 500 */     centerPosition(this.palette.centerX, top, false);
/*     */   }
/*     */ 
/*     */   public void moveDown()
/*     */   {
/* 505 */     this.isTop = Boolean.valueOf(false);
/*     */     int right;
/* 506 */     if (this.isRight.booleanValue())
/* 507 */       right = this.palette.cols - 5;
/*     */     else
/* 509 */       right = this.palette.centerX;
/* 510 */     centerPosition(right, 10, false);
/*     */   }
/*     */ 
/*     */   public void moveUp()
/*     */   {
/* 515 */     this.isTop = Boolean.valueOf(true);
/*     */     int right;
/* 516 */     if (this.isRight.booleanValue())
/* 517 */       right = this.palette.cols - 5;
/*     */     else
/* 519 */       right = this.palette.centerX;
/* 520 */     centerPosition(right, 3, false);
/*     */   }
/*     */ 
/*     */   private class PainterThread extends Thread
/*     */   {
/*     */     private PainterThread()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void run()
/*     */     {
/*     */       while (true)
/*     */         try
/*     */         {
/* 111 */           DuluxPaletteView.this.invalidate();
/*     */ 
/* 113 */           if ((DuluxPaletteView.this.offsetX == DuluxPaletteView.this.targetOffsetX) && (DuluxPaletteView.this.offsetY == DuluxPaletteView.this.targetOffsetY))
/*     */           {
/* 115 */             synchronized (DuluxPaletteView.this.panSemaphore)
/*     */             {
/* 117 */               DuluxPaletteView.this.panSemaphore.wait();
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/* 123 */           sleep(50L);
/*     */         }
/*     */         catch (InterruptedException e)
/*     */         {
/*     */         }
/*     */     }
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     dulux.DuluxPaletteView
 * JD-Core Version:    0.6.2
 */