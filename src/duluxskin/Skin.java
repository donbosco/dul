/*      */ package duluxskin;
/*      */ 
/*      */ import java.awt.Color;
/*      */ import java.awt.Component;
/*      */ import java.awt.Container;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.Font;
/*      */ import java.awt.FontFormatException;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.Graphics2D;
/*      */ import java.awt.Image;
/*      */ import java.awt.MediaTracker;
/*      */ import java.awt.Point;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.RenderingHints;
/*      */ import java.awt.Shape;
/*      */ import java.awt.Toolkit;
/*      */ import java.awt.event.MouseEvent;
/*      */ import java.awt.event.MouseListener;
/*      */ import java.awt.event.MouseMotionListener;
/*      */ import java.awt.event.MouseWheelEvent;
/*      */ import java.awt.event.MouseWheelListener;
/*      */ import java.io.BufferedInputStream;
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.PrintStream;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map;
/*      */ import java.util.Vector;
/*      */ import javax.xml.parsers.DocumentBuilder;
/*      */ import javax.xml.parsers.DocumentBuilderFactory;
/*      */ import org.w3c.dom.Attr;
/*      */ import org.w3c.dom.Document;
/*      */ import org.w3c.dom.Element;
/*      */ import org.w3c.dom.NamedNodeMap;
/*      */ import org.w3c.dom.Node;
/*      */ import org.w3c.dom.NodeList;
/*      */ 
/*      */ public class Skin extends Container
/*      */   implements MouseListener, MouseMotionListener, MouseWheelListener
/*      */ {
/*      */   private Vector layers;
/*      */   private Rectangle invalid;
/*      */   private Widget capture;
/*      */   private Widget lastEntered;
/*      */   private Map widgetMap;
/*      */   private Map fontMap;
/*      */   private Map layerMap;
/*      */   private Dimension dimension;
/*      */   private Image backImage;
/*      */   private Font layoutFont;
/*      */   private Vector exceptionListeners;
/*      */   private Rectangle maxRect;
/*      */ 
/*      */   public Skin()
/*      */   {
/*   38 */     this.layers = new Vector();
/*      */ 
/*   40 */     this.widgetMap = new HashMap();
/*      */ 
/*   42 */     this.fontMap = new HashMap();
/*      */ 
/*   44 */     this.layerMap = new HashMap();
/*      */ 
/*   46 */     this.exceptionListeners = new Vector();
/*      */ 
/*   48 */     addMouseListener(this);
/*      */ 
/*   50 */     addMouseMotionListener(this);
/*      */ 
/*   52 */     addMouseWheelListener(this);
/*      */ 
/*   54 */     this.invalid = null;
/*      */ 
/*   56 */     setLayout(null);
/*      */   }
/*      */ 
/*      */   public void addExceptionListener(ExceptionListener l)
/*      */   {
/*   62 */     this.exceptionListeners.add(l);
/*      */   }
/*      */ 
/*      */   private void notifyExceptionOccured(Throwable t)
/*      */   {
/*   68 */     for (Iterator it = this.exceptionListeners.iterator(); it.hasNext(); )
/*   69 */       ((ExceptionListener)it.next()).exceptionOccured(this, t);
/*      */   }
/*      */ 
/*      */   public void update(Graphics g)
/*      */   {
/*   76 */     paint(g);
/*      */   }
/*      */ 
/*      */   public void paint(Graphics g)
/*      */   {
/*   82 */     if (this.backImage == null) {
/*   83 */       this.backImage = createImage(getWidth(), getHeight());
/*      */     }
/*      */ 
/*   88 */     Graphics backGraphics = this.backImage.getGraphics();
/*      */ 
/*   90 */     Graphics2D g2D = (Graphics2D)backGraphics;
/*      */ 
/*   92 */     g2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
/*      */ 
/*   95 */     g2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
/*      */ 
/*  100 */     if (this.invalid != null) {
/*  101 */       this.invalid = this.invalid.intersection(this.maxRect);
/*      */     }
/*      */ 
/*  106 */     Shape oldClip = g.getClip();
/*      */ 
/*  108 */     g2D.setClip(this.invalid);
/*      */ 
/*  110 */     g.setClip(this.invalid);
/*      */ 
/*  114 */     for (Iterator it = this.layers.iterator(); it.hasNext(); )
/*      */     {
/*  116 */       Layer l = (Layer)it.next();
/*      */ 
/*  118 */       l.paint(backGraphics);
/*      */     }
/*      */ 
/*  124 */     if (this.invalid == null)
/*  125 */       g.drawImage(this.backImage, 0, 0, getWidth(), getHeight(), null);
/*      */     else {
/*  127 */       g.drawImage(this.backImage, this.invalid.x, this.invalid.y, this.invalid.x + this.invalid.width, this.invalid.y + this.invalid.height, this.invalid.x, this.invalid.y, this.invalid.x + this.invalid.width, this.invalid.y + this.invalid.height, null);
/*      */     }
/*      */ 
/*  135 */     this.invalid = null;
/*      */ 
/*  137 */     g.setClip(oldClip);
/*      */ 
/*  141 */     Component[] children = getComponents();
/*      */ 
/*  143 */     for (int i = 0; i < children.length; i++)
/*  144 */       children[i].paint(g);
/*      */   }
/*      */ 
/*      */   public Dimension getPreferredSize()
/*      */   {
/*  151 */     return this.dimension;
/*      */   }
/*      */ 
/*      */   public Dimension getMinimumSize()
/*      */   {
/*  157 */     return this.dimension;
/*      */   }
/*      */ 
/*      */   public Dimension getMaximumSize()
/*      */   {
/*  163 */     return this.dimension;
/*      */   }
/*      */ 
/*      */   public void load(InputStream is)
/*      */   {
/*      */     try
/*      */     {
/*  171 */       DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
/*      */ 
/*  174 */       DocumentBuilder builder = factory.newDocumentBuilder();
/*      */ 
/*  176 */       Document doc = builder.parse(is);
/*      */ 
/*  178 */       load(doc);
/*      */     }
/*      */     catch (Throwable t)
/*      */     {
/*  182 */       notifyExceptionOccured(t);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void load(Document doc)
/*      */   {
/*      */     try
/*      */     {
/*  192 */       Element root = doc.getDocumentElement();
/*      */ 
/*  196 */       NodeList elems = root.getChildNodes();
/*      */ 
/*  198 */       for (int i = 0; i < elems.getLength(); i++)
/*      */       {
/*  200 */         Node n = elems.item(i);
/*      */ 
/*  202 */         if (n.getNodeType() == 1)
/*      */         {
/*  204 */           if (n.getNodeName().equals("layout"))
/*  205 */             loadLayout((Element)n);
/*  206 */           else if (n.getNodeName().equals("font")) {
/*  207 */             loadFont((Element)n);
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  214 */       this.maxRect = new Rectangle(0, 0, this.dimension.width, this.dimension.height);
/*      */     }
/*      */     catch (Throwable t)
/*      */     {
/*  218 */       notifyExceptionOccured(t);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void loadFont(Element elem)
/*      */   {
/*  226 */     String id = elem.getAttribute("id");
/*      */ 
/*  228 */     String typeName = elem.getAttribute("type");
/*      */ 
/*  230 */     String resourceName = elem.getAttribute("src");
/*      */ 
/*  232 */     int type = 0;
/*      */ 
/*  234 */     if (typeName.equals("type1")) {
/*  235 */       type = 1;
/*      */     }
/*      */ 
/*  238 */     InputStream is = getClass().getResourceAsStream(resourceName);
/*      */ 
/*  240 */     Font font = null;
/*      */     try
/*      */     {
/*  244 */       font = Font.createFont(type, is);
/*      */     }
/*      */     catch (IllegalArgumentException e)
/*      */     {
/*  248 */       e.printStackTrace();
/*      */ 
/*  250 */       font = new Font("SansSerif", 0, 10);
/*      */     }
/*      */     catch (FontFormatException e)
/*      */     {
/*  254 */       e.printStackTrace();
/*      */ 
/*  256 */       font = new Font("SansSerif", 0, 10);
/*      */     }
/*      */     catch (IOException e)
/*      */     {
/*  260 */       e.printStackTrace();
/*      */ 
/*  262 */       font = new Font("SansSerif", 0, 10);
/*      */     }
/*      */ 
/*  266 */     font = font.deriveFont(parseInteger(elem.getAttribute("size"), 12));
/*      */ 
/*  269 */     this.fontMap.put(id, font);
/*      */   }
/*      */ 
/*      */   public void loadLayout(Element root)
/*      */   {
/*  275 */     this.dimension = parseDimension(root.getAttribute("dimensions"));
/*      */ 
/*  277 */     String fontName = root.getAttribute("font");
/*      */ 
/*  279 */     if (fontName != null) {
/*  280 */       this.layoutFont = ((Font)this.fontMap.get(fontName));
/*      */     }
/*      */ 
/*  285 */     NodeList layers = root.getChildNodes();
/*      */ 
/*  287 */     for (int i = 0; i < layers.getLength(); i++)
/*      */     {
/*  289 */       Node n = layers.item(i);
/*      */ 
/*  291 */       if ((n.getNodeType() == 1) && (n.getNodeName().equals("layer")))
/*      */       {
/*  294 */         loadLayer((Element)n);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void loadLayer(Element elem)
/*      */   {
/*  304 */     String id = elem.getAttribute("id");
/*      */ 
/*  306 */     Layer layer = new Layer(id);
/*      */ 
/*  308 */     layer.setSkin(this);
/*      */ 
/*  310 */     layer.setVisible(parseBoolean(elem.getAttribute("visible"), true));
/*      */ 
/*  312 */     this.layers.add(layer);
/*      */ 
/*  314 */     this.layerMap.put(id, layer);
/*      */ 
/*  318 */     NodeList widgets = elem.getChildNodes();
/*      */ 
/*  320 */     for (int i = 0; i < widgets.getLength(); i++)
/*      */     {
/*  322 */       Node n = widgets.item(i);
/*      */ 
/*  324 */       if (n.getNodeType() == 1)
/*      */       {
/*  330 */         Element widgetElem = (Element)n;
/*      */ 
/*  332 */         Widget widget = null;
/*      */ 
/*  334 */         if (widgetElem.getNodeName().equals("button"))
/*  335 */           widget = loadButton(widgetElem);
/*  336 */         else if (widgetElem.getNodeName().equals("image"))
/*  337 */           widget = loadImage(widgetElem);
/*  338 */         else if (widgetElem.getNodeName().equals("list"))
/*  339 */           widget = loadList(widgetElem);
/*  340 */         else if (widgetElem.getNodeName().equals("label"))
/*  341 */           widget = loadLabel(widgetElem);
/*  342 */         else if (widgetElem.getNodeName().equals("region"))
/*  343 */           widget = loadRegion(widgetElem);
/*  344 */         else if (widgetElem.getNodeName().equals("scrollbar"))
/*  345 */           widget = loadScrollbar(widgetElem);
/*  346 */         else if (widgetElem.getNodeName().equals("slider"))
/*  347 */           widget = loadSlider(widgetElem);
/*  348 */         else if (widgetElem.getNodeName().equals("progress"))
/*  349 */           widget = loadProgress(widgetElem);
/*  350 */         else if (widgetElem.getNodeName().equals("placeholder"))
/*  351 */           widget = loadPlaceholder(widgetElem);
/*  352 */         else if (!widgetElem.getNodeName().equals("videoplayer"));
/*  358 */         if (widget != null)
/*      */         {
/*  360 */           boolean enabled = parseBoolean(widgetElem.getAttribute("enabled"), true);
/*      */ 
/*  363 */           boolean visible = parseBoolean(widgetElem.getAttribute("visible"), true);
/*      */ 
/*  366 */           widget.setEnabled(enabled);
/*      */ 
/*  368 */           widget.setVisible(visible);
/*      */ 
/*  370 */           widget.setFocusable(false);
/*      */ 
/*  372 */           this.widgetMap.put(widget.getId(), widget);
/*      */ 
/*  374 */           layer.add(widget);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private SkinnedLabel loadLabel(Element elem)
/*      */   {
/*  384 */     String id = elem.getAttribute("id");
/*      */ 
/*  386 */     Point p = parsePoint(elem.getAttribute("pos"));
/*      */ 
/*  388 */     String label = elem.getAttribute("label");
/*      */ 
/*  390 */     String fontName = elem.getAttribute("font");
/*      */ 
/*  392 */     Dimension d = parseDimension(elem.getAttribute("dimensions"));
/*      */ 
/*  394 */     Color color = parseColor(elem.getAttribute("color"));
/*      */ 
/*  396 */     String alignString = elem.getAttribute("align");
/*      */ 
/*  398 */     int align = 0;
/*      */ 
/*  400 */     boolean wrap = parseBoolean(elem.getAttribute("wrap"));
/*      */ 
/*  402 */     if (alignString.equals("center"))
/*  403 */       align = 1;
/*  404 */     else if (alignString.equals("right")) {
/*  405 */       align = 2;
/*      */     }
/*      */ 
/*  410 */     String verticalAlignString = elem.getAttribute("vertical-align");
/*      */ 
/*  412 */     int verticalAlign = 0;
/*      */ 
/*  414 */     if (verticalAlignString.equals("center"))
/*  415 */       verticalAlign = 1;
/*  416 */     else if (verticalAlignString.equals("bottom")) {
/*  417 */       verticalAlign = 2;
/*      */     }
/*      */ 
/*  422 */     Font font = this.layoutFont;
/*      */ 
/*  424 */     if (fontName != null) {
/*  425 */       font = (Font)this.fontMap.get(fontName);
/*      */     }
/*      */ 
/*  430 */     SkinnedLabel lab = new SkinnedLabel(id, p.x, p.y, label);
/*      */ 
/*  432 */     lab.setFont(font);
/*      */ 
/*  434 */     lab.setColor(color);
/*      */ 
/*  436 */     lab.setAlignment(align);
/*      */ 
/*  438 */     lab.setVerticalAlignment(verticalAlign);
/*      */ 
/*  441 */     if (wrap) {
/*  442 */       lab.setWrap(d);
/*      */     }
/*  444 */     return lab;
/*      */   }
/*      */ 
/*      */   private SkinnedRegion loadRegion(Element elem)
/*      */   {
/*  451 */     String id = elem.getAttribute("id");
/*      */ 
/*  453 */     Point p = parsePoint(elem.getAttribute("pos"));
/*      */ 
/*  455 */     Dimension d = parseDimension(elem.getAttribute("dimensions"));
/*      */ 
/*  457 */     String colorName = elem.getAttribute("color");
/*      */ 
/*  459 */     Color c = null;
/*      */ 
/*  461 */     if (colorName.length() > 0) {
/*  462 */       c = parseColor(colorName);
/*      */     }
/*      */ 
/*  465 */     float opacity = parseFloat(elem.getAttribute("opacity"), 1.0F);
/*      */ 
/*  469 */     SkinnedRegion region = new SkinnedRegion(id, p.x, p.y, d.width, d.height);
/*      */ 
/*  472 */     region.setColor(c);
/*      */ 
/*  474 */     region.setOpacity(opacity);
/*      */ 
/*  476 */     return region;
/*      */   }
/*      */ 
/*      */   private SkinnedButton loadButton(Element elem)
/*      */   {
/*  495 */     String id = elem.getAttribute("id");
/*      */ 
/*  497 */     Point p = parsePoint(elem.getAttribute("pos"));
/*      */ 
/*  499 */     Image up = parseImage(elem.getAttribute("up"));
/*      */ 
/*  501 */     Image down = parseImage(elem.getAttribute("down"));
/*      */ 
/*  503 */     Image disabled = parseImage(elem.getAttribute("disabled"));
/*      */ 
/*  505 */     Image on = parseImage(elem.getAttribute("on"));
/*      */ 
/*  507 */     Image over = parseImage(elem.getAttribute("over"));
/*      */ 
/*  509 */     boolean toggle = parseBoolean(elem.getAttribute("toggle"));
/*      */ 
/*  511 */     boolean state = parseBoolean(elem.getAttribute("state"));
/*      */ 
/*  513 */     Dimension di = parseDimension(elem.getAttribute("dimensions"));
/*      */ 
/*  515 */     int type = parseInteger(elem.getAttribute("type"), 0);
/*      */ 
/*  517 */     int colour = parseInteger(elem.getAttribute("colour"), 0);
/*      */     try
/*      */     {
/*  521 */       MediaTracker mt = new MediaTracker(this);
/*      */ 
/*  523 */       mt.addImage(up, 1);
/*      */ 
/*  525 */       mt.addImage(down, 2);
/*      */ 
/*  527 */       mt.addImage(disabled, 3);
/*      */ 
/*  529 */       mt.addImage(on, 4);
/*      */ 
/*  531 */       mt.addImage(over, 5);
/*      */ 
/*  533 */       mt.waitForAll();
/*      */     }
/*      */     catch (InterruptedException e)
/*      */     {
/*      */     }
SkinnedButton e;
/*      */ 
/*  539 */     if (type == 3)
/*  540 */       e = new SkinnedButton(id, p.x, p.y, di.width, di.height, type);
/*      */     SkinnedButton button;
/*  543 */     if ((di.height == 0) || (di.width == 0)) {
/*  544 */       button = new SkinnedButton(id, p.x, p.y, up, down, over, disabled, on, toggle, colour, type);
/*      */ 
/*  546 */       button.setState(state);
/*      */     }
/*      */     else {
/*  549 */       button = new SkinnedButton(id, p.x, p.y, up, down, disabled, on, toggle, colour, type, di.width, di.height);
/*      */ 
/*  551 */       button.setState(state);
/*      */     }
/*      */ 
/*  555 */     return button;
/*      */   }
/*      */ 
/*      */   private SkinnedImage loadImage(Element elem)
/*      */   {
/*  562 */     String id = elem.getAttribute("id");
/*      */ 
/*  564 */     Point p = parsePoint(elem.getAttribute("pos"));
/*      */ 
/*  566 */     Image img = parseImage(elem.getAttribute("image"));
/*      */     try
/*      */     {
/*  570 */       MediaTracker mt = new MediaTracker(this);
/*      */ 
/*  572 */       mt.addImage(img, 1);
/*      */ 
/*  574 */       mt.waitForAll();
/*      */     }
/*      */     catch (InterruptedException e)
/*      */     {
/*  578 */       e.printStackTrace();
/*      */     }
/*      */ 
/*  584 */     SkinnedImage image = new SkinnedImage(id, p.x, p.y, img);
/*      */ 
/*  589 */     return image;
/*      */   }
/*      */ 
/*      */   private SkinnedList loadList(Element elem)
/*      */   {
/*  595 */     String id = elem.getAttribute("id");
/*      */ 
/*  597 */     Point p = parsePoint(elem.getAttribute("pos"));
/*      */ 
/*  599 */     Dimension sz = parseDimension(elem.getAttribute("dimensions"));
/*      */     SkinnedList list;
/*  605 */     if (parseBoolean(elem.getAttribute("popup"), false))
/*  606 */       list = new SkinnedPopupList(id, p.x, p.y, sz.width, sz.height);
/*      */     else {
/*  608 */       list = new SkinnedList(id, p.x, p.y, sz.width, sz.height);
/*      */     }
/*      */ 
/*  613 */     String fontName = elem.getAttribute("font");
/*      */ 
/*  615 */     if (fontName == null)
/*  616 */       list.setFont(this.layoutFont);
/*      */     else {
/*  618 */       list.setFont((Font)this.fontMap.get(fontName));
/*      */     }
/*      */ 
/*  621 */     list.setHighlight(parseColor(elem.getAttribute("highlight-background")), parseColor(elem.getAttribute("highlight-foreground")));
/*      */ 
/*  624 */     list.setTextColor(parseColor(elem.getAttribute("text-foreground")));
/*      */ 
/*  626 */     list.setLineHeight(parseInteger(elem.getAttribute("line-height"), 12));
/*      */ 
/*  628 */     list.setHighlightHanging(parseInteger(elem.getAttribute("highlight-hanging"), 2));
/*      */ 
/*  631 */     return list;
/*      */   }
/*      */ 
/*      */   private SkinnedScrollbar loadScrollbar(Element elem)
/*      */   {
/*  637 */     String id = elem.getAttribute("id");
/*      */ 
/*  639 */     Point p = parsePoint(elem.getAttribute("pos"));
/*      */ 
/*  641 */     Dimension sz = parseDimension(elem.getAttribute("dimensions"));
/*      */ 
/*  645 */     SkinnedScrollbar scrollbar = new SkinnedScrollbar(id, p.x, p.y, sz.width, sz.height);
/*      */ 
/*  648 */     scrollbar.setUnit(parseInteger(elem.getAttribute("unit"), 1));
/*      */ 
/*  650 */     scrollbar.setColor(parseColor(elem.getAttribute("color")));
/*      */ 
/*  654 */     String listName = elem.getAttribute("list");
/*      */ 
/*  656 */     if (listName != null) {
/*  657 */       scrollbar.setList((SkinnedList)this.widgetMap.get(listName));
/*      */     }
/*      */ 
/*  662 */     String upButtonName = elem.getAttribute("button-up");
/*      */ 
/*  664 */     if (upButtonName != null) {
/*  665 */       scrollbar.setButtonUp((SkinnedButton)this.widgetMap.get(upButtonName));
/*      */     }
/*      */ 
/*  670 */     String downButtonName = elem.getAttribute("button-down");
/*      */ 
/*  672 */     if (downButtonName != null) {
/*  673 */       scrollbar.setButtonDown((SkinnedButton)this.widgetMap.get(downButtonName));
/*      */     }
/*      */ 
/*  679 */     return scrollbar;
/*      */   }
/*      */ 
/*      */   private SkinnedSlider loadSlider(Element elem)
/*      */   {
/*  685 */     String id = elem.getAttribute("id");
/*      */ 
/*  687 */     Point p = parsePoint(elem.getAttribute("pos"));
/*      */ 
/*  689 */     Dimension sz = parseDimension(elem.getAttribute("dimensions"));
/*      */ 
/*  691 */     String leftLabel = elem.getAttribute("leftLabel");
/*      */ 
/*  693 */     String rightLabel = elem.getAttribute("rightLabel");
/*      */ 
/*  695 */     boolean sliderTextVisible = parseBoolean(elem.getAttribute("sliderTextVisible"));
/*      */ 
/*  697 */     SkinnedSlider slider = new SkinnedSlider(id, p.x, p.y, sz.width, sz.height, leftLabel, rightLabel, sliderTextVisible);
/*      */ 
/*  699 */     slider.setColor(parseColor(elem.getAttribute("color")));
/*      */ 
/*  701 */     slider.setMinimum(parseInteger(elem.getAttribute("min"), 0));
/*      */ 
/*  703 */     slider.setMaximum(parseInteger(elem.getAttribute("max"), 100));
/*      */ 
/*  705 */     slider.setVisibleAmount(parseInteger(elem.getAttribute("size"), 10));
/*      */ 
/*  709 */     return slider;
/*      */   }
/*      */ 
/*      */   private SkinnedProgress loadProgress(Element elem)
/*      */   {
/*  715 */     String id = elem.getAttribute("id");
/*      */ 
/*  717 */     Point p = parsePoint(elem.getAttribute("pos"));
/*      */ 
/*  719 */     Dimension sz = parseDimension(elem.getAttribute("dimensions"));
/*      */ 
/*  723 */     SkinnedProgress progress = new SkinnedProgress(id, p.x, p.y, sz.width, sz.height);
/*      */ 
/*  726 */     progress.setColor(parseColor(elem.getAttribute("color")));
/*      */ 
/*  730 */     return progress;
/*      */   }
/*      */ 
/*      */   private SkinnedPlaceholder loadPlaceholder(Element elem)
/*      */   {
/*  736 */     String id = elem.getAttribute("id");
/*      */ 
/*  738 */     Point p = parsePoint(elem.getAttribute("pos"));
/*      */ 
/*  740 */     Dimension sz = parseDimension(elem.getAttribute("dimensions"));
/*      */ 
/*  744 */     SkinnedPlaceholder progress = new SkinnedPlaceholder(id, p.x, p.y, sz.width, sz.height);
/*      */ 
/*  747 */     NamedNodeMap attrs = elem.getAttributes();
/*      */ 
/*  749 */     for (int i = 0; i < attrs.getLength(); i++)
/*      */     {
/*  751 */       Attr n = (Attr)attrs.item(i);
/*      */ 
/*  753 */       progress.addAttribute(n.getName(), n.getValue());
/*      */     }
/*      */ 
/*  759 */     return progress;
/*      */   }
/*      */ 
/*      */   private Image parseImage(String filename)
/*      */   {
/*      */     try
/*      */     {
/*  767 */       InputStream is = getClass().getResourceAsStream(filename);
/*      */ 
/*  769 */       BufferedInputStream bis = new BufferedInputStream(is);
/*      */ 
/*  771 */       ByteArrayOutputStream os = new ByteArrayOutputStream();
/*      */ 
/*  775 */       byte[] buf = new byte[8192];
/*      */       int len;
/*  777 */       while ((len = bis.read(buf, 0, buf.length)) > 0) {
/*  778 */         os.write(buf, 0, len);
/*      */       }
/*      */ 
/*  781 */       buf = os.toByteArray();
/*      */ 
/*  785 */       Toolkit tk = Toolkit.getDefaultToolkit();
/*      */ 
/*  787 */       return tk.createImage(buf);
/*      */     }
/*      */     catch (IOException e)
/*      */     {
/*  793 */       if ((filename != null) && (filename.length() > 0)) {
/*  794 */         System.out.println("Missing resource " + filename);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  799 */     return null;
/*      */   }
/*      */ 
/*      */   private boolean parseBoolean(String b, boolean def)
/*      */   {
/*  805 */     if ((b == null) || (b.length() == 0)) {
/*  806 */       return def;
/*      */     }
/*      */ 
/*  811 */     b = b.toLowerCase();
/*      */ 
/*  813 */     return (b.equals("true")) || (b.equals("on")) || (b.equals("yes"));
/*      */   }
/*      */ 
/*      */   private boolean parseBoolean(String b)
/*      */   {
/*  819 */     return parseBoolean(b, false);
/*      */   }
/*      */ 
/*      */   private int parseInteger(String i, int def)
/*      */   {
/*  825 */     if (i == null) {
/*  826 */       return def;
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/*  833 */       return Integer.parseInt(i);
/*      */     }
/*      */     catch (NumberFormatException e)
/*      */     {
/*      */     }
/*  838 */     return def;
/*      */   }
/*      */ 
/*      */   private float parseFloat(String i, float def)
/*      */   {
/*  844 */     if (i == null) {
/*  845 */       return def;
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/*  852 */       return Float.parseFloat(i);
/*      */     }
/*      */     catch (NumberFormatException e)
/*      */     {
/*      */     }
/*  857 */     return def;
/*      */   }
/*      */ 
/*      */   private Point parsePoint(String p)
/*      */   {
/*  863 */     if (p == null) {
/*  864 */       return new Point(0, 0);
/*      */     }
/*      */ 
/*  869 */     int x = 0; int y = 0;
/*      */     try
/*      */     {
/*  873 */       String[] xy = p.split(",");
/*      */ 
/*  875 */       x = Integer.parseInt(xy[0]);
/*      */ 
/*  877 */       y = Integer.parseInt(xy[1]);
/*      */     }
/*      */     catch (NumberFormatException e)
/*      */     {
/*      */     }
/*  882 */     return new Point(x, y);
/*      */   }
/*      */ 
/*      */   private Dimension parseDimension(String p)
/*      */   {
/*  888 */     if (p == null) {
/*  889 */       return new Dimension(0, 0);
/*      */     }
/*      */ 
/*  894 */     int x = 0; int y = 0;
/*      */     try
/*      */     {
/*  898 */       String[] xy = p.split("x");
/*      */ 
/*  900 */       x = Integer.parseInt(xy[0]);
/*      */ 
/*  902 */       y = Integer.parseInt(xy[1]);
/*      */     }
/*      */     catch (NumberFormatException e)
/*      */     {
/*      */     }
/*  907 */     return new Dimension(x, y);
/*      */   }
/*      */ 
/*      */   private Color parseColor(String c)
/*      */   {
/*  913 */     if (c == null) {
/*  914 */       return Color.black;
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/*  921 */       return new Color(Integer.parseInt(c, 16));
/*      */     }
/*      */     catch (NumberFormatException e)
/*      */     {
/*      */     }
/*      */ 
/*  928 */     return Color.black;
/*      */   }
/*      */ 
/*      */   public Layer getLayer(String id)
/*      */   {
/*  934 */     return (Layer)this.layerMap.get(id);
/*      */   }
/*      */ 
/*      */   public Widget getWidget(String id)
/*      */   {
/*  940 */     return (Widget)this.widgetMap.get(id);
/*      */   }
/*      */ 
/*      */   public SkinnedButton getButton(String id)
/*      */   {
/*  950 */     return (SkinnedButton)this.widgetMap.get(id);
/*      */   }
/*      */ 
/*      */   public SkinnedList getList(String id)
/*      */   {
/*  956 */     return (SkinnedList)this.widgetMap.get(id);
/*      */   }
/*      */ 
/*      */   public SkinnedLabel getLabel(String id)
/*      */   {
/*  962 */     return (SkinnedLabel)this.widgetMap.get(id);
/*      */   }
/*      */ 
/*      */   public SkinnedProgress getProgress(String id)
/*      */   {
/*  968 */     return (SkinnedProgress)this.widgetMap.get(id);
/*      */   }
/*      */ 
/*      */   public SkinnedSlider getSlider(String id)
/*      */   {
/*  974 */     return (SkinnedSlider)this.widgetMap.get(id);
/*      */   }
/*      */ 
/*      */   public SkinnedPlaceholder getPlaceholder(String id)
/*      */   {
/*  980 */     return (SkinnedPlaceholder)this.widgetMap.get(id);
/*      */   }
/*      */ 
/*      */   public void invalidate(int x, int y, int width, int height)
/*      */   {
/*  986 */     if (this.invalid != null)
/*  987 */       this.invalid = this.invalid.union(new Rectangle(x, y, width, height));
/*      */     else {
/*  989 */       this.invalid = new Rectangle(x, y, width, height);
/*      */     }
/*      */ 
/*  992 */     repaint();
/*      */   }
/*      */ 
/*      */   public void invalidate()
/*      */   {
/*  998 */     this.invalid = new Rectangle(0, 0, getWidth(), getHeight());
/*      */ 
/* 1000 */     repaint();
/*      */   }
/*      */ 
/*      */   public void mouseClicked(MouseEvent e)
/*      */   {
/* 1008 */     handleMouseEvent(e);
/*      */   }
/*      */ 
/*      */   public void mouseEntered(MouseEvent e)
/*      */   {
/*      */   }
/*      */ 
/*      */   public void mouseExited(MouseEvent e)
/*      */   {
/*      */   }
/*      */ 
/*      */   public void mousePressed(MouseEvent e) {
/* 1020 */     handleMouseEvent(e);
/*      */   }
/*      */ 
/*      */   public void mouseReleased(MouseEvent e)
/*      */   {
/* 1026 */     handleMouseEvent(e);
/*      */   }
/*      */ 
/*      */   public void mouseMoved(MouseEvent e)
/*      */   {
/* 1032 */     handleMouseEvent(e);
/*      */   }
/*      */ 
/*      */   public void mouseDragged(MouseEvent e)
/*      */   {
/* 1038 */     handleMouseEvent(e);
/*      */   }
/*      */ 
/*      */   public void mouseWheelMoved(MouseWheelEvent e)
/*      */   {
/* 1044 */     handleMouseEvent(e);
/*      */   }
/*      */ 
/*      */   private void handleMouseEvent(MouseEvent e)
/*      */   {
/*      */     try
/*      */     {
/* 1052 */       if (this.capture != null)
/*      */       {
/* 1054 */         if (this.capture.hitTest(e))
/* 1055 */           enteredWidget(e, this.capture);
/*      */         else {
/* 1057 */           enteredWidget(e, null);
/*      */         }
/*      */ 
/* 1062 */         this.capture.mouseEvent(e);
/*      */       }
/*      */       else
/*      */       {
/* 1066 */         for (int i = this.layers.size() - 1; i >= 0; i--)
/*      */         {
/* 1068 */           Layer layer = (Layer)this.layers.get(i);
/*      */ 
/* 1070 */           if ((layer.getVisible()) && (layer.mouseEvent(e)))
/*      */           {
/*      */             break;
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */     catch (Throwable t)
/*      */     {
/* 1080 */       notifyExceptionOccured(t);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setCapture(Widget w)
/*      */   {
/* 1088 */     this.capture = w;
/*      */   }
/*      */ 
/*      */   public void releaseCapture(Widget w)
/*      */   {
/* 1094 */     if (this.capture == w)
/* 1095 */       this.capture = null;
/*      */   }
/*      */ 
/*      */   void enteredWidget(MouseEvent e, Widget w)
/*      */   {
/* 1102 */     if (w != this.lastEntered)
/*      */     {
/* 1104 */       if (this.lastEntered != null)
/*      */       {
/* 1106 */         this.lastEntered.mouseEvent(new MouseEvent(this.lastEntered, 505, e.getWhen(), e.getModifiers(), e.getX(), e.getY(), e.getClickCount(), false));
/*      */       }
/*      */ 
/* 1116 */       if (w != null)
/*      */       {
/* 1118 */         w.mouseEvent(new MouseEvent(w, 504, e.getWhen(), e.getModifiers(), e.getX(), e.getY(), e.getClickCount(), false));
/*      */       }
/*      */ 
/* 1128 */       this.lastEntered = w;
/*      */     }
/*      */   }
/*      */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     duluxskin.Skin
 * JD-Core Version:    0.6.2
 */