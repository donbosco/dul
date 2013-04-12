/*     */ package pelib.ui;
/*     */ 
/*     */ import java.awt.Button;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dialog;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.FlowLayout;
/*     */ import java.awt.Frame;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Panel;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.awt.event.MouseListener;
/*     */ import java.util.Iterator;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class ColourPaletteDialog extends Dialog
/*     */   implements ActionListener
/*     */ {
/*     */   private ColourPalette palette;
/*     */   private Button okButton;
/*     */   private Button cancelButton;
/*     */   private ColourPreview preview;
/*     */   private boolean cancelled;
/*     */ 
/*     */   private ColourPaletteDialog(Frame owner)
/*     */   {
/*  24 */     super(owner, "Choose colour", true);
/*  25 */     this.palette = new ColourPalette();
/*  26 */     this.palette.addActionListener(this);
/*  27 */     add(this.palette);
/*     */ 
/*  29 */     Panel panel = new Panel();
/*  30 */     add(panel, "South");
/*  31 */     panel.setLayout(new FlowLayout(2));
/*     */ 
/*  34 */     this.okButton = new Button("OK");
/*  35 */     this.cancelButton = new Button("Cancel");
/*  36 */     this.preview = new ColourPreview();
/*  37 */     panel.add(this.preview);
/*  38 */     panel.add(this.okButton);
/*  39 */     panel.add(this.cancelButton);
/*  40 */     this.okButton.addActionListener(this);
/*  41 */     this.cancelButton.addActionListener(this);
/*     */ 
/*  43 */     setSize(350, 250);
/*     */   }
/*     */ 
/*     */   public void actionPerformed(ActionEvent e)
/*     */   {
/*  48 */     if (e.getSource() == this.palette)
/*     */     {
/*  50 */       this.preview.setColor(this.palette.getColor());
/*  51 */       this.preview.repaint();
/*     */     }
/*  53 */     else if (e.getSource() == this.okButton)
/*     */     {
/*  55 */       this.cancelled = false;
/*  56 */       setVisible(false);
/*     */     }
/*  58 */     else if (e.getSource() == this.cancelButton)
/*     */     {
/*  60 */       this.cancelled = true;
/*  61 */       setVisible(false);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean wasCancelled()
/*     */   {
/*  67 */     return this.cancelled;
/*     */   }
/*     */ 
/*     */   public static int promptColour(Frame owner, int defaultColour)
/*     */   {
/*  72 */     ColourPaletteDialog dlg = new ColourPaletteDialog(owner);
/*  73 */     dlg.palette.setColour(defaultColour);
/*  74 */     dlg.show();
/*  75 */     if (dlg.wasCancelled()) {
/*  76 */       return defaultColour;
/*     */     }
/*  78 */     return dlg.palette.getBGR();
/*     */   }
/*     */ 
/*     */   public static class ColourPreview extends Component implements MouseListener
/*     */   {
/*     */     private Vector listeners;
/*     */     private Color colour;
/*     */ 
/*     */     public ColourPreview()
/*     */     {
/*  89 */       this.listeners = new Vector();
/*  90 */       addMouseListener(this);
/*  91 */       this.colour = new Color(0);
/*     */     }
/*     */ 
/*     */     public void setColor(Color colour)
/*     */     {
/*  96 */       this.colour = colour;
/*  97 */       repaint();
/*     */     }
/*     */ 
/*     */     public Color getColor()
/*     */     {
/* 102 */       return this.colour;
/*     */     }
/*     */ 
/*     */     public void update(Graphics g)
/*     */     {
/* 107 */       paint(g);
/*     */     }
/*     */ 
/*     */     public void paint(Graphics g)
/*     */     {
/* 112 */       g.setColor(this.colour);
/* 113 */       g.fillRect(1, 1, getWidth() - 2, getHeight() - 2);
/*     */ 
/* 115 */       g.setColor(Color.black);
/* 116 */       g.drawRect(0, 0, getWidth(), getHeight());
/*     */     }
/*     */ 
/*     */     public Dimension getPreferredSize()
/*     */     {
/* 121 */       return new Dimension(20, 20);
/*     */     }
/*     */ 
/*     */     public void addActionListener(ActionListener listener)
/*     */     {
/* 126 */       this.listeners.add(listener);
/*     */     }
/*     */ 
/*     */     public void mousePressed(MouseEvent event)
/*     */     {
/* 131 */       for (Iterator it = this.listeners.iterator(); it.hasNext(); )
/*     */       {
/* 133 */         ActionListener listener = (ActionListener)it.next();
/* 134 */         listener.actionPerformed(new ActionEvent(this, 1001, ""));
/*     */       }
/*     */     }
/*     */ 
/*     */     public void mouseEntered(MouseEvent event)
/*     */     {
/*     */     }
/*     */ 
/*     */     public void mouseExited(MouseEvent event)
/*     */     {
/*     */     }
/*     */ 
/*     */     public void mouseReleased(MouseEvent event)
/*     */     {
/*     */     }
/*     */ 
/*     */     public void mouseClicked(MouseEvent event)
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.ui.ColourPaletteDialog
 * JD-Core Version:    0.6.2
 */