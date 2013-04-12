/*     */ package pelib.print;
/*     */ 
/*     */ import java.awt.geom.GeneralPath;
/*     */ 
/*     */ public class SVGPath
/*     */ {
/*     */   private String data;
/*     */   private static final int CMD_NONE = -1;
/*     */   private static final int CMD_MOVETO = 0;
/*     */   private static final int CMD_LINETO = 1;
/*     */   private static final int CMD_CURVETO = 2;
/*     */   private static final int CMD_CURVETO2 = 3;
/*     */   private static final int CMD_CURVETO3 = 4;
/*     */   private int cmd;
/*     */   private char[] xBuf;
/*     */   private char[] yBuf;
/*     */   private int bufIdx;
/*     */   private char[] buf;
/*     */   private int xBufIdx;
/*     */   private int yBufIdx;
/*     */   private double c1x;
/*     */   private double c1y;
/*     */   private double c2x;
/*     */   private double c2y;
/*     */   private GeneralPath path;
/*     */ 
/*     */   public SVGPath(String data)
/*     */   {
/*  71 */     this.data = data;
/*     */ 
/*  73 */     this.path = new GeneralPath();
/*     */ 
/*  75 */     this.xBuf = new char[16];
/*     */ 
/*  77 */     this.yBuf = new char[16];
/*     */ 
/*  81 */     char[] d = data.toCharArray();
/*     */ 
/*  83 */     this.bufIdx = 0;
/*     */ 
/*  85 */     this.buf = this.xBuf;
/*     */ 
/*  87 */     this.cmd = -1;
/*     */ 
/*  91 */     for (int i = 0; i < d.length; i++)
/*     */     {
/*  95 */       switch (d[i])
/*     */       {
/*     */       case '.':
/*     */       case '0':
/*     */       case '1':
/*     */       case '2':
/*     */       case '3':
/*     */       case '4':
/*     */       case '5':
/*     */       case '6':
/*     */       case '7':
/*     */       case '8':
/*     */       case '9':
/* 121 */         this.buf[(this.bufIdx++)] = d[i];
/*     */ 
/* 123 */         break;
/*     */       case '\t':
/*     */       case '\n':
/*     */       case ' ':
/* 133 */         applyCommand();
/*     */ 
/* 135 */         break;
/*     */       case ',':
/* 141 */         this.xBufIdx = this.bufIdx;
/*     */ 
/* 143 */         this.bufIdx = 0;
/*     */ 
/* 145 */         this.buf = this.yBuf;
/*     */ 
/* 147 */         break;
/*     */       case 'M':
/* 153 */         applyCommand();
/*     */ 
/* 155 */         this.cmd = 0;
/*     */ 
/* 157 */         break;
/*     */       case 'L':
/* 163 */         applyCommand();
/*     */ 
/* 165 */         this.cmd = 1;
/*     */ 
/* 167 */         break;
/*     */       case 'C':
/* 173 */         applyCommand();
/*     */ 
/* 175 */         this.cmd = 2;
/*     */ 
/* 177 */         break;
/*     */       case 'Z':
/*     */       case 'z':
/* 185 */         applyCommand();
/*     */ 
/* 187 */         this.path.closePath();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 195 */     applyCommand();
/*     */   }
/*     */ 
/*     */   public GeneralPath getPath()
/*     */   {
/* 205 */     return this.path;
/*     */   }
/*     */ 
/*     */   private void applyCommand()
/*     */     throws NumberFormatException
/*     */   {
/* 217 */     if ((this.cmd == -1) || (this.bufIdx == 0))
/*     */     {
/* 219 */       return;
/*     */     }
/*     */ 
/* 223 */     this.yBufIdx = this.bufIdx;
/*     */ 
/* 227 */     double x = Double.parseDouble(new String(this.xBuf, 0, this.xBufIdx));
/*     */ 
/* 229 */     double y = Double.parseDouble(new String(this.yBuf, 0, this.yBufIdx));
/*     */ 
/* 233 */     switch (this.cmd)
/*     */     {
/*     */     case 0:
/* 239 */       this.path.moveTo((float)x, (float)y);
/*     */ 
/* 241 */       break;
/*     */     case 1:
/* 247 */       this.path.lineTo((float)x, (float)y);
/*     */ 
/* 249 */       break;
/*     */     case 2:
/* 255 */       this.c1x = x;
/*     */ 
/* 257 */       this.c1y = y;
/*     */ 
/* 259 */       this.cmd = 3;
/*     */ 
/* 261 */       break;
/*     */     case 3:
/* 267 */       this.c2x = x;
/*     */ 
/* 269 */       this.c2y = y;
/*     */ 
/* 271 */       this.cmd = 4;
/*     */ 
/* 273 */       break;
/*     */     case 4:
/* 279 */       this.path.curveTo((float)this.c1x, (float)this.c1y, (float)this.c2x, (float)this.c2y, (float)x, (float)y);
/*     */ 
/* 285 */       this.cmd = 2;
/*     */     }
/*     */ 
/* 291 */     this.xBufIdx = 0;
/*     */ 
/* 293 */     this.yBufIdx = 0;
/*     */ 
/* 295 */     this.bufIdx = 0;
/*     */ 
/* 297 */     this.buf = this.xBuf;
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.print.SVGPath
 * JD-Core Version:    0.6.2
 */