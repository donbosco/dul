/*    */ package dulux;
/*    */ 
/*    */ public class DuluxColour
/*    */   implements Cloneable
/*    */ {
/*    */   public DuluxPalette palette;
/*    */   public int colour;
/*    */   public int displayColour;
/*    */   public String name;
/*    */   public DuluxColour[] schemes;
/*    */   public String chip;
/*    */   private int position;
/*    */   private int itemId;
/*    */ 
/*    */   public DuluxColour(int colour, int displayColour, String name, DuluxColour[] schemes, String chip, int position, int itemId)
/*    */   {
/* 23 */     this.colour = colour;
/*    */ 
/* 25 */     this.chip = chip;
/*    */ 
/* 27 */     this.position = position;
/*    */ 
/* 29 */     this.displayColour = displayColour;
/*    */ 
/* 31 */     this.name = name.toUpperCase();
/*    */ 
/* 33 */     this.schemes = schemes;
/*    */ 
/* 35 */     this.itemId = itemId;
/*    */   }
/*    */ 
/*    */   public int getId()
/*    */   {
/* 41 */     return this.itemId;
/*    */   }
/*    */   public int getPosition() {
/* 44 */     return this.position;
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 49 */     return this.name + " (" + Integer.toString(this.colour) + ") " + this.itemId + " [" + this.chip + "]" + " (" + this.itemId + ")";
/*    */   }
/*    */ 
/*    */   public Object clone()
/*    */   {
/*    */     try {
/* 55 */       return super.clone();
/*    */     } catch (CloneNotSupportedException e) {
/*    */     }
/* 58 */     return null;
/*    */   }
/*    */ 
/*    */   public boolean equals(DuluxColour dColour)
/*    */   {
/* 63 */     return (this.name.equalsIgnoreCase(dColour.name)) && (this.displayColour == dColour.displayColour);
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     dulux.DuluxColour
 * JD-Core Version:    0.6.2
 */