/*      */ package com.sun.jimi.core.decoder.png;
/*      */ 
/*      */ import com.sun.jimi.core.JimiException;
/*      */ import com.sun.jimi.core.compat.AdaptiveRasterImage;
/*      */ import com.sun.jimi.core.util.JimiUtil;
/*      */ import java.awt.image.ColorModel;
/*      */ import java.awt.image.DirectColorModel;
/*      */ import java.awt.image.IndexColorModel;
/*      */ import java.io.DataInputStream;
/*      */ import java.io.FilterInputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.SequenceInputStream;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Vector;
/*      */ import java.util.zip.Inflater;
/*      */ import java.util.zip.InflaterInputStream;
/*      */ 
/*      */ public class PNGReader
/*      */ {
/*      */   private int dataWidth;
/*      */   private int dataHeight;
/*   60 */   private int width = -1;
/*   61 */   private int height = -1;
/*   62 */   private int sigmask = 65535;
/*      */   private ColorModel model;
/*      */   private Object pixels;
/*      */   private int[] ipixels;
/*      */   private byte[] bpixels;
/*      */   private Hashtable properties;
/*      */   private Vector theConsumers;
/*      */   private boolean multipass;
/*      */   private boolean complete;
/*      */   private boolean error;
/*      */   InputStream underlyingStream_;
/*      */   DataInputStream inputStream;
/*      */   private Thread controlThread;
/*   79 */   private boolean infoAvailable = false;
/*   80 */   private boolean completePasses = false;
/*      */ 
/*   83 */   private int updateDelay = 750;
/*      */ 
/*   87 */   private boolean headerFound = false;
/*   88 */   private int compressionMethod = -1;
/*   89 */   private int depth = -1;
/*   90 */   private int colorType = -1;
/*   91 */   private int filterMethod = -1;
/*   92 */   private int interlaceMethod = -1;
/*      */   private int pass;
/*      */   private byte[] palette;
/*      */   private boolean transparency;
/*      */   int chunkLength;
/*      */   int chunkType;
/*  102 */   boolean needChunkInfo = true;
/*      */   static final int CHUNK_bKGD = 1649100612;
/*      */   static final int CHUNK_cHRM = 1665684045;
/*      */   static final int CHUNK_gAMA = 1732332865;
/*      */   static final int CHUNK_hIST = 1749635924;
/*      */   static final int CHUNK_IDAT = 1229209940;
/*      */   static final int CHUNK_IEND = 1229278788;
/*      */   static final int CHUNK_IHDR = 1229472850;
/*      */   static final int CHUNK_PLTE = 1347179589;
/*      */   static final int CHUNK_pHYs = 1883789683;
/*      */   static final int CHUNK_sBIT = 1933723988;
/*      */   static final int CHUNK_tEXt = 1950701684;
/*      */   static final int CHUNK_tIME = 1950960965;
/*      */   static final int CHUNK_tRNS = 1951551059;
/*      */   static final int CHUNK_zTXt = 2052348020;
/*  119 */   static final int[] startingRow = { 0, 0, 0, 4, 0, 2, 0, 1 };
/*  120 */   static final int[] startingCol = { 0, 0, 4, 0, 2, 0, 1 };
/*  121 */   static final int[] rowInc = { 1, 8, 8, 8, 4, 4, 2, 2 };
/*  122 */   static final int[] colInc = { 1, 8, 8, 4, 4, 2, 2, 1 };
/*  123 */   static final int[] blockHeight = { 1, 8, 8, 4, 4, 2, 2, 1 };
/*  124 */   static final int[] blockWidth = { 1, 8, 4, 4, 2, 2, 1, 1 };
/*      */   static final int CT_PALETTE = 1;
/*      */   static final int CT_COLOR = 2;
/*      */   static final int CT_ALPHA = 4;
/*      */   static final int IT_GRAYSCALE = 0;
/*      */   static final int IT_RGB = 2;
/*      */   static final int IT_PALETTE = 3;
/*      */   static final int IT_GRAYALPHA = 4;
/*      */   static final int IT_RGBA = 6;
/*      */   AdaptiveRasterImage ji_;
/*      */ 
/*      */   PNGReader(DataInputStream paramDataInputStream, AdaptiveRasterImage paramAdaptiveRasterImage)
/*      */   {
/*  146 */     this.underlyingStream_ = paramDataInputStream;
/*  147 */     this.inputStream = paramDataInputStream;
/*  148 */     this.ji_ = paramAdaptiveRasterImage;
/*      */   }
/*      */ 
/*      */   private void createColorModel()
/*      */     throws JimiException, IOException
/*      */   {
/* 1110 */     int i = 0;
/* 1111 */     if (this.depth == 16)
/* 1112 */       i = 255;
/*      */     else {
/* 1114 */       i = (1 << this.depth) - 1;
/*      */     }
/* 1116 */     switch (this.colorType)
/*      */     {
/*      */     case 3:
/* 1119 */       if (this.palette == null)
/* 1120 */         throw new JimiException("No palette located");
/* 1121 */       if (this.transparency)
/* 1122 */         this.model = new IndexColorModel(8, this.palette.length / 4, this.palette, 0, true);
/*      */       else
/* 1124 */         this.model = new IndexColorModel(8, this.palette.length / 3, this.palette, 0, false);
/* 1125 */       break;
/*      */     case 0:
/* 1128 */       if (this.depth == 16)
/* 1129 */         this.model = new DirectColorModel(8, i, i, i);
/*      */       else
/* 1131 */         this.model = new DirectColorModel(this.depth, i, i, i);
/* 1132 */       break;
/*      */     case 2:
/* 1135 */       this.model = new DirectColorModel(24, 16711680, 65280, 255);
/* 1136 */       break;
/*      */     case 4:
/* 1140 */       int j = i << 8;
/* 1141 */       this.model = new DirectColorModel(16, j, j, j, i);
/* 1142 */       break;
/*      */     case 6:
/* 1145 */       this.model = new DirectColorModel(32, 16711680, 65280, 255, -16777216);
/* 1146 */       break;
/*      */     case 1:
/*      */     case 5:
/*      */     default:
/* 1149 */       throw new JimiException("Image has unknown color type " + this.colorType);
/*      */     }
/*      */   }
/*      */ 
/*      */   void decodeImage()
/*      */     throws JimiException
/*      */   {
/*      */     try
/*      */     {
/*  159 */       handleSignature();
/*      */       do {
/*  161 */         handleChunk();
/*      */ 
/*  160 */         if (this.complete) break;  } while (!this.error);
/*      */     }
/*      */     catch (IOException localIOException)
/*      */     {
/*  165 */       throw new JimiException("IO Error");
/*      */     }
/*      */   }
/*      */ 
/*      */   private boolean filterRow(byte[] paramArrayOfByte, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt1, int paramInt2)
/*      */   {
/*  997 */     int i = paramArrayOfInt1.length;
/*      */     int j;
/*      */     int k;
/*  999 */     switch (paramInt1)
/*      */     {
/*      */     case 0:
/* 1003 */       for (j = 0; j < i; j++)
/* 1004 */         paramArrayOfInt1[j] = (0xFF & paramArrayOfByte[j]);
/* 1005 */       break;
/*      */     case 1:
/* 1009 */       for (j = 0; 
/* 1010 */         j < paramInt2; j++)
/* 1011 */         paramArrayOfInt1[j] = (0xFF & paramArrayOfByte[j]);
/* 1012 */       for (; j < i; j++)
/* 1013 */         paramArrayOfInt1[j] = (0xFF & paramArrayOfByte[j] + paramArrayOfInt1[(j - paramInt2)]);
/* 1014 */       break;
/*      */     case 2:
/* 1018 */       if (paramArrayOfInt2 != null)
/*      */       {
/* 1020 */         for (j = 0; j < i; j++) {
/* 1021 */           paramArrayOfInt1[j] = (0xFF & paramArrayOfInt2[j] + paramArrayOfByte[j]);
/*      */         }
/*      */       }
/*      */       else {
/* 1025 */         for (j = 0; j < i; j++)
/* 1026 */           paramArrayOfInt1[j] = (0xFF & paramArrayOfByte[j]);
/*      */       }
/* 1028 */       break;
/*      */     case 3:
/* 1032 */       if (paramArrayOfInt2 != null)
/*      */       {
/* 1034 */         for (j = 0; 
/* 1035 */           j < paramInt2; j++)
/*      */         {
/* 1037 */           k = paramArrayOfInt2[j];
/* 1038 */           paramArrayOfInt1[j] = (0xFF & (k >> 1) + paramArrayOfByte[j]);
/*      */         }
/* 1040 */         for (; j < i; j++)
/*      */         {
/* 1042 */           k = paramArrayOfInt2[j] + paramArrayOfInt1[(j - paramInt2)];
/* 1043 */           paramArrayOfInt1[j] = (0xFF & (k >> 1) + paramArrayOfByte[j]);
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 1048 */         for (j = 0; 
/* 1049 */           j < paramInt2; j++) {
/* 1050 */           paramArrayOfInt1[j] = (0xFF & paramArrayOfByte[j]);
/*      */         }
/* 1052 */         for (; j < i; j++)
/*      */         {
/* 1054 */           k = paramArrayOfInt1[(j - paramInt2)];
/* 1055 */           paramArrayOfInt1[j] = (0xFF & (k >> 1) + paramArrayOfByte[j]);
/*      */         }
/*      */       }
/* 1058 */       break;
/*      */     case 4:
/* 1062 */       if (paramArrayOfInt2 != null)
/*      */       {
/* 1064 */         for (j = 0; 
/* 1065 */           j < paramInt2; j++) {
/* 1066 */           paramArrayOfInt1[j] = (0xFF & paramArrayOfInt2[j] + paramArrayOfByte[j]);
/*      */         }
/* 1068 */         for (; j < i; j++)
/*      */         {
/* 1071 */           k = paramArrayOfInt1[(j - paramInt2)];
/* 1072 */           int m = paramArrayOfInt2[j];
/* 1073 */           int n = paramArrayOfInt2[(j - paramInt2)];
/* 1074 */           int i1 = k + m - n;
/* 1075 */           int i2 = i1 > k ? i1 - k : k - i1;
/* 1076 */           int i3 = i1 > m ? i1 - m : m - i1;
/* 1077 */           int i4 = i1 > n ? i1 - n : n - i1;
/*      */           int i5;
/* 1079 */           if ((i2 <= i3) && (i2 <= i4))
/* 1080 */             i5 = k;
/* 1081 */           else if (i3 <= i4)
/* 1082 */             i5 = m;
/*      */           else
/* 1084 */             i5 = n;
/* 1085 */           paramArrayOfInt1[j] = (0xFF & i5 + paramArrayOfByte[j]);
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 1090 */         for (j = 0; 
/* 1091 */           j < paramInt2; j++) {
/* 1092 */           paramArrayOfInt1[j] = (0xFF & paramArrayOfByte[j]);
/*      */         }
/* 1094 */         for (; j < i; j++)
/*      */         {
/* 1096 */           k = paramArrayOfInt1[(j - paramInt2)];
/* 1097 */           paramArrayOfInt1[j] = (0xFF & k + paramArrayOfByte[j]);
/*      */         }
/*      */       }
/* 1100 */       break;
/*      */     default:
/* 1103 */       return false;
/*      */     }
/* 1105 */     return true;
/*      */   }
/*      */ 
/*      */   private void handleChunk()
/*      */     throws JimiException, IOException
/*      */   {
/*  186 */     if (this.needChunkInfo)
/*      */     {
/*  188 */       this.chunkLength = this.inputStream.readInt();
/*  189 */       this.chunkType = this.inputStream.readInt();
/*  190 */       this.needChunkInfo = false;
/*      */     }
/*      */ 
/*  198 */     switch (this.chunkType)
/*      */     {
/*      */     case 1649100612:
/*  201 */       handlebKGD();
/*  202 */       break;
/*      */     case 1665684045:
/*  204 */       handlecHRM();
/*  205 */       break;
/*      */     case 1732332865:
/*  207 */       handlegAMA();
/*  208 */       break;
/*      */     case 1749635924:
/*  210 */       handlehIST();
/*  211 */       break;
/*      */     case 1229209940:
/*  213 */       handleIDAT();
/*  214 */       break;
/*      */     case 1229278788:
/*  216 */       handleIEND();
/*  217 */       break;
/*      */     case 1229472850:
/*  219 */       handleIHDR();
/*  220 */       break;
/*      */     case 1883789683:
/*  222 */       handlepHYs();
/*  223 */       break;
/*      */     case 1347179589:
/*  225 */       handlePLTE();
/*  226 */       break;
/*      */     case 1933723988:
/*  228 */       handlesBIT();
/*  229 */       break;
/*      */     case 1950701684:
/*  231 */       handletEXt();
/*  232 */       break;
/*      */     case 1950960965:
/*  234 */       handletIME();
/*  235 */       break;
/*      */     case 1951551059:
/*  237 */       handletRNS();
/*  238 */       break;
/*      */     case 2052348020:
/*  240 */       handlezTXt();
/*  241 */       break;
/*      */     default:
/*  245 */       this.inputStream.skip(this.chunkLength);
/*      */     }
/*      */ 
/*  248 */     int i = this.inputStream.readInt();
/*  249 */     this.needChunkInfo = true;
/*      */   }
/*      */ 
/*      */   private void handleIDAT()
/*      */     throws JimiException, IOException
/*      */   {
/*  390 */     if (!this.infoAvailable)
/*      */     {
/*  392 */       if ((this.height == -1) || (this.width == -1))
/*      */       {
/*  394 */         this.width = this.dataWidth;
/*  395 */         this.height = this.dataHeight;
/*      */       }
/*      */       else {
/*  398 */         throw new JimiException("IDAT before IHDR");
/*      */       }
/*  400 */       createColorModel();
/*  401 */       this.ji_.setSize(this.width, this.height);
/*  402 */       this.ji_.setColorModel(this.model);
/*  403 */       this.ji_.setPixels();
/*      */ 
/*  405 */       if (this.interlaceMethod != 0) {
/*  406 */         this.multipass = true;
/*      */       }
/*      */ 
/*  409 */       this.infoAvailable = true;
/*      */     }
/*  411 */     readImageData();
/*      */   }
/*      */ 
/*      */   private void handleIEND()
/*      */     throws IOException
/*      */   {
/*  347 */     this.complete = true;
/*      */   }
/*      */ 
/*      */   private void handleIHDR()
/*      */     throws JimiException, IOException
/*      */   {
/*  254 */     if (this.headerFound)
/*  255 */       throw new JimiException("Extraneous IHDR chunk encountered.");
/*  256 */     if (this.chunkLength != 13)
/*  257 */       throw new JimiException("IHDR chunk length wrong: " + this.chunkLength);
/*  258 */     this.dataWidth = this.inputStream.readInt();
/*  259 */     this.dataHeight = this.inputStream.readInt();
/*  260 */     this.depth = this.inputStream.read();
/*  261 */     this.colorType = this.inputStream.read();
/*  262 */     this.compressionMethod = this.inputStream.read();
/*  263 */     this.filterMethod = this.inputStream.read();
/*  264 */     this.interlaceMethod = this.inputStream.read();
/*  265 */     this.headerFound = true;
/*      */   }
/*      */ 
/*      */   private void handlePLTE()
/*      */     throws IOException
/*      */   {
/*  272 */     if (this.colorType == 3)
/*      */     {
/*  274 */       this.palette = new byte[this.chunkLength];
/*  275 */       this.inputStream.readFully(this.palette);
/*      */     }
/*      */     else
/*      */     {
/*  280 */       this.inputStream.skip(this.chunkLength);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void handleSignature()
/*      */     throws JimiException, IOException
/*      */   {
/*  171 */     if ((this.inputStream.read() != 137) || 
/*  172 */       (this.inputStream.read() != 80) || 
/*  173 */       (this.inputStream.read() != 78) || 
/*  174 */       (this.inputStream.read() != 71) || 
/*  175 */       (this.inputStream.read() != 13) || 
/*  176 */       (this.inputStream.read() != 10) || 
/*  177 */       (this.inputStream.read() != 26) || 
/*  178 */       (this.inputStream.read() != 10))
/*      */     {
/*  180 */       throw new JimiException("Not a PNG File");
/*      */     }
/*      */   }
/*      */ 
/*      */   private void handlebKGD()
/*      */     throws IOException
/*      */   {
/*  327 */     this.inputStream.skip(this.chunkLength);
/*      */   }
/*      */ 
/*      */   private void handlecHRM() throws IOException
/*      */   {
/*  332 */     this.inputStream.skip(this.chunkLength);
/*      */   }
/*      */ 
/*      */   private void handlegAMA() throws IOException
/*      */   {
/*  337 */     this.inputStream.skip(this.chunkLength);
/*      */   }
/*      */ 
/*      */   private void handlehIST() throws IOException
/*      */   {
/*  342 */     this.inputStream.skip(this.chunkLength);
/*      */   }
/*      */ 
/*      */   private void handlepHYs()
/*      */     throws IOException
/*      */   {
/*  359 */     this.inputStream.skip(this.chunkLength);
/*      */   }
/*      */ 
/*      */   private void handlesBIT() throws IOException
/*      */   {
/*  364 */     this.inputStream.skip(this.chunkLength);
/*      */   }
/*      */ 
/*      */   private void handletEXt() throws IOException
/*      */   {
/*  369 */     this.inputStream.skip(this.chunkLength);
/*      */   }
/*      */ 
/*      */   private void handletIME()
/*      */     throws IOException
/*      */   {
/*  376 */     this.inputStream.skip(this.chunkLength);
/*      */   }
/*      */ 
/*      */   private void handletRNS()
/*      */     throws JimiException, IOException
/*      */   {
/*  286 */     int i = this.chunkLength;
/*      */ 
/*  288 */     switch (this.colorType)
/*      */     {
/*      */     case 3:
/*  291 */       if (this.palette == null)
/*  292 */         throw new JimiException("tRNS chunk encountered before pLTE");
/*  293 */       int j = this.palette.length;
/*  294 */       this.transparency = true;
/*  295 */       int k = j / 3;
/*  296 */       byte[] arrayOfByte1 = new byte[k];
/*  297 */       this.inputStream.readFully(arrayOfByte1, 0, this.chunkLength);
/*      */ 
/*  301 */       for (int m = this.chunkLength; m < k; m++) {
/*  302 */         arrayOfByte1[m] = -1;
/*      */       }
/*  304 */       byte[] arrayOfByte2 = new byte[j + k];
/*  305 */       for (int n = arrayOfByte2.length; n > 0; )
/*      */       {
/*  307 */         arrayOfByte2[(--n)] = arrayOfByte1[(--k)];
/*  308 */         arrayOfByte2[(--n)] = this.palette[(--j)];
/*  309 */         arrayOfByte2[(--n)] = this.palette[(--j)];
/*  310 */         arrayOfByte2[(--n)] = this.palette[(--j)];
/*      */       }
/*  312 */       this.palette = arrayOfByte2;
/*  313 */       break;
/*      */     default:
/*  316 */       this.inputStream.skip(this.chunkLength);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void handlezTXt() throws IOException
/*      */   {
/*  322 */     this.inputStream.skip(this.chunkLength);
/*      */   }
/*      */ 
/*      */   private void insertGreyPixels(int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3)
/*      */     throws JimiException
/*      */   {
/*  747 */     int i = paramArrayOfInt[0];
/*  748 */     int j = colInc[this.pass];
/*  749 */     int k = 0;
/*      */     int m;
/*  751 */     switch (this.colorType)
/*      */     {
/*      */     case 0:
/*  754 */       switch (this.depth)
/*      */       {
/*      */       case 1:
/*  757 */         for (m = 0; m < paramInt1; paramInt2 += j)
/*      */         {
/*  759 */           if (k != 0) {
/*  760 */             k--;
/*      */           }
/*      */           else {
/*  763 */             k = 7;
/*  764 */             i = paramArrayOfInt[(m >> 3)];
/*      */           }
/*  766 */           this.ji_.setChannel(paramInt2, paramInt3, i >> k & 0x1);
/*      */ 
/*  757 */           m++;
/*      */         }
/*      */ 
/*  769 */         break;
/*      */       case 2:
/*  772 */         for (m = 0; m < paramInt1; paramInt2 += j)
/*      */         {
/*  774 */           if (k != 0) {
/*  775 */             k -= 2;
/*      */           }
/*      */           else {
/*  778 */             k = 6;
/*  779 */             i = paramArrayOfInt[(m >> 2)];
/*      */           }
/*  781 */           this.ji_.setChannel(paramInt2, paramInt3, i >> k & 0x3);
/*      */ 
/*  772 */           m++;
/*      */         }
/*      */ 
/*  784 */         break;
/*      */       case 4:
/*  787 */         for (m = 0; m < paramInt1; paramInt2 += j)
/*      */         {
/*  789 */           if (k != 0) {
/*  790 */             k = 0;
/*      */           }
/*      */           else {
/*  793 */             k = 4;
/*  794 */             i = paramArrayOfInt[(m >> 1)];
/*      */           }
/*  796 */           this.ji_.setChannel(paramInt2, paramInt3, i >> k & 0xF);
/*      */ 
/*  787 */           m++;
/*      */         }
/*      */ 
/*  799 */         break;
/*      */       case 8:
/*  802 */         for (m = 0; m < paramInt1; paramInt2 += j) {
/*  803 */           this.ji_.setChannel(paramInt2, paramInt3, paramArrayOfInt[m]);
/*      */ 
/*  802 */           m++;
/*      */         }
/*      */ 
/*  805 */         break;
/*      */       case 16:
/*  808 */         for (m = 0; m < paramInt1; paramInt2 += j) {
/*  809 */           this.ji_.setChannel(paramInt2, paramInt3, paramArrayOfInt[m]);
/*      */ 
/*  808 */           m += 2;
/*      */         }
/*      */       }
/*  811 */       break;
/*      */     case 4:
/*  819 */       if (this.depth == 8)
/*      */       {
/*  821 */         for (m = 0; m < paramInt1; paramInt2 += j) {
/*  822 */           this.ji_.setChannel(paramInt2, paramInt3, paramArrayOfInt[(m++)] << 8 | paramArrayOfInt[(m++)]);
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/*  827 */         m = 0;
/*      */         while (true) { m += 2; this.ji_.setChannel(paramInt2, paramInt3, paramArrayOfInt[m] << 8 | paramArrayOfInt[m]);
/*      */ 
/*  827 */           m += 2; paramInt2 += j; if (m >= paramInt1)
/*      */           {
/*  831 */             break;
/*      */           }
/*      */         }
/*      */       }
/*      */       break;
/*      */     case 1:
/*      */     case 2:
/*      */     case 3:
/*      */     }
/*      */   }
/*      */ 
/*      */   private void insertJimiPixels(int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3)
/*      */     throws JimiException
/*      */   {
	/*  542 */     if ((startingCol[this.pass] == 0) && (colInc[this.pass] == 1));
	/*      */     int j;
	/*      */     int m;
	/*      */    
	/*  550 */     switch (this.colorType)
	/*      */     {
	/*      */     case 0:
	/*  555 */       if (this.depth == 16)
	/*      */       {
	/*  557 */         byte[] localObject1 = new byte[this.width];
	/*  558 */         j = paramArrayOfInt.length;
	/*  559 */         int k = this.width;
	/*      */         do { j -= 2; localObject1[k] = ((byte)paramArrayOfInt[j]);
	/*      */ 
	/*  559 */           k--; } while (k >= 0);
	/*      */ 
	/*  561 */         this.ji_.setChannel(3, paramInt3, ((byte[])localObject1));
	/*      */       }
	/*      */       else
	/*      */       {
	/*  566 */         byte[] localObject1 = new byte[paramArrayOfInt.length];
	/*  567 */         j = paramArrayOfInt.length;
	/*      */         do { localObject1[j] = ((byte)paramArrayOfInt[j]);
	/*      */ 
	/*  567 */           j--; } while (j >= 0);
	/*      */ 
	/*  570 */         if (this.depth == 8)
	/*      */         {
	/*  572 */           this.ji_.setChannel(3, paramInt3, (byte[])localObject1);
	/*      */         }
	/*      */         else
	/*      */         {
	/*  577 */           byte[] arrayOfByte = new byte[this.width];
	/*  578 */           JimiUtil.expandPixels(this.depth, (byte[])localObject1, arrayOfByte, arrayOfByte.length);
	/*  579 */           this.ji_.setChannel(3, paramInt3, arrayOfByte);
	/*      */         }
	/*      */       }
	/*      */ 
	/*  583 */       break;
	/*      */     case 4:
	/*      */ 
	/*  587 */        int[] localObject4 = new int[this.width];
	/*  589 */       if (this.depth == 16)
	/*      */       {
	/*  591 */         j = paramArrayOfInt.length;
	/*  592 */         m = this.width;
	/*      */         do { j -= 2; j -= 2; paramArrayOfInt[j] |= paramArrayOfInt[j] << 8;
	/*      */ 
	/*  592 */           m--; } while (m >= 0);
	/*      */ 
	/*  594 */         this.ji_.setChannel(paramInt3, (int[])localObject4);
	/*      */       }
	/*      */       else
	/*      */       {
	/*  599 */         j = paramArrayOfInt.length;
	/*  600 */         m = this.width;
	/*      */         do { localObject4[m] = (paramArrayOfInt[(--j)] | paramArrayOfInt[(--j)] << 8);
	/*      */ 
	/*  600 */           m--; } while (m >= 0);
	/*      */ 
	/*  602 */         this.ji_.setChannel(paramInt3, (int[])localObject4);
	/*      */       }
	/*      */ 
	/*  605 */       break;
	/*      */     case 2:
	/*  609 */       int[] localObject2 = new int[this.width];
	/*      */ 
	/*  612 */       if (this.depth == 8)
	/*      */       {
	/*  614 */         j = paramArrayOfInt.length;
	/*  615 */         m = this.width;
	/*      */         do { localObject2[m] = (paramArrayOfInt[(--j)] | paramArrayOfInt[(--j)] << 8 | paramArrayOfInt[(--j)] << 16);
	/*      */ 
	/*  615 */           m--; } while (m >= 0);
	/*      */ 
	/*  617 */         this.ji_.setChannel(paramInt3, (int[])localObject2);
	/*      */       }
	/*      */       else
	/*      */       {
	/*  622 */         j = paramArrayOfInt.length;
	/*  623 */         m = this.width;
	/*      */         do { j -= 2; j -= 2; j -= 2; localObject2[m] = (paramArrayOfInt[j] | paramArrayOfInt[j] << 8 | paramArrayOfInt[j] << 16);
	/*      */ 
	/*  623 */           m--; } while (m >= 0);
	/*      */ 
	/*  625 */         this.ji_.setChannel(paramInt3, (int[])localObject2);
	/*      */       }
	/*      */ 
	/*  628 */       break;
	/*      */     case 3:
	/*  633 */       byte[] localObject3 = new byte[paramArrayOfInt.length];
	/*  634 */       j = paramArrayOfInt.length;
	/*      */       do { localObject3[j] = ((byte)paramArrayOfInt[j]);
	/*      */ 
	/*  634 */         j--; } while (j >= 0);
	/*      */ 
	/*  637 */       if (this.depth == 8)
	/*      */       {
	/*  639 */         this.ji_.setChannel(0, paramInt3, (byte[])localObject3);
	/*      */       }
	/*      */       else
	/*      */       {
	/*  644 */         byte[] localObject32 = new byte[this.width];
	/*  645 */         JimiUtil.expandPixels(this.depth, (byte[])localObject3, (byte[])localObject32, localObject32.length);
	/*  646 */         this.ji_.setChannel(0, paramInt3, (byte[])localObject32);
	/*      */       }
	/*      */ 
	/*  649 */       break;
	/*      */     case 6:
	/*      */       int i;
	/*  655 */       if (this.depth == 8)
	/*      */       {
	/*  657 */         localObject2 = new int[this.width];
	/*  658 */         i = paramArrayOfInt.length;
	/*  659 */         j = this.width;
	/*      */         do { localObject2[j] = (paramArrayOfInt[(--i)] << 24 | paramArrayOfInt[(--i)] | paramArrayOfInt[(--i)] << 8 | paramArrayOfInt[(--i)] << 16);
	/*      */ 
	/*  659 */           j--; } while (j >= 0);
	/*      */ 
	/*  661 */         this.ji_.setChannel(paramInt3, (int[])localObject2);
	/*      */       }
	/*      */       else
	/*      */       {
	/*  666 */         localObject2 = new int[this.width];
	/*  667 */         i = paramArrayOfInt.length;
	/*  668 */         j = this.width;
	/*      */         do { i -= 2; i -= 2; i -= 2; i -= 2; localObject2[j] = (paramArrayOfInt[i] << 24 | paramArrayOfInt[i] | paramArrayOfInt[i] << 8 | paramArrayOfInt[i] << 16);
	/*      */ 
	/*  668 */           j--; } while (j >= 0);
	/*      */ 
	/*  670 */         this.ji_.setChannel(paramInt3, (int[])localObject2);
	/*      */ 
	/*  678 */         insertPixels(paramArrayOfInt, paramInt1, paramInt2, paramInt3);
	/*  673 */         break;
	/*      */ 
	/*      */       }
	/*      */       break;
	/*      */     case 1:
	/*      */     case 5:
	/*      */     }
	/*      */   }
/*      */ 
/*      */   private void insertPalettedPixels(int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3)
/*      */     throws JimiException
/*      */   {
/*  837 */     int i = 0;
/*  838 */     int j = paramArrayOfInt[0];
/*  839 */     byte[] arrayOfByte = this.bpixels;
/*  840 */     int k = colInc[this.pass];
/*      */     int m;
/*  842 */     switch (this.depth)
/*      */     {
/*      */     case 1:
/*  845 */       for (m = 0; m < paramInt1; paramInt2 += k)
/*      */       {
/*  847 */         if (i != 0) {
/*  848 */           i--;
/*      */         }
/*      */         else {
/*  851 */           i = 7;
/*  852 */           j = paramArrayOfInt[(m >> 3)];
/*      */         }
/*  854 */         this.ji_.setChannel(paramInt2, paramInt3, j >> i & 0x1);
/*      */ 
/*  845 */         m++;
/*      */       }
/*      */ 
/*  857 */       break;
/*      */     case 2:
/*  860 */       for (m = 0; m < paramInt1; paramInt2 += k)
/*      */       {
/*  862 */         if (i != 0) {
/*  863 */           i -= 2;
/*      */         }
/*      */         else {
/*  866 */           i = 6;
/*  867 */           j = paramArrayOfInt[(m >> 2)];
/*      */         }
/*  869 */         this.ji_.setChannel(paramInt2, paramInt3, j >> i & 0x3);
/*      */ 
/*  860 */         m++;
/*      */       }
/*      */ 
/*  872 */       break;
/*      */     case 4:
/*  875 */       for (m = 0; m < paramInt1; paramInt2 += k)
/*      */       {
/*  877 */         if (i != 0) {
/*  878 */           i = 0;
/*      */         }
/*      */         else {
/*  881 */           i = 4;
/*  882 */           j = paramArrayOfInt[(m >> 1)];
/*      */         }
/*  884 */         this.ji_.setChannel(paramInt2, paramInt3, j >> i & 0xF);
/*      */ 
/*  875 */         m++;
/*      */       }
/*      */ 
/*  887 */       break;
/*      */     case 8:
/*  890 */       m = 0;
/*      */       while (true) { this.ji_.setChannel(paramInt2, paramInt3, paramArrayOfInt[m]);
/*      */ 
/*  890 */         m++; paramInt2 += k; if (m >= paramInt1)
/*      */         {
/*  893 */           break;
/*      */         }
/*      */       }
/*      */     case 3:
/*      */     case 5:
/*      */     case 6:
/*      */     case 7:
/*      */     }
/*      */   }
/*      */ 
/*      */   private void insertPixels(int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3)
/*      */     throws JimiException
/*      */   {
/*      */     int i;
/*  692 */     switch (this.colorType)
/*      */     {
/*      */     case 0:
/*      */     case 4:
/*  696 */       insertGreyPixels(paramArrayOfInt, paramInt1, paramInt2, paramInt3);
/*  697 */       break;
/*      */     case 2:
/*  701 */       i = 0;
/*  702 */       int j = colInc[this.pass];
/*  703 */       if (this.depth == 8)
/*      */       {
/*  705 */         for (i = 0; i < paramInt1; paramInt2 += j)
/*      */         {
/*  707 */           this.ji_.setChannel(paramInt2, paramInt3, paramArrayOfInt[(i++)] << 16 | paramArrayOfInt[(i++)] << 8 | paramArrayOfInt[(i++)]);
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*  713 */         for (i = 0; i < paramInt1; paramInt2 += j) {
/*  714 */           i += 2; i += 2; this.ji_.setChannel(paramInt2, paramInt3, paramArrayOfInt[i] << 16 | paramArrayOfInt[i] << 8 | paramArrayOfInt[i]);
/*      */ 
/*  713 */           i += 2;
/*      */         }
/*      */       }
/*      */ 
/*  717 */       break;
/*      */     case 3:
/*  720 */       insertPalettedPixels(paramArrayOfInt, paramInt1, paramInt2, paramInt3);
/*  721 */       break;
/*      */     case 6:
/*  725 */       i = 0;
/*  726 */       int[] arrayOfInt = this.ipixels;
/*  727 */       int k = colInc[this.pass];
/*  728 */       if (this.depth == 8)
/*      */       {
/*  730 */         for (i = 0; i < paramInt1; paramInt2 += k) {
/*  731 */           this.ji_.setChannel(paramInt2, paramInt3, paramArrayOfInt[(i++)] << 16 | paramArrayOfInt[(i++)] << 8 | paramArrayOfInt[(i++)] | paramArrayOfInt[(i++)] << 24);
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/*  736 */         i = 0;
/*      */         while (true) { i += 2; i += 2; i += 2; this.ji_.setChannel(paramInt2, paramInt3, paramArrayOfInt[i] << 16 | paramArrayOfInt[i] << 8 | paramArrayOfInt[i] | paramArrayOfInt[i] << 24);
/*      */ 
/*  736 */           i += 2; paramInt2 += k; if (i >= paramInt1)
/*      */           {
/*  740 */             break;
/*      */           }
/*      */         }
/*      */       }
/*      */       break;
/*      */     case 1:
/*      */     case 5:
/*      */     }
/*      */   }
/*      */ 
/*      */   private void readImageData()
/*      */     throws JimiException, IOException
/*      */   {
/*  419 */     SequenceInputStream localSequenceInputStream = new SequenceInputStream(new IDATEnumeration(this));
/*      */ 
/*  426 */     DataInputStream localDataInputStream = new DataInputStream(
/*  427 */       new InflaterInputStream(localSequenceInputStream, 
/*  429 */       new Inflater()));
/*  430 */     int i = 0;
/*      */ 
/*  433 */     switch (this.colorType)
/*      */     {
/*      */     case 0:
/*      */     case 3:
/*  437 */       i = this.depth;
/*  438 */       break;
/*      */     case 2:
/*  441 */       i = 3 * this.depth;
/*  442 */       break;
/*      */     case 4:
/*  445 */       i = this.depth << 1;
/*  446 */       break;
/*      */     case 6:
/*  449 */       i = this.depth << 2;
/*  450 */       break;
/*      */     case 1:
/*      */     case 5:
/*  453 */     }int j = i + 7 >> 3;
/*      */ 
/*  461 */     for (this.pass = (this.multipass ? 1 : 0); this.pass < 8; this.pass += 1)
/*      */     {
/*  463 */       int k = this.pass;
/*  464 */       int m = rowInc[k];
/*  465 */       int n = colInc[k];
/*  466 */       int i1 = startingCol[k];
/*      */ 
/*  468 */       int i2 = (this.dataWidth - i1 + n - 1) / n;
/*  469 */       int i3 = i2 * j;
/*  470 */       int i4 = i2 * i + 7 >> 3;
/*  471 */       int i5 = startingRow[k];
/*      */ 
/*  473 */       if ((this.dataHeight > i5) && (i4 != 0))
/*      */       {
/*  476 */         int i6 = m * this.dataWidth;
/*      */ 
/*  478 */         byte[] arrayOfByte = new byte[i4];
/*  479 */         Object localObject1 = new int[i4];
/*  480 */         Object localObject2 = null;
/*  481 */         Object localObject3 = new int[i4];
/*      */ 
/*  484 */         int i7 = i5;
/*  485 */         int i8 = 0;
/*      */ 
/*  488 */         for (int i9 = i5; i9 < this.dataHeight; i9 += m)
/*      */         {
/*  493 */           int i10 = localDataInputStream.read();
/*  494 */           localDataInputStream.readFully(arrayOfByte);
/*      */ 
/*  496 */           if (!filterRow(arrayOfByte, (int[])localObject1, (int[])localObject2, i10, j)) {
/*  497 */             throw new JimiException("Unknown filter type: " + i10);
/*      */           }
/*      */ 
/*  508 */           insertJimiPixels((int[])localObject1, i3, i1, i9);
/*      */ 
/*  512 */           localObject2 = localObject1;
/*  513 */           localObject1 = localObject3;
/*  514 */           localObject3 = localObject2;
/*      */         }
/*  516 */         if (!this.multipass)
/*      */         {
/*      */           break;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  528 */     while (localDataInputStream.read() != -1);
/*      */   }
/*      */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.decoder.png.PNGReader
 * JD-Core Version:    0.6.2
 */