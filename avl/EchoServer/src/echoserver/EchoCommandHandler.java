/*     */ package echoserver;
/*     */ 
/*     */ import com.mysql.jdbc.Connection;
/*     */ import com.mysql.jdbc.Statement;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketTimeoutException;
/*     */ import java.text.ParseException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import java.util.logging.Level;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.quickserver.net.server.ClientCommandHandler;
/*     */ import org.quickserver.net.server.ClientEventHandler;
/*     */ import org.quickserver.net.server.ClientHandler;
/*     */ import org.quickserver.net.server.QuickServer;
/*     */ import org.quickserver.sql.DBPoolUtil;
/*     */ 
/*     */ public class EchoCommandHandler
/*     */   implements ClientCommandHandler, ClientEventHandler
/*     */ {
/*     */   public void gotConnected(ClientHandler handler)
/*     */     throws SocketTimeoutException, IOException
/*     */   {
/*  33 */     System.out.println("Connection opened : " + handler.getHostAddress());
/*     */   }
/*     */   
/*     */   public void lostConnection(ClientHandler handler)
/*     */     throws IOException
/*     */   {
/*  43 */     handler.sendSystemMsg("Connection lost : " + handler.getSocket().getInetAddress(), Level.FINE);
/*     */   }
/*     */   
/*     */   public void closingConnection(ClientHandler handler)
/*     */     throws IOException
/*     */   {
/*  47 */     System.out.println("Connection closed : " + handler.getSocket().getInetAddress());
/*     */   }
/*     */   
/*     */   public void handleCommand(ClientHandler handler, String deviceResponse)
/*     */     throws SocketTimeoutException, IOException
/*     */   {
/*  53 */     System.out.println("Client Command : " + deviceResponse);
/*     */     
/*  55 */     System.out.println(handler.getHostAddress().toString() + " : " + deviceResponse);
/*     */     
/*     */ 
/*  58 */     Pattern p = Pattern.compile("\\$+\\$.*");
/*  59 */     Matcher matcher = p.matcher(deviceResponse);
/* 125 */     if (matcher.find())
/*     */     {
/* 126 */       String deviceStr = matcher.group(0);
/*     */       
/*     */ 
/*     */ 
/* 130 */       String Deviceid = "";
/* 131 */       String datetime = null;
/* 132 */       String latitude = "";
/* 133 */       String longitude = "";
/* 134 */       Double speed = Double.valueOf(0.0D);
/* 135 */       String fuel = "";
/* 136 */       String statusStr = "";
/* 137 */       String ignitionStatus = "";
/* 138 */       String ignitionStatusStr = "";
/* 139 */       String doorStatus = "";
/* 140 */       String doorStatusStr = "";
/*     */       
/* 142 */       String[] deviceInfoArr = deviceStr.split("\\|");
/* 143 */       Deviceid = deviceInfoArr[0].replace("$", "");
/* 144 */       Deviceid = Deviceid.substring(2);
/* 145 */       String[] latonArr = deviceInfoArr[1].split("\\,");
/* 146 */       if (latonArr.length > 5)
/*     */       {
/* 147 */         latitude = latonArr[3];
/* 148 */         longitude = latonArr[5];
/* 149 */         speed = Double.valueOf(latonArr[7]);
/* 151 */         if (speed.doubleValue() != 0.0D) {
/* 152 */           speed = Double.valueOf(speed.doubleValue() * 1.852D);
/*     */         }
/* 154 */         fuel = deviceInfoArr[8];
/*     */         
/*     */ 
/* 157 */         String datetimeStr = deviceInfoArr[6];
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 162 */         SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
/*     */         try
/*     */         {
/* 164 */           Date datetimeC = dateFormat.parse(datetimeStr);
/* 165 */           SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
/* 166 */           datetime = sdf.format(datetimeC);
/*     */         }
/*     */         catch (ParseException e)
/*     */         {
/* 170 */           e.printStackTrace();
/*     */         }
/* 173 */         statusStr = deviceInfoArr[5];
/* 174 */         ignitionStatus = statusStr.substring(4, 5);
/* 175 */         if (ignitionStatus.equals("1")) {
/* 176 */           ignitionStatusStr = "ON";
/*     */         } else {
/* 179 */           ignitionStatusStr = "OFF";
/*     */         }
/* 182 */         doorStatus = statusStr.substring(5, 6);
/* 183 */         if (doorStatus.equals("1")) {
/* 184 */           doorStatusStr = "OPEN";
/*     */         } else {
/* 187 */           doorStatusStr = "CLOSED";
/*     */         }
/* 190 */         System.out.println("Deviceid :" + Deviceid);
/* 191 */         System.out.println("Latitude :" + latitude);
/* 192 */         System.out.println("Longitude :" + longitude);
/* 193 */         System.out.println("Datetime :" + datetime);
/* 194 */         System.out.println("Speed :" + speed.toString());
/* 195 */         System.out.println("Fuel :" + fuel.toString());
/* 196 */         System.out.println("Ignition :" + ignitionStatus + " : " + ignitionStatusStr.toString());
/* 197 */         System.out.println("Door :" + doorStatus + " : " + doorStatusStr.toString());
/*     */         
/*     */ 
/* 200 */         String strLa = latitude;
/* 201 */         String strLo = longitude;
/*     */         
/* 203 */         String strLo1 = "";String strlo2 = "";String strLa1 = "";
/* 204 */         String SN = "N";String WE = "E";
/* 205 */         strLa1 = strLa.substring(0, 2);
/* 206 */         String strla2 = strLa.substring(2);
/*     */         
/* 208 */         double la = Integer.parseInt(strLa1) + Double.parseDouble(strla2) / 60.0D;
/*     */         
/* 210 */         strLo1 = strLo.substring(0, 3);
/* 211 */         strlo2 = strLo.substring(3);
/* 212 */         double lo = Integer.parseInt(strLo1) + Double.parseDouble(strlo2) / 60.0D;
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 218 */         Connection con = null;
/*     */         try
/*     */         {
/* 220 */           con = (Connection)handler.getServer().getDBPoolUtil().getConnection("jdbc");
/* 221 */           Statement st = (Statement)con.createStatement();
/* 222 */           int insQry = st.executeUpdate("INSERT INTO gpsdata(deviceIMEI, latitude, longitude, posdatetime,speed,fuel,ignition,door) VALUES('" + Deviceid + "','" + la + "','" + lo + "','" + datetime + "','" + speed + "','" + fuel + "','" + ignitionStatusStr + "','" + doorStatusStr + "')");
/* 223 */           int uptQry = st.executeUpdate("UPDATE gpsdata_live SET latitude='" + la + "', longitude='" + lo + "', posdatetime='" + datetime + "',speed='" + speed + "',fuel='" + fuel + "',ignition='" + ignitionStatusStr + "',door='" + doorStatusStr + "' WHERE deviceIMEI='" + Deviceid + "'");
/* 224 */           System.out.println("1 row Insertted");
/*     */         }
/*     */         catch (Exception e)
/*     */         {
/* 226 */           System.out.println(e);
/*     */           try
/*     */           {
/* 229 */             con.close();
/*     */           }
/*     */           catch (Exception e1)
/*     */           {
/* 231 */             handler.sendSystemMsg("IGNORING: " + e1);
/*     */           }
/*     */         }
/*     */         finally
/*     */         {
/*     */           try
/*     */           {
/* 229 */             con.close();
/*     */           }
/*     */           catch (Exception e)
/*     */           {
/* 231 */             handler.sendSystemMsg("IGNORING: " + e);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:           F:\projects\EchoServer\avl\EchoServer\echoServer.jar
 * Qualified Name:     echoserver.EchoCommandHandler
 * JD-Core Version:    0.7.0.1
 */