package echoserver;

import java.net.*;
import java.io.*;
import org.quickserver.net.server.ClientExtendedEventHandler;
import org.quickserver.net.server.ClientHandler;
import java.util.logging.*;

public class EchoExtendedEventHandler implements ClientExtendedEventHandler {
	private static Logger logger = 
			Logger.getLogger(EchoExtendedEventHandler.class.getName());

	public void handleTimeout(ClientHandler handler) 
			throws SocketException, IOException {
		//handler.sendClientMsg("-ERR Timeout");
		System.out.println("ERR Timeout : "+handler.getSocket().getInetAddress());
		if(true) throw new SocketException();
	}

	public void handleMaxAuthTry(ClientHandler handler) throws IOException {
		//handler.sendClientMsg("-ERR Max Auth Try Reached");
		System.out.println("ERR Max Auth Try Reached : "+handler.getSocket().getInetAddress());
	}

	public boolean handleMaxConnection(ClientHandler handler) throws IOException {
		//for now lets reject all excess clients
		if(true) {
			//handler.sendClientMsg("Server Busy - Max Connection Reached");
			System.out.println("Server Busy - Max Connection Reached : "+handler.getSocket().getInetAddress());
			return false;
		}
		return true;
	}
}
