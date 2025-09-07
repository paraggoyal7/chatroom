package BroadcastServer.clientLauncher;

import java.net.InetAddress;

public class WindowsClientLauncher implements ClientLauncher {

  @Override
  public Boolean launchClient(String username, InetAddress serverAddress, int serverPort) {
    String javaHome = System.getProperty("java.home"); // Java installation folder
    String javaBin = javaHome + "/bin/java";           // Java executable
    String classpath = System.getProperty("java.class.path"); // Current classpath
    String className = "BroadcastServer.Client";                  // Your main class
    ProcessBuilder builder = new ProcessBuilder(
        "cmd", "/c", "start", "java", "-cp", classpath, "BroadcastServer.Client", "username", "serverIP", "serverPort"
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
