/*     */ package duluxskin;
/*     */ 
/*     */ import java.awt.Adjustable;
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.event.AdjustmentEvent;
/*     */ import java.awt.event.AdjustmentListener;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.util.Iterator;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class SkinnedSlider extends Widget
/*     */   implements Adjustable
/*     */ {
/*     */   private boolean sliderTextVisible;
/*     */   private Color color;
/*     */   private String leftLabel;
/*     */   private String rightLabel;
/*     */   private int min;
/*     */   private int max;
/*     */   private int value;
/*     */   private int size;
/*     */   private boolean dragging;
/*     */   private int mouseOffset;
/*     */   private Vector adjustmentListeners;
/*     */ 
/*     */   public SkinnedSlider(String id, int x, int y, int width, int height, String leftLabel, String rightLabel, boolean sliderTextVisible)
/*     */   {
/*  58 */     super(id, x, y);
/*     */ 
/*  60 */     this.width = width;
/*     */ 
/*  62 */     this.height = height;
/*     */ 
/*  64 */     this.sliderTextVisible = sliderTextVisible;
/*     */ 
/*  66 */     this.min = 0;
/*     */ 
/*  68 */     this.max = 100;
/*     */ 
/*  70 */     this.size = 10;
/*     */ 
/*  72 */     this.value = 0;
/*     */ 
/*  74 */     this.leftLabel = leftLabel;
/*     */ 
/*  76 */     this.rightLabel = rightLabel;
/*     */ 
/*  81 */     this.adjustmentListeners = new Vector();
/*     */   }
/*     */ 
/*     */   public void setColor(Color c)
/*     */   {
/*  91 */     this.color = c;
/*     */   }
/*     */ 
/*     */   public void paint(Graphics g)
/*     */   {
/* 101 */     if ((this.max - this.min <= 0) || (!this.enabled))
/*     */     {
/* 103 */       return;
/*     */     }
/*     */ 
/* 107 */     int left = (this.value - this.min) * (this.width - this.size) / (this.max - this.min);
/*     */ 
/* 109 */     g.setColor(Color.black);
/* 110 */     if (this.sliderTextVisible)
/*     */     {
/* 114 */       g.drawString(this.leftLabel, 4, this.height);
/* 115 */       g.drawString(this.rightLabel, this.width - this.size - 10, this.height);
/*     */     }
/* 117 */     g.drawLine(0, this.height / 2, this.width - 2, this.height / 2);
/* 118 */     g.setColor(this.color);
/* 119 */     g.fillRect(left, this.height / 2 - this.height / 4, this.size, this.height / 2);
/*     */   }
/*     */ 
/*     */   public void setSliderTestVisible(boolean visible) {
/* 123 */     this.sliderTextVisible = visible;
/*     */   }
/*     */ 
/*     */   public void addAdjustmentListener(AdjustmentListener l)
/*     */   {
/* 132 */     this.adjustmentListeners.add(l);
/*     */   }
/*     */ 
/*     */   public void removeAdjustmentListener(AdjustmentListener l)
/*     */   {
/* 142 */     this.adjustmentListeners.remove(l);
/*     */   }
/*     */ 
/*     */   protected boolean firstChanceMouseEvent(MouseEvent e)
/*     */   {
/* 152 */     if (!this.enabled)
/*     */     {
/* 156 */       e.consume();
/*     */ 
/* 158 */       return true;
/*     */     }
/*     */ 
/* 164 */     if (this.max - this.min <= 0)
/*     */     {
/* 166 */       return true;
/*     */     }
/*     */ 
/* 178 */     int position = 0;
/*     */ 
/* 180 */     int thumbLeft = (this.value - this.min) * (this.width - this.size) / (this.max - this.min);
/*     */ 
/* 182 */     if ((e.getX() >= thumbLeft) && (e.getX() - thumbLeft < this.size))
/*     */     {
/* 184 */       position = 0;
/*     */     }
/* 186 */     else if (e.getX() < thumbLeft)
/*     */     {
/* 188 */       position = -1;
/*     */     }
/*     */     else
/*     */     {
/* 192 */       position = 1;
/*     */     }
/*     */ 
/* 196 */     switch (e.getID())
/*     */     {
/*     */     case 501:
/* 202 */       if (position == 0)
/*     */       {
/* 206 */         this.dragging = true;
/*     */ 
/* 208 */         this.mouseOffset = (e.getX() - thumbLeft);
/*     */ 
/* 210 */         setCapture(); } break;
/*     */     case 503:
/*     */     case 506:
/* 224 */       if (this.dragging)
/*     */       {
/* 228 */         int left = e.getX() - this.mouseOffset;
/*     */ 
/* 230 */         this.value = (left * (this.max - this.min) / (this.width - this.size) + this.min);
/*     */ 
/* 232 */         this.value = Math.max(this.min, Math.min(this.max, this.value));
/*     */ 
/* 234 */         invalidate();
/*     */ 
/* 238 */         notifyAdjustmentEvent(new AdjustmentEvent(this, 601, 5, this.value, true));
/*     */       }
/*     */ 
/* 246 */       break;
/*     */     case 502:
/* 254 */       if (this.dragging)
/*     */       {
/* 258 */         this.dragging = false;
/*     */ 
/* 260 */         releaseCapture();
/*     */ 
/* 264 */         notifyAdjustmentEvent(new AdjustmentEvent(this, 601, 5, this.value, false));
/*     */       }
/*     */ 
/*     */       break;
/*     */     case 504:
/*     */     case 505:
/*     */     }
/*     */ 
/* 278 */     return true;
/*     */   }
/*     */ 
/*     */   private void notifyAdjustmentEvent(AdjustmentEvent e)
/*     */   {
/* 288 */     for (Iterator it = this.adjustmentListeners.iterator(); it.hasNext(); )
/*     */     {
/* 290 */       ((AdjustmentListener)it.next()).adjustmentValueChanged(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getBlockIncrement()
/*     */   {
/* 306 */     return 0;
/*     */   }
/*     */ 
/*     */   public int getMaximum()
/*     */   {
/* 316 */     return this.max;
/*     */   }
/*     */ 
/*     */   public int getMinimum()
/*     */   {
/* 326 */     return this.min;
/*     */   }
/*     */ 
/*     */   public int getOrientation()
/*     */   {
/* 336 */     return 0;
/*     */   }
/*     */ 
/*     */   public int getUnitIncrement()
/*     */   {
/* 346 */     return 0;
/*     */   }
/*     */ 
/*     */   public int getValue()
/*     */   {
/* 356 */     return this.value;
/*     */   }
/*     */ 
/*     */   public int getVisibleAmount()
/*     */   {
/* 366 */     return this.size;
/*     */   }
/*     */ 
/*     */   public void setBlockIncrement(int b)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void setMaximum(int m)
/*     */   {
/* 384 */     this.max = m;
/*     */ 
/* 386 */     invalidate();
/*     */   }
/*     */ 
/*     */   public void setMinimum(int m)
/*     */   {
/* 396 */     this.min = m;
/*     */ 
/* 398 */     invalidate();
/*     */   }
/*     */ 
/*     */   public void setUnitIncrement(int i)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void setValue(int v)
/*     */   {
/* 416 */     this.value = v;
/*     */ 
/* 418 */     invalidate();
/*     */   }
/*     */ 
/*     */   public void setVisibleAmount(int s)
/*     */   {
/* 428 */     this.size = s;
/*     */ 
/* 430 */     invalidate();
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     duluxskin.SkinnedSlider
 * JD-Core Version:    0.6.2
 */