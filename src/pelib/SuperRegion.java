/*    */ package pelib;
/*    */ 
/*    */ import java.util.HashSet;
/*    */ import java.util.Iterator;
/*    */ import java.util.Set;
/*    */ 
/*    */ public class SuperRegion extends AbstractRegion
/*    */ {
/*    */   protected Set subRegions;
/*    */ 
/*    */   public SuperRegion()
/*    */   {
/* 17 */     this.subRegions = new HashSet();
/*    */   }
/*    */ 
/*    */   public void merge(AbstractRegion region)
/*    */   {
/* 22 */     this.subRegions.add(region);
/* 23 */     this.statistics.add(region.statistics);
/* 24 */     this.area.bound(region.area);
/*    */   }
/*    */ 
/*    */   public Iterator getSubRegionsIterator()
/*    */   {
/* 29 */     return this.subRegions.iterator();
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.SuperRegion
 * JD-Core Version:    0.6.2
 */