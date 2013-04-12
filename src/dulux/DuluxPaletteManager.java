/*     */ package dulux;
/*     */ 
/*     */ import dulux.sax.DuluxColourLoaderSAX;
/*     */ import dulux.sax.DuluxRenderColourLoaderSAX;
/*     */ import dulux.sax.DuluxSchemeLoaderSAX;
/*     */ import dulux.sax.DuluxSpecialtyLoaderSAX;
/*     */ import java.io.PrintStream;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Vector;
/*     */ import org.ksoap2.serialization.PropertyInfo;
/*     */ import org.ksoap2.serialization.SoapObject;
/*     */ import org.ksoap2.serialization.SoapSerializationEnvelope;
/*     */ import org.ksoap2.transport.HttpTransportBasicAuth;
/*     */ 
/*     */ public class DuluxPaletteManager
/*     */ {
/*  23 */   private final String XML_NAMESPACE = "http://www.dulux.com.au";
/*     */   private final String WEB_SERVICE_DOMAIN;
/*     */   private HashMap<String, DuluxColour> chipsString;
/*  30 */   private Map colours = new HashMap();
/*     */   private DuluxErrorLog log;
/*     */   private static String NAMESPACE;
/*     */   private static String SOAP_ACTION_GETHUEGROUPS;
/*     */   private static String SOAP_ACTION_GETCOLOURBYHUE;
/*     */   private static String SOAP_ACTION_SELECTSPECIALITY;
/*     */   private static String SOAP_ACTION_SELECTCHIP;
/*     */   private static String URL;
/*     */   private static final String USER_NAME = "dulux_user";
/*     */   private static final String PASSWORD = "dulux_access";
/*     */   private static final String METHOD_NAME_GETHUEGROUPS = "GetHueGroups";
/*     */   private static final String METHOD_NAME_GETCOLOURBYHUE = "GetColourByHue";
/*     */   private static final String METHOD_NAME_GETSCHEMES = "SelectChip";
/*     */   private static final String METHOD_NAME_SELECTSPECIALITY = "SelectSpeciality";
/*  44 */   private Vector palettes = new Vector();
/*  45 */   private Vector renderPalettes = new Vector();
/*  46 */   private Vector specifiers = new Vector();
/*  47 */   private Map specialties = new HashMap();
/*     */   private SoapObject request;
/*     */   private SoapSerializationEnvelope envelope;
/*     */   private HttpTransportBasicAuth transport;
/*     */   private Map hues;
/*     */   private DuluxColour[][] scheme;
/*  55 */   private String[] specialtyGroups = { "suede", "tuscan", "metallic", "colorBond", "riverRock", "designer", "render", "sprayFast", "quitRust", "garageFloors", "weathershieldGardenShades", "weathershieldRoofandTrim", "whites", "traditionals", "WhitesCONZ" };
/*     */ 
/*  57 */   private String[] coloursOfNZ = { "RedsCONZ", "OrangesCONZ", "YellowsCONZ", "GreensCONZ", "BluesCONZ", "PurplesCONZ", "BrownsCONZ", "NeutralsCONZ" };
/*  58 */   private String[] coloursOfAU = { "Reds", "Oranges", "Yellows", "Greens", "Blues", "Purples", "Browns", "Neutrals" };
/*     */ 
/*     */   public DuluxPaletteManager(String webserviceDomain)
/*     */   {
/*  66 */     this.WEB_SERVICE_DOMAIN = webserviceDomain;
/*  67 */     NAMESPACE = "http://www.dulux.com.au/umbraco/webservices/dulux/";
/*  68 */     URL = this.WEB_SERVICE_DOMAIN + "/umbraco/webservices/dulux/Colour.asmx";
/*     */ 
/*  70 */     SOAP_ACTION_GETHUEGROUPS = NAMESPACE + "GetHueGroups";
/*  71 */     SOAP_ACTION_GETCOLOURBYHUE = NAMESPACE + "GetColourByHue";
/*  72 */     SOAP_ACTION_SELECTSPECIALITY = NAMESPACE + "SelectSpeciality";
/*  73 */     SOAP_ACTION_SELECTCHIP = NAMESPACE + "SelectChip";
/*     */   }
/*     */ 
/*     */   public void loadSpecialties()
/*     */   {
/*     */     try {
/*  79 */       for (int i = 0; i < this.specialtyGroups.length; i++) {
/*  80 */         this.request = new SoapObject(NAMESPACE, "SelectSpeciality");
/*  81 */         String specialtyName = this.specialtyGroups[i];
/*  82 */         this.envelope = new SoapSerializationEnvelope(110);
/*  83 */         this.request.addProperty("specialityName", specialtyName);
/*     */ 
/*  85 */         this.envelope.dotNet = true;
/*  86 */         this.envelope.setOutputSoapObject(this.request);
/*  87 */         addClassMappings(this.envelope, "SelectSpeciality");
/*  88 */         this.transport = new HttpTransportBasicAuth(URL, "dulux_user", "dulux_access");
/*  89 */         this.transport.call(SOAP_ACTION_SELECTSPECIALITY, this.envelope);
/*  90 */         DuluxSpecialtyLoaderSAX specialtyLoader = new DuluxSpecialtyLoaderSAX();
/*     */ 
/*  92 */         this.specialties.put(new String(this.specialtyGroups[i]), specialtyLoader.readXML(this.envelope.getResponse().toString(), specialtyName));
/*  93 */         this.colours.putAll(specialtyLoader.getColours());
/*     */       }
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public void loadColoursOfNZ()
/*     */   {
/* 103 */     for (int i = 0; i < this.coloursOfNZ.length; i++) {
/* 104 */       this.request = new SoapObject(NAMESPACE, "SelectSpeciality");
/* 105 */       String hueName = this.coloursOfNZ[i];
/*     */ 
/* 107 */       this.envelope = new SoapSerializationEnvelope(110);
/* 108 */       this.request.addProperty("specialityName", hueName);
/*     */ 
/* 110 */       this.envelope.dotNet = true;
/* 111 */       this.envelope.setOutputSoapObject(this.request);
/* 112 */       addClassMappings(this.envelope, "SelectSpeciality");
/* 113 */       this.transport = new HttpTransportBasicAuth(URL, "dulux_user", "dulux_access");
/*     */       try
/*     */       {
/* 116 */         this.transport.call(SOAP_ACTION_SELECTSPECIALITY, this.envelope);
/* 117 */         DuluxSpecialtyLoaderSAX specialtyLoader = new DuluxSpecialtyLoaderSAX();
/*     */ 
/* 119 */         this.palettes.add(specialtyLoader.readXML(this.envelope.getResponse().toString(), hueName));
/* 120 */         this.colours.putAll(specialtyLoader.getColours());
/*     */       } catch (Exception e) {
/* 122 */         System.err.println("Web Service Error : " + e);
					e.printStackTrace();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void loadColourWall()
/*     */   {
/* 146 */     for (int i = 0; i < this.coloursOfAU.length; i++) {
/* 147 */       this.request = new SoapObject(NAMESPACE, "GetColourByHue");
/* 148 */       String hueName = this.coloursOfAU[i];
/* 149 */       this.envelope = new SoapSerializationEnvelope(110);
/* 150 */       this.request.addProperty("hueName", hueName);
/* 151 */       this.request.addProperty("finish", "");
/* 152 */       this.envelope.dotNet = true;
/*     */ 
/* 154 */       this.envelope.setOutputSoapObject(this.request);
/* 155 */       addClassMappings(this.envelope, "GetColourByHue");
/* 156 */       this.transport = new HttpTransportBasicAuth(URL, "dulux_user", "dulux_access");
/*     */       try
/*     */       {
/* 159 */         this.transport.call(SOAP_ACTION_GETCOLOURBYHUE, this.envelope);
/* 160 */         DuluxColourLoaderSAX colourLoader = new DuluxColourLoaderSAX();
/* 161 */         this.palettes.add(colourLoader.readXML(this.envelope.getResponse().toString(), hueName));
/* 162 */         this.colours.putAll(colourLoader.getColours());
/*     */       } catch (Exception e) {
/* 164 */         System.err.println("Web Service Error : " + e);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void loadSpecifier()
/*     */   {
/* 186 */     for (int i = 0; i < this.coloursOfAU.length; i++) {
/* 187 */       this.request = new SoapObject(NAMESPACE, "GetColourByHue");
/* 188 */       String hueName = this.coloursOfAU[i];
/* 189 */       this.envelope = new SoapSerializationEnvelope(110);
/* 190 */       this.request.addProperty("hueName", hueName);
/* 191 */       this.request.addProperty("finish", "");
/* 192 */       this.envelope.dotNet = true;
/*     */ 
/* 194 */       this.envelope.setOutputSoapObject(this.request);
/* 195 */       addClassMappings(this.envelope, "GetColourByHue");
/* 196 */       this.transport = new HttpTransportBasicAuth(URL, "dulux_user", "dulux_access");
/*     */       try
/*     */       {
/* 199 */         this.transport.call(SOAP_ACTION_GETCOLOURBYHUE, this.envelope);
/* 200 */         DuluxColourLoaderSAX colourLoader = new DuluxColourLoaderSAX();
/* 201 */         this.specifiers.add(colourLoader.readXML(this.envelope.getResponse().toString(), hueName));
/* 202 */         this.colours.putAll(colourLoader.getColours());
/*     */       } catch (Exception e) {
/* 204 */         System.err.println("Web Service Error : " + e);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void loadRenderColourWall()
/*     */   {
/* 217 */     this.transport = new HttpTransportBasicAuth(URL, "dulux_user", "dulux_access");
/*     */     try
/*     */     {
/* 220 */       for (int i = 0; i < this.hues.size(); i++) {
/* 221 */         this.request = new SoapObject(NAMESPACE, "GetColourByHue");
/* 222 */         String hueName = (String)this.hues.get(Integer.valueOf(i));
/* 223 */         this.envelope = new SoapSerializationEnvelope(110);
/* 224 */         this.request.addProperty("hueName", hueName);
/* 225 */         this.request.addProperty("finish", "RenderMed");
/* 226 */         this.envelope.dotNet = true;
/*     */ 
/* 229 */         this.envelope.setOutputSoapObject(this.request);
/* 230 */         addClassMappings(this.envelope, "GetColourByHue");
/*     */         try
/*     */         {
/* 233 */           this.transport.call(SOAP_ACTION_GETCOLOURBYHUE, this.envelope);
/*     */ 
/* 236 */           DuluxSpecialtyLoaderSAX specialtyLoader = new DuluxSpecialtyLoaderSAX();
/* 237 */           DuluxRenderColourLoaderSAX colourLoader = new DuluxRenderColourLoaderSAX();
/* 238 */           this.renderPalettes.add(colourLoader.readXML(this.envelope.getResponse().toString(), hueName));
/*     */         }
/*     */         catch (Exception e) {
/* 241 */           System.err.println("Web Service Error : " + e);
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (Exception e) {
/* 246 */       System.err.println(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public DuluxColour[][] getScheme(DuluxColour dColour) {
/* 251 */     this.request = new SoapObject(NAMESPACE, "SelectChip");
/* 252 */     this.envelope = new SoapSerializationEnvelope(110);
/* 253 */     this.request.addProperty("colourId", Integer.valueOf(dColour.getId()));
/* 254 */     this.envelope.dotNet = true;
/* 255 */     this.envelope.setOutputSoapObject(this.request);
/* 256 */     addClassMappings(this.envelope, "SelectChip");
/* 257 */     this.transport = new HttpTransportBasicAuth(URL, "dulux_user", "dulux_access");
/*     */     try
/*     */     {
/* 260 */       this.transport.call(SOAP_ACTION_SELECTCHIP, this.envelope);
/* 261 */       String response = this.envelope.getResponse().toString();
/* 262 */       DuluxSchemeLoaderSAX schemeLoader = new DuluxSchemeLoaderSAX();
/* 263 */       this.scheme = schemeLoader.getSchemes(response);
/*     */     } catch (Exception e) {
/* 265 */       System.err.println(e);
/*     */     }
/* 267 */     return this.scheme;
/*     */   }
/*     */ 
/*     */   private void addClassMappings(SoapSerializationEnvelope envelope, String method) {
/* 271 */     createWrappingResultTemplate(envelope, method);
/* 272 */     new DuluxWebserviceResult().register(envelope, NAMESPACE, method + "Result");
/*     */   }
/*     */ 
/*     */   private void createWrappingResultTemplate(SoapSerializationEnvelope envelope, String method)
/*     */   {
/* 277 */     PropertyInfo info = new PropertyInfo();
/* 278 */     info.name = (method + "Result");
/* 279 */     info.type = new DuluxWebserviceResult().getClass();
/*     */ 
/* 281 */     SoapObject template = new SoapObject(NAMESPACE, method + "Result");
/* 282 */     template.addProperty(info, "Returned Info");
/*     */ 
/* 284 */     envelope.addTemplate(template);
/*     */   }
/*     */ 
/*     */   public void setLog(DuluxErrorLog log) {
/* 288 */     this.log = log;
/*     */   }
/*     */ 
/*     */   public Vector getPaletteSet() {
/* 292 */     return this.palettes;
/*     */   }
/*     */ 
/*     */   public DuluxPalette getPalette(int index) {
/* 296 */     return (DuluxPalette)this.palettes.get(index - 1);
/*     */   }
/*     */ 
/*     */   public DuluxPalette getSpecialtyPalette(String name) {
/* 300 */     return (DuluxPalette)this.specialties.get(new String(name));
/*     */   }
/*     */ 
/*     */   public DuluxPalette getRenderPalette(int index) {
/* 304 */     return (DuluxPalette)this.renderPalettes.get(index - 1);
/*     */   }
/*     */ 
/*     */   public DuluxPalette getSpecifierPalette(int index) {
/* 308 */     return (DuluxPalette)this.specifiers.get(index - 1);
/*     */   }
/*     */ 
/*     */   public DuluxColour getColour(int itemId) {
/* 312 */     return (DuluxColour)this.colours.get(Integer.valueOf(itemId));
/*     */   }
/*     */ 
/*     */   public DuluxColour getBase(String chip) {
/* 316 */     return (DuluxColour)this.chipsString.get(new String(chip + "0"));
/*     */   }
/*     */ 
/*     */   public void putExternalColour(int rgba, DuluxColour dcol) {
/* 320 */     this.colours.put(new Integer(rgba), dcol);
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     dulux.DuluxPaletteManager
 * JD-Core Version:    0.6.2
 */