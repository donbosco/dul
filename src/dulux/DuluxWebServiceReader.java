/*     */ package dulux;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.io.PrintStream;
/*     */ import org.ksoap2.serialization.PropertyInfo;
/*     */ import org.ksoap2.serialization.SoapObject;
/*     */ import org.ksoap2.serialization.SoapSerializationEnvelope;
/*     */ import org.ksoap2.transport.HttpTransportSE;
/*     */ 
/*     */ public class DuluxWebServiceReader
/*     */ {
/*     */   private static final String NAMESPACE = "http://www.dulux.com.au/umbraco/webservices/dulux/";
/*     */   private static final String SOAP_ACTION_GET = "http://www.dulux.com.au/umbraco/webservices/dulux/GetSchemeData";
/*     */   private static final String SOAP_ACTION_SET = "dulux/SetSchemeData";
/*     */   private static final String METHOD_NAME_GET = "GetSchemeData";
/*     */   private static final String METHOD_NAME_SET = "SetSchemeData";
/*     */   private static final String URL = "http://duluxtesting.staging.es-i.com.au/umbraco/webservices/dulux/myproject.asmx";
/*     */   private static final int NUMBER_OF_COLOURS = 3;
/*     */   private DuluxColour schemeColour1;
/*     */   private DuluxColour schemeColour2;
/*     */   private DuluxColour schemeColour3;
/*     */   private DuluxColour[] schemes;
/*     */ 
/*     */   public DuluxColour[] getSchemeColours(String sessionId)
/*     */   {
/*  34 */     SoapObject request = new SoapObject("http://www.dulux.com.au/umbraco/webservices/dulux/", "GetSchemeData");
/*  35 */     SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(110);
/*  36 */     request.addProperty("sessionId", sessionId);
/*  37 */     envelope.setOutputSoapObject(request);
/*  38 */     DuluxColour[] colours = new DuluxColour[3];
/*     */ 
/*  41 */     addClassMappings(envelope);
/*     */ 
/*  44 */     HttpTransportSE httpTransportSE = new HttpTransportSE("http://duluxtesting.staging.es-i.com.au/umbraco/webservices/dulux/myproject.asmx");
/*     */     try
/*     */     {
/*  47 */       httpTransportSE.call("http://www.dulux.com.au/umbraco/webservices/dulux/GetSchemeData", envelope);
/*     */ 
/*  50 */       SoapObject schemeColours = (SoapObject)envelope.getResponse();
/*  51 */       for (int i = 1; i < 4; i++) {
/*  52 */         SoapObject colour = (SoapObject)schemeColours.getProperty("colour" + i);
/*  53 */         int r = Integer.parseInt(colour.getProperty("R").toString());
/*  54 */         int g = Integer.parseInt(colour.getProperty("G").toString());
/*  55 */         int b = Integer.parseInt(colour.getProperty("B").toString());
/*  56 */         String name = colour.getProperty("Name").toString();
/*  57 */         if (i == 1)
/*  58 */           this.schemeColour1 = new DuluxColour(calculateColour(r, g, b), calculateDisplayColour(r, g, b), name, null, null, 0, 0);
/*  59 */         else if (i == 2)
/*  60 */           this.schemeColour2 = new DuluxColour(calculateColour(r, g, b), calculateDisplayColour(r, g, b), name, null, null, 0, 0);
/*  61 */         else if (i == 3) {
/*  62 */           this.schemeColour3 = new DuluxColour(calculateColour(r, g, b), calculateDisplayColour(r, g, b), name, null, null, 0, 0);
/*     */         }
/*     */       }
/*     */ 
/*  66 */       colours[0] = this.schemeColour1;
/*  67 */       colours[1] = this.schemeColour2;
/*  68 */       colours[2] = this.schemeColour3;
/*     */     }
/*     */     catch (Exception e) {
/*  71 */       System.err.println("++++++++++++++Web Service Error Messages+++++++++++++");
/*  72 */       e.printStackTrace();
/*  73 */       System.out.println("=== connection to the dulux web service could not be made ===");
/*  74 */       this.schemeColour1 = new DuluxColour(calculateColour(255, 0, 0), calculateDisplayColour(255, 0, 0), "RED", null, null, 0, 0);
/*  75 */       this.schemeColour2 = new DuluxColour(calculateColour(0, 255, 0), calculateDisplayColour(0, 255, 0), "GREEN", null, null, 0, 0);
/*  76 */       this.schemeColour3 = new DuluxColour(calculateColour(0, 0, 255), calculateDisplayColour(0, 0, 255), "BLUE", null, null, 0, 0);
/*  77 */       colours[0] = this.schemeColour1;
/*  78 */       colours[1] = this.schemeColour2;
/*  79 */       colours[2] = this.schemeColour3;
/*  80 */       return colours;
/*     */     }
/*  82 */     return colours;
/*     */   }
/*     */ 
/*     */   public boolean setSchemeColours(DuluxColour colour1, DuluxColour colour2, DuluxColour colour3, int uid) {
/*  86 */     SoapObject request = new SoapObject("http://www.dulux.com.au/umbraco/webservices/dulux/", "SetSchemeData");
/*  87 */     SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(110);
/*  88 */     envelope.setOutputSoapObject(request);
/*  89 */     addClassMappings(envelope);
/*  90 */     request.addProperty("uid", Integer.valueOf(uid));
/*     */ 
/* 105 */     HttpTransportSE httpTransportSE = new HttpTransportSE("http://duluxtesting.staging.es-i.com.au/umbraco/webservices/dulux/myproject.asmx");
/*     */     try
/*     */     {
/* 108 */       httpTransportSE.call("dulux/SetSchemeData", envelope);
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 112 */       System.err.println(e.toString());
/*     */     }
/*     */ 
/* 115 */     return false;
/*     */   }
/*     */ 
/*     */   private void addClassMappings(SoapSerializationEnvelope envelope) {
/* 119 */     createWrappingResultTemplate(envelope);
/* 120 */     new DuluxWebserviceResult().register(envelope, "http://www.dulux.com.au/umbraco/webservices/dulux/", "returnSomethingResult");
/*     */   }
/*     */ 
/*     */   private DuluxColour[] getSchemeForColour(Color color)
/*     */   {
/* 128 */     return null;
/*     */   }
/*     */ 
/*     */   private void createWrappingResultTemplate(SoapSerializationEnvelope envelope)
/*     */   {
/* 133 */     PropertyInfo info = new PropertyInfo();
/* 134 */     info.name = "returnSomethingResult";
/* 135 */     info.type = new DuluxWebserviceResult().getClass();
/*     */ 
/* 145 */     SoapObject template = new SoapObject("http://www.dulux.com.au/umbraco/webservices/dulux/", "returnSomethingResult");
/* 146 */     template.addProperty(info, "Returned HueGroup");
/*     */ 
/* 148 */     envelope.addTemplate(template);
/*     */   }
/*     */ 
/*     */   public int calculateColour(int r, int g, int b) {
/* 152 */     int colour = (b & 0xFF) << 16 | (g & 0xFF) << 8 | r & 0xFF;
/*     */ 
/* 154 */     return colour;
/*     */   }
/*     */ 
/*     */   public int calculateDisplayColour(int r, int g, int b) {
/* 158 */     int displayColour = (r & 0xFF) << 16 | (g & 0xFF) << 8 | b & 0xFF;
/*     */ 
/* 160 */     return displayColour;
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     dulux.DuluxWebServiceReader
 * JD-Core Version:    0.6.2
 */