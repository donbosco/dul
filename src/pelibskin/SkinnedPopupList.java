/*     */ package pelibskin;
/*     */ 
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class SkinnedPopupList extends SkinnedList
/*     */ {
/*     */   private boolean popped;
/*     */ 
/*     */   public SkinnedPopupList(String id, int x, int y, int width, int height)
/*     */   {
/*  35 */     super(id, x, y, width, height);
/*     */   }
/*     */ 
/*     */   public void popup(MouseEvent e)
/*     */   {
/*  45 */     this.popped = true;
/*     */ 
/*  47 */     setCapture();
/*     */ 
/*  49 */     selectItemAt(e.getY());
/*     */   }
/*     */ 
/*     */   protected void selectItemAt(int y)
/*     */   {
/*  59 */     int index = y / this.lineHeight;
/*     */ 
/*  61 */     if ((index < 0) || (index >= this.items.size()))
/*     */     {
/*  63 */       return;
/*     */     }
/*  65 */     this.selectedItem = ((SkinnedListItem)this.items.get(index));
/*     */ 
/*  67 */     invalidate();
/*     */   }
/*     */ 
/*     */   protected boolean firstChanceMouseEvent(MouseEvent e)
/*     */   {
/*  77 */     if (this.popped)
/*     */     {
/*  81 */       switch (e.getID())
/*     */       {
/*     */       case 503:
/*     */       case 506:
/*  89 */         selectItemAt(e.getY());
/*     */ 
/*  91 */         break;
/*     */       case 502:
/*  97 */         releaseCapture();
/*     */ 
/*  99 */         notifyItemSelected();
/*     */       case 504:
/*     */       case 505:
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 107 */     return true;
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelibskin.SkinnedPopupList
 * JD-Core Version:    0.6.2
 */