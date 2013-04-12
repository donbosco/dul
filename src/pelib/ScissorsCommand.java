/*    */ package pelib;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import java.util.Vector;
/*    */ 
/*    */ public class ScissorsCommand extends Command
/*    */ {
/*    */   private ImageFillMask fillMask;
/*    */   private ImageFillMask oldFillMask;
/*    */   private Vector cutEdges;
/*    */   private Vector uncutEdges;
/*    */ 
/*    */   public ScissorsCommand(PaintExplorer explorer, ImageFillMask fillMask, Vector cutEdges)
/*    */   {
/* 21 */     super(explorer);
/*    */ 
/* 23 */     this.fillMask = fillMask;
/* 24 */     this.cutEdges = cutEdges;
/*    */ 
/* 26 */     this.uncutEdges = new Vector();
/*    */   }
/*    */ 
/*    */   public void execute(Area dirty)
/*    */   {
/*    */     Iterator it;
/* 32 */     if (this.oldFillMask == null)
/*    */     {
/* 35 */       this.oldFillMask = this.explorer.getFillMask().clone();
/*    */ 
/* 38 */       for (it = this.cutEdges.iterator(); it.hasNext(); )
/*    */       {
/* 40 */         Edge edge = (Edge)it.next();
/* 41 */         if (!edge.isCut())
/*    */         {
/* 43 */           this.uncutEdges.add(edge);
/* 44 */           edge.cut();
/*    */         }
/*    */       }
/*    */ 
/*    */     }
/*    */     else
/*    */     {
/* 51 */       for (it = this.cutEdges.iterator(); it.hasNext(); )
/*    */       {
/* 53 */         Edge edge = (Edge)it.next();
/* 54 */         edge.cut();
/*    */       }
/*    */     }
/*    */ 
/* 58 */     this.explorer.setFillMask(this.fillMask);
/* 59 */     this.explorer.recreateHierarchy();
/*    */ 
/* 61 */     dirty.bound(this.fillMask.getArea());
/*    */ 
/* 63 */     this.explorer.notifyHistoryEvent(new PaintExplorerScissorHistoryEvent(this.explorer, false, this.cutEdges));
/*    */   }
/*    */ 
/*    */   public void undo(Area dirty)
/*    */   {
/* 69 */     for (Iterator it = this.uncutEdges.iterator(); it.hasNext(); )
/*    */     {
/* 71 */       Edge edge = (Edge)it.next();
/* 72 */       edge.uncut();
/*    */     }
/*    */ 
/* 75 */     this.explorer.setFillMask(this.oldFillMask);
/* 76 */     this.explorer.recreateHierarchy();
/*    */ 
/* 78 */     dirty.bound(this.fillMask.getArea());
/*    */ 
/* 80 */     this.explorer.notifyHistoryEvent(new PaintExplorerScissorHistoryEvent(this.explorer, true, this.cutEdges));
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.ScissorsCommand
 * JD-Core Version:    0.6.2
 */