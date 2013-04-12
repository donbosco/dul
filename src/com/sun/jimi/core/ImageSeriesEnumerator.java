/*     */ package com.sun.jimi.core;
/*     */ 
/*     */ import com.sun.jimi.core.util.JimiUtil;
/*     */ import java.util.Enumeration;
/*     */ 
/*     */ class ImageSeriesEnumerator
/*     */   implements Enumeration
/*     */ {
/*     */   protected JimiReader reader;
/*     */   protected int type;
/*     */   protected static final int IMAGE = 0;
/*     */   protected static final int JIMIIMAGE = 1;
/*     */   protected static final int IMAGEPRODUCER = 2;
/* 850 */   protected boolean loadedFirstImage = false;
/*     */ 
/* 852 */   protected boolean error = false;
/*     */   protected Object prev;
/*     */ 
/*     */   public ImageSeriesEnumerator(JimiReader paramJimiReader, int paramInt)
/*     */   {
/* 863 */     this.reader = paramJimiReader;
/* 864 */     this.type = paramInt;
/*     */   }
/*     */ 
/*     */   public Object createNextElement()
/*     */   {
/* 879 */     this.loadedFirstImage = true;
/* 880 */     if (this.type == 1) {
/*     */       try
/*     */       {
/* 883 */         return this.reader.getNextJimiImage();
/*     */       } catch (JimiException localJimiException1) {
/* 885 */         this.error = true;
/* 886 */         return null;
/*     */       }
/*     */     }
/* 889 */     if (this.type == 0) {
/*     */       try {
/* 891 */         return this.reader.getNextImage();
/*     */       } catch (JimiException localJimiException2) {
/* 893 */         this.error = true;
/* 894 */         return JimiUtil.getErrorImage();
/*     */       }
/*     */     }
/* 897 */     if (this.type == 2) {
/*     */       try {
/* 899 */         return this.reader.getNextImageProducer();
/*     */       } catch (JimiException localJimiException3) {
/* 901 */         this.error = true;
/* 902 */         return JimiUtil.getErrorImageProducer();
/*     */       }
/*     */     }
/* 905 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean hasMoreElements()
/*     */   {
/* 869 */     return (!this.error) && ((!this.loadedFirstImage) || (this.reader.hasMoreElements()));
/*     */   }
/*     */ 
/*     */   public Object nextElement()
/*     */   {
/* 874 */     return createNextElement();
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.ImageSeriesEnumerator
 * JD-Core Version:    0.6.2
 */