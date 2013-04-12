/*     */ package pelib;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import pelib.lighting.LightingAlgorithm;
/*     */ 
/*     */ public class Mask
/*     */ {
/*     */   private Set regions;
/*     */   private Set regionsDecorate;
/*     */   private Area area;
/*     */   private Area areaDecorate;
/*     */   private Statistics intensity;
/*     */   private int colour;
/*     */   private int adjustedColour;
/*     */   private LightingAlgorithm lightingAlgorithm;
/*     */   private Object userData;
/*     */   private int blurLevel;
/*     */   private int itemId;
/*     */   private int positionID;
/*     */   private float oldMean;
/*     */   private float oldVariance;
/*     */   public static final float MEAN_TOLERANCE = 0.01F;
/*     */   public static final float VARIANCE_TOLERANCE = 0.01F;
/*     */   public static final boolean ENABLE_SATURATION_COMPENSATION = true;
/*     */   private int id;
/*  42 */   private static int nextId = 1;
/*     */ 
/*     */   public Mask()
/*     */   {
/*  46 */     this.regions = new HashSet();
/*  47 */     this.regionsDecorate = new HashSet();
/*  48 */     this.area = new Area();
/*  49 */     this.areaDecorate = new Area();
/*  50 */     this.intensity = new Statistics();
/*  51 */     this.colour = Colour.createRandomColour(hashCode());
/*  52 */     this.blurLevel = 0;
/*  53 */     synchronized (getClass()) {
/*  54 */       this.id = (nextId++);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setItemId(int itemId)
/*     */   {
/*  60 */     this.itemId = itemId;
/*     */   }
/*     */ 
/*     */   public void setPositionID(int positionID) {
/*  64 */     this.positionID = positionID;
/*     */   }
/*     */ 
/*     */   public int getItemId() {
/*  68 */     return this.itemId;
/*     */   }
/*     */ 
/*     */   public void setUserData(Object obj) {
/*  72 */     this.userData = obj;
/*     */   }
/*     */ 
/*     */   public Object getUserData() {
/*  76 */     return this.userData;
/*     */   }
/*     */ 
/*     */   public int getColour() {
/*  80 */     return this.colour;
/*     */   }
/*     */ 
/*     */   void setLightingAlgorithm(LightingAlgorithm algorithm) {
/*  84 */     this.lightingAlgorithm = algorithm;
/*  85 */     setColour(this.colour);
/*     */   }
/*     */ 
/*     */   public void setColour(int colour) {
/*  89 */     this.colour = colour;
/*  90 */     if (this.lightingAlgorithm != null)
/*  91 */       this.adjustedColour = this.lightingAlgorithm.getAdjustedColour(colour);
/*     */     else
/*  93 */       this.adjustedColour = colour;
/*     */   }
/*     */ 
/*     */   public Area getArea()
/*     */   {
/*  98 */     return this.area;
/*     */   }
/*     */ 
/*     */   public Area getAreaDecorate() {
/* 102 */     return this.areaDecorate;
/*     */   }
/*     */ 
/*     */   public void setBlurLevel(int level) {
/* 106 */     this.blurLevel = level;
/*     */   }
/*     */ 
/*     */   public int getBlurLevel() {
/* 110 */     return this.blurLevel;
/*     */   }
/*     */ 
/*     */   public int getId() {
/* 114 */     return this.id;
/*     */   }
/*     */ 
/*     */   public int getPositionID() {
/* 118 */     return this.positionID;
/*     */   }
/*     */ 
/*     */   public boolean isEmpty() {
/* 122 */     return this.regions.size() == 0;
/*     */   }
/*     */ 
/*     */   public boolean equals(Mask anotherMask) {
/* 126 */     if (anotherMask == null) {
/* 127 */       return false;
/*     */     }
/*     */ 
/* 130 */     boolean result = this.colour == anotherMask.getColour();
/* 131 */     return result;
/*     */   }
/*     */ 
/*     */   void addRegion(AbstractRegion region) {
/* 135 */     PaintExplorer.logStatic("Mask(" + this + ").addRegion(" + region + ")");
/* 136 */     this.regions.add(region);
/* 137 */     this.area.bound(region.getArea());
/* 138 */     this.intensity.add(region.getStatistics().getIntensityStatistics());
/*     */   }
/*     */ 
/*     */   void addRegionDecorate(AbstractRegion region) {
/* 142 */     PaintExplorer.logStatic("Mask(" + this + ").addRegionDecorate(" + region + ")");
/* 143 */     this.regionsDecorate.add(region);
/* 144 */     this.areaDecorate.bound(region.getArea());
/* 145 */     this.intensity.add(region.getStatistics().getIntensityStatistics());
/*     */   }
/*     */ 
/*     */   void removeRegion(AbstractRegion region) {
/* 149 */     PaintExplorer.logStatic("Mask(" + this + ").removeRegion(" + region + ")");
/* 150 */     this.regions.remove(region);
/* 151 */     this.intensity.subtract(region.getStatistics().getIntensityStatistics());
/*     */ 
/* 155 */     Area regionArea = region.getArea();
/* 156 */     if ((this.area.left < regionArea.left) && (this.area.top < regionArea.top) && (this.area.right > regionArea.right) && (this.area.bottom > regionArea.bottom))
/*     */     {
/* 160 */       return;
/*     */     }
/*     */ 
/* 164 */     this.area.clear();
/* 165 */     for (Iterator it = this.regions.iterator(); it.hasNext(); ) {
/* 166 */       AbstractRegion r = (AbstractRegion)it.next();
/* 167 */       this.area.bound(r.getArea());
/*     */     }
/*     */   }
/*     */ 
/*     */   void removeRegionDecorate(AbstractRegion region) {
/* 172 */     PaintExplorer.logStatic("Mask(" + this + ").removeRegionDecorate(" + region + ")");
/* 173 */     this.regionsDecorate.remove(region);
/* 174 */     this.intensity.subtract(region.getStatistics().getIntensityStatistics());
/*     */ 
/* 178 */     Area regionArea = region.getArea();
/* 179 */     if ((this.areaDecorate.left < regionArea.left) && (this.areaDecorate.top < regionArea.top) && (this.areaDecorate.right > regionArea.right) && (this.areaDecorate.bottom > regionArea.bottom))
/*     */     {
/* 183 */       return;
/*     */     }
/*     */ 
/* 187 */     this.areaDecorate.clear();
/* 188 */     for (Iterator it = this.regionsDecorate.iterator(); it.hasNext(); ) {
/* 189 */       AbstractRegion r = (AbstractRegion)it.next();
/* 190 */       this.areaDecorate.bound(r.getArea());
/*     */     }
/*     */   }
/*     */ 
/*     */   void replaceRegion(AbstractRegion old, AbstractRegion replacement) {
/* 195 */     PaintExplorer.logStatic("Mask(" + this + ").replaceRegion(" + old + ", " + replacement + ")");
/*     */ 
/* 199 */     this.regions.remove(old);
/* 200 */     this.regions.add(replacement);
/*     */   }
/*     */ 
/*     */   void replaceRegionDecorate(AbstractRegion old, AbstractRegion replacement) {
/* 204 */     PaintExplorer.logStatic("Mask(" + this + ").replaceRegionDecorate(" + old + ", " + replacement + ")");
/*     */ 
/* 208 */     this.regionsDecorate.remove(old);
/* 209 */     this.regionsDecorate.add(replacement);
/*     */   }
/*     */ 
/*     */   public Set getRegions()
/*     */   {
/* 218 */     return this.regions;
/*     */   }
/*     */ 
/*     */   Set getRegionsDecorate() {
/* 222 */     return this.regionsDecorate;
/*     */   }
/*     */ 
/*     */   void clearRegions() {
/* 226 */     this.regions.clear();
/* 227 */     this.regionsDecorate.clear();
/* 228 */     this.intensity.clear();
/* 229 */     this.area = new Area();
/*     */   }
/*     */ 
/*     */   boolean mustRepaintAll()
/*     */   {
/* 238 */     if ((Math.abs(this.oldMean - this.intensity.getMean()) > 0.01F) || (Math.abs(this.oldVariance - this.intensity.getVariance()) > 0.01F))
/*     */     {
/* 240 */       this.oldMean = this.intensity.getMean();
/* 241 */       this.oldVariance = this.intensity.getVariance();
/* 242 */       return true;
/*     */     }
/* 244 */     return false;
/*     */   }
/*     */ 
/*     */   int getRecolour(int intensity)
/*     */   {
/* 252 */     float brightness = intensity / 256.0F;
/* 253 */     float mean = this.oldMean;
/* 254 */     float variance = this.oldVariance;
/*     */ 
/* 257 */     float brightSD = (float)Math.sqrt(Math.abs(variance));
/* 258 */     if (brightSD < 0.1F) {
/* 259 */       brightSD = 0.1F;
/*     */     }
/*     */ 
/* 262 */     float brightnessMin = mean - 2.5F * brightSD;
/* 263 */     float brightnessMed = mean;
/* 264 */     float brightnessMax = mean + 2.5F * brightSD;
/*     */ 
/* 268 */     float imgRelBrightnessNorm = 1.0F + (brightness - brightnessMed) / (brightnessMax - brightnessMin);
/*     */ 
/* 274 */     int r = (int)((this.adjustedColour & 0xFF) * imgRelBrightnessNorm);
/* 275 */     int g = (int)((this.adjustedColour >> 8 & 0xFF) * imgRelBrightnessNorm);
/* 276 */     int b = (int)((this.adjustedColour >> 16 & 0xFF) * imgRelBrightnessNorm);
/*     */ 
/* 281 */     int spill = 0;
/* 282 */     r = Math.max(r, 0);
/* 283 */     if (r > 255) {
/* 284 */       spill = r - 255;
/* 285 */       r = 255;
/*     */     }
/* 287 */     g = Math.max(g, 0);
/* 288 */     if (g > 255) {
/* 289 */       spill = g - 255;
/* 290 */       g = 255;
/*     */     }
/* 292 */     b = Math.max(b, 0);
/* 293 */     if (b > 255) {
/* 294 */       spill = b - 255;
/* 295 */       b = 255;
/*     */     }
/*     */ 
/* 298 */     int sat = Math.min(r, Math.min(g, b)) + 1;
/* 299 */     sat /= 50;
/* 300 */     sat = Math.max(sat, 1);
/*     */ 
/* 302 */     r += spill / sat;
/* 303 */     g += spill / sat;
/* 304 */     b += spill / sat;
/*     */ 
/* 308 */     r = Math.min(Math.max(r, 0), 255);
/* 309 */     g = Math.min(Math.max(g, 0), 255);
/* 310 */     b = Math.min(Math.max(b, 0), 255);
/*     */ 
/* 312 */     return r | g << 8 | b << 16;
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.Mask
 * JD-Core Version:    0.6.2
 */