/*     */ package pelib;
/*     */ 
/*     */ import java.awt.Point;
/*     */ import java.awt.Rectangle;
/*     */ 
/*     */ public final class Area
/*     */ {
/*     */   public int top;
/*     */   public int left;
/*     */   public int right;
/*     */   public int bottom;
/*     */ 
/*     */   public Area()
/*     */   {
/*  19 */     clear();
/*     */   }
/*     */ 
/*     */   public Area(Area a)
/*     */   {
/*  24 */     this.top = a.top;
/*  25 */     this.left = a.left;
/*  26 */     this.bottom = a.bottom;
/*  27 */     this.right = a.right;
/*     */   }
/*     */ 
/*     */   public Area(Point topLeft, Point bottomRight)
/*     */   {
/*  32 */     this.left = topLeft.x;
/*  33 */     this.top = topLeft.y;
/*  34 */     this.right = bottomRight.x;
/*  35 */     this.bottom = bottomRight.y;
/*     */   }
/*     */ 
/*     */   public Area(int left, int top, int right, int bottom)
/*     */   {
/*  40 */     this.left = left;
/*  41 */     this.top = top;
/*  42 */     this.right = right;
/*  43 */     this.bottom = bottom;
/*     */   }
/*     */ 
/*     */   public void bound(Point p)
/*     */   {
/*  48 */     if (p.x < this.left)
/*  49 */       this.left = p.x;
/*  50 */     if (p.y < this.top)
/*  51 */       this.top = p.y;
/*  52 */     if (p.x > this.right)
/*  53 */       this.right = p.x;
/*  54 */     if (p.y > this.bottom)
/*  55 */       this.bottom = p.y;
/*     */   }
/*     */ 
/*     */   public void bound(int x, int y)
/*     */   {
/*  60 */     if (x < this.left)
/*  61 */       this.left = x;
/*  62 */     if (y < this.top)
/*  63 */       this.top = y;
/*  64 */     if (x > this.right)
/*  65 */       this.right = x;
/*  66 */     if (y > this.bottom)
/*  67 */       this.bottom = y;
/*     */   }
/*     */ 
/*     */   public void bound(Area a)
/*     */   {
/*  72 */     if (!a.isEmpty())
/*     */     {
/*  74 */       bound(a.left, a.top);
/*  75 */       bound(a.right, a.bottom);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void crop(Area a)
/*     */   {
/*  81 */     this.top = Math.max(this.top, a.top);
/*  82 */     this.left = Math.max(this.left, a.left);
/*  83 */     this.bottom = Math.min(this.bottom, a.bottom);
/*  84 */     this.right = Math.min(this.right, a.right);
/*     */   }
/*     */ 
/*     */   public void cropForImageSize(int width, int height)
/*     */   {
/*  89 */     this.top = Math.max(this.top, 0);
/*  90 */     this.left = Math.max(this.left, 0);
/*  91 */     this.bottom = Math.min(this.bottom, height - 1);
/*  92 */     this.right = Math.min(this.right, width - 1);
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/*  98 */     this.left = 2147483647;
/*  99 */     this.top = 2147483647;
/* 100 */     this.right = -2147483648;
/* 101 */     this.bottom = -2147483648;
/*     */   }
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/* 106 */     return (this.left > this.right) || (this.top > this.bottom);
/*     */   }
/*     */ 
/*     */   public int getWidth()
/*     */   {
/* 111 */     return this.right - this.left + 1;
/*     */   }
/*     */ 
/*     */   public int getHeight()
/*     */   {
/* 116 */     return this.bottom - this.top + 1;
/*     */   }
/*     */ 
/*     */   public Rectangle getRectangle()
/*     */   {
/* 122 */     return new Rectangle(this.left, this.top, getWidth(), getHeight());
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 127 */     return "Area [left: " + this.left + ", top: " + this.top + ", right: " + this.right + ", bottom: " + this.bottom + "]";
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.Area
 * JD-Core Version:    0.6.2
 */