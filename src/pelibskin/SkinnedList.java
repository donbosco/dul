/*     */ package pelibskin;
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
/*  67 */     super(id, x, y);
/*     */ 
/*  69 */     this.width = width;
/*     */ 
/*  71 */     this.height = height;
/*     */ 
/*  73 */     this.items = new Vector();
/*     */ 
/*  75 */     this.listListeners = new Vector();
/*     */   }
/*     */ 
/*     */   public void setEditable(boolean editable)
/*     */   {
/*  85 */     this.editable = editable;
/*     */   }
/*     */ 
/*     */   public void add(String label, Object data)
/*     */   {
/*  95 */     this.items.add(new SkinnedListTextItem(label, data));
/*     */ 
/*  97 */     invalidate();
/*     */ 
/*  99 */     notifyListModified();
/*     */   }
/*     */ 
/*     */   public void add(SkinnedListItem item)
/*     */   {
/* 109 */     this.items.add(item);
/*     */ 
/* 111 */     invalidate();
/*     */ 
/* 113 */     notifyListModified();
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 123 */     this.selectedItem = null;
/*     */ 
/* 125 */     this.items.clear();
/*     */ 
/* 127 */     invalidate();
/*     */ 
/* 129 */     notifyListModified();
/*     */   }
/*     */ 
/*     */   public void remove(SkinnedListItem item)
/*     */   {
/* 139 */     this.items.remove(item);
/*     */ 
/* 141 */     if (item == this.selectedItem)
/*     */     {
/* 143 */       this.selectedItem = null;
/*     */     }
/* 145 */     invalidate();
/*     */ 
/* 147 */     notifyListModified();
/*     */   }
/*     */ 
/*     */   public void remove(Object data)
/*     */   {
/* 157 */     for (Iterator it = this.items.iterator(); it.hasNext(); )
/*     */     {
/* 161 */       SkinnedListItem item = (SkinnedListItem)it.next();
/*     */ 
/* 163 */       if (data.equals(item.getData()))
/*     */       {
/* 167 */         remove(item);
/*     */ 
/* 169 */         break;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 175 */     notifyListModified();
/*     */   }
/*     */ 
/*     */   public void setSelectedItem(SkinnedListItem item)
/*     */   {
/* 185 */     this.selectedItem = item;
/*     */ 
/* 187 */     invalidate();
/*     */ 
/* 189 */     if (item != null)
/*     */     {
/* 191 */       notifyItemHighlighted();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setSelectedData(Object data)
/*     */   {
/* 201 */     setSelectedItem(getItem(data));
/*     */   }
/*     */ 
/*     */   public SkinnedListItem getItem(Object data)
/*     */   {
/*     */     Iterator it;
/* 211 */     if (data != null)
/*     */     {
/* 215 */       for (it = this.items.iterator(); it.hasNext(); )
/*     */       {
/* 219 */         SkinnedListItem item = (SkinnedListItem)it.next();
/*     */ 
/* 221 */         if (data.equals(item.getData()))
/*     */         {
/* 223 */           return item;
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 229 */     return null;
/*     */   }
/*     */ 
/*     */   public void setSelectedItem(int index)
/*     */   {
/* 239 */     if ((index < 0) || (index >= this.items.size()))
/*     */     {
/* 241 */       setSelectedItem(null);
/*     */     }
/*     */     else
/*     */     {
/* 245 */       setSelectedItem((SkinnedListItem)this.items.get(index));
/*     */     }
/*     */   }
/*     */ 
/*     */   public SkinnedListItem getSelectedItem()
/*     */   {
/* 255 */     return this.selectedItem;
/*     */   }
/*     */ 
/*     */   public Object getSelectedData()
/*     */   {
/* 265 */     if (this.selectedItem == null)
/*     */     {
/* 267 */       return null;
/*     */     }
/*     */ 
/* 271 */     return this.selectedItem.getData();
/*     */   }
/*     */ 
/*     */   public Iterator getItems()
/*     */   {
/* 281 */     return this.items.iterator();
/*     */   }
/*     */ 
/*     */   public int getItemCount()
/*     */   {
/* 291 */     return this.items.size();
/*     */   }
/*     */ 
/*     */   public void setFont(Font f)
/*     */   {
/* 301 */     this.font = f;
/*     */   }
/*     */ 
/*     */   public void setHighlight(Color background, Color foreground)
/*     */   {
/* 311 */     this.highlightBackground = background;
/*     */ 
/* 313 */     this.highlightForeground = foreground;
/*     */   }
/*     */ 
/*     */   public Color getHighlightForeground()
/*     */   {
/* 323 */     return this.highlightForeground;
/*     */   }
/*     */ 
/*     */   public void setTextColor(Color foreground)
/*     */   {
/* 333 */     this.textForeground = foreground;
/*     */   }
/*     */ 
/*     */   public Color getTextColor()
/*     */   {
/* 343 */     return this.textForeground;
/*     */   }
/*     */ 
/*     */   public void setLineHeight(int lineHeight)
/*     */   {
/* 353 */     this.lineHeight = lineHeight;
/*     */   }
/*     */ 
/*     */   public int getLineHeight()
/*     */   {
/* 363 */     return this.lineHeight;
/*     */   }
/*     */ 
/*     */   public void setHighlightHanging(int hanging)
/*     */   {
/* 373 */     this.highlightHanging = hanging;
/*     */   }
/*     */ 
/*     */   public int getHighlightHanging()
/*     */   {
/* 383 */     return this.highlightHanging;
/*     */   }
/*     */ 
/*     */   public void setScrollOffset(int offset)
/*     */   {
/* 393 */     this.scrollOffset = offset;
/*     */ 
/* 395 */     invalidate();
/*     */   }
/*     */ 
/*     */   public int getScrollOffset()
/*     */   {
/* 405 */     return this.scrollOffset;
/*     */   }
/*     */ 
/*     */   public void paint(Graphics g)
/*     */   {
/* 415 */     Shape oldClip = g.getClip();
/*     */ 
/* 417 */     g.setClip(0, 0, this.width, this.height);
/*     */ 
/* 419 */     g.setFont(this.font);
/*     */ 
/* 423 */     int y = this.lineHeight - this.scrollOffset;
/*     */ 
/* 425 */     for (Iterator it = this.items.iterator(); it.hasNext(); )
/*     */     {
/* 429 */       SkinnedListItem item = (SkinnedListItem)it.next();
/*     */ 
/* 431 */       boolean highlighted = item == this.selectedItem;
/*     */ 
/* 433 */       if (highlighted)
/*     */       {
/* 437 */         g.setColor(this.highlightBackground);
/*     */ 
/* 439 */         g.fillRect(0, y - this.lineHeight, this.width, this.lineHeight);
/*     */       }
/*     */ 
/* 443 */       item.draw(g, this, 0, y, highlighted);
/*     */ 
/* 445 */       y += this.lineHeight;
/*     */     }
/*     */ 
/* 449 */     g.setClip(oldClip);
/*     */   }
/*     */ 
/*     */   protected boolean firstChanceMouseEvent(MouseEvent e)
/*     */   {
/* 459 */     if ((this.editField != null) && (e.getID() == 501))
/*     */     {
/* 463 */       completeEditing();
/*     */ 
/* 465 */       e.consume();
/*     */ 
/* 467 */       return true;
/*     */     }
/*     */ 
/* 473 */     if (!this.enabled)
/*     */     {
/* 477 */       e.consume();
/*     */ 
/* 479 */       return true;
/*     */     }
/*     */ 
/* 487 */     switch (e.getID())
/*     */     {
/*     */     case 501:
/* 493 */       SkinnedListItem item = getItemAt(e.getY() + this.scrollOffset);
/*     */ 
/* 495 */       if ((item != null) && (item == this.selectedItem) && (this.editable))
/*     */       {
/* 497 */         beginEditing(item);
/*     */       }
/* 499 */       else if ((item != this.selectedItem) && (item != null))
/*     */       {
/* 503 */         setSelectedItem(item);
/*     */ 
/* 505 */         notifyItemSelected();
/*     */       }
/*     */ 
/*     */       break;
/*     */     }
/*     */ 
/* 513 */     return true;
/*     */   }
/*     */ 
/*     */   protected SkinnedListItem getItemAt(int y)
/*     */   {
/* 523 */     int index = y / this.lineHeight;
/*     */ 
/* 525 */     if ((index < 0) || (index >= this.items.size()))
/*     */     {
/* 527 */       return null;
/*     */     }
/* 529 */     return (SkinnedListItem)this.items.get(index);
/*     */   }
/*     */ 
/*     */   public void beginEditing(SkinnedListItem item)
/*     */   {
/* 539 */     int itemY = 0;
/*     */ 
/* 541 */     for (Iterator it = this.items.iterator(); it.hasNext(); )
/*     */     {
/* 545 */       if (it.next() == item)
/*     */       {
/*     */         break;
/*     */       }
/* 549 */       itemY += this.lineHeight;
/*     */     }
/*     */ 
/* 555 */     int border = 2;
/*     */ 
/* 559 */     setCapture();
/*     */ 
/* 561 */     this.editItem = item;
/*     */ 
/* 563 */     this.editField = new TextField();
/*     */ 
/* 565 */     this.layer.getSkin().add(this.editField);
/*     */ 
/* 567 */     this.editField.setFont(this.font);
/*     */ 
/* 569 */     this.editField.setLocation(this.x, this.y + itemY - this.scrollOffset - border);
/*     */ 
/* 571 */     this.editField.setSize(this.width, this.lineHeight + border * 2);
/*     */ 
/* 573 */     this.editField.setText(item.getLabel());
/*     */ 
/* 575 */     this.editField.setSelectionStart(0);
/*     */ 
/* 577 */     this.editField.setSelectionEnd(item.getLabel().length());
/*     */ 
/* 579 */     this.editField.requestFocus();
/*     */ 
/* 581 */     this.editField.addKeyListener(new KeyAdapter()
/*     */     {
/*     */       public void keyPressed(KeyEvent e)
/*     */       {
/* 585 */         if (e.getKeyCode() == 10)
/*     */         {
/* 587 */           SkinnedList.this.completeEditing();
/*     */         }
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   public void completeEditing()
/*     */   {
/* 601 */     releaseCapture();
/*     */ 
/* 603 */     this.layer.getSkin().remove(this.editField);
/*     */ 
/* 605 */     notifyItemRenamed(this.editItem, this.editField.getText());
/*     */ 
/* 607 */     this.editField = null;
/*     */   }
/*     */ 
/*     */   public void addListListener(ListListener l)
/*     */   {
/* 617 */     this.listListeners.add(l);
/*     */   }
/*     */ 
/*     */   protected void notifyListModified()
/*     */   {
/* 627 */     ListEvent e = new ListEvent(this, 65538);
/*     */ 
/* 629 */     for (Iterator it = this.listListeners.iterator(); it.hasNext(); )
/*     */     {
/* 631 */       ((ListListener)it.next()).listModified(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void notifyItemSelected()
/*     */   {
/* 641 */     ListEvent e = new ListEvent(this, 65537);
/*     */ 
/* 643 */     for (Iterator it = this.listListeners.iterator(); it.hasNext(); )
/*     */     {
/* 645 */       ((ListListener)it.next()).itemSelected(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void notifyItemHighlighted()
/*     */   {
/* 655 */     ListEvent e = new ListEvent(this, 65539);
/*     */ 
/* 657 */     for (Iterator it = this.listListeners.iterator(); it.hasNext(); )
/*     */     {
/* 659 */       ((ListListener)it.next()).itemHighlighted(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void notifyItemRenamed(SkinnedListItem item, String name)
/*     */   {
/* 669 */     ListEvent e = new ListEvent(this, 65540);
/*     */ 
/* 671 */     for (Iterator it = this.listListeners.iterator(); it.hasNext(); )
/*     */     {
/* 673 */       ((ListListener)it.next()).itemRenamed(e, item, name);
/*     */     }
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelibskin.SkinnedList
 * JD-Core Version:    0.6.2
 */