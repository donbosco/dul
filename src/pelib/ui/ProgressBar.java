/*    */ package pelib.ui;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Component;
/*    */ import java.awt.Dimension;
/*    */ import java.awt.Graphics;
/*    */ import java.awt.Image;
/*    */ 
/*    */ public class ProgressBar extends Component
/*    */ {
/*    */   private int value;
/*    */   private int range;
/*    */   private Image backImage;
/*    */   private Graphics backGraphics;
/*    */ 
/*    */   public void paint(Graphics g)
/*    */   {
/* 24 */     int width = getWidth();
/* 25 */     int height = getHeight();
/*    */ 
/* 27 */     if (this.backImage == null)
/*    */     {
/* 29 */       this.backImage = createImage(width, height);
/* 30 */       this.backGraphics = this.backImage.getGraphics();
/*    */     }
/*    */ 
/* 33 */     this.backGraphics.setColor(Color.white);
/* 34 */     this.backGraphics.fillRect(0, 0, width, height);
/* 35 */     this.backGraphics.setColor(Color.blue);
/* 36 */     if (this.range > 0)
/* 37 */       this.backGraphics.fillRect(0, 0, this.value * width / this.range, height);
/* 38 */     this.backGraphics.setColor(Color.black);
/* 39 */     this.backGraphics.drawRect(0, 0, width, height);
/* 40 */     g.drawImage(this.backImage, 0, 0, null);
/*    */   }
/*    */ 
/*    */   public void setValue(int value)
/*    */   {
/* 45 */     this.value = value;
/*    */   }
/*    */ 
/*    */   public void setRange(int range)
/*    */   {
/* 50 */     this.range = range;
/*    */   }
/*    */ 
/*    */   public Dimension getPreferredSize()
/*    */   {
/* 55 */     Dimension d = new Dimension(150, 16);
/* 56 */     return d;
/*    */   }
/*    */ 
/*    */   public void updateImmediately()
/*    */   {
/* 61 */     Graphics g = getGraphics();
/* 62 */     if (g != null)
/* 63 */       update(g);
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.ui.ProgressBar
 * JD-Core Version:    0.6.2
 */