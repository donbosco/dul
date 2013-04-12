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
/*    */ public class DuluxStatusCodeReaderSAX extends DefaultHandler
/*    */ {
/* 14 */   private CharArrayWriter contents = new CharArrayWriter();
/* 15 */   private static String currentElement = "";
/* 16 */   private static String statusCode = "";
/*    */ 
/*    */   public void startElement(String namespaceURI, String localName, String qName, Attributes attr)
/*    */     throws SAXException
/*    */   {
/* 27 */     if (localName.equalsIgnoreCase("StatusCode")) {
/* 28 */       currentElement = "StatusCode";
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
/* 49 */       if (currentElement.equalsIgnoreCase("StatusCode")) {
/* 50 */         statusCode = new String(ch, start, length);
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
/* 61 */       DuluxStatusCodeReaderSAX msa = new DuluxStatusCodeReaderSAX();
/* 62 */       xr.setContentHandler(msa);
/*    */ 
/* 64 */       xr.parse(new InputSource(new StringReader(input)));
/*    */     }
/*    */     catch (Exception e) {
/* 67 */       e.printStackTrace();
/*    */     }
/* 69 */     return statusCode.trim();
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     dulux.sax.DuluxStatusCodeReaderSAX
 * JD-Core Version:    0.6.2
 */