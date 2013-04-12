/*     */ package pelib;
/*     */ 
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.awt.event.KeyListener;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.awt.event.MouseListener;
/*     */ import java.awt.event.MouseMotionListener;
/*     */ import java.awt.event.MouseWheelEvent;
/*     */ import java.awt.event.MouseWheelListener;
/*     */ 
/*     */ public class PaintInput
/*     */   implements MouseListener, MouseMotionListener, MouseWheelListener, KeyListener
/*     */ {
/*     */   private PaintExplorer explorer;
/*     */   private PaintView view;
/*     */   private PaintInputLayer layer;
/*     */   public static final int MODE_PAINT = 0;
/*     */   public static final int MODE_SCISSORS = 1;
/*     */   private int mode;
/*     */   private static final int MOUSE_NONE = 0;
/*     */   private static final int MOUSE_PAN = 1;
/*     */   private static final int MOUSE_PAINT = 2;
/*     */   private static final int MOUSE_UNPAINT = 3;
/*     */   private int mouse;
/*     */   private boolean spaceDown;
/*     */ 
/*     */   public PaintInput(PaintExplorer explorer, PaintView view)
/*     */   {
/*  58 */     this.explorer = explorer;
/*  59 */     this.view = view;
/*  60 */     this.layer = new PaintInputLayer(explorer, view);
/*     */ 
/*  62 */     view.addMouseListener(this);
/*  63 */     view.addMouseMotionListener(this);
/*  64 */     view.addMouseWheelListener(this);
/*  65 */     view.addKeyListener(this);
/*     */ 
/*  67 */     this.mode = 0;
/*  68 */     this.mouse = 0;
/*  69 */     this.spaceDown = false;
/*     */   }
/*     */ 
/*     */   public void setMode(int mode)
/*     */   {
/*  74 */     this.mode = mode;
/*  75 */     if (mode != 1)
/*     */     {
/*  77 */       this.layer.scissorsCancel();
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getMode()
/*     */   {
/*  83 */     return this.mode;
/*     */   }
/*     */ 
/*     */   public void mousePressed(MouseEvent e)
/*     */   {
/*  89 */     if (e.getButton() == 1)
/*     */     {
/*  91 */       if (this.spaceDown) {
/*  92 */         this.mouse = 1;
/*  93 */       } else if (this.mode == 0)
/*     */       {
/*  95 */         if (e.isControlDown())
/*  96 */           this.layer.unpaintBegin(e.getPoint());
/*     */         else
/*  98 */           this.layer.paintBegin(e.getPoint());
/*  99 */         this.mouse = 2;
/*     */       }
/* 101 */       else if (this.mode == 1)
/*     */       {
/* 103 */         this.layer.scissorsSeed(e.getPoint());
/*     */       }
/*     */ 
/*     */     }
/* 107 */     else if (e.getButton() == 2)
/*     */     {
/* 109 */       this.mouse = 1;
/* 110 */       this.layer.panBegin(e.getPoint());
/*     */     }
/* 113 */     else if (e.getButton() == 3)
/*     */     {
/* 115 */       if (this.mode == 0)
/*     */       {
/* 117 */         this.layer.unpaintBegin(e.getPoint());
/* 118 */         this.mouse = 3;
/*     */       }
/* 120 */       else if (this.mode == 1)
/*     */       {
/* 122 */         this.layer.scissorsCancel();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void mouseReleased(MouseEvent e)
/*     */   {
/* 129 */     if ((this.mouse == 2) || (this.mouse == 3))
/* 130 */       this.layer.paintEnd();
/* 131 */     this.mouse = 0;
/*     */   }
/*     */ 
/*     */   public void mouseDragged(MouseEvent e)
/*     */   {
/* 136 */     if (this.mode == 1) {
/* 137 */       this.layer.scissorsHover(e.getPoint());
/*     */     }
/* 139 */     switch (this.mouse)
/*     */     {
/*     */     case 1:
/* 142 */       this.layer.panContinue(e.getPoint());
/* 143 */       break;
/*     */     case 2:
/* 145 */       if (this.mode == 0)
/*     */       {
/* 147 */         if (e.isControlDown())
/* 148 */           this.layer.unpaintContinue(e.getPoint());
/*     */         else
/* 150 */           this.layer.paintContinue(e.getPoint());  } break;
/*     */     case 3:
/* 154 */       if (this.mode == 0)
/* 155 */         this.layer.unpaintContinue(e.getPoint());
/*     */       break;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void mouseMoved(MouseEvent e)
/*     */   {
/* 162 */     if (this.mode == 1)
/* 163 */       this.layer.scissorsHover(e.getPoint());
/*     */   }
/*     */ 
/*     */   public void mouseClicked(MouseEvent e)
/*     */   {
/* 169 */     if ((this.mode == 1) && (e.getClickCount() == 2))
/* 170 */       this.layer.scissorsCommit(); 
/*     */   }
/*     */   public void mouseEntered(MouseEvent e) {
/*     */   }
/*     */ 
/*     */   public void mouseExited(MouseEvent e) {
/*     */   }
/*     */ 
/* 178 */   public void mouseWheelMoved(MouseWheelEvent e) { this.layer.zoomIn(-e.getWheelRotation()); }
/*     */ 
/*     */ 
/*     */   public void keyPressed(KeyEvent e)
/*     */   {
/* 183 */     if (this.view.getImage() == null) {
/* 184 */       return;
/*     */     }
/* 186 */     switch (e.getKeyCode())
/*     */     {
/*     */     case 16:
/* 189 */       this.layer.setScissorsStraightLine(true);
/* 190 */       break;
/*     */     case 90:
/* 193 */       if ((e.isControlDown()) && (this.mouse != 2))
/*     */       {
/* 195 */         this.explorer.undo();
/* 196 */         this.view.updateView(); } break;
/*     */     case 89:
/* 201 */       if ((e.isControlDown()) && (this.mouse != 2))
/*     */       {
/* 203 */         this.explorer.redo();
/* 204 */         this.view.updateView(); } break;
/*     */     case 32:
/* 209 */       this.spaceDown = true;
/* 210 */       e.consume();
/* 211 */       break;
/*     */     case 27:
/* 214 */       if (this.mode == 1)
/* 215 */         this.layer.scissorsCancel();
/*     */       else
/* 217 */         this.explorer.getPaintManager().setSelectedMask(null);
/*     */       break;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void keyReleased(KeyEvent e)
/*     */   {
/* 225 */     switch (e.getKeyCode())
/*     */     {
/*     */     case 16:
/* 228 */       this.layer.setScissorsStraightLine(false);
/* 229 */       break;
/*     */     case 32:
/* 232 */       this.spaceDown = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void keyTyped(KeyEvent e)
/*     */   {
/* 238 */     switch (e.getKeyChar())
/*     */     {
/*     */     case '+':
/*     */     case '=':
/* 242 */       this.layer.zoomIn(1);
/* 243 */       e.consume();
/* 244 */       break;
/*     */     case '-':
/* 247 */       this.layer.zoomOut(1);
/* 248 */       e.consume();
/*     */     }
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.PaintInput
 * JD-Core Version:    0.6.2
 */