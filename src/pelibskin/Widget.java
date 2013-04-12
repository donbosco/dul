/*     */ package pelibskin;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.awt.event.MouseListener;
/*     */ import java.awt.event.MouseMotionListener;
/*     */ import java.awt.event.MouseWheelEvent;
/*     */ import java.awt.event.MouseWheelListener;
/*     */ import java.awt.geom.AffineTransform;
/*     */ import java.util.Iterator;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public abstract class Widget extends Component
/*     */ {
/*     */   protected String id;
/*     */   protected Rectangle rect;
/*     */   protected int x;
/*     */   protected int y;
/*     */   protected int width;
/*     */   protected int height;
/*     */   protected boolean enabled;
/*     */   protected boolean visible;
/*     */   protected Layer layer;
/*     */   protected boolean valid;
/*     */   private Vector mouseListeners;
/*     */   private Vector mouseMotionListeners;
/*     */   private Vector mouseWheelListeners;
/*     */ 
/*     */   protected Widget(String id, int x, int y)
/*     */   {
/*  67 */     this.id = id;
/*     */ 
/*  69 */     this.x = x;
/*     */ 
/*  71 */     this.y = y;
/*     */ 
/*  73 */     this.enabled = true;
/*     */ 
/*  75 */     this.valid = false;
/*     */ 
/*  77 */     this.mouseListeners = new Vector();
/*     */ 
/*  79 */     this.mouseMotionListeners = new Vector();
/*     */ 
/*  81 */     this.mouseWheelListeners = new Vector();
/*     */   }
/*     */ 
/*     */   public final int getX()
/*     */   {
/*  91 */     return this.x;
/*     */   }
/*     */ 
/*     */   public final int getY()
/*     */   {
/* 101 */     return this.y;
/*     */   }
/*     */ 
/*     */   public final int getHeight()
/*     */   {
/* 111 */     return this.height;
/*     */   }
/*     */ 
/*     */   public final int getWidth()
/*     */   {
/* 121 */     return this.width;
/*     */   }
/*     */ 
/*     */   public void setVisible(boolean v)
/*     */   {
/* 131 */     this.visible = v;
/*     */ 
/* 133 */     invalidate();
/*     */   }
/*     */ 
/*     */   public boolean getVisible()
/*     */   {
/* 143 */     return this.visible;
/*     */   }
/*     */ 
/*     */   public void setEnabled(boolean v)
/*     */   {
/* 153 */     this.enabled = v;
/*     */ 
/* 155 */     invalidate();
/*     */   }
/*     */ 
/*     */   public boolean getEnabled()
/*     */   {
/* 165 */     return this.enabled;
/*     */   }
/*     */ 
/*     */   public String getId()
/*     */   {
/* 175 */     return this.id;
/*     */   }
/*     */ 
/*     */   void setLayer(Layer l)
/*     */   {
/* 185 */     this.layer = l;
/*     */ 
/* 191 */     this.rect = new Rectangle(this.x, this.y, this.width, this.height);
/*     */   }
/*     */ 
/*     */   public void paintWidget(Graphics g)
/*     */   {
/* 201 */     if (!this.visible)
/*     */     {
/* 203 */       return;
/*     */     }
/*     */ 
/* 207 */     Graphics2D g2D = (Graphics2D)g;
/*     */ 
/* 209 */     AffineTransform t = g2D.getTransform();
/*     */ 
/* 211 */     g2D.translate(this.x, this.y);
/*     */ 
/* 213 */     paint(g2D);
/*     */ 
/* 215 */     g2D.setTransform(t);
/*     */ 
/* 217 */     this.valid = true;
/*     */   }
/*     */ 
/*     */   public void invalidate()
/*     */   {
/* 227 */     if (this.layer != null)
/*     */     {
/* 229 */       this.layer.invalidate(this.x, this.y, this.width, this.height);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void invalidate(int x, int y, int width, int height)
/*     */   {
/* 239 */     this.layer.invalidate(x + this.x, y + this.y, width, height);
/*     */   }
/*     */ 
/*     */   protected boolean firstChanceMouseEvent(MouseEvent e)
/*     */   {
/* 249 */     if (!this.enabled)
/*     */     {
/* 251 */       e.consume();
/*     */     }
/*     */ 
/* 255 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean hitTest(MouseEvent e)
/*     */   {
/* 265 */     return this.rect.contains(e.getPoint());
/*     */   }
/*     */ 
/*     */   boolean mouseEvent(MouseEvent e)
/*     */   {
/* 275 */     e.translatePoint(-this.x, -this.y);
/*     */ 
/* 279 */     if (!firstChanceMouseEvent(e))
/*     */     {
/* 281 */       return false;
/*     */     }
/*     */ 
/* 285 */     if (e.isConsumed())
/*     */     {
/* 287 */       return true;
/*     */     }
/*     */     Iterator it;
/* 291 */     switch (e.getID())
/*     */     {
/*     */     case 501:
/* 297 */       for (it = this.mouseListeners.iterator(); it.hasNext(); )
/*     */       {
/* 299 */         ((MouseListener)it.next()).mousePressed(e);
/*     */       }
/* 301 */       break;
/*     */     case 502:
/* 307 */       for (it = this.mouseListeners.iterator(); it.hasNext(); )
/*     */       {
/* 309 */         ((MouseListener)it.next()).mouseReleased(e);
/*     */       }
/* 311 */       break;
/*     */     case 500:
/* 317 */       for (it = this.mouseListeners.iterator(); it.hasNext(); )
/*     */       {
/* 319 */         ((MouseListener)it.next()).mouseClicked(e);
/*     */       }
/* 321 */       break;
/*     */     case 504:
/* 327 */       for (it = this.mouseListeners.iterator(); it.hasNext(); )
/*     */       {
/* 329 */         ((MouseListener)it.next()).mouseEntered(e);
/*     */       }
/* 331 */       break;
/*     */     case 505:
/* 337 */       for (it = this.mouseListeners.iterator(); it.hasNext(); )
/*     */       {
/* 339 */         ((MouseListener)it.next()).mouseExited(e);
/*     */       }
/* 341 */       break;
/*     */     case 503:
/* 347 */       for (it = this.mouseMotionListeners.iterator(); it.hasNext(); )
/*     */       {
/* 349 */         ((MouseMotionListener)it.next()).mouseMoved(e);
/*     */       }
/* 351 */       break;
/*     */     case 506:
/* 357 */       for (it = this.mouseMotionListeners.iterator(); it.hasNext(); )
/*     */       {
/* 359 */         ((MouseMotionListener)it.next()).mouseDragged(e);
/*     */       }
/* 361 */       break;
/*     */     case 507:
/* 367 */       for (it = this.mouseMotionListeners.iterator(); it.hasNext(); )
/*     */       {
/* 369 */         ((MouseWheelListener)it.next()).mouseWheelMoved((MouseWheelEvent)e);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 377 */     return true;
/*     */   }
/*     */ 
/*     */   public void addMouseListener(MouseListener l)
/*     */   {
/* 389 */     this.mouseListeners.add(l);
/*     */   }
/*     */ 
/*     */   public void addMouseMotionListener(MouseMotionListener l)
/*     */   {
/* 399 */     this.mouseMotionListeners.add(l);
/*     */   }
/*     */ 
/*     */   public void addMouseWheelListener(MouseWheelListener l)
/*     */   {
/* 409 */     this.mouseWheelListeners.add(l);
/*     */   }
/*     */ 
/*     */   public void setCapture()
/*     */   {
/* 419 */     this.layer.setCapture(this);
/*     */   }
/*     */ 
/*     */   public void releaseCapture()
/*     */   {
/* 429 */     this.layer.releaseCapture(this);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 439 */     return "Widget [" + this.id + "]";
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelibskin.Widget
 * JD-Core Version:    0.6.2
 */