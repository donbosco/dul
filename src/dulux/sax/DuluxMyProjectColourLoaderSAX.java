/*    */ package dulux.sax;
/*    */ 
/*    */ import dulux.DuluxColour;
/*    */ import java.io.CharArrayWriter;
/*    */ import java.io.StringReader;
/*    */ import org.xml.sax.Attributes;
/*    */ import org.xml.sax.InputSource;
/*    */ import org.xml.sax.SAXException;
/*    */ import org.xml.sax.XMLReader;
/*    */ import org.xml.sax.helpers.DefaultHandler;
/*    */ import org.xml.sax.helpers.XMLReaderFactory;
/*    */ 
/*    */ public class DuluxMyProjectColourLoaderSAX extends DefaultHandler
/*    */ {
/* 14 */   private static DuluxColour[] scheme = new DuluxColour[3];
/* 15 */   private CharArrayWriter contents = new CharArrayWriter();
/* 16 */   private int count = 0;
/*    */ 
/*    */   public void startElement(String namespaceURI, String localName, String qName, Attributes attr)
/*    */     throws SAXException
/*    */   {
/* 24 */     if (localName.equals("Colour"))
/*    */       try {
/* 26 */         int r = Integer.parseInt(attr.getValue("R"));
/* 27 */         int g = Integer.parseInt(attr.getValue("G"));
/* 28 */         int b = Integer.parseInt(attr.getValue("B"));
/* 29 */         int id = Integer.parseInt(attr.getValue("ItemID"));
/* 30 */         DuluxColour dColour = new DuluxColour(calculateColour(r, g, b), calculateDisplayColour(r, g, b), attr.getValue("Name"), null, attr.getValue("Chip"), 0, id);
/*    */ 
/* 37 */         scheme[this.count] = dColour;
/*    */ 
/* 39 */         this.count += 1;
/*    */       } catch (Exception e) {
/* 41 */         scheme[this.count] = null;
/* 42 */         this.count += 1;
/*    */       }
/*    */   }
/*    */ 
/*    */   public void endElement(String namespaceURI, String localName, String qName)
/*    */     throws SAXException
/*    */   {
/*    */   }
/*    */ 
/*    */   public void characters(char[] ch, int start, int length)
/*    */     throws SAXException
/*    */   {
/* 56 */     this.contents.write(ch, start, length);
/*    */   }
/*    */ 
/*    */   public DuluxColour[] getMyProjectColours(String input)
/*    */   {
/*    */     try {
/* 62 */       XMLReader xr = XMLReaderFactory.createXMLReader();
/*    */ 
/* 64 */       DuluxMyProjectColourLoaderSAX msa = new DuluxMyProjectColourLoaderSAX();
/* 65 */       xr.setContentHandler(msa);
/*    */ 
/* 67 */       xr.parse(new InputSource(new StringReader(input)));
/*    */     }
/*    */     catch (Exception e)
/*    */     {
/* 72 */       e.printStackTrace();
/* 73 */       return null;
/*    */     }
/* 75 */     return scheme;
/*    */   }
/*    */ 
/*    */   public int calculateColour(int r, int g, int b) {
/* 79 */     int colour = (b & 0xFF) << 16 | (g & 0xFF) << 8 | r & 0xFF;
/*    */ 
/* 81 */     return colour;
/*    */   }
/*    */ 
/*    */   public int calculateDisplayColour(int r, int g, int b) {
/* 85 */     int displayColour = (r & 0xFF) << 16 | (g & 0xFF) << 8 | b & 0xFF;
/*    */ 
/* 87 */     return displayColour;
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     dulux.sax.DuluxMyProjectColourLoaderSAX
 * JD-Core Version:    0.6.2
 */