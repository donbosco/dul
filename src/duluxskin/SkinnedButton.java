/*     */ package duluxskin;
/*     */ 
/*     */ import dulux.DuluxColour;
/*     */ import java.awt.AlphaComposite;
/*     */ import java.awt.Color;
/*     */ import java.awt.Font;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Image;
/*     */ import java.awt.Point;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.util.Iterator;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class SkinnedButton extends Widget
/*     */ {
/*  19 */   private boolean notShifted = true;
/*  20 */   public final int THUMBNAIL_MAX_SIZE = 200;
/*     */   private boolean toggle;
/*     */   private boolean state;
/*     */   private boolean down;
/*     */   private boolean over;
/*     */   private boolean capturing;
/*     */   private boolean repeating;
/*     */   private Image thumbnail;
/*     */   private Image upImage;
/*     */   private Image downImage;
/*     */   private Image disabledImage;
/*     */   private Image onImage;
/*     */   private Image overImage;
/*     */   private Vector actionListeners;
/*     */   private RepeatPressThread repeatThread;
/*     */   private Color color;
/*     */   private DuluxColour colour;
/*     */   private String data;
/*  39 */   private Color[] scheme = new Color[3];
/*     */   private boolean selected;
/*     */   private int type;
/*  42 */   private final int IS_IMAGE = 0;
/*  43 */   private final int IS_COLOUR = 1;
/*  44 */   private final int IS_SCHEME = 2;
/*  45 */   private final int IS_DYNAMIC = 3;
/*     */   private String dmcURL;
/*     */ 
/*     */   public SkinnedButton(String id, int x, int y, int width, int height, int type)
/*     */   {
/*  49 */     super(id, x, y);
/*  50 */     this.width = width;
/*  51 */     this.height = height;
/*  52 */     this.type = type;
/*     */   }
/*     */ 
/*     */   public SkinnedButton(String id, int x, int y, Image up, Image down, Image over, Image disabled, Image on, boolean toggle, int colour, int type)
/*     */   {
/*  58 */     super(id, x, y);
/*  59 */     this.type = type;
/*  60 */     if ((type == 1) || (type == 2)) {
/*  61 */       this.width = 80;
/*  62 */       this.height = 168;
/*  63 */     } else if (type == 0) {
/*  64 */       this.width = up.getWidth(null);
/*     */ 
/*  66 */       this.height = up.getHeight(null);
/*     */     }
/*     */ 
/*  72 */     this.toggle = toggle;
/*     */ 
/*  74 */     this.upImage = up;
/*     */ 
/*  76 */     this.downImage = down;
/*     */ 
/*  78 */     this.overImage = over;
/*     */ 
/*  80 */     this.disabledImage = disabled;
/*     */ 
/*  82 */     this.onImage = on;
/*     */ 
/*  84 */     this.actionListeners = new Vector();
/*     */ 
/*  86 */     this.data = null;
/*     */   }
/*     */ 
/*     */   public SkinnedButton(String id, int x, int y, Image up, Image down, Image disabled, Image on, boolean toggle, int colour, int type, int width, int height)
/*     */   {
/*  95 */     super(id, x, y);
/*  96 */     this.type = type;
/*     */ 
/*  98 */     this.width = width;
/*  99 */     this.height = height;
/*     */ 
/* 101 */     this.toggle = toggle;
/*     */ 
/* 103 */     this.upImage = up;
/*     */ 
/* 105 */     this.downImage = down;
/*     */ 
/* 107 */     this.disabledImage = disabled;
/*     */ 
/* 109 */     this.onImage = on;
/*     */ 
/* 111 */     this.actionListeners = new Vector();
/*     */   }
/*     */ 
/*     */   public void setColour(int colour) {
/* 115 */     this.color = new Color(colour);
/* 116 */     invalidate();
/*     */   }
/*     */ 
/*     */   public void setColour(int colour, String data) {
/* 120 */     this.color = new Color(colour);
/* 121 */     this.data = new String(data);
/* 122 */     invalidate();
/*     */   }
/*     */ 
/*     */   public void setColour(DuluxColour dc) {
/*     */     try {
/* 127 */       this.colour = dc;
/* 128 */       this.color = new Color(dc.displayColour);
/* 129 */       this.visible = true;
/* 130 */       invalidate();
/*     */     }
/*     */     catch (Exception np) {
/* 133 */       this.visible = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   public DuluxColour getColour()
/*     */   {
/* 143 */     return this.colour;
/*     */   }
/*     */   public String getURL() {
/* 146 */     return this.dmcURL;
/*     */   }
/*     */ 
/*     */   public void setOver(boolean over) {
/* 150 */     this.over = over;
/* 151 */     invalidate();
/*     */   }
/*     */ 
/*     */   public void setScheme(int[] scheme) {
/* 155 */     for (int i = 0; i < scheme.length; i++) {
/* 156 */       this.scheme[i] = new Color(scheme[i]);
/*     */     }
/* 158 */     invalidate();
/*     */   }
/*     */ 
/*     */   public void setRepeating(boolean repeating)
/*     */   {
/* 163 */     this.repeating = repeating;
/*     */   }
/*     */ 
/*     */   public void setState(boolean s)
/*     */   {
/* 169 */     this.state = s;
/*     */ 
/* 171 */     invalidate();
/*     */   }
/*     */ 
/*     */   public void setSelected(boolean selected)
/*     */   {
/* 176 */     this.selected = selected;
/* 177 */     invalidate();
/*     */   }
/*     */ 
/*     */   public boolean getState()
/*     */   {
/* 182 */     return this.state;
/*     */   }
/*     */ 
/*     */   public void setImage(Image image, String url)
/*     */   {
/* 187 */     this.upImage = null;
/* 188 */     this.downImage = null;
/* 189 */     invalidate();
/* 190 */     this.upImage = image;
/* 191 */     this.downImage = loadTranslucentImage(image, 0.8F);
/*     */ 
/* 194 */     if (this.upImage.getHeight(null) < this.upImage.getWidth(null))
/*     */     {
/* 196 */       this.height = (200 * this.upImage.getHeight(null) / this.upImage.getWidth(null));
/* 197 */       this.width = 200;
/*     */     }
/*     */     else {
/* 200 */       this.width = (200 * this.upImage.getWidth(null) / this.upImage.getHeight(null));
/* 201 */       this.height = 200;
/*     */     }
/* 203 */     this.dmcURL = url;
/* 204 */     invalidate();
/* 205 */     this.notShifted = false;
/*     */   }
/*     */   public void removeImage() {
/* 208 */     this.upImage = null;
/* 209 */     this.downImage = null;
/* 210 */     invalidate();
/*     */   }
/*     */ 
/*     */   public void paint(Graphics g) {
/* 214 */     if (this.type == 0) {
/* 215 */       if (this.down)
/* 216 */         g.drawImage(this.downImage, 0, 0, this.width, this.height, null);
/* 217 */       else if (!this.enabled)
/* 218 */         g.drawImage(this.disabledImage, 0, 0, this.width, this.height, null);
/* 219 */       else if ((this.state) || (this.selected))
/* 220 */         g.drawImage(this.onImage, 0, 0, this.width, this.height, null);
/* 221 */       else if (this.over)
/* 222 */         g.drawImage(this.overImage, 0, 0, this.width, this.height, null);
/*     */       else
/* 224 */         g.drawImage(this.upImage, 0, 0, this.width, this.height, null);
/*     */     }
/* 226 */     else if (this.type == 1) {
/* 227 */       g.setColor(this.color);
/* 228 */       if (this.down) {
/* 229 */         g.setColor(new Color(this.color.getRed(), this.color.getGreen(), this.color.getBlue(), 100));
/* 230 */         g.fillRect(0, 0, this.width, this.height);
/* 231 */       } else if (!this.enabled) {
/* 232 */         g.setColor(this.color);
/* 233 */         g.fillRect(0, 0, this.width, this.height);
/* 234 */       } else if (this.selected) {
/* 235 */         g.fillRect(0, 0, this.width, this.height);
/* 236 */         g.setColor(new Color(Color.BLACK.getRGB()));
/* 237 */         g.drawRect(0, 0, this.width, this.height);
/* 238 */         g.drawRect(1, 1, this.width, this.height);
/*     */       } else {
/* 240 */         g.setColor(this.color);
/* 241 */         g.fillRect(0, 0, this.width, this.height);
/*     */       }
/* 243 */       g.setColor(Color.WHITE);
/* 244 */       if (this.colour != null)
/*     */       {
/* 248 */         double luminance = this.color.getRed() * 0.2126D + 0.7152D * this.color.getGreen() + 0.0722D * this.color.getBlue();
/*     */ 
/* 251 */         if (luminance > 190.0D) {
/* 252 */           g.setColor(Color.gray);
/*     */         }
/* 254 */         g.setFont(new Font("", 0, 8));
/*     */ 
/* 256 */         String name = new String(this.colour.name);
/*     */ 
/* 258 */         if ((this.width == 80) && (name.length() > 13)) {
/* 259 */           name = name.substring(0, 13);
/*     */         }
/*     */ 
/* 262 */         g.drawString(name, 8, this.height - 8);
/*     */         try {
/* 264 */           g.drawString(this.colour.chip, 8, 8);
/*     */         } catch (NullPointerException e) {
/*     */         }
/*     */       }
/*     */     }
/* 269 */     else if (this.type == 2)
/*     */     {
/* 271 */       g.setColor(this.scheme[0]);
/* 272 */       if (this.down)
/*     */       {
/* 274 */         g.setColor(new Color(this.scheme[0].getRed(), this.scheme[0].getGreen(), this.scheme[0].getBlue(), 100));
/* 275 */         g.fillRect(0, 0, this.width, 56);
/* 276 */         g.setColor(new Color(this.scheme[1].getRed(), this.scheme[1].getGreen(), this.scheme[1].getBlue(), 100));
/* 277 */         g.fillRect(0, 56, this.width, 56);
/* 278 */         g.setColor(new Color(this.scheme[2].getRed(), this.scheme[2].getGreen(), this.scheme[2].getBlue(), 100));
/* 279 */         g.fillRect(0, 112, this.width, 56);
/*     */ 
/* 282 */         g.setColor(new Color(this.scheme[0].getRed(), this.scheme[0].getGreen(), this.scheme[0].getBlue(), 100));
/* 283 */         g.fillRect(0, 0, this.width, 56);
/* 284 */         g.setColor(new Color(this.scheme[1].getRed(), this.scheme[1].getGreen(), this.scheme[1].getBlue(), 100));
/* 285 */         g.fillRect(0, 56, this.width, 56);
/* 286 */         g.setColor(new Color(this.scheme[2].getRed(), this.scheme[2].getGreen(), this.scheme[2].getBlue(), 100));
/* 287 */         g.fillRect(0, 112, this.width, 56);
/*     */       }
/* 289 */       else if (!this.enabled) {
/* 290 */         g.setColor(Color.ORANGE);
/* 291 */         g.fillRect(0, 0, this.width, this.height);
/* 292 */       } else if (this.selected) {
/* 293 */         g.setColor(this.scheme[0]);
/* 294 */         g.fillRect(0, 0, this.width, this.height / 3);
/* 295 */         g.setColor(this.scheme[1]);
/* 296 */         g.fillRect(0, 56, this.width, this.height / 3);
/* 297 */         g.setColor(this.scheme[2]);
/* 298 */         g.fillRect(0, 112, this.width, this.height / 3);
/* 299 */         g.setColor(new Color(Color.BLACK.getRGB()));
/* 300 */         g.drawRect(0, 0, this.width, this.height);
/* 301 */         g.drawRect(1, 1, this.width - 2, this.height - 2);
/*     */       } else {
/* 303 */         g.setColor(this.scheme[0]);
/* 304 */         g.fillRect(0, 0, this.width, this.height / 3);
/* 305 */         g.setColor(this.scheme[1]);
/* 306 */         g.fillRect(0, 56, this.width, this.height / 3);
/* 307 */         g.setColor(this.scheme[2]);
/* 308 */         g.fillRect(0, 112, this.width, this.height / 3);
/*     */       }
/* 310 */     } else if (this.type == 3)
/*     */     {
/* 319 */       if (this.down)
/*     */       {
/* 323 */         g.drawImage(this.downImage, 0, 0, this.width, this.height, null);
/*     */       }
/*     */       else
/* 326 */         g.drawImage(this.upImage, 0, 0, this.width, this.height, null);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Point shiftGraphics()
/*     */   {
/* 340 */     return new Point(0, 0);
/*     */   }
/*     */ 
/*     */   public static Image loadTranslucentImage(Image image, float transperancy)
/*     */   {
/* 350 */     BufferedImage loaded = (BufferedImage)image;
/*     */ 
/* 352 */     BufferedImage aimg = new BufferedImage(loaded.getWidth(), loaded.getHeight(), 3);
/*     */ 
/* 354 */     Graphics2D g = aimg.createGraphics();
/*     */ 
/* 356 */     g.setComposite(AlphaComposite.getInstance(3, transperancy));
/*     */ 
/* 358 */     g.drawImage(loaded, null, 0, 0);
/*     */ 
/* 360 */     g.dispose();
/*     */ 
/* 362 */     return aimg;
/*     */   }
/*     */ 
/*     */   protected void notifyActionPerformed()
/*     */   {
/* 367 */     ActionEvent ae = new ActionEvent(this, 0, this.id);
/*     */ 
/* 369 */     for (Iterator it = this.actionListeners.iterator(); it.hasNext(); )
/* 370 */       ((ActionListener)it.next()).actionPerformed(ae);
/*     */   }
/*     */ 
/*     */   protected boolean firstChanceMouseEvent(MouseEvent e)
/*     */   {
/* 377 */     if (!this.enabled)
/*     */     {
/* 379 */       e.consume();
/*     */ 
/* 381 */       return true;
/*     */     }
/*     */ 
/* 387 */     switch (e.getID())
/*     */     {
/*     */     case 501:
/* 391 */       this.down = true;
/*     */ 
/* 393 */       this.capturing = true;
/*     */ 
/* 395 */       setCapture();
/*     */ 
/* 397 */       invalidate();
/*     */ 
/* 401 */       if (this.repeating)
/*     */       {
/* 403 */         notifyActionPerformed();
/*     */ 
/* 405 */         this.repeatThread = new RepeatPressThread();
/*     */ 
/* 407 */         this.repeatThread.start(); } break;
/*     */     case 502:
/* 417 */       if ((this.down) && (this.capturing))
/*     */       {
/* 419 */         releaseCapture();
/*     */ 
/* 423 */         if (this.toggle) {
/* 424 */           this.state = (!this.state);
/*     */         }
/*     */ 
/* 429 */         if (this.repeatThread == null) {
/* 430 */           notifyActionPerformed();
/*     */         }
/*     */         else {
/* 433 */           synchronized (this.repeatThread)
/*     */           {
/* 435 */             this.repeatThread.run = false;
/*     */ 
/* 437 */             this.repeatThread = null;
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/* 443 */       else if (this.capturing) {
/* 444 */         releaseCapture();
/*     */       }
/*     */ 
/* 447 */       this.down = false;
/*     */ 
/* 449 */       this.capturing = false;
/*     */ 
/* 451 */       invalidate();
/*     */ 
/* 453 */       break;
/*     */     case 505:
/* 459 */       if ((this.capturing) && (e.getSource() == this))
/*     */       {
/* 461 */         this.down = false;
/*     */ 
/* 463 */         invalidate(); } break;
/*     */     case 504:
/* 473 */       if ((this.capturing) && (e.getSource() == this))
/*     */       {
/* 475 */         this.down = true;
/*     */ 
/* 477 */         invalidate();
/*     */       }
/*     */ 
/*     */       break;
/*     */     case 503:
/*     */     }
/*     */ 
/* 485 */     return true;
/*     */   }
/*     */ 
/*     */   public void addActionListener(ActionListener l)
/*     */   {
/* 491 */     this.actionListeners.add(l);
/*     */   }
/*     */ 
/*     */   private class RepeatPressThread extends Thread
/*     */   {
/*     */     public boolean run;
/*     */     private static final int DELAY_MILLIS = 400;
/*     */     private static final int RATE_MILLIS = 50;
/*     */ 
/*     */     public RepeatPressThread() {
/* 503 */       this.run = true;
/*     */     }
/*     */ 
/*     */     public void run()
/*     */     {
/*     */       try
/*     */       {
/* 511 */         sleep(400L);
/*     */       }
/*     */       catch (InterruptedException e)
/*     */       {
/*     */       }
/*     */       try
/*     */       {
/*     */         while (true)
/*     */         {
/* 520 */           synchronized (this)
/*     */           {
/* 522 */             if (!this.run)
/*     */             {
/*     */               break;
/*     */             }
/* 526 */             if (SkinnedButton.this.down) {
/* 527 */               SkinnedButton.this.notifyActionPerformed();
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/* 532 */           sleep(50L);
/*     */         }
/*     */       }
/*     */       catch (InterruptedException e)
/*     */       {
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     duluxskin.SkinnedButton
 * JD-Core Version:    0.6.2
 */