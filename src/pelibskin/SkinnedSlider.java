/*     */ package pelibskin;
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
/*     */   private Color color;
/*     */   private int min;
/*     */   private int max;
/*     */   private int value;
/*     */   private int size;
/*     */   private boolean dragging;
/*     */   private int mouseOffset;
/*     */   private Vector adjustmentListeners;
/*     */ 
/*     */   public SkinnedSlider(String id, int x, int y, int width, int height)
/*     */   {
/*  55 */     super(id, x, y);
/*     */ 
/*  57 */     this.width = width;
/*     */ 
/*  59 */     this.height = height;
/*     */ 
/*  63 */     this.min = 0;
/*     */ 
/*  65 */     this.max = 100;
/*     */ 
/*  67 */     this.size = 10;
/*     */ 
/*  69 */     this.value = 0;
/*     */ 
/*  73 */     this.adjustmentListeners = new Vector();
/*     */   }
/*     */ 
/*     */   public void setColor(Color c)
/*     */   {
/*  83 */     this.color = c;
/*     */   }
/*     */ 
/*     */   public void paint(Graphics g)
/*     */   {
/*  93 */     if ((this.max - this.min <= 0) || (!this.enabled))
/*     */     {
/*  95 */       return;
/*     */     }
/*     */ 
/*  99 */     int left = (this.value - this.min) * (this.width - this.size) / (this.max - this.min);
/*     */ 
/* 103 */     g.setColor(this.color);
/*     */ 
/* 105 */     g.fillRect(left, 0, this.size, this.height);
/*     */   }
/*     */ 
/*     */   public void addAdjustmentListener(AdjustmentListener l)
/*     */   {
/* 115 */     this.adjustmentListeners.add(l);
/*     */   }
/*     */ 
/*     */   public void removeAdjustmentListener(AdjustmentListener l)
/*     */   {
/* 125 */     this.adjustmentListeners.remove(l);
/*     */   }
/*     */ 
/*     */   protected boolean firstChanceMouseEvent(MouseEvent e)
/*     */   {
/* 135 */     if (!this.enabled)
/*     */     {
/* 139 */       e.consume();
/*     */ 
/* 141 */       return true;
/*     */     }
/*     */ 
/* 147 */     if (this.max - this.min <= 0)
/*     */     {
/* 149 */       return true;
/*     */     }
/*     */ 
/* 161 */     int position = 0;
/*     */ 
/* 163 */     int thumbLeft = (this.value - this.min) * (this.width - this.size) / (this.max - this.min);
/*     */ 
/* 165 */     if ((e.getX() >= thumbLeft) && (e.getX() - thumbLeft < this.size))
/*     */     {
/* 167 */       position = 0;
/*     */     }
/* 169 */     else if (e.getX() < thumbLeft)
/*     */     {
/* 171 */       position = -1;
/*     */     }
/*     */     else
/*     */     {
/* 175 */       position = 1;
/*     */     }
/*     */ 
/* 179 */     switch (e.getID())
/*     */     {
/*     */     case 501:
/* 185 */       if (position == 0)
/*     */       {
/* 189 */         this.dragging = true;
/*     */ 
/* 191 */         this.mouseOffset = (e.getX() - thumbLeft);
/*     */ 
/* 193 */         setCapture(); } break;
/*     */     case 503:
/*     */     case 506:
/* 207 */       if (this.dragging)
/*     */       {
/* 211 */         int left = e.getX() - this.mouseOffset;
/*     */ 
/* 213 */         this.value = (left * (this.max - this.min) / (this.width - this.size) + this.min);
/*     */ 
/* 215 */         this.value = Math.max(this.min, Math.min(this.max, this.value));
/*     */ 
/* 217 */         invalidate();
/*     */ 
/* 221 */         notifyAdjustmentEvent(new AdjustmentEvent(this, 601, 5, this.value, true));
/*     */       }
/*     */ 
/* 229 */       break;
/*     */     case 502:
/* 237 */       if (this.dragging)
/*     */       {
/* 241 */         this.dragging = false;
/*     */ 
/* 243 */         releaseCapture();
/*     */ 
/* 247 */         notifyAdjustmentEvent(new AdjustmentEvent(this, 601, 5, this.value, false));
/*     */       }
/*     */ 
/*     */       break;
/*     */     case 504:
/*     */     case 505:
/*     */     }
/*     */ 
/* 261 */     return true;
/*     */   }
/*     */ 
/*     */   private void notifyAdjustmentEvent(AdjustmentEvent e)
/*     */   {
/* 271 */     for (Iterator it = this.adjustmentListeners.iterator(); it.hasNext(); )
/*     */     {
/* 273 */       ((AdjustmentListener)it.next()).adjustmentValueChanged(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getBlockIncrement()
/*     */   {
/* 289 */     return 0;
/*     */   }
/*     */ 
/*     */   public int getMaximum()
/*     */   {
/* 299 */     return this.max;
/*     */   }
/*     */ 
/*     */   public int getMinimum()
/*     */   {
/* 309 */     return this.min;
/*     */   }
/*     */ 
/*     */   public int getOrientation()
/*     */   {
/* 319 */     return 0;
/*     */   }
/*     */ 
/*     */   public int getUnitIncrement()
/*     */   {
/* 329 */     return 0;
/*     */   }
/*     */ 
/*     */   public int getValue()
/*     */   {
/* 339 */     return this.value;
/*     */   }
/*     */ 
/*     */   public int getVisibleAmount()
/*     */   {
/* 349 */     return this.size;
/*     */   }
/*     */ 
/*     */   public void setBlockIncrement(int b)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void setMaximum(int m)
/*     */   {
/* 367 */     this.max = m;
/*     */ 
/* 369 */     invalidate();
/*     */   }
/*     */ 
/*     */   public void setMinimum(int m)
/*     */   {
/* 379 */     this.min = m;
/*     */ 
/* 381 */     invalidate();
/*     */   }
/*     */ 
/*     */   public void setUnitIncrement(int i)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void setValue(int v)
/*     */   {
/* 399 */     this.value = v;
/*     */ 
/* 401 */     invalidate();
/*     */   }
/*     */ 
/*     */   public void setVisibleAmount(int s)
/*     */   {
/* 411 */     this.size = s;
/*     */ 
/* 413 */     invalidate();
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelibskin.SkinnedSlider
 * JD-Core Version:    0.6.2
 */