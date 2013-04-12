/*     */ package com.sun.jimi.core;
/*     */ 
/*     */ import com.sun.jimi.core.raster.JimiRasterImage;
/*     */ import com.sun.jimi.core.raster.JimiRasterImageImporter;
/*     */ import com.sun.jimi.core.util.JimiImageFactoryProxy;
/*     */ import com.sun.jimi.core.util.JimiUtil;
/*     */ import com.sun.jimi.core.util.QueuedImageProducerProxy;
/*     */ import java.awt.Image;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.image.ImageProducer;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.net.URL;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
import java.util.Properties;
/*     */ 
/*     */ public class Jimi
/*     */ {
/*     */   public static final int ASYNCHRONOUS = 1;
/*     */   public static final int SYNCHRONOUS = 2;
/*     */   public static final int IN_MEMORY = 4;
/*     */   public static final int VIRTUAL_MEMORY = 8;
/*     */   public static final int ONE_SHOT = 16;
/*     */   protected static int defaultFlags;
/* 975 */   protected static JimiImageFactory memoryFactory = JimiFactoryManager.getMemoryFactory();
/*     */   protected static JimiImageFactory vmemFactory;
/* 976 */   protected static JimiImageFactory oneshotFactory = JimiFactoryManager.getOneshotFactory();
/*     */   static boolean limited;
/*     */   static boolean crippled;
/*     */   static boolean demoversion;
/*     */ 
/*     */   static
/*     */   {
/* 127 */     if (System.getSecurityManager() == null) {
/* 128 */       System.runFinalizersOnExit(true);
/*     */     }
/*     */ 
/* 145 */     defaultFlags = 5;
/*     */ 
/* 964 */     limited = false;
/*     */ 
/* 966 */     crippled = false;
/*     */ 
/* 968 */     demoversion = false;
/*     */ 
/* 974 */     JimiControl.addExtension(new JimiProExtension());
/*     */   }
/*     */ 
/*     */   public static JimiReader createJimiReader(InputStream paramInputStream)
/*     */     throws JimiException
/*     */   {
/* 773 */     return createJimiReader(paramInputStream, defaultFlags);
/*     */   }
/*     */ 
/*     */   public static JimiReader createJimiReader(InputStream paramInputStream, int paramInt)
/*     */     throws JimiException
/*     */   {
/* 845 */     return new JimiReader(getFactory(paramInt), paramInputStream);
/*     */   }
/*     */ 
/*     */   public static JimiReader createJimiReader(String paramString)
/*     */     throws JimiException
/*     */   {
/* 761 */     return createJimiReader(paramString, defaultFlags);
/*     */   }
/*     */ 
/*     */   public static JimiReader createJimiReader(String paramString, int paramInt)
/*     */     throws JimiException
/*     */   {
/* 833 */     return new JimiReader(getFactory(paramInt), paramString);
/*     */   }
/*     */ 
/*     */   public static JimiReader createJimiReader(URL paramURL)
/*     */     throws JimiException
/*     */   {
/* 821 */     return createJimiReader(paramURL, defaultFlags);
/*     */   }
/*     */ 
/*     */   public static JimiReader createJimiReader(URL paramURL, int paramInt)
/*     */     throws JimiException
/*     */   {
/* 857 */     return new JimiReader(getFactory(paramInt), paramURL);
/*     */   }
/*     */ 
/*     */   public static JimiWriter createJimiWriter(String paramString)
/*     */     throws JimiException
/*     */   {
/* 869 */     return new JimiWriter(paramString);
/*     */   }
/*     */ 
/*     */   public static JimiWriter createJimiWriter(String paramString, OutputStream paramOutputStream)
/*     */     throws JimiException
/*     */   {
/* 882 */     return new JimiWriter(paramOutputStream, paramString);
/*     */   }
/*     */ 
/*     */   public static JimiRasterImage createRasterImage(ImageProducer paramImageProducer)
/*     */     throws JimiException
/*     */   {
/* 699 */     return createRasterImage(paramImageProducer, 4);
/*     */   }
/*     */ 
/*     */   public static JimiRasterImage createRasterImage(ImageProducer paramImageProducer, int paramInt)
/*     */     throws JimiException
/*     */   {
/* 713 */     if ((paramImageProducer instanceof QueuedImageProducerProxy)) {
/* 714 */       paramImageProducer = ((QueuedImageProducerProxy)paramImageProducer).getImageProducer();
/*     */     }
/* 716 */     if ((paramImageProducer instanceof JimiRasterImage)) {
/* 717 */       return (JimiRasterImage)paramImageProducer;
/*     */     }
/*     */ 
/* 720 */     return createRasterImage(paramImageProducer, getFactory(paramInt));
/*     */   }
/*     */ 
/*     */   protected static JimiRasterImage createRasterImage(ImageProducer paramImageProducer, JimiImageFactory paramJimiImageFactory)
/*     */     throws JimiException
/*     */   {
/* 926 */     while ((paramJimiImageFactory instanceof JimiImageFactoryProxy)) {
/* 927 */       paramJimiImageFactory = ((JimiImageFactoryProxy)paramJimiImageFactory).getProxiedFactory();
/*     */     }
/* 929 */     return JimiRasterImageImporter.importImage(paramImageProducer, paramJimiImageFactory);
/*     */   }
/*     */ 
/*     */   public static JimiReader createTypedJimiReader(InputStream paramInputStream, String paramString)
/*     */     throws JimiException
/*     */   {
/* 786 */     return createTypedJimiReader(paramInputStream, paramString);
/*     */   }
/*     */ 
/*     */   public static JimiReader createTypedJimiReader(InputStream paramInputStream, String paramString, int paramInt)
/*     */     throws JimiException
/*     */   {
/* 799 */     return new JimiReader(getFactory(paramInt), paramInputStream, paramString);
/*     */   }
/*     */ 
/*     */   public static JimiReader createTypedJimiReader(String paramString)
/*     */     throws JimiException
/*     */   {
/* 733 */     return createTypedJimiReader(paramString, defaultFlags);
/*     */   }
/*     */ 
/*     */   public static JimiReader createTypedJimiReader(String paramString, int paramInt)
/*     */     throws JimiException
/*     */   {
/* 746 */     JimiReader localJimiReader = new JimiReader(getFactory(paramInt));
/* 747 */     localJimiReader.setMimeType(paramString);
/*     */ 
/* 749 */     return localJimiReader;
/*     */   }
/*     */ 
/*     */   public static JimiWriter createTypedJimiWriter(String paramString)
/*     */     throws JimiException
/*     */   {
/* 809 */    return new JimiWriter("", paramString);//TODO: changed null to ""
/*     */   }
/*     */ 
/*     */   public static String[] getDecoderTypes()
/*     */   {
/* 915 */     String[] arrayOfString = new String[JimiControl.mimeToDecoderMap.size()];
/* 916 */     Enumeration localEnumeration = JimiControl.mimeToDecoderMap.keys();
/* 917 */     for (int i = 0; (i < arrayOfString.length) && (localEnumeration.hasMoreElements()); i++) {
/* 918 */       arrayOfString[i] = ((String)localEnumeration.nextElement());
/*     */     }
/* 920 */     return arrayOfString;
/*     */   }
/*     */ 
/*     */   public static String[] getEncoderTypes()
/*     */   {
/* 901 */     String[] arrayOfString = new String[JimiControl.mimeToEncoderMap.size()];
/* 902 */     Enumeration localEnumeration = JimiControl.mimeToEncoderMap.keys();
/* 903 */     for (int i = 0; (i < arrayOfString.length) && (localEnumeration.hasMoreElements()); i++) {
/* 904 */       arrayOfString[i] = ((String)localEnumeration.nextElement());
/*     */     }
/* 906 */     return arrayOfString;
/*     */   }
/*     */ 
/*     */   public static JimiImageFactory getFactory(int paramInt)
/*     */   {
/* 938 */     if ((paramInt & 0x10) != 0) {
/* 939 */       return oneshotFactory;
/*     */     }
/* 941 */     if ((paramInt & 0x8) != 0) {
/* 942 */       if (vmemFactory == null) {
/* 943 */         if (limited)
/* 944 */           vmemFactory = memoryFactory;
/*     */         try
/*     */         {
/* 947 */           Class localClass = Class.forName("com.sun.jimi.core.VMemJimiImageFactory");
/* 948 */           vmemFactory = (JimiImageFactory)localClass.newInstance();
/*     */         }
/*     */         catch (Exception localException)
/*     */         {
/* 953 */           vmemFactory = memoryFactory;
/*     */         }
/*     */       }
/* 956 */       return vmemFactory;
/*     */     }
/*     */ 
/* 959 */     return memoryFactory;
/*     */   }
/*     */ 
/*     */   public static Image getImage(InputStream paramInputStream)
/*     */   {
/* 323 */     return getImage(paramInputStream, defaultFlags);
/*     */   }
/*     */ 
/*     */   public static Image getImage(InputStream paramInputStream, int paramInt)
/*     */   {
/* 334 */     return Toolkit.getDefaultToolkit().createImage(getImageProducer(paramInputStream, paramInt));
/*     */   }
/*     */ 
/*     */   public static Image getImage(InputStream paramInputStream, String paramString)
/*     */   {
/* 344 */     return getImage(paramInputStream, paramString, defaultFlags);
/*     */   }
/*     */ 
/*     */   public static Image getImage(InputStream paramInputStream, String paramString, int paramInt)
/*     */   {
/* 355 */     return Toolkit.getDefaultToolkit().createImage(getImageProducer(paramInputStream, paramString, paramInt));
/*     */   }
/*     */ 
/*     */   public static Image getImage(String paramString)
/*     */   {
/* 190 */     return getImage(paramString, defaultFlags);
/*     */   }
/*     */ 
/*     */   public static Image getImage(String paramString, int paramInt)
/*     */   {
/* 201 */     return Toolkit.getDefaultToolkit().createImage(getImageProducer(paramString, paramInt));
/*     */   }
/*     */ 
/*     */   public static Image getImage(String paramString1, String paramString2)
/*     */   {
/* 377 */     return getImage(paramString1, paramString2, defaultFlags);
/*     */   }
/*     */ 
/*     */   public static Image getImage(String paramString1, String paramString2, int paramInt)
/*     */   {
/* 366 */     return Toolkit.getDefaultToolkit().createImage(getImageProducer(paramString1, paramString2, paramInt));
/*     */   }
/*     */ 
/*     */   public static Image getImage(URL paramURL)
/*     */   {
/* 466 */     return getImage(paramURL, defaultFlags);
/*     */   }
/*     */ 
/*     */   public static Image getImage(URL paramURL, int paramInt)
/*     */   {
/* 477 */     return Toolkit.getDefaultToolkit().createImage(getImageProducer(paramURL, paramInt));
/*     */   }
/*     */ 
/*     */   public static Image getImage(URL paramURL, String paramString)
/*     */   {
/* 456 */     return getImage(paramURL, paramString, defaultFlags);
/*     */   }
/*     */ 
/*     */   public static Image getImage(URL paramURL, String paramString, int paramInt)
/*     */   {
/* 445 */     return Toolkit.getDefaultToolkit().createImage(getImageProducer(paramURL, paramString, paramInt));
/*     */   }
/*     */ 
/*     */   public static ImageProducer getImageProducer(InputStream paramInputStream)
/*     */   {
/* 235 */     return getImageProducer(paramInputStream, defaultFlags);
/*     */   }
/*     */ 
/*     */   public static ImageProducer getImageProducer(InputStream paramInputStream, int paramInt)
/*     */   {
/*     */     try
/*     */     {
/* 247 */       JimiReader localJimiReader = new JimiReader(getFactory(paramInt), paramInputStream);
/* 248 */       localJimiReader.setBlocking(JimiUtil.flagSet(paramInt, 2));
/* 249 */       return localJimiReader.getImageProducer();
/*     */     } catch (JimiException localJimiException) {
/*     */     }
/* 252 */     return JimiUtil.getErrorImageProducer();
/*     */   }
/*     */ 
/*     */   public static ImageProducer getImageProducer(InputStream paramInputStream, String paramString)
/*     */   {
/* 264 */     return getImageProducer(paramInputStream, paramString, defaultFlags);
/*     */   }
/*     */ 
/*     */   public static ImageProducer getImageProducer(InputStream paramInputStream, String paramString, int paramInt)
/*     */   {
/*     */     try
/*     */     {
/* 277 */       JimiReader localJimiReader = new JimiReader(getFactory(paramInt), paramInputStream, paramString);
/* 278 */       localJimiReader.setBlocking(JimiUtil.flagSet(paramInt, 2));
/* 279 */       return localJimiReader.getImageProducer();
/*     */     } catch (JimiException localJimiException) {
/*     */     }
/* 282 */     return JimiUtil.getErrorImageProducer();
/*     */   }
/*     */ 
/*     */   public static ImageProducer getImageProducer(String paramString)
/*     */   {
/* 162 */     return getImageProducer(paramString, defaultFlags);
/*     */   }
/*     */ 
/*     */   public static ImageProducer getImageProducer(String paramString, int paramInt)
/*     */   {
/*     */     try
/*     */     {
/* 174 */       JimiReader localJimiReader = new JimiReader(getFactory(paramInt), paramString);
/* 175 */       localJimiReader.setBlocking(JimiUtil.flagSet(paramInt, 2));
/* 176 */       return localJimiReader.getImageProducer();
/*     */     } catch (JimiException localJimiException) {
/*     */     }
/* 179 */     return JimiUtil.getErrorImageProducer();
/*     */   }
/*     */ 
/*     */   public static ImageProducer getImageProducer(String paramString1, String paramString2)
/*     */   {
/* 294 */     return getImageProducer(paramString1, paramString2, defaultFlags);
/*     */   }
/*     */ 
/*     */   public static ImageProducer getImageProducer(String paramString1, String paramString2, int paramInt)
/*     */   {
/*     */     try
/*     */     {
/* 307 */       JimiReader localJimiReader = new JimiReader(getFactory(paramInt), paramString1, paramString2);
/* 308 */       localJimiReader.setBlocking(JimiUtil.flagSet(paramInt, 2));
/* 309 */       return localJimiReader.getImageProducer();
/*     */     } catch (JimiException localJimiException) {
/*     */     }
/* 312 */     return JimiUtil.getErrorImageProducer();
/*     */   }
/*     */ 
/*     */   public static ImageProducer getImageProducer(URL paramURL)
/*     */   {
/* 387 */     return getImageProducer(paramURL, defaultFlags);
/*     */   }
/*     */ 
/*     */   public static ImageProducer getImageProducer(URL paramURL, int paramInt)
/*     */   {
/*     */     try
/*     */     {
/* 399 */       JimiReader localJimiReader = new JimiReader(getFactory(paramInt), paramURL);
/* 400 */       return localJimiReader.getImageProducer();
/*     */     } catch (JimiException localJimiException) {
/*     */     }
/* 403 */     return JimiUtil.getErrorImageProducer();
/*     */   }
/*     */ 
/*     */   public static ImageProducer getImageProducer(URL paramURL, String paramString)
/*     */   {
/* 433 */     return getImageProducer(paramURL, paramString, defaultFlags);
/*     */   }
/*     */ 
/*     */   public static ImageProducer getImageProducer(URL paramURL, String paramString, int paramInt)
/*     */   {
/*     */     try
/*     */     {
/* 417 */       JimiReader localJimiReader = new JimiReader(getFactory(paramInt), paramURL, paramString);
/* 418 */       return localJimiReader.getImageProducer();
/*     */     } catch (JimiException localJimiException) {
/*     */     }
/* 421 */     return JimiUtil.getErrorImageProducer();
/*     */   }
/*     */ 
/*     */   public static JimiRasterImage getRasterImage(InputStream paramInputStream)
/*     */     throws JimiException
/*     */   {
/* 556 */     return getRasterImage(paramInputStream, defaultFlags);
/*     */   }
/*     */ 
/*     */   public static JimiRasterImage getRasterImage(InputStream paramInputStream, int paramInt)
/*     */     throws JimiException
/*     */   {
/* 562 */     JimiReader localJimiReader = new JimiReader(getFactory(paramInt), paramInputStream);
/* 563 */     return localJimiReader.getRasterImage();
/*     */   }
/*     */ 
/*     */   public static JimiRasterImage getRasterImage(InputStream paramInputStream, String paramString)
/*     */     throws JimiException
/*     */   {
/* 536 */     return getRasterImage(paramInputStream, paramString, defaultFlags);
/*     */   }
/*     */ 
/*     */   public static JimiRasterImage getRasterImage(InputStream paramInputStream, String paramString, int paramInt)
/*     */     throws JimiException
/*     */   {
/* 549 */     JimiReader localJimiReader = new JimiReader(getFactory(paramInt), paramInputStream, paramString);
/* 550 */     return localJimiReader.getRasterImage();
/*     */   }
/*     */ 
/*     */   public static JimiRasterImage getRasterImage(String paramString)
/*     */     throws JimiException
/*     */   {
/* 211 */     return getRasterImage(paramString, defaultFlags);
/*     */   }
/*     */ 
/*     */   public static JimiRasterImage getRasterImage(String paramString, int paramInt)
/*     */     throws JimiException
/*     */   {
/* 223 */     JimiReader localJimiReader = new JimiReader(getFactory(paramInt), paramString);
/* 224 */     localJimiReader.setBlocking(JimiUtil.flagSet(paramInt, 2));
/* 225 */     return localJimiReader.getRasterImage();
/*     */   }
/*     */ 
/*     */   public static JimiRasterImage getRasterImage(URL paramURL)
/*     */     throws JimiException
/*     */   {
/* 487 */     return getRasterImage(paramURL, defaultFlags);
/*     */   }
/*     */ 
/*     */   public static JimiRasterImage getRasterImage(URL paramURL, int paramInt)
/*     */     throws JimiException
/*     */   {
/* 499 */     JimiReader localJimiReader = new JimiReader(getFactory(paramInt), paramURL);
/* 500 */     return localJimiReader.getRasterImage();
/*     */   }
/*     */ 
/*     */   public static JimiRasterImage getRasterImage(URL paramURL, String paramString)
/*     */     throws JimiException
/*     */   {
/* 511 */     return getRasterImage(paramURL, paramString, defaultFlags);
/*     */   }
/*     */ 
/*     */   public static JimiRasterImage getRasterImage(URL paramURL, String paramString, int paramInt)
/*     */     throws JimiException
/*     */   {
/* 523 */     JimiReader localJimiReader = new JimiReader(getFactory(paramInt), paramURL, paramString);
/* 524 */     return localJimiReader.getRasterImage();
/*     */   }
/*     */ 
/*     */   static void init()
/*     */   {
/*     */   }
/*     */ 
/*     */   public static void main(String[] paramArrayOfString)
/*     */   {
/* 988 */     if (paramArrayOfString.length == 0) {
/* 989 */       System.err.println("Usage: Jimi <-version | -encoders | -decoders>");
/*     */     }
/* 991 */     if (paramArrayOfString[0].equals("-version")) {
/*     */       try {
/* 993 */         Properties localProperties = new Properties();
/* 994 */         localProperties.load(Jimi.class.getResourceAsStream("version"));
/* 995 */         System.out.println("Jimi version: " + localProperties.getProperty("jimi.version") + 
/* 996 */           " (build " + localProperties.getProperty("jimi.build") + ")");
/*     */       }
/*     */       catch (Exception localException) {
/* 999 */         System.err.println("Unable to read version information: " + localException.toString());
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/*     */       String[] arrayOfString;
/*     */       int i;
/* 1002 */       if (paramArrayOfString[0].equals("-encoders")) {
/* 1003 */         System.out.println("Supported encoder mimetypes:");
/* 1004 */         arrayOfString = getEncoderTypes();
/* 1005 */         for (i = 0; i < arrayOfString.length; i++) {
/* 1006 */           System.out.println(arrayOfString[i]);
/*     */         }
/*     */       }
/* 1009 */       else if (paramArrayOfString[0].equals("-decoders")) {
/* 1010 */         System.out.println("Supported decoder mimetypes:");
/* 1011 */         arrayOfString = getDecoderTypes();
/* 1012 */         for (i = 0; i < arrayOfString.length; i++)
/* 1013 */           System.out.println(arrayOfString[i]);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void putImage(JimiImage paramJimiImage, String paramString)
/*     */     throws JimiException
/*     */   {
/* 599 */     JimiWriter localJimiWriter = new JimiWriter(paramString);
/* 600 */     localJimiWriter.setSource(paramJimiImage);
/* 601 */     localJimiWriter.putImage(paramString);
/*     */   }
/*     */ 
/*     */   public static void putImage(Image paramImage, String paramString)
/*     */     throws JimiException
/*     */   {
/* 575 */     putImage(paramImage.getSource(), paramString);
/*     */   }
/*     */ 
/*     */   public static void putImage(ImageProducer paramImageProducer, String paramString)
/*     */     throws JimiException
/*     */   {
/* 587 */     putImage(createRasterImage(paramImageProducer), paramString);
/*     */   }
/*     */ 
/*     */   public static void putImage(String paramString, JimiImage paramJimiImage, OutputStream paramOutputStream)
/*     */     throws JimiException
/*     */   {
/* 640 */     JimiWriter localJimiWriter = new JimiWriter(paramOutputStream, paramString);
/* 641 */     localJimiWriter.setSource(paramJimiImage);
/* 642 */     localJimiWriter.putImage(paramOutputStream);
/*     */   }
/*     */ 
/*     */   public static void putImage(String paramString1, JimiImage paramJimiImage, String paramString2)
/*     */     throws JimiException
/*     */   {
/* 655 */     JimiWriter localJimiWriter = new JimiWriter(paramString2, paramString1);
/* 656 */     localJimiWriter.setSource(paramJimiImage);
/* 657 */     localJimiWriter.putImage(paramString2);
/*     */   }
/*     */ 
/*     */   public static void putImage(String paramString, Image paramImage, OutputStream paramOutputStream)
/*     */     throws JimiException
/*     */   {
/* 614 */     putImage(paramString, paramImage.getSource(), paramOutputStream);
/*     */   }
/*     */ 
/*     */   public static void putImage(String paramString1, Image paramImage, String paramString2)
/*     */     throws JimiException
/*     */   {
/* 670 */     JimiWriter localJimiWriter = new JimiWriter(paramString2, paramString1);
/* 671 */     localJimiWriter.setSource(paramImage);
/* 672 */     localJimiWriter.putImage(paramString2);
/*     */   }
/*     */ 
/*     */   public static void putImage(String paramString, ImageProducer paramImageProducer, OutputStream paramOutputStream)
/*     */     throws JimiException
/*     */   {
/* 627 */     putImage(paramString, createRasterImage(paramImageProducer), paramOutputStream);
/*     */   }
/*     */ 
/*     */   public static void putImage(String paramString1, ImageProducer paramImageProducer, String paramString2)
/*     */     throws JimiException
/*     */   {
/* 685 */     JimiWriter localJimiWriter = new JimiWriter(paramString2, paramString1);
/* 686 */     localJimiWriter.setSource(paramImageProducer);
/* 687 */     localJimiWriter.putImage(paramString2);
/*     */   }
/*     */ 
/*     */   public static void setDefaultFlags(int paramInt)
/*     */   {
/* 892 */     defaultFlags = paramInt;
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.Jimi
 * JD-Core Version:    0.6.2
 */