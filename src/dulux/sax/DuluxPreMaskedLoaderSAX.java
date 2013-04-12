/*    */ package dulux.sax;
/*    */ 
/*    */ import dulux.DuluxPreMasked;
/*    */ import java.io.CharArrayWriter;
/*    */ import java.io.InputStream;
/*    */ import org.xml.sax.Attributes;
/*    */ import org.xml.sax.InputSource;
/*    */ import org.xml.sax.SAXException;
/*    */ import org.xml.sax.XMLReader;
/*    */ import org.xml.sax.helpers.DefaultHandler;
/*    */ import org.xml.sax.helpers.XMLReaderFactory;
/*    */ 
/*    */ public class DuluxPreMaskedLoaderSAX extends DefaultHandler
/*    */ {
/* 14 */   private CharArrayWriter contents = new CharArrayWriter();
/* 15 */   private static String currentElement = "";
/*    */   private static DuluxPreMasked[] premasked;
/*    */   private boolean isInterior;
/* 18 */   private int count = 0;
/*    */   private final String DOMAIN;
/*    */ 
/*    */   public DuluxPreMaskedLoaderSAX(String domain)
/*    */   {
/* 22 */     this.DOMAIN = domain;
/*    */   }
/*    */ 
/*    */   public void startElement(String namespaceURI, String localName, String qName, Attributes attr)
/*    */     throws SAXException
/*    */   {
/* 30 */     if (localName.equalsIgnoreCase("premasked")) {
/* 31 */       int interior = Integer.parseInt(attr.getValue("interior"));
/* 32 */       int exterior = Integer.parseInt(attr.getValue("exterior"));
/* 33 */       premasked = new DuluxPreMasked[interior + exterior];
/*    */     }
/* 35 */     if (localName.equalsIgnoreCase("interior")) {
/* 36 */       this.isInterior = true;
/*    */     }
/* 38 */     if (localName.equalsIgnoreCase("exterior")) {
/* 39 */       this.isInterior = false;
/*    */     }
/* 41 */     if (localName.equalsIgnoreCase("dmc")) {
/* 42 */       String id = attr.getValue("id");
/* 43 */       String thumbnail = attr.getValue("thumbnail");
/* 44 */       String address = this.DOMAIN + attr.getValue("location");
/*    */ 
/* 46 */       premasked[this.count] = new DuluxPreMasked(id, thumbnail, address, this.isInterior);
/* 47 */       this.count += 1;
/*    */     }
/*    */ 
/* 50 */     this.contents.reset();
/*    */   }
/*    */ 
/*    */   public void endElement(String namespaceURI, String localName, String qName)
/*    */     throws SAXException
/*    */   {
/* 56 */     currentElement = "";
/*    */   }
/*    */ 
/*    */   public void characters(char[] ch, int start, int length)
/*    */     throws SAXException
/*    */   {
/* 71 */     this.contents.write(ch, start, length);
/*    */   }
/*    */ 
/*    */   public DuluxPreMasked[] readXML(InputStream stream)
/*    */   {
/*    */     try {
/* 77 */       XMLReader xr = XMLReaderFactory.createXMLReader();
/*    */ 
/* 79 */       DuluxPreMaskedLoaderSAX msa = new DuluxPreMaskedLoaderSAX(this.DOMAIN);
/* 80 */       xr.setContentHandler(msa);
/*    */ 
/* 82 */       xr.parse(new InputSource(stream));
/*    */     }
/*    */     catch (Exception e) {
/* 85 */       e.printStackTrace();
/*    */     }
/* 87 */     return premasked;
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     dulux.sax.DuluxPreMaskedLoaderSAX
 * JD-Core Version:    0.6.2
 */