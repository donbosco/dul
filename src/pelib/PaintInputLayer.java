/*     */ package pelib;
/*     */ 
/*     */ import java.awt.Point;
/*     */ import pelib.scissors.Dijkstra2Scissor;
/*     */ import pelib.scissors.ScissorAlgorithm;
/*     */ import pelib.scissors.StraightLineScissor;
/*     */ 
/*     */ public class PaintInputLayer
/*     */   implements PaintExplorerListener
/*     */ {
/*     */   private PaintExplorer explorer;
/*     */   private PaintView view;
/*     */   private ScissorsPath path;
/*     */   private ScissorAlgorithm straightLineAlgorithm;
/*     */   private ScissorAlgorithm dijkstraAlgorithm;
/*     */   private ScissorAlgorithm algorithm;
/*     */   private Point oldPoint;
/*     */   private Point oldHoverPoint;
/*     */   private boolean painting;
/*     */   private int interpolateStep;
/*     */ 
/*     */   public PaintInputLayer(PaintExplorer explorer, PaintView view)
/*     */   {
/*  32 */     this.explorer = explorer;
/*  33 */     this.view = view;
/*     */ 
/*  35 */     explorer.addListener(this);
/*     */ 
/*  37 */     this.straightLineAlgorithm = new StraightLineScissor();
/*  38 */     this.dijkstraAlgorithm = new Dijkstra2Scissor(explorer);
/*  39 */     this.interpolateStep = 1;
/*     */ 
/*  41 */     this.algorithm = this.dijkstraAlgorithm;
/*  42 */     this.painting = false;
/*     */   }
/*     */ 
/*     */   public void panBegin(Point p)
/*     */   {
/*  47 */     this.oldPoint = p;
/*     */   }
/*     */ 
/*     */   public void panContinue(Point p)
/*     */   {
/*  52 */     if (this.view.getImage() != null)
/*     */     {
/*  54 */       this.view.translate((int)-(p.getX() - this.oldPoint.x), (int)-(p.getY() - this.oldPoint.y));
/*     */ 
/*  56 */       this.view.repaint();
/*     */     }
/*  58 */     this.oldPoint = p;
/*     */   }
/*     */ 
/*     */   public void zoomIn(int amount)
/*     */   {
/*  63 */     if (this.view.getImage() == null) {
/*  64 */       return;
/*     */     }
/*  66 */     this.view.zoom(amount);
/*  67 */     this.view.repaint();
/*     */   }
/*     */ 
/*     */   public void zoomOut(int amount)
/*     */   {
/*  72 */     zoomIn(-amount);
/*     */   }
/*     */ 
/*     */   public void paintBegin(Point p)
/*     */   {
/*  77 */     if (!this.painting)
/*  78 */       this.explorer.beginPaint();
/*  79 */     paint(null, p, false);
/*  80 */     this.oldPoint = p;
/*  81 */     this.painting = true;
/*     */   }
/*     */ 
/*     */   public void paintContinue(Point p)
/*     */   {
/*  86 */     if (this.painting)
/*  87 */       paint(this.oldPoint, p, false);
/*  88 */     this.oldPoint = p;
/*     */   }
/*     */ 
/*     */   public void paintEnd()
/*     */   {
/*  93 */     if (this.painting)
/*  94 */       this.explorer.endPaint();
/*  95 */     this.painting = false;
/*     */   }
/*     */ 
/*     */   public void unpaintBegin(Point p)
/*     */   {
/* 100 */     if (!this.painting)
/* 101 */       this.explorer.beginPaint();
/* 102 */     paint(null, p, true);
/* 103 */     this.oldPoint = p;
/* 104 */     this.painting = true;
/*     */   }
/*     */ 
/*     */   public void unpaintContinue(Point p)
/*     */   {
/* 109 */     if (this.painting)
/* 110 */       paint(this.oldPoint, p, true);
/* 111 */     this.oldPoint = p;
/*     */   }
/*     */ 
/*     */   public void unpaintEnd()
/*     */   {
/* 116 */     if (this.view.getImage() == null) {
/* 117 */       return;
/*     */     }
/* 119 */     if (this.painting)
/* 120 */       this.explorer.endPaint();
/* 121 */     this.painting = false;
/*     */   }
/*     */ 
/*     */   private void paint(Point from, Point to, boolean unpaint)
/*     */   {
/* 127 */     if (this.view.getImage() == null) {
/* 128 */       return;
/*     */     }
/* 130 */     if (from != null)
/*     */     {
/* 134 */       int dx = to.x - from.x;
/* 135 */       int dy = to.y - from.y;
/*     */ 
/* 139 */       if (Math.abs(dx) > Math.abs(dy))
/*     */       {
/* 142 */         if (dx < 0)
/*     */         {
/* 144 */           Point tmp = from;
/* 145 */           from = to;
/* 146 */           to = tmp;
/* 147 */           dx = -dx;
/* 148 */           dy = -dy;
/*     */         }
/*     */ 
/* 151 */         float grad = this.interpolateStep * dy / dx;
/* 152 */         Point ip = new Point();
/* 153 */         float y = from.y;
/*     */ 
/* 155 */         for (ip.x = from.x; 
/* 156 */           ip.x < to.x; 
/* 157 */           ip.x += this.interpolateStep)
/*     */         {
/* 159 */           ip.y = ((int)y);
/* 160 */           Point imgPoint = this.view.transformViewToImage(ip);
/* 161 */           if (imgPoint.x != -1)
/* 162 */             this.explorer.paint(imgPoint.x, imgPoint.y, unpaint);
/* 163 */           y += grad;
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 168 */         if (dy < 0)
/*     */         {
/* 170 */           Point tmp = from;
/* 171 */           from = to;
/* 172 */           to = tmp;
/* 173 */           dx = -dx;
/* 174 */           dy = -dy;
/*     */         }
/*     */ 
/* 177 */         float grad = this.interpolateStep * dx / dy;
/* 178 */         Point ip = new Point();
/* 179 */         ip.x = from.x;
/* 180 */         float x = from.x;
/* 181 */         for (ip.y = from.y; 
/* 182 */           ip.y < to.y; 
/* 183 */           ip.y += this.interpolateStep)
/*     */         {
/* 185 */           ip.x = ((int)x);
/* 186 */           Point imgPoint = this.view.transformViewToImage(ip);
/* 187 */           if (imgPoint.x != -1)
/* 188 */             this.explorer.paint(imgPoint.x, imgPoint.y, unpaint);
/* 189 */           x += grad;
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 195 */     Point imgPoint = this.view.transformViewToImage(to);
/* 196 */     if (imgPoint.x != -1)
/* 197 */       this.explorer.paint(imgPoint.x, imgPoint.y, unpaint);
/* 198 */     this.view.updateView();
/*     */   }
/*     */ 
/*     */   public void scissorsHover(Point p)
/*     */   {
/* 203 */     if (this.view.getImage() == null) {
/* 204 */       return;
/*     */     }
/* 206 */     Point ip = this.view.transformViewToImage(p);
/*     */ 
/* 208 */     if (this.explorer.pointInImage(ip))
/*     */     {
/* 210 */       if (this.path != null) {
/* 211 */         this.path.hover(ip);
/*     */       }
/* 213 */       Node n = this.explorer.findNearestNode(ip);
/* 214 */       this.view.setHighlight(n.x, n.y);
/* 215 */       this.oldHoverPoint = p;
/*     */     }
/*     */     else {
/* 218 */       this.view.unsetHighlight();
/*     */     }
/* 220 */     this.view.repaint();
/*     */   }
/*     */ 
/*     */   public boolean scissorsSeed(Point p)
/*     */   {
/* 225 */     if (this.view.getImage() == null) {
/* 226 */       return false;
/*     */     }
/* 228 */     Point ip = this.view.transformViewToImage(p);
/* 229 */     this.oldHoverPoint = p;
/*     */ 
/* 231 */     if (this.explorer.pointInImage(ip))
/*     */     {
/* 233 */       if (this.path == null)
/*     */       {
/* 235 */         this.explorer.setScissorsAlgorithm(this.algorithm);
/* 236 */         this.path = this.explorer.createScissorsPath();
/* 237 */         this.view.setScissorsPath(this.path);
/*     */       }
/*     */ 
/* 240 */       this.path.seed(ip);
/* 241 */       this.view.repaint();
/* 242 */       return true;
/*     */     }
/*     */ 
/* 245 */     return false;
/*     */   }
/*     */ 
/*     */   public void scissorsCommit()
/*     */   {
/* 250 */     if (this.view.getImage() == null) {
/* 251 */       return;
/*     */     }
/* 253 */     if (this.path != null)
/*     */     {
/* 255 */       this.path.commit();
/* 256 */       this.path = null;
/* 257 */       this.view.setScissorsPath(null);
/* 258 */       this.view.repaint();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void scissorsCancel()
/*     */   {
/* 264 */     if (this.view.getImage() == null) {
/* 265 */       return;
/*     */     }
/* 267 */     if (this.path != null)
/*     */     {
/* 269 */       this.path.cancel();
/* 270 */       this.view.setScissorsPath(null);
/*     */     }
/* 272 */     this.view.unsetHighlight();
/* 273 */     this.view.repaint();
/*     */   }
/*     */ 
/*     */   public void setScissorsStraightLine(boolean straightLine)
/*     */   {
/* 278 */     if (this.view.getImage() == null) {
/* 279 */       return;
/*     */     }
/* 281 */     if (straightLine)
/* 282 */       this.algorithm = this.straightLineAlgorithm;
/*     */     else {
/* 284 */       this.algorithm = this.dijkstraAlgorithm;
/*     */     }
/* 286 */     if (this.path != null)
/*     */     {
/* 288 */       this.path.setAlgorithm(this.algorithm);
/* 289 */       Point p = this.view.transformViewToImage(this.oldHoverPoint);
/* 290 */       if (this.explorer.pointInImage(p))
/* 291 */         this.path.hover(p);
/* 292 */       this.view.repaint();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void onMaskEvent(PaintExplorerMaskEvent event) {
/*     */   }
/*     */ 
/*     */   public void onScissorsEvent(PaintExplorerScissorsEvent event) {
/* 300 */     if (this.view.getImage() == null) {
/* 301 */       return;
/*     */     }
/* 303 */     switch (event.getType())
/*     */     {
/*     */     case 1:
/* 306 */       if (event.getPath() == this.path)
/*     */       {
/* 308 */         this.path = null;
/* 309 */         this.view.setScissorsPath(null);
/* 310 */         this.view.repaint(); } break;
/*     */     case 2:
/* 315 */       if (event.getPath() == this.path)
/*     */       {
/* 317 */         Point p = this.view.transformViewToImage(this.oldHoverPoint);
/* 318 */         this.path.hover(p);
/* 319 */         this.view.repaint();
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
 * Qualified Name:     pelib.PaintInputLayer
 * JD-Core Version:    0.6.2
 */