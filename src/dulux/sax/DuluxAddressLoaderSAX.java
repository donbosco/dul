/*    */ package dulux.sax;
/*    */ 
/*    */ import java.io.CharArrayWriter;
/*    */ import java.io.StringReader;
/*    */ import org.xml.sax.Attributes;
/*    */ import org.xml.sax.InputSource;
/*    */ import org.xml.sax.SAXException;
/*    */ import org.xml.sax.XMLReader;
/*    */ import org.xml.sax.helpers.DefaultHandler;
/*    */ import org.xml.sax.helpers.XMLReaderFactory;
/*    */ 
/*    */ public class DuluxAddressLoaderSAX extends DefaultHandler
/*    */ {
/* 14 */   private CharArrayWriter contents = new CharArrayWriter();
/* 15 */   private static String currentElement = "";
/* 16 */   private static String address = "";
/*    */ 
/*    */   public void startElement(String namespaceURI, String localName, String qName, Attributes attr)
/*    */     throws SAXException
/*    */   {
/* 27 */     if (localName.equalsIgnoreCase("ConfigFile")) {
/* 28 */       currentElement = "ConfigFile";
/*    */     }
/*    */ 
/* 31 */     this.contents.reset();
/*    */   }
/*    */ 
/*    */   public void endElement(String namespaceURI, String localName, String qName)
/*    */     throws SAXException
/*    */   {
/* 38 */     currentElement = "";
/*    */   }
/*    */ 
/*    */   public void characters(char[] ch, int start, int length)
/*    */     throws SAXException
/*    */   {
/* 45 */     if (!currentElement.equalsIgnoreCase(""))
/*    */     {
/* 49 */       if (currentElement.equalsIgnoreCase("ConfigFile")) {
/* 50 */         address = new String(ch, start, length);
/*    */       }
/*    */     }
/* 53 */     this.contents.write(ch, start, length);
/*    */   }
/*    */ 
/*    */   public String readXML(String input)
/*    */   {
/*    */     try {
/* 59 */       XMLReader xr = XMLReaderFactory.createXMLReader();
/*    */ 
/* 61 */       DuluxAddressLoaderSAX msa = new DuluxAddressLoaderSAX();
/* 62 */       xr.setContentHandler(msa);
/*    */ 
/* 64 */       xr.parse(new InputSource(new StringReader(input)));
/*    */     }
/*    */     catch (Exception e) {
/* 67 */       e.printStackTrace();
/*    */     }
/* 69 */     return address.trim();
/*    */   }
/*    */ 
/*    */   public int calculateColour(int r, int g, int b) {
/* 73 */     int colour = (b & 0xFF) << 16 | (g & 0xFF) << 8 | r & 0xFF;
/*    */ 
/* 75 */     return colour;
/*    */   }
/*    */ 
/*    */   public int calculateDisplayColour(int r, int g, int b) {
/* 79 */     int displayColour = (r & 0xFF) << 16 | (g & 0xFF) << 8 | b & 0xFF;
/*    */ 
/* 81 */     return displayColour;
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     dulux.sax.DuluxAddressLoaderSAX
 * JD-Core Version:    0.6.2
 */