/*    */ package pelib;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ 
/*    */ final class MaskDataSet
/*    */ {
/*    */   public MaskData[] maskData;
/*    */   public int maskCount;
/*    */ 
/*    */   public MaskDataSet(int size)
/*    */   {
/* 17 */     this.maskData = new MaskData[size];
/* 18 */     for (int i = 0; i < this.maskData.length; i++)
/* 19 */       this.maskData[i] = new MaskData();
/* 20 */     this.maskCount = 0;
/*    */   }
/*    */ 
/*    */   public final void clear()
/*    */   {
/* 25 */     this.maskCount = 0;
/*    */   }
/*    */ 
/*    */   public final void add(Mask mask)
/*    */   {int i ;
/* 32 */     for ( i = 0; i < this.maskCount; i++)
/*    */     {
/* 34 */       if (this.maskData[i].mask == mask)
/*    */       {
/* 36 */         this.maskData[i].weight += 1;
/* 37 */         break;
/*    */       }
/*    */     }
/*    */ 
/* 41 */     if (i == this.maskCount)
/*    */     {
/* 43 */       this.maskData[this.maskCount].mask = mask;
/* 44 */       this.maskData[this.maskCount].weight = 1;
/* 45 */       this.maskCount += 1;
/*    */     }
/*    */   }
/*    */ 
/*    */   public final void subtract(Mask mask)
/*    */   {
/* 52 */     boolean found = false;
/* 53 */     for (int i = 0; i < this.maskCount; i++)
/*    */     {
/* 55 */       if (this.maskData[i].mask == mask)
/*    */       {
/* 57 */         found = true;
/* 58 */         this.maskData[i].weight -= 1;
/* 59 */         if (this.maskData[i].weight != 0) {
/*    */           break;
/*    */         }
/* 62 */         for (int j = i + 1; j < this.maskCount; j++)
/*    */         {
/* 64 */           this.maskData[(j - 1)].mask = this.maskData[j].mask;
/* 65 */           this.maskData[(j - 1)].weight = this.maskData[j].weight;
/*    */         }
/* 67 */         this.maskCount -= 1; break;
/*    */       }
/*    */ 
/*    */     }
/*    */ 
/* 73 */     if (!found)
/* 74 */       System.out.println("Not found: " + mask);
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.MaskDataSet
 * JD-Core Version:    0.6.2
 */