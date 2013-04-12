/*     */ package com.sun.jimi.core.component;
/*     */ 
/*     */ import java.awt.Button;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Event;
/*     */ import java.awt.Scrollbar;
/*     */ 
/*     */ public class JimiScrollPane extends Container
/*     */ {
/*     */   public static final int SCROLL_HORIZONTAL = 0;
/*     */   public static final int SCROLL_VERTICAL = 1;
/*     */   public static final int SCROLL_AS_NEEDED = 2;
/*     */   public static final int SCROLL_ALWAYS = 3;
/*     */   private static final int DEFAULT_POLICY = 2;
/*     */   private int policy;
/*     */   private Scrollbar horizontal;
/*     */   private Scrollbar vertical;
/*     */   private Component comp;
/*     */   private Button box;
/*     */   private int compHeight;
/*     */   private int compWidth;
/*     */   private int paintX;
/*     */   private int paintY;
/*     */ 
/*     */   public JimiScrollPane()
/*     */   {
/*  46 */     this.policy = 2;
/*     */ 
/*  48 */     setLayout(new ScrollPaneLayout());
/*     */ 
/*  50 */     this.horizontal = new Scrollbar(0);
/*     */ 
/*  52 */     this.horizontal.setLineIncrement(10);
/*     */ 
/*  54 */     this.vertical = new Scrollbar(1);
/*     */ 
/*  56 */     this.vertical.setLineIncrement(10);
/*     */ 
/*  59 */     this.box = new Button();
/*     */   }
/*     */ 
/*     */   public JimiScrollPane(int paramInt)
/*     */   {
/*  74 */     this();
/*  75 */     this.policy = paramInt;
/*     */   }
/*     */ 
/*     */   public JimiScrollPane(Component paramComponent)
/*     */   {
/* 108 */     this(paramComponent, 2);
/*     */   }
/*     */ 
/*     */   public JimiScrollPane(Component paramComponent, int paramInt)
/*     */   {
/*  91 */     this(paramInt);
/*  92 */     this.comp = paramComponent;
/*  93 */     super.add(paramComponent, 0);
/*     */   }
/*     */ 
/*     */   public Component add(Component paramComponent)
/*     */   {
/* 119 */     this.comp = paramComponent;
/* 120 */     return super.add(paramComponent, 0);
/*     */   }
/*     */ 
/*     */   public int getHBarOffset()
/*     */   {
/* 158 */     int i = Math.max(this.horizontal.size().height, this.horizontal.preferredSize().height);
/*     */ 
/* 160 */     return i;
/*     */   }
/*     */ 
/*     */   public Scrollbar getHorizontalBar()
/*     */   {
/* 174 */     return this.horizontal;
/*     */   }
/*     */ 
/*     */   public int getHorizontalPosition()
/*     */   {
/* 139 */     return this.paintX;
/*     */   }
/*     */ 
/*     */   public int getScrollPolicy()
/*     */   {
/* 193 */     return this.policy;
/*     */   }
/*     */ 
/*     */   public int getVBarOffset()
/*     */   {
/* 148 */     int i = Math.max(this.vertical.size().width, this.vertical.preferredSize().width);
/*     */ 
/* 150 */     return i;
/*     */   }
/*     */ 
/*     */   public Scrollbar getVerticalBar()
/*     */   {
/* 167 */     return this.vertical;
/*     */   }
/*     */ 
/*     */   public int getVerticalPosition()
/*     */   {
/* 132 */     return this.paintY;
/*     */   }
/*     */ 
/*     */   protected Dimension getViewPort()
/*     */   {
/* 285 */     int i = 0;
/* 286 */     int j = 0;
/*     */ 
/* 288 */     if (this.policy == 1)
/*     */     {
/* 291 */       if (this.vertical != null)
/*     */       {
/* 293 */         i = size().width;
/* 294 */         j = size().height;
/*     */       }
/*     */ 
/*     */     }
/* 299 */     else if (this.policy == 0)
/*     */     {
/* 302 */       i = size().width;
/* 303 */       j = size().height;
/*     */     }
/* 307 */     else if (this.policy == 3)
/*     */     {
/* 310 */       i = size().width - this.vertical.preferredSize().width;
/*     */ 
/* 312 */       j = size().height - this.horizontal.preferredSize().height;
/*     */     }
/* 317 */     else if (this.policy == 2)
/*     */     {
/* 320 */       if (this.vertical != null)
/*     */       {
/* 322 */         i = size().width - this.vertical.preferredSize().width;
/*     */ 
/* 324 */         if (this.horizontal != null)
/*     */         {
/* 327 */           j = size().height - this.horizontal.preferredSize().height;
/*     */         }
/*     */         else
/*     */         {
/* 331 */           j = size().height;
/*     */         }
/*     */       }
/*     */ 
/* 335 */       if (this.horizontal != null)
/*     */       {
/* 337 */         j = size().height - this.horizontal.preferredSize().height;
/*     */ 
/* 339 */         if (this.vertical != null)
/*     */         {
/* 342 */           i = size().width - this.vertical.preferredSize().width;
/*     */         }
/*     */         else
/*     */         {
/* 346 */           i = size().width;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 351 */     return new Dimension(i, j);
/*     */   }
/*     */ 
/*     */   public boolean handleEvent(Event paramEvent)
/*     */   {
/* 359 */     Object localObject = paramEvent.target;
/*     */ 
/* 365 */     if ((localObject instanceof Button))
/*     */     {
/* 367 */       this.comp.move(-this.compWidth / 2, -this.compHeight / 2);
/*     */ 
/* 369 */       this.vertical.setValue(this.compHeight / 2);
/* 370 */       this.horizontal.setValue(this.compWidth / 2);
/*     */     }
/* 374 */     else if ((localObject instanceof Scrollbar))
/*     */     {
/* 376 */       int i = ((Integer)paramEvent.arg).intValue();
/*     */ 
/* 379 */       if (this.policy == 1)
/*     */       {
/* 381 */         this.paintY = (-i);
/* 382 */         this.paintX = 0;
/* 383 */         this.comp.move(this.paintX, this.paintY);
/*     */       }
/* 387 */       else if (this.policy == 0)
/*     */       {
/* 389 */         this.paintY = 0;
/* 390 */         this.paintX = (-i);
/* 391 */         this.comp.move(this.paintX, this.paintY);
/*     */       }
/* 395 */       else if ((this.policy == 3) || (this.policy == 2))
/*     */       {
/* 397 */         if (localObject == this.horizontal)
/*     */         {
/* 399 */           this.paintX = (-i);
/* 400 */           this.paintY = (-this.vertical.getValue());
/*     */         }
/* 403 */         else if (localObject == this.vertical)
/*     */         {
/* 405 */           this.paintY = (-i);
/* 406 */           this.paintX = (-this.horizontal.getValue());
/*     */         }
/* 408 */         this.comp.move(this.paintX, this.paintY);
/*     */       }
/*     */     }
/*     */     else {
/* 412 */       return false;
/*     */     }
/* 414 */     return super.handleEvent(paramEvent);
/*     */   }
/*     */ 
/*     */   public void layout()
/*     */   {
/* 202 */     switch (this.policy)
/*     */     {
/*     */     case 0:
/* 206 */       add("south", this.horizontal);
/* 207 */       add("center", this.comp);
/* 208 */       break;
/*     */     case 1:
/* 212 */       add("east", this.vertical);
/* 213 */       add("center", this.comp);
/* 214 */       break;
/*     */     case 2:
/* 219 */       int i = 0;
/* 220 */       int j = 0;
/*     */ 
/* 222 */       int k = this.comp.preferredSize().width;
/* 223 */       int m = this.comp.preferredSize().height;
/*     */ 
/* 226 */       if (m > size().height - this.vertical.preferredSize().height)
/*     */       {
/* 229 */         add("east", this.vertical);
/* 230 */         j = 1;
/*     */       }
/*     */ 
/* 234 */       if (k > size().width - this.horizontal.preferredSize().width)
/*     */       {
/* 237 */         add("south", this.horizontal);
/* 238 */         i = 1;
/*     */       }
/*     */ 
/* 241 */       if ((i != 0) && (j != 0)) {
/* 242 */         add("corner", this.box);
/*     */       }
/*     */ 
/* 245 */       add("center", this.comp);
/* 246 */       break;
/*     */     case 3:
/* 250 */       add("east", this.vertical);
/* 251 */       add("south", this.horizontal);
/* 252 */       add("corner", this.box);
/* 253 */       add("center", this.comp);
/* 254 */       break;
/*     */     }
/*     */ 
/* 262 */     if (this.vertical != null)
/*     */     {
/* 264 */       this.compHeight = (this.comp.preferredSize().height - getViewPort().height);
/*     */ 
/* 266 */       this.vertical.setValues(0, 20, 0, this.compHeight + 20);
/*     */     }
/*     */ 
/* 269 */     if (this.horizontal != null)
/*     */     {
/* 271 */       this.compWidth = (this.comp.preferredSize().width - getViewPort().width);
/*     */ 
/* 273 */       this.horizontal.setValues(0, 20, 0, this.compWidth + 20);
/*     */     }
/*     */ 
/* 277 */     super.layout();
/*     */   }
/*     */ 
/*     */   public Dimension preferredSize()
/*     */   {
/* 125 */     return super.preferredSize();
/*     */   }
/*     */ 
/*     */   public void setScrollPolicy(int paramInt)
/*     */   {
/* 186 */     this.policy = paramInt;
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.component.JimiScrollPane
 * JD-Core Version:    0.6.2
 */