package echoserver;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.quickserver.net.server.ClientCommandHandler;
import org.quickserver.net.server.ClientEventHandler;
import org.quickserver.net.server.ClientHandler;
import org.quickserver.net.server.QuickServer;
import org.quickserver.sql.DBPoolUtil;

public class EchoCommandHandler
  implements ClientCommandHandler, ClientEventHandler
{
  public void gotConnected(ClientHandler handler)
    throws SocketTimeoutException, IOException
  {
    System.out.println("Connection opened : " + handler.getHostAddress());
  }
  
  public void lostConnection(ClientHandler handler)
    throws IOException
  {
    handler.sendSystemMsg("Connection lost : " + handler.getSocket().getInetAddress(), Level.FINE);
  }
  
  public void closingConnection(ClientHandler handler)
    throws IOException
  {
    System.out.println("Connection closed : " + handler.getSocket().getInetAddress());
  }
  
  public void handleCommand(ClientHandler handler, String deviceResponse)
    throws SocketTimeoutException, IOException
  {
    System.out.println("Client Command : " + deviceResponse);
    
    System.out.println(handler.getHostAddress().toString() + " : " + deviceResponse);
    
    /***
     * 
     * AVL Device
     * The GPRS command server sent to device must be 8-bit ASCII format. The GPRS command must be same as sms command in this user guide. 
     * The data of the device send to the server: 
     * Format:$$(2 Bytes) + Len(2 Bytes) + IMEI(15 Bytes) + | + AlarmType(2 Bytes) + GPRMC + | + PDOP + | + HDOP + | + VDOP + | + Status(12 Bytes) + | + RTC(14 Bytes) + | + Voltage(8 Bytes) + | + ADC(8 Bytes)  + | + LACCI(8 Bytes) + | + Temperature(4 Bytes) | + Mile-meter(14 Bytes)+ | Serial(4 Bytes) + | + Checksum (4 Byte) + ArAn(2 Bytes)
     * The format of ASCII:
     * $$B0353358019462410|AA$GPRMC,102156.000,A,2232.4690,N,11403.6847,E,0.00,,180909,,*15|02.0|01.2|01.6|000000001010|20090918102156|14181353|00000000|279311AA|0000|0.7614|0080|D2B
     * 
     * WiMan Device
     * $WTGPS,FP01,863071013850588,20150120152839,1,0,0.0000000,0.0000000,0.0,0.0,0,2007.89,0,12.04,0|0,19,131|c7d0|404|40,AA|0,0,0,0.00,0,0,1216,3B#
     * 
     * $WTGPS,FP01,863071013850588,20150330004604,0,1,12.9997083,80.2638383,14.0,0.0,0,3594.56,0,12.37,0|0,11,131|c7d0|404|40,AA|0,0,0,0.00,0,0,4395,0D
     */   
    Pattern p = Pattern.compile("\\$.*");
    Matcher matcher = p.matcher(deviceResponse);
    if (matcher.find())
    {
      System.out.println("Matched... ");	
      String deviceStr = matcher.group(0);
      
      String PacketType = "";
      String Deviceid = "";
      String datetimeStr = "";
      String datetime = null;
      Boolean isGpsValid = Boolean.valueOf(false); //false;
      String latitude = "";
      String longitude = "";
      String altitude = "";
//      Double speed = Double.valueOf(0.0D);
      float speed = 0.0F; //f;
      String fuel = "";
      String statusStr = "";
      String ignitionStatus = "";
      String ignitionStatusStr = "";
      String doorStatus = "";
      String doorStatusStr = "";
      
      String[] deviceInfoArr = deviceStr.split("\\,");
      System.out.println("deviceStr is "+deviceStr);
      PacketType = deviceInfoArr[0].replace("$", "");       //$WTLOGIN, $WTGPS
      Deviceid = deviceInfoArr[2];
      
      datetimeStr = deviceInfoArr[3];
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
      try
      {
        Date datetimeC = dateFormat.parse(datetimeStr);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        datetime = sdf.format(datetimeC);
      }
      catch (ParseException e)
      {
        e.printStackTrace();
      }      
      System.out.println("deviceInfoArr[5] is "+deviceInfoArr[5]);
      if( Integer.parseInt( deviceInfoArr[5],2 ) ==1 ){ // GPS Data Packet 1–Valid , 0–Invalid
    	  isGpsValid = Boolean.valueOf(true); //true;
    	  System.out.println("isGpsValid Valid...");
      }else{
    	  isGpsValid = Boolean.valueOf(false); //false;
    	  System.out.println("isGpsValid InValid...");
      }
      
      if (isGpsValid.booleanValue())
      {
    	  System.out.println("Valid GPS Data...");
    	  latitude = deviceInfoArr[6];	//Latitude in degrees
    	  longitude = deviceInfoArr[7];	//Longitude in degrees
    	  altitude = deviceInfoArr[8];	//Altitude in meters

//        speed = Double.valueOf(deviceInfoArr[9]);	//Speed in Km/s        
//        if (speed.doubleValue() != 0.0D) {
//          speed = Double.valueOf(speed.doubleValue() * 1.852D);
//        }
        
    	  speed = Float.valueOf(deviceInfoArr[9]).floatValue();	//Speed in Km/s
    	  if(speed !=0.0D){
    		  speed *= 3600.0F;		//convert km/s to km/h
    	  }
    	  
    	  ignitionStatus = deviceInfoArr[22]; //Ignition status 1-ON,0-OFF
    	  if (ignitionStatus.equals("1")) {
    		  ignitionStatusStr = "ON";
		  } else {
			  ignitionStatusStr = "OFF";
		  }
//        fuel = deviceInfoArr[8];

        doorStatus = deviceInfoArr[24]; //Digital input1 state: 1-ON,0-OFF
        if (doorStatus.equals("1")) {
          doorStatusStr = "OPEN";
        } else {
          doorStatusStr = "CLOSED";
        }
        System.out.println("Deviceid :" + Deviceid);
        System.out.println("Latitude :" + latitude);
        System.out.println("Longitude :" + longitude);
        System.out.println("Datetime :" + datetime);
        System.out.println("Speed :" + speed);
        System.out.println("Fuel :" + fuel.toString());
        System.out.println("Ignition :" + ignitionStatus + " : " + ignitionStatusStr.toString());
        System.out.println("Door :" + doorStatus + " : " + doorStatusStr.toString());
               
        String la = latitude; 
        String lo = longitude; 

        Connection con = null;
        try
        {
          con = (Connection)handler.getServer().getDBPoolUtil().getConnection("jdbc");
          Statement st = (Statement)con.createStatement();
          System.out.println("INSERT INTO gpsdata(deviceIMEI, latitude, longitude, posdatetime,speed,fuel,ignition,door) VALUES('" + Deviceid + "','" + la + "','" + lo + "','" + datetime + "','" + speed + "','" + fuel + "','" + ignitionStatusStr + "','" + doorStatusStr + "')");
          int insQry = st.executeUpdate("INSERT INTO gpsdata(deviceIMEI, latitude, longitude, posdatetime,speed,fuel,ignition,door) VALUES('" + Deviceid + "','" + la + "','" + lo + "','" + datetime + "','" + speed + "','" + fuel + "','" + ignitionStatusStr + "','" + doorStatusStr + "')");
          int uptQry = st.executeUpdate("UPDATE gpsdata_live SET latitude='" + la + "', longitude='" + lo + "', posdatetime='" + datetime + "',speed='" + speed + "',fuel='" + fuel + "',ignition='" + ignitionStatusStr + "',door='" + doorStatusStr + "' WHERE deviceIMEI='" + Deviceid + "'");
          System.out.println("1 row Inserted");
        }
        catch (Exception e)
        {
          System.out.println(e);
          try
          {
            con.close();
          }
          catch (Exception ex)
          {
            handler.sendSystemMsg("IGNORING: " + ex);
          }
        }
        finally
        {
          try
          {
            con.close();
          }
          catch (Exception e)
          {
            handler.sendSystemMsg("IGNORING: " + e);
          }
        }
      }
    }
  }
}
