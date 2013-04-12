/*     */ package dulux.sax;
/*     */ 
/*     */ import dulux.DuluxColour;
/*     */ import java.io.CharArrayWriter;
/*     */ import java.io.StringReader;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.XMLReader;
/*     */ import org.xml.sax.helpers.DefaultHandler;
/*     */ import org.xml.sax.helpers.XMLReaderFactory;
/*     */ 
/*     */ public class DuluxSchemeLoaderSAX extends DefaultHandler
/*     */ {
/*     */   private static DuluxColour[][] scheme;
/*  16 */   private int[] schemes = new int[3];
/*  17 */   private CharArrayWriter contents = new CharArrayWriter();
/*  18 */   private final int SELECTION = 0;
/*  19 */   private final int INTERIOR = 1;
/*  20 */   private final int EXTERIOR = 2;
/*     */   private int group;
/*     */   private int count;
/*     */ 
/*     */   public void startElement(String namespaceURI, String localName, String qName, Attributes attr)
/*     */     throws SAXException
/*     */   {
/*  29 */     if (localName.equals("ChipGroup")) {
/*  30 */       this.schemes[0] = Integer.parseInt(attr.getValue("MasterSchemes"));
/*  31 */       this.schemes[1] = Integer.parseInt(attr.getValue("InteriorSchemes"));
/*  32 */       this.schemes[2] = Integer.parseInt(attr.getValue("ExteriorSchemes"));
/*     */     }
/*  34 */     if (localName.equals("Selection")) {
/*  35 */       this.group = 0;
/*  36 */       this.count = 0;
/*     */     }
/*  38 */     else if (localName.equals("Interior")) {
/*  39 */       this.group = 1;
/*  40 */       this.count = 0;
/*     */     }
/*  42 */     else if (localName.equals("Exterior")) {
/*  43 */       this.group = 2;
/*  44 */       this.count = 0;
/*     */     }
/*  46 */     if ((localName.equals("Chip")) && (this.count != 0) && 
/*  47 */       (this.count != 3) && (this.count < 3)) {
/*  48 */       this.count = 3;
/*     */     }
/*     */ 
/*  52 */     if ((localName.equals("Colour")) && (this.group == 0) && (this.count < 3)) {
/*  53 */       int r = Integer.parseInt(attr.getValue("R"));
/*  54 */       int g = Integer.parseInt(attr.getValue("G"));
/*  55 */       int b = Integer.parseInt(attr.getValue("B"));
/*  56 */       int id = Integer.parseInt(attr.getValue("ItemID"));
/*  57 */       DuluxColour dColour = new DuluxColour(calculateColour(r, g, b), calculateDisplayColour(r, g, b), attr.getValue("Name"), null, attr.getValue("Chip"), 0, id);
/*     */ 
/*  64 */       scheme[0][this.count] = dColour;
/*  65 */       this.count += 1;
/*     */     }
/*  67 */     else if ((localName.equals("Colour")) && (this.group == 1) && (this.count < 6)) {
/*  68 */       int r = Integer.parseInt(attr.getValue("R"));
/*  69 */       int g = Integer.parseInt(attr.getValue("G"));
/*  70 */       int b = Integer.parseInt(attr.getValue("B"));
/*  71 */       int id = Integer.parseInt(attr.getValue("ItemID"));
/*  72 */       DuluxColour dColour = new DuluxColour(calculateColour(r, g, b), calculateDisplayColour(r, g, b), attr.getValue("Name"), null, attr.getValue("Chip"), 0, id);
/*     */ 
/*  79 */       scheme[1][this.count] = dColour;
/*  80 */       this.count += 1;
/*     */     }
/*  82 */     else if ((localName.equals("Colour")) && (this.group == 2) && (this.count < 6)) {
/*  83 */       int r = Integer.parseInt(attr.getValue("R"));
/*  84 */       int g = Integer.parseInt(attr.getValue("G"));
/*  85 */       int b = Integer.parseInt(attr.getValue("B"));
/*  86 */       int id = Integer.parseInt(attr.getValue("ItemID"));
/*  87 */       DuluxColour dColour = new DuluxColour(calculateColour(r, g, b), calculateDisplayColour(r, g, b), attr.getValue("Name"), null, attr.getValue("Chip"), 0, id);
/*     */ 
/*  94 */       scheme[2][this.count] = dColour;
/*  95 */       this.count += 1;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void endElement(String namespaceURI, String localName, String qName)
/*     */     throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void characters(char[] ch, int start, int length)
/*     */     throws SAXException
/*     */   {
/* 113 */     this.contents.write(ch, start, length);
/*     */   }
/*     */ 
/*     */   public DuluxColour[][] getSchemes(String input) {
/*     */     try {
/* 118 */       scheme = new DuluxColour[3][6];
/*     */ 
/* 120 */       XMLReader xr = XMLReaderFactory.createXMLReader();
/*     */ 
/* 122 */       DuluxSchemeLoaderSAX msa = new DuluxSchemeLoaderSAX();
/* 123 */       xr.setContentHandler(msa);
/*     */ 
/* 125 */       xr.parse(new InputSource(new StringReader(input)));
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 130 */       e.printStackTrace();
/*     */     }
/* 132 */     return scheme;
/*     */   }
/*     */   public int calculateColour(int r, int g, int b) {
/* 135 */     int colour = (b & 0xFF) << 16 | (g & 0xFF) << 8 | r & 0xFF;
/*     */ 
/* 137 */     return colour;
/*     */   }
/*     */ 
/*     */   public int calculateDisplayColour(int r, int g, int b) {
/* 141 */     int displayColour = (r & 0xFF) << 16 | (g & 0xFF) << 8 | b & 0xFF;
/*     */ 
/* 143 */     return displayColour;
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     dulux.sax.DuluxSchemeLoaderSAX
 * JD-Core Version:    0.6.2
 */