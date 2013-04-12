/*     */ package dulux;
/*     */ 
/*     */ import duluxskin.Skin;
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.awt.event.KeyListener;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.awt.event.MouseListener;
/*     */ import java.awt.event.MouseMotionListener;
/*     */ import java.awt.event.MouseWheelEvent;
/*     */ import java.awt.event.MouseWheelListener;
/*     */ import pelib.PaintExplorer;
/*     */ import pelib.PaintManager;
/*     */ 
/*     */ public class DuluxPaintInput
/*     */   implements MouseListener, MouseMotionListener, MouseWheelListener, KeyListener
/*     */ {
/*     */   private PaintExplorer explorer;
/*     */   private DuluxPaintView view;
/*     */   private DuluxPaintInputLayer layer;
/*     */   private DuluxMyColour duluxMyColour;
/*  45 */   private boolean requestFocus = false;
/*     */   public static final int MODE_PAINT = 0;
/*     */   public static final int MODE_SCISSORS = 1;
/*     */   public static final int MODE_PAN = 2;
/*     */   public static final int MODE_ERASE = 3;
/*     */   private int mode;
/*     */   private static final int MOUSE_NONE = 0;
/*     */   private static final int MOUSE_PAN = 1;
/*     */   private static final int MOUSE_PAINT = 2;
/*     */   private static final int MOUSE_UNPAINT = 3;
/*     */   private static final int MOUSE_ERASE = 4;
/*     */   private static final int MOUSE_UNERASE = 5;
/*     */   private int mouse;
/*     */   private boolean spaceDown;
/*     */ 
/*     */   public DuluxPaintInput(PaintExplorer explorer, DuluxPaintView view, DuluxMyColour duluxMyColour)
/*     */   {
/*  66 */     this.explorer = explorer;
/*  67 */     this.view = view;
/*  68 */     this.layer = new DuluxPaintInputLayer(explorer, view, duluxMyColour);
/*  69 */     this.duluxMyColour = duluxMyColour;
/*  70 */     view.addMouseListener(this);
/*  71 */     view.addMouseMotionListener(this);
/*  72 */     view.addMouseWheelListener(this);
/*  73 */     view.addKeyListener(this);
/*     */ 
/*  75 */     this.mode = 0;
/*  76 */     this.mouse = 0;
/*  77 */     this.spaceDown = false;
/*     */   }
/*     */ 
/*     */   public void setMode(int mode)
/*     */   {
/*  83 */     this.mode = mode;
/*     */   }
/*     */ 
/*     */   public int getMode()
/*     */   {
/*  94 */     return this.mode;
/*     */   }
/*     */ 
/*     */   public void setEraserRadius(int radius)
/*     */   {
/*  99 */     this.layer.setEraserRadius(radius);
/*     */   }
/*     */ 
/*     */   public int getEraserRadius()
/*     */   {
/* 104 */     return this.layer.getEraserRadius();
/*     */   }
/*     */ 
/*     */   public DuluxPaintInputLayer getInputLayer()
/*     */   {
/* 109 */     return this.layer;
/*     */   }
/*     */ 
/*     */   public void mousePressed(MouseEvent e)
/*     */   {
/* 114 */     this.view.setCapture();
/*     */ 
/* 117 */     if (e.getButton() == 1)
/*     */     {
/* 119 */       if ((this.spaceDown) || (this.mode == 2))
/*     */       {
/* 121 */         this.mouse = 1;
/* 122 */         this.layer.panBegin(e.getPoint());
/*     */       }
/* 124 */       else if (this.mode == 0)
/*     */       {
/* 126 */         if (e.isControlDown())
/* 127 */           this.layer.unpaintBegin(e.getPoint());
/*     */         else {
/* 129 */           this.layer.paintBegin(e.getPoint());
/*     */         }
/* 131 */         this.mouse = 2;
/*     */       }
/* 133 */       else if (this.mode == 1)
/*     */       {
/* 135 */         this.layer.scissorsSeed(e.getPoint());
/*     */       }
/* 137 */       else if (this.mode == 3) {
/* 138 */         this.layer.eraseBegin(e.getPoint());
/* 139 */         this.mouse = 4;
/*     */       }
/*     */ 
/*     */     }
/* 143 */     else if (e.getButton() == 2)
/*     */     {
/* 145 */       this.mouse = 1;
/* 146 */       this.layer.panBegin(e.getPoint());
/*     */     }
/* 149 */     else if (e.getButton() == 3)
/*     */     {
/* 151 */       if (this.mode == 0)
/*     */       {
/* 153 */         this.layer.unpaintBegin(e.getPoint());
/* 154 */         this.mouse = 3;
/*     */       }
/* 156 */       else if (this.mode == 1)
/*     */       {
/* 158 */         this.layer.scissorsCancel();
/*     */       }
/* 160 */       else if (this.mode == 3)
/*     */       {
/* 162 */         this.layer.unEraseBegin(e.getPoint());
/* 163 */         this.mouse = 5;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void mouseReleased(MouseEvent e)
/*     */   {
/* 170 */     if ((this.mouse == 2) || (this.mouse == 3))
/* 171 */       this.layer.paintEnd();
/* 172 */     else if ((this.mouse == 4) || (this.mouse == 5))
/* 173 */       this.layer.eraseEnd();
/* 174 */     this.mouse = 0;
/*     */ 
/* 176 */     this.view.releaseCapture();
/*     */   }
/*     */ 
/*     */   public void mouseDragged(MouseEvent e)
/*     */   {
/* 181 */     if (this.mode == 1) {
/* 182 */       this.layer.scissorsHover(e.getPoint());
/*     */     }
/* 184 */     switch (this.mouse)
/*     */     {
/*     */     case 1:
/* 187 */       this.layer.panContinue(e.getPoint());
/* 188 */       break;
/*     */     case 2:
/* 190 */       if (this.mode == 0)
/*     */       {
/* 192 */         if (e.isControlDown())
/* 193 */           this.layer.unpaintContinue(e.getPoint());
/*     */         else
/* 195 */           this.layer.paintContinue(e.getPoint());  } break;
/*     */     case 3:
/* 199 */       if (this.mode == 0)
/* 200 */         this.layer.unpaintContinue(e.getPoint()); break;
/*     */     case 4:
/* 203 */       if (this.mode == 3)
/* 204 */         this.layer.eraseContinue(e.getPoint()); break;
/*     */     case 5:
/* 207 */       if (this.mode == 3)
/* 208 */         this.layer.unEraseContinue(e.getPoint());
/*     */       break;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void mouseMoved(MouseEvent e) {
/* 214 */     if (!this.requestFocus) {
/* 215 */       this.duluxMyColour.getSkin().requestFocus();
/* 216 */       this.requestFocus = true;
/*     */     }
/* 218 */     if (this.mode == 1)
/* 219 */       this.layer.scissorsHover(e.getPoint());
/*     */   }
/*     */ 
/*     */   public void mouseClicked(MouseEvent e)
/*     */   {
/* 225 */     if ((this.mode == 1) && (e.getClickCount() == 2)) {
/* 226 */       this.layer.scissorsCommit();
/* 227 */       setMode(0);
/* 228 */       this.duluxMyColour.refreshModes();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void mouseEntered(MouseEvent e) {
/*     */   }
/*     */ 
/*     */   public void mouseExited(MouseEvent e) {
/*     */   }
/*     */ 
/*     */   public void mouseWheelMoved(MouseWheelEvent e) {
/* 239 */     this.layer.zoomIn(-e.getWheelRotation());
/*     */   }
/*     */ 
/*     */   public void keyPressed(KeyEvent e)
/*     */   {
/* 245 */     if (!this.explorer.isReady()) {
/* 246 */       return;
/*     */     }
/* 248 */     switch (e.getKeyCode())
/*     */     {
/*     */     case 16:
/* 251 */       this.layer.setScissorsStraightLine(true);
/* 252 */       break;
/*     */     case 90:
/* 255 */       if ((e.isControlDown()) && (this.mouse != 2))
/*     */       {
/* 257 */         this.explorer.undo();
/* 258 */         this.view.updateView();
/* 259 */         this.duluxMyColour.refreshButtonColours(); } break;
/*     */     case 89:
/* 264 */       if ((e.isControlDown()) && (this.mouse != 2))
/*     */       {
/* 266 */         this.explorer.redo();
/* 267 */         this.view.updateView();
/* 268 */         this.duluxMyColour.refreshButtonColours(); } break;
/*     */     case 32:
/* 273 */       this.spaceDown = true;
/* 274 */       e.consume();
/* 275 */       break;
/*     */     case 27:
/* 278 */       if (this.mode == 1)
/* 279 */         this.layer.scissorsCancel();
/*     */       else
/* 281 */         this.explorer.getPaintManager().setSelectedMask(null);
/*     */       break;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void keyReleased(KeyEvent e)
/*     */   {
/* 289 */     if (!this.explorer.isReady()) {
/* 290 */       return;
/*     */     }
/* 292 */     switch (e.getKeyCode())
/*     */     {
/*     */     case 16:
/* 295 */       this.layer.setScissorsStraightLine(false);
/* 296 */       break;
/*     */     case 32:
/* 299 */       this.spaceDown = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void keyTyped(KeyEvent e)
/*     */   {
/* 305 */     if (!this.explorer.isReady()) {
/* 306 */       return;
/*     */     }
/* 308 */     switch (e.getKeyChar())
/*     */     {
/*     */     case '+':
/*     */     case '=':
/* 312 */       this.layer.zoomIn(1);
/* 313 */       e.consume();
/* 314 */       break;
/*     */     case '-':
/* 317 */       this.layer.zoomOut(1);
/* 318 */       e.consume();
/*     */     }
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     dulux.DuluxPaintInput
 * JD-Core Version:    0.6.2
 */