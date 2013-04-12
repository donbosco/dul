/*     */ package pelibskin;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ public class SkinnedScrollbar extends Widget
/*     */   implements ActionListener, ListListener
/*     */ {
/*     */   private Color color;
/*     */   private int unit;
/*     */   private SkinnedList list;
/*     */   private SkinnedButton upButton;
/*     */   private SkinnedButton downButton;
/*     */   private int min;
/*     */   private int max;
/*     */   private int value;
/*     */   private int size;
/*     */   private boolean dragging;
/*     */   private int mouseOffset;
/*     */ 
/*     */   public SkinnedScrollbar(String id, int x, int y, int width, int height)
/*     */   {
/*  61 */     super(id, x, y);
/*     */ 
/*  63 */     this.width = width;
/*     */ 
/*  65 */     this.height = height;
/*     */ 
/*  69 */     this.min = 0;
/*     */ 
/*  71 */     this.max = 100;
/*     */ 
/*  73 */     this.size = 10;
/*     */ 
/*  75 */     this.value = 0;
/*     */   }
/*     */ 
/*     */   public void setEnabled(boolean enabled)
/*     */   {
/*  85 */     super.setEnabled(enabled);
/*     */ 
/*  87 */     if (this.upButton != null)
/*     */     {
/*  89 */       this.upButton.setEnabled(enabled);
/*     */     }
/*  91 */     if (this.downButton != null)
/*     */     {
/*  93 */       this.downButton.setEnabled(enabled);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setColor(Color c)
/*     */   {
/* 103 */     this.color = c;
/*     */   }
/*     */ 
/*     */   public void setUnit(int unit)
/*     */   {
/* 113 */     this.unit = unit;
/*     */   }
/*     */ 
/*     */   public void setList(SkinnedList list)
/*     */   {
/* 123 */     this.list = list;
/*     */ 
/* 125 */     list.addListListener(this);
/*     */ 
/* 127 */     updateListMetrics();
/*     */   }
/*     */ 
/*     */   public void setButtonUp(SkinnedButton button)
/*     */   {
/* 137 */     this.upButton = button;
/*     */ 
/* 139 */     button.setRepeating(true);
/*     */ 
/* 141 */     button.addActionListener(this);
/*     */   }
/*     */ 
/*     */   public void setButtonDown(SkinnedButton button)
/*     */   {
/* 151 */     this.downButton = button;
/*     */ 
/* 153 */     button.setRepeating(true);
/*     */ 
/* 155 */     button.addActionListener(this);
/*     */   }
/*     */ 
/*     */   private void updateListMetrics()
/*     */   {
/* 165 */     if (this.list == null)
/*     */     {
/* 167 */       return;
/*     */     }
/*     */ 
/* 171 */     int items = this.list.getItemCount();
/*     */ 
/* 173 */     int lineHeight = this.list.getLineHeight();
/*     */ 
/* 175 */     int totalHeight = items * lineHeight;
/*     */ 
/* 177 */     int windowHeight = this.list.getHeight();
/*     */ 
/* 181 */     this.unit = lineHeight;
/*     */ 
/* 183 */     this.min = 0;
/*     */ 
/* 185 */     this.max = (totalHeight - windowHeight);
/*     */ 
/* 187 */     if (totalHeight != 0)
/*     */     {
/* 189 */       this.size = (this.height * windowHeight / totalHeight);
/*     */     }
/* 191 */     this.value = this.list.getScrollOffset();
/*     */ 
/* 193 */     this.value = Math.max(this.min, Math.min(this.max, this.value));
/*     */ 
/* 197 */     ensureSelectedVisible();
/*     */ 
/* 201 */     if (this.max <= 0)
/*     */     {
/* 205 */       this.max = 0;
/*     */ 
/* 207 */       setEnabled(false);
/*     */     }
/*     */     else
/*     */     {
/* 215 */       setEnabled(true);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void actionPerformed(ActionEvent e)
/*     */   {
/* 227 */     if (e.getSource() == this.upButton)
/*     */     {
/* 229 */       this.value -= this.unit;
/*     */     }
/* 231 */     else if (e.getSource() == this.downButton)
/*     */     {
/* 233 */       this.value += this.unit;
/*     */     }
/* 235 */     this.value = Math.max(this.min, Math.min(this.max, this.value));
/*     */ 
/* 237 */     invalidate();
/*     */ 
/* 239 */     if (this.list != null)
/*     */     {
/* 241 */       this.list.setScrollOffset(this.value);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void listModified(ListEvent e)
/*     */   {
/* 251 */     updateListMetrics();
/*     */ 
/* 253 */     invalidate();
/*     */   }
/*     */ 
/*     */   public void ensureSelectedVisible()
/*     */   {
/* 263 */     int index = 0;
/*     */ 
/* 265 */     SkinnedListItem selectedItem = this.list.getSelectedItem();
/*     */ 
/* 267 */     if (selectedItem == null)
/*     */     {
/* 271 */       this.list.setScrollOffset(0);
/*     */ 
/* 273 */       return;
/*     */     }
/*     */ 
/* 279 */     for (Iterator it = this.list.getItems(); it.hasNext(); )
/*     */     {
/* 283 */       if ((SkinnedListItem)it.next() == selectedItem)
/*     */       {
/*     */         break;
/*     */       }
/* 287 */       index++;
/*     */     }
/*     */ 
/* 293 */     int items = this.list.getItemCount();
/*     */ 
/* 295 */     int lineHeight = this.list.getLineHeight();
/*     */ 
/* 297 */     int totalHeight = items * lineHeight;
/*     */ 
/* 299 */     int windowHeight = this.list.getHeight();
/*     */ 
/* 301 */     int topIndex = this.value / lineHeight;
/*     */ 
/* 303 */     int bottomIndex = (this.value + windowHeight) / lineHeight;
/*     */ 
/* 305 */     if (index < topIndex)
/*     */     {
/* 307 */       this.value = (index * lineHeight);
/*     */     }
/* 309 */     else if (index >= bottomIndex)
/*     */     {
/* 311 */       this.value = ((index + 1) * lineHeight - windowHeight);
/*     */     }
/*     */ 
/* 315 */     this.value = Math.max(this.min, Math.min(this.max, this.value));
/*     */ 
/* 317 */     this.list.setScrollOffset(this.value);
/*     */   }
/*     */ 
/*     */   public void itemRenamed(ListEvent e, SkinnedListItem item, String name)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void itemSelected(ListEvent e)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void itemHighlighted(ListEvent e)
/*     */   {
/* 345 */     ensureSelectedVisible();
/*     */   }
/*     */ 
/*     */   public void paint(Graphics g)
/*     */   {
/* 355 */     if (this.max - this.min <= 0)
/*     */     {
/* 357 */       return;
/*     */     }
/*     */ 
/* 361 */     int top = (this.value - this.min) * (this.height - this.size) / (this.max - this.min);
/*     */ 
/* 365 */     g.setColor(this.color);
/*     */ 
/* 367 */     g.fillRect(0, top, this.width, this.size);
/*     */   }
/*     */ 
/*     */   protected boolean firstChanceMouseEvent(MouseEvent e)
/*     */   {
/* 377 */     if (!this.enabled)
/*     */     {
/* 381 */       e.consume();
/*     */ 
/* 383 */       return true;
/*     */     }
/*     */ 
/* 389 */     if (this.max - this.min <= 0)
/*     */     {
/* 391 */       return true;
/*     */     }
/*     */ 
/* 403 */     int position = 0;
/*     */ 
/* 405 */     int thumbTop = (this.value - this.min) * (this.height - this.size) / (this.max - this.min);
/*     */ 
/* 407 */     if ((e.getY() >= thumbTop) && (e.getY() - thumbTop < this.size))
/*     */     {
/* 409 */       position = 0;
/*     */     }
/* 411 */     else if (e.getY() < thumbTop)
/*     */     {
/* 413 */       position = -1;
/*     */     }
/*     */     else
/*     */     {
/* 417 */       position = 1;
/*     */     }
/*     */ 
/* 421 */     switch (e.getID())
/*     */     {
/*     */     case 501:
/* 427 */       if (position == 0)
/*     */       {
/* 431 */         this.dragging = true;
/*     */ 
/* 433 */         this.mouseOffset = (e.getY() - thumbTop);
/*     */ 
/* 435 */         setCapture(); } break;
/*     */     case 503:
/*     */     case 506:
/* 447 */       if (this.dragging)
/*     */       {
/* 451 */         int top = e.getY() - this.mouseOffset;
/*     */ 
/* 453 */         this.value = (top * (this.max - this.min) / (this.height - this.size) + this.min);
/*     */ 
/* 455 */         this.value = Math.max(this.min, Math.min(this.max, this.value));
/*     */ 
/* 457 */         if (this.list != null)
/*     */         {
/* 459 */           this.list.setScrollOffset(this.value);
/*     */         }
/* 461 */         invalidate();
/*     */       }
/* 463 */       break;
/*     */     case 502:
/* 471 */       if (this.dragging)
/*     */       {
/* 475 */         this.dragging = false;
/*     */ 
/* 477 */         releaseCapture();
/*     */       }
/*     */ 
/*     */       break;
/*     */     case 504:
/*     */     case 505:
/*     */     }
/*     */ 
/* 485 */     return true;
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelibskin.SkinnedScrollbar
 * JD-Core Version:    0.6.2
 */