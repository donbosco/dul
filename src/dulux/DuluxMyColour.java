/*      */ package dulux;
/*      */ 
/*      */ import dulux.sax.DuluxPreMaskedLoaderSAX;
/*      */ import duluxskin.ExceptionListener;
/*      */ import duluxskin.LabelEvent;
/*      */ import duluxskin.LabelListener;
/*      */ import duluxskin.Layer;
/*      */ import duluxskin.ListEvent;
/*      */ import duluxskin.ListListener;
/*      */ import duluxskin.Skin;
/*      */ import duluxskin.SkinnedButton;
/*      */ import duluxskin.SkinnedImage;
/*      */ import duluxskin.SkinnedLabel;
/*      */ import duluxskin.SkinnedList;
/*      */ import duluxskin.SkinnedListItem;
/*      */ import duluxskin.SkinnedListSwatchItem;
/*      */ import duluxskin.SkinnedListTextItem;
/*      */ import duluxskin.SkinnedPlaceholder;
/*      */ import duluxskin.SkinnedPopupList;
/*      */ import duluxskin.SkinnedProgress;
/*      */ import duluxskin.SkinnedSlider;
/*      */ import duluxskin.Widget;
/*      */ import duluxskin.WidgetGroup;
/*      */ import java.awt.Button;
/*      */ import java.awt.Color;
/*      */ import java.awt.Container;
/*      */ import java.awt.Cursor;
/*      */ import java.awt.Dialog;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.FlowLayout;
/*      */ import java.awt.Frame;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.Graphics2D;
/*      */ import java.awt.Image;
/*      */ import java.awt.Label;
/*      */ import java.awt.Panel;
/*      */ import java.awt.Point;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.Toolkit;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.ActionListener;
/*      */ import java.awt.event.AdjustmentEvent;
/*      */ import java.awt.event.AdjustmentListener;
/*      */ import java.awt.event.ComponentAdapter;
/*      */ import java.awt.event.ComponentEvent;
/*      */ import java.awt.event.MouseAdapter;
/*      */ import java.awt.event.MouseEvent;
/*      */ import java.awt.event.MouseListener;
/*      */ import java.awt.event.WindowAdapter;
/*      */ import java.awt.event.WindowEvent;
/*      */ import java.awt.image.BufferedImage;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.PrintStream;
/*      */ import java.net.URL;
/*      */ import java.util.Collection;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Properties;
/*      */ import java.util.Set;
/*      */ import java.util.Stack;
/*      */ import java.util.Vector;
/*      */ import javax.imageio.ImageIO;
/*      */ import javax.swing.JFileChooser;
/*      */ import javax.swing.UIManager;
/*      */ import javax.swing.filechooser.FileFilter;
/*      */ import pelib.Colour;
/*      */ import pelib.ExpertPaintManager;
/*      */ import pelib.ImageColour;
/*      */ import pelib.Mask;
/*      */ import pelib.PaintExplorer;
/*      */ import pelib.PaintExplorerHistoryEvent;
/*      */ import pelib.PaintExplorerListener;
/*      */ import pelib.PaintExplorerMaskEvent;
/*      */ import pelib.PaintExplorerProgressEvent;
/*      */ import pelib.PaintExplorerScissorsEvent;
/*      */ import pelib.lighting.DaylightLighting;
/*      */ import pelib.lighting.FluorescentLighting;
/*      */ import pelib.lighting.HalogenLighting;
/*      */ import pelib.lighting.IncandescentLighting;
/*      */ import pelib.lighting.LightingAlgorithm;
/*      */ 
/*      */ public class DuluxMyColour extends Container
/*      */   implements ActionListener, ListListener, PaintExplorerListener, DuluxPaletteListener, AdjustmentListener, ExceptionListener, LabelListener
/*      */ {
/*      */   private static final long serialVersionUID = 3652752360063086058L;
/*   31 */   private final String VERSION = "4.8.10 for AUS/NZ";
/*      */   private static final String LAYOUT_XML_AUS = "/ui/layout_AUS.xml";
/*      */   private static final String LAYOUT_XML_NZ = "/ui/layout_NZ.xml";
/*      */   private static final String PREMASKED_IMAGES_AUS = "/premasked/AUS/PreMasked.xml";
/*      */   private static final String PREMASKED_IMAGES_NZ = "/premasked/NZ/PreMasked.xml";
/*      */   private static final String cursorName = "Eraser Cursor";
/*      */   private static final String PRINT_SVG = "/print.svg";
/*      */   private static final String PROJECT_EXTENSION = ".dmc";
/*      */   private static DuluxLogger logger;
/*      */   private boolean IS_NZ;
/*      */   private final String WEB_SERVICE_DOMAIN;
/*      */   private final String SESSIONID;
/*   45 */   private final int PAINT_MODE = 0;
/*   46 */   private final int ERASER_MODE = 1;
/*   47 */   private final int MASK_MODE = 2;
/*   48 */   private final int PAN_MODE = 3;
/*      */   private static final int STANDARD_TYPE = 0;
/*      */   private static final int RENDER_TYPE = 1;
/*      */   private static final int SPECIFIER_TYPE = 2;
/*   53 */   private final int OVERWRITE_LAST = 1;
/*   54 */   private final int CONTINUE_PROJECT = 2;
/*   55 */   private final int SAVE_BEFORE_EXIT = 3;
/*      */ 
/*   57 */   private final int _STANDARDS = 0;
/*   58 */   private final int _SPECIALTIES = 1;
/*   59 */   private final int _OTHERS = 2;
/*      */   private int colour1;
/*      */   private int colour2;
/*      */   private int colour3;
/*      */   private Map helpImages;
/*      */   private Map wallImages;
/*      */   private Map specialtyImages;
/*      */   private File directory;
/*   71 */   private int colourWallType = 0;
/*   72 */   private int thumbnailpage = 0;
/*   73 */   private int maxEraserSize = 15;
/*   74 */   private int mode = 0;
/*      */   private int change;
/*      */   private int layerCount;
/*      */   private int questionType;
/*      */   private int typeOfPalette;
/*   80 */   private boolean tipsOn = true;
/*   81 */   private boolean saveSuccess = false;
/*      */   private boolean imageLoaded;
/*      */   private boolean projectLoaded;
/*      */   private boolean isNewProject;
/*      */   private boolean isBusy;
/*      */   private boolean isReady;
/*      */   private boolean eraserOn;
/*      */   private String dmcURL;
/*      */   private String filename;
/*      */   private byte[] initialImage;
/*      */   private Frame frame;
/*      */   private Skin skin;
/*      */   private SkinnedImage thumbnail;
/*      */   private SkinnedPlaceholder thumbnailPlaceHolder;
/*      */   private SkinnedSlider eraserSlider;
/*      */   private SkinnedSlider blurSlider;
/*      */   private SkinnedProgress progress;
/*      */   private SkinnedPopupList lightingList2;
/*      */   private SkinnedPopupList lightingList;
/*      */   private SkinnedList colourList;
/*      */   private SkinnedList maskList;
/*      */   private SkinnedList paletteList;
/*      */   private PaintExplorer explorer;
/*      */   private ExpertPaintManager paintManager;
/*      */   private Mask selectedMask;
/*      */   private Mask lastSelected;
/*      */   private DuluxPaletteManager paletteManager;
/*      */   private DuluxPaintView view;
/*      */   private DuluxPaintInput input;
/*      */   private DuluxPaintInputLayer inputLayer;
/*      */   private DuluxColour[] myColours;
/*      */   private DuluxColour old;
/*      */   private DuluxPaletteView paletteView;
/*      */   private DuluxColour[][] schemes;
/*      */   private DuluxSaveAndLoad saveAndLoad;
/*      */   private WidgetGroup colourWallWidgets;
/*      */   private WidgetGroup bottomWidgets;
/*      */   private WidgetGroup thumbnails;
/*      */   private WidgetGroup requireColour1Group;
/*      */   private WidgetGroup requireColour2Group;
/*      */   private WidgetGroup requireColour3Group;
/*      */   private WidgetGroup allWidgets;
/*      */   private WidgetGroup alwaysReadyWidgets;
/*      */   private WidgetGroup requireImageWidgets;
/*      */   private WidgetGroup requireMaskWidgets;
/*      */   private WidgetGroup requireLayerSelectedWidgets;
/*      */   private WidgetGroup tipsLayerWidgets;
/*      */   private WidgetGroup colourWallArrowWidgets;
/*      */   private WidgetGroup atLeastOneWidgets;
/*      */   private Layer errorLayer;
/*      */   private Layer editMaskLayer;
/*      */   private Layer yesNoLayer;
/*      */   private Layer helpLayer;
/*      */   private Layer thumbnailLayer;
/*      */   private Layer progressLayer;
/*      */   private Layer lightingLayer;
/*      */   private Layer colourWallLayer;
/*      */   private Layer buttons;
/*      */   private Layer coloursLayer;
/*      */   private Layer backgroundColourWall;
/*      */   private Layer groupsLayer;
/*      */   private Layer specialtiesLayer;
/*      */   private Layer selectedLayer;
/*      */   private Layer premaskedLayer;
/*      */   private Layer localOrPremaskedLayer;
/*      */   private SkinnedLabel errorLabel;
/*      */   private SkinnedLabel yesNoLabel;
/*      */   private SkinnedLabel progressLabel;
/*      */   private SkinnedLabel editinglabel;
/*      */   private SkinnedLabel selectedName;
/*      */   private SkinnedButton loadButton;
/*      */   private SkinnedButton loadFromFileButton;
/*      */   private SkinnedButton loadFromPreMaskedButton;
/*      */   private SkinnedButton cancel1Button;
/*      */   private SkinnedButton cancel2Button;
/*      */   private SkinnedButton thumbnail1Button;
/*      */   private SkinnedButton thumbnail2Button;
/*      */   private SkinnedButton thumbnail3Button;
/*      */   private SkinnedButton nextthumbnailsButton;
/*      */   private SkinnedButton previousthumbnailsButton;
/*      */   private SkinnedButton loadProject;
/*      */   private SkinnedButton tutorialsButton;
/*      */   private SkinnedButton tipsMinimizedButton;
/*      */   private SkinnedButton tipsPaintButton;
/*      */   private SkinnedButton tipsMaskButton;
/*      */   private SkinnedButton tipsEraserButton;
/*      */   private SkinnedButton helpButton;
/*      */   private SkinnedButton original0;
/*      */   private SkinnedButton base_1;
/*      */   private SkinnedButton original2;
/*      */   private SkinnedButton original3;
/*      */   private SkinnedButton original4;
/*      */   private SkinnedButton original5;
/*      */   private SkinnedButton interior1_1;
/*      */   private SkinnedButton interior1_2;
/*      */   private SkinnedButton interior2_1;
/*      */   private SkinnedButton interior2_2;
/*      */   private SkinnedButton exterior1_1;
/*      */   private SkinnedButton exterior1_2;
/*      */   private SkinnedButton exterior2_1;
/*      */   private SkinnedButton exterior2_2;
/*      */   private SkinnedButton base_2;
/*      */   private SkinnedButton base_3;
/*      */   private SkinnedButton schemePopupCloseButton;
/*      */   private SkinnedButton colour1Button;
/*      */   private SkinnedButton colour2Button;
/*      */   private SkinnedButton colour3Button;
/*      */   private SkinnedButton changeSwatchButton1;
/*      */   private SkinnedButton changeSwatchButton2;
/*      */   private SkinnedButton changeSwatchButton3;
/*      */   private SkinnedButton addLayerButton1;
/*      */   private SkinnedButton addLayerButton2;
/*      */   private SkinnedButton addLayerButton3;
/*      */   private SkinnedButton editLayerButton;
/*      */   private SkinnedButton deleteButton;
/*      */   private SkinnedButton eraserButton;
/*      */   private SkinnedButton paintButton;
/*      */   private SkinnedButton maskButton;
/*      */   private SkinnedButton undoButton;
/*      */   private SkinnedButton redoButton;
/*      */   private SkinnedButton saveToFileButton;
/*      */   private SkinnedButton saveToWebsiteButton;
/*      */   private SkinnedButton returnToDuluxButton;
/*      */   private SkinnedButton centreButton;
/*      */   private SkinnedButton zoomInButton;
/*      */   private SkinnedButton zoomOutButton;
/*      */   private SkinnedButton errorOkButton;
/*      */   private SkinnedButton yesButton;
/*      */   private SkinnedButton noButton;
/*      */   private SkinnedButton changeSelectedMaskButton1;
/*      */   private SkinnedButton changeSelectedMaskButton2;
/*      */   private SkinnedButton changeSelectedMaskButton3;
/*      */   private SkinnedButton closeEditMaskButton;
/*      */   private SkinnedButton specialtiesButton;
/*      */   private SkinnedButton standardsButton;
/*      */   private SkinnedButton whitesButton;
/*      */   private SkinnedButton traditionalsButton;
/*      */   private SkinnedButton backToWallButton;
/*      */   private SkinnedButton backToImageButton;
/*      */   private SkinnedButton rightButton;
/*      */   private SkinnedButton leftButton;
/*      */   private SkinnedButton upButton;
/*      */   private SkinnedButton downButton;
/*      */   private SkinnedButton reds;
/*      */   private SkinnedButton oranges;
/*      */   private SkinnedButton browns;
/*      */   private SkinnedButton neutrals;
/*      */   private SkinnedButton yellows;
/*      */   private SkinnedButton greens;
/*      */   private SkinnedButton blues;
/*      */   private SkinnedButton purples;
/*      */   private SkinnedButton suede;
/*      */   private SkinnedButton tuscan;
/*      */   private SkinnedButton metallic;
/*      */   private SkinnedButton colorBond;
/*      */   private SkinnedButton riverRock;
/*      */   private SkinnedButton designer;
/*      */   private SkinnedButton render;
/*      */   private SkinnedButton sprayFast;
/*      */   private SkinnedButton quitRust;
/*      */   private SkinnedButton garageFloors;
/*      */   private SkinnedButton weatherShieldGardenShades;
/*      */   private SkinnedButton weatherShieldRoofandTrim;
/*      */   private Layer chooseLightingLayer;
/*      */   private boolean touched;
/*      */   private boolean loadProjectAtStartup;
/*      */ 
/*      */   public DuluxMyColour(Frame frame, String sId, String webserviceDomain, boolean loadProjectAtStartup, boolean loadImage, boolean isNZ, int colour1, int colour2, int colour3)
/*      */     throws IOException
/*      */   {
/*  333 */     this.frame = frame;
/*  334 */     this.loadProjectAtStartup = loadProjectAtStartup;
/*  335 */     this.WEB_SERVICE_DOMAIN = webserviceDomain;
/*  336 */     this.paletteManager = new DuluxPaletteManager(this.WEB_SERVICE_DOMAIN);
/*  337 */     this.SESSIONID = sId;
/*  338 */     this.IS_NZ = isNZ;
/*  339 */     this.colour1 = colour1;
/*  340 */     this.colour2 = colour2;
/*  341 */     this.colour3 = colour3;
/*  342 */     checkVersion();
/*  343 */     logger = new DuluxLogger(this);
/*  344 */     logger.log("NZ Version : " + this.IS_NZ);
/*  345 */     logger.log("SID : " + this.SESSIONID);
/*  346 */     logger.log("Web Service Domain : " + this.WEB_SERVICE_DOMAIN);
/*      */ 
/*  349 */     setLayout(null);
/*  350 */     addComponentListener(new ComponentAdapter() {
/*      */       public void componentResized(ComponentEvent e) {
/*  352 */         DuluxMyColour.this.resize();
/*      */       }
/*      */     });
/*  356 */     if (this.IS_NZ) {
/*  357 */       loadSkin("/ui/layout_NZ.xml");
/*      */     }
/*      */     else {
/*  360 */       loadSkin("/ui/layout_AUS.xml");
/*      */     }
/*      */ 
/*  363 */     initPaintExplorer();
/*  364 */     this.saveAndLoad = new DuluxSaveAndLoad(this.WEB_SERVICE_DOMAIN, this.explorer, logger);
/*  365 */     //loadPalettes();
/*  366 */     this.paletteManager.loadSpecialties();
/*  367 */     this.paletteManager.loadRenderColourWall();
/*      */ 
/*  369 */     loadMyProjectColours();
/*  370 */     createWidgetGroups();
/*  371 */     loadThumbnails(this.thumbnailpage);
/*      */ 
/*  373 */     updateUI();
/*  374 */     updateTitle();
/*  375 */     this.requireImageWidgets.setEnabled(false);
/*  376 */     if (loadProjectAtStartup) {
/*      */       try {
/*  378 */         this.isNewProject = true;
/*  379 */         loadProjectFromWeb();
/*  380 */         this.skin.requestFocus();
/*      */       } catch (Exception e) {
/*  382 */         System.err.print("Error loading project at startup : ");
/*  383 */         e.printStackTrace();
/*      */       }
/*      */     }
/*      */ 
/*  387 */     this.addLayerButton1.setVisible(true);
/*  388 */     this.addLayerButton2.setVisible(false);
/*  389 */     this.addLayerButton3.setVisible(false);
/*  390 */     this.changeSwatchButton1.setVisible(true);
/*  391 */     this.changeSwatchButton2.setVisible(false);
/*  392 */     this.changeSwatchButton3.setVisible(false);
/*  393 */     this.thumbnailLayer.setVisible(false);
/*      */ 
/*  395 */     this.touched = false;
/*      */   }
/*      */ 
/*      */   private void changeColour(SkinnedButton sButton) {
/*  399 */     this.selectedLayer.setVisible(false);
/*  400 */     this.colourWallLayer.setVisible(false);
/*  401 */     this.coloursLayer.setVisible(false);
/*  402 */     boolean hasColour = false;
/*  403 */     for (int i = 0; i < this.myColours.length; i++) {
/*  404 */       if (sButton.getColour().equals(this.myColours[i])) {
/*  405 */         hasColour = true;
/*      */       }
/*      */     }
/*  408 */     if (!hasColour) {
/*  409 */       this.myColours[this.change] = sButton.getColour();
/*  410 */       changeMaskColour(this.old, this.myColours[this.change]);
/*      */     }
/*  412 */     updateViewAndThumbnail();
/*  413 */     this.thumbnailLayer.setVisible(false);
/*  414 */     this.bottomWidgets.setVisible(true);
/*  415 */     this.groupsLayer.setVisible(false);
/*  416 */     this.backToImageButton.setVisible(false);
/*  417 */     this.backgroundColourWall.setVisible(false);
/*  418 */     refreshButtonColours();
/*      */   }
/*      */ 
/*      */   private void changePalette(int palette) {
/*  422 */     if (this.paletteManager.getPalette(palette).cols <= 10) {
/*  423 */       this.rightButton.setVisible(false);
/*  424 */       this.leftButton.setVisible(false);
/*      */     }
/*      */     else {
/*  427 */       this.rightButton.setVisible(true);
/*  428 */       this.leftButton.setVisible(true);
/*      */     }
/*      */ 
/*  431 */     this.paletteView.setPalette(this.paletteManager.getPalette(palette));
/*  432 */     this.coloursLayer.setVisible(true);
/*  433 */     this.colourWallLayer.setVisible(false);
/*      */ 
/*  435 */     this.paletteView.setDefaultPosition();
/*  436 */     this.backToWallButton.setVisible(true);
/*  437 */     this.backgroundColourWall.setVisible(true);
/*      */   }
/*      */ 
/*      */   private void changePalette(int index, boolean isRender)
/*      */   {
/*  443 */     if (this.paletteManager.getRenderPalette(index).cols <= 10) {
/*  444 */       this.rightButton.setVisible(false);
/*  445 */       this.leftButton.setVisible(false);
/*      */     }
/*      */     else {
/*  448 */       this.rightButton.setVisible(true);
/*  449 */       this.leftButton.setVisible(true);
/*      */     }
/*  451 */     if (this.paletteManager.getRenderPalette(index).rows <= 7) {
/*  452 */       this.upButton.setVisible(false);
/*  453 */       this.downButton.setVisible(false);
/*      */     } else {
/*  455 */       this.upButton.setVisible(true);
/*  456 */       this.downButton.setVisible(true);
/*      */     }
/*      */ 
/*  459 */     this.paletteView.setPalette(this.paletteManager.getRenderPalette(index));
/*  460 */     this.coloursLayer.setVisible(true);
/*  461 */     this.colourWallLayer.setVisible(false);
/*      */ 
/*  463 */     this.paletteView.setDefaultPosition();
/*  464 */     this.backToWallButton.setVisible(true);
/*  465 */     this.backgroundColourWall.setVisible(true);
/*      */   }
/*      */ 
/*      */   private void changePaletteSpecifier(int index)
/*      */   {
/*  470 */     if (this.paletteManager.getSpecifierPalette(index).cols <= 10) {
/*  471 */       this.rightButton.setVisible(false);
/*  472 */       this.leftButton.setVisible(false);
/*      */     }
/*      */     else {
/*  475 */       this.rightButton.setVisible(true);
/*  476 */       this.leftButton.setVisible(true);
/*      */     }
/*  478 */     if (this.paletteManager.getSpecifierPalette(index).rows <= 7) {
/*  479 */       this.upButton.setVisible(false);
/*  480 */       this.downButton.setVisible(false);
/*      */     } else {
/*  482 */       this.upButton.setVisible(true);
/*  483 */       this.downButton.setVisible(true);
/*      */     }
/*      */ 
/*  486 */     this.paletteView.setPalette(this.paletteManager.getSpecifierPalette(index));
/*  487 */     this.coloursLayer.setVisible(true);
/*  488 */     this.colourWallLayer.setVisible(false);
/*      */ 
/*  490 */     this.paletteView.setDefaultPosition();
/*  491 */     this.backToWallButton.setVisible(true);
/*  492 */     this.backgroundColourWall.setVisible(true);
/*      */   }
/*      */ 
/*      */   private boolean checkHasLayer(DuluxColour dColour)
/*      */   {
/*  497 */     for (Iterator it = this.explorer.getMasks(); it.hasNext(); ) {
/*  498 */       Mask mask = (Mask)it.next();
/*  499 */       if (mask.getColour() == dColour.colour) {
/*  500 */         return true;
/*      */       }
/*      */     }
/*      */ 
/*  504 */     return false;
/*      */   }
/*      */ 
/*      */   private void colourButtonSelected(SkinnedButton selected) {
/*  508 */     if (selected == this.colour1Button) {
/*  509 */       if (!checkHasLayer(selected.getColour())) {
/*  510 */         this.explorer.createMask(selected.getColour().colour, "Area " + this.layerCount, selected.getColour().getId(), 0);
/*  511 */         this.layerCount += 1;
/*      */       }
/*  513 */       refreshButtonColours();
/*  514 */       this.colour1Button.setSelected(true);
/*  515 */       this.colour2Button.setSelected(false);
/*  516 */       this.colour3Button.setSelected(false);
/*  517 */       this.addLayerButton1.setVisible(true);
/*  518 */       this.addLayerButton2.setVisible(false);
/*  519 */       this.addLayerButton3.setVisible(false);
/*  520 */       this.changeSwatchButton1.setVisible(true);
/*  521 */       this.changeSwatchButton2.setVisible(false);
/*  522 */       this.changeSwatchButton3.setVisible(false);
/*  523 */     } else if (selected == this.colour2Button) {
/*  524 */       if (!checkHasLayer(selected.getColour())) {
/*  525 */         this.explorer.createMask(selected.getColour().colour, "Area " + this.layerCount, selected.getColour().getId(), 1);
/*  526 */         this.layerCount += 1;
/*      */       }
/*  528 */       refreshButtonColours();
/*  529 */       this.colour1Button.setSelected(false);
/*  530 */       this.colour2Button.setSelected(true);
/*  531 */       this.colour3Button.setSelected(false);
/*  532 */       this.addLayerButton1.setVisible(false);
/*  533 */       this.addLayerButton2.setVisible(true);
/*  534 */       this.addLayerButton3.setVisible(false);
/*  535 */       this.changeSwatchButton1.setVisible(false);
/*  536 */       this.changeSwatchButton2.setVisible(true);
/*  537 */       this.changeSwatchButton3.setVisible(false);
/*  538 */     } else if (selected == this.colour3Button) {
/*  539 */       if (!checkHasLayer(selected.getColour())) {
/*  540 */         this.explorer.createMask(selected.getColour().colour, "Area " + this.layerCount, selected.getColour().getId(), 2);
/*  541 */         this.layerCount += 1;
/*      */       }
/*  543 */       refreshButtonColours();
/*  544 */       this.colour1Button.setSelected(false);
/*  545 */       this.colour2Button.setSelected(false);
/*  546 */       this.colour3Button.setSelected(true);
/*  547 */       this.addLayerButton1.setVisible(false);
/*  548 */       this.addLayerButton2.setVisible(false);
/*  549 */       this.addLayerButton3.setVisible(true);
/*  550 */       this.changeSwatchButton1.setVisible(false);
/*  551 */       this.changeSwatchButton2.setVisible(false);
/*  552 */       this.changeSwatchButton3.setVisible(true);
/*      */     }
/*  554 */     Stack stack = new Stack();
/*  555 */     for (Iterator it = this.explorer.getMasks(); it.hasNext(); ) {
/*  556 */       Mask mask = (Mask)it.next();
/*  557 */       stack.push(mask);
/*      */     }
/*  559 */     while (!stack.empty()) {
/*  560 */       Mask mask = (Mask)stack.pop();
/*  561 */       if (mask.getColour() == selected.getColour().colour) {
/*  562 */         this.maskList.setSelectedData(mask);
/*      */       }
/*      */     }
/*  565 */     Mask mask = (Mask)this.maskList.getSelectedItem().getData();
/*  566 */     this.paintManager.setSelectedMask(mask);
/*  567 */     this.lastSelected = mask;
/*  568 */     this.explorer.log("DuluxMyColour: Selected mask " + mask);
/*      */   }
/*      */ 
/*      */   private void resize() {
/*  572 */     Rectangle rect = getSkinRect();
/*  573 */     this.skin.setLocation(rect.x, rect.y);
/*  574 */     this.skin.setSize(rect.width, rect.height);
/*      */   }
/*      */ 
/*      */   private Rectangle getSkinRect() {
/*  578 */     Dimension d = this.skin.getPreferredSize();
/*  579 */     int a = getWidth() - d.width;
/*  580 */     int b = getHeight() - d.height;
/*      */ 
/*  582 */     return new Rectangle(a / 2, b / 2, d.width, d.height);
/*      */   }
/*      */ 
/*      */   private void loadThumbnails(int page) {
/*  586 */     DuluxPreMaskedLoaderSAX premaskedLaoder = new DuluxPreMaskedLoaderSAX(this.WEB_SERVICE_DOMAIN + "/dmc_images");
/*      */     DuluxPreMasked[] preMasked;
/*  588 */     if (this.IS_NZ)
/*  589 */       preMasked = premaskedLaoder.readXML(getClass().getResourceAsStream("/premasked/NZ/PreMasked.xml"));
/*      */     else
/*  591 */       preMasked = premaskedLaoder.readXML(getClass().getResourceAsStream("/premasked/AUS/PreMasked.xml"));
/*  592 */     BufferedImage bimg = null;
/*      */     int count;
/*  594 */     if (page <= 0) {
/*  595 */       this.thumbnailpage = 0;
/*  596 */       count = 0;
/*      */     } else {
/*  598 */       count = page * 3;
/*      */     }
/*  600 */     if (count + 1 > preMasked.length) {
/*  601 */       this.thumbnailpage = (page - 1);
/*  602 */       count = this.thumbnailpage * 3;
/*      */     }
/*      */ 
/*  605 */     for (Iterator it = this.thumbnails.getIterator(); it.hasNext(); ) {
/*      */       try {
/*  607 */         bimg = ImageIO.read(getClass().getResourceAsStream(preMasked[count].getThumbnail()));
/*  608 */         SkinnedButton button = (SkinnedButton)it.next();
/*  609 */         button.setImage(bimg, preMasked[count].getAddress());
/*      */       } catch (ArrayIndexOutOfBoundsException e) {
/*  611 */         SkinnedButton button = (SkinnedButton)it.next();
/*  612 */         button.removeImage();
/*  613 */         if (it.hasNext()) {
/*  614 */           button = (SkinnedButton)it.next();
/*  615 */           button.removeImage();
/*      */         }
/*  617 */         break;
/*      */       }
/*      */       catch (IOException e) {
/*  620 */         System.err.println(e);
/*      */       }
/*  622 */       count++;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void loadSkin(String skinName)
/*      */     throws IOException
/*      */   {
/*  630 */     InputStream is = getClass().getResourceAsStream(skinName);
/*  631 */     this.skin = new Skin();
/*  632 */     this.skin.addExceptionListener(this);
/*  633 */     this.skin.load(is);
/*  634 */     add(this.skin);
/*  635 */     resize();
/*      */ 
/*  637 */     this.cancel1Button = this.skin.getButton("cancel1");
/*  638 */     this.cancel1Button.addActionListener(this);
/*  639 */     this.cancel2Button = this.skin.getButton("cancel2");
/*  640 */     this.cancel2Button.addActionListener(this);
/*      */ 
/*  642 */     this.nextthumbnailsButton = this.skin.getButton("nextthumbnails");
/*  643 */     this.nextthumbnailsButton.addActionListener(this);
/*  644 */     this.previousthumbnailsButton = this.skin.getButton("previousthumbnails");
/*  645 */     this.previousthumbnailsButton.addActionListener(this);
/*  646 */     this.thumbnail1Button = this.skin.getButton("picture1");
/*  647 */     this.thumbnail1Button.addActionListener(this);
/*  648 */     this.thumbnail2Button = this.skin.getButton("picture2");
/*  649 */     this.thumbnail2Button.addActionListener(this);
/*  650 */     this.thumbnail3Button = this.skin.getButton("picture3");
/*  651 */     this.thumbnail3Button.addActionListener(this);
/*      */ 
/*  653 */     this.localOrPremaskedLayer = this.skin.getLayer("localorpremasked");
/*  654 */     this.premaskedLayer = this.skin.getLayer("premaskedLayer");
/*      */ 
/*  656 */     this.loadFromFileButton = this.skin.getButton("loadFromFile");
/*  657 */     this.loadFromFileButton.addActionListener(this);
/*      */ 
/*  659 */     this.loadFromPreMaskedButton = this.skin.getButton("loadFromPremasked");
/*  660 */     this.loadFromPreMaskedButton.addActionListener(this);
/*      */ 
/*  662 */     this.centreButton = this.skin.getButton("centreImage");
/*  663 */     this.centreButton.addActionListener(this);
/*      */ 
/*  665 */     this.tutorialsButton = this.skin.getButton("tutorials");
/*  666 */     this.tutorialsButton.addActionListener(this);
/*      */ 
/*  668 */     this.returnToDuluxButton = this.skin.getButton("returnToDulux");
/*  669 */     this.returnToDuluxButton.addActionListener(this);
/*      */ 
/*  671 */     this.backgroundColourWall = this.skin.getLayer("backgroundColourWall");
/*      */ 
/*  673 */     this.tipsMinimizedButton = this.skin.getButton("tipsMinimized");
/*  674 */     this.tipsMinimizedButton.addActionListener(this);
/*      */ 
/*  676 */     this.tipsPaintButton = this.skin.getButton("tipsPaint");
/*  677 */     this.tipsPaintButton.addActionListener(this);
/*      */ 
/*  679 */     this.tipsMaskButton = this.skin.getButton("tipsMask");
/*  680 */     this.tipsMaskButton.addActionListener(this);
/*      */ 
/*  682 */     this.tipsEraserButton = this.skin.getButton("tipsEraser");
/*  683 */     this.tipsEraserButton.addActionListener(this);
/*      */ 
/*  685 */     this.editLayerButton = this.skin.getButton("editLayer");
/*  686 */     this.editLayerButton.addActionListener(this);
/*      */ 
/*  688 */     this.specialtiesLayer = this.skin.getLayer("specialties");
/*  689 */     this.groupsLayer = this.skin.getLayer("groups");
/*  690 */     this.specialtyImages = new HashMap();
/*      */ 
/*  692 */     if (this.IS_NZ) {
/*  693 */       this.standardsButton = this.skin.getButton("coloursOfNZ");
/*  694 */       this.standardsButton.addActionListener(this);
/*  695 */       this.traditionalsButton = this.skin.getButton("specifier");
/*  696 */       this.traditionalsButton.addActionListener(this);
/*  697 */       this.specialtiesButton = this.skin.getButton("specialty");
/*  698 */       this.specialtiesButton.addActionListener(this);
/*  699 */       this.whitesButton = this.skin.getButton("whites");
/*  700 */       this.whitesButton.addActionListener(this);
/*      */     }
/*      */     else {
/*  703 */       this.standardsButton = this.skin.getButton("standards");
/*  704 */       this.standardsButton.addActionListener(this);
/*  705 */       this.specialtiesButton = this.skin.getButton("specialty");
/*  706 */       this.specialtiesButton.addActionListener(this);
/*  707 */       this.whitesButton = this.skin.getButton("whites");
/*  708 */       this.whitesButton.addActionListener(this);
/*  709 */       this.traditionalsButton = this.skin.getButton("traditionals");
/*  710 */       this.traditionalsButton.addActionListener(this);
/*      */     }
/*      */ 
/*  715 */     this.chooseLightingLayer = this.skin.getLayer("chooseLightingLayer");
/*  716 */     this.suede = this.skin.getButton("suede");
/*  717 */     this.suede.addActionListener(this);
/*  718 */     this.tuscan = this.skin.getButton("tuscan");
/*  719 */     this.tuscan.addActionListener(this);
/*  720 */     this.metallic = this.skin.getButton("metallic");
/*  721 */     this.metallic.addActionListener(this);
/*  722 */     this.colorBond = this.skin.getButton("colorBond");
/*  723 */     this.colorBond.addActionListener(this);
/*  724 */     this.riverRock = this.skin.getButton("riverRock");
/*  725 */     this.riverRock.addActionListener(this);
/*  726 */     this.designer = this.skin.getButton("designer");
/*  727 */     this.designer.addActionListener(this);
/*  728 */     this.render = this.skin.getButton("render");
/*  729 */     this.render.addActionListener(this);
/*  730 */     this.sprayFast = this.skin.getButton("sprayFast");
/*  731 */     this.sprayFast.addActionListener(this);
/*  732 */     this.quitRust = this.skin.getButton("quitRust");
/*  733 */     this.quitRust.addActionListener(this);
/*  734 */     this.garageFloors = this.skin.getButton("garageFloors");
/*  735 */     this.garageFloors.addActionListener(this);
/*  736 */     this.weatherShieldGardenShades = this.skin.getButton("weatherShieldGardenShades");
/*  737 */     this.weatherShieldGardenShades.addActionListener(this);
/*  738 */     this.weatherShieldRoofandTrim = this.skin.getButton("weatherShieldRoofandTrim");
/*  739 */     this.weatherShieldRoofandTrim.addActionListener(this);
/*      */ 
/*  743 */     this.specialtyImages.put(this.suede, Integer.valueOf(0));
/*  744 */     this.specialtyImages.put(this.tuscan, Integer.valueOf(1));
/*  745 */     this.specialtyImages.put(this.metallic, Integer.valueOf(2));
/*  746 */     this.specialtyImages.put(this.colorBond, Integer.valueOf(3));
/*  747 */     this.specialtyImages.put(this.riverRock, Integer.valueOf(4));
/*  748 */     this.specialtyImages.put(this.designer, Integer.valueOf(5));
/*  749 */     this.specialtyImages.put(this.render, Integer.valueOf(6));
/*  750 */     this.specialtyImages.put(this.sprayFast, Integer.valueOf(7));
/*  751 */     this.specialtyImages.put(this.quitRust, Integer.valueOf(8));
/*  752 */     this.specialtyImages.put(this.garageFloors, Integer.valueOf(9));
/*  753 */     this.specialtyImages.put(this.weatherShieldGardenShades, Integer.valueOf(10));
/*  754 */     this.specialtyImages.put(this.weatherShieldRoofandTrim, Integer.valueOf(11));
/*      */ 
/*  756 */     MouseListener specialtyMouseListener = new MouseAdapter()
/*      */     {
/*      */       public void mouseEntered(MouseEvent e) {
/*  759 */         SkinnedButton s = (SkinnedButton)e.getSource();
/*  760 */         s.setOver(true);
/*      */       }
/*      */     };
/*  763 */     MouseListener specialtyItemMouseListener = new MouseAdapter()
/*      */     {
/*      */       public void mouseExited(MouseEvent e) {
/*  766 */         SkinnedButton s = (SkinnedButton)e.getSource();
/*  767 */         s.setOver(false);
/*      */       }
/*      */     };
/*  770 */     for (Iterator it = this.specialtyImages.entrySet().iterator(); it.hasNext(); ) {
/*  771 */       Map.Entry entry = (Map.Entry)it.next();
/*  772 */       Widget item = (Widget)entry.getKey();
/*  773 */       item.addMouseListener(specialtyMouseListener);
/*  774 */       item.addMouseListener(specialtyItemMouseListener);
/*      */     }
/*      */ 
/*  777 */     this.editMaskLayer = this.skin.getLayer("editMaskLayer");
/*  778 */     this.editinglabel = this.skin.getLabel("editingLabel");
/*  779 */     this.editinglabel.addLabelListener(this);
/*  780 */     this.editinglabel.setEditable(true);
/*      */ 
/*  783 */     this.changeSelectedMaskButton1 = this.skin.getButton("changeMaskColour1");
/*  784 */     this.changeSelectedMaskButton1.addActionListener(this);
/*  785 */     this.changeSelectedMaskButton2 = this.skin.getButton("changeMaskColour2");
/*  786 */     this.changeSelectedMaskButton2.addActionListener(this);
/*  787 */     this.changeSelectedMaskButton3 = this.skin.getButton("changeMaskColour3");
/*  788 */     this.changeSelectedMaskButton3.addActionListener(this);
/*  789 */     this.closeEditMaskButton = this.skin.getButton("closeEditbutton");
/*  790 */     this.closeEditMaskButton.addActionListener(this);
/*      */ 
/*  792 */     this.buttons = this.skin.getLayer("buttons");
/*      */ 
/*  794 */     this.coloursLayer = this.skin.getLayer("coloursLayer");
/*  795 */     this.thumbnailLayer = this.skin.getLayer("thumbnailLayer");
/*      */ 
/*  797 */     this.loadProject = this.skin.getButton("loadproject");
/*  798 */     this.loadProject.addActionListener(this);
/*  799 */     this.rightButton = this.skin.getButton("slideright");
/*  800 */     this.rightButton.addActionListener(this);
/*  801 */     this.leftButton = this.skin.getButton("slideleft");
/*  802 */     this.leftButton.addActionListener(this);
/*  803 */     this.downButton = this.skin.getButton("slidedown");
/*  804 */     this.downButton.addActionListener(this);
/*  805 */     this.upButton = this.skin.getButton("slideup");
/*  806 */     this.upButton.addActionListener(this);
/*  807 */     this.backToWallButton = this.skin.getButton("closecolours");
/*  808 */     this.backToWallButton.addActionListener(this);
/*  809 */     this.backToImageButton = this.skin.getButton("closewall");
/*  810 */     this.backToImageButton.addActionListener(this);
/*  811 */     this.wallImages = new HashMap();
/*  812 */     this.reds = this.skin.getButton("reds");
/*  813 */     this.wallImages.put(this.reds, Integer.valueOf(0));
/*  814 */     this.reds.addActionListener(this);
/*  815 */     this.oranges = this.skin.getButton("oranges");
/*  816 */     this.wallImages.put(this.oranges, Integer.valueOf(1));
/*  817 */     this.oranges.addActionListener(this);
/*  818 */     this.browns = this.skin.getButton("browns");
/*  819 */     this.wallImages.put(this.browns, Integer.valueOf(2));
/*  820 */     this.browns.addActionListener(this);
/*  821 */     this.neutrals = this.skin.getButton("neutrals");
/*  822 */     this.wallImages.put(this.neutrals, Integer.valueOf(3));
/*  823 */     this.neutrals.addActionListener(this);
/*  824 */     this.yellows = this.skin.getButton("yellows");
/*  825 */     this.wallImages.put(this.yellows, Integer.valueOf(4));
/*  826 */     this.yellows.addActionListener(this);
/*  827 */     this.greens = this.skin.getButton("greens");
/*  828 */     this.wallImages.put(this.greens, Integer.valueOf(5));
/*  829 */     this.greens.addActionListener(this);
/*  830 */     this.blues = this.skin.getButton("blues");
/*  831 */     this.wallImages.put(this.blues, Integer.valueOf(6));
/*  832 */     this.blues.addActionListener(this);
/*  833 */     this.purples = this.skin.getButton("purples");
/*  834 */     this.wallImages.put(this.purples, Integer.valueOf(7));
/*  835 */     this.purples.addActionListener(this);
/*      */ 
/*  837 */     MouseListener colourWallMouseListener = new MouseAdapter()
/*      */     {
/*      */       public void mouseEntered(MouseEvent e) {
/*  840 */         SkinnedButton s = (SkinnedButton)e.getSource();
/*  841 */         s.setOver(true);
/*      */       }
/*      */     };
/*  844 */     MouseListener colourWallItemMouseListener = new MouseAdapter()
/*      */     {
/*      */       public void mouseExited(MouseEvent e) {
/*  847 */         SkinnedButton s = (SkinnedButton)e.getSource();
/*  848 */         s.setOver(false);
/*      */       }
/*      */     };
/*  851 */     for (Iterator it = this.wallImages.entrySet().iterator(); it.hasNext(); ) {
/*  852 */       Map.Entry entry = (Map.Entry)it.next();
/*  853 */       Widget item = (Widget)entry.getKey();
/*  854 */       item.addMouseListener(colourWallMouseListener);
/*  855 */       item.addMouseListener(colourWallItemMouseListener);
/*      */     }
/*      */ 
/*  859 */     this.addLayerButton1 = this.skin.getButton("addLayer1");
/*  860 */     this.addLayerButton1.addActionListener(this);
/*  861 */     this.addLayerButton2 = this.skin.getButton("addLayer2");
/*  862 */     this.addLayerButton2.addActionListener(this);
/*  863 */     this.addLayerButton3 = this.skin.getButton("addLayer3");
/*  864 */     this.addLayerButton3.addActionListener(this);
/*      */ 
/*  866 */     this.changeSwatchButton1 = this.skin.getButton("changeSwatch1");
/*  867 */     this.changeSwatchButton1.addActionListener(this);
/*  868 */     this.changeSwatchButton2 = this.skin.getButton("changeSwatch2");
/*  869 */     this.changeSwatchButton2.addActionListener(this);
/*  870 */     this.changeSwatchButton3 = this.skin.getButton("changeSwatch3");
/*  871 */     this.changeSwatchButton3.addActionListener(this);
/*      */ 
/*  873 */     this.schemePopupCloseButton = this.skin.getButton("closebutton");
/*  874 */     this.schemePopupCloseButton.addActionListener(this);
/*  875 */     this.selectedLayer = this.skin.getLayer("selectedLayer");
/*  876 */     this.selectedName = this.skin.getLabel("selectedName");
/*      */ 
/*  878 */     this.thumbnailPlaceHolder = this.skin.getPlaceholder("thumbnail");
/*  879 */     this.original0 = this.skin.getButton("original0");
/*  880 */     this.original0.addActionListener(this);
/*  881 */     this.base_1 = this.skin.getButton("original1");
/*  882 */     this.base_1.addActionListener(this);
/*  883 */     this.original2 = this.skin.getButton("original2");
/*  884 */     this.original2.addActionListener(this);
/*  885 */     this.original3 = this.skin.getButton("original3");
/*  886 */     this.original3.addActionListener(this);
/*  887 */     this.original4 = this.skin.getButton("original4");
/*  888 */     this.original4.addActionListener(this);
/*  889 */     this.original5 = this.skin.getButton("original5");
/*  890 */     this.original5.addActionListener(this);
/*  891 */     this.base_2 = this.skin.getButton("base_1");
/*  892 */     this.base_2.addActionListener(this);
/*  893 */     this.base_3 = this.skin.getButton("base_2");
/*  894 */     this.base_3.addActionListener(this);
/*  895 */     this.interior1_1 = this.skin.getButton("interior1_1");
/*  896 */     this.interior1_1.addActionListener(this);
/*  897 */     this.interior1_2 = this.skin.getButton("interior1_2");
/*  898 */     this.interior1_2.addActionListener(this);
/*  899 */     this.interior2_1 = this.skin.getButton("interior2_1");
/*  900 */     this.interior2_1.addActionListener(this);
/*  901 */     this.interior2_2 = this.skin.getButton("interior2_2");
/*  902 */     this.interior2_2.addActionListener(this);
/*  903 */     this.exterior1_1 = this.skin.getButton("exterior1_1");
/*  904 */     this.exterior1_1.addActionListener(this);
/*  905 */     this.exterior1_2 = this.skin.getButton("exterior1_2");
/*  906 */     this.exterior1_2.addActionListener(this);
/*  907 */     this.exterior2_1 = this.skin.getButton("exterior2_1");
/*  908 */     this.exterior2_1.addActionListener(this);
/*  909 */     this.exterior2_2 = this.skin.getButton("exterior2_2");
/*  910 */     this.exterior2_2.addActionListener(this);
/*      */ 
/*  912 */     this.eraserButton = this.skin.getButton("eraser");
/*  913 */     this.eraserButton.addActionListener(this);
/*      */ 
/*  917 */     this.loadButton = this.skin.getButton("load");
/*  918 */     this.loadButton.addActionListener(this);
/*      */ 
/*  920 */     this.saveToFileButton = this.skin.getButton("save");
/*  921 */     this.saveToFileButton.addActionListener(this);
/*      */ 
/*  923 */     this.saveToWebsiteButton = this.skin.getButton("saveToWebsite");
/*  924 */     this.saveToWebsiteButton.addActionListener(this);
/*      */ 
/*  926 */     this.paintButton = this.skin.getButton("paint");
/*  927 */     this.paintButton.addActionListener(this);
/*      */ 
/*  929 */     this.maskButton = this.skin.getButton("mask");
/*  930 */     this.maskButton.addActionListener(this);
/*      */ 
/*  932 */     this.deleteButton = this.skin.getButton("delete");
/*  933 */     this.deleteButton.addActionListener(this);
/*      */ 
/*  935 */     this.eraserSlider = this.skin.getSlider("eraserSizeSlider");
/*  936 */     this.eraserSlider.addAdjustmentListener(this);
/*  937 */     this.eraserSlider.setValue(5);
/*      */ 
/*  939 */     this.blurSlider = this.skin.getSlider("blurSlider");
/*  940 */     this.blurSlider.addAdjustmentListener(this);
/*      */ 
/*  942 */     this.undoButton = this.skin.getButton("undo");
/*  943 */     this.undoButton.addActionListener(this);
/*      */ 
/*  945 */     this.redoButton = this.skin.getButton("redo");
/*  946 */     this.redoButton.addActionListener(this);
/*      */ 
/*  948 */     this.zoomInButton = this.skin.getButton("zoom_in");
/*  949 */     this.zoomInButton.addActionListener(this);
/*      */ 
/*  951 */     this.zoomOutButton = this.skin.getButton("zoom_out");
/*  952 */     this.zoomOutButton.addActionListener(this);
/*      */ 
/*  954 */     this.progress = this.skin.getProgress("progress");
/*  955 */     this.progressLabel = this.skin.getLabel("progressLabel");
/*  956 */     this.progressLayer = this.skin.getLayer("progressLayer");
/*      */ 
/*  958 */     this.maskList = this.skin.getList("maskList");
/*  959 */     this.maskList.setEditable(true);
/*  960 */     this.maskList.addListListener(this);
/*      */ 
/*  962 */     this.errorLayer = this.skin.getLayer("errorLayer");
/*  963 */     this.errorOkButton = this.skin.getButton("errorOk");
/*  964 */     this.errorOkButton.addActionListener(this);
/*  965 */     this.errorLabel = this.skin.getLabel("errorLabel");
/*      */ 
/*  967 */     this.yesNoLayer = this.skin.getLayer("yesOrNo");
/*  968 */     this.yesButton = this.skin.getButton("addMaskYes");
/*  969 */     this.yesButton.addActionListener(this);
/*  970 */     this.noButton = this.skin.getButton("addMaskNo");
/*  971 */     this.noButton.addActionListener(this);
/*  972 */     this.yesNoLabel = this.skin.getLabel("choiceAddMaskLabel");
/*      */ 
/*  974 */     this.colourWallLayer = this.skin.getLayer("colourwallLayer");
/*      */ 
/*  976 */     this.helpLayer = this.skin.getLayer("helpLayer");
/*  977 */     this.helpImages = new HashMap();
/*  978 */     this.helpImages.put(this.skin.getWidget("helpPaintIcon"), this.skin.getWidget("helpPaint"));
/*      */ 
/*  981 */     this.helpImages.put(this.skin.getWidget("helpUndoIcon"), this.skin.getWidget("helpUndo"));
/*      */ 
/*  983 */     this.helpImages.put(this.skin.getWidget("helpLoadIcon"), this.skin.getWidget("helpLoad"));
/*      */ 
/*  985 */     this.helpImages.put(this.skin.getWidget("helpZoomIcon"), this.skin.getWidget("helpZoom"));
/*      */ 
/*  987 */     this.helpImages.put(this.skin.getWidget("helpEraseIcon"), this.skin.getWidget("helpErase"));
/*      */ 
/*  989 */     this.helpImages.put(this.skin.getWidget("helpChangeIcon"), this.skin.getWidget("helpChange"));
/*      */ 
/*  991 */     this.helpImages.put(this.skin.getWidget("helpAddIcon"), this.skin.getWidget("helpAdd"));
/*      */ 
/*  993 */     this.helpImages.put(this.skin.getWidget("helpMaskIcon"), this.skin.getWidget("helpMask"));
/*      */ 
/*  995 */     this.helpImages.put(this.skin.getWidget("helpColourIcon"), this.skin.getWidget("helpColour"));
/*      */ 
/*  997 */     this.helpImages.put(this.skin.getWidget("helpLayerIcon"), this.skin.getWidget("helpLayer"));
/*      */ 
/*  999 */     this.helpImages.put(this.skin.getWidget("helpLightingIcon"), this.skin.getWidget("helpLighting"));
/*      */ 
/* 1001 */     this.helpImages.put(this.skin.getWidget("helpEditIcon"), this.skin.getWidget("helpEdit"));
/*      */ 
/* 1003 */     this.helpImages.put(this.skin.getWidget("helpDeleteIcon"), this.skin.getWidget("helpDelete"));
/*      */ 
/* 1006 */     MouseListener helpIconMouseListener = new MouseAdapter()
/*      */     {
/*      */       public void mouseEntered(MouseEvent e) {
/* 1009 */         Iterator it = DuluxMyColour.this.helpImages.values().iterator();
/* 1010 */         while (it.hasNext()) {
/* 1011 */           ((Widget)it.next()).setVisible(false);
/*      */         }
/* 1013 */         Widget w = (Widget)DuluxMyColour.this.helpImages.get(e.getSource());
/* 1014 */         w.setVisible(true);
/*      */       }
/*      */     };
/* 1017 */     MouseListener helpItemMouseListener = new MouseAdapter()
/*      */     {
/*      */       public void mouseExited(MouseEvent e) {
/* 1020 */         Widget w = (Widget)e.getSource();
/* 1021 */         w.setVisible(false);
/*      */       }
/*      */     };
/* 1024 */     for (Iterator it = this.helpImages.entrySet().iterator(); it.hasNext(); ) {
/* 1025 */       Map.Entry entry = (Map.Entry)it.next();
/* 1026 */       Widget icon = (Widget)entry.getKey();
/* 1027 */       icon.addMouseListener(helpIconMouseListener);
/* 1028 */       Widget item = (Widget)entry.getValue();
/* 1029 */       item.addMouseListener(helpItemMouseListener);
/*      */     }
/*      */ 
/* 1032 */     this.lightingLayer = this.skin.getLayer("lightingLayer");
/*      */ 
/* 1035 */     this.lightingList = ((SkinnedPopupList)this.skin.getList("lightingList"));
/* 1036 */     this.lightingList.add("DAYLIGHT", new DaylightLighting());
/* 1037 */     this.lightingList.add("HALOGEN", new HalogenLighting());
/* 1038 */     this.lightingList.add("INCANDESCENT", new IncandescentLighting());
/* 1039 */     this.lightingList.add("FLUORESCENT", new FluorescentLighting());
/* 1040 */     this.lightingList.addListListener(this);
/*      */ 
/* 1042 */     Widget lightingRegion = this.skin.getWidget("lightingHit");
/* 1043 */     lightingRegion.addMouseListener(new MouseAdapter()
/*      */     {
/*      */       public void mousePressed(MouseEvent e) {
/* 1046 */         DuluxMyColour.this.lightingLayer.setVisible(true);
/* 1047 */         DuluxMyColour.this.lightingList.popup(e);
/*      */       }
/*      */     });
/* 1051 */     this.lightingList.addListListener(new ListListener()
/*      */     {
/*      */       public void listModified(ListEvent e)
/*      */       {
/*      */       }
/*      */ 
/*      */       public void itemHighlighted(ListEvent e) {
/*      */       }
/*      */ 
/*      */       public void itemRenamed(ListEvent e, SkinnedListItem item, String name) {
/*      */       }
/*      */ 
/*      */       public void itemSelected(ListEvent e) {
/* 1064 */         DuluxMyColour.this.lightingLayer.setVisible(false);
/* 1065 */         String label = ((SkinnedListTextItem)DuluxMyColour.this.lightingList.getSelectedItem()).label;
/* 1066 */         DuluxMyColour.this.skin.getLabel("lightingLabel").setLabel(label);
/*      */       }
/*      */     });
/* 1069 */     Widget lightingRegion2 = this.skin.getWidget("lightingHit2");
/* 1070 */     lightingRegion2.addMouseListener(new MouseAdapter()
/*      */     {
/*      */       public void mousePressed(MouseEvent e) {
/* 1073 */         DuluxMyColour.this.chooseLightingLayer.setVisible(true);
/* 1074 */         DuluxMyColour.this.lightingList2.popup(e);
/*      */       }
/*      */     });
/* 1078 */     this.chooseLightingLayer = this.skin.getLayer("lightingLayer2");
/*      */ 
/* 1081 */     this.lightingList2 = ((SkinnedPopupList)this.skin.getList("lightingList2"));
/* 1082 */     this.lightingList2.add("DAYLIGHT", new DaylightLighting());
/* 1083 */     this.lightingList2.add("HALOGEN", new HalogenLighting());
/* 1084 */     this.lightingList2.add("INCANDESCENT", new IncandescentLighting());
/* 1085 */     this.lightingList2.add("FLUORESCENT", new FluorescentLighting());
/* 1086 */     this.lightingList2.addListListener(this);
/*      */ 
/* 1088 */     this.lightingList2.addListListener(new ListListener()
/*      */     {
/*      */       public void listModified(ListEvent e)
/*      */       {
/*      */       }
/*      */ 
/*      */       public void itemHighlighted(ListEvent e) {
/*      */       }
/*      */ 
/*      */       public void itemRenamed(ListEvent e, SkinnedListItem item, String name) {
/*      */       }
/*      */ 
/*      */       public void itemSelected(ListEvent e) {
/* 1101 */         DuluxMyColour.this.chooseLightingLayer.setVisible(false);
/* 1102 */         String label = ((SkinnedListTextItem)DuluxMyColour.this.lightingList2.getSelectedItem()).label;
/* 1103 */         DuluxMyColour.this.skin.getLabel("selectLightingLabel").setLabel(label);
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   private void checkVersion()
/*      */   {
/* 1144 */     String java = System.getProperty("java.version");
/* 1145 */     String[] parts = java.split("[._]");
/* 1146 */     int major = 0;
/* 1147 */     int minor = 0;
/*      */     try {
/* 1149 */       major = Integer.parseInt(parts[0]);
/* 1150 */       minor = Integer.parseInt(parts[1]);
/*      */     }
/*      */     catch (Throwable e)
/*      */     {
/*      */     }
/* 1155 */     if (((major < 1) || (minor < 5)) && (major <= 1)) {
/* 1156 */       new MessageBox(this.frame, "Dulux MyColour requires Java 1.5 or later to run.  You can download the latest version from  http://www.java.com/").show();
/*      */ 
/* 1158 */       System.exit(1);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void initPaintExplorer()
/*      */   {
/* 1165 */     String os = System.getProperty("os.name").toLowerCase();
/*      */     String logFilename;
/* 1166 */     if (os.indexOf("windows") != -1) {
/* 1167 */       logFilename = "c:\\mycolour.log";
/*      */     }
/*      */     else
/*      */     {
/* 1168 */       if (os.indexOf("mac") != -1)
/* 1169 */         logFilename = "/tmp/mycolour.log";
/*      */       else {
/* 1171 */         logFilename = "mycolour.log";
/*      */       }
/*      */     }
/*      */ 
/* 1175 */     Properties props = new Properties();
/* 1176 */     props.setProperty("nominalWidth", "1024");
/* 1177 */     props.setProperty("nominalHeight", "768");
/* 1178 */     props.setProperty("enableLog", "true");
/* 1179 */     props.setProperty("enableSelfCheck", "true");
/* 1180 */     props.setProperty("logFilename", logFilename);
/* 1181 */     this.explorer = new PaintExplorer(props);
/* 1182 */     this.explorer.addListener(this);
/*      */ 
/* 1184 */     SkinnedPlaceholder pelibPlaceholder = this.skin.getPlaceholder("pelib");
/* 1185 */     this.view = new DuluxPaintView("", pelibPlaceholder.getX(), pelibPlaceholder.getY(), pelibPlaceholder.getWidth(), pelibPlaceholder.getHeight(), this.explorer);
/*      */ 
/* 1192 */     pelibPlaceholder.replace(this.view);
/*      */ 
/* 1195 */     Image imageThumbnail = this.explorer.getThumbnail(50, 50).getAWTImage();
/* 1196 */     this.thumbnail = new SkinnedImage("", this.thumbnailPlaceHolder.getX(), this.thumbnailPlaceHolder.getY(), imageThumbnail);
/*      */ 
/* 1200 */     this.thumbnailPlaceHolder.replace(this.thumbnail);
/* 1201 */     this.thumbnailPlaceHolder.setVisible(false);
/*      */ 
/* 1206 */     this.paintManager = new ExpertPaintManager(this.explorer);
/* 1207 */     this.explorer.setPaintManager(this.paintManager);
/*      */ 
/* 1210 */     this.input = new DuluxPaintInput(this.explorer, this.view, this);
/* 1211 */     this.inputLayer = this.input.getInputLayer();
/* 1212 */     this.skin.addKeyListener(this.input);
/*      */   }
/*      */ 
/*      */   private void loadPalettes()
/*      */     throws IOException
/*      */   {
/* 1219 */     if (this.IS_NZ) {
/* 1220 */       this.paletteManager.loadColoursOfNZ();
/* 1221 */       this.paletteManager.loadSpecifier();
/*      */     }
/*      */     else {
/* 1224 */       this.paletteManager.loadColourWall();
/*      */     }
/*      */ 
/* 1227 */     SkinnedPlaceholder palettePlaceholder = this.skin.getPlaceholder("palette");
/* 1228 */     this.paletteView = new DuluxPaletteView("", palettePlaceholder.getX(), palettePlaceholder.getY(), 650, 350, 7, 10);
/* 1229 */     this.paletteView.addListener(this);
/* 1230 */     palettePlaceholder.replace(this.paletteView);
/*      */   }
/*      */ 
/*      */   private void createDefaultLayers()
/*      */   {
/* 1235 */     this.layerCount = 1;
/* 1236 */     for (int i = 0; i < this.myColours.length; i++) {
/* 1237 */       this.explorer.createMask(this.myColours[i].colour, "Area " + this.layerCount, this.myColours[i].getId(), i);
/* 1238 */       this.layerCount += 1;
/*      */     }
/*      */ 
/* 1241 */     this.maskList.setSelectedItem(0);
/* 1242 */     Mask mask = (Mask)this.maskList.getSelectedItem().getData();
/* 1243 */     this.paintManager.setSelectedMask(mask);
/* 1244 */     this.explorer.log("DuluxMyColour: Selected mask " + mask);
/* 1245 */     this.undoButton.setEnabled(this.explorer.canUndo());
/* 1246 */     this.lastSelected = ((Mask)this.maskList.getSelectedItem().getData());
/*      */   }
/*      */ 
/*      */   private void loadMyProjectColours()
/*      */   {
/* 1253 */     this.myColours = this.saveAndLoad.getSchemeColours(this.SESSIONID);
/*      */ 
/* 1255 */     if (this.myColours == null)
/*      */     {
/* 1257 */       this.myColours = new DuluxColour[3];
/* 1258 */       this.myColours[0] = this.paletteManager.getColour(this.colour1);
/* 1259 */       this.myColours[1] = this.paletteManager.getColour(this.colour2);
/* 1260 */       this.myColours[2] = this.paletteManager.getColour(this.colour3);
/*      */     }
/* 1262 */     if (this.myColours[0] == null)
/* 1263 */       this.myColours[0] = this.paletteManager.getColour(this.colour1);
/* 1264 */     if (this.myColours[1] == null)
/* 1265 */       this.myColours[1] = this.paletteManager.getColour(this.colour2);
/* 1266 */     if (this.myColours[2] == null) {
/* 1267 */       this.myColours[2] = this.paletteManager.getColour(this.colour3);
/*      */     }
/*      */ 
/* 1270 */     this.colour1Button = this.skin.getButton("colour1");
/* 1271 */     this.colour1Button.addActionListener(this);
/*      */ 
/* 1273 */     this.colour2Button = this.skin.getButton("colour2");
/* 1274 */     this.colour2Button.addActionListener(this);
/*      */ 
/* 1276 */     this.colour3Button = this.skin.getButton("colour3");
/* 1277 */     this.colour3Button.addActionListener(this);
/* 1278 */     this.colour1Button.setColour(this.myColours[0]);
/* 1279 */     this.colour2Button.setColour(this.myColours[1]);
/* 1280 */     this.colour3Button.setColour(this.myColours[2]);
/*      */ 
/* 1282 */     //this.myColours[0].palette = null;
/* 1283 */     //this.myColours[1].palette = null;
/* 1284 */     //this.myColours[2].palette = null;
/*      */   }
/*      */ 
/*      */   private void createWidgetGroups()
/*      */   {
/* 1294 */     this.allWidgets = new WidgetGroup();
/* 1295 */     this.allWidgets.add(this.loadButton);
/* 1296 */     this.allWidgets.add(this.saveToFileButton);
/* 1297 */     this.allWidgets.add(this.paintButton);
/* 1298 */     this.allWidgets.add(this.maskButton);
/* 1299 */     this.allWidgets.add(this.deleteButton);
/* 1300 */     this.allWidgets.add(this.blurSlider);
/* 1301 */     this.allWidgets.add(this.eraserSlider);
/* 1302 */     this.allWidgets.add(this.undoButton);
/* 1303 */     this.allWidgets.add(this.redoButton);
/* 1304 */     this.allWidgets.add(this.zoomInButton);
/* 1305 */     this.allWidgets.add(this.zoomOutButton);
/* 1306 */     this.allWidgets.add(this.colour1Button);
/* 1307 */     this.allWidgets.add(this.colour2Button);
/* 1308 */     this.allWidgets.add(this.colour3Button);
/* 1309 */     this.allWidgets.add(this.centreButton);
/* 1310 */     this.allWidgets.add(this.saveToWebsiteButton);
/*      */ 
/* 1312 */     this.tipsLayerWidgets = new WidgetGroup();
/* 1313 */     this.tipsLayerWidgets.add(this.tipsMinimizedButton);
/* 1314 */     this.tipsLayerWidgets.add(this.tipsPaintButton);
/* 1315 */     this.tipsLayerWidgets.add(this.tipsMaskButton);
/* 1316 */     this.tipsLayerWidgets.add(this.tipsEraserButton);
/*      */ 
/* 1318 */     this.colourWallArrowWidgets = new WidgetGroup();
/* 1319 */     this.colourWallArrowWidgets.add(this.upButton);
/* 1320 */     this.colourWallArrowWidgets.add(this.downButton);
/* 1321 */     this.colourWallArrowWidgets.add(this.leftButton);
/* 1322 */     this.colourWallArrowWidgets.add(this.rightButton);
/*      */ 
/* 1324 */     this.bottomWidgets = new WidgetGroup();
/* 1325 */     this.bottomWidgets.add(this.zoomInButton);
/* 1326 */     this.bottomWidgets.add(this.zoomOutButton);
/* 1327 */     this.bottomWidgets.add(this.centreButton);
/*      */ 
/* 1329 */     this.bottomWidgets.add(this.view);
/*      */ 
/* 1336 */     this.alwaysReadyWidgets = new WidgetGroup();
/* 1337 */     this.alwaysReadyWidgets.add(this.loadButton);
/* 1338 */     this.alwaysReadyWidgets.add(this.colour1Button);
/* 1339 */     this.alwaysReadyWidgets.add(this.colour2Button);
/* 1340 */     this.alwaysReadyWidgets.add(this.colour3Button);
/*      */ 
/* 1342 */     this.requireImageWidgets = new WidgetGroup();
/* 1343 */     this.requireImageWidgets.add(this.saveToFileButton);
/* 1344 */     this.requireImageWidgets.add(this.paintButton);
/* 1345 */     this.requireImageWidgets.add(this.maskButton);
/* 1346 */     this.requireImageWidgets.add(this.undoButton);
/* 1347 */     this.requireImageWidgets.add(this.redoButton);
/* 1348 */     this.requireImageWidgets.add(this.zoomInButton);
/* 1349 */     this.requireImageWidgets.add(this.zoomOutButton);
/* 1350 */     this.requireImageWidgets.add(this.centreButton);
/* 1351 */     this.requireImageWidgets.add(this.saveToWebsiteButton);
/* 1352 */     this.requireImageWidgets.add(this.eraserSlider);
/* 1353 */     this.requireImageWidgets.add(this.eraserButton);
/* 1354 */     this.requireImageWidgets.add(this.addLayerButton1);
/* 1355 */     this.requireImageWidgets.add(this.addLayerButton2);
/* 1356 */     this.requireImageWidgets.add(this.addLayerButton3);
/* 1357 */     this.requireImageWidgets.add(this.changeSwatchButton1);
/* 1358 */     this.requireImageWidgets.add(this.changeSwatchButton2);
/* 1359 */     this.requireImageWidgets.add(this.changeSwatchButton3);
/* 1360 */     this.requireImageWidgets.add(this.editLayerButton);
/* 1361 */     this.requireImageWidgets.add(this.saveToWebsiteButton);
/* 1362 */     this.requireImageWidgets.add(this.colour1Button);
/* 1363 */     this.requireImageWidgets.add(this.colour2Button);
/* 1364 */     this.requireImageWidgets.add(this.colour3Button);
/* 1365 */     this.requireImageWidgets.add(this.deleteButton);
/* 1366 */     this.requireImageWidgets.add(this.paintButton);
/* 1367 */     this.requireImageWidgets.add(this.eraserButton);
/* 1368 */     this.requireImageWidgets.add(this.maskButton);
/*      */ 
/* 1370 */     this.requireMaskWidgets = new WidgetGroup();
/*      */ 
/* 1372 */     this.requireLayerSelectedWidgets = new WidgetGroup();
/* 1373 */     this.requireLayerSelectedWidgets.add(this.deleteButton);
/* 1374 */     this.requireLayerSelectedWidgets.add(this.blurSlider);
/* 1375 */     this.requireLayerSelectedWidgets.add(this.eraserButton);
/*      */ 
/* 1377 */     this.atLeastOneWidgets = new WidgetGroup();
/* 1378 */     this.atLeastOneWidgets.add(this.paintButton);
/* 1379 */     this.atLeastOneWidgets.add(this.maskButton);
/* 1380 */     this.atLeastOneWidgets.add(this.eraserButton);
/*      */ 
/* 1382 */     this.requireColour1Group = new WidgetGroup();
/* 1383 */     this.requireColour1Group.add(this.changeSwatchButton1);
/* 1384 */     this.requireColour1Group.add(this.addLayerButton1);
/*      */ 
/* 1386 */     this.requireColour2Group = new WidgetGroup();
/* 1387 */     this.requireColour2Group.add(this.changeSwatchButton2);
/* 1388 */     this.requireColour2Group.add(this.addLayerButton2);
/*      */ 
/* 1390 */     this.requireColour3Group = new WidgetGroup();
/* 1391 */     this.requireColour3Group.add(this.changeSwatchButton3);
/* 1392 */     this.requireColour3Group.add(this.addLayerButton3);
/*      */ 
/* 1394 */     this.thumbnails = new WidgetGroup();
/* 1395 */     this.thumbnails.add(this.thumbnail1Button);
/* 1396 */     this.thumbnails.add(this.thumbnail2Button);
/* 1397 */     this.thumbnails.add(this.thumbnail3Button);
/*      */   }
/*      */ 
/*      */   private void updateTitle()
/*      */   {
/* 1402 */     if (this.frame == null) {
/* 1403 */       return;
/*      */     }
/*      */ 
/* 1406 */     if (this.filename == null)
/* 1407 */       this.frame.setTitle("Dulux MyColour");
/*      */     else
/* 1409 */       this.frame.setTitle(this.filename + " - Dulux MyColour");
/*      */   }
/*      */ 
/*      */   private void updateUI()
/*      */   {
/* 1414 */     if (this.isBusy) {
/* 1415 */       this.allWidgets.setEnabled(false);
/*      */     } else {
/* 1417 */       this.alwaysReadyWidgets.setEnabled(true);
/*      */ 
/* 1419 */       if (!this.explorer.isReady()) {
/* 1420 */         this.view.setEnabled(false);
/* 1421 */         this.requireImageWidgets.setEnabled(false);
/* 1422 */         this.requireMaskWidgets.setEnabled(false);
/* 1423 */       } else if (this.explorer.isReady()) {
/* 1424 */         this.view.setEnabled(true);
/* 1425 */         this.requireImageWidgets.setEnabled(true);
/* 1426 */         this.requireMaskWidgets.setEnabled(this.paintManager.getSelectedMask() != null);
/* 1427 */         this.undoButton.setEnabled(this.explorer.canUndo());
/* 1428 */         this.redoButton.setEnabled(this.explorer.canRedo());
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void load() {
/*      */     try {
/* 1435 */       UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
/*      */     }
/*      */     catch (Exception e)
/*      */     {
/*      */     }
/* 1440 */     JFileChooser dlg = new JFileChooser();
/* 1441 */     if (this.directory != null)
/* 1442 */       dlg.setCurrentDirectory(this.directory);
/*      */     else {
/* 1444 */       dlg.setCurrentDirectory(new File("./Sample Images"));
/*      */     }
/*      */ 
/* 1447 */     dlg.addChoosableFileFilter(new FileFilter()
/*      */     {
/*      */       public boolean accept(File f) {
/* 1450 */         String s = f.toString().toLowerCase();
/* 1451 */         return (f.isDirectory()) || (s.endsWith(".jpg")) || (s.endsWith(".png")) || (s.endsWith(".bmp")) || (s.endsWith(".tif"));
/*      */       }
/*      */ 
/*      */       public String getDescription()
/*      */       {
/* 1459 */         return "Images (*.jpg;*.png;*.bmp;*.tif)";
/*      */       }
/*      */     });
/* 1462 */     dlg.addChoosableFileFilter(new FileFilter()
/*      */     {
/*      */       public boolean accept(File f) {
/* 1465 */         String s = f.toString().toLowerCase();
/* 1466 */         return (f.isDirectory()) || (s.endsWith(".dmc"));
/*      */       }
/*      */ 
/*      */       public String getDescription()
/*      */       {
/* 1471 */         return "Dulux MyColour projects (*.dmc)";
/*      */       }
/*      */     });
/* 1474 */     dlg.addChoosableFileFilter(new FileFilter()
/*      */     {
/*      */       public boolean accept(File f) {
/* 1477 */         String s = f.toString().toLowerCase();
/* 1478 */         return (f.isDirectory()) || (s.endsWith(".jpg")) || (s.endsWith(".png")) || (s.endsWith(".bmp")) || (s.endsWith(".tif")) || (s.endsWith(".dmc"));
/*      */       }
/*      */ 
/*      */       public String getDescription()
/*      */       {
/* 1487 */         return "All recognised file types";
/*      */       }
/*      */     });
/* 1493 */     if (dlg.showOpenDialog(this) == 0) {
/* 1494 */       this.filename = dlg.getSelectedFile().toString();
/* 1495 */       this.directory = dlg.getSelectedFile().getParentFile();
/* 1496 */       load(this.filename);
/*      */     }
/*      */ 
/* 1500 */     this.skin.requestFocus();
/* 1501 */     refreshCursor();
/*      */   }
/*      */ 
/*      */   private void load(final String filename)
/*      */   {
/* 1508 */     this.view.setImage(null);
/* 1509 */     this.view.setEnabled(false);
/* 1510 */     this.maskList.clear();
/* 1511 */     this.isReady = false;
/* 1512 */     Thread t = new Thread()
/*      */     {
/*      */       public void run() {
/*      */         try {
/* 1516 */           if (!DuluxMyColour.this.explorer.load(filename))
/* 1517 */             DuluxMyColour.this.createDefaultLayers();
/*      */         }
/*      */         catch (Throwable e) {
/* 1520 */           DuluxMyColour.this.exceptionOccured(e);
/* 1521 */           DuluxMyColour.this.view.setImage(null);
/* 1522 */           DuluxMyColour.this.updateUI();
/*      */         }
/*      */       }
/*      */     };
/* 1527 */     Thread s = new Thread()
/*      */     {
/*      */       public void run()
/*      */       {
/* 1531 */         int count = 0;
/* 1532 */         Vector colours = new Vector();
/* 1533 */         for (Iterator it = DuluxMyColour.this.explorer.getMasks(); it.hasNext(); ) {
/* 1534 */           Mask mask = (Mask)it.next();
/*      */ 
/* 1536 */           if (!colours.contains(Integer.valueOf(mask.getColour()))) {
/* 1537 */             colours.add(Integer.valueOf(mask.getColour()));
/* 1538 */             int colour = mask.getColour();
/* 1539 */             Color rgb = new Color(colour);
/* 1540 */             String userData = (String)mask.getUserData();
/*      */             try
/*      */             {
/* 1543 */               DuluxMyColour.this.myColours[count] = new DuluxColour(colour, DuluxMyColour.this.BGRToRGB(rgb.getRed(), rgb.getGreen(), rgb.getBlue()), DuluxMyColour.this.paletteManager.getColour(mask.getItemId()).name, null, "", 0, mask.getItemId());
/*      */             }
/*      */             catch (Exception e)
/*      */             {
/*      */             }
/*      */ 
/* 1554 */             count++;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 1560 */         DuluxMyColour.this.requireLayerSelectedWidgets.setEnabled(true);
/*      */         try {
/* 1562 */           DuluxMyColour.this.initialImage = DuluxMyColour.this.explorer.saveImageToStream();
/*      */         } catch (IOException ie) {
/* 1564 */           System.err.println("loadFile error");
/* 1565 */           ie.printStackTrace();
/*      */         }
/*      */       }
/*      */     };
/* 1570 */     Thread r = new Thread()
/*      */     {
/*      */       public void run() {
/* 1573 */         for (Iterator it = DuluxMyColour.this.explorer.getMasks(); it.hasNext(); ) {
/* 1574 */           Mask mask = (Mask)it.next();
/* 1575 */           if (mask.getPositionID() == 0)
/* 1576 */             DuluxMyColour.this.explorer.changeMask(mask, DuluxMyColour.this.myColours[0].colour, (String)mask.getUserData(), DuluxMyColour.this.myColours[0].getId(), 0);
/* 1577 */           else if (mask.getPositionID() == 1)
/* 1578 */             DuluxMyColour.this.explorer.changeMask(mask, DuluxMyColour.this.myColours[1].colour, (String)mask.getUserData(), DuluxMyColour.this.myColours[1].getId(), 1);
/* 1579 */           else if (mask.getPositionID() == 2) {
/* 1580 */             DuluxMyColour.this.explorer.changeMask(mask, DuluxMyColour.this.myColours[2].colour, (String)mask.getUserData(), DuluxMyColour.this.myColours[2].getId(), 2);
/*      */           }
/* 1582 */           //DuluxMyColour.access$1608(DuluxMyColour.this); TODO: for resolving compilation error.
/*      */         }
/*      */         try {
/* 1585 */           DuluxMyColour.this.initialImage = DuluxMyColour.this.explorer.saveImageToStream();
/*      */         } catch (IOException ie) {
/* 1587 */           System.err.print("Save Image to Stream : ");
/* 1588 */           ie.printStackTrace();
/*      */         }
/* 1590 */         DuluxMyColour.this.updateViewAndThumbnail();
/*      */       }
/*      */     };
/*      */     try {
/* 1594 */       t.start();
/* 1595 */       t.join();
/* 1596 */       s.start();
/* 1597 */       s.join();
/* 1598 */       r.start();
/* 1599 */       r.join();
/* 1600 */       this.imageLoaded = true;
/* 1601 */       refreshButtonColours();
/* 1602 */       this.requireImageWidgets.setEnabled(true);
/* 1603 */       colourButtonSelected(this.colour1Button);
/* 1604 */       this.explorer.clearCommandHistory();
/* 1605 */       this.undoButton.setEnabled(this.explorer.canUndo());
/* 1606 */       this.redoButton.setEnabled(this.explorer.canRedo());
/* 1607 */       refreshCursor();
/* 1608 */       this.requireImageWidgets.setEnabled(true);
/*      */     }
/*      */     catch (Exception e) {
/*      */     }
/*      */   }
/*      */ 
/*      */   private void loadProjectFromWeb() {
/* 1615 */     this.layerCount = 1;
/* 1616 */     this.view.setImage(null);
/* 1617 */     this.view.setEnabled(false);
/* 1618 */     this.maskList.clear();
/* 1619 */     this.isReady = false;
/* 1620 */     this.projectLoaded = false;
/*      */ 
/* 1622 */     Thread t = new Thread()
/*      */     {
/*      */       public void run() {
/*      */         try {
/* 1626 */           InputStream is = DuluxMyColour.this.saveAndLoad.loadProject(DuluxMyColour.this.SESSIONID);
/* 1627 */           if (is != null) {
/* 1628 */             if (!DuluxMyColour.this.explorer.loadFromWeb(is)) {
/* 1629 */               DuluxMyColour.this.createDefaultLayers();
/*      */             }
/* 1631 */             DuluxMyColour.this.projectLoaded = true;
/* 1632 */             DuluxMyColour.this.isNewProject = false;
/*      */           } else {
/* 1634 */             DuluxMyColour.this.isNewProject = true;
/* 1635 */             DuluxMyColour.this.projectLoaded = false;
/* 1636 */             DuluxMyColour.this.requireImageWidgets.setEnabled(false);
/*      */           }
/*      */         }
/*      */         catch (Throwable e) {
/* 1640 */           DuluxMyColour.this.exceptionOccured(e);
/* 1641 */           DuluxMyColour.this.view.setImage(null);
/* 1642 */           DuluxMyColour.this.updateUI();
/*      */         }
/*      */       }
/*      */     };
/* 1647 */     Thread r = new Thread()
/*      */     {
/*      */       public void run() {
/* 1650 */         for (Iterator it = DuluxMyColour.this.explorer.getMasks(); it.hasNext(); ) {
/* 1651 */           Mask mask = (Mask)it.next();
/* 1652 */           if (mask.getPositionID() == 0)
/* 1653 */             DuluxMyColour.this.explorer.changeMask(mask, DuluxMyColour.this.myColours[0].colour, (String)mask.getUserData(), DuluxMyColour.this.myColours[0].getId(), 0);
/* 1654 */           else if (mask.getPositionID() == 1)
/* 1655 */             DuluxMyColour.this.explorer.changeMask(mask, DuluxMyColour.this.myColours[1].colour, (String)mask.getUserData(), DuluxMyColour.this.myColours[1].getId(), 1);
/* 1656 */           else if (mask.getPositionID() == 2) {
/* 1657 */             DuluxMyColour.this.explorer.changeMask(mask, DuluxMyColour.this.myColours[2].colour, (String)mask.getUserData(), DuluxMyColour.this.myColours[2].getId(), 2);
/*      */           }
/* 1659 */           //DuluxMyColour.access$1608(DuluxMyColour.this);
/*      */         }
/*      */         try {
/* 1662 */           DuluxMyColour.this.initialImage = DuluxMyColour.this.explorer.saveImageToStream();
/*      */         } catch (IOException ie) {
/* 1664 */           System.err.print("Save Image to Stream : ");
/* 1665 */           ie.printStackTrace();
/*      */         }
/* 1667 */         DuluxMyColour.this.updateViewAndThumbnail();
/*      */       }
/*      */ 
/*      */     };
/*      */     try
/*      */     {
/* 1673 */       t.start();
/* 1674 */       t.join();
/* 1675 */       if (this.projectLoaded) {
/* 1676 */         r.start();
/* 1677 */         r.join();
/* 1678 */         this.requireImageWidgets.setEnabled(true);
/*      */       }
/*      */ 
/* 1681 */       this.paintButton.setSelected(true);
/* 1682 */       this.paintButton.setState(true);
/*      */ 
/* 1684 */       this.input.setMode(0);
/* 1685 */       this.mode = 0;
/* 1686 */       colourButtonSelected(this.colour1Button);
/*      */ 
/* 1688 */       this.imageLoaded = true;
/* 1689 */       refreshButtonColours();
/*      */ 
/* 1691 */       this.explorer.clearCommandHistory();
/* 1692 */       this.undoButton.setEnabled(this.explorer.canUndo());
/* 1693 */       this.redoButton.setEnabled(this.explorer.canRedo());
/*      */     }
/*      */     catch (Exception e) {
/*      */     }
/*      */   }
/*      */ 
/*      */   private void loadProjectFromWeb(String url) {
/* 1700 */     this.dmcURL = url;
/* 1701 */     this.layerCount = 1;
/* 1702 */     this.view.setImage(null);
/*      */ 
/* 1704 */     this.view.setEnabled(false);
/* 1705 */     this.maskList.clear();
/* 1706 */     this.isReady = false;
/* 1707 */     this.projectLoaded = false;
/*      */ 
/* 1709 */     Thread t = new Thread()
/*      */     {
/*      */       public void run() {
/*      */         try {
/* 1713 */           InputStream is = new URL(DuluxMyColour.this.dmcURL).openStream();
/* 1714 */           if (is != null) {
/* 1715 */             if (!DuluxMyColour.this.explorer.loadFromWeb(is)) {
/* 1716 */               DuluxMyColour.this.createDefaultLayers();
/*      */             }
/* 1718 */             DuluxMyColour.this.projectLoaded = true;
/* 1719 */             DuluxMyColour.this.isNewProject = false;
/*      */           } else {
/* 1721 */             DuluxMyColour.this.isNewProject = true;
/* 1722 */             DuluxMyColour.this.projectLoaded = false;
/* 1723 */             DuluxMyColour.this.requireImageWidgets.setEnabled(false);
/*      */           }
/*      */         }
/*      */         catch (Throwable e) {
/* 1727 */           DuluxMyColour.this.exceptionOccured(e);
/* 1728 */           DuluxMyColour.this.view.setImage(null);
/* 1729 */           DuluxMyColour.this.updateUI();
/*      */         }
/*      */       }
/*      */     };
/* 1734 */     Thread r = new Thread()
/*      */     {
/*      */       public void run() {
/* 1737 */         for (Iterator it = DuluxMyColour.this.explorer.getMasks(); it.hasNext(); ) {
/* 1738 */           Mask mask = (Mask)it.next();
/* 1739 */           if (mask.getPositionID() == 0)
/* 1740 */             DuluxMyColour.this.explorer.changeMask(mask, DuluxMyColour.this.myColours[0].colour, (String)mask.getUserData(), DuluxMyColour.this.myColours[0].getId(), 0);
/* 1741 */           else if (mask.getPositionID() == 1)
/* 1742 */             DuluxMyColour.this.explorer.changeMask(mask, DuluxMyColour.this.myColours[1].colour, (String)mask.getUserData(), DuluxMyColour.this.myColours[1].getId(), 1);
/* 1743 */           else if (mask.getPositionID() == 2) {
/* 1744 */             DuluxMyColour.this.explorer.changeMask(mask, DuluxMyColour.this.myColours[2].colour, (String)mask.getUserData(), DuluxMyColour.this.myColours[2].getId(), 2);
/*      */           }
///* 1746 */           DuluxMyColour.access$1608(DuluxMyColour.this);
/*      */         }
/*      */         try {
/* 1749 */           DuluxMyColour.this.initialImage = DuluxMyColour.this.explorer.saveImageToStream();
/*      */         } catch (IOException ie) {
/* 1751 */           System.err.print("Save Image to Stream : ");
/* 1752 */           ie.printStackTrace();
/*      */         }
/* 1754 */         DuluxMyColour.this.updateViewAndThumbnail();
/*      */       }
/*      */     };
/*      */     try
/*      */     {
/* 1759 */       t.start();
/* 1760 */       t.join();
/*      */ 
/* 1763 */       if (this.projectLoaded) {
/* 1764 */         r.start();
/* 1765 */         r.join();
/* 1766 */         this.requireImageWidgets.setEnabled(true);
/*      */       }
/* 1768 */       this.paintButton.setSelected(true);
/* 1769 */       this.paintButton.setState(true);
/*      */ 
/* 1771 */       this.input.setMode(0);
/* 1772 */       this.mode = 0;
/* 1773 */       colourButtonSelected(this.colour1Button);
/* 1774 */       this.skin.addKeyListener(this.input);
/* 1775 */       this.skin.requestFocus();
/*      */ 
/* 1778 */       this.imageLoaded = true;
/* 1779 */       refreshButtonColours();
/*      */ 
/* 1782 */       this.explorer.clearCommandHistory();
/*      */ 
/* 1784 */       this.undoButton.setEnabled(this.explorer.canUndo());
/* 1785 */       this.redoButton.setEnabled(this.explorer.canRedo());
/*      */     } catch (Exception e) {
/*      */     }
/*      */   }
/*      */ 
/*      */   private String getProjectFilename(String filename) {
/* 1791 */     if (filename == null) {
/* 1792 */       return "tempSave.dmc";
/*      */     }
/* 1794 */     if (filename.endsWith(".dmc")) {
/* 1795 */       return filename;
/*      */     }
/*      */ 
/* 1798 */     return "tempSave.dmc";
/*      */   }
/*      */ 
/*      */   public Skin getSkin() {
/* 1802 */     return this.skin;
/*      */   }
/*      */ 
/*      */   private void save() {
/* 1806 */     JFileChooser dlg = new JFileChooser();
/* 1807 */     File file = new File(getProjectFilename(this.filename));
/* 1808 */     dlg.setSelectedFile(file);
/* 1809 */     dlg.addChoosableFileFilter(new FileFilter()
/*      */     {
/*      */       public boolean accept(File f) {
/* 1812 */         String s = f.toString().toLowerCase();
/* 1813 */         return (f.isDirectory()) || (s.endsWith(".jpg"));
/*      */       }
/*      */ 
/*      */       public String getDescription()
/*      */       {
/* 1818 */         return "JPEG image (*.jpg)";
/*      */       }
/*      */     });
/* 1822 */     dlg.addChoosableFileFilter(new FileFilter()
/*      */     {
/*      */       public boolean accept(File f) {
/* 1825 */         String s = f.toString().toLowerCase();
/* 1826 */         return (f.isDirectory()) || (s.endsWith(".dmc"));
/*      */       }
/*      */ 
/*      */       public String getDescription()
/*      */       {
/* 1831 */         return "Dulux MyColour project (*.dmc)";
/*      */       }
/*      */     });
/* 1835 */     if (dlg.showSaveDialog(this) == 0) {
/* 1836 */       this.filename = dlg.getSelectedFile().toString();
/* 1837 */       new Thread()
/*      */       {
/*      */         public void run() {
/*      */           try {
/* 1841 */             DuluxMyColour.this.explorer.save(DuluxMyColour.this.filename);
/* 1842 */             DuluxMyColour.this.updateTitle();
/*      */           } catch (IOException err) {
/* 1844 */             DuluxMyColour.this.exceptionOccured(err);
/*      */           }
/*      */         }
/*      */       }
/* 1837 */       .start();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void refreshButtonColours()
/*      */   {
/* 1853 */     for (Iterator it = this.explorer.getMasks(); it.hasNext(); ) {
/* 1854 */       Mask mask = (Mask)it.next();
/* 1855 */       if ((mask.getPositionID() == 0) && (this.paletteManager.getColour(mask.getItemId()) != null))
/* 1856 */         this.myColours[0] = this.paletteManager.getColour(mask.getItemId());
/* 1857 */       else if ((mask.getPositionID() == 1) && (this.paletteManager.getColour(mask.getItemId()) != null))
/* 1858 */         this.myColours[1] = this.paletteManager.getColour(mask.getItemId());
/* 1859 */       else if ((mask.getPositionID() == 2) && (this.paletteManager.getColour(mask.getItemId()) != null)) {
/* 1860 */         this.myColours[2] = this.paletteManager.getColour(mask.getItemId());
/*      */       }
/*      */     }
/* 1863 */     this.colour1Button.setColour(this.myColours[0]);
/* 1864 */     this.colour2Button.setColour(this.myColours[1]);
/* 1865 */     this.colour3Button.setColour(this.myColours[2]);
/*      */   }
/*      */ 
/*      */   private void loadTip()
/*      */   {
/* 1871 */     if (this.tipsOn) {
/* 1872 */       if (this.mode == 0) {
/* 1873 */         this.tipsLayerWidgets.setVisible(false);
/* 1874 */         this.tipsPaintButton.setVisible(true);
/*      */       }
/* 1876 */       else if (this.mode == 1) {
/* 1877 */         this.tipsLayerWidgets.setVisible(false);
/* 1878 */         this.tipsEraserButton.setVisible(true);
/*      */       }
/* 1880 */       else if (this.mode == 2) {
/* 1881 */         this.tipsLayerWidgets.setVisible(false);
/* 1882 */         this.tipsMaskButton.setVisible(true);
/*      */       }
/* 1884 */       else if (this.mode != 3);
/*      */     }
/*      */     else
/* 1887 */       hideTips();
/*      */   }
/*      */ 
/*      */   private void hideTips()
/*      */   {
/* 1894 */     this.tipsLayerWidgets.setVisible(false);
/* 1895 */     this.tipsMinimizedButton.setVisible(true);
/* 1896 */     this.tipsOn = false;
/*      */   }
/*      */ 
/*      */   private void testUtility() {
/* 1900 */     this.maskList.setSelectedData(this.lastSelected);
/* 1901 */     setCursor(new Cursor(0));
/* 1902 */     this.eraserButton.setState(false);
/* 1903 */     this.eraserButton.setSelected(false);
/* 1904 */     this.paintButton.setState(false);
/* 1905 */     this.paintButton.setSelected(false);
/* 1906 */     this.maskButton.setState(true);
/* 1907 */     this.maskButton.setSelected(true);
/* 1908 */     this.eraserOn = false;
/* 1909 */     this.input.setMode(1);
/* 1910 */     this.mode = 2;
/*      */ 
/* 1913 */     this.inputLayer.scissorsSeed(new Point(52, 299));
/*      */ 
/* 1915 */     this.inputLayer.setScissorsStraightLine(true);
/*      */ 
/* 1917 */     this.inputLayer.scissorsSeed(new Point(115, 182));
/* 1918 */     this.inputLayer.scissorsSeed(new Point(225, 323));
/* 1919 */     this.inputLayer.scissorsSeed(new Point(51, 301));
/* 1920 */     this.inputLayer.scissorsSeed(new Point(51, 301));
/*      */ 
/* 1922 */     this.inputLayer.scissorsCommit();
/*      */ 
/* 1924 */     this.input.setMode(0);
/* 1925 */     getClass(); this.mode = 0;
/*      */ 
/* 1927 */     updateViewAndThumbnail();
/*      */   }
/*      */ 
/*      */   public void actionPerformed(ActionEvent e)
/*      */   {
/* 1933 */     if (e.getSource() != this.saveToWebsiteButton) {
/* 1934 */       this.touched = true;
/*      */     }
/* 1936 */     if (e.getSource() == this.loadButton) {
/* 1937 */       this.localOrPremaskedLayer.setVisible(true);
/* 1938 */       this.coloursLayer.setVisible(false);
/* 1939 */       this.colourWallLayer.setVisible(false);
/* 1940 */       this.backgroundColourWall.setVisible(false);
/* 1941 */       this.groupsLayer.setVisible(false);
/* 1942 */       this.bottomWidgets.setVisible(true);
/* 1943 */       this.thumbnailLayer.setVisible(false);
/* 1944 */       this.backToImageButton.setVisible(false);
/* 1945 */       this.backToWallButton.setVisible(false);
/*      */     }
/* 1948 */     else if (e.getSource() == this.loadProject) {
/* 1949 */       loadProjectFromWeb();
/*      */     }
/* 1951 */     else if (e.getSource() == this.saveToFileButton) {
/* 1952 */       save();
/* 1953 */     } else if (e.getSource() == this.loadFromFileButton) {
/* 1954 */       this.localOrPremaskedLayer.setVisible(false);
/* 1955 */       load();
/* 1956 */       updateViewAndThumbnail();
/* 1957 */       this.thumbnailLayer.setVisible(false);
/* 1958 */     } else if (e.getSource() == this.loadFromPreMaskedButton) {
/* 1959 */       this.localOrPremaskedLayer.setVisible(false);
/* 1960 */       this.premaskedLayer.setVisible(true);
/*      */     }
/* 1962 */     else if (e.getSource() == this.cancel1Button) {
/* 1963 */       this.localOrPremaskedLayer.setVisible(false);
/*      */     }
/* 1966 */     else if (e.getSource() == this.cancel2Button)
/*      */     {
/* 1968 */       this.premaskedLayer.setVisible(false);
/*      */     }
/* 1970 */     else if (e.getSource() == this.centreButton) {
/* 1971 */       this.view.resetView(DuluxPaintView.maxZoomOut);
/*      */     }
/* 1974 */     else if (e.getSource() == this.saveToWebsiteButton) {
/* 1975 */       if (!this.imageLoaded) {
/* 1976 */         this.errorLabel.setLabel("Please load an image before saving your project");
/* 1977 */         this.errorLayer.setVisible(true);
/*      */       }
/* 1979 */       else if (!this.isNewProject) {
/* 1980 */         this.questionType = 1;
/* 1981 */         this.yesNoLabel.setLabel("Would you like to save and ovewrite the previous changes?");
/* 1982 */         this.yesNoLayer.setVisible(true);
/*      */       } else {
/* 1984 */         saveProject();
/* 1985 */         this.isNewProject = false;
/*      */       }
/*      */     }
/* 1988 */     else if (e.getSource() == this.returnToDuluxButton) {
/* 1989 */       if (this.touched) {
/* 1990 */         this.questionType = 3;
/* 1991 */         this.yesNoLabel.setLabel("Would you like to save before returning to the Dulux Website?");
/* 1992 */         this.yesNoLayer.setVisible(true);
/* 1993 */         this.touched = false;
/*      */       }
/*      */     }
/* 1996 */     else if (e.getSource() == this.noButton) {
/* 1997 */       if (this.questionType == 3) {
/* 1998 */         this.saveAndLoad.applicationClosed(this.SESSIONID);
/* 1999 */         System.exit(0);
/*      */       }
/* 2001 */       else if (this.questionType == 1) {
/* 2002 */         this.yesNoLayer.setVisible(false);
/*      */       }
/*      */     }
/* 2005 */     else if (e.getSource() == this.yesButton) {
/* 2006 */       if (this.questionType == 3) {
/* 2007 */         this.yesNoLayer.setVisible(false);
/*      */ 
/* 2009 */         Thread t = new Thread() {
/*      */           public void run() {
/* 2011 */             DuluxMyColour.this.saveProject();
/*      */           }
/*      */         };
/* 2014 */         Thread s = new Thread() {
/*      */           public void run() {
/* 2016 */             DuluxMyColour.this.saveAndLoad.applicationClosed(DuluxMyColour.this.SESSIONID);
/* 2017 */             System.exit(0);
/*      */           }
/*      */         };
/*      */         try {
/* 2021 */           t.start();
/* 2022 */           t.join();
/* 2023 */           s.start();
/* 2024 */           s.join();
/*      */         }
/*      */         catch (InterruptedException ignorable)
/*      */         {
/*      */         }
/*      */ 
/*      */       }
/* 2031 */       else if (this.questionType == 1) {
/* 2032 */         saveProject();
/* 2033 */         this.yesNoLayer.setVisible(false);
/*      */       }
/* 2035 */     } else if (this.thumbnails.contains((Widget)e.getSource())) {
/* 2036 */       loadProjectFromWeb(((SkinnedButton)e.getSource()).getURL());
/* 2037 */       this.premaskedLayer.setVisible(false);
/*      */     }
/* 2039 */     else if (e.getSource() == this.nextthumbnailsButton) {
/* 2040 */       this.thumbnailpage += 1;
/* 2041 */       loadThumbnails(this.thumbnailpage);
/* 2042 */     } else if (e.getSource() == this.previousthumbnailsButton) {
/* 2043 */       this.thumbnailpage -= 1;
/* 2044 */       loadThumbnails(this.thumbnailpage);
/*      */     }
/* 2046 */     else if (e.getSource() == this.reds) {
/* 2047 */       if (this.colourWallType == 0)
/* 2048 */         changePalette(1);
/* 2049 */       else if (this.colourWallType == 1)
/* 2050 */         changePalette(1, true);
/* 2051 */       else if (this.colourWallType == 2)
/* 2052 */         changePaletteSpecifier(1);
/*      */     }
/* 2054 */     else if (e.getSource() == this.oranges) {
/* 2055 */       if (this.colourWallType == 0)
/* 2056 */         changePalette(2);
/* 2057 */       else if (this.colourWallType == 1)
/* 2058 */         changePalette(2, true);
/* 2059 */       else if (this.colourWallType == 2)
/* 2060 */         changePaletteSpecifier(2);
/*      */     }
/* 2062 */     else if (e.getSource() == this.yellows) {
/* 2063 */       if (this.colourWallType == 0)
/* 2064 */         changePalette(3);
/* 2065 */       else if (this.colourWallType == 1)
/* 2066 */         changePalette(3, true);
/* 2067 */       else if (this.colourWallType == 2)
/* 2068 */         changePaletteSpecifier(3);
/*      */     }
/* 2070 */     else if (e.getSource() == this.greens) {
/* 2071 */       if (this.colourWallType == 0)
/* 2072 */         changePalette(4);
/* 2073 */       else if (this.colourWallType == 1)
/* 2074 */         changePalette(4, true);
/* 2075 */       else if (this.colourWallType == 2)
/* 2076 */         changePaletteSpecifier(4);
/*      */     }
/* 2078 */     else if (e.getSource() == this.blues) {
/* 2079 */       if (this.colourWallType == 0)
/* 2080 */         changePalette(5);
/* 2081 */       else if (this.colourWallType == 1)
/* 2082 */         changePalette(5, true);
/* 2083 */       else if (this.colourWallType == 2)
/* 2084 */         changePaletteSpecifier(5);
/*      */     }
/* 2086 */     else if (e.getSource() == this.purples) {
/* 2087 */       if (this.colourWallType == 0)
/* 2088 */         changePalette(6);
/* 2089 */       else if (this.colourWallType == 1)
/* 2090 */         changePalette(6, true);
/* 2091 */       else if (this.colourWallType == 2)
/* 2092 */         changePaletteSpecifier(6);
/*      */     }
/* 2094 */     else if (e.getSource() == this.browns) {
/* 2095 */       if (this.colourWallType == 0)
/* 2096 */         changePalette(7);
/* 2097 */       else if (this.colourWallType == 1)
/* 2098 */         changePalette(7, true);
/* 2099 */       else if (this.colourWallType == 2)
/* 2100 */         changePaletteSpecifier(7);
/*      */     }
/* 2102 */     else if (e.getSource() == this.neutrals) {
/* 2103 */       if (this.colourWallType == 0)
/* 2104 */         changePalette(8);
/* 2105 */       else if (this.colourWallType == 1)
/* 2106 */         changePalette(8, true);
/* 2107 */       else if (this.colourWallType == 2)
/* 2108 */         changePaletteSpecifier(8);
/*      */     }
/* 2110 */     else if (e.getSource() == this.suede) {
/* 2111 */       changeToSpecialityPalette("suede", false);
/* 2112 */     } else if (e.getSource() == this.tuscan) {
/* 2113 */       changeToSpecialityPalette("tuscan", false);
/* 2114 */     } else if (e.getSource() == this.metallic) {
/* 2115 */       changeToSpecialityPalette("metallic", false);
/* 2116 */     } else if (e.getSource() == this.colorBond) {
/* 2117 */       changeToSpecialityPalette("colorBond", false);
/* 2118 */     } else if (e.getSource() == this.riverRock) {
/* 2119 */       changeToSpecialityPalette("riverRock", false);
/* 2120 */     } else if (e.getSource() == this.designer) {
/* 2121 */       changeToSpecialityPalette("designer", false);
/* 2122 */     } else if (e.getSource() == this.render)
/*      */     {
/* 2125 */       this.colourWallType = 1;
/* 2126 */       this.colourWallLayer.setVisible(true);
/* 2127 */       this.specialtiesLayer.setVisible(false);
/* 2128 */     } else if (e.getSource() == this.sprayFast) {
/* 2129 */       changeToSpecialityPalette("sprayFast", false);
/* 2130 */     } else if (e.getSource() == this.quitRust) {
/* 2131 */       changeToSpecialityPalette("quitRust", false);
/* 2132 */     } else if (e.getSource() == this.garageFloors) {
/* 2133 */       changeToSpecialityPalette("garageFloors", false);
/* 2134 */     } else if (e.getSource() == this.weatherShieldGardenShades) {
/* 2135 */       changeToSpecialityPalette("weathershieldGardenShades", false);
/* 2136 */     } else if (e.getSource() == this.weatherShieldRoofandTrim) {
/* 2137 */       changeToSpecialityPalette("weathershieldRoofandTrim", false);
/*      */     }
/* 2139 */     else if (e.getSource() == this.backToWallButton) {
/* 2140 */       this.coloursLayer.setVisible(false);
/* 2141 */       if (this.typeOfPalette == 0)
/* 2142 */         this.colourWallLayer.setVisible(true);
/* 2143 */       else if (this.typeOfPalette == 1) {
/* 2144 */         this.specialtiesLayer.setVisible(true);
/*      */       }
/* 2146 */       this.backgroundColourWall.setVisible(false);
/* 2147 */     } else if (e.getSource() == this.backToImageButton) {
/* 2148 */       this.specialtiesLayer.setVisible(false);
/* 2149 */       this.colourWallLayer.setVisible(false);
/* 2150 */       this.groupsLayer.setVisible(false);
/* 2151 */       this.thumbnailLayer.setVisible(false);
/* 2152 */       this.bottomWidgets.setVisible(true);
/* 2153 */       this.backToImageButton.setVisible(false);
/* 2154 */       this.coloursLayer.setVisible(false);
/* 2155 */       this.backgroundColourWall.setVisible(false);
/*      */     }
/* 2158 */     else if (e.getSource() == this.tipsMinimizedButton) {
/* 2159 */       this.tipsOn = true;
/* 2160 */       loadTip();
/* 2161 */     } else if (e.getSource() == this.tipsPaintButton) {
/* 2162 */       hideTips();
/* 2163 */     } else if (e.getSource() == this.tipsEraserButton) {
/* 2164 */       hideTips();
/* 2165 */     } else if (e.getSource() == this.tipsMaskButton) {
/* 2166 */       hideTips();
/*      */     }
/* 2172 */     else if (e.getSource() == this.tutorialsButton)
/*      */     {
/*      */       Process p;
/*      */       try
/*      */       {
/*      */         URL url;
/* 2175 */         if (this.IS_NZ)
/* 2176 */           url = new URL("http://www.dulux.co.nz/colour/mycolour/tutorials.html");
/*      */         else {
/* 2178 */           url = new URL("http://www.dulux.com.au/colour/mycolour/tutorials.html");
/*      */         }
/*      */ 
/* 2181 */         Runtime runtime = Runtime.getRuntime();
/*      */ 
/* 2183 */         String cmd = "rundll32 url.dll,FileProtocolHandler " + url;
/* 2184 */         p = runtime.exec(cmd);
/*      */       }
/*      */       catch (Exception ex)
/*      */       {
/*      */       }
/*      */     }
/* 2190 */     else if (e.getSource() == this.editLayerButton) {
/* 2191 */       this.selectedMask = ((Mask)this.maskList.getSelectedData());
/* 2192 */       editLayer();
/* 2193 */       this.requireLayerSelectedWidgets.setEnabled(true);
/*      */     }
/* 2199 */     else if (e.getSource() == this.standardsButton) {
/* 2200 */       this.colourWallArrowWidgets.setVisible(true);
/* 2201 */       this.colourWallLayer.setVisible(true);
/* 2202 */       this.specialtiesLayer.setVisible(false);
/* 2203 */       this.coloursLayer.setVisible(false);
/* 2204 */       this.standardsButton.setSelected(true);
/* 2205 */       this.specialtiesButton.setSelected(false);
/* 2206 */       this.whitesButton.setSelected(false);
/* 2207 */       this.traditionalsButton.setSelected(false);
/* 2208 */       this.typeOfPalette = 0;
/* 2209 */       this.backToImageButton.setVisible(true);
/* 2210 */       this.backgroundColourWall.setVisible(false);
/* 2211 */       this.colourWallType = 0;
/* 2212 */     } else if (e.getSource() == this.specialtiesButton) {
/* 2213 */       this.colourWallArrowWidgets.setVisible(true);
/* 2214 */       this.specialtiesLayer.setVisible(true);
/* 2215 */       this.colourWallLayer.setVisible(false);
/* 2216 */       this.backToImageButton.setVisible(true);
/* 2217 */       this.coloursLayer.setVisible(false);
/* 2218 */       this.standardsButton.setSelected(false);
/* 2219 */       this.specialtiesButton.setSelected(true);
/* 2220 */       this.whitesButton.setSelected(false);
/* 2221 */       this.traditionalsButton.setSelected(false);
/* 2222 */       this.backgroundColourWall.setVisible(false);
/* 2223 */       this.typeOfPalette = 1;
/* 2224 */       this.colourWallType = 0;
/* 2225 */     } else if (e.getSource() == this.whitesButton) {
/* 2226 */       if (this.IS_NZ) {
/* 2227 */         changeToSpecialityPalette("WhitesCONZ", true);
/*      */       }
/*      */       else {
/* 2230 */         changeToSpecialityPalette("whites", true);
/*      */       }
/* 2232 */       this.colourWallArrowWidgets.setVisible(true);
/*      */ 
/* 2234 */       this.paletteView.setWhites(true);
/* 2235 */       this.standardsButton.setSelected(false);
/* 2236 */       this.backToImageButton.setVisible(true);
/* 2237 */       this.specialtiesButton.setSelected(false);
/* 2238 */       this.whitesButton.setSelected(true);
/* 2239 */       this.traditionalsButton.setSelected(false);
/* 2240 */       this.backgroundColourWall.setVisible(false);
/* 2241 */       this.typeOfPalette = 2;
/* 2242 */       this.colourWallType = 0;
/* 2243 */     } else if (e.getSource() == this.traditionalsButton) {
/* 2244 */       if (this.IS_NZ) {
/* 2245 */         this.colourWallArrowWidgets.setVisible(true);
/* 2246 */         this.backToImageButton.setVisible(true);
/* 2247 */         this.colourWallLayer.setVisible(true);
/* 2248 */         this.specialtiesLayer.setVisible(false);
/* 2249 */         this.coloursLayer.setVisible(false);
/* 2250 */         this.standardsButton.setSelected(false);
/* 2251 */         this.specialtiesButton.setSelected(false);
/* 2252 */         this.whitesButton.setSelected(false);
/* 2253 */         this.traditionalsButton.setSelected(true);
/* 2254 */         this.backgroundColourWall.setVisible(false);
/* 2255 */         this.colourWallType = 2;
/*      */       }
/*      */       else {
/* 2258 */         changeToSpecialityPalette("traditionals", true);
/* 2259 */         this.colourWallArrowWidgets.setVisible(true);
/* 2260 */         this.backToImageButton.setVisible(true);
/* 2261 */         this.standardsButton.setSelected(false);
/* 2262 */         this.specialtiesButton.setSelected(false);
/* 2263 */         this.whitesButton.setSelected(false);
/* 2264 */         this.traditionalsButton.setSelected(true);
/* 2265 */         this.backgroundColourWall.setVisible(false);
/* 2266 */         this.typeOfPalette = 2;
/* 2267 */         this.colourWallType = 0;
/*      */       }
/*      */ 
/*      */     }
/* 2271 */     else if (e.getSource() == this.changeSelectedMaskButton1) {
/* 2272 */       changeSingleMaskColour(this.changeSelectedMaskButton1, 0);
/* 2273 */       updateViewAndThumbnail();
/* 2274 */       this.editinglabel.completeEditing();
/* 2275 */     } else if (e.getSource() == this.changeSelectedMaskButton2) {
/* 2276 */       changeSingleMaskColour(this.changeSelectedMaskButton2, 1);
/* 2277 */       updateViewAndThumbnail();
/* 2278 */       this.editinglabel.completeEditing();
/* 2279 */     } else if (e.getSource() == this.changeSelectedMaskButton3) {
/* 2280 */       changeSingleMaskColour(this.changeSelectedMaskButton3, 2);
/* 2281 */       updateViewAndThumbnail();
/* 2282 */       this.editinglabel.completeEditing();
/* 2283 */     } else if (e.getSource() == this.closeEditMaskButton) {
/* 2284 */       this.editMaskLayer.setVisible(false);
/* 2285 */       this.editinglabel.completeEditing();
/*      */     }
/* 2292 */     else if (e.getSource() == this.loadProject) {
/* 2293 */       loadProjectFromWeb();
/*      */     }
/* 2296 */     else if (e.getSource() == this.colour1Button) {
/* 2297 */       colourButtonSelected(this.colour1Button);
/*      */     }
/* 2299 */     else if (e.getSource() == this.colour2Button) {
/* 2300 */       colourButtonSelected(this.colour2Button);
/*      */     }
/* 2302 */     else if (e.getSource() == this.colour3Button) {
/* 2303 */       colourButtonSelected(this.colour3Button);
/*      */     }
/*      */     else
/*      */     {
/*      */       Iterator it;
/* 2305 */       if (e.getSource() == this.addLayerButton1) {
/* 2306 */         this.explorer.createMask(this.myColours[0].colour, "Area " + this.layerCount, this.myColours[0].getId(), 0);
/* 2307 */         this.layerCount += 1;
/* 2308 */         for (it = this.explorer.getMasks(); it.hasNext(); ) {
/* 2309 */           Mask mask = (Mask)it.next();
/* 2310 */           if (mask.getItemId() == this.myColours[0].getId()) {
/* 2311 */             this.maskList.setSelectedData(mask);
/* 2312 */             this.paintManager.setSelectedMask(mask);
/*      */           }
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 2315 */         if (e.getSource() == this.addLayerButton2) {
/* 2316 */           this.explorer.createMask(this.myColours[1].colour, "Area " + this.layerCount, this.myColours[1].getId(), 1);
/* 2317 */           this.layerCount += 1;
/* 2318 */           for (it = this.explorer.getMasks(); it.hasNext(); ) {
/* 2319 */             Mask mask = (Mask)it.next();
/* 2320 */             if (mask.getItemId() == this.myColours[1].getId()) {
/* 2321 */               this.maskList.setSelectedData(mask);
/* 2322 */               this.paintManager.setSelectedMask(mask);
/*      */             }
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/* 2325 */           if (e.getSource() == this.addLayerButton3) {
/* 2326 */             this.explorer.createMask(this.myColours[2].colour, "Area " + this.layerCount, this.myColours[2].getId(), 2);
/* 2327 */             this.layerCount += 1;
/* 2328 */             for (it = this.explorer.getMasks(); it.hasNext(); ) {
/* 2329 */               Mask mask = (Mask)it.next();
/* 2330 */               if (mask.getItemId() == this.myColours[2].getId()) {
/* 2331 */                 this.maskList.setSelectedData(mask);
/* 2332 */                 this.paintManager.setSelectedMask(mask);
/*      */               }
/*      */             }
/* 2335 */           } else if (e.getSource() == this.changeSwatchButton1) {
/* 2336 */             this.colourWallLayer.setVisible(true);
/* 2337 */             this.coloursLayer.setVisible(false);
/* 2338 */             this.change = 0;
/* 2339 */             this.old = this.colour1Button.getColour();
/*      */ 
/* 2341 */             this.thumbnailLayer.setVisible(true);
/* 2342 */             this.bottomWidgets.setVisible(false);
/* 2343 */             this.groupsLayer.setVisible(true);
/* 2344 */             this.standardsButton.setSelected(true);
/* 2345 */             this.specialtiesButton.setSelected(false);
/* 2346 */             this.whitesButton.setSelected(false);
/* 2347 */             this.traditionalsButton.setSelected(false);
/* 2348 */             this.backToImageButton.setVisible(true);
/* 2349 */             this.typeOfPalette = 0;
/* 2350 */             this.colourWallType = 0;
/* 2351 */           } else if (e.getSource() == this.changeSwatchButton2) {
/* 2352 */             this.colourWallLayer.setVisible(true);
/* 2353 */             this.coloursLayer.setVisible(false);
/* 2354 */             this.paletteView.setPalette(this.paletteManager.getPalette(1));
/* 2355 */             this.change = 1;
/* 2356 */             this.old = this.colour2Button.getColour();
/*      */ 
/* 2358 */             this.thumbnailLayer.setVisible(true);
/* 2359 */             this.bottomWidgets.setVisible(false);
/* 2360 */             this.groupsLayer.setVisible(true);
/* 2361 */             this.standardsButton.setSelected(true);
/* 2362 */             this.specialtiesButton.setSelected(false);
/* 2363 */             this.whitesButton.setSelected(false);
/* 2364 */             this.traditionalsButton.setSelected(false);
/* 2365 */             this.backToImageButton.setVisible(true);
/* 2366 */             this.typeOfPalette = 0;
/* 2367 */             this.colourWallType = 0;
/* 2368 */           } else if (e.getSource() == this.changeSwatchButton3) {
/* 2369 */             this.colourWallLayer.setVisible(true);
/* 2370 */             this.coloursLayer.setVisible(false);
/* 2371 */             this.change = 2;
/* 2372 */             this.old = this.colour3Button.getColour();
/*      */ 
/* 2374 */             this.thumbnailLayer.setVisible(true);
/* 2375 */             this.bottomWidgets.setVisible(false);
/* 2376 */             this.groupsLayer.setVisible(true);
/* 2377 */             this.standardsButton.setSelected(true);
/* 2378 */             this.specialtiesButton.setSelected(false);
/* 2379 */             this.whitesButton.setSelected(false);
/* 2380 */             this.traditionalsButton.setSelected(false);
/* 2381 */             this.backToImageButton.setVisible(true);
/* 2382 */             this.typeOfPalette = 0;
/* 2383 */             this.colourWallType = 0;
/* 2384 */           } else if ((e.getSource() == this.original0) || (e.getSource() == this.base_1) || (e.getSource() == this.original2) || (e.getSource() == this.original3) || (e.getSource() == this.original4) || (e.getSource() == this.original5))
/*      */           {
/* 2386 */             this.selectedLayer.setVisible(false);
/* 2387 */             this.colourWallLayer.setVisible(false);
/* 2388 */             this.coloursLayer.setVisible(false);
/* 2389 */             this.groupsLayer.setVisible(false);
/* 2390 */             this.backToImageButton.setVisible(false);
/* 2391 */             this.backgroundColourWall.setVisible(false);
/* 2392 */             boolean hasColour = false;
/* 2393 */             for (int i = 0; i < this.myColours.length; i++) {
/* 2394 */               if (this.paletteView.getSelectedColour().equals(this.myColours[i])) {
/* 2395 */                 hasColour = true;
/*      */               }
/*      */             }
/* 2398 */             if (!hasColour) {
/* 2399 */               this.myColours[this.change] = this.paletteView.getSelectedColour();
/* 2400 */               changeMaskColour(this.old, this.myColours[this.change]);
/*      */             }
/* 2402 */             this.thumbnailLayer.setVisible(false);
/* 2403 */             this.bottomWidgets.setVisible(true);
/* 2404 */             refreshButtonColours();
/* 2405 */             updateViewAndThumbnail();
/* 2406 */           } else if (e.getSource() == this.interior1_1) {
/* 2407 */             changeColour(this.interior1_1);
/* 2408 */           } else if (e.getSource() == this.interior1_2) {
/* 2409 */             changeColour(this.interior1_2);
/* 2410 */           } else if (e.getSource() == this.interior2_1) {
/* 2411 */             changeColour(this.interior2_1);
/* 2412 */           } else if (e.getSource() == this.interior2_2) {
/* 2413 */             changeColour(this.interior2_2);
/* 2414 */           } else if (e.getSource() == this.exterior1_1) {
/* 2415 */             changeColour(this.exterior1_1);
/* 2416 */           } else if (e.getSource() == this.exterior1_2) {
/* 2417 */             changeColour(this.exterior1_2);
/* 2418 */           } else if (e.getSource() == this.exterior2_1) {
/* 2419 */             changeColour(this.exterior2_1);
/* 2420 */           } else if (e.getSource() == this.exterior2_2) {
/* 2421 */             changeColour(this.exterior2_2);
/* 2422 */           } else if (e.getSource() == this.base_2) {
/* 2423 */             changeColour(this.base_2);
/* 2424 */           } else if (e.getSource() == this.base_3) {
/* 2425 */             changeColour(this.base_3);
/*      */           }
/* 2427 */           else if (e.getSource() == this.schemePopupCloseButton) {
/* 2428 */             this.selectedLayer.setVisible(false);
/* 2429 */             this.schemePopupCloseButton.setVisible(false);
/*      */           }
/* 2431 */           else if (e.getSource() == this.eraserButton) {
/* 2432 */             this.maskList.setSelectedData(this.lastSelected);
/* 2433 */             if ((this.paintButton.getState()) || (this.maskButton.getState())) {
/* 2434 */               this.input.setMode(3);
/* 2435 */               this.paintButton.setState(false);
/* 2436 */               this.paintButton.setSelected(false);
/* 2437 */               this.maskButton.setState(false);
/* 2438 */               this.maskButton.setSelected(false);
/* 2439 */               this.eraserButton.setSelected(true);
/* 2440 */               this.eraserButton.setState(true);
/* 2441 */               this.mode = 1;
/* 2442 */               this.eraserOn = true;
/* 2443 */             } else if ((!this.paintButton.getState()) && (!this.maskButton.getState()) && (!this.eraserButton.getState())) {
/* 2444 */               this.input.setMode(3);
/* 2445 */               this.paintButton.setState(false);
/* 2446 */               this.paintButton.setSelected(false);
/* 2447 */               this.maskButton.setState(false);
/* 2448 */               this.maskButton.setSelected(false);
/* 2449 */               this.eraserButton.setSelected(true);
/* 2450 */               this.eraserButton.setState(true);
/* 2451 */               this.mode = 1;
/* 2452 */               this.eraserOn = true;
/*      */             }
/*      */ 
/* 2455 */             refreshCursor();
/* 2456 */             loadTip();
/* 2457 */           } else if (e.getSource() == this.paintButton) {
/* 2458 */             this.maskList.setSelectedData(this.lastSelected);
/* 2459 */             setCursor(new Cursor(0));
/* 2460 */             this.eraserOn = false;
/* 2461 */             this.eraserButton.setState(false);
/* 2462 */             this.eraserButton.setSelected(false);
/* 2463 */             this.paintButton.setState(true);
/* 2464 */             this.paintButton.setSelected(true);
/* 2465 */             this.maskButton.setState(false);
/* 2466 */             this.maskButton.setSelected(false);
/* 2467 */             if (this.paintButton.getState()) {
/* 2468 */               this.input.setMode(0);
/* 2469 */               this.mode = 0;
/*      */             }
/*      */ 
/* 2472 */             refreshCursor();
/* 2473 */             loadTip();
/*      */           }
/* 2475 */           else if (e.getSource() == this.maskButton) {
/* 2476 */             this.maskList.setSelectedData(this.lastSelected);
/* 2477 */             setCursor(new Cursor(0));
/* 2478 */             this.eraserButton.setState(false);
/* 2479 */             this.eraserButton.setSelected(false);
/* 2480 */             this.paintButton.setState(false);
/* 2481 */             this.paintButton.setSelected(false);
/* 2482 */             this.maskButton.setState(true);
/* 2483 */             this.maskButton.setSelected(true);
/* 2484 */             this.eraserOn = false;
/* 2485 */             if (this.maskButton.getState()) {
/* 2486 */               this.input.setMode(1);
/* 2487 */               this.mode = 2;
/*      */             }
/*      */ 
/* 2490 */             refreshCursor();
/* 2491 */             loadTip();
/*      */           }
/* 2494 */           else if (e.getSource() == this.helpButton) {
/* 2495 */             this.helpLayer.setVisible(!this.helpLayer.getVisible());
/* 2496 */             this.helpButton.setSelected(this.helpLayer.getVisible());
/*      */           }
/* 2498 */           else if (e.getSource() == this.undoButton) {
/* 2499 */             this.explorer.undo();
/* 2500 */             updateMaskList(this.selectedMask);
/* 2501 */             updateViewAndThumbnail();
/* 2502 */             refreshButtonColours();
/* 2503 */             refreshCursor();
/*      */           }
/* 2505 */           else if (e.getSource() == this.redoButton) {
/* 2506 */             this.explorer.redo();
/* 2507 */             updateViewAndThumbnail();
/* 2508 */             updateMaskList(this.selectedMask);
/* 2509 */             refreshButtonColours();
/* 2510 */             refreshCursor();
/*      */           }
/* 2513 */           else if (e.getSource() == this.deleteButton) {
/* 2514 */             this.explorer.log("DuluxMyColour: delete mask");
/* 2515 */             Mask mask = (Mask)this.maskList.getSelectedItem().getData();
/* 2516 */             this.explorer.destroyMask(mask);
/* 2517 */             updateViewAndThumbnail();
/* 2518 */           } else if (e.getSource() == this.zoomInButton) {
/* 2519 */             this.inputLayer.zoomIn(1);
/* 2520 */             updateThumbnail();
/* 2521 */           } else if (e.getSource() == this.zoomOutButton) {
/* 2522 */             this.inputLayer.zoomOut(1);
/* 2523 */             updateThumbnail();
/* 2524 */           } else if (e.getSource() == this.errorOkButton) {
/* 2525 */             this.errorLayer.setVisible(false);
/*      */           }
/* 2529 */           else if (e.getSource() == this.rightButton) {
/* 2530 */             this.paletteView.moveRight();
/* 2531 */           } else if (e.getSource() == this.leftButton) {
/* 2532 */             this.paletteView.moveLeft();
/* 2533 */           } else if (e.getSource() == this.upButton) {
/* 2534 */             this.paletteView.moveUp();
/* 2535 */           } else if (e.getSource() == this.downButton) {
/* 2536 */             this.paletteView.moveDown();
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/* 2543 */   private void changeToSpecialityPalette(String name, boolean whitesOrTraditionals) { this.paletteView.setPalette(this.paletteManager.getSpecialtyPalette(name));
/* 2544 */     if (this.paletteManager.getSpecialtyPalette(name).rows <= 7) {
/* 2545 */       this.upButton.setVisible(false);
/* 2546 */       this.downButton.setVisible(false);
/*      */     } else {
/* 2548 */       this.upButton.setVisible(true);
/* 2549 */       this.downButton.setVisible(true);
/*      */     }
/*      */ 
/* 2552 */     if (this.paletteManager.getSpecialtyPalette(name).cols <= 10) {
/* 2553 */       this.rightButton.setVisible(false);
/* 2554 */       this.leftButton.setVisible(false);
/*      */     } else {
/* 2556 */       this.rightButton.setVisible(true);
/* 2557 */       this.leftButton.setVisible(true);
/*      */     }
/* 2559 */     this.coloursLayer.setVisible(true);
/* 2560 */     this.colourWallLayer.setVisible(false);
/* 2561 */     this.paletteView.setDefaultPosition();
/*      */ 
/* 2563 */     this.specialtiesLayer.setVisible(false);
/* 2564 */     if (whitesOrTraditionals)
/* 2565 */       this.backToWallButton.setVisible(false);
/*      */     else {
/* 2567 */       this.backToWallButton.setVisible(true);
/*      */     }
/* 2569 */     this.backgroundColourWall.setVisible(true);
/*      */   }
/*      */ 
/*      */   private void changeMaskColour(DuluxColour oldColour, DuluxColour newColour)
/*      */   {
/* 2576 */     for (Iterator it = this.explorer.getMasks(); it.hasNext(); ) {
/* 2577 */       Mask mask = (Mask)it.next();
/* 2578 */       if (mask.getColour() == oldColour.colour) {
/* 2579 */         this.explorer.changeMask(mask, newColour.colour, (String)mask.getUserData(), newColour.getId(), -1);
/* 2580 */         updateMaskList(mask);
/*      */       }
/*      */     }
/* 2583 */     this.backgroundColourWall.setVisible(false);
/*      */   }
/*      */ 
/*      */   private void changeSingleMaskColour(SkinnedButton button, int positionID) {
/* 2587 */     this.explorer.changeMask(this.selectedMask, button.getColour().colour, (String)this.selectedMask.getUserData(), button.getColour().getId(), positionID);
/* 2588 */     updateMaskList(this.selectedMask);
/* 2589 */     this.editMaskLayer.setVisible(false);
/*      */   }
/*      */ 
/*      */   private void updateViewAndThumbnail() {
/* 2593 */     this.view.updateView();
/* 2594 */     updateThumbnail();
/*      */   }
/*      */ 
/*      */   private void updateThumbnail()
/*      */   {
/* 2600 */     Image imageThumbnail = this.explorer.getThumbnail(103, 77).getAWTImage();
/* 2601 */     this.thumbnail = new SkinnedImage("", this.thumbnailPlaceHolder.getX(), this.thumbnailPlaceHolder.getY(), imageThumbnail);
/*      */ 
/* 2606 */     this.thumbnailPlaceHolder.replace(this.thumbnail);
/*      */   }
/*      */ 
/*      */   private void smoothMinimize()
/*      */   {
/*      */   }
/*      */ 
/*      */   public void listModified(ListEvent e)
/*      */   {
/*      */   }
/*      */ 
/*      */   public void itemHighlighted(ListEvent e) {
/*      */   }
/*      */ 
/*      */   public void itemSelected(ListEvent e) {
/* 2621 */     if (e.getSource() == this.paletteList) {
/* 2622 */       DuluxPalette palette = (DuluxPalette)this.paletteList.getSelectedItem().getData();
/*      */ 
/* 2624 */       setPalette(palette);
/*      */     }
/* 2626 */     else if (e.getSource() != this.colourList)
/*      */     {
/* 2631 */       if (e.getSource() == this.maskList) {
/* 2632 */         Mask mask = (Mask)this.maskList.getSelectedItem().getData();
/* 2633 */         this.paintManager.setSelectedMask(mask);
/* 2634 */         this.lastSelected = ((Mask)this.maskList.getSelectedItem().getData());
/* 2635 */         if (mask.getColour() == this.colour1Button.getColour().colour) {
/* 2636 */           this.colour1Button.setSelected(true);
/* 2637 */           this.colour2Button.setSelected(false);
/* 2638 */           this.colour3Button.setSelected(false);
/* 2639 */           this.addLayerButton1.setVisible(true);
/* 2640 */           this.addLayerButton2.setVisible(false);
/* 2641 */           this.addLayerButton3.setVisible(false);
/* 2642 */           this.changeSwatchButton1.setVisible(true);
/* 2643 */           this.changeSwatchButton2.setVisible(false);
/* 2644 */           this.changeSwatchButton3.setVisible(false);
/* 2645 */         } else if (mask.getColour() == this.colour2Button.getColour().colour) {
/* 2646 */           this.colour1Button.setSelected(false);
/* 2647 */           this.colour2Button.setSelected(true);
/* 2648 */           this.colour3Button.setSelected(false);
/* 2649 */           this.addLayerButton1.setVisible(false);
/* 2650 */           this.addLayerButton2.setVisible(true);
/* 2651 */           this.addLayerButton3.setVisible(false);
/* 2652 */           this.changeSwatchButton1.setVisible(false);
/* 2653 */           this.changeSwatchButton2.setVisible(true);
/* 2654 */           this.changeSwatchButton3.setVisible(false);
/* 2655 */         } else if (mask.getColour() == this.colour3Button.getColour().colour) {
/* 2656 */           this.colour1Button.setSelected(false);
/* 2657 */           this.colour2Button.setSelected(false);
/* 2658 */           this.colour3Button.setSelected(true);
/* 2659 */           this.addLayerButton1.setVisible(false);
/* 2660 */           this.addLayerButton2.setVisible(false);
/* 2661 */           this.addLayerButton3.setVisible(true);
/* 2662 */           this.changeSwatchButton1.setVisible(false);
/* 2663 */           this.changeSwatchButton2.setVisible(false);
/* 2664 */           this.changeSwatchButton3.setVisible(true);
/*      */         }
/* 2666 */         this.explorer.log("DuluxMyColour: Selected mask " + mask);
/*      */ 
/* 2669 */         this.requireLayerSelectedWidgets.setEnabled(true);
/*      */       }
/* 2671 */       else if (e.getSource() == this.lightingList) {
/* 2672 */         LightingAlgorithm lighting = (LightingAlgorithm)this.lightingList.getSelectedItem().getData();
/*      */ 
/* 2674 */         this.explorer.setLightingAlgorithm(lighting);
/* 2675 */         this.view.updateView();
/* 2676 */       } else if (e.getSource() == this.lightingList2) {
/* 2677 */         LightingAlgorithm lighting = (LightingAlgorithm)this.lightingList2.getSelectedItem().getData();
/*      */ 
/* 2679 */         this.explorer.setLightingAlgorithm(lighting);
/* 2680 */         this.view.updateView();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void itemSelected(LabelEvent e)
/*      */   {
/* 2687 */     if (e.getSource() == this.editinglabel)
/* 2688 */       this.editinglabel.beginEditing();
/*      */   }
/*      */ 
/*      */   public void refreshCursor()
/*      */   {
/* 2693 */     if (this.eraserOn)
/*      */     {
/*      */       try
/*      */       {
/* 2697 */         BufferedImage bi = new BufferedImage(32, 32, 2);
/* 2698 */         Graphics g = bi.getGraphics();
/* 2699 */         float zoomFactor = this.view.getZoom();
/* 2700 */         int eraserSize = (int)(zoomFactor * this.input.getEraserRadius());
/*      */ 
/* 2702 */         if (eraserSize > this.maxEraserSize) {
/* 2703 */           eraserSize = this.maxEraserSize;
/* 2704 */           this.input.setEraserRadius((int)(eraserSize / zoomFactor));
/* 2705 */           this.eraserSlider.setValue((int)(eraserSize / zoomFactor));
/*      */         }
/* 2707 */         paintCircle(g, eraserSize);
/* 2708 */         Point hotspot = new Point(15, 15);
/* 2709 */         setCursor(getToolkit().createCustomCursor(bi, hotspot, "Eraser Cursor"));
/*      */       }
/*      */       catch (Exception error)
/*      */       {
/* 2713 */         setCursor(new Cursor(0));
/* 2714 */         System.err.println(error.getMessage());
/*      */       }
/*      */     }
/*      */     else
/* 2718 */       setCursor(new Cursor(0));
/*      */   }
/*      */ 
/*      */   public void itemRenamed(ListEvent e, SkinnedListItem item, String name)
/*      */   {
/* 2726 */     assert (e.getSource() == this.maskList);
/* 2727 */     Mask mask = (Mask)item.getData();
/* 2728 */     mask.setUserData(name);
/* 2729 */     ((SkinnedListSwatchItem)item).label = name;
/* 2730 */     this.maskList.invalidate();
/* 2731 */     this.skin.requestFocus();
/*      */   }
/*      */ 
/*      */   public void itemRenamed(LabelEvent e, SkinnedLabel label, String name) {
/* 2735 */     this.explorer.log("DuluxMyColour: rename mask");
/* 2736 */     Mask mask = (Mask)this.maskList.getSelectedItem().getData();
/* 2737 */     mask.setUserData(name);
/* 2738 */     ((SkinnedListSwatchItem)this.maskList.getSelectedItem()).label = name;
/* 2739 */     label.setLabel(name);
/*      */ 
/* 2741 */     this.maskList.invalidate();
/* 2742 */     this.skin.requestFocus();
/*      */   }
/*      */ 
/*      */   public void colourSelected(DuluxColour colour)
/*      */   {
/* 2748 */     if ((this.typeOfPalette != 0) || (this.colourWallType == 1) || (this.colourWallType == 2)) {
/* 2749 */       this.myColours[this.change] = colour;
/* 2750 */       changeMaskColour(this.old, this.myColours[this.change]);
/* 2751 */       refreshButtonColours();
/* 2752 */       updateViewAndThumbnail();
/*      */ 
/* 2754 */       this.selectedLayer.setVisible(false);
/* 2755 */       this.colourWallLayer.setVisible(false);
/* 2756 */       this.coloursLayer.setVisible(false);
/* 2757 */       this.view.setVisible(true);
/* 2758 */       this.thumbnailLayer.setVisible(false);
/* 2759 */       this.zoomInButton.setVisible(true);
/* 2760 */       this.zoomOutButton.setVisible(true);
/* 2761 */       this.groupsLayer.setVisible(false);
/* 2762 */       this.backToImageButton.setVisible(false);
/* 2763 */       this.backgroundColourWall.setVisible(false);
/* 2764 */       this.colourWallType = 0;
/*      */     }
/* 2766 */     else if (this.typeOfPalette == 0) {
/* 2767 */       setColour(colour, true);
/* 2768 */       this.selectedLayer.setVisible(true);
/* 2769 */       this.schemePopupCloseButton.setVisible(true);
/* 2770 */       this.selectedName.setColor(Color.black);
/* 2771 */       this.selectedName.setLabel(colour.name);
/* 2772 */       this.original0.setColour(colour);
/* 2773 */       this.original0.setVisible(true);
/*      */ 
/* 2775 */       this.schemes = this.paletteManager.getScheme(colour);
/* 2776 */       this.base_1.setColour(this.schemes[0][0]);
/* 2777 */       this.base_2.setColour(this.schemes[0][1]);
/* 2778 */       this.base_3.setColour(this.schemes[0][2]);
/*      */ 
/* 2780 */       this.original2.setColour(this.schemes[1][0]);
/* 2781 */       this.interior1_1.setColour(this.schemes[1][1]);
/* 2782 */       this.interior1_2.setColour(this.schemes[1][2]);
/* 2783 */       this.original3.setColour(this.schemes[1][3]);
/* 2784 */       this.interior2_1.setColour(this.schemes[1][4]);
/* 2785 */       this.interior2_2.setColour(this.schemes[1][5]);
/* 2786 */       this.original4.setColour(this.schemes[2][0]);
/* 2787 */       this.exterior1_1.setColour(this.schemes[2][1]);
/* 2788 */       this.exterior1_2.setColour(this.schemes[2][2]);
/* 2789 */       this.original5.setColour(this.schemes[2][3]);
/* 2790 */       this.exterior2_1.setColour(this.schemes[2][4]);
/* 2791 */       this.exterior2_2.setColour(this.schemes[2][5]);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void setPalette(DuluxPalette palette) {
/* 2796 */     setPalette(palette, true);
/*      */   }
/*      */ 
/*      */   private void setPalette(DuluxPalette palette, boolean selectColour) {
/* 2800 */     this.paletteView.setPalette(palette, selectColour);
/*      */   }
/*      */ 
/*      */   private void setColour(DuluxColour colour, boolean smooth) {
/* 2804 */     this.paletteView.setSelectedColour(colour, smooth);
/*      */   }
/*      */ 
/*      */   private String createMaskName() {
/* 2808 */     return "(no name)";
/*      */   }
/*      */ 
/*      */   private void updateMaskList(Mask selectedMask) {
/* 2812 */     this.maskList.clear();
/* 2813 */     for (Iterator it = this.explorer.getMasks(); it.hasNext(); )
/*      */     {
/* 2815 */       Mask mask = (Mask)it.next();
/* 2816 */       String name = (String)mask.getUserData();
/* 2817 */       this.maskList.add(new SkinnedListSwatchItem(name, Colour.getAWTColor(mask.getColour()), mask));
/*      */     }
/*      */ 
/* 2822 */     this.paintManager.setSelectedMask(selectedMask);
/*      */   }
/*      */ 
/*      */   public void onMaskEvent(PaintExplorerMaskEvent event) {
/* 2826 */     Mask mask = event.getMask();
/* 2827 */     String name = null;
/* 2828 */     if (mask != null) {
/* 2829 */       name = (String)mask.getUserData();
/*      */     }
/*      */ 
/* 2832 */     switch (event.getType()) {
/*      */     case 1:
/* 2834 */       if (name == null) {
/* 2835 */         name = createMaskName();
/* 2836 */         mask.setUserData(name);
/*      */       }
/*      */ 
/* 2839 */       this.maskList.add(new SkinnedListSwatchItem(name, Colour.getAWTColor(mask.getColour()), mask));
/*      */ 
/* 2842 */       break;
/*      */     case 2:
/* 2845 */       this.maskList.remove(mask);
/* 2846 */       break;
/*      */     case 5:
/* 2849 */       this.maskList.setSelectedData(mask);
/*      */ 
/* 2851 */       if (mask != null) {
/* 2852 */         DuluxColour dc = this.paletteManager.getColour(mask.getColour());
/*      */         try {
/* 2854 */           setPalette(dc.palette, false);
/* 2855 */           setColour(dc, false);
/*      */         }
/*      */         catch (NullPointerException e) {
/*      */         }
/* 2859 */         this.requireMaskWidgets.setEnabled(true);
/* 2860 */         this.blurSlider.setValue(mask.getBlurLevel());
/*      */       } else {
/* 2862 */         this.requireMaskWidgets.setEnabled(false);
/*      */       }
/*      */ 
/* 2865 */       break;
/*      */     case 3:
/* 2867 */       SkinnedListSwatchItem item = (SkinnedListSwatchItem)this.maskList.getItem(mask);
/* 2868 */       if (item != null) {
/* 2869 */         item.color = Colour.getAWTColor(mask.getColour());
/* 2870 */         this.maskList.invalidate(); } break;
/*      */     case 4:
/* 2875 */       if (mask == this.paintManager.getSelectedMask())
/* 2876 */         this.blurSlider.setValue(mask.getBlurLevel()); break;
/*      */     case 6:
/*      */     }
/*      */   }
/*      */ 
/*      */   public void onScissorsEvent(PaintExplorerScissorsEvent event)
/*      */   {
/*      */   }
/*      */ 
/*      */   private void unloadImage()
/*      */   {
/* 2891 */     this.view.setImage(null);
/* 2892 */     this.filename = null;
/*      */ 
/* 2894 */     this.isReady = false;
/*      */ 
/* 2896 */     this.explorer.close();
/*      */ 
/* 2898 */     updateUI();
/*      */ 
/* 2900 */     updateTitle();
/*      */   }
/*      */ 
/*      */   private void cancelProgress()
/*      */   {
/* 2905 */     this.isBusy = false;
/* 2906 */     setCursor(null);
/* 2907 */     this.progressLayer.setVisible(false);
/*      */   }
/*      */ 
/*      */   public void onProgress(PaintExplorerProgressEvent event)
/*      */   {
/* 2915 */     this.isBusy = event.isBusy();
/*      */ 
/* 2917 */     if (event.isBusy()) {
/* 2918 */       setCursor(Cursor.getPredefinedCursor(3));
/* 2919 */       this.progressLayer.setVisible(true);
/* 2920 */       this.progress.setValue(event.getProgress() / event.getRange());
/* 2921 */       this.progressLabel.setLabel(event.getDescription());
/* 2922 */       this.view.setEnabled(false);
/*      */     } else {
/* 2924 */       if (this.eraserOn != true) {
/* 2925 */         setCursor(null);
/*      */       }
/* 2927 */       this.progressLayer.setVisible(false);
/* 2928 */       this.view.setEnabled(true);
/* 2929 */       if ((this.explorer.isReady()) && (!this.isReady))
/*      */       {
/* 2931 */         updateMaskList(null);
/* 2932 */         updateTitle();
/*      */ 
/* 2934 */         this.isReady = true;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2939 */     updateUI();
/*      */ 
/* 2941 */     if ((this.view.getImage() == null) && (this.explorer.getPaintedImage() != null))
/* 2942 */       this.view.setImage(this.explorer.getPaintedImage());
/*      */   }
/*      */ 
/*      */   public void onHistoryEvent(PaintExplorerHistoryEvent event)
/*      */   {
/* 2948 */     this.undoButton.setEnabled(this.explorer.canUndo());
/* 2949 */     this.redoButton.setEnabled(this.explorer.canRedo());
/*      */   }
/*      */ 
/*      */   public void adjustmentValueChanged(AdjustmentEvent e) {
/* 2953 */     assert ((e.getSource() == this.blurSlider) || (e.getSource() == this.eraserSlider));
/* 2954 */     if (e.getSource() == this.blurSlider) {
/* 2955 */       if (!e.getValueIsAdjusting()) {
/* 2956 */         Mask mask = this.paintManager.getSelectedMask();
/* 2957 */         this.explorer.setMaskBlur(mask, e.getValue());
/* 2958 */         this.view.updateView();
/* 2959 */         this.requireLayerSelectedWidgets.setEnabled(true);
/*      */       }
/*      */     }
/* 2962 */     else if (e.getSource() == this.eraserSlider) {
/* 2963 */       BufferedImage bi = new BufferedImage(32, 32, 2);
/* 2964 */       Graphics g = bi.getGraphics();
/* 2965 */       float zoomFactor = this.view.getZoom();
/* 2966 */       int eraserSize = (int)(zoomFactor * this.input.getEraserRadius());
/* 2967 */       if (eraserSize > this.maxEraserSize) {
/* 2968 */         eraserSize = this.maxEraserSize;
/* 2969 */         this.input.setEraserRadius((int)(eraserSize / zoomFactor));
/* 2970 */         this.eraserSlider.setValue((int)(eraserSize / zoomFactor));
/*      */       } else {
/* 2972 */         this.input.setEraserRadius(e.getValue());
/*      */       }
/*      */ 
/* 2975 */       paintCircle(g, (int)(zoomFactor * this.input.getEraserRadius()));
/*      */ 
/* 2977 */       Point hotspot = new Point(15, 15);
/* 2978 */       setCursor(getToolkit().createCustomCursor(bi, hotspot, "Eraser Cursor"));
/* 2979 */       this.eraserButton.setState(true);
/* 2980 */       this.eraserButton.setSelected(true);
/* 2981 */       this.paintButton.setState(false);
/* 2982 */       this.paintButton.setSelected(false);
/* 2983 */       this.maskButton.setSelected(false);
/* 2984 */       this.maskButton.setState(false);
/* 2985 */       this.input.setMode(3);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void exceptionOccured(Throwable t)
/*      */   {
/* 2991 */     if (this.explorer == null)
/*      */     {
/* 2993 */       t.printStackTrace();
/* 2994 */       System.exit(1);
/*      */     }
/*      */ 
/* 2997 */     this.explorer.log("DuluxMyColour.exceptionOccurred(" + t + ")");
/* 2998 */     this.explorer.log(t);
/*      */ 
/* 3001 */     cancelProgress();
/*      */ 
/* 3003 */     this.errorLayer.setVisible(true);
/*      */     String message;
/* 3006 */     if (t.getMessage() != null)
/* 3007 */       message = t.getMessage().toUpperCase();
/*      */     else {
/* 3009 */       message = "AN UNEXPECTED ERROR HAS OCCURRED.  YOU MAY NOT BE ABLE TO CONTINUE WORKING ON THIS IMAGE.";
/*      */     }
/*      */ 
/* 3014 */     if ((t instanceof OutOfMemoryError)) {
/* 3015 */       this.inputLayer.scissorsCancel();
/* 3016 */       this.inputLayer.setScissorsStraightLine(false);
/* 3017 */       unloadImage();
/*      */ 
/* 3019 */       message = "MYCOLOUR HAS RUN OUT OF MEMORY, AND IS UNABLE TO CONTINUE WORKING ON THIS IMAGE.  YOU SHOULD TRY RESTARTING MYCOLOUR, OR RESIZING THE IMAGE TO A SMALLER SIZE.";
/*      */     }
/*      */ 
/* 3025 */     this.errorLabel.setLabel(message);
/* 3026 */     t.printStackTrace();
/*      */   }
/*      */ 
/*      */   public void exceptionOccured(Skin skin, Throwable t)
/*      */   {
/* 3031 */     exceptionOccured(t);
/*      */   }
/*      */ 
/*      */   public void update(Graphics g)
/*      */   {
/* 3036 */     paint(g);
/*      */   }
/*      */ 
/*      */   public void paint(Graphics g)
/*      */   {
/* 3042 */     Rectangle rect = getSkinRect();
/* 3043 */     g.setColor(Color.black);
/* 3044 */     g.fillRect(0, 0, getWidth(), rect.y);
/* 3045 */     g.fillRect(0, rect.y + rect.height, getWidth(), rect.y);
/* 3046 */     g.fillRect(0, rect.y, rect.x, rect.height);
/* 3047 */     g.fillRect(rect.x + rect.width, rect.y, rect.x, rect.height);
/* 3048 */     ((Graphics2D)g).translate(rect.x, rect.y);
/* 3049 */     this.skin.paint(g);
/*      */   }
/*      */ 
/*      */   public void paintCircle(Graphics g, int eraserRadius) {
/* 3053 */     g.setColor(Color.black);
/* 3054 */     g.drawOval(16 - eraserRadius, 16 - eraserRadius, eraserRadius * 2, eraserRadius * 2);
/*      */   }
/*      */ 
/*      */   private void saveProject()
/*      */   {
/* 3059 */     Thread t = new Thread()
/*      */     {
/*      */       public void run() {
/*      */         try {
/* 3063 */           byte[] resultJPEG = DuluxMyColour.this.explorer.saveImageToStream();
/* 3064 */           byte[] resultDMC = DuluxMyColour.this.explorer.saveProjectToStream();
/* 3065 */           DuluxMyColour.logger.log(DuluxMyColour.this.explorer.getByteSize());
/* 3066 */           DuluxMyColour.this.saveSuccess = DuluxMyColour.this.saveAndLoad.saveProject(DuluxMyColour.this.SESSIONID, DuluxMyColour.this.myColours[0].getId(), DuluxMyColour.this.myColours[1].getId(), DuluxMyColour.this.myColours[2].getId(), resultJPEG, DuluxMyColour.this.initialImage, resultDMC);
/*      */ 
/* 3073 */           if (!DuluxMyColour.this.saveSuccess) {
/* 3074 */             DuluxMyColour.this.errorLabel.setLabel("There has been a proble with saving the project. Please try saving again");
/* 3075 */             DuluxMyColour.this.errorLayer.setVisible(true);
/* 3076 */             DuluxMyColour.this.isNewProject = false;
/*      */           }
/*      */         } catch (Throwable e) {
/* 3079 */           DuluxMyColour.this.exceptionOccured(e);
/* 3080 */           e.printStackTrace();
/*      */         }
/*      */       }
/*      */     };
/*      */     try {
/* 3085 */       t.start();
/* 3086 */       t.join();
/*      */     }
/*      */     catch (Exception e) {
/*      */     }
/*      */   }
/*      */ 
/*      */   private void chooseLightingPopup() {
/* 3093 */     this.chooseLightingLayer.setVisible(true);
/*      */   }
/*      */ 
/*      */   public void refreshModes()
/*      */   {
/* 3100 */     if (this.input.getMode() == 3) {
/* 3101 */       this.eraserButton.setSelected(true);
/* 3102 */       this.paintButton.setSelected(false);
/* 3103 */       this.maskButton.setSelected(false);
/* 3104 */     } else if (this.input.getMode() == 0) {
/* 3105 */       this.eraserButton.setSelected(false);
/* 3106 */       this.paintButton.setSelected(true);
/* 3107 */       this.maskButton.setSelected(false);
/* 3108 */       this.maskButton.setState(false);
/* 3109 */     } else if (this.input.getMode() == 1) {
/* 3110 */       this.eraserButton.setSelected(false);
/* 3111 */       this.paintButton.setSelected(false);
/* 3112 */       this.maskButton.setSelected(true);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void editLayer() {
/* 3117 */     String maskName = (String)this.selectedMask.getUserData();
/* 3118 */     int itemId = this.selectedMask.getItemId();
/* 3119 */     this.editinglabel.setLabel(maskName);
/* 3120 */     this.editMaskLayer.setVisible(true);
/* 3121 */     this.changeSelectedMaskButton1.setColour(this.myColours[0]);
/* 3122 */     this.changeSelectedMaskButton2.setColour(this.myColours[1]);
/* 3123 */     this.changeSelectedMaskButton3.setColour(this.myColours[2]);
/* 3124 */     this.editinglabel.startEditing();
/*      */   }
/*      */ 
/*      */   public int BGRToRGB(int r, int g, int b) {
/* 3128 */     int colour = (b & 0xFF) << 16 | (g & 0xFF) << 8 | r & 0xFF;
/*      */ 
/* 3130 */     return colour;
/*      */   }
/*      */ 
/*      */   public String getVersion() {
/* 3134 */     return "4.8.10 for AUS/NZ";
/*      */   }
/*      */ 
/*      */   private class MessageBox extends Dialog
/*      */   {
/*      */     public MessageBox(Frame owner, String message)
/*      */     {
/* 1111 */       super(owner, "Dulux MyColour", true);
/*      */ 
/* 1113 */       addWindowListener(new WindowAdapter()
/*      */       {
/*      */         public void windowClosing(WindowEvent e) {
/* 1116 */           DuluxMyColour.MessageBox.this.hide();
/*      */         }
/*      */       });
/* 1119 */       add(new Label(message));
/* 1120 */       Button ok = new Button("Ok");
/* 1121 */       ok.addActionListener(new ActionListener()
/*      */       {
/*      */         public void actionPerformed(ActionEvent e) {
/* 1124 */           DuluxMyColour.MessageBox.this.hide();
/*      */         }
/*      */       });
/* 1127 */       Panel panel = new Panel();
/* 1128 */       panel.setLayout(new FlowLayout());
/* 1129 */       panel.add(ok);
/* 1130 */       add(panel, "South");
/* 1131 */       pack();
/*      */ 
/* 1133 */       Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
/* 1134 */       setLocation((screen.width - getWidth()) / 2, (screen.height - getHeight()) / 2);
/*      */     }
/*      */   }
/*      */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     dulux.DuluxMyColour
 * JD-Core Version:    0.6.2
 */