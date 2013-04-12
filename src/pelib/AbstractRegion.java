/*     */ package pelib;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ 
/*     */ public abstract class AbstractRegion
/*     */ {
/*     */   protected ImageStatistics statistics;
/*     */   protected Area area;
/*     */   protected SuperRegion superRegion;
/*     */   protected Mask mask;
/*     */   private Mask decorateMask;
/*     */   protected Set neighbours;
/*     */   protected float lowestCost;
/*     */   protected AbstractRegion lowestCostNeighbour;
/*     */ 
/*     */   protected AbstractRegion()
/*     */   {
/*  24 */     this.statistics = new ImageStatistics();
/*  25 */     this.area = new Area();
/*  26 */     this.neighbours = new HashSet();
/*  27 */     this.mask = null;
/*  28 */     this.decorateMask = null;
/*  29 */     this.lowestCost = 3.4028235E+38F;
/*  30 */     this.lowestCostNeighbour = null;
/*     */   }
/*     */ 
/*     */   public ImageStatistics getStatistics()
/*     */   {
/*  35 */     return this.statistics;
/*     */   }
/*     */ 
/*     */   public void addNeighbour(AbstractRegion region)
/*     */   {
/*  40 */     this.neighbours.add(region);
/*     */ 
/*  42 */     float cost = this.statistics.computeCost(region.statistics);
/*  43 */     if (cost < this.lowestCost)
/*     */     {
/*  45 */       this.lowestCost = cost;
/*  46 */       this.lowestCostNeighbour = region;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void removeAllNeighbours()
/*     */   {
/*  52 */     this.neighbours.clear();
/*  53 */     this.lowestCostNeighbour = null;
/*  54 */     this.lowestCost = 3.4028235E+38F;
/*     */   }
/*     */ 
/*     */   public Iterator getNeighboursIterator()
/*     */   {
/*  59 */     return this.neighbours.iterator();
/*     */   }
/*     */ 
/*     */   public AbstractRegion getLowestCostNeighbour()
/*     */   {
/*  64 */     return this.lowestCostNeighbour;
/*     */   }
/*     */ 
/*     */   public float getLowestCost()
/*     */   {
/*  69 */     return this.lowestCost;
/*     */   }
/*     */ 
/*     */   public Area getArea()
/*     */   {
/*  74 */     return this.area;
/*     */   }
/*     */ 
/*     */   public void setSuperRegion(SuperRegion region)
/*     */   {
/*  79 */     this.superRegion = region;
/*     */   }
/*     */ 
/*     */   public SuperRegion getSuperRegion()
/*     */   {
/*  84 */     return this.superRegion;
/*     */   }
/*     */ 
/*     */   public SuperRegion getHighestSuperRegion()
/*     */   {
/*  89 */     SuperRegion region = this.superRegion;
/*  90 */     if (this.superRegion == null) {
/*  91 */       return null;
/*     */     }
/*  93 */     while (region.superRegion != null)
/*  94 */       region = region.superRegion;
/*  95 */     return region;
/*     */   }
/*     */ 
/*     */   public void setMask(Mask mask)
/*     */   {
/* 100 */     this.mask = mask;
/*     */   }
/*     */ 
/*     */   public void setDecorateMask(Mask mask)
/*     */   {
/* 105 */     this.decorateMask = mask;
/*     */   }
/*     */ 
/*     */   public Mask getMask()
/*     */   {
/* 110 */     return this.mask;
/*     */   }
/*     */ 
/*     */   public Mask getDecorateMask()
/*     */   {
/* 116 */     return this.decorateMask;
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.AbstractRegion
 * JD-Core Version:    0.6.2
 */