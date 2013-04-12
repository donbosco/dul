/*     */ package duluxskin;
/*     */ 
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Shape;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.util.Iterator;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class Layer
/*     */ {
/*     */   private Vector widgets;
/*     */   private String id;
/*     */   private boolean visible;
/*     */   private Skin skin;
/*     */ 
/*     */   public Layer(String id)
/*     */   {
/*  41 */     this.id = id;
/*     */ 
/*  43 */     this.visible = true;
/*     */ 
/*  45 */     this.widgets = new Vector();
/*     */   }
/*     */ 
/*     */   void setSkin(Skin s)
/*     */   {
/*  55 */     this.skin = s;
/*     */   }
/*     */ 
/*     */   Skin getSkin()
/*     */   {
/*  65 */     return this.skin;
/*     */   }
/*     */ 
/*     */   public void setVisible(boolean v)
/*     */   {
/*  75 */     this.visible = v;
/*     */ 
/*  77 */     this.skin.invalidate();
/*     */   }
/*     */ 
/*     */   public boolean getVisible()
/*     */   {
/*  87 */     return this.visible;
/*     */   }
/*     */ 
/*     */   public String getId()
/*     */   {
/*  97 */     return this.id;
/*     */   }
/*     */ 
/*     */   public void add(Widget w)
/*     */   {
/* 107 */     this.widgets.add(w);
/*     */ 
/* 109 */     w.setLayer(this);
/*     */   }
/*     */ 
/*     */   public void paint(Graphics g)
/*     */   {
/* 120 */     if (!this.visible)
/*     */       return;
/*     */     Iterator it;
/* 123 */     if (this.visible)
/*     */     {
/* 127 */       for (it = this.widgets.iterator(); it.hasNext(); )
/*     */       {
/* 131 */         Widget w = (Widget)it.next();
/*     */ 
/* 133 */         Shape clip = g.getClip();
/*     */ 
/* 135 */         if ((clip == null) || (clip.intersects(w.x, w.y, w.width, w.height)) || ((w instanceof SkinnedLabel)))
/*     */         {
/* 141 */           w.paintWidget(g);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void invalidate(int x, int y, int width, int height)
/*     */   {
/* 153 */     this.skin.invalidate(x, y, width, height);
/*     */   }
/*     */ 
/*     */   boolean mouseEvent(MouseEvent e)
/*     */   {
/* 163 */     for (int i = this.widgets.size() - 1; i >= 0; i--)
/*     */     {
/* 167 */       Widget widget = (Widget)this.widgets.get(i);
/*     */ 
/* 169 */       if ((widget.getVisible()) && (widget.hitTest(e)))
/*     */       {
/* 173 */         this.skin.enteredWidget(e, widget);
/*     */ 
/* 175 */         return widget.mouseEvent(e);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 181 */     return false;
/*     */   }
/*     */ 
/*     */   void setCapture(Widget w)
/*     */   {
/* 191 */     this.skin.setCapture(w);
/*     */   }
/*     */ 
/*     */   void releaseCapture(Widget w)
/*     */   {
/* 201 */     this.skin.releaseCapture(w);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 211 */     return "Layer [" + this.id + "]";
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     duluxskin.Layer
 * JD-Core Version:    0.6.2
 */