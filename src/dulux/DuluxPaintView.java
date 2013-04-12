/*     */ package dulux;
/*     */ 
/*     */ import duluxskin.Widget;
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Image;
/*     */ import java.awt.Point;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Shape;
/*     */ import java.util.Iterator;
/*     */ import java.util.Timer;
/*     */ import java.util.TimerTask;
/*     */ import java.util.Vector;
/*     */ import pelib.Crack;
/*     */ import pelib.Edge;
/*     */ import pelib.ImageColour;
/*     */ import pelib.Node;
/*     */ import pelib.PaintExplorer;
/*     */ import pelib.PaintExplorerHistoryEvent;
/*     */ import pelib.PaintExplorerListener;
/*     */ import pelib.PaintExplorerMaskEvent;
/*     */ import pelib.PaintExplorerProgressEvent;
/*     */ import pelib.PaintExplorerScissorHistoryEvent;
/*     */ import pelib.PaintExplorerScissorsEvent;
/*     */ import pelib.ScissorsPath;
/*     */ import pelib.StraightLineEdge;
/*     */ import pelib.filters.PointFilter;
/*     */ import pelib.filters.ResizingFilter;
/*     */ import pelib.filters.TriangulationFilter;
/*     */ 
/*     */ public class DuluxPaintView extends Widget
/*     */   implements PaintExplorerListener
/*     */ {
/*     */   private int originalWidth;
/*     */   private int originalHeight;
/*     */   public static final float MAX_ZOOM = 32.0F;
/*  25 */   public static float maxZoomOut = 1.0F;
/*  26 */   public Point offset = new Point(0, 0);
/*  27 */   private final int DEFAULT_WIDTH = 618;
/*  28 */   private final int DEFAULT_HEIGHT = 464;
/*  29 */   Rectangle destRect = new Rectangle(0, 0, this.width, this.height);
/*     */   private PaintExplorer explorer;
/*     */   private ImageColour image;
/*     */   private ImageColour scaledImage;
/*     */   private boolean updateView;
/*     */   private float zoomFactor;
/*     */   private ResizingFilter defaultZoomFilter;
/*     */   private ResizingFilter zoomFilter;
/*     */   private Point translation;
/*     */   private ScissorsPath scissorsPath;
/*     */   private Vector scissorsOverlay;
/*     */   private int scissorsOverlaySegments;
/*     */   private Point highlightPoint;
/*  46 */   private static final Color backgroundColor = new Color(0, 0, 0);
/*     */   private boolean zoomOffscreenImage;
/*     */   private Timer timer;
/*     */   private boolean readyToDraw;
/*     */   private boolean showScissor;
/*     */ 
/*     */   public DuluxPaintView(String id, int x, int y, int width, int height, PaintExplorer explorer)
/*     */   {
/*  57 */     super(id, x, y);
/*  58 */     this.width = width;
/*  59 */     this.height = height;
/*     */ 
/*  61 */     this.showScissor = true;
/*     */ 
/*  63 */     this.explorer = explorer;
/*  64 */     explorer.addListener(this);
/*     */ 
/*  68 */     this.translation = new Point(0, 0);
/*     */ 
/*  70 */     this.zoomOffscreenImage = false;
/*  71 */     this.zoomFilter = getZoomFilter(1.0F);
/*  72 */     this.defaultZoomFilter = new PointFilter();
/*     */ 
/*  74 */     this.timer = new Timer();
/*  75 */     this.offset = new Point(x, y);
/*     */   }
/*     */ 
/*     */   public void setShowScissor(boolean show)
/*     */   {
/*  81 */     this.showScissor = show;
/*     */   }
/*     */ 
/*     */   public void setImage(ImageColour image) {
/*  85 */     this.readyToDraw = false;
/*  86 */     this.scaledImage = null;
/*  87 */     setImageSubstitute(image);
/*     */ 
/*  89 */     if (image != null) {
/*  90 */       this.defaultZoomFilter = new TriangulationFilter(image);
/*     */     }
/*  92 */     if (image != null)
/*     */     {
/*  94 */       this.originalWidth = image.getWidth();
/*  95 */       this.originalHeight = image.getHeight();
/*  96 */       float horizontal_scale = 618.0F / image.getWidth();
/*  97 */       float vertical_scale = 464.0F / image.getHeight();
/*  98 */       float scale = horizontal_scale < vertical_scale ? horizontal_scale : vertical_scale;
/*     */ 
/* 100 */       maxZoomOut = scale;
/* 101 */       resetView(scale);
/* 102 */       this.zoomFilter = getZoomFilter(scale);
/*     */     }
/*     */ 
/* 105 */     this.readyToDraw = true;
/*     */   }
/*     */ 
/*     */   public void setImageSubstitute(ImageColour image)
/*     */   {
/* 113 */     this.image = image;
/* 114 */     this.updateView = true;
/* 115 */     invalidate();
/*     */   }
/*     */ 
/*     */   public ImageColour getImage() {
/* 119 */     return this.image;
/*     */   }
/*     */ 
/*     */   public void setScissorsPath(ScissorsPath scissorsPath) {
/* 123 */     this.scissorsPath = scissorsPath;
/*     */   }
/*     */ 
/*     */   public void setHighlight(int x, int y) {
/* 127 */     if (this.highlightPoint == null) {
/* 128 */       this.highlightPoint = new Point();
/*     */     }
/* 130 */     this.highlightPoint.x = x;
/* 131 */     this.highlightPoint.y = y;
/*     */   }
/*     */ 
/*     */   public void setHighlightNearestNode(Point p) {
/* 135 */     Node n = this.explorer.findNearestNode(p);
/* 136 */     setHighlight(n.x, n.y);
/*     */   }
/*     */ 
/*     */   public void unsetHighlight() {
/* 140 */     this.highlightPoint = null;
/*     */   }
/*     */ 
/*     */   public void resetView() {
/* 144 */     setZoom(1.0F);
/* 145 */     if (this.image != null) {
/* 146 */       this.translation = new Point(this.width / 2, this.height / 2);
/*     */     }
/*     */ 
/* 150 */     invalidate();
/*     */   }
/*     */ 
/*     */   public void resetView(float zoom) {
/* 154 */     setZoom(zoom);
/* 155 */     if (this.image != null) {
/* 156 */       this.translation = new Point(this.width / 2, this.height / 2);
/*     */     }
/*     */ 
/* 160 */     invalidate();
/*     */   }
/*     */ 
/*     */   public Point transformViewToImage(Point p)
/*     */   {
/* 169 */     int width = getWidth();
/* 170 */     int height = getHeight();
/* 171 */     Point ret = new Point(-(width / 2 + shiftGraphics().x) + this.translation.x + p.x, -(height / 2 + shiftGraphics().y) + this.translation.y + p.y);
/*     */     Point tmp71_69 = ret; tmp71_69.y = ((int)(tmp71_69.y / this.zoomFactor));
/*     */     Point tmp87_85 = ret; tmp87_85.x = ((int)(tmp87_85.x / this.zoomFactor));
/*     */ 
/* 178 */     if ((ret.x < 0) || (ret.y < 0) || (ret.x >= this.image.getWidth()) || (ret.y >= this.image.getHeight()))
/*     */     {
/* 180 */       ret.x = -1;
/* 181 */       ret.y = -1;
/*     */     }
/* 183 */     return ret;
/*     */   }
/*     */ 
/*     */   public Point transformImageToView(Point p)
/*     */   {
/* 191 */     int width = getWidth();
/* 192 */     int height = getHeight();
/*     */ 
/* 194 */     int offsetX = width / 2 - this.translation.x;
/* 195 */     int offsetY = height / 2 - this.translation.y;
/*     */ 
/* 197 */     int viewX = (int)(-offsetX / this.zoomFactor);
/* 198 */     int viewY = (int)(-offsetY / this.zoomFactor);
/*     */ 
/* 200 */     Point ret = new Point();
/* 201 */     ret.x = ((int)(p.x * this.zoomFactor + offsetX));
/* 202 */     ret.y = ((int)(p.y * this.zoomFactor + offsetY));
/*     */ 
/* 204 */     if (offsetX < 0) {
/* 205 */       ret.x -= offsetX + (int)(viewX * this.zoomFactor);
/*     */     }
/* 207 */     if (offsetY < 0)
/* 208 */       ret.y -= offsetY + (int)(viewY * this.zoomFactor);
/*     */     Point tmp165_163 = ret; tmp165_163.x = ((int)(tmp165_163.x * ((width + this.zoomFactor) / width)));
/*     */     Point tmp187_185 = ret; tmp187_185.y = ((int)(tmp187_185.y * ((height + this.zoomFactor) / height)));
/*     */ 
/* 214 */     return ret;
/*     */   }
/*     */ 
/*     */   public Rectangle transformImageToView(Rectangle r)
/*     */   {
/* 222 */     Rectangle ret = new Rectangle(transformImageToView(r.getLocation()));
/*     */ 
/* 224 */     ret.width = ((int)((r.width + 2) * this.zoomFactor));
/* 225 */     ret.height = ((int)((r.height + 2) * this.zoomFactor));
/* 226 */     return ret;
/*     */   }
/*     */ 
/*     */   public void translate(int x, int y)
/*     */   {
/* 234 */     this.translation.translate(x, y);
/* 235 */     this.updateView = true;
/*     */   }
/*     */ 
/*     */   public void zoom(int amount)
/*     */   {
/* 242 */     setZoom(this.zoomFactor + amount * 0.3F);
/*     */   }
/*     */ 
/*     */   public void setDefaultZoomFilter(ResizingFilter filter) {
/* 246 */     this.zoomFilter = filter;
/* 247 */     this.defaultZoomFilter = filter;
/*     */   }
/*     */ 
/*     */   protected ResizingFilter getZoomFilter(float factor)
/*     */   {
/* 256 */     return this.defaultZoomFilter;
/*     */   }
/*     */ 
/*     */   public float getZoom() {
/* 260 */     return this.zoomFactor;
/*     */   }
/*     */ 
/*     */   public void setZoom(float factor)
/*     */   {
/* 267 */     if (factor > 32.0F) {
/* 268 */       factor = 32.0F;
/*     */     }
/* 270 */     if (factor < maxZoomOut) {
/* 271 */       factor = maxZoomOut;
/*     */     }
/*     */ 
/* 274 */     if ((factor > 1.0F) || (
/* 280 */       (this.scaledImage == null) || (factor != this.zoomFactor)))
/*     */     {
/* 282 */       int width = (int)(this.image.getWidth() * factor);
/* 283 */       int height = (int)(this.image.getHeight() * factor);
/*     */ 
/* 285 */       this.zoomFilter = getZoomFilter(factor);
/*     */ 
/* 287 */       if (this.zoomOffscreenImage) {
/* 288 */         this.scaledImage = new ImageColour(width, height);
/* 289 */         this.zoomFilter.filter(this.image, this.scaledImage);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 294 */     this.translation.setLocation((int)(this.translation.x * factor / this.zoomFactor), (int)(this.translation.y * factor / this.zoomFactor));
/*     */ 
/* 298 */     this.zoomFactor = factor;
/* 299 */     this.updateView = true;
/*     */   }
/*     */ 
/*     */   public void invalidateView(int x, int y, int width, int height)
/*     */   {
/* 306 */     this.updateView = true;
/* 307 */     invalidate(x, y, width, height);
/*     */   }
/*     */ 
/*     */   private void drawScissors(Graphics g)
/*     */   {
/* 312 */     int rad = 3;
/* 313 */     Point prevPoint = null;
/* 314 */     for (Iterator it = this.scissorsPath.getSeeds(); it.hasNext(); ) {
/* 315 */       Point p = (Point)it.next();
/* 316 */       p = transformImageToView(p);
/* 317 */       g.setColor(Color.RED);
/* 318 */       g.drawOval(p.x - rad, p.y - rad, rad * 2, rad * 2);
/*     */ 
/* 324 */       prevPoint = p;
/*     */     }
/*     */ 
/* 327 */     g.setColor(Color.BLUE);
/* 328 */     Point p1 = new Point();
/* 329 */     Point p2 = new Point();
/* 330 */     for (Iterator it = this.scissorsPath.getEdges(); it.hasNext(); ) {
/* 331 */       Edge edge = (Edge)it.next();
/* 332 */       if ((edge instanceof StraightLineEdge)) {
/* 333 */         p1.x = edge.getNodeA().x;
/* 334 */         p1.y = edge.getNodeA().y;
/* 335 */         p2.x = edge.getNodeB().x;
/* 336 */         p2.y = edge.getNodeB().y;
/*     */ 
/* 338 */         p1 = transformImageToView(p1);
/* 339 */         p2 = transformImageToView(p2);
/*     */ 
/* 341 */         g.drawLine(p1.x, p1.y, p2.x, p2.y);
/*     */       } else {
/* 343 */         drawCrackSequence(g, edge.getCrackSequence().iterator());
/*     */       }
/*     */     }
/*     */ 
/* 347 */     g.setColor(Color.CYAN);
/* 348 */     for (Iterator it = this.scissorsPath.getHoverEdges(); it.hasNext(); ) {
/* 349 */       Edge edge = (Edge)it.next();
/* 350 */       if ((edge instanceof StraightLineEdge)) {
/* 351 */         p1.x = edge.getNodeA().x;
/* 352 */         p1.y = edge.getNodeA().y;
/* 353 */         p2.x = edge.getNodeB().x;
/* 354 */         p2.y = edge.getNodeB().y;
/* 355 */         p1 = transformImageToView(p1);
/* 356 */         p2 = transformImageToView(p2);
/* 357 */         g.drawLine(p1.x, p1.y, p2.x, p2.y);
/*     */       } else {
/* 359 */         drawCrackSequence(g, edge.getCrackSequence().iterator());
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void drawScissorsOverlay(Graphics g) {
/* 365 */     g.setColor(Color.red);
/* 366 */     int i = 0;
/* 367 */     for (Iterator it = this.scissorsOverlay.iterator(); (it.hasNext()) && 
/* 368 */       (i++ <= this.scissorsOverlaySegments); )
/*     */     {
/* 371 */       Edge edge = (Edge)it.next();
/* 372 */       drawCrackSequence(g, edge.getCrackSequence().iterator());
/*     */     }
/*     */   }
/*     */ 
/*     */   private void drawCrackSequence(Graphics g, Iterator cracks)
/*     */   {
/* 378 */     Point p1 = new Point();
/* 379 */     Point p2 = new Point();
/* 380 */     for (Iterator it = cracks; it.hasNext(); ) {
/* 381 */       Crack crack = (Crack)it.next();
/* 382 */       if (crack.y1 == crack.y2) {
/* 383 */         p1.y = crack.y1;
/* 384 */         p2.y = (crack.y1 + 1);
/*     */ 
/* 387 */         if (crack.x1 > crack.x2)
/* 388 */           p1.x = crack.x1;
/*     */         else {
/* 390 */           p1.x = crack.x2;
/*     */         }
/* 392 */         p2.x = p1.x;
/*     */       }
/*     */       else {
/* 395 */         p1.x = crack.x1;
/* 396 */         p2.x = (crack.x1 + 1);
/* 397 */         if (crack.y1 > crack.y2)
/* 398 */           p1.y = crack.y1;
/*     */         else {
/* 400 */           p1.y = crack.y2;
/*     */         }
/* 402 */         p2.y = p1.y;
/*     */       }
/*     */ 
/* 405 */       p1 = transformImageToView(p1);
/* 406 */       p2 = transformImageToView(p2);
/* 407 */       g.drawLine(p1.x, p1.y, p2.x, p2.y);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void drawHighlightPoint(Graphics g)
/*     */   {
/* 413 */     Point p = transformImageToView(this.highlightPoint);
/* 414 */     int sz = 4;
/* 415 */     g.setColor(Color.RED);
/* 416 */     g.drawLine(p.x - sz, p.y - sz, p.x + sz, p.y + sz);
/* 417 */     g.drawLine(p.x - sz, p.y + sz, p.x + sz, p.y - sz);
/*     */   }
/*     */ 
/*     */   public void updateView()
/*     */   {
/* 426 */     Rectangle rect = this.explorer.update();
/*     */ 
/* 429 */     rect = transformImageToView(rect);
/*     */ 
/* 432 */     rect = rect.intersection(new Rectangle(0, 0, getWidth(), getHeight()));
/*     */ 
/* 436 */     if ((rect.width > 0) && (rect.height > 0))
/* 437 */       invalidateView(rect.x, rect.y, rect.width, rect.height);
/*     */   }
/*     */ 
/*     */   public void paint(Graphics g)
/*     */   {
/* 446 */     if ((this.image == null) || (!this.readyToDraw)) {
/* 447 */       return;
/*     */     }
/*     */ 
/* 450 */     Shape oldClip = g.getClip();
/* 451 */     g.setClip(0, 0, this.width, this.height);
/*     */ 
/* 455 */     if ((!this.zoomOffscreenImage) && (this.scaledImage == null)) {
/* 456 */       this.scaledImage = new ImageColour(this.width + 64, this.height + 64);
/*     */ 
/* 458 */       this.updateView = true;
/*     */     }
/*     */ 
/* 465 */     this.offset = new Point(this.width / 2 - this.translation.x, this.height / 2 - this.translation.y);
/*     */ 
/* 470 */     if ((!this.zoomOffscreenImage) && (this.zoomFactor != 1.0F))
/*     */     {
/* 473 */       int imageHeight = this.image.getHeight();
/* 474 */       int imageWidth = this.image.getWidth();
/*     */ 
/* 478 */       int width_vp = (int)(this.width / this.zoomFactor + 1.0F);
/* 479 */       int height_vp = (int)(this.height / this.zoomFactor + 1.0F);
/*     */ 
/* 481 */       if (width_vp > imageWidth) {
/* 482 */         width_vp = imageWidth;
/*     */       }
/* 484 */       if (height_vp > imageHeight) {
/* 485 */         height_vp = imageHeight;
/*     */       }
/*     */ 
/* 488 */       Rectangle viewport = new Rectangle((int)(-this.offset.x / this.zoomFactor), (int)(-this.offset.y / this.zoomFactor), width_vp, height_vp);
/*     */ 
/* 495 */       if (viewport.x < 0) {
/* 496 */         viewport.width += viewport.x;
/* 497 */         viewport.x = 0;
/*     */       }
/* 499 */       if (viewport.y < 0) {
/* 500 */         viewport.height += viewport.y;
/* 501 */         viewport.y = 0;
/*     */       }
/* 503 */       if (viewport.x + viewport.width > imageWidth) {
/* 504 */         viewport.width = (imageWidth - viewport.x);
/*     */       }
/* 506 */       if (viewport.y + viewport.height > imageHeight) {
/* 507 */         viewport.height = (imageHeight - viewport.y);
/*     */       }
/*     */ 
/* 512 */       this.destRect = new Rectangle(0, 0, this.width, this.height);
/*     */ 
/* 517 */       if (this.offset.x > 0)
/*     */       {
/* 519 */         this.destRect.x = this.offset.x;
/* 520 */         this.destRect.width -= this.destRect.x;
/*     */       }
/* 522 */       if (this.offset.y > 0)
/*     */       {
/* 524 */         this.destRect.y = this.offset.y;
/* 525 */         this.destRect.height -= this.destRect.y;
/*     */       }
/*     */ 
/* 528 */       this.destRect.width = ((int)((viewport.width + 1) * this.zoomFactor));
/* 529 */       this.destRect.height = ((int)((viewport.height + 1) * this.zoomFactor));
/*     */ 
/* 532 */       if (this.updateView)
/*     */       {
/* 534 */         if ((this.destRect.width < this.width) || (this.destRect.height < this.height)) {
/* 535 */           this.scaledImage.fill(0);
/*     */         }
/*     */ 
/* 538 */         if ((this.destRect.width > 0) && (this.destRect.height > 0)) {
/* 539 */           this.zoomFilter.filter(this.image, this.scaledImage, viewport, this.destRect);
/*     */         }
/*     */       }
/*     */ 
/* 543 */       int shiftX = 0;
/* 544 */       if (this.offset.x < 0) {
/* 545 */         shiftX = this.offset.x + (int)(viewport.x * this.zoomFactor);
/*     */       }
/* 547 */       int shiftY = 0;
/* 548 */       if (this.offset.y < 0) {
/* 549 */         shiftY = this.offset.y + (int)(viewport.y * this.zoomFactor);
/*     */       }
/*     */ 
/* 552 */       Image img = this.scaledImage.getAWTImage();
/*     */ 
/* 557 */       g.drawImage(img, this.destRect.x, this.destRect.y, this.destRect.x + this.destRect.width, this.destRect.y + this.destRect.height, this.destRect.x, this.destRect.y, this.destRect.x + this.destRect.width, this.destRect.y + this.destRect.height, null);
/*     */ 
/* 566 */       if ((this.scissorsPath != null) && (this.showScissor == true)) {
/* 567 */         drawScissors(g);
/*     */       }
/*     */ 
/* 570 */       if (this.scissorsOverlay != null) {
/* 571 */         drawScissorsOverlay(g);
/*     */       }
/*     */ 
/* 574 */       if (this.highlightPoint != null)
/* 575 */         drawHighlightPoint(g);
/*     */     }
/*     */     else
/*     */     {
/*     */       Image bltImage;
/* 588 */       if ((this.zoomOffscreenImage) && (this.zoomFactor != 1.0F))
/* 589 */         bltImage = this.scaledImage.getAWTImage();
/*     */       else {
/* 591 */         bltImage = this.image.getAWTImage();
/*     */       }
/*     */ 
/* 598 */       g.drawImage(bltImage, this.offset.x, this.offset.y, null);
/*     */ 
/* 605 */       if ((this.scissorsPath != null) && (this.showScissor == true)) {
/* 606 */         drawScissors(g);
/*     */       }
/*     */ 
/* 609 */       if (this.scissorsOverlay != null) {
/* 610 */         drawScissorsOverlay(g);
/*     */       }
/*     */ 
/* 613 */       if (this.highlightPoint != null) {
/* 614 */         drawHighlightPoint(g);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 619 */     this.updateView = false;
/* 620 */     g.setClip(oldClip);
/*     */   }
/*     */ 
/*     */   public Point shiftGraphics() {
/* 624 */     if (this.image != null) {
/* 625 */       Point p = new Point();
/*     */ 
/* 630 */       p.x = ((618 - (int)(getZoom() * this.originalWidth)) / 2);
/* 631 */       p.y = ((464 - (int)(getZoom() * this.originalHeight)) / 2);
/* 632 */       if (p.y < 0) {
/* 633 */         p.y = 0;
/*     */       }
/* 635 */       if (p.x < 0) {
/* 636 */         p.x = 0;
/*     */       }
/*     */ 
/* 640 */       return p;
/*     */     }
/* 642 */     return null;
/*     */   }
/*     */ 
/*     */   public void onMaskEvent(PaintExplorerMaskEvent event)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void onScissorsEvent(PaintExplorerScissorsEvent event) {
/*     */   }
/*     */ 
/*     */   public void onProgress(PaintExplorerProgressEvent event) {
/*     */   }
/*     */ 
/*     */   public void onHistoryEvent(PaintExplorerHistoryEvent event) {
/* 656 */     if ((event instanceof PaintExplorerScissorHistoryEvent)) {
/* 657 */       PaintExplorerScissorHistoryEvent scissorEvent = (PaintExplorerScissorHistoryEvent)event;
/*     */ 
/* 662 */       this.scissorsOverlay = scissorEvent.getEdges();
/*     */ 
/* 664 */       int iterations = 20;
/*     */       int delta;
/* 665 */       if (scissorEvent.wasUndone()) {
/* 666 */         delta = -this.scissorsOverlay.size() / iterations;
/* 667 */         if (delta == 0) {
/* 668 */           delta = -1;
/*     */         }
/* 670 */         this.scissorsOverlaySegments = this.scissorsOverlay.size();
/*     */       } else {
/* 672 */         delta = this.scissorsOverlay.size() / iterations;
/* 673 */         if (delta == 0) {
/* 674 */           delta = 1;
/*     */         }
/* 676 */         this.scissorsOverlaySegments = 0;
/*     */       }
/* 678 */       this.timer.schedule(new UpdateScissorOverlayTask(delta), 200L, 5L);
/* 679 */       invalidate();
/*     */     }
/*     */   }
/*     */ 
/*     */   private class UpdateScissorOverlayTask extends TimerTask
/*     */   {
/*     */     private int delta;
/*     */ 
/*     */     public UpdateScissorOverlayTask(int delta) {
/* 688 */       this.delta = delta;
/*     */     }
/*     */ 
/*     */     public void run() {
/* 692 */       //DuluxPaintView.access$012(DuluxPaintView.this, this.delta); TODO RESOLVING COMPILATIN ERROR
/* 693 */       if ((DuluxPaintView.this.scissorsOverlaySegments <= 0) || (DuluxPaintView.this.scissorsOverlaySegments > DuluxPaintView.this.scissorsOverlay.size()))
/*     */       {
/* 695 */         DuluxPaintView.this.scissorsOverlay = null;
/* 696 */         cancel();
/*     */       }
/* 698 */       DuluxPaintView.this.invalidate();
/*     */     }
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     dulux.DuluxPaintView
 * JD-Core Version:    0.6.2
 */