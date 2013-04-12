/*     */ package pelib;
/*     */ 
/*     */ import pelib.filters.GaussianFilter;
/*     */ 
/*     */ public class BlurManager
/*     */ {
/*     */   private PaintExplorer explorer;
/*     */   private ImageColour[] images;
/*     */   private int[] refCounts;
/*     */   private int totalRefs;
/*     */   private int totalImages;
/*     */ 
/*     */   public BlurManager(PaintExplorer explorer)
/*     */   {
/*  20 */     this.explorer = explorer;
/*  21 */     this.images = new ImageColour[0];
/*  22 */     this.refCounts = new int[0];
/*     */   }
/*     */ 
/*     */   public void reference(int level)
/*     */   {
/*  27 */     if (level == 0) {
/*  28 */       return;
/*     */     }
/*  30 */     if (level >= this.images.length)
/*     */     {
/*  32 */       ImageColour[] tmpImages = new ImageColour[level + 10];
/*  33 */       System.arraycopy(this.images, 0, tmpImages, 0, this.images.length);
/*  34 */       this.images = tmpImages;
/*  35 */       int[] tmpRefCounts = new int[level + 10];
/*  36 */       System.arraycopy(this.refCounts, 0, tmpRefCounts, 0, this.refCounts.length);
/*  37 */       this.refCounts = tmpRefCounts;
/*     */     }
/*     */ 
/*  40 */     this.refCounts[level] += 1;
/*  41 */     this.totalRefs += 1;
/*     */ 
/*  44 */     if (this.totalImages > this.totalRefs + 10)
/*  45 */       freeImages();
/*     */   }
/*     */ 
/*     */   public void dereference(int level)
/*     */   {
/*  50 */     if (level == 0) {
/*  51 */       return;
/*     */     }
/*  53 */     assert (this.refCounts[level] > 0);
/*  54 */     this.refCounts[level] -= 1;
/*  55 */     this.totalRefs -= 1;
/*     */   }
/*     */ 
/*     */   public ImageColour get(int level)
/*     */   {
/*  60 */     if (level == 0) {
/*  61 */       return this.explorer.getOriginalImage();
/*     */     }
/*  63 */     assert ((this.refCounts.length > level) && (this.refCounts[level] > 0));
/*     */ 
/*  66 */     if (this.images[level] == null)
/*     */     {
/*  68 */       this.explorer.progress("PROGRESS_BLUR", level);
/*  69 */       GaussianFilter filter = new GaussianFilter();
/*  70 */       this.images[level] = ((ImageColour)this.explorer.getOriginalImage().copy());
/*     */ 
/*  72 */       ImageColour old = new ImageColour(this.images[level]);
/*  73 */       for (int i = 0; i < level; i++)
/*     */       {
/*  75 */         ImageColour tmp = old;
/*  76 */         old = this.images[level];
/*  77 */         this.images[level] = tmp;
/*  78 */         filter.filter(old, this.images[level]);
/*  79 */         this.explorer.progress(level);
/*     */       }
/*  81 */       this.totalImages += 1;
/*  82 */       this.explorer.progress();
/*     */     }
/*     */ 
/*  85 */     return this.images[level];
/*     */   }
/*     */ 
/*     */   public void freeImages()
/*     */   {
/*  94 */     for (int i = 0; i < this.refCounts.length; i++)
/*     */     {
/*  96 */       if ((this.refCounts[i] == 0) && (this.images[i] != null))
/*     */       {
/*  98 */         this.totalImages -= 1;
/*  99 */         this.images[i] = null;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 106 */     for (int i = 0; i < this.refCounts.length; i++)
/*     */     {
/* 108 */       this.refCounts[i] = 0;
/* 109 */       this.images[i] = null;
/*     */     }
/* 111 */     this.totalImages = 0;
/* 112 */     this.totalRefs = 0;
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.BlurManager
 * JD-Core Version:    0.6.2
 */