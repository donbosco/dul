/*     */ package org.ksoap2.transport;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import org.kobjects.base64.Base64;
/*     */ import org.ksoap2.SoapEnvelope;
/*     */ import org.xmlpull.v1.XmlPullParserException;
/*     */ 
/*     */ public class HttpTransportBasicAuth extends Transport
/*     */ {
/*     */   private String username;
/*     */   private String password;
/*     */ 
/*     */   public HttpTransportBasicAuth(String url, String username, String password)
/*     */   {
/*  51 */     super(url);
/*  52 */     this.username = username;
/*  53 */     this.password = password;
/*     */   }
/*     */ 
/*     */   protected ServiceConnection getServiceConnection() throws IOException {
/*  57 */     ServiceConnectionSE seConnection = new ServiceConnectionSE(this.url);
/*     */ 
/*  59 */     return seConnection;
/*     */   }
/*     */ 
/*     */   protected void addBasicAuthentication(ServiceConnection seConnection) throws IOException {
/*  63 */     if ((this.username != null) && (this.password != null)) {
/*  64 */       StringBuffer buf = new StringBuffer(this.username);
/*  65 */       buf.append(':').append(this.password);
/*  66 */       byte[] raw = buf.toString().getBytes();
/*  67 */       buf.setLength(0);
/*  68 */       buf.append("Basic ");
/*  69 */       Base64.encode(raw, 0, raw.length, buf);
/*  70 */       seConnection.setRequestProperty("Authorization", buf.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void call(String soapAction, SoapEnvelope envelope)
/*     */     throws IOException, XmlPullParserException
/*     */   {
/*  83 */     if (soapAction == null)
/*  84 */       soapAction = "\"\""; byte[] requestData = createRequestData(envelope);
/*  86 */     this.requestDump = (this.debug ? new String(requestData) : null);
/*  87 */     this.responseDump = null;
/*  88 */     ServiceConnection connection = getServiceConnection();
/*  89 */     connection.setRequestProperty("User-Agent", "kSOAP/2.0");
/*  90 */     connection.setRequestProperty("SOAPAction", soapAction);
/*  91 */     connection.setRequestProperty("Content-Type", "text/xml");
/*  92 */     connection.setRequestProperty("Connection", "close");
/*  93 */     connection.setRequestProperty("Content-Length", "" + requestData.length);
/*  94 */     connection.setRequestMethod("POST");
/*  95 */     connection.connect();
/*  96 */     OutputStream os = connection.openOutputStream();
/*  97 */     os.write(requestData, 0, requestData.length);
/*  98 */     os.flush();
/*  99 */     os.close();
/* 100 */     requestData = null;
/*     */     InputStream is;
/*     */     try { connection.connect();
/* 104 */       is = connection.openInputStream();
/*     */     } catch (IOException e) {
/* 106 */       is = connection.getErrorStream();
/* 107 */       if (is == null) {
/* 108 */         connection.disconnect();
/* 109 */         throw e;
/*     */       }
/*     */     }
/* 112 */     if (this.debug) {
/* 113 */       ByteArrayOutputStream bos = new ByteArrayOutputStream();
/* 114 */       byte[] buf = new byte[256];
/*     */       while (true) {
/* 116 */         int rd = is.read(buf, 0, 256);
/* 117 */         if (rd == -1)
/*     */           break;
/* 119 */         bos.write(buf, 0, rd);
/*     */       }
/* 121 */       bos.flush();
/* 122 */       buf = bos.toByteArray();
/* 123 */       this.responseDump = new String(buf);
/* 124 */       is.close();
/* 125 */       is = new ByteArrayInputStream(buf);
/*     */     }
/* 127 */     parseResponse(envelope, is);
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     org.ksoap2.transport.HttpTransportBasicAuth
 * JD-Core Version:    0.6.2
 */