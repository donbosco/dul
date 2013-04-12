/*     */ package dulux;
/*     */ 
/*     */ public class DuluxTempPalette
/*     */ {
/*     */   public String name;
/*     */   public DuluxColour[][] colours;
/*     */   public int rows;
/*     */   public int cols;
/*     */   public int centerX;
/*     */   public int centerY;
/*     */   public boolean fx;
/*     */   public boolean scheming;
/*     */ 
/*     */   public DuluxTempPalette(String name, int centerX, int centerY, boolean fx)
/*     */   {
/*  49 */     this.name = name.toUpperCase();
/*     */ 
/*  51 */     this.centerX = centerX;
/*     */ 
/*  53 */     this.centerY = centerY;
/*     */ 
/*  55 */     this.fx = fx;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 115 */     return this.name + " (" + this.cols + "x" + this.rows + ")" + " [" + this.centerX + "," + this.centerY + "]";
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     dulux.DuluxTempPalette
 * JD-Core Version:    0.6.2
 */