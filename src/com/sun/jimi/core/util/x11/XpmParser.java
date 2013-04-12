/*     */ package com.sun.jimi.core.util.x11;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Hashtable;
/*     */ import java.util.StringTokenizer;
/*     */ 
/*     */ public class XpmParser
/*     */ {
/*     */   private DataInputStream input;
/*  33 */   private int lineNo = 0;
/*  34 */   private int width = 0;
/*  35 */   private int height = 0;
/*  36 */   private int nColors = 0;
/*  37 */   private int charsPerPixel = 0;
/*     */   private Hashtable colors;
/*     */   private Color[] colorTable;
/*  43 */   private byte[] pixmap = null;
/*     */   private String line;
/*     */ 
/*     */   public XpmParser(InputStream paramInputStream)
/*     */   {
/*  51 */     this.input = new DataInputStream(paramInputStream);
/*  52 */     this.colors = new Hashtable();
/*     */   }
/*     */ 
/*     */   private void checkForHeader()
/*     */     throws Exception
/*     */   {
/* 120 */     readLine();
/* 121 */     if ((this.line != null) && (this.line.startsWith("/*")) && (this.line.endsWith("*/"))) {
/* 122 */       String str = this.line.substring(2);
/* 123 */       str = str.trim();
/*     */ 
/* 125 */       readLine();
/*     */     }
/*     */   }
/*     */ 
/*     */   public Color[] getColorTable()
/*     */   {
/*  98 */     return this.colorTable;
/*     */   }
/*     */ 
/*     */   public int getHeight()
/*     */   {
/*  82 */     return this.height;
/*     */   }
/*     */ 
/*     */   public byte[] getPixmap()
/*     */   {
/*  90 */     return this.pixmap;
/*     */   }
/*     */ 
/*     */   public int getWidth()
/*     */   {
/*  74 */     return this.width;
/*     */   }
/*     */ 
/*     */   public boolean parse()
/*     */   {
/*     */     try
/*     */     {
/*  61 */       parseInput();
/*  62 */       return true;
/*     */     } catch (Exception localException) {
/*     */     }
/*  65 */     return false;
/*     */   }
/*     */ 
/*     */   private Color parseColor(String paramString)
/*     */   {
/* 278 */     Color localColor = null;
/* 279 */     if (paramString.charAt(0) == '#')
/*     */     {
/* 281 */       switch (paramString.length())
/*     */       {
/*     */       case 7:
/* 287 */         Integer localInteger1 = Integer.valueOf(paramString.substring(1), 16);
/* 288 */         localColor = new Color(localInteger1.intValue());
/* 289 */         break;
/*     */       case 13:
/* 291 */         Integer localInteger2 = Integer.valueOf(paramString.substring(1, 3), 16);
/* 292 */         Integer localInteger3 = Integer.valueOf(paramString.substring(5, 7), 16);
/* 293 */         Integer localInteger4 = Integer.valueOf(paramString.substring(9, 11), 16);
/* 294 */         localColor = new Color(localInteger2.intValue(), localInteger3.intValue(), 
/* 295 */           localInteger4.intValue());
/* 296 */         break;
/*     */       case 4:
/* 298 */       }if (localColor == null)
/*     */       {
/* 300 */         localColor = Color.black;
/*     */       }
/*     */ 
/*     */     }
/* 305 */     else if (paramString.equalsIgnoreCase("none"))
/*     */     {
/* 307 */       localColor = null;
/*     */     }
/*     */     else
/*     */     {
/* 311 */       int i = XColorNames.getRgb(paramString);
/* 312 */       if (i != -2147483648)
/* 313 */         localColor = new Color(i);
/* 314 */       if (localColor == null)
/*     */       {
/* 316 */         localColor = Color.black;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 321 */     return localColor;
/*     */   }
/*     */ 
/*     */   private void parseInput()
/*     */     throws Exception
/*     */   {
/* 106 */     checkForHeader();
/* 107 */     skipLineStartingWith("static char");
/* 108 */     readHintsLine();
/*     */ 
/* 110 */     this.colorTable = new Color[this.nColors];
/*     */ 
/* 112 */     readColorTable();
/* 113 */     readPixels();
/*     */   }
/*     */ 
/*     */   private void readColorTable()
/*     */     throws Exception
/*     */   {
/* 180 */     for (int i = 0; i < this.nColors; i++) {
/* 181 */       readLine();
/*     */ 
/* 183 */       if ((this.line != null) && (this.line.startsWith("/*")) && (this.line.endsWith("*/")))
/* 184 */         readLine();
/* 185 */       if (this.line == null) {
/* 186 */         throw new Exception("Invalid Xpm format: unexpected EOF, line: " + 
/* 187 */           this.lineNo);
/*     */       }
/* 189 */       int j = this.line.indexOf('"');
/* 190 */       if (j < 0)
/* 191 */         throw new Exception("Invalid Xpm format: color table, line: " + 
/* 192 */           this.lineNo);
/* 193 */       String str1 = this.line.substring(j + 1);
/* 194 */       int k = 0;
/* 195 */       for (int m = 0; m < this.charsPerPixel; m++) {
/* 196 */         k = (k << 8) + str1.charAt(m);
/*     */       }
/* 198 */       str1 = this.line.substring(j + 1 + this.charsPerPixel);
/* 199 */       StringTokenizer localStringTokenizer = new StringTokenizer(str1, " \"\t\n\r");
/* 200 */       int n = 0;
/* 201 */       int i1 = 0;
/* 202 */       while ((localStringTokenizer.hasMoreTokens()) && (n < 2))
/*     */       {
/* 204 */         String str2 = localStringTokenizer.nextToken();
/* 205 */         switch (n)
/*     */         {
/*     */         case 0:
/* 208 */           if (str2.equals("c"))
/* 209 */             n++;
/* 210 */           break;
/*     */         case 1:
/* 213 */           this.colorTable[i] = parseColor(str2);
/* 214 */           this.colors.put(new Integer(k), new Integer(i));
/* 215 */           n++;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void readHintsLine()
/*     */     throws Exception
/*     */   {
/* 147 */     if ((this.line != null) && (this.line.startsWith("/*")) && (this.line.endsWith("*/")))
/* 148 */       readLine();
/* 149 */     if (this.line == null)
/* 150 */       throw new Exception("Invalid Xpm format: unexpected EOF, line: " + 
/* 151 */         this.lineNo);
/* 152 */     int i = this.line.indexOf('"');
/* 153 */     int j = this.line.lastIndexOf('"');
/* 154 */     if ((i < 0) || (j < 0) || (j <= i))
/* 155 */       throw new Exception("Invalid Xpm format: hints line: " + this.lineNo);
/* 156 */     String str = this.line.substring(i + 1, j);
/* 157 */     StringTokenizer localStringTokenizer = new StringTokenizer(str);
/* 158 */     if (localStringTokenizer.countTokens() < 4)
/* 159 */       throw new Exception("Invalid Xpm format: hints line: " + this.lineNo);
/*     */     try {
/* 161 */       this.width = Integer.parseInt(localStringTokenizer.nextToken());
/* 162 */       this.height = Integer.parseInt(localStringTokenizer.nextToken());
/* 163 */       this.nColors = Integer.parseInt(localStringTokenizer.nextToken());
/* 164 */       this.charsPerPixel = Integer.parseInt(localStringTokenizer.nextToken());
/*     */     }
/*     */     catch (Exception localException) {
/* 167 */       throw new Exception("Invalid Xpm format: hints line: " + this.lineNo);
/*     */     }
/* 169 */     if (this.charsPerPixel > 3)
/* 170 */       throw new Exception("Invalid Xpm format: Can only handle up to 3 chars per pixels");
/*     */   }
/*     */ 
/*     */   private void readLine()
/*     */   {
/* 262 */     this.line = null;
/*     */     try {
/* 264 */       this.line = this.input.readLine();
/*     */     }
/*     */     catch (IOException localIOException) {
/* 267 */       this.line = null;
/*     */     }
/* 269 */     if (this.line != null)
/* 270 */       this.line = this.line.trim();
/* 271 */     this.lineNo += 1;
/*     */   }
/*     */ 
/*     */   private void readPixels()
/*     */     throws Exception
/*     */   {
/* 226 */     readLine();
/*     */ 
/* 228 */     if ((this.line != null) && (this.line.startsWith("/*")) && (this.line.endsWith("*/")))
/* 229 */       readLine();
/* 230 */     if (this.line == null) {
/* 231 */       throw new Exception("Invalid Xpm format: EOF at line: " + 
/* 232 */         this.lineNo);
/*     */     }
/* 234 */     int i = this.width * this.height;
/* 235 */     this.pixmap = new byte[i];
/* 236 */     int j = 0;
/*     */ 
/* 238 */     while (this.line != null) {
/* 239 */       int k = this.line.indexOf('"');
/* 240 */       int m = this.line.lastIndexOf('"');
/* 241 */       if ((k < 0) || (m < 0) || (m <= k))
/* 242 */         throw new Exception("Invalid Xpm format: line: " + this.lineNo);
/* 243 */       String str = this.line.substring(k + 1, m);
/* 244 */       int n = 0;
/* 245 */       while (n < str.length()) {
/* 246 */         int i1 = 0;
/* 247 */         for (int i2 = 0; i2 < this.charsPerPixel; i2++)
/* 248 */           i1 = (i1 << 8) + str.charAt(n++);
/* 249 */         this.pixmap[(j++)] = ((byte)((Integer)this.colors.get(new Integer(i1))).intValue());
/*     */       }
/*     */ 
/* 252 */       if (j >= i)
/* 253 */         return;
/* 254 */       readLine();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void skipLineStartingWith(String paramString)
/*     */     throws Exception
/*     */   {
/* 133 */     if (this.line == null)
/* 134 */       throw new Exception("Invalid Xpm format, line: " + this.lineNo);
/* 135 */     if (this.line.startsWith(paramString))
/*     */     {
/* 137 */       readLine();
/*     */     }
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.util.x11.XpmParser
 * JD-Core Version:    0.6.2
 */