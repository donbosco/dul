/*    */ package com.sun.jimi.core.util.x11;
/*    */ 
/*    */ import java.util.Hashtable;
/*    */ 
/*    */ public class XColorNames
/*    */ {
/*    */   public static final int NOT_FOUND = -2147483648;
/* 27 */   static Hashtable table = new Hashtable(100);
/*    */ 
/* 29 */   static { table.put("black", new Integer(0));
/* 30 */     table.put("dark slate gray", new Integer(3100495));
/* 31 */     table.put("slate gray", new Integer(7372944));
/* 32 */     table.put("gray", new Integer(12500670));
/*    */ 
/* 36 */     table.put("gray20", new Integer(3355443));
/* 37 */     table.put("gray40", new Integer(6710886));
/* 38 */     table.put("gray60", new Integer(10066329));
/* 39 */     table.put("gray80", new Integer(13421772));
/* 40 */     table.put("gray100", new Integer(16777215));
/*    */ 
/* 42 */     table.put("gainsboro", new Integer(14474460));
/* 43 */     table.put("white", new Integer(16777215));
/* 44 */     table.put("purple", new Integer(10494192));
/* 45 */     table.put("magenta", new Integer(16711935));
/* 46 */     table.put("violet", new Integer(15631086));
/* 47 */     table.put("firebrick", new Integer(11674146));
/* 48 */     table.put("red", new Integer(16711680));
/* 49 */     table.put("tomato", new Integer(16737095));
/* 50 */     table.put("orange", new Integer(16753920));
/* 51 */     table.put("gold", new Integer(16766720));
/* 52 */     table.put("yellow", new Integer(16776960));
/* 53 */     table.put("sienna", new Integer(10506797));
/* 54 */     table.put("peru", new Integer(13468991));
/* 55 */     table.put("tan", new Integer(13808840));
/* 56 */     table.put("wheat", new Integer(16113331));
/* 57 */     table.put("lemon chiffon", new Integer(16775885));
/* 58 */     table.put("sea green", new Integer(3050327));
/* 59 */     table.put("lime green", new Integer(3329330));
/* 60 */     table.put("green", new Integer(65280));
/* 61 */     table.put("pale green", new Integer(10025880));
/* 62 */     table.put("navy", new Integer(128));
/* 63 */     table.put("blue", new Integer(255));
/* 64 */     table.put("dodger blue", new Integer(2003199));
/* 65 */     table.put("sky blue", new Integer(8900331));
/* 66 */     table.put("lavender", new Integer(15132410));
/* 67 */     table.put("tan", new Integer(13808780));
/* 68 */     table.put("cyan", new Integer(65535));
/*    */   }
/*    */ 
/*    */   public static int getRgb(String paramString)
/*    */   {
/* 77 */     Object localObject = table.get(paramString);
/* 78 */     if (localObject == null)
/* 79 */       return -2147483648;
/* 80 */     return ((Integer)localObject).intValue();
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.util.x11.XColorNames
 * JD-Core Version:    0.6.2
 */