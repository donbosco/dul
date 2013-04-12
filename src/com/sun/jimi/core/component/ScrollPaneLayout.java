/*     */ package com.sun.jimi.core.component;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Insets;
import java.awt.LayoutManager;
/*     */ 
/*     */ public class ScrollPaneLayout
/*     */   implements LayoutManager
/*     */ {
/*     */   Component south;
/*     */   Component east;
/*     */   Component corner;
/*     */   Component center;
/*     */ 
/*     */   public void addLayoutComponent(String paramString, Component paramComponent)
/*     */   {
/* 193 */     if (paramString.equals("corner"))
/*     */     {
/* 195 */       this.corner = paramComponent;
/*     */     }
/* 197 */     if (paramString.equals("south"))
/*     */     {
/* 199 */       this.south = paramComponent;
/*     */     }
/* 201 */     if (paramString.equals("east"))
/*     */     {
/* 203 */       this.east = paramComponent;
/*     */     }
/* 205 */     if (paramString.equals("center"))
/*     */     {
/* 207 */       this.center = paramComponent;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void layoutContainer(Container paramContainer)
/*     */   {
/* 109 */     synchronized (paramContainer.getTreeLock())
/*     */     {
/* 111 */       Insets localInsets = paramContainer.insets();
/* 112 */       int i = -localInsets.top;
/* 113 */       int j = paramContainer.size().height - localInsets.bottom;
/* 114 */       int k = -localInsets.left;
/* 115 */       int m = paramContainer.size().width - localInsets.right;
/*     */       Dimension localDimension;
/* 118 */       if ((this.east != null) && (this.east.isVisible()))
/*     */       {
/* 121 */         localDimension = this.east.preferredSize();
/*     */ 
/* 123 */         if (this.south != null) {
/* 124 */           this.east.reshape(m - localDimension.width, i, localDimension.width, j - this.south.preferredSize().height);
/*     */         }
/* 127 */         else if (this.south == null) {
/* 128 */           this.east.reshape(m - localDimension.width, i, localDimension.width, j);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 133 */       if ((this.south != null) && (this.south.isVisible()))
/*     */       {
/* 136 */         localDimension = this.south.preferredSize();
/*     */ 
/* 138 */         if (this.east != null) {
/* 139 */           this.south.reshape(k, j - localDimension.height, m - this.east.preferredSize().width, localDimension.height);
/*     */         }
/* 142 */         else if (this.east == null) {
/* 143 */           this.south.reshape(k, j - localDimension.height, m, localDimension.height);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 148 */       if ((this.east != null) && (this.south != null) && (this.corner != null) && (this.corner.isVisible()) && (this.south.isVisible()) && (this.east.isVisible()))
/*     */       {
/* 150 */         localDimension = this.corner.preferredSize();
/* 151 */         int n = m - this.east.size().width;
/* 152 */         int i1 = j - this.south.size().height;
/* 153 */         int i2 = m - this.south.size().width;
/* 154 */         int i3 = j - this.east.size().height;
/*     */ 
/* 156 */         this.corner.reshape(n, i1, i2, i3);
/*     */ 
/* 159 */         this.corner.setBackground(Color.lightGray);
/* 160 */         this.corner.setForeground(Color.gray);
/*     */       }
/*     */ 
/* 164 */       if ((this.center != null) && (this.center.isVisible()))
/*     */       {
/* 166 */         localDimension = this.center.preferredSize();
/*     */ 
/* 168 */         if ((this.east != null) && (this.east.isVisible()) && (this.south == null))
/*     */         {
/* 172 */           this.center.reshape(k, i, m - this.east.size().width, localDimension.height);
/*     */         }
/* 176 */         else if ((this.south != null) && (this.south.isVisible()) && (this.east == null))
/*     */         {
/* 179 */           this.center.reshape(k, i, localDimension.width, j - this.south.size().height);
/*     */         }
/*     */         else
/*     */         {
/* 185 */           this.center.reshape(k, i, localDimension.width, localDimension.height);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public Dimension minimumLayoutSize(Container paramContainer)
/*     */   {
/*  48 */     return new Dimension(50, 50);
/*     */   }
/*     */ 
/*     */   public Dimension preferredLayoutSize(Container paramContainer)
/*     */   {
	/*  58 */     synchronized (paramContainer.getTreeLock())
	/*     */     {
	/*  60 */       Dimension localDimension2 = new Dimension(0, 0);
	/*     */ 
	/*  62 */       if ((this.center != null) && (this.center.isVisible()))
	/*     */       {
						Dimension localObject3;
	/*  65 */         localObject3 = this.center.preferredSize();
	/*  66 */         localDimension2.width += ((Dimension)localObject3).width;
	/*  67 */         localDimension2.height = Math.max(((Dimension)localObject3).height, localDimension2.height);
	/*     */       }
	/*  70 */       else if ((this.east != null) && (this.east.isVisible()))
	/*     */       {
		Dimension localObject3;
	/*  73 */         localObject3 = this.east.preferredSize();
	/*  74 */         localDimension2.width += ((Dimension)localObject3).width;
	/*  75 */         localDimension2.height = Math.max(((Dimension)localObject3).height, localDimension2.height);
	/*     */       }
	/*  78 */       else if ((this.south != null) && (this.south.isVisible()))
	/*     */       {Dimension localObject3;
	/*  81 */         localObject3 = this.south.preferredSize();
	/*  82 */         localDimension2.width = Math.max(((Dimension)localObject3).width, localDimension2.width);
	/*  83 */         localDimension2.height += ((Dimension)localObject3).height;
	/*     */       }
	/*  86 */       else if ((this.corner != null) && (this.corner.isVisible()))
	/*     */       {Dimension localObject3;
	/*  89 */         localObject3 = this.corner.preferredSize();
	/*  90 */         localDimension2.width = Math.max(((Dimension)localObject3).width, localDimension2.width);
	/*  91 */         localDimension2.height += ((Dimension)localObject3).height;
	/*     */       }
	/*     */       else
	/*     */       {
	/*  95 */         localDimension2.width = 100;
	/*  96 */         localDimension2.height = 100;
	/*     */       }
	/*     */ 
	/*  99 */       Object localObject3 = paramContainer.insets();
	/* 100 */       localDimension2.width += ((Insets)localObject3).left + ((Insets)localObject3).right;
	/* 101 */       localDimension2.height += ((Insets)localObject3).top + ((Insets)localObject3).bottom;
	/*     */ 
	/* 103 */       Dimension localDimension1 = localDimension2; return localDimension1;
	/*     */     }
	/*     */   }
/*     */ 
/*     */   public void removeLayoutComponent(Component paramComponent)
/*     */   {
/*  22 */     if (paramComponent == this.corner) {
/*  23 */       this.corner = null;
/*     */     }
/*  26 */     else if (paramComponent == this.south) {
/*  27 */       this.south = null;
/*     */     }
/*  30 */     else if (paramComponent == this.center) {
/*  31 */       this.center = null;
/*     */     }
/*  34 */     else if (paramComponent == this.east) {
/*  35 */       this.east = null;
/*     */     }
/*     */     else
/*  38 */       paramComponent = null;
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.component.ScrollPaneLayout
 * JD-Core Version:    0.6.2
 */