/*    */ package com.sun.jimi.core.util;
/*    */ 
/*    */ import java.awt.Canvas;
/*    */ import java.awt.Component;
/*    */ import java.awt.Image;
/*    */ import java.awt.MediaTracker;
import java.awt.image.ImageObserver;
/*    */ 
/*    */ public class GraphicsUtils
/*    */ {
/*    */   protected static Canvas waitComponent;
/*    */ 
/*    */   public static void waitForImage(Component paramComponent, Image paramImage)
/*    */   {
/* 28 */     MediaTracker localMediaTracker = new MediaTracker(paramComponent);
/* 29 */     localMediaTracker.addImage(paramImage, 0);
/*    */     try { localMediaTracker.waitForAll(); }
/*    */     catch (InterruptedException localInterruptedException)
/*    */     {
/*    */     }
/*    */   }
/*    */ 
/*    */   public static void waitForImage(Component paramComponent, final Image paramImage, ImageObserver paramImageObserver)
/*    */   {
	/* 39 */     MediaTracker localMediaTracker = new MediaTracker(paramComponent);
	/* 40 */     Image localImage = paramImage;
	/* 41 */     ImageObserver localImageObserver = paramImageObserver;
	/* 42 */     Component localComponent = paramComponent;
	/*    */ 
	/* 44 */     new Thread(new Runnable() { 
					private final ImageObserver val$imageobserver=null;
	/*    */       private final MediaTracker val$mt=null;
	/*    */ 
	/* 47 */       public void run() { 
						this.val$mt.addImage(paramImage, 0);
						try { 
							this.val$mt.waitForAll(); 
						}catch (InterruptedException localInterruptedException) { 
							
						} 
						this.val$imageobserver.imageUpdate(paramImage,0, 0, 0, paramImage.getWidth(null), paramImage.getHeight(null));
	/*    */       }
	/*    */     }).start();
	/*    */   }
/*    */ 
/*    */   public static void waitForImage(Image paramImage)
/*    */   {
/* 20 */     if (waitComponent == null) {
/* 21 */       waitComponent = new Canvas();
/*    */     }
/* 23 */     waitForImage(waitComponent, paramImage);
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.util.GraphicsUtils
 * JD-Core Version:    0.6.2
 */