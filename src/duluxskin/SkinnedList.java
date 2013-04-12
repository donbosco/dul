/*     */ package duluxskin;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Font;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Shape;
/*     */ import java.awt.TextField;
/*     */ import java.awt.event.KeyAdapter;
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.util.Iterator;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class SkinnedList extends Widget
/*     */   implements Cloneable
/*     */ {
/*     */   protected Font font;
/*     */   protected Color highlightBackground;
/*     */   protected Color highlightForeground;
/*     */   protected Color textForeground;
/*     */   protected int lineHeight;
/*     */   protected int highlightHanging;
/*     */   protected Vector items;
/*     */   protected SkinnedListItem selectedItem;
/*     */   protected Vector listListeners;
/*     */   protected int scrollOffset;
/*     */   protected boolean editable;
/*     */   protected TextField editField;
/*     */   protected SkinnedListItem editItem;
/*     */ 
/*     */   public SkinnedList(String id, int x, int y, int width, int height)
/*     */   {
/*  33 */     super(id, x, y);
/*     */ 
/*  35 */     this.width = width;
/*     */ 
/*  37 */     this.height = height;
/*     */ 
/*  39 */     this.items = new Vector();
/*     */ 
/*  41 */     this.listListeners = new Vector();
/*     */   }
/*     */ 
/*     */   public void setEditable(boolean editable)
/*     */   {
/*  47 */     this.editable = editable;
/*     */   }
/*     */ 
/*     */   public void add(String label, Object data)
/*     */   {
/*  53 */     this.items.add(new SkinnedListTextItem(label, data));
/*     */ 
/*  55 */     invalidate();
/*     */ 
/*  57 */     notifyListModified();
/*     */   }
/*     */ 
/*     */   public void add(SkinnedListItem item)
/*     */   {
/*  63 */     this.items.add(item);
/*     */ 
/*  65 */     invalidate();
/*     */ 
/*  67 */     notifyListModified();
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/*  73 */     this.selectedItem = null;
/*     */ 
/*  75 */     this.items.clear();
/*     */ 
/*  77 */     invalidate();
/*     */ 
/*  79 */     notifyListModified();
/*     */   }
/*     */ 
/*     */   public void remove(SkinnedListItem item)
/*     */   {
/*  85 */     this.items.remove(item);
/*     */ 
/*  87 */     if (item == this.selectedItem) {
/*  88 */       this.selectedItem = null;
/*     */     }
/*     */ 
/*  91 */     invalidate();
/*     */ 
/*  93 */     notifyListModified();
/*     */   }
/*     */ 
/*     */   public int getIndex(Object object) {
/*  97 */     return this.items.indexOf(object);
/*     */   }
/*     */ 
/*     */   public void remove(Object data)
/*     */   {
/* 102 */     for (Iterator it = this.items.iterator(); it.hasNext(); )
/*     */     {
/* 104 */       SkinnedListItem item = (SkinnedListItem)it.next();
/*     */ 
/* 106 */       if (data.equals(item.getData()))
/*     */       {
/* 108 */         remove(item);
/*     */ 
/* 110 */         break;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 116 */     notifyListModified();
/*     */   }
/*     */ 
/*     */   public void setSelectedItem(SkinnedListItem item)
/*     */   {
/* 122 */     this.selectedItem = item;
/*     */ 
/* 124 */     invalidate();
/*     */ 
/* 126 */     if (item != null)
/* 127 */       notifyItemHighlighted();
/*     */   }
/*     */ 
/*     */   public void setSelectedData(Object data)
/*     */   {
/* 134 */     setSelectedItem(getItem(data));
/*     */   }
/*     */ 
/*     */   public SkinnedListItem getItem(Object data)
/*     */   {
/*     */     Iterator it;
/* 140 */     if (data != null)
/*     */     {
/* 142 */       for (it = this.items.iterator(); it.hasNext(); )
/*     */       {
/* 144 */         SkinnedListItem item = (SkinnedListItem)it.next();
/*     */ 
/* 146 */         if (data.equals(item.getData())) {
/* 147 */           return item;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 154 */     return null;
/*     */   }
/*     */ 
/*     */   public void setSelectedItem(int index)
/*     */   {
/* 160 */     if ((index < 0) || (index >= this.items.size()))
/* 161 */       setSelectedItem(null);
/*     */     else
/* 163 */       setSelectedItem((SkinnedListItem)this.items.get(index));
/*     */   }
/*     */ 
/*     */   public SkinnedListItem getSelectedItem()
/*     */   {
/* 170 */     return this.selectedItem;
/*     */   }
/*     */ 
/*     */   public Object getSelectedData()
/*     */   {
/* 176 */     if (this.selectedItem == null) {
/* 177 */       return null;
/*     */     }
/*     */ 
/* 182 */     return this.selectedItem.getData();
/*     */   }
/*     */ 
/*     */   public Iterator getItems()
/*     */   {
/* 188 */     return this.items.iterator();
/*     */   }
/*     */ 
/*     */   public int getItemCount()
/*     */   {
/* 194 */     return this.items.size();
/*     */   }
/*     */ 
/*     */   public void setFont(Font f)
/*     */   {
/* 200 */     this.font = f;
/*     */   }
/*     */ 
/*     */   public void setHighlight(Color background, Color foreground)
/*     */   {
/* 206 */     this.highlightBackground = background;
/*     */ 
/* 208 */     this.highlightForeground = foreground;
/*     */   }
/*     */ 
/*     */   public Color getHighlightForeground()
/*     */   {
/* 214 */     return this.highlightForeground;
/*     */   }
/*     */ 
/*     */   public void setTextColor(Color foreground)
/*     */   {
/* 220 */     this.textForeground = foreground;
/*     */   }
/*     */ 
/*     */   public Color getTextColor()
/*     */   {
/* 226 */     return this.textForeground;
/*     */   }
/*     */ 
/*     */   public void setLineHeight(int lineHeight)
/*     */   {
/* 232 */     this.lineHeight = lineHeight;
/*     */   }
/*     */ 
/*     */   public int getLineHeight()
/*     */   {
/* 238 */     return this.lineHeight;
/*     */   }
/*     */ 
/*     */   public void setHighlightHanging(int hanging)
/*     */   {
/* 244 */     this.highlightHanging = hanging;
/*     */   }
/*     */ 
/*     */   public int getHighlightHanging()
/*     */   {
/* 250 */     return this.highlightHanging;
/*     */   }
/*     */ 
/*     */   public void setScrollOffset(int offset)
/*     */   {
/* 256 */     this.scrollOffset = offset;
/*     */ 
/* 258 */     invalidate();
/*     */   }
/*     */ 
/*     */   public int getScrollOffset()
/*     */   {
/* 264 */     return this.scrollOffset;
/*     */   }
/*     */ 
/*     */   public void paint(Graphics g)
/*     */   {
/* 270 */     Shape oldClip = g.getClip();
/*     */ 
/* 272 */     g.setClip(0, 0, this.width, this.height);
/*     */ 
/* 274 */     g.setFont(this.font);
/*     */ 
/* 278 */     int y = this.lineHeight - this.scrollOffset;
/*     */ 
/* 280 */     for (Iterator it = this.items.iterator(); it.hasNext(); )
/*     */     {
/* 282 */       SkinnedListItem item = (SkinnedListItem)it.next();
/*     */ 
/* 284 */       boolean highlighted = item == this.selectedItem;
/*     */ 
/* 286 */       if (highlighted)
/*     */       {
/* 288 */         g.setColor(this.highlightBackground);
/*     */ 
/* 290 */         g.fillRect(0, y - this.lineHeight, this.width, this.lineHeight);
/*     */       }
/*     */ 
/* 294 */       item.draw(g, this, 0, y, highlighted);
/*     */ 
/* 296 */       y += this.lineHeight;
/*     */     }
/*     */ 
/* 300 */     g.setClip(oldClip);
/*     */   }
/*     */ 
/*     */   protected boolean firstChanceMouseEvent(MouseEvent e)
/*     */   {
/* 306 */     if ((this.editField != null) && (e.getID() == 501))
/*     */     {
/* 308 */       completeEditing();
/*     */ 
/* 310 */       e.consume();
/*     */ 
/* 312 */       return true;
/*     */     }
/*     */ 
/* 318 */     if (!this.enabled)
/*     */     {
/* 320 */       e.consume();
/*     */ 
/* 322 */       return true;
/*     */     }
/*     */ 
/* 330 */     switch (e.getID())
/*     */     {
/*     */     case 501:
/* 334 */       SkinnedListItem item = getItemAt(e.getY() + this.scrollOffset);
/*     */ 
/* 336 */       if ((item != null) && (item == this.selectedItem) && (this.editable)) {
/* 337 */         beginEditing(item);
/* 338 */       } else if ((item != this.selectedItem) && (item != null))
/*     */       {
/* 340 */         setSelectedItem(item);
/*     */ 
/* 342 */         notifyItemSelected();
/*     */       }
/*     */ 
/*     */       break;
/*     */     }
/*     */ 
/* 350 */     return true;
/*     */   }
/*     */ 
/*     */   protected SkinnedListItem getItemAt(int y)
/*     */   {
/* 356 */     int index = y / this.lineHeight;
/*     */ 
/* 358 */     if ((index < 0) || (index >= this.items.size())) {
/* 359 */       return null;
/*     */     }
/*     */ 
/* 362 */     return (SkinnedListItem)this.items.get(index);
/*     */   }
/*     */ 
/*     */   public void beginEditing(SkinnedListItem item)
/*     */   {
/* 368 */     int itemY = 0;
/*     */ 
/* 370 */     for (Iterator it = this.items.iterator(); it.hasNext(); )
/*     */     {
/* 372 */       if (it.next() == item)
/*     */       {
/*     */         break;
/*     */       }
/* 376 */       itemY += this.lineHeight;
/*     */     }
/*     */ 
/* 382 */     int border = 2;
/*     */ 
/* 386 */     setCapture();
/*     */ 
/* 388 */     this.editItem = item;
/*     */ 
/* 390 */     this.editField = new TextField();
/*     */ 
/* 392 */     this.layer.getSkin().add(this.editField);
/*     */ 
/* 394 */     this.editField.setFont(this.font);
/*     */ 
/* 396 */     this.editField.setLocation(this.x, this.y + itemY - this.scrollOffset - border);
/*     */ 
/* 398 */     this.editField.setSize(this.width, this.lineHeight + border * 2);
/*     */ 
/* 400 */     this.editField.setText(item.getLabel());
/*     */ 
/* 402 */     this.editField.setSelectionStart(0);
/*     */ 
/* 404 */     this.editField.setSelectionEnd(item.getLabel().length());
/*     */ 
/* 406 */     this.editField.requestFocus();
/*     */ 
/* 408 */     this.editField.addKeyListener(new KeyAdapter()
/*     */     {
/*     */       public void keyPressed(KeyEvent e)
/*     */       {
/* 412 */         if (e.getKeyCode() == 10)
/* 413 */           SkinnedList.this.completeEditing();
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   public void completeEditing()
/*     */   {
/* 423 */     releaseCapture();
/*     */ 
/* 425 */     this.layer.getSkin().remove(this.editField);
/*     */ 
/* 427 */     notifyItemRenamed(this.editItem, this.editField.getText());
/*     */ 
/* 429 */     this.editField = null;
/*     */   }
/*     */ 
/*     */   public void addListListener(ListListener l)
/*     */   {
/* 435 */     this.listListeners.add(l);
/*     */   }
/*     */ 
/*     */   protected void notifyListModified()
/*     */   {
/* 441 */     ListEvent e = new ListEvent(this, 65538);
/*     */ 
/* 443 */     for (Iterator it = this.listListeners.iterator(); it.hasNext(); )
/* 444 */       ((ListListener)it.next()).listModified(e);
/*     */   }
/*     */ 
/*     */   protected void notifyItemSelected()
/*     */   {
/* 451 */     ListEvent e = new ListEvent(this, 65537);
/*     */ 
/* 453 */     for (Iterator it = this.listListeners.iterator(); it.hasNext(); )
/* 454 */       ((ListListener)it.next()).itemSelected(e);
/*     */   }
/*     */ 
/*     */   protected void notifyItemHighlighted()
/*     */   {
/* 461 */     ListEvent e = new ListEvent(this, 65539);
/*     */ 
/* 463 */     for (Iterator it = this.listListeners.iterator(); it.hasNext(); )
/* 464 */       ((ListListener)it.next()).itemHighlighted(e);
/*     */   }
/*     */ 
/*     */   protected void notifyItemRenamed(SkinnedListItem item, String name)
/*     */   {
/* 471 */     ListEvent e = new ListEvent(this, 65540);
/*     */ 
/* 474 */     for (Iterator it = this.listListeners.iterator(); it.hasNext(); )
/* 475 */       ((ListListener)it.next()).itemRenamed(e, item, name);
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/*     */     try
/*     */     {
/* 482 */       return super.clone(); } catch (CloneNotSupportedException e) {
/*     */     }
/* 484 */     return null;
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     duluxskin.SkinnedList
 * JD-Core Version:    0.6.2
 */