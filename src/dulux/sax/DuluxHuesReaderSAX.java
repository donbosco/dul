/*    */ package dulux.sax;
/*    */ 
/*    */ import java.io.CharArrayWriter;
/*    */ import java.io.StringReader;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import org.xml.sax.Attributes;
/*    */ import org.xml.sax.InputSource;
/*    */ import org.xml.sax.SAXException;
/*    */ import org.xml.sax.XMLReader;
/*    */ import org.xml.sax.helpers.DefaultHandler;
/*    */ import org.xml.sax.helpers.XMLReaderFactory;
/*    */ 
/*    */ public class DuluxHuesReaderSAX extends DefaultHandler
/*    */ {
/* 17 */   private static Map hues = new HashMap();
/* 18 */   private CharArrayWriter contents = new CharArrayWriter();
/*    */ 
/*    */   public void startElement(String namespaceURI, String localName, String qName, Attributes attr)
/*    */     throws SAXException
/*    */   {
/* 25 */     if ((!localName.equals("ColourWall")) || 
/* 28 */       (localName.equals("Hue"))) {
/* 29 */       hues.put(Integer.valueOf(Integer.parseInt(attr.getValue("SortOrder"))), attr.getValue("Name"));
/*    */     }
/*    */ 
/* 32 */     this.contents.reset();
/*    */   }
/*    */ 
/*    */   public void endElement(String namespaceURI, String localName, String qName)
/*    */     throws SAXException
/*    */   {
/*    */   }
/*    */ 
/*    */   public void characters(char[] ch, int start, int length) throws SAXException
/*    */   {
/* 42 */     this.contents.write(ch, start, length);
/*    */   }
/*    */ 
/*    */   public Map readXML(String input)
/*    */   {
/*    */     try
/*    */     {
/* 52 */       XMLReader xr = XMLReaderFactory.createXMLReader();
/*    */ 
/* 54 */       DuluxHuesReaderSAX msa = new DuluxHuesReaderSAX();
/* 55 */       xr.setContentHandler(msa);
/*    */ 
/* 57 */       xr.parse(new InputSource(new StringReader(input)));
/*    */     }
/*    */     catch (Exception e)
/*    */     {
/* 62 */       e.printStackTrace();
/*    */     }
/*    */ 
/* 65 */     return hues;
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     dulux.sax.DuluxHuesReaderSAX
 * JD-Core Version:    0.6.2
 */