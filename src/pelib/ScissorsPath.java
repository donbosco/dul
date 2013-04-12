/*     */ package pelib;
/*     */ 
/*     */ import java.awt.Point;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import java.util.Vector;
/*     */ import pelib.scissors.Dijkstra2Scissor;
/*     */ import pelib.scissors.GreedyDistanceScissor;
/*     */ import pelib.scissors.ScissorAlgorithm;
/*     */ 
/*     */ public class ScissorsPath
/*     */ {
/*     */   private PaintExplorer explorer;
/*     */   private Vector seeds;
/*     */   private Vector seedPoints;
/*     */   private Vector edges;
/*     */   private Vector hoverEdges;
/*     */   private Node lastSeed;
/*     */   private ScissorAlgorithm algorithm;
/*     */   private Command beginCommand;
/*     */   private static final double TOLERANCE = 3.0D;
/*     */ 
/*     */   ScissorsPath(PaintExplorer explorer, ScissorAlgorithm algorithm)
/*     */   {
/*  31 */     if (algorithm == null) {
/*  32 */       algorithm = new GreedyDistanceScissor();
/*     */     }
/*  34 */     this.explorer = explorer;
/*  35 */     this.algorithm = algorithm;
/*  36 */     this.seeds = new Vector();
/*  37 */     this.seedPoints = new Vector();
/*  38 */     this.edges = new Vector();
/*  39 */     this.hoverEdges = new Vector();
/*     */ 
/*  41 */     algorithm.reset();
/*     */   }
/*     */ 
/*     */   public void setAlgorithm(ScissorAlgorithm algorithm)
/*     */   {
/*  46 */     this.algorithm = algorithm;
/*     */   }
/*     */ 
/*     */   public ScissorAlgorithm getAlgorithm()
/*     */   {
/*  51 */     return this.algorithm;
/*     */   }
/*     */ 
/*     */   public void seed(Point p)
/*     */   {
/*  57 */     Node n = this.explorer.findNearestNode(p);
/*  58 */     if (n == null) {
/*  59 */       return;
/*     */     }
/*  61 */     if (this.lastSeed != null)
/*     */     {
/*  63 */       Vector newEdges = new Vector();
/*  64 */       this.lastSeed = this.algorithm.addEdges(this.lastSeed, n, newEdges, true);
/*     */ 
/*  66 */       this.explorer.addCommandAndExecute(new ScissorsSeedCommand(this.explorer, this, this.lastSeed, newEdges));
/*     */     }
/*     */     else
/*     */     {
/*  71 */       this.beginCommand = new ScissorsBeginCommand(this.explorer, n);
/*  72 */       this.explorer.addCommandAndExecute(this.beginCommand);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void hover(Point p)
/*     */   {
/*  79 */     Node n = this.explorer.findNearestNode(p);
/*  80 */     if (n == null) {
/*  81 */       return;
/*     */     }
/*  83 */     if (this.lastSeed != null)
/*     */     {
/*  86 */       this.hoverEdges.clear();
/*  87 */       this.algorithm.addEdges(this.lastSeed, n, this.hoverEdges, false);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void cancel()
/*     */   {
/*  95 */     this.explorer.removeCommand(this.beginCommand);
/*  96 */     this.explorer.notifyScissorsEvent(new PaintExplorerScissorsEvent(this.explorer, 1, this));
/*     */   }
/*     */ 
/*     */   public void commit()
/*     */   {
/* 106 */     if (this.seeds.size() < 2)
/*     */     {
/* 108 */       cancel();
/* 109 */       return;
/*     */     }
/*     */ 
/* 114 */     Node seed_first = (Node)this.seeds.get(0);
/*     */ 
/* 116 */     Node seed_last = (Node)this.seeds.get(this.seeds.size() - 1);
/* 117 */     double distance = (seed_first.x - seed_last.x) * (seed_first.y - seed_last.y);
/* 118 */     distance = Math.sqrt(distance);
/* 119 */     if ((!seed_first.equals(seed_last)) && (distance < 3.0D)) {
/* 120 */       seed(new Point(seed_first.x, seed_first.y));
/*     */     }
/* 122 */     this.explorer.progress("PROGRESS_SCISSORS", 40);
/*     */ 
/* 124 */     Memento memento = null;
/* 125 */     ImageFillMask fillMask = this.explorer.getFillMask().clone();
/* 126 */     byte[] mask = fillMask.getBufferByte();
/* 127 */     int maskPitch = fillMask.getPitch();
/* 128 */     Vector cutEdges = new Vector();
/*     */ 
/* 131 */     Vector newEdges = new Vector();
/* 132 */     for (Iterator it = this.edges.iterator(); it.hasNext(); )
/*     */     {
/* 134 */       Edge e = (Edge)it.next();
/* 135 */       if ((e instanceof StraightLineEdge))
/*     */       {
/* 138 */         if (memento == null)
/* 139 */           memento = this.explorer.saveMemento();
/* 140 */         Vector lineEdges = this.explorer.addStraightLineEdge((StraightLineEdge)e);
/*     */ 
/* 142 */         newEdges.addAll(lineEdges);
/*     */       }
/*     */       else {
/* 145 */         newEdges.add(e);
/*     */       }
/*     */     }
/* 148 */     if (newEdges.size() < 2)
/*     */     {
/* 150 */       cancel();
/* 151 */       this.explorer.progress();
/* 152 */       return;
/*     */     }
/*     */ 
/* 155 */     this.explorer.progress(10);
/*     */     ScissorAlgorithm edgeFinder;
/*     */     Vector replacementEdges;
/*     */     Node n1;
/*     */     Node n2;
/*     */     Node n3;
/*     */     Node n4;
/*     */     Node lastNode;
/*     */     Iterator it;
/* 166 */     if (memento != null)
/*     */     {
/* 168 */       this.edges.clear();
/* 169 */       edgeFinder = new Dijkstra2Scissor(this.explorer);
/* 170 */       replacementEdges = new Vector();
/*     */ 
/* 173 */       n1 = ((Edge)newEdges.get(0)).getNodeA();
/* 174 */       n2 = ((Edge)newEdges.get(0)).getNodeB();
/* 175 */       n3 = ((Edge)newEdges.get(1)).getNodeA();
/* 176 */       n4 = ((Edge)newEdges.get(1)).getNodeB();
/*     */ 
/* 178 */       lastNode = null;
/* 179 */       if ((n1 == n3) || (n1 == n4))
/* 180 */         lastNode = n2;
/* 181 */       else if ((n2 == n3) || (n2 == n4)) {
/* 182 */         lastNode = n1;
/*     */       }
/*     */ 
/* 189 */       for (it = newEdges.iterator(); it.hasNext(); )
/*     */       {
/* 191 */         Edge e = (Edge)it.next();
/*     */ 
/* 193 */         if ((e.getNodeA() != lastNode) && (e.getNodeB() != lastNode))
/*     */         {
/* 197 */           n1 = ((Edge)newEdges.get(0)).getNodeA();
/* 198 */           n2 = ((Edge)newEdges.get(0)).getNodeB();
/* 199 */           n3 = ((Edge)newEdges.get(1)).getNodeA();
/* 200 */           n4 = ((Edge)newEdges.get(1)).getNodeB();
/*     */ 
/* 202 */           Node startNode = null;
/* 203 */           if ((n1 == n3) || (n1 == n4))
/* 204 */             startNode = n2;
/* 205 */           else if ((n2 == n3) || (n2 == n4)) {
/* 206 */             startNode = n1;
/*     */           }
/*     */ 
/* 213 */           if (startNode != null)
/*     */           {
/* 216 */             replacementEdges.clear();
/* 217 */             edgeFinder.reset();
/* 218 */             Node endNode = edgeFinder.addEdges(lastNode, startNode, replacementEdges, true);
/*     */ 
/* 221 */             assert (endNode == startNode);
/*     */ 
/* 224 */             this.edges.addAll(replacementEdges);
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 229 */         if ((!e.getNodeA().hasEdge(e)) || (!e.getNodeB().hasEdge(e)))
/*     */         {
/* 231 */           this.explorer.log("Replacing edge " + e);
/*     */ 
/* 233 */           replacementEdges.clear();
/* 234 */           edgeFinder.reset();
/* 235 */           Node endNode = edgeFinder.addEdges(e.getNodeA(), e.getNodeB(), replacementEdges, true);
/*     */ 
/* 238 */           assert (endNode == e.getNodeB());
/* 239 */           Iterator jt = replacementEdges.iterator();
/* 240 */           while (jt.hasNext()) {
/* 241 */             this.explorer.log(" with " + jt.next());
/*     */           }
/*     */ 
/* 244 */           this.edges.addAll(replacementEdges);
/*     */         }
/*     */         else
/*     */         {
/* 249 */           this.explorer.log("Valid " + e);
/* 250 */           this.edges.add(e);
/*     */         }
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 256 */       this.edges = newEdges;
/*     */     }
/*     */ 
/* 259 */     this.explorer.progress(20);
/*     */ 
/* 262 */     if (this.edges.size() >= 3)
/*     */     {
/* 265 */       Edge[] edgeArray = new Edge[this.edges.size()];
/* 266 */       this.edges.copyInto(edgeArray);
/*     */ 
/* 270 */        n1 = ((Edge)this.edges.get(0)).getNodeA();
/* 271 */        n2 = ((Edge)this.edges.get(0)).getNodeB();
/* 272 */        n3 = ((Edge)this.edges.get(1)).getNodeA();
/* 273 */        n4 = ((Edge)this.edges.get(1)).getNodeB();
/*     */ 
/* 275 */        lastNode = null;
/* 276 */       if ((n1 == n3) || (n1 == n4))
/* 277 */         lastNode = n2;
/* 278 */       else if ((n2 == n3) || (n2 == n4)) {
/* 279 */         lastNode = n1;
/*     */       }
/* 281 */      // else if (!$assertionsDisabled) throw new AssertionError();
/* 282 */       lastNode.marked = true;
/* 283 */       this.explorer.log("Begin loop");
/*     */ 
/* 286 */       for (int i = 0; i < edgeArray.length; i++)
/*     */       {
/* 288 */         Edge edge = edgeArray[i];
/* 289 */         Node n = edge.getNodeA();
/* 290 */         if (n == lastNode)
/* 291 */           n = edge.getNodeB();
/* 292 */         else if (edge.getNodeB() != lastNode)
/*     */         {
/* 294 */           this.explorer.log("Discontinuity at " + n);
/*     */         }
/* 296 */         this.explorer.log("Scissor " + n);
/* 297 */         if ((n.marked) && (n != lastNode))
/*     */         {
/* 299 */           this.explorer.log("Intersect loop:");
/*     */ 
/* 304 */           Vector loopEdges = new Vector();
/* 305 */           Area bbox = new Area();
/* 306 */           loopEdges.add(edgeArray[i]);
/* 307 */           bbox.bound(edgeArray[i].getTrapA().getArea());
/* 308 */           this.explorer.log("  " + edgeArray[i]);
/* 309 */           for (int j = i - 1; ; j--)
/*     */           {
/* 311 */             this.explorer.log("  " + edgeArray[j]);
/* 312 */             loopEdges.add(edgeArray[j]);
/* 313 */             bbox.bound(edgeArray[j].getTrapA().getArea());
/* 314 */             bbox.bound(edgeArray[j].getTrapB().getArea());
/* 315 */             Node nA = edgeArray[j].getNodeA();
/* 316 */             Node nB = edgeArray[j].getNodeB();
/* 317 */             if ((nA == n) || (nB == n)) {
/*     */               break;
/*     */             }
/*     */           }
/* 321 */           bbox.left = Math.max(0, bbox.left - 1);
/* 322 */           bbox.top = Math.max(0, bbox.top - 1);
/* 323 */           bbox.right = Math.min(fillMask.getWidth() - 1, bbox.right + 1);
/*     */ 
/* 325 */           bbox.bottom = Math.min(fillMask.getHeight() - 1, bbox.bottom + 1);
/*     */ 
/* 328 */           fillLoop(loopEdges, bbox, fillMask);
/*     */         }
/* 330 */         n.marked = true;
/* 331 */         lastNode = n;
/*     */       }
/*     */ 
/* 334 */       this.explorer.log("End loop");
/*     */     }
/*     */ 
/* 337 */     this.explorer.progress(30);
/*     */ 
/* 342 */     int width = this.explorer.getLabelledImage().getWidth();
/* 343 */     int height = this.explorer.getLabelledImage().getHeight();
/* 344 */     for ( it = this.edges.iterator(); it.hasNext(); )
/*     */     {
/* 346 */       Edge e = (Edge)it.next();
/*     */ 
/* 348 */       for (Iterator gt = e.getGradientVector().iterator(); gt.hasNext(); )
/*     */       {
/* 350 */         Point p = (Point)gt.next();
/* 351 */         if ((p.x < width) && (p.y < height)) {
/* 352 */           mask[(p.y * maskPitch + p.x)] = 1;
/*     */         }
/*     */       }
/* 355 */       cutEdges.add(e);
/* 356 */       e.getNodeA().marked = false;
/* 357 */       e.getNodeB().marked = false;
/*     */     }
/*     */ 
/* 360 */     ScissorsCommand command = new ScissorsCommand(this.explorer, fillMask, cutEdges);
/*     */ 
/* 370 */     if (memento == null)
/*     */     {
/* 373 */       this.explorer.replaceCommandAndExecute(this.beginCommand, command);
/*     */     }
/*     */     else
/*     */     {
/* 378 */       this.explorer.executeCommand(command);
/* 379 */       this.explorer.removeCommand(this.beginCommand);
/* 380 */       this.explorer.addCommand(new ScissorsStateCommand(this.explorer, memento, cutEdges));
/*     */     }
/*     */ 
/* 384 */     this.explorer.capHistory();
/* 385 */     this.explorer.progress();
/*     */   }
/*     */ 
/*     */   private void fillLoop(Vector edges, Area area, ImageFillMask fillMask)
/*     */   {
/* 399 */     ImageLabelled labelled = this.explorer.getLabelledImage();
/* 400 */     int[] labels = labelled.getBufferInteger();
/* 401 */     int labelPitch = labelled.getPitch();
/* 402 */     byte[] mask = fillMask.getBufferByte();
/* 403 */     int maskPitch = fillMask.getPitch();
/*     */ 
/* 406 */     Set crackHash = new HashSet();
/* 407 */     for (Iterator it = edges.iterator(); it.hasNext(); )
/*     */     {
/* 409 */       Edge edge = (Edge)it.next();
/* 410 */       Iterator jt = edge.getCrackSequence().iterator();
/* 411 */       while (jt.hasNext())
/*     */       {
/* 413 */         Crack crack = (Crack)jt.next();
/* 414 */         if (crack.y1 == crack.y2)
/*     */         {
/* 417 */           if (crackHash.contains(crack)) {
/* 418 */             crackHash.remove(crack);
/*     */           }
/*     */           else {
/* 421 */             crackHash.add(crack);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 427 */     Crack c = new Crack(0, 0, 0, 0);
/* 428 */     for (int y = area.top; y <= area.bottom; y++)
/*     */     {
/* 430 */       boolean value = false;
/* 431 */       int lastLabel = -1;
/* 432 */       Trap lastTrap = null;
/* 433 */       for (int x = area.left; x <= area.right; x++)
/*     */       {
/* 435 */         int label = labels[(y * labelPitch + x)];
/* 436 */         if (label != lastLabel)
/*     */         {
/* 440 */           Trap trap = this.explorer.getTrapWithLabel(label);
/* 441 */           if (lastLabel == -1) {
/* 442 */             lastTrap = this.explorer.getTrapWithLabel(label);
/*     */           }
/* 444 */           c.x1 = (x - 1);
/* 445 */           c.y1 = y;
/* 446 */           c.x2 = x;
/* 447 */           c.y2 = y;
/* 448 */           if (crackHash.contains(c)) {
/* 449 */             value = !value;
/*     */           }
/* 451 */           lastTrap = trap;
/* 452 */           lastLabel = label;
/*     */         }
/*     */ 
/* 457 */         if (value)
/*     */         {
/* 459 */           mask[(y * maskPitch + x)] = 0;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public Iterator getSeeds()
/*     */   {
/* 471 */     return this.seedPoints.iterator();
/*     */   }
/*     */ 
/*     */   public Iterator getEdges()
/*     */   {
/* 476 */     return this.edges.iterator();
/*     */   }
/*     */ 
/*     */   public Iterator getHoverEdges()
/*     */   {
/* 481 */     return this.hoverEdges.iterator();
/*     */   }
/*     */ 
/*     */   public class ScissorsBeginCommand extends Command
/*     */   {
/*     */     private Node seed;
/*     */ 
/*     */     public ScissorsBeginCommand(PaintExplorer explorer, Node seed)
/*     */     {
/* 547 */       super(explorer);
/* 548 */       this.seed = seed;
/*     */     }
/*     */ 
/*     */     public void execute(Area area)
/*     */     {
/* 553 */       ScissorsPath.this.seeds.add(this.seed);
/* 554 */       ScissorsPath.this.seedPoints.add(new Point(this.seed.x, this.seed.y));
/* 555 */       ScissorsPath.this.lastSeed = this.seed;
/*     */     }
/*     */ 
/*     */     public void undo(Area area)
/*     */     {
/* 561 */       ScissorsPath.this.cancel();
/*     */     }
/*     */   }
/*     */ 
/*     */   public class ScissorsSeedCommand extends Command
/*     */   {
/*     */     private Node seed;
/*     */     private Vector newEdges;
/*     */     private int edgeCount;
/*     */     private ScissorsPath path;
/*     */ 
/*     */     public ScissorsSeedCommand(PaintExplorer explorer, ScissorsPath path, Node seed, Vector newEdges)
/*     */     {
/* 496 */       super(explorer);
/* 497 */       this.seed = seed;
/* 498 */       this.newEdges = newEdges;
/* 499 */       this.path = path;
/*     */     }
/*     */ 
/*     */     public void execute(Area area)
/*     */     {
/* 504 */       this.edgeCount = ScissorsPath.this.edges.size();
/*     */ 
/* 507 */       for (Iterator it = this.newEdges.iterator(); it.hasNext(); )
/*     */       {
/* 509 */         Edge e = (Edge)it.next();
/*     */       }
/*     */       Edge e;
/* 511 */       ScissorsPath.this.edges.addAll(this.newEdges);
/*     */ 
/* 513 */       ScissorsPath.this.seeds.add(this.seed);
/* 514 */       ScissorsPath.this.seedPoints.add(new Point(this.seed.x, this.seed.y));
/* 515 */       ScissorsPath.this.lastSeed = this.seed;
/*     */ 
/* 517 */       this.explorer.notifyScissorsEvent(new PaintExplorerScissorsEvent(this.explorer, 2, this.path));
/*     */     }
/*     */ 
/*     */     public void undo(Area area)
/*     */     {
/* 525 */       ScissorsPath.this.seeds.remove(ScissorsPath.this.seeds.size() - 1);
/* 526 */       ScissorsPath.this.seedPoints.remove(ScissorsPath.this.seedPoints.size() - 1);
/* 527 */       ScissorsPath.this.lastSeed = ((Node)ScissorsPath.this.seeds.get(ScissorsPath.this.seeds.size() - 1));
/*     */ 
/* 530 */       for (int i = ScissorsPath.this.edges.size() - 1; i >= this.edgeCount; i--) {
/* 531 */         ScissorsPath.this.edges.remove(i);
/*     */       }
/* 533 */       this.explorer.notifyScissorsEvent(new PaintExplorerScissorsEvent(this.explorer, 2, this.path));
/*     */     }
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.ScissorsPath
 * JD-Core Version:    0.6.2
 */