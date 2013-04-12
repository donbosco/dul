/*     */ package dulux.sax;
/*     */ 
/*     */ import dulux.DuluxColour;
/*     */ import dulux.DuluxPalette;
/*     */ import java.io.CharArrayWriter;
/*     */ import java.io.StringReader;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.XMLReader;
/*     */ import org.xml.sax.helpers.DefaultHandler;
/*     */ import org.xml.sax.helpers.XMLReaderFactory;
/*     */ 
/*     */ public class DuluxRenderColourLoaderSAX extends DefaultHandler
/*     */ {
/*  16 */   private int rowIndex = 0;
/*  17 */   private int colIndex = 0;
/*  18 */   private int newIndex = 0;
/*     */   private static DuluxPalette dPalette;
/*  20 */   private static Map colours = new HashMap();
/*  21 */   private CharArrayWriter contents = new CharArrayWriter();
/*     */ 
/*     */   public void startElement(String namespaceURI, String localName, String qName, Attributes attr)
/*     */     throws SAXException
/*     */   {
/*  35 */     if (localName.equals("HueGroup")) {
/*  36 */       dPalette.rows = (Integer.parseInt(attr.getValue("RowMax")) * 3);
/*  37 */       dPalette.cols = (attr.getValue("ColumnMax").charAt(0) - '@');
/*  38 */       dPalette.colours = new DuluxColour[dPalette.cols][dPalette.rows];
/*     */ 
/*  40 */       dPalette.centerX = 5;
/*  41 */       dPalette.centerY = 3;
/*     */     }
/*  43 */     if (localName.equals("Row")) {
/*  44 */       this.rowIndex = (Integer.parseInt(attr.getValue("Index")) - 1);
/*     */     }
/*     */ 
/*  55 */     if (localName.equals("Chip")) {
/*  56 */       this.colIndex = (attr.getValue("Index").charAt(0) - '@');
/*  57 */       this.newIndex = (this.rowIndex * 3);
/*     */     }
/*     */ 
/*  74 */     if (localName.equals("Colour"))
/*     */     {
/*  76 */       int r = Integer.parseInt(attr.getValue("R"));
/*  77 */       int g = Integer.parseInt(attr.getValue("G"));
/*  78 */       int b = Integer.parseInt(attr.getValue("B"));
/*  79 */       int id = Integer.parseInt(attr.getValue("ItemID"));
/*  80 */       DuluxColour dColour = new DuluxColour(calculateColour(r, g, b), calculateDisplayColour(r, g, b), attr.getValue("Name"), null, attr.getValue("ChipNumber"), 0, id);
/*     */ 
/*  88 */       dPalette.colours[(this.colIndex - 1)][this.newIndex] = dColour;
/*  89 */       colours.put(Integer.valueOf(id), dColour);
/*     */ 
/*  91 */       this.newIndex += 1;
/*     */     }
/*     */ 
/*  94 */     this.contents.reset();
/*     */   }
/*     */ 
/*     */   public void endElement(String namespaceURI, String localName, String qName)
/*     */     throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   public Map getColours()
/*     */   {
/* 105 */     return colours;
/*     */   }
/*     */ 
/*     */   public void characters(char[] ch, int start, int length) throws SAXException
/*     */   {
/* 110 */     this.contents.write(ch, start, length);
/*     */   }
/*     */ 
/*     */   public DuluxPalette readXML(String input, String hueName) {
/* 114 */     dPalette = new DuluxPalette(hueName, 0, 0, false);
/*     */     try
/*     */     {
/* 117 */       XMLReader xr = XMLReaderFactory.createXMLReader();
/*     */ 
/* 119 */       DuluxRenderColourLoaderSAX msa = new DuluxRenderColourLoaderSAX();
/* 120 */       xr.setContentHandler(msa);
/*     */ 
/* 122 */       xr.parse(new InputSource(new StringReader(input)));
/*     */     }
/*     */     catch (Exception e) {
/* 125 */       e.printStackTrace();
/*     */     }
/*     */ 
/* 129 */     return dPalette;
/*     */   }
/*     */ 
/*     */   public int calculateColour(int r, int g, int b) {
/* 133 */     int colour = (b & 0xFF) << 16 | (g & 0xFF) << 8 | r & 0xFF;
/*     */ 
/* 135 */     return colour;
/*     */   }
/*     */ 
/*     */   public int calculateDisplayColour(int r, int g, int b) {
/* 139 */     int displayColour = (r & 0xFF) << 16 | (g & 0xFF) << 8 | b & 0xFF;
/*     */ 
/* 141 */     return displayColour;
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     dulux.sax.DuluxRenderColourLoaderSAX
 * JD-Core Version:    0.6.2
 */