/*    */ package pelib;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import java.util.Set;
/*    */ 
/*    */ public class RemoveMaskCommand extends Command
/*    */ {
/*    */   private Mask mask;
/*    */ 
/*    */   public RemoveMaskCommand(PaintExplorer explorer, Mask mask)
/*    */   {
/* 16 */     super(explorer);
/* 17 */     assert (mask != null);
/* 18 */     this.mask = mask;
/*    */   }
/*    */ 
/*    */   public void execute(Area dirty)
/*    */   {
/* 23 */     this.explorer.removeMask(this.mask);
/* 24 */     Set regions = this.mask.getRegions();
/* 25 */     for (Iterator it = regions.iterator(); it.hasNext(); )
/*    */     {
/* 27 */       AbstractRegion region = (AbstractRegion)it.next();
/* 28 */       region.setMask(null);
/*    */     }
/*    */ 
/* 33 */     dirty.bound(this.mask.getArea());
/*    */   }
/*    */ 
/*    */   public void undo(Area dirty)
/*    */   {
/* 38 */     this.explorer.addMask(this.mask);
/* 39 */     Set regions = this.mask.getRegions();
/* 40 */     for (Iterator it = regions.iterator(); it.hasNext(); )
/*    */     {
/* 42 */       AbstractRegion region = (AbstractRegion)it.next();
/* 43 */       region.setMask(this.mask);
/*    */     }
/* 45 */     dirty.bound(this.mask.getArea());
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.RemoveMaskCommand
 * JD-Core Version:    0.6.2
 */