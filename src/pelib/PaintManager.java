/*    */ package pelib;
/*    */ 
/*    */ public abstract class PaintManager
/*    */ {
/*    */   protected PaintExplorer explorer;
/*    */ 
/*    */   protected PaintManager(PaintExplorer explorer)
/*    */   {
/* 18 */     this.explorer = explorer;
/*    */   }
/*    */ 
/*    */   abstract Mask getPaintedMask(Mask paramMask);
/*    */ 
/*    */   public abstract boolean canUnpaint(Mask paramMask);
/*    */ 
/*    */   boolean autoRemoveEmptyMasks()
/*    */   {
/* 28 */     return true;
/*    */   }
/*    */ 
/*    */   public void maskRemoved(Mask mask)
/*    */   {
/*    */   }
/*    */ 
/*    */   public abstract void setSelectedColour(int paramInt);
/*    */ 
/*    */   public abstract void setSelectedMask(Mask paramMask);
/*    */ 
/*    */   public abstract void setSelectedBlur(int paramInt);
/*    */ 
/*    */   public abstract boolean canSelectMask();
/*    */ 
/*    */   public abstract boolean canSelectColour();
/*    */ 
/*    */   public abstract boolean canSelectBlur();
/*    */ 
/*    */   public abstract int getSelectedColour();
/*    */ 
/*    */   public abstract Mask getSelectedMask();
/*    */ 
/*    */   public abstract Mask createMask();
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.PaintManager
 * JD-Core Version:    0.6.2
 */