/*    */ package echoserver;
/*    */ 
/*    */ import org.apache.commons.pool.PoolableObjectFactory;
/*    */ 
/*    */ public class EchoServerPoolableData
/*    */   extends EchoServerData
/*    */   implements PoolableObjectFactory
/*    */ {
/*    */   public void activateObject(Object obj) {}
/*    */   
/*    */   public void destroyObject(Object obj)
/*    */   {
/* 14 */     if (obj == null) {
/* 14 */       return;
/*    */     }
/* 15 */     passivateObject(obj);
/* 16 */     obj = null;
/*    */   }
/*    */   
/*    */   public Object makeObject()
/*    */   {
/* 19 */     return new EchoServerPoolableData();
/*    */   }
/*    */   
/*    */   public void passivateObject(Object obj)
/*    */   {
/* 22 */     EchoServerPoolableData pd = (EchoServerPoolableData)obj;
/* 23 */     pd.setHelloCount(0);
/* 24 */     pd.setUsername(null);
/*    */   }
/*    */   
/*    */   public boolean validateObject(Object obj)
/*    */   {
/* 27 */     if (obj == null) {
/* 28 */       return false;
/*    */     }
/* 30 */     return true;
/*    */   }
/*    */ }


/* Location:           F:\projects\EchoServer\avl\EchoServer\echoServer.jar
 * Qualified Name:     echoserver.EchoServerPoolableData
 * JD-Core Version:    0.7.0.1
 */