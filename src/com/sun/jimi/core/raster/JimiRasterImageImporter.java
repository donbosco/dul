/*    */ package com.sun.jimi.core.raster;
/*    */ 
/*    */ import com.sun.jimi.core.JimiException;
/*    */ import com.sun.jimi.core.JimiImageFactory;
/*    */ import java.awt.image.ImageProducer;
/*    */ 
/*    */ public class JimiRasterImageImporter
/*    */ {
/*    */   public static JimiRasterImage importImage(ImageProducer paramImageProducer, JimiImageFactory paramJimiImageFactory)
/*    */     throws JimiException
/*    */   {
/* 40 */     ImportConsumer localImportConsumer1 = new ImportConsumer(paramJimiImageFactory, paramImageProducer, false);
/* 41 */     localImportConsumer1.startImporting();
/* 42 */     localImportConsumer1.waitFinished();
/* 43 */     if (localImportConsumer1.isAborted()) {
/* 44 */       ImportConsumer localImportConsumer2 = new ImportConsumer(paramJimiImageFactory, paramImageProducer, true);
/* 45 */       localImportConsumer2.startImporting();
/* 46 */       localImportConsumer2.waitFinished();
/* 47 */       if (localImportConsumer2.isAborted()) {
/* 48 */         throw new JimiException("Error creating image.");
/*    */       }
/*    */ 
/* 51 */       return localImportConsumer2.getImage();
/*    */     }
/*    */ 
/* 55 */     return localImportConsumer1.getImage();
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.raster.JimiRasterImageImporter
 * JD-Core Version:    0.6.2
 */