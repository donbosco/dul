/*    */ package com.sun.jimi.core;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class VMMControl
/*    */ {
/* 27 */   protected static int threshold = 1048576;
/*    */   protected static File directory;
/*    */ 
/*    */   public static File getDirectory()
/*    */   {
/* 62 */     if (directory == null) {
/* 63 */       directory = new File(System.getProperty("user.dir"));
/*    */     }
/* 65 */     return directory;
/*    */   }
/*    */ 
/*    */   public static void setDirectory(File paramFile)
/*    */   {
/* 46 */     directory = paramFile;
/*    */   }
/*    */ 
/*    */   public static void setDirectory(String paramString)
/*    */     throws IOException
/*    */   {
/* 38 */     if (paramString == null) {
/* 39 */       return;
/*    */     }
/* 41 */     setDirectory(new File(paramString));
/*    */   }
/*    */ 
/*    */   public static void setVMMThreshold(int paramInt)
/*    */   {
/* 57 */     threshold = paramInt;
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.VMMControl
 * JD-Core Version:    0.6.2
 */