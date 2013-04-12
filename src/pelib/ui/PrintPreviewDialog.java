/*     */ package pelib.ui;
/*     */ 
/*     */ import java.awt.Adjustable;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Button;
/*     */ import java.awt.Canvas;
/*     */ import java.awt.Choice;
/*     */ import java.awt.Color;
/*     */ import java.awt.Dialog;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.FlowLayout;
/*     */ import java.awt.Frame;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Label;
/*     */ import java.awt.Panel;
/*     */ import java.awt.RenderingHints;
/*     */ import java.awt.ScrollPane;
/*     */ import java.awt.SystemColor;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.ItemEvent;
/*     */ import java.awt.event.ItemListener;
/*     */ import java.awt.event.WindowAdapter;
/*     */ import java.awt.event.WindowEvent;
/*     */ import java.awt.geom.AffineTransform;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.print.PageFormat;
/*     */ import java.awt.print.Printable;
/*     */ import java.awt.print.PrinterException;
/*     */ import java.awt.print.PrinterJob;
/*     */ 
/*     */ public class PrintPreviewDialog extends Dialog
/*     */   implements ActionListener, ItemListener
/*     */ {
/*     */   private Printable printable;
/*     */   private PrinterJob job;
/*     */   private PageFormat format;
/*     */   private int page;
/*     */   private ScrollPane scroll;
/*     */   private PreviewArea area;
/*     */   private Button printButton;
/*     */   private Button setupButton;
/*     */   private Button closeButton;
/*     */   private Choice zoomChoice;
/*     */   private int zoomIndex;
/*  61 */   private static String[] zoomNames = { "50%", "75%", "100%", "125%", "150%" };
/*     */ 
/*  65 */   private double[] zooms = { 0.5D, 0.75D, 1.0D, 1.25D, 1.5D };
/*     */ 
/*     */   public PrintPreviewDialog(Frame owner, Printable p, PrinterJob job, PageFormat format)
/*     */   {
/*  77 */     super(owner, "Print Preview");
/*     */ 
/*  79 */     this.printable = p;
/*     */ 
/*  81 */     setModal(true);
/*     */ 
/*  85 */     this.page = 0;
/*     */ 
/*  87 */     this.job = job;
/*     */ 
/*  89 */     this.format = format;
/*     */ 
/*  95 */     this.zoomIndex = 1;
/*     */ 
/*  99 */     setLayout(new BorderLayout());
/*     */ 
/* 103 */     this.area = new PreviewArea();
/*     */ 
/* 105 */     add(this.area);
/*     */ 
/* 109 */     this.scroll = new ScrollPane();
/*     */ 
/* 111 */     this.scroll.add(this.area);
/*     */ 
/* 113 */     this.scroll.getVAdjustable().setUnitIncrement(20);
/*     */ 
/* 115 */     this.scroll.getHAdjustable().setUnitIncrement(20);
/*     */ 
/* 117 */     add(this.scroll);
/*     */ 
/* 121 */     Panel buttonPanel = new Panel();
/*     */ 
/* 123 */     add(buttonPanel, "North");
/*     */ 
/* 125 */     buttonPanel.setLayout(new FlowLayout(0));
/*     */ 
/* 129 */     this.printButton = new Button("Print");
/*     */ 
/* 131 */     this.printButton.addActionListener(this);
/*     */ 
/* 133 */     buttonPanel.add(this.printButton);
/*     */ 
/* 137 */     this.setupButton = new Button("Page setup...");
/*     */ 
/* 139 */     this.setupButton.addActionListener(this);
/*     */ 
/* 141 */     buttonPanel.add(this.setupButton);
/*     */ 
/* 145 */     Label zoomLabel = new Label("Zoom:");
/*     */ 
/* 147 */     buttonPanel.add(zoomLabel);
/*     */ 
/* 151 */     this.zoomChoice = new Choice();
/*     */ 
/* 153 */     this.zoomChoice.addItemListener(this);
/*     */ 
/* 155 */     buttonPanel.add(this.zoomChoice);
/*     */ 
/* 157 */     for (int i = 0; i < zoomNames.length; i++)
/*     */     {
/* 159 */       this.zoomChoice.addItem(zoomNames[i]);
/*     */     }
/* 161 */     this.zoomChoice.select(this.zoomIndex);
/*     */ 
/* 165 */     this.closeButton = new Button("Close");
/*     */ 
/* 167 */     this.closeButton.addActionListener(this);
/*     */ 
/* 169 */     buttonPanel.add(this.closeButton);
/*     */ 
/* 173 */     Dimension size = this.area.getSize();
/*     */ 
/* 175 */     size.height += 70;
/*     */ 
/* 177 */     size.width += 20;
/*     */ 
/* 179 */     setSize(size);
/*     */ 
/* 183 */     addWindowListener(new WindowAdapter()
/*     */     {
/*     */       public void windowClosing(WindowEvent e)
/*     */       {
/* 187 */         PrintPreviewDialog.this.hide();
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   public PageFormat getPageFormat()
/*     */   {
/* 201 */     return this.format;
/*     */   }
/*     */ 
/*     */   public void actionPerformed(ActionEvent e)
/*     */   {
/* 211 */     if (e.getSource() == this.printButton)
/*     */     {
/*     */       try
/*     */       {
/* 217 */         hide();
/*     */ 
/* 219 */         this.job.setPrintable(this.printable, this.format);
/*     */ 
/* 221 */         this.job.print();
/*     */       }
/*     */       catch (PrinterException err)
/*     */       {
/* 225 */         err.printStackTrace();
/*     */       }
/*     */ 
/*     */     }
/* 231 */     else if (e.getSource() == this.setupButton)
/*     */     {
/* 235 */       this.format = this.job.pageDialog(this.format);
/*     */ 
/* 239 */       this.scroll.doLayout();
/*     */ 
/* 241 */       this.scroll.invalidate();
/*     */ 
/* 243 */       this.scroll.repaint();
/*     */ 
/* 245 */       this.area.repaint();
/*     */     }
/* 249 */     else if (e.getSource() == this.closeButton)
/*     */     {
/* 253 */       hide();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void itemStateChanged(ItemEvent e)
/*     */   {
/* 265 */     if (e.getSource() == this.zoomChoice)
/*     */     {
/* 269 */       this.zoomIndex = this.zoomChoice.getSelectedIndex();
/*     */ 
/* 273 */       this.scroll.doLayout();
/*     */ 
/* 275 */       this.scroll.invalidate();
/*     */ 
/* 277 */       this.scroll.repaint();
/*     */ 
/* 279 */       this.area.repaint();
/*     */     }
/*     */   }
/*     */ 
/*     */   class PreviewArea extends Canvas
/*     */   {
/*     */     private BufferedImage backImage;
/*     */     private Color backgroundColor;
/*     */ 
/*     */     public PreviewArea()
/*     */     {
/* 301 */       this.backgroundColor = SystemColor.control;
/*     */     }
/*     */ 
/*     */     public Dimension getSize()
/*     */     {
/* 311 */       return new Dimension((int)(PrintPreviewDialog.this.format.getWidth() * PrintPreviewDialog.this.zooms[PrintPreviewDialog.this.zoomIndex]) + 10, (int)(PrintPreviewDialog.this.format.getHeight() * PrintPreviewDialog.this.zooms[PrintPreviewDialog.this.zoomIndex]) + 10);
/*     */     }
/*     */ 
/*     */     public void update(Graphics graphics)
/*     */     {
/* 325 */       paint(graphics);
/*     */     }
/*     */ 
/*     */     public void paint(Graphics graphics)
/*     */     {
/* 335 */       if ((this.backImage == null) || (this.backImage.getWidth() != getWidth()) || (this.backImage.getHeight() != getHeight()))
/*     */       {
/* 343 */         this.backImage = ((BufferedImage)createImage(getWidth(), getHeight()));
/*     */       }
/*     */ 
/* 351 */       Graphics2D g = (Graphics2D)this.backImage.getGraphics();
/*     */ 
/* 353 */       g.setColor(this.backgroundColor);
/*     */ 
/* 355 */       g.fillRect(0, 0, getWidth(), getHeight());
/*     */ 
/* 359 */       AffineTransform oldTransform = g.getTransform();
/*     */ 
/* 361 */       g.translate(5, 5);
/*     */ 
/* 363 */       g.scale(PrintPreviewDialog.this.zooms[PrintPreviewDialog.this.zoomIndex], PrintPreviewDialog.this.zooms[PrintPreviewDialog.this.zoomIndex]);
/*     */ 
/* 369 */       g.setColor(Color.gray);
/*     */ 
/* 371 */       g.fillRect(3, 3, (int)PrintPreviewDialog.this.format.getWidth(), (int)PrintPreviewDialog.this.format.getHeight());
/*     */ 
/* 375 */       g.setColor(Color.white);
/*     */ 
/* 377 */       g.fillRect(0, 0, (int)PrintPreviewDialog.this.format.getWidth(), (int)PrintPreviewDialog.this.format.getHeight());
/*     */ 
/* 381 */       g.setColor(Color.black);
/*     */ 
/* 383 */       g.drawRect(0, 0, (int)PrintPreviewDialog.this.format.getWidth(), (int)PrintPreviewDialog.this.format.getHeight());
/*     */ 
/* 387 */       g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
/*     */ 
/* 391 */       g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
/*     */       try
/*     */       {
/* 399 */         PrintPreviewDialog.this.printable.print(g, PrintPreviewDialog.this.format, PrintPreviewDialog.this.page);
/*     */       }
/*     */       catch (PrinterException e)
/*     */       {
/* 403 */         e.printStackTrace();
/*     */       }
/*     */ 
/* 427 */       g.setTransform(oldTransform);
/*     */ 
/* 433 */       graphics.drawImage(this.backImage, 0, 0, null);
/*     */     }
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.ui.PrintPreviewDialog
 * JD-Core Version:    0.6.2
 */