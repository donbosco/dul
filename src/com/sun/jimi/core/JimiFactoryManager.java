/*    */ package com.sun.jimi.core;
/*    */ 
/*    */ public class JimiFactoryManager
/*    */ {
/*    */   protected static JimiImageFactory memoryFactory;
/*    */   protected static JimiImageFactory oneshotFactory;
/*    */   protected static JimiImageFactory vmemFactory;
/*    */   protected static boolean stampingAvailable;
/*    */   protected static boolean vmemInitialized;
/*    */   protected static final String ONESHOT_FACTORY_NAME = "com.sun.jimi.core.OneshotJimiImageFactory";
/*    */   protected static final String VMEM_FACTORY_NAME = "com.sun.jimi.core.VMemJimiImageFactory";
/*    */ 
/*    */   static
/*    */   {
/*    */     try
/*    */     {
/* 36 */       memoryFactory = new MemoryJimiImageFactory();
/*    */       try
/*    */       {
/* 40 */         Class localClass = Class.forName("com.sun.jimi.core.OneshotJimiImageFactory");
/* 41 */         oneshotFactory = (JimiImageFactory)localClass.newInstance();
/*    */       }
/*    */       catch (Exception localException1) {
/* 44 */         oneshotFactory = memoryFactory;
/*    */       }
/*    */ 
/* 48 */       stampingAvailable = false;
/*    */     }
/*    */     catch (Exception localException2)
/*    */     {
/*    */     }
/*    */   }
/*    */ 
/*    */   public static JimiImageFactory getMemoryFactory()
/*    */   {
/* 59 */     return memoryFactory;
/*    */   }
/*    */ 
/*    */   public static JimiImageFactory getOneshotFactory()
/*    */   {
/* 72 */     return oneshotFactory;
/*    */   }
/*    */ 
/*    */   public static JimiImageFactory getVMemFactory()
/*    */   {
/* 64 */     if (!vmemInitialized) {
/* 65 */       initVMemFactory();
/*    */     }
/* 67 */     return vmemFactory;
/*    */   }
/*    */ 
/*    */   protected static void initVMemFactory()
/*    */   {
/* 77 */     vmemInitialized = true;
/*    */     try {
/* 79 */       Class localClass = Class.forName("com.sun.jimi.core.VMemJimiImageFactory");
/* 80 */       vmemFactory = (JimiImageFactory)localClass.newInstance();
/*    */     }
/*    */     catch (Exception localException) {
/* 83 */       vmemFactory = memoryFactory;
/*    */     }
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.JimiFactoryManager
 * JD-Core Version:    0.6.2
 */