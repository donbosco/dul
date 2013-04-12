/*    */ package pelib;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import java.util.Vector;
/*    */ 
/*    */ public class MergeMaskCommand extends Command
/*    */ {
/*    */   private Mask src;
/*    */   private Mask dest;
/*    */   private Vector regions;
/*    */ 
/*    */   public MergeMaskCommand(PaintExplorer explorer, Mask dest, Mask src)
/*    */   {
/* 19 */     super(explorer);
/* 20 */     this.dest = dest;
/* 21 */     this.src = src;
/*    */   }
/*    */ 
/*    */   public void execute(Area dirty)
/*    */   {
/* 26 */     if (this.regions == null) {
/* 27 */       this.regions = new Vector(this.src.getRegions());
/*    */     }
/* 29 */     this.explorer.removeMask(this.src);
/* 30 */     for (Iterator it = this.regions.iterator(); it.hasNext(); )
/*    */     {
/* 32 */       SuperRegion region = (SuperRegion)it.next();
/* 33 */       this.dest.addRegion(region);
/* 34 */       region.setMask(this.dest);
/*    */     }
/*    */ 
/* 37 */     dirty.bound(this.dest.getArea());
/*    */   }
/*    */ 
/*    */   public void undo(Area dirty)
/*    */   {
/* 42 */     dirty.bound(this.dest.getArea());
/*    */ 
/* 44 */     for (Iterator it = this.regions.iterator(); it.hasNext(); )
/*    */     {
/* 46 */       SuperRegion region = (SuperRegion)it.next();
/* 47 */       this.dest.removeRegion(region);
/* 48 */       region.setMask(this.src);
/*    */     }
/* 50 */     this.explorer.addMask(this.src);
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.MergeMaskCommand
 * JD-Core Version:    0.6.2
 */