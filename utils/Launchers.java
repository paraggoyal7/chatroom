package utils;

import java.net.InetAddress;

import BroadcastServer.launcher.LauncherFactory;

public class Launchers {
  public static Boolean launchClient(String username, InetAddress serverAddress, int serverPort) {
    String[] args = {username, serverAddress.getHostAddress(), String.valueOf(serverPort)};
    try {
      LauncherFactory.getLauncher().buildProcess("BroadcastServer.Client", args);
    }
    catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  public static Boolean launchBroadcast() {
    try {
      LauncherFactory.getLauncher().buildProcess("BroadcastServer.Broadcaster", new String[0]);
    }
    catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  public static Boolean launchListener() {
    try {
      LauncherFactory.getLauncher().buildProcess("BroadcastServer.Listener", new String[0]);
    }
    catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }
}
