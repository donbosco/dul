/*     */ package pelib.ui;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.LayoutManager;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class VerticalLayout
/*     */   implements LayoutManager
/*     */ {
/*     */   private int verticalPad;
/*     */   private int horizontalPad;
/*     */   private Map layouts;
/*     */   public static final String ALIGN_TOP = "top";
/*     */   public static final String ALIGN_BOTTOM = "bottom";
/*     */ 
/*     */   public VerticalLayout()
/*     */   {
/*  24 */     this(4, 4);
/*     */   }
/*     */ 
/*     */   public VerticalLayout(int horizontalPad, int verticalPad)
/*     */   {
/*  29 */     this.horizontalPad = horizontalPad;
/*  30 */     this.verticalPad = verticalPad;
/*  31 */     this.layouts = new HashMap();
/*     */   }
/*     */ 
/*     */   public void setHorizontalPad(int pad)
/*     */   {
/*  36 */     this.horizontalPad = pad;
/*     */   }
/*     */ 
/*     */   public void setVerticalPad(int pad)
/*     */   {
/*  41 */     this.verticalPad = pad;
/*     */   }
/*     */ 
/*     */   public void addLayoutComponent(String name, Component comp)
/*     */   {
/*  46 */     this.layouts.put(comp, name);
/*     */   }
/*     */ 
/*     */   public void layoutContainer(Container c)
/*     */   {
/*  51 */     int topY = this.verticalPad;
/*  52 */     int bottomY = c.getHeight() - this.verticalPad;
/*  53 */     int width = c.getWidth() - this.horizontalPad;
/*     */ 
/*  55 */     Component[] components = c.getComponents();
/*  56 */     for (int i = 0; i < components.length; i++)
/*     */     {
/*  58 */       Dimension sz = new Dimension(components[i].getPreferredSize());
/*  59 */       sz.width = width;
/*  60 */       components[i].setSize(sz);
/*     */ 
/*  62 */       if ((this.layouts.get(components[i]) != null) && (this.layouts.get(components[i]).equals("bottom")))
/*     */       {
/*  65 */         components[i].setLocation(this.horizontalPad / 2, bottomY - sz.height);
/*     */ 
/*  67 */         bottomY -= sz.height + this.verticalPad;
/*     */       }
/*     */       else
/*     */       {
/*  71 */         components[i].setLocation(this.horizontalPad / 2, topY);
/*  72 */         topY += sz.height + this.verticalPad;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public Dimension minimumLayoutSize(Container c)
/*     */   {
/*  79 */     Component[] components = c.getComponents();
/*  80 */     int minWidth = 0;
/*  81 */     int minHeight = this.verticalPad;
/*  82 */     for (int i = 0; i < components.length; i++)
/*     */     {
/*  84 */       Dimension minimumSize = components[i].getMinimumSize();
/*  85 */       minWidth = Math.max(minWidth, minimumSize.width);
/*  86 */       minHeight += minimumSize.height + this.verticalPad;
/*     */     }
/*     */ 
/*  89 */     return new Dimension(minWidth + this.horizontalPad, minHeight);
/*     */   }
/*     */ 
/*     */   public Dimension preferredLayoutSize(Container c)
/*     */   {
/*  94 */     Component[] components = c.getComponents();
/*  95 */     int width = 0;
/*  96 */     int height = this.verticalPad;
/*  97 */     for (int i = 0; i < components.length; i++)
/*     */     {
/*  99 */       Dimension size = components[i].getPreferredSize();
/* 100 */       width = Math.max(width, size.width);
/* 101 */       height += size.height + this.verticalPad;
/*     */     }
/*     */ 
/* 104 */     return new Dimension(width + this.horizontalPad, height);
/*     */   }
/*     */ 
/*     */   public void removeLayoutComponent(Component comp)
/*     */   {
/* 109 */     this.layouts.remove(comp);
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.ui.VerticalLayout
 * JD-Core Version:    0.6.2
 */