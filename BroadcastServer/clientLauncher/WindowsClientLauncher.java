package BroadcastServer.clientLauncher;

import java.net.InetAddress;

public class WindowsClientLauncher implements ClientLauncher {

  @Override
  public Boolean launchClient(String username, InetAddress serverAddress, int serverPort) {
    String javaHome = System.getProperty("java.home"); // Java installation folder
    String javaBin = javaHome + "\\bin\\java.exe";           // Java executable
    String classpath = System.getProperty("java.class.path"); // Current classpath
    String className = "BroadcastServer.Client";                  // Your main class
    ProcessBuilder builder = new ProcessBuilder(
        "cmd", "/c", "start", "", javaBin, "-cp", classpath, className, username, serverAddress.getHostAddress(), String.valueOf(serverPort)
    );
    try {
      Process process = builder.start();
    }
    catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }
}
