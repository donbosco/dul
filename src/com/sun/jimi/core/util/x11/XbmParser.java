/*     */ package com.sun.jimi.core.util.x11;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ import java.io.StreamTokenizer;
/*     */ 
/*     */ public class XbmParser
/*     */ {
/*     */   private static final int GET_WIDTH = 0;
/*     */   private static final int GET_HEIGHT = 1;
/*     */   private static final int GET_START = 2;
/*     */   private static final int GET_BYTES = 3;
/*     */   private StreamTokenizer tokenizer;
/*  28 */   private int width = 0;
/*  29 */   private int height = 0;
/*  30 */   private int length = 0;
/*  31 */   private int[] bitmap = null;
/*     */ 
/*     */   public XbmParser(InputStream paramInputStream)
/*     */   {
/*  38 */     this.tokenizer = new StreamTokenizer(paramInputStream);
/*  39 */     this.tokenizer.slashStarComments(true);
/*  40 */     this.tokenizer.ordinaryChar(47);
/*     */   }
/*     */ 
/*     */   public int[] getBitmap()
/*     */   {
/*  78 */     return this.bitmap;
/*     */   }
/*     */ 
/*     */   public int getHeight()
/*     */   {
/*  70 */     return this.height;
/*     */   }
/*     */ 
/*     */   public int getWidth()
/*     */   {
/*  62 */     return this.width;
/*     */   }
/*     */ 
/*     */   public boolean parse()
/*     */   {
/*     */     try
/*     */     {
/*  49 */       parseInput();
/*  50 */       return true;
/*     */     } catch (Exception localException) {
/*     */     }
/*  53 */     return false;
/*     */   }
/*     */ 
/*     */   private void parseInput()
/*     */     throws Exception
/*     */   {
/*  86 */     int i = 0;
/*  87 */     int j = -1;
/*  88 */     int k = 0;
/*     */ 
/*  90 */     j = this.tokenizer.nextToken();
/*  91 */     while (j != -1) {
/*  92 */       switch (i) {
/*     */       case 0:
/*  94 */         if (j == -2) {
/*  95 */           this.width = ((int)this.tokenizer.nval);
/*  96 */           i = 1;
/*     */         }
/*  98 */         break;
/*     */       case 1:
/* 100 */         if (j == -2) {
/* 101 */           this.height = ((int)this.tokenizer.nval);
/* 102 */           i = 2;
/*     */         }
/* 104 */         break;
/*     */       case 2:
/* 106 */         if (j == 123)
/*     */         {
/* 108 */           resetTokenizer();
/*     */ 
/* 110 */           this.width = ((this.width + 7) / 8 * 8);
/* 111 */           this.length = (this.width / 8 * this.height);
/* 112 */           this.bitmap = new int[this.length];
/* 113 */           i = 3;
/*     */         }
/* 115 */         break;
/*     */       case 3:
/* 118 */         if (j == -3) {
/* 119 */           if ((this.tokenizer.sval.length() > 2) && 
/* 120 */             (this.tokenizer.sval.charAt(0) == '0') && (
/* 121 */             (this.tokenizer.sval.charAt(1) == 'x') || 
/* 122 */             (this.tokenizer.sval.charAt(1) == 'x'))) {
/*     */             try {
/* 124 */               int m = Integer.parseInt(
/* 125 */                 this.tokenizer.sval.substring(2), 16);
/* 126 */               this.bitmap[(k++)] = m;
/*     */             }
/*     */             catch (Exception localException)
/*     */             {
/* 130 */               j = -1;
/* 131 */               k--;
/* 132 */               continue;
/*     */             }
/*     */           }
/*     */         }
/* 136 */         else if (j == 125)
/*     */         {
/* 138 */           j = -1;
/*     */         }break;
/*     */       }
/* 141 */       j = this.tokenizer.nextToken();
/*     */     }
/*     */ 
/* 144 */     if ((this.width == 0) || (this.height == 0) || (k != this.length))
/* 145 */       throw new Exception();
/*     */   }
/*     */ 
/*     */   private void resetTokenizer()
/*     */   {
/* 156 */     this.tokenizer.resetSyntax();
/* 157 */     this.tokenizer.wordChars(97, 102);
/* 158 */     this.tokenizer.wordChars(65, 70);
/* 159 */     this.tokenizer.wordChars(48, 57);
/* 160 */     this.tokenizer.wordChars(120, 120);
/* 161 */     this.tokenizer.wordChars(88, 88);
/* 162 */     this.tokenizer.whitespaceChars(0, 32);
/* 163 */     this.tokenizer.slashStarComments(true);
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.util.x11.XbmParser
 * JD-Core Version:    0.6.2
 */