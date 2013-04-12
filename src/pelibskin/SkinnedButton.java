/*     */ package pelibskin;
/*     */ 
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Image;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.util.Iterator;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class SkinnedButton extends Widget
/*     */ {
/*     */   private boolean toggle;
/*     */   private boolean state;
/*     */   private boolean down;
/*     */   private boolean capturing;
/*     */   private boolean repeating;
/*     */   private Image upImage;
/*     */   private Image downImage;
/*     */   private Image disabledImage;
/*     */   private Image onImage;
/*     */   private Vector actionListeners;
/*     */   private RepeatPressThread repeatThread;
/*     */ 
/*     */   public SkinnedButton(String id, int x, int y, Image up, Image down, Image disabled, Image on, boolean toggle)
/*     */   {
/*  61 */     super(id, x, y);
/*     */ 
/*  63 */     this.width = up.getWidth(null);
/*     */ 
/*  65 */     this.height = up.getHeight(null);
/*     */ 
/*  67 */     this.toggle = toggle;
/*     */ 
/*  69 */     this.upImage = up;
/*     */ 
/*  71 */     this.downImage = down;
/*     */ 
/*  73 */     this.disabledImage = disabled;
/*     */ 
/*  75 */     this.onImage = on;
/*     */ 
/*  77 */     this.actionListeners = new Vector();
/*     */   }
/*     */ 
/*     */   public void setRepeating(boolean repeating)
/*     */   {
/*  87 */     this.repeating = repeating;
/*     */   }
/*     */ 
/*     */   public void setState(boolean s)
/*     */   {
/*  97 */     this.state = s;
/*     */ 
/*  99 */     invalidate();
/*     */   }
/*     */ 
/*     */   public boolean getState()
/*     */   {
/* 109 */     return this.state;
/*     */   }
/*     */ 
/*     */   public void paint(Graphics g)
/*     */   {
/* 119 */     if (this.down)
/*     */     {
/* 121 */       g.drawImage(this.downImage, 0, 0, this.width, this.height, null);
/*     */     }
/* 123 */     else if (!this.enabled)
/*     */     {
/* 125 */       g.drawImage(this.disabledImage, 0, 0, this.width, this.height, null);
/*     */     }
/* 127 */     else if (this.state)
/*     */     {
/* 129 */       g.drawImage(this.onImage, 0, 0, this.width, this.height, null);
/*     */     }
/*     */     else
/*     */     {
/* 133 */       g.drawImage(this.upImage, 0, 0, this.width, this.height, null);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void notifyActionPerformed()
/*     */   {
/* 143 */     ActionEvent ae = new ActionEvent(this, 0, this.id);
/*     */ 
/* 145 */     for (Iterator it = this.actionListeners.iterator(); it.hasNext(); )
/*     */     {
/* 147 */       ((ActionListener)it.next()).actionPerformed(ae);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected boolean firstChanceMouseEvent(MouseEvent e)
/*     */   {
/* 157 */     if (!this.enabled)
/*     */     {
/* 161 */       e.consume();
/*     */ 
/* 163 */       return true;
/*     */     }
/*     */ 
/* 169 */     switch (e.getID())
/*     */     {
/*     */     case 501:
/* 175 */       this.down = true;
/*     */ 
/* 177 */       this.capturing = true;
/*     */ 
/* 179 */       setCapture();
/*     */ 
/* 181 */       invalidate();
/*     */ 
/* 185 */       if (this.repeating)
/*     */       {
/* 189 */         notifyActionPerformed();
/*     */ 
/* 191 */         this.repeatThread = new RepeatPressThread();
/*     */ 
/* 193 */         this.repeatThread.start(); } break;
/*     */     case 502:
/* 203 */       if ((this.down) && (this.capturing))
/*     */       {
/* 207 */         releaseCapture();
/*     */ 
/* 211 */         if (this.toggle)
/*     */         {
/* 213 */           this.state = (!this.state);
/*     */         }
/*     */ 
/* 217 */         if (this.repeatThread == null)
/*     */         {
/* 219 */           notifyActionPerformed();
/*     */         }
/*     */         else
/*     */         {
/* 225 */           synchronized (this.repeatThread)
/*     */           {
/* 227 */             this.repeatThread.run = false;
/*     */ 
/* 229 */             this.repeatThread = null;
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/* 237 */       else if (this.capturing)
/*     */       {
/* 239 */         releaseCapture();
/*     */       }
/* 241 */       this.down = false;
/*     */ 
/* 243 */       this.capturing = false;
/*     */ 
/* 245 */       invalidate();
/*     */ 
/* 247 */       break;
/*     */     case 505:
/* 253 */       if ((this.capturing) && (e.getSource() == this))
/*     */       {
/* 257 */         this.down = false;
/*     */ 
/* 259 */         invalidate(); } break;
/*     */     case 504:
/* 269 */       if ((this.capturing) && (e.getSource() == this))
/*     */       {
/* 273 */         this.down = true;
/*     */ 
/* 275 */         invalidate();
/*     */       }
/*     */ 
/*     */       break;
/*     */     case 503:
/*     */     }
/*     */ 
/* 283 */     return true;
/*     */   }
/*     */ 
/*     */   public void addActionListener(ActionListener l)
/*     */   {
/* 293 */     this.actionListeners.add(l);
/*     */   }
/*     */ 
/*     */   private class RepeatPressThread extends Thread
/*     */   {
/*     */     public boolean run;
/*     */     private static final int DELAY_MILLIS = 400;
/*     */     private static final int RATE_MILLIS = 50;
/*     */ 
/*     */     public RepeatPressThread()
/*     */     {
/* 315 */       this.run = true;
/*     */     }
/*     */ 
/*     */     public void run()
/*     */     {
/*     */       try
/*     */       {
/* 327 */         sleep(400L);
/*     */       }
/*     */       catch (InterruptedException e)
/*     */       {
/*     */       }
/*     */ 
/*     */       try
/*     */       {
/*     */         while (true)
/*     */         {
/* 339 */           synchronized (this)
/*     */           {
/* 341 */             if (!this.run)
/*     */             {
/*     */               break;
/*     */             }
/* 345 */             if (SkinnedButton.this.down)
/*     */             {
/* 347 */               SkinnedButton.this.notifyActionPerformed();
/*     */             }
/*     */           }
/*     */ 
/* 351 */           sleep(50L);
/*     */         }
/*     */       }
/*     */       catch (InterruptedException e)
/*     */       {
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelibskin.SkinnedButton
 * JD-Core Version:    0.6.2
 */