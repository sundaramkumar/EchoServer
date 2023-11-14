/*    */ package echoserver;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import java.sql.Connection;
/*    */ import java.sql.DriverManager;
/*    */ import java.util.HashMap;
/*    */ import java.util.Iterator;
/*    */ import java.util.Set;
/*    */ import org.quickserver.util.xmlreader.DatabaseConnectionConfig;
/*    */ 
/*    */ public class DBPoolUtil
/*    */   implements org.quickserver.sql.DBPoolUtil
/*    */ {
/*    */   private HashMap dbPool;
/*    */   
/*    */   public void setDatabaseConnections(Iterator iterator)
/*    */     throws Exception
/*    */   {
/* 20 */     this.dbPool = new HashMap();
/* 21 */     while (iterator.hasNext())
/*    */     {
/* 22 */       DatabaseConnectionConfig dcc = (DatabaseConnectionConfig)iterator.next();
/* 23 */       this.dbPool.put(dcc.getId(), dcc);
/*    */     }
/*    */   }
/*    */   
/*    */   public boolean initPool()
/*    */   {
/* 28 */     if (this.dbPool == null) {
/* 29 */       throw new IllegalStateException("Call setDatabaseConnections first.!!");
/*    */     }
/* 30 */     Iterator iterator = this.dbPool.keySet().iterator();
/*    */     try
/*    */     {
/* 32 */       while (iterator.hasNext())
/*    */       {
/* 33 */         DatabaseConnectionConfig dcc = (DatabaseConnectionConfig)this.dbPool.get((String)iterator.next());
/* 34 */         Class.forName(dcc.getDriver());
/*    */       }
/* 36 */       return true;
/*    */     }
/*    */     catch (Exception e)
/*    */     {
/* 38 */       System.err.println("In DBPoolUtil.initPool : " + e);
/*    */     }
/* 39 */     return false;
/*    */   }
/*    */   
/*    */   public boolean clean()
/*    */   {
/* 44 */     this.dbPool = null;
/* 45 */     return true;
/*    */   }
/*    */   
/*    */   public Connection getConnection(String id)
/*    */     throws Exception
/*    */   {
/* 49 */     DatabaseConnectionConfig dcc = 
/* 50 */       (DatabaseConnectionConfig)this.dbPool.get(id);
/* 51 */     return DriverManager.getConnection(dcc.getUrl(), dcc.getUsername(), 
/* 52 */       dcc.getPassword());
/*    */   }
/*    */ }


/* Location:           F:\projects\EchoServer\avl\EchoServer\echoServer.jar
 * Qualified Name:     echoserver.DBPoolUtil
 * JD-Core Version:    0.7.0.1
 */