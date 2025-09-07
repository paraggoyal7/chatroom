package BroadcastServer.launcher;

import java.io.IOException;
import java.net.InetAddress;

public class MacClientLauncher implements Launcher {

  @Override
  public void buildProcess(String className, String[] args) {
    String javaHome = System.getProperty("java.home"); // Java installation folder
    String javaBin = javaHome + "/bin/java";           // Java executable
    String classpath = System.getProperty("java.class.path");

    String cmd = "osascript -e 'tell application \"Terminal\" to do script \"" +
        javaBin + " -cp " + classpath + " " + className + " " +
        String.join(" ", args) + "; exit\"'";

    ProcessBuilder builder = new ProcessBuilder("bash", "-c", cmd);

    try {
      Process process = builder.start();
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }


}
