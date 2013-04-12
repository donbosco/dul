/*     */ package dulux;
/*     */ 
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Frame;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Image;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.event.WindowAdapter;
/*     */ import java.awt.event.WindowEvent;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.net.URL;
/*     */ 
/*     */ public class DuluxMyColourFrame extends Frame
/*     */ {
/*     */   private boolean fullscreen;
/*     */   private boolean inhibitFullscreenMinimize;
/*     */ 
/*     */   public DuluxMyColourFrame(boolean v_fullscreen, String sId, String webserviceDomain, boolean loadProjectAtStartup, boolean loadImage, boolean isNZ, int colour1, int colour2, int colour3)
/*     */     throws IOException
/*     */   {
/*  32 */     this.fullscreen = v_fullscreen;
/*  33 */     addWindowListener(new WindowAdapter() {
/*     */       public void windowClosing(WindowEvent e) {
/*  35 */         System.exit(0);
/*     */       }
/*     */ 
/*     */       public void windowDeactivated(WindowEvent e) {
/*  39 */         if ((DuluxMyColourFrame.this.fullscreen) && (e.getOppositeWindow() == null) && (!DuluxMyColourFrame.this.inhibitFullscreenMinimize))
/*     */         {
/*  41 */           DuluxMyColourFrame.this.setExtendedState(1);
/*     */         }
/*     */       }
/*     */ 
/*     */       public void windowActivated(WindowEvent e) {
/*  46 */         if (DuluxMyColourFrame.this.fullscreen)
/*  47 */           DuluxMyColourFrame.this.setExtendedState(6);
/*     */       }
/*     */     });
/*  53 */     URL iconURL = getClass().getResource("/mycolour.gif");
/*  54 */     Image iconImage = Toolkit.getDefaultToolkit().createImage(iconURL);
/*  55 */     setIconImage(iconImage);
/*     */ 
/*  57 */     if (this.fullscreen)
/*  58 */       setFullscreen();
/*     */     else {
/*  60 */       setSize(1024, 795);
/*     */     }
/*  62 */     setLayout(new BorderLayout());
/*  63 */     add(new DuluxMyColour(this, sId, webserviceDomain, loadProjectAtStartup, loadImage, isNZ, colour1, colour2, colour3));
/*     */   }
/*     */ 
/*     */   public void setEnablePrintDialog(boolean enable) {
/*  67 */     this.inhibitFullscreenMinimize = enable;
/*  68 */     if (this.fullscreen)
/*  69 */       setAlwaysOnTop(!enable);
/*     */   }
/*     */ 
/*     */   private void setFullscreen()
/*     */   {
/*  74 */     setUndecorated(true);
/*  75 */     setAlwaysOnTop(true);
/*  76 */     setExtendedState(6);
/*     */   }
/*     */ 
/*     */   public void update(Graphics g) {
/*  80 */     paint(g); } 
/*  84 */   public static void main(String[] args) { String sId = "";
/*     */ 
/*  86 */     boolean isNZ = true;
/*  87 */     boolean loadProjectAtStart = true;
/*  88 */     boolean loadImage = false;
/*  89 */     int colour1 = 330835;
/*  90 */     int colour2 = 330921;
/*  91 */     int colour3 = 330923;
/*     */     String webserviceDomain;
/*     */     try { //sId = args[0];
/*  95 */       /*webserviceDomain = args[1];
  96        isNZ = Boolean.parseBoolean(args[2]);
  97        colour1 = Integer.parseInt(args[3]);
  98        colour2 = Integer.parseInt(args[4]);
  99        colour3 = Integer.parseInt(args[5]);*/
/*     */     }
/*     */     catch (ArrayIndexOutOfBoundsException e)
/*     */     {
/* 101 */       System.out.println("NOT ENOUGH INPUT ARGUMENTS FOUND");
/* 102 */       loadProjectAtStart = true;
/* 103 */       loadImage = false;
/*     */       try
/*     */       {
/* 107 */         if ((args[2].toLowerCase().equals("false")) || (args[2].toLowerCase().equals("true")))
/*     */         {
/* 109 */           isNZ = Boolean.parseBoolean(args[2]);
/*     */         }
/* 111 */         if (isNZ)
/*     */         {
/* 113 */           colour1 = 330835;
/* 114 */           colour2 = 330921;
/* 115 */           colour3 = 330923;
/*     */         }
/*     */         else
/*     */         {
/* 119 */           colour1 = 252443;
/* 120 */           colour2 = 252466;
/* 121 */           colour3 = 252463;
/*     */         }
/*     */       }
/*     */       catch (Exception eex) {
/* 125 */         colour1 = 412695;
/* 126 */         colour2 = 412718;
/* 127 */         colour3 = 412715;
/*     */       }
/*     */ 
/* 130 */       if (isNZ) {
/* 131 */         webserviceDomain = "http://www.dulux.co.nz";
/* 132 */         if ((sId == null) || (sId == "") || (sId.length() != 24))
/*     */         {
/* 138 */           sId = "qp1n2d55tl3pkqmlvgkcwfyq";
/*     */         }
/*     */       }
/*     */       else {
/* 142 */         webserviceDomain = "http://dulux.staging.es-i.com.au";
/* 143 */         sId = "qp1n2d55tl3pkqmlvgkcwfyq";
/*     */       }
/*     */     }
			webserviceDomain = "http://www.dulux.co.nz";
			sId = "qp1n2d55tl3pkqmlvgkcwfyq";
/*     */     try {
/* 147 */       new DuluxMyColourFrame(false, sId, webserviceDomain, loadProjectAtStart, loadImage, isNZ, colour1, colour2, colour3).show();
/*     */     }
/*     */     catch (IOException ea)
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     dulux.DuluxMyColourFrame
 * JD-Core Version:    0.6.2
 */