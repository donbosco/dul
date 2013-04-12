/*     */ package dulux;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Image;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.Vector;
/*     */ import pelib.Colour;
/*     */ import pelib.ImageColour;
/*     */ import pelib.Mask;
/*     */ import pelib.PaintExplorer;
/*     */ import pelib.print.DefaultPrintContentProvider;
/*     */ 
/*     */ public class DuluxPrintContentProvider extends DefaultPrintContentProvider
/*     */ {
/*     */   private PaintExplorer explorer;
/*     */   private String filename;
/*     */   private Mask[] masks;
/*     */   private Map colours;
/*     */   private Map chips;
/*     */ 
/*     */   public DuluxPrintContentProvider(PaintExplorer explorer)
/*     */   {
/*  53 */     this.explorer = explorer;
/*     */ 
/*  55 */     this.filename = explorer.getFilename();
/*     */ 
/*  57 */     Vector maskVector = new Vector();
/*     */ 
/*  59 */     for (Iterator it = explorer.getMasks(); it.hasNext(); )
/*     */     {
/*  61 */       maskVector.add(it.next());
/*     */     }
/*     */ 
/*  65 */     this.masks = ((Mask[])maskVector.toArray(new Mask[0]));
/*     */ 
/*  69 */     createColourMap();
/*     */   }
/*     */ 
/*     */   private void createColourMap()
/*     */   {
/*  93 */     this.colours = new HashMap();
/*     */ 
/*  95 */     this.chips = new HashMap();
/*     */     try
/*     */     {
/*  99 */       InputStream is = getClass().getResourceAsStream("/colour.csv");
/*     */ 
/* 101 */       BufferedReader in = new BufferedReader(new InputStreamReader(is));
/*     */       String line;
/* 111 */       while ((line = in.readLine()) != null)
/*     */       {
/* 115 */         StringTokenizer toks = new StringTokenizer(line, ",");
/*     */         try
/*     */         {
/* 121 */           Integer chip = Integer.decode(toks.nextToken());
/*     */ 
/* 123 */           Long colour = Long.decode(toks.nextToken());
/*     */ 
/* 125 */           long col = colour.longValue();
/*     */ 
/* 131 */           col = (col & 0xFF) << 24 | (col >> 8 & 0xFF) << 16 | (col >> 16 & 0xFF) << 8 | col >> 24 & 0xFF;
/*     */ 
/* 141 */           String name = toks.nextToken();
/*     */ 
/* 145 */           this.colours.put(new Integer((int)col), name);
/*     */ 
/* 147 */           this.chips.put(new Integer((int)col), chip);
/*     */         }
/*     */         catch (NoSuchElementException e)
/*     */         {
/*     */         }
/*     */         catch (NumberFormatException e)
/*     */         {
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean provideContentVisible(String id)
/*     */   {
/* 169 */     if ((id.startsWith("mask")) && (parseMaskId(id) == null))
/*     */     {
/* 171 */       return false;
/*     */     }
/*     */ 
/* 175 */     return true;
/*     */   }
/*     */ 
/*     */   public String provideContentText(String id)
/*     */   {
/* 185 */     if (id.equals("imageFilename"))
/*     */     {
/* 187 */       return this.filename;
/*     */     }
/*     */ 
/* 191 */     if (id.endsWith("ColourName"))
/*     */     {
/* 195 */       Mask m = parseMaskId(id);
/*     */ 
/* 197 */       if (m != null)
/*     */       {
/* 201 */         int col = m.getColour();
/*     */ 
/* 203 */         String name = (String)this.colours.get(new Integer(col));
/*     */ 
/* 205 */         if (name == null)
/*     */         {
/* 207 */           return "COLOUR #" + Integer.toString(col, 16);
/*     */         }
/*     */ 
/* 211 */         return name.toUpperCase();
/*     */       }
/*     */ 
/*     */     }
/* 217 */     else if (id.endsWith("ChipId"))
/*     */     {
/* 221 */       Mask m = parseMaskId(id);
/*     */ 
/* 223 */       if (m != null)
/*     */       {
/* 227 */         int col = m.getColour();
/*     */ 
/* 229 */         Integer chip = (Integer)this.chips.get(new Integer(col));
/*     */ 
/* 231 */         if (chip == null)
/*     */         {
/* 233 */           return "??";
/*     */         }
/*     */ 
/* 237 */         return chip.toString();
/*     */       }
/*     */ 
/*     */     }
/* 243 */     else if (id.endsWith("SurfaceName"))
/*     */     {
/* 247 */       Mask m = parseMaskId(id);
/*     */ 
/* 249 */       if (m != null)
/*     */       {
/* 253 */         String name = "";
/*     */ 
/* 255 */         if (m.getUserData() != null)
/*     */         {
/* 257 */           name = m.getUserData().toString();
/*     */         }
/* 259 */         return name;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 267 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean provideContentGraphics(String id, Graphics2D g, int x, int y, int width, int height)
/*     */   {
/* 287 */     if (id.equals("paintedImage"))
/*     */     {
/* 291 */       Image img = this.explorer.getPaintedImage().getAWTImage();
/*     */ 
/* 295 */       float imgAspect = img.getWidth(null) / img.getHeight(null);
/*     */ 
/* 297 */       float pageAspect = width / height;
/*     */ 
/* 299 */       int drawWidth = width;
/*     */ 
/* 301 */       int drawHeight = height;
/*     */ 
/* 303 */       if (imgAspect < pageAspect)
/*     */       {
/* 305 */         drawWidth = (int)(height * imgAspect);
/*     */       }
/*     */       else
/*     */       {
/* 309 */         drawHeight = (int)(width / imgAspect);
/*     */       }
/*     */ 
/* 313 */       g.drawImage(img, x, y, drawWidth, drawHeight, null);
/*     */ 
/* 317 */       return true;
/*     */     }
/*     */ 
/* 321 */     return false;
/*     */   }
/*     */ 
/*     */   public Color provideContentFillColor(String id)
/*     */   {
/* 331 */     if (!id.endsWith("Colour"))
/*     */     {
/* 333 */       return null;
/*     */     }
/*     */ 
/* 337 */     Mask mask = parseMaskId(id);
/*     */ 
/* 339 */     if (mask != null)
/*     */     {
/* 343 */       return Colour.getAWTColor(mask.getColour());
/*     */     }
/*     */ 
/* 349 */     return null;
/*     */   }
/*     */ 
/*     */   public Color provideContentStrokeColor(String id)
/*     */   {
/* 359 */     return null;
/*     */   }
/*     */ 
/*     */   private Mask parseMaskId(String id)
/*     */   {
/* 369 */     if (!id.startsWith("mask"))
/*     */     {
/* 371 */       return null;
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 377 */       String num = id.replaceAll("[^0-9]", "");
/*     */ 
/* 379 */       int idx = Integer.parseInt(num) - 1;
/*     */ 
/* 381 */       if (idx < this.masks.length)
/*     */       {
/* 383 */         return this.masks[idx];
/*     */       }
/*     */ 
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 389 */       return null;
/*     */     }
/*     */ 
/* 395 */     return null;
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     dulux.DuluxPrintContentProvider
 * JD-Core Version:    0.6.2
 */