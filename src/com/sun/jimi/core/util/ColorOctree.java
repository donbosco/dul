/*     */ package com.sun.jimi.core.util;
/*     */ 
/*     */ import com.sun.jimi.util.ExpandableArray;
/*     */ 
/*     */ public final class ColorOctree
/*     */   implements OctreeCallback
/*     */ {
/*     */   int leaf_level;
/*     */   int maxColors;
/*     */   OctreeNode tree;
/*     */   ExpandableArray[] reduce;
/*     */   int numLeaves;
/*     */   protected boolean alpha;
/*     */   int cacheCount;
/*     */   public ExpandableArray cachedONodes;
/*     */   public static final int MAXCACHE = 25;
/*     */   boolean caching;
/*     */ 
/*     */   public ColorOctree(int paramInt)
/*     */   {
/*  61 */     this.numLeaves = 0;
/*  62 */     this.leaf_level = 8;
/*  63 */     this.maxColors = paramInt;
/*  64 */     this.tree = new OctreeNode(this);
/*     */ 
/*  67 */     this.reduce = new ExpandableArray[8];
/*  68 */     for (int i = 0; i < 8; i++) {
/*  69 */       this.reduce[i] = new ExpandableArray(10, 10);
/*     */     }
/*  71 */     this.cachedONodes = new ExpandableArray(10);
/*     */   }
/*     */ 
/*     */   public void addColor(int paramInt)
/*     */   {
/*  77 */     if ((paramInt & 0xFF000000) == 0)
/*     */     {
/*  79 */       if (!this.alpha) {
/*  80 */         this.alpha = true;
/*  81 */         this.maxColors -= 1;
/*  82 */         if (this.numLeaves > this.maxColors) {
/*  83 */           reduceColors();
/*     */         }
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/*  89 */       this.numLeaves += this.tree.insertColor(paramInt, this.leaf_level);
/*     */ 
/*  92 */       if (this.numLeaves > this.maxColors)
/*  93 */         reduceColors();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addColor(int[] paramArrayOfInt)
/*     */   {
/* 101 */     for (int i = 0; i < paramArrayOfInt.length; i++)
/*     */     {
/* 104 */       if ((paramArrayOfInt[i] & 0xFF000000) == 0)
/*     */       {
/* 106 */         if (!this.alpha) {
/* 107 */           this.alpha = true;
/* 108 */           this.maxColors -= 1;
/* 109 */           if (this.numLeaves > this.maxColors) {
/* 110 */             reduceColors();
/*     */           }
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 116 */         this.numLeaves += this.tree.insertColor(paramArrayOfInt[i], this.leaf_level);
/*     */ 
/* 119 */         if (this.numLeaves > this.maxColors)
/* 120 */           reduceColors();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void cacheONode(OctreeNode paramOctreeNode)
/*     */   {
/* 299 */     OctreeNode.levCounts[paramOctreeNode.level] -= 1;
/*     */ 
/* 301 */     if ((this.caching) && 
/* 302 */       (this.cacheCount < 25))
/*     */     {
/* 304 */       this.cachedONodes.addElement(paramOctreeNode);
/* 305 */       this.cacheCount += 1;
/*     */     }
/*     */   }
/*     */ 
/*     */   public OctreeNode getONode(OctreeCallback paramOctreeCallback, int paramInt)
/*     */   {
/*     */     OctreeNode localOctreeNode;
/* 265 */     if (this.cacheCount > 0)
/*     */     {
/* 267 */       localOctreeNode = (OctreeNode)this.cachedONodes.lastElement();
/* 268 */       this.cachedONodes.removeElementAt(this.cacheCount - 1);
/* 269 */       this.cacheCount -= 1;
/* 270 */       localOctreeNode.setFields(paramOctreeCallback, paramInt);
/*     */     }
/*     */     else
/*     */     {
/* 274 */       localOctreeNode = new OctreeNode(paramOctreeCallback, paramInt);
/*     */     }
/* 276 */     return localOctreeNode;
/*     */   }
/*     */ 
/*     */   int getPalette(byte[] paramArrayOfByte)
/*     */   {
/* 168 */     byte[] arrayOfByte = new byte[1024];
/* 169 */     int i = this.tree.createPalette(arrayOfByte, 0) / 3;
/* 170 */     i = Math.min(i, this.maxColors);
/*     */ 
/* 172 */     int j = 0;
/* 173 */     int k = 0;
/* 174 */     while (j < i * 3) {
/* 175 */       paramArrayOfByte[(k++)] = arrayOfByte[(j++)];
/* 176 */       paramArrayOfByte[(k++)] = arrayOfByte[(j++)];
/* 177 */       paramArrayOfByte[(k++)] = arrayOfByte[(j++)];
/* 178 */       paramArrayOfByte[(k++)] = -1;
/*     */     }
/* 180 */     return this.alpha ? i + 1 : i;
/*     */   }
/*     */ 
/*     */   public OctreeNode getReducible()
/*     */   {
	/* 227 */     Object localObject = null;
	ExpandableArray localExpandableArray;
	/*     */ 
	/* 231 */     for (int i = this.leaf_level - 1; this.reduce[i].size() == 0; i--){
		/* 235 */     localExpandableArray = this.reduce[i];
		/* 236 */     int k = localExpandableArray.size();
		/* 237 */     if (k > 0)
		/*     */     {
		/* 239 */       int m = 0;
		/* 240 */       localObject = (OctreeNode)localExpandableArray.elementAt(0);
		/* 241 */       for (int j = 1; j < k; j++)
		/*     */       {
		/* 243 */         OctreeNode localOctreeNode = (OctreeNode)localExpandableArray.elementAt(j);
		/* 244 */         if (localOctreeNode.count >= ((OctreeNode)localObject).count)
		/*     */         {
		/* 246 */           localObject = localOctreeNode;
		/* 247 */           m = j;
		/*     */         }
		/*     */       }
		/* 250 */       localExpandableArray.removeElementAt(m);
		/*     */     }
	/*     */ 	  }
	/* 253 */     return (OctreeNode) localObject;
	/*     */   }
/*     */ 
/*     */   public boolean hasAlpha()
/*     */   {
/* 131 */     return this.alpha;
/*     */   }
/*     */ 
/*     */   public void markReducible(OctreeNode paramOctreeNode)
/*     */   {
/* 206 */     paramOctreeNode.marked = true;
/* 207 */     this.reduce[paramOctreeNode.level].addElement(paramOctreeNode);
/*     */   }
/*     */ 
/*     */   int quantizeColor(int paramInt)
/*     */   {
/* 193 */     if ((paramInt & 0xFF000000) == 0) {
/* 194 */       return this.maxColors * 3;
/*     */     }
/*     */ 
/* 197 */     return this.tree.quantizeColor(paramInt);
/*     */   }
/*     */ 
/*     */   void reduceColors()
/*     */   {
/* 137 */     this.caching = true;
/*     */ 
/* 139 */     OctreeNode localOctreeNode = getReducible();
/*     */ 
/* 142 */     this.numLeaves -= localOctreeNode.collapseOctree();
/*     */ 
/* 146 */     localOctreeNode.leaf = true;
/* 147 */     this.numLeaves += 1;
/*     */ 
/* 151 */     if (localOctreeNode.level < this.leaf_level - 1)
/* 152 */       this.leaf_level = (localOctreeNode.level + 1);
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.util.ColorOctree
 * JD-Core Version:    0.6.2
 */