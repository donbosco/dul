/*     */ package duluxskin;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Point;
/*     */ import java.awt.TextField;
/*     */ import java.awt.event.KeyAdapter;
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.awt.font.FontRenderContext;
/*     */ import java.awt.font.LineBreakMeasurer;
/*     */ import java.awt.font.TextAttribute;
/*     */ import java.awt.font.TextLayout;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import java.text.AttributedCharacterIterator;
/*     */ import java.text.AttributedString;
/*     */ import java.util.Iterator;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class SkinnedLabel extends Widget
/*     */   implements Cloneable
/*     */ {
/*     */   private Boolean hasBoarder;
/*     */   private Font font;
/*     */   private Color color;
/*     */   private String label;
/*     */   private int align;
/*     */   private int verticalAlign;
/*     */   private int spacing;
/*     */   private Vector labelListeners;
/*     */   private boolean offsetCached;
/*     */   private int offset;
/*     */   private boolean wrap;
/*     */   private boolean editable;
/*     */   private TextField editField;
/*     */   public static final int ALIGN_LEFT = 0;
/*     */   public static final int ALIGN_CENTER = 1;
/*     */   public static final int ALIGN_RIGHT = 2;
/*     */   public static final int ALIGN_TOP = 0;
/*     */   public static final int ALIGN_BOTTOM = 2;
/*     */ 
/*     */   public SkinnedLabel(String id, int x, int y, String label)
/*     */   {
/*  44 */     super(id, x, y);
/*     */ 
/*  46 */     this.label = label;
/*     */ 
/*  48 */     this.color = Color.black;
/*     */ 
/*  50 */     this.spacing = 2;
/*     */ 
/*  52 */     this.labelListeners = new Vector();
/*     */   }
/*     */ 
/*     */   public void setEditable(boolean editable)
/*     */   {
/*  57 */     this.editable = editable;
/*     */   }
/*     */   public boolean getEditable() {
/*  60 */     return this.editable;
/*     */   }
/*     */   public void setHasBoarder(boolean hasBoarder) {
/*  63 */     this.hasBoarder = Boolean.valueOf(hasBoarder);
/*     */   }
/*     */   public boolean getHasBoarder() {
/*  66 */     return this.hasBoarder.booleanValue();
/*     */   }
/*     */ 
/*     */   protected boolean firstChanceMouseEvent(MouseEvent e)
/*     */   {
/*  81 */     if (!this.enabled) {
/*  82 */       e.consume();
/*  83 */       return true;
/*     */     }
/*     */ 
/*  87 */     switch (e.getID())
/*     */     {
/*     */     case 501:
/*  91 */       if ((getItemClicked(e.getPoint())) && (this.editable)) {
/*  92 */         notifyItemSelected();
/*     */       }
/*     */ 
/*     */       break;
/*     */     }
/*     */ 
/*  98 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean getItemClicked(Point p)
/*     */   {
/* 107 */     return true;
/*     */   }
/*     */ 
/*     */   public void beginEditing()
/*     */   {
/* 112 */     setCapture();
/*     */ 
/* 116 */     this.editField = new TextField();
/*     */ 
/* 118 */     this.layer.getSkin().add(this.editField);
/*     */ 
/* 120 */     this.editField.setFont(this.font);
/*     */ 
/* 122 */     this.editField.setLocation(this.x, this.y);
/*     */ 
/* 124 */     this.editField.setSize(this.width, this.height);
/*     */ 
/* 126 */     this.editField.setText(this.label);
/*     */ 
/* 128 */     this.editField.setSelectionStart(0);
/*     */ 
/* 130 */     this.editField.setSelectionEnd(this.label.length());
/*     */ 
/* 132 */     this.editField.requestFocus();
/*     */ 
/* 134 */     this.editField.addKeyListener(new KeyAdapter()
/*     */     {
/*     */       public void keyPressed(KeyEvent e)
/*     */       {
/* 138 */         if (e.getKeyCode() == 10)
/* 139 */           SkinnedLabel.this.completeEditing();
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   public void startEditing()
/*     */   {
/* 154 */     this.editField = new TextField();
/*     */ 
/* 156 */     this.layer.getSkin().add(this.editField);
/*     */ 
/* 158 */     this.editField.setFont(this.font);
/*     */ 
/* 160 */     this.editField.setLocation(this.x, this.y);
/*     */ 
/* 162 */     this.editField.setSize(this.width, this.height);
/*     */ 
/* 164 */     this.editField.setText(this.label);
/*     */ 
/* 166 */     this.editField.setSelectionStart(0);
/*     */ 
/* 168 */     this.editField.setSelectionEnd(this.label.length());
/*     */ 
/* 170 */     this.editField.requestFocus();
/*     */   }
/*     */ 
/*     */   public void setVisible(boolean v)
/*     */   {
/* 188 */     this.visible = v;
/*     */   }
/*     */ 
/*     */   public void completeEditing()
/*     */   {
/* 195 */     releaseCapture();
/*     */ 
/* 197 */     this.layer.getSkin().remove(this.editField);
/*     */ 
/* 199 */     notifyItemRenamed(this, this.editField.getText());
/*     */ 
/* 201 */     this.editField = null;
/*     */   }
/*     */ 
/*     */   private void notifyItemRenamed(SkinnedLabel currentLabel, String text) {
/* 205 */     LabelEvent e = new LabelEvent(this, 1);
/*     */ 
/* 207 */     for (Iterator it = this.labelListeners.iterator(); it.hasNext(); )
/* 208 */       ((LabelListener)it.next()).itemRenamed(e, this, text);
/*     */   }
/*     */ 
/*     */   private void notifyItemSelected()
/*     */   {
/* 213 */     LabelEvent e = new LabelEvent(this, 2);
/* 214 */     for (Iterator it = this.labelListeners.iterator(); it.hasNext(); )
/* 215 */       ((LabelListener)it.next()).itemSelected(e);
/*     */   }
/*     */ 
/*     */   public void addLabelListener(LabelListener l)
/*     */   {
/* 220 */     this.labelListeners.add(l);
/*     */   }
/*     */ 
/*     */   public void setWrap(Dimension d)
/*     */   {
/* 225 */     this.wrap = (d != null);
/*     */ 
/* 227 */     this.width = d.width;
/*     */ 
/* 229 */     this.height = d.height;
/*     */   }
/*     */ 
/*     */   public void setAlignment(int align)
/*     */   {
/* 235 */     this.align = align;
/*     */ 
/* 237 */     this.offsetCached = false;
/*     */ 
/* 239 */     invalidate();
/*     */   }
/*     */ 
/*     */   public void setVerticalAlignment(int align)
/*     */   {
/* 245 */     this.verticalAlign = align;
/*     */ 
/* 247 */     invalidate();
/*     */   }
/*     */ 
/*     */   public void setFont(Font font)
/*     */   {
/* 253 */     this.font = font;
/*     */ 
/* 255 */     this.offsetCached = false;
/*     */ 
/* 257 */     invalidate();
/*     */   }
/*     */ 
/*     */   public void setLabel(String label)
/*     */   {
/* 263 */     this.label = label;
/*     */ 
/* 265 */     this.offsetCached = false;
/*     */ 
/* 267 */     invalidate();
/*     */   }
/*     */ 
/*     */   public String getLabel()
/*     */   {
/* 273 */     return this.label;
/*     */   }
/*     */ 
/*     */   public void setColor(Color c)
/*     */   {
/* 279 */     this.color = c;
/*     */ 
/* 281 */     invalidate();
/*     */   }
/*     */ 
/*     */   public Color getColor()
/*     */   {
/* 287 */     return this.color;
/*     */   }
/*     */ 
/*     */   public void paint(Graphics g)
/*     */   {
/* 293 */     if (this.wrap)
/*     */     {
/* 295 */       FontRenderContext frc = ((Graphics2D)g).getFontRenderContext();
/*     */ 
/* 297 */       AttributedString as = new AttributedString(this.label);
/*     */ 
/* 299 */       as.addAttribute(TextAttribute.FONT, this.font);
/*     */ 
/* 301 */       as.addAttribute(TextAttribute.FOREGROUND, this.color);
/*     */ 
/* 303 */       AttributedCharacterIterator aci = as.getIterator();
/*     */ 
/* 309 */       int h = 0;
/*     */ 
/* 311 */       if ((this.verticalAlign == 1) || (this.verticalAlign == 2))
/*     */       {
/* 313 */         LineBreakMeasurer measurer = new LineBreakMeasurer(aci, frc);
/*     */ 
/* 315 */         while (measurer.getPosition() < this.label.length())
/*     */         {
/* 317 */           TextLayout layout = measurer.nextLayout(this.width);
/*     */ 
/* 319 */           Rectangle2D bounds = layout.getBounds();
/*     */ 
/* 321 */           h = (int)(h + layout.getAscent());
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 329 */       aci = as.getIterator();
/*     */ 
/* 331 */       LineBreakMeasurer measurer = new LineBreakMeasurer(aci, frc);
/*     */ 
/* 333 */       int y = 0;
/*     */ 
/* 335 */       switch (this.verticalAlign)
/*     */       {
/*     */       case 1:
/* 339 */         y = this.height / 2 - h / 2;
/*     */ 
/* 341 */         break;
/*     */       case 2:
/* 345 */         y = this.height - h;
/*     */       }
/*     */ 
/* 352 */       while (measurer.getPosition() < this.label.length())
/*     */       {
/* 354 */         TextLayout layout = measurer.nextLayout(this.width);
/*     */ 
/* 356 */         Rectangle2D bounds = layout.getBounds();
/*     */ 
/* 358 */         y = (int)(y + layout.getAscent());
/*     */ 
/* 360 */         int offset = 0;
/*     */ 
/* 362 */         switch (this.align)
/*     */         {
/*     */         case 1:
/* 366 */           offset = (int)(this.width / 2 - bounds.getWidth() / 2.0D);
/*     */ 
/* 368 */           break;
/*     */         case 2:
/* 371 */           offset = (int)(this.width - bounds.getWidth());
/*     */         }
/*     */ 
/* 376 */         layout.draw((Graphics2D)g, offset, y);
/*     */ 
/* 378 */         y = (int)(y + (layout.getDescent() + layout.getLeading() + this.spacing));
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 383 */       g.setColor(this.color);
/*     */ 
/* 385 */       g.setFont(this.font);
/*     */ 
/* 387 */       if (!this.offsetCached)
/*     */       {
/* 389 */         FontRenderContext frc = ((Graphics2D)g).getFontRenderContext();
/*     */ 
/* 391 */         Rectangle2D rect = this.font.getStringBounds(this.label, frc);
/*     */ 
/* 393 */         switch (this.align)
/*     */         {
/*     */         case 0:
/* 397 */           this.offset = 0;
/*     */ 
/* 399 */           break;
/*     */         case 1:
/* 405 */           this.offset = ((int)-rect.getWidth() / 2);
/*     */ 
/* 407 */           break;
/*     */         case 2:
/* 410 */           this.offset = ((int)-rect.getWidth());
/*     */         }
/*     */ 
/* 415 */         this.offsetCached = true;
/*     */       }
/*     */ 
/* 418 */       g.drawString(this.label, this.offset, 0);
/*     */     }
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     duluxskin.SkinnedLabel
 * JD-Core Version:    0.6.2
 */