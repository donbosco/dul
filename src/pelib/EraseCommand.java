/*    */ package pelib;
/*    */ 
/*    */ import java.awt.Point;
/*    */ 
/*    */ public class EraseCommand extends Command
/*    */ {
/*    */   private Area affectedArea;
/*    */   private ImageByte decorated;
/*    */   int x;
/*    */   int y;
/*    */   int r;
/*    */   boolean unErase;
/*    */ 
/*    */   public EraseCommand(PaintExplorer explorer)
/*    */   {
/* 19 */     super(explorer);
/* 20 */     this.affectedArea = new Area();
/* 21 */     this.decorated = explorer.getDecorated();
/*    */   }
/*    */ 
/*    */   public void addAffectedArea(Area newArea, int x, int y, int r, boolean unErase)
/*    */   {
/* 26 */     this.x = x;
/* 27 */     this.y = y;
/* 28 */     this.r = r;
/* 29 */     this.unErase = unErase;
/*    */ 
/* 31 */     this.affectedArea.bound(newArea);
/*    */   }
/*    */ 
/*    */   public Area getAffectedArea()
/*    */   {
/* 36 */     return this.affectedArea;
/*    */   }
/*    */ 
/*    */   public void execute(Area dirty)
/*    */   {
/* 41 */     int min_x = this.affectedArea.left;
/* 42 */     int min_y = this.affectedArea.top;
/* 43 */     int max_x = this.affectedArea.right;
/* 44 */     int max_y = this.affectedArea.bottom;
/*    */ 
/* 46 */     int width = this.decorated.width;
/*    */ 
/* 48 */     byte[] decoratedData = this.decorated.data;
/*    */ 
/* 50 */     for (int iRow = min_y; iRow <= max_y; iRow++)
/* 51 */       for (int iCol = min_x; iCol <= max_x; iCol++)
/*    */       {
/* 53 */         if (Point.distance(this.x, this.y, iCol, iRow) < this.r)
/*    */         {
/* 55 */           if (!this.unErase)
/*    */           {
/* 57 */             decoratedData[(iRow * width + iCol)] = 1;
/*    */           }
/*    */           else
/* 60 */             decoratedData[(iRow * width + iCol)] = 0;
/*    */         }
/*    */       }
/* 63 */     dirty.bound(this.affectedArea);
/*    */   }
/*    */ 
/*    */   public void undo(Area dirty)
/*    */   {
/* 68 */     int min_x = this.affectedArea.left;
/* 69 */     int min_y = this.affectedArea.top;
/* 70 */     int max_x = this.affectedArea.right;
/* 71 */     int max_y = this.affectedArea.bottom;
/*    */ 
/* 73 */     int width = this.decorated.width;
/*    */ 
/* 75 */     byte[] decoratedData = this.decorated.data;
/*    */ 
/* 77 */     for (int iRow = min_y; iRow <= max_y; iRow++) {
/* 78 */       for (int iCol = min_x; iCol <= max_x; iCol++)
/*    */       {
/* 80 */         if (Point.distance(this.x, this.y, iCol, iRow) < this.r)
/*    */         {
/* 82 */           if (!this.unErase)
/* 83 */             decoratedData[(iRow * width + iCol)] = 0;
/*    */           else
/* 85 */             decoratedData[(iRow * width + iCol)] = 1;
/*    */         }
/*    */       }
/*    */     }
/* 89 */     dirty.bound(this.affectedArea);
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.EraseCommand
 * JD-Core Version:    0.6.2
 */