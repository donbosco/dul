/*    */ package pelib.ui;
/*    */ 
/*    */ import java.awt.Dialog;
/*    */ import java.awt.FlowLayout;
/*    */ import java.awt.Frame;
/*    */ import java.awt.Label;
/*    */ import java.awt.Panel;
/*    */ 
/*    */ public class ProgressDialog extends Dialog
/*    */ {
/*    */   private ProgressBar bar;
/*    */   private Label label;
/*    */ 
/*    */   public ProgressDialog(Frame owner)
/*    */   {
/* 17 */     super(owner);
/* 18 */     setModal(false);
/*    */ 
/* 20 */     Panel panel = new Panel();
/* 21 */     panel.setLayout(new FlowLayout(0));
/* 22 */     this.label = new Label();
/* 23 */     panel.add(this.label);
/*    */ 
/* 25 */     this.bar = new ProgressBar();
/*    */ 
/* 27 */     add(panel, "North");
/* 28 */     add(this.bar, "Center");
/*    */ 
/* 30 */     pack();
/* 31 */     setLocationRelativeTo(getOwner());
/*    */   }
/*    */ 
/*    */   public void setProgress(int value, int range)
/*    */   {
/* 36 */     this.bar.setValue(value);
/* 37 */     this.bar.setRange(range);
/* 38 */     this.bar.updateImmediately();
/*    */   }
/*    */ 
/*    */   public void setDescription(String description)
/*    */   {
/* 43 */     this.label.setText(description);
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.ui.ProgressDialog
 * JD-Core Version:    0.6.2
 */