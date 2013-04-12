/*     */ package dulux;
/*     */ 
/*     */ import dulux.sax.DuluxColourLoaderSAX;
/*     */ import dulux.sax.DuluxHuesReaderSAX;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Map;
/*     */ import java.util.Vector;
/*     */ import org.ksoap2.serialization.PropertyInfo;
/*     */ import org.ksoap2.serialization.SoapObject;
/*     */ import org.ksoap2.serialization.SoapSerializationEnvelope;
/*     */ import org.ksoap2.transport.HttpTransportSE;
/*     */ 
/*     */ public class TestLoadFromWebService
/*     */ {
/*     */   private static final String NAMESPACE = "http://www.dulux.com.au/umbraco/webservices/dulux/";
/*     */   private static final String SOAP_ACTION_GETHUEGROUPS = "http://www.dulux.com.au/umbraco/webservices/dulux/GetHueGroups";
/*     */   private static final String SOAP_ACTION_GETCOLOURBYHUE = "http://www.dulux.com.au/umbraco/webservices/dulux/GetColourByHue";
/*     */   private static final String URL = "http://duluxtesting.staging.es-i.com.au/umbraco/webservices/dulux/Colour.asmx";
/*     */   private static final String METHOD_NAME_GETHUEGROUPS = "GetHueGroups";
/*     */   private static final String METHOD_NAME_GETCOLOURBYHUE = "GetColourByHue";
/*  29 */   private Vector palettes = new Vector();
/*     */ 
/*     */   public void loadColourWall()
/*     */   {
/*  37 */     SoapObject request = new SoapObject("http://www.dulux.com.au/umbraco/webservices/dulux/", "GetHueGroups");
/*     */ 
/*  39 */     SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(110);
/*     */ 
/*  41 */     envelope.setOutputSoapObject(request);
/*     */ 
/*  43 */     addClassMappings(envelope, "GetHueGroups");
/*     */ 
/*  45 */     HttpTransportSE httpTransportSE = new HttpTransportSE("http://duluxtesting.staging.es-i.com.au/umbraco/webservices/dulux/Colour.asmx");
/*     */     try
/*     */     {
/*  50 */       httpTransportSE.call("http://www.dulux.com.au/umbraco/webservices/dulux/GetHueGroups", envelope);
/*     */ 
/*  52 */       String temp = envelope.getResponse().toString();
/*     */ 
/*  54 */       DuluxHuesReaderSAX reader = new DuluxHuesReaderSAX();
/*  55 */       Map hues = reader.readXML(temp);
/*     */ 
/*  57 */       for (int i = 0; i < hues.size(); i++) {
/*  58 */         request = new SoapObject("http://www.dulux.com.au/umbraco/webservices/dulux/", "GetColourByHue");
/*  59 */         String hueName = (String)hues.get(Integer.valueOf(i));
/*  60 */         envelope = new SoapSerializationEnvelope(110);
/*  61 */         request.addProperty("hueName", hueName);
/*  62 */         envelope.dotNet = true;
/*     */ 
/*  65 */         envelope.setOutputSoapObject(request);
/*  66 */         addClassMappings(envelope, "GetColourByHue");
/*  67 */         httpTransportSE = new HttpTransportSE("http://duluxtesting.staging.es-i.com.au/umbraco/webservices/dulux/Colour.asmx");
/*     */         try {
/*  69 */           httpTransportSE.call("http://www.dulux.com.au/umbraco/webservices/dulux/GetColourByHue", envelope);
/*  70 */           DuluxColourLoaderSAX colourLoader = new DuluxColourLoaderSAX();
/*     */ 
/*  72 */           this.palettes.add(colourLoader.readXML(envelope.getResponse().toString(), hueName));
/*     */         } catch (Exception e) {
/*  74 */           System.err.println("Web Service Error : " + e);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/*  90 */       System.out.println(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void addClassMappings(SoapSerializationEnvelope envelope, String method)
/*     */   {
/*  96 */     createWrappingResultTemplate(envelope, method);
/*  97 */     new DuluxWebserviceResult().register(envelope, "http://www.dulux.com.au/umbraco/webservices/dulux/", method + "Result");
/*     */   }
/*     */ 
/*     */   private void createWrappingResultTemplate(SoapSerializationEnvelope envelope, String method)
/*     */   {
/* 102 */     PropertyInfo info = new PropertyInfo();
/* 103 */     info.name = (method + "Result");
/* 104 */     info.type = new DuluxWebserviceResult().getClass();
/*     */ 
/* 114 */     SoapObject template = new SoapObject("http://www.dulux.com.au/umbraco/webservices/dulux/", method + "Result");
/* 115 */     template.addProperty(info, "Returned Info");
/*     */ 
/* 117 */     envelope.addTemplate(template);
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     dulux.TestLoadFromWebService
 * JD-Core Version:    0.6.2
 */