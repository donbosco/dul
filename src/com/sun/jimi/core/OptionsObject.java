/*   */ package com.sun.jimi.core;
/*   */ 
/*   */ import com.sun.jimi.util.PropertyOwner;
/*   */ 
/*   */ public abstract interface OptionsObject extends PropertyOwner
/*   */ {
/* 9 */   public static final OptionsObject DO_NOTHING_IMPL = new DoNothingOptionsObject();
/*   */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     com.sun.jimi.core.OptionsObject
 * JD-Core Version:    0.6.2
 */