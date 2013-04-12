/*     */ package pelib;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Image;
/*     */ import java.awt.Panel;
/*     */ import java.awt.Point;
/*     */ import java.awt.Rectangle;
/*     */ import java.util.Iterator;
/*     */ import java.util.Timer;
/*     */ import java.util.TimerTask;
/*     */ import java.util.Vector;
/*     */ import pelib.filters.PointFilter;
/*     */ import pelib.filters.ResizingFilter;
/*     */ import pelib.filters.TriangulationFilter;
/*     */ 
/*     */ public class PaintView extends Panel
/*     */   implements PaintExplorerListener
/*     */ {
/*     */   public static final float MAX_ZOOM = 32.0F;
/*     */   private PaintExplorer explorer;
/*     */   private ImageColour image;
/*     */   private ImageColour scaledImage;
/*     */   private boolean updateView;
/*     */   private Image backImage;
/*     */   private Graphics backGraphics;
/*     */   private float zoomFactor;
/*     */   private ResizingFilter defaultZoomFilter;
/*     */   private ResizingFilter zoomFilter;
/*     */   private Point translation;
/*     */   private ScissorsPath scissorsPath;
/*     */   private Vector scissorsOverlay;
/*     */   private int scissorsOverlaySegments;
/*     */   private Point highlightPoint;
/*  55 */   private static final Color backgroundColor = new Color(0, 0, 0);
/*     */   private boolean zoomOffscreenImage;
/*     */   private Timer timer;
/*     */ 
/*     */   public PaintView(PaintExplorer explorer)
/*     */   {
/*  66 */     this.explorer = explorer;
/*  67 */     explorer.addListener(this);
/*     */ 
/*  69 */     this.translation = new Point(0, 0);
/*     */ 
/*  71 */     this.zoomOffscreenImage = false;
/*  72 */     this.zoomFilter = getZoomFilter(1.0F);
/*  73 */     this.defaultZoomFilter = new PointFilter();
/*     */ 
/*  75 */     this.timer = new Timer();
/*     */   }
/*     */ 
/*     */   public void setImage(ImageColour image)
/*     */   {
/*  80 */     setImageSubstitute(image);
/*  81 */     this.defaultZoomFilter = new TriangulationFilter(image);
/*  82 */     resetView();
/*     */   }
/*     */ 
/*     */   public void setImageSubstitute(ImageColour image)
/*     */   {
/*  91 */     this.image = image;
/*  92 */     this.updateView = true;
/*  93 */     repaint();
/*     */   }
/*     */ 
/*     */   public ImageColour getImage()
/*     */   {
/*  98 */     return this.image;
/*     */   }
/*     */ 
/*     */   public void setScissorsPath(ScissorsPath scissorsPath)
/*     */   {
/* 103 */     this.scissorsPath = scissorsPath;
/*     */   }
/*     */ 
/*     */   public void setHighlight(int x, int y)
/*     */   {
/* 108 */     if (this.highlightPoint == null)
/* 109 */       this.highlightPoint = new Point();
/* 110 */     this.highlightPoint.x = x;
/* 111 */     this.highlightPoint.y = y;
/*     */   }
/*     */ 
/*     */   public void setHighlightNearestNode(Point p)
/*     */   {
/* 116 */     Node n = this.explorer.findNearestNode(p);
/* 117 */     setHighlight(n.x, n.y);
/*     */   }
/*     */ 
/*     */   public void unsetHighlight()
/*     */   {
/* 122 */     this.highlightPoint = null;
/*     */   }
/*     */ 
/*     */   public void resetView()
/*     */   {
/* 127 */     setZoom(1.0F);
/* 128 */     if (this.image != null) {
/* 129 */       this.translation = new Point(this.image.getWidth() / 2, this.image.getHeight() / 2);
/*     */     }
/* 131 */     repaint();
/*     */   }
/*     */ 
/*     */   public Point transformViewToImage(Point p)
/*     */   {
/* 142 */     int width = getWidth();
/* 143 */     int height = getHeight();
/* 144 */     Point ret = new Point(-width / 2 + this.translation.x + p.x, -height / 2 + this.translation.y + p.y);
/*     */     Point tmp55_53 = ret; tmp55_53.y = ((int)(tmp55_53.y / this.zoomFactor));
/*     */     Point tmp71_69 = ret; tmp71_69.x = ((int)(tmp71_69.x / this.zoomFactor));
/*     */ 
/* 150 */     if ((ret.x < 0) || (ret.y < 0) || (ret.x >= this.image.getWidth()) || (ret.y >= this.image.getHeight()))
/*     */     {
/* 153 */       ret.x = -1;
/* 154 */       ret.y = -1;
/*     */     }
/* 156 */     return ret;
/*     */   }
/*     */ 
/*     */   public Point transformImageToView(Point p)
/*     */   {
/* 165 */     int width = getWidth();
/* 166 */     int height = getHeight();
/*     */ 
/* 168 */     int offsetX = width / 2 - this.translation.x;
/* 169 */     int offsetY = height / 2 - this.translation.y;
/*     */ 
/* 171 */     int viewX = (int)(-offsetX / this.zoomFactor);
/* 172 */     int viewY = (int)(-offsetY / this.zoomFactor);
/*     */ 
/* 174 */     Point ret = new Point();
/* 175 */     ret.x = ((int)(p.x * this.zoomFactor + offsetX));
/* 176 */     ret.y = ((int)(p.y * this.zoomFactor + offsetY));
/*     */ 
/* 178 */     if (offsetX < 0)
/* 179 */       ret.x -= offsetX + (int)(viewX * this.zoomFactor);
/* 180 */     if (offsetY < 0)
/* 181 */       ret.y -= offsetY + (int)(viewY * this.zoomFactor);
/*     */     Point tmp165_163 = ret; tmp165_163.x = ((int)(tmp165_163.x * ((width + this.zoomFactor) / width)));
/*     */     Point tmp187_185 = ret; tmp187_185.y = ((int)(tmp187_185.y * ((height + this.zoomFactor) / height)));
/*     */ 
/* 186 */     return ret;
/*     */   }
/*     */ 
/*     */   public Rectangle transformImageToView(Rectangle r)
/*     */   {
/* 195 */     Rectangle ret = new Rectangle(transformImageToView(r.getLocation()));
/*     */ 
/* 197 */     ret.width = ((int)((r.width + 2) * this.zoomFactor));
/* 198 */     ret.height = ((int)((r.height + 2) * this.zoomFactor));
/* 199 */     return ret;
/*     */   }
/*     */ 
/*     */   public void translate(int x, int y)
/*     */   {
/* 208 */     this.translation.translate(x, y);
/* 209 */     this.updateView = true;
/*     */   }
/*     */ 
/*     */   public void zoom(int amount)
/*     */   {
/* 217 */     setZoom(this.zoomFactor + amount * 0.3F);
/*     */   }
/*     */ 
/*     */   public void setDefaultZoomFilter(ResizingFilter filter)
/*     */   {
/* 222 */     this.zoomFilter = filter;
/* 223 */     this.defaultZoomFilter = filter;
/*     */   }
/*     */ 
/*     */   protected ResizingFilter getZoomFilter(float factor)
/*     */   {
/* 233 */     return this.defaultZoomFilter;
/*     */   }
/*     */ 
/*     */   public void setZoom(float factor)
/*     */   {
/* 241 */     if (factor > 32.0F) {
/* 242 */       factor = 32.0F;
/*     */     }
/* 244 */     if (factor <= 1.0F)
/*     */     {
/* 249 */       factor = 1.0F;
/*     */     }
/* 251 */     else if ((this.scaledImage == null) || (factor != this.zoomFactor))
/*     */     {
/* 254 */       int width = (int)(this.image.getWidth() * factor);
/* 255 */       int height = (int)(this.image.getHeight() * factor);
/*     */ 
/* 257 */       this.zoomFilter = getZoomFilter(factor);
/*     */ 
/* 259 */       if (this.zoomOffscreenImage)
/*     */       {
/* 261 */         this.scaledImage = new ImageColour(width, height);
/* 262 */         this.zoomFilter.filter(this.image, this.scaledImage);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 267 */     this.translation.setLocation((int)(this.translation.x * factor / this.zoomFactor), (int)(this.translation.y * factor / this.zoomFactor));
/*     */ 
/* 271 */     this.zoomFactor = factor;
/* 272 */     this.updateView = true;
/*     */   }
/*     */ 
/*     */   public boolean isDoubleBuffered()
/*     */   {
/* 280 */     return true;
/*     */   }
/*     */ 
/*     */   public void update(Graphics g)
/*     */   {
/* 285 */     paint(g);
/*     */   }
/*     */ 
/*     */   public void repaintView(int x, int y, int width, int height)
/*     */   {
/* 293 */     this.updateView = true;
/* 294 */     repaint(x, y, width, height);
/*     */   }
/*     */ 
/*     */   private void drawScissors(Graphics g)
/*     */   {
/* 300 */     int rad = 3;
/* 301 */     Point prevPoint = null;
/* 302 */     for (Iterator it = this.scissorsPath.getSeeds(); it.hasNext(); )
/*     */     {
/* 304 */       Point p = (Point)it.next();
/* 305 */       p = transformImageToView(p);
/* 306 */       g.setColor(Color.RED);
/* 307 */       g.drawOval(p.x - rad, p.y - rad, rad * 2, rad * 2);
/*     */ 
/* 313 */       prevPoint = p;
/*     */     }
/*     */ 
/* 316 */     g.setColor(Color.BLUE);
/* 317 */     Point p1 = new Point();
/* 318 */     Point p2 = new Point();
/* 319 */     for (Iterator it = this.scissorsPath.getEdges(); it.hasNext(); )
/*     */     {
/* 321 */       Edge edge = (Edge)it.next();
/* 322 */       if ((edge instanceof StraightLineEdge))
/*     */       {
/* 324 */         p1.x = edge.getNodeA().x;
/* 325 */         p1.y = edge.getNodeA().y;
/* 326 */         p2.x = edge.getNodeB().x;
/* 327 */         p2.y = edge.getNodeB().y;
/* 328 */         p1 = transformImageToView(p1);
/* 329 */         p2 = transformImageToView(p2);
/* 330 */         g.drawLine(p1.x, p1.y, p2.x, p2.y);
/*     */       }
/*     */       else {
/* 333 */         drawCrackSequence(g, edge.getCrackSequence().iterator());
/*     */       }
/*     */     }
/* 336 */     g.setColor(Color.CYAN);
/* 337 */     for (Iterator it = this.scissorsPath.getHoverEdges(); it.hasNext(); )
/*     */     {
/* 339 */       Edge edge = (Edge)it.next();
/* 340 */       if ((edge instanceof StraightLineEdge))
/*     */       {
/* 342 */         p1.x = edge.getNodeA().x;
/* 343 */         p1.y = edge.getNodeA().y;
/* 344 */         p2.x = edge.getNodeB().x;
/* 345 */         p2.y = edge.getNodeB().y;
/* 346 */         p1 = transformImageToView(p1);
/* 347 */         p2 = transformImageToView(p2);
/* 348 */         g.drawLine(p1.x, p1.y, p2.x, p2.y);
/*     */       }
/*     */       else {
/* 351 */         drawCrackSequence(g, edge.getCrackSequence().iterator());
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void drawScissorsOverlay(Graphics g) {
/* 357 */     g.setColor(Color.red);
/* 358 */     int i = 0;
/* 359 */     for (Iterator it = this.scissorsOverlay.iterator(); it.hasNext(); )
/*     */     {
/* 361 */       if (i++ > this.scissorsOverlaySegments)
/*     */         break;
/* 363 */       Edge edge = (Edge)it.next();
/* 364 */       drawCrackSequence(g, edge.getCrackSequence().iterator());
/*     */     }
/*     */   }
/*     */ 
/*     */   private void drawCrackSequence(Graphics g, Iterator cracks)
/*     */   {
/* 371 */     Point p1 = new Point();
/* 372 */     Point p2 = new Point();
/* 373 */     for (Iterator it = cracks; it.hasNext(); )
/*     */     {
/* 375 */       Crack crack = (Crack)it.next();
/* 376 */       if (crack.y1 == crack.y2)
/*     */       {
/* 378 */         p1.y = crack.y1;
/* 379 */         p2.y = (crack.y1 + 1);
/*     */ 
/* 382 */         if (crack.x1 > crack.x2)
/* 383 */           p1.x = crack.x1;
/*     */         else
/* 385 */           p1.x = crack.x2;
/* 386 */         p2.x = p1.x;
/*     */       }
/*     */       else
/*     */       {
/* 391 */         p1.x = crack.x1;
/* 392 */         p2.x = (crack.x1 + 1);
/* 393 */         if (crack.y1 > crack.y2)
/* 394 */           p1.y = crack.y1;
/*     */         else
/* 396 */           p1.y = crack.y2;
/* 397 */         p2.y = p1.y;
/*     */       }
/*     */ 
/* 400 */       p1 = transformImageToView(p1);
/* 401 */       p2 = transformImageToView(p2);
/* 402 */       g.drawLine(p1.x, p1.y, p2.x, p2.y);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void drawHighlightPoint(Graphics g)
/*     */   {
/* 409 */     Point p = transformImageToView(this.highlightPoint);
/* 410 */     int sz = 4;
/* 411 */     g.setColor(Color.RED);
/* 412 */     g.drawLine(p.x - sz, p.y - sz, p.x + sz, p.y + sz);
/* 413 */     g.drawLine(p.x - sz, p.y + sz, p.x + sz, p.y - sz);
/*     */   }
/*     */ 
/*     */   public void updateView()
/*     */   {
/* 423 */     Rectangle rect = this.explorer.update();
/*     */ 
/* 426 */     rect = transformImageToView(rect);
/*     */ 
/* 429 */     rect = rect.intersection(new Rectangle(0, 0, getWidth(), getHeight()));
/*     */ 
/* 433 */     repaintView(rect.x, rect.y, rect.width, rect.height);
/*     */   }
/*     */ 
/*     */   public void paint(Graphics g)
/*     */   {
/* 442 */     int width = getWidth();
/* 443 */     int height = getHeight();
/*     */ 
/* 446 */     if (this.image == null)
/*     */     {
/* 448 */       g.setColor(Color.black);
/* 449 */       g.fillRect(0, 0, width, height);
/* 450 */       return;
/*     */     }
/*     */ 
/* 454 */     if ((this.backImage == null) || (this.backImage.getWidth(null) != width) || (this.backImage.getHeight(null) != height))
/*     */     {
/* 458 */       this.backImage = createImage(width, height);
/* 459 */       this.backGraphics = this.backImage.getGraphics();
/*     */ 
/* 462 */       if (!this.zoomOffscreenImage) {
/* 463 */         this.scaledImage = new ImageColour(width + 64, height + 64);
/*     */       }
/* 465 */       this.updateView = true;
/*     */     }
/*     */ 
/* 471 */     Point offset = new Point(width / 2 - this.translation.x, height / 2 - this.translation.y);
/*     */ 
/* 475 */     if ((!this.zoomOffscreenImage) && (this.zoomFactor > 1.0F))
/*     */     {
/* 479 */       int imageHeight = this.image.getHeight();
/* 480 */       int imageWidth = this.image.getWidth();
/*     */ 
/* 483 */       Rectangle viewport = new Rectangle((int)(-offset.x / this.zoomFactor), (int)(-offset.y / this.zoomFactor), (int)(width / this.zoomFactor + 1.0F), (int)(height / this.zoomFactor + 1.0F));
/*     */ 
/* 489 */       if (viewport.x < 0)
/*     */       {
/* 491 */         viewport.width += viewport.x;
/* 492 */         viewport.x = 0;
/*     */       }
/* 494 */       if (viewport.y < 0)
/*     */       {
/* 496 */         viewport.height += viewport.y;
/* 497 */         viewport.y = 0;
/*     */       }
/* 499 */       if (viewport.x + viewport.width > imageWidth)
/* 500 */         viewport.width = (imageWidth - viewport.x);
/* 501 */       if (viewport.y + viewport.height > imageHeight) {
/* 502 */         viewport.height = (imageHeight - viewport.y);
/*     */       }
/*     */ 
/* 505 */       Rectangle destRect = new Rectangle(0, 0, width, height);
/* 506 */       if (offset.x > 0)
/*     */       {
/* 509 */         destRect.x = offset.x;
/* 510 */         destRect.width -= destRect.x;
/*     */       }
/* 512 */       if (offset.y > 0)
/*     */       {
/* 515 */         destRect.y = offset.y;
/* 516 */         destRect.height -= destRect.y;
/*     */       }
/* 518 */       destRect.width = ((int)((viewport.width + 1) * this.zoomFactor));
/* 519 */       destRect.height = ((int)((viewport.height + 1) * this.zoomFactor));
/*     */ 
/* 522 */       if (this.updateView)
/*     */       {
/* 525 */         if ((destRect.width < width) || (destRect.height < height)) {
/* 526 */           this.scaledImage.fill(0);
/*     */         }
/* 528 */         if ((destRect.width > 0) && (destRect.height > 0)) {
/* 529 */           this.zoomFilter.filter(this.image, this.scaledImage, viewport, destRect);
/*     */         }
/*     */       }
/* 532 */       int shiftX = 0;
/* 533 */       if (offset.x < 0)
/* 534 */         shiftX = offset.x + (int)(viewport.x * this.zoomFactor);
/* 535 */       int shiftY = 0;
/* 536 */       if (offset.y < 0) {
/* 537 */         shiftY = offset.y + (int)(viewport.y * this.zoomFactor);
/*     */       }
/* 539 */       Image img = this.scaledImage.getAWTImage();
/* 540 */       if (this.scissorsPath != null) {
/* 541 */         drawScissors(img.getGraphics());
/*     */       }
/* 543 */       if (this.scissorsOverlay != null) {
/* 544 */         drawScissorsOverlay(img.getGraphics());
/*     */       }
/* 546 */       if (this.highlightPoint != null) {
/* 547 */         drawHighlightPoint(img.getGraphics());
/*     */       }
/*     */ 
/* 550 */       g.drawImage(img, shiftX, shiftY, null);
/*     */     }
/*     */     else
/*     */     {
/*     */       Image bltImage;
/* 555 */       if ((this.zoomOffscreenImage) && (this.zoomFactor > 1.0F))
/* 556 */         bltImage = this.scaledImage.getAWTImage();
/*     */       else {
/* 558 */         bltImage = this.image.getAWTImage();
/*     */       }
/*     */ 
/* 561 */       this.backGraphics.setColor(backgroundColor);
/* 562 */       this.backGraphics.fillRect(0, 0, width, height);
/*     */ 
/* 564 */       this.backGraphics.drawImage(bltImage, offset.x, offset.y, null);
/*     */ 
/* 570 */       if (this.scissorsPath != null) {
/* 571 */         drawScissors(this.backGraphics);
/*     */       }
/* 573 */       if (this.scissorsOverlay != null) {
/* 574 */         drawScissorsOverlay(this.backGraphics);
/*     */       }
/* 576 */       if (this.highlightPoint != null) {
/* 577 */         drawHighlightPoint(this.backGraphics);
/*     */       }
/*     */ 
/* 580 */       g.drawImage(this.backImage, 0, 0, null);
/*     */     }
/*     */ 
/* 583 */     this.updateView = false;
/*     */   }
/*     */   public void onMaskEvent(PaintExplorerMaskEvent event) {
/*     */   }
/*     */   public void onScissorsEvent(PaintExplorerScissorsEvent event) {
/*     */   }
/*     */   public void onProgress(PaintExplorerProgressEvent event) {
/*     */   }
/*     */   public void onHistoryEvent(PaintExplorerHistoryEvent event) {
/* 592 */     if ((event instanceof PaintExplorerScissorHistoryEvent))
/*     */     {
/* 594 */       PaintExplorerScissorHistoryEvent scissorEvent = (PaintExplorerScissorHistoryEvent)event;
/*     */ 
/* 599 */       this.scissorsOverlay = scissorEvent.getEdges();
/*     */ 
/* 601 */       int iterations = 20;
/*     */       int delta;
/* 602 */       if (scissorEvent.wasUndone())
/*     */       {
/* 604 */         delta = -this.scissorsOverlay.size() / iterations;
/* 605 */         if (delta == 0)
/* 606 */           delta = -1;
/* 607 */         this.scissorsOverlaySegments = this.scissorsOverlay.size();
/*     */       }
/*     */       else
/*     */       {
/* 611 */         delta = this.scissorsOverlay.size() / iterations;
/* 612 */         if (delta == 0)
/* 613 */           delta = 1;
/* 614 */         this.scissorsOverlaySegments = 0;
/*     */       }
/* 616 */       this.timer.schedule(new UpdateScissorOverlayTask(delta), 200L, 5L);
/* 617 */       repaint();
/*     */     }
/*     */   }
/*     */ 
/*     */   private class UpdateScissorOverlayTask extends TimerTask
/*     */   {
/*     */     private int delta;
/*     */ 
/*     */     public UpdateScissorOverlayTask(int delta)
/*     */     {
/* 627 */       this.delta = delta;
/*     */     }
/*     */ 
/*     */     public void run()
/*     */     {
/* 632 */       //PaintView.access$012(PaintView.this, this.delta);
/* 633 */       if ((PaintView.this.scissorsOverlaySegments <= 0) || (PaintView.this.scissorsOverlaySegments > PaintView.this.scissorsOverlay.size()))
/*     */       {
/* 636 */         PaintView.this.scissorsOverlay = null;
/* 637 */         cancel();
/*     */       }
/* 639 */       PaintView.this.repaint();
/*     */     }
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.PaintView
 * JD-Core Version:    0.6.2
 */