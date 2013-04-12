/*     */ package dulux;
/*     */ 
/*     */ import java.awt.Point;
/*     */ import java.io.PrintStream;
/*     */ import pelib.Node;
/*     */ import pelib.PaintExplorer;
/*     */ import pelib.PaintExplorerHistoryEvent;
/*     */ import pelib.PaintExplorerListener;
/*     */ import pelib.PaintExplorerMaskEvent;
/*     */ import pelib.PaintExplorerProgressEvent;
/*     */ import pelib.PaintExplorerScissorsEvent;
/*     */ import pelib.ScissorsPath;
/*     */ import pelib.scissors.Dijkstra2Scissor;
/*     */ import pelib.scissors.ScissorAlgorithm;
/*     */ import pelib.scissors.StraightLineScissor;
/*     */ 
/*     */ public class DuluxPaintInputLayer
/*     */   implements PaintExplorerListener
/*     */ {
/*     */   private PaintExplorer explorer;
/*     */   private DuluxPaintView view;
/*     */   private ScissorsPath path;
/*     */   private ScissorAlgorithm straightLineAlgorithm;
/*     */   private ScissorAlgorithm dijkstraAlgorithm;
/*     */   private DuluxMyColour duluxMyColour;
/*     */   private ScissorAlgorithm algorithm;
/*     */   private Point oldPoint;
/*     */   private Point oldHoverPoint;
/*     */   private boolean painting;
/*     */   private int interpolateStep;
/*     */   private int radius_eraser;
/*     */   static final int DEFAULT_RADIUS_ERASER = 5;
/*     */ 
/*     */   public DuluxPaintInputLayer(PaintExplorer explorer, DuluxPaintView view, DuluxMyColour duluxMyColour)
/*     */   {
/*  43 */     this.explorer = explorer;
/*  44 */     this.view = view;
/*  45 */     this.duluxMyColour = duluxMyColour;
/*     */ 
/*  47 */     explorer.addListener(this);
/*     */ 
/*  49 */     this.straightLineAlgorithm = new StraightLineScissor();
/*  50 */     this.dijkstraAlgorithm = new Dijkstra2Scissor(explorer);
/*  51 */     this.interpolateStep = 1;
/*     */ 
/*  53 */     this.algorithm = this.dijkstraAlgorithm;
/*  54 */     this.painting = false;
/*  55 */     this.radius_eraser = 5;
/*     */   }
/*     */ 
/*     */   public void setEraserRadius(int radius)
/*     */   {
/*  60 */     this.radius_eraser = radius;
/*     */   }
/*     */ 
/*     */   public int getEraserRadius()
/*     */   {
/*  65 */     return this.radius_eraser;
/*     */   }
/*     */ 
/*     */   public void panBegin(Point p)
/*     */   {
/*  70 */     this.oldPoint = p;
/*     */   }
/*     */ 
/*     */   public void panContinue(Point p)
/*     */   {
/*  75 */     if (this.view.getImage() != null)
/*     */     {
/*  77 */       this.view.translate((int)-(p.getX() - this.oldPoint.x), (int)-(p.getY() - this.oldPoint.y));
/*     */ 
/*  79 */       this.view.invalidate();
/*     */     }
/*  81 */     this.oldPoint = p;
/*     */   }
/*     */ 
/*     */   public void zoomIn(int amount)
/*     */   {
/*  86 */     if (this.view.getImage() == null) {
/*  87 */       return;
/*     */     }
/*  89 */     this.view.zoom(amount);
/*  90 */     this.view.invalidate();
/*  91 */     this.duluxMyColour.refreshCursor();
/*     */   }
/*     */ 
/*     */   public void zoomOut(int amount)
/*     */   {
/*  96 */     zoomIn(-amount);
/*  97 */     this.duluxMyColour.refreshCursor();
/*     */   }
/*     */ 
/*     */   public void paintBegin(Point p)
/*     */   {
/* 102 */     Point imgPoint = this.view.transformViewToImage(p);
/* 103 */     if (imgPoint.x != -1)
/*     */     {
/* 105 */       if (!this.painting)
/* 106 */         this.explorer.beginPaint();
/* 107 */       paint(null, p, false);
/* 108 */       this.oldPoint = p;
/* 109 */       this.painting = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void paintContinue(Point p)
/*     */   {
/* 116 */     if (this.painting)
/* 117 */       paint(this.oldPoint, p, false);
/* 118 */     this.oldPoint = p;
/*     */   }
/*     */ 
/*     */   public void paintEnd()
/*     */   {
/* 123 */     if (this.painting) {
/* 124 */       this.explorer.endPaint();
/*     */     }
/* 126 */     scissorsReset();
/*     */ 
/* 129 */     this.painting = false;
/*     */   }
/*     */ 
/*     */   public void unpaintBegin(Point p)
/*     */   {
/* 134 */     Point imgPoint = this.view.transformViewToImage(p);
/* 135 */     if (imgPoint.x != -1)
/*     */     {
/* 137 */       if (!this.painting)
/* 138 */         this.explorer.beginPaint();
/* 139 */       paint(null, p, true);
/* 140 */       this.oldPoint = p;
/* 141 */       this.painting = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void unpaintContinue(Point p)
/*     */   {
/* 147 */     if (this.painting)
/* 148 */       paint(this.oldPoint, p, true);
/* 149 */     this.oldPoint = p;
/*     */   }
/*     */ 
/*     */   public void unpaintEnd()
/*     */   {
/* 154 */     if (this.view.getImage() == null) {
/* 155 */       return;
/*     */     }
/* 157 */     if (this.painting)
/* 158 */       this.explorer.endPaint();
/* 159 */     this.painting = false;
/*     */   }
/*     */ 
/*     */   private void paint(Point from, Point to, boolean unpaint)
/*     */   {
/* 165 */     if (this.view.getImage() == null) {
/* 166 */       return;
/*     */     }
/* 168 */     if (from != null)
/*     */     {
/* 172 */       int dx = to.x - from.x;
/* 173 */       int dy = to.y - from.y;
/*     */ 
/* 177 */       if (Math.abs(dx) > Math.abs(dy))
/*     */       {
/* 180 */         if (dx < 0)
/*     */         {
/* 182 */           Point tmp = from;
/* 183 */           from = to;
/* 184 */           to = tmp;
/* 185 */           dx = -dx;
/* 186 */           dy = -dy;
/*     */         }
/*     */ 
/* 189 */         float grad = this.interpolateStep * dy / dx;
/* 190 */         Point ip = new Point();
/* 191 */         float y = from.y;
/*     */ 
/* 193 */         for (ip.x = from.x; 
/* 194 */           ip.x < to.x; 
/* 195 */           ip.x += this.interpolateStep)
/*     */         {
/* 197 */           ip.y = ((int)y);
/* 198 */           Point imgPoint = this.view.transformViewToImage(ip);
/* 199 */           if (imgPoint.x != -1)
/* 200 */             this.explorer.paint(imgPoint.x, imgPoint.y, unpaint);
/* 201 */           y += grad;
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 206 */         if (dy < 0)
/*     */         {
/* 208 */           Point tmp = from;
/* 209 */           from = to;
/* 210 */           to = tmp;
/* 211 */           dx = -dx;
/* 212 */           dy = -dy;
/*     */         }
/*     */ 
/* 215 */         float grad = this.interpolateStep * dx / dy;
/* 216 */         Point ip = new Point();
/* 217 */         ip.x = from.x;
/* 218 */         float x = from.x;
/* 219 */         for (ip.y = from.y; 
/* 220 */           ip.y < to.y; 
/* 221 */           ip.y += this.interpolateStep)
/*     */         {
/* 223 */           ip.x = ((int)x);
/* 224 */           Point imgPoint = this.view.transformViewToImage(ip);
/* 225 */           if (imgPoint.x != -1)
/* 226 */             this.explorer.paint(imgPoint.x, imgPoint.y, unpaint);
/* 227 */           x += grad;
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 233 */     Point imgPoint = this.view.transformViewToImage(to);
/* 234 */     if (imgPoint.x != -1)
/*     */     {
/* 236 */       this.explorer.paint(imgPoint.x, imgPoint.y, unpaint);
/*     */     }
/*     */ 
/* 254 */     this.view.updateView();
/*     */   }
/*     */ 
/*     */   public void scissorsHover(Point p)
/*     */   {
/* 259 */     if (this.view.getImage() == null) {
/* 260 */       return;
/*     */     }
/* 262 */     Point ip = this.view.transformViewToImage(p);
/* 263 */     if (this.explorer.pointInImage(ip))
/*     */     {
/* 265 */       if (this.path != null) {
/* 266 */         this.path.hover(ip);
/*     */       }
/* 268 */       Node n = this.explorer.findNearestNode(ip);
/* 269 */       this.view.setHighlight(n.x, n.y);
/* 270 */       this.oldHoverPoint = p;
/*     */     }
/*     */     else {
/* 273 */       this.view.unsetHighlight();
/*     */     }
/* 275 */     this.view.invalidate();
/*     */   }
/*     */ 
/*     */   public boolean scissorsSeed(Point p)
/*     */   {
/* 280 */     if (this.view.getImage() == null)
/* 281 */       return false;
/* 282 */     Point ip = this.view.transformViewToImage(p);
/* 283 */     this.oldHoverPoint = p;
/* 284 */     if (this.explorer.pointInImage(ip))
/*     */     {
/* 286 */       if (this.path == null)
/*     */       {
/* 289 */         this.path = this.explorer.createScissorsPath();
/* 290 */         this.view.setScissorsPath(this.path);
/*     */       }
/*     */ 
/* 293 */       this.path.seed(ip);
/* 294 */       this.view.invalidate();
/* 295 */       return true;
/*     */     }
/*     */ 
/* 298 */     return false;
/*     */   }
/*     */ 
/*     */   public void scissorsCommit()
/*     */   {
/* 303 */     if (this.view.getImage() == null) {
/* 304 */       return;
/*     */     }
/* 306 */     if (this.path != null)
/*     */     {
/* 308 */       this.path.commit();
/*     */ 
/* 311 */       this.view.invalidate();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void removeScissorPath()
/*     */   {
/* 317 */     this.path = null;
/* 318 */     this.view.setScissorsPath(null);
/*     */   }
/*     */ 
/*     */   public void scissorsCancel()
/*     */   {
/* 323 */     if (this.view.getImage() == null) {
/* 324 */       return;
/*     */     }
/* 326 */     if (this.path != null)
/*     */     {
/* 328 */       this.path.cancel();
/* 329 */       this.path = null;
/* 330 */       this.view.setScissorsPath(null);
/*     */     }
/*     */ 
/* 333 */     this.view.unsetHighlight();
/* 334 */     this.view.invalidate();
/*     */   }
/*     */ 
/*     */   public void scissorsReset()
/*     */   {
/* 339 */     if (this.view.getImage() == null) {
/* 340 */       return;
/*     */     }
/* 342 */     if (this.path != null)
/*     */     {
/* 345 */       this.path = null;
/* 346 */       this.view.setScissorsPath(null);
/*     */     }
/*     */ 
/* 349 */     this.view.unsetHighlight();
/* 350 */     this.view.invalidate();
/*     */   }
/*     */ 
/*     */   public void resetScissors()
/*     */   {
/* 355 */     System.out.println("enter resetScissors");
/* 356 */     if (this.path != null)
/*     */     {
/* 358 */       this.path.cancel();
/* 359 */       this.path = null;
/* 360 */       this.view.setScissorsPath(null);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void eraseBegin(Point p) {
/* 365 */     Point imgPoint = this.view.transformViewToImage(p);
/* 366 */     this.explorer.beginErase();
/*     */ 
/* 368 */     erase(imgPoint.x, imgPoint.y, this.radius_eraser, false);
/*     */   }
/*     */ 
/*     */   public void eraseContinue(Point p)
/*     */   {
/* 373 */     Point imgPoint = this.view.transformViewToImage(p);
/* 374 */     erase(imgPoint.x, imgPoint.y, this.radius_eraser, false);
/*     */   }
/*     */ 
/*     */   public void eraseEnd()
/*     */   {
/* 379 */     this.explorer.endErase();
/*     */   }
/*     */ 
/*     */   private void erase(int x, int y, int radius, boolean unerase)
/*     */   {
/* 384 */     this.explorer.erase(x, y, radius, unerase);
/* 385 */     this.view.updateView();
/*     */   }
/*     */ 
/*     */   public void unEraseBegin(Point p)
/*     */   {
/* 390 */     Point imgPoint = this.view.transformViewToImage(p);
/* 391 */     if (imgPoint.x != -1)
/*     */     {
/* 393 */       if (!this.painting)
/* 394 */         this.explorer.beginErase();
/* 395 */       erase(imgPoint.x, imgPoint.y, this.radius_eraser, true);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void unEraseContinue(Point p)
/*     */   {
/* 401 */     Point imgPoint = this.view.transformViewToImage(p);
/* 402 */     erase(imgPoint.x, imgPoint.y, this.radius_eraser, true);
/*     */   }
/*     */ 
/*     */   public void unEraseEnd()
/*     */   {
/* 407 */     if (this.view.getImage() == null) {
/* 408 */       return;
/*     */     }
/* 410 */     this.explorer.endErase();
/*     */   }
/*     */ 
/*     */   public void setScissorsStraightLine(boolean straightLine)
/*     */   {
/* 415 */     if (this.view.getImage() == null) {
/* 416 */       return;
/*     */     }
/* 418 */     if (straightLine)
/* 419 */       this.algorithm = this.straightLineAlgorithm;
/*     */     else {
/* 421 */       this.algorithm = this.dijkstraAlgorithm;
/*     */     }
/* 423 */     if (this.path != null)
/*     */     {
/* 425 */       this.path.setAlgorithm(this.algorithm);
/* 426 */       Point p = this.view.transformViewToImage(this.oldHoverPoint);
/* 427 */       if (this.explorer.pointInImage(p))
/* 428 */         this.path.hover(p);
/* 429 */       this.view.invalidate();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void onMaskEvent(PaintExplorerMaskEvent event) {
/*     */   }
/*     */ 
/* 436 */   public void onScissorsEvent(PaintExplorerScissorsEvent event) { if (this.view.getImage() == null) {
/* 437 */       return;
/*     */     }
/* 439 */     switch (event.getType())
/*     */     {
/*     */     case 1:
/* 442 */       if (event.getPath() == this.path)
/*     */       {
/* 444 */         this.path = null;
/* 445 */         this.view.setScissorsPath(null);
/* 446 */         this.view.invalidate(); } break;
/*     */     case 2:
/* 451 */       if (event.getPath() == this.path)
/*     */       {
/* 453 */         Point p = this.view.transformViewToImage(this.oldHoverPoint);
/* 454 */         this.path.hover(p);
/* 455 */         this.view.invalidate();
/*     */       }
/*     */       break;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void onHistoryEvent(PaintExplorerHistoryEvent event)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void onProgress(PaintExplorerProgressEvent event)
/*     */   {
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     dulux.DuluxPaintInputLayer
 * JD-Core Version:    0.6.2
 */