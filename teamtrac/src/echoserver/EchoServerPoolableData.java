//---- EchoServerPoolableData.java ----
package echoserver; 

import org.quickserver.net.server.*; 
import java.io.*; 

@SuppressWarnings("serial")
public class EchoServerPoolableData  extends EchoServerData implements org.apache.commons.pool.PoolableObjectFactory  {

	public void activateObject(Object obj) { 
		
	} 
	public void destroyObject(Object obj) { 		
		if(obj==null) return; 
		passivateObject(obj); 
		obj = null; 
	}   
	public Object makeObject() { 
		return new EchoServerPoolableData(); 
	} 
	public void passivateObject(Object obj) { 
		EchoServerPoolableData pd = (EchoServerPoolableData)obj; 
		pd.setHelloCount(0); 
		pd.setUsername(null); 
	} 
	public boolean validateObject(Object obj) {
		if(obj==null)  
			return false; 
		else
			return true; 
	} 
} 
//--- end of code ---