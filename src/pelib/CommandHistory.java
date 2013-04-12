/*     */ package pelib;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.Stack;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class CommandHistory
/*     */ {
/*     */   private Vector history;
/*     */   private int index;
/*     */   private int max;
/*     */ 
/*     */   public CommandHistory(int max)
/*     */   {
/*  20 */     this.history = new Vector();
/*  21 */     this.index = 0;
/*  22 */     if (max == -1)
/*  23 */       this.max = 2147483647;
/*     */     else
/*  25 */       this.max = max;
/*     */   }
/*     */ 
/*     */   public void printHistory()
/*     */   {int i;
/*  31 */     for ( i = 0; i < this.history.size(); i++)
/*     */     {
/*  33 */       if (this.index == i)
/*  34 */         System.err.println("-----------");
/*  35 */       System.err.println(i + ": " + this.history.get(i));
/*     */     }
/*  37 */     if (this.index == i)
/*  38 */       System.err.println("----------");
/*  39 */     System.err.println("index of commandhistory " + this.index);
/*     */   }
/*     */ 
/*     */   public void add(Command command)
/*     */   {
/*  44 */     if (this.history.size() > 1)
/*     */     {
/*  46 */       for (int i = this.history.size() - 1; i >= this.index; i--) {
/*  47 */         this.history.remove(i);
/*     */       }
/*     */     }
/*  50 */     this.history.add(command);
/*  51 */     this.index += 1;
/*     */   }
/*     */ 
/*     */   public void remove(Command command)
/*     */   {int i;
/*  58 */     for ( i = this.history.size() - 1; i >= 0; i--)
/*     */     {
/*  60 */       if (this.history.remove(i) == command) {
/*     */         break;
/*     */       }
/*     */     }
/*  64 */     if (this.index > i)
/*  65 */       this.index = i;
/*     */   }
/*     */ 
/*     */   public void cap()
/*     */   {
/*  73 */     if (this.history.size() > this.max)
/*     */     {
/*  75 */       int rm = this.history.size() - this.max;
/*  76 */       for (int i = 0; i < rm; i++)
/*  77 */         this.history.remove(0);
/*  78 */       this.index -= rm;
/*     */     }
/*     */   }
/*     */ 
/*     */   public Stack removeAndReturn(Command command)
/*     */   {
/*  90 */     Stack cmds = new Stack();
/*     */ int i;
/*  92 */     for ( i = this.history.size() - 1; i >= 0; i--)
/*     */     {
/*  94 */       Command cmd = (Command)this.history.remove(i);
/*  95 */       if (cmd == command) {
/*     */         break;
/*     */       }
/*  98 */       cmds.push(cmd);
/*     */     }
/*     */ 
/* 101 */     if (this.index > i) {
/* 102 */       this.index = i;
/*     */     }
/* 104 */     return cmds;
/*     */   }
/*     */ 
/*     */   public boolean canUndo()
/*     */   {
/* 110 */     return this.index > 0;
/*     */   }
/*     */ 
/*     */   public boolean canRedo()
/*     */   {
/* 115 */     return this.index < this.history.size();
/*     */   }
/*     */ 
/*     */   public void undo(Area dirty)
/*     */   {
/* 120 */     if (!canUndo()) {
/* 121 */       return;
/*     */     }
/* 123 */     this.index -= 1;
/*     */ 
/* 125 */     Command cmd = (Command)this.history.get(this.index);
/* 126 */     if (cmd != null)
/* 127 */       cmd.undo(dirty);
/*     */   }
/*     */ 
/*     */   public void redo(Area dirty)
/*     */   {
/* 134 */     if (!canRedo()) {
/* 135 */       return;
/*     */     }
/* 137 */     Command cmd = (Command)this.history.get(this.index);
/* 138 */     cmd.execute(dirty);
/* 139 */     this.index += 1;
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 144 */     this.index = 0;
/* 145 */     this.history.clear();
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.CommandHistory
 * JD-Core Version:    0.6.2
 */