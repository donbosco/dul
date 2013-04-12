/*     */ package dulux;
/*     */ 
/*     */ import dulux.sax.DuluxAddressLoaderSAX;
/*     */ import dulux.sax.DuluxMyProjectColourLoaderSAX;
/*     */ import dulux.sax.DuluxStatusCodeReaderSAX;
/*     */ import java.awt.Color;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.net.URL;
/*     */ import org.kobjects.base64.Base64;
/*     */ import org.ksoap2.serialization.PropertyInfo;
/*     */ import org.ksoap2.serialization.SoapObject;
/*     */ import org.ksoap2.serialization.SoapPrimitive;
/*     */ import org.ksoap2.serialization.SoapSerializationEnvelope;
/*     */ import org.ksoap2.transport.HttpTransportBasicAuth;
/*     */ import pelib.PaintExplorer;
/*     */ 
/*     */ public class DuluxSaveAndLoad
/*     */ {
/*     */   private static final String NAMESPACE = "http://www.dulux.com.au/umbraco/webservices/dulux/";
/*     */   private static final String SOAP_ACTION_SAVE = "http://www.dulux.com.au/umbraco/webservices/dulux/SaveProject";
/*     */   private static final String SOAP_ACTION_LOAD = "http://www.dulux.com.au/umbraco/webservices/dulux/GetMyColourImage";
/*     */   private static final String SOAP_ACTION_CLOSE = "http://www.dulux.com.au/umbraco/webservices/dulux/ApplicationClosed";
/*     */   private static final String SOAP_ACTION_GET = "http://www.dulux.com.au/umbraco/webservices/dulux/GetSchemeData";
/*     */   private static final String METHOD_NAME_SAVE = "SaveProject";
/*     */   private static final String METHOD_NAME_LOAD = "GetMyColourImage";
/*     */   private static final String METHOD_NAME_CLOSE = "ApplicationClosed";
/*     */   private static final String METHOD_NAME_GET = "GetSchemeData";
/*     */   private HttpTransportBasicAuth transport;
/*     */   private final String WEB_SERVICE_DOMAIN;
/*     */   private final String URL;
/*     */   private DuluxColour schemeColour1;
/*     */   private DuluxColour schemeColour2;
/*     */   private DuluxColour schemeColour3;
/*     */   private PaintExplorer explorer;
/*  35 */   private final String USER_NAME = "dulux_user";
/*  36 */   private final String PASSWORD = "dulux_access";
/*     */   private final DuluxLogger logger;
/*     */ 
/*     */   public DuluxSaveAndLoad(String webserviceDomain, PaintExplorer explorer, DuluxLogger logger)
/*     */   {
/*  40 */     this.logger = logger;
/*  41 */     this.explorer = explorer;
/*  42 */     this.WEB_SERVICE_DOMAIN = webserviceDomain;
/*  43 */     this.URL = (this.WEB_SERVICE_DOMAIN + "/umbraco/webservices/dulux/myproject.asmx");
/*     */   }
/*     */ 
/*     */   public void applicationClosed(String sessionId)
/*     */   {
/*  51 */     SoapObject request = new SoapObject("http://www.dulux.com.au/umbraco/webservices/dulux/", "ApplicationClosed");
/*  52 */     SoapSerializationEnvelope envelope2 = new SoapSerializationEnvelope(110);
/*  53 */     request.addProperty("sessionId", sessionId);
/*  54 */     envelope2.setOutputSoapObject(request);
/*  55 */     envelope2.dotNet = true;
/*     */ 
/*  57 */     addClassMappings(envelope2, "ApplicationClosed");
/*     */ 
/*  59 */     this.transport = new HttpTransportBasicAuth(this.URL, "dulux_user", "dulux_access");
/*     */     try
/*     */     {
/*  62 */       this.transport.call("http://www.dulux.com.au/umbraco/webservices/dulux/ApplicationClosed", envelope2);
/*     */     } catch (Exception e) {
/*  64 */       System.err.println("Application closing webservice error: " + e);
/*  65 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public DuluxColour[] getSchemeColours(String sessionId)
/*     */   {
/*  77 */     SoapObject request = new SoapObject("http://www.dulux.com.au/umbraco/webservices/dulux/", "GetSchemeData");
/*  78 */     SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(110);
/*  79 */     request.addProperty("sessionId", sessionId);
/*  80 */     envelope.setOutputSoapObject(request);
/*  81 */     DuluxColour[] colours = new DuluxColour[3];
/*  82 */     envelope.dotNet = true;
/*     */ 
/*  84 */     addClassMappings(envelope, "GetSchemeData");
/*  85 */     this.transport = new HttpTransportBasicAuth(this.URL, "dulux_user", "dulux_access");
/*     */     try {
/*  87 */       this.transport.call("http://www.dulux.com.au/umbraco/webservices/dulux/GetSchemeData", envelope);
/*     */ 
/*  89 */       String response = envelope.getResponse().toString();
/*  90 */       this.logger.log("Retrieve user's Colours : " + response);
/*     */ 
/*  92 */       DuluxMyProjectColourLoaderSAX myProjectLoader = new DuluxMyProjectColourLoaderSAX();
/*  93 */       colours = myProjectLoader.getMyProjectColours(response);
/*  94 */       if ((colours[0] == null) && (colours[1] == null) && (colours[2] == null))
/*     */       {
/* 101 */         return null;
/*     */       }
/* 103 */       if ((colours[0] != null) || (
/* 107 */         (colours[1] != null) || 
/* 111 */         (colours[2] == null)));
/* 115 */       return colours;
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/*     */     }
/*     */ 
/* 125 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean saveProject(String sessionId, int colourId1, int colourId2, int colourId3, byte[] imageFileByteArray, byte[] myColourImageFileByteArray, byte[] myColourConfigFileByteArray)
/*     */   {
/* 145 */     SoapObject request = new SoapObject("http://www.dulux.com.au/umbraco/webservices/dulux/", "SaveProject");
/* 146 */     SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(110);
/* 147 */     envelope.dotNet = true;
/* 148 */     request.addProperty("sessionId", sessionId);
/*     */ 
/* 150 */     request.addProperty("colourId1", Integer.valueOf(colourId1));
/* 151 */     request.addProperty("colourId2", Integer.valueOf(colourId2));
/* 152 */     request.addProperty("colourId3", Integer.valueOf(colourId3));
/*     */ 
/* 154 */     this.logger.log("User's Colours : (" + colourId1 + ", " + colourId2 + ", " + colourId3 + ")\n");
/*     */ 
/* 158 */     request.addProperty("imageFileByteArray", new SoapPrimitive("http://schemas.xmlsoap.org/soap/encoding/", "base64", Base64.encode(imageFileByteArray)));
/* 159 */     request.addProperty("myColourImageFileByteArray", new SoapPrimitive("http://schemas.xmlsoap.org/soap/encoding/", "base64", Base64.encode(myColourImageFileByteArray)));
/* 160 */     request.addProperty("myColourConfigFileByteArray", new SoapPrimitive("http://schemas.xmlsoap.org/soap/encoding/", "base64", Base64.encode(myColourConfigFileByteArray)));
/*     */ 
/* 162 */     request.addProperty("imageFileExtension", "jpg");
/* 163 */     request.addProperty("myColourImageFileExtension", "jpg");
/* 164 */     request.addProperty("myColourConfigFileExtension", "dmc");
/*     */ 
/* 166 */     envelope.setOutputSoapObject(request);
/*     */ 
/* 168 */     this.explorer.progress(100);
/*     */ 
/* 170 */     addClassMappings(envelope, "SaveProject");
/*     */ 
/* 173 */     this.transport = new HttpTransportBasicAuth(this.URL, "dulux_user", "dulux_access");
/*     */     int statusCode;
/*     */     try {
/* 176 */       this.transport.call("http://www.dulux.com.au/umbraco/webservices/dulux/SaveProject", envelope);
/* 177 */       DuluxStatusCodeReaderSAX statusCodeReader = new DuluxStatusCodeReaderSAX();
/* 178 */       String response = envelope.getResponse().toString();
/* 179 */       this.logger.log("Save Project Response : " + response);
/* 180 */       statusCode = Integer.parseInt(statusCodeReader.readXML(response));
/*     */ 
/* 182 */       this.explorer.progress(140);
/* 183 */       this.explorer.progress();
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 187 */       System.err.println("Exception in saving the project");
/* 188 */       System.err.println(e);
/* 189 */       return false;
/*     */     }
/*     */ 
/* 192 */     if (statusCode == 1) {
/* 193 */       return true;
/*     */     }
/* 195 */     return false;
/*     */   }
/*     */ 
/*     */   public InputStream loadProject(String sessionId)
/*     */   {
/* 203 */     SoapObject request = new SoapObject("http://www.dulux.com.au/umbraco/webservices/dulux/", "GetMyColourImage");
/* 204 */     SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(110);
/* 205 */     request.addProperty("sessionId", sessionId);
/*     */ 
/* 207 */     envelope.setOutputSoapObject(request);
/*     */ 
/* 209 */     addClassMappings(envelope, "GetMyColourImage");
/*     */ 
/* 211 */     this.transport = new HttpTransportBasicAuth(this.URL, "dulux_user", "dulux_access");
/* 212 */     envelope.dotNet = true;
/*     */     try {
/* 214 */       this.transport.call("http://www.dulux.com.au/umbraco/webservices/dulux/GetMyColourImage", envelope);
/* 215 */       DuluxAddressLoaderSAX dal = new DuluxAddressLoaderSAX();
/* 216 */       String response = envelope.getResponse().toString();
/* 217 */       this.logger.log("Load Project Response : " + response);
/* 218 */       String myProject = dal.readXML(response);
/* 219 */       if (myProject.length() < 2) {
/* 220 */         return null;
/*     */       }
/* 222 */       if (!myProject.contains(".dmc")) {
/* 223 */         return null;
/*     */       }
/* 225 */       URL address = new URL(myProject);
/*     */ 
/* 227 */       return address.openStream();
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 231 */       System.err.println("++++++++++++++Web Service Error Messages+++++++++++++");
/* 232 */       e.printStackTrace();
/*     */     }
/*     */ 
/* 235 */     return null;
/*     */   }
/*     */ 
/*     */   private DuluxColour[] getSchemeForColour(Color color)
/*     */   {
/* 243 */     return null;
/*     */   }
/*     */ 
/*     */   private void addClassMappings(SoapSerializationEnvelope envelope, String method) {
/* 247 */     createWrappingResultTemplate(envelope, method);
/* 248 */     new DuluxWebserviceResult().register(envelope, "http://www.dulux.com.au/umbraco/webservices/dulux/", method + "Result");
/*     */   }
/*     */ 
/*     */   private void createWrappingResultTemplate(SoapSerializationEnvelope envelope, String method)
/*     */   {
/* 253 */     PropertyInfo info = new PropertyInfo();
/* 254 */     info.name = (method + "Result");
/* 255 */     info.type = new DuluxWebserviceResult().getClass();
/*     */ 
/* 258 */     SoapObject template = new SoapObject("http://www.dulux.com.au/umbraco/webservices/dulux/", method + "Result");
/* 259 */     template.addProperty(info, "Returned Info");
/*     */ 
/* 261 */     envelope.addTemplate(template);
/*     */   }
/*     */ 
/*     */   public int calculateColour(int r, int g, int b) {
/* 265 */     int colour = (b & 0xFF) << 16 | (g & 0xFF) << 8 | r & 0xFF;
/*     */ 
/* 267 */     return colour;
/*     */   }
/*     */ 
/*     */   public int calculateDisplayColour(int r, int g, int b) {
/* 271 */     int displayColour = (r & 0xFF) << 16 | (g & 0xFF) << 8 | b & 0xFF;
/*     */ 
/* 273 */     return displayColour;
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     dulux.DuluxSaveAndLoad
 * JD-Core Version:    0.6.2
 */