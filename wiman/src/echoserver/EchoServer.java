package echoserver; 

import org.quickserver.net.*; 
import org.quickserver.net.server.*; 


public class EchoServer { 
	public static String version = "1.3";
	public static void main(String s[])  { 
		//System.out.println("Error in server : "+s[0]); 
	    
		/*String cmd  = "echoserver.EchoCommandHandler"; 
		String data = "echoserver.EchoServerPoolableData"; //Poolable
		
		QuickServer myServer = new QuickServer(cmd);     
		myServer.setClientData(data); 
		
		myServer.setPort(10085); 
		myServer.setName("Echo Server v 1.0"); 
		   
		try  { 
			myServer.startQSAdminServer(); 
			myServer.startServer(); 
		} catch(AppException e){ 
			System.out.println("Error in server : "+e); 
		}  */
		QuickServer echoServer;		
		String confFile = s[0];

		try	{
			echoServer = QuickServer.load(confFile);
		} catch(AppException e) {
			System.out.println("Error in server : "+e);
			e.printStackTrace();
		}
	} 
}