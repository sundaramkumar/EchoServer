/*    */ package echoserver;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import org.quickserver.net.AppException;
/*    */ import org.quickserver.net.server.QuickServer;
/*    */ 
/*    */ public class EchoServer
/*    */ {
/*  8 */   public static String version = "1.3";
/*    */   
/*    */   public static void main(String[] s)
/*    */   {
/* 28 */     String confFile = s[0];
/*    */     try
/*    */     {
/* 31 */       echoServer = QuickServer.load(confFile);
/*    */     }
/*    */     catch (AppException e)
/*    */     {
/*    */       QuickServer echoServer;
/* 33 */       System.out.println("Error in server : " + e);
/* 34 */       e.printStackTrace();
/*    */     }
/*    */   }
/*    */ }


/* Location:           F:\projects\EchoServer\avl\EchoServer\echoServer.jar
 * Qualified Name:     echoserver.EchoServer
 * JD-Core Version:    0.7.0.1
 */