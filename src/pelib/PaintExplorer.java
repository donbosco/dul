/*      */ package pelib;
/*      */ 
/*      */ import com.sun.jimi.core.Jimi;
/*      */ import com.sun.jimi.core.JimiException;
/*      */ import com.sun.jimi.core.JimiWriter;
/*      */ import com.sun.jimi.core.options.JPGOptions;
/*      */ import java.awt.Image;
/*      */ import java.awt.Point;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.image.ImageProducer;
/*      */ import java.io.BufferedInputStream;
/*      */ import java.io.BufferedOutputStream;
/*      */ import java.io.ByteArrayInputStream;
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.DataInput;
/*      */ import java.io.DataInputStream;
/*      */ import java.io.DataOutput;
/*      */ import java.io.DataOutputStream;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.FileOutputStream;
/*      */ import java.io.FileWriter;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.io.PrintStream;
/*      */ import java.io.PrintWriter;
/*      */ import java.net.URL;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Date;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Properties;
/*      */ import java.util.Set;
/*      */ import java.util.Stack;
/*      */ import java.util.Vector;
/*      */ import java.util.zip.ZipEntry;
/*      */ import java.util.zip.ZipInputStream;
/*      */ import java.util.zip.ZipOutputStream;
/*      */ import pelib.filters.BilinearFilter;
/*      */ import pelib.filters.GaussianFilter;
/*      */ import pelib.filters.MedianFilter;
/*      */ import pelib.filters.ResizingConsumer;
/*      */ import pelib.filters.SobelFilter;
/*      */ import pelib.filters.TriangulationFilter;
/*      */ import pelib.lighting.LightingAlgorithm;
/*      */ import pelib.scissors.Dijkstra2Scissor;
/*      */ import pelib.scissors.ScissorAlgorithm;
/*      */ 
/*      */ public class PaintExplorer
/*      */ {
/*      */   private String byteSize;
/*      */   private boolean isReady;
/*      */   private ImageColour original;
/*      */   private ImageColour painted;
/*      */   private ImageDecorate decorated;
/*      */   private byte[] compressedOriginal;
/*      */   private ImageColour median;
/*      */   private ImageColour gaussian;
/*      */   private ImageFloat gradient;
/*      */   private ImageByte direction;
/*      */   private ImageLabelled labelled;
/*      */   private ImageFillMask fillMask;
/*      */   private ImageFillMask fillMaskDecorate;
/*      */   private Trap[] traps;
/*      */   private Vector nodes;
/*      */   private Vector edges;
/*      */   private Vector masks;
/*      */   private RecolourData[] recolourData;
/*      */   private int[] recolourFilteredWeight;
/*      */   private CommandHistory commandHistory;
/*      */   private BeginPaintCommand beginPaintCommand;
/*      */   private BeginEraseCommand beginEraseCommand;
/*      */   private Area dirty;
/*      */   private Vector listeners;
/*      */   private int cachedX;
/*      */   private int cachedY;
/*      */   private int paintArea;
/*      */   private boolean floodFillPaint;
/*      */   private boolean enableMedianFilter;
/*      */   private boolean createCrackSequence;
/*      */   private int nominalWidth;
/*      */   private int nominalHeight;
/*      */   private boolean enableSelfCheck;
/*      */   private boolean enableLog;
/*      */   private String logFilename;
/*      */   private int regionHierarchyLevels;
/*      */   private boolean allowOverpaint;
/*      */   private boolean enableHistory;
/*      */   private boolean enableDecorate;
/*      */   private boolean enableMementoHistory;
/*      */   private int maximumHistory;
/*      */   private boolean enableRecolourMedianFilter;
/*      */   private int jpegEncodeQuality;
/*      */   private boolean keepIntermediateImages;
/*      */   private PaintManager paintManager;
/*      */   private BlurManager blurManager;
/*      */   private boolean autoRemoveMask;
/*      */   private LightingAlgorithm lightingAlgorithm;
/*      */   private ScissorAlgorithm scissorsAlgorithm;
/*      */   private String filename;
/*      */   public static final float FILL_MASK_PERCENTILE = 50.0F;
/*      */   public static final int PAINT_WINDOW_SIZE = 5;
/*      */   public static final float REGION_MEAN_TOLERANCE = 10.24F;
/*      */   public static final int INTERMEDIATE_MEDIAN = 1;
/*      */   public static final int INTERMEDIATE_GAUSSIAN = 2;
/*      */   public static final int INTERMEDIATE_GRADIENT = 3;
/*      */   public static final int INTERMEDIATE_DIRECTION = 4;
/*      */   public static final int INTERMEDIATE_LABELLED = 5;
/*      */   public static final int INTERMEDIATE_NODES = 6;
/*      */   public static final int INTERMEDIATE_EDGES = 7;
/*      */   public static final int INTERMEDIATE_GRADIENT_VECTORS = 8;
/*      */   public static final int INTERMEDIATE_BOUNDING_BOXES = 9;
/*      */   public static final int INTERMEDIATE_FILL_MASK = 10;
/*      */   public static final int INTERMEDIATE_STATUS = 11;
/*      */   public static final int INTERMEDIATE_RECOLOUR_WEIGHT = 12;
/*      */   public static final int INTERMEDIATE_RECOLOUR_FILTERED_WEIGHT = 13;
/*      */   private static final int DIRECTION_UP = 1;
/*      */   private static final int DIRECTION_RIGHT = 2;
/*      */   private static final int DIRECTION_DOWN = 3;
/*      */   private static final int DIRECTION_LEFT = 4;
/*      */   public static final byte DECORATE_UNTOUCH = 0;
/*      */   public static final byte DECORATE_REMOVE = 1;
/*      */   public static final byte DECORATE_PAINT = 2;
/*      */   public static final int PAINT_NORMAL_AREA = 0;
/*      */   public static final int PAINT_ERASE_AREA = 1;
/*      */   private String progressDescription;
/*      */   private int progressRange;
/* 1686 */   private static int SERIALISE_MAGIC_NUMBER = 1246774577;
/*      */ 
/* 1690 */   private static int SERIALISE_SENTINAL = -559038737;
/*      */ 
/* 1693 */   private static String SERIALISE_ZIP_ENTRY_NAME = "jpe1";
/*      */   private static PrintWriter logOut;
/*      */ 
/*      */   public PaintExplorer()
/*      */   {
/*  150 */     this(null);
/*      */   }
/*      */ 
/*      */   public PaintExplorer(Properties properties)
/*      */   {
/*  158 */     if (properties == null) {
/*  159 */       properties = new Properties();
/*      */     }
/*      */ 
/*  162 */     this.isReady = false;
/*      */ 
/*  164 */     this.enableMedianFilter = parseBoolean(properties.getProperty("enableMedianFilter", "true"));
/*      */ 
/*  166 */     this.floodFillPaint = parseBoolean(properties.getProperty("floodFillPaint", "true"));
/*      */ 
/*  168 */     this.createCrackSequence = parseBoolean(properties.getProperty("createCrackSequence", "true"));
/*      */ 
/*  170 */     this.nominalWidth = parseInteger(properties.getProperty("nominalWidth", "-1"));
/*      */ 
/*  172 */     this.nominalHeight = parseInteger(properties.getProperty("nominalHeight", "-1"));
/*      */ 
/*  174 */     this.enableSelfCheck = parseBoolean(properties.getProperty("enableSelfCheck", "true"));
/*      */ 
/*  178 */     this.enableLog = false;
/*  179 */     this.regionHierarchyLevels = parseInteger(properties.getProperty("regionHierarchyLevels", "2"));
/*      */ 
/*  181 */     this.logFilename = properties.getProperty("logFilename", "log");
/*  182 */     this.allowOverpaint = parseBoolean(properties.getProperty("allowOverpaint", "false"));
/*      */ 
/*  184 */     this.enableHistory = parseBoolean(properties.getProperty("enableHistory", "true"));
/*      */ 
/*  186 */     this.enableDecorate = parseBoolean(properties.getProperty("enableDecorate", "true"));
/*      */ 
/*  188 */     this.enableMementoHistory = parseBoolean(properties.getProperty("enableMementoHistory", "true"));
/*      */ 
/*  190 */     this.maximumHistory = parseInteger(properties.getProperty("maximumHistory", "-1"));
/*      */ 
/*  192 */     this.enableRecolourMedianFilter = parseBoolean(properties.getProperty("enableRecolourMedianFilter", "true"));
/*      */ 
/*  194 */     this.jpegEncodeQuality = parseInteger(properties.getProperty("jpegEncodeQuality", "90"));
/*      */ 
/*  196 */     this.keepIntermediateImages = parseBoolean(properties.getProperty("keepIntermediateImages", "false"));
/*      */     Iterator it;
/*  199 */     if (this.enableLog) {
/*  200 */       log("PaintExplorer started " + new Date());
/*  201 */       for (it = properties.entrySet().iterator(); it.hasNext(); ) {
/*  202 */         Map.Entry entry = (Map.Entry)it.next();
/*  203 */         log("  " + entry.getKey() + " = " + entry.getValue());
/*      */       }
/*      */     }
/*      */ 
/*  207 */     this.paintManager = new ManualPaintManager(this);
/*  208 */     this.masks = new Vector();
/*  209 */     this.dirty = new Area();
/*  210 */     this.commandHistory = new CommandHistory(this.maximumHistory);
/*  211 */     this.blurManager = new BlurManager(this);
/*  212 */     this.beginPaintCommand = new BeginPaintCommand();
/*  213 */     this.listeners = new Vector();
/*      */ 
/*  215 */     this.nodes = new Vector();
/*  216 */     this.edges = new Vector();
/*      */ 
/*  220 */     this.scissorsAlgorithm = new Dijkstra2Scissor(this);
/*      */   }
/*      */ 
/*      */   private boolean parseBoolean(String value)
/*      */   {
/*  227 */     return (value.equalsIgnoreCase("true")) || (value.equalsIgnoreCase("yes")) || (value.equalsIgnoreCase("on"));
/*      */   }
/*      */ 
/*      */   private int parseInteger(String value)
/*      */   {
/*      */     try
/*      */     {
/*  234 */       return Integer.parseInt(value); } catch (NumberFormatException e) {
/*      */     }
/*  236 */     return 0;
/*      */   }
/*      */ 
/*      */   public void setEnableFloodFill(boolean floodFill)
/*      */   {
/*  248 */     this.floodFillPaint = floodFill;
/*      */   }
/*      */ 
/*      */   public boolean isReady()
/*      */   {
/*  258 */     return this.isReady;
/*      */   }
/*      */   public CommandHistory getCommandHistory() {
/*  261 */     return this.commandHistory;
/*      */   }
/*      */ 
/*      */   public void addListener(PaintExplorerListener listener)
/*      */   {
/*  268 */     this.listeners.add(listener);
/*      */   }
/*      */ 
/*      */   public void setPaintManager(PaintManager paintManager)
/*      */   {
/*  278 */     log("PaintExplorer.setPaintManager(" + paintManager + ")");
/*  279 */     this.paintManager = paintManager;
/*      */   }
/*      */ 
/*      */   public PaintManager getPaintManager()
/*      */   {
/*  287 */     return this.paintManager;
/*      */   }
/*      */ 
/*      */   public void setLightingAlgorithm(LightingAlgorithm algorithm)
/*      */   {
/*  297 */     log("PaintExplorer.setLightingAlgorithm(" + algorithm + ")");
/*  298 */     this.lightingAlgorithm = algorithm;
/*  299 */     for (Iterator it = this.masks.iterator(); it.hasNext(); ) {
/*  300 */       Mask mask = (Mask)it.next();
/*  301 */       mask.setLightingAlgorithm(algorithm);
/*  302 */       this.dirty.bound(mask.getArea());
/*      */     }
/*      */   }
/*      */ 
/*      */   public LightingAlgorithm getLightingAlgorithm()
/*      */   {
/*  310 */     return this.lightingAlgorithm;
/*      */   }
/*      */ 
/*      */   public void undo()
/*      */   {
/*  318 */     log("PaintExplorer.undo()");
/*  319 */     this.commandHistory.undo(this.dirty);
/*  320 */     notifyHistoryEvent();
/*      */   }
/*      */ 
/*      */   public void redo()
/*      */   {
/*  328 */     log("PaintExplorer.redo()");
/*  329 */     this.commandHistory.redo(this.dirty);
/*  330 */     notifyHistoryEvent();
/*      */   }
/*      */ 
/*      */   public boolean canUndo()
/*      */   {
/*  340 */     return this.commandHistory.canUndo();
/*      */   }
/*      */ 
/*      */   public boolean canRedo()
/*      */   {
/*  350 */     return this.commandHistory.canRedo();
/*      */   }
/*      */ 
/*      */   public void setScissorsAlgorithm(ScissorAlgorithm algorithm) {
/*  354 */     this.scissorsAlgorithm = algorithm;
/*      */   }
/*      */ 
/*      */   public ScissorsPath createScissorsPath()
/*      */   {
/*  363 */     log("PaintExplorer.createScissorsPath(" + this.scissorsAlgorithm + ")");
/*  364 */     return new ScissorsPath(this, this.scissorsAlgorithm);
/*      */   }
/*      */ 
/*      */   public void beginPaint()
/*      */   {
/*  385 */     log("PaintExplorer.beginPaint()");
/*  386 */     addCommand(this.beginPaintCommand);
/*      */   }
/*      */ 
/*      */   public void endPaint()
/*      */   {
/*  396 */     log("PaintExplorer.endPaint()");
/*      */ 
/*  399 */     Vector maskCmds = new Vector();
Mask mask;
/*  400 */     for (Iterator it = this.masks.iterator(); it.hasNext(); )
/*  401 */       mask = (Mask)it.next();
/*  407 */     for (Iterator it = maskCmds.iterator(); it.hasNext(); ) {
/*  408 */       Command cmd = (Command)it.next();
/*  409 */       addCommandAndExecute(cmd);
/*      */     }
/*      */ 
/*  413 */     Stack cmds = this.commandHistory.removeAndReturn(this.beginPaintCommand);
/*  414 */     BatchCommand batchCmd = new BatchCommand(this);
/*  415 */     addCommand(batchCmd);
/*  416 */     FloodFillPaintCommand paintBatchCmd = null;
/*      */     Iterator it;
/*  418 */     if (this.enableLog) {
/*  419 */       for (it = cmds.iterator(); it.hasNext(); ) {
/*  420 */         log(" .. " + it.next());
/*      */       }
/*      */     }
/*      */ 
/*  424 */     while (cmds.size() > 0) {
/*  425 */       Command cmd = (Command)cmds.pop();
/*      */ 
/*  427 */       if ((cmd instanceof PaintCommand)) {
/*  428 */         PaintCommand paintCmd = (PaintCommand)cmd;
/*  429 */         mask = paintCmd.getMask();
/*  430 */         if ((paintBatchCmd == null) || (mask != paintBatchCmd.getMask()))
/*      */         {
/*  432 */           paintBatchCmd = new FloodFillPaintCommand(this, mask);
/*  433 */           if (this.paintArea == 1)
/*  434 */             paintBatchCmd.setDecorated(true);
/*  435 */           batchCmd.add(paintBatchCmd);
/*      */         }
/*      */ 
/*  438 */         paintBatchCmd.addPaintedRegion(paintCmd.getLabelIndex(), paintCmd.getOldMask());
/*      */       }
/*  440 */       else if ((cmd instanceof FloodFillPaintCommand)) {
/*  441 */         FloodFillPaintCommand fillCmd = (FloodFillPaintCommand)cmd;
/*  442 */         mask = fillCmd.getMask();
/*  443 */         if ((paintBatchCmd == null) || (mask != paintBatchCmd.getMask()))
/*      */         {
/*  445 */           paintBatchCmd = new FloodFillPaintCommand(this, mask);
/*  446 */           if (this.paintArea == 1)
/*  447 */             paintBatchCmd.setDecorated(true);
/*  448 */           batchCmd.add(paintBatchCmd);
/*      */         }
/*  450 */         paintBatchCmd.addFloodFillRegions(fillCmd);
/*      */       }
/*      */       else {
/*  453 */         batchCmd.add(cmd);
/*  454 */         paintBatchCmd = null;
/*      */       }
/*      */     }
/*      */ 
/*  458 */     capHistory();
/*      */   }
/*      */ 
/*      */   private void paintDecorate(int x, int y, boolean unpaint)
/*      */   {
/*  463 */     Mask currentMask = null;
/*  464 */     int pitch = this.labelled.getPitch();
/*      */ 
/*  467 */     SuperRegion region = getTrap(x, y).getHighestSuperRegion();
/*      */ 
/*  469 */     if (this.decorated.data[(y * pitch + x)] == 2) {
/*  470 */       currentMask = region.getDecorateMask();
/*      */     }
/*      */ 
/*  473 */     Mask appliedMask = null;
/*  474 */     if ((unpaint) && (!this.paintManager.canUnpaint(currentMask)))
/*  475 */       return;
/*  476 */     if (!unpaint) {
/*  477 */       if (hasMaskWithSameColour(this.paintManager.getSelectedColour())) {
/*  478 */         appliedMask = this.paintManager.getPaintedMask(null);
/*      */ 
/*  480 */         if ((currentMask != null) && (appliedMask != null) && (!appliedMask.equals(currentMask))) {
/*  481 */           return;
/*      */         }
/*  483 */         if (appliedMask == null) {
/*  484 */           this.cachedX = x;
/*  485 */           this.cachedY = y;
/*      */         }
/*      */       }
/*      */       else {
/*  489 */         appliedMask = this.paintManager.createMask();
/*  490 */         this.cachedX = x;
/*  491 */         this.cachedY = y;
/*  492 */         paint(appliedMask);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  501 */     if (this.decorated.data[(y * pitch + x)] == 2) {
/*  502 */       currentMask = region.getDecorateMask();
/*      */     }
/*      */ 
/*  506 */     if (currentMask == appliedMask) {
/*  507 */       return;
/*      */     }
/*      */ 
/*  511 */     Area area = new Area();
/*      */ 
/*  514 */     if (this.floodFillPaint) {
/*  515 */       log(" .. floodFill");
/*  516 */       FloodFillPaintCommand cmd = new FloodFillPaintCommand(this, appliedMask);
/*      */ 
/*  519 */       cmd.setDecorated(true);
/*      */ 
/*  521 */       ImageFillMask select = this.fillMaskDecorate.fillWithLimit(this.decorated.data, x, y, area);
/*      */ 
/*  523 */       byte[] selectData = select.getBufferByte();
/*  524 */       int pixels = 0;
/*      */ 
/*  528 */       for (int iRow = area.top; iRow <= area.bottom; iRow++) {
/*  529 */         for (int iCol = area.left; iCol <= area.right; iCol++)
/*      */         {
/*  531 */           int idx = iRow * pitch + iCol;
/*      */ 
/*  533 */           if (selectData[idx] == 1)
/*      */           {
/*  535 */             Trap trap = getTrap(idx);
/*  536 */             SuperRegion fillRegion = trap.getHighestSuperRegion();
/*  537 */             this.dirty.bound(fillRegion.area);
/*  538 */             cmd.addRegionDecorate(idx, fillRegion);
/*  539 */             if (appliedMask != null)
/*  540 */               appliedMask.addRegionDecorate(fillRegion);
/*      */           }
/*      */         }
/*      */       }
/*  544 */       addCommandAndExecute(cmd);
/*      */     }
/*  546 */     else if ((this.allowOverpaint) || (currentMask == null) || (appliedMask == null))
/*      */     {
/*  549 */       addCommandAndExecute(new PaintCommand(this, appliedMask, y * pitch + x));
/*      */     }
/*      */   }
/*      */ 
/*      */   public void paint(int x, int y, boolean unpaint)
/*      */   {
/*  568 */     Mask currentMask = null;
/*  569 */     int pitch = this.labelled.getPitch();
/*      */ 
/*  572 */     if ((this.decorated.data[(y * pitch + x)] == 1) || (this.decorated.data[(y * pitch + x)] == 2))
/*      */     {
/*  575 */       this.paintArea = 1;
/*  576 */       paintDecorate(x, y, unpaint);
/*  577 */       return;
/*      */     }
/*      */ 
/*  580 */     this.paintArea = 0;
/*      */ 
/*  583 */     AbstractRegion region = getTrap(x, y).getHighestSuperRegion();
/*  584 */     currentMask = region.getMask();
/*      */ 
/*  587 */     Mask appliedMask = null;
/*  588 */     if ((unpaint) && (!this.paintManager.canUnpaint(currentMask)))
/*  589 */       return;
/*  590 */     if (!unpaint) {
/*  591 */       if (hasMaskWithSameColour(this.paintManager.getSelectedColour())) {
/*  592 */         appliedMask = this.paintManager.getPaintedMask(currentMask);
/*  593 */         if (appliedMask == null) {
/*  594 */           this.cachedX = x;
/*  595 */           this.cachedY = y;
/*      */         }
/*      */       }
/*      */       else {
/*  599 */         appliedMask = this.paintManager.createMask();
/*  600 */         this.cachedX = x;
/*  601 */         this.cachedY = y;
/*  602 */         paint(appliedMask);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  610 */     currentMask = region.getMask();
/*      */ 
/*  613 */     if (currentMask == appliedMask) {
/*  614 */       return;
/*      */     }
/*      */ 
/*  618 */     if (this.floodFillPaint) {
/*  619 */       log(" .. floodFill");
/*  620 */       FloodFillPaintCommand cmd = new FloodFillPaintCommand(this, appliedMask);
/*      */ 
/*  622 */       ImageFillMask select = this.fillMask.fill(x, y);
/*  623 */       byte[] selectData = select.getBufferByte();
/*  624 */       int pixels = 0;
/*  625 */       for (int i = 0; i < selectData.length; i++) {
/*  626 */         if (selectData[i] == 1) {
/*  627 */           Trap trap = getTrap(i);
/*  628 */           SuperRegion fillRegion = trap.getHighestSuperRegion();
/*  629 */           if ((this.allowOverpaint) || (currentMask == null) || (appliedMask == null))
/*      */           {
/*  632 */             cmd.addRegion(i, fillRegion);
/*      */           }
/*      */         }
/*      */       }
/*  636 */       addCommandAndExecute(cmd);
/*      */     }
/*  639 */     else if ((this.allowOverpaint) || (currentMask == null) || (appliedMask == null))
/*      */     {
/*  642 */       addCommandAndExecute(new PaintCommand(this, appliedMask, y * pitch + x));
/*      */     }
/*      */   }
/*      */ 
/*      */   public void paint(Mask appliedMask)
/*      */   {
/*  649 */     int pitch = this.labelled.getPitch();
/*      */ 
/*  652 */     AbstractRegion region = getTrap(this.cachedX, this.cachedY).getHighestSuperRegion();
/*  653 */     Mask currentMask = region.getMask();
/*      */ 
/*  656 */     if (this.floodFillPaint) {
/*  657 */       log(" .. floodFill");
/*  658 */       FloodFillPaintCommand cmd = new FloodFillPaintCommand(this, appliedMask);
/*      */ 
/*  660 */       ImageFillMask select = this.fillMask.fill(this.cachedX, this.cachedY);
/*      */ 
/*  662 */       byte[] selectData = select.getBufferByte();
/*  663 */       int pixels = 0;
/*  664 */       for (int i = 0; i < selectData.length; i++) {
/*  665 */         if (selectData[i] == 1) {
/*  666 */           Trap trap = getTrap(i);
/*  667 */           SuperRegion fillRegion = trap.getHighestSuperRegion();
/*  668 */           if ((this.allowOverpaint) || (currentMask == null) || (appliedMask == null))
/*      */           {
/*  671 */             cmd.addRegion(i, fillRegion);
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/*  676 */       addCommandAndExecute(cmd);
/*      */     }
/*  678 */     else if ((this.allowOverpaint) || (currentMask == null) || (appliedMask == null))
/*      */     {
/*  681 */       addCommandAndExecute(new PaintCommand(this, appliedMask, this.cachedY * pitch + this.cachedX));
/*      */     }
/*      */   }
/*      */ 
/*      */   public void beginErase()
/*      */   {
/*  688 */     addCommand(this.beginEraseCommand);
/*      */   }
/*      */ 
/*      */   public void endErase() {
/*  692 */     Stack cmds = this.commandHistory.removeAndReturn(this.beginEraseCommand);
/*  693 */     BatchCommand batchCommand = new BatchCommand(this);
/*  694 */     addCommand(batchCommand);
/*      */     Iterator it;
/*  696 */     if (this.enableLog) {
/*  697 */       for (it = cmds.iterator(); it.hasNext(); ) {
/*  698 */         log(" .. " + it.next());
/*      */       }
/*      */     }
/*  701 */     while (cmds.size() > 0) {
/*  702 */       Command cmd = (Command)cmds.pop();
/*  703 */       if ((cmd instanceof EraseCommand)) {
/*  704 */         EraseCommand eraseCommand = (EraseCommand)cmd;
/*  705 */         batchCommand.add(eraseCommand);
/*      */       }
/*      */     }
/*      */ 
/*  709 */     capHistory();
/*      */   }
/*      */ 
/*      */   public void erase(int x, int y, int r, boolean unerase) {
/*  713 */     int width = this.decorated.width;
/*  714 */     int height = this.decorated.height;
/*      */ 
/*  717 */     if ((x < 0) || (x >= width) || (y < 0) || (y >= height)) {
/*  718 */       return;
/*      */     }
/*  720 */     int min_x = x - r < 0 ? 0 : x - r;
/*  721 */     int min_y = y - r < 0 ? 0 : y - r;
/*  722 */     int max_x = x + r >= width ? width - 1 : x + r;
/*  723 */     int max_y = y + r >= height ? height - 1 : y + r;
/*      */ 
/*  725 */     Mask selectedMask = this.paintManager.getPaintedMask(null);
/*  726 */     Trap trap = getTrap(x + y * width);
/*  727 */     SuperRegion superRegion = trap.getHighestSuperRegion();
/*      */ 
/*  729 */     if (superRegion.getMask() == null) {
/*  730 */       return;
/*      */     }
/*  732 */     if (this.decorated.data[(y * width + x)] == 0)
/*      */     {
/*  734 */       if ((selectedMask == null) || (selectedMask.equals(superRegion.getMask())));
/*      */     }
/*  739 */     else if ((selectedMask != null) && (!selectedMask.equals(superRegion.getDecorateMask()))) {
/*  740 */       return;
/*      */     }
/*      */ 
/*  743 */     EraseCommand cmd = new EraseCommand(this);
/*      */ 
/*  745 */     cmd.addAffectedArea(new Area(min_x, min_y, max_x, max_y), x, y, r, unerase);
/*  746 */     addCommandAndExecute(cmd);
/*      */   }
/*      */ 
/*      */   public void setMaskColour(Mask mask, int colour)
/*      */   {
/*  757 */     log("PaintExplorer.setMaskColour(" + mask + ", " + colour + ")");
/*  758 */     assert (this.masks.contains(mask));
/*  759 */     addCommandAndExecute(new MaskColourCommand(this, mask, colour));
/*      */   }
/*      */ 
/*      */   public void setMaskName(Mask mask, String name)
/*      */   {
/*  764 */     log("PaintExplorer.setMaskName(" + mask + ", " + name + ")");
/*  765 */     assert (this.masks.contains(mask));
/*  766 */     addCommandAndExecute(new MaskNameCommand(this, mask, name));
/*      */   }
/*      */ 
/*      */   public void setMaskItemId(Mask mask, int itemId) {
/*  770 */     log("PaintExplorer.setItemId(" + mask + ", " + itemId + ")");
/*  771 */     assert (this.masks.contains(mask));
/*  772 */     addCommandAndExecute(new MaskItemIDCommand(this, mask, itemId));
/*      */   }
/*      */ 
/*      */   public void setPositionId(Mask mask, int positionID) {
/*  776 */     log("PaintExplorer.setPositionId(" + mask + ", " + positionID + ")");
/*  777 */     assert (this.masks.contains(mask));
/*  778 */     addCommandAndExecute(new MaskPositionIdCommand(this, mask, positionID));
/*      */   }
/*      */ 
/*      */   public void changeMask(Mask mask, int colour, String name, int itemId, int positionID) {
/*  782 */     log("PaintExplorer.changeMask(" + mask + ", " + colour + ", " + name + ", " + itemId + ")");
/*  783 */     assert (this.masks.contains(mask));
/*  784 */     BatchCommand changeMaskCommand = new BatchCommand(this);
/*  785 */     changeMaskCommand.add(new MaskColourCommand(this, mask, colour));
/*  786 */     changeMaskCommand.add(new MaskNameCommand(this, mask, name));
/*  787 */     changeMaskCommand.add(new MaskItemIDCommand(this, mask, itemId));
/*  788 */     if (positionID == -1)
/*  789 */       changeMaskCommand.add(new MaskPositionIdCommand(this, mask, mask.getPositionID()));
/*      */     else
/*  791 */       changeMaskCommand.add(new MaskPositionIdCommand(this, mask, positionID));
/*  792 */     addCommandAndExecute(changeMaskCommand);
/*      */   }
/*      */ 
/*      */   public void setMaskBlur(Mask mask, int level)
/*      */   {
/*  803 */     log("PaintExplorer.setMaskBlur(" + mask + ", " + level + ")");
/*  804 */     assert (this.masks.contains(mask));
/*  805 */     addCommandAndExecute(new MaskBlurCommand(this, mask, level));
/*      */   }
/*      */ 
/*      */   public Mask createMask(int colour, Object userData, int itemId)
/*      */   {
/*  821 */     log("PaintExplorer.createMask(" + colour + ", " + userData + ")");
/*      */ 
/*  826 */     for (Iterator it = this.masks.iterator(); it.hasNext(); ) {
/*  827 */       Mask mask = (Mask)it.next();
/*  828 */       if ((mask.getColour() == colour) && (mask.getUserData() == userData)) {
/*  829 */         return mask;
/*      */       }
/*      */     }
/*      */ 
/*  833 */     Mask mask = new Mask();
/*  834 */     mask.setColour(colour);
/*  835 */     mask.setUserData(userData);
/*  836 */     mask.setItemId(itemId);
/*  837 */     addCommandAndExecute(new AddMaskCommand(this, mask));
/*      */ 
/*  840 */     return mask;
/*      */   }
/*      */ 
/*      */   public Mask createMask(int colour, Object userData, int itemId, int positionID) {
/*  844 */     log("PaintExplorer.createMaskWithPosition(" + colour + ", " + userData + ")");
/*      */ 
/*  849 */     for (Iterator it = this.masks.iterator(); it.hasNext(); ) {
/*  850 */       Mask mask = (Mask)it.next();
/*  851 */       if ((mask.getColour() == colour) && (mask.getUserData() == userData)) {
/*  852 */         return mask;
/*      */       }
/*      */     }
/*      */ 
/*  856 */     Mask mask = new Mask();
/*  857 */     mask.setColour(colour);
/*  858 */     mask.setUserData(userData);
/*  859 */     mask.setItemId(itemId);
/*  860 */     mask.setPositionID(positionID);
/*  861 */     addCommandAndExecute(new AddMaskCommand(this, mask));
/*      */ 
/*  863 */     return mask;
/*      */   }
/*      */ 
/*      */   public boolean hasMaskWithSameColour(int colour)
/*      */   {
/*  868 */     for (Iterator it = this.masks.iterator(); it.hasNext(); ) {
/*  869 */       Mask mask = (Mask)it.next();
/*  870 */       if (mask.getColour() == colour) {
/*  871 */         return true;
/*      */       }
/*      */     }
/*      */ 
/*  875 */     return false;
/*      */   }
/*      */ 
/*      */   public Mask createDefaultMask(int colour, Object userData) {
/*  879 */     Mask mask = new Mask();
/*  880 */     mask.setColour(colour);
/*  881 */     mask.setUserData(userData);
/*  882 */     return mask;
/*      */   }
/*      */ 
/*      */   public void destroyMask(Mask mask)
/*      */   {
/*  889 */     log("PaintExplorer.destroyMask(" + mask + ")");
/*  890 */     addCommandAndExecute(new RemoveMaskCommand(this, mask));
/*      */   }
/*      */ 
/*      */   public Iterator getMasks()
/*      */   {
/*  900 */     return this.masks.iterator();
/*      */   }
/*      */ 
/*      */   void addCommandAndExecute(Command cmd)
/*      */   {
/*  912 */     log("PaintExplorer.addCommandAndExecute(" + cmd + ")");
/*  913 */     if ((this.enableHistory) && ((!(cmd instanceof StateCommand)) || (this.enableMementoHistory)))
/*      */     {
/*  915 */       this.commandHistory.add(cmd);
/*      */     }
/*  917 */     else this.commandHistory.clear();
/*      */ 
/*  920 */     cmd.execute(this.dirty);
/*  921 */     notifyHistoryEvent();
/*      */   }
/*      */ 
/*      */   void addCommand(Command cmd)
/*      */   {
/*  930 */     log("PaintExplorer.addCommand(" + cmd + ")");
/*  931 */     if ((this.enableHistory) && ((!(cmd instanceof StateCommand)) || (this.enableMementoHistory)))
/*      */     {
/*  933 */       this.commandHistory.add(cmd);
/*      */     }
/*  935 */     else this.commandHistory.clear();
/*      */ 
/*  937 */     notifyHistoryEvent();
/*      */   }
/*      */ 
/*      */   void executeCommand(Command cmd)
/*      */   {
/*  944 */     log("PaintExplorer.executeCommand(" + cmd + ")");
/*  945 */     cmd.execute(this.dirty);
/*      */   }
/*      */ 
/*      */   void removeCommand(Command cmd)
/*      */   {
/*  953 */     log("PaintExplorer.removeCommand(" + cmd + ")");
/*  954 */     if ((this.enableHistory) && ((!(cmd instanceof StateCommand)) || (this.enableMementoHistory)))
/*      */     {
/*  956 */       this.commandHistory.remove(cmd);
/*      */     }
/*  958 */     else this.commandHistory.clear();
/*      */ 
/*  960 */     notifyHistoryEvent();
/*      */   }
/*      */ 
/*      */   void replaceCommandAndExecute(Command old, Command cmd)
/*      */   {
/*  969 */     log("PaintExplorer.replaceCommandAndExecute(" + old + ", " + cmd + ")");
/*  970 */     if ((this.enableHistory) && ((!(cmd instanceof StateCommand)) || (this.enableMementoHistory)))
/*      */     {
/*  972 */       this.commandHistory.remove(old);
/*  973 */       this.commandHistory.add(cmd);
/*      */     } else {
/*  975 */       this.commandHistory.clear();
/*      */     }
/*      */ 
/*  978 */     cmd.execute(this.dirty);
/*  979 */     notifyHistoryEvent();
/*      */   }
/*      */ 
/*      */   public void clearHistory()
/*      */   {
/*  986 */     log("PaintExplorer.clearHistory()");
/*  987 */     this.commandHistory.clear();
/*  988 */     notifyHistoryEvent();
/*      */   }
/*      */ 
/*      */   void capHistory()
/*      */   {
/*  996 */     log("PaintExplorer.capHistory()");
/*  997 */     this.commandHistory.cap();
/*  998 */     notifyHistoryEvent();
/*      */   }
/*      */ 
/*      */   void notifyMaskEvent(PaintExplorerMaskEvent event)
/*      */   {
/* 1008 */     log("PaintExplorer.notifyMaskEvent(" + event + ")");
/* 1009 */     for (Iterator it = this.listeners.iterator(); it.hasNext(); ) {
/* 1010 */       PaintExplorerListener listener = (PaintExplorerListener)it.next();
/* 1011 */       listener.onMaskEvent(event);
/*      */     }
/*      */   }
/*      */ 
/*      */   void notifyScissorsEvent(PaintExplorerScissorsEvent event)
/*      */   {
/* 1019 */     log("PaintExplorer.notifyScissorsEvent(" + event + ")");
/* 1020 */     for (Iterator it = this.listeners.iterator(); it.hasNext(); ) {
/* 1021 */       PaintExplorerListener listener = (PaintExplorerListener)it.next();
/* 1022 */       listener.onScissorsEvent(event);
/*      */     }
/*      */   }
/*      */ 
/*      */   void notifyHistoryEvent()
/*      */   {
/* 1030 */     log("PaintExplorer.notifyHistoryEvent()");
/* 1031 */     PaintExplorerHistoryEvent event = new PaintExplorerHistoryEvent(this);
/* 1032 */     for (Iterator it = this.listeners.iterator(); it.hasNext(); ) {
/* 1033 */       PaintExplorerListener listener = (PaintExplorerListener)it.next();
/* 1034 */       listener.onHistoryEvent(event);
/*      */     }
/*      */   }
/*      */ 
/*      */   void notifyHistoryEvent(PaintExplorerHistoryEvent event)
/*      */   {
/* 1042 */     log("PaintExplorer.notifyHistoryEvent(" + event + ")");
/* 1043 */     for (Iterator it = this.listeners.iterator(); it.hasNext(); ) {
/* 1044 */       PaintExplorerListener listener = (PaintExplorerListener)it.next();
/* 1045 */       listener.onHistoryEvent(event);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void notifyProgressEvent(PaintExplorerProgressEvent event)
/*      */   {
/* 1053 */     log("PaintExplorer.notifyProgressEvent(" + event + ")");
/* 1054 */     for (Iterator it = this.listeners.iterator(); it.hasNext(); ) {
/* 1055 */       PaintExplorerListener listener = (PaintExplorerListener)it.next();
/* 1056 */       listener.onProgress(event);
/*      */     }
/*      */   }
/*      */ 
/*      */   BlurManager getBlurManager()
/*      */   {
/* 1064 */     return this.blurManager;
/*      */   }
/*      */ 
/*      */   public void progress(String description, int range)
/*      */   {
/* 1079 */     this.progressDescription = description;
/* 1080 */     this.progressRange = range;
/* 1081 */     notifyProgressEvent(new PaintExplorerProgressEvent(this, description, 0, range));
/*      */   }
/*      */ 
/*      */   public void progress(String description)
/*      */   {
/* 1090 */     this.progressDescription = description;
/* 1091 */     this.progressRange = 0;
/* 1092 */     notifyProgressEvent(new PaintExplorerProgressEvent(this, description, 0, 0));
/*      */   }
/*      */ 
/*      */   public void progress(int value)
/*      */   {
/* 1100 */     notifyProgressEvent(new PaintExplorerProgressEvent(this, this.progressDescription, value, this.progressRange));
/*      */   }
/*      */ 
/*      */   public void progress()
/*      */   {
/* 1109 */     notifyProgressEvent(new PaintExplorerProgressEvent(this));
/*      */   }
/*      */ 
/*      */   void mergeMask(Mask dest, Mask src)
/*      */   {
/* 1120 */     log("PaintExplorer.mergeMask(" + dest + ", " + src + ")");
/* 1121 */     addCommandAndExecute(new MergeMaskCommand(this, dest, src));
/*      */   }
/*      */ 
/*      */   void addMask(Mask mask)
/*      */   {
/* 1129 */     log("PaintExplorer.addMask(" + mask + ")");
/* 1130 */     assert (!this.masks.contains(mask));
/* 1131 */     this.masks.add(mask);
/* 1132 */     mask.setLightingAlgorithm(this.lightingAlgorithm);
/* 1133 */     this.blurManager.reference(mask.getBlurLevel());
/*      */ 
/* 1135 */     notifyMaskEvent(new PaintExplorerMaskEvent(this, 1, mask));
/*      */   }
/*      */ 
/*      */   void removeMask(Mask mask)
/*      */   {
/* 1145 */     log("PaintExplorer.removeMask(" + mask + ")");
/* 1146 */     assert (this.masks.contains(mask));
/* 1147 */     this.masks.remove(mask);
/* 1148 */     this.blurManager.dereference(mask.getBlurLevel());
/*      */ 
/* 1150 */     notifyMaskEvent(new PaintExplorerMaskEvent(this, 2, mask));
/*      */ 
/* 1154 */     this.paintManager.maskRemoved(mask);
/*      */   }
/*      */ 
/*      */   public Memento saveMemento()
/*      */   {
/* 1174 */     log("PaintExplorer.saveMemento()");
/*      */ 
/* 1177 */     Memento memento = new Memento();
/* 1178 */     memento.traps = new Trap[this.traps.length];
/* 1179 */     memento.nodes = new Vector(this.nodes.size());
/* 1180 */     memento.edges = new Vector(this.edges.size());
/* 1181 */     memento.labelled = ((ImageLabelled)this.labelled.copy());
/* 1182 */     memento.fillMask = this.fillMask.clone();
/* 1183 */     memento.paintState = new Mask[this.traps.length];
/*      */ 
/* 1185 */     Map newTraps = new HashMap();
/* 1186 */     Map newNodes = new HashMap();
/* 1187 */     Map newEdges = new HashMap();
/*      */ 
/* 1190 */     for (int i = 0; i < this.traps.length; i++) {
/* 1191 */       Trap newTrap = new Trap();
/* 1192 */       memento.traps[i] = newTrap;
/* 1193 */       newTraps.put(this.traps[i], newTrap);
/* 1194 */       memento.paintState[i] = this.traps[i].getHighestSuperRegion().getMask();
/*      */ 
/* 1197 */       for (Iterator it = this.traps[i].getNodes().iterator(); it.hasNext(); ) {
/* 1198 */         Node oldNode = (Node)it.next();
/* 1199 */         Node newNode = (Node)newNodes.get(oldNode);
/* 1200 */         if (newNode == null)
/*      */         {
/* 1202 */           newNode = new Node(oldNode.x, oldNode.y, 4, oldNode.isBorder());
/*      */ 
/* 1207 */           newNodes.put(oldNode, newNode);
/* 1208 */           memento.nodes.add(newNode);
/*      */         }
/*      */ 
/* 1212 */         if (!newNode.hasTrap(newTrap)) {
/* 1213 */           newNode.addTrap(newTrap);
/*      */         }
/* 1215 */         newTrap.addNode(newNode);
/*      */       }
/* 1217 */       newTrap.getArea().bound(this.traps[i].getArea());
/* 1218 */       newTrap.getStatistics().add(this.traps[i].getStatistics());
/*      */     }
/*      */ 
/* 1222 */     for (Iterator it = this.edges.iterator(); it.hasNext(); ) {
/* 1223 */       Edge oldEdge = (Edge)it.next();
/* 1224 */       Node nodeA = (Node)newNodes.get(oldEdge.getNodeA());
/* 1225 */       Node nodeB = (Node)newNodes.get(oldEdge.getNodeB());
/* 1226 */       Trap trapA = (Trap)newTraps.get(oldEdge.getTrapA());
/* 1227 */       Trap trapB = (Trap)newTraps.get(oldEdge.getTrapB());
/* 1228 */       assert (nodeA != null);
/* 1229 */       assert (nodeB != null);
/* 1230 */       assert (trapA != null);
/* 1231 */       assert (trapB != null);
/*      */ 
/* 1234 */       Edge newEdge = new Edge(nodeA, nodeB, trapA, trapB, oldEdge.getCrackSequence());
/*      */ 
/* 1236 */       nodeA.addEdge(newEdge);
/* 1237 */       if (nodeA != nodeB) {
/* 1238 */         nodeB.addEdge(newEdge);
/*      */       }
/* 1240 */       newEdge.setGradientVector(oldEdge.getGradientVector());
/* 1241 */       newEdge.setCost(oldEdge.getCost());
/* 1242 */       if (oldEdge.isCut()) {
/* 1243 */         newEdge.cut();
/*      */       }
/*      */ 
/* 1246 */       memento.edges.add(newEdge);
/*      */     }
/*      */ 
/* 1249 */     return memento;
/*      */   }
/*      */ 
/*      */   public void restoreMemento(Memento memento)
/*      */   {
/* 1258 */     log("PaintExplorer.restoreMemento()");
/* 1259 */     this.labelled = memento.labelled;
/* 1260 */     this.fillMask = memento.fillMask;
/* 1261 */     this.traps = memento.traps;
/* 1262 */     this.nodes = memento.nodes;
/* 1263 */     this.edges = memento.edges;
/*      */ 
/* 1265 */     recreateHierarchy();
/*      */ 
/* 1267 */     for (Iterator it = this.masks.iterator(); it.hasNext(); ) {
/* 1268 */       ((Mask)it.next()).clearRegions();
/*      */     }
/*      */ 
/* 1272 */     for (int i = 0; i < memento.paintState.length; i++) {
/* 1273 */       Mask mask = memento.paintState[i];
/* 1274 */       AbstractRegion region = this.traps[i].getHighestSuperRegion();
/* 1275 */       region.setMask(mask);
/* 1276 */       if (mask != null) {
/* 1277 */         mask.addRegion(region);
/*      */       }
/*      */     }
/*      */ 
/* 1281 */     this.dirty.bound(this.fillMask.getArea());
/*      */   }
/*      */ 
/*      */   public void save(String filename)
/*      */     throws IOException
/*      */   {
/* 1292 */     log("PaintExplorer.save(" + filename + ")");
/* 1293 */     if (filename.toLowerCase().endsWith(".jpg")) {
/* 1294 */       saveImage(filename);
/*      */     } else {
/* 1296 */       saveProject(filename);
/* 1297 */       progress();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void saveProject(String filename)
/*      */     throws IOException
/*      */   {
/* 1306 */     log("PaintExplorer.saveProject(" + filename + ")");
/* 1307 */     FileOutputStream fos = new FileOutputStream(filename);
/* 1308 */     ZipOutputStream gzos = new ZipOutputStream(fos);
/* 1309 */     gzos.putNextEntry(new ZipEntry(SERIALISE_ZIP_ENTRY_NAME));
/* 1310 */     BufferedOutputStream b = new BufferedOutputStream(gzos);
/* 1311 */     DataOutputStream dos = new DataOutputStream(b);
/* 1312 */     serialise(dos);
/* 1313 */     b.flush();
/* 1314 */     dos.flush();
/* 1315 */     gzos.finish();
/* 1316 */     fos.close();
/*      */   }
/*      */ 
/*      */   public void saveImage(String filename)
/*      */     throws IOException
/*      */   {
/* 1324 */     log("PaintExplorer.saveImage(" + filename + ")");
/*      */     try {
/* 1326 */       Jimi.putImage("image/jpeg", this.painted.getAWTImage(), filename);
/*      */     } catch (JimiException e) {
/* 1328 */       throw new IOException(e.getMessage());
/*      */     }
/*      */   }
/*      */ 
/*      */   public byte[] saveImageToStream()
/*      */     throws IOException
/*      */   {
/* 1336 */     log("PaintExplorer.saveImageToStream(" + this.filename + ")");
/* 1337 */     OutputStream output = new ByteArrayOutputStream();
/*      */     try {
/* 1339 */       Image image = this.painted.getAWTImage();
/*      */ 
/* 1341 */       Jimi.putImage("image/jpeg", image.getScaledInstance(402 * this.painted.width / 1024, 302 * this.painted.height / 768, 4), output);
/*      */     } catch (JimiException e) {
/* 1343 */       throw new IOException(e.getMessage());
/*      */     }
/* 1345 */     return ((ByteArrayOutputStream)output).toByteArray();
/*      */   }
/*      */ 
/*      */   public byte[] saveProjectToStream() throws IOException {
/* 1349 */     log("PaintExplorer.saveProject(" + this.filename + ")");
/* 1350 */     OutputStream baos = new ByteArrayOutputStream();
/* 1351 */     ZipOutputStream gzos = new ZipOutputStream(baos);
/* 1352 */     gzos.putNextEntry(new ZipEntry(SERIALISE_ZIP_ENTRY_NAME));
/* 1353 */     BufferedOutputStream b = new BufferedOutputStream(gzos);
/* 1354 */     DataOutputStream dos = new DataOutputStream(b);
/* 1355 */     serialise(dos);
/* 1356 */     b.flush();
/* 1357 */     dos.flush();
/* 1358 */     gzos.finish();
/*      */ 
/* 1360 */     return ((ByteArrayOutputStream)baos).toByteArray();
/*      */   }
/*      */ 
/*      */   public ImageColour getThumbnail(int width, int height) {
/* 1364 */     ImageColour newImage = new ImageColour(width, height);
/*      */ 
/* 1366 */     if (this.painted != null) {
/* 1367 */       new TriangulationFilter().filter(this.painted, newImage);
/*      */     }
/*      */ 
/* 1370 */     return newImage;
/*      */   }
/*      */ 
/*      */   public String getFilename() {
/* 1374 */     return this.filename;
/*      */   }
/*      */ 
/*      */   public void loadProject(String filename)
/*      */     throws PaintExplorerFileFormatException
/*      */   {
/* 1382 */     log("PaintExplorer.loadProject(" + filename + ")");
/* 1383 */     close();
/*      */     try
/*      */     {
/* 1386 */       FileInputStream fis = new FileInputStream(filename);
/* 1387 */       ZipInputStream gzis = new ZipInputStream(fis);
/* 1388 */       if (gzis.available() == 0) {
/* 1389 */         throw new PaintExplorerFileFormatException("ERR_LOAD_ZIP");
/*      */       }
/*      */ 
/* 1392 */       ZipEntry entry = gzis.getNextEntry();
/* 1393 */       if ((entry == null) || (entry.getName() == null) || (!entry.getName().equals(SERIALISE_ZIP_ENTRY_NAME)))
/*      */       {
/* 1396 */         throw new PaintExplorerFileFormatException("ERR_LOAD_ZIP");
/*      */       }
/*      */ 
/* 1399 */       BufferedInputStream b = new BufferedInputStream(gzis);
/* 1400 */       DataInputStream dis = new DataInputStream(b);
/* 1401 */       deserialise(dis);
/* 1402 */       fis.close();
/*      */     }
/*      */     catch (IOException e) {
/* 1405 */       close();
/* 1406 */       throw new PaintExplorerFileFormatException("ERR_LOAD_IO", e);
/*      */     } catch (RuntimeException e) {
/* 1408 */       close();
/* 1409 */       throw new PaintExplorerFileFormatException("ERR_LOAD_PROJECT", e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void clearCommandHistory()
/*      */   {
/* 1448 */     this.commandHistory.clear();
/*      */   }
/*      */ 
/*      */   public boolean loadFromWeb(InputStream in) throws PaintExplorerFileFormatException {
/* 1452 */     log("PaintExplorer.loadProject(web service)");
/* 1453 */     close();
/*      */     try
/*      */     {
/* 1474 */       ZipInputStream gzis = new ZipInputStream(in);
/* 1475 */       if (gzis.available() == 0) {
/* 1476 */         throw new PaintExplorerFileFormatException("ERR_LOAD_ZIP");
/*      */       }
/*      */ 
/* 1479 */       ZipEntry entry = gzis.getNextEntry();
/* 1480 */       if ((entry == null) || (entry.getName() == null) || (!entry.getName().equals(SERIALISE_ZIP_ENTRY_NAME)))
/*      */       {
/* 1483 */         throw new PaintExplorerFileFormatException("ERR_LOAD_ZIP");
/*      */       }
/*      */ 
/* 1486 */       BufferedInputStream b = new BufferedInputStream(gzis);
/* 1487 */       DataInputStream dis = new DataInputStream(b);
/* 1488 */       deserialise(dis);
/*      */ 
/* 1490 */       in.close();
/* 1491 */       return true;
/*      */     } catch (IOException e) {
/* 1493 */       System.err.println("The IO Error was : " + e);
/* 1494 */       close();
/*      */ 
/* 1496 */       throw new PaintExplorerFileFormatException("ERR_LOAD_IO", e);
/*      */     }
/*      */     catch (RuntimeException e)
/*      */     {
/* 1500 */       close();
/* 1501 */       throw new PaintExplorerFileFormatException("ERR_LOAD_PROJECT", e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void loadImage(URL url)
/*      */     throws PaintExplorerFileFormatException
/*      */   {
/* 1510 */     log("PaintExplorer.loadImagefromURL(" + url + ")");
/* 1511 */     close();
/*      */     try
/*      */     {
/* 1514 */       progress("PROGRESS_LOAD_IMAGE_FROM_URL");
/* 1515 */       ImageProducer prod = Jimi.getImageProducer(url, 16);
/*      */ 
/* 1517 */       ResizingConsumer cons = new ResizingConsumer(prod, this.nominalWidth, this.nominalHeight);
/*      */ 
/* 1520 */       prod.startProduction(cons);
/* 1521 */       cons.waitForCompletion();
/*      */ 
/* 1523 */       ImageColour resized = cons.getResizedImage();
/* 1524 */       if (resized == null) {
/* 1525 */         log("Warning: piped convolution failed");
/* 1526 */         loadImage(new ImageColour(this.filename));
/*      */       } else {
/* 1528 */         loadImage(resized);
/*      */       }
/*      */     }
/*      */     catch (PaintExplorerFileFormatException e) {
/* 1532 */       throw e;
/*      */     }
/*      */     catch (Exception e) {
/* 1535 */       throw new PaintExplorerFileFormatException("ERR_LOAD_IMAGE", e);
/*      */     } finally {
/* 1537 */       progress();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void loadImage(String filename)
/*      */     throws PaintExplorerFileFormatException
/*      */   {
/* 1545 */     log("PaintExplorer.loadImage(" + filename + ")");
/* 1546 */     close();
/*      */     try
/*      */     {
/* 1549 */       progress("PROGRESS_LOAD_IMAGE");
/* 1550 */       ImageProducer prod = Jimi.getImageProducer(filename, 16);
/*      */ 
/* 1552 */       ResizingConsumer cons = new ResizingConsumer(prod, this.nominalWidth, this.nominalHeight);
/*      */ 
/* 1555 */       prod.startProduction(cons);
/* 1556 */       cons.waitForCompletion();
/*      */ 
/* 1558 */       ImageColour resized = cons.getResizedImage();
/* 1559 */       if (resized == null) {
/* 1560 */         log("Warning: piped convolution failed");
/* 1561 */         loadImage(new ImageColour(filename));
/*      */       } else {
/* 1563 */         loadImage(resized);
/*      */       }
/*      */     }
/*      */     catch (PaintExplorerFileFormatException e) {
/* 1567 */       throw e;
/*      */     }
/*      */     catch (Exception e) {
/* 1570 */       throw new PaintExplorerFileFormatException("ERR_LOAD_IMAGE", e);
/*      */     } finally {
/* 1572 */       progress();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void loadImage(ImageColour image)
/*      */     throws PaintExplorerFileFormatException
/*      */   {
/* 1581 */     log("PaintExplorer.loadImage(" + image + ")");
/* 1582 */     close();
/*      */ 
/* 1585 */     float resizeWidth = this.nominalWidth / image.getWidth();
/* 1586 */     float resizeHeight = this.nominalHeight / image.getHeight();
/* 1587 */     float resize = Math.min(resizeWidth, resizeHeight);
/*      */ 
/* 1589 */     if ((resize < 1.0F) && (this.nominalWidth != -1) && (this.nominalHeight != -1))
/*      */     {
/* 1591 */       progress("PROGRESS_DOWNSAMPLE");
/* 1592 */       ImageColour resized = new ImageColour((int)(image.getWidth() * resize), (int)(image.getHeight() * resize));
/*      */ 
/* 1596 */       new BilinearFilter(1).filter(image, resized);
/*      */ 
/* 1598 */       image = resized;
/* 1599 */       progress();
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/* 1605 */       ByteArrayOutputStream os = new ByteArrayOutputStream();
/* 1606 */       JimiWriter writer = Jimi.createTypedJimiWriter("image/jpeg");
/* 1607 */       JPGOptions jpegOptions = new JPGOptions();
/* 1608 */       jpegOptions.setQuality(this.jpegEncodeQuality);
/* 1609 */       writer.setOptions(jpegOptions);
/* 1610 */       writer.setSource(image.getAWTImage());
/* 1611 */       writer.putImage(os);
/* 1612 */       this.compressedOriginal = os.toByteArray();
/*      */ 
/* 1615 */       Image img = Jimi.getImage(new ByteArrayInputStream(this.compressedOriginal));
/*      */ 
/* 1617 */       this.original = new ImageColour(img);
/*      */     } catch (JimiException e) {
/* 1619 */       throw new PaintExplorerFileFormatException("ERR_LOAD_IMAGE", e);
/*      */     }
/*      */ 
/* 1622 */     this.painted = new ImageColour(this.original);
/* 1623 */     preprocess();
/* 1624 */     this.isReady = true;
/* 1625 */     progress();
/*      */   }
/*      */ 
/*      */   public boolean load(String filename)
/*      */     throws PaintExplorerFileFormatException
/*      */   {
/* 1635 */     log("PaintExplorer.load(" + filename + ")");
/*      */     try {
/* 1637 */       loadProject(filename);
/* 1638 */       return true;
/*      */     } catch (PaintExplorerFileFormatException e) {
/* 1640 */       if ((e.getKey().equals("ERR_LOAD_MAGIC_NUMBER")) || (e.getKey().equals("ERR_LOAD_ZIP")))
/*      */       {
/* 1642 */         loadImage(filename);
/*      */       } else {
/* 1644 */         close();
/* 1645 */         throw e;
/*      */       }
/*      */     }
/*      */ 
/* 1649 */     this.filename = filename;
/* 1650 */     return false;
/*      */   }
/*      */ 
/*      */   public void close()
/*      */   {
/* 1658 */     log("PaintExplorer.close()");
/* 1659 */     this.isReady = false;
/*      */ 
/* 1662 */     this.original = null;
/* 1663 */     this.painted = null;
/* 1664 */     this.median = null;
/* 1665 */     this.gaussian = null;
/* 1666 */     this.gradient = null;
/* 1667 */     this.direction = null;
/* 1668 */     this.labelled = null;
/* 1669 */     this.fillMask = null;
/* 1670 */     this.traps = null;
/* 1671 */     this.nodes.clear();
/* 1672 */     this.edges.clear();
/* 1673 */     this.masks.clear();
/* 1674 */     this.commandHistory.clear();
/* 1675 */     this.dirty.clear();
/* 1676 */     this.blurManager.clear();
/*      */ 
/* 1678 */     notifyHistoryEvent();
/*      */   }
/*      */ 
/*      */   public void serialise(DataOutput out)
/*      */     throws IOException
/*      */   {
/* 1701 */     log("PaintExplorer.serialise(" + out + ")");
/* 1702 */     progress("PROGRESS_SAVE_PROJECT", 150);
/*      */ 
/* 1704 */     log("serialise");
/* 1705 */     selfCheck();
/* 1706 */     this.byteSize = "";
/*      */ 
/* 1708 */     Vector tmpRegions = new Vector();
/*      */ 
/* 1710 */     this.byteSize = (this.byteSize + "Begin : " + ((DataOutputStream)out).size() + "\n");
/*      */ 
/* 1712 */     out.writeInt(SERIALISE_MAGIC_NUMBER);
/* 1713 */     out.writeInt(this.labelled.getWidth());
/* 1714 */     out.writeInt(this.labelled.getHeight());
/* 1715 */     out.writeInt(this.traps.length);
/* 1716 */     out.writeInt(this.nodes.size());
/* 1717 */     out.writeInt(this.edges.size());
/* 1718 */     out.writeInt(this.masks.size());
/* 1719 */     out.writeInt(SERIALISE_SENTINAL);
/*      */ 
/* 1721 */     this.byteSize = (this.byteSize + "after writing sizes : " + ((DataOutputStream)out).size() + "\n");
/*      */ 
/* 1723 */     Map nodeMap = new HashMap();
/* 1724 */     int nodeIndex = 0;
/* 1725 */     for (Iterator it = this.nodes.iterator(); it.hasNext(); ) {
/* 1726 */       Node node = (Node)it.next();
/* 1727 */       nodeMap.put(node, new Integer(nodeIndex++));
/* 1728 */       out.writeByte((byte)node.getTrapCount());
/* 1729 */       out.writeInt(node.x);
/* 1730 */       out.writeInt(node.y);
/*      */     }
/*      */ 
/* 1733 */     this.byteSize = (this.byteSize + "after writing nodes : " + ((DataOutputStream)out).size() + "\n");
/*      */ 
/* 1736 */     progress(10);
/* 1737 */     out.writeInt(SERIALISE_SENTINAL);
/*      */ 
/* 1739 */     Map trapMap = new HashMap();
/*      */     Iterator it;
/* 1740 */     for (int i = 0; i < this.traps.length; i++) {
/* 1741 */       log("Write trap " + i);
/* 1742 */       trapMap.put(this.traps[i], new Integer(i));
/* 1743 */       out.writeInt(this.traps[i].getNodes().size());
/*      */ 
/* 1746 */       int index = 0;
/* 1747 */       SuperRegion trapRegion = this.traps[i].getHighestSuperRegion();
/* 1748 */       for (it = tmpRegions.iterator(); it.hasNext(); ) {
/* 1749 */         SuperRegion region = (SuperRegion)it.next();
/* 1750 */         if (region == trapRegion) {
/*      */           break;
/*      */         }
/* 1753 */         index++;
/*      */       }
/* 1755 */       if (index == tmpRegions.size()) {
/* 1756 */         tmpRegions.add(trapRegion);
/*      */       }
/* 1758 */       out.writeInt(index);
/*      */ 
/* 1761 */       for (it = this.traps[i].getNodes().iterator(); it.hasNext(); ) {
/* 1762 */         Node node = (Node)it.next();
/* 1763 */         log("  node " + node);
/* 1764 */         out.writeInt(((Integer)nodeMap.get(node)).intValue());
/*      */       }
/*      */     }
/*      */ 
/* 1768 */     this.byteSize = (this.byteSize + "after writing traps : " + ((DataOutputStream)out).size());
/*      */ 
/* 1770 */     progress(15);
/* 1771 */     out.writeInt(SERIALISE_SENTINAL);
/*      */ 
/* 1773 */     int[] labelData = this.labelled.getBufferInteger();
/* 1774 */     for (int i = 0; i < labelData.length; i++) {
/* 1775 */       out.writeInt(labelData[i]);
/*      */     }
/*      */ 
/* 1778 */     this.byteSize = (this.byteSize + "after writing labelled : " + ((DataOutputStream)out).size() + "\n");
/* 1779 */     out.writeInt(SERIALISE_SENTINAL);
/*      */ 
/* 1781 */     float[] gradData = this.gradient.getBufferFloat();
/* 1782 */     for (int i = 0; i < gradData.length; i++) {
/* 1783 */       out.writeFloat(gradData[i]);
/*      */     }
/*      */ 
/* 1786 */     this.byteSize = (this.byteSize + "after writing gradient : " + ((DataOutputStream)out).size() + "\n");
/*      */ 
/* 1788 */     progress(30);
/*      */ 
/* 1819 */     progress(40);
/* 1820 */     out.writeInt(SERIALISE_SENTINAL);
/*      */ 
/* 1823 */     for ( it = tmpRegions.iterator(); it.hasNext(); ) {
/* 1824 */       SuperRegion region = (SuperRegion)it.next();
/* 1825 */       Mask regionMask = region.getMask();
/* 1826 */       int index = 0;
/* 1827 */       if (regionMask == null) {
/* 1828 */         index = -1;
/*      */       } else {
/* 1830 */         for (Iterator jt = this.masks.iterator(); jt.hasNext(); ) {
/* 1831 */           Mask mask = (Mask)jt.next();
/* 1832 */           if (mask == regionMask) {
/*      */             break;
/*      */           }
/* 1835 */           index++;
/*      */         }
/* 1837 */         assert (index < this.masks.size());
/*      */       }
/* 1839 */       out.writeInt(index);
/*      */     }
/*      */ 
/* 1842 */     this.byteSize = (this.byteSize + "after writing use of mask for superregion : " + ((DataOutputStream)out).size() + "\n");
/*      */ 
/* 1845 */     for ( it = tmpRegions.iterator(); it.hasNext(); ) {
/* 1846 */       SuperRegion region = (SuperRegion)it.next();
/* 1847 */       Mask regionMask = region.getDecorateMask();
/* 1848 */       int index = 0;
/* 1849 */       if (regionMask == null) {
/* 1850 */         index = -1;
/*      */       } else {
/* 1852 */         for (Iterator jt = this.masks.iterator(); jt.hasNext(); ) {
/* 1853 */           Mask mask = (Mask)jt.next();
/* 1854 */           if (mask == regionMask) {
/*      */             break;
/*      */           }
/* 1857 */           index++;
/*      */         }
/* 1859 */         assert (index < this.masks.size());
/*      */       }
/* 1861 */       out.writeInt(index);
/*      */     }
/*      */ 
/* 1864 */     this.byteSize = (this.byteSize + "after saving use of mask for erased : " + ((DataOutputStream)out).size() + "\n");
/*      */ 
/* 1866 */     progress(50);
/* 1867 */     out.writeInt(SERIALISE_SENTINAL);
/*      */ 
/* 1869 */     for ( it = this.masks.iterator(); it.hasNext(); ) {
/* 1870 */       Mask mask = (Mask)it.next();
/* 1871 */       out.writeInt(mask.getColour());
/* 1872 */       out.writeInt(mask.getPositionID());
/* 1873 */       out.writeByte((byte)mask.getBlurLevel());
/* 1874 */       if (mask.getUserData() == null)
/* 1875 */         out.writeUTF("");
/*      */       else {
/* 1877 */         out.writeUTF(mask.getUserData().toString());
/*      */       }
/* 1879 */       out.writeInt(mask.getItemId());
/*      */     }
/*      */ 
/* 1882 */     this.byteSize = (this.byteSize + "after writing mask data(colours etc) : " + ((DataOutputStream)out).size() + "\n");
/*      */ 
/* 1885 */     progress(60);
/*      */ 
/* 1888 */     out.writeInt(SERIALISE_SENTINAL);
/*      */ 
/* 1890 */     byte[] fillMaskData = this.fillMask.getBufferByte();
/* 1891 */     out.write(fillMaskData);
/*      */ 
/* 1893 */     this.byteSize = (this.byteSize + "after writing fillmask : " + ((DataOutputStream)out).size() + "\n");
/*      */ 
/* 1897 */     out.writeInt(SERIALISE_SENTINAL);
/*      */ 
/* 1899 */     out.writeInt(this.compressedOriginal.length);
/* 1900 */     out.write(this.compressedOriginal);
/*      */ 
/* 1902 */     out.writeInt(SERIALISE_SENTINAL);
/*      */ 
/* 1904 */     if (this.enableDecorate) {
/* 1905 */       byte[] decoratedData = this.decorated.getBufferByte();
/* 1906 */       out.write(decoratedData);
/* 1907 */       out.writeInt(SERIALISE_SENTINAL);
/*      */     }
/*      */ 
/* 1910 */     this.byteSize = (this.byteSize + "after writing decorated : " + ((DataOutputStream)out).size() + "\n");
/* 1911 */     progress(80);
/*      */   }
/*      */ 
/*      */   public void deserialise(DataInput in)
/*      */     throws IOException, PaintExplorerFileFormatException
/*      */   {
/* 1921 */     log("PaintExplorer.deserialise(" + in + ")");
/* 1922 */     progress("PROGRESS_LOAD_PROJECT", 100);
/*      */ 
/* 1925 */     close();
/*      */ 
/* 1927 */     if (in.readInt() != SERIALISE_MAGIC_NUMBER) {
/* 1928 */       throw new PaintExplorerFileFormatException("ERR_LOAD_MAGIC_NUMBER");
/*      */     }
/*      */ 
/* 1932 */     int width = in.readInt();
/* 1933 */     int height = in.readInt();
/* 1934 */     int trapCount = in.readInt();
/* 1935 */     int nodeCount = in.readInt();
/* 1936 */     int edgeCount = in.readInt();
/* 1937 */     int maskCount = in.readInt();
/*      */ 
/* 1939 */     if (in.readInt() != SERIALISE_SENTINAL) {
/* 1940 */       throw new PaintExplorerFileFormatException("ERR_LOAD_HEADER");
/*      */     }
/*      */ 
/* 1944 */     progress(10);
/*      */ 
/* 1947 */     Node[] tmpNodes = new Node[nodeCount];
/* 1948 */     for (int i = 0; i < nodeCount; i++) {
/* 1949 */       byte nodeTrapCount = in.readByte();
/* 1950 */       int nodeX = in.readInt();
/* 1951 */       int nodeY = in.readInt();
/* 1952 */       boolean nodeBorder = false;
/* 1953 */       if ((nodeX == 0) || (nodeY == 0) || (nodeX == width) || (nodeY == height)) {
/* 1954 */         nodeBorder = true;
/*      */       }
/* 1956 */       tmpNodes[i] = new Node(nodeX, nodeY, nodeTrapCount, nodeBorder);
/* 1957 */       this.nodes.add(tmpNodes[i]);
/*      */     }
/*      */ 
/* 1960 */     if (in.readInt() != SERIALISE_SENTINAL) {
/* 1961 */       throw new PaintExplorerFileFormatException("ERR_LOAD_NODES");
/*      */     }
/*      */ 
/* 1965 */     progress(20);
/*      */ 
/* 1968 */     Vector tmpRegions = new Vector();
/* 1969 */     this.traps = new Trap[trapCount];
/*      */ 
/* 1971 */     for (int i = 0; i < trapCount; i++) {
/* 1972 */       log("Load trap " + i);
/* 1973 */       this.traps[i] = new Trap();
/* 1974 */       int trapNodeCount = in.readInt();
/* 1975 */       int trapRegionIndex = in.readInt();
/*      */ 
/* 1978 */       if (trapRegionIndex == tmpRegions.size()) {
/* 1979 */         SuperRegion region = new SuperRegion();
/* 1980 */         tmpRegions.add(region);
/*      */       }
/* 1982 */       if (trapRegionIndex >= tmpRegions.size()) {
/* 1983 */         throw new PaintExplorerFileFormatException("ERR_LOAD_TRAPS");
/*      */       }
/*      */ 
/* 1986 */       SuperRegion region = (SuperRegion)tmpRegions.get(trapRegionIndex);
/* 1987 */       this.traps[i].setSuperRegion(region);
/*      */ 
/* 1990 */       for (int j = 0; j < trapNodeCount; j++) {
/* 1991 */         int trapNodeIndex = in.readInt();
/* 1992 */         log("  node " + trapNodeIndex + " " + tmpNodes[trapNodeIndex]);
/* 1993 */         if (trapNodeIndex > tmpNodes.length) {
/* 1994 */           throw new PaintExplorerFileFormatException("ERR_LOAD_TRAPS");
/*      */         }
/*      */ 
/* 1997 */         Node trapNode = tmpNodes[trapNodeIndex];
/* 1998 */         this.traps[i].addNode(trapNode);
/* 1999 */         trapNode.addTrap(this.traps[i]);
/*      */       }
/*      */     }
/*      */ 
/* 2003 */     progress(30);
/*      */ 
/* 2005 */     if (in.readInt() != SERIALISE_SENTINAL) {
/* 2006 */       throw new PaintExplorerFileFormatException("ERR_LOAD_TRAPS");
/*      */     }
/*      */ 
/* 2010 */     this.labelled = new ImageLabelled(width, height);
/* 2011 */     int[] labelData = this.labelled.getBufferInteger();
/* 2012 */     for (int i = 0; i < labelData.length; i++) {
/* 2013 */       labelData[i] = in.readInt();
/*      */     }
/*      */ 
/* 2017 */     if (in.readInt() != SERIALISE_SENTINAL) {
/* 2018 */       throw new PaintExplorerFileFormatException("ERR_LOAD_LABELLED");
/*      */     }
/*      */ 
/* 2022 */     this.gradient = new ImageFloat(width, height);
/* 2023 */     float[] gradData = this.gradient.getBufferFloat();
/* 2024 */     for (int i = 0; i < gradData.length; i++) {
/* 2025 */       gradData[i] = in.readFloat();
/*      */     }
/*      */ 
/* 2028 */     if (in.readInt() != SERIALISE_SENTINAL) {
/* 2029 */       throw new PaintExplorerFileFormatException("ERR_LOAD_GRADIENT");
/*      */     }
/*      */ 
/* 2033 */     createEdges(this.nodes);
/* 2034 */     progress(40);
/*      */ 
/* 2091 */     Mask[] tmpMasks = new Mask[maskCount];
/* 2092 */     for (int i = 0; i < tmpMasks.length; i++) {
/* 2093 */       tmpMasks[i] = new Mask();
/* 2094 */       this.masks.add(tmpMasks[i]);
/*      */     }
/*      */ 
/* 2097 */     for (Iterator it = tmpRegions.iterator(); it.hasNext(); ) {
/* 2098 */       SuperRegion region = (SuperRegion)it.next();
/* 2099 */       int maskIndex = in.readInt();
/* 2100 */       if (maskIndex > tmpMasks.length) {
/* 2101 */         throw new PaintExplorerFileFormatException("ERR_LOAD_REGIONS");
/*      */       }
/*      */ 
/* 2104 */       if (maskIndex != -1) {
/* 2105 */         region.setMask(tmpMasks[maskIndex]);
/*      */       }
/*      */     }
/*      */ 
/* 2109 */     for (Iterator it = tmpRegions.iterator(); it.hasNext(); ) {
/* 2110 */       SuperRegion region = (SuperRegion)it.next();
/* 2111 */       int maskIndex = in.readInt();
/* 2112 */       if (maskIndex > tmpMasks.length) {
/* 2113 */         throw new PaintExplorerFileFormatException("ERR_LOAD_REGIONS");
/*      */       }
/*      */ 
/* 2116 */       if (maskIndex != -1) {
/* 2117 */         region.setDecorateMask(tmpMasks[maskIndex]);
/*      */       }
/*      */     }
/*      */ 
/* 2121 */     progress(60);
/*      */ 
/* 2123 */     if (in.readInt() != SERIALISE_SENTINAL) {
/* 2124 */       throw new PaintExplorerFileFormatException("ERR_LOAD_REGIONS");
/*      */     }
/*      */ 
/* 2129 */     for (int i = 0; i < tmpMasks.length; i++) {
/* 2130 */       int maskColour = in.readInt();
/* 2131 */       int maskPositionID = in.readInt();
/* 2132 */       int blurLevel = in.readByte();
/* 2133 */       String userData = in.readUTF();
/* 2134 */       int itemId = in.readInt();
/* 2135 */       tmpMasks[i].setColour(maskColour);
/* 2136 */       tmpMasks[i].setPositionID(maskPositionID);
/* 2137 */       tmpMasks[i].setBlurLevel(blurLevel);
/* 2138 */       this.blurManager.reference(blurLevel);
/* 2139 */       tmpMasks[i].setUserData(userData);
/* 2140 */       tmpMasks[i].setItemId(itemId);
/*      */     }
/*      */ 
/* 2143 */     progress(70);
/* 2144 */     if (in.readInt() != SERIALISE_SENTINAL) {
/* 2145 */       throw new PaintExplorerFileFormatException("ERR_LOAD_MASKS");
/*      */     }
/*      */ 
/* 2152 */     this.fillMask = new ImageFillMask(width, height);
/* 2153 */     byte[] fillMaskData = this.fillMask.getBufferByte();
/* 2154 */     in.readFully(fillMaskData);
/*      */ 
/* 2156 */     this.fillMaskDecorate = this.fillMask.clone();
/*      */ 
/* 2158 */     progress(80);
/* 2159 */     if (in.readInt() != SERIALISE_SENTINAL) {
/* 2160 */       throw new PaintExplorerFileFormatException("ERR_LOAD_FILLMASK");
/*      */     }
/*      */ 
/* 2167 */     int compressedSize = in.readInt();
/* 2168 */     this.compressedOriginal = new byte[compressedSize];
/* 2169 */     in.readFully(this.compressedOriginal);
/*      */ 
/* 2171 */     if (this.enableDecorate) {
/* 2172 */       if (in.readInt() != SERIALISE_SENTINAL) {
/* 2173 */         throw new PaintExplorerFileFormatException("ERR_LOAD_GRADIENT");
/*      */       }
/*      */ 
/* 2178 */       this.decorated = new ImageDecorate(width, height);
/* 2179 */       byte[] decoratedData = this.decorated.getBufferByte();
/* 2180 */       in.readFully(decoratedData);
/*      */     }
/*      */ 
/* 2184 */     Image img = Jimi.getImage(new ByteArrayInputStream(this.compressedOriginal));
/*      */ 
/* 2186 */     this.original = new ImageColour(img);
/*      */ 
/* 2188 */     progress(90);
/* 2189 */     if (in.readInt() != SERIALISE_SENTINAL) {
/* 2190 */       throw new PaintExplorerFileFormatException("ERR_LOAD_ORIGINAL");
/*      */     }
/*      */ 
/* 2194 */     this.dirty.bound(this.original.getArea());
/*      */ 
/* 2197 */     createTrapRegions();
/*      */ 
/* 2200 */     for (int i = 0; i < this.traps.length; i++) {
/* 2201 */       this.traps[i].getHighestSuperRegion().merge(this.traps[i]);
/*      */     }
/*      */ 
/* 2205 */     for (Iterator it = tmpRegions.iterator(); it.hasNext(); ) {
/* 2206 */       SuperRegion region = (SuperRegion)it.next();
/* 2207 */       Mask mask = region.getMask();
/* 2208 */       if (mask != null) {
/* 2209 */         mask.addRegion(region);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2214 */     for (Iterator it = this.masks.iterator(); it.hasNext(); ) {
/* 2215 */       Mask mask = (Mask)it.next();
/* 2216 */       mask.mustRepaintAll();
/*      */     }
/*      */ 
/* 2220 */     ImageColour gaussianSource = this.original;
/* 2221 */     if (this.enableMedianFilter) {
/* 2222 */       this.median = new ImageColour(this.original);
/* 2223 */       new MedianFilter().filter(this.original, this.median);
/* 2224 */       gaussianSource = this.median;
/*      */     }
/* 2226 */     this.gaussian = new ImageColour(this.original);
/* 2227 */     GaussianFilter.getSmall().filter(this.original, this.gaussian);
/*      */ 
/* 2230 */     this.recolourData = new RecolourData[this.labelled.getBufferInteger().length];
/* 2231 */     for (int i = 0; i < this.recolourData.length; i++) {
/* 2232 */       this.recolourData[i] = new RecolourData();
/*      */     }
/* 2234 */     this.recolourFilteredWeight = new int[this.recolourData.length];
/*      */ 
/* 2237 */     this.painted = new ImageColour(this.original);
/* 2238 */     update();
/*      */ 
/* 2240 */     if (!selfCheck()) {
/* 2241 */       throw new PaintExplorerFileFormatException("ERR_SELFCHECK");
/*      */     }
/*      */ 
/* 2244 */     this.isReady = true;
/* 2245 */     progress();
/*      */   }
/*      */ 
/*      */   public Rectangle update()
/*      */   {
/* 2277 */     if ((this.painted == null) || (this.dirty.isEmpty())) {
/* 2278 */       return this.dirty.getRectangle();
/*      */     }
/*      */ 
/* 2282 */     if (this.dirty.getWidth() * this.dirty.getHeight() > 40000) {
/* 2283 */       progress("PROGRESS_UPDATE");
/*      */     }
/*      */ 
/* 2286 */     int[] colourData = this.original.getBufferIntBGR();
/* 2287 */     int[] paintData = this.painted.getBufferIntBGR();
/* 2288 */     int[] labelData = this.labelled.getBufferInteger();
/* 2289 */     byte[] decoratedData = this.decorated.getBufferByte();
/* 2290 */     int labelPitch = this.labelled.getPitch();
/* 2291 */     int pitch = this.original.getPitch();
/* 2292 */     int width = this.original.getWidth();
/* 2293 */     int height = this.original.getHeight();
/*      */ 
/* 2297 */     int range = 2;
/* 2298 */     this.dirty.top -= range;
/* 2299 */     this.dirty.left -= range;
/* 2300 */     this.dirty.bottom += range;
/* 2301 */     this.dirty.right += range;
/* 2302 */     this.dirty.cropForImageSize(this.original.getWidth(), this.original.getHeight());
/*      */ 
/* 2312 */     int maskDataSetSize = 25;
/* 2313 */     maskDataSetSize += 5;
/* 2314 */     MaskDataSet maskDataSet = new MaskDataSet(maskDataSetSize);
/* 2315 */     MaskDataSet decorateMaskDataSet = new MaskDataSet(maskDataSetSize);
/*      */ 
/* 2318 */     int[] domSamples = new int[25];
/* 2319 */     int[] subdomSamples = new int[25];
/*      */ 
/* 2321 */     Decomposition decomposition = new Decomposition();
/*      */ 
/* 2323 */     Mask mask = null; Mask decorateMask = null;
/*      */ 
/* 2326 */     for (int y = this.dirty.top; y <= this.dirty.bottom; y++) {
/* 2327 */       int x = this.dirty.left;
/* 2328 */       int dyStop = Math.min(y + range, height - 1);
/* 2329 */       int windowElements = 0;
/*      */ 
/* 2332 */       int windowLeft = Math.max(0, x - range);
/* 2333 */       int windowRight = Math.min(x + range, width - 1);
/* 2334 */       int yRange = dyStop - Math.max(0, y - range) + 1;
/*      */ 
/* 2336 */       maskDataSet.clear();
/* 2337 */       decorateMaskDataSet.clear();
/*      */ 
/* 2339 */       for (int dy = Math.max(0, y - range); dy <= dyStop; dy++) {
/* 2340 */         for (int dx = windowLeft; dx <= windowRight; dx++) {
/* 2341 */           Trap trap = this.traps[labelData[(dy * labelPitch + dx)]];
/* 2342 */           AbstractRegion region = trap.getHighestSuperRegion();
/* 2343 */           if (decoratedData[(dy * labelPitch + dx)] == 0)
/*      */           {
/* 2345 */             mask = region.getMask();
/* 2346 */             maskDataSet.add(mask);
/*      */           }
/*      */           else
/*      */           {
/* 2350 */             decorateMask = region.getDecorateMask();
/* 2351 */             decorateMaskDataSet.add(decorateMask);
/*      */           }
/*      */ 
/* 2354 */           windowElements++;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 2359 */       for (x = this.dirty.left; x <= this.dirty.right; x++)
/*      */       {
/* 2361 */         int dx = Math.min(x + range, width - 1);
/* 2362 */         if (dx > windowRight) {
/* 2363 */           for (int dy = Math.max(0, y - range); dy <= dyStop; dy++) {
/* 2364 */             Trap trap = this.traps[labelData[(dy * labelPitch + dx)]];
/* 2365 */             AbstractRegion region = trap.getHighestSuperRegion();
/* 2366 */             if (decoratedData[(dy * labelPitch + dx)] == 0)
/*      */             {
/* 2368 */               mask = region.getMask();
/* 2369 */               maskDataSet.add(mask);
/*      */             }
/*      */             else
/*      */             {
/* 2373 */               decorateMask = region.getDecorateMask();
/* 2374 */               decorateMaskDataSet.add(decorateMask);
/*      */             }
/*      */ 
/* 2377 */             windowElements += yRange;
/*      */           }
/*      */ 
/* 2380 */           windowRight++;
/*      */         }
/*      */ 
/* 2384 */         dx = Math.max(0, x - range);
/* 2385 */         if (dx > windowLeft) {
/* 2386 */           for (int dy = Math.max(0, y - range); dy <= dyStop; dy++) {
/* 2387 */             Trap trap = this.traps[labelData[(dy * labelPitch + windowLeft)]];
/* 2388 */             AbstractRegion region = trap.getHighestSuperRegion();
/* 2389 */             if (decoratedData[(dy * labelPitch + windowLeft)] == 0)
/*      */             {
/* 2391 */               mask = region.getMask();
/* 2392 */               maskDataSet.subtract(mask);
/*      */             }
/*      */             else
/*      */             {
/* 2396 */               decorateMask = region.getDecorateMask();
/* 2397 */               decorateMaskDataSet.subtract(decorateMask);
/*      */             }
/*      */ 
/* 2400 */             windowElements -= yRange;
/*      */           }
/* 2402 */           windowLeft++;
/*      */         }
/*      */ 
/* 2405 */         int originalColour = colourData[(y * pitch + x)];
/*      */ 
/* 2407 */         if (maskDataSet.maskCount + decorateMaskDataSet.maskCount != 0)
/*      */         {
/* 2409 */           if (maskDataSet.maskCount + decorateMaskDataSet.maskCount == 1)
/*      */           {
/* 2411 */             if (maskDataSet.maskCount == 1)
/* 2412 */               mask = maskDataSet.maskData[0].mask;
/*      */             else
/* 2414 */               mask = decorateMaskDataSet.maskData[0].mask;
/* 2415 */             if (mask == null)
/*      */             {
/* 2417 */               this.recolourData[(y * pitch + x)].weight = 255;
/* 2418 */               this.recolourData[(y * pitch + x)].component1 = originalColour;
/* 2419 */               this.recolourData[(y * pitch + x)].maskA = null;
/* 2420 */               this.recolourData[(y * pitch + x)].maskB = null;
/* 2421 */               this.recolourData[(y * pitch + x)].painted = true;
/*      */             } else {
/* 2423 */               if (mask.getBlurLevel() > 0) {
/* 2424 */                 ImageColour blurImage = this.blurManager.get(mask.getBlurLevel());
/*      */ 
/* 2426 */                 originalColour = blurImage.getBufferIntBGR()[(y * pitch + x)];
/*      */               }
/*      */ 
/* 2431 */               this.recolourData[(y * pitch + x)].weight = 255;
/* 2432 */               this.recolourData[(y * pitch + x)].component1 = mask.getRecolour((int)Colour.getIntensity(originalColour));
/*      */ 
/* 2434 */               this.recolourData[(y * pitch + x)].maskA = mask;
/* 2435 */               this.recolourData[(y * pitch + x)].maskB = null;
/* 2436 */               this.recolourData[(y * pitch + x)].painted = true;
/*      */             }
/*      */ 
/*      */           }
/*      */           else
/*      */           {
/* 2445 */             MaskData domMaskData = null;
/* 2446 */             MaskData subdomMaskData = null;
/*      */ 
/* 2448 */             for (int i = 0; i < maskDataSet.maskCount; i++) {
/* 2449 */               MaskData md = maskDataSet.maskData[i];
/* 2450 */               if ((domMaskData == null) || (md.weight > domMaskData.weight)) {
/* 2451 */                 subdomMaskData = domMaskData;
/* 2452 */                 domMaskData = maskDataSet.maskData[i];
/* 2453 */               } else if ((subdomMaskData == null) || (md.weight > subdomMaskData.weight))
/*      */               {
/* 2455 */                 subdomMaskData = maskDataSet.maskData[i];
/*      */               }
/*      */             }
/*      */ 
/* 2459 */             for (int i = 0; i < decorateMaskDataSet.maskCount; i++) {
/* 2460 */               MaskData md = decorateMaskDataSet.maskData[i];
/* 2461 */               if ((domMaskData == null) || (md.weight > domMaskData.weight)) {
/* 2462 */                 subdomMaskData = domMaskData;
/* 2463 */                 domMaskData = decorateMaskDataSet.maskData[i];
/* 2464 */               } else if ((subdomMaskData == null) || (md.weight > subdomMaskData.weight))
/*      */               {
/* 2466 */                 subdomMaskData = decorateMaskDataSet.maskData[i];
/*      */               }
/*      */ 
/*      */             }
/*      */ 
/* 2473 */             int domIndex = 0;
/* 2474 */             int subdomIndex = 0;
/* 2475 */             int[] smoothData = this.gaussian.getBufferIntBGR();
/* 2476 */             for (int dy = Math.max(0, y - range); dy <= dyStop; dy++) {
/* 2477 */               for (dx = windowLeft; dx <= windowRight; dx++) {
/* 2478 */                 Trap trap = this.traps[labelData[(dy * labelPitch + dx)]];
/* 2479 */                 SuperRegion region = trap.getHighestSuperRegion();
/* 2480 */                 if (decoratedData[(dy * labelPitch + dx)] == 0)
/* 2481 */                   mask = region.getMask();
/* 2482 */                 else mask = region.getDecorateMask();
/* 2483 */                 if (mask == domMaskData.mask)
/* 2484 */                   domSamples[(domIndex++)] = smoothData[(dy * pitch + dx)];
/* 2485 */                 else if (mask == subdomMaskData.mask) {
/* 2486 */                   subdomSamples[(subdomIndex++)] = smoothData[(dy * pitch + dx)];
/*      */                 }
/*      */               }
/*      */             }
/*      */ 
/* 2491 */             assert (domIndex == domMaskData.weight);
/* 2492 */             assert (subdomIndex == subdomMaskData.weight);
/*      */ 
/* 2495 */             int domAverage = averageColour3(domSamples, domIndex);
/* 2496 */             int subdomAverage = averageColour3(subdomSamples, subdomIndex);
/*      */ 
/* 2498 */             decompose(originalColour, domAverage, subdomAverage, decomposition);
/*      */ 
/* 2502 */             int original = originalColour;
/*      */ 
/* 2507 */             int bright1 = (int)Colour.getIntensity(domAverage);
/* 2508 */             int bright2 = (int)Colour.getIntensity(subdomAverage);
/*      */             int weight;
/* 2510 */             if ((decomposition.diff < 5) || (subdomMaskData.weight < 3) || (subdomAverage == 0))
/*      */             {
/* 2515 */               weight = domMaskData.weight * 256 / (domMaskData.weight + subdomMaskData.weight);
/*      */             }
/*      */             else
/*      */             {
/* 2519 */               if ((domMaskData.mask == null) && (decomposition.f1 > 0)) {
/* 2520 */                 original = Colour.adjust(original, domAverage, subdomAverage, decomposition.f2);
/*      */               }
/*      */ 
/* 2523 */               if ((subdomMaskData.mask == null) && (decomposition.f2 > 0)) {
/* 2524 */                 original = Colour.adjust(original, subdomAverage, domAverage, decomposition.f1);
/*      */               }
/*      */ 
/* 2528 */               weight = decomposition.f1;
/*      */             }
/*      */ 
/* 2533 */             int component1 = original;
/* 2534 */             if (domMaskData.mask != null) {
/* 2535 */               component1 = domMaskData.mask.getRecolour(bright1);
/*      */             }
/* 2537 */             int component2 = original;
/* 2538 */             if (subdomMaskData.mask != null) {
/* 2539 */               component2 = subdomMaskData.mask.getRecolour(bright2);
/*      */             }
/*      */ 
/* 2542 */             if ((domMaskData.mask != null) && (subdomMaskData.mask != null) && ((domMaskData.mask.equals(subdomMaskData.mask)) || (component1 == component2)))
/*      */             {
/* 2546 */               this.recolourData[(y * pitch + x)].weight = 255;
/* 2547 */               this.recolourData[(y * pitch + x)].component1 = domMaskData.mask.getRecolour((int)Colour.getIntensity(originalColour));
/*      */ 
/* 2549 */               this.recolourData[(y * pitch + x)].maskA = mask;
/* 2550 */               this.recolourData[(y * pitch + x)].maskB = null;
/*      */             }
/*      */             else {
/* 2553 */               weight = Math.max(Math.min(254, weight), 1);
/* 2554 */               this.recolourData[(y * pitch + x)].weight = weight;
/* 2555 */               this.recolourData[(y * pitch + x)].component1 = component1;
/* 2556 */               this.recolourData[(y * pitch + x)].component2 = component2;
/* 2557 */               this.recolourData[(y * pitch + x)].maskA = domMaskData.mask;
/* 2558 */               this.recolourData[(y * pitch + x)].maskB = subdomMaskData.mask;
/*      */             }
/* 2560 */             this.recolourData[(y * pitch + x)].painted = true;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 2566 */     int recolourFilterSize = 3;
/* 2567 */     int[] samples = new int[recolourFilterSize * recolourFilterSize];
/* 2568 */     range = recolourFilterSize / 2;
/* 2569 */     for (int y = this.dirty.top; y <= this.dirty.bottom; y++) {
/* 2570 */       for (int x = this.dirty.left; x < this.dirty.right; x++) {
/* 2571 */         int idx = y * pitch + x;
/* 2572 */         RecolourData data = this.recolourData[idx];
/*      */ 
/* 2574 */         if ((data.weight != 255) && (decoratedData[(y * labelPitch + x)] != 1)) {
/* 2575 */           int weight = data.weight;
/* 2576 */           if (this.enableRecolourMedianFilter) {
/* 2577 */             int sampleCount = 0;
/* 2578 */             int stopX = Math.min(x + range, width - 1);
/* 2579 */             int stopY = Math.min(y + range, height - 1);
/* 2580 */             for (int iy = Math.max(0, y - range); iy < stopY; iy++) {
/* 2581 */               for (int ix = Math.max(0, x - range); ix < stopX; ix++) {
/* 2582 */                 RecolourData sampleData = this.recolourData[(iy * pitch + ix)];
/*      */ 
/* 2584 */                 if ((!sampleData.painted) && (data.maskA == null))
/* 2585 */                   samples[(sampleCount++)] = 255;
/* 2586 */                 else if ((!sampleData.painted) && (data.maskA != null))
/*      */                 {
/* 2588 */                   samples[(sampleCount++)] = 0;
/* 2589 */                 } else if ((data.maskA == sampleData.maskA) || (data.maskB == sampleData.maskB))
/*      */                 {
/* 2591 */                   samples[(sampleCount++)] = sampleData.weight;
/* 2592 */                 } else if ((data.maskA == sampleData.maskB) || (data.maskB == sampleData.maskA))
/*      */                 {
/* 2594 */                   samples[(sampleCount++)] = (255 - sampleData.weight);
/*      */                 }
/*      */               }
/*      */ 
/*      */             }
/*      */ 
/* 2600 */             Arrays.sort(samples, 0, sampleCount - 1);
/* 2601 */             weight = (samples[(sampleCount / 2)] + samples[(sampleCount / 2 + 1)]) / 2;
/*      */           }
/*      */ 
/* 2604 */           this.recolourFilteredWeight[idx] = weight;
/*      */ 
/* 2606 */           paintData[idx] = Colour.blend(data.component1, data.component2, weight);
/*      */         }
/*      */         else
/*      */         {
/* 2610 */           paintData[idx] = data.component1;
/* 2611 */           this.recolourFilteredWeight[idx] = 255;
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2617 */     Rectangle rect = this.dirty.getRectangle();
/* 2618 */     this.dirty.clear();
/*      */ 
/* 2620 */     progress();
/* 2621 */     return rect;
/*      */   }
/*      */ 
/*      */   private static void decompose(int blend, int c1, int c2, Decomposition result)
/*      */   {
/* 2626 */     int r1 = c1 & 0xFF;
/* 2627 */     int r2 = c2 & 0xFF;
/* 2628 */     int g1 = c1 >> 8 & 0xFF;
/* 2629 */     int g2 = c2 >> 8 & 0xFF;
/* 2630 */     int b1 = c1 >> 16 & 0xFF;
/* 2631 */     int b2 = c2 >> 16 & 0xFF;
/* 2632 */     int l1 = (int)Colour.getIntensity(c1);
/* 2633 */     int l2 = (int)Colour.getIntensity(c2);
/*      */ 
/* 2635 */     int diffR = Math.abs(r2 - r1);
/* 2636 */     int diffG = Math.abs(g2 - g1);
/* 2637 */     int diffB = Math.abs(b2 - b1);
/* 2638 */     int diffI = Math.abs(l2 - l1);
/*      */ 
/* 2640 */     int currentDiff = 0;
/* 2641 */     int x = 0;
/* 2642 */     int y = 0;
/* 2643 */     int z = 0;
/*      */ 
/* 2645 */     if (diffR > currentDiff) {
/* 2646 */       x = r1;
/* 2647 */       y = r2;
/* 2648 */       z = blend & 0xFF;
/* 2649 */       currentDiff = diffR;
/*      */     }
/* 2651 */     if (diffG > currentDiff) {
/* 2652 */       x = g1;
/* 2653 */       y = g2;
/* 2654 */       z = blend >> 8 & 0xFF;
/* 2655 */       currentDiff = diffG;
/*      */     }
/* 2657 */     if (diffB > currentDiff) {
/* 2658 */       x = b1;
/* 2659 */       y = b2;
/* 2660 */       z = blend >> 16 & 0xFF;
/* 2661 */       currentDiff = diffB;
/*      */     }
/*      */ 
/* 2664 */     if (diffI > currentDiff) {
/* 2665 */       x = l1;
/* 2666 */       y = l2;
/* 2667 */       z = (int)Colour.getIntensity(blend);
/* 2668 */       currentDiff = diffI;
/*      */     }
/*      */ 
/* 2671 */     if (currentDiff <= 2) {
/* 2672 */       result.f1 = 0;
/* 2673 */       result.f2 = 255;
/* 2674 */       result.diff = currentDiff;
/* 2675 */       return;
/*      */     }
/*      */ 
/* 2679 */     if (x < y)
/* 2680 */       z = Math.max(Math.min(z, y), x);
/*      */     else {
/* 2682 */       z = Math.max(Math.min(z, x), y);
/*      */     }
/*      */ 
/* 2686 */     result.f1 = ((z - y) * 256 / (x - y));
/* 2687 */     result.f2 = (256 - result.f1);
/* 2688 */     result.diff = currentDiff;
/*      */   }
/*      */ 
/*      */   private static int averageColour(int[] samples, int count)
/*      */   {
/* 2705 */     if (count == 0) {
/* 2706 */       return 0;
/*      */     }
/*      */ 
/* 2709 */     int[] magSamples = new int[count];
/* 2710 */     for (int i = 0; i < count; i++) {
/* 2711 */       magSamples[i] = Colour.getMagnitude(samples[i]);
/*      */     }
/*      */ 
/* 2714 */     int[] magSamplesUnsorted = new int[count];
/* 2715 */     System.arraycopy(magSamples, 0, magSamplesUnsorted, 0, count);
/*      */ 
/* 2718 */     Arrays.sort(magSamples);
/*      */ 
/* 2721 */     int magMedian = magSamples[(count / 2)];
/*      */ int i ;
/* 2725 */     for ( i = 0; (i < count) && 
/* 2726 */       (magSamplesUnsorted[i] != magMedian); i++);
/* 2730 */     return samples[i];
/*      */   }
/*      */ 
/*      */   private static int averageColour2(int[] samples, int count)
/*      */   {
/* 2737 */     int size = count;
/* 2738 */     boolean[] in = new boolean[count];
/* 2739 */     for (int i = 0; i < in.length; i++) {
/* 2740 */       in[i] = true;
/*      */     }
/*      */ 
/* 2743 */     int sumR = 0;
/* 2744 */     int sumG = 0;
/* 2745 */     int sumB = 0;
/* 2746 */     for (int i = 0; i < count; i++) {
/* 2747 */       sumR += (samples[i] & 0xFF);
/* 2748 */       sumG += (samples[i] >> 8 & 0xFF);
/* 2749 */       sumB += (samples[i] >> 16 & 0xFF); } 
/*      */ boolean done = false;
/*      */     int avgR;
/*      */     int avgG;
/*      */     int avgB;
/*      */     do { avgR = sumR / size;
/* 2758 */       avgG = sumG / size;
/* 2759 */       avgB = sumB / size;
/*      */ 
/* 2761 */       int avgDist = 0;
/* 2762 */       int maxDist = -1;
/* 2763 */       int outlier = 0;
/* 2764 */       int sz = count;
/* 2765 */       for (int i = 0; i < sz; i++) {
/* 2766 */         if (in[i] ) {
/* 2767 */           int dist = Colour.getMagnitude(samples[i]) - (avgR + avgG + avgB);
/*      */ 
/* 2769 */           avgDist += dist;
/* 2770 */           if (dist > maxDist) {
/* 2771 */             outlier = i;
/* 2772 */             maxDist = dist;
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/* 2777 */       if (maxDist > 30) {
/* 2778 */         in[outlier] = false;
/* 2779 */         size--;
/*      */ 
/* 2781 */         sumR -= (samples[outlier] & 0xFF);
/* 2782 */         sumG -= (samples[outlier] >> 8 & 0xFF);
/* 2783 */         sumB -= (samples[outlier] >> 16 & 0xFF);
/*      */       } else {
/* 2785 */         done = true;
/*      */       } }
/* 2787 */     while ((!done) && (size > 5));
/*      */ 
/* 2789 */     assert ((avgR >= 0) && (avgR <= 255));
/* 2790 */     assert ((avgG >= 0) && (avgG <= 255));
/* 2791 */     assert ((avgB >= 0) && (avgB <= 255));
/*      */ 
/* 2793 */     return avgR | avgG << 8 | avgB << 16;
/*      */   }
/*      */ 
/*      */   private static int averageColour3(int[] samples, int count)
/*      */   {
/* 2800 */     if (count == 0) {
/* 2801 */       return 0;
/*      */     }
/*      */ 
/* 2804 */     int[] samplesR = new int[count];
/* 2805 */     int[] samplesG = new int[count];
/* 2806 */     int[] samplesB = new int[count];
/* 2807 */     for (int i = 0; i < count; i++) {
/* 2808 */       samples[i] &= 255;
/* 2809 */       samplesG[i] = (samples[i] >> 8 & 0xFF);
/* 2810 */       samplesB[i] = (samples[i] >> 16 & 0xFF);
/*      */     }
/*      */ 
/* 2813 */     Arrays.sort(samplesR);
/* 2814 */     Arrays.sort(samplesG);
/* 2815 */     Arrays.sort(samplesB);
/*      */ 
/* 2817 */     int avgR = samplesR[(count / 2)];
/* 2818 */     int avgG = samplesG[(count / 2)];
/* 2819 */     int avgB = samplesB[(count / 2)];
/* 2820 */     return avgR | avgG << 8 | avgB << 16;
/*      */   }
/*      */ 
/*      */   public final Trap getTrap(int x, int y)
/*      */   {
/* 2830 */     int[] labelData = this.labelled.getBufferInteger();
/* 2831 */     int labelPitch = this.labelled.getPitch();
/* 2832 */     return this.traps[labelData[(y * labelPitch + x)]];
/*      */   }
/*      */ 
/*      */   public final Trap getTrap(int index)
/*      */   {
/* 2839 */     int[] labelData = this.labelled.getBufferInteger();
/* 2840 */     return this.traps[labelData[index]];
/*      */   }
/*      */ 
/*      */   public final Trap getTrapWithLabel(int label)
/*      */   {
/* 2847 */     return this.traps[label];
/*      */   }
/*      */ 
/*      */   public final SuperRegion getRegionForIndex(int labelIndex)
/*      */   {
/* 2854 */     int[] labelData = this.labelled.getBufferInteger();
/* 2855 */     return this.traps[labelData[labelIndex]].getHighestSuperRegion();
/*      */   }
/*      */ 
/*      */   public Node findNearestNode(Point p)
/*      */   {
/* 2863 */     int index = this.labelled.getNearestPixel(p.x, p.y);
/* 2864 */     Trap t = this.traps[index];
/* 2865 */     return t.findNearestNode(p.x, p.y);
/*      */   }
/*      */ 
/*      */   public Point findNearestNode(int x, int y) {
/* 2869 */     int index = this.labelled.getNearestPixel(x, y);
/* 2870 */     Trap t = this.traps[index];
/* 2871 */     Node n = t.findNearestNode(x, y);
/* 2872 */     return new Point(n.x, n.y);
/*      */   }
/*      */ 
/*      */   public boolean pointInImage(Point p)
/*      */   {
/* 2879 */     return (p.x >= 0) && (p.x < this.labelled.getWidth()) && (p.y >= 0) && (p.y < this.labelled.getHeight());
/*      */   }
/*      */ 
/*      */   public ImageColour getOriginalImage()
/*      */   {
/* 2891 */     return this.original;
/*      */   }
/*      */ 
/*      */   public ImageColour getPaintedImage()
/*      */   {
/* 2899 */     return this.painted;
/*      */   }
/*      */ 
/*      */   public ImageLabelled getLabelledImage() {
/* 2903 */     return this.labelled;
/*      */   }
/*      */ 
/*      */   public ImageFillMask getFillMask() {
/* 2907 */     return this.fillMask;
/*      */   }
/*      */ 
/*      */   public ImageDecorate getDecorated() {
/* 2911 */     return this.decorated;
/*      */   }
/*      */ 
/*      */   public Trap[] getTraps() {
/* 2915 */     return this.traps;
/*      */   }
/*      */ 
/*      */   void setFillMask(ImageFillMask fillMask)
/*      */   {
/* 2922 */     this.fillMask = fillMask;
/*      */   }
/*      */ 
/*      */   public ImageColour getIntermediateImage(int diagnostic)
/*      */   {
/*      */     try
/*      */     {
/* 2933 */       switch (diagnostic) {
/*      */       case 1:
/* 2935 */         return this.median;
/*      */       case 2:
/* 2937 */         return this.gaussian;
/*      */       case 3:
/* 2939 */         if (this.gradient != null) {
/* 2940 */           return this.gradient.createColourImage();
/*      */         }
/* 2942 */         return null;
/*      */       case 4:
/* 2944 */         if (this.direction != null) {
/* 2945 */           return new ImageColour(this.direction.getAWTImage());
/*      */         }
/* 2947 */         return null;
/*      */       case 5:
/* 2949 */         if (this.labelled != null) {
/* 2950 */           return new ImageColour(this.labelled.getAWTImage());
/*      */         }
/* 2952 */         return null;
/*      */       case 6:
/* 2954 */         return createNodeColourImage();
/*      */       case 7:
/* 2956 */         return createEdgeColourImage();
/*      */       case 8:
/* 2958 */         return createGradientVectorColourImage();
/*      */       case 9:
/* 2960 */         return createBoundingBoxColourImage();
/*      */       case 10:
/* 2962 */         if (this.fillMask != null) {
/* 2963 */           return new ImageColour(this.fillMask.getAWTImage());
/*      */         }
/* 2965 */         return null;
/*      */       case 11:
/* 2967 */         return createStatusColourImage();
/*      */       case 12:
/* 2969 */         return createRecolourWeightColourImage(false);
/*      */       case 13:
/* 2971 */         return createRecolourWeightColourImage(true);
/*      */       }
/* 2973 */       return null;
/*      */     } catch (PaintExplorerFileFormatException e) {
/*      */     }
/* 2976 */     return null;
/*      */   }
/*      */ 
/*      */   private ImageColour createNodeColourImage()
/*      */   {
/* 2981 */     ImageColour img = (ImageColour)this.original.createCompatibleImage();
/*      */ 
/* 2983 */     img.fill(0);
/* 2984 */     int[] imgData = img.getBufferIntBGR();
/* 2985 */     int width = img.getWidth();
/* 2986 */     int height = img.getHeight();
/* 2987 */     int pitch = img.getPitch();
/*      */ 
/* 2989 */     for (Iterator it = this.nodes.iterator(); it.hasNext(); ) {
/* 2990 */       Node n = (Node)it.next();
/* 2991 */       if ((n.x < width) && (n.y < height))
/* 2992 */         imgData[(n.y * pitch + n.x)] = 16777215;
/* 2993 */       else if (n.y >= height)
/* 2994 */         imgData[((height - 1) * pitch + n.x)] = 255;
/* 2995 */       else if (n.x >= width) {
/* 2996 */         imgData[(n.y * pitch + width - 1)] = 65280;
/*      */       }
/*      */     }
/*      */ 
/* 3000 */     return img;
/*      */   }
/*      */ 
/*      */   private ImageColour createEdgeColourImage() {
/* 3004 */     ImageColour img = (ImageColour)this.original.createCompatibleImage();
/*      */ 
/* 3006 */     img.fill(0);
/* 3007 */     int[] imgData = img.getBufferIntBGR();
/* 3008 */     int width = img.getWidth();
/* 3009 */     int height = img.getHeight();
/* 3010 */     int pitch = img.getPitch();
/*      */ 
/* 3012 */     for (Iterator it = this.edges.iterator(); it.hasNext(); ) {
/* 3013 */       Edge e = (Edge)it.next();
/*      */ 
/* 3016 */       if (e.getCrackSequence() != null) {
/* 3017 */         Iterator jt = e.getCrackSequence().iterator();
/* 3018 */         while (jt.hasNext()) {
/* 3019 */           Crack c = (Crack)jt.next();
/* 3020 */           if ((c.y1 >= 0) && (c.x1 >= 0) && (c.y1 < height - 1) && (c.x1 < width - 1))
/*      */           {
/* 3022 */             imgData[(c.y1 * pitch + c.x1)] = 12632256;
/*      */           }
/* 3024 */           if ((c.y2 >= 0) && (c.x2 >= 0) && (c.y2 < height - 1) && (c.x2 < width - 1))
/*      */           {
/* 3026 */             imgData[(c.y2 * pitch + c.x2)] = 7368816;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 3032 */     return img;
/*      */   }
/*      */ 
/*      */   private ImageColour createGradientVectorColourImage() {
/* 3036 */     ImageColour img = (ImageColour)this.original.createCompatibleImage();
/*      */ 
/* 3038 */     img.fill(0);
/* 3039 */     int[] imgData = img.getBufferIntBGR();
/* 3040 */     int width = img.getWidth();
/* 3041 */     int height = img.getHeight();
/* 3042 */     int pitch = img.getPitch();
/*      */ 
/* 3044 */     for (Iterator it = this.edges.iterator(); it.hasNext(); ) {
/* 3045 */       Edge e = (Edge)it.next();
/*      */ 
/* 3047 */       int colour = (int)((8.0F - e.getCost()) * 32.0F) & 0xFF;
/* 3048 */       colour = colour | colour << 8 | colour << 16;
/*      */ 
/* 3051 */       if (e.getGradientVector() != null) {
/* 3052 */         Iterator jt = e.getGradientVector().iterator();
/* 3053 */         while (jt.hasNext()) {
/* 3054 */           Point p = (Point)jt.next();
/* 3055 */           if ((p.x < width) && (p.y < height)) {
/* 3056 */             imgData[(p.y * pitch + p.x)] = colour;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 3062 */     return img;
/*      */   }
/*      */ 
/*      */   public ImageColour createBoundingBoxColourImage() throws PaintExplorerFileFormatException
/*      */   {
/* 3067 */     ImageInteger img = (ImageInteger)this.labelled.createCompatibleImage();
/* 3068 */     int[] imgData = img.getBufferInteger();
/* 3069 */     int pitch = img.getPitch();
/* 3070 */     for (int i = 0; i < this.traps.length; i++) {
/* 3071 */       Area a = this.traps[i].getArea();
/*      */ 
/* 3074 */       int y = a.top;
/* 3075 */       for (int x = a.left; x <= a.right; x++) {
/* 3076 */         imgData[(y * pitch + x)] = i;
/*      */       }
/*      */ 
/* 3079 */       y = a.bottom;
int x;
/* 3080 */       for (x = a.left; x <= a.right; x++) {
/* 3081 */         imgData[(y * pitch + x)] = i;
/*      */       }
/*      */ 
/* 3084 */       x = a.left;
/* 3085 */       for (y = a.top; y <= a.bottom; y++) {
/* 3086 */         imgData[(y * pitch + x)] = i;
/*      */       }
/*      */ 
/* 3089 */       x = a.right;
/* 3090 */       for (y = a.top; y <= a.bottom; y++) {
/* 3091 */         imgData[(y * pitch + x)] = i;
/*      */       }
/*      */     }
/* 3094 */     return new ImageColour(img.getAWTImage());
/*      */   }
/*      */ 
/*      */   public ImageColour createSuperRegionColourImage(int level)
/*      */     throws PaintExplorerFileFormatException
/*      */   {
/* 3103 */     ImageInteger img = (ImageInteger)this.labelled.createCompatibleImage();
/* 3104 */     int[] imgData = img.getBufferInteger();
/* 3105 */     int[] labelData = this.labelled.getBufferInteger();
/* 3106 */     int pitch = img.getPitch();
/* 3107 */     for (int i = 0; i < imgData.length; i++) {
/* 3108 */       AbstractRegion region = this.traps[labelData[i]];
/* 3109 */       for (int j = 1; (region.getSuperRegion() != null) && (j < level); j++) {
/* 3110 */         region = region.getSuperRegion();
/*      */       }
/*      */ 
/* 3114 */       imgData[i] = region.hashCode();
/*      */     }
/*      */ 
/* 3117 */     return new ImageColour(img.getAWTImage());
/*      */   }
/*      */ 
/*      */   public ImageColour createStatusColourImage() throws PaintExplorerFileFormatException
/*      */   {
/* 3122 */     ImageByte img = (ImageByte)this.fillMask.createCompatibleImage();
/* 3123 */     byte[] imgData = img.getBufferByte();
/* 3124 */     int[] labelData = this.labelled.getBufferInteger();
/* 3125 */     int pitch = img.getPitch();
/* 3126 */     for (int i = 0; i < imgData.length; i++) {
/* 3127 */       AbstractRegion region = this.traps[labelData[i]];
/* 3128 */       if (region.getHighestSuperRegion().getMask() != null) {
/* 3129 */         imgData[i] = 1;
/*      */       }
/*      */     }
/* 3132 */     return new ImageColour(img.getAWTImage());
/*      */   }
/*      */ 
/*      */   public ImageColour createRecolourWeightColourImage(boolean filtered) throws PaintExplorerFileFormatException
/*      */   {
/* 3137 */     ImageFloat img = new ImageFloat(this.original.getWidth(), this.original.getHeight());
/*      */ 
/* 3139 */     float[] imgData = img.getBufferFloat();
/* 3140 */     if (filtered) {
/* 3141 */       for (int i = 0; i < imgData.length; i++)
/* 3142 */         imgData[i] = (this.recolourFilteredWeight[i] / 255.0F);
/*      */     }
/*      */     else {
/* 3145 */       for (int i = 0; i < imgData.length; i++) {
/* 3146 */         imgData[i] = (this.recolourData[i].weight / 255.0F);
/*      */       }
/*      */     }
/* 3149 */     return new ImageColour(img.getAWTImage());
/*      */   }
/*      */ 
/*      */   private void preprocess()
/*      */   {
/* 3159 */     progress("PROGRESS_PREPROCESS", 70);
/*      */ 
/* 3161 */     ImageColour gaussianSource = this.original;
/*      */ 
/* 3163 */     if (this.enableMedianFilter) {
/* 3164 */       log("Creating median");
/* 3165 */       this.median = ((ImageColour)this.original.createCompatibleImage());
/* 3166 */       new MedianFilter().filter(this.original, this.median);
/* 3167 */       gaussianSource = this.median;
/*      */     }
/* 3169 */     progress(10);
/*      */ 
/* 3171 */     log("Creating gaussian");
/* 3172 */     this.gaussian = ((ImageColour)gaussianSource.createCompatibleImage());
/* 3173 */     GaussianFilter.getSmall().filter(gaussianSource, this.gaussian);
/*      */ 
/* 3175 */     if (!this.keepIntermediateImages) {
/* 3176 */       this.median = null;
/*      */     }
/*      */ 
/* 3179 */     progress(15);
/*      */ 
/* 3181 */     log("Creating intensity");
/* 3182 */     ImageFloat sobelSource = this.gaussian.createIntensityImage();
/*      */ 
/* 3185 */     progress(20);
/*      */ 
/* 3187 */     log("Creating sobel");
/* 3188 */     this.gradient = ((ImageFloat)sobelSource.createCompatibleImage());
/* 3189 */     new SobelFilter().filter(sobelSource, this.gradient);
/*      */ 
/* 3191 */     if (!this.keepIntermediateImages) {
/* 3192 */       sobelSource = null;
/*      */     }
/*      */ 
/* 3195 */     progress(25);
/*      */ 
/* 3197 */     log("Creating direction");
/* 3198 */     this.direction = Toboggan.createDirectionMap(this.gradient);
/* 3199 */     log("Creating labelled");
/* 3200 */     this.labelled = Toboggan.createLabelledImage(this.direction);
/*      */ 
/* 3202 */     if (!this.keepIntermediateImages) {
/* 3203 */       this.direction = null;
/*      */     }
/*      */ 
/* 3206 */     progress(30);
/*      */ 
/* 3209 */     this.traps = new Trap[this.labelled.getLabelCount()];
/* 3210 */     for (int i = 0; i < this.traps.length; i++) {
/* 3211 */       this.traps[i] = new Trap();
/*      */     }
/*      */ 
/* 3214 */     progress(40);
/* 3215 */     log("... " + this.edges.size() + " edges");
/* 3216 */     log("Creating trap regions");
/* 3217 */     createTrapRegions();
/*      */ 
/* 3219 */     log("Creating nodes");
/* 3220 */     createNodes();
/*      */ 
/* 3222 */     progress(50);
/* 3223 */     log("... " + this.nodes.size() + " nodes");
/* 3224 */     log("Creating edges");
/* 3225 */     createEdges(this.nodes);
/*      */ 
/* 3227 */     progress(60);
/* 3228 */     log("Creating hierarchy");
/* 3229 */     createHierarchy();
/*      */ 
/* 3231 */     progress(70);
/* 3232 */     log("Creating fill mask");
/* 3233 */     this.fillMask = new ImageFillMask(this.gradient, 50.0F);
/*      */ 
/* 3235 */     this.fillMaskDecorate = this.fillMask.clone();
/*      */ 
/* 3237 */     if (this.enableDecorate) {
/* 3238 */       progress(80);
/* 3239 */       log("Creating decorated");
/* 3240 */       this.decorated = new ImageDecorate(this.fillMask.width, this.fillMask.height);
/*      */     }
/*      */ 
/* 3243 */     log("Done preprocessing");
/*      */ 
/* 3245 */     for (Iterator it = this.nodes.iterator(); it.hasNext(); ) {
/* 3246 */       log(((Node)it.next()).toString());
/*      */     }
/*      */ 
/* 3249 */     this.recolourData = new RecolourData[this.labelled.getBufferInteger().length];
/* 3250 */     for (int i = 0; i < this.recolourData.length; i++) {
/* 3251 */       this.recolourData[i] = new RecolourData();
/*      */     }
/* 3253 */     this.recolourFilteredWeight = new int[this.recolourData.length];
/*      */ 
/* 3255 */     progress();
/*      */   }
/*      */ 
/*      */   private void createNodes()
/*      */   {
/* 3263 */     int width = this.labelled.getWidth();
/* 3264 */     int height = this.labelled.getHeight();
/* 3265 */     int pitch = this.labelled.getPitch();
/* 3266 */     int[] data = this.labelled.getBufferInteger();
/*      */ 
/* 3268 */     int[] abutting = new int[4];
/*      */ 
/* 3271 */     for (int y = 0; y < height - 1; y++) {
/* 3272 */       for (int x = 0; x < width - 1; x++)
/*      */       {
/* 3277 */         int a = data[(y * pitch + x)];
/* 3278 */         int b = data[(y * pitch + x + 1)];
/* 3279 */         int c = data[((y + 1) * pitch + x)];
/* 3280 */         int d = data[((y + 1) * pitch + x + 1)];
/*      */ 
/* 3283 */         if ((a != b) || (c != d))
/*      */         {
/* 3287 */           abutting[0] = a;
/* 3288 */           int numAbutting = 1;
/* 3289 */           if (a != b) {
/* 3290 */             abutting[1] = b;
/* 3291 */             numAbutting++;
/*      */           }
/* 3293 */           if ((a != c) && (b != c)) {
/* 3294 */             abutting[numAbutting] = c;
/* 3295 */             numAbutting++;
/*      */           }
/* 3297 */           if ((a != d) && (b != d) && (c != d)) {
/* 3298 */             abutting[numAbutting] = d;
/* 3299 */             numAbutting++;
/*      */           }
/*      */ 
/* 3302 */           if (numAbutting > 2) {
/* 3303 */             Node node = new Node(x + 1, y + 1, numAbutting, false);
/* 3304 */             for (int i = 0; i < numAbutting; i++) {
/* 3305 */               Trap trap = this.traps[abutting[i]];
/* 3306 */               node.addTrap(trap);
/* 3307 */               trap.addNode(node);
/*      */             }
/* 3309 */             this.nodes.add(node);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 3315 */     Node lastNodeTop = null; Node lastNodeBottom = null;
/* 3316 */     for (int x = 0; x < width - 1; x++)
/*      */     {
/* 3318 */       int y = 0;
/*      */ 
/* 3320 */       int a = data[(y * pitch + x)];
/* 3321 */       int b = data[(y * pitch + x + 1)];
/* 3322 */       if (a != b) {
/* 3323 */         Node node = new Node(x + 1, 0, 2, true);
/* 3324 */         node.addTrap(this.traps[a]);
/* 3325 */         node.addTrap(this.traps[b]);
/* 3326 */         this.traps[a].addNode(node);
/* 3327 */         this.traps[b].addNode(node);
/* 3328 */         this.nodes.add(node);
/* 3329 */         if (lastNodeTop != null) {
/* 3330 */           if (lastNodeTop.hasTrap(this.traps[a]))
/* 3331 */             createBorderHorizontalEdge(lastNodeTop, node, this.traps[a]);
/*      */           else {
/* 3333 */             createBorderHorizontalEdge(lastNodeTop, node, this.traps[b]);
/*      */           }
/*      */         }
/* 3336 */         lastNodeTop = node;
/*      */       }
/*      */ 
/* 3340 */       y = height - 1;
/*      */ 
/* 3342 */       a = data[(y * pitch + x)];
/* 3343 */       b = data[(y * pitch + x + 1)];
/* 3344 */       if (a != b) {
/* 3345 */         Node node = new Node(x + 1, height, 2, true);
/* 3346 */         node.addTrap(this.traps[a]);
/* 3347 */         node.addTrap(this.traps[b]);
/* 3348 */         this.traps[a].addNode(node);
/* 3349 */         this.traps[b].addNode(node);
/* 3350 */         this.nodes.add(node);
/* 3351 */         if (lastNodeBottom != null) {
/* 3352 */           if (lastNodeBottom.hasTrap(this.traps[a])) {
/* 3353 */             createBorderHorizontalEdge(lastNodeBottom, node, this.traps[a]);
/*      */           }
/*      */           else {
/* 3356 */             createBorderHorizontalEdge(lastNodeBottom, node, this.traps[b]);
/*      */           }
/*      */         }
/*      */ 
/* 3360 */         lastNodeBottom = node;
/*      */       }
/*      */     }
/*      */ 
/* 3364 */     Node lastNodeLeft = null; Node lastNodeRight = null;
/* 3365 */     for (int y = 0; y < height - 1; y++)
/*      */     {
/* 3367 */       int x = 0;
/*      */ 
/* 3369 */       int a = data[(y * pitch + x)];
/* 3370 */       int b = data[((y + 1) * pitch + x)];
/* 3371 */       if (a != b) {
/* 3372 */         Node node = new Node(0, y + 1, 2, true);
/* 3373 */         node.addTrap(this.traps[a]);
/* 3374 */         node.addTrap(this.traps[b]);
/* 3375 */         this.traps[a].addNode(node);
/* 3376 */         this.traps[b].addNode(node);
/* 3377 */         this.nodes.add(node);
/* 3378 */         if (lastNodeLeft != null) {
/* 3379 */           if (lastNodeLeft.hasTrap(this.traps[a]))
/* 3380 */             createBorderVerticalEdge(lastNodeLeft, node, this.traps[a]);
/*      */           else {
/* 3382 */             createBorderVerticalEdge(lastNodeLeft, node, this.traps[b]);
/*      */           }
/*      */         }
/* 3385 */         lastNodeLeft = node;
/*      */       }
/*      */ 
/* 3389 */       x = width - 1;
/*      */ 
/* 3391 */       a = data[(y * pitch + x)];
/* 3392 */       b = data[((y + 1) * pitch + x)];
/* 3393 */       if (a != b) {
/* 3394 */         Node node = new Node(width, y + 1, 2, true);
/* 3395 */         node.addTrap(this.traps[a]);
/* 3396 */         node.addTrap(this.traps[b]);
/* 3397 */         this.traps[a].addNode(node);
/* 3398 */         this.traps[b].addNode(node);
/* 3399 */         this.nodes.add(node);
/* 3400 */         if (lastNodeRight != null) {
/* 3401 */           if (lastNodeRight.hasTrap(this.traps[a]))
/* 3402 */             createBorderVerticalEdge(lastNodeRight, node, this.traps[a]);
/*      */           else {
/* 3404 */             createBorderVerticalEdge(lastNodeRight, node, this.traps[b]);
/*      */           }
/*      */         }
/* 3407 */         lastNodeRight = node;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 3412 */     for (int i = 0; i < this.traps.length; i++)
/* 3413 */       if (this.traps[i].getNodes().size() == 0) {
/* 3414 */         boolean foundNode = false;
/*      */ 
/* 3417 */         for (int y = 1; (y < height - 1) && (!foundNode); y++)
/* 3418 */           for (int x = 1; (x < width - 1) && (!foundNode); x++) {
/* 3419 */             int a = data[(y * pitch + x)];
/* 3420 */             if (a == i)
/*      */             {
/* 3424 */               int b = data[(y * pitch + x - 1)];
/* 3425 */               Node node = new Node(x, y + 1, 2, false);
/* 3426 */               node.addTrap(this.traps[a]);
/* 3427 */               node.addTrap(this.traps[b]);
/* 3428 */               this.traps[a].addNode(node);
/* 3429 */               this.traps[b].addNode(node);
/* 3430 */               this.nodes.add(node);
/* 3431 */               foundNode = true;
/*      */             }
/*      */           }
/*      */       }
/*      */   }
/*      */ 
/*      */   private void createEdges(Collection nodeSet)
/*      */   {
/* 3443 */     int pitch = this.labelled.getPitch();
/* 3444 */     int width = this.labelled.getWidth();
/* 3445 */     int height = this.labelled.getHeight();
/* 3446 */     int[] labelData = this.labelled.getBufferInteger();
/*      */ 		Trap b;
/*      */      Trap a;
/* 3448 */     for (Iterator it = nodeSet.iterator(); it.hasNext(); ) {
/* 3449 */       Node n = (Node)it.next();
/*      */ 
/* 3451 */       if (n.isBorder())
/*      */       {
/* 3454 */         if (n.getEdgeCount() != 3)
/*      */         {
/*      */           int direction;
/* 3461 */           if (n.x == 0) {
/* 3462 */              a = this.traps[labelData[(n.y * pitch + 0)]];
/* 3463 */              b = this.traps[labelData[((n.y - 1) * pitch + 0)]];
/* 3464 */             direction = 2;
/*      */           }
/*      */           else
/*      */           {
/* 3465 */             if (n.x == width) {
/* 3466 */                a = this.traps[labelData[(n.y * pitch + n.x - 1)]];
/* 3467 */                b = this.traps[labelData[((n.y - 1) * pitch + n.x - 1)]];
/* 3468 */               direction = 4;
/*      */             }
/*      */             else
/*      */             {
/* 3469 */               if (n.y == 0) {
/* 3470 */                  a = this.traps[labelData[(0 * pitch + n.x)]];
/* 3471 */                  b = this.traps[labelData[(0 * pitch + n.x - 1)]];
/* 3472 */                 direction = 3;
/*      */               }
/*      */               else
/*      */               {
/* 3473 */                 if (n.y == height) {
/* 3474 */                    a = this.traps[labelData[((n.y - 1) * pitch + n.x)]];
/* 3475 */                    b = this.traps[labelData[((n.y - 1) * pitch + n.x - 1)]];
/* 3476 */                   direction = 1;
/*      */                 } else {
/* 3478 */                   System.err.println(n);
/* 3479 */                   //if ($assertionsDisabled) 
								//	continue;
								throw new AssertionError();
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */           
/* 3483 */           if (!n.hasEdge(a, b)) {
/* 3484 */             createEdge(n, a, b, direction);
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/* 3493 */          a = this.traps[labelData[((n.y - 1) * pitch + n.x - 1)]];
/* 3494 */          b = this.traps[labelData[((n.y - 1) * pitch + n.x)]];
/* 3495 */          Trap c = this.traps[labelData[(n.y * pitch + n.x - 1)]];
/* 3496 */          Trap d = this.traps[labelData[(n.y * pitch + n.x)]];
/*      */ 
/* 3499 */         if ((a != b) && (!n.hasEdge(a, b))) {
/* 3500 */           createEdge(n, a, b, 1);
/*      */         }
/* 3502 */         if ((b != d) && (!n.hasEdge(b, d))) {
/* 3503 */           createEdge(n, b, d, 2);
/*      */         }
/* 3505 */         if ((d != c) && (!n.hasEdge(d, c))) {
/* 3506 */           createEdge(n, d, c, 3);
/*      */         }
/* 3508 */         if ((c != a) && (!n.hasEdge(c, a)))
/* 3509 */           createEdge(n, c, a, 4);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void createEdge(Node n, Trap trapA, Trap trapB, int direction)
/*      */   {
/* 3526 */     Vector cracks = null;
/* 3527 */     int x = n.x;
/* 3528 */     int y = n.y;
/* 3529 */     int width = this.labelled.getWidth();
/* 3530 */     int height = this.labelled.getHeight();
/* 3531 */     int pitch = this.labelled.getPitch();
/* 3532 */     int[] labelData = this.labelled.getBufferInteger();
/* 3533 */     int gradPitch = this.gradient.getPitch();
/* 3534 */     float[] gradData = this.gradient.getBufferFloat();
/* 3535 */     Vector gradientVector = new Vector();
/* 3536 */     float cost = 0.0F;
/*      */ 
/* 3540 */     if (this.createCrackSequence) {
/* 3541 */       cracks = new Vector(8);
/*      */     }
/*      */ 
/* 3544 */     Point lastPoint = new Point(-1, -1);
/* 3545 */     Point crackPoint1 = new Point();
/* 3546 */     Point crackPoint2 = new Point();
/*      */     while (true)
/*      */     {
/* 3558 */       boolean hitBorder = false;
/* 3559 */       switch (direction) {
/*      */       case 1:
/* 3561 */         if (this.createCrackSequence) {
/* 3562 */           cracks.add(new Crack(x - 1, y - 1, x, y - 1));
/*      */         }
/* 3564 */         crackPoint1.setLocation(x - 1, y - 1);
/* 3565 */         crackPoint2.setLocation(x, y - 1);
/* 3566 */         y--;
/* 3567 */         hitBorder = y == 0;
/* 3568 */         break;
/*      */       case 2:
/* 3570 */         if (this.createCrackSequence) {
/* 3571 */           cracks.add(new Crack(x, y - 1, x, y));
/*      */         }
/* 3573 */         crackPoint1.setLocation(x, y - 1);
/* 3574 */         crackPoint2.setLocation(x, y);
/* 3575 */         x++;
/* 3576 */         hitBorder = x == width;
/* 3577 */         break;
/*      */       case 3:
/* 3579 */         if (this.createCrackSequence) {
/* 3580 */           cracks.add(new Crack(x - 1, y, x, y));
/*      */         }
/* 3582 */         crackPoint1.setLocation(x - 1, y);
/* 3583 */         crackPoint2.setLocation(x, y);
/* 3584 */         y++;
/* 3585 */         hitBorder = y == height;
/* 3586 */         break;
/*      */       case 4:
/* 3588 */         if (this.createCrackSequence) {
/* 3589 */           cracks.add(new Crack(x - 1, y - 1, x - 1, y));
/*      */         }
/* 3591 */         crackPoint1.setLocation(x - 1, y - 1);
/* 3592 */         crackPoint2.setLocation(x - 1, y);
/* 3593 */         x--;
/* 3594 */         hitBorder = x == 0;
/* 3595 */         break;
/*      */       default:
/* 3597 */         //if (!$assertionsDisabled) throw new AssertionError();
/* 3598 */         return;
/*      */       }
/*      */ 
/* 3603 */       float v1 = gradData[(crackPoint1.y * gradPitch + crackPoint1.x)];
/* 3604 */       float v2 = gradData[(crackPoint2.y * gradPitch + crackPoint2.x)];
/*      */       float gradient;
/*      */       Point candidate;
/* 3605 */       if (v1 > v2) {
/* 3606 */         candidate = crackPoint1;
/* 3607 */         gradient = v1;
/*      */       } else {
/* 3609 */         candidate = crackPoint2;
/* 3610 */         gradient = v2;
/*      */       }
/*      */ 
/* 3613 */       if (!candidate.equals(lastPoint)) {
/* 3614 */         gradientVector.add(new Point(candidate));
/* 3615 */         float c = 1.0F - gradient;
/* 3616 */         cost += c * c;
/*      */       }
/*      */ 
/* 3620 */       if (hitBorder)
/*      */       {
/*      */         break;
/*      */       }
/*      */ 
/* 3625 */       if ((x == n.x) && (y == n.y))
/*      */       {
/*      */         break;
/*      */       }
/*      */ 
/* 3633 */       Trap a = this.traps[labelData[((y - 1) * pitch + x - 1)]];
/* 3634 */       Trap b = this.traps[labelData[((y - 1) * pitch + x)]];
/* 3635 */       Trap c = this.traps[labelData[(y * pitch + x - 1)]];
/* 3636 */       Trap d = this.traps[labelData[(y * pitch + x)]];
/*      */ 
/* 3639 */       int numAbutting = 1;
/* 3640 */       if (a != b) {
/* 3641 */         numAbutting++;
/*      */       }
/* 3643 */       if ((a != c) && (b != c)) {
/* 3644 */         numAbutting++;
/*      */       }
/* 3646 */       if ((a != d) && (b != d) && (c != d)) {
/* 3647 */         numAbutting++;
/*      */       }
/* 3649 */       if (numAbutting > 2)
/*      */       {
/*      */         break;
/*      */       }
/*      */ 
/* 3654 */       if ((direction != 3) && (((a == trapA) && (b == trapB)) || ((a == trapB) && (b == trapA))))
/*      */       {
/* 3656 */         direction = 1;
/* 3657 */       } else if ((direction != 4) && (((b == trapA) && (d == trapB)) || ((b == trapB) && (d == trapA))))
/*      */       {
/* 3659 */         direction = 2;
/* 3660 */       } else if ((direction != 1) && (((d == trapA) && (c == trapB)) || ((d == trapB) && (c == trapA))))
/*      */       {
/* 3662 */         direction = 3; } else {
/* 3663 */         if ((direction == 2) || (((c != trapA) || (a != trapB)) && ((c != trapB) || (a != trapA))))
/*      */           break;
/* 3665 */         direction = 4;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 3672 */     Node terminator = trapA.getNode(x, y);
/* 3673 */     if (terminator == null) {
/* 3674 */       log("Terminator null for point " + x + ", " + y + " on trap " + trapA);
/*      */     }
/*      */ 
/* 3677 */     assert (terminator != null);
/*      */ 
/* 3680 */     assert ((!n.hasEdge(terminator, trapA, trapB)) && (!terminator.hasEdge(n, trapA, trapB)));
/*      */ 
/* 3684 */     Edge edge = new Edge(n, terminator, trapA, trapB, cracks);
/* 3685 */     edge.setGradientVector(gradientVector);
/* 3686 */     edge.setCost(cost);
/* 3687 */     this.edges.add(edge);
/*      */ 
/* 3690 */     n.addEdge(edge);
/* 3691 */     if (terminator != n) {
/* 3692 */       terminator.addEdge(edge);
/*      */     }
/*      */ 
/* 3696 */     trapA.addNeighbour(trapB);
/* 3697 */     trapB.addNeighbour(trapA);
/*      */   }
/*      */ 
/*      */   private void createBorderVerticalEdge(Node n1, Node n2, Trap trap)
/*      */   {
/* 3705 */     int d = n2.y - n1.y;
/* 3706 */     assert (d > 0);
/*      */ 
/* 3709 */     Vector gradientVector = new Vector(d);
/* 3710 */     Vector crackVector = new Vector(d);
/* 3711 */     int x = n1.x == 0 ? 0 : n1.x - 1;
/* 3712 */     for (int y = n1.y; y <= n2.y; y++) {
/* 3713 */       gradientVector.add(new Point(x, y));
/* 3714 */       crackVector.add(new Crack(n1.x - 1, y, n1.x, y));
/*      */     }
/* 3716 */     Edge edge = new Edge(n1, n2, trap, trap, crackVector);
/* 3717 */     edge.setGradientVector(gradientVector);
/* 3718 */     edge.setCost(d * 0.8F);
/*      */ 
/* 3721 */     this.edges.add(edge);
/* 3722 */     n1.addEdge(edge);
/* 3723 */     n2.addEdge(edge);
/*      */   }
/*      */ 
/*      */   private void createBorderHorizontalEdge(Node n1, Node n2, Trap trap)
/*      */   {
/* 3732 */     int d = n2.x - n1.x;
/* 3733 */     assert (d > 0);
/*      */ 
/* 3735 */     Vector gradientVector = new Vector(d);
/* 3736 */     Vector crackVector = new Vector(d);
/* 3737 */     int y = n1.y == 0 ? 0 : n1.y - 1;
/* 3738 */     for (int x = n1.x; x <= n2.x; x++) {
/* 3739 */       gradientVector.add(new Point(x, y));
/* 3740 */       crackVector.add(new Crack(x, n1.y - 1, x, n1.y));
/*      */     }
/* 3742 */     Edge edge = new Edge(n1, n2, trap, trap, crackVector);
/* 3743 */     edge.setGradientVector(gradientVector);
/* 3744 */     edge.setCost(d * 0.8F);
/*      */ 
/* 3746 */     this.edges.add(edge);
/* 3747 */     n1.addEdge(edge);
/* 3748 */     n2.addEdge(edge);
/*      */   }
/*      */ 
/*      */   private void createTrapRegions()
/*      */   {
/* 3756 */     int[] labelData = this.labelled.getBufferInteger();
/* 3757 */     int[] imageData = this.original.getBufferIntBGR();
/* 3758 */     int width = this.labelled.getWidth();
/* 3759 */     int height = this.labelled.getHeight();
/*      */ 
/* 3761 */     int i = 0;
/* 3762 */     for (int y = 0; y < height; y++)
/* 3763 */       for (int x = 0; x < width; x++) {
/* 3764 */         Trap trap = this.traps[labelData[i]];
/* 3765 */         trap.addSample(imageData[i]);
/* 3766 */         trap.bound(x, y);
/* 3767 */         i++;
/*      */       }
/*      */   }
/*      */ 
/*      */   public void recreateHierarchy()
/*      */   {
/* 3777 */     Mask[] trapMasks = new Mask[this.traps.length];
/*      */ 
/* 3779 */     Mask[] trapMasksDecorate = new Mask[this.traps.length];
/*      */ 
/* 3781 */     AbstractRegion[] trapRegions = new AbstractRegion[this.traps.length];
/*      */ 
/* 3783 */     for (int i = 0; i < this.traps.length; i++) {
/* 3784 */       trapRegions[i] = this.traps[i].getHighestSuperRegion();
/* 3785 */       if (trapRegions[i] != null) {
/* 3786 */         trapMasks[i] = trapRegions[i].getMask();
/* 3787 */         trapMasksDecorate[i] = trapRegions[i].getDecorateMask();
/*      */       }
/* 3789 */       this.traps[i].setSuperRegion(null);
/* 3790 */       this.traps[i].removeAllNeighbours();
/*      */     }
/* 3792 */     log("Cleared trap superregions");
/*      */ 
/* 3795 */     for (Iterator it = this.edges.iterator(); it.hasNext(); ) {
/* 3796 */       Edge e = (Edge)it.next();
/* 3797 */       if (!e.isCut()) {
/* 3798 */         e.getTrapA().addNeighbour(e.getTrapB());
/* 3799 */         e.getTrapB().addNeighbour(e.getTrapA());
/*      */       }
/*      */     }
/*      */ 
/* 3803 */     createHierarchy();
/*      */ 
/* 3806 */     for (int i = 0; i < this.traps.length; i++) {
/* 3807 */       AbstractRegion region = this.traps[i].getHighestSuperRegion();
/* 3808 */       region.setMask(trapMasks[i]);
/* 3809 */       region.setDecorateMask(trapMasksDecorate[i]);
/*      */ 
/* 3811 */       if (trapMasks[i] != null) {
/* 3812 */         trapMasks[i].replaceRegion(trapRegions[i], region);
/*      */       }
/*      */ 
/* 3815 */       if (trapMasksDecorate[i] != null)
/* 3816 */         trapMasksDecorate[i].replaceRegion(trapRegions[i], region);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void createHierarchy()
/*      */   {
/* 3829 */     int levels = this.regionHierarchyLevels;
/* 3830 */     assert (levels >= 1);
/*      */ 
/* 3832 */     Vector regions = new Vector();
/*      */ 
/* 3835 */     for (int i = 0; i < this.traps.length; i++) {
/* 3836 */       regions.add(this.traps[i]);
/*      */     }
/*      */ 
/* 3840 */     for (int level = 1; level <= levels; level++) {
/* 3841 */       log("  Level " + level);
/* 3842 */       log("    Toboggan");
/* 3843 */       SuperRegion freshRegion = new SuperRegion();
/* 3844 */       Vector subRegions = new Vector();
/* 3845 */       Vector higherRegions = new Vector();
/*      */ 
/* 3848 */       for (Iterator itR = regions.iterator(); itR.hasNext(); ) {
/* 3849 */         AbstractRegion drop = (AbstractRegion)itR.next();
/*      */ 
/* 3852 */         while (drop.getSuperRegion() == null) {
/* 3853 */           drop.setSuperRegion(freshRegion);
/* 3854 */           subRegions.add(drop);
/* 3855 */           AbstractRegion best = drop.getLowestCostNeighbour();
/* 3856 */           float bestCost = drop.getLowestCost();
/* 3857 */           if (bestCost > 10.24F) {
/* 3858 */             best = null;
/*      */           }
/* 3860 */           if (best == null)
/*      */           {
/* 3862 */             higherRegions.add(freshRegion);
/* 3863 */             freshRegion = new SuperRegion();
/*      */           } else {
/* 3865 */             drop = best;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 3871 */         for (Iterator itA = subRegions.iterator(); itA.hasNext(); ) {
/* 3872 */           AbstractRegion subRegion = (AbstractRegion)itA.next();
/* 3873 */           SuperRegion superRegion = drop.getSuperRegion();
/* 3874 */           superRegion.merge(subRegion);
/* 3875 */           subRegion.setSuperRegion(superRegion);
/*      */         }
/*      */ 
/* 3878 */         if (drop.getSuperRegion() == freshRegion)
/*      */         {
/* 3880 */           higherRegions.add(freshRegion);
/* 3881 */           freshRegion = new SuperRegion();
/*      */         }
/*      */ 
/* 3885 */         subRegions.clear();
/*      */       }
/*      */ 
/* 3890 */       log("    Find neighbours");
/*      */ 
/* 3893 */       for (Iterator itR = higherRegions.iterator(); itR.hasNext(); ) {
/* 3894 */         SuperRegion higherRegion = (SuperRegion)itR.next();
/*      */ 
/* 3897 */         Iterator itS = higherRegion.getSubRegionsIterator();
/* 3898 */         while (itS.hasNext()) {
/* 3899 */           AbstractRegion subRegion = (AbstractRegion)itS.next();
/*      */ 
/* 3902 */           Iterator itN = subRegion.getNeighboursIterator();
/* 3903 */           while (itN.hasNext()) {
/* 3904 */             AbstractRegion subNeighbour = (AbstractRegion)itN.next();
/*      */ 
/* 3909 */             if (subNeighbour.getSuperRegion() != higherRegion) {
/* 3910 */               higherRegion.addNeighbour(subNeighbour.getSuperRegion());
/*      */             }
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 3918 */       regions.clear();
/* 3919 */       for (Iterator itR = higherRegions.iterator(); itR.hasNext(); ) {
/* 3920 */         regions.add((AbstractRegion)itR.next());
/*      */       }
/* 3922 */       higherRegions.clear();
/*      */     }
/*      */ 
/* 3926 */     log("Cutting super-regions");
/* 3927 */     for (Iterator it = this.edges.iterator(); it.hasNext(); ) {
/* 3928 */       Edge e = (Edge)it.next();
/* 3929 */       if (e.getTrapA().getHighestSuperRegion() != e.getTrapB().getHighestSuperRegion())
/*      */       {
/* 3931 */         e.cut();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public Vector addStraightLineEdge(StraightLineEdge line)
/*      */   {
/* 3949 */     log("PaintExplorer.addStraightLineEdge(" + line + ")");
/*      */ 
/* 3951 */     int[] data = this.labelled.getBufferInteger();
/* 3952 */     int pitch = this.labelled.getPitch();
/* 3953 */     int width = this.labelled.getWidth();
/* 3954 */     int height = this.labelled.getHeight();
/* 3955 */     boolean steep = line.isSteep();
/*      */ 
/* 3957 */     int lastLabel = -1;
/* 3958 */     int newLabel = this.traps.length - 1;
/*      */ 
/* 3964 */     if (line.getNodeA() == line.getNodeB()) {
/* 3965 */       return new Vector();
/*      */     }
/*      */ 
/* 3968 */     log("Line from " + line.getNodeA() + " to " + line.getNodeB());
/*      */ 
/* 3970 */     StackInteger fillQueue = new StackInteger();
/* 3971 */     Set touchedTraps = new HashSet();
/* 3972 */     Set touchedNodes = new HashSet();
/* 3973 */     Area bbox = new Area();
/*      */ 
/* 3993 */     int lineType = 0;
/* 3994 */     int dxdy = (line.getNodeA().x - line.getNodeB().x) * (line.getNodeA().y - line.getNodeB().y);
/*      */ 
/* 3996 */     if ((!steep) && (dxdy < 0))
/* 3997 */       lineType = 1;
/* 3998 */     else if ((!steep) && (dxdy >= 0))
/* 3999 */       lineType = 2;
/* 4000 */     else if ((steep) && (dxdy < 0))
/* 4001 */       lineType = 3;
/* 4002 */     else if ((steep) && (dxdy >= 0)) {
/* 4003 */       lineType = 4;
/*      */     }
/* 4005 */     assert (lineType != 0);
/*      */ 
/* 4009 */     boolean fillLeft = false;
/* 4010 */     boolean fillUp = false;
/* 4011 */     boolean fillRight = false;
/* 4012 */     boolean fillDown = false;
/* 4013 */     switch (lineType) {
/*      */     case 1:
/* 4015 */       fillRight = true;
/* 4016 */       fillDown = true;
/* 4017 */       break;
/*      */     case 2:
/* 4019 */       fillLeft = true;
/* 4020 */       fillDown = true;
/* 4021 */       break;
/*      */     case 3:
/* 4023 */       fillRight = true;
/* 4024 */       fillDown = true;
/* 4025 */       break;
/*      */     case 4:
/* 4027 */       fillRight = true;
/* 4028 */       fillUp = true;
/*      */     }
/*      */ 
/* 4033 */     boolean skipFirst = false;
/* 4034 */     boolean isFirst = true;
/* 4035 */     if ((!steep) && (line.getNodeB().x - line.getNodeA().x < 0))
/* 4036 */       skipFirst = true;
/* 4037 */     else if ((steep) && (line.getNodeB().y - line.getNodeA().y < 0)) {
/* 4038 */       skipFirst = true;
/*      */     }
/*      */ 
/* 4045 */     Vector addedTraps = new Vector();
/* 4046 */     for (Iterator it = line.getCrackSequence().iterator(); it.hasNext(); ) {
/* 4047 */       Crack c = (Crack)it.next();
/* 4048 */       int x = c.x1;
/* 4049 */       int y = c.y1;
/*      */ 
/* 4052 */       if ((x < width) && (y < height))
/*      */       {
/* 4056 */         int label = data[(y * pitch + x)];
/* 4057 */         if (label != lastLabel) {
/* 4058 */           if (label < this.traps.length) {
/* 4059 */             touchedTraps.add(this.traps[label]);
/* 4060 */             bbox.bound(this.traps[label].getArea());
/*      */           }
/*      */ 
/* 4063 */           if (fillQueue.size() > 0)
/*      */           {
/* 4065 */             this.labelled.fillReplace(fillQueue, lastLabel, newLabel);
/*      */           }
/*      */ 
/* 4070 */           if (newLabel != this.traps.length - 1)
/*      */           {
/* 4072 */             Trap t = new Trap();
/* 4073 */             addedTraps.add(t);
/* 4074 */             if (lastLabel < this.traps.length)
/* 4075 */               t.setSuperRegion(this.traps[lastLabel].getSuperRegion());
/*      */             else {
/* 4077 */               t.setSuperRegion(((Trap)addedTraps.get(lastLabel - this.traps.length)).getSuperRegion());
/*      */             }
/*      */           }
/*      */ 
/* 4081 */           newLabel++;
/* 4082 */           fillQueue.clear();
/*      */         }
/*      */ 
/* 4085 */         if ((!isFirst) || (!skipFirst))
/*      */         {
/* 4087 */           data[(y * pitch + x)] = newLabel;
/*      */ 
/* 4089 */           if ((fillUp) && (y > 0) && (data[((y - 1) * pitch + x)] == label)) {
/* 4090 */             fillQueue.pushPair(x, y - 1);
/*      */           }
/* 4092 */           if ((fillLeft) && (x > 0) && (data[(y * pitch + x - 1)] == label)) {
/* 4093 */             fillQueue.pushPair(x - 1, y);
/*      */           }
/* 4095 */           if ((fillDown) && (y < height - 1) && (data[((y + 1) * pitch + x)] == label)) {
/* 4096 */             fillQueue.pushPair(x, y + 1);
/*      */           }
/* 4098 */           if ((fillRight) && (x < width - 1) && (data[(y * pitch + x + 1)] == label)) {
/* 4099 */             fillQueue.pushPair(x + 1, y);
/*      */           }
/*      */         }
/*      */ 
/* 4103 */         isFirst = false;
/* 4104 */         lastLabel = label;
/*      */       }
/*      */     }
/* 4107 */     this.labelled.fillReplace(fillQueue, lastLabel, newLabel);
/*      */ 
/* 4109 */     Trap t = new Trap();
/* 4110 */     addedTraps.add(t);
/* 4111 */     if (lastLabel < this.traps.length)
/* 4112 */       t.setSuperRegion(this.traps[lastLabel].getSuperRegion());
/*      */     else {
/* 4114 */       t.setSuperRegion(((Trap)addedTraps.get(lastLabel - this.traps.length)).getSuperRegion());
/*      */     }
/*      */ 
/* 4119 */     Trap[] newTraps = new Trap[this.traps.length + addedTraps.size()];
/* 4120 */     System.arraycopy(this.traps, 0, newTraps, 0, this.traps.length);
/* 4121 */     System.arraycopy(addedTraps.toArray(), 0, newTraps, this.traps.length, addedTraps.size());
/*      */ 
/* 4124 */     this.traps = newTraps;
/*      */ 
/* 4130 */     Node[] endPoints = new Node[2];
/* 4131 */     endPoints[0] = line.getNodeA();
/* 4132 */     endPoints[1] = line.getNodeB();
/* 4133 */     Trap[] nodeTraps = new Trap[4];
/* 4134 */     for (int s = 0; s < endPoints.length; s++) {
/* 4135 */       for (int j = 0; j <= 1; j++) {
/* 4136 */         for (int k = 0; k <= 1; k++) {
/* 4137 */           int x = endPoints[s].x + k;
/* 4138 */           int y = endPoints[s].y + j;
/* 4139 */           if ((x <= width) && (y <= height))
/*      */           {
/* 4142 */             int numAbutting = findNodeTraps(nodeTraps, x, y);
/* 4143 */             log("Neighbourhood point " + x + ", " + y + " abuts " + numAbutting);
/*      */ 
/* 4145 */             if (numAbutting > 2)
/*      */             {
/* 4151 */               Node n = null;
/* 4152 */               boolean found = false;
/* 4153 */               for (Iterator it = this.nodes.iterator(); it.hasNext(); ) {
/* 4154 */                 n = (Node)it.next();
/* 4155 */                 if ((n.x == x) && (n.y == y)) {
/* 4156 */                   found = true;
/*      */                 }
/*      */ 
/*      */               }
/*      */ 
/* 4161 */               if (!found) {
/* 4162 */                 boolean isBorder = (x == 0) || (y == 0) || (x == width) || (y == height);
/*      */ 
/* 4167 */                 n = new Node(x, y, 0, isBorder);
/* 4168 */                 log("Created neighbourhood " + n);
/*      */ 
/* 4171 */                 this.nodes.add(n);
/*      */               } else {
/* 4173 */                 log("Found " + n + ", no problem.");
/*      */               }
/*      */ 
/* 4177 */               if (n != null) {
/* 4178 */                 touchedNodes.add(n);
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 4187 */     for (Iterator it = touchedTraps.iterator(); it.hasNext(); ) {
/* 4188 */       Trap trap = (Trap)it.next();
/* 4189 */       log("Clear nodes of " + trap);
/* 4190 */       touchedNodes.addAll(trap.getNodes());
/* 4191 */       trap.getNodes().clear();
/*      */     }
/*      */ 
/* 4195 */     Set cutCracks = new HashSet();
/*      */ 
/* 4199 */     for (Iterator it = touchedNodes.iterator(); it.hasNext(); ) {
/* 4200 */       Node node = (Node)it.next();
/* 4201 */       log("Touched " + node);
/* 4202 */       for (Iterator jt = node.getEdges(); jt.hasNext(); )
/*      */       {
/* 4204 */         Edge edge = (Edge)jt.next();
/* 4205 */         if (edge.getNodeA() != node) {
/* 4206 */           edge.getNodeA().removeEdge(edge);
/* 4207 */           log("  affected " + edge.getNodeA());
/*      */         }
/* 4209 */         if (edge.getNodeB() != node) {
/* 4210 */           edge.getNodeB().removeEdge(edge);
/* 4211 */           log("  affected " + edge.getNodeA());
/*      */         }
/*      */ 
/* 4226 */         if (edge.isCut()) {
/* 4227 */           Iterator kt = edge.getCrackSequence().iterator();
/* 4228 */           while (kt.hasNext()) {
/* 4229 */             Crack c = (Crack)kt.next();
/* 4230 */             cutCracks.add(c);
/*      */           }
/*      */         }
/*      */ 
/* 4234 */         this.edges.remove(edge);
/*      */       }
/*      */ 
/* 4237 */       node.clear();
/*      */ 
/* 4239 */       int nTraps = findNodeTraps(nodeTraps, node.x, node.y);
/* 4240 */       for (int i = 0; i < nTraps; i++) {
/* 4241 */         node.addTrap(nodeTraps[i]);
/* 4242 */         nodeTraps[i].addNode(node);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 4252 */     Vector lineEdges = new Vector();
/* 4253 */     Vector edgeCrackVector = new Vector();
/* 4254 */     Vector gradientVector = new Vector();
/* 4255 */     int lastx = -1; int lasty = -1;
/* 4256 */     Node lastNode = null;
/* 4257 */     boolean validEdge = true;
/* 4258 */     boolean discardSuccessors = false;
/*      */ 
/* 4261 */     int nextx = -1; int nexty = -1;
/*      */ 
/* 4263 */     Iterator it = line.getCrackSequence().iterator();
/* 4264 */     while ((it.hasNext()) || (nextx != -1))
/*      */     {
/*      */       int x;
/*      */       int y;
/* 4266 */       if (nextx == -1) {
/* 4267 */         Crack crack = (Crack)it.next();
/* 4268 */          x = crack.x1;
/* 4269 */          y = crack.y1;
/* 4270 */         log("Line point " + x + ", " + y);
/*      */ 
/* 4273 */         gradientVector.add(new Point(x, y));
/*      */ 
/* 4277 */         if ((x != lastx) && (y != lasty) && (lastx != -1)) {
/* 4278 */           nextx = x;
/* 4279 */           nexty = y;
/*      */ 
/* 4283 */           int dx = x - lastx;
/* 4284 */           int dy = y - lasty;
/*      */ 
/* 4286 */           if (dx > dy)
/*      */           {
/* 4288 */             y++;
/* 4289 */           } else if (dy > dx)
/*      */           {
/* 4291 */             x++;
/* 4292 */           } else if (dx > 0)
/*      */           {
/* 4294 */             if (steep)
/* 4295 */               x--;
/*      */             else
/* 4297 */               y--;
/*      */           }
/* 4299 */           else if (dx < 0)
/*      */           {
/* 4301 */             if (steep)
/* 4302 */               y++;
/*      */             else {
/* 4304 */               x++;
/*      */             }
/*      */           }
///* 4307 */           else if (!$assertionsDisabled) throw new AssertionError();
/*      */ 
/* 4310 */           log("  Adjusted to " + x + ", " + y);
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 4315 */         x = nextx;
/* 4316 */         y = nexty;
/* 4317 */         nextx = -1;
/* 4318 */         nexty = -1;
/* 4319 */         log("Second step " + x + ", " + y);
/*      */ 
/* 4321 */         if (gradientVector.size() == 0) {
/* 4322 */           gradientVector.add(new Point(x, y));
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 4327 */       Crack newCrack = null;
/* 4328 */       if (lastx != -1) {
/* 4329 */         int dx = x - lastx;
/* 4330 */         int dy = y - lasty;
/* 4331 */         assert ((dx == 0) || (dy == 0));
/* 4332 */         if (dx == 0) {
/* 4333 */           if (dy > 0)
/* 4334 */             newCrack = new Crack(x - 1, y - 1, x, y - 1);
/*      */           else {
/* 4336 */             newCrack = new Crack(x - 1, y, x, y);
/*      */           }
/*      */         }
/* 4339 */         else if (dx > 0)
/* 4340 */           newCrack = new Crack(x - 1, y - 1, x - 1, y);
/*      */         else {
/* 4342 */           newCrack = new Crack(x, y - 1, x, y);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 4348 */       Trap trapA = null;
/* 4349 */       Trap trapB = null;
/* 4350 */       if (lastx != -1) {
/* 4351 */         if ((newCrack.y1 >= 0) && (newCrack.x1 >= 0)) {
/* 4352 */           trapA = this.traps[data[(newCrack.y1 * pitch + newCrack.x1)]];
/*      */         }
/* 4354 */         if ((newCrack.y2 >= 0) && (newCrack.x2 >= 0)) {
/* 4355 */           trapB = this.traps[data[(newCrack.y2 * pitch + newCrack.x2)]];
/*      */         }
/* 4357 */         edgeCrackVector.add(newCrack);
/*      */ 
/* 4371 */         if (trapA == trapB) {
/* 4372 */           validEdge = false;
/*      */         }
/*      */       }
/*      */ 
/* 4376 */       int nTraps = findNodeTraps(nodeTraps, x, y);
/* 4377 */       log("  Abutting: " + nTraps);
/* 4378 */       if (nTraps > 2)
/*      */       {
/* 4381 */         if (trapA == null) {
/* 4382 */           trapA = this.traps[data[(y * pitch + x)]];
/*      */         }
/* 4384 */         Node node = trapA.getNode(x, y);
/* 4385 */         if (node == null)
/*      */         {
/* 4387 */           boolean isBorder = (x == 0) || (y == 0) || (x == width) || (y == height);
/*      */ 
/* 4391 */           node = new Node(x, y, nTraps, isBorder);
/* 4392 */           for (int i = 0; i < nTraps; i++) {
/* 4393 */             node.addTrap(nodeTraps[i]);
/* 4394 */             nodeTraps[i].addNode(node);
/*      */           }
/* 4396 */           this.nodes.add(node);
/* 4397 */           log("Created new node " + node);
/*      */         } else {
/* 4399 */           log("Using existing " + node);
/*      */ 
/* 4405 */           touchedNodes.remove(node);
/*      */         }
/*      */ 
/* 4409 */         if ((lastNode != null) && (validEdge) && (!discardSuccessors))
/*      */         {
/* 4411 */           Edge newEdge = new Edge(lastNode, node, trapA, trapB, edgeCrackVector);
/*      */ 
/* 4413 */           newEdge.setGradientVector(gradientVector);
/* 4414 */           newEdge.setCost(0.8F * gradientVector.size());
/* 4415 */           lastNode.addEdge(newEdge);
/* 4416 */           node.addEdge(newEdge);
/* 4417 */           lineEdges.add(newEdge);
/* 4418 */           this.edges.add(newEdge);
/*      */ 
/* 4420 */           edgeCrackVector = new Vector();
/* 4421 */           gradientVector = new Vector();
/* 4422 */         } else if ((lastNode != null) && (!validEdge) && (!discardSuccessors))
/*      */         {
/* 4424 */           validEdge = true;
/* 4425 */           edgeCrackVector = new Vector();
/* 4426 */           gradientVector = new Vector();
/* 4427 */           log("Invalid edge (island)");
/*      */ 
/* 4431 */           if (lineEdges.size() < 5)
/*      */           {
/* 4433 */             lineEdges.clear();
/* 4434 */             log(".. discarding predecessors");
/*      */           }
/*      */           else {
/* 4437 */             log(".. discarding successors");
/* 4438 */             discardSuccessors = true;
/*      */           }
/*      */         }
/* 4441 */         lastNode = node;
/*      */       }
/*      */ 
/* 4444 */       lastx = x;
/* 4445 */       lasty = y;
/*      */     }
/*      */ 
/* 4449 */     int oldEdgeCount = this.edges.size();
/* 4450 */     createEdges(touchedNodes);
/*      */ 
/* 4454 */     for (int i = oldEdgeCount; i < this.edges.size(); i++) {
/* 4455 */       Edge edge = (Edge)this.edges.get(i);
/* 4456 */       it = edge.getCrackSequence().iterator();
/* 4457 */       while (it.hasNext()) {
/* 4458 */         Crack crack = (Crack)it.next();
/* 4459 */         if (cutCracks.contains(crack)) {
/* 4460 */           edge.cut();
/* 4461 */           break;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 4469 */     for (int i = 0; i < this.traps.length; i++) {
/* 4470 */       this.traps[i].getArea().clear();
/* 4471 */       this.traps[i].getStatistics().clear();
/*      */     }
/* 4473 */     createTrapRegions();
/*      */ 
/* 4475 */     return lineEdges;
/*      */   }
/*      */ 
/*      */   private int findNodeTraps(Trap[] trapsFound, int x, int y)
/*      */   {
/* 4483 */     int[] data = this.labelled.getBufferInteger();
/* 4484 */     int pitch = this.labelled.getPitch();
/* 4485 */     int width = this.labelled.getWidth();
/* 4486 */     int height = this.labelled.getHeight();
/* 4487 */     int numAbutting = 1;
/*      */ 
/* 4490 */     int a = -1;
/* 4491 */     int b = -1;
/* 4492 */     if (x == 0) {
/* 4493 */       a = data[((y - 1) * pitch + x)];
/* 4494 */       b = data[(y * pitch + x)];
/* 4495 */     } else if (x == width) {
/* 4496 */       a = data[((y - 1) * pitch + x - 1)];
/* 4497 */       a = data[(y * pitch + x - 1)];
/* 4498 */     } else if (y == 0) {
/* 4499 */       a = data[(x - 1)];
/* 4500 */       b = data[x];
/* 4501 */     } else if (y == height) {
/* 4502 */       a = data[((y - 1) * pitch + x - 1)];
/* 4503 */       b = data[((y - 1) * pitch + x)];
/*      */     }
/*      */ 
/* 4506 */     if ((a != -1) && (b != -1))
/*      */     {
/* 4508 */       if (a != b)
/*      */       {
/* 4510 */         numAbutting = 2;
/* 4511 */         trapsFound[0] = this.traps[a];
/* 4512 */         trapsFound[1] = this.traps[b];
/* 4513 */         return numAbutting;
/*      */       }
/*      */ 
/* 4516 */       return 1;
/*      */     }
/*      */ 
/* 4525 */     a = data[((y - 1) * pitch + x - 1)];
/* 4526 */     b = data[((y - 1) * pitch + x)];
/* 4527 */     int c = data[(y * pitch + x - 1)];
/* 4528 */     int d = data[(y * pitch + x)];
/*      */ 
/* 4530 */     trapsFound[0] = this.traps[a];
/* 4531 */     if (a != b) {
/* 4532 */       trapsFound[(numAbutting++)] = this.traps[b];
/*      */     }
/* 4534 */     if ((a != c) && (b != c)) {
/* 4535 */       trapsFound[(numAbutting++)] = this.traps[c];
/*      */     }
/* 4537 */     if ((a != d) && (b != d) && (c != d)) {
/* 4538 */       trapsFound[(numAbutting++)] = this.traps[d];
/*      */     }
/*      */ 
/* 4541 */     return numAbutting;
/*      */   }
/*      */ 
/*      */   public void log(String str)
/*      */   {
/* 4553 */     if (this.enableLog) {
/* 4554 */       if (logOut == null) {
/*      */         try {
/* 4556 */           logOut = new PrintWriter(new FileWriter(this.logFilename));
/*      */         } catch (Exception e) {
/* 4558 */           this.enableLog = false;
/* 4559 */           e.printStackTrace();
/* 4560 */           return;
/*      */         }
/*      */       }
/* 4563 */       logOut.println(str);
/* 4564 */       logOut.flush();
/*      */     }
/*      */   }
/*      */ 
/*      */   public static void logStatic(String str) {
/* 4569 */     if (logOut != null) {
/* 4570 */       logOut.println(str);
/* 4571 */       logOut.flush();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void log(Throwable t) {
/* 4576 */     if ((this.enableLog) && (logOut != null))
/* 4577 */       t.printStackTrace(logOut);
/*      */   }
/*      */ 
/*      */   public PrintWriter getLogWriter()
/*      */   {
/* 4582 */     if (this.enableLog) {
/* 4583 */       return logOut;
/*      */     }
/* 4585 */     return null;
/*      */   }
/*      */ 
/*      */   public void restartLog()
/*      */   {
/* 4593 */     logOut = null;
/*      */   }
/*      */ 
/*      */   public boolean selfCheck()
/*      */   {
/* 4606 */     if (!this.enableSelfCheck) {
/* 4607 */       return true;
/*      */     }
/*      */ 
/* 4610 */     boolean ok = true;
/* 4611 */     log("Begin self check");
/*      */ 
/* 4614 */     Map pointMap = new HashMap();
/* 4615 */     for (Iterator it = this.nodes.iterator(); it.hasNext(); ) {
/* 4616 */       Node node = (Node)it.next();
/* 4617 */       Point p = new Point(node.x, node.y);
/* 4618 */       if (pointMap.containsKey(p)) {
/* 4619 */         log("Error: " + node + " is a duplicate of " + pointMap.get(p));
/* 4620 */         ok = false;
/*      */       } else {
/* 4622 */         pointMap.put(p, node);
/*      */       }
/*      */     }
/*      */     Trap trap;
/*      */     Iterator it;
/* 4627 */     for (int i = 0; i < this.traps.length; i++) {
/* 4628 */       trap = this.traps[i];
/* 4629 */       for (it = trap.getNodes().iterator(); it.hasNext(); ) {
/* 4630 */         Node node = (Node)it.next();
/* 4631 */         if (!node.hasTrap(trap)) {
/* 4632 */           log("Error: " + node + " does not know about " + trap);
/* 4633 */           ok = false;
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 4639 */     int width = this.labelled.getWidth();
/* 4640 */     int height = this.labelled.getHeight();
/* 4641 */     for (it = this.nodes.iterator(); it.hasNext(); ) {
/* 4642 */       Node node = (Node)it.next();
/* 4643 */       Point p = new Point(node.x, node.y);
/* 4644 */       if ((p.x == 0) || (p.y == 0) || (p.x == width) || (p.y == height)) {
/* 4645 */         if (!node.isBorder()) {
/* 4646 */           log("Error: " + node + " is not reporting border status");
/* 4647 */           ok = false;
/*      */         }
/* 4649 */       } else if (node.isBorder()) {
/* 4650 */         log("Error: " + node + " is incorrectly reporting border " + "status");
/*      */ 
/* 4652 */         ok = false;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 4657 */     for (it = this.nodes.iterator(); it.hasNext(); ) {
/* 4658 */       Node node = (Node)it.next();
/* 4659 */       for (int i = 0; i < node.getTrapCount(); i++) {
/* 4660 */         trap = node.getTrap(i);
/* 4661 */         if (trap.getNode(node.x, node.y) == null) {
/* 4662 */           log("Error: " + trap + " does not reference " + node);
/* 4663 */           ok = false;
/* 4664 */         } else if (trap.getNode(node.x, node.y) != node) {
/* 4665 */           log("Error: " + trap + " references incorrect " + node);
/* 4666 */           ok = false;
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 4672 */     for (it = this.nodes.iterator(); it.hasNext(); ) {
/* 4673 */       Node node = (Node)it.next();
/* 4674 */       for (Iterator jt = node.getEdges(); jt.hasNext(); ) {
/* 4675 */         Edge edge = (Edge)jt.next();
/* 4676 */         if (edge == null) {
/* 4677 */           log("Error: null edge in " + node);
/* 4678 */           ok = false;
/* 4679 */         } else if ((edge.getNodeA() != node) && (edge.getNodeB() != node)) {
/* 4680 */           log("Error: " + edge + " does not reference " + node);
/* 4681 */           ok = false;
/*      */         }
/*      */       }
/*      */     }
/*      */     Node node;
/*      */     Iterator jt;
/* 4687 */     for ( it = this.edges.iterator(); it.hasNext(); ) {
/* 4688 */       Edge edge = (Edge)it.next();
/* 4689 */       if (!edge.getNodeA().hasEdge(edge)) {
/* 4690 */         log("Error: " + edge.getNodeA() + " does not know about " + edge);
/*      */ 
/* 4692 */         ok = false;
/*      */       }
/* 4694 */       if (!edge.getNodeB().hasEdge(edge)) {
/* 4695 */         log("Error: " + edge.getNodeB() + " does not know about " + edge);
/*      */ 
/* 4697 */         ok = false;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 4702 */     Set trapSet = new HashSet();
/* 4703 */     for (int i = 0; i < this.traps.length; i++) {
/* 4704 */       if (trapSet.contains(this.traps[i])) {
/* 4705 */         log("Duplicate trap " + this.traps[i] + " at " + i);
/* 4706 */         ok = false;
/*      */       }
/* 4708 */       trapSet.add(this.traps[i]);
/*      */     }
/*      */ 
/* 4712 */     for ( it = this.edges.iterator(); it.hasNext(); ) {
/* 4713 */       Edge edge = (Edge)it.next();
/* 4714 */       if (edge.getGradientVector().size() == 0) {
/* 4715 */         log("Zero-length gradient edge " + edge);
/* 4716 */         ok = false;
/*      */       }
/* 4718 */       if (edge.getCrackSequence().size() == 0) {
/* 4719 */         log("Zero-length cracks edge " + edge);
/* 4720 */         ok = false;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 4725 */     for (int i = 0; i < this.traps.length; i++) {
/* 4726 */       Mask m = this.traps[i].getHighestSuperRegion().getMask();
/* 4727 */       if ((m != null) && (!this.masks.contains(m))) {
/* 4728 */         log("Trap " + this.traps[i] + " has unknown mask " + m);
/* 4729 */         ok = false;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 4752 */     log("Self check finished.");
/*      */ 
/* 4754 */     return ok;
/*      */   }
/*      */ 
/*      */   public String getByteSize() {
/* 4758 */     return this.byteSize;
/*      */   }
/*      */ 
/*      */   private static class Decomposition
/*      */   {
/*      */     public int diff;
/*      */     public int f1;
/*      */     public int f2;
/*      */   }
/*      */ 
/*      */   private static class RecolourData
/*      */   {
/*      */     public int weight;
/*      */     public int component1;
/*      */     public int component2;
/*      */     public Mask maskA;
/*      */     public Mask maskB;
/*      */     public boolean painted;
/*      */   }
/*      */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.PaintExplorer
 * JD-Core Version:    0.6.2
 */