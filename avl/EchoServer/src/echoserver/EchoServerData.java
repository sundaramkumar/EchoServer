/*    */ package echoserver;
/*    */ 
/*    */ import org.quickserver.net.server.ClientData;
/*    */ 
/*    */ public class EchoServerData
/*    */   implements ClientData
/*    */ {
/*    */   private int helloCount;
/*    */   private String username;
/*    */   
/*    */   public void setHelloCount(int count)
/*    */   {
/* 12 */     this.helloCount = count;
/*    */   }
/*    */   
/*    */   public int getHelloCount()
/*    */   {
/* 15 */     return this.helloCount;
/*    */   }
/*    */   
/*    */   public void setUsername(String username)
/*    */   {
/* 19 */     this.username = username;
/*    */   }
/*    */   
/*    */   public String getUsername()
/*    */   {
/* 21 */     return this.username;
/*    */   }
/*    */ }


/* Location:           F:\projects\EchoServer\avl\EchoServer\echoServer.jar
 * Qualified Name:     echoserver.EchoServerData
 * JD-Core Version:    0.7.0.1
 */