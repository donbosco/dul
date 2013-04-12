/*     */ package com.sun.jimi.tools;
/*     */ 
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.FilterOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ public class Debug
/*     */ {
/*     */   public static final boolean DEBUG = false;
/*  41 */   private static DataOutputStream logOutput_ = new DataOutputStream(System.err);
/*     */ 
/*     */   public static void jdMethod_assert(boolean paramBoolean, String paramString)
/*     */   {
/*  77 */     if (!paramBoolean)
/*     */     {
/*  79 */       System.err.println(paramString);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void assertNot(boolean paramBoolean, String paramString)
/*     */   {
/*  93 */     jdMethod_assert(paramBoolean ^ true, paramString);
/*     */   }
/*     */ 
/*     */   public static void log(String paramString)
/*     */   {
/*     */     try
/*     */     {
/* 131 */       logOutput_.writeBytes(paramString);
/* 132 */       logOutput_.flush();
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/* 136 */       throw new RuntimeException("Couldn't log: " + paramString);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void print(String paramString)
/*     */   {
/*     */   }
/*     */ 
/*     */   public static void println(String paramString)
/*     */   {
/*     */   }
/*     */ 
/*     */   public static void setLogLocation(OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/* 117 */     logOutput_ = new DataOutputStream(paramOutputStream);
/*     */   }
/*     */ 
/*     */   public static void setLogLocation(String paramString)
/*     */     throws IOException
/*     */   {
/* 105 */     setLogLocation(new FileOutputStream(paramString));
/*     */   }
/*     */ 
/*     */   public static void stopLogging()
/*     */   {
/*     */     try
/*     */     {
/* 149 */       logOutput_.close();
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/* 153 */       throw new RuntimeException("Couldn't close log stream");
/*     */     }
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.tools.Debug
 * JD-Core Version:    0.6.2
 */