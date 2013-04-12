/*     */ package dulux;
/*     */ 
/*     */ import java.io.CharArrayWriter;
/*     */ import java.io.FileReader;
/*     */ import java.util.Vector;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.XMLReader;
/*     */ import org.xml.sax.helpers.DefaultHandler;
/*     */ import org.xml.sax.helpers.XMLReaderFactory;
/*     */ 
/*     */ public class DuluxXMLReader extends DefaultHandler
/*     */ {
/*     */   private int colourR;
/*     */   private int colourG;
/*     */   private int colourB;
/*     */   private int column;
/*     */   private String name;
/*     */   private int value;
/*     */   private DuluxPalette palette;
/*     */   private int colour;
/*     */   private int displayColour;
/*     */   private DuluxColour[] schemes;
/*     */   private DuluxColour dCol;
/*     */   private static DuluxPalette dPalette;
/*     */   private static Vector colourVector;
/*     */   private static int rows;
/*     */   private static int columns;
/*  35 */   private CharArrayWriter contents = new CharArrayWriter();
/*     */ 
/*     */   public void startElement(String namespaceURI, String localName, String qName, Attributes attr)
/*     */     throws SAXException
/*     */   {
/*  46 */     if ((localName.equals("Row")) && 
/*  47 */       (rows < Integer.parseInt(attr.getValue("RowNumber")))) {
/*  48 */       rows = Integer.parseInt(attr.getValue("RowNumber"));
/*     */     }
/*  50 */     if (localName.equals("Colour"))
/*     */     {
/*  52 */       this.colourR = Integer.parseInt(attr.getValue("R"));
/*  53 */       this.colourG = Integer.parseInt(attr.getValue("G"));
/*  54 */       this.colourB = Integer.parseInt(attr.getValue("B"));
/*  55 */       this.name = attr.getValue("Name");
/*  56 */       this.column = Integer.parseInt(attr.getValue("Column"));
/*     */ 
/*  58 */       if (this.column > columns)
/*  59 */         columns = this.column;
/*  60 */       this.value = Integer.parseInt(attr.getValue("Value"));
/*  61 */       calculateColour();
/*  62 */       this.dCol = new DuluxColour(this.colour, this.displayColour, this.name, this.schemes, null, 0, 0);
/*     */ 
/*  65 */       this.dCol.palette = dPalette;
/*  66 */       colourVector.add(this.dCol);
/*     */     }
/*     */ 
/*  71 */     this.contents.reset();
/*     */   }
/*     */ 
/*     */   public void endElement(String namespaceURI, String localName, String qName)
/*     */     throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void characters(char[] ch, int start, int length) throws SAXException
/*     */   {
/*  81 */     this.contents.write(ch, start, length);
/*     */   }
/*     */   public void calculateColour() {
/*  84 */     this.colour = ((this.colourB & 0xFF) << 16 | (this.colourG & 0xFF) << 8 | this.colourR & 0xFF);
/*     */ 
/*  86 */     this.displayColour = ((this.colourR & 0xFF) << 16 | (this.colourG & 0xFF) << 8 | this.colourB & 0xFF);
/*     */   }
/*     */ 
/*     */   public DuluxPalette readXML()
/*     */   {
/* 114 */     colourVector = new Vector();
/*     */ 
/* 116 */     dPalette = new DuluxPalette("Loaded Palette", 0, 0, true);
/*     */     try
/*     */     {
/* 121 */       XMLReader xr = XMLReaderFactory.createXMLReader();
/*     */ 
/* 123 */       DuluxXMLReader msa = new DuluxXMLReader();
/* 124 */       xr.setContentHandler(msa);
/*     */ 
/* 126 */       xr.parse(new InputSource(new FileReader("/Test.xml")));
/*     */ 
/* 141 */       dPalette.cols = columns;
/* 142 */       dPalette.rows = rows;
/*     */     }
/*     */     catch (Exception e) {
/* 145 */       e.printStackTrace();
/*     */     }
/*     */ 
/* 148 */     return dPalette;
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     dulux.DuluxXMLReader
 * JD-Core Version:    0.6.2
 */