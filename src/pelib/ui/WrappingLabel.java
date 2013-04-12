/*     */ package pelib.ui;
/*     */ 
/*     */ import java.awt.Canvas;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class WrappingLabel extends Canvas
/*     */ {
/*     */   private static final int DEFAULT_WIDTH = 600;
/*     */   protected String text;
/*     */   protected float m_nHAlign;
/*     */   protected float m_nVAlign;
/*     */   protected int baseline;
/*     */   protected FontMetrics fm;
/*     */ 
/*     */   public WrappingLabel()
/*     */   {
/* 113 */     this("");
/*     */   }
/*     */ 
/*     */   public WrappingLabel(String s)
/*     */   {
/* 123 */     this(s, 0.0F, 0.0F);
/*     */   }
/*     */ 
/*     */   public WrappingLabel(String s, float nHorizontal, float nVertical)
/*     */   {
/* 133 */     setText(s);
/*     */ 
/* 135 */     setHAlignStyle(nHorizontal);
/*     */ 
/* 137 */     setVAlignStyle(nVertical);
/*     */   }
/*     */ 
/*     */   public float getHAlignStyle()
/*     */   {
/* 153 */     return this.m_nHAlign;
/*     */   }
/* 155 */   public float getVAlignStyle() { return this.m_nVAlign; } 
/*     */   public String getText() {
/* 157 */     return this.text;
/*     */   }
/*     */ 
/*     */   public void setHAlignStyle(float a)
/*     */   {
/* 165 */     this.m_nHAlign = a;
/*     */ 
/* 167 */     invalidate();
/*     */   }
/*     */ 
/*     */   public void setVAlignStyle(float a)
/*     */   {
/* 177 */     this.m_nVAlign = a;
/*     */ 
/* 179 */     invalidate();
/*     */   }
/*     */ 
/*     */   public void setText(String s)
/*     */   {
/* 189 */     this.text = s;
/*     */ 
/* 191 */     repaint();
/*     */   }
/*     */ 
/*     */   public String paramString()
/*     */   {
/* 211 */     return "";
/*     */   }
/*     */ 
/*     */   public Dimension getPreferredSize()
/*     */   {
/* 221 */     this.fm = getFontMetrics(getFont());
/*     */ 
/* 223 */     int height = this.fm.getHeight();
/*     */ 
/* 225 */     Vector lines = breakIntoLines(this.text, 600);
/*     */ 
/* 227 */     this.fm = null;
/*     */ 
/* 229 */     return new Dimension(600, (lines.size() + 1) * height);
/*     */   }
/*     */ 
/*     */   public void paint(Graphics g)
/*     */   {
/* 239 */     if (this.text != null)
/*     */     {
/* 245 */       int currentY = 0;
/*     */ 
/* 253 */       this.fm = getFontMetrics(getFont());
/*     */ 
/* 255 */       this.baseline = this.fm.getMaxAscent();
/*     */ 
/* 261 */       Dimension d = getSize();
/*     */ 
/* 265 */       Vector lines = breakIntoLines(this.text, d.width);
/*     */ 
/* 271 */       if (this.m_nVAlign == 0.5F)
/*     */       {
/* 275 */         int center = d.height / 2;
/*     */ 
/* 277 */         currentY = center - lines.size() / 2 * this.fm.getHeight();
/*     */       }
/* 283 */       else if (this.m_nVAlign == 1.0F)
/*     */       {
/* 287 */         currentY = d.height - lines.size() * this.fm.getHeight();
/*     */       }
/*     */ 
/* 295 */       Enumeration elements = lines.elements();
/*     */ 
/* 297 */       while (elements.hasMoreElements())
/*     */       {
/* 301 */         drawAlignedString(g, (String)elements.nextElement(), 0, currentY, d.width);
/*     */ 
/* 307 */         currentY += this.fm.getHeight();
/*     */       }
/*     */ 
/* 315 */       this.fm = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected Vector breakIntoLines(String s, int width)
/*     */   {
/* 329 */     int fromIndex = 0;
/*     */ 
/* 331 */     int pos = 0;
/*     */ 
/* 337 */     Vector lines = new Vector();
/*     */ 
/* 343 */     while (fromIndex != -1)
/*     */     {
/* 351 */       while ((fromIndex < this.text.length()) && (this.text.charAt(fromIndex) == ' '))
/*     */       {
/* 355 */         fromIndex++;
/*     */ 
/* 361 */         if (fromIndex >= this.text.length()) break;
/*     */ 
/*     */       }
/*     */ 
/* 369 */       pos = fromIndex;
/*     */ 
/* 371 */       int bestpos = -1;
/*     */ 
/* 373 */       String largestString = null;
/*     */ 
/* 377 */       while (pos >= fromIndex)
/*     */       {
/* 381 */         boolean bHardNewline = false;
/*     */ 
/* 383 */         int newlinePos = this.text.indexOf(10, pos);
/*     */ 
/* 385 */         int spacePos = this.text.indexOf(32, pos);
/*     */ 
/* 389 */         if ((newlinePos != -1) && ((spacePos == -1) || ((spacePos != -1) && (newlinePos < spacePos))))
/*     */         {
/* 401 */           pos = newlinePos;
/*     */ 
/* 403 */           bHardNewline = true;
/*     */         }
/*     */         else
/*     */         {
/* 411 */           pos = spacePos;
/*     */ 
/* 413 */           bHardNewline = false;
/*     */         }
/*     */ 
/* 421 */         if (pos == -1)
/*     */         {
/* 425 */           s = this.text.substring(fromIndex);
/*     */         }
/*     */         else
/*     */         {
/* 433 */           s = this.text.substring(fromIndex, pos);
/*     */         }
/*     */ 
/* 441 */         if (this.fm.stringWidth(s) >= width)
/*     */         {
/*     */           break;
/*     */         }
/* 445 */         largestString = s;
/*     */ 
/* 447 */         bestpos = pos;
/*     */ 
/* 455 */         if (bHardNewline)
/*     */         {
/* 457 */           bestpos++;
/*     */         }
/* 459 */         if ((pos == -1) || (bHardNewline))
/*     */         {
/*     */           break;
/*     */         }
/*     */ 
/* 473 */         pos++;
/*     */       }
/*     */ 
/* 479 */       if (largestString == null)
/*     */       {
/* 491 */         int totalWidth = 0;
/*     */ 
/* 493 */         int oneCharWidth = 0;
/*     */ 
/* 497 */         pos = fromIndex;
/*     */ 
/* 501 */         while (pos < this.text.length())
/*     */         {
/* 505 */           oneCharWidth = this.fm.charWidth(this.text.charAt(pos));
/*     */ 
/* 507 */           if (totalWidth + oneCharWidth >= width)
/*     */             break;
/* 509 */           totalWidth += oneCharWidth;
/*     */ 
/* 511 */           pos++;
/*     */         }
/*     */ 
/* 517 */         lines.addElement(this.text.substring(fromIndex, pos));
/*     */ 
/* 519 */         fromIndex = pos;
/*     */       }
/*     */       else
/*     */       {
/* 527 */         lines.addElement(largestString);
/*     */ 
/* 529 */         fromIndex = bestpos;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 537 */     return lines;
/*     */   }
/*     */ 
/*     */   protected void drawAlignedString(Graphics g, String s, int x, int y, int width)
/*     */   {
/* 557 */     int drawx = x;
/*     */ 
/* 559 */     int drawy = y + this.baseline;
/*     */ 
/* 563 */     if (this.m_nHAlign != 0.0F)
/*     */     {
/* 571 */       int sw = this.fm.stringWidth(s);
/*     */ 
/* 575 */       if (this.m_nHAlign == 0.5F)
/*     */       {
/* 579 */         drawx += (width - sw) / 2;
/*     */       }
/* 583 */       else if (this.m_nHAlign == 1.0F)
/*     */       {
/* 587 */         drawx = drawx + width - sw;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 595 */     g.drawString(s, drawx, drawy);
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.ui.WrappingLabel
 * JD-Core Version:    0.6.2
 */