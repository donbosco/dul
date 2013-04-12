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
/*     */ public class DuluxSpecialtyLoaderSAX extends DefaultHandler
/*     */ {
/*  18 */   private int rowIndex = 0;
/*  19 */   private int colIndex = 0;
/*  20 */   private int newIndex = 0;
/*     */   private static DuluxPalette dPalette;
/*  22 */   private static Map colours = new HashMap();
/*  23 */   private CharArrayWriter contents = new CharArrayWriter();
/*     */ 
/*     */   public void startElement(String namespaceURI, String localName, String qName, Attributes attr)
/*     */     throws SAXException
/*     */   {
/*  30 */     if (localName.equals("HueGroup")) {
/*  31 */       dPalette.rows = Integer.parseInt(attr.getValue("RowMax"));
/*  32 */       dPalette.cols = (attr.getValue("ColumnMax").charAt(0) - '@');
/*  33 */       dPalette.colours = new DuluxColour[dPalette.cols][dPalette.rows];
/*  34 */       dPalette.centerX = 5;
/*  35 */       dPalette.centerY = 3;
/*     */     }
/*  37 */     if (localName.equals("Row")) {
/*  38 */       this.rowIndex = Integer.parseInt(attr.getValue("Index"));
/*     */     }
/*     */ 
/*  41 */     if (localName.equals("Chip")) {
/*  42 */       this.colIndex = (attr.getValue("Index").charAt(0) - '@');
/*     */     }
/*  44 */     if (localName.equals("Colour")) {
/*  45 */       int r = Integer.parseInt(attr.getValue("R"));
/*  46 */       int g = Integer.parseInt(attr.getValue("G"));
/*  47 */       int b = Integer.parseInt(attr.getValue("B"));
/*  48 */       int id = Integer.parseInt(attr.getValue("ItemID"));
/*  49 */       DuluxColour dColour = new DuluxColour(calculateColour(r, g, b), calculateDisplayColour(r, g, b), attr.getValue("Name"), null, attr.getValue("ChipNumber"), 0, id);
/*     */ 
/*  56 */       dPalette.colours[(this.colIndex - 1)][(this.rowIndex - 1)] = dColour;
/*     */ 
/*  58 */       colours.put(Integer.valueOf(id), dColour);
/*     */     }
/*     */ 
/*  63 */     this.contents.reset();
/*     */   }
/*     */ 
/*     */   public void endElement(String namespaceURI, String localName, String qName)
/*     */     throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   public Map getColours()
/*     */   {
/*  74 */     return colours;
/*     */   }
/*     */ 
/*     */   public void characters(char[] ch, int start, int length) throws SAXException
/*     */   {
/*  79 */     this.contents.write(ch, start, length);
/*     */   }
/*     */ 
/*     */   public DuluxPalette readXML(String input, String hueName) {
/*  83 */     dPalette = new DuluxPalette(hueName, 0, 0, false);
/*  84 */     colours = new HashMap();
/*     */     try
/*     */     {
/*  87 */       XMLReader xr = XMLReaderFactory.createXMLReader();
/*     */ 
/*  89 */       DuluxSpecialtyLoaderSAX msa = new DuluxSpecialtyLoaderSAX();
/*  90 */       xr.setContentHandler(msa);
/*     */ 
/*  92 */       xr.parse(new InputSource(new StringReader(input)));
/*     */     }
/*     */     catch (Exception e) {
/*  95 */       e.printStackTrace();
/*     */     }
/*  97 */     return dPalette;
/*     */   }
/*     */ 
/*     */   public int calculateColour(int r, int g, int b) {
/* 101 */     int colour = (b & 0xFF) << 16 | (g & 0xFF) << 8 | r & 0xFF;
/*     */ 
/* 103 */     return colour;
/*     */   }
/*     */ 
/*     */   public int calculateDisplayColour(int r, int g, int b) {
/* 107 */     int displayColour = (r & 0xFF) << 16 | (g & 0xFF) << 8 | b & 0xFF;
/*     */ 
/* 109 */     return displayColour;
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     dulux.sax.DuluxSpecialtyLoaderSAX
 * JD-Core Version:    0.6.2
 */