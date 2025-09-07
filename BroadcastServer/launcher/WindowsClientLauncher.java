package BroadcastServer.launcher;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WindowsClientLauncher implements Launcher {

  @Override
  public void buildProcess(String className, String[] args){

    String javaHome = System.getProperty("java.home"); // Java installation folder
    String javaBin = javaHome + "\\bin\\java.exe";           // Java executable
    String classpath = System.getProperty("java.class.path"); // Current classpath

    List<String> command = new ArrayList<>();
    command.add("cmd");
    command.add("/c");
    command.add("start");
    command.add("");
    command.add(javaBin);
    command.add("-cp");
    command.add(classpath);
    command.add(className);

    // Add all the args
    command.addAll(Arrays.asList(args));

    ProcessBuilder builder = new ProcessBuilder(command);

    try {
      Process process = builder.start();
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }
}
