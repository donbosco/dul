/*     */ package com.sun.jimi.core.options;
/*     */ 
/*     */ public class GIFOptions extends BasicFormatOptionSet
/*     */ {
/*     */   public static final int NO_TRANSPARENCY = -1;
/*     */   public static final int LOOP_FOREVER = 0;
/*     */   protected IntOption transparentIndex;
/*     */   protected BooleanOption interlace;
/*     */   protected IntOption frameDelay;
/*     */   protected BooleanOption localPalette;
/*     */   protected IntOption numberOfLoops;
/*     */ 
/*     */   public GIFOptions()
/*     */   {
/*  35 */     this.transparentIndex = new IntOption("Transparent index", "The palette index of the image which should be treated as transparent, or -1 for no transparency.", 
/*  37 */       -1);
/*     */ 
/*  39 */     this.interlace = new BooleanOption("Interlace", "Whether the image is interlaced.", false);
/*     */ 
/*  41 */     this.frameDelay = new IntOption("Frame delay", 
/*  42 */       "For Animated GIFs, the number of hundredths of seconds to display a frame for.", 
/*  43 */       100, 0, 65535);
/*     */ 
/*  45 */     this.localPalette = new BooleanOption("Local palette", 
/*  46 */       "For Animated GIFs, whether each frame has its own palette.", 
/*  47 */       true);
/*     */ 
/*  49 */     this.numberOfLoops = new IntOption("Number of loops", 
/*  50 */       "For Animated GIFs, how many times to loop through the animation, or -1 to loop forever.", 
/*  51 */       0);
/*     */ 
/*  53 */     initWithOptions(new FormatOption[] { 
/*  54 */       this.transparentIndex, this.interlace, this.frameDelay, this.localPalette, this.numberOfLoops });
/*     */   }
/*     */ 
/*     */   public int getFrameDelay()
/*     */   {
/* 112 */     return this.frameDelay.getIntValue();
/*     */   }
/*     */ 
/*     */   public int getNumberOfLoops()
/*     */   {
/* 149 */     return this.numberOfLoops.getIntValue();
/*     */   }
/*     */ 
/*     */   public int getTransparentIndex()
/*     */   {
/*  74 */     return this.transparentIndex.getIntValue();
/*     */   }
/*     */ 
/*     */   public boolean isInterlaced()
/*     */   {
/*  93 */     return this.interlace.getBooleanValue();
/*     */   }
/*     */ 
/*     */   public boolean isUsingLocalPalettes()
/*     */   {
/* 130 */     return this.localPalette.getBooleanValue();
/*     */   }
/*     */ 
/*     */   public void setFrameDelay(int paramInt)
/*     */     throws OptionException
/*     */   {
/* 103 */     this.frameDelay.setIntValue(paramInt);
/*     */   }
/*     */ 
/*     */   public void setInterlaced(boolean paramBoolean)
/*     */     throws OptionException
/*     */   {
/*  84 */     this.interlace.setBooleanValue(paramBoolean);
/*     */   }
/*     */ 
/*     */   public void setNumberOfLoops(int paramInt)
/*     */     throws OptionException
/*     */   {
/* 140 */     this.numberOfLoops.setIntValue(paramInt);
/*     */   }
/*     */ 
/*     */   public void setTransparentIndex(int paramInt)
/*     */     throws OptionException
/*     */   {
/*  66 */     this.transparentIndex.setIntValue(paramInt);
/*     */   }
/*     */ 
/*     */   public void setUseLocalPalettes(boolean paramBoolean)
/*     */   {
/* 121 */     this.localPalette.setBooleanValue(paramBoolean);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 154 */     return "[transparentIndex=" + this.transparentIndex.getIntValue() + 
/* 155 */       ", interlace=" + this.interlace.getBooleanValue() + 
/* 156 */       ", frameDelay=" + this.frameDelay.getIntValue() + 
/* 157 */       ", localPalette=" + this.localPalette.getBooleanValue() + 
/* 158 */       ", numberOfLoops=" + this.numberOfLoops.getIntValue() + "]";
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.options.GIFOptions
 * JD-Core Version:    0.6.2
 */