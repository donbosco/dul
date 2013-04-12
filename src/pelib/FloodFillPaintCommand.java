/*     */ package pelib;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class FloodFillPaintCommand extends Command
/*     */ {
/*     */   private Mask mask;
/*     */   private Map indices;
/*     */   private Set regions;
/*     */   private boolean is_decorated;
/*     */ 
/*     */   public FloodFillPaintCommand(PaintExplorer explorer, Mask mask)
/*     */   {
/*  22 */     super(explorer);
/*  23 */     this.mask = mask;
/*  24 */     this.regions = new HashSet();
/*  25 */     this.indices = new HashMap();
/*  26 */     this.is_decorated = false;
/*     */   }
/*     */ 
/*     */   public void setDecorated(boolean is_decorated)
/*     */   {
/*  31 */     this.is_decorated = is_decorated;
/*     */   }
/*     */ 
/*     */   public Mask getMask()
/*     */   {
/*  36 */     return this.mask;
/*     */   }
/*     */ 
/*     */   public void addRegion(int labelIndex, SuperRegion region)
/*     */   {
/*  41 */     if (!this.regions.contains(region))
/*     */     {
/*  43 */       Mask oldMask = region.getMask();
/*  44 */       this.regions.add(region);
/*  45 */       if (oldMask != this.mask)
/*  46 */         this.indices.put(new Integer(labelIndex), oldMask);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addRegionDecorate(int labelIndex, SuperRegion region)
/*     */   {
/*  52 */     if (!this.regions.contains(region))
/*     */     {
/*  54 */       Mask oldMask = region.getDecorateMask();
/*  55 */       this.regions.add(region);
/*  56 */       if (oldMask != this.mask)
/*  57 */         this.indices.put(new Integer(labelIndex), oldMask);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addPaintedRegion(int labelIndex, Mask oldMask)
/*     */   {
/*  63 */     SuperRegion region = this.explorer.getRegionForIndex(labelIndex);
/*  64 */     if (!this.regions.contains(region))
/*     */     {
/*  66 */       this.indices.put(new Integer(labelIndex), oldMask);
/*  67 */       this.regions.add(region);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addFloodFillRegions(FloodFillPaintCommand cmd)
/*     */   {
/*  73 */     assert (cmd.mask == this.mask);
/*  74 */     for (Iterator it = cmd.indices.entrySet().iterator(); it.hasNext(); )
/*     */     {
/*  76 */       Map.Entry entry = (Map.Entry)it.next();
/*  77 */       Integer labelIndex = (Integer)entry.getKey();
/*  78 */       Mask oldMask = (Mask)entry.getValue();
/*  79 */       SuperRegion region = this.explorer.getRegionForIndex(labelIndex.intValue());
/*     */ 
/*  81 */       if (!this.regions.contains(region))
/*     */       {
/*  83 */         this.regions.add(region);
/*  84 */         this.indices.put(labelIndex, oldMask);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void execute(Area dirty)
/*     */   {
/*  92 */     this.regions = null;
/*  93 */     for (Iterator it = this.indices.entrySet().iterator(); it.hasNext(); )
/*     */     {
/*  95 */       Map.Entry entry = (Map.Entry)it.next();
/*  96 */       Integer labelIndex = (Integer)entry.getKey();
/*  97 */       Mask oldMask = (Mask)entry.getValue();
/*  98 */       SuperRegion region = this.explorer.getRegionForIndex(labelIndex.intValue());
/*     */ 
/* 101 */       if (this.is_decorated)
/*     */       {
/* 103 */         ImageDecorate decorated = this.explorer.getDecorated();
/* 104 */         int pitch = decorated.width;
/*     */ 
/* 106 */         if (oldMask != null)
/*     */         {
/* 108 */           oldMask.removeRegionDecorate(region);
/* 109 */           if (oldMask.mustRepaintAll()) {
/* 110 */             dirty.bound(oldMask.getArea());
/*     */           }
/*     */         }
/* 113 */         for (int i = region.area.top; i <= region.area.bottom; i++) {
/* 114 */           for (int j = region.area.left; j <= region.area.right; j++)
/*     */           {
/* 116 */             if (decorated.data[(i * pitch + j)] == 1)
/* 117 */               decorated.data[(i * pitch + j)] = 2;
/*     */           }
/*     */         }
/* 120 */         region.setDecorateMask(this.mask);
/* 121 */         dirty.bound(region.getArea());
/* 122 */         if (this.mask != null)
/*     */         {
/* 124 */           this.mask.addRegionDecorate(region);
/*     */         }
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 130 */         if (oldMask != null)
/*     */         {
/* 132 */           oldMask.removeRegion(region);
/* 133 */           if (oldMask.mustRepaintAll()) {
/* 134 */             dirty.bound(oldMask.getArea());
/*     */           }
/*     */         }
/* 137 */         region.setMask(this.mask);
/* 138 */         dirty.bound(region.getArea());
/* 139 */         if (this.mask != null)
/*     */         {
/* 141 */           this.mask.addRegion(region);
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 147 */     if ((this.mask != null) && (this.mask.mustRepaintAll()))
/*     */     {
/* 149 */       dirty.bound(this.mask.getArea());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void undo(Area dirty)
/*     */   {
/* 155 */     ImageDecorate decorated = this.explorer.getDecorated();
/* 156 */     int pitch = decorated.width;
/*     */ 
/* 158 */     for (Iterator it = this.indices.entrySet().iterator(); it.hasNext(); )
/*     */     {
/* 160 */       Map.Entry entry = (Map.Entry)it.next();
/* 161 */       Integer labelIndex = (Integer)entry.getKey();
/* 162 */       Mask oldMask = (Mask)entry.getValue();
/* 163 */       SuperRegion region = this.explorer.getRegionForIndex(labelIndex.intValue());
/*     */ 
/* 166 */       if (this.mask != null)
/*     */       {
/* 168 */         this.mask.removeRegion(region);
/*     */       }
/*     */ 
/* 171 */       if (this.is_decorated)
/*     */       {
/* 174 */         region.setDecorateMask(oldMask);
/*     */ 
/* 176 */         for (int i = region.area.top; i <= region.area.bottom; i++) {
/* 177 */           for (int j = region.area.left; j <= region.area.right; j++)
/*     */           {
/* 179 */             if (decorated.data[(i * pitch + j)] == 2)
/* 180 */               decorated.data[(i * pitch + j)] = 1;
/*     */           }
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 186 */         region.setMask(oldMask);
/*     */       }
/*     */ 
/* 189 */       if (oldMask != null)
/*     */       {
/* 191 */         if (this.is_decorated)
/* 192 */           oldMask.addRegionDecorate(region);
/*     */         else
/* 194 */           oldMask.addRegion(region);
/* 195 */         if (oldMask.mustRepaintAll()) {
/* 196 */           dirty.bound(oldMask.getArea());
/*     */         }
/*     */       }
/* 199 */       dirty.bound(region.getArea());
/*     */     }
/*     */ 
/* 202 */     if ((this.mask != null) && (this.mask.mustRepaintAll()))
/* 203 */       dirty.bound(this.mask.getArea());
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.FloodFillPaintCommand
 * JD-Core Version:    0.6.2
 */