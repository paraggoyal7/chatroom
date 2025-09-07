package BroadcastServer.launcher;

public class LauncherFactory {

  public static Launcher getLauncher() {
    String osName = System.getProperty("os.name").toLowerCase();

    if (osName.contains("win")) {
      return new WindowsClientLauncher();
    } else if (osName.contains("mac")) {
      return new MacClientLauncher();
    } else {
      throw new IllegalArgumentException("Unsupported OS: " + osName);
    }
  }
}
