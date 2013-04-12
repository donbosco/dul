/*    */ package dulux;
/*    */ 
/*    */ import duluxskin.Widget;
/*    */ import java.awt.Color;
/*    */ import java.awt.Graphics;
/*    */ import java.awt.Shape;
/*    */ import java.awt.event.MouseAdapter;
/*    */ import java.awt.event.MouseEvent;
/*    */ import java.io.PrintStream;
/*    */ import java.util.Vector;
/*    */ import pelib.PaintExplorerHistoryEvent;
/*    */ import pelib.PaintExplorerListener;
/*    */ import pelib.PaintExplorerMaskEvent;
/*    */ import pelib.PaintExplorerProgressEvent;
/*    */ import pelib.PaintExplorerScissorsEvent;
/*    */ 
/*    */ public class DuluxMyProjectPaletteView extends Widget
/*    */   implements PaintExplorerListener
/*    */ {
/* 19 */   private final int NUMBER_OF_COLOURS = 3;
/*    */   private int chipWidth;
/*    */   private int chipHeight;
/* 22 */   private final int OFFSETX = 5;
/*    */   private Vector paletteListeners;
/*    */   private int colour1;
/*    */   private int colour2;
/*    */   private int colour3;
/* 27 */   private final int XPOSITION = 734;
/* 28 */   private final int YPOSITION = 503;
/* 29 */   private final int WIDTH = 80;
/* 30 */   private final int HEIGHT = 168;
/*    */   private int selected;
/*    */ 
/*    */   public DuluxMyProjectPaletteView(String id, int x, int y, int width, int height, int colour1, int colour2, int colour3)
/*    */   {
/* 34 */     super(id, x, y);
/* 35 */     this.paletteListeners = new Vector();
/* 36 */     this.colour1 = colour1;
/* 37 */     this.colour2 = colour2;
/* 38 */     this.colour3 = colour3;
/* 39 */     addMouseListener(new MouseAdapter()
/*    */     {
/*    */       public void mousePressed(MouseEvent e)
/*    */       {
/* 43 */         if ((e.getX() >= 734) && (e.getY() >= 503))
/*    */         {
/* 45 */           if ((e.getX() < 814) && (e.getY() < 671))
/* 46 */             DuluxMyProjectPaletteView.this.selected = 1;
/* 47 */           else if ((e.getX() < 899) && (e.getY() < 671))
/* 48 */             DuluxMyProjectPaletteView.this.selected = 2;
/* 49 */           else if ((e.getX() < 984) && (e.getY() < 671)) {
/* 50 */             DuluxMyProjectPaletteView.this.selected = 3;
/*    */           }
/* 52 */           DuluxMyProjectPaletteView.this.selectPosition(DuluxMyProjectPaletteView.this.selected);
/*    */         }
/*    */       }
/*    */     });
/*    */   }
/*    */ 
/*    */   public void selectPosition(int selected)
/*    */   {
/*    */   }
/*    */ 
/*    */   public void addListener(DuluxPaletteListener l)
/*    */   {
/* 65 */     this.paletteListeners.add(l);
/*    */   }
/*    */ 
/*    */   public void paint(Graphics g)
/*    */   {
/* 71 */     Shape oldClip = g.getClip();
/* 72 */     g.setClip(0, 0, this.width, this.height);
/* 73 */     System.out.println("ran paint in duluxmyprojectpaletteview");
/* 74 */     g.setColor(new Color(this.colour1));
/* 75 */     g.fillRect(0, 0, this.width, this.height);
/* 76 */     g.setColor(new Color(this.colour2));
/* 77 */     g.fillRect(0 + this.width + 5, this.y, this.width, this.height);
/* 78 */     g.setColor(new Color(this.colour3));
/* 79 */     g.fillRect(this.x + this.width * 2 + 20, this.y, this.width, this.height);
/* 80 */     g.setClip(oldClip);
/*    */   }
/*    */ 
/*    */   public void onHistoryEvent(PaintExplorerHistoryEvent event) {
/* 84 */     throw new UnsupportedOperationException("Not supported yet.");
/*    */   }
/*    */ 
/*    */   public void onMaskEvent(PaintExplorerMaskEvent event) {
/* 88 */     throw new UnsupportedOperationException("Not supported yet.");
/*    */   }
/*    */ 
/*    */   public void onProgress(PaintExplorerProgressEvent event) {
/* 92 */     throw new UnsupportedOperationException("Not supported yet.");
/*    */   }
/*    */ 
/*    */   public void onScissorsEvent(PaintExplorerScissorsEvent event) {
/* 96 */     throw new UnsupportedOperationException("Not supported yet.");
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     dulux.DuluxMyProjectPaletteView
 * JD-Core Version:    0.6.2
 */