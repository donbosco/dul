/*     */ package com.sun.jimi.core.filters;
/*     */ 
/*     */ import java.awt.image.ImageProducer;
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ public class Gamma extends RGBBlockFilter
/*     */ {
/*     */   private double rValue;
/*     */   private double gValue;
/*     */   private double bValue;
/*     */   private int[] rTable;
/*     */   private int[] gTable;
/*     */   private int[] bTable;
/*  85 */   private boolean initialized = false;
/*     */ 
/*     */   public Gamma(ImageProducer paramImageProducer, double paramDouble)
/*     */   {
/*  49 */     this(paramImageProducer, paramDouble, paramDouble, paramDouble);
/*     */   }
/*     */ 
/*     */   public Gamma(ImageProducer paramImageProducer, double paramDouble1, double paramDouble2, double paramDouble3)
/*     */   {
/*  55 */     super(paramImageProducer);
/*  56 */     this.rValue = paramDouble1;
/*  57 */     this.gValue = paramDouble2;
/*  58 */     this.bValue = paramDouble3;
/*     */   }
/*     */ 
/*     */   private int[] buildTable(double paramDouble)
/*     */   {
/* 110 */     int[] arrayOfInt = new int[256];
/* 111 */     double d = 1.0D / paramDouble;
/* 112 */     for (int i = 0; i < 256; i++)
/*     */     {
/* 114 */       int j = (int)
/* 115 */         (255.0D * Math.pow(i / 255.0D, d) + 0.5D);
/* 116 */       if (j > 255)
/* 117 */         j = 255;
/* 118 */       arrayOfInt[i] = j;
/*     */     }
/* 120 */     return arrayOfInt;
/*     */   }
/*     */ 
/*     */   public int[][] filterRGBBlock(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[][] paramArrayOfInt)
/*     */   {
/*  66 */     initialize();
/*  67 */     for (int i = 0; i < paramInt4; i++)
/*  68 */       for (int j = 0; j < paramInt3; j++)
/*     */       {
/*  70 */         int k = paramArrayOfInt[i][j];
/*  71 */         int m = k >> 24 & 0xFF;
/*  72 */         int n = k >> 16 & 0xFF;
/*  73 */         int i1 = k >> 8 & 0xFF;
/*  74 */         int i2 = k & 0xFF;
/*  75 */         n = this.rTable[n];
/*  76 */         i1 = this.gTable[i1];
/*  77 */         i2 = this.bTable[i2];
/*  78 */         paramArrayOfInt[i][j] = 
/*  79 */           (m << 24 | n << 16 | i1 << 8 | i2);
/*     */       }
/*  81 */     return paramArrayOfInt;
/*     */   }
/*     */ 
/*     */   private void initialize()
/*     */   {
/*  89 */     if (this.initialized)
/*  90 */       return;
/*  91 */     this.initialized = true;
/*     */ 
/*  93 */     this.rTable = buildTable(this.rValue);
/*     */ 
/*  95 */     if (this.gValue == this.rValue)
/*  96 */       this.gTable = this.rTable;
/*     */     else {
/*  98 */       this.gTable = buildTable(this.gValue);
/*     */     }
/* 100 */     if (this.bValue == this.rValue)
/* 101 */       this.bTable = this.rTable;
/* 102 */     else if (this.bValue == this.gValue)
/* 103 */       this.bTable = this.gTable;
/*     */     else
/* 105 */       this.bTable = buildTable(this.bValue);
/*     */   }
/*     */ 
/*     */   public static void main(String[] paramArrayOfString)
/*     */   {
/* 127 */     Gamma localGamma = null;
/* 128 */     if (paramArrayOfString.length == 1)
/* 129 */       localGamma = new Gamma(null, Double.valueOf(paramArrayOfString[0]).doubleValue());
/* 130 */     else if (paramArrayOfString.length == 3)
/* 131 */       localGamma = new Gamma(null, 
/* 132 */         Double.valueOf(paramArrayOfString[0]).doubleValue(), 
/* 133 */         Double.valueOf(paramArrayOfString[1]).doubleValue(), 
/* 134 */         Double.valueOf(paramArrayOfString[2]).doubleValue());
/*     */     else
/* 136 */       usage();
/* 137 */     System.exit(
/* 138 */       ImageFilterPlus.filterStream(System.in, System.out, localGamma));
/*     */   }
/*     */ 
/*     */   private static void usage()
/*     */   {
/* 143 */     System.err.println("usage: Gamma <value>");
/* 144 */     System.err.println("or:    Gamma <rValue> <gValue> <bValue>");
/* 145 */     System.exit(1);
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.filters.Gamma
 * JD-Core Version:    0.6.2
 */