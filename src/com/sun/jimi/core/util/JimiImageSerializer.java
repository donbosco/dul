/*     */ package com.sun.jimi.core.util;
/*     */ 
/*     */ import com.sun.jimi.core.Jimi;
/*     */ import com.sun.jimi.core.JimiException;
/*     */ import com.sun.jimi.util.ObjectInputToStreamAdapter;
/*     */ import com.sun.jimi.util.ObjectOutputToStreamAdapter;
/*     */ import java.awt.Canvas;
/*     */ import java.awt.Image;
/*     */ import java.io.Externalizable;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ 
/*     */ public class JimiImageSerializer
/*     */   implements Externalizable
/*     */ {
/*     */   private static final long serialVersionUID = -3284218653375208081L;
/*     */   protected static final String DEFAULT_ENCODING = "image/png";
/*  27 */   private String encodingFormat_ = "image/png";
/*     */   protected transient Image image_;
/*     */ 
/*     */   public JimiImageSerializer()
/*     */   {
/*     */   }
/*     */ 
/*     */   public JimiImageSerializer(Image paramImage)
/*     */   {
/*  45 */     this(paramImage, "image/png");
/*     */   }
/*     */ 
/*     */   private JimiImageSerializer(Image paramImage, String paramString)
/*     */   {
/*  55 */     this.image_ = paramImage;
/*  56 */     this.encodingFormat_ = paramString;
/*     */   }
/*     */ 
/*     */   public Image getImage()
/*     */   {
/*  65 */     return this.image_;
/*     */   }
/*     */ 
/*     */   public void readExternal(ObjectInput paramObjectInput)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 109 */       this.encodingFormat_ = ((String)paramObjectInput.readObject());
/*     */     }
/*     */     catch (ClassNotFoundException localClassNotFoundException)
/*     */     {
/* 113 */       throw new IOException(localClassNotFoundException.getMessage());
/*     */     }
/*     */ 
/* 117 */     ObjectInputToStreamAdapter localObjectInputToStreamAdapter = new ObjectInputToStreamAdapter(paramObjectInput);
/*     */ 
/* 120 */     this.image_ = Jimi.getImage(localObjectInputToStreamAdapter, this.encodingFormat_);
/* 121 */     GraphicsUtils.waitForImage(new Canvas(), this.image_);
/*     */   }
/*     */ 
/*     */   public void setImage(Image paramImage)
/*     */   {
/*  74 */     this.image_ = paramImage;
/*     */   }
/*     */ 
/*     */   public void writeExternal(ObjectOutput paramObjectOutput)
/*     */     throws IOException
/*     */   {
/*  87 */     ObjectOutputToStreamAdapter localObjectOutputToStreamAdapter = new ObjectOutputToStreamAdapter(paramObjectOutput);
/*     */ 
/*  90 */     paramObjectOutput.writeObject(this.encodingFormat_);
/*     */     try
/*     */     {
/*  94 */       Jimi.putImage(this.encodingFormat_, this.image_, localObjectOutputToStreamAdapter);
/*     */     }
/*     */     catch (JimiException localJimiException) {
/*  97 */       throw new IOException("Unable to write image: " + localJimiException.getMessage());
/*     */     }
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.util.JimiImageSerializer
 * JD-Core Version:    0.6.2
 */