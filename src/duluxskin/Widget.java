/*     */ package duluxskin;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Point;
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
/*     */   protected Point shiftGraphics()
/*     */   {
/* 197 */     return null;
/*     */   }
/*     */ 
/*     */   public void paintWidget(Graphics g)
/*     */   {
/* 204 */     if (!this.visible)
/*     */     {
/* 206 */       return;
/*     */     }
/*     */ 
/* 210 */     Graphics2D g2D = (Graphics2D)g;
/*     */ 
/* 212 */     AffineTransform t = g2D.getTransform();
/*     */ 
/* 214 */     Point p = shiftGraphics();
/*     */ 
/* 216 */     if (p == null)
/* 217 */       g2D.translate(this.x, this.y);
/*     */     else {
/* 219 */       g2D.translate(this.x + p.x, this.y + p.y);
/*     */     }
/* 221 */     paint(g2D);
/*     */ 
/* 223 */     g2D.setTransform(t);
/*     */ 
/* 225 */     this.valid = true;
/*     */   }
/*     */ 
/*     */   public void invalidate()
/*     */   {
/* 235 */     if (this.layer != null)
/*     */     {
/* 237 */       this.layer.invalidate(this.x, this.y, this.width, this.height);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void invalidate(int x, int y, int width, int height)
/*     */   {
/* 247 */     this.layer.invalidate(x + this.x, y + this.y, width, height);
/*     */   }
/*     */ 
/*     */   protected boolean firstChanceMouseEvent(MouseEvent e)
/*     */   {
/* 257 */     if (!this.enabled)
/*     */     {
/* 259 */       e.consume();
/*     */     }
/*     */ 
/* 263 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean hitTest(MouseEvent e)
/*     */   {
/* 273 */     return this.rect.contains(e.getPoint());
/*     */   }
/*     */ 
/*     */   boolean mouseEvent(MouseEvent e)
/*     */   {
/* 283 */     e.translatePoint(-this.x, -this.y);
/*     */ 
/* 287 */     if (!firstChanceMouseEvent(e))
/*     */     {
/* 289 */       return false;
/*     */     }
/*     */ 
/* 293 */     if (e.isConsumed())
/*     */     {
/* 295 */       return true;
/*     */     }
/*     */     Iterator it;
/* 299 */     switch (e.getID())
/*     */     {
/*     */     case 501:
/* 305 */       for (it = this.mouseListeners.iterator(); it.hasNext(); )
/*     */       {
/* 307 */         ((MouseListener)it.next()).mousePressed(e);
/*     */       }
/* 309 */       break;
/*     */     case 502:
/* 315 */       for (it = this.mouseListeners.iterator(); it.hasNext(); )
/*     */       {
/* 317 */         ((MouseListener)it.next()).mouseReleased(e);
/*     */       }
/* 319 */       break;
/*     */     case 500:
/* 325 */       for (it = this.mouseListeners.iterator(); it.hasNext(); )
/*     */       {
/* 327 */         ((MouseListener)it.next()).mouseClicked(e);
/*     */       }
/* 329 */       break;
/*     */     case 504:
/* 335 */       for (it = this.mouseListeners.iterator(); it.hasNext(); )
/*     */       {
/* 337 */         ((MouseListener)it.next()).mouseEntered(e);
/*     */       }
/* 339 */       break;
/*     */     case 505:
/* 345 */       for (it = this.mouseListeners.iterator(); it.hasNext(); )
/*     */       {
/* 347 */         ((MouseListener)it.next()).mouseExited(e);
/*     */       }
/* 349 */       break;
/*     */     case 503:
/* 355 */       for (it = this.mouseMotionListeners.iterator(); it.hasNext(); )
/*     */       {
/* 357 */         ((MouseMotionListener)it.next()).mouseMoved(e);
/*     */       }
/* 359 */       break;
/*     */     case 506:
/* 365 */       for (it = this.mouseMotionListeners.iterator(); it.hasNext(); )
/*     */       {
/* 367 */         ((MouseMotionListener)it.next()).mouseDragged(e);
/*     */       }
/* 369 */       break;
/*     */     case 507:
/* 375 */       for (it = this.mouseMotionListeners.iterator(); it.hasNext(); )
/*     */       {
/* 377 */         ((MouseWheelListener)it.next()).mouseWheelMoved((MouseWheelEvent)e);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 385 */     return true;
/*     */   }
/*     */ 
/*     */   public void addMouseListener(MouseListener l)
/*     */   {
/* 397 */     this.mouseListeners.add(l);
/*     */   }
/*     */ 
/*     */   public void addMouseMotionListener(MouseMotionListener l)
/*     */   {
/* 407 */     this.mouseMotionListeners.add(l);
/*     */   }
/*     */ 
/*     */   public void addMouseWheelListener(MouseWheelListener l)
/*     */   {
/* 417 */     this.mouseWheelListeners.add(l);
/*     */   }
/*     */ 
/*     */   public void setCapture()
/*     */   {
/* 427 */     this.layer.setCapture(this);
/*     */   }
/*     */ 
/*     */   public void releaseCapture()
/*     */   {
/* 437 */     this.layer.releaseCapture(this);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 447 */     return "Widget [" + this.id + "]";
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     duluxskin.Widget
 * JD-Core Version:    0.6.2
 */