package BroadcastServer;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

import ManualServer.Client;

public class Listener {

  static String username;
  static InetAddress serverAddress;
  static int serverPort;


  public static void main(String[] args) throws Exception {
    DatagramSocket socket = new DatagramSocket(8888);
    byte[] buffer = new byte[1024];
    System.out.println("Listening for chat rooms...");
    Scanner scanner = new Scanner(System.in);
    while (true) {
      DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
      socket.receive(packet);
      String msg = new String(packet.getData(), 0, packet.getLength());
      System.out.println("Found: " + msg + " from " + packet.getAddress() + ":" + packet.getPort());
      System.out.println("Enter Username: ");
      username = scanner.nextLine();
      serverAddress = packet.getAddress();
      serverPort = packet.getPort();
      break;
    }
    socket.close();

    Client client = new Client(username, serverAddress, serverPort);
    client.startClient(scanner);

  }
}
