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
/*     */ public class DuluxColourLoaderSAX extends DefaultHandler
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
/*  36 */       dPalette.rows = (Integer.parseInt(attr.getValue("RowMax")) + 8);
/*  37 */       dPalette.cols = (attr.getValue("ColumnMax").charAt(0) - '@');
/*  38 */       dPalette.colours = new DuluxColour[dPalette.cols][dPalette.rows + 8];
/*     */ 
/*  40 */       dPalette.centerX = 5;
/*  41 */       dPalette.centerY = 3;
/*     */     }
/*  43 */     if (localName.equals("Row")) {
/*  44 */       this.rowIndex = Integer.parseInt(attr.getValue("Index"));
/*  45 */       if (this.rowIndex == 5) {
/*  46 */         this.rowIndex = 9;
/*     */       }
/*  48 */       else if (this.rowIndex == 4)
/*  49 */         this.rowIndex = 8;
/*  50 */       else if (this.rowIndex == 3)
/*  51 */         this.rowIndex = 5;
/*  52 */       else if (this.rowIndex == 6)
/*  53 */         this.rowIndex = 12;
/*     */     }
/*  55 */     if (localName.equals("Chip")) {
/*  56 */       this.colIndex = (attr.getValue("Index").charAt(0) - '@');
/*  57 */       this.newIndex = this.rowIndex;
/*     */     }
/*  59 */     if ((localName.equals("Colour")) && ((this.rowIndex == 1) || (this.rowIndex == 8))) {
/*  60 */       int r = Integer.parseInt(attr.getValue("R"));
/*  61 */       int g = Integer.parseInt(attr.getValue("G"));
/*  62 */       int b = Integer.parseInt(attr.getValue("B"));
/*  63 */       int id = Integer.parseInt(attr.getValue("ItemID"));
/*  64 */       DuluxColour dColour = new DuluxColour(calculateColour(r, g, b), calculateDisplayColour(r, g, b), attr.getValue("Name"), null, attr.getValue("ChipNumber"), 0, id);
/*     */ 
/*  71 */       dPalette.colours[(this.colIndex - 1)][(this.rowIndex - 1)] = dColour;
/*  72 */       colours.put(Integer.valueOf(id), dColour);
/*     */     }
/*  74 */     if ((localName.equals("Colour")) && (this.rowIndex != 1) && (this.rowIndex != 8))
/*     */     {
/*  76 */       int r = Integer.parseInt(attr.getValue("R"));
/*  77 */       int g = Integer.parseInt(attr.getValue("G"));
/*  78 */       int b = Integer.parseInt(attr.getValue("B"));
/*  79 */       int id = Integer.parseInt(attr.getValue("ItemID"));
/*  80 */       DuluxColour dColour = new DuluxColour(calculateColour(r, g, b), calculateDisplayColour(r, g, b), attr.getValue("Name"), null, attr.getValue("ChipNumber"), 0, id);
/*     */ 
/*  88 */       dPalette.colours[(this.colIndex - 1)][(this.newIndex - 1)] = dColour;
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
/* 104 */     return colours;
/*     */   }
/*     */ 
/*     */   public void characters(char[] ch, int start, int length) throws SAXException
/*     */   {
/* 109 */     this.contents.write(ch, start, length);
/*     */   }
/*     */ 
/*     */   public DuluxPalette readXML(String input, String hueName) {
/* 113 */     dPalette = new DuluxPalette(hueName, 0, 0, false);
/*     */     try
/*     */     {
/* 116 */       XMLReader xr = XMLReaderFactory.createXMLReader();
/*     */ 
/* 118 */       DuluxColourLoaderSAX msa = new DuluxColourLoaderSAX();
/* 119 */       xr.setContentHandler(msa);
/*     */ 
/* 121 */       xr.parse(new InputSource(new StringReader(input)));
/*     */     }
/*     */     catch (Exception e) {
/* 124 */       e.printStackTrace();
/*     */     }
/*     */ 
/* 128 */     return dPalette;
/*     */   }
/*     */ 
/*     */   public int calculateColour(int r, int g, int b) {
/* 132 */     int colour = (b & 0xFF) << 16 | (g & 0xFF) << 8 | r & 0xFF;
/*     */ 
/* 134 */     return colour;
/*     */   }
/*     */ 
/*     */   public int calculateDisplayColour(int r, int g, int b) {
/* 138 */     int displayColour = (r & 0xFF) << 16 | (g & 0xFF) << 8 | b & 0xFF;
/*     */ 
/* 140 */     return displayColour;
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     dulux.sax.DuluxColourLoaderSAX
 * JD-Core Version:    0.6.2
 */