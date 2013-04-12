/*     */ package com.sun.jimi.core.encoder.xpm;
/*     */ 
/*     */ import com.sun.jimi.core.JimiException;
/*     */ import com.sun.jimi.core.compat.AdaptiveRasterImage;
/*     */ import com.sun.jimi.core.compat.JimiEncoderBase;
/*     */ import java.awt.image.IndexColorModel;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ public class XPMEncoder extends JimiEncoderBase
/*     */ {
/*     */   protected static final String MAGIC = "/* XPM */\n";
/*     */   protected static final String HEADER_STRING = "static char* image[]={\n";
/*     */   protected static char[] paletteStringCharacters;
/*     */   protected PrintStream output;
/*     */   protected AdaptiveRasterImage jimiImage;
/*     */   protected int state;
/*     */   protected int paletteSize;
/*     */   protected String[] paletteStrings;
/*     */   protected int entrySize;
/*     */ 
/*     */   public XPMEncoder()
/*     */   {
/*  50 */     if (paletteStringCharacters == null)
/*  51 */       generatePaletteStringCharacters();
/*     */   }
/*     */ 
/*     */   protected void createPaletteStrings(IndexColorModel paramIndexColorModel)
/*     */   {
/* 112 */     int i = paramIndexColorModel.getMapSize();
/*     */ 
/* 115 */     this.entrySize = 0;
/* 116 */     int j = i;
/*     */     do {
/* 118 */       this.entrySize += 1;
/* 119 */       j /= paletteStringCharacters.length;
/* 120 */     }while (j != 0);
/*     */ 
/* 123 */     this.paletteStrings = new String[i];
/*     */ 
/* 125 */     for (int k = 0; k < i; k++) {
/* 126 */       int m = k;
/* 127 */       String str = "";
/*     */ 
/* 129 */       for (int n = 0; n < this.entrySize; n++) {
/* 130 */         str = String.valueOf(paletteStringCharacters[(m % paletteStringCharacters.length)]) + 
/* 131 */           str;
/* 132 */         m /= paletteStringCharacters.length;
/*     */       }
/* 134 */       this.paletteStrings[k] = str;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void doImageEncode()
/*     */     throws JimiException, IOException
/*     */   {
/*  95 */     if (!(this.jimiImage.getColorModel() instanceof IndexColorModel)) {
/*  96 */       throw new JimiException("XPM only encodes palette-based images.");
/*     */     }
/*  98 */     IndexColorModel localIndexColorModel = (IndexColorModel)this.jimiImage.getColorModel();
/*     */ 
/* 100 */     createPaletteStrings(localIndexColorModel);
/* 101 */     writeHeader();
/* 102 */     writePalette(localIndexColorModel);
/* 103 */     writeImageData();
/* 104 */     writeTrailer();
/*     */   }
/*     */ 
/*     */   public boolean driveEncoder()
/*     */     throws JimiException
/*     */   {
/*     */     try
/*     */     {
/*  65 */       this.jimiImage = getJimiImage();
/*  66 */       doImageEncode();
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/*  71 */       this.state = 1;
/*  72 */       localException.printStackTrace();
/*  73 */       throw new JimiException(localException.toString());
/*     */     }
/*     */ 
/*  76 */     this.state = 2;
/*     */ 
/*  78 */     return false;
/*     */   }
/*     */ 
/*     */   protected void generatePaletteStringCharacters()
/*     */   {
/* 208 */     int i = 62;
/* 209 */     paletteStringCharacters = new char[i];
/*     */ 
/* 211 */     int k = 0;
/*     */ 
/* 213 */     for (int j = 48; j <= 57; j = (char)(j + 1)) {
/* 214 */       paletteStringCharacters[(k++)] = (char) j;
/*     */     }
int j;
/*     */ 
/* 217 */     for (j = 97; j <= 122; j = (char)(j + 1)) {
/* 218 */       paletteStringCharacters[(k++)] = (char) j;
/*     */     }
/*     */ 
/* 221 */     for (j = 65; j <= 90; j = (char)(j + 1))
/* 222 */       paletteStringCharacters[(k++)] = (char) j;
/*     */   }
/*     */ 
/*     */   public int getState()
/*     */   {
/*  86 */     return this.state;
/*     */   }
/*     */ 
/*     */   public void initSpecificEncoder(OutputStream paramOutputStream, AdaptiveRasterImage paramAdaptiveRasterImage)
/*     */   {
/*  56 */     this.output = new PrintStream(paramOutputStream);
/*     */   }
/*     */ 
/*     */   protected String toPaddedHexString(int paramInt)
/*     */   {
/* 195 */     String str = Integer.toHexString(paramInt);
/* 196 */     while (str.length() < 6) {
/* 197 */       str = "0" + str;
/*     */     }
/* 199 */     return str;
/*     */   }
/*     */ 
/*     */   protected void writeHeader()
/*     */     throws IOException
/*     */   {
/* 143 */     this.output.print("/* XPM */\n");
/* 144 */     this.output.print("static char* image[]={\n");
/* 145 */     int i = this.jimiImage.getWidth();
/* 146 */     int j = this.jimiImage.getHeight();
/*     */ 
/* 148 */     this.output.println("\"" + i + " " + j + " " + this.paletteStrings.length + " " + this.entrySize + "\",");
/*     */   }
/*     */ 
/*     */   protected void writeImageData()
/*     */     throws IOException, JimiException
/*     */   {
/* 167 */     int[] arrayOfInt = new int[this.jimiImage.getWidth()];
/* 168 */     int i = this.jimiImage.getHeight();
/*     */ 
/* 170 */     for (int j = 0; j < i; j++) {
/* 171 */       this.jimiImage.getChannel(j, arrayOfInt, 0);
/* 172 */       this.output.print("\"");
/* 173 */       for (int k = 0; k < arrayOfInt.length; k++) {
/* 174 */         this.output.print(this.paletteStrings[arrayOfInt[k]]);
/*     */       }
/* 176 */       if (j < i - 1)
/* 177 */         this.output.println("\",");
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void writePalette(IndexColorModel paramIndexColorModel)
/*     */     throws IOException
/*     */   {
/* 156 */     for (int i = 0; i < this.paletteStrings.length; i++)
/*     */     {
/* 158 */       this.output.println("\"" + this.paletteStrings[i] + " c #" + toPaddedHexString(paramIndexColorModel.getRGB(i) & 0xFFFFFF) + "\",");
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void writeTrailer()
/*     */     throws IOException
/*     */   {
/* 187 */     this.output.println("\"};");
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.encoder.xpm.XPMEncoder
 * JD-Core Version:    0.6.2
 */