/*     */ package com.sun.jimi.core;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.PushbackInputStream;
/*     */ import java.util.Hashtable;
import java.util.Vector;
/*     */ 
/*     */ public class JimiControl
/*     */ {
/*  26 */   protected static Hashtable mimeToDecoderMap = new Hashtable();
/*  27 */   protected static Hashtable mimeToEncoderMap = new Hashtable();
/*     */ 
/*  29 */   protected static Hashtable fileExtensionToDecoderMap = new Hashtable();
/*  30 */   protected static Hashtable fileExtensionToEncoderMap = new Hashtable();
/*     */ 
/*  32 */   protected static Vector decoderFactories = new Vector();
/*  33 */   protected static Vector encoderFactories = new Vector();
/*     */ 
/*  35 */   protected static Vector extensions = new Vector();
/*     */ 
/*  37 */   protected static boolean extensionsAllowed = true;
/*     */ 
/*     */   protected static void addDecoder(JimiDecoderFactory paramJimiDecoderFactory)
/*     */   {
/* 126 */     decoderFactories.addElement(paramJimiDecoderFactory);
/*     */ 
/* 129 */     String[] arrayOfString = paramJimiDecoderFactory.getMimeTypes();
/* 130 */     for (int i = 0; i < arrayOfString.length; i++) {
/* 131 */       mimeToDecoderMap.put(arrayOfString[i], paramJimiDecoderFactory);
/* 132 */       if (arrayOfString[i].indexOf('/') > 0) {
/* 133 */         String localObject = arrayOfString[i].substring(0, arrayOfString[i].indexOf('/') + 1) + 
/* 134 */           "x-" + 
/* 135 */           arrayOfString[i].substring(arrayOfString[i].indexOf('/') + 1);
/* 136 */         mimeToDecoderMap.put(localObject, paramJimiDecoderFactory);
/*     */       }
/*     */     }
/*     */ 
/* 140 */     String[] localObject  = paramJimiDecoderFactory.getFilenameExtensions();
/* 141 */     for (int j = 0; j < localObject.length; j++)
/* 142 */       fileExtensionToDecoderMap.put(localObject[j], paramJimiDecoderFactory);
/*     */   }
/*     */ 
/*     */   protected static void addEncoder(JimiEncoderFactory paramJimiEncoderFactory)
/*     */   {
/* 148 */     encoderFactories.addElement(paramJimiEncoderFactory);
/*     */ 
/* 151 */     String[] arrayOfString = paramJimiEncoderFactory.getMimeTypes();
/* 152 */     for (int i = 0; i < arrayOfString.length; i++) {
/* 153 */       mimeToEncoderMap.put(arrayOfString[i], paramJimiEncoderFactory);
/* 154 */       if (arrayOfString[i].indexOf('/') > 0) {
/* 155 */         String localObject = arrayOfString[i].substring(0, arrayOfString[i].indexOf('/') + 1) + 
/* 156 */           "x-" + 
/* 157 */           arrayOfString[i].substring(arrayOfString[i].indexOf('/') + 1);
/* 158 */         mimeToEncoderMap.put(localObject, paramJimiEncoderFactory);
/*     */       }
/*     */     }
/*     */ 
/* 162 */     String[] localObject  = paramJimiEncoderFactory.getFilenameExtensions();
/* 163 */     for (int j = 0; j < localObject.length; j++)
/* 164 */       fileExtensionToEncoderMap.put(localObject[j], paramJimiEncoderFactory);
/*     */   }
/*     */ 
/*     */   public static synchronized void addExtension(JimiExtension paramJimiExtension)
/*     */   {
/*  45 */     if (!extensionsAllowed) {
/*  46 */       throw new RuntimeException("This JIMI license does not permit extensions.");
/*     */     }
/*     */ 
/*  49 */     extensions.addElement(paramJimiExtension);
/*     */ 
/*  51 */     JimiDecoderFactory[] arrayOfJimiDecoderFactory = paramJimiExtension.getDecoders();
/*  52 */     for (int i = 0; i < arrayOfJimiDecoderFactory.length; i++) {
/*  53 */       addDecoder(arrayOfJimiDecoderFactory[i]);
/*     */     }
/*     */ 
/*  56 */     JimiEncoderFactory[] arrayOfJimiEncoderFactory = paramJimiExtension.getEncoders();
/*  57 */     for (int j = 0; j < arrayOfJimiEncoderFactory.length; j++)
/*  58 */       addEncoder(arrayOfJimiEncoderFactory[j]);
/*     */   }
/*     */ 
/*     */   protected static void disableExtensions()
/*     */   {
/* 170 */     extensionsAllowed = false;
/*     */   }
/*     */ 
/*     */   protected static JimiDecoderFactory getDecoderByFileExtension(String paramString)
/*     */   {
/*  69 */     String str = paramString;
/*  70 */     if (str.lastIndexOf(".") != -1) {
/*  71 */       str = str.substring(str.lastIndexOf(".") + 1);
/*     */     }
/*  73 */     return (JimiDecoderFactory)fileExtensionToDecoderMap.get(str.toLowerCase());
/*     */   }
/*     */ 
/*     */   protected static JimiDecoderFactory getDecoderByType(String paramString)
/*     */   {
/*  64 */     return (JimiDecoderFactory)mimeToDecoderMap.get(paramString);
/*     */   }
/*     */ 
/*     */   protected static synchronized JimiDecoderFactory getDecoderForInputStream(PushbackInputStream paramPushbackInputStream)
/*     */   {
/*  79 */     byte[] arrayOfByte = new byte[16];
/*     */     try {
/*  81 */       DataInputStream localDataInputStream = new DataInputStream(paramPushbackInputStream);
/*  82 */       localDataInputStream.readFully(arrayOfByte);
/*  83 */       paramPushbackInputStream.unread(arrayOfByte);
/*  84 */       int i = decoderFactories.size();
/*     */ 
/*  86 */       for (int j = 0; j < i; j++) {
/*  87 */         JimiDecoderFactory localJimiDecoderFactory = (JimiDecoderFactory)decoderFactories.elementAt(j);
/*  88 */         byte[][] arrayOfByte1 = localJimiDecoderFactory.getFormatSignatures();
/*  89 */         if (arrayOfByte1 != null)
/*     */         {
/*  92 */           for (int k = 0; k < arrayOfByte1.length; k++) {
/*  93 */             int m = Math.min(arrayOfByte1[k].length, arrayOfByte.length);
/*  94 */             for (int n = 0; n < m; ) {
/*  95 */               if (arrayOfByte1[k][n] != arrayOfByte[n])
/*     */               {
/*     */                 break;
/*     */               }
/*  99 */               return localJimiDecoderFactory;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/* 103 */       return null;
/*     */     } catch (IOException localIOException) {
/*     */     }
/* 106 */     return null;
/*     */   }
/*     */ 
/*     */   protected static JimiEncoderFactory getEncoderByFileExtension(String paramString)
/*     */   {
/* 117 */     String str = paramString;
/* 118 */     if (str.lastIndexOf(".") != -1) {
/* 119 */       str = str.substring(str.lastIndexOf(".") + 1);
/*     */     }
/* 121 */     return (JimiEncoderFactory)fileExtensionToEncoderMap.get(str.toLowerCase());
/*     */   }
/*     */ 
/*     */   protected static JimiEncoderFactory getEncoderByType(String paramString)
/*     */   {
/* 112 */     return (JimiEncoderFactory)mimeToEncoderMap.get(paramString);
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.JimiControl
 * JD-Core Version:    0.6.2
 */