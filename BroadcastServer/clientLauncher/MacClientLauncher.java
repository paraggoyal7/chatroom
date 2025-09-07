package BroadcastServer.clientLauncher;

import java.net.InetAddress;

public class MacClientLauncher implements ClientLauncher {

    @Override
    public Boolean launchClient(String username, InetAddress serverAddress, int serverPort) {
      String javaHome = System.getProperty("java.home"); // Java installation folder
      String javaBin = javaHome + "/bin/java";           // Java executable
      String classpath = System.getProperty("java.class.path"); // Current classpath
      String className = "BroadcastServer.Client";                  // Your main class
      String cmd = "osascript -e 'tell application \"Terminal\" to do script \"" +
          javaBin + " -cp " + classpath + " BroadcastServer.Client " + username +" " + serverAddress.getHostAddress() +" " + serverPort + "; exit\"'";
      ProcessBuilder builder = new ProcessBuilder("bash", "-c", cmd);
      //builder.start();

      //ProcessBuilder builder = new ProcessBuilder(javaBin, "-cp", classpath, className, username, serverAddress.getHostAddress(), String.valueOf(serverPort));
      try {
        Process process = builder.start();

// Optional: redirect output to console
//        new Thread(() -> {
//          try (var reader = new java.io.BufferedReader(new java.io.InputStreamReader(process.getInputStream()))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//              System.out.println("[Client stdout] " + line);
//            }
//          } catch (Exception ignored) {}
//        }).start();
//
//        new Thread(() -> {
//          try (var reader = new java.io.BufferedReader(new java.io.InputStreamReader(process.getErrorStream()))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//              System.err.println("[Client stderr] " + line);
//            }
//          } catch (Exception ignored) {}
//        }).start();
      }
      catch (Exception e) {
        e.printStackTrace();
        return false;
      }
      return true;
    }
}
