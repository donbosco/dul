/*     */ package dulux;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ 
/*     */ public class DuluxPalette
/*     */ {
/*     */   public String name;
/*     */   public DuluxColour[][] colours;
/*     */   public int rows;
/*     */   public int cols;
/*     */   public int centerX;
/*     */   public int centerY;
/*     */   public boolean fx;
/*     */   public boolean scheming;
/*     */   private DuluxColour[] sortedColours;
/*     */ 
/*     */   public DuluxPalette(String name, int centerX, int centerY, boolean fx)
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
/*     */   public void clear()
/*     */   {
/*  60 */     this.sortedColours = null;
/*     */   }
/*     */ 
/*     */   public DuluxColour[] getSortedColours()
/*     */   {
/*  67 */     if (this.sortedColours != null)
/*     */     {
/*  69 */       return this.sortedColours;
/*     */     }
/*     */ 
/*  73 */     this.sortedColours = new DuluxColour[this.colours.length];
/*     */ 
/*  75 */     System.arraycopy(this.colours, 0, this.sortedColours, 0, this.colours.length);
/*     */ 
/*  77 */     Arrays.sort(this.sortedColours, new Comparator()
/*     */     {
/*     */       public int compare(Object o1, Object o2)
/*     */       {
/*  81 */         if ((o1 != null) && (o2 != null))
/*     */         {
/*  83 */           return ((DuluxColour)o1).name.compareTo(((DuluxColour)o2).name);
/*     */         }
/*     */ 
/*  87 */         if (o1 == null)
/*     */         {
/*  89 */           return 1;
/*     */         }
/*  91 */         if (o2 == null)
/*     */         {
/*  93 */           return -1;
/*     */         }
/*     */ 
/*  97 */         return 0;
/*     */       }
/*     */     });
/* 105 */     return this.sortedColours;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 115 */     return this.name + " (" + this.cols + "x" + this.rows + ")" + " [" + this.centerX + "," + this.centerY + "]";
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     dulux.DuluxPalette
 * JD-Core Version:    0.6.2
 */