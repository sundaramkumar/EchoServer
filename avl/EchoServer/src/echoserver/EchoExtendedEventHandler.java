/*    */ package echoserver;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.PrintStream;
/*    */ import java.net.Socket;
/*    */ import java.net.SocketException;
/*    */ import java.util.logging.Logger;
/*    */ import org.quickserver.net.server.ClientExtendedEventHandler;
/*    */ import org.quickserver.net.server.ClientHandler;
/*    */ 
/*    */ public class EchoExtendedEventHandler
/*    */   implements ClientExtendedEventHandler
/*    */ {
/* 11 */   private static Logger logger = Logger.getLogger(EchoExtendedEventHandler.class.getName());
/*    */   
/*    */   public void handleTimeout(ClientHandler handler)
/*    */     throws SocketException, IOException
/*    */   {
/* 16 */     System.out.println("ERR Timeout : " + handler.getSocket().getInetAddress());
/* 17 */     throw new SocketException();
/*    */   }
/*    */   
/*    */   public void handleMaxAuthTry(ClientHandler handler)
/*    */     throws IOException
/*    */   {
/* 22 */     System.out.println("ERR Max Auth Try Reached : " + handler.getSocket().getInetAddress());
/*    */   }
/*    */   
/*    */   public boolean handleMaxConnection(ClientHandler handler)
/*    */     throws IOException
/*    */   {
/* 29 */     System.out.println("Server Busy - Max Connection Reached : " + handler.getSocket().getInetAddress());
/* 30 */     return false;
/*    */   }
/*    */ }


/* Location:           F:\projects\EchoServer\avl\EchoServer\echoServer.jar
 * Qualified Name:     echoserver.EchoExtendedEventHandler
 * JD-Core Version:    0.7.0.1
 */