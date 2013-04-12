/*     */ package pelib.print;
/*     */ 
/*     */ import java.awt.BasicStroke;
/*     */ import java.awt.Canvas;
/*     */ import java.awt.Color;
/*     */ import java.awt.Font;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Image;
/*     */ import java.awt.MediaTracker;
/*     */ import java.awt.Shape;
/*     */ import java.awt.Stroke;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.font.FontRenderContext;
/*     */ import java.awt.geom.AffineTransform;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import java.awt.print.Book;
/*     */ import java.awt.print.PageFormat;
/*     */ import java.awt.print.Printable;
/*     */ import java.awt.print.PrinterException;
/*     */ import java.awt.print.PrinterJob;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Properties;
/*     */ import java.util.Stack;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public class SVGPrintable
/*     */   implements Printable
/*     */ {
/*     */   private Document doc;
/*     */   private Stack transforms;
/*     */   private String basePath;
/*     */   private PrintContentProvider provider;
/*     */   private Color providedFillColor;
/*     */   private Color providedStrokeColor;
/*     */   private String providedText;
/*     */   private static final int TEXT_ALIGN_LEFT = 0;
/*     */   private static final int TEXT_ALIGN_CENTER = 1;
/*     */   private static final int TEXT_ALIGN_RIGHT = 2;
/*     */ 
/*     */   public SVGPrintable(String resourceTemplate)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/*  48 */       InputStream is = getClass().getResourceAsStream(resourceTemplate);
/*     */ 
/*  50 */       DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
/*     */ 
/*  52 */       DocumentBuilder builder = factory.newDocumentBuilder();
/*  53 */       this.doc = builder.parse(is);
/*  54 */       this.basePath = new File(resourceTemplate).getParent().replace('\\', '/');
/*     */ 
/*  56 */       if (this.basePath == null)
/*  57 */         this.basePath = "";
/*  58 */       this.provider = new DefaultPrintContentProvider();
/*     */     } catch (ParserConfigurationException e) {
/*     */     }
/*     */     catch (SAXException e) {
/*     */     }
/*  63 */     this.transforms = new Stack();
/*     */   }
/*     */ 
/*     */   public void setContentProvider(PrintContentProvider provider)
/*     */   {
/*  68 */     this.provider = provider;
/*     */   }
/*     */ 
/*     */   public int print(Graphics graphics, PageFormat format, int page)
/*     */   {
/*  76 */     if (page != 0)
/*  77 */       return 1;
/*     */     try
/*     */     {
/*  80 */       scaleGraphics(graphics, format);
/*  81 */       drawNode(this.doc.getDocumentElement(), (Graphics2D)graphics);
/*  82 */       restoreGraphics(graphics);
/*     */     }
/*     */     catch (NumberFormatException e) {
/*  85 */       e.printStackTrace();
/*     */     }
/*     */ 
/*  88 */     return 0;
/*     */   }
/*     */ 
/*     */   private void drawNode(Node node, Graphics2D g)
/*     */   {
/*  96 */     if (node.getNodeType() != 1)
/*  97 */       return;
/*  98 */     Element elem = (Element)node;
/*     */ 
/* 100 */     Color oldProvidedFillColor = this.providedFillColor;
/* 101 */     Color oldProvidedStrokeColor = this.providedStrokeColor;
/* 102 */     String oldProvidedText = this.providedText;
/*     */ 
/* 105 */     if (elem.hasAttribute("id"))
/*     */     {
/* 107 */       String id = elem.getAttribute("id");
/* 108 */       if (!this.provider.provideContentVisible(id))
/* 109 */         return;
/* 110 */       this.providedFillColor = this.provider.provideContentFillColor(id);
/* 111 */       this.providedStrokeColor = this.provider.provideContentStrokeColor(id);
/* 112 */       this.providedText = this.provider.provideContentText(id);
/*     */     }
/*     */ 
/* 115 */     if (node.getNodeName().equals("rect"))
/* 116 */       drawRectElement(elem, g);
/* 117 */     else if (node.getNodeName().equals("path"))
/* 118 */       drawPathElement(elem, g);
/* 119 */     else if (node.getNodeName().equals("image"))
/* 120 */       drawImageElement(elem, g);
/* 121 */     else if (node.getNodeName().equals("text"))
/* 122 */       drawTextElement(elem, g);
/*     */     else {
/* 124 */       drawChildren(elem, g);
/*     */     }
/* 126 */     this.providedFillColor = oldProvidedFillColor;
/* 127 */     this.providedStrokeColor = oldProvidedStrokeColor;
/* 128 */     this.providedText = oldProvidedText;
/*     */   }
/*     */ 
/*     */   private void drawChildren(Node node, Graphics2D g)
/*     */   {
/* 133 */     NodeList nodes = node.getChildNodes();
/* 134 */     int count = nodes.getLength();
/* 135 */     for (int i = 0; i < count; i++)
/* 136 */       drawNode(nodes.item(i), g);
/*     */   }
/*     */ 
/*     */   private void drawTextChildren(Node node, Graphics2D g, double x, double y, int align)
/*     */   {
/* 142 */     NodeList nodes = node.getChildNodes();
/* 143 */     int count = nodes.getLength();
/* 144 */     for (int i = 0; i < count; i++)
/* 145 */       drawTextNode(nodes.item(i), g, x, y, align);
/*     */   }
/*     */ 
/*     */   private void drawRectElement(Element elem, Graphics2D g)
/*     */   {
/*     */     try {
/* 151 */       double x = parseDouble(elem.getAttribute("x"));
/* 152 */       double y = parseDouble(elem.getAttribute("y"));
/* 153 */       double width = parseDouble(elem.getAttribute("width"));
/* 154 */       double height = parseDouble(elem.getAttribute("height"));
/*     */ 
/* 156 */       double rx = 0.0D;
/* 157 */       if (elem.hasAttribute("rx"))
/* 158 */         rx = parseDouble(elem.getAttribute("rx"));
/* 159 */       double ry = 0.0D;
/* 160 */       if (elem.hasAttribute("ry"))
/* 161 */         ry = parseDouble(elem.getAttribute("ry"));
/* 162 */       Properties style = parseStyle(elem.getAttribute("style"));
/*     */ 
/* 164 */       if (setFillColor(style, g))
/*     */       {
/* 166 */         if ((rx == 0.0D) && (ry == 0.0D))
/* 167 */           g.fillRect((int)x, (int)y, (int)width, (int)height);
/*     */         else {
/* 169 */           g.fillRoundRect((int)x, (int)y, (int)width, (int)height, (int)rx, (int)ry);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 174 */       applyStroke(style, g);
/* 175 */       if (setStrokeColor(style, g))
/*     */       {
/* 177 */         if ((rx == 0.0D) && (ry == 0.0D))
/* 178 */           g.drawRect((int)x, (int)y, (int)width, (int)height);
/*     */         else {
/* 180 */           g.drawRoundRect((int)x, (int)y, (int)width, (int)height, (int)rx, (int)ry);
/*     */         }
/*     */       }
/*     */ 
/* 184 */       restoreStroke(g);
/*     */     }
/*     */     catch (NumberFormatException e) {
/*     */     }
/*     */   }
/*     */ 
/*     */   private void drawPathElement(Element elem, Graphics2D g) {
/* 191 */     String data = elem.getAttribute("d");
/* 192 */     Properties style = parseStyle(elem.getAttribute("style"));
/* 193 */     Color fillCol = parseColor(style.getProperty("fill"));
/* 194 */     Shape path = new SVGPath(data).getPath();
/*     */ 
/* 196 */     if (fillCol != null)
/*     */     {
/* 198 */       g.setColor(fillCol);
/* 199 */       g.fill(path);
/*     */     }
/*     */ 
/* 202 */     applyStroke(style, g);
/* 203 */     Color strokeCol = parseColor(style.getProperty("stroke"));
/* 204 */     if (strokeCol != null)
/*     */     {
/* 206 */       g.setColor(strokeCol);
/* 207 */       g.draw(path);
/*     */     }
/* 209 */     restoreStroke(g);
/*     */   }
/*     */ 
/*     */   private void drawImageElement(Element elem, Graphics2D g)
/*     */   {
/*     */     try {
/* 215 */       double x = parseDouble(elem.getAttribute("x"));
/* 216 */       double y = parseDouble(elem.getAttribute("y"));
/* 217 */       double width = parseDouble(elem.getAttribute("width"));
/* 218 */       double height = parseDouble(elem.getAttribute("height"));
/*     */ 
/* 220 */       if ((elem.hasAttribute("id")) && (this.provider.provideContentGraphics(elem.getAttribute("id"), g, (int)x, (int)y, (int)width, (int)height)))
/*     */       {
/* 227 */         return;
/*     */       }
/* 229 */       String filename = this.basePath + elem.getAttribute("xlink:href");
/* 230 */       InputStream is = getClass().getResourceAsStream(filename);
/* 231 */       ByteArrayOutputStream bs = new ByteArrayOutputStream();
/*     */ 
/* 233 */       byte[] buf = new byte[8192];
/*     */       int len;
/* 234 */       while ((len = is.read(buf, 0, buf.length)) > 0)
/* 235 */         bs.write(buf, 0, len);
/* 236 */       Image img = Toolkit.getDefaultToolkit().createImage(bs.toByteArray());
/*     */ 
/* 239 */       MediaTracker tracker = new MediaTracker(new Canvas());
/* 240 */       tracker.addImage(img, 1);
/* 241 */       tracker.waitForAll();
/*     */ 
/* 243 */       g.drawImage(img, (int)x, (int)y, (int)width, (int)height, null);
/*     */     } catch (Exception e) {
/* 245 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void drawTextElement(Element elem, Graphics2D g)
/*     */   {
/*     */     try {
/* 252 */       double x = parseDouble(elem.getAttribute("x"));
/* 253 */       double y = parseDouble(elem.getAttribute("y"));
/* 254 */       Properties style = parseStyle(elem.getAttribute("style"));
/* 255 */       String alignString = style.getProperty("text-align");
/* 256 */       int align = 0;
/* 257 */       if (alignString != null)
/*     */       {
/* 259 */         if (alignString.equals("center"))
/* 260 */           align = 1;
/* 261 */         else if (alignString.equals("right"))
/* 262 */           align = 2;
/*     */       }
/* 264 */       Font font = createFont(style);
/* 265 */       g.setFont(font);
/*     */ 
/* 267 */       String oldProvidedText = this.providedText;
/* 268 */       if (elem.hasAttribute("id"))
/*     */       {
/* 270 */         String id = elem.getAttribute("id");
/* 271 */         this.providedText = this.provider.provideContentText(id);
/*     */       }
/*     */ 
/* 274 */       drawTextChildren(elem, g, x, y, align);
/*     */ 
/* 276 */       this.providedText = oldProvidedText;
/*     */     }
/*     */     catch (NumberFormatException e)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   private void drawTextNode(Node node, Graphics2D g, double x, double y, int align) {
/* 284 */     if (node.getNodeType() == 1)
/*     */     {
/* 286 */       if (node.getNodeName().equals("tspan"))
/*     */         try
/*     */         {
/* 289 */           x = parseDouble(((Element)node).getAttribute("x"));
/* 290 */           y = parseDouble(((Element)node).getAttribute("y"));
/* 291 */           drawTextChildren(node, g, x, y, align);
/*     */         }
/*     */         catch (NumberFormatException e) {
/*     */         }
/*     */     }
/* 296 */     else if (node.getNodeType() == 3)
/*     */     {
/* 298 */       String text = this.providedText;
/* 299 */       if (text == null) {
/* 300 */         text = node.getNodeValue();
/*     */       }
/* 302 */       FontRenderContext frc = g.getFontRenderContext();
/* 303 */       Rectangle2D rect = g.getFont().getStringBounds(text, frc);
/*     */ 
/* 305 */       switch (align)
/*     */       {
/*     */       case 1:
/* 308 */         x -= (int)rect.getWidth() / 2;
/* 309 */         break;
/*     */       case 2:
/* 311 */         x -= (int)rect.getWidth();
/*     */       }
/*     */ 
/* 314 */       g.drawString(text, (int)x, (int)y);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void scaleGraphics(Graphics graphics, PageFormat format)
/*     */     throws NumberFormatException
/*     */   {
/* 325 */     Graphics2D g = (Graphics2D)graphics;
/*     */ 
/* 327 */     Element root = this.doc.getDocumentElement();
/* 328 */     double templateWidth = parseDouble(root.getAttribute("width"));
/* 329 */     double templateHeight = parseDouble(root.getAttribute("height"));
/*     */ 
/* 331 */     double finalWidth = format.getImageableWidth();
/* 332 */     double finalHeight = format.getImageableHeight();
/* 333 */     if (finalWidth / templateWidth > finalHeight / templateHeight)
/* 334 */       finalWidth = templateWidth * finalHeight / templateHeight;
/*     */     else {
/* 336 */       finalHeight = templateHeight * finalWidth / templateWidth;
/*     */     }
/* 338 */     this.transforms.push(g.getTransform());
/*     */ 
/* 340 */     g.translate(format.getImageableX(), format.getImageableY());
/* 341 */     g.scale(finalWidth / templateWidth, finalHeight / templateHeight);
/*     */   }
/*     */ 
/*     */   private void restoreGraphics(Graphics graphics)
/*     */   {
/* 347 */     Graphics2D g = (Graphics2D)graphics;
/*     */ 
/* 349 */     g.setTransform((AffineTransform)this.transforms.pop());
/*     */   }
/*     */ 
/*     */   private Properties parseStyle(String style)
/*     */   {
/* 354 */     Properties props = new Properties();
/* 355 */     String[] items = style.split(";");
/* 356 */     for (int i = 0; i < items.length; i++)
/*     */     {
/* 358 */       if (items[i].indexOf(':') != -1)
/*     */       {
/* 360 */         String[] pair = items[i].split(":");
/* 361 */         props.put(pair[0], pair[1]);
/*     */       }
/*     */     }
/* 364 */     return props;
/*     */   }
/*     */ 
/*     */   private Color parseColor(String value)
/*     */   {
/*     */     try
/*     */     {
/* 371 */       int rgb = Integer.parseInt(value.substring(1), 16);
/* 372 */       return new Color(rgb);
/*     */     } catch (NumberFormatException e) {
/*     */     }
/* 375 */     return null;
/*     */   }
/*     */ 
/*     */   private void applyStroke(Properties style, Graphics2D g)
/*     */   {
/*     */     try {
/* 381 */       double width = parseDouble(style.getProperty("stroke-width", ""));
/*     */ 
/* 383 */       this.transforms.push(g.getStroke());
/* 384 */       g.setStroke(new BasicStroke((float)width));
/*     */     }
/*     */     catch (NumberFormatException e) {
/*     */     }
/*     */   }
/*     */ 
/*     */   private boolean setFillColor(Properties style, Graphics2D g) {
/* 391 */     Color col = null;
/* 392 */     if (this.providedFillColor != null)
/* 393 */       col = this.providedFillColor;
/*     */     else
/* 395 */       col = parseColor(style.getProperty("fill"));
/* 396 */     if (col != null)
/*     */     {
/* 398 */       g.setColor(col);
/* 399 */       return true;
/*     */     }
/* 401 */     return false;
/*     */   }
/*     */ 
/*     */   private boolean setStrokeColor(Properties style, Graphics2D g)
/*     */   {
/* 406 */     Color col = null;
/* 407 */     if (this.providedStrokeColor != null)
/* 408 */       col = this.providedStrokeColor;
/*     */     else
/* 410 */       col = parseColor(style.getProperty("stroke"));
/* 411 */     if (col != null)
/*     */     {
/* 413 */       g.setColor(col);
/* 414 */       return true;
/*     */     }
/* 416 */     return false;
/*     */   }
/*     */ 
/*     */   private void restoreStroke(Graphics2D g) {
/* 420 */     g.setStroke((Stroke)this.transforms.pop());
/*     */   }
/*     */ 
/*     */   private Font createFont(Properties style)
/*     */   {
/* 425 */     String family = style.getProperty("font-family", "default");
/* 426 */     String height = style.getProperty("font-size", "12");
/* 427 */     String weight = style.getProperty("font-weight", "normal");
/* 428 */     String italic = style.getProperty("font-style", "normal");
/*     */ 
/* 430 */     int sty = 0;
/* 431 */     if (weight.equals("bold"))
/* 432 */       sty |= 1;
/* 433 */     if (italic.equals("italic")) {
/* 434 */       sty |= 2;
/*     */     }
/* 436 */     int size = 12;
/*     */     try {
/* 438 */       size = parseInt(height);
/*     */     } catch (Exception e) {
/*     */     }
/* 441 */     return new Font(family, sty, size);
/*     */   }
/*     */ 
/*     */   private double parseDouble(String val)
/*     */     throws NumberFormatException
/*     */   {
/* 447 */     return Double.parseDouble(val.split("[^0-9\\.]")[0]);
/*     */   }
/*     */ 
/*     */   private int parseInt(String val)
/*     */     throws NumberFormatException
/*     */   {
/* 453 */     return Integer.parseInt(val.split("[^0-9]")[0]);
/*     */   }
/*     */ 
/*     */   public void print()
/*     */     throws PrinterException
/*     */   {
/* 463 */     PrinterJob job = PrinterJob.getPrinterJob();
/* 464 */     Book book = new Book();
/* 465 */     book.append(this, job.defaultPage());
/*     */ 
/* 467 */     job.setPageable(book);
/* 468 */     if (job.printDialog())
/* 469 */       job.print();
/*     */   }
/*     */ 
/*     */   public void print(PrinterJob job, PageFormat format)
/*     */     throws PrinterException
/*     */   {
/* 475 */     Book book = new Book();
/* 476 */     book.append(this, format);
/*     */ 
/* 478 */     job.setPageable(book);
/* 479 */     if (job.printDialog())
/* 480 */       job.print();
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.print.SVGPrintable
 * JD-Core Version:    0.6.2
 */