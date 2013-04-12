/*    */ package pelib;
/*    */ 
/*    */ public class ImageDecorate extends ImageByte
/*    */ {
/*    */   public ImageDecorate(int width, int height)
/*    */   {
/* 15 */     super(width, height);
/*    */   }
/*    */ 
/*    */   public Area getDirty()
/*    */   {
/* 20 */     Area dirty = new Area();
/* 21 */     for (int iRow = 0; iRow < this.height; iRow++)
/* 22 */       for (int iCol = 0; iCol < this.width; iCol++)
/*    */       {
/* 24 */         if (this.data[(iRow * this.width + iCol)] != 0)
/* 25 */           dirty.bound(iCol, iRow);
/*    */       }
/* 27 */     return dirty;
/*    */   }
/*    */ 
/*    */   public void floodfill(int x, int y)
/*    */   {
/* 32 */     int pitch = this.width;
/*    */ 
/* 34 */     StackInteger todo = new StackInteger();
/* 35 */     todo.push(x); todo.push(y);
/*    */     int ny;
/* 70 */     for (; !todo.empty(); 
/* 70 */       todo.push(ny))
/*    */     {
/* 39 */       y = todo.pop(); x = todo.pop();
/* 40 */       this.data[(y * pitch + x)] = 2;
/*    */ 
/* 45 */       int nx = x - 1; ny = y;
/* 46 */       if ((nx >= 0) && (this.data[(ny * pitch + nx)] == 1))
/*    */       {
/* 49 */         todo.push(nx); todo.push(ny);
/*    */       }
/*    */ 
/* 52 */       nx = x + 1; ny = y;
/* 53 */       if ((nx < this.width) && (this.data[(ny * pitch + nx)] == 1))
/*    */       {
/* 56 */         todo.push(nx); todo.push(ny);
/*    */       }
/*    */ 
/* 59 */       nx = x; ny = y - 1;
/* 60 */       if ((ny >= 0) && (this.data[(ny * pitch + nx)] == 1))
/*    */       {
/* 63 */         todo.push(nx); todo.push(ny);
/*    */       }
/*    */ 
/* 66 */       nx = x; ny = y + 1;
/* 67 */       if ((ny < this.height) && (this.data[(ny * pitch + nx)] == 1))
/*    */       {
/* 70 */         todo.push(nx);
/*    */       }
/*    */     }
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.ImageDecorate
 * JD-Core Version:    0.6.2
 */