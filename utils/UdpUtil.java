package utils;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class UdpUtil {
  public static void printMessage(String message) {
    String[] parts = message.split("###");
    String username = parts[0];
    String messageText = parts[1];
    System.out.println("\n" + username + ": " + messageText);
  }

  public static void connectToServer(DatagramSocket socket, String username, InetAddress serverAddress, int serverPort) throws Exception {
    String msg = username + "###connect";
    byte[] data = msg.getBytes();
    DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress, serverPort);
    socket.send(packet);
    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      try {
        String disconnectMsg = username + "###disconnect";
        byte[] dataTemp = disconnectMsg.getBytes();
        DatagramPacket packetTemp = new DatagramPacket(dataTemp, dataTemp.length, serverAddress, serverPort);
        socket.send(packetTemp);
        socket.close();
        System.out.println("Graceful shutdown message sent.");
      } catch (Exception e) {
        // ignore if socket already closed
      }
    }));
  }

  public static void disconnectFromServer(DatagramSocket socket, String username, InetAddress serverAddress, int serverPort) throws Exception {
    String disconnectMsg = username + "###disconnect";
    byte[] data = disconnectMsg.getBytes();
    DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress, serverPort);
    socket.send(packet);
    socket.close();
    System.out.println("You have disconnected gracefully.");
  }

  public static void sendMessage(DatagramSocket socket, String username, InetAddress serverAddress, int serverPort, String message) throws Exception {
    message = username +"###" + message;
    byte[] data = message.getBytes();
    DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress, serverPort);
    socket.send(packet);
  }

  public static void sendThread(DatagramSocket socket, String username, InetAddress serverAddress, int serverPort, Scanner scanner) throws Exception {
    String msg = scanner.nextLine();
    UdpUtil.sendMessage(socket, username, serverAddress, serverPort, msg);
  }
}
