/*    */ package dulux;
/*    */ 
/*    */ public class DuluxPreMasked
/*    */ {
/*    */   private String id;
/*    */   private String thumbnail;
/*    */   private String address;
/*    */   private boolean isInterior;
/*    */ 
/*    */   public DuluxPreMasked(String id, String thumbnail, String address, boolean isInterior)
/*    */   {
/* 20 */     this.id = id;
/* 21 */     this.thumbnail = thumbnail;
/* 22 */     this.address = address;
/* 23 */     this.isInterior = isInterior;
/*    */   }
/*    */ 
/*    */   public String getAddress()
/*    */   {
/* 28 */     return this.address;
/*    */   }
/*    */ 
/*    */   public boolean isIsInterior() {
/* 32 */     return this.isInterior;
/*    */   }
/*    */ 
/*    */   public void setIsInterior(boolean isInterior) {
/* 36 */     this.isInterior = isInterior;
/*    */   }
/*    */ 
/*    */   public String getId() {
/* 40 */     return this.id;
/*    */   }
/*    */ 
/*    */   public String getThumbnail() {
/* 44 */     return this.thumbnail;
/*    */   }
/*    */ 
/*    */   public void setAddress(String address) {
/* 48 */     this.address = address;
/*    */   }
/*    */ 
/*    */   public void setId(String id) {
/* 52 */     this.id = id;
/*    */   }
/*    */ 
/*    */   public void setThumbnail(String thumbnail) {
/* 56 */     this.thumbnail = thumbnail;
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     dulux.DuluxPreMasked
 * JD-Core Version:    0.6.2
 */