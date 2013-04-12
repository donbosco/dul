/*    */ package dulux;
/*    */ 
/*    */ import java.io.BufferedWriter;
/*    */ import java.io.FileWriter;
/*    */ import java.util.Date;
/*    */ 
/*    */ public class DuluxLogger
/*    */ {
/*    */   FileWriter fstream;
/*    */   BufferedWriter out;
/*    */   DuluxMyColour dc;
/*    */ 
/*    */   public DuluxLogger(DuluxMyColour dc)
/*    */   {
/* 21 */     this.dc = dc;
/* 22 */     Date date = new Date();
/*    */     try {
/* 24 */       if (getOS().equals("Windows")) {
/* 25 */         this.fstream = new FileWriter(System.getProperty("user.home") + "\\MyColour4.log", true);
/* 26 */         this.out = new BufferedWriter(this.fstream);
/*    */       }
/* 28 */       else if ((getOS().equals("Mac")) || (getOS().equals("Linux/Unix"))) {
/* 29 */         this.fstream = new FileWriter(System.getProperty("user.home") + "/MyColour4.log", true);
/* 30 */         this.out = new BufferedWriter(this.fstream);
/*    */       }
/* 32 */       this.out.write("\n\n\n======================================================================================\n");
/* 33 */       this.out.write("Date : " + date.toString() + "\n");
/* 34 */       this.out.write("Dulux MyColour4 Version : " + dc.getVersion() + "\n");
/* 35 */       this.out.write("Operating System : " + System.getProperty("os.name") + " " + System.getProperty("os.version") + "\n");
/* 36 */       this.out.write("Java Architecture : " + System.getProperty("os.arch") + "\n");
/* 37 */       this.out.write("Java version : vm = " + System.getProperty("java.vm.version") + " runtime = " + System.getProperty("java.runtime.version") + "\n");
/* 38 */       this.out.flush();
/*    */     }
/*    */     catch (Exception e)
/*    */     {
/* 42 */       e.printStackTrace();
/*    */     }
/*    */   }
/*    */ 
/*    */   public void log(String s) {
/*    */     try {
/* 48 */       this.out = new BufferedWriter(this.fstream);
/* 49 */       this.out.write(s + "\n");
/* 50 */       this.out.flush();
/*    */     } catch (Exception e) {
/* 52 */       e.printStackTrace();
/*    */     }
/*    */   }
/*    */ 
/*    */   public void log(Object o) {
/*    */     try {
/* 58 */       this.out = new BufferedWriter(this.fstream);
/* 59 */       this.out.write(o.toString() + "\n");
/* 60 */       this.out.flush();
/*    */     } catch (Exception e) {
/* 62 */       e.printStackTrace();
/*    */     }
/*    */   }
/*    */ 
/*    */   public void close() {
/*    */     try {
/* 68 */       this.out.close();
/*    */     } catch (Exception e) {
/* 70 */       e.printStackTrace();
/*    */     }
/*    */   }
/*    */ 
/*    */   private String getOS() {
/* 75 */     String os = System.getProperty("os.name").toLowerCase();
/*    */ 
/* 77 */     if (os.contains("win"))
/* 78 */       return "Windows";
/* 79 */     if (os.contains("mac"))
/* 80 */       return "Mac";
/* 81 */     if ((os.contains("nix")) || (os.contains("nux"))) {
/* 82 */       return "Linux/Unix";
/*    */     }
/* 84 */     return null;
/*    */   }
/*    */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     dulux.DuluxLogger
 * JD-Core Version:    0.6.2
 */