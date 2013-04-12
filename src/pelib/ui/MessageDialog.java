/*     */ package pelib.ui;
/*     */ 
/*     */ import java.awt.Button;
/*     */ import java.awt.Dialog;
/*     */ import java.awt.FlowLayout;
/*     */ import java.awt.Frame;
/*     */ import java.awt.Label;
/*     */ import java.awt.Panel;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.util.Iterator;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class MessageDialog extends Dialog
/*     */   implements ActionListener
/*     */ {
/*     */   private String message;
/*     */   private String title;
/*     */   private boolean created;
/*     */   private Vector buttons;
/*     */   private int buttonPressed;
/*     */ 
/*     */   public MessageDialog(Frame owner, String title, String message)
/*     */   {
/*  24 */     super(owner);
/*  25 */     this.message = message;
/*  26 */     this.title = title;
/*     */   }
/*     */ 
/*     */   public void addButton(String label)
/*     */   {
/*  31 */     if (this.buttons == null) {
/*  32 */       this.buttons = new Vector();
/*     */     }
/*  34 */     this.buttons.add(new Button(label));
/*     */   }
/*     */ 
/*     */   public void show()
/*     */   {
/*  39 */     if (!this.created)
/*     */     {
/*  41 */       this.created = true;
/*     */ 
/*  43 */       setModal(true);
/*  44 */       setTitle(this.title);
/*     */ 
/*  46 */       if (this.message.length() > 150)
/*     */       {
/*  48 */         WrappingLabel messageLabel = new WrappingLabel(this.message);
/*  49 */         add(messageLabel, "Center");
/*     */       }
/*     */       else
/*     */       {
/*  53 */         Panel main = new Panel();
/*  54 */         main.setLayout(new FlowLayout());
/*  55 */         Label messageLabel = new Label(this.message);
/*  56 */         main.add(messageLabel);
/*  57 */         add(main, "Center");
/*     */       }
/*     */ 
/*  60 */       Panel buttonPanel = new Panel();
/*  61 */       buttonPanel.setLayout(new FlowLayout(1));
/*     */       Iterator it;
/*  62 */       if (this.buttons == null)
/*     */       {
/*  64 */         Button okButton = new Button("OK");
/*  65 */         okButton.addActionListener(this);
/*  66 */         buttonPanel.add(okButton);
/*     */       }
/*     */       else
/*     */       {
/*  70 */         for (it = this.buttons.iterator(); it.hasNext(); )
/*     */         {
/*  72 */           Button but = (Button)it.next();
/*  73 */           but.addActionListener(this);
/*  74 */           buttonPanel.add(but);
/*     */         }
/*     */       }
/*  77 */       add(buttonPanel, "South");
/*     */ 
/*  79 */       pack();
/*     */     }
/*     */ 
/*  82 */     setLocationRelativeTo(null);
/*     */ 
/*  84 */     super.show();
/*     */   }
/*     */ 
/*     */   public int getResponse()
/*     */   {
/*  89 */     return this.buttonPressed;
/*     */   }
/*     */ 
/*     */   public void actionPerformed(ActionEvent e)
/*     */   {
/*  94 */     if (this.buttons != null) {
/*  95 */       this.buttonPressed = this.buttons.indexOf(e.getSource());
/*     */     }
/*  97 */     hide();
/*     */   }
/*     */ 
/*     */   public static void message(Frame owner, String title, String message)
/*     */   {
/* 110 */     new MessageDialog(owner, title, message).show();
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.ui.MessageDialog
 * JD-Core Version:    0.6.2
 */