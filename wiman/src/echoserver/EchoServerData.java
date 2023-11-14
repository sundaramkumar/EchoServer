//---- EchoServerData.java ----
package echoserver;

import org.quickserver.net.server.*;
import java.io.*;

public class EchoServerData implements ClientData {
  private int helloCount;
  private String username;

  public void setHelloCount(int count) {
    helloCount = count;
  }
 public int getHelloCount() {
    return helloCount;
  }

  public void setUsername(String username) {
    this.username = username;
  }
public String getUsername() {return username;
  }
}
 //--- end of code --- 