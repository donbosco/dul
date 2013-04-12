/*    */ package pelib;
/*    */ 
/*    */ public class PaintExplorerScissorsEvent extends PaintExplorerEvent
/*    */ {
/*    */   private ScissorsPath path;
/*    */   private int type;
/*    */   public static final int TYPE_CANCELLED = 1;
/*    */   public static final int TYPE_PATH_CHANGED = 2;
/*    */ 
/*    */   public PaintExplorerScissorsEvent(PaintExplorer source, int type, ScissorsPath path)
/*    */   {
/* 20 */     super(source);
/* 21 */     this.type = type;
/* 22 */     this.path = path;
/*    */   }
/*    */ 
/*    */   public int getType()
/*    */   {
/* 27 */     return this.type;
/*    */   }
/*    */ 
/*    */   public ScissorsPath getPath()
/*    */   {
/* 32 */     return this.path;
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.PaintExplorerScissorsEvent
 * JD-Core Version:    0.6.2
 */