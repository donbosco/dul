/*      */ package pelibskin;
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
/*   75 */     this.layers = new Vector();
/*      */ 
/*   77 */     this.widgetMap = new HashMap();
/*      */ 
/*   79 */     this.fontMap = new HashMap();
/*      */ 
/*   81 */     this.layerMap = new HashMap();
/*      */ 
/*   83 */     this.exceptionListeners = new Vector();
/*      */ 
/*   85 */     addMouseListener(this);
/*      */ 
/*   87 */     addMouseMotionListener(this);
/*      */ 
/*   89 */     addMouseWheelListener(this);
/*      */ 
/*   91 */     this.invalid = null;
/*      */ 
/*   93 */     setLayout(null);
/*      */   }
/*      */ 
/*      */   public void addExceptionListener(ExceptionListener l)
/*      */   {
/*  103 */     this.exceptionListeners.add(l);
/*      */   }
/*      */ 
/*      */   private void notifyExceptionOccured(Throwable t)
/*      */   {
/*  113 */     for (Iterator it = this.exceptionListeners.iterator(); it.hasNext(); )
/*      */     {
/*  115 */       ((ExceptionListener)it.next()).exceptionOccured(this, t);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void update(Graphics g)
/*      */   {
/*  125 */     paint(g);
/*      */   }
/*      */ 
/*      */   public void paint(Graphics g)
/*      */   {
/*  135 */     if (this.backImage == null)
/*      */     {
/*  137 */       this.backImage = createImage(getWidth(), getHeight());
/*      */     }
/*      */ 
/*  141 */     Graphics backGraphics = this.backImage.getGraphics();
/*      */ 
/*  143 */     Graphics2D g2D = (Graphics2D)backGraphics;
/*      */ 
/*  145 */     g2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
/*      */ 
/*  149 */     g2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
/*      */ 
/*  155 */     if (this.invalid != null)
/*      */     {
/*  157 */       this.invalid = this.invalid.intersection(this.maxRect);
/*      */     }
/*      */ 
/*  161 */     Shape oldClip = g.getClip();
/*      */ 
/*  163 */     g2D.setClip(this.invalid);
/*      */ 
/*  165 */     g.setClip(this.invalid);
/*      */ 
/*  169 */     for (Iterator it = this.layers.iterator(); it.hasNext(); )
/*      */     {
/*  173 */       Layer l = (Layer)it.next();
/*      */ 
/*  175 */       l.paint(backGraphics);
/*      */     }
/*      */ 
/*  181 */     if (this.invalid == null)
/*      */     {
/*  183 */       g.drawImage(this.backImage, 0, 0, getWidth(), getHeight(), null);
/*      */     }
/*      */     else
/*      */     {
/*  187 */       g.drawImage(this.backImage, this.invalid.x, this.invalid.y, this.invalid.x + this.invalid.width, this.invalid.y + this.invalid.height, this.invalid.x, this.invalid.y, this.invalid.x + this.invalid.width, this.invalid.y + this.invalid.height, null);
/*      */     }
/*      */ 
/*  199 */     this.invalid = null;
/*      */ 
/*  201 */     g.setClip(oldClip);
/*      */ 
/*  205 */     Component[] children = getComponents();
/*      */ 
/*  207 */     for (int i = 0; i < children.length; i++)
/*      */     {
/*  209 */       children[i].paint(g);
/*      */     }
/*      */   }
/*      */ 
/*      */   public Dimension getPreferredSize()
/*      */   {
/*  219 */     return this.dimension;
/*      */   }
/*      */ 
/*      */   public Dimension getMinimumSize()
/*      */   {
/*  229 */     return this.dimension;
/*      */   }
/*      */ 
/*      */   public Dimension getMaximumSize()
/*      */   {
/*  239 */     return this.dimension;
/*      */   }
/*      */ 
/*      */   public void load(InputStream is)
/*      */   {
/*      */     try
/*      */     {
/*  251 */       DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
/*      */ 
/*  255 */       DocumentBuilder builder = factory.newDocumentBuilder();
/*      */ 
/*  257 */       Document doc = builder.parse(is);
/*      */ 
/*  259 */       load(doc);
/*      */     }
/*      */     catch (Throwable t)
/*      */     {
/*  263 */       notifyExceptionOccured(t);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void load(Document doc)
/*      */   {
/*      */     try
/*      */     {
/*  277 */       Element root = doc.getDocumentElement();
/*      */ 
/*  281 */       NodeList elems = root.getChildNodes();
/*      */ 
/*  283 */       for (int i = 0; i < elems.getLength(); i++)
/*      */       {
/*  287 */         Node n = elems.item(i);
/*      */ 
/*  289 */         if (n.getNodeType() == 1)
/*      */         {
/*  293 */           if (n.getNodeName().equals("layout"))
/*      */           {
/*  295 */             loadLayout((Element)n);
/*      */           }
/*  297 */           else if (n.getNodeName().equals("font"))
/*      */           {
/*  299 */             loadFont((Element)n);
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  305 */       this.maxRect = new Rectangle(0, 0, this.dimension.width, this.dimension.height);
/*      */     }
/*      */     catch (Throwable t)
/*      */     {
/*  309 */       notifyExceptionOccured(t);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void loadFont(Element elem)
/*      */   {
/*  321 */     String id = elem.getAttribute("id");
/*      */ 
/*  323 */     String typeName = elem.getAttribute("type");
/*      */ 
/*  325 */     String resourceName = elem.getAttribute("src");
/*      */ 
/*  327 */     int type = 0;
/*      */ 
/*  329 */     if (typeName.equals("type1"))
/*      */     {
/*  331 */       type = 1;
/*      */     }
/*  333 */     InputStream is = getClass().getResourceAsStream(resourceName);
/*      */ 
/*  335 */     Font font = null;
/*      */     try
/*      */     {
/*  339 */       font = Font.createFont(type, is);
/*      */     }
/*      */     catch (IllegalArgumentException e)
/*      */     {
/*  343 */       e.printStackTrace();
/*      */ 
/*  345 */       font = new Font("SansSerif", 0, 10);
/*      */     }
/*      */     catch (FontFormatException e)
/*      */     {
/*  349 */       e.printStackTrace();
/*      */ 
/*  351 */       font = new Font("SansSerif", 0, 10);
/*      */     }
/*      */     catch (IOException e)
/*      */     {
/*  355 */       e.printStackTrace();
/*      */ 
/*  357 */       font = new Font("SansSerif", 0, 10);
/*      */     }
/*      */ 
/*  361 */     font = font.deriveFont(parseInteger(elem.getAttribute("size"), 12));
/*      */ 
/*  365 */     this.fontMap.put(id, font);
/*      */   }
/*      */ 
/*      */   public void loadLayout(Element root)
/*      */   {
/*  375 */     this.dimension = parseDimension(root.getAttribute("dimensions"));
/*      */ 
/*  377 */     String fontName = root.getAttribute("font");
/*      */ 
/*  379 */     if (fontName != null)
/*      */     {
/*  381 */       this.layoutFont = ((Font)this.fontMap.get(fontName));
/*      */     }
/*      */ 
/*  385 */     NodeList layers = root.getChildNodes();
/*      */ 
/*  387 */     for (int i = 0; i < layers.getLength(); i++)
/*      */     {
/*  391 */       Node n = layers.item(i);
/*      */ 
/*  393 */       if ((n.getNodeType() == 1) && (n.getNodeName().equals("layer")))
/*      */       {
/*  399 */         loadLayer((Element)n);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void loadLayer(Element elem)
/*      */   {
/*  413 */     String id = elem.getAttribute("id");
/*      */ 
/*  415 */     Layer layer = new Layer(id);
/*      */ 
/*  417 */     layer.setSkin(this);
/*      */ 
/*  419 */     layer.setVisible(parseBoolean(elem.getAttribute("visible"), true));
/*      */ 
/*  421 */     this.layers.add(layer);
/*      */ 
/*  423 */     this.layerMap.put(id, layer);
/*      */ 
/*  427 */     NodeList widgets = elem.getChildNodes();
/*      */ 
/*  429 */     for (int i = 0; i < widgets.getLength(); i++)
/*      */     {
/*  433 */       Node n = widgets.item(i);
/*      */ 
/*  435 */       if (n.getNodeType() == 1)
/*      */       {
/*  441 */         Element widgetElem = (Element)n;
/*      */ 
/*  443 */         Widget widget = null;
/*      */ 
/*  445 */         if (widgetElem.getNodeName().equals("button"))
/*      */         {
/*  447 */           widget = loadButton(widgetElem);
/*      */         }
/*  449 */         else if (widgetElem.getNodeName().equals("image"))
/*      */         {
/*  451 */           widget = loadImage(widgetElem);
/*      */         }
/*  453 */         else if (widgetElem.getNodeName().equals("list"))
/*      */         {
/*  455 */           widget = loadList(widgetElem);
/*      */         }
/*  457 */         else if (widgetElem.getNodeName().equals("label"))
/*      */         {
/*  459 */           widget = loadLabel(widgetElem);
/*      */         }
/*  461 */         else if (widgetElem.getNodeName().equals("region"))
/*      */         {
/*  463 */           widget = loadRegion(widgetElem);
/*      */         }
/*  465 */         else if (widgetElem.getNodeName().equals("scrollbar"))
/*      */         {
/*  467 */           widget = loadScrollbar(widgetElem);
/*      */         }
/*  469 */         else if (widgetElem.getNodeName().equals("slider"))
/*      */         {
/*  471 */           widget = loadSlider(widgetElem);
/*      */         }
/*  473 */         else if (widgetElem.getNodeName().equals("progress"))
/*      */         {
/*  475 */           widget = loadProgress(widgetElem);
/*      */         }
/*  477 */         else if (widgetElem.getNodeName().equals("placeholder"))
/*      */         {
/*  479 */           widget = loadPlaceholder(widgetElem);
/*      */         }
/*      */ 
/*  483 */         if (widget != null)
/*      */         {
/*  487 */           boolean enabled = parseBoolean(widgetElem.getAttribute("enabled"), true);
/*      */ 
/*  491 */           boolean visible = parseBoolean(widgetElem.getAttribute("visible"), true);
/*      */ 
/*  495 */           widget.setEnabled(enabled);
/*      */ 
/*  497 */           widget.setVisible(visible);
/*      */ 
/*  499 */           widget.setFocusable(false);
/*      */ 
/*  501 */           this.widgetMap.put(widget.getId(), widget);
/*      */ 
/*  503 */           layer.add(widget);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private SkinnedLabel loadLabel(Element elem)
/*      */   {
/*  517 */     String id = elem.getAttribute("id");
/*      */ 
/*  519 */     Point p = parsePoint(elem.getAttribute("pos"));
/*      */ 
/*  521 */     String label = elem.getAttribute("label");
/*      */ 
/*  523 */     String fontName = elem.getAttribute("font");
/*      */ 
/*  525 */     Dimension d = parseDimension(elem.getAttribute("dimensions"));
/*      */ 
/*  527 */     Color color = parseColor(elem.getAttribute("color"));
/*      */ 
/*  529 */     String alignString = elem.getAttribute("align");
/*      */ 
/*  531 */     int align = 0;
/*      */ 
/*  533 */     boolean wrap = parseBoolean(elem.getAttribute("wrap"));
/*      */ 
/*  535 */     if (alignString.equals("center"))
/*      */     {
/*  537 */       align = 1;
/*      */     }
/*  539 */     else if (alignString.equals("right"))
/*      */     {
/*  541 */       align = 2;
/*      */     }
/*      */ 
/*  545 */     String verticalAlignString = elem.getAttribute("vertical-align");
/*      */ 
/*  547 */     int verticalAlign = 0;
/*      */ 
/*  549 */     if (verticalAlignString.equals("center"))
/*      */     {
/*  551 */       verticalAlign = 1;
/*      */     }
/*  553 */     else if (verticalAlignString.equals("bottom"))
/*      */     {
/*  555 */       verticalAlign = 2;
/*      */     }
/*      */ 
/*  559 */     Font font = this.layoutFont;
/*      */ 
/*  561 */     if (fontName != null)
/*      */     {
/*  563 */       font = (Font)this.fontMap.get(fontName);
/*      */     }
/*      */ 
/*  567 */     SkinnedLabel lab = new SkinnedLabel(id, p.x, p.y, label);
/*      */ 
/*  569 */     lab.setFont(font);
/*      */ 
/*  571 */     lab.setColor(color);
/*      */ 
/*  573 */     lab.setAlignment(align);
/*      */ 
/*  575 */     lab.setVerticalAlignment(verticalAlign);
/*      */ 
/*  577 */     if (wrap)
/*      */     {
/*  579 */       lab.setWrap(d);
/*      */     }
/*      */ 
/*  583 */     return lab;
/*      */   }
/*      */ 
/*      */   private SkinnedRegion loadRegion(Element elem)
/*      */   {
/*  593 */     String id = elem.getAttribute("id");
/*      */ 
/*  595 */     Point p = parsePoint(elem.getAttribute("pos"));
/*      */ 
/*  597 */     Dimension d = parseDimension(elem.getAttribute("dimensions"));
/*      */ 
/*  599 */     String colorName = elem.getAttribute("color");
/*      */ 
/*  601 */     Color c = null;
/*      */ 
/*  603 */     if (colorName.length() > 0)
/*      */     {
/*  605 */       c = parseColor(colorName);
/*      */     }
/*  607 */     float opacity = parseFloat(elem.getAttribute("opacity"), 1.0F);
/*      */ 
/*  611 */     SkinnedRegion region = new SkinnedRegion(id, p.x, p.y, d.width, d.height);
/*      */ 
/*  615 */     region.setColor(c);
/*      */ 
/*  617 */     region.setOpacity(opacity);
/*      */ 
/*  621 */     return region;
/*      */   }
/*      */ 
/*      */   private SkinnedButton loadButton(Element elem)
/*      */   {
/*  631 */     String id = elem.getAttribute("id");
/*      */ 
/*  633 */     Point p = parsePoint(elem.getAttribute("pos"));
/*      */ 
/*  635 */     Image up = parseImage(elem.getAttribute("up"));
/*      */ 
/*  637 */     Image down = parseImage(elem.getAttribute("down"));
/*      */ 
/*  639 */     Image disabled = parseImage(elem.getAttribute("disabled"));
/*      */ 
/*  641 */     Image on = parseImage(elem.getAttribute("on"));
/*      */ 
/*  643 */     boolean toggle = parseBoolean(elem.getAttribute("toggle"));
/*      */ 
/*  645 */     boolean state = parseBoolean(elem.getAttribute("state"));
/*      */     try
/*      */     {
/*  649 */       MediaTracker mt = new MediaTracker(this);
/*      */ 
/*  651 */       mt.addImage(up, 1);
/*      */ 
/*  653 */       mt.addImage(down, 2);
/*      */ 
/*  655 */       mt.addImage(disabled, 3);
/*      */ 
/*  657 */       mt.addImage(on, 4);
/*      */ 
/*  659 */       mt.waitForAll();
/*      */     }
/*      */     catch (InterruptedException e)
/*      */     {
/*      */     }
/*      */ 
/*  665 */     SkinnedButton button = new SkinnedButton(id, p.x, p.y, up, down, disabled, on, toggle);
/*      */ 
/*  669 */     button.setState(state);
/*      */ 
/*  673 */     return button;
/*      */   }
/*      */ 
/*      */   private SkinnedImage loadImage(Element elem)
/*      */   {
/*  683 */     String id = elem.getAttribute("id");
/*      */ 
/*  685 */     Point p = parsePoint(elem.getAttribute("pos"));
/*      */ 
/*  687 */     Image img = parseImage(elem.getAttribute("image"));
/*      */     try
/*      */     {
/*  691 */       MediaTracker mt = new MediaTracker(this);
/*      */ 
/*  693 */       mt.addImage(img, 1);
/*      */ 
/*  695 */       mt.waitForAll();
/*      */     }
/*      */     catch (InterruptedException e)
/*      */     {
/*  699 */       e.printStackTrace();
/*      */     }
/*      */ 
/*  705 */     SkinnedImage image = new SkinnedImage(id, p.x, p.y, img);
/*      */ 
/*  711 */     return image;
/*      */   }
/*      */ 
/*      */   private SkinnedList loadList(Element elem)
/*      */   {
/*  721 */     String id = elem.getAttribute("id");
/*      */ 
/*  723 */     Point p = parsePoint(elem.getAttribute("pos"));
/*      */ 
/*  725 */     Dimension sz = parseDimension(elem.getAttribute("dimensions"));
/*      */     SkinnedList list;
/*  731 */     if (parseBoolean(elem.getAttribute("popup"), false))
/*      */     {
/*  733 */       list = new SkinnedPopupList(id, p.x, p.y, sz.width, sz.height);
/*      */     }
/*      */     else
/*      */     {
/*  737 */       list = new SkinnedList(id, p.x, p.y, sz.width, sz.height);
/*      */     }
/*      */ 
/*  741 */     String fontName = elem.getAttribute("font");
/*      */ 
/*  743 */     if (fontName == null)
/*      */     {
/*  745 */       list.setFont(this.layoutFont);
/*      */     }
/*      */     else
/*      */     {
/*  749 */       list.setFont((Font)this.fontMap.get(fontName));
/*      */     }
/*  751 */     list.setHighlight(parseColor(elem.getAttribute("highlight-background")), parseColor(elem.getAttribute("highlight-foreground")));
/*      */ 
/*  757 */     list.setTextColor(parseColor(elem.getAttribute("text-foreground")));
/*      */ 
/*  759 */     list.setLineHeight(parseInteger(elem.getAttribute("line-height"), 12));
/*      */ 
/*  761 */     list.setHighlightHanging(parseInteger(elem.getAttribute("highlight-hanging"), 2));
/*      */ 
/*  765 */     return list;
/*      */   }
/*      */ 
/*      */   private SkinnedScrollbar loadScrollbar(Element elem)
/*      */   {
/*  775 */     String id = elem.getAttribute("id");
/*      */ 
/*  777 */     Point p = parsePoint(elem.getAttribute("pos"));
/*      */ 
/*  779 */     Dimension sz = parseDimension(elem.getAttribute("dimensions"));
/*      */ 
/*  783 */     SkinnedScrollbar scrollbar = new SkinnedScrollbar(id, p.x, p.y, sz.width, sz.height);
/*      */ 
/*  787 */     scrollbar.setUnit(parseInteger(elem.getAttribute("unit"), 1));
/*      */ 
/*  789 */     scrollbar.setColor(parseColor(elem.getAttribute("color")));
/*      */ 
/*  793 */     String listName = elem.getAttribute("list");
/*      */ 
/*  795 */     if (listName != null)
/*      */     {
/*  797 */       scrollbar.setList((SkinnedList)this.widgetMap.get(listName));
/*      */     }
/*      */ 
/*  801 */     String upButtonName = elem.getAttribute("button-up");
/*      */ 
/*  803 */     if (upButtonName != null)
/*      */     {
/*  805 */       scrollbar.setButtonUp((SkinnedButton)this.widgetMap.get(upButtonName));
/*      */     }
/*      */ 
/*  809 */     String downButtonName = elem.getAttribute("button-down");
/*      */ 
/*  811 */     if (downButtonName != null)
/*      */     {
/*  813 */       scrollbar.setButtonDown((SkinnedButton)this.widgetMap.get(downButtonName));
/*      */     }
/*      */ 
/*  819 */     return scrollbar;
/*      */   }
/*      */ 
/*      */   private SkinnedSlider loadSlider(Element elem)
/*      */   {
/*  829 */     String id = elem.getAttribute("id");
/*      */ 
/*  831 */     Point p = parsePoint(elem.getAttribute("pos"));
/*      */ 
/*  833 */     Dimension sz = parseDimension(elem.getAttribute("dimensions"));
/*      */ 
/*  837 */     SkinnedSlider slider = new SkinnedSlider(id, p.x, p.y, sz.width, sz.height);
/*      */ 
/*  841 */     slider.setColor(parseColor(elem.getAttribute("color")));
/*      */ 
/*  843 */     slider.setMinimum(parseInteger(elem.getAttribute("min"), 0));
/*      */ 
/*  845 */     slider.setMaximum(parseInteger(elem.getAttribute("max"), 100));
/*      */ 
/*  847 */     slider.setVisibleAmount(parseInteger(elem.getAttribute("size"), 10));
/*      */ 
/*  851 */     return slider;
/*      */   }
/*      */ 
/*      */   private SkinnedProgress loadProgress(Element elem)
/*      */   {
/*  861 */     String id = elem.getAttribute("id");
/*      */ 
/*  863 */     Point p = parsePoint(elem.getAttribute("pos"));
/*      */ 
/*  865 */     Dimension sz = parseDimension(elem.getAttribute("dimensions"));
/*      */ 
/*  869 */     SkinnedProgress progress = new SkinnedProgress(id, p.x, p.y, sz.width, sz.height);
/*      */ 
/*  873 */     progress.setColor(parseColor(elem.getAttribute("color")));
/*      */ 
/*  877 */     return progress;
/*      */   }
/*      */ 
/*      */   private SkinnedPlaceholder loadPlaceholder(Element elem)
/*      */   {
/*  887 */     String id = elem.getAttribute("id");
/*      */ 
/*  889 */     Point p = parsePoint(elem.getAttribute("pos"));
/*      */ 
/*  891 */     Dimension sz = parseDimension(elem.getAttribute("dimensions"));
/*      */ 
/*  895 */     SkinnedPlaceholder progress = new SkinnedPlaceholder(id, p.x, p.y, sz.width, sz.height);
/*      */ 
/*  899 */     NamedNodeMap attrs = elem.getAttributes();
/*      */ 
/*  901 */     for (int i = 0; i < attrs.getLength(); i++)
/*      */     {
/*  905 */       Attr n = (Attr)attrs.item(i);
/*      */ 
/*  907 */       progress.addAttribute(n.getName(), n.getValue());
/*      */     }
/*      */ 
/*  913 */     return progress;
/*      */   }
/*      */ 
/*      */   private Image parseImage(String filename)
/*      */   {
/*      */     try
/*      */     {
/*  925 */       InputStream is = getClass().getResourceAsStream(filename);
/*      */ 
/*  927 */       BufferedInputStream bis = new BufferedInputStream(is);
/*      */ 
/*  929 */       ByteArrayOutputStream os = new ByteArrayOutputStream();
/*      */ 
/*  933 */       byte[] buf = new byte[8192];
/*      */       int len;
/*  935 */       while ((len = bis.read(buf, 0, buf.length)) > 0)
/*      */       {
/*  937 */         os.write(buf, 0, len);
/*      */       }
/*  939 */       buf = os.toByteArray();
/*      */ 
/*  943 */       Toolkit tk = Toolkit.getDefaultToolkit();
/*      */ 
/*  945 */       return tk.createImage(buf);
/*      */     }
/*      */     catch (IOException e)
/*      */     {
/*  951 */       if ((filename != null) && (filename.length() > 0))
/*      */       {
/*  953 */         System.out.println("Missing resource " + filename);
/*      */       }
/*      */     }
/*      */ 
/*  957 */     return null;
/*      */   }
/*      */ 
/*      */   private boolean parseBoolean(String b, boolean def)
/*      */   {
/*  967 */     if ((b == null) || (b.length() == 0))
/*      */     {
/*  969 */       return def;
/*      */     }
/*      */ 
/*  973 */     b = b.toLowerCase();
/*      */ 
/*  975 */     return (b.equals("true")) || (b.equals("on")) || (b.equals("yes"));
/*      */   }
/*      */ 
/*      */   private boolean parseBoolean(String b)
/*      */   {
/*  985 */     return parseBoolean(b, false);
/*      */   }
/*      */ 
/*      */   private int parseInteger(String i, int def)
/*      */   {
/*  995 */     if (i == null)
/*      */     {
/*  997 */       return def;
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/* 1003 */       return Integer.parseInt(i);
/*      */     }
/*      */     catch (NumberFormatException e) {
/*      */     }
/* 1007 */     return def;
/*      */   }
/*      */ 
/*      */   private float parseFloat(String i, float def)
/*      */   {
/* 1017 */     if (i == null)
/*      */     {
/* 1019 */       return def;
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/* 1025 */       return Float.parseFloat(i);
/*      */     }
/*      */     catch (NumberFormatException e) {
/*      */     }
/* 1029 */     return def;
/*      */   }
/*      */ 
/*      */   private Point parsePoint(String p)
/*      */   {
/* 1039 */     if (p == null)
/*      */     {
/* 1041 */       return new Point(0, 0);
/*      */     }
/*      */ 
/* 1045 */     int x = 0; int y = 0;
/*      */     try
/*      */     {
/* 1049 */       String[] xy = p.split(",");
/*      */ 
/* 1051 */       x = Integer.parseInt(xy[0]);
/*      */ 
/* 1053 */       y = Integer.parseInt(xy[1]);
/*      */     }
/*      */     catch (NumberFormatException e)
/*      */     {
/*      */     }
/*      */ 
/* 1059 */     return new Point(x, y);
/*      */   }
/*      */ 
/*      */   private Dimension parseDimension(String p)
/*      */   {
/* 1069 */     if (p == null)
/*      */     {
/* 1071 */       return new Dimension(0, 0);
/*      */     }
/*      */ 
/* 1075 */     int x = 0; int y = 0;
/*      */     try
/*      */     {
/* 1079 */       String[] xy = p.split("x");
/*      */ 
/* 1081 */       x = Integer.parseInt(xy[0]);
/*      */ 
/* 1083 */       y = Integer.parseInt(xy[1]);
/*      */     }
/*      */     catch (NumberFormatException e)
/*      */     {
/*      */     }
/*      */ 
/* 1089 */     return new Dimension(x, y);
/*      */   }
/*      */ 
/*      */   private Color parseColor(String c)
/*      */   {
/* 1099 */     if (c == null)
/*      */     {
/* 1101 */       return Color.black;
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/* 1107 */       return new Color(Integer.parseInt(c, 16));
/*      */     }
/*      */     catch (NumberFormatException e)
/*      */     {
/*      */     }
/*      */ 
/* 1113 */     return Color.black;
/*      */   }
/*      */ 
/*      */   public Layer getLayer(String id)
/*      */   {
/* 1123 */     return (Layer)this.layerMap.get(id);
/*      */   }
/*      */ 
/*      */   public Widget getWidget(String id)
/*      */   {
/* 1133 */     return (Widget)this.widgetMap.get(id);
/*      */   }
/*      */ 
/*      */   public SkinnedButton getButton(String id)
/*      */   {
/* 1143 */     return (SkinnedButton)this.widgetMap.get(id);
/*      */   }
/*      */ 
/*      */   public SkinnedList getList(String id)
/*      */   {
/* 1153 */     return (SkinnedList)this.widgetMap.get(id);
/*      */   }
/*      */ 
/*      */   public SkinnedLabel getLabel(String id)
/*      */   {
/* 1163 */     return (SkinnedLabel)this.widgetMap.get(id);
/*      */   }
/*      */ 
/*      */   public SkinnedProgress getProgress(String id)
/*      */   {
/* 1173 */     return (SkinnedProgress)this.widgetMap.get(id);
/*      */   }
/*      */ 
/*      */   public SkinnedSlider getSlider(String id)
/*      */   {
/* 1183 */     return (SkinnedSlider)this.widgetMap.get(id);
/*      */   }
/*      */ 
/*      */   public SkinnedPlaceholder getPlaceholder(String id)
/*      */   {
/* 1193 */     return (SkinnedPlaceholder)this.widgetMap.get(id);
/*      */   }
/*      */ 
/*      */   public void invalidate(int x, int y, int width, int height)
/*      */   {
/* 1203 */     if (this.invalid != null)
/*      */     {
/* 1205 */       this.invalid = this.invalid.union(new Rectangle(x, y, width, height));
/*      */     }
/*      */     else
/*      */     {
/* 1209 */       this.invalid = new Rectangle(x, y, width, height);
/*      */     }
/* 1211 */     repaint();
/*      */   }
/*      */ 
/*      */   public void invalidate()
/*      */   {
/* 1221 */     this.invalid = new Rectangle(0, 0, getWidth(), getHeight());
/*      */ 
/* 1223 */     repaint();
/*      */   }
/*      */ 
/*      */   public void mouseClicked(MouseEvent e)
/*      */   {
/* 1235 */     handleMouseEvent(e);
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
/*      */   public void mousePressed(MouseEvent e)
/*      */   {
/* 1261 */     handleMouseEvent(e);
/*      */   }
/*      */ 
/*      */   public void mouseReleased(MouseEvent e)
/*      */   {
/* 1271 */     handleMouseEvent(e);
/*      */   }
/*      */ 
/*      */   public void mouseMoved(MouseEvent e)
/*      */   {
/* 1281 */     handleMouseEvent(e);
/*      */   }
/*      */ 
/*      */   public void mouseDragged(MouseEvent e)
/*      */   {
/* 1291 */     handleMouseEvent(e);
/*      */   }
/*      */ 
/*      */   public void mouseWheelMoved(MouseWheelEvent e)
/*      */   {
/* 1301 */     handleMouseEvent(e);
/*      */   }
/*      */ 
/*      */   private void handleMouseEvent(MouseEvent e)
/*      */   {
/*      */     try
/*      */     {
/* 1313 */       if (this.capture != null)
/*      */       {
/* 1317 */         if (this.capture.hitTest(e))
/*      */         {
/* 1319 */           enteredWidget(e, this.capture);
/*      */         }
/*      */         else
/*      */         {
/* 1323 */           enteredWidget(e, null);
/*      */         }
/*      */ 
/* 1327 */         this.capture.mouseEvent(e);
/*      */       }
/*      */       else
/*      */       {
/* 1335 */         for (int i = this.layers.size() - 1; i >= 0; i--)
/*      */         {
/* 1339 */           Layer layer = (Layer)this.layers.get(i);
/*      */ 
/* 1341 */           if ((layer.getVisible()) && (layer.mouseEvent(e)))
/*      */           {
/*      */             break;
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */     catch (Throwable t)
/*      */     {
/* 1351 */       notifyExceptionOccured(t);
/*      */     }
/*      */   }
/*      */ 
/*      */   void setCapture(Widget w)
/*      */   {
/* 1363 */     this.capture = w;
/*      */   }
/*      */ 
/*      */   void releaseCapture(Widget w)
/*      */   {
/* 1373 */     if (this.capture == w)
/*      */     {
/* 1375 */       this.capture = null;
/*      */     }
/*      */   }
/*      */ 
/*      */   void enteredWidget(MouseEvent e, Widget w)
/*      */   {
/* 1385 */     if (w != this.lastEntered)
/*      */     {
/* 1389 */       if (this.lastEntered != null)
/*      */       {
/* 1393 */         this.lastEntered.mouseEvent(new MouseEvent(this.lastEntered, 505, e.getWhen(), e.getModifiers(), e.getX(), e.getY(), e.getClickCount(), false));
/*      */       }
/*      */ 
/* 1407 */       if (w != null)
/*      */       {
/* 1411 */         w.mouseEvent(new MouseEvent(w, 504, e.getWhen(), e.getModifiers(), e.getX(), e.getY(), e.getClickCount(), false));
/*      */       }
/*      */ 
/* 1425 */       this.lastEntered = w;
/*      */     }
/*      */   }
/*      */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelibskin.Skin
 * JD-Core Version:    0.6.2
 */