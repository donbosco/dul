/*     */ package pelibskin;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.font.FontRenderContext;
/*     */ import java.awt.font.LineBreakMeasurer;
/*     */ import java.awt.font.TextAttribute;
/*     */ import java.awt.font.TextLayout;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import java.text.AttributedCharacterIterator;
/*     */ import java.text.AttributedString;
/*     */ 
/*     */ public class SkinnedLabel extends Widget
/*     */ {
/*     */   private Font font;
/*     */   private Color color;
/*     */   private String label;
/*     */   private int align;
/*     */   private int verticalAlign;
/*     */   private int spacing;
/*     */   private boolean offsetCached;
/*     */   private int offset;
/*     */   private boolean wrap;
/*     */   public static final int ALIGN_LEFT = 0;
/*     */   public static final int ALIGN_CENTER = 1;
/*     */   public static final int ALIGN_RIGHT = 2;
/*     */   public static final int ALIGN_TOP = 0;
/*     */   public static final int ALIGN_BOTTOM = 2;
/*     */ 
/*     */   public SkinnedLabel(String id, int x, int y, String label)
/*     */   {
/*  71 */     super(id, x, y);
/*     */ 
/*  73 */     this.label = label;
/*     */ 
/*  75 */     this.color = Color.black;
/*     */ 
/*  77 */     this.spacing = 2;
/*     */   }
/*     */ 
/*     */   public void setWrap(Dimension d)
/*     */   {
/*  87 */     this.wrap = (d != null);
/*     */ 
/*  89 */     this.width = d.width;
/*     */ 
/*  91 */     this.height = d.height;
/*     */   }
/*     */ 
/*     */   public void setAlignment(int align)
/*     */   {
/* 101 */     this.align = align;
/*     */ 
/* 103 */     this.offsetCached = false;
/*     */ 
/* 105 */     invalidate();
/*     */   }
/*     */ 
/*     */   public void setVerticalAlignment(int align)
/*     */   {
/* 115 */     this.verticalAlign = align;
/*     */ 
/* 117 */     invalidate();
/*     */   }
/*     */ 
/*     */   public void setFont(Font font)
/*     */   {
/* 127 */     this.font = font;
/*     */ 
/* 129 */     this.offsetCached = false;
/*     */ 
/* 131 */     invalidate();
/*     */   }
/*     */ 
/*     */   public void setLabel(String label)
/*     */   {
/* 141 */     this.label = label;
/*     */ 
/* 143 */     this.offsetCached = false;
/*     */ 
/* 145 */     invalidate();
/*     */   }
/*     */ 
/*     */   public String getLabel()
/*     */   {
/* 155 */     return this.label;
/*     */   }
/*     */ 
/*     */   public void setColor(Color c)
/*     */   {
/* 165 */     this.color = c;
/*     */ 
/* 167 */     invalidate();
/*     */   }
/*     */ 
/*     */   public Color getColor()
/*     */   {
/* 177 */     return this.color;
/*     */   }
/*     */ 
/*     */   public void paint(Graphics g)
/*     */   {
/* 187 */     if (this.wrap)
/*     */     {
/* 191 */       FontRenderContext frc = ((Graphics2D)g).getFontRenderContext();
/*     */ 
/* 193 */       AttributedString as = new AttributedString(this.label);
/*     */ 
/* 195 */       as.addAttribute(TextAttribute.FONT, this.font);
/*     */ 
/* 197 */       as.addAttribute(TextAttribute.FOREGROUND, this.color);
/*     */ 
/* 199 */       AttributedCharacterIterator aci = as.getIterator();
/*     */ 
/* 207 */       int h = 0;
/*     */ 
/* 209 */       if ((this.verticalAlign == 1) || (this.verticalAlign == 2))
/*     */       {
/* 213 */         LineBreakMeasurer measurer = new LineBreakMeasurer(aci, frc);
/*     */ 
/* 215 */         while (measurer.getPosition() < this.label.length())
/*     */         {
/* 219 */           TextLayout layout = measurer.nextLayout(this.width);
/*     */ 
/* 221 */           Rectangle2D bounds = layout.getBounds();
/*     */ 
/* 223 */           h = (int)(h + layout.getAscent());
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 233 */       aci = as.getIterator();
/*     */ 
/* 235 */       LineBreakMeasurer measurer = new LineBreakMeasurer(aci, frc);
/*     */ 
/* 237 */       int y = 0;
/*     */ 
/* 239 */       switch (this.verticalAlign)
/*     */       {
/*     */       case 1:
/* 245 */         y = this.height / 2 - h / 2;
/*     */ 
/* 247 */         break;
/*     */       case 2:
/* 253 */         y = this.height - h;
/*     */       }
/*     */ 
/* 261 */       while (measurer.getPosition() < this.label.length())
/*     */       {
/* 265 */         TextLayout layout = measurer.nextLayout(this.width);
/*     */ 
/* 267 */         Rectangle2D bounds = layout.getBounds();
/*     */ 
/* 269 */         y = (int)(y + layout.getAscent());
/*     */ 
/* 271 */         int offset = 0;
/*     */ 
/* 273 */         switch (this.align)
/*     */         {
/*     */         case 1:
/* 279 */           offset = (int)(this.width / 2 - bounds.getWidth() / 2.0D);
/*     */ 
/* 281 */           break;
/*     */         case 2:
/* 287 */           offset = (int)(this.width - bounds.getWidth());
/*     */         }
/*     */ 
/* 293 */         layout.draw((Graphics2D)g, offset, y);
/*     */ 
/* 295 */         y = (int)(y + (layout.getDescent() + layout.getLeading() + this.spacing));
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 305 */       g.setColor(this.color);
/*     */ 
/* 307 */       g.setFont(this.font);
/*     */ 
/* 309 */       if (!this.offsetCached)
/*     */       {
/* 313 */         FontRenderContext frc = ((Graphics2D)g).getFontRenderContext();
/*     */ 
/* 315 */         Rectangle2D rect = this.font.getStringBounds(this.label, frc);
/*     */ 
/* 317 */         switch (this.align)
/*     */         {
/*     */         case 0:
/* 323 */           this.offset = 0;
/*     */ 
/* 325 */           break;
/*     */         case 1:
/* 331 */           this.offset = ((int)-rect.getWidth() / 2);
/*     */ 
/* 333 */           break;
/*     */         case 2:
/* 339 */           this.offset = ((int)-rect.getWidth());
/*     */         }
/*     */ 
/* 345 */         this.offsetCached = true;
/*     */       }
/*     */ 
/* 351 */       g.drawString(this.label, this.offset, 0);
/*     */     }
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelibskin.SkinnedLabel
 * JD-Core Version:    0.6.2
 */