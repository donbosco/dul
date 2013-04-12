/*     */ package pelib.ui;
/*     */ 
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Image;
/*     */ import java.awt.Panel;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.awt.event.MouseListener;
/*     */ import java.awt.event.MouseMotionListener;
/*     */ import java.util.Iterator;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class ColourPalette extends Panel
/*     */ {
/*     */   private float hue;
/*     */   private float saturation;
/*     */   private float brightness;
/*     */   private HueSaturationPicker hsPicker;
/*     */   private BrightnessPicker bPicker;
/*     */   private Dimension preferredSize;
/*     */   private Vector listeners;
/*     */ 
/*     */   public ColourPalette()
/*     */   {
/*  26 */     setLayout(new BorderLayout());
/*     */ 
/*  28 */     this.hsPicker = new HueSaturationPicker();
/*  29 */     add(this.hsPicker);
/*     */ 
/*  31 */     this.bPicker = new BrightnessPicker();
/*  32 */     add(this.bPicker, "East");
/*     */ 
/*  34 */     this.listeners = new Vector();
/*     */ 
/*  36 */     setPreferredSize(new Dimension(250, 200));
/*     */   }
/*     */ 
/*     */   public void addActionListener(ActionListener listener)
/*     */   {
/*  41 */     if (!this.listeners.contains(listener))
/*  42 */       this.listeners.add(listener);
/*     */   }
/*     */ 
/*     */   public void updatePalettes()
/*     */   {
/*  47 */     this.hsPicker.repaint();
/*  48 */     this.bPicker.repaint();
/*  49 */     for (Iterator it = this.listeners.iterator(); it.hasNext(); )
/*     */     {
/*  51 */       ActionListener listener = (ActionListener)it.next();
/*  52 */       listener.actionPerformed(new ActionEvent(this, 1001, "Palette"));
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setPreferredSize(Dimension d)
/*     */   {
/*  61 */     this.preferredSize = d;
/*     */   }
/*     */ 
/*     */   public Dimension getPreferredSize()
/*     */   {
/*  66 */     return this.preferredSize;
/*     */   }
/*     */ 
/*     */   public Dimension getMinimumSize()
/*     */   {
/*  71 */     return this.preferredSize;
/*     */   }
/*     */ 
/*     */   public boolean isDoubleBuffered()
/*     */   {
/*  76 */     return true;
/*     */   }
/*     */ 
/*     */   public void update(Graphics g)
/*     */   {
/*  81 */     paint(g);
/*     */   }
/*     */ 
/*     */   public void setColour(int bgr)
/*     */   {
/*  86 */     setColour(bgr & 0xFF, bgr >> 8 & 0xFF, bgr >> 16 & 0xFF);
/*     */   }
/*     */ 
/*     */   public void setColour(int red, int green, int blue)
/*     */   {
/*  91 */     float[] vals = Color.RGBtoHSB(red, green, blue, null);
/*  92 */     this.hue = vals[0];
/*  93 */     this.saturation = vals[1];
/*  94 */     this.brightness = vals[2];
/*     */   }
/*     */ 
/*     */   public int getBGR()
/*     */   {
/*  99 */     Color color = Color.getHSBColor(this.hue, this.saturation, this.brightness);
/* 100 */     return color.getRed() | color.getGreen() << 8 | color.getBlue() << 16;
/*     */   }
/*     */ 
/*     */   public Color getColor()
/*     */   {
/* 107 */     return Color.getHSBColor(this.hue, this.saturation, this.brightness);
/*     */   }
/*     */ 
/*     */   private class BrightnessPicker extends Component
/*     */     implements MouseListener, MouseMotionListener
/*     */   {
/*     */     private Image gradientImage;
/*     */     private Image backImage;
/*     */     private Graphics backGraphics;
/*     */     private float oldHue;
/*     */     private float oldSaturation;
/*     */ 
/*     */     public BrightnessPicker()
/*     */     {
/* 232 */       addMouseListener(this);
/* 233 */       addMouseMotionListener(this);
/*     */     }
/*     */ 
/*     */     public boolean isDoubleBuffered()
/*     */     {
/* 238 */       return true;
/*     */     }
/*     */ 
/*     */     public void update(Graphics g)
/*     */     {
/* 243 */       paint(g);
/*     */     }
/*     */ 
/*     */     public void paint(Graphics g)
/*     */     {
/* 248 */       int width = getWidth();
/* 249 */       int height = getHeight();
/*     */ 
/* 251 */       if ((this.backImage == null) || (this.backImage.getWidth(null) != width) || (this.backImage.getHeight(null) != height))
/*     */       {
/* 255 */         this.backImage = createImage(width, height);
/* 256 */         this.backGraphics = this.backImage.getGraphics();
/* 257 */         this.gradientImage = createImage(width, height);
/* 258 */         this.oldHue = -1.0F;
/*     */       }
/*     */ 
/* 261 */       if ((ColourPalette.this.hue != this.oldHue) || (ColourPalette.this.saturation != this.oldSaturation)) {
/* 262 */         createGradientImage(width, height);
/*     */       }
/*     */ 
/* 265 */       this.backGraphics.drawImage(this.gradientImage, 0, 0, null);
/*     */ 
/* 268 */       int y = (int)(height * ColourPalette.this.brightness);
/* 269 */       this.backGraphics.setColor(new Color(1.0F - ColourPalette.this.brightness, 1.0F - ColourPalette.this.brightness, 1.0F - ColourPalette.this.brightness));
/*     */ 
/* 271 */       this.backGraphics.drawLine(0, y, width, y);
/*     */ 
/* 273 */       g.drawImage(this.backImage, 0, 0, null);
/*     */     }
/*     */ 
/*     */     private void createGradientImage(int width, int height)
/*     */     {
/* 278 */       Graphics g = this.gradientImage.getGraphics();
/*     */ 
/* 280 */       for (int y = 0; y < height; y++)
/*     */       {
/* 282 */         g.setColor(Color.getHSBColor(ColourPalette.this.hue, ColourPalette.this.saturation, y / height));
/*     */ 
/* 284 */         g.drawLine(0, y, width, y);
/*     */       }
/*     */ 
/* 287 */       this.oldHue = ColourPalette.this.hue;
/* 288 */       this.oldSaturation = ColourPalette.this.saturation;
/*     */     }
/*     */ 
/*     */     public void updateColour(int x, int y)
/*     */     {
/* 293 */       int height = getHeight();
/* 294 */       ColourPalette.this.brightness = (y / height);
/*     */ 
/* 296 */       ColourPalette.this.brightness = Math.min(Math.max(0.0F, ColourPalette.this.brightness), 1.0F);
/*     */ 
/* 298 */       ColourPalette.this.updatePalettes();
/*     */     }
/*     */ 
/*     */     public Dimension getPreferredSize()
/*     */     {
/* 303 */       return new Dimension(20, 100);
/*     */     }
/*     */ 
/*     */     public void mouseReleased(MouseEvent e)
/*     */     {
/*     */     }
/*     */ 
/*     */     public void mouseDragged(MouseEvent e)
/*     */     {
/* 312 */       updateColour(e.getX(), e.getY());
/*     */     }
/*     */ 
/*     */     public void mousePressed(MouseEvent e)
/*     */     {
/* 317 */       updateColour(e.getX(), e.getY());
/*     */     }
/*     */ 
/*     */     public void mouseMoved(MouseEvent e)
/*     */     {
/*     */     }
/*     */ 
/*     */     public void mouseClicked(MouseEvent e)
/*     */     {
/*     */     }
/*     */ 
/*     */     public void mouseEntered(MouseEvent e)
/*     */     {
/*     */     }
/*     */ 
/*     */     public void mouseExited(MouseEvent e)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   private class HueSaturationPicker extends Component
/*     */     implements MouseListener, MouseMotionListener
/*     */   {
/*     */     private Image gradientImage;
/*     */     private Image backImage;
/*     */     private Graphics backGraphics;
/*     */     private float oldBrightness;
/*     */ 
/*     */     public HueSaturationPicker()
/*     */     {
/* 122 */       addMouseListener(this);
/* 123 */       addMouseMotionListener(this);
/*     */     }
/*     */ 
/*     */     public boolean isDoubleBuffered()
/*     */     {
/* 128 */       return true;
/*     */     }
/*     */ 
/*     */     public void update(Graphics g)
/*     */     {
/* 133 */       paint(g);
/*     */     }
/*     */ 
/*     */     public void paint(Graphics g)
/*     */     {
/* 138 */       int width = getWidth();
/* 139 */       int height = getHeight();
/*     */ 
/* 141 */       if ((this.backImage == null) || (this.backImage.getWidth(null) != width) || (this.backImage.getHeight(null) != height))
/*     */       {
/* 145 */         this.backImage = createImage(width, height);
/* 146 */         this.backGraphics = this.backImage.getGraphics();
/* 147 */         this.gradientImage = createImage(width, height);
/* 148 */         this.oldBrightness = -1.0F;
/*     */       }
/*     */ 
/* 151 */       if (this.oldBrightness != ColourPalette.this.brightness) {
/* 152 */         createGradientImage(width, height);
/*     */       }
/*     */ 
/* 155 */       this.backGraphics.drawImage(this.gradientImage, 0, 0, null);
/*     */ 
/* 158 */       int x = (int)(width * ColourPalette.this.hue);
/* 159 */       int y = (int)(height * (1.0F - ColourPalette.this.saturation));
/* 160 */       this.backGraphics.setColor(new Color(1.0F - ColourPalette.this.brightness, 1.0F - ColourPalette.this.brightness, 1.0F - ColourPalette.this.brightness));
/*     */ 
/* 162 */       this.backGraphics.drawRect(x - 2, y - 2, 4, 4);
/*     */ 
/* 165 */       g.drawImage(this.backImage, 0, 0, null);
/*     */     }
/*     */ 
/*     */     private void createGradientImage(int width, int height)
/*     */     {
/* 171 */       Graphics g = this.gradientImage.getGraphics();
/*     */ 
/* 173 */       for (int x = 0; x < width; x++)
/*     */       {
/* 175 */         for (int y = 0; y < height; y++)
/*     */         {
/* 177 */           g.setColor(Color.getHSBColor(x / width, 1.0F - y / height, ColourPalette.this.brightness));
/*     */ 
/* 180 */           g.drawRect(x, y, 1, 1);
/*     */         }
/*     */       }
/*     */ 
/* 184 */       this.oldBrightness = ColourPalette.this.brightness;
/*     */     }
/*     */ 
/*     */     public void updateColour(int x, int y)
/*     */     {
/* 189 */       int width = getWidth();
/* 190 */       int height = getHeight();
/*     */ 
/* 192 */       ColourPalette.this.hue = (x / width);
/* 193 */       ColourPalette.this.saturation = (1.0F - y / height);
/*     */ 
/* 195 */       ColourPalette.this.hue = Math.min(Math.max(0.0F, ColourPalette.this.hue), 1.0F);
/* 196 */       ColourPalette.this.saturation = Math.min(Math.max(0.0F, ColourPalette.this.saturation), 1.0F);
/*     */ 
/* 198 */       ColourPalette.this.updatePalettes();
/*     */     }
/*     */ 
/*     */     public void mouseReleased(MouseEvent e)
/*     */     {
/*     */     }
/*     */ 
/*     */     public void mouseDragged(MouseEvent e)
/*     */     {
/* 207 */       updateColour(e.getX(), e.getY());
/*     */     }
/*     */ 
/*     */     public void mousePressed(MouseEvent e)
/*     */     {
/* 212 */       updateColour(e.getX(), e.getY());
/*     */     }
/*     */ 
/*     */     public void mouseMoved(MouseEvent e)
/*     */     {
/*     */     }
/*     */ 
/*     */     public void mouseClicked(MouseEvent e)
/*     */     {
/*     */     }
/*     */ 
/*     */     public void mouseEntered(MouseEvent e)
/*     */     {
/*     */     }
/*     */ 
/*     */     public void mouseExited(MouseEvent e)
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.ui.ColourPalette
 * JD-Core Version:    0.6.2
 */