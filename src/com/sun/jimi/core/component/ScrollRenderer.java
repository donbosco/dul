/*    */ package com.sun.jimi.core.component;
/*    */ 
/*    */ import java.awt.Component;
/*    */ import java.awt.Container;
/*    */ import java.awt.Dimension;
/*    */ import java.awt.Graphics;
/*    */ import java.awt.ScrollPane;
/*    */ 
/*    */ public class ScrollRenderer extends AbstractRenderer
/*    */ {
/*    */   private ScrollPane jsp;
/*    */ 
/*    */   public ScrollRenderer(JimiCanvas paramJimiCanvas)
/*    */   {
/* 14 */     this.canvas = paramJimiCanvas;
/* 15 */     this.jsp = new ScrollPane();
/* 16 */     this.jsp.add(this);
/*    */   }
/*    */ 
/*    */   public Component getContentPane() {
/* 20 */     return this.jsp;
/*    */   }
/*    */ 
/*    */   public void paint(Graphics paramGraphics)
/*    */   {
/* 33 */     paramGraphics.setColor(getForeground());
/* 34 */     paramGraphics.fillRect(0, 0, getSize().width, getSize().height);
/* 35 */     super.paint(paramGraphics);
/*    */   }
/*    */ 
/*    */   public void render()
/*    */   {
/* 25 */     getImage();
/* 26 */     repaint();
/* 27 */     invalidate();
/* 28 */     this.jsp.validate();
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.component.ScrollRenderer
 * JD-Core Version:    0.6.2
 */